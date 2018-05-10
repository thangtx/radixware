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

package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


class AdsModuleUploader extends ModuleUploader<AdsModule> {

    public AdsModuleUploader(AdsModule module) {
        super(module);
    }

    @Override
    public AdsDefinition uploadChild(File file) throws IOException {
        if (Utils.equals(file.getParentFile(), getModule().getImages().getDirectory())) {
            FSRepositoryAdsImageDefinition rep = new FSRepositoryAdsImageDefinition(file);
            final AdsImageDef imageDef = getModule().getImages().addFromRepository(rep);
            return imageDef;
        } else {
            FSRepositoryAdsDefinition rep = null;
            if (getModule().getLayer().isLocalizing()) {
                try {
                    File parentFile = file.getParentFile();
                    EIsoLanguage lang = EIsoLanguage.getForValue(parentFile.getName());
                    if (lang != null) {
                        String fileName = file.getName();
                        int index = fileName.indexOf(".xml");
                        if (index != -1) {
                            fileName = new String(file.getName().substring(0, index));
                        }
                        rep = new FSRepositoryAdsLocaleDefinition(parentFile.getParentFile(), Id.Factory.loadFrom(fileName));
                    }
                } catch (NoConstItemWithSuchValueError ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            } else {
                rep = new FSRepositoryAdsDefinition(file);
            }
            final AdsDefinition def = rep == null ? null : getModule().getDefinitions().addFromRepository(rep);
            return def;
        }
    }

    private void loadNewDefinitions() {
        AdsModule module = getModule();
        IRepositoryAdsModule rep = module.getRepository();
        if (rep != null) {
            rep.setModule(module);
            IRepositoryAdsDefinition[] defs = rep.listDefinitions();
            if (defs != null) {
                for (IRepositoryAdsDefinition def : defs) {
                    File defFile = def.getFile();
                    if (defFile != null) {
                        if (def instanceof IRepositoryAdsLocaleDefinition) {
                            loadLocalizingDefs((IRepositoryAdsLocaleDefinition) def);
                        } else {
                            final Id id = AdsDefinition.fileName2DefinitionId(defFile);
                            if (module.getDefinitions().findById(id) == null) {
                                tryToUploadChild(defFile, Definition.DEFINITION_TYPE_TITLE);
                            }
                        }
                    }
                }
            }
        }
    }

    private void loadLocalizingDefs(IRepositoryAdsLocaleDefinition def) {
        for (IRepositoryAdsDefinition localeDef : def.getRepositories().values()) {
            if (localeDef instanceof IRepositoryAdsLocaleDefinition) {
                loadLocalizingDefs((IRepositoryAdsLocaleDefinition) localeDef);
            } else {
                File defLocaleFile = localeDef.getFile();
                if (defLocaleFile != null && defLocaleFile.exists()) {
                    final Id id = AdsDefinition.fileName2DefinitionId(defLocaleFile);
                    if (getModule().getDefinitions().findById(id) == null) {
                        tryToUploadChild(defLocaleFile, Definition.DEFINITION_TYPE_TITLE);
                    }
                }
            }
        }
    }

    private void loadNewImageDefs() {
        AdsModule module = getModule();
        final File[] files = module.getImages().collectImageDefFiles();
        if (files != null) {
            for (File file : files) {
                final Id id = AdsDefinition.fileName2DefinitionId(file);
                if (module.getImages().findById(id) == null) {
                    tryToUploadChild(file, "Image");
                }
            }
        }
    }

    @Override
    public void updateChildren() {
        if (!getModule().isAutoAploadAllowed()) {
            return;
        }
        final ModuleDefinitions definitions = getModule().getDefinitionsIfLoaded();
        if (definitions != null) {
            for (AdsDefinition definition : definitions) {
                final AdsDefinitionUploader definitionUpdater = new AdsDefinitionUploader(definition);
                definitionUpdater.update();
            }
            loadNewDefinitions();
            definitions.closeExternallyModifiedLocalizingBundles();
        }

        final ModuleImages imageDefs = getModule().getImagesIfLoaded();
        if (imageDefs != null) {
            for (AdsImageDef imageDef : imageDefs) {
                final AdsImageUploader imageUpdater = new AdsImageUploader(imageDef);
                imageUpdater.update();
            }
            loadNewImageDefs();
        }
    }
}
