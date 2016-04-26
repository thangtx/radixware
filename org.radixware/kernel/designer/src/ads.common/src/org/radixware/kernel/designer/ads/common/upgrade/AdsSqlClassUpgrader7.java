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
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Field and Parameter report cell => Property cell.
 * Parameter => ParameterProperty
 */
public class AdsSqlClassUpgrader7 implements IRadixObjectUpgrader {

    public void firstStep(Element root) {
        final Node adsClassDefinitionNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");

        // collect fields

        final Node reportNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Report");
        if (reportNode != null) {
            final Node formNode = XmlUtils.findChildByLocalName(reportNode, "Form");
            if (formNode != null) {
                for (Node bandNode = formNode.getFirstChild(); bandNode != null; bandNode = bandNode.getNextSibling()) {
                    final Node cellsNode = XmlUtils.findChildByLocalName(bandNode, "Cells");
                    if (cellsNode != null) {
                        for (Node cellNode = cellsNode.getFirstChild(); cellNode != null; cellNode = cellNode.getNextSibling()) {
                            final String type = XmlUtils.getString(cellNode, "Type");
                            if ("Field".equals(type) || "Parameter".equals(type)) {
                                ((Element) cellNode).setAttribute("Type", "Property");
                            }
                        }
                    }
                }
            }
        }
    }

    public void finalStep(RadixObject radixObject) {
        /*
        if (radixObject instanceof AdsSqlClassDef) {
            final AdsSqlClassDef sqlClass = (AdsSqlClassDef) radixObject;

            for (AdsPropertyDef prop : sqlClass.getProperties().getLocal()) {
                if (sqlClass.getOldParameters().findById(prop.getId()) != null) {
                    prop.delete();
                }
            }

            for (AdsParameterDef param : sqlClass.getOldParameters()) {
                final AdsPropertyDef prop = AdsParameterPropertyDef.Factory.newInstance(param.getId(), param.getName());
                final AdsTypeDeclaration requiredTypeDeclaration = param.getTypeDeclaration();
                prop.getValue().setType(requiredTypeDeclaration);
                sqlClass.getProperties().getLocal().add(prop);
            }

            sqlClass.getOldParameters().clear();
        }
         */
    }
}
