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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * ObjectClassId => ContextParameterId
 */
public class AdsSqlClassUpgrader8 implements IRadixObjectUpgrader {

    @Override
    public void firstStep(Element root) {
        final Node adsClassDefinitionNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");

        // collect fields

        final Node reportNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Report");
        if (reportNode != null && reportNode.getAttributes() != null) {
            final Node objectClassIdNode = reportNode.getAttributes().getNamedItem("ObjectClassId");
            if (objectClassIdNode != null) {
                final String guid = objectClassIdNode.getNodeValue();
                final Node contextParameterIdNode = root.getOwnerDocument().createAttribute("ContextParameterId");
                contextParameterIdNode.setNodeValue(guid);
                reportNode.getAttributes().setNamedItem(contextParameterIdNode);
                reportNode.getAttributes().removeNamedItem("ObjectClassId");
            }
        }
    }

    @Override
    public void finalStep(RadixObject radixObject) {
        if (radixObject instanceof AdsReportClassDef) {
            final AdsReportClassDef report = (AdsReportClassDef) radixObject;
            final Id contextClassId = report.getContextParameterId();
            if (contextClassId != null) {
                for (AdsPropertyDef prop : report.getProperties().getLocal()) {
                    if (prop.getValue().getType().getTypeId() == EValType.PARENT_REF) {
                        Id[] ids = prop.getValue().getType().getPath().asArray();
                        if (ids != null && ids.length == 1 && Utils.equals(ids[0], contextClassId)) {
                            report.setContextParameterId(prop.getId());
                        }
                    }
                }
            }
        }
    }
}
