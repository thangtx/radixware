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

import java.net.URI;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizedString;


public class AdsMultilingualStringDef extends AdsDefinition implements IOverwritable<AdsMultilingualStringDef>, IMultilingualStringDef {

    @Override
    public void afterOverwrite() {
    }
    
    @Override
    public boolean allowOverwrite() {
        return true;
    }
  
    public static final class Factory {
        public static AdsMultilingualStringDef newInstance(ELocalizedStringKind stringKind, Id id) {
            if (id != null){
                switch (stringKind) {
                    case EVENT_CODE:
                        return newEventCodeInstance(id);
                    case SIMPLE:
                        return newInstance(id);
                    case DESCRIPTION:
                        return newDescriptionInstance(id);
                }
                return newInstance(id);
            }
            
            switch (stringKind) {
                case EVENT_CODE:
                    return newEventCodeInstance();
                case SIMPLE:
                    return newInstance();
                case DESCRIPTION:
                    return newDescriptionInstance();
            }
            return newInstance();
        }
        
        public static AdsMultilingualStringDef newInstance(ELocalizedStringKind stringKind) {
            return newInstance(stringKind, null);
        }
        
        public static AdsMultilingualStringDef newInstance() {
            return newInstance(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZED_STRING));
        }
        
        public static AdsMultilingualStringDef newInstance(Id id) {
            return new AdsMultilingualStringDef(id);
        }
        
