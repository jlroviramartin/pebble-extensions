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
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import org.essence.commons.ArrayAsList;

/**
 * Reverse filter. This filter reverses the items of a collection.
 *
 * @author joseluis
 */
public class ReverseFilter implements Filter {

    @Override
    public Object apply(Object inputObject, Map<String, Object> args) {
        if (inputObject != null) {
            if (inputObject.getClass().isArray()) {
                Deque deque = new ArrayDeque<>();
                new ArrayAsList<>(inputObject).forEach(x -> deque.push(x));
                return deque.stream().toArray();
            } else if (inputObject instanceof Collection) {
                Deque deque = new ArrayDeque<>();
                ((Collection) inputObject).forEach(x -> deque.push(x));
                return deque.stream().toArray();
            }
        }
        return inputObject;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(new String[0]);
    }
}
