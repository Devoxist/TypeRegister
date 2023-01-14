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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

public class TypeRegisterTests {

    @Test
    public void checkIfTypeIsRegistered() {
        TypeRegister.register(TestClass.class, TestClass::new);

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass.class));
    }

    @Test
    public void checkIfGottenTypeSameType1() {
        TypeRegister.register(TestClass.class, TestClass::new);

        Assertions.assertEquals(TypeRegister.getProvider(TestClass.class).get().getClass(), TestClass.class);
        Assertions.assertEquals(
                ((Supplier<?>) TypeRegister.getProviderByType(TestClass.class)).get().getClass(),
                TestClass.class
        );
    }

    @Test
    public void checkIfGottenTypeSameType2() {
        TypeRegister.register(TestClass2.class, new TestClass2());

        Assertions.assertEquals(TypeRegister.getProviderByType(TestClass2.class).getClass(), TestClass2.class);
    }

    @Test
    public void checkIfGottenTypeSameType3() {
        TypeRegister.register(TestClass2.class, new TestClass2());

        Assertions.assertEquals(TypeRegister.getInitProvider(TestClass2.class).getClass(), TestClass2.class);
    }

    @Test
    public void checkIfGottenTypeSameType4() {
        TypeRegister.register(TestClass.class, TestClass::new);

        Assertions.assertEquals(TypeRegister.getInitProvider(TestClass.class).getClass(), TestClass.class);
    }

    @Test
    public void checkIfAllTest() {
        TypeRegister.register(TestClass2.class, new TestClass2());
        TypeRegister.register(TestClass.class, TestClass::new);

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass.class));

        Assertions.assertEquals(TypeRegister.getProvider(TestClass.class).get().getClass(), TestClass.class);
        Assertions.assertEquals(
                ((Supplier<?>) TypeRegister.getProviderByType(TestClass.class)).get().getClass(),
                TestClass.class
        );

        Assertions.assertTrue(TypeRegister.hasProvider(TestClass2.class));

        Assertions.assertEquals(TypeRegister.getProviderByType(TestClass2.class).getClass(), TestClass2.class);
    }


    public static class TestClass {

    }

    public static class TestClass2 {

    }

}
