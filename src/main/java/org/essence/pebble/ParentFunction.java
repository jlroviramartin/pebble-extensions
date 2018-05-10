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
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.template.ScopeChain;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.essence.commons.Helper.as;

/**
 *
 * @author joseluis
 */
public class ParentFunction implements Function {

    public static final String PARENT = "_parent";
    public static final String SELF = "_self";
    public static final String CONTEXT = "_context";

    @Override
    public Object execute(Map<String, Object> map) {
        PebbleTemplateImpl self = as(PebbleTemplateImpl.class, map.get(SELF));
        EvaluationContext context = as(EvaluationContext.class, map.get(CONTEXT));

        if (self == null) {
            return null;
        }

        // We get the "_parent" variable. The variable is the scope where is defined
        // the dynamic macro.
        ScopeChain copy = context.getScopeChain().deepCopy();
        try {
            // We remove the first local scope.
            copy.popScope();
            ScopeChain parent = (ScopeChain) copy.get(PARENT);
            if (parent != null) {
                return new ParentMap(parent);
            }
        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(new String[0]);
    }

    private static class ParentMap implements Map<String, Object> {

        public ParentMap(ScopeChain scopeChain) {
            this.scopeChain = scopeChain;
        }

        private final ScopeChain scopeChain;

        @Override
        public int size() {
            return 10;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean containsKey(Object key) {
            return scopeChain.containsKey((String) key);
        }

        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object get(Object key) {
            return scopeChain.get((String) key);
        }

        @Override
        public Object put(String key, Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object remove(Object key) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Set<String> keySet() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Collection<Object> values() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
