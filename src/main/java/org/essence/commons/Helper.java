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
package org.essence.commons;

/**
 *
 * @author joseluis
 */
public class Helper {

    /**
     * This method is similar to the C# {@code as} operator.
     *
     * @param <T> Type to cast
     * @param type Type to cast.
     * @param o Object to cast.
     * @return Value.
     */
    public static <T> T as(Class<T> type, Object o) {
        if (type.isInstance(o)) {
            return (T) o;
        }
        return null;
    }

    /**
     * This method converts {@code o} to an int value.
     *
     * @param o Object to convert.
     * @param defValue Default value.
     * @return Value.
     */
    public static int asInt(Object o, int defValue) {
        if (Number.class.isInstance(o)) {
            return ((Number) o).intValue();
        }
        return defValue;
    }
}
