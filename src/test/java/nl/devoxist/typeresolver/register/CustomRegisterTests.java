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
import nl.devoxist.typeresolver.providers.TypeObjectProvider;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.providers.TypeSupplierProvider;
import nl.devoxist.typeresolver.providers.builders.IdentifiersBuilder;
import nl.devoxist.typeresolver.providers.builders.TypeProviderBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Supplier;

public class CustomRegisterTests {


    @Test
    public void checkIfTypeIsRegistered() {
        Register register = new Register();
        register.register(TestClass.class, TestClass::new);

        Assertions.assertTrue(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfTypeIsRegistered2() {
        Register register = new Register();
        register.register(TestClass.class, new TestClass());

        Assertions.assertTrue(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfTypeIsRegistered3() {
        Register register = new Register();
        register.register(new TypeObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertTrue(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfTypeIsRegistered4() {
        Register register = new Register();
        register.register(new TypeSupplierProvider<>(TestClass.class, TestClass::new));

        Assertions.assertTrue(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfTypeIsRegistered5() {
        Register register = new Register();
        register.register(TestClass2.class, TestClass2::new);

        Assertions.assertTrue(register.hasProvider(TestClass2.class));
    }

    @Test
    public void checkIfTypeIsRegistered6() {
        Register register = new Register();
        CustomType<TestCls> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestCls(1);
        register.register(new CustomTypeProvider<>(TestCls.class, testClassCustomType));

        Assertions.assertTrue(register.hasProvider(TestCls.class));
    }

    @Test
    public void checkIfTypeIsRegistered7() {
        Register register = new Register();

        register.register(
                Exporter.class,
                (IdentifiersBuilder<Exporter, Class<? extends Exporter>> settings) -> settings.addIdentifier(
                        CarOneExporter.class,
                        new CarOneExporter()
                ).addIdentifier(CarTwoExporter.class, new CarTwoExporter())
        );

        Assertions.assertTrue(register.hasProvider(Exporter.class));

    }

    @Test
    public void registerFailTest() {
        Register register = new Register();

        Assertions.assertThrows(
                RegisterException.class,
                () -> register.register(
                        Exporter.class,
                        (TypeProviderBuilder<Exporter> settings) -> settings.getClass()
                )
        );

    }

    @Test
    public void registerFailTest2() {
        Register register = new Register();

        Assertions.assertThrows(RegisterException.class, () -> register.register(Exporter.class, new TestCls(1)));
    }

    @Test
    public void checkIfTypeIsNotRegistered() {
        Register register = new Register();
        register.register(TestClass.class, TestClass::new);

        Assertions.assertFalse(register.hasProvider(TestClass2.class));
    }

    @Test
    public void checkIfTypeIsNotRegistered2() {
        Register register = new Register();
        register.register(TestClass.class, new TestClass());

        Assertions.assertFalse(register.hasProvider(TestClass2.class));

    }

    @Test
    public void checkIfTypeIsNotRegistered3() {
        Register register = new Register();
        register.register(new TypeObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertFalse(register.hasProvider(TestClass2.class));
    }

    @Test
    public void checkIfTypeIsNotRegistered4() {
        Register register = new Register();
        register.register(new TypeSupplierProvider<>(TestClass.class, TestClass::new));

        Assertions.assertFalse(register.hasProvider(TestClass2.class));
    }

    @Test
    public void checkIfTypeIsNotRegistered5() {
        Register register = new Register();
        register.register(TestClass2.class, TestClass2::new);

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfTypeIsNotRegistered6() {
        Register register = new Register();
        CustomType<TestCls> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestCls(1);
        register.register(new CustomTypeProvider<>(TestCls.class, testClassCustomType));

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfTypeIsNotRegistered7() {

        Register register = new Register();

        register.register(
                TestClass.class,
                (IdentifiersBuilder<TestClass, Class<TestClass>> settings) ->
                        settings.addIdentifier(TestClass.class, new TestClass())
        );

        Assertions.assertFalse(register.hasProvider(Exporter.class));
    }


    @Test
    public void checkIfGottenTypeSameType1() {
        Register register = new Register();
        register.register(TestClass.class, TestClass::new);

        Assertions.assertEquals(
                TestClass.class,
                ((Supplier<?>) register.getProviderByType(TestClass.class)).get().getClass()
        );
    }

    @Test
    public void checkIfGottenTypeSameType2() {
        Register register = new Register();
        register.register(TestClass2.class, new TestClass2());

        Assertions.assertEquals(TestClass2.class, register.getInitProvider(TestClass2.class).getClass());
    }

    @Test
    public void checkIfGottenTypeSameType3() {
        Register register = new Register();
        register.register(TestClass2.class, new TestClass2());

        Assertions.assertEquals(TestClass2.class, register.getInitProvider(TestClass2.class).getClass());
    }

    @Test
    public void checkIfGottenTypeSameType4() {
        Register register = new Register();
        register.register(TestClass.class, TestClass::new);

        Assertions.assertEquals(TestClass.class, register.getInitProvider(TestClass.class).getClass());
    }

    @Test
    public void checkIfGottenTypeSameType7() {
        Register register = new Register();
        register.register(Supplier.class, (Supplier<TestClass>) TestClass::new);

        Assertions.assertTrue(register.getInitProvider(Supplier.class) instanceof Supplier<?>);
    }

    @Test
    public void checkIfGottenTypeSameType5() {
        Register register = new Register();
        CustomType<TestCls> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestCls(1);
        register.register(new CustomTypeProvider<>(TestCls.class, testClassCustomType));

        Assertions.assertEquals(testClassCustomType.type, register.getInitProvider(TestCls.class));

    }


    @Test
    public void checkIfGottenTypeSameType6() {
        Register register = new Register();
        CarOneExporter carOneExporter = new CarOneExporter();
        CarTwoExporter carTwoExporter = new CarTwoExporter();

        register.register(
                Exporter.class,
                (IdentifiersBuilder<Exporter, Class<? extends Exporter>> settings) -> settings
                        .addIdentifier(CarOneExporter.class, carOneExporter)
                        .addIdentifier(CarTwoExporter.class, carTwoExporter)
        );

        Assertions.assertEquals(carOneExporter, register.getInitProvider(
                Exporter.class,
                initProviderSettings -> initProviderSettings.setIdentifiers(CarOneExporter.class)
        ));

        Assertions.assertEquals(carTwoExporter, register.getInitProvider(
                Exporter.class,
                initProviderSettings -> initProviderSettings.setIdentifiers(CarTwoExporter.class)
        ));

    }

    @Test
    public void getTypeProviderTestFail() {
        Register register = new Register();
        Assertions.assertThrows(
                RegisterException.class,
                () -> ((Supplier<?>) register.getProviderByType(TestClass.class)).get()
        );
    }

    @Test
    public void checkIfUnregisterType() {
        Register register = new Register();
        register.register(TestClass.class, TestClass::new);
        register.unregister(TestClass.class);

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType2() {
        Register register = new Register();
        register.register(TestClass.class, new TestClass());
        register.unregister(TestClass.class);

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType3() {
        Register register = new Register();
        register.register(new TypeObjectProvider<>(TestClass.class, new TestClass()));
        register.unregister(TestClass.class);

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType4() {
        Register register = new Register();
        register.register(new TypeSupplierProvider<>(TestClass.class, TestClass::new));
        register.unregister(TestClass.class);

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType5() {
        Register register = new Register();
        register.register(TestClass.class, TestClass::new);
        register.unregister(new TypeSupplierProvider<>(TestClass.class, TestClass::new));

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType6() {
        Register register = new Register();
        register.register(TestClass.class, new TestClass());
        register.unregister(new TypeObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType7() {
        Register register = new Register();
        register.register(new TypeObjectProvider<>(TestClass.class, new TestClass()));
        register.unregister(new TypeObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType8() {
        Register register = new Register();
        register.register(new TypeSupplierProvider<>(TestClass.class, TestClass::new));
        register.unregister(new TypeSupplierProvider<>(TestClass.class, TestClass::new));

        Assertions.assertFalse(register.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterFail() {
        Register register = new Register();
        Assertions.assertThrows(RegisterException.class, () -> register.unregister(TypeRegisterTests.TestClass.class));
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

    public static class CustomTypeProvider<T, P extends T> extends TypeProvider<T, CustomType<P>> {

        public CustomTypeProvider(Class<T> typeCls, CustomType<P> provider) {
            super(typeCls, provider);
        }

        @Override
        public T getInitProvider() {
            return getProvider().type;
        }

    }

    public static class CustomType<P> {
        private P type;
    }

    public interface Exporter {

    }

    public static class CarTwoExporter implements Exporter {

    }

    public static class CarOneExporter implements Exporter {
    }
}
