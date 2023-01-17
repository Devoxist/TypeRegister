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

package nl.devoxist.typeresolver.register;

import nl.devoxist.typeresolver.providers.TypeProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Find an object in a multiple {@link Register}s in another {@link Thread} than the main {@link Thread}.
 *
 * @param <T> The type of the object.
 *
 * @author Dev-Bjorn
 * @version 1.3.0
 * @since 1.3.0
 */
public final class RegisterSearch<T> extends Thread {
    /**
     * The {@link Register}s to do the search through.
     *
     * @since 1.3.0
     */
    private final Set<Register> registers;
    /**
     * The type to search in the {@link Register}.
     *
     * @since 1.3.0
     */
    private final Class<T> typeToSearch;
    /**
     * The future callback of the search.
     *
     * @since 1.3.0
     */
    private final CompletableFuture<TypeProvider<T, ?>> providerCompletableFuture = new CompletableFuture<>();

    /**
     * Constructs a new {@link RegisterSearch} which searches the object into the {@link Register}s.
     *
     * @param registers    The {@link Register}s to search the object from.
     * @param typeToSearch The value to find from the register.
     *
     * @since 1.3.0
     */
    private RegisterSearch(Set<Register> registers, Class<T> typeToSearch) {
        this.registers = registers;
        this.typeToSearch = typeToSearch;
    }

    /**
     * Search through the object on the non-main {@link Thread} from {@link Register}s.
     *
     * @param registers    The {@link Register}s to search the object from.
     * @param typeToSearch The key to find the value from.
     * @param <T>          The type of the {@link TypeProvider} which has been searched.
     *
     * @return The value of the key. If {@code null} the object is not found or there was an error while processing the search.
     *
     * @since 1.3.0
     */
    public static <T> TypeProvider<T, ?> searchRegisters(Set<Register> registers, Class<T> typeToSearch) {
        RegisterSearch<T> registerSearch = new RegisterSearch<>(registers, typeToSearch);
        registerSearch.start();
        return registerSearch.getTypeProvider();
    }

    /**
     * If this thread was constructed using a separate
     * {@code Runnable} run object, then that
     * {@code Runnable} object's {@code run} method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of {@code Thread} should override this method.
     *
     * @see #start()
     * @since 1.3.0
     */
    @Override
    public void run() {
        for (Register register : registers) {
            TypeProvider<T, ?> typeProvider = ObjectFinder.findTypeProvider(register, typeToSearch);

            if (typeProvider == null) {
                continue;
            }

            providerCompletableFuture.complete(typeProvider);
            return;
        }

        providerCompletableFuture.complete(null);
    }

    /**
     * The callback of the search. This can take a while, because it comes from the {@link #providerCompletableFuture}.
     *
     * @return The callback of the search.
     *
     * @since 1.3.0
     */
    public @Nullable TypeProvider<T, ?> getTypeProvider() {
        try {
            return providerCompletableFuture.get();
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }
}
