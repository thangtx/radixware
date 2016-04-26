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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 'Parameter Direction' => 'Parameter Tag Direction'.
 */
class AdsSqlClassUpgrader2 implements IRadixObjectUpgrader {

    @Override
    public void firstStep(Element root) {
        final Node adsClassDefinitionNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");
        final Node sqlNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Sql");
        final Node parametersNode = XmlUtils.findChildByLocalName(sqlNode, "Parameters");
        if (parametersNode == null) {
            return;
        }

        final Map<String, String> outParam2Direction = new HashMap<String, String>();

        for (Node paramNode = parametersNode.getFirstChild(); paramNode != null; paramNode = paramNode.getNextSibling()) {
            if ("Parameter".equals(paramNode.getLocalName())) {
                final Element paramElement = XmlUtils.findFirstElement(paramNode);
                final String direction = paramElement.getAttribute("Direction");
                if ("1".equals(direction) || "2".equals(direction)) { // EParamDirection.OUT, EParamDirection.BOTH at moment of upgrade
                    final String id = paramElement.getAttribute("Id");
                    outParam2Direction.put(id, direction);
                }
            }
        }

        if (outParam2Direction.isEmpty()) {
            return;
        }

        final Node sourceNode = XmlUtils.findChildByLocalName(sqlNode, "Source");
        if (sourceNode == null) {
            return;
        }

        for (Node itemNode = sourceNode.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling()) {
            if ("Item".equals(itemNode.getLocalName())) {
                final Element parameterTagElement = XmlUtils.findChildByLocalName(itemNode, "Parameter");
                if (parameterTagElement != null) {
                    final String id = parameterTagElement.getAttribute("ParamId");
                    final String direction = outParam2Direction.get(id);
                    if (direction != null) {
                        parameterTagElement.setAttribute("Direction", direction);
                    }
                }
            }
        }
    }

    @Override
    public void finalStep(RadixObject radixObject) {
        // NOTHING
    }
}
