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

import nl.devoxist.typeresolver.TypeRegister;
import nl.devoxist.typeresolver.exception.RegisterException;
import nl.devoxist.typeresolver.functions.SerializableConsumer;
import nl.devoxist.typeresolver.providers.ObjectProvider;
import nl.devoxist.typeresolver.providers.ScopedProvider;
import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.providers.builders.IdentifiersBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Supplier;

public class TypeRegisterTests {

    @Test
    public void checkIfTypeIsRegistered() {
        TypeRegister.registerScoped(TestClass.class, TestClass::new);

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsRegistered2() {
        TypeRegister.register(TestClass.class, new TestClass());

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsRegistered3() {
        TypeRegister.register(new ObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsRegistered4() {
        TypeRegister.register(new ScopedProvider<>(TestClass.class, TestClass::new));

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsRegistered5() {
        TypeRegister.registerScoped(TestClass2.class, TestClass2::new);

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass2.class));

        TypeRegister.unregister(TestClass2.class);
    }

    @Test
    public void checkIfTypeIsRegistered6() {
        CustomType<TestCls> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestCls(1);
        TypeRegister.register(new CustomTypeProvider<>(TestCls.class, testClassCustomType));

        Assertions.assertTrue(TypeRegister.hasProvider(TestCls.class));

        TypeRegister.unregister(TestCls.class);
    }

    @Test
    public void checkIfTypeIsRegistered7() {
        TypeRegister.register(
                Exporter.class,
                (SerializableConsumer<IdentifiersBuilder<Exporter, Class<? extends Exporter>>>) (IdentifiersBuilder<Exporter, Class<? extends Exporter>> settings) -> settings
                        .addIdentifier(CarOneExporter.class, new CarOneExporter())
                        .addIdentifier(CarTwoExporter.class, new CarTwoExporter())
        );

        Assertions.assertTrue(TypeRegister.hasProvider(Exporter.class));

        TypeRegister.unregister(Exporter.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered() {
        TypeRegister.registerScoped(TestClass.class, TestClass::new);

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass2.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered2() {
        TypeRegister.register(TestClass.class, new TestClass());

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass2.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered3() {
        TypeRegister.register(new ObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass2.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered4() {
        TypeRegister.register(new ScopedProvider<>(TestClass.class, TestClass::new));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass2.class));

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered5() {
        TypeRegister.registerScoped(TestClass2.class, TestClass2::new);

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));

        TypeRegister.unregister(TestClass2.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered6() {
        CustomType<TestCls> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestCls(1);
        TypeRegister.register(new CustomTypeProvider<>(TestCls.class, testClassCustomType));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));

        TypeRegister.unregister(TestCls.class);
    }

    @Test
    public void checkIfTypeIsNotRegistered7() {
        TypeRegister.register(
                TestClass.class,
                (SerializableConsumer<IdentifiersBuilder<TestClass, Class<TestClass>>>) (IdentifiersBuilder<TestClass, Class<TestClass>> settings) -> settings
                        .addIdentifier(TestClass.class, new TestClass())
        );

        Assertions.assertFalse(TypeRegister.hasProvider(Exporter.class));

        TypeRegister.unregister(TestClass.class);
    }


    @Test
    public void checkIfGottenTypeSameType1() {
        TypeRegister.registerScoped(TestClass.class, TestClass::new);

        Assertions.assertEquals(
                TestClass.class,
                ((Supplier<?>) TypeRegister.getProviderByType(TestClass.class)).get().getClass()
        );

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfGottenTypeSameType2() {
        TypeRegister.register(TestClass2.class, new TestClass2());

        Assertions.assertEquals(TestClass2.class, TypeRegister.getInitProvider(TestClass2.class).getClass());

        TypeRegister.unregister(TestClass2.class);
    }

    @Test
    public void checkIfGottenTypeSameType3() {
        TypeRegister.register(TestClass2.class, new TestClass2());

        Assertions.assertEquals(TestClass2.class, TypeRegister.getInitProvider(TestClass2.class).getClass());

        TypeRegister.unregister(TestClass2.class);
    }

    @Test
    public void checkIfGottenTypeSameType4() {
        TypeRegister.registerScoped(TestClass.class, TestClass::new);

        Assertions.assertEquals(TestClass.class, TypeRegister.getInitProvider(TestClass.class).getClass());

        TypeRegister.unregister(TestClass.class);
    }

    @Test
    public void checkIfGottenTypeSameType5() {
        CustomType<TestCls> testClassCustomType = new CustomType<>();
        testClassCustomType.type = new TestCls(1);
        TypeRegister.register(new CustomTypeProvider<>(TestCls.class, testClassCustomType));

        Assertions.assertEquals(testClassCustomType.type, TypeRegister.getInitProvider(TestCls.class));

        TypeRegister.unregister(TestCls.class);
    }

    @Test
    public void checkIfGottenTypeSameType6() {
        CarOneExporter carOneExporter = new CarOneExporter();
        CarTwoExporter carTwoExporter = new CarTwoExporter();
        TypeRegister.register(
                Exporter.class,
                (SerializableConsumer<IdentifiersBuilder<Exporter, Class<? extends Exporter>>>) (IdentifiersBuilder<Exporter, Class<? extends Exporter>> settings) -> settings
                        .addIdentifier(CarOneExporter.class, carOneExporter)
                        .addIdentifier(CarTwoExporter.class, carTwoExporter)
        );

        Assertions.assertEquals(
                carOneExporter,
                TypeRegister.getInitProvider(
                        Exporter.class,
                        initProviderSettings -> initProviderSettings.setIdentifiers(CarOneExporter.class)
                )
        );

        Assertions.assertEquals(
                carTwoExporter,
                TypeRegister.getInitProvider(
                        Exporter.class,
                        initProviderSettings -> initProviderSettings.setIdentifiers(CarTwoExporter.class)
                )
        );

        TypeRegister.unregister(Exporter.class);
    }

    @Test
    public void checkIfUnregisterType() {
        TypeRegister.registerScoped(TestClass.class, TestClass::new);
        TypeRegister.unregister(TestClass.class);

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType2() {
        TypeRegister.register(TestClass.class, new TestClass());
        TypeRegister.unregister(TestClass.class);

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType3() {
        TypeRegister.register(new ObjectProvider<>(TestClass.class, new TestClass()));
        TypeRegister.unregister(TestClass.class);

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType4() {
        TypeRegister.register(new ScopedProvider<>(TestClass.class, TestClass::new));
        TypeRegister.unregister(TestClass.class);

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType5() {
        TypeRegister.registerScoped(TestClass.class, TestClass::new);
        TypeRegister.unregister(new ScopedProvider<>(TestClass.class, TestClass::new));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType6() {
        TypeRegister.register(TestClass.class, new TestClass());
        TypeRegister.unregister(new ObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType7() {
        TypeRegister.register(new ObjectProvider<>(TestClass.class, new TestClass()));
        TypeRegister.unregister(new ObjectProvider<>(TestClass.class, new TestClass()));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterType8() {
        TypeRegister.register(new ScopedProvider<>(TestClass.class, TestClass::new));
        TypeRegister.unregister(new ScopedProvider<>(TestClass.class, TestClass::new));

        Assertions.assertFalse(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfUnregisterFail() {
        Assertions.assertThrows(
                RegisterException.class,
                () -> TypeRegister.unregister(TestClass.class)
        );
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
