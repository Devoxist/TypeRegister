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

import nl.devoxist.typeresolver.constructor.ConstructorResolver;
import nl.devoxist.typeresolver.constructor.ConstructorResolving;
import nl.devoxist.typeresolver.exception.ConstructorException;
import nl.devoxist.typeresolver.providers.TypeKeyProvider;
import nl.devoxist.typeresolver.register.Register;

import java.lang.reflect.InvocationTargetException;

/**
 * {@link ConstructionSettingsBuilder} is an object that chain-edits the {@link ConstructionSettings}, when the
 * options are set call the {@link ConstructionSettingsBuilder#initClass()} to initialize the class.
 *
 * @param <T> The type that represents the constructed class.
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.5.0
 */
public class ConstructionSettingsBuilder<T> {
    /**
     * The settings which manipulate the pick order of the constructor and parameter injection types.
     *
     * @see ConstructionSettings
     * @since 1.5.0
     */
    private final ConstructionSettings constructionSettings = new ConstructionSettings();
    /**
     * The class that represents the class that is going to be constructed.
     *
     * @since 1.5.0
     */
    private final Class<T> constructionCls;

    /**
     * Construct a {@link ConstructionSettingsBuilder} that will chain-edit the {@link ConstructionSettings}.
     *
     * @param constructionCls The class that represents the class that is going to be constructed.
     *
     * @since 1.5.0
     */
    public ConstructionSettingsBuilder(Class<T> constructionCls) {
        this.constructionCls = constructionCls;
    }

    /**
     * Set the registers that are going to be used to resolve the parameter types of the constructor. The given
     * register(s) cannot be empty, add at least one register.
     *
     * @param registers The registers that are going to be used to resolve the parameter types of the constructor.
     *
     * @return The builder of the {@link ConstructionSettings} to chain-edit the {@link ConstructionSettings}, when the
     * options are set call the {@link ConstructionSettingsBuilder#initClass()} to initialize the class.
     *
     * @since 1.5.0
     */
    public ConstructionSettingsBuilder<T> setRegisters(Register... registers) {
        this.constructionSettings.setRegisters(registers);
        return this;
    }


    /**
     * Set whether the annotation {@link ConstructorResolving} is needed on a constructor.
     *
     * @param needAnnotation If {@code true} the {@link ConstructorResolving} annotation is needed to find the
     *                       constructor.
     *
     * @return The builder of the {@link ConstructionSettings} to chain-edit the {@link ConstructionSettings}, when the
     * options are set call the {@link ConstructionSettingsBuilder#initClass()} to initialize the class.
     *
     * @since 1.5.0
     */
    public ConstructionSettingsBuilder<T> setNeedAnnotation(boolean needAnnotation) {
        this.constructionSettings.setNeedAnnotation(needAnnotation);
        return this;
    }


    /**
     * Set the identifier objects that the {@link TypeKeyProvider} or other key providers needs to use, to choose which
     * implementation it will use.
     *
     * @param identifiers The identifier objects that the {@link TypeKeyProvider} or other key providers needs to use
     *
     * @return The builder of the {@link ConstructionSettings} to chain-edit the {@link ConstructionSettings}, when the
     * options are set call the {@link ConstructionSettingsBuilder#initClass()} to initialize the class.
     *
     * @since 1.5.0
     */
    public ConstructionSettingsBuilder<T> setIdentifiers(Object... identifiers) {
        this.constructionSettings.setIdentifiers(identifiers);
        return this;
    }

    /**
     * Constructing the specified class, where the parameters have been resolved by a {@link Register}. This uses the
     * settings specified in this chain-edited object.
     *
     * @return The initialized class, where the parameters have been resolver by a {@link Register}.
     *
     * @throws ConstructorException      if there is no valid constructor, or if the construction class is an interface,
     *                                   enum or abstract class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws IllegalAccessException    if this Constructor object is enforcing Java language access control and the
     *                                   underlying constructor is inaccessible.
     * @since 1.5.0
     */
    public T initClass()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return ConstructorResolver.initClass(constructionCls, constructionSettings);
    }
}