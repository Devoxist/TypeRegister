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

package nl.devoxist.typeresolver.typeproviders.builder;

import nl.devoxist.typeresolver.exception.RegisterException;
import nl.devoxist.typeresolver.providers.TypeKeyProvider;
import nl.devoxist.typeresolver.providers.builders.IdentifiersBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentifiersBuilderTest {

    @Test
    public void buildProviderTest() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();
        identifiersBuilder.addIdentifier(CarOneExporter.class, new CarOneExporter());

        Assertions.assertTrue(identifiersBuilder.buildProvider(Exporter.class) instanceof TypeKeyProvider<Exporter, Class<? extends Exporter>>);
    }

    @Test
    public void buildProviderFailTest() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();

        Assertions.assertThrows(RegisterException.class, () -> identifiersBuilder.buildProvider(Exporter.class));
    }

    @Test
    public void addIdentifierTest() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();
        identifiersBuilder.addIdentifier(CarTwoExporter.class, new CarTwoExporter());

        TypeKeyProvider<Exporter, Class<? extends Exporter>> typeKeyProvider =
                identifiersBuilder.buildProvider(Exporter.class);

        Assertions.assertEquals(1, typeKeyProvider.getIdentifiersMap().size());
        Assertions.assertTrue(typeKeyProvider.getIdentifiersMap().containsKey(CarTwoExporter.class));
    }

    @Test
    public void addIdentifierTest2() {
        IdentifiersBuilder<Exporter, Class<? extends Exporter>> identifiersBuilder = new IdentifiersBuilder<>();
        identifiersBuilder.addIdentifier(CarOneExporter.class, new CarOneExporter());
        identifiersBuilder.addIdentifier(CarTwoExporter.class, new CarTwoExporter());

        TypeKeyProvider<Exporter, Class<? extends Exporter>> typeKeyProvider =
                identifiersBuilder.buildProvider(Exporter.class);

        Assertions.assertEquals(2, typeKeyProvider.getIdentifiersMap().size());
        Assertions.assertTrue(typeKeyProvider.getIdentifiersMap().containsKey(CarTwoExporter.class));
    }

    public interface Exporter {

    }

    public static class CarTwoExporter implements Exporter {

    }

    public static class CarOneExporter implements Exporter {
    }

}
