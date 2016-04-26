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

package org.radixware.kernel.designer.ads.common.upgrade;

import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgraderFactory;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;


public abstract class AdsClassUpgraderFactory implements IRadixObjectUpgraderFactory {

    protected AdsClassUpgraderFactory() {
    }

    public abstract boolean isSupportedClassType(EClassType classType);

    @Override
    public boolean isSupported(Element root) {
        if (!"AdsDefinition".equals(root.getLocalName())) {
            return false;
        }

        final Element adsClassDefinitionXmlNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");
        if (adsClassDefinitionXmlNode == null) {
            return false;
        }

        final String value = XmlUtils.getString(adsClassDefinitionXmlNode, "Type");
        final Long type;
        try {
            type = Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return false;
        }

        final EClassType classType;
        try {
            classType = EClassType.getForValue(type);
        } catch (NoConstItemWithSuchValueError ex) {
            return false;
        }

        return isSupportedClassType(classType);
    }
}
