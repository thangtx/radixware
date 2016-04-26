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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.EAccessAreaType;


class AccessAreaTypeComboBoxItem extends Object {

    private EAccessAreaType accessAreaType;
    private static final Map<EAccessAreaType, String> accessAreaType2NameMap = new HashMap<EAccessAreaType, String>(3);
    private static final String ownAccessAreasStr = "Own";
    private static final String inheritedStr = "Inherited";
    private static final String noAccessAreasStr = "No";
    private static final String notOwerriddenAccessAreasStr = "Not overwrite";
    

    static {
        accessAreaType2NameMap.put(EAccessAreaType.OWN, ownAccessAreasStr);
        accessAreaType2NameMap.put(EAccessAreaType.INHERITED, inheritedStr);
        accessAreaType2NameMap.put(EAccessAreaType.NONE, noAccessAreasStr);
        accessAreaType2NameMap.put(EAccessAreaType.NOT_OVERRIDDEN, notOwerriddenAccessAreasStr);

    }

    public AccessAreaTypeComboBoxItem(EAccessAreaType accessAreaType) {
        this.accessAreaType = accessAreaType;
    }

    @Override
    public String toString() {
        return accessAreaType2NameMap.get(accessAreaType);
    }

    public EAccessAreaType getAccessAreaType() {
        return accessAreaType;
    }
}
