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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
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

        
        @Override
        protected Set<Id> getUsedStringsIds(EScope scope, boolean apiMode) {
            java.util.Set<Id> usedMlStringIds = super.getUsedStringsIds(scope, apiMode);
            Module module = getModule();
            if (module != null){
                final Collection<ILocalizedDef.MultilingualStringInfo> stringInfos = new ArrayList<>();
                module.collectUsedMlStringIds(stringInfos);
                
                if (!stringInfos.isEmpty()) {
                    if (usedMlStringIds == null) {
                        usedMlStringIds = new HashSet<>();
                    }
                    
                    for (ILocalizedDef.MultilingualStringInfo info : stringInfos) {
                        usedMlStringIds.add(info.getId());
                    }

                }
            }
            return usedMlStringIds;
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
        synchronized (stringLoadLock) {
            if (wasLoaded) {
                return;
            }
            List<Layer> layers = getLayers();
            if (layers.isEmpty()) {
                return;
            }
            wasLoaded = true;
            strings = new Strings();
            
            for (Layer l : layers) {
                for (EIsoLanguage lang : l.getLanguages()) {
                    try {
                        final File file = getFile(lang, l, false);
                        if (file != null && file.exists() && file.isFile()) {
                            try {
                                AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(file);
                                if (xDoc.getAdsDefinition() != null && xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition() != null) {
                                    strings.loadFrom(xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition());
                                }
                            } catch (IOException | XmlException e) {
                                throw new RadixError("Unable to load localization for DDS module " + getModule().getQualifiedName()
                                        + " (file - " + file.getAbsolutePath() + ")", e);
                            }
                        }
                    } catch (IOException ex) {
                        throw new RadixError("Unable to load localization for DDS module " + getModule().getQualifiedName(), ex);
                    }
                }
            }
            ILocalizingBundleDef.version.incrementAndGet();
            if (isSaveable() && !isReadOnly() && isInBranch()) {
                setEditState(EEditState.MODIFIED);
                setEditState(EEditState.NONE);
            }
        }
    }
    
    public void reloadStrings(){
        wasLoaded = false;
        uploadIfNesessary();
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

    public File getFile(final EIsoLanguage language, Layer layer, boolean create) throws IOException {
        if (language == null || layer == null) {
            return null;
        }
        final Module ownerModule = getModule();
        if (ownerModule == null || ownerModule.getDirectory() == null) {
            return null;
        }
        final File moduleDir = ownerModule.getDirectory();
        if (moduleDir == null) {
            return null;
        }
        if (layer.getLanguages().contains(language)) {
            if (layer.isLocalizing()) {
                File localizingLayerDir = layer.getDirectory();
                String licalizingModuleDir = localizingLayerDir.getPath() + File.separatorChar + moduleDir.getParentFile().getName() + File.separatorChar
                        + moduleDir.getName().substring(moduleDir.getName().indexOf(File.separatorChar) + 1) + File.separatorChar;
                String path = licalizingModuleDir + FileNames.DDS_LOCALE_DIR + File.separatorChar + language.getValue() + ".xml";
                if (create) {
                    File f = new File(licalizingModuleDir, Module.MODULE_XML_FILE_NAME);
                    FileUtils.copyFile(ownerModule.getFile(), f);
                }
                return new File(path);
            }

            File langDir = new File(moduleDir, FileNames.DDS_LOCALE_DIR);
            return new File(langDir, language.getValue() + ".xml");
        }
        
        return null;
    }
    
    
    public List<Layer> getLayers(){
        final List<Layer> layers = new ArrayList<>();
        final Module ownerModule = getModule();
        final Layer ownerLayer;
        if (ownerModule == null || (ownerLayer = ownerModule.getLayer()) == null) {
            return layers;
        }
        layers.add(ownerLayer);
        final Branch branch = ownerLayer.getBranch();
        if (branch == null) {
            return layers;
        }
        for (Layer l : branch.getLayers()) {
            if (l.isLocalizing() && l.getBaseLayerURIs().contains(ownerLayer.getURI())) {
                layers.add(l);
            }
        }
        return layers;
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
        final DdsModule module = getModule();
        if (module == null) {
            return;
        }
        List<Layer> layers = getLayers();
        for (Layer l : layers) {
            if (!l.isReadOnly()) {
                for (EIsoLanguage lang : l.getLanguages()) {
                    final File file = getFile(lang, l, true);
                    if (file != null) {

                        final AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                        final LocalizingBundleDefinition xBundle = xDoc.addNewAdsDefinition().addNewAdsLocalizingBundleDefinition();
                        appendTo(xBundle, lang);

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
                module.saveDirectoryXml(); // update digest
                if (l.isLocalizing()){
                    ((DdsSegment) l.getDds()).saveDirectoryXml();
                }
            }
        }
        setEditState(EEditState.NONE);
    }

    protected void appendTo(LocalizingBundleDefinition xDef) {
        xDef.setId(getId());
        strings.appendTo(xDef, false, null);
    }
    
    public void appendTo(LocalizingBundleDefinition xDef, EIsoLanguage lang) {
        xDef.setId(getId());
        strings.appendTo(xDef, false, lang);
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
    public boolean isReadOnly() {
        boolean result = super.isReadOnly();
        
        if (result){
            Layer layer = getLayer();
            if (layer != null){
                if (!layer.isReadOnly()){
                    result = false;
                } else {
                    Branch branch = layer.getBranch();
                    if (branch != null) {
                        for (Layer l : branch.getLayers()) {
                            if (l.isLocalizing() && l.getBaseLayerURIs().contains(getLayer().getURI()) && !l.isReadOnly()) {
                                result = false;
                            }
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    

    @Override
    public File getFile() {
        return null;
    }
    
    @Override
    public List<EIsoLanguage> getLanguages() {
        return strings.getLanguages();
    }
}
