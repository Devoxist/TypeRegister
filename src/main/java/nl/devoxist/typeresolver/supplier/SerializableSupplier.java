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

package nl.devoxist.typeresolver.supplier;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * {@link SerializableSupplier} is a subclass of the {@link Supplier}. This makes it possible to serialize a
 * {@link Supplier}.
 *
 * @param <T> type of the {@link Supplier}
 *
 * @author Dev-Bjorn
 * @version 1.1
 * @apiNote This can be used to retrieve the type of the {@link Supplier}.
 * @since 1.1
 */
@FunctionalInterface
public interface SerializableSupplier<T> extends Serializable, Supplier<T> {

    /**
     * Get the type of the {@link Supplier}.
     *
     * @return The type of the {@link Supplier}
     *
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    default Class<T> getSupplierClass() {
        try {
            Method writeReplace = this.getClass().getDeclaredMethod("writeReplace");

            writeReplace.setAccessible(true);

            SerializedLambda sl = (SerializedLambda) writeReplace.invoke(this);

            writeReplace.setAccessible(false);

            String classPath = sl.getInstantiatedMethodType();
            classPath = classPath.substring(3, classPath.length() - 1);
            classPath = classPath.replaceAll("/", ".");

            return (Class<T>) Class.forName(classPath);
        } catch (NoSuchMethodException | InvocationTargetException | ClassNotFoundException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}