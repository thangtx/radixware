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

package org.radixware.kernel.release.fs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsLocaleDefinition;
import static org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsLocaleDefinition.getBinLocaleFiles;
import static org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsLocaleDefinition.loadFilesFromBin;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class RfsRepositoryAdsLocaleDefinition extends RfsRepositoryAdsDefinition implements IRepositoryAdsLocaleDefinition {

    private Map<EIsoLanguage, IRepositoryAdsDefinition> repositories = new HashMap<>();

    public RfsRepositoryAdsLocaleDefinition(AdsDefinition ownerDef, ReleaseRepository release, String moduleDir) {
        super(ownerDef, release, moduleDir);
        loadRepositories();
    }

    private void loadRepositories() {
        String userDirName = RadixLoader.getInstance().getRoot().getAbsolutePath();
        calcMlsRepository(userDirName + File.separatorChar + moduleDir, false);
    }

    private void calcMlsRepository(String mDir, boolean isLocalizingLayer) {
        final File parent = new File(mDir);
        if (!parent.exists()) {
            return;
        }
        File mlsFile = new File(parent, AdsModule.LOCALE_DIR_NAME);
        File srcFile = new File(parent, AdsModule.SOURCES_DIR_NAME);
        Id mlbId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + pointer.getId().toString());
        if (mlsFile.exists()) {
            FSRepositoryAdsLocaleDefinition localeRepository = new FSRepositoryAdsLocaleDefinition(mlsFile, mlbId);
            if (!localeRepository.getRepositories().isEmpty()) {
                for (EIsoLanguage lang : localeRepository.getRepositories().keySet()) {
                    if (!repositories.containsKey(lang)) {
                        repositories.put(lang, localeRepository);
                    }
                }
            } else if (!isLocalizingLayer) {
                mlsFile = new File(srcFile, EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + pointer.getId().toString() + ".xml");
                if (mlsFile.exists()) {
                    repositories.put(null, new FSRepositoryAdsDefinition(mlsFile));
                }
            }
        } else if (!isLocalizingLayer && srcFile.exists()) {
            if (repositories.isEmpty()) {
                mlsFile = new File(srcFile, EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + pointer.getId().toString() + ".xml");
                if (mlsFile.exists()) {
                    repositories.put(null, new FSRepositoryAdsDefinition(mlsFile));
                }
            }
        } else {
            try {
                //File mlsFile = new File(parent, AdsModule.LOCALE_DIR_NAME);
                //File srcFile = new File(parent, AdsModule.SOURCES_DIR_NAME);
                //Id mlbId=Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + pointer.getId().toString());

                loadFilesFromBin(mDir, mlbId, repositories);
            } catch (IOException ex) {
                try {
                    Logger.getLogger(RfsRepositoryAdsLocaleDefinition.class.getName()).log(Level.FINE, null, ex);
                } catch (RuntimeException e) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, e.getMessage(), e);
                }
            }
        }
        if (!isLocalizingLayer) {
            checkLocalizingLayers();
        }
    }

    public void loadFilesFromBin(String mDir, Id id, Map<EIsoLanguage, IRepositoryAdsDefinition> repositories) throws IOException {
        RevisionMeta revMeta = ((RadixClassLoader) pointer.getClass().getClassLoader()).getRevisionMeta();

        File binFile = new File(mDir, AdsModule.BINARIES_DIR_NAME);
        if (binFile.exists()) {
            File[] langDirs = FSRepositoryAdsLocaleDefinition.getBinLocaleFiles(binFile);
            if (langDirs.length == 0) {
                for (String lang : revMeta.getLanguages()) {
                    final StringBuilder sbFileName = new StringBuilder(AdsModule.LOCALE_DIR_NAME);
                    sbFileName.append("-").append(lang).append(".jar");
                    FileMeta fileMeta = ((RadixClassLoader) pointer.getClass().getClassLoader()).getRevisionMeta().findFile((moduleDir + AdsModule.BINARIES_DIR_NAME + "/" + sbFileName).replace("\\", "/"));
                    if (fileMeta != null) {
                        byte[] fileData = RadixLoader.getInstance().readFileData(fileMeta, revMeta);
                        JarInputStream in = new JarInputStream(new ByteArrayInputStream(fileData));
                        try {
                            for (;;) {
                                JarEntry entry = in.getNextJarEntry();
                                if (entry == null) {
                                    break;
                                }
                                String entrieName = entry.getName();
                                int index = entrieName.lastIndexOf("/");
                                String name = id.toString() + ".xml";
                                if (entrieName.substring(index + 1).equals(name)) {
                                    File tempFile = RadixLoader.getInstance().createTempFile(binFile.getName());
                                    FileOutputStream xmlOut = new FileOutputStream(tempFile);
                                    try {
                                        FileUtils.copyStream(in, xmlOut);
                                        try {
                                            EIsoLanguage language = EIsoLanguage.getForValue(lang);
                                            if (lang != null) {
                                                repositories.put(language, new FSRepositoryAdsDefinition(tempFile));
                                            }
                                        } catch (NoConstItemWithSuchValueError ex) {
                                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                                        }
                                    } finally {
                                        try {
                                            xmlOut.close();
                                        } catch (IOException ex) {
                                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                                        }
                                    }

                                    break;
                                }
                            }
                        } finally {
                            try {
                                in.close();
                            } catch (IOException ex) {
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                    }
                }
            } else {
                FSRepositoryAdsLocaleDefinition.loadFilesFromBin(langDirs, id, repositories);
            }
        }

        //File binFile = new File(moduleDir, AdsModule.BINARIES_DIR_NAME);
        // if (binFile.exists()) {
        //    File[] langDirs = FSRepositoryAdsLocaleDefinition.getBinLocaleFiles(binFile);
        //    for (File langDir : langDirs) {
        //  try {
        //      JarFile jf = new JarFile(langDir);
        //       try {
          /*              Enumeration<JarEntry> entries = jf.entries();
         while (entries.hasMoreElements()) {

         JarEntry entry = entries.nextElement();
         String entrieName = entry.getName();
         int index = entrieName.lastIndexOf("/");
         String name = id.toString() + ".xml";
         if (entrieName.substring(index + 1).equals(name)) {
         File file = new File(jf.getName(), entry.getName());
         EIsoLanguage lang=getLangFromJarName(jf.getName());
         if(lang!=null){
         repositories.put(lang,new FSRepositoryAdsDefinition(file));
         }      
         break;
         } 
         }*/
        //    } finally {
        //           if (jf != null) {
        //                 try {
        //                    jf.close();
        //                } catch (IOException e) {
        //                 }
        //            }
        //            }
        //        } catch (Throwable ex) {
        //            ex.printStackTrace();
        //         }
        //   }
        //  }
    }

    private void checkLocalizingLayers() {
        final String s = pointer.getLayer().getURI();
        String dirName = RadixLoader.getInstance().getRoot().getAbsolutePath();
        File branchDir = new File(dirName);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                try {
                    String str = s + Layer.LOCALE_LAYER_SUFFIX;
                    return name.startsWith(str) && EIsoLanguage.getForValue(name.substring(str.length())) != null;
                } catch (NoConstItemWithSuchValueError ex) {
                    return false;
                }
            }
        };
        for (File layerDir : branchDir.listFiles(filter)) {
            String licalizingModuleDir = layerDir.getPath() + File.separatorChar + moduleDir.substring(moduleDir.indexOf(File.separatorChar) + 1);
            calcMlsRepository(licalizingModuleDir, true);
        }
    }

    @Override
    public Id getId() {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + pointer.getId().toString());
    }

    @Override
    public AdsDefinition getPreLoadedDefinition() {
        return null;
    }

    @Override
    public String getName() {
        return pointer.getName() + " Multilingual Bundle";
    }

    @Override
    public EDefType getType() {
        return EDefType.LOCALIZING_BUNDLE;
    }

    /*  @Override
     public AdsDefinition.ESaveMode getUploadMode() {
     return AdsDefinition.ESaveMode.NORMAL;
     }    */
    @Override
    public Map<EIsoLanguage, IRepositoryAdsDefinition> getRepositories() {
        return repositories;
    }
}
