/*
 * Copyright (c) 2022-2023 Devoxist, Dev-Bjorn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package nl.devoxist.typeresolver.constructor;

import nl.devoxist.typeresolver.TypeRegister;
import nl.devoxist.typeresolver.exception.ConstructorException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * <h2> Example usage: </h2>
 * <p>
 * Register a type:
 * <pre>{@code
 *      ModularUtils.getTypeRegister().register(Type, Provider)
 * }</pre>
 * <p>
 * The constructor looks like this:
 * <pre>{@code
 *      @ConstructorResolving(ConstructorPriority.Normal)
 *      public constructor(Types...) {
 *          ...
 *      }
 * }</pre>
 *
 * @author Dev-Bjorn
 * @version 1.2.0
 * @since 1.0.0
 */
public final class ConstructorResolver {

    /**
     * Construct a new {@link ConstructorResolver} object. This always fails, because the class is a static class. So it
     * only contains static objects. Thus, it throws an {@link IllegalAccessException}.
     *
     * @throws IllegalAccessException If the {@link ConstructorResolver} was try to construct the class. The
     *                                construction of this class is not possible, because this is a static class.
     * @since 1.0.0
     */
    @Contract(value = " -> fail",
              pure = true)
    @ApiStatus.Internal
    private ConstructorResolver() throws IllegalAccessException {
        throw new IllegalAccessException("This class is an static class, so this class cannot be initialized.");
    }

    /**
     * Used for initializing classes by the type resolver.
     *
     * @param tClass class to automatically initialize
     * @param <T>    type of the class.
     *
     * @return initialized class.
     *
     * @throws ConstructorException      if there is no valid constructor.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @since 1.0.0
     */
    public static <T> T initClass(@NotNull Class<T> tClass)
            throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        Optional<Constructor<?>> optionalConstructor = getClassConstructor(tClass);
        if (optionalConstructor.isEmpty()) {
            throw new ConstructorException(String.format("%s has no valid constructor.", tClass.getSimpleName()));
        }
        Constructor<?> constructor = optionalConstructor.get();
        Object[] initObjects = getResolvedObjects(constructor);
        constructor.setAccessible(true);
        T clazz = tClass.cast(constructor.newInstance(initObjects));
        constructor.setAccessible(false);
        return clazz;
    }

    /**
     * Get the constructor of a class. First it will check if there is a constructor or a public constructor has no
     * parameters. If non there will be looked for a constructor with resolvable types.
     *
     * @param tClass class to get the constructors from.
     *
     * @return {@link Optional} of a constructor. This will be empty if no valid constructor is found.
     *
     * @throws NoSuchMethodException if a matching method is not found.
     * @since 1.0.0
     */
    private static Optional<Constructor<?>> getClassConstructor(@NotNull Class<?> tClass) throws NoSuchMethodException {
        if ((tClass.getConstructors().length == 0 && tClass.getDeclaredConstructors().length == 0) ||
            hasConstructorWithNoParams(tClass.getConstructors())) {
            return Optional.of(tClass.getConstructor());
        }
        Optional<Constructor<?>> constructor = getConstructor(tClass.getConstructors());
        return constructor.isEmpty() ? getConstructor(tClass.getDeclaredConstructors()) : constructor;
    }

    /**
     * Get the resolved objects of a constructor.
     *
     * @param constructor constructor to resolve the types from
     *
     * @return resolved objects of the given constructor.
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private static Object @NotNull [] getResolvedObjects(@NotNull Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(TypeRegister::getInitProvider)
                .toArray();
    }

    /**
     * Get a constructor that has the {@link ConstructorResolving} annotation. This constructor can only have resolvable
     * types. Those will be provided by the {@link TypeRegister}.
     *
     * @param constructors constructors that will be checked to the conditions.
     *
     * @return constructor with the highest {@link ConstructorPriority} and that has resolvable types.
     *
     * @since 1.0.0
     */
    private static Optional<Constructor<?>> getConstructor(@NotNull Constructor<?> @NotNull [] constructors) {
        try {
            return Arrays.stream(constructors)
                    .parallel()
                    .filter(constructor -> constructor.getAnnotation(ConstructorResolving.class) != null &&
                                           hasResolvableTypes(constructor.getParameterTypes()))
                    .max(getConstructorComparator());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    /**
     * Check if all classes are a resolvable type.
     *
     * @param parameters classes that will be checked.
     *
     * @return If {@code true} all classes are a resolvable type.
     *
     * @since 1.0.0
     */
    private static boolean hasResolvableTypes(Class<?> @NotNull [] parameters) {
        return Arrays.stream(parameters).parallel().allMatch(TypeRegister::hasProvider);
    }

    /**
     * The {@link Comparator} of the {@link Constructor}. This will check on the {@link ConstructorPriority} of the
     * {@link ConstructorResolving}.
     *
     * @return {@link Comparator} of the {@link Constructor}
     *
     * @since 1.0.0
     */
    private static Comparator<Constructor<?>> getConstructorComparator() {
        return Comparator.comparing(constructor -> constructor.getAnnotation(ConstructorResolving.class)
                .value()
                .ordinal());
    }

    /**
     * Check if there is a public constructor with no parameters.
     *
     * @param constructors constructors to check
     *
     * @return If {@code true} there is a public constructor with no parameters.
     *
     * @since 1.0.0
     */
    private static boolean hasConstructorWithNoParams(@NotNull Constructor<?> @NotNull [] constructors) {
        return Arrays.stream(constructors)
                .parallel()
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

}
