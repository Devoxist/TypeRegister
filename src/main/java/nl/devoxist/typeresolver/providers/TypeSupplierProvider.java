/*
 * Copyright (c) 2023 Devoxist, Dev-Bjorn
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

package nl.devoxist.typeresolver.providers;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * {@link TypeSupplierProvider} is a subclass of {@link TypeProvider} and it links a type with a {@link Supplier}.
 *
 * @param <T> The type of the type.
 * @param <P> The type of the {@link Supplier} provider.
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.1.0
 * @deprecated Use {@link ScopedProvider}, this is caused by refactoring.
 */
@Deprecated(since = "1.6.1",
            forRemoval = true)
public final class TypeSupplierProvider<T, P extends T> extends TypeProvider<T, Supplier<P>> {

    /**
     * Construct a {@link TypeProvider} object. This object holds the {@link Supplier} object and the type.
     *
     * @param typeCls  The class or interface that is representing the type of this {@link TypeProvider}.
     * @param provider the provider class
     *
     * @since 1.1.0
     */
    public TypeSupplierProvider(@NotNull Class<T> typeCls, @NotNull Supplier<P> provider) {
        super(typeCls, provider);
    }

    /**
     * Get the initiated object of the provider. For example a {@link Supplier} returns the {@link Supplier#get()}.
     *
     * @return The initiated object of the provider.
     *
     * @since 1.1.0
     */
    @Override
    public T getInitProvider() {
        return getProvider().get();
    }
}
