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
import nl.devoxist.typeresolver.register.Register;
import nl.devoxist.typeresolver.settings.ConstructionSettings;
import nl.devoxist.typeresolver.settings.ConstructionSettingsBuilder;
import nl.devoxist.typeresolver.settings.InitProviderSettings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * {@link ConstructorResolver} is an object that is responsible for the auto construction of a {@link Class}.
 *
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
 * @param <T> type of the class that is going to be constructed.
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.0.0
 */
public final class ConstructorResolver<T> {
    /**
     * The represented class that is getting constructed.
     *
     * @since 1.5.0
     */
    private final Class<T> constructionCls;
    /**
     * The settings which manipulate the pick order of the constructor and parameter injection types.
     *
     * @see ConstructionSettings
     * @since 1.5.0
     */
    private final ConstructionSettings constructionSettings;
    /**
     * The register where the types of the parameters are gathered from.
     *
     * @since 1.5.0
     */
    private final Register searchableRegisters;

    /**
     * Construct the {@link ConstructorResolver} object with class that will be constructed and the settings of the
     * construction.
     *
     * @param constructionCls      The class that will be constructed.
     * @param constructionSettings The settings which manipulate the pick order of the constructor and parameter
     *                             injection types.
     *
     * @since 1.5.0
     */
    private ConstructorResolver(
            @NotNull Class<T> constructionCls, @NotNull ConstructionSettings constructionSettings
    ) {
        this.constructionCls = constructionCls;
        this.constructionSettings = constructionSettings;
        this.searchableRegisters = new Register(constructionSettings.getRegisters());
    }

