/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.essence.pebble;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.ArgumentsNode;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import java.io.IOException;
import java.io.Writer;

import static org.essence.commons.Helper.as;

/**
 *
 * @author joseluis
 */
public class DynamicNode extends AbstractRenderableNode {

    private final String name;
    private final ArgumentsNode args;

    public DynamicNode(int lineNumber, String name, ArgumentsNode args) {
        super(lineNumber);
        this.name = name;
        this.args = args;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException {
        MacroAction macro = as(MacroAction.class, context.getScopeChain().get(name));

        if (macro == null) {
            return;
        }

        String result = macro.exec(args);
        try {
            writer.write(result);
        } catch (IOException ex) {
            throw new PebbleException(ex, "Exception while rendering");
        }

        /*Object macro = context.getScopeChain().get(name);
        MacroAction aux = null;
        if (macro instanceof MacroAction) {
            aux = (MacroAction) macro;
        } else if (macro instanceof String && self.hasMacro((String) macro)) {
            aux = x -> self.macro(context, name, x, false, this.getLineNumber()).toString();
        }

        if (aux != null) {
            String result = aux.exec(args);
            try {
                writer.write(result);
            } catch (IOException ex) {
                throw new PebbleException(ex, "Exception while rendering");
            }
        }*/
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public ArgumentsNode getArgs() {
        return args;
    }

    public String getName() {
        return name;
    }
}
