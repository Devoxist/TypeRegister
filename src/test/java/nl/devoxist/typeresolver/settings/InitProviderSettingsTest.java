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

package nl.devoxist.typeresolver.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InitProviderSettingsTest {

    @Test
    public void constructionTest() {
        InitProviderSettings initProviderSettings = new InitProviderSettings();

        Assertions.assertFalse(initProviderSettings.useAllRegisters());
        Assertions.assertEquals(0, initProviderSettings.getIdentifiers().length);
    }

    @Test
    public void setIdentifiersTest() {
        InitProviderSettings initProviderSettings = new InitProviderSettings();
        initProviderSettings.setIdentifiers(initProviderSettings);

        Assertions.assertEquals(1, initProviderSettings.getIdentifiers().length);
        Assertions.assertEquals(initProviderSettings, initProviderSettings.getIdentifiers()[0]);
    }

    @Test
    public void setIdentifiersTest2() {
        InitProviderSettings initProviderSettings = new InitProviderSettings();
        initProviderSettings.setIdentifiers(initProviderSettings);

        Object object = new Object();
        initProviderSettings.getIdentifiers()[0] = object;

        Assertions.assertEquals(1, initProviderSettings.getIdentifiers().length);
        Assertions.assertEquals(initProviderSettings, initProviderSettings.getIdentifiers()[0]);
        Assertions.assertNotEquals(object, initProviderSettings.getIdentifiers()[0]);
    }
}
