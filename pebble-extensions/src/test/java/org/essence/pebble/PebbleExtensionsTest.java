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

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import org.essence.commons.MapBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author joseluis
 */
public class PebbleExtensionsTest extends TemplateEngine {

    public PebbleExtensionsTest() {
        super(PebbleExtensionsTest::buildEngine);
    }

    private static PebbleEngine buildEngine() {
        return new PebbleEngine.Builder()
                .loader(new StringLoader())
                .addEscapingStrategy("none", x -> x)
                .defaultEscapingStrategy("none")
                .extension(new DynamicExtension())
                .build();
    }

    @Test
    public void nlFilterTest() {
        StringBuilder buff = new StringBuilder();
        buff.append("{{ \"Line 1%nLine 2%nLine 3%n\" | nl }}");

        Template t = load(buff.toString());
        String s = t.toString(new MapBuilder<String, Object>().build());
        String e = String.format("Line 1%n"
                                 + "Line 2%n"
                                 + "Line 3%n");

        Assert.assertEquals(e, s);
    }

    @Test
    public void dynamicMacroNodeTest() {
        StringBuilder buff = new StringBuilder();
        buff.append("{# Macro 'Lambda' is defined to call it as a lambda macro. #}").append(System.lineSeparator());
        buff.append("{% dynmacro Lambda( a, b, c ) %}").append(System.lineSeparator());
        buff.append("<{{ a }} {{ b }} {{ c }}>").append(System.lineSeparator());
        buff.append("{%- enddynmacro %}").append(System.lineSeparator());

        buff.append("{{ MyMethod( Lambda ) }}").append(System.lineSeparator());

        buff.append("{% macro MyMethod( macroToCall ) %}").append(System.lineSeparator());
        buff.append("{# 'dynamic' is used to dynamically call macro 'macroToCall'. #}").append(System.lineSeparator());
        buff.append("Result: {{ dynamic( macroToCall, [ 1, \"String\", 1.5 ] ) }}").append(System.lineSeparator());
        buff.append("{%- endmacro %}").append(System.lineSeparator());

        Template t = load(buff.toString());
        String s = t.toString(new MapBuilder<String, Object>().build());
        String e = String.format("Result: <1 String 1.5>");

        Assert.assertEquals(e, s);
    }

    @Test
    public void parentFunctionTest() {
        StringBuilder buff = new StringBuilder();
        buff.append("{% macro Macro1( a ) %}").append(System.lineSeparator());
        buff.append("    {%- dynmacro Macro2( b ) %}").append(System.lineSeparator());
        buff.append("Result: {{ p().a }}, {{ b }}").append(System.lineSeparator());
        buff.append("    {%- enddynmacro %}").append(System.lineSeparator());
        buff.append("{{ Macro2(\"Inner text\") }}").append(System.lineSeparator());
        buff.append("{%- endmacro %}").append(System.lineSeparator());

        buff.append("{{ Macro1(\"Outer text\") }}").append(System.lineSeparator());

        Template t = load(buff.toString());
        String s = t.toString(new MapBuilder<String, Object>().build());
        String e = String.format("Result: Outer text, Inner text");

        Assert.assertEquals(e, s);
    }

    @Test
    public void indentFilterTest() {
        StringBuilder buff = new StringBuilder();
        buff.append("{# We reuse the four-space indentation and indent the text by 1 (* 4 spaces). #}").append(System.lineSeparator());
        buff.append("    {{ \"NOT indented%nIndented%nIndented\" | nl | indent(1) }}").append(System.lineSeparator());

        Template t = load(buff.toString());
        String s = t.toString(new MapBuilder<String, Object>().build());
        String e = String.format("    NOT indented%n"
                                 + "    Indented%n"
                                 + "    Indented");

        Assert.assertEquals(e, s);
    }

    @Test
    public void invokeMacroTest() {
        String str = "{% macro Test( a, b, c ) %}\n"
                     + "Result: {{ a }}, {{ b }}, {{ c }}\n"
                     + "{%- endmacro %}\n"
                     + "{% set mname = \"Test\" %}\n"
                     + "{{ invoke( mname, [ \"Arg 1\", \"Arg 2\", \"Arg 3\" ] ) }}";

        Template t = load(str);
        String s = t.toString(new MapBuilder<String, Object>().build());
        String e = String.format("Result: Arg 1, Arg 2, Arg 3");

        Assert.assertEquals(e, s);
    }
}
