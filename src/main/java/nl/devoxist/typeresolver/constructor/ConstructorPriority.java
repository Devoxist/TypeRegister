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

package nl.devoxist.typeresolver.constructor;


/**
 * The priority's of the constructors.
 * <p>
 * The highest priority of a constructor, from which the constructor parameters can be resolved, is used in the
 * {@link ConstructorResolver} process.
 *
 * @author Dev-Bjorn
 * @version 1.1
 * @since 1.0
 */
public enum ConstructorPriority {
    /**
     * The lowest priority, this constructor is used when nothing of the higher priority's can be used.
     */
    LOWEST,
    /**
     * The low priority, this constructor is used when nothing of the higher priority's can be used.
     */
    LOW,
    /**
     * The normal priority, this constructor is used when nothing of the higher priority's can be used.
     */
    NORMAL,
    /**
     * The high priority, this constructor is used when nothing of the higher priority's can be used.
     */
    HIGH,
    /**
     * The highest priority, this constructor is used, if the parameters can be resolved.
     */
    HIGHEST
}
