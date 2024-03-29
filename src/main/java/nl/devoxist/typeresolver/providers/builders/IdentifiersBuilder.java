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

import nl.devoxist.typeresolver.exception.RegisterException;
import nl.devoxist.typeresolver.functions.SerializableSupplier;
import nl.devoxist.typeresolver.providers.IdentifierProvider;
import nl.devoxist.typeresolver.providers.ObjectProvider;
import nl.devoxist.typeresolver.providers.ScopedProvider;
import nl.devoxist.typeresolver.providers.TypeProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * {@link IdentifiersBuilder} is the subclass of {@link TypeProviderBuilder} which will chain-edit the a
 * {@link IdentifierProvider}. Eventually it will build the {@link IdentifierProvider} with the values in this object.
 *
 * @param <T> The type that is representing the type of the {@link TypeProvider}.
 * @param <I> The type that is representing the type of the identifier.
 *
 * @author Dev-Bjorn
 * @version 1.6.1
 * @since 1.5.0
 */
public final class IdentifiersBuilder<T, I> extends TypeProviderBuilder<T> {
    /**
     * The {@link Map} that holds the identifiers with their corresponding values.
     *
     * @since 1.5.0
     */
    private final Map<I, TypeProvider<T, ?>> identifiersMap = new HashMap<>();

    /**
     * Add an identifier with a value to the {@link IdentifierProvider#getIdentifiersMap()}.
     *
     * @param identifier The identifier which will be linked to the value.
     * @param value      The value of the identifier. This value will be returned when the given identifier is applied
     *                   in {@link IdentifierProvider}.
     *
     * @return The builder of the {@link IdentifierProvider} to chain-edit the {@link IdentifierProvider}, when the
     * options are set call the {@link IdentifiersBuilder#buildProvider(Class)} to get the {@link TypeProvider}.
     *
     * @since 1.5.0
     */
    @Contract("_, _ -> this")
    @SuppressWarnings("unchecked")
    public IdentifiersBuilder<T, I> addIdentifier(I identifier, T value) {
        this.identifiersMap.put(identifier, new ObjectProvider<>((Class<T>) value.getClass(), value));
        return this;
    }

    /**
     * Add an identifier with a value to the {@link IdentifierProvider#getIdentifiersMap()}.
     *
     * @param identifier The identifier which will be linked to the value.
     * @param value      The supplier value of the identifier. Each time the
     *                   {@link IdentifierProvider#getInitProvider()}
     *                   and the given identifier is applied, it will return the output of {@link Supplier#get()}.
     *
     * @return The builder of the {@link IdentifierProvider} to chain-edit the {@link IdentifierProvider}, when the
     * options are set call the {@link IdentifiersBuilder#buildProvider(Class)} to get the {@link TypeProvider}.
     *
     * @since 1.6.0
     * @deprecated Due to refactoring use {@link #addScopedIdentifier(Object, SerializableSupplier)}
     */
    @Contract("_, _ -> this")
    @Deprecated(since = "1.6.1",
                forRemoval = true)
    public IdentifiersBuilder<T, I> addSupplierIdentifier(I identifier, SerializableSupplier<T> value) {
        this.identifiersMap.put(identifier, new ScopedProvider<>(value.getSupplierClass(), value));
        return this;
    }

    /**
     * Add an identifier with a value to the {@link IdentifierProvider#getIdentifiersMap()}.
     *
     * @param identifier The identifier which will be linked to the value.
     * @param value      The supplier value of the identifier. Each time the
     *                   {@link IdentifierProvider#getInitProvider()}
     *                   and the given identifier is applied, it will return the output of {@link Supplier#get()}.
     *
     * @return The builder of the {@link IdentifierProvider} to chain-edit the {@link IdentifierProvider}, when the
     * options are set call the {@link IdentifiersBuilder#buildProvider(Class)} to get the {@link TypeProvider}.
     *
     * @since 1.6.1
     */
    @Contract("_, _ -> this")
    public IdentifiersBuilder<T, I> addScopedIdentifier(I identifier, SerializableSupplier<T> value) {
        this.identifiersMap.put(identifier, new ScopedProvider<>(value.getSupplierClass(), value));
        return this;
    }

    /**
     * Build the {@link TypeProvider} of chain-edited {@link TypeProvider}.
     *
     * @param typeCls The class or interface that is representing the type of this {@link TypeProvider}.
     *
     * @return The build {@link TypeProvider}
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    @Override
    public @NotNull IdentifierProvider<T, I> buildProvider(@NotNull Class<T> typeCls) {
        if (identifiersMap.isEmpty()) {
            throw new RegisterException(
                    "There are no identifiers registered, and thus no provider registered. So first use the #addIdentifier(I,T), to add some identifiers");
        }
        return new IdentifierProvider<>(typeCls, identifiersMap);
    }
}
