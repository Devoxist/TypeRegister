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

import nl.devoxist.typeresolver.exception.RegisterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Supplier;

public class CombinedCustomRegisterTests {

    @Test
    public void checkIfTypeIsRegistered() {
        Register register1 = new Register(RegisterPriority.HIGHEST);
        register1.register(TestClass2.class, new TestClass2());
        Register register = new Register(register1);
        register.registerScoped(TestClass.class, TestClass::new);

        Assertions.assertFalse(register.hasProvider(TestClass2.class));
        Assertions.assertTrue(register.hasProvider(TestClass.class));
        Assertions.assertTrue(register.hasProvider(TestClass.class, true));
        Assertions.assertTrue(register.hasProvider(TestClass.class, true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkIfGottenTypeSameType1() {
        Register register1 = new Register(RegisterPriority.HIGHEST);
        TestCls provider1 = new TestCls(1);
        register1.register(TestCls.class, provider1);
        Register register = new Register(register1);
        register.registerScoped(TestCls.class, () -> new TestCls(2));

        Assertions.assertEquals(2, ((Supplier<TestCls>) register.getProviderByType(TestCls.class)).get().i);
        Assertions.assertEquals(1, ((TestCls) register.getProviderByType(TestCls.class, true)).i);
    }

    @Test
    public void checkIfGottenTypeSameType2() {
        Register register1 = new Register(RegisterPriority.HIGHEST);
        TestCls provider1 = new TestCls(1);
        register1.register(TestCls.class, provider1);
        Register register = new Register(register1);
        TestCls provider = new TestCls(2);
        register.register(TestCls.class, provider);

        Assertions.assertEquals(2, register.getInitProvider(TestCls.class).i);
        Assertions.assertNotEquals(1, register.getInitProvider(TestCls.class).i);
        Assertions.assertEquals(1, register.getInitProvider(TestCls.class, true).i);
        Assertions.assertNotEquals(2, register.getInitProvider(TestCls.class, true).i);

        Assertions.assertEquals(provider, register.getInitProvider(TestCls.class));
        Assertions.assertNotEquals(provider1, register.getInitProvider(TestCls.class));
        Assertions.assertEquals(provider1, register.getInitProvider(TestCls.class, true));
        Assertions.assertNotEquals(provider, register.getInitProvider(TestCls.class, true));
    }

    @Test
    public void checkIfGottenTypeSameType3() {
        Register register2 = new Register(RegisterPriority.HIGH);
        TestClass provider2 = new TestClass();
        register2.register(TestClass.class, provider2);
        Register register1 = new Register(RegisterPriority.HIGHEST, register2);
        TestCls provider1 = new TestCls(1);
        register1.register(TestCls.class, provider1);
        Register register = new Register(register1);
        TestCls provider = new TestCls(2);
        register.register(TestCls.class, provider);

        Assertions.assertEquals(1, register.getInitProvider(TestCls.class, true).i);
        Assertions.assertEquals(2, register.getInitProvider(TestCls.class).i);
        Assertions.assertEquals(provider, register.getInitProvider(TestCls.class));
        Assertions.assertEquals(provider2, register.getInitProvider(TestClass.class, true));
        Assertions.assertEquals(provider1, register.getInitProvider(TestCls.class, true));
        Assertions.assertThrowsExactly(RegisterException.class, () -> register.getInitProvider(TestClass.class));
        Assertions.assertNotEquals(1, register.getInitProvider(TestCls.class).i);
        Assertions.assertNotEquals(2, register.getInitProvider(TestCls.class, true).i);
        Assertions.assertNotEquals(provider1, register.getInitProvider(TestCls.class));
        Assertions.assertNotEquals(provider, register.getInitProvider(TestCls.class, true));
    }

    @Test
    public void checkIfGottenTypeSameType4() {
        Register register2 = new Register(RegisterPriority.HIGH);
        TestClass provider2 = new TestClass();
        register2.register(TestClass.class, provider2);
        Register register1 = new Register(RegisterPriority.HIGHEST, register2);
        TestCls provider1 = new TestCls(1);
        register1.register(TestCls.class, provider1);
        Register register = new Register(register1);
        TestCls provider = new TestCls(2);
        register.register(TestCls.class, provider);

        Assertions.assertEquals(
                1,
                register.getInitProvider(
                        TestCls.class,
                        initProviderSettings -> initProviderSettings.useAllRegisters(true)
                ).i
        );
        Assertions.assertEquals(2, register.getInitProvider(TestCls.class).i);
        Assertions.assertEquals(provider, register.getInitProvider(TestCls.class));
        Assertions.assertEquals(
                provider2,
                register.getInitProvider(
                        TestClass.class,
                        initProviderSettings -> initProviderSettings.useAllRegisters(true)
                )
        );
        Assertions.assertEquals(
                provider1,
                register.getInitProvider(
                        TestCls.class,
                        initProviderSettings -> initProviderSettings.useAllRegisters(true)
                )
        );
        Assertions.assertThrowsExactly(RegisterException.class, () -> register.getInitProvider(TestClass.class));
        Assertions.assertNotEquals(1, register.getInitProvider(TestCls.class).i);
        Assertions.assertNotEquals(
                2,
                register.getInitProvider(
                        TestCls.class,
                        initProviderSettings -> initProviderSettings.useAllRegisters(true)
                ).i
        );
        Assertions.assertNotEquals(provider1, register.getInitProvider(TestCls.class));
        Assertions.assertNotEquals(
                provider,
                register.getInitProvider(
                        TestCls.class,
                        initProviderSettings -> initProviderSettings.useAllRegisters(true)
                )
        );
    }

    @Test
    public void checkIfHasMultiple1() {
        Register register = new Register();
        Register register2 = new Register(register);
        Register register1 = new Register(register2);

        Assertions.assertEquals(3, register1.getRegistries().size());
        Assertions.assertTrue(register1.getRegistries().contains(register));
        Assertions.assertTrue(register1.getRegistries().contains(register1));
        Assertions.assertTrue(register1.getRegistries().contains(register2));
    }

    @Test
    public void checkIfHasMultiple2() {
        Register register = new Register();
        TestClass provider = new TestClass();
        register.register(TestClass.class, provider);
        Register register2 = new Register(register);
        TestCls provider2 = new TestCls(2);
        register2.register(TestCls.class, provider2);
        Register register1 = new Register(register2);
        TestCls provider1 = new TestCls(1);
        register1.register(TestCls.class, provider1);

        Assertions.assertEquals(3, register1.getRegistries().size());
        Assertions.assertTrue(register1.getRegistries().contains(register));
        Assertions.assertTrue(register1.getRegistries().contains(register1));
        Assertions.assertTrue(register1.getRegistries().contains(register2));
    }

    @Test
    public void checkIfHasMultiple3() {
        Register register = new Register();
        Register register2 = new Register();
        Register register1 = new Register(register, register2);

        Assertions.assertEquals(3, register1.getRegistries().size());
        Assertions.assertTrue(register1.getRegistries().contains(register));
        Assertions.assertTrue(register1.getRegistries().contains(register1));
        Assertions.assertTrue(register1.getRegistries().contains(register2));
    }

    @Test
    public void checkIfUnregisterType() {
        Register register1 = new Register(RegisterPriority.HIGHEST);
        TestClass provider1 = new TestClass();
        register1.register(TestClass.class, provider1);
        Register register = new Register(register1);
        TestClass provider = new TestClass();
        register.register(TestClass.class, provider);

        register.unregister(TestClass.class);

        Assertions.assertFalse(register.hasProvider(TestClass.class));
        Assertions.assertTrue(register.hasProvider(TestClass.class, true));
    }


    public static class TestClass {

    }

    public static class TestClass2 {

    }

    public static class TestCls {
        public int i;

        public TestCls(int i) {
            this.i = i;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i);
        }
    }
}
