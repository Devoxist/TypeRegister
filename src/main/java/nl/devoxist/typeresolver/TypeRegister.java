/*
 * Copyright (c) 2022 Devoxist, Dev-Bjorn
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class TypeRegister {
    private static final Map<Class<?>, TypeProvider<?, ?>> typeProviders = new HashMap<>();

    /**
     * Register a type with a provider.
     *
     * @param typeClazz The typeClazz with a link to the provider.
     * @param provider  The {@link Supplier} of the provider.
     * @param <T>       type of the typeClazz
     * @param <P>       type of the provider
     */
    public static <T, P extends T> void register(Class<T> typeClazz, Supplier<P> provider) {
        typeProviders.putIfAbsent(typeClazz, createTypeProvider(typeClazz, provider));
    }

    /**
     * Check if the typeClazz is registered.
     *
     * @param typeClazz typeClazz to check if it is registered.
     * @param <T>       type of the typeClazz.
     *
     * @return If {@code true} the typeClazz is already registered.
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
     *
     * @return {@link Supplier} of the provider.
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Supplier<T> getProvider(Class<T> typeClazz) {
        return (Supplier<T>) typeProviders.get(typeClazz).provider();
    }

    /**
     * Get the initialized provider.
     *
     * @param typeClazz The type to get the provider from.
     * @param <T>       type of the typeClazz
     *
     * @return The initialized provider
     */
    public static <T> @NotNull T getInitProvider(Class<T> typeClazz) {
        return getProvider(typeClazz).get();
    }

    /**
     * Create a type provider.
     *
     * @param typeClazz The type of the provider class.
     * @param provider  Provider supplier of the type.
     * @param <T>       type of the typeClazz
     * @param <P>       type of the provider
     *
     * @return The TypeProvider Class.
     */
    @Contract("_, _ -> new")
    private static <T, P extends T> @NotNull TypeProvider<T, P> createTypeProvider(
            Class<T> typeClazz,
            Supplier<P> provider
    ) {
        return new TypeProvider<>(typeClazz, provider);
    }
}
