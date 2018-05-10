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
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.explorer.Explorer;
import org.radixware.kernel.explorer.utils.TranslationsFile;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;


class ExplorerLocaleManager extends org.radixware.kernel.common.client.env.LocaleManager{

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
        final boolean localeChanged = newLanguage != getLanguage() || country!=getCountry();
        final boolean languageChanged = newLanguage != getLanguage();
        if (localeChanged) {
            if (languageChanged){
                if (appTranslations != null) {
                    com.trolltech.qt.gui.QApplication.removeTranslator(appTranslations);
                    appTranslations = null;
                }
                if (qtTranslations != null) {
                    com.trolltech.qt.gui.QApplication.removeTranslator(qtTranslations);
                    qtTranslations = null;
                }
                if (newLanguage != EIsoLanguage.ENGLISH) {                                        
                    final TranslationsFile appTranslationsFile = TranslationsFile.getApplicationTranslations(newLanguage, codeBase);
                    appTranslations = loadTranslationsFile(appTranslationsFile);
                    if (appTranslations != null) {
                        final TranslationsFile qtTranslationsFile = TranslationsFile.getQtTranslations(newLanguage, codeBase);
                        qtTranslations = loadTranslationsFile(qtTranslationsFile);
                        final String languageName = newLanguage.getName();
                        if (qtTranslations != null) {
                            environment.getTracer().debug(languageName + " language was successfully installed", false);
                        } else {
                            environment.getTracer().error(languageName + " language was installed with errors");
                        }
                    }
                } else {
                    environment.getTracer().debug(EIsoLanguage.ENGLISH.getName() + " language was successfully installed", false);
                } 
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
            if (languageChanged){
                environment.afterLanguageChange(newLanguage);
            }
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

    private QTranslator loadTranslationsFile(final TranslationsFile file) {
        final QTranslator translator = file.loadTranslator(environment.getTracer());
        if (translator!=null){
            translator.setParent(Application.getInstance());
            com.trolltech.qt.gui.QApplication.installTranslator(translator);
        }
        return translator;
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
        TranslationsFile.clearCache();
    }

    private static boolean isTranslationFileExists(final EIsoLanguage language, final URL codeBase) {
        if (language == EIsoLanguage.ENGLISH) {
            return true;
        }
        final TranslationsFile file = TranslationsFile.getApplicationTranslations(language, codeBase);
        return file!=null && file.loadTranslator(null)!=null;
    }
}
