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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class HandleInfoAdapter implements ILocalizingStringContext {

    private HandleInfo handleInfo;

    protected HandleInfoAdapter(HandleInfo handleInfo) {
        this.handleInfo = handleInfo;
    }

    @Override
    public void enableSpellcheck(boolean enable) {
        if (hasValue()) {
            handleInfo.getAdsMultilingualStringDef().setSpellCheckEnabled(enable);
        }
    }

    @Override
    public String getValue(EIsoLanguage language) {
        if (hasValue()) {
            return handleInfo.getAdsMultilingualStringDef().getValue(language);
        }
        return null;
    }

    @Override
    public void setValue(EIsoLanguage language, String value) {
        if (!hasValue()) {
            create();
        }

        getHandleInfo().onLanguagesPatternChange(language, value);
    }

    @Override
    public void create() {
        if (!hasValue()) {
            getHandleInfo().createAdsMultilingualStringDef();
        }
    }

    @Override
    public void remove() {
        if (hasValue() && isRemovable()) {
            getHandleInfo().removeAdsMultilingualStringDef();
        }
    }

    @Override
    public boolean hasValue() {
        return handleInfo.getAdsMultilingualStringDef() != null;
    }

    @Override
    public Definition getAdsDefinition() {
        return handleInfo.getAdsDefinition();
    }

    @Override
    public boolean isSpellcheckEnable() {
        return hasValue() && handleInfo.getAdsMultilingualStringDef().isSpellCheckEnabled();
    }

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public final boolean isProxy() {
        return false;
    }

    @Override
    public Map<EIsoLanguage, String> getValueMap() {
        if (hasValue()) {
            IMultilingualStringDef string = getHandleInfo().getAdsMultilingualStringDef();
            string.getValues(ExtendableDefinitions.EScope.LOCAL);
            final Set<EIsoLanguage> languages = string.getLanguages();

            final Map<EIsoLanguage, String> valueMap = new EnumMap<>(EIsoLanguage.class);
            for (final EIsoLanguage lang : languages) {
                valueMap.put(lang, getValue(lang));
            }
            return valueMap;
        }
        return null;
    }

    @Override
    public boolean isRemovable() {
        return handleInfo.isBundleResettable();
    }

    public HandleInfo getHandleInfo() {
        return handleInfo;
    }
//
//    boolean isValid() {
//        if (handleInfo.getTitleId() != null && handleInfo.getAdsMultilingualStringDef() == null) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public ILocalizedStringInfo getStringInfo() {
        IMultilingualStringDef string = handleInfo.getAdsMultilingualStringDef();
        if (string != null){
            return new LocalizedStringInfo(string);
        }
        return null;
    }
    
    @Override
    public boolean isReadOnly() {
        Definition definition = getAdsDefinition();
        return definition == null ? true : definition.isReadOnly();
    }
}
