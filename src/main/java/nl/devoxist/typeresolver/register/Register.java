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

package nl.devoxist.typeresolver.register;

import nl.devoxist.typeresolver.collection.MergeSets;
import nl.devoxist.typeresolver.exception.RegisterException;
import nl.devoxist.typeresolver.providers.TypeObjectProvider;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.providers.TypeSupplierProvider;
import nl.devoxist.typeresolver.supplier.SerializableSupplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Supplier;

/**
 * {@link Register} is an object that registers the {@link TypeProvider}s. The {@link Register} is a tool which can be
 * used to create custom {@link Register}s.
 *
 * @author Dev-Bjorn
 * @version 1.3.1
 * @since 1.3.0
 */
public final class Register implements Cloneable, Comparable<Register> {
    /**
     * The map of the registered types with the corresponding {@link TypeProvider}.
     *
     * @since 1.3.0
     */
    private final Map<Class<?>, TypeProvider<?, ?>> typeProviders = new HashMap<>();
    /**
     * The priority of this register.
     *
     * @see RegisterPriority
     * @since 1.3.0
     */
    private final RegisterPriority priority;

    /**
     * The registers which has been combined with this {@link Register}
     *
     * @since 1.3.0
     */
    private final Set<Register> registers;

    /**
     * Construct a {@link Register}. This register is prioritized as {@link RegisterPriority#NORMAL}. There are
     * no combined {@link Register}s in this {@link Register}.
     *
     * @since 1.3.0
     */
    public Register() {
        this(RegisterPriority.NORMAL);
    }

    /**
     * Construct a {@link Register} with {@link Register}s which can be optionally combined with this {@link Register}.
     * This register is prioritized as {@link RegisterPriority#NORMAL}.
     *
     * @param registers The registers combining to this register.
     *
     * @since 1.3.0
     */
    public Register(Register... registers) {
        this(RegisterPriority.NORMAL, registers);
    }

    /**
     * Construct a {@link Register} with a {@link Collection} of {@link Register}s. These {@link Register}s can
     * optionally be combined with this {@link Register}. This {@link Register} is prioritized as
     * {@link RegisterPriority#NORMAL}.
     *
     * @param registers A collection {@link Register}s combining to this register.
     *
     * @since 1.3.0
     */
    public Register(@NotNull Collection<Register> registers) {
        this(RegisterPriority.NORMAL, registers.toArray(Register[]::new));
    }

    /**
     * Construct a {@link Register} with a priority and optional {@link Register}s which can be optionally combined with
     * this {@link Register}.
     *
     * @param priority  The priority of the constructed register.
     * @param registers The registers combining to this register.
     *
     * @implSpec The default priority of the register is {@link RegisterPriority#NORMAL}
     * @see RegisterPriority
     * @since 1.3.0
     */
    public Register(RegisterPriority priority, Register... registers) {
        this.priority = priority;

        Supplier<Set<Register>> setSupplier = TreeSet::new;
        this.registers = MergeSets.mergeSets(Set.of(registers), Register::getRegistries, setSupplier);
        this.registers.add(this);
    }

    /**
     * Get the registries of this {@link Register}.
     *
     * @return The registries of this {@link Register}.
     *
     * @see RegisterPriority
     * @since 1.3.0
     */
    @Contract(pure = true)
    public @NotNull @UnmodifiableView Set<Register> getRegistries() {
        Set<Register> registerSet = new TreeSet<>(this.registers);
        return Collections.unmodifiableSet(registerSet);
    }

    /**
     * Get the priority of this {@link Register}.
     *
     * @return The priority of this {@link Register}.
     *
     * @see RegisterPriority
     * @since 1.3.0
     */
    @Contract(pure = true)
    public RegisterPriority getPriority() {
        return priority;
    }

