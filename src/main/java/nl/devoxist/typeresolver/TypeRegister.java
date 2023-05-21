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

import nl.devoxist.typeresolver.exception.ProviderException;
import nl.devoxist.typeresolver.exception.RegisterException;
import nl.devoxist.typeresolver.functions.SerializableConsumer;
import nl.devoxist.typeresolver.functions.SerializableSupplier;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.providers.builders.TypeProviderBuilder;
import nl.devoxist.typeresolver.register.Register;
import nl.devoxist.typeresolver.register.RegisterPriority;
import nl.devoxist.typeresolver.settings.InitProviderSettings;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link TypeRegister} is an object that registers the {@link TypeProvider}s. This can be used as the general registery
 * of these types. This {@link Register} uses the {@link RegisterPriority#NORMAL}.
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.0.0
 */
public final class TypeRegister {
    /**
     * The register of the {@link TypeRegister}.
     *
     * @since 1.3.0
     */
    private static final Register REGISTER = new Register();

    /**
     * Construct a new {@link TypeRegister} object. This always fails, because the class is a static class. So it
     * only contains static objects. Thus, it throws an {@link IllegalAccessException}.
     *
     * @throws IllegalAccessException If the {@link TypeRegister} was try to construct the class. The
     *                                construction of this class is not possible, because this is a static class.
     * @since 1.3.0
     */
    @Contract(value = " -> fail",
              pure = true)
    @ApiStatus.Internal
    private TypeRegister() throws IllegalAccessException {
        throw new IllegalAccessException("This class is an static class, so this class cannot be initialized.");
    }

    /**
     * Get the {@link Register} of the {@link TypeRegister}.
     *
     * @return The {@link Register} of the {@link TypeRegister}.
     */
    @Contract(pure = true)
    public static Register getRegister() {
        return REGISTER;
    }

    /**
     * Register a type with a {@link Supplier} provider. The registering of a {@link TypeProvider} causes a link to
     * appear in the {@link TypeRegister}.
     *
     * @param typeCls  The type which is going to be registered and linked to the provider.
     * @param provider The {@link Supplier} provider of the type which is going to be registered and linked to the type.
     * @param <T>      type of the type which is going to be registered.
     * @param <P>      type of the {@link Supplier} provider which is going to be registered.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.0.0
     * @deprecated Due to refactoring use {@link #registerScoped(Class, SerializableSupplier)}.
     */
    @Deprecated(since = "1.6.1",
                forRemoval = true)
    public static <T, P extends T> boolean register(
            @NotNull Class<T> typeCls,
            @NotNull SerializableSupplier<P> provider
    ) {
        return REGISTER.register(typeCls, provider);
    }

    /**
     * Register a type with a {@link Supplier} provider. The registering of a {@link TypeProvider} causes a link to
     * appear in the {@link TypeRegister}.
     *
     * @param typeCls  The type which is going to be registered and linked to the provider.
     * @param provider The {@link Supplier} provider of the type which is going to be registered and linked to the type.
     * @param <T>      type of the type which is going to be registered.
     * @param <P>      type of the {@link Supplier} provider which is going to be registered.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.0.0
     */
    public static <T, P extends T> boolean registerScoped(
            @NotNull Class<T> typeCls,
            @NotNull SerializableSupplier<P> provider
    ) {
        return REGISTER.registerScoped(typeCls, provider);
    }

    /**
     * Register a type with a provider. The registering of a {@link TypeProvider} causes a link to appear in the
     * {@link TypeRegister}.
     *
     * @param typeCls  The type which is going to be registered and linked to the provider.
     * @param provider The provider of the type which is going to be registered and linked to the type.
     * @param <T>      type of the type which is going to be registered.
     * @param <P>      type of the provider which is going to be registered.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @throws RegisterException if the type is not assignable from the provider.
     * @since 1.1.0
     */
    public static <T, P extends T> boolean register(@NotNull Class<T> typeCls, @NotNull P provider) {
        return REGISTER.register(typeCls, provider);
    }

    /**
     * Register a type with a {@link TypeProvider}. A custom implementation of the {@link TypeProvider} is possible to
     * override the {@link TypeProvider} class. The registering of a {@link TypeProvider} causes a link to appear in
     * the {@link TypeRegister}.
     *
     * @param typeProvider The {@link TypeProvider} which is going to be registered.
     * @param <T>          type of the type of the {@link TypeProvider}.
     * @param <P>          type of the provider of the {@link TypeProvider#getProvider()}.
     *
     * @return if {@code true} the {@link TypeProvider} is registered.
     *
     * @since 1.2.0
     */
    public static <T, P> boolean register(
            TypeProvider<T, P> typeProvider
    ) {
        return REGISTER.register(typeProvider);
    }

    /**
     * Register or update a type with a provider, that has been build by a {@link TypeProviderBuilder}. If the type
     * already is registered, it will update the provider.
     *
     * @param typeCls         The type which is going to be registered and linked to the provider.
     * @param builderConsumer The {@link Consumer} of the builder.
     * @param <T>             type of the type which is going to be registered.
     * @param <X>             type of the builder which is going to build the {@link TypeProvider}.
     *
     * @return if {@code true} the type with the build provider is registered.
     *
     * @throws RegisterException If the consumer is an abstract type, or if there is no valid constructor, or if a
     *                           matching method is not found, or if the underlying constructor throws an exception, or
     *                           if the class that declares the underlying constructor represents an abstract class, or
     *                           if this Constructor object is enforcing Java language access control and the underlying
     *                           constructor is inaccessible.
     * @see TypeProviderBuilder
     * @since 1.5.0
     */
    public static <T, X extends TypeProviderBuilder<T>> boolean register(
            @NotNull Class<T> typeCls,
            @NotNull SerializableConsumer<X> builderConsumer
    ) {
        return REGISTER.register(typeCls, builderConsumer);
    }

    /**
     * Unregister a type by its {@link TypeProvider}. Unregistering a {@link TypeProvider} causes the link to disappear
     * between the type and provider in the {@link TypeRegister}.
     *
     * @param typeProvider The {@link TypeProvider} which is going to be unregistered.
     * @param <T>          type of the type of the {@link TypeProvider}.
     * @param <P>          type of the provider of the {@link TypeProvider#getProvider()}.
     *
     * @throws RegisterException If the type is not registered.
     * @since 1.2.0
     */
    public static <T, P> void unregister(@NotNull TypeProvider<T, P> typeProvider) {
        REGISTER.unregister(typeProvider);
    }

    /**
     * Unregister a type by its type. Unregistering a {@link TypeProvider} causes the link to disappear between the type
     * and provider in the {@link TypeRegister}.
     *
     * @param typeCls The type which is going to be unregistered.
     * @param <T>     type of the type which is going to be unregistered.
     *
     * @throws RegisterException If the type is not registered.
     * @since 1.2.0
     */
    public static <T> void unregister(Class<T> typeCls) {
        REGISTER.unregister(typeCls);
    }


    /**
     * Check if the type is registered. The check is done over this {@link Register}.
     *
     * @param typeCls The type to check, if there is a link with any registered provider ({@link TypeProvider}).
     * @param <T>     type of the type to check.
     *
     * @return If {@code true} the type is registered.
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static <T> boolean hasProvider(Class<T> typeCls) {
        return REGISTER.hasProvider(typeCls);
    }

    /**
     * Search and get the provider by its type. A way to create a custom implementation is to override the
     * {@link TypeProvider#getProvider()} in a custom {@link TypeProvider}.
     *
     * @param typeCls The type to search the link from between the provider ({@link TypeProvider}).
     * @param <T>     type of the type to search the link from.
     * @param <P>     type of the provider which has been searched and linked to the type ({@link TypeProvider}).
     *
     * @return The provider which has been searched by its type.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.0.0
     */
    public static <T, P> @NotNull P getProviderByType(Class<T> typeCls) {
        return REGISTER.getProviderByType(typeCls);
    }

    /**
     * Search and get the initialized provider. A way to create a custom implementation is to override the
     * {@link TypeProvider#getInitProvider()} in a custom {@link TypeProvider}.
     *
     * @param typeCls The type to search the link from between the provider ({@link TypeProvider}).
     * @param <T>     type of the type to search the link from.
     *
     * @return The initialized provider which has been searched by its type. This will return the output of the
     * {@link TypeProvider#getInitProvider()}.
     *
     * @throws RegisterException If the provider is not registered.
     * @since 1.0.0
     */
    public static <T> @NotNull T getInitProvider(Class<T> typeCls) {
        return REGISTER.getInitProvider(typeCls);
    }

    /**
     * Search and get the initialized provider. A way to create a custom implementation is to override the
     * {@link TypeProvider#getInitProvider()} in a custom {@link TypeProvider}. It uses the provided settings to
     * manipulate the search of the type.
     *
     * @param typeCls                  The type to search the link from between the provider ({@link TypeProvider}).
     * @param providerSettingsConsumer The settings that will manipulate the search of the {@link TypeProvider}.
     * @param <T>                      type of the type to search the link from.
     *
     * @return The initialized provider which has been searched by its type. This will return the output of the
     * {@link TypeProvider#getInitProvider()}.
     *
     * @throws RegisterException If the provider is not registered.
     * @throws ProviderException If the identifiers is null or empty, or if the identifier is not registered.
     * @since 1.5.0
     */
    public static <T> @NotNull T getInitProvider(
            Class<T> typeCls,
            @NotNull Consumer<InitProviderSettings> providerSettingsConsumer
    ) {
        return REGISTER.getInitProvider(typeCls, providerSettingsConsumer);
    }
}
