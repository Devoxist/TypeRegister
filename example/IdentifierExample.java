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

public class IdentifierExample {

    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Register register = new Register();

        register.register(
                Car.class,
                (IdentifiersBuilder<Car, Cars> settings) -> settings
                        .addIdentifier(Cars.ONE, new CarOne())
                        .addIdentifier(Cars.TWO, new CarTwo())
        );

        CarExporter carExporter = ConstructorResolver.constructClass(CarExporter.class)
                .setNeedAnnotation(false)
                .setIdentifiers(Cars.ONE)
                .setRegisters(register)
                .initClass();

        // DO YOUR STUFF WITH CAR EXPORTER
    }

    public enum Cars {
        ONE,
        TWO
    }

    public record CarExporter(Car car) {
        // PUT YOUR STUFF HERE
    }

    public interface Car {
        // PUT YOUR STUFF HERE
    }

    public static class CarOne implements Car {
        // PUT YOUR STUFF HERE
    }

    public static class CarTwo implements Car {
        // PUT YOUR STUFF HERE
    }
}