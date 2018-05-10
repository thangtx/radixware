/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.wps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


final class WpsLocaleManager extends org.radixware.kernel.common.client.env.LocaleManager{
    
    private static final String TS_FILE_PATH = "kernel/web/explorer_%s.ts";

    private final WpsEnvironment env;

    public WpsLocaleManager(final WpsEnvironment env) {
        this.env = env;
    }

    @Override
    public List<EIsoLanguage> getSupportedLanguages() {
        final List<EIsoLanguage> result = new LinkedList<>();
        if (env.getClass().getClassLoader() instanceof RadixClassLoader) {
            final RadixClassLoader radixClassLoader = (RadixClassLoader) env.getClass().getClassLoader();
            final Collection<String> languageNames = radixClassLoader.getRevisionMeta().getLanguages();
            for (String langageName : languageNames) {
                try {
                    result.add(EIsoLanguage.getForValue(langageName));
                } catch (NoConstItemWithSuchValueError error) {//NOPMD
                    //Unknown language name - ommiting
                }
            }
        } else {
//            final EnumSet<EIsoLanguage> allLanguages = EnumSet.allOf(EIsoLanguage.class);
//            for (EIsoLanguage lng : allLanguages) {
//                if (isTranslationFileExists(lng)) {
//                    result.add(lng);
//                }
//            }
        }
        if (result.isEmpty()) {
            result.add(EIsoLanguage.ENGLISH);
        }

        return result;
    }
    
    public EIsoLanguage autoSelectLanguage(final HttpConnectionOptions httpConnection, final ConnectionOptions easConnection){
        EIsoLanguage lang;
        if (getSupportedLanguages().size() == 1) {
            lang = getSupportedLanguages().get(0);
        } else {
            lang = httpConnection != null ? httpConnection.getUrlLanguage() : null;
            if (lang == null || !getSupportedLanguages().contains(lang)) {
                lang = easConnection != null ? easConnection.getLanguage() : null;
                if (lang != null && !getSupportedLanguages().contains(lang)) {
                    lang = httpConnection != null ? httpConnection.getBrowserLanguage() : null;
                    if (lang != null && !getSupportedLanguages().contains(lang)) {
                        lang = EIsoLanguage.ENGLISH;
                        if (!getSupportedLanguages().contains(lang)) {
                            lang = getSupportedLanguages().get(0);
                        }
                    }
                }
            }
        }
        return lang;
    }

    static final class TranslationSet {

        private Map<String, Context> contexts;

        public static class Context {

            Map<String, String> map = new HashMap<>();

            public static class Message {

                String src;
                boolean expectSrc;
                boolean expectTranslation;
                String translation;
            }
            private Message currentLoadMessage;

            private Message currentLoadMessage() {
                return currentLoadMessage;
            }

            public Message beginMessage() {
                if (currentLoadMessage != null) {
                    closeCurrentLoadMessage();
                }
                currentLoadMessage = new Message();
                return currentLoadMessage;
            }

            public void closeCurrentLoadMessage() {
                if (currentLoadMessage != null 
                    && currentLoadMessage.translation!=null 
                    && !currentLoadMessage.translation.isEmpty()) {
                    map.put(currentLoadMessage.src.toLowerCase(), currentLoadMessage.translation);
                }
            }
            private String name;
            private boolean expectName;
        }
        private Context currentLoadContext = null;

        public Context beginContext() {
            currentLoadContext = new Context();
            return currentLoadContext;
        }

        public Context currentLoadContext() {
            return currentLoadContext;
        }

        public void closeCurrentLoadContext() {
            if (currentLoadContext != null && currentLoadContext.name != null) {
                currentLoadContext.closeCurrentLoadMessage();
                if (contexts == null) {
                    contexts = new HashMap<>();
                }
                Context exContext = contexts.get(currentLoadContext.name);
                if (exContext != null) {
                    if (currentLoadContext.map != null) {
                        exContext.map.putAll(currentLoadContext.map);
                    }

                } else {
                    contexts.put(currentLoadContext.name, currentLoadContext);
                }
                currentLoadContext = null;
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (contexts != null) {
                for (Map.Entry<String, Context> e : contexts.entrySet()) {
                    builder.append("\n");
                    builder.append(e.getKey());
                    if (e.getValue().map != null) {
                        for (Map.Entry<String, String> e2 : e.getValue().map.entrySet()) {
                            builder.append("\n");
                            builder.append(e2.getKey());
                            builder.append(" : ");
                            builder.append(e2.getValue());
                        }
                    }
                }
            }
            return builder.toString();
        }
    }
    private static final TranslationSet NONE = new TranslationSet();
    private static Map<EIsoLanguage, TranslationSet> translations = new HashMap<>();

