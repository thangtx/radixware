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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QLocale;
import com.trolltech.qt.core.QTranslator;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.Explorer;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;


class ExplorerLocaleManager extends org.radixware.kernel.common.client.env.LocaleManager{

    private final static String TR_APP_TRANSLATIONS_FILE_NAME = "explorer_%s.qm";
    private final static String TR_QT_TRANSLATIONS_FILE_NAME = "qt_%s.qm";
    private final static String TR_DIR_NAME = "translation";
    private final static String TR_FILE_PATH = "org/radixware/kernel/explorer/" + TR_DIR_NAME;    
    private final Environment environment;    
    private final URL codeBase;
    private QTranslator appTranslations, qtTranslations;

    ExplorerLocaleManager(final Environment environment, final URL codeBase) {
        this.environment = environment;
        this.codeBase = codeBase;        
    }

    @Override
    public void changeLocale(final EIsoLanguage lng, final EIsoCountry country) {
        EIsoLanguage newLanguage = lng != null ? lng : getSystemLanguage();
        if (newLanguage != getLanguage()) {
            if (appTranslations != null) {
                com.trolltech.qt.gui.QApplication.removeTranslator(appTranslations);
                appTranslations = null;
            }
            if (qtTranslations != null) {
                com.trolltech.qt.gui.QApplication.removeTranslator(qtTranslations);
                qtTranslations = null;
            }
            if (newLanguage != EIsoLanguage.ENGLISH) {
                final String appTranslationsFileName = String.format(TR_APP_TRANSLATIONS_FILE_NAME, newLanguage.getValue());
                final String languageName = newLanguage.getName();
                appTranslations = installTranslationFile(appTranslationsFileName, languageName, codeBase);
                if (appTranslations != null) {
                    final String qtTranslationsFileName = String.format(TR_QT_TRANSLATIONS_FILE_NAME, newLanguage.getValue());
                    qtTranslations = installTranslationFile(qtTranslationsFileName, languageName, codeBase);
                    if (qtTranslations != null) {
                        environment.getTracer().debug(languageName + " language was successfully installed", false);
                    } else {
                        environment.getTracer().error(languageName + " language was installed with errors");
                    }
                }
            } else {
                environment.getTracer().debug(EIsoLanguage.ENGLISH.getName() + " language was successfully installed", false);                
            }            
            final QLocale locale;
            if (country==null){
                locale = new QLocale(newLanguage.getValue());
            }
            else{
                locale = new QLocale(newLanguage.getValue()+"_"+country.getValue());
            }
            QLocale.setDefault(locale);
            if (environment.getMainWindow() instanceof QWidget) {
                ((QWidget)environment.getMainWindow()).setLocale(locale);
            }
            super.changeLocale(newLanguage, country);
            Locale.setDefault(getLocale());
            environment.afterLanguageChange(newLanguage);
        }
    }

    private EIsoLanguage getSystemLanguage() {
        final String languageName = com.trolltech.qt.core.QLocale.system().language().name();
        EIsoLanguage language;
        try {            
            language = EIsoLanguage.valueOf(EIsoLanguage.class, languageName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            language = EIsoLanguage.ENGLISH;
        }
        final List<EIsoLanguage> supportedLanguages = getSupportedLanguages();
        if (!supportedLanguages.contains(language) && !supportedLanguages.isEmpty()){
            language = supportedLanguages.get(0);
        }
        return language;
    }

    private QTranslator installTranslationFile(final String fileName, final String languageName, final URL codeBase) {
        String temporaryFile = extractTranslationFile(TR_FILE_PATH + "/" + fileName);
        QTranslator translator;
        if (temporaryFile != null) {
            translator = applyTranslationsFile(temporaryFile);
            if (translator != null) {
                return translator;
            }
            environment.getTracer().error("Error on installing translation for " + languageName.toLowerCase() + " language from translation file " + fileName);
        }
        if (codeBase != null) {
            final String filePath = TR_DIR_NAME + "/" + fileName;
            temporaryFile = loadByHttp(filePath, codeBase);
        }
        if (temporaryFile != null) {
            translator = applyTranslationsFile(temporaryFile);
            if (translator != null) {
                return translator;
            }
            environment.getTracer().error("Error on installing translation for " + languageName.toLowerCase() + " language - using english language");
        } else {
            environment.getTracer().warning("Can`t find translation for " + languageName.toLowerCase() + " language - using english language");
        }
        return null;
    }

    private QTranslator applyTranslationsFile(final String fileName) {
        environment.getTracer().debug("Trying to load  translation file \"" + fileName + "\"", false);
        final QTranslator translator = new QTranslator(Application.getInstance());
        if (translator.load(fileName) && !translator.isEmpty()) {
            com.trolltech.qt.gui.QApplication.installTranslator(translator);
            return translator;
        }
        return null;
    }

    private String loadByHttp(final String filePath, final URL url) {
        environment.getTracer().debug("Trying to load translation file \"" + filePath + "\" from " + url.toString(), false);        
        final File tempFile = createTemporaryFile();
        if (tempFile == null) {
            return null;
        }
        final FileOutputStream oStream = openTemporaryFile(tempFile);
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
            environment.getTracer().warning("Can`t load translation file \"" + filePath + "\" from " + url.toString() + ":\n " + ex.getMessage());
            return null;
        }
    }

