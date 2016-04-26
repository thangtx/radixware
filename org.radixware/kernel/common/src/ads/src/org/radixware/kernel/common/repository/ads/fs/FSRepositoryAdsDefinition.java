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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;

public class FSRepositoryAdsDefinition implements IRepositoryAdsDefinition {

    protected final File file;
    protected final AdsDefinition def;
    protected AdsLocalizingBundleDef bundleDef;
    private boolean wasLoadedFromAPI = false;

    public FSRepositoryAdsDefinition(final File file) {
        this.file = file;
        this.def = null;
    }

    public FSRepositoryAdsDefinition(final AdsDefinition def, boolean wasLoadedFromAPI) {
        this.file = null;
        this.def = def;
        this.wasLoadedFromAPI = wasLoadedFromAPI;
    }

    public void setBundle(final AdsLocalizingBundleDef bundleDef) {
        this.bundleDef = bundleDef;
    }

    @Override
    public File getFile() {
        return getFileImpl(getUploadMode());
    }

    public File getFileImpl(final AdsDefinition.ESaveMode saveMode) {
        if (file != null) {
            return file;
        } else {
            AdsModule module = def.getModule();
            if (module == null) {
                return null;
            } else {
                IRepositoryAdsModule repo = def.getModule().getRepository();
                if (repo == null) {
                    return null;
                } else {
                    File moduleDir = repo.getDirectory();
                    if (moduleDir == null) {
                        return null;
                    }
                    File srcDir = new File(moduleDir, getSrcDirShortName(saveMode));
                    if (def instanceof AdsLocalizingBundleDef) {
                        return srcDir;
                    }
                    return new File(srcDir, def.getId().toString() + ".xml");
                }
            }
        }
    }

    @Override
    public InputStream getData() throws FileNotFoundException, IOException {
        final File defFile = getFileImpl(getUploadMode());
        if (defFile != null) {
            if (defFile.exists()) {
                return new BufferedInputStream(new FileInputStream(defFile));
            } else {
                try {
                    final int index = defFile.getAbsolutePath().indexOf(".jar");
                    if (index >= 0) {
                        final JarFile jar = new JarFile(defFile.getAbsolutePath().substring(0, index + 4));
                        try {
                            final JarEntry entry = jar.getJarEntry(defFile.getAbsolutePath().substring(index + 5, defFile.getAbsolutePath().length()).replace(File.separatorChar, '/'));
                            if (entry != null) {
                                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                                final InputStream entryStream = jar.getInputStream(entry);
                                try {
                                    FileUtils.copyStream(entryStream, out);
                                    return new ByteArrayInputStream(out.toByteArray());
                                } finally {
                                    try {
                                        entryStream.close();
                                    } catch (IOException ex) {
                                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                                    }
                                }
                            }
                        } finally {
                            try {
                                jar.close();
                            } catch (IOException ex) {
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw e;
                }
            }
        } else {
            System.out.println("DEF FILE IS NULL FOR " + getId().toString());
        }
        return null;
    }

    @Override
    public IRepositoryAdsDefinition getMlsRepository() {
        if (bundleDef != null) {
            return new FSRepositoryAdsLocaleDefinition(bundleDef, bundleDef.wasLoadedFromAPI());
        }
        FSRepositoryAdsLocaleDefinition localeRepository = null;
        final File definitionFile = getFile();
        if (definitionFile != null) {
            final File srcDir = definitionFile.getParentFile();
            final File moduleDir = srcDir.getParentFile();
            File mlsDir = new File(moduleDir, AdsModule.LOCALE_DIR_NAME);

            Id mlbId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + getId().toString());
            localeRepository = new FSRepositoryAdsLocaleDefinition(mlsDir, mlbId);
            if (localeRepository.getRepositories().isEmpty()) {
                File mlsFile = new File(srcDir, EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + definitionFile.getName());
                if (mlsFile.exists()) {
                    localeRepository.add(new FSRepositoryAdsDefinition(mlsFile), null);
                }
            }
            checkLocalizingLayers(moduleDir, mlbId, localeRepository);
        }
        return localeRepository;
    }

    private void checkLocalizingLayers(final File moduleFile, Id mlbId, final FSRepositoryAdsLocaleDefinition localeRepository) {
        final File adsFile = moduleFile.getParentFile();
        final File layerFile = moduleFile.getParentFile().getParentFile();
        if (layerFile != null) {
            File branchDir = layerFile.getParentFile();
            if (branchDir != null) {
                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        try {
                            String str = layerFile.getName() + Layer.LOCALE_LAYER_SUFFIX;
                            return name.startsWith(str) && EIsoLanguage.getForValue(name.substring(str.length())) != null;
                        } catch (NoConstItemWithSuchValueError ex) {
                            return false;
                        }
                    }
                };
                for (File layerDir : branchDir.listFiles(filter)) {
                    String licalizingModuleDir = layerDir.getPath() + File.separatorChar + adsFile.getName() + File.separatorChar + moduleFile.getName().substring(moduleFile.getName().indexOf(File.separatorChar) + 1);
                    loadRepositoryFromLocalizingLayer(new File(licalizingModuleDir), mlbId, localeRepository);
                }
            }
        }
    }