    /**
     * Constructing the specified class by the type resolver. The {@link TypeRegister} is used to resolve the types of
     * the parameters of the constructor. At least one constructor needs to have the annotation
     * {@link ConstructorResolving}.
     *
     * @param constructionCls The class which need to be auto constructed.
     * @param <T>             type of the class which gets auto constructed.
     *
     * @return initialized class.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @apiNote This uses the {@link TypeRegister} to search from.
     * @since 1.0.0
     */
    public static <T> T initClass(@NotNull Class<T> constructionCls) throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        return ConstructorResolver.initClass(constructionCls, true);
    }

    /**
     * Constructing the specified class by the type resolver. The specified {@link Register}s are used to resolve the
     * types of the parameters of the constructor. At least one constructor needs to have the annotation
     * {@link ConstructorResolving}.
     *
     * @param constructionCls The class which need to be auto constructed.
     * @param registers       The registers that are used to retrieve the types from.
     * @param <T>             type of the class which gets auto constructed.
     *
     * @return The initialized class, which has been auto formed by the type resolver.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @since 1.3.0
     */
    public static <T> T initClass(@NotNull Class<T> constructionCls, Register... registers) throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        return ConstructorResolver.initClass(constructionCls, (settings) -> settings.setRegisters(registers));
    }

    /**
     * Constructing the specified class by the type resolver. The {@link TypeRegister} is used to resolve the types of
     * the parameters of the constructor. If the needAnnotation option is set to {@code false}, than no constructor
     * needs to have the annotation {@link ConstructorResolving}.
     *
     * @param constructionCls The class which need to be auto constructed.
     * @param needAnnotation  Whether the annotation {@link ConstructorResolving} is needed on a constructor.
     * @param <T>             type of the class which gets auto constructed.
     *
     * @return The initialized class, which has been auto formed by the type resolver.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @since 1.4.0
     */
    public static <T> T initClass(@NotNull Class<T> constructionCls, boolean needAnnotation) throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        return ConstructorResolver.initClass(constructionCls, (settings) -> settings.setNeedAnnotation(needAnnotation));
    }

    /**
     * Constructing the specified class by the type resolver. The specified {@link Register}s are used to resolve the
     * types of the parameters of the constructor. If the needAnnotation option is set to {@code false}, than no
     * constructor needs to have the annotation {@link ConstructorResolving}.
     *
     * @param constructionCls The class which need to be auto constructed.
     * @param needAnnotation  Whether the annotation {@link ConstructorResolving} is needed on a constructor.
     * @param registers       The registers that are used to retrieve the types from.
     * @param <T>             type of the class which gets auto constructed.
     *
     * @return The initialized class, which has been auto formed by the type resolver.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @since 1.4.0
     */
    public static <T> T initClass(@NotNull Class<T> constructionCls, boolean needAnnotation, Register... registers)
            throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        return ConstructorResolver.initClass(constructionCls, (settings) -> {
            settings.setRegisters(registers);
            settings.setNeedAnnotation(needAnnotation);
        });
    }


    /**
     * Constructing the specified class by the type resolver. This uses the specified options in the
     * {@link ConstructionSettings}. The default register that is used to resolve the parameters of the constructor is
     * the {@link TypeRegister}. The default annotation need is that at least one constructor needs to have the
     * annotation {@link ConstructorResolving}. It can also add identifiers, this is used for types that have
     * multiple options. For example an interface that has multiple implementations, and with the identifier and
     * implementation registered, it chose the correct implementation by its identifier.
     *
     * @param constructionCls              The class which need to be auto constructed.
     * @param constructionSettingsConsumer The {@link Consumer} of the {@link ConstructionSettings}.
     * @param <T>                          type of the class which gets auto constructed.
     *
     * @return The initialized class, which has been auto formed by the type resolver.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @see ConstructionSettings
     * @since 1.5.0
     */
    public static <T> T initClass(
            @NotNull Class<T> constructionCls, @NotNull Consumer<ConstructionSettings> constructionSettingsConsumer
    ) throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        ConstructionSettings constructionSettings = new ConstructionSettings();
        constructionSettingsConsumer.accept(constructionSettings);

        return new ConstructorResolver<>(constructionCls, constructionSettings).initClass();
    }


    /**
     * Constructing the specified class by the type resolver. This uses the specified options in the
     * {@link ConstructionSettings}. The default register that is used to resolve the parameters of the constructor is
     * the {@link TypeRegister}. The default annotation need is that at least one constructor needs to have the
     * annotation {@link ConstructorResolving}. It can also add identifiers, this is used for types that have
     * multiple options. For example an interface that has multiple implementations, and with the identifier and
     * implementation registered, it chose the correct implementation by its identifier.
     *
     * @param constructionCls      The class which need to be auto constructed.
     * @param constructionSettings The settings which the construction process uses to manipulate the pick order of the
     *                             constructors.
     * @param <T>                  type of the class which gets auto constructed.
     *
     * @return The initialized class, which has been auto formed by the type resolver.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @see ConstructionSettings
     * @since 1.5.0
     */
    public static <T> T initClass(
            @NotNull Class<T> constructionCls, ConstructionSettings constructionSettings
    ) throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        return new ConstructorResolver<>(constructionCls, constructionSettings).initClass();
    }

    /**
     * Constructing the specified class by the type resolver. This uses the specified options in the
     * {@link ConstructionSettings}. Those settings will be set by a builder. The builder causes to chain-edit the
     * {@link ConstructionSettings}, when the options are set call the {@link ConstructionSettingsBuilder#initClass()}
     * to initialize the class.
     *
     * @param constructionCls The class which need to be auto constructed.
     * @param <T>             type of the class which gets auto constructed.
     *
     * @return The builder of the {@link ConstructionSettings} to chain-edit the {@link ConstructionSettings}, when the
     * options are set call the {@link ConstructionSettingsBuilder#initClass()} to initialize the class.
     *
     * @see ConstructionSettingsBuilder
     * @since 1.5.0
     */
    @Contract("_ -> new")
    public static <T> @NotNull ConstructionSettingsBuilder<T> constructClass(@NotNull Class<T> constructionCls) {
        return new ConstructionSettingsBuilder<>(constructionCls);
    }


    /**
     * Constructing the specified class by the type resolver. This uses the settings specified in the
     * {@link #constructionSettings} class.
     *
     * @return The initialized class, which has been auto formed by the type resolver.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @since 1.5.0
     */
    private T initClass() throws
            ConstructorException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        if (constructionCls.isInterface() ||
            constructionCls.isEnum() ||
            (constructionCls.getModifiers() & Modifier.ABSTRACT) != 0) {
            throw new ConstructorException("%s is an interface, enum or abstract class, those cannot be constructed.".formatted(
                    constructionCls.getSimpleName()));
        }
        Optional<Constructor<?>> optionalConstructor = getClassConstructor();

        if (optionalConstructor.isEmpty()) {
            throw new ConstructorException("%s has no valid constructor.".formatted(constructionCls.getSimpleName()));
        }

        Constructor<?> constructor = optionalConstructor.get();

        Object[] initObjects = getResolvedObjects(constructor);

        constructor.setAccessible(true);
        T clazz = constructionCls.cast(constructor.newInstance(initObjects));
        constructor.setAccessible(false);

        return clazz;
    }

    /**
     * Get the constructor of a class. First it will check if there is a constructor or a public constructor has no
     * parameters. If non there will be looked for a constructor with resolvable types.
     *
     * @return {@link Optional} of a constructor. This will be empty if no valid constructor is found.
     *
     * @throws NoSuchMethodException If a matching method is not found.
     * @
     * @since 1.5.0
     */
    private Optional<Constructor<?>> getClassConstructor() throws NoSuchMethodException {
        if (constructionCls.getDeclaredConstructors().length == 0 ||
            hasConstructorWithNoParams(constructionCls.getConstructors())) {
            return Optional.of(constructionCls.getConstructor());
        }
        Optional<Constructor<?>> constructor = getConstructor(constructionCls.getConstructors());
        return constructor.isEmpty() ? getConstructor(constructionCls.getDeclaredConstructors()) : constructor;
    }

    /**
     * Get the resolved objects of a constructor.
     *
     * @param constructor constructor to resolve the types from
     *
     * @return resolved objects of the given constructor.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    private Object @NotNull [] getResolvedObjects(
            @NotNull Constructor<?> constructor
    ) {
        Consumer<InitProviderSettings> settingsConsumer = (settings) -> {
            settings.setIdentifiers(constructionSettings.getIdentifiers());
            settings.useAllRegisters(true);
        };

        Function<Class<?>, Object> mapper = (type) -> searchableRegisters.getInitProvider(type, settingsConsumer);

        return Arrays.stream(constructor.getParameterTypes()).map(mapper).toArray();
    }

    /**
     * Get a constructor that has the {@link ConstructorResolving} annotation. This constructor can only have resolvable
     * types. Those will be provided by the {@link TypeRegister}.
     *
     * @param constructors constructors that will be checked to the conditions.
     *
     * @return constructor with the highest {@link ConstructorPriority} and that has resolvable types.
     *
     * @since 1.5.0
     */
    private Optional<Constructor<?>> getConstructor(
            @NotNull Constructor<?> @NotNull [] constructors
    ) {
        Predicate<Constructor<?>> constructorPredicate =
                constructor -> (!constructionSettings.needAnnotation() || getResolverAnnotation(constructor) != null) &&
                               hasResolvableTypes(constructor.getParameterTypes());

        Stream<Constructor<?>> constructorStream = Arrays.stream(constructors).parallel().filter(constructorPredicate);
        try {
            if (!constructionSettings.needAnnotation()) {
                return constructorStream.findFirst();
            }

            return constructorStream.max(getConstructorComparator());

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
     * @since 1.5.0
     */
    private boolean hasResolvableTypes(Class<?> @NotNull [] parameters) {
        return Arrays.stream(parameters).parallel().allMatch(type -> searchableRegisters.hasProvider(type, true));
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
        return Comparator.comparing(constructor -> getResolverAnnotation(constructor).value().ordinal());
    }

    /**
     * Get the annotation of the constructor. The annotation will be used is the {@link ConstructorResolving}
     * annotation.
     *
     * @param constructor The constructor where the annotation could be present.
     *
     * @return If {@code null} the annotation is not present on the constructor.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    private static ConstructorResolving getResolverAnnotation(@NotNull Constructor<?> constructor) {
        return constructor.getAnnotation(ConstructorResolving.class);
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
        return Arrays.stream(constructors).parallel().anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

}
