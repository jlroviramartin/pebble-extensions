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

import com.mitchellbosecke.pebble.extension.Filter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * New Line filter. This filter replaces the %n characters with system new line characters.
 *
 * @author joseluis
 */
public class NlFilter implements Filter {

    @Override
    public Object apply(Object inputObject, Map<String, Object> args) {
        return inputObject == null ? null : replace(inputObject.toString());
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(new String[0]);
    }

    private static String replace(String str) {

        // This counts the first %n characters.
        int first = 0;
        while (first < str.length() - 1 && str.startsWith("%n", first)) {
            first += 2;
        }

        // This counts the last %n characters.
        int last = 0;
        while (last < str.length() - 1 - first && str.startsWith("%n", str.length() - 2 - last)) {
            last += 2;
        }
        str = str.substring(first, str.length() - last);

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < first; i += 2) {
            buff.append(System.lineSeparator());
        }

        // This indicates if a line separator is added to the next iteration.
        boolean skip = true;
        for (String line : str.split("%n")) {
            if (skip) {
                skip = false;
            } else {
                buff.append(System.lineSeparator());
            }
            buff.append(line);
        }

        for (int i = 0; i < last; i += 2) {
            buff.append(System.lineSeparator());
        }

        return buff.toString();
    }
}
