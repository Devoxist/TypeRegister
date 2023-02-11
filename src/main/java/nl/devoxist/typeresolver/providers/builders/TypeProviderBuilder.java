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

package nl.devoxist.typeresolver.providers.builders;

import nl.devoxist.typeresolver.providers.TypeProvider;
import org.jetbrains.annotations.NotNull;

/**
 * {@link TypeProviderBuilder} is the superclass of those {@link TypeProviderBuilder} which will chain-edit the current
 * {@link TypeProvider}.
 *
 * @param <T> The type that is representing the type of the {@link TypeProvider}.
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.5.0
 */
public sealed abstract class TypeProviderBuilder<T> permits IdentifiersBuilder {

    /**
     * Build the {@link TypeProvider} of chain-edited {@link TypeProvider}.
     *
     * @param typeCls The class or interface that is representing the type of this {@link TypeProvider}.
     *
     * @return The build {@link TypeProvider}
     *
     * @since 1.5.0
     */
    public abstract @NotNull TypeProvider<T, ?> buildProvider(Class<T> typeCls);
}
