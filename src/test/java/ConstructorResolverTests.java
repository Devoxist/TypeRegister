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
import nl.devoxist.typeresolver.constructor.ConstructorPriority;
import nl.devoxist.typeresolver.constructor.ConstructorResolver;
import nl.devoxist.typeresolver.constructor.ConstructorResolving;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.register.Register;
import nl.devoxist.typeresolver.register.RegisterPriority;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ConstructorResolverTests {

    @Test
    public void checkIfConstructionIsInitialized()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TypeRegister.register(TestClass.class, () -> new TestClass(1));

        Assertions.assertNotNull(ConstructorResolver.initClass(ConstructionClass.class).testClass());

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfConstructionIsCorrectInitialized()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TypeRegister.register(TestClass.class, () -> new TestClass(2));

        Assertions.assertEquals(
                TypeRegister.getInitProvider(TestClass.class).hashCode(),
                ConstructorResolver.initClass(ConstructionClass.class).testClass().hashCode()
        );

        TypeRegister.unregister(TestClass.class);
    }


    @Test
    public void checkIfConstructionIsCorrectInitialized2()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CustomType<TestClass> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestClass(1);
        TypeRegister.register(new CustomTypeProvider<>(TestClass.class, testClassCustomType));

        Assertions.assertEquals(
                testClassCustomType.type,
                ConstructorResolver.initClass(ConstructionClass.class).testClass()
        );

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfConstructionIsCorrectInitialized3()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CustomType<TestClass> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestClass(1);
        TypeRegister.register(new CustomTypeProvider<>(TestClass.class, testClassCustomType));
        ConstructionClass provider = new ConstructionClass(testClassCustomType.type);
        TypeRegister.register(ConstructionClass.class, provider);

        TestCls testCls = ConstructorResolver.initClass(TestCls.class);
        Assertions.assertEquals(testClassCustomType.type, testCls.testClass);
        Assertions.assertEquals(provider, testCls.constructionClass);

        TypeRegister.unregister(TestClass.class);
        TypeRegister.unregister(ConstructionClass.class);
    }

    @Test
    public void checkIfConstructionIsCorrectInitialized4()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CustomType<TestClass> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestClass(1);
        TypeRegister.register(new CustomTypeProvider<>(TestClass.class, testClassCustomType));

        TestCls testCls = ConstructorResolver.initClass(TestCls.class);
        Assertions.assertEquals(testClassCustomType.type, testCls.testClass);
        Assertions.assertNull(testCls.constructionClass);

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfConstructionIsCorrectInitialized5()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CustomType<TestClass> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestClass(1);
        TypeRegister.register(new CustomTypeProvider<>(TestClass.class, testClassCustomType));
        ConstructionClass provider = new ConstructionClass(testClassCustomType.type);
        TypeRegister.register(ConstructionClass.class, provider);

        TestCls2 testCls2 = ConstructorResolver.initClass(TestCls2.class);
        Assertions.assertEquals(testClassCustomType.type, testCls2.testClass);
        Assertions.assertNull(testCls2.constructionClass);

        TypeRegister.unregister(TestClass.class);
        TypeRegister.unregister(ConstructionClass.class);
    }

    @Test
    public void checkIfConstructionIsCorrectInitialized6()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Register register1 = new Register(RegisterPriority.HIGHEST);
        TestClass provider1 = new TestClass(2);
        register1.register(TestClass.class, provider1);
        Register register = new Register(register1);
        TestClass provider = new TestClass(1);
        register.register(TestClass.class, provider);

        TestCls2 testCls2 = ConstructorResolver.initClass(TestCls2.class, register);
        Assertions.assertEquals(provider1, testCls2.testClass);
        Assertions.assertEquals(provider1.i, testCls2.testClass.i);
        Assertions.assertNull(testCls2.constructionClass);
    }

    @Test
    public void checkIfConstructionIsCorrectInitialized7()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Register register1 = new Register(RegisterPriority.HIGHEST);
        TestClass provider1 = new TestClass(2);
        register1.register(TestClass.class, provider1);
        Register register = new Register(register1);
        TestClass provider = new TestClass(1);
        register.register(TestClass.class, provider);
        TestClass provider2 = new TestClass(3);
        TypeRegister.register(TestClass.class, provider2);

        TestCls2 testCls2 = ConstructorResolver.initClass(TestCls2.class);
        Assertions.assertEquals(provider2, testCls2.testClass);
        Assertions.assertEquals(provider2.i, testCls2.testClass.i);
        Assertions.assertNull(testCls2.constructionClass);
    }

    public static class TestClass {
        public int i;

        public TestClass(int i) {
            this.i = i;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i);
        }
    }

    public record ConstructionClass(TestClass testClass) {
        @Contract(pure = true)
        @ConstructorResolving
        public ConstructionClass {
        }

    }

    public static final class TestCls {
        public final TestClass testClass;
        public ConstructionClass constructionClass;

        @ConstructorResolving(ConstructorPriority.LOWEST)
        public TestCls(TestClass testClass) {
            this.testClass = testClass;
        }

        @ConstructorResolving(ConstructorPriority.HIGHEST)
        public TestCls(TestClass testClass, ConstructionClass constructionClass) {
            this.testClass = testClass;
            this.constructionClass = constructionClass;
        }

        @Override
        public int hashCode() {
            return Objects.hash(testClass, constructionClass);
        }

    }

    public static final class TestCls2 {
        public final TestClass testClass;
        public ConstructionClass constructionClass;

        @ConstructorResolving(ConstructorPriority.HIGHEST)
        public TestCls2(TestClass testClass) {
            this.testClass = testClass;
        }

        @ConstructorResolving(ConstructorPriority.LOWEST)
        public TestCls2(TestClass testClass, ConstructionClass constructionClass) {
            this.testClass = testClass;
            this.constructionClass = constructionClass;
        }

        @Override
        public int hashCode() {
            return Objects.hash(testClass, constructionClass);
        }

    }

    public static class CustomTypeProvider<T, P extends T> extends TypeProvider<T, CustomType<P>> {

        public CustomTypeProvider(Class<T> type, CustomType<P> provider) {
            super(type, provider);
        }

        @Override
        public T getInitProvider() {
            return getProvider().type;
        }

    }

    public static class CustomType<P> {
        private P type;
    }

}