    private File createTemporaryFile() {
        //creating temp file
        final File tempFile;
        try {
            tempFile = File.createTempFile("translation", ".qm");
        } catch (IOException ex) {
            environment.getTracer().error("Can`t create temporary file: " + ex.getMessage());
            return null;
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    private FileOutputStream openTemporaryFile(final File tempFile) {
        //opening temp file
        try {
            return new FileOutputStream(tempFile);
        } catch (IOException ex) {
            environment.getTracer().error("Can`t write into temporary file: \"" + tempFile.getAbsolutePath() + "\":\n" + ex.getMessage());
            return null;
        }
    }

    private String extractTranslationFile(final String fileName) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL url = classLoader.getResource(fileName);
        if (url != null) {
            environment.getTracer().debug("Trying to extract translation file \"" + fileName + "\" from " + url.toExternalForm(), false);
            final File tempFile = createTemporaryFile();
            if (tempFile == null) {
                return null;
            }
            final FileOutputStream oStream = openTemporaryFile(tempFile);
            if (oStream == null) {
                return null;
            }
            final String filePath = tempFile.getAbsolutePath();
            //opening translation file
            final InputStream iStream;
            try {
                iStream = url.openStream();
            } catch (IOException ex) {
                environment.getTracer().error("Can`t extract file: \"" + url.toExternalForm() + "\" into temporary file: \"" + filePath + "\":\n" + ex.getMessage());
                return null;
            }
            //extracting translations file
            try {
                FileUtils.copyStream(iStream, oStream);
                oStream.flush();
                return filePath;
            } catch (IOException ex) {
                environment.getTracer().error("Can`t extract file: \"" + url.toExternalForm() + "\" into temporary file: \"" + filePath + "\":\n" + ex.getMessage());
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
            environment.getTracer().debug("Translation file \"" + fileName + "\" was not found localy", false);
        }
        return null;
    }

    @Override
    public List<EIsoLanguage> getSupportedLanguages() {
        final List<EIsoLanguage> result = new LinkedList<>();
        if (Explorer.class.getClassLoader() instanceof RadixClassLoader) {
            final RadixClassLoader radixClassLoader = (RadixClassLoader) Explorer.class.getClassLoader();
            final Collection<String> languageNames = radixClassLoader.getRevisionMeta().getLanguages();
            for (String langageName : languageNames) {
                try {
                    result.add(EIsoLanguage.getForValue(langageName));
                } catch (NoConstItemWithSuchValueError error) {//NOPMD
                    //Unknown language name - ommiting
                }
            }
        } else {
            final EnumSet<EIsoLanguage> allLanguages = EnumSet.allOf(EIsoLanguage.class);
            for (EIsoLanguage lng : allLanguages) {
                if (isTranslationFileExists(lng,codeBase)) {
                    result.add(lng);
                }
            }
        }
        if (result.isEmpty()) {
            result.add(EIsoLanguage.ENGLISH);
        }

        return result;
    }
    
    public void clear(){
        if (appTranslations != null) {
            com.trolltech.qt.gui.QApplication.removeTranslator(appTranslations);
            appTranslations = null;
        }
        if (qtTranslations != null) {
            com.trolltech.qt.gui.QApplication.removeTranslator(qtTranslations);
            qtTranslations = null;
        }
    }

    private static boolean isTranslationFileExists(final EIsoLanguage language, final URL codeBase) {
        if (language == EIsoLanguage.ENGLISH) {
            return true;
        }
        final String appTranslationsFileName = String.format(TR_APP_TRANSLATIONS_FILE_NAME, language.getValue());
        {
            final String filePath = TR_FILE_PATH + "/" + appTranslationsFileName;
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader.getResource(filePath) != null) {
                return true;
            }
        }
        if (codeBase != null) {
            final String filePath = TR_DIR_NAME + "/" + appTranslationsFileName;
            try {
                final URL url = new URL(codeBase + filePath);
                final java.net.URLConnection connection = url.openConnection();
                return connection.getContentLength() > 0;
            } catch (MalformedURLException exception) {
                return false;
            } catch (IOException exception) {
                return false;
            }
        }
        return false;
    }
}