    /**
     * Get the {@link Map} where the {@link TypeProvider}s are saved.
     *
     * @return The {@link Map} where the {@link TypeProvider}s are saved.
     *
     * @since 1.3.0
     */
    public @NotNull @UnmodifiableView Map<Class<?>, TypeProvider<?, ?>> getTypeProviders() {
        Map<Class<?>, TypeProvider<?, ?>> clonedMap = new HashMap<>(typeProviders);
        return Collections.unmodifiableMap(clonedMap);
    }

    /**
     * Register a type with a {@link Supplier} provider.The registering of a {@link TypeProvider} causes a link to
     * appear in this {@link Register}. The registration of an object can only be taken place in this {@link Register}.
     *
     * @param type     The type which is going to be registered and linked to the provider.
     * @param provider The {@link Supplier} provider of the type which is going to be registered and linked to the type.
     * @param <T>      type of the type which is going to be registered.
     * @param <P>      type of the {@link Supplier} provider which is going to be registered.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.3.0
     */
    public <T, P extends T> boolean register(
            @NotNull Class<T> type,
            @NotNull SerializableSupplier<P> provider
    ) {
        Class<?> typeOfSupplier = provider.getSupplierClass();

        if (!type.isAssignableFrom(typeOfSupplier)) {
            throw new RegisterException("The type is not assignable from the provider.");
        }

        TypeProvider<T, ?> typeProvider = new TypeSupplierProvider<>(type, provider);

        return this.register(typeProvider);
    }

    /**
     * Register a type with a provider. The registering of a {@link TypeProvider} causes a link to appear in this
     * {@link Register}. The registration of an object can only be taken place in this {@link Register}.
     *
     * @param type     The type which is going to be registered and linked to the provider.
     * @param provider The provider of the type which is going to be registered and linked to the type.
     * @param <T>      type of the type which is going to be registered.
     * @param <P>      type of the provider which is going to be registered.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.3.0
     */
    @SuppressWarnings("unchecked")
    public <T, P> boolean register(@NotNull Class<T> type, @NotNull P provider) {
        Class<?> typeOfProvider = provider.getClass();

        if (!type.isAssignableFrom(typeOfProvider)) {
            throw new RegisterException("The type is not assignable from the provider.");
        }

        TypeProvider<T, ?> typeProvider = new TypeObjectProvider<>(type, (T) provider);

        return this.register(typeProvider);
    }

    /**
     * Register a type with a {@link TypeProvider}. A custom implementation of the {@link TypeProvider} is possible to
     * override the {@link TypeProvider} class. The registering of a {@link TypeProvider} causes a link to appear in
     * this {@link Register}. The registration of an object can only be taken place in this {@link Register}.
     *
     * @param typeProvider The {@link TypeProvider} which is going to be registered.
     * @param <T>          type of the type of the {@link TypeProvider}.
     * @param <P>          type of the provider of the {@link TypeProvider#getProvider()}.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @since 1.3.0
     */
    public <T, P> boolean register(
            TypeProvider<T, P> typeProvider
    ) {
        return typeProviders.putIfAbsent(typeProvider.getType(), typeProvider) == null;
    }

    /**
     * Unregister a type by its {@link TypeProvider}. Unregistering a {@link TypeProvider} causes the link to disappear
     * between the type and provider in this {@link Register}. Only this register can be used to
     * unregister a type. Note: This uses only the type check for unregistering the {@link TypeProvider}. It will not
     * check if there is a link between the type and provider. So, if the type of the {@link TypeProvider} is registered
     * in all ways the type will be unregistered and causes the link to disappear in this {@link Register}.
     *
     * @param typeProvider The {@link TypeProvider} which is going to be unregistered.
     * @param <T>          type of the type of the {@link TypeProvider}.
     * @param <P>          type of the provider of the {@link TypeProvider#getProvider()}.
     *
     * @throws RegisterException If the type is not registered.
     * @since 1.3.0
     */
    public <T, P> void unregister(
            @NotNull TypeProvider<T, P> typeProvider
    ) {
        Class<T> type = typeProvider.getType();
        this.unregister(type);
    }