    static TranslationSet getTranslations(final EIsoLanguage lang) {
        TranslationSet set = translations.get(lang);
        if (set == null) {
            loadTranslations(lang);
            set = translations.get(lang);
        }

        if (set == NONE) {
            return null;
        } else {
            return set;
        }
    }

    static String translateImpl(EIsoLanguage lang, String key, String value) {
        TranslationSet set = getTranslations(lang);
        if (set == null) {
            return null;
        }
        if (set.contexts == null) {
            return null;
        }
        TranslationSet.Context c = set.contexts.get(key);
        if (c == null) {
            return null;
        }
        if (c.map == null) {
            return null;
        }
        return c.map.get(value.toLowerCase());
    }

    static String translate(EIsoLanguage lang, String key, String value) {

        String res = translateImpl(lang, key, value);
        if (res == null) {
            return value;
        }
        return res;
    }
    
    private static InputStream loadTranslationsFromRepository(final EIsoLanguage lang){
        final RadixLoader loader = RadixLoader.getInstance();
        final RevisionMeta revision = loader.getCurrentRevisionMeta();
        final String fileName = String.format(TS_FILE_PATH, lang.getValue());
        final FileMeta fileMeta = revision.findOverFile(fileName);
        if (fileMeta==null){
            return null;
        }
        final byte[] translation;
        try{
            translation = loader.readFileData(fileMeta, revision);
        }catch(IOException exception){
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Failed to extract repository file: \"" +fileName+ "\"",exception);
            return null;
        }  
        return translation==null || translation.length==0 ? null : new ByteArrayInputStream(translation);        
    }

    private static void loadTranslations(final EIsoLanguage lang) {
        InputStream stream = loadTranslationsFromRepository(lang);
        if (stream!=null){
            final TranslationSet set = readTranslations(stream);
            if (set!=null){
                translations.put(lang, set);
                return;
            }
        }
        stream = WpsLocaleManager.class.getResourceAsStream("translations_" + lang.getValue() + ".properties");
        if (stream!=null){
            final TranslationSet set = readTranslations(stream);
            if (set!=null){
                translations.put(lang, set);
                return;
            }
        }
        translations.put(lang, NONE);
    }
    
    private static TranslationSet readTranslations(final InputStream stream) {
        try {
            final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

            final TranslationSet set = new TranslationSet();
            parser.parse(stream, new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if ("context".equals(qName)) {
                        set.closeCurrentLoadContext();
                        set.beginContext();
                    } else if ("name".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            c.expectName = true;
                        }
                    } else if ("message".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            c.beginMessage();
                        }
                    } else if ("source".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            TranslationSet.Context.Message m = c.currentLoadMessage();
                            if (m != null) {
                                m.expectSrc = true;
                            }
                        }
                    } else if ("translation".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            TranslationSet.Context.Message m = c.currentLoadMessage();
                            if (m != null) {
                                m.expectTranslation = true;
                            }
                        }
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if ("context".equals(qName)) {
                        set.closeCurrentLoadContext();
                    } else if ("translation".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            TranslationSet.Context.Message m = c.currentLoadMessage();
                            if (m != null) {
                                m.expectTranslation = false;
                            }
                        }
                    } else if ("source".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            TranslationSet.Context.Message m = c.currentLoadMessage();
                            if (m != null) {
                                m.expectSrc = false;
                            }
                        }
                    } else if ("name".equals(qName)) {
                        TranslationSet.Context c = set.currentLoadContext();
                        if (c != null) {
                            c.expectName = false;
                        }
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    super.characters(ch, start, length);

                    TranslationSet.Context c = set.currentLoadContext();
                    if (c != null) {

                        if (c.expectName) {
                            if (c.name != null) {
                                c.name += String.valueOf(ch, start, length);
                            } else {
                                c.name = String.valueOf(ch, start, length);
                            }

                        } else {
                            TranslationSet.Context.Message m = c.currentLoadMessage();
                            if (m != null) {
                                if (m.expectSrc) {
                                    if (m.src != null) {
                                        m.src += String.valueOf(ch, start, length);
                                    } else {
                                        m.src = String.valueOf(ch, start, length);
                                    }

                                }
                                if (m.expectTranslation) {
                                    if (m.translation != null) {
                                        m.translation += String.valueOf(ch, start, length);
                                    } else {
                                        m.translation = String.valueOf(ch, start, length);
                                    }
                                }
                            }
                        }
                    }
                }
            });
            return set;
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on loading translations", ex);
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
            }
        }        
    }
}
