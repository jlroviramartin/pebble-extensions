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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.ConvertUtils;

import static org.essence.commons.Helper.as;

/**
 *
 * @author joseluis
 */
public class ReflectionFunction implements Function {

    public static final String OBJECT = "object";
    public static final String NAME = "name";
    public static final String ARGS = "args";

    @Override
    public Object execute(Map<String, Object> map) {
        Object obj = map.get(OBJECT);
        String name = as(String.class, map.get(NAME));
        List<?> args = as(List.class, map.get(ARGS));

        if (obj == null || name == null || args == null) {
            return null;
        }

        Object[] array = args.toArray(new Object[args.size()]);

        Method method = Arrays.stream(obj.getClass().getMethods())
                .filter(x -> x.getName().equals(name) && compatibles(x.getParameters(), array))
                .findFirst()
                .orElse(null);

        if (method == null) {
            return null;
        }

        try {
            updateParameters(method.getParameters(), array);
            return method.invoke(obj, array);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ReflectionFunction.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(new String[]{OBJECT, NAME, ARGS});
    }

    private static boolean compatibles(Parameter[] pargs, Object[] args) {
        if (pargs.length != args.length) {
            return false;
        }
        for (int i = 0; i < pargs.length; i++) {
            if (args[i] != null
                && (args[i].getClass().isPrimitive() || Number.class.isInstance(args[i]))
                && (pargs[i].getType().isPrimitive() || Number.class.isInstance(pargs[i]))) {
            } else if (args[i] != null && pargs[i].getType().isInstance(args[i])) {
            } else if (args[i] == null && !pargs[i].getType().isPrimitive()) {
            } else {
                return false;
            }
        }
        return true;
    }

    private static void updateParameters(Parameter[] pargs, Object[] args) {
        if (pargs.length != args.length) {
            throw new Error();
        }
        for (int i = 0; i < pargs.length; i++) {
            if (args[i] != null
                && (args[i].getClass().isPrimitive() || Number.class.isInstance(args[i]))
                && (pargs[i].getType().isPrimitive() || Number.class.isInstance(pargs[i]))) {
                args[i] = ConvertUtils.convert(args[i], pargs[i].getType());
            } else if (args[i] != null && pargs[i].getType().isInstance(args[i])) {
            } else if (args[i] == null && !pargs[i].getType().isPrimitive()) {
            } else {
                throw new Error();
            }
        }
    }
}