    /**
     * Unregister a type by its type. Unregistering a {@link TypeProvider} causes the link to disappear between the type
     * and provider in this {@link Register}. Only this register can be used to unregister a type.
     *
     * @param type The type which is going to be unregistered.
     * @param <T>  type of the type which is going to be unregistered.
     *
     * @throws RegisterException If the type is not registered.
     * @since 1.3.0
     */
    public <T> void unregister(
            Class<T> type
    ) {
        if (!this.hasProvider(type)) {
            throw new RegisterException("'%s' is not registered.".formatted(type.getName()));
        }

        typeProviders.remove(type);
    }


    /**
     * Check if the type is registered. The check is done over this {@link Register}.
     *
     * @param type The type to check, if there is a link with any registered provider ({@link TypeProvider}).
     * @param <T>  type of the type to check.
     *
     * @return If {@code true} the type is registered.
     *
     * @since 1.3.0
     */
    @Contract(pure = true)
    public <T> boolean hasProvider(Class<T> type) {
        return this.hasProvider(type, false);
    }

    /**
     * Check if the type is registered. The check can be done over all the provided registers.
     *
     * @param type         The type to check, if there is a link with any registered provider ({@link TypeProvider}).
     * @param allRegisters If {@code true} it type is checked through all the provided registers from the construction
     *                     of the class ({@link #registers}). Otherwise, it only checks this {@link Register}.
     * @param <T>          type of the type to check.
     *
     * @return If {@code true} the type is registered.
     *
     * @since 1.3.0
     */
    @Contract(pure = true)
    public <T> boolean hasProvider(Class<T> type, boolean allRegisters) {
        return allRegisters ? this.searchRegisters(type) != null : typeProviders.containsKey(type);
    }

    /**
     * Search and get the provider by its type. A way to create a custom implementation is to override the
     * {@link TypeProvider#getProvider()} in a custom {@link TypeProvider}. The search is completed through the
     * current {@link Register}.
     *
     * @param type The type to search the link from between the provider ({@link TypeProvider}).
     * @param <T>  type of the type to search the link from.
     * @param <P>  type of the provider which has been searched and linked to the type ({@link TypeProvider}).
     *
     * @return The provider which has been searched by its type.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.3.0
     */
    public <T, P> @NotNull P getProviderByType(Class<T> type) {
        return this.getProviderByType(type, false);
    }

    /**
     * Search and get the provider by its type. A way to create a custom implementation is to override the
     * {@link TypeProvider#getProvider()} in a custom {@link TypeProvider}. The search can be done through all
     * provided registers ({@link #registers}).
     *
     * @param type         The type to search the link from between the provider ({@link TypeProvider}).
     * @param allRegisters If {@code true} it search through all the provided registers from the construction of the
     *                     class ({@link #registers}). Otherwise, it only searches through this {@link Register}.
     * @param <T>          type of the type to search the link from.
     * @param <P>          type of the provider which has been searched and linked to the type ({@link TypeProvider}).
     *
     * @return The provider which has been searched by its type.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.3.0
     */
    @SuppressWarnings("unchecked")
    public <T, P> @NotNull P getProviderByType(Class<T> type, boolean allRegisters) {
        TypeProvider<T, ?> typeProvider = this.findTypeProvider(type, allRegisters);
        return (P) typeProvider.getProvider();
    }

    /**
     * Search and get the initialized provider. A way to create a custom implementation is to override the
     * {@link TypeProvider#getInitProvider()} in a custom {@link TypeProvider}. The search is completed through the
     * current {@link Register}.
     *
     * @param type The type to search the link from between the provider ({@link TypeProvider}).
     * @param <T>  type of the type to search the link from.
     *
     * @return The initialized provider which has been searched by its type.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.3.0
     */
    public <T> @NotNull T getInitProvider(Class<T> type) {
        return this.getInitProvider(type, false);
    }

