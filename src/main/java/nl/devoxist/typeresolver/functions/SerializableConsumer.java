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

package nl.devoxist.typeresolver.functions;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * {@link SerializableConsumer} is a subclass of the {@link Consumer}, which makes it possible to serialize a
 * {@link Consumer} and retrieve the type that has been used in the {@link Consumer}.
 *
 * @param <T> type of the {@link Consumer}
 *
 * @author Dev-Bjorn
 * @version 1.5.0
 * @since 1.5.0
 */
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {

    /**
     * Get the type of the {@link Consumer}.
     *
     * @return The type of the {@link Consumer}
     *
     * @since 1.5.0
     */
    @SuppressWarnings("unchecked")
    default Class<T> getConsumerCls() {
        try {
            Method writeReplace = this.getClass().getDeclaredMethod("writeReplace");

            writeReplace.setAccessible(true);

            SerializedLambda sl = (SerializedLambda) writeReplace.invoke(this);

            writeReplace.setAccessible(false);

            String classPath = sl.getInstantiatedMethodType();
            classPath = classPath.substring(2, classPath.length() - 3);
            classPath = classPath.replaceAll("/", ".");

            return (Class<T>) Class.forName(classPath);
        } catch (NoSuchMethodException | InvocationTargetException | ClassNotFoundException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
