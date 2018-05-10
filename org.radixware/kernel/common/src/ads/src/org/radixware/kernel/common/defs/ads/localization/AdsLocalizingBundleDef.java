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
package org.radixware.kernel.common.defs.ads.localization;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;

import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.localization.AdsLocalizingBundleWriter;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;

/**
 * An implementation for radix localization subsystem
 *
 */
public class AdsLocalizingBundleDef extends AdsDefinition implements IJavaSource, ILocalizingBundleDef<AdsMultilingualStringDef> {

    public static final AdsLocalizingBundleDef NO_BUNDLE = new AdsLocalizingBundleDef(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE));

    @Override
    public boolean needsDocumentation() {
        return false;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsLocalizingBundleWriter(this, AdsLocalizingBundleDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(getUsageEnvironment());
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                return sc == ERuntimeEnvironmentType.COMMON ? EnumSet.of(CodeType.META) : null;
            }
        };
    }

    public static final class Factory {

        public static AdsLocalizingBundleDef loadFrom(LocalizingBundleDefinition xDef) {
            return new AdsLocalizingBundleDef(xDef);
        }

        public static AdsLocalizingBundleDef loadFrom(Id ownerId, LocalizingBundleDefinition xDef) {
            return new AdsLocalizingBundleDef(ownerId, xDef);
        }

        public static AdsLocalizingBundleDef newInstance(Id ownerId) {
            return new AdsLocalizingBundleDef(ownerId);
        }
    }

    public class Strings extends ILocalizingBundleDef.Strings<AdsMultilingualStringDef> {

        public Strings() {
            super(AdsLocalizingBundleDef.this);
        }

        private void appendTo(LocalizingBundleDefinition xDef, ESaveMode saveMode, EIsoLanguage lang) {
            super.appendTo(xDef, saveMode == ESaveMode.API, lang);
        }

        private List<IMultilingualStringDef> getUsedStrings(ESaveMode saveMode, EScope scope) {
            return super.getUsedStrings(scope, saveMode == ESaveMode.API);
        }

        @Override
        public void loadFrom(LocalizingBundleDefinition xDef) {
            super.loadFrom(xDef);
        }

        @Override
        protected HierarchyIterator<ExtendableDefinitions<AdsMultilingualStringDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new StringsHierarchyIterator(scope, mode);
        }

        @Override
        protected IMultilingualStringDef loadString(LocalizedString xDef) {
            return AdsMultilingualStringDef.Factory.loadFrom(xDef);
        }

        private class StringsHierarchyIterator extends HierarchyIterator<ExtendableDefinitions<AdsMultilingualStringDef>> {

            private final HierarchyIterator<AdsLocalizingBundleDef> internal;

            private StringsHierarchyIterator(EScope scope, HierarchyIterator.Mode mode) {
                super(mode);
                internal = new DefaultHierarchyIterator<>(AdsLocalizingBundleDef.this, scope, mode);
            }

            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Chain<ExtendableDefinitions<AdsMultilingualStringDef>> next() {
                Chain<AdsLocalizingBundleDef> strins = internal.next();
                List<ExtendableDefinitions<AdsMultilingualStringDef>> result = new LinkedList<>();
                for (AdsLocalizingBundleDef bundle : strins) {
                    result.add(bundle.getStrings());
                }
                return Chain.newInstance(result);
            }
        }
    }
    private Strings strings = new Strings();

    private AdsLocalizingBundleDef(LocalizingBundleDefinition xDef) {
        super(xDef.getId(), "localizing bundle");
        this.strings.loadFrom(xDef);
    }

    protected AdsLocalizingBundleDef(Id ownerId, LocalizingBundleDefinition xDef) {
        super(Id.Factory.loadFrom("mlb" + ownerId.toString()), "localizing bundle");
        this.strings.loadFrom(xDef);
    }

    protected AdsLocalizingBundleDef(Id ownerId) {
        super(Id.Factory.loadFrom("mlb" + ownerId.toString()));
    }

