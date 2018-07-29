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

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.node.ArgumentsNode;
import com.mitchellbosecke.pebble.node.BodyNode;
import com.mitchellbosecke.pebble.node.MacroNode;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.Macro;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.template.ScopeChain;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joseluis
 */
public class DynamicMacroNode extends MacroNode {

    public static final String PARENT = "_parent";

    public DynamicMacroNode(String name, ArgumentsNode args, BodyNode body) {
        super(name, args, body);
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException, IOException {
        super.render(self, writer, context);

        if (self.hasMacro(getName())) {
            MacroAction macro = args -> {
                SafeString safe = self.macro(context, getName(), args, false, -1);
                return safe.toString();
            };

            context.getScopeChain().set(getName(), macro);
            currentScope = context.getScopeChain().deepCopy();
        }
    }

    @Override
    public Macro getMacro() {
        Macro macro = super.getMacro();
        return new Macro() {

            @Override
            public List<String> getArgumentNames() {
                return macro.getArgumentNames();
            }

            @Override
            public String getName() {
                return macro.getName();
            }

            @Override
            public String call(PebbleTemplateImpl self, EvaluationContext context, Map<String, Object> macroArgs)
                    throws PebbleException {
                // We add the variable "_parent" to the scope.
                context.getScopeChain().put(PARENT, currentScope);
                return macro.call(self, context, macroArgs);
            }
        };
    }

    public ScopeChain getCurrentScope() {
        return currentScope;
    }

    private ScopeChain currentScope;
}
