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

import nl.devoxist.typeresolver.exception.ProviderException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link TypeKeyProvider} is a subclass of {@link TypeProvider}, which links a type with a {@link Collection}. It uses
 * an identifier to retrieve the implementation in the collection.
 *
 * @param <T> type that represents the type of the {@link TypeProvider}.
 * @param <I> type that represents the identifier.
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.5.0
 */
public final class TypeKeyProvider<T, I> extends TypeProvider<T, Collection<? extends T>> implements Cloneable {
    /**
     * The {@link Map} that holds the identifiers with their implementations of the type.
     *
     * @since 1.5.0
     */
    private final Map<I, T> identifiersMap;
    /**
     * The identifier to get the value {@link #identifiersMap}. NOTE: this can only be applied when this is a clone. The
     * {@link #applyIdentifiers(Object[])} sets the value of this field.
     *
     * @since 1.5.0
     */
    private Object[] identifiers;

    /**
     * Construct a new {@link TypeProvider} object. This links a type to an object.
     *
     * @param typeCls        The class or interface that is representing the type of this {@link TypeProvider}.
     * @param identifiersMap The {@link Map} that holds the identifiers with their implementations of the type
     *
     * @throws ProviderException If there are no identifiers registered.
     * @since 1.5.0
     */
    public TypeKeyProvider(@NotNull Class<T> typeCls, @NotNull Map<I, T> identifiersMap) {
        super(typeCls, identifiersMap.values());

        if (identifiersMap.size() == 0) {
            throw new ProviderException("There need to be at least one identifier with it type.");
        }

        this.identifiersMap = identifiersMap;
    }

    /**
     * Get the identifiers with its types.
     *
     * @return A {@link Map} of an identifiers with the types.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public @NotNull Map<I, T> getIdentifiersMap() {
        return new HashMap<>(identifiersMap);
    }

    /**
     * Get the provider by its identifier. The identifier can be applied through the {@link #applyIdentifiers(Object[])}
     * method.
     *
     * @return The provider retrieved by its identifier.
     *
     * @throws ProviderException If the identifier is null, this can only be prevented if the
     *                           {@link #applyIdentifiers(Object[])} return is used, or if the identifier is not
     *                           registered.
     * @see #applyIdentifiers(Object[])
     * @since 1.5.0
     */
    @Override
    public @NotNull T getInitProvider() {
        if (identifiers == null) {
            throw new ProviderException(
                    "The identifier cannot be null, use #applyIdentifier to clone this instance with an identifier.");
        }

        T instance = getInstance();

        if (instance == null) {
            throw new ProviderException("The identifier has not been registered.");
        }

        return instance;
    }

    /**
     * Get the provider by its identifier.
     *
     * @return The provider that is retrieved by its identifier.
     *
     * @since 1.5.0
     */
    @Nullable
    private T getInstance() {
        for (Object o : identifiers) {
            T t = identifiersMap.get(o);

            if (t == null) {
                continue;
            }

            return t;
        }

        return null;
    }

    /**
     * Set the identifier of the cloned instance of this instance.
     *
     * @param identifier The identifier that is going to be used in the resolver of this provider.
     *
     * @return A cloned instance of this instance with an identifier that has been set.
     *
     * @since 1.5.0
     */
    public @NotNull TypeKeyProvider<T, I> applyIdentifiers(@NotNull Object... identifier) {
        TypeKeyProvider<T, I> clonedTypeProvider = this.clone();
        clonedTypeProvider.identifiers = identifier;
        return clonedTypeProvider;
    }

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     * <p>
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     * <p>
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by {@code super.clone} before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by {@code super.clone}
     * need to be modified.
     *
     * @return a clone of this instance.
     *
     * @throws AssertionError If the object's class does not
     *                        support the {@code Cloneable} interface. Subclasses
     *                        that override the {@code clone} method can also
     *                        throw this exception to indicate that an instance cannot
     *                        be cloned.
     * @implSpec The method {@code clone} for class {@code Object} performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface {@code Cloneable}, then a
     * {@code CloneNotSupportedException} is thrown. Note that all arrays
     * are considered to implement the interface {@code Cloneable} and that
     * the return type of the {@code clone} method of an array type {@code T[]}
     * is {@code T[]} where T is any reference or primitive type.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     * <p>
     * The class {@code Object} does not itself implement the interface
     * {@code Cloneable}, so calling the {@code clone} method on an object
     * whose class is {@code Object} will result in throwing an
     * exception at run time.
     * @see java.lang.Cloneable
     * @since 1.5.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public TypeKeyProvider<T, I> clone() {
        try {
            return (TypeKeyProvider<T, I>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
