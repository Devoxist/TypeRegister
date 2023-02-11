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

import nl.devoxist.typeresolver.register.Register;
import nl.devoxist.typeresolver.register.RegisterPriority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SerializableSupplierTest {

    @Test
    public void getCorrectClass() {
        SerializableSupplier<Register> supplier = Register::new;

        Assertions.assertEquals(Register.class, supplier.getSupplierClass());
    }

    @Test
    public void getCorrectClassWithParam() {
        SerializableSupplier<Register> supplier = () -> new Register(RegisterPriority.HIGH);

        Assertions.assertEquals(Register.class, supplier.getSupplierClass());
    }

    @Test
    public void getCorrectClassWithGeneric() {
        SerializableSupplier<Map<?, ?>> supplier = HashMap::new;

        Assertions.assertEquals(Map.class, supplier.getSupplierClass());
    }
}
