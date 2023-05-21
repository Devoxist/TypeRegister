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

import nl.devoxist.typeresolver.TypeRegister;
import nl.devoxist.typeresolver.constructor.ConstructorResolving;
import nl.devoxist.typeresolver.providers.IdentifierProvider;
import nl.devoxist.typeresolver.register.Register;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ConstructionSettings} is an object that is holds the settings for the auto construction of a {@link Class}.
 *
 * @author Dev-Bjorn
 * @version 1.6.1
 * @since 1.5.0
 */
public final class ConstructionSettings {
    /**
     * The registers that are going to be used to resolve the parameter types of the constructor.
     *
     * @since 1.5.0
     */
    private Register[] registers = {
            TypeRegister.getRegister(),
    };
    /**
     * The identifier objects that the {@link IdentifierProvider} or other key providers needs to use, to choose which
     * implementation it will use.
     *
     * @since 1.5.0
     */
    private Object[] identifiers = {};
    /**
     * Whether the annotation {@link ConstructorResolving} is needed on a constructor.
     *
     * @since 1.5.0
     */
    private boolean needAnnotation = true;

    /**
     * Get the registers that are going to be used to resolve the parameter types of the constructor.
     *
     * @return The registers that are going to be used to resolve the parameter types of the constructor.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public Register[] getRegisters() {
        return registers.clone();
    }

    /**
     * Set the registers that are going to be used to resolve the parameter types of the constructor. The given
     * register(s) cannot be empty, add at least one register.
     *
     * @param registers The registers that are going to be used to resolve the parameter types of the constructor.
     *
     * @throws IllegalArgumentException If the parameter registers has not been provided with at least one constructor
     * @since 1.5.0
     */
    public void setRegisters(Register @NotNull ... registers) {
        if (registers.length == 0) {
            throw new IllegalArgumentException("There need to be at least one register, it can not be empty.");
        }
        this.registers = registers;
    }

    /**
     * Get the identifier objects that the {@link IdentifierProvider} or other key providers needs to use, to choose
     * which implementation it will use.
     *
     * @return The identifier objects that the {@link IdentifierProvider} or other key providers needs to use
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public Object @NotNull [] getIdentifiers() {
        return identifiers.clone();
    }

    /**
     * Set the identifier objects that the {@link IdentifierProvider} or other key providers needs to use, to choose
     * which implementation it will use.
     *
     * @param identifiers The identifier objects that the {@link IdentifierProvider} or other key providers needs to use
     *
     * @since 1.5.0
     */
    public void setIdentifiers(Object... identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Check whether the annotation {@link ConstructorResolving} is needed on a constructor.
     *
     * @return If {@code true} the {@link ConstructorResolving} annotation is needed to find the constructor.
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public boolean needAnnotation() {
        return needAnnotation;
    }

    /**
     * Set whether the annotation {@link ConstructorResolving} is needed on a constructor.
     *
     * @param needAnnotation If {@code true} the {@link ConstructorResolving} annotation is needed to find the
     *                       constructor.
     *
     * @since 1.5.0
     */
    public void setNeedAnnotation(boolean needAnnotation) {
        this.needAnnotation = needAnnotation;
    }
}
