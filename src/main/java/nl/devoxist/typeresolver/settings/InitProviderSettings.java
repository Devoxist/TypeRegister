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

package nl.devoxist.typeresolver.settings;

import nl.devoxist.typeresolver.providers.IdentifierProvider;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.register.Register;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link InitProviderSettings} is an object that is holds the settings for the initiation of a provider
 * ({@link TypeProvider#getInitProvider()}).
 *
 * @author Dev-Bjorn
 * @version 1.6.1
 * @since 1.5.0
 */
public final class InitProviderSettings {
    /**
     * Whether the search goes through all the provided registers from the construction, otherwise it only will search
     * to the current using register. If the value has not been set it will only use the used {@link Register}.
     *
     * @since 1.5.0
     */
    private boolean allRegisters = false;
    /**
     * The identifier objects that the {@link IdentifierProvider} or other key providers needs to use, to choose which
     * implementation it will use.
     *
     * @since 1.5.0
     */
    private Object[] identifiers = {};

    /**
     * Check whether the search need to go through all the provided registers from the construction of the
     * {@link Register}, otherwise it only will search to the used {@link Register}.
     *
     * @return If {@code true} it will use all provided {@link Register}s, otherwise it will only use the used
     * {@link Register}. If the value has not been set it will only use the used {@link Register}.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public boolean useAllRegisters() {
        return allRegisters;
    }

    /**
     * Set whether the search need to go through all the provided registers from the construction of the
     * {@link Register}, otherwise it only will search to the used {@link Register}.
     *
     * @param allRegisters If {@code true} it will use all provided {@link Register}s, otherwise it will only use the
     *                     used {@link Register}.
     *
     * @since 1.5.0
     */
    public void useAllRegisters(boolean allRegisters) {
        this.allRegisters = allRegisters;
    }

    /**
     * Get the identifier objects that the {@link IdentifierProvider} or other key providers need to use, to choose
     * which implementation it will use.
     *
     * @return The identifier objects that the {@link IdentifierProvider} or other key providers need to use, to choose which
     * implementation it will use.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public Object[] getIdentifiers() {
        return identifiers.clone();
    }

    /**
     * Set the identifier objects that the {@link IdentifierProvider} or other key providers need to use, to choose
     * which implementation it will use.
     *
     * @param identifier The identifier objects that the {@link IdentifierProvider} or other key providers need to use,
     *                   to choose which implementation it will use.
     *
     * @since 1.5.0
     */
    public void setIdentifiers(@NotNull Object... identifier) {
        this.identifiers = identifier;
    }
}
