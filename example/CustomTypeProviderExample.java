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

import nl.devoxist.typeresolver.TypeRegister;
import nl.devoxist.typeresolver.constructor.ConstructorResolver;
import nl.devoxist.typeresolver.constructor.ConstructorResolving;

import java.lang.reflect.InvocationTargetException;

public class CustomTypeProviderExample {

    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TypeRegister.register(new CustomTypeProvider<>(TestType.class, new CustomType<TestType>()));

        Example example = ConstructorResolver.initClass(Example.class);
    }


    public static class Example {

        @ConstructorResolving
        public Example(TestType type) {
            // Put here your stuff
        }
    }

    public static class CustomTypeProvider<T, P extends T> extends TypeProvider<T, CustomType<P>> {

        public CustomTypeProvider(Class<T> typeCls, CustomType<P> provider) {
            super(typeCls, provider);
        }

        @Override
        public T getInitProvider() {
            // Put here your stuff
        }

        // Put here your stuff
    }

    public static class CustomType<P> {
        // Put here your stuff
    }

    public static class TestType {
        // Put here your stuff
    }
}