/*
 * Copyright (C) 2018 joseluis.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.essence.pebble;

import java.util.function.Function;
import org.essence.commons.MapBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author joseluis
 */
public class IndentFilterTest {

    @Test
    public void hello() {
        Function<String, String> indent = s -> {
            return new IndentFilter().apply(
                    s,
                    new MapBuilder<String, Object>().put("size", 2).build());
        };

        Assert.assertEquals("", indent.apply(""));

        Assert.assertEquals("Prueba", indent.apply("Prueba"));

        Assert.assertEquals("Prueba\r\n", indent.apply("Prueba\r\n"));

        Assert.assertEquals("Prueba\r\n        Prueba", indent.apply("Prueba\r\nPrueba"));

        Assert.assertEquals("Prueba\r\n        Prueba\r\n", indent.apply("Prueba\r\nPrueba\r\n"));

        Assert.assertEquals("\r\n        Prueba\r\n        Prueba", indent.apply("\r\nPrueba\r\nPrueba"));

        Assert.assertEquals("Prueba\r\n        Prueba\r\n\r\n\r\n", indent.apply("Prueba\r\nPrueba\r\n\r\n\r\n"));
    }
}
