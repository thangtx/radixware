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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;



public class DescriptionStorage {
    private Map<Object, DescriptionModel> descriptionMap = new HashMap<>();

    public void putAll(Map<Object, DescriptionModel> descriptionMap) {
        this.descriptionMap.putAll(descriptionMap);
    }

    public void putDescription(Object key, DescriptionModel descriptionModel) {
        descriptionMap.put(key, descriptionModel);
    }

    public boolean setDescription(AdsDefinition definition) {
        return setDescription(definition.getId(), definition);
    }

    public <T extends Object & IDescribable & ILocalizedDescribable> boolean setDescription(Object key, T definition) {
        if (definition == null || key == null) {
            return false;
        }
        final DescriptionModel descriptionModel = descriptionMap.get(key);
        if (descriptionModel != null) {
            return descriptionModel.applyFor(definition);
        }
        return false;
    }

    public boolean setDescription(Object key, IDescribable definition) {
        if (definition == null || key == null) {
            return false;
        }
        final DescriptionModel descriptionModel = descriptionMap.get(key);
        if (descriptionModel != null) {
            return descriptionModel.applyFor(definition);
        }
        return false;
    }

    public boolean setDescription(Object key, ILocalizedDescribable definition) {
        if (definition == null || key == null) {
            return false;
        }
        final DescriptionModel descriptionModel = descriptionMap.get(key);
        if (descriptionModel != null) {
            return descriptionModel.applyFor(definition);
        }
        return false;
    }

}