    /**
     * Search and get the initialized provider. A way to create a custom implementation is to override the
     * {@link TypeProvider#getInitProvider()} in a custom {@link TypeProvider}. The search can be done through all
     * provided registers ({@link #registers}).
     *
     * @param type         The type to search the link from between the provider ({@link TypeProvider}).
     * @param allRegisters If {@code true} it search through all the provided registers from the construction of the
     *                     class ({@link #registers}). Otherwise, it only searches through this {@link Register}.
     * @param <T>          type of the type to search the link from.
     *
     * @return The initialized provider which has been searched by its type.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.3.0
     */
    public <T> @NotNull T getInitProvider(Class<T> type, boolean allRegisters) {
        TypeProvider<T, ?> typeProvider = this.findTypeProvider(type, allRegisters);
        return typeProvider.getInitProvider();
    }

    /**
     * Search and get the {@link TypeProvider} of the type. The search can be done through all
     * provided registers ({@link #registers}).
     *
     * @param type         The type to search the link from between the provider ({@link TypeProvider}).
     * @param allRegisters If {@code true} it search through all the provided registers from the construction of the
     *                     class ({@link #registers}). Otherwise, it only searches through this {@link Register}.
     * @param <T>          type of the type to search the link from.
     *
     * @return The {@link TypeProvider} of the searched type.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.3.0
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private <T> TypeProvider<T, ?> findTypeProvider(Class<T> type, boolean allRegisters) {
        TypeProvider<T, ?> typeProvider;

        if (allRegisters) {
            typeProvider = searchRegisters(type);
        } else {
            typeProvider = (TypeProvider<T, ?>) typeProviders.get(type);
        }

        if (typeProvider == null) {
            throw new RegisterException("The provider of '%s' is not registered.".formatted(type.getName()));
        }

        return typeProvider;
    }

    /**
     * Search and get the {@link TypeProvider} of the {@link Register} with the highest priority containing the type.
     *
     * @param type The type to search the link from between the provider ({@link TypeProvider}).
     * @param <T>  type of the type to search the link from.
     *
     * @return The {@link TypeProvider} of the type. If {@code null} the type is not registered or an exception was thrown.
     *
     * @since 1.3.0
     */
    private <T> @Nullable TypeProvider<T, ?> searchRegisters(Class<T> type) {
        return RegisterSearch.searchRegisters(registers, type);
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
     * @see Cloneable
     */
    @Override
    public @Nullable Register clone() {
        try {
            return (Register) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     *
     * @apiNote In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * The string output is not necessarily stable over time or across
     * JVM invocations.
     * @implSpec The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     */
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Register{" +
               "typeProviders=" + typeProviders +
               ", priority=" + priority +
               '}';
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(@NotNull Register o) {
        return Comparator.comparing(Register::getPriority)
                .thenComparing(register -> register.getTypeProviders().keySet().hashCode())
                .reversed()
                .compare(this, o);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     *
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param obj the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     *
     * @implSpec The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * In other words, under the reference equality equivalence
     * relation, each equivalence class only has a single element.
     * @apiNote It is generally necessary to override the {@link #hashCode hashCode}
     * method whenever this method is overridden, to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     * @see #hashCode()
     * @see java.util.HashMap
     */
    @Contract(value = "null -> false",
              pure = true)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Register register = (Register) obj;
        return typeProviders.equals(register.typeProviders) && priority == register.priority;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@link
     *     #equals(Object) equals} method, then calling the {@code
     *     hashCode} method on each of the two objects must produce the
     *     same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link #equals(Object) equals} method, then
     *     calling the {@code hashCode} method on each of the two objects
     *     must produce distinct integer results.  However, the programmer
     *     should be aware that producing distinct integer results for
     *     unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     *
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined
     * by class {@code Object} returns distinct integers for distinct objects.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(typeProviders, priority);
    }
}
