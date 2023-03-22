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

package nl.devoxist.typeresolver.typeproviders;

import nl.devoxist.typeresolver.exception.ProviderException;
import nl.devoxist.typeresolver.providers.TypeKeyProvider;
import nl.devoxist.typeresolver.providers.builders.IdentifiersBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeKeyProviderTest {

    @Test
    public void getInitProviderTest() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();
        CarOneExporter carOneExporter = new CarOneExporter();
        CarTwoExporter carTwoExporter = new CarTwoExporter();
        identifiersBuilder.addIdentifier(CarOneExporter.class, carOneExporter);
        identifiersBuilder.addIdentifier(CarTwoExporter.class, carTwoExporter);

        TypeKeyProvider<Exporter, Class<? extends Exporter>> typeKeyProvider =
                identifiersBuilder.buildProvider(Exporter.class);

        typeKeyProvider = typeKeyProvider.applyIdentifiers(CarOneExporter.class);

        Assertions.assertEquals(carOneExporter, typeKeyProvider.getInitProvider());

        typeKeyProvider = typeKeyProvider.applyIdentifiers(CarTwoExporter.class);

        Assertions.assertEquals(carTwoExporter, typeKeyProvider.getInitProvider());
    }

    @Test
    public void getInitProviderTest2() {
        IdentifiersBuilder<Exporter, Exporters> identifiersBuilder = new IdentifiersBuilder<>();
        CarOneExporter carOneExporter = new CarOneExporter();
        CarTwoExporter carTwoExporter = new CarTwoExporter();
        identifiersBuilder.addIdentifier(Exporters.CAR_ONE, carOneExporter);
        identifiersBuilder.addIdentifier(Exporters.CAR_TWO, carTwoExporter);

        TypeKeyProvider<Exporter, Exporters> typeKeyProvider = identifiersBuilder.buildProvider(Exporter.class);

        typeKeyProvider = typeKeyProvider.applyIdentifiers(Exporters.CAR_ONE);

        Assertions.assertEquals(carOneExporter, typeKeyProvider.getInitProvider());

        typeKeyProvider = typeKeyProvider.applyIdentifiers(Exporters.CAR_TWO);

        Assertions.assertEquals(carTwoExporter, typeKeyProvider.getInitProvider());
    }

    @Test
    public void getInitProviderTest3() {
        IdentifiersBuilder<Exporter, Exporters> identifiersBuilder = new IdentifiersBuilder<>();
        identifiersBuilder.addSupplierIdentifier(Exporters.CAR_ONE, CarOneExporter::new);
        identifiersBuilder.addSupplierIdentifier(Exporters.CAR_TWO, CarTwoExporter::new);

        TypeKeyProvider<Exporter, Exporters> typeKeyProvider = identifiersBuilder.buildProvider(Exporter.class);

        typeKeyProvider = typeKeyProvider.applyIdentifiers(Exporters.CAR_ONE);

        Assertions.assertEquals(CarOneExporter.class, typeKeyProvider.getInitProvider().getClass());
        Assertions.assertNotEquals(typeKeyProvider.getInitProvider(), typeKeyProvider.getInitProvider());

        typeKeyProvider = typeKeyProvider.applyIdentifiers(Exporters.CAR_TWO);

        Assertions.assertEquals(CarTwoExporter.class, typeKeyProvider.getInitProvider().getClass());
        Assertions.assertNotEquals(typeKeyProvider.getInitProvider(), typeKeyProvider.getInitProvider());
    }

    @Test
    public void getInitProviderTest4() {
        IdentifiersBuilder<Exporter, Exporters> identifiersBuilder = new IdentifiersBuilder<>();
        identifiersBuilder.addSupplierIdentifier(Exporters.CAR_ONE, CarOneExporter::new);
        CarTwoExporter carTwoExporter = new CarTwoExporter();
        identifiersBuilder.addIdentifier(Exporters.CAR_TWO, carTwoExporter);

        TypeKeyProvider<Exporter, Exporters> typeKeyProvider = identifiersBuilder.buildProvider(Exporter.class);

        typeKeyProvider = typeKeyProvider.applyIdentifiers(Exporters.CAR_ONE);

        Assertions.assertEquals(CarOneExporter.class, typeKeyProvider.getInitProvider().getClass());
        Assertions.assertNotEquals(typeKeyProvider.getInitProvider(), typeKeyProvider.getInitProvider());

        typeKeyProvider = typeKeyProvider.applyIdentifiers(Exporters.CAR_TWO);

        Assertions.assertEquals(carTwoExporter, typeKeyProvider.getInitProvider());
        Assertions.assertEquals(typeKeyProvider.getInitProvider(), typeKeyProvider.getInitProvider());
    }


    @Test
    public void getInitProviderFailTest() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();
        identifiersBuilder.addIdentifier(CarOneExporter.class, new CarOneExporter());
        identifiersBuilder.addIdentifier(CarTwoExporter.class, new CarTwoExporter());

        TypeKeyProvider<Exporter, Class<? extends Exporter>> typeKeyProvider =
                identifiersBuilder.buildProvider(Exporter.class);

        Assertions.assertThrows(ProviderException.class, typeKeyProvider::getInitProvider);
    }

    @Test
    public void getInitProviderFailTest2() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();
        CarOneExporter carOneExporter = new CarOneExporter();
        identifiersBuilder.addIdentifier(CarOneExporter.class, carOneExporter);

        TypeKeyProvider<Exporter, Class<? extends Exporter>> typeKeyProvider =
                identifiersBuilder.buildProvider(Exporter.class);

        typeKeyProvider = typeKeyProvider.applyIdentifiers(CarOneExporter.class);

        Assertions.assertEquals(carOneExporter, typeKeyProvider.getInitProvider());

        typeKeyProvider = typeKeyProvider.applyIdentifiers(CarTwoExporter.class);

        Assertions.assertThrows(ProviderException.class, typeKeyProvider::getInitProvider);
    }


    public enum Exporters {
        CAR_ONE,
        CAR_TWO
    }

    public interface Exporter {

    }

    public static class CarTwoExporter implements Exporter {

    }

    public static class CarOneExporter implements Exporter {
    }
}
