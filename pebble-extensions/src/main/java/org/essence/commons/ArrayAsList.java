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

import java.lang.reflect.Array;
import java.util.AbstractList;

/**
 *
 * @author joseluis
 */
public class ArrayAsList<T> extends AbstractList<T> {

    private final Class<?> componentType;
    private Object array;

    public ArrayAsList(Object array) {
        componentType = array.getClass().getComponentType();
        this.array = array;
    }

    @Override
    public int size() {
        return Array.getLength(array);
    }

    @Override
    public T get(int index) {
        return (T) Array.get(array, index);
    }

    @Override
    public T set(int index, T element) {
        Array.set(array, index, element);
        return element;
    }

    @Override
    public void add(int index, T element) {
        int sz = Array.getLength(array);
        Object newArray = Array.newInstance(componentType, sz + 1);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index, newArray, index + 1, sz - index);
        Array.set(newArray, index, element);
        this.array = newArray;
    }

    @Override
    public T remove(int index) {
        Object element = Array.get(array, index);
        int sz = size();
        Object newArray = Array.newInstance(componentType, sz - 1);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, sz - index - 1);
        this.array = newArray;
        return (T) element;
    }
}
