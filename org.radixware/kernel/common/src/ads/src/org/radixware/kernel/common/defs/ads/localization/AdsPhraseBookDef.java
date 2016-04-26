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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.PhraseBookDefinition;


public class AdsPhraseBookDef extends AdsDefinition {

    private Id extendsId;
    final List<IMultilingualStringDef> strings = new LinkedList<>();

    private AdsPhraseBookDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_PHRASE_BOOK));
    }

    private AdsPhraseBookDef(Id id, String name) {
        super(id, name);
    }

    private AdsPhraseBookDef(PhraseBookDefinition xDef) {
        super(xDef);
        extendsId=xDef.getExtendsId();
    }

    public static final class Factory {

        public static AdsPhraseBookDef loadFrom(PhraseBookDefinition xDef) {
            return new AdsPhraseBookDef(xDef);
        }

        public static AdsPhraseBookDef newInstance() {
            return new AdsPhraseBookDef();
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.PHRASE_BOOK;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        PhraseBookDefinition xDef = xDefRoot.addNewAdsPhraseBookDefinition();
        if(extendsId!=null)
            xDef.setExtendsId(extendsId);
        appendTo(xDef, saveMode);
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PHRASE_BOOK;
    }

    public void setSuperPhraseBook(Id superPhraseBook){
         this.extendsId = superPhraseBook;
         this.setEditState(EEditState.MODIFIED);
    }

    public Id getSuperPhraseBook(){
        return extendsId;
    }

    public void addString(IMultilingualStringDef string){
        if (!strings.contains(string)){
            strings.add(string);
        }
    }
    
    public void removeString(IMultilingualStringDef string){
        if (strings.contains(string)){
            strings.remove(string);
            ILocalizingBundleDef.version.incrementAndGet();
        }
    }
    
    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        for(final IMultilingualStringDef mlstring : strings){
            ids.add(new MultilingualStringInfo(AdsPhraseBookDef.this) {
                @Override
                public Id getId() {
                    return mlstring.getId();
                }

                @Override
                public void updateId(Id newId) {
                }

                @Override
                public EAccess getAccess() {
                    return AdsPhraseBookDef.this.getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return getTypeTitle().concat(" Phrase");
                }

                @Override
                public boolean isPublished() {
                    return AdsPhraseBookDef.this.isPublished();
                }

                @Override
                public EMultilingualStringKind getKind() {
                    if (mlstring.getSrcKind() == ELocalizedStringKind.EVENT_CODE)
                        return EMultilingualStringKind.EVENT_CODE;
                    else
                        return EMultilingualStringKind.TITLE;
                }
                
            });
        }
        super.collectUsedMlStringIds(ids);
    }
}