        public static AdsEventCodeDef newEventCodeInstance() {
            return newEventCodeInstance(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZED_STRING));
        }
        
        public static AdsEventCodeDef newEventCodeInstance(Id id) {
            return new AdsEventCodeDef(id);
        }
        
        public static AdsDescriptionDef newDescriptionInstance() {
            return newDescriptionInstance(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZED_STRING));
        }
        
        public static AdsDescriptionDef newDescriptionInstance(Id id) {
            return new AdsDescriptionDef(id);
        }
        
        public static AdsEventCodeDef newEventCodeInstance(AdsMultilingualStringDef src) {
            return new AdsEventCodeDef(src, false);
        }
        
        public static AdsMultilingualStringDef newInstance(AdsMultilingualStringDef src) {
            return newInstance(src, false);
        }
        
        public static AdsMultilingualStringDef newInstance(AdsMultilingualStringDef src, boolean forOverwrite) {
            if (src instanceof AdsEventCodeDef) {
                return new AdsEventCodeDef((AdsEventCodeDef) src, forOverwrite);
            } else if (src instanceof AdsDescriptionDef) {
                return new AdsDescriptionDef((AdsDescriptionDef) src, forOverwrite);
            } else {
                return new AdsMultilingualStringDef(src, forOverwrite);
            }
        }
        
        public static AdsMultilingualStringDef newInstance(AdsMultilingualStringDef src, boolean forOverwrite, boolean useSrcId) {
            if (src instanceof AdsDescriptionDef) {
                return new AdsDescriptionDef((AdsDescriptionDef) src, forOverwrite,useSrcId);
            } else {
                return new AdsMultilingualStringDef(src, forOverwrite, useSrcId);
            }
        }
        
        public static AdsMultilingualStringDef loadFrom(LocalizedString xDef) {
            if (xDef != null) {
                switch (xDef.getSrcKind()) {
                    case EVENT_CODE:
                        return new AdsEventCodeDef(xDef);
                    case SIMPLE:
                        return new AdsMultilingualStringDef(xDef);
                    case DESCRIPTION:
                        return new AdsDescriptionDef(xDef);
                    default:
                        return new AdsMultilingualStringDef(xDef);
                }
            }
            return null;
        }
        
        public static AdsEventCodeDef convertToEventCode(AdsMultilingualStringDef src) {
            final AdsEventCodeDef eventCodeDef = new AdsEventCodeDef(src.getId());
            
            Storage.copyValue(src.storage, eventCodeDef.getInternalStorage());
            
            return eventCodeDef;
        }
        
        public static AdsMultilingualStringDef convertToString(AdsEventCodeDef src) {
            final AdsMultilingualStringDef eventCodeDef = new AdsMultilingualStringDef(src.getId());
            
            Storage.copyValue(src.getInternalStorage(), eventCodeDef.getInternalStorage());
            
            return eventCodeDef;
        }
    }
    
    private final class InternalStorage extends IMultilingualStringDef.Storage {
        
        public InternalStorage(Definition owner) {
            super(owner);
        }
        
        public InternalStorage(Definition owner, LocalizedString xDef) {
            super(owner, xDef);
        }
        
        @Override
        public SearchResult findOverwritten(Definition definition) {
            if (definition instanceof AdsMultilingualStringDef) {
                return ((AdsMultilingualStringDef) definition).getHierarchy().findOverwritten();
            } else {
                return SearchResult.empty();
            }
        }
    }
    private final Storage storage;
    
    protected AdsMultilingualStringDef(Id id) {
        super(id);
        storage = new InternalStorage(this);
    }
    
    protected AdsMultilingualStringDef(LocalizedString xDef) {
        super(xDef);
        storage = new InternalStorage(this, xDef);
    }
    
    protected AdsMultilingualStringDef(AdsMultilingualStringDef src, boolean forOverwrite) {
        this(src,forOverwrite,false);
    }
    
    protected AdsMultilingualStringDef(AdsMultilingualStringDef src, boolean forOverwrite, boolean useSrcId){
        super(forOverwrite || useSrcId? src.getId() : Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZED_STRING));
        storage = new InternalStorage(this);
        this.setOverwrite(forOverwrite);
        Storage.copyValue(src.storage, storage);
    }
    
    @Override
    public Storage getInternalStorage() {
        return storage;
    }
    
    @Override
    public String getValue(EIsoLanguage language) {
        return storage.getValue(language);
    }
    
    @Override
    public boolean setValue(EIsoLanguage language, String value) {
        return storage.setValue(language, value);
    }
    
    @Override
    public boolean loadValue(IMultilingualStringDef multilingualString, EIsoLanguage language) {
        return storage.loadValue(multilingualString, language);
    }
    
    public String findValue(EIsoLanguage preferredLanguage) {
        return storage.findValue(preferredLanguage);
    }

    /**
     * Creates copy of the multilingual string. if parameter bundle is not null
     * string will be added to the bundle
     */
    @Override
    public AdsMultilingualStringDef cloneString(ILocalizingBundleDef bundle) {
        AdsMultilingualStringDef clone = Factory.newInstance(this);
        
        if (bundle != null) {
            bundle.getStrings().getLocal().add(clone);
        }
        return clone;
    }
    
    @Override
    public IMultilingualStringDef cloneString(ILocalizingBundleDef bundle,boolean useSrcId){
        AdsMultilingualStringDef clone = Factory.newInstance(this, false, useSrcId);
        
        if (bundle != null) {
            bundle.getStrings().getLocal().add(clone);
        }
        
        return clone;
    }
    
    @Override
    public void updateLanguages(Set<EIsoLanguage> languages) {
        storage.updateLanguages(languages);
    }
    
    public AdsLocalizingBundleDef getOwnerBundle() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsLocalizingBundleDef) {
                return (AdsLocalizingBundleDef) owner;
            }
        }
        return null;
    }
    
    @Override
    public boolean getNeedsCheck(EIsoLanguage language) {
        return storage.getNeedsCheck(language);
    }
    
    public void setChecked(EIsoLanguage language, boolean needsCheck) {
        storage.setChecked(language, needsCheck);
    }
    
    @Override
    public Timestamp getTimeChangeOfStatus(EIsoLanguage language) {
        return storage.getTimeChangeOfStatus(language);
    }

    @Override
    public String getAuthorChangeOfStatus(EIsoLanguage language) {
        return storage.getAuthorChangeOfStatus(language);
    }
    
    @Override
    public Timestamp getLastChangeTime(EIsoLanguage lang) {
        return storage.getLastChangeTime(lang);
    }
    
    @Override
    public String getLastChangeAuthor(EIsoLanguage lang) {
        return storage.getLastChangeAuthor(lang);
    }
    
    public URI getTargetNamespace() {
        return null;
    }
    
    @Override
    public String getName() {
        return "Multilingual String";
    }
    
    @Override
    public void appendTo(LocalizedString xDef) {
        appendTo(xDef, ESaveMode.NORMAL);
    }
    
    public void appendTo(LocalizedString xDef, ESaveMode saveMode) {
        appendTo(xDef, saveMode, null);
    }
    
    public void appendTo(LocalizedString xDef, ESaveMode saveMode, EIsoLanguage lang) {
        super.appendTo(xDef, saveMode);
        xDef.setSrcKind(getSrcKind());
        storage.appendTo(xDef, saveMode == ESaveMode.API, lang);
    }
    
    @Override
    public ELocalizedStringKind getSrcKind() {
        return ELocalizedStringKind.SIMPLE;
    }
    
    @Override
    public String getAuthor() {
        return storage.getAuthor();
    }
    
    @Override
    public long getCreationDate() {
        return storage.getCreationDate();
    }
    
    @Override
    public List<StringStorage> getValues(EScope scope) {
        return storage.getValues(scope);
    }
    
    @Override
    public Set<EIsoLanguage> getLanguages() {
        return storage.getLanguages();
    }
    
    @Override
    public boolean isSpellCheckEnabled() {
        return storage.isDoSpellCheck();
    }
    
    @Override
    public void setSpellCheckEnabled(boolean enabled) {
        storage.setDoSpellCheck(enabled);
    }
    
    public int getVersion() {
        return storage.getVersion();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsMultilingualStringDef> getHierarchy() {
        return new MLSHierarchy(this);
    }
    
    private class MLSHierarchy extends Hierarchy<AdsMultilingualStringDef> {
        
        public MLSHierarchy(AdsMultilingualStringDef object) {
            super(object);
        }
        
        @Override
        public SearchResult<AdsMultilingualStringDef> findOverwritten() {
            List<AdsMultilingualStringDef> result = new LinkedList<>();
            collectAllOverwritten(result);
            if (result.isEmpty()) {
                return SearchResult.empty();
            } else {
                return SearchResult.list(result);
            }
            
        }
        
        @Override
        public SearchResult<AdsMultilingualStringDef> findOverridden() {
            return SearchResult.empty();
        }
        
        @SuppressWarnings("unchecked")
        private void collectAllOverwritten(List<AdsMultilingualStringDef> collection) {
            final List<AdsLocalizingBundleDef> bundles = getOwnerBundle().<AdsLocalizingBundleDef>getHierarchy().findOverwritten().all();
            for (AdsLocalizingBundleDef bundleOvr : bundles) {
                
                if (bundleOvr == getOwnerBundle()) {
                    Logger.getLogger(AdsMultilingualStringDef.class.getName()).log(Level.SEVERE, "ERROR");
                } else {
                    AdsMultilingualStringDef string = bundleOvr.getStrings().findById(getId(), EScope.LOCAL_AND_OVERWRITE).get();
                    if (string != null) {
                        if (!collection.contains(string)) {
                            collection.add(string);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void appendTo(LocalizedString xDef, EIsoLanguage lang) {
        appendTo(xDef, ESaveMode.NORMAL, lang);
    }
    
    @Override
    public SearchResult findOverwritten() {
        return getHierarchy().findOverwritten();
    }
    
    @Override
    public EDefType getDefinitionType() {
        return EDefType.MULTILINGUAL_STRING;
    }
    
    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.MULTILINGUAL_STRING;
    }
    
    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CONST;
    }
        
    @Override
    public void setVersion(int version) {
        storage.setVersion(version);
    }
}
