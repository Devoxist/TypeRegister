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

package nl.devoxist.typeresolver.collection;

import nl.devoxist.typeresolver.providers.TypeProvider;
import nl.devoxist.typeresolver.register.Register;
import nl.devoxist.typeresolver.register.RegisterPriority;
import nl.devoxist.typeresolver.register.RegisterSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class RegisterSearchTests {

    @Test
    public void checkIfTypeIsRegistered() {
        Register register = new Register(RegisterPriority.HIGHEST);
        register.register(TestClass2.class, new TestClass2());
        Register register1 = new Register();
        register1.register(TestClass2.class, new TestClass2());

        final Set<Register> registers = new TreeSet<>(Comparator.comparing(Register::getPriority).reversed());
        registers.add(register);
        registers.add(register1);

        TypeProvider<TestClass2, ?> typeProvider = RegisterSearch.searchRegisters(registers, TestClass2.class);

        Assertions.assertNotNull(typeProvider);
    }

    @Test
    public void checkIfTypeIsSame() {
        Register register = new Register(RegisterPriority.HIGHEST);
        TestClass2 provider = new TestClass2();
        register.register(TestClass2.class, provider);
        Register register1 = new Register();
        TestClass2 provider1 = new TestClass2();
        register1.register(TestClass2.class, provider1);

        final Set<Register> registers = new TreeSet<>(Comparator.comparing(Register::getPriority).reversed());
        registers.add(register);
        registers.add(register1);

        TypeProvider<TestClass2, ?> typeProvider = RegisterSearch.searchRegisters(registers, TestClass2.class);

        Assertions.assertEquals(provider, typeProvider.getInitProvider());
        Assertions.assertNotEquals(provider1, typeProvider.getInitProvider());
    }

    @Test
    public void checkIfTypeIsSame2() {
        Register register = new Register(RegisterPriority.HIGHEST);
        TestClass provider = new TestClass();
        register.register(TestClass.class, provider);
        Register register1 = new Register();
        TestClass2 provider1 = new TestClass2();
        register1.register(TestClass2.class, provider1);

        final Set<Register> registers = new TreeSet<>(Comparator.comparing(Register::getPriority).reversed());
        registers.add(register);
        registers.add(register1);

        TypeProvider<TestClass2, ?> typeProvider = RegisterSearch.searchRegisters(registers, TestClass2.class);

        Assertions.assertEquals(provider1, typeProvider.getInitProvider());
    }

    public static class TestClass {

    }

    public static class TestClass2 {

    }
}
