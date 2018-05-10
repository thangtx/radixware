/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.utils;

import com.trolltech.qt.core.QTranslator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.env.HttpLoader;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class TranslationsFile {
    
    private final static String TR_APP_TRANSLATIONS_FILE_NAME = "explorer_%s.qm";
    private final static String TR_QT_TRANSLATIONS_FILE_NAME = "qt_%s.qm";
    private final static String TR_DIR_NAME = "translation";
    private final static String TR_RESOURCE_FILE_PATH = "org/radixware/kernel/explorer/" + TR_DIR_NAME;    
    private final static String TR_REPOSITORY_FILE_PATH = "kernel/explorer";    
    
    private final static class HashKey{
        
        private final String filePath;
        private final URL codeBase;
        
        public HashKey(final String filePath, final URL codeBase){
            this.filePath = filePath;
            this.codeBase = codeBase;            
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.filePath);
            hash = 83 * hash + Objects.hashCode(this.codeBase);            
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final HashKey other = (HashKey) obj;
            if (!Objects.equals(this.filePath, other.filePath)) {
                return false;
            }
            if (!Objects.equals(this.codeBase, other.codeBase)) {
                return false;
            }
            return true;
        }
    }
    
    private final static Map<HashKey,TranslationsFile> CACHED_TRANSLATIONS = new HashMap<>();
    
    public static TranslationsFile getApplicationTranslations(final EIsoLanguage language,  final URL codeBase){
        final String appTranslationsFileName = String.format(TR_APP_TRANSLATIONS_FILE_NAME, language.getValue());
        final HashKey key = new HashKey(appTranslationsFileName, codeBase);
        TranslationsFile file = CACHED_TRANSLATIONS.get(key);
        if (file==null){
            file = new TranslationsFile(appTranslationsFileName, language, codeBase);
            CACHED_TRANSLATIONS.put(key, file);
        }
        return file;
    }
    
    public static TranslationsFile getQtTranslations(final EIsoLanguage language,  final URL codeBase){
        final String appTranslationsFileName = String.format(TR_QT_TRANSLATIONS_FILE_NAME, language.getValue());
        final HashKey key = new HashKey(appTranslationsFileName, codeBase);
        TranslationsFile file = CACHED_TRANSLATIONS.get(key);
        if (file==null){
            file = new TranslationsFile(appTranslationsFileName, language, codeBase);
            CACHED_TRANSLATIONS.put(key, file);
        }
        return file;        
    }
    
    public static TranslationsFile getDefaultAppTranslations(){
        {
            final EIsoLanguage language = RunParams.getLanguage();
            if (language!=null){                
                final TranslationsFile result = getApplicationTranslations(language, null);
                if (result!=null && result.loadTranslator(null)!=null){
                    return result;
                }
            }
        }
        final String languageName = com.trolltech.qt.core.QLocale.system().language().name();
        EIsoLanguage language;
        try {            
            language = EIsoLanguage.valueOf(EIsoLanguage.class, languageName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }        
        return getApplicationTranslations(language, null);
    }
    
    public static void clearCache(){
        CACHED_TRANSLATIONS.clear();
    }
        

    private final String fileName;
    private final EIsoLanguage language;
    private final URL codeBase;
    private QTranslator translator;
    
    private TranslationsFile(final String fileName, final EIsoLanguage language, final URL codeBase){
        this.fileName = fileName;
        this.language = language;
        this.codeBase = codeBase;
    }
    
    public EIsoLanguage getLanguage(){
        return language;
    }
    
    public QTranslator loadTranslator(final ClientTracer tracer) {
        if (translator==null && language!=EIsoLanguage.ENGLISH){
            final String languageName = language.getValue();
            String temporaryFile = extractRepositoryTranslationFile(TR_REPOSITORY_FILE_PATH+"/"+fileName, tracer);
            if (temporaryFile==null){
                temporaryFile = extractResourceTranslationFile(TR_RESOURCE_FILE_PATH + "/" + fileName, tracer);
            }            
            if (temporaryFile != null) {
                translator = applyTranslationsFile(temporaryFile, tracer);
                if (translator == null) {
                    if (tracer!=null){
                        tracer.error("Error on installing translation for " + languageName.toLowerCase() + " language from translation file " + fileName);
                    }
                    return null;
                }
            }
            if (codeBase != null) {
                final String filePath = TR_DIR_NAME + "/" + fileName;
                temporaryFile = loadByHttp(filePath, codeBase, tracer);
            }
            if (temporaryFile != null) {
                translator = applyTranslationsFile(temporaryFile,tracer);
                if (translator == null && tracer!=null) {
                    tracer.error("Error on installing translation for " + languageName.toLowerCase() + " language - using english language");
                }                                
            } else {
                if (tracer!=null){
                    tracer.warning("Can`t find translation for " + languageName.toLowerCase() + " language - using english language");
                }
            }            
        }
        return translator;
    }    
    
    private static String extractRepositoryTranslationFile(final String fileName, final ClientTracer tracer) {
        final RadixLoader loader = RadixLoader.getInstance();
        if (loader==null){
            return null;
        }
        final RevisionMeta revision = loader.getCurrentRevisionMeta();
        if (revision==null){
            return null;
        }
        final FileMeta fileMeta = revision.findOverFile(fileName);
        if (fileMeta==null){
            return null;
        }
        final byte[] translation;
        try{
            translation = loader.readFileData(fileMeta, revision);
        }catch(IOException exception){
            if (tracer!=null){
                tracer.error("Failed to extract repository file: \"" +fileName+ "\"",exception);
            }
            return null;
        }
        final File tmpFile = loader.createTempFile("explorer_translation");
        final String filePath = tmpFile.getAbsolutePath();
        try{
            FileUtils.writeBytes(tmpFile, translation);
        }catch(IOException exception){
            if (tracer!=null){
                tracer.error("Failed to write temporary translation file: \"" +filePath+ "\"",exception);
            }
            return null;
        }
        return filePath;
    }
    
    private static String extractResourceTranslationFile(final String fileName, final ClientTracer tracer) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL url = classLoader.getResource(fileName);
        if (url != null) {
            if (tracer!=null){
                tracer.debug("Trying to extract translation file \"" + fileName + "\" from " + url.toExternalForm(), false);
            }
            final File tempFile = createTemporaryFile(tracer);
            if (tempFile == null) {
                return null;
            }
            final FileOutputStream oStream = openTemporaryFile(tempFile, tracer);
            if (oStream == null) {
                return null;
            }
            final String filePath = tempFile.getAbsolutePath();
            //opening translation file
            final InputStream iStream;
            try {
                iStream = url.openStream();
            } catch (IOException ex) {
                if (tracer!=null){
                    tracer.error("Can`t extract file: \"" + url.toExternalForm() + "\" into temporary file: \"" + filePath + "\":\n" + ex.getMessage());
                }                
                return null;
            }
            //extracting translations file
            try {
                FileUtils.copyStream(iStream, oStream);
                oStream.flush();
                return filePath;
            } catch (IOException ex) {
                if (tracer!=null){
                    tracer.error("Can`t extract file: \"" + url.toExternalForm() + "\" into temporary file: \"" + filePath + "\":\n" + ex.getMessage());
                }                
                return null;
            } finally {
                try {
                    iStream.close();
                } catch (IOException ex) {//NOPMD
                }
                try {
                    oStream.close();
                } catch (IOException ex) {//NOPMD
                }
            }
        } else {
            if (tracer!=null){
                tracer.debug("Translation file \"" + fileName + "\" was not found localy", false);
            }            
        }
        return null;
    }
    
    private static QTranslator applyTranslationsFile(final String fileName, final ClientTracer tracer) {
        if (tracer!=null){
            tracer.debug("Trying to load  translation file \"" + fileName + "\"", false);
        }
        final QTranslator translator = new QTranslator();        
        if (translator.load(fileName) && !translator.isEmpty()) {
            return translator;
        }
        return null;
    }

    private static String loadByHttp(final String filePath, final URL url, final ClientTracer tracer) {
        if (tracer!=null){
            tracer.debug("Trying to load translation file \"" + filePath + "\" from " + url.toString(), false);
        }
        final File tempFile = createTemporaryFile(tracer);
        if (tempFile == null) {
            return null;
        }
        final FileOutputStream oStream = openTemporaryFile(tempFile, tracer);
        if (oStream == null) {
            return null;
        }
        final String resultFilePath = tempFile.getAbsolutePath();
        final HttpLoader loader = new HttpLoader(null);
        try {
            if (loader.download(url + filePath, oStream) > 0) {
                oStream.flush();
                return resultFilePath;
            } else {
                return null;
            }
        } catch (IOException ex) {
            if (tracer!=null){
                tracer.warning("Can`t load translation file \"" + filePath + "\" from " + url.toString() + ":\n " + ex.getMessage());
            }
            return null;
        }
    }    
    
    private static File createTemporaryFile(final ClientTracer tracer) {
        //creating temp file
        final File tempFile;
        try {
            tempFile = File.createTempFile("translation", ".qm");
        } catch (IOException ex) {
            if (tracer!=null){
                tracer.error("Can`t create temporary file: " + ex.getMessage());
            }
            return null;
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    private static FileOutputStream openTemporaryFile(final File tempFile, final ClientTracer tracer) {
        //opening temp file
        try {
            return new FileOutputStream(tempFile);
        } catch (IOException ex) {
            if (tracer!=null){
                tracer.error("Can`t write into temporary file: \"" + tempFile.getAbsolutePath() + "\":\n" + ex.getMessage());
            }            
            return null;
        }
    }    
    
}
