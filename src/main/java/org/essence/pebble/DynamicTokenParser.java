/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.essence.pebble;

import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.ArgumentsNode;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.AbstractTokenParser;

/**
 *
 * @author joseluis
 */
public class DynamicTokenParser extends AbstractTokenParser {

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip the 'dynamic' token
        stream.next();

        String name = parser.getExpressionParser().parseNewVariableName();

        ArgumentsNode args = parser.getExpressionParser().parseArguments(false);

        stream.expect(Token.Type.EXECUTE_END);

        return new DynamicNode(lineNumber, name, args);
    }

    @Override
    public String getTag() {
        return "dynamic";
    }
}
