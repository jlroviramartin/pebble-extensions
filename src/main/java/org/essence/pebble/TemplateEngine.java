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
 * Template engine for Pebble. It is a little bit stupid class but... at first it was used to encasulate others engines.
 * Maybe in a future...
 *
 * @author joseluis
 */
public class TemplateEngine {

//<editor-fold defaultstate="collapsed" desc="fields">
    private final PebbleEngine pebbleEngine;
//</editor-fold>

    public TemplateEngine() {
        this(TemplateEngine::buildEngine);
    }

    public TemplateEngine(Function0<PebbleEngine> build) {
        pebbleEngine = build.apply();
    }

    public Template load(String resourceName) {
        try {
            return new MyPebbleTemplate(this, pebbleEngine.getTemplate(resourceName));
        } catch (PebbleException ex) {
            Logger.getLogger(TemplateEngine.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error(ex);
        }
    }

//<editor-fold defaultstate="collapsed" desc="private">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Inner classes">
    public static interface Template {

        void writeTo(OutputStream str, Map<String, Object> params);

        default void writeToFile(Map<String, Object> params, String fileName) {
            File file = new File(fileName);
            preparePath(file);

            try (OutputStream stream = new FileOutputStream(file)) {

                writeTo(stream, params);

                System.out.println("Output " + fileName);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        default String toString(Map<String, Object> params) {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {

                writeTo(stream, params);
                return stream.toString(StandardCharsets.UTF_8.name());
            } catch (IOException ex) {
                Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }
        }
    }

    protected static class MyPebbleTemplate implements Template {

        public MyPebbleTemplate(TemplateEngine base, PebbleTemplate template) {
            this.base = base;
            this.template = template;
        }

        private final TemplateEngine base;
        private final PebbleTemplate template;

        @Override
        public void writeTo(OutputStream stream, Map<String, Object> params) {
            try (Writer writer = new OutputStreamWriter(stream)) {

                template.evaluate(writer, params);
            } catch (IOException | PebbleException ex) {
                Logger.getLogger(MyPebbleTemplate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//</editor-fold>

    /*import java.net.URL;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.DefaultEnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.rythmengine.RythmEngine;
import org.rythmengine.conf.RythmConfigurationKey;
import org.rythmengine.extension.ICodeType;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;*/

 /*protected final String mainPath;
    private final RythmEngine rythmEngine;
    private final EnvironmentConfiguration jtwigConfiguration;

    private final PebbleEngine pebbleEngine;

    {
        //mainPath = "C:\\Users\\joseluis\\source\\Java\\TemplatesForVectors\\src\\generated\\java";
        mainPath = System.getProperty("user.dir") + "\\src\\generated\\java";

        Map<String, Object> rythmConfiguration = new HashMap<>();
        rythmConfiguration.put(RythmConfigurationKey.CODEGEN_COMPACT_ENABLED.getKey(), false);
        rythmConfiguration.put(RythmConfigurationKey.HOME_TEMPLATE.getKey(), ".");
        rythmConfiguration.put(RythmConfigurationKey.HOME_TMP.getKey(), "C:/Temp/rythm/");

        rythmEngine = new RythmEngine(rythmConfiguration);
        rythmEngine.prepare(ICodeType.DefImpl.RAW);
        //engine.registerFastTag(new RemoveLast());

        jtwigConfiguration = new EnvironmentConfigurationBuilder(new DefaultEnvironmentConfiguration())
                .functions().add(new FrustifrusFunction()).and()
                .build();

        pebbleEngine = new PebbleEngine.Builder()
                .addEscapingStrategy("none", x -> x)
                .defaultEscapingStrategy("none")
                .extension(new DynamicExtension())
                .build();
    }*/
 /*protected Template loadSTTemplate(String fileName, String name) {
        STGroup group = new STGroupFile(fileName);
        group.registerRenderer(String.class, new StringRenderer());
        return new MySTTemplate(group, name);
    }

    protected Template loadSTTemplate(File file, String name) {
        STGroup group = new STGroupFile(file.toString());
        group.registerRenderer(String.class, new StringRenderer());
        return new MySTTemplate(group, name);
    }

    protected Template loadTwigTemplate(String fileName) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(fileName, jtwigConfiguration);
        return new MyJTwigTemplate(template);
    }

    protected Template loadTwigTemplate(File file) {
        return new MyJTwigTemplate(JtwigTemplate.classpathTemplate(file.toString(), jtwigConfiguration));
    }

    protected Template loadRythmResource(String resourceName) {
        URL url = BaseTemplate.class.getClassLoader().getResource(resourceName);
        return new MyRythmTemplate(new File(url.getPath()));
    }

    protected Template loadRythmTemplate(String fileName) {
        return new MyRythmTemplate(new File(fileName));
    }

    protected Template loadRythmTemplate(File file) {
        return new MyRythmTemplate(file);
    }*/
 /*protected static class MySTTemplate implements Template {

        public MySTTemplate(STGroup group, String name) {
            this.group = group;
            this.name = name;
        }

        protected final STGroup group;
        protected final String name;

        @Override
        public void writeTo(BaseTemplate base, OutputStream stream, Map<String, Object> params) {

            try (OutputStreamWriter osWriter = new OutputStreamWriter(stream)) {
                ST st = group.getInstanceOf(name);
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    st.add(entry.getKey(), entry.getValue());
                }

                st.write(new AutoIndentWriter(osWriter));
                osWriter.flush();
            } catch (IOException ex) {
                Logger.getLogger(BaseTemplate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected static class MyJTwigTemplate implements Template {

        public MyJTwigTemplate(JtwigTemplate template) {
            this.template = template;
        }

        protected final JtwigTemplate template;

        @Override
        public void writeTo(BaseTemplate base, OutputStream stream, Map<String, Object> params) {

            JtwigModel model = JtwigModel.newModel();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                model.with(entry.getKey(), entry.getValue());
            }

            template.render(model, stream);
        }
    }

    protected static class MyRythmTemplate implements Template {

        public MyRythmTemplate(File file) {
            this.file = file;
        }

        protected final File file;

        @Override
        public void writeTo(BaseTemplate base, OutputStream stream, Map<String, Object> params) {

            base.rythmEngine.render(stream, file, params);

            //rythmEngine.getTemplate(template.file);
            //template.__setRenderArgs(params);
            //template.render(stream);
        }
    }*/
}