    private void loadRepositoryFromLocalizingLayer(final File moduleDir, Id mlbId, FSRepositoryAdsLocaleDefinition localeRepository) {
        if (moduleDir.exists()) {
            File mlsDir = new File(moduleDir, AdsModule.LOCALE_DIR_NAME);
            if (mlsDir.exists()) {
                FSRepositoryAdsLocaleDefinition licalizingRepository = new FSRepositoryAdsLocaleDefinition(mlsDir, mlbId);
                if (!licalizingRepository.getRepositories().isEmpty()) {
                    for (EIsoLanguage lang : licalizingRepository.getRepositories().keySet()) {
                        if (!localeRepository.getRepositories().containsKey(lang)) {
                            localeRepository.add(licalizingRepository.getRepositories().get(lang), lang);
                        }
                    }
                } /*else {
                 File mlsFile = new File(moduleDir, mlbId.toString()+".xml");
                 if (mlsFile.exists()) {
                 localeRepository.add(new FSRepositoryAdsDefinition(mlsFile));
                 }
                 }*/

            }
        }
    }

    @Override
    public File getFile(ESaveMode saveMode) {
        return getFileImpl(saveMode);
    }

    @Override
    public ESaveMode getUploadMode() {
        if (wasLoadedFromAPI) {
            return ESaveMode.API;
        }

        if (def != null) {
            if (def.getModule() != null && (def.getModule().isUserModule() || def.getModule().getSegmentType() == ERepositorySegmentType.UDS)) {
                return ESaveMode.NORMAL;
            } else {
                return def.getSaveMode();
            }
        } else {
            return ESaveMode.NORMAL;
        }
    }

    @Override
    public String getPath() {
        File defFile = getFile();

        if (defFile != null) {
            return defFile.getAbsolutePath();
        } else {
            return "";
        }
    }

    protected String getSrcDirShortName(ESaveMode saveMode) {
        return saveMode == AdsDefinition.ESaveMode.API ? AdsModule.STRIP_SOURCES_DIR_NAME : AdsModule.SOURCES_DIR_NAME;
    }

    @Override
    public Id getId() {
        if (def != null) {
            return def.getId();
        } else {
            return AdsDefinition.fileName2DefinitionId(file);
        }
    }

    @Override
    public String getName() {
        if (def != null) {
            return def.getName();
        } else {
            return null;
        }
    }

    @Override
    public EDefType getType() {
        if (def != null) {
            return def.getDefinitionType();
        } else {
            return null;
        }
    }

    @Override
    public ERuntimeEnvironmentType getEnvironment() {
        if (def != null) {
            return def.getUsageEnvironment();
        } else {
            return null;
        }
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public AdsDefinition getPreLoadedDefinition() {
        return def;
    }

    @Override
    public boolean isPublished() {
        if (def != null) {
            return def.isPublished();
        } else {
            return true;
        }
    }

    @Override
    public EAccess getAccessMode() {
        if (def != null) {
            return def.getAccessMode();
        }
        return null;

    }

    @Override
    public boolean willLoadFromAPI() {
        return wasLoadedFromAPI;
    }
}
