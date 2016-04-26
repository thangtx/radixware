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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class AdsPresentableClassUpgrader7 implements IRadixObjectUpgrader {

    @Override
    public void firstStep(Element root) {
        processNode(root);
    }

    @Override
    public void finalStep(RadixObject radixObject) {
    }

    private void processNode(Node node) {
        try {
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                if ("SelectorPresentations".equals(child.getLocalName())) {
                    for (Node candidate = child.getFirstChild(); candidate != null; candidate = candidate.getNextSibling()) {
                        if ("SelectorPresentation".equals(candidate.getLocalName())) {
                            final Node spNode = candidate;
                            final NamedNodeMap attributes = spNode.getAttributes();
                            if (attributes != null) {
                                final Node restrictionsNode = attributes.getNamedItem("Restrictions");
                                if (restrictionsNode != null) {
                                    String restrictionsAsStr = restrictionsNode.getNodeValue();
                                    if (restrictionsAsStr != null) {
                                        long restrictions = Long.valueOf(restrictionsAsStr).longValue();
                                        restrictions |= 0x40000;//ERestriction.CONTEXTLESS_USAGE
                                        restrictionsAsStr = String.valueOf(restrictions);
                                        restrictionsNode.setNodeValue(restrictionsAsStr);
                                    }

                                }
                            }
                        }
                    }
                } else {
                    processNode(child);
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
