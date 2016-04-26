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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class AdsSqlClassUpgrader5 implements IRadixObjectUpgrader {

    public void firstStep(Element root) {
        final Document doc = root.getOwnerDocument();
        final Node adsClassDefinitionNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");

        // collect fields

        final Node propertiesNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Properties");
        if (propertiesNode != null) {
            for (Node propertyNode = propertiesNode.getFirstChild(); propertyNode != null; propertyNode = propertyNode.getNextSibling()) {
                if ("Property".equals(propertyNode.getLocalName())) {
                    final Integer idx = XmlUtils.getInteger(propertyNode, "Nature");
                    if (idx != null) {
                        if (idx.intValue() == EPropNature.FIELD.getValue().intValue()) {
                            Node typeNode = XmlUtils.findChildByLocalName(propertyNode, "Type");
                            if (typeNode != null) {
                                Integer typeId = XmlUtils.getInteger(typeNode, "TypeId");
                                if (typeId != null && typeId.intValue() == EValType.PARENT_REF.getValue().intValue()) {
                                    ((Element) propertyNode).setAttribute("Nature", String.valueOf(EPropNature.FIELD_REF.getValue().intValue()));
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    public void finalStep(RadixObject radixObject) {
    }
}
