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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * New Line filter. This filter replaces the %n characters with system new line characters.
 *
 * @author joseluis
 */
public class IndentFilter implements Filter {

    private static final String SIZE = "size";

    @Override
    public Object apply(Object inputObject, Map<String, Object> args) {
        int indentSize = 0;
        Object o = args.get(SIZE);
        if (o instanceof Number) {
            indentSize = ((Number) o).intValue();
        }
        return inputObject == null ? null : replace(inputObject.toString(), indentSize);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(new String[]{SIZE});
    }

    public static String replace(String str, int indentSize) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentSize; i++) {
            indent.append("    ");
        }

        StringBuilder buff = new StringBuilder();

        Pattern p = Pattern.compile("\r?\n");
        Matcher m = p.matcher(str);
        int f = 0;
        boolean insertIndent = false;
        while (m.find()) {
            String s = str.substring(f, m.end());
            if (insertIndent) {
                if (!s.trim().isEmpty()) {
                    buff.append(indent);
                }
            } else {
                insertIndent = true;
            }
            buff.append(s);
            f = m.end();
        }

        /*int f = 0;
        int i = str.indexOf('\n', f);
        while (i >= 0) {
            buff.append(str.substring(f, i + 1));

            f = i + 1;
            i = str.indexOf('\n', f);
            if (i - f == 1) {
                // In the blank lines we do not insert indentation.
                System.err.print("caca");
            } else {
                buff.append(indent);
            }
        }*/
        if (f < str.length()) {
            String s = str.substring(f);
            if (insertIndent) {
                if (!s.trim().isEmpty()) {
                    buff.append(indent);
                }
            }
            buff.append(s);
        }
        return buff.toString();
    }
}
