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
package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;

public class FSRepositoryAdsLocaleDefinition extends FSRepositoryAdsDefinition implements IRepositoryAdsLocaleDefinition {

    private Map<EIsoLanguage, IRepositoryAdsDefinition> repositories = new HashMap<>();
    private final Id defId;
    private final static String JAR_NAME_PREFIX = "locale-";
    private final static String JAR_SUFFIX = ".jar";

    public FSRepositoryAdsLocaleDefinition(AdsLocalizingBundleDef def, boolean fromAPI) {
        super(def, false);
        defId = def.getId();
        loadRepositories();
    }

    public FSRepositoryAdsLocaleDefinition(File file, Id defId) {
        super(file);
        this.defId = defId;
        loadRepositories();
    }

    /* public FSRepositoryAdsLocaleDefinition(File file, Id defId,EIsoLanguage language) {
     super(file);
     this.defId = defId;
     loadRepositories(language);
     }*/
    @Override
    protected String getSrcDirShortName(AdsDefinition.ESaveMode saveMode) {
        return AdsModule.LOCALE_DIR_NAME;
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    public void add(IRepositoryAdsDefinition repository, EIsoLanguage lang) {
        repositories.put(lang, repository);
    }

    @Override
    public Map<EIsoLanguage, IRepositoryAdsDefinition> getRepositories() {
        if (repositories == null) {
            loadRepositories();
        }
        return repositories;
    }

    @Override
    public Id getId() {
        return defId;
    }

    @Override
    public File getFile() {
        return getFileImpl(getUploadMode());
    }

    private void loadRepositories() {
        File localeDir = getFile();
        if (localeDir != null) {
            if (localeDir.exists()) {
                File[] files = localeDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    try {
                        EIsoLanguage lang = EIsoLanguage.getForValue(files[i].getName());
                        if (files[i].isDirectory() && lang != null /*&&(language==null ||language.equals(lang))*/) {
                            File mlsFile = new File(files[i], getId().toString() + ".xml");
                            if (mlsFile.exists()) {
                                repositories.put(lang, new FSRepositoryAdsDefinition(mlsFile));
                            }
                        }
                    } catch (NoConstItemWithSuchValueError ex) {
                    }
                }
            } else {
                loadFilesFromBin(localeDir.getParentFile(), getId(), repositories);
            }
        }
    }

    public static void loadFilesFromBin(final File moduleDir, Id id, Map<EIsoLanguage, IRepositoryAdsDefinition> repositories) {
        File binFile = new File(moduleDir, AdsModule.BINARIES_DIR_NAME);
        if (binFile.exists()) {
            File[] langDirs = getBinLocaleFiles(binFile);
            loadFilesFromBin(langDirs, id, repositories);
        }
    }

    public static void loadFilesFromBin(File[] langDirs, Id id, Map<EIsoLanguage, IRepositoryAdsDefinition> repositories) {
        if (langDirs != null) {
            for (File langDir : langDirs) {
                try {
                    JarFile jf = new JarFile(langDir);
                    try {
                        Enumeration<JarEntry> entries = jf.entries();
                        while (entries.hasMoreElements()) {

                            JarEntry entry = entries.nextElement();
                            String entrieName = entry.getName();
                            int index = entrieName.lastIndexOf("/");
                            String name = id.toString() + ".xml";

                            if (entrieName.substring(index + 1).equals(name)) {
                                File file = new File(jf.getName(), entry.getName());
                                EIsoLanguage lang = getLangFromJarName(jf.getName());
                                if (lang != null) {
                                    repositories.put(lang, new FSRepositoryAdsDefinition(file));
                                }
                                break;
                            } else {
                            }
                        }
                    } finally {
                        if (jf != null) {
                            try {
                                jf.close();
                            } catch (IOException ex) {
                                Logger.getLogger(FSRepositoryAdsLocaleDefinition.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(FSRepositoryAdsLocaleDefinition.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    private static EIsoLanguage getLangFromJarName(String jarName) {
        int startIndex = jarName.lastIndexOf(JAR_NAME_PREFIX);
        int endIndex = jarName.lastIndexOf(JAR_SUFFIX);
        if (startIndex != -1 && endIndex != -1) {
            String langVal = jarName.substring(startIndex + JAR_NAME_PREFIX.length(), endIndex);
            try {
                return EIsoLanguage.getForValue(langVal);
            } catch (NoConstItemWithSuchValueError ex) {
                return null;
            }
        }
        return null;
    }

    public static File[] getBinLocaleFiles(File binFile) {
        File[] langDirs = binFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                final String prefix = JAR_NAME_PREFIX;
                final String sufix = JAR_SUFFIX;
                if (name.startsWith(prefix) && name.endsWith(sufix)) {
                    String sLang = name.substring(prefix.length(), name.length() - sufix.length());
                    try {
                        EIsoLanguage.getForValue(sLang);
                        return true;
                    } catch (NoConstItemWithSuchValueError ex) {
                        return false;
                    }
                }
                return false;
            }
        });
        return langDirs;
    }
}
