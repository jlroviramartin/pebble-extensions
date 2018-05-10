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

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.essence.commons.Helper.as;

/**
 *
 * @author joseluis
 */
public class GetTemplateFunction implements Function {

    public static final String SELF = "_self";
    public static final String CONTEXT = "_context";
    public static final String NAME = "name";

    @Override
    public Object execute(Map<String, Object> map) {
        String name = as(String.class, map.get(NAME));
        PebbleTemplateImpl self = as(PebbleTemplateImpl.class, map.get(SELF));
        EvaluationContext context = as(EvaluationContext.class, map.get(CONTEXT));

        if (name == null || self == null || !self.hasMacro(name)) {
            return null;
        }

        MacroAction macro = args -> {
            SafeString safe = self.macro(context, name, args, false, -1);
            return safe.toString();
        };
        return macro;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(new String[]{NAME});
    }
}