//    public Strings getStrings() {
//        return strings;
//    }
    @Override
    public Strings getStrings() {
        return strings;
    }

    @Override
    public SearchResult findOverwrittenFor(Definition def) {
        if (def instanceof AdsDefinition) {
            Hierarchy<AdsDefinition> h = ((AdsDefinition) def).getHierarchy();
            return h.findOverwritten();
        } else if (def instanceof AdsModule) {
            final AdsModule overwrittenModule = ((AdsModule) def).findOverwritten();
            if (overwrittenModule != null) {
                return SearchResult.single(overwrittenModule);
            } else {
                return SearchResult.empty();
            }
        }
        return null;
    }

    public List<IMultilingualStringDef> getUsedStrings(EScope scope) {
        return strings.getUsedStrings(ESaveMode.NORMAL, scope);
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        return strings.getLanguages();
    }

    public void loadStrings(LocalizingBundleDefinition xDef) {
        this.strings.loadFrom(xDef);
    }

    @Override
    public String getName() {
        final Definition owner = findBundleOwner();
        return (owner != null ? owner.getName() + " - " : "") + "Localizing Bundle";
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        strings.visit(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        LocalizingBundleDefinition xDef = xDefRoot.addNewAdsLocalizingBundleDefinition();
        appendTo(xDef, saveMode);
    }

    public void appendTo(LocalizingBundleDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        strings.appendTo(xDef, saveMode, null);
    }

    public void appendTo(LocalizingBundleDefinition xDef, ESaveMode saveMode, EIsoLanguage lang) {
        super.appendTo(xDef, saveMode);
        strings.appendTo(xDef, saveMode, lang);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public Id getBundleOwnerId() {
        return Id.Factory.loadFrom(getId().toString().substring(3));
    }

    public Definition findBundleOwner() {
        AdsModule module = getModule();
        if (module == null) {
            return null;
        }
        
        Id bundleOwnerId = getBundleOwnerId();
        
        if (module.getId() == bundleOwnerId){
            return module;
        }
        
        return module.getTopContainer().findById(bundleOwnerId);
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.COMMON;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.LOCALIZING_BUNDLE;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.LOCALIZING_BUNDLE;
    }

    private Id runtimeId = null;

    public Id getRuntimeId() {
        Definition owner = findBundleOwner();
        if (owner instanceof AdsUserReportClassDef) {
            synchronized (this) {
                if (runtimeId == null) {
                    runtimeId = Id.Factory.loadFrom("mlb" + ((AdsUserReportClassDef) owner).getRuntimeId().toString());;
                }
                return runtimeId;
            }
        } else {
            return getId();
        }
    }

    public void resetRuntimeId() {
        synchronized (this) {
            runtimeId = null;
        }
    }

    @Override
    public File getFile() {
        final Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition instanceof AdsModule) {
            return ((AdsModule) ownerDefinition).getDefinitions().getSourceFile(this, getSaveMode());
        } else {
            return super.getFile();
        }
    }

    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        final Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition instanceof AdsModule) {
            collectFiles(true, files, files);
        } else {
            final File file = getFile();
            if (file != null) {
                files.add(file);
            }
        }
        return files;
    }
    
    public void collectFiles(boolean all, List<File> moduleFiles, List<File> localizingFiles) {
        final Definition ownerDefinition = getOwnerDefinition();
        if (all || !ownerDefinition.isReadOnly()) {
            IRepositoryAdsModule r = ((AdsModule) ownerDefinition).getRepository();
            if (r != null) {
                IRepositoryAdsDefinition rd = r.getDefinitionRepository(this);
                if (rd instanceof IRepositoryAdsLocaleDefinition) {
                    getFiles((IRepositoryAdsLocaleDefinition) rd, moduleFiles);
                }
            }
        }
        final Branch branch = getBranch();
        if (branch == null) {
            return;
        }
        for (Layer l : branch.getLayers()) {
            if (l.isLocalizing() && l.getBaseLayerURIs().contains(getLayer().getURI())) {
                AdsModule m = (AdsModule) l.getAds().getModules().findById(getModule().getId());
                if (m != null) {
                    if (all || !m.isReadOnly()) {
                        File localeDir = new File(m.getSrcDirContainer(), AdsModule.LOCALE_DIR_NAME);
                        if (localeDir.exists()) {
                            for (File langDir : localeDir.listFiles()) {
                                File[] arrfiles = langDir.listFiles(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File dir, String name) {
                                        try {
                                            return EIsoLanguage.getForValue(dir.getName()) != null && name.equals(getId() + ".xml");
                                        } catch (NoConstItemWithSuchValueError e) {
                                            return false;
                                        }
                                    }
                                });
                                if (arrfiles.length > 0) {
                                    localizingFiles.add(arrfiles[0]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void getFiles(IRepositoryAdsLocaleDefinition rd, List<File> files) {
        for (IRepositoryAdsDefinition rep : rd.getRepositories().values()) {
            if (rep instanceof IRepositoryAdsLocaleDefinition) {
                getFiles((IRepositoryAdsLocaleDefinition) rep, files);
            }
            files.add(rep.getFile());
        }
    }

    @Override
    public boolean delete() {
        final Definition ownerDefinition = getOwnerDefinition();
        final List<File> files;

        if (ownerDefinition instanceof AdsModule) {
            files = getFiles();
        } else {
            files = null;
        }

        if (!super.delete()) {
            return false;
        }

        if (files != null) {
            try {
                for (File file : files) {
                    FileUtils.deleteFileExt(file);
                }
            } catch (IOException cause) {
                throw new DefinitionError("Unable to delete definition file.", this, cause);
            }
        }
        return true;
    }

    @Override
    public IMultilingualStringDef createString(ELocalizedStringKind kind) {
        return AdsMultilingualStringDef.Factory.newInstance(kind);
    }

    @Override
    public boolean isReadOnly() {
        boolean result = super.isReadOnly();
        
        if (result){
            Layer layer = getLayer();
            if (layer != null){
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
        
        return result;
    }
}
