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

import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizedStringInfo;


public class MixedDescriptionHandleInfo<T extends Object & IDescribable & ILocalizedDescribable> extends DescriptionHandleInfo {

    private final T object;
    private IDescriptionHandleInfo understudy;

    public MixedDescriptionHandleInfo(T object, boolean proxy) {
        super(proxy);
        this.object = object;

        final EDescriptionType descriptionType = getEditorModeFor(object);

        understudy = createUnderstudy(object, descriptionType, proxy);
    }

    @Override
    public EDescriptionType getDescriptionType() {
        return understudy.getDescriptionType();
    }
    private boolean localize = false;

    @Override
    public boolean localize(EIsoLanguage language) {
        if (isLocalizable()) {
            final String stringDescription = understudy.getStringDescription();

            understudy = createUnderstudy(object, EDescriptionType.LOCALIZED, isProxy());
            understudy.setValue(language, stringDescription);

            if (!isProxy()) {
                afterLocalize();
            }

            localize = true;

            return true;
        }
        return false;
    }

    @Override
    public boolean isLocalizable() {
        return getDescriptionType() == EDescriptionType.STRING
                && DescriptionHandleInfo.getDefaultEditorModeFor(object) == EDescriptionType.LOCALIZED;
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
    public String getStringDescription() {
        return understudy.getStringDescription();
    }

    @Override
    public boolean setStringDescription(String description) {
        return understudy.setStringDescription(description);
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
        return object.getDescriptionLocation();
    }

    @Override
    public boolean isRemovable() {
        return understudy.isRemovable();
    }

    @Override
    public boolean commit() {
        if (isProxy() && localize) {
            afterLocalize();
        }
        return understudy.commit();
    }

    @Override
    public Map<EIsoLanguage, String> getValueMap() {
        return understudy.getValueMap();
    }

    @Override
    public IDescribable getDescribable() {
        return object;
    }

    @Override
    public ILocalizedDescribable getLocalizedDescribable() {
        return object;
    }

    @Override
    public String toString() {
        return understudy.toString();
    }

    private void afterLocalize() {
        object.setDescription("");
    }

    /**
     * Factory method, called in constructor.
     */
    protected IDescriptionHandleInfo createUnderstudy(T object, EDescriptionType type, boolean proxy) {
        return type == EDescriptionType.LOCALIZED ? new LocalizedDescriptionHandleInfo(object, proxy)
                : new StringDescriptionHandleInfo(object, proxy);
    }

    @Override
    public ILocalizedStringInfo getStringInfo() {
        return understudy.getStringInfo();
    }
    
    @Override
    public boolean isReadOnly() {
        if (object instanceof MixedDescriptionWrapper) {
            // если есть возможность, выясняем ReadOnly у дифиниции, к которой относится описание
            return ((MixedDescriptionWrapper)object).isReadOnly();
        } else {
            // иначе у дифиниции где лежит описание
            Definition definition = getAdsDefinition();
            return definition == null ? true : definition.isReadOnly();
        }                
    }    
}
