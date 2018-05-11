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

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.essence.commons.Function0;

/**
 *
 * @author joseluis
 */
public class BaseTemplate {

//<editor-fold defaultstate="collapsed" desc="fields">
    private final PebbleEngine pebbleEngine;
//</editor-fold>

    public BaseTemplate() {
        this(BaseTemplate::buildEngine);
    }

    public BaseTemplate(Function0<PebbleEngine> build) {
        pebbleEngine = build.apply();
    }

    public void execute() {
    }

    protected Template loadPebbleResource(String resourceName) {
        try {
            return new MyPebbleTemplate(pebbleEngine.getTemplate(resourceName));
        } catch (PebbleException ex) {
            Logger.getLogger(BaseTemplate.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error(ex);
        }
    }

    protected void exec(Template template, Map<String, Object> params, String fileName) {
        File file = new File(fileName);
        preparePath(file);

        try (OutputStream stream = new FileOutputStream(file)) {

            template.writeTo(this, stream, params);

            System.out.println("Output " + fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BaseTemplate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BaseTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String exec(Template template, Map<String, Object> params) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {

            template.writeTo(this, stream, params);
            return stream.toString(StandardCharsets.UTF_8.name());
        } catch (IOException ex) {
            Logger.getLogger(BaseTemplate.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static PebbleEngine buildEngine() {
        return new PebbleEngine.Builder()
                .addEscapingStrategy("none", x -> x)
                .defaultEscapingStrategy("none")
                .extension(new DynamicExtension())
                .build();
    }

    private static void preparePath(File file) {
        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Cannot delete file " + file);
            }
            if (file.exists()) {
                System.out.println("Error deleting file " + file);
            }
        } else if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    protected static interface Template {

        void writeTo(BaseTemplate base, OutputStream str, Map<String, Object> params);
    }

    protected static class MyPebbleTemplate implements Template {

        public MyPebbleTemplate(PebbleTemplate template) {
            this.template = template;
        }

        private final PebbleTemplate template;

        @Override
        public void writeTo(BaseTemplate base, OutputStream stream, Map<String, Object> params) {
            try (Writer writer = new OutputStreamWriter(stream)) {

                template.evaluate(writer, params);
            } catch (IOException | PebbleException ex) {
                Logger.getLogger(BaseTemplate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
