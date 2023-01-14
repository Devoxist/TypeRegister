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

package nl.devoxist.typeresolver;

import nl.devoxist.typeresolver.exception.RegisterException;
import nl.devoxist.typeresolver.providers.TypeObjectProvider;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.providers.TypeSupplierProvider;
import nl.devoxist.typeresolver.supplier.SerializableSupplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * {@link TypeRegister} is an object that registers the {@link TypeProvider}s.
 *
 * @author Dev-Bjorn
 * @version 1.1
 * @since 1.0
 */
public final class TypeRegister {
    /**
     * The map of the registered types with the corresponding {@link TypeProvider}.
     *
     * @since 1.0
     */
    private static final Map<Class<?>, TypeProvider<?, ?>> typeProviders = new HashMap<>();

    /**
     * Register a type with a provider.
     *
     * @param typeClazz The typeClazz with a link to the provider.
     * @param provider  The {@link Supplier} of the provider.
     * @param <T>       type of the typeClazz
     * @param <P>       type of the provider
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.0
     */
    public static <T, P extends T> void register(
            @NotNull Class<T> typeClazz,
            @NotNull SerializableSupplier<P> provider
    ) {
        Class<?> type = provider.getSupplierClass();

        if (!typeClazz.isAssignableFrom(type)) {
            throw new RegisterException("The type is not assignable from the provider.");
        }

        TypeProvider<T, ?> typeProvider = new TypeSupplierProvider<>(typeClazz, provider);

        typeProviders.putIfAbsent(typeClazz, typeProvider);
    }

    /**
     * Register a type with a provider.
     *
     * @param typeClazz The typeClazz with a link to the provider.
     * @param provider  The {@link Supplier} of the provider.
     * @param <T>       type of the typeClazz
     * @param <P>       type of the provider
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T, P> void register(@NotNull Class<T> typeClazz, @NotNull P provider) {
        Class<?> type = provider.getClass();

        if (!typeClazz.isAssignableFrom(type)) {
            throw new RegisterException("The type is not assignable from the provider.");
        }

        TypeProvider<T, ?> typeProvider = new TypeObjectProvider<>(typeClazz, (T) provider);

        typeProviders.putIfAbsent(typeClazz, typeProvider);
    }

    /**
     * Check if the typeClazz is registered.
     *
     * @param typeClazz typeClazz to check if it is registered.
     * @param <T>       type of the typeClazz.
     *
     * @return If {@code true} the typeClazz is already registered.
     *
     * @since 1.0
     */
    @Contract(pure = true)
    public static <T> boolean hasProvider(Class<T> typeClazz) {
        return typeProviders.containsKey(typeClazz);
    }

    /**
     * Get {@link Supplier} of the provider.
     *
     * @param typeClazz The type to get the provider from.
     * @param <T>       type of the typeClazz
     * @param <P>       type of the provider.
     *
     * @return {@link Supplier} of the provider.
     *
     * @since 1.0
     * @deprecated Use {@link #getProviderByType(Class)}
     */
    @SuppressWarnings("unchecked")
    @Deprecated(since = "1.1",
                forRemoval = true)
    public static <T, P> @NotNull Supplier<P> getProvider(Class<T> typeClazz) {
        TypeProvider<?, ?> typeProvider = findTypeProvider(typeClazz);

        if (!(typeProvider instanceof TypeSupplierProvider<?, ?> typeSupplierProvider)) {
            throw new RegisterException("The provider of '%s' is not a supplier.".formatted(typeClazz.getName()));
        }

        return (Supplier<P>) typeSupplierProvider.getProvider();
    }

    /**
     * Get the provider.
     *
     * @param typeClazz The type to get the provider from.
     * @param <T>       type of the typeClazz
     * @param <P>       type of the provider
     *
     * @return {@link Supplier} of the provider.
     *
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static <T, P> @NotNull P getProviderByType(Class<T> typeClazz) {
        TypeProvider<T, ?> typeProvider = findTypeProvider(typeClazz);
        return (P) typeProvider.getProvider();
    }

    /**
     * Get the initialized provider.
     *
     * @param typeClazz The type of the {@link TypeProvider}.
     * @param <T>       type of the typeClazz
     *
     * @return The initialized provider
     *
     * @since 1.0
     */
    public static <T> @NotNull T getInitProvider(Class<T> typeClazz) {
        TypeProvider<T, ?> typeProvider = findTypeProvider(typeClazz);
        return typeProvider.getInitProvider();
    }

    /**
     * Get the {@link TypeProvider} of the type.
     *
     * @param typeClazz The type of the provider
     * @param <T>       type of the type.
     *
     * @return The {@link TypeProvider} of the type.
     *
     * @since 1.1
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private static <T> TypeProvider<T, ?> findTypeProvider(Class<T> typeClazz) {
        TypeProvider<?, ?> typeProvider = typeProviders.get(typeClazz);
        if (typeProvider == null) {
            throw new RegisterException("The provider of '%s' is not registerd.".formatted(typeClazz.getName()));
        }
        return (TypeProvider<T, ?>) typeProvider;
    }
}
