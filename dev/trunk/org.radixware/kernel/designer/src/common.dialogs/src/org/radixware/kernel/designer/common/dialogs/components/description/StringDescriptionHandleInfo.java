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
import java.util.Objects;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizedStringInfo;


public class StringDescriptionHandleInfo extends DescriptionHandleInfo {

    private final IDescribable definition;
    private String description;

    public StringDescriptionHandleInfo(IDescribable definition, boolean proxy) {
        super(proxy);
        this.definition = definition;
        this.description = definition.getDescription();
    }

    @Override
    public EDescriptionType getDescriptionType() {
        return EDescriptionType.STRING;
    }

    @Override
    public String getStringDescription() {
        if (isProxy()) {
            return description != null ? description : "";
        }
        return definition.getDescription();
    }

    @Override
    public boolean setStringDescription(String description) {
        if (isProxy()) {
            this.description = description;
            return true;
        }

        if (!Objects.equals(definition.getDescription(), description)) {
            definition.setDescription(description);
        }
        return true;
    }

    @Override
    public boolean isSpellcheckEnable() {
        return false;
    }

    @Override
    public boolean hasValue() {
        return !getStringDescription().isEmpty();
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return null;
    }

    @Override
    public boolean isRemovable() {
        return false;
    }

    @Override
    public boolean commit() {
        if (isProxy()) {
            definition.setDescription(getStringDescription());
            return true;
        }
        return false;
    }

    @Override
    public IDescribable getDescribable() {
        return definition;
    }

    @Override
    public String toString() {
        return getStringDescription() != null ? getStringDescription() : "";
    }

    @Override
    public Map<EIsoLanguage, String> getValueMap() {
        return null;
    }

    @Override
    public String getValue(EIsoLanguage language) {
        return null;
    }

    @Override
    public void setValue(EIsoLanguage language, String value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void create() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void enableSpellcheck(boolean enable) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public ILocalizedStringInfo getStringInfo() {
        return null;
    }
}
