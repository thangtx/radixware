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

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizedString;


public class DdsMultilingualStringDef extends Definition implements IMultilingualStringDef {

    private static class InternalStorage extends IMultilingualStringDef.Storage {
        
        public InternalStorage(Definition owner) {
            super(owner);
        }
        
        public InternalStorage(Definition owner, LocalizedString xDef) {
            super(owner, xDef);
        }
    }
    private final Storage storage;
    
    public DdsMultilingualStringDef(Id id) {
        super(id);
        this.storage = new InternalStorage(this);
    }
    
    public DdsMultilingualStringDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZED_STRING));
        this.storage = new InternalStorage(this);
    }
    
    public DdsMultilingualStringDef(IMultilingualStringDef src) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_LOCALIZED_STRING));
        this.storage = new InternalStorage(this);
        if (src != null) {
            Storage.copyValue(src.getInternalStorage(), storage);
        }
        
    }
    
    public DdsMultilingualStringDef(LocalizedString xDef) {
        super(xDef);
        this.storage = new InternalStorage(this, xDef);
    }
    
    @Override
    public Set<EIsoLanguage> getLanguages() {
        return storage.getLanguages();
    }
    
    @Override
    public IMultilingualStringDef cloneString(ILocalizingBundleDef bundle) {
        final DdsMultilingualStringDef clone = new DdsMultilingualStringDef(this);
        
        if (bundle != null) {
            bundle.getStrings().getLocal().add(clone);
        }
        return clone;
    }
    
    @Override
    public IMultilingualStringDef cloneString(ILocalizingBundleDef bundle,boolean useSrcId){
        return cloneString(bundle);
    }
    
    @Override
    public void appendTo(org.radixware.schemas.adsdef.LocalizedString xDef) {
        appendTo(xDef, null);
    }
    
    @Override
    public void appendTo(org.radixware.schemas.adsdef.LocalizedString xDef, EIsoLanguage lang) {
        xDef.setId(getId());
        xDef.setSrcKind(getSrcKind());
        storage.appendTo(xDef, false, lang);
    }
    
    @Override
    public String getValue(EIsoLanguage lang) {
        return storage.getValue(lang);
    }
    
    @Override
    public boolean setValue(EIsoLanguage lang, String value) {
        return storage.setValue(lang, value);
    }
    
    @Override
    public boolean getNeedsCheck(EIsoLanguage language) {
        return storage.getNeedsCheck(language);
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
    public Timestamp getLastChangeTime(EIsoLanguage language) {
        return storage.getLastChangeTime(language);
    }
    
    @Override
    public String getLastChangeAuthor(EIsoLanguage language) {
        return storage.getLastChangeAuthor(language);
    }
    
    @Override
    public boolean loadValue(IMultilingualStringDef multilingualString, EIsoLanguage language) {
        return storage.loadValue(multilingualString, language);
    }
    
    @Override
    public List<StringStorage> getValues(ExtendableDefinitions.EScope scope) {
        return storage.getValues(scope);
    }
    
    @Override
    public ELocalizedStringKind getSrcKind() {
        return ELocalizedStringKind.DESCRIPTION;
    }
    
    @Override
    public boolean isSpellCheckEnabled() {
        return storage.isDoSpellCheck();
    }
    
    @Override
    public void setSpellCheckEnabled(boolean enabled) {
        storage.setDoSpellCheck(enabled);
    }
    
    @Override
    public long getCreationDate() {
        return storage.getCreationDate();
    }
    
    @Override
    public String getAuthor() {
        return storage.getAuthor();
    }
    
    @Override
    public Storage getInternalStorage() {
        return storage;
    }
    
    public DdsLocalizingBundleDef getOwnerBundle() {
        for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof DdsLocalizingBundleDef) {
                return (DdsLocalizingBundleDef) obj;
            }
        }
        return null;
    }
    
    @Override
    public SearchResult findOverwritten() {
        final DdsLocalizingBundleDef owner = getOwnerBundle();
        return owner == null ? SearchResult.empty() : owner.findOverwrittenFor(this);
    }
    
    @Override
    public void setChecked(EIsoLanguage lang, boolean isChecked) {
        storage.setChecked(lang, isChecked);
    }
    
    @Override
    public EDefType getDefinitionType() {
        return EDefType.MULTILINGUAL_STRING;
    }
    
    @Override
    public int getVersion() {
        return storage.getVersion();
    }
    
    @Override
    public void setVersion(int version) {
        storage.setVersion(version);
    }
    
    @Override
    public void updateLanguages(Set<EIsoLanguage> languages) {
        storage.updateLanguages(languages);
    }
}
