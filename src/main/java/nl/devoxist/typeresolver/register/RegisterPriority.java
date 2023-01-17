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


/**
 * The priority's of the register.
 * <p>
 * The higher the priority the sooner the register is searched through or checked if a type exists.
 *
 * @author Dev-Bjorn
 * @version 1.3.0
 * @since 1.3.0
 */
public enum RegisterPriority {
    /**
     * The lowest priority, used when the no objects were found in the registers with higher priorities.
     *
     * @since 1.3.0
     */
    LOWEST,
    /**
     * The low priority, used when the no objects were found in the registers with higher priorities.
     *
     * @since 1.3.0
     */
    LOW,
    /**
     * The normal priority, used when the no objects were found in the registers with higher priorities.
     *
     * @since 1.3.0
     */
    NORMAL,
    /**
     * The high priority, used when the no objects were found in the registers with higher priorities.
     *
     * @since 1.3.0
     */
    HIGH,
    /**
     * The highest priority, used when the no objects were found in the registers with higher priorities.
     *
     * @since 1.3.0
     */
    HIGHEST
}
