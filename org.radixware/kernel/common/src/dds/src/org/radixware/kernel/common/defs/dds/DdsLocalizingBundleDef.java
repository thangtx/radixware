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

package org.radixware.kernel.common.defs.dds;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;


public class DdsLocalizingBundleDef extends DdsDefinition implements ILocalizingBundleDef<DdsMultilingualStringDef> {

    private class Strings extends ILocalizingBundleDef.Strings<DdsMultilingualStringDef> {

        public Strings() {
            super(DdsLocalizingBundleDef.this);
        }

        @Override
        protected IMultilingualStringDef loadString(LocalizedString xDef) {
            return new DdsMultilingualStringDef(xDef);
        }

        @Override
        protected void appendTo(LocalizingBundleDefinition xDef, boolean apiMode, EIsoLanguage lang) {
            super.appendTo(xDef, apiMode, lang);
        }

        @Override
        protected HierarchyIterator<? extends ExtendableDefinitions<DdsMultilingualStringDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return null;
        }
    }
    private Strings strings = new Strings();

    public DdsLocalizingBundleDef(DdsModelDef model) {
        super(Id.Factory.loadFrom("mlb" + model.getId().toString()), "StringBundle");
        setContainer(model);
    }

    @Override
    public ILocalizingBundleDef.Strings getStrings() {
        uploadIfNesessary();
        return strings;
    }
    private volatile boolean wasLoaded = false;
    private final Object stringLoadLock = new Object();

    private void uploadIfNesessary() {
        final Layer ownerLayer;
        synchronized (stringLoadLock) {
            if (wasLoaded) {
                return;
            }
            ownerLayer = getLayer();
            if (ownerLayer == null) {
                return;
            }
            wasLoaded = true;
            strings = new Strings();
            for (EIsoLanguage lang : ownerLayer.getLanguages()) {
                final File file = getFile(lang);
                if (file != null && file.exists() && file.isFile()) {
                    try {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(file);
                        if (xDoc.getAdsDefinition() != null && xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition() != null) {
                            strings.loadFrom(xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition());
                        }
                    } catch (IOException | XmlException e) {
                        throw new RadixError("Unable to load localization for DDS module " + getModule().getQualifiedName());
                    }
                }
            }
            if (isSaveable()) {
                setEditState(EEditState.NONE);
            }
        }
    }

    @Override
    public Definition findBundleOwner() {
        return getOwnerModel();
    }

    @Override
    public SearchResult findOverwrittenFor(Definition def) {
        Module module = def.getModule();
        if (module instanceof DdsModule) {
            DdsModule ovr = (DdsModule) module.findOverwritten();
            if (ovr != null) {
                try {
                    return SearchResult.single(ovr.getModelManager().getModel());
                } catch (IOException ex) {
                    Logger.getLogger(DdsLocalizingBundleDef.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return SearchResult.empty();

    }

    public File getFile(EIsoLanguage language) {
        if (language == null) {
            return null;
        }
        final Module ownerModule = getModule();
        if (ownerModule == null) {
            return null;
        }
        final File moduleDir = ownerModule.getDirectory();
        if (moduleDir == null) {
            return null;
        }
        final File localeDir = new File(moduleDir, "locale");
        return new File(localeDir, language.getValue().toString() + ".xml");
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        getStrings().visit(visitor, provider);
    }

    @Override
    public String getName() {
        return "DdsModuleLocalizingBundle";
    }

    @Override
    public IMultilingualStringDef createString(ELocalizedStringKind kind) {
        return new DdsMultilingualStringDef();
    }

    @Override
    public void save() throws IOException {
        final Layer ownerLayer = getLayer();
        if (ownerLayer == null) {
            return;
        }
        for (EIsoLanguage lang : ownerLayer.getLanguages()) {
            final File file = getFile(lang);
            if (file != null) {

                final AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                final LocalizingBundleDefinition xBundle = xDoc.addNewAdsDefinition().addNewAdsLocalizingBundleDefinition();
                xBundle.setId(getId());

                strings.appendTo(xBundle, false, lang);

                boolean noStrings = xBundle.getStringList().isEmpty();

                if (noStrings && !file.exists()) {
                    continue;
                }
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw new IOException("Unable to save localized information from DDS module " + getModule().getQualifiedName());
                }

                XmlFormatter.save(xDoc, file);
            }
        }
        setEditState(EEditState.NONE);
    }

    protected void appendTo(LocalizingBundleDefinition xDef) {
        xDef.setId(getId());
        strings.appendTo(xDef, false, null);
    }

    protected void loadFrom(LocalizingBundleDefinition xDef) {
        synchronized (this) {
            if (xDef != null) {
                strings.loadFrom(xDef);
            }
            wasLoaded = true;
        }
    }

    @Override
    public boolean isSaveable() {
        DdsModelDef model = getOwnerModel();
        try {
            return model != null && getModule().getModelManager().getModifiedModel() != model;
        } catch (IOException ex) {
            Logger.getLogger(DdsLocalizingBundleDef.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public File getFile() {
        return null;
    }
}
