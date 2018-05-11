/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.essence.pebble;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.essence.commons.MapBuilder;

/**
 *
 * @author joseluis
 */
public class DynamicExtension extends AbstractExtension {

    @Override
    public List<TokenParser> getTokenParsers() {
        return Arrays.asList(
                new DynamicTokenParser(),
                new DynamicMacroTokenParser());
    }

    @Override
    public Map<String, Filter> getFilters() {
        return new MapBuilder<String, Filter>()
                .put("nl", new NlFilter())
                .put("indent", new IndentFilter())
                .put("reverse", new ReverseFilter())
                .build();
    }

    @Override
    public Map<String, Function> getFunctions() {
        return new MapBuilder<String, Function>()
                .put("reflection", new ReflectionFunction())
                .put("getMacro", new GetMacroFunction())
                .put("dynamic", new DynamicFunction())
                .put("p", new ParentFunction())
                .put("invoke", new InvokeMacroFunction())
                .build();
    }
}
