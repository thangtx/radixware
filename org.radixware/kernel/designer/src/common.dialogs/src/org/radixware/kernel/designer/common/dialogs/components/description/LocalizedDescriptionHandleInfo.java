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

package org.radixware.kernel.designer.common.dialogs.components.description;

import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;

import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfoAdapter;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizedStringInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizingStringContext;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringContextFactory;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ProxyHandleInfo;


public class LocalizedDescriptionHandleInfo extends DescriptionHandleInfo {

    private HandleInfoAdapter understudy;
    private final ILocalizedDescribable definition;

    public LocalizedDescriptionHandleInfo(final ILocalizedDescribable definition, boolean proxy) {
        super(proxy);

        this.definition = definition;
        final HandleInfo handleInfo;
        if (proxy){
            handleInfo = new ProxyHandleInfo(){
                @Override
                public Definition getAdsDefinition() {
                    return definition.getDescriptionLocation();
                }

                @Override
                public Id getTitleId() {
                    return definition.getDescriptionId();
                }

                @Override
                public ELocalizedStringKind getLocalizedStringKind() {
                    return ELocalizedStringKind.DESCRIPTION;
                }

                @Override
                protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                }

                @Override
                public boolean isProxyState() {
                    if (definition instanceof ILocalizedDescribable.Inheritable){
                        return !((ILocalizedDescribable.Inheritable) definition).isDescriptionInherited();
                    }
                    return true;
                }

                @Override
                protected void updateProxyString() {
                    for (EIsoLanguage language : EIsoLanguage.values()){
                        String value = definition.getDescription(language);
                        if (value != null){
                            if (adsMultilingualStringDef == null){
                                adsMultilingualStringDef = AdsMultilingualStringDef.Factory.newDescriptionInstance();
                            }
                            onLanguagesPatternChange(language, value);
                        }
                    }
                }

                @Override
                public boolean commit() {
                    definition.setDescriptionId(adsMultilingualStringDef != null ? adsMultilingualStringDef.getId() : null);
                    if (adsMultilingualStringDef != null){
                        for (IMultilingualStringDef.StringStorage storage : adsMultilingualStringDef.getValues(EScope.LOCAL)){
                            definition.setDescription(storage.getLanguage(), storage.getValue());
                        }
                    }
                    return true;
                }

            };
        } else {
            handleInfo = new HandleInfo() {
                @Override
                public Definition getAdsDefinition() {
                    return definition.getDescriptionLocation();
                }

                @Override
                public Id getTitleId() {
                    return definition.getDescriptionId();
                }

                @Override
                public ELocalizedStringKind getLocalizedStringKind() {
                    return ELocalizedStringKind.DESCRIPTION;
                }

                @Override
                protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                    definition.setDescriptionId(multilingualStringDef != null ? multilingualStringDef.getId() : null);
                }

                @Override
                protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                    definition.setDescription(language, newStringValue);
                }
            };
        }
        

        understudy = LocalizingStringContextFactory.newInstance(handleInfo);
    }

    @Override
    public EDescriptionType getDescriptionType() {
        return EDescriptionType.LOCALIZED;
    }

    @Override
    public void enableSpellcheck(boolean enable) {
        understudy.enableSpellcheck(enable);
    }

    @Override
    public boolean isSpellcheckEnable() {
        return understudy.isSpellcheckEnable();
    }

    @Override
    public String getValue(EIsoLanguage language) {
        return understudy.getValue(language);
    }

    @Override
    public void setValue(EIsoLanguage language, String value) {
        understudy.setValue(language, value);
    }

    @Override
    public boolean hasValue() {
        return understudy.hasValue();
    }

    @Override
    public Definition getAdsDefinition() {
        return understudy.getAdsDefinition();
    }

    @Override
    public boolean isRemovable() {
        return understudy.isRemovable();
    }

    @Override
    public void create() {
        understudy.create();
    }

    @Override
    public void remove() {
        understudy.remove();
    }

    @Override
    public boolean commit() {
        if (isProxy()) {
            HandleInfo handleInfo = understudy.getHandleInfo();
            if (handleInfo instanceof ProxyHandleInfo){
                ((ProxyHandleInfo) handleInfo).commit();
            }
        }
        return understudy.commit();
    }

    @Override
    public Map<EIsoLanguage, String> getValueMap() {
        return understudy.getValueMap();
    }

    @Override
    public ILocalizedDescribable getLocalizedDescribable() {
        return definition;
    }

    @Override
    public String toString() {
        final Map<EIsoLanguage, String> localizedDescription = understudy.getValueMap();
        if (localizedDescription == null) {
            return "";
        } else {
            final StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (final EIsoLanguage lang : localizedDescription.keySet()) {
                if (first) {
                    first = false;
                } else {
                    builder.append("; ");
                }
                builder.append(lang.getName()).append(": ").append(localizedDescription.get(lang));
            }
            return builder.toString();
        }
    }

    @Override
    public String getStringDescription() {
        return null;
    }

    @Override
    public boolean setStringDescription(String description) {
        return false;
    }

    @Override
    public ILocalizedStringInfo getStringInfo() {
        return understudy.getStringInfo();
    }
}
