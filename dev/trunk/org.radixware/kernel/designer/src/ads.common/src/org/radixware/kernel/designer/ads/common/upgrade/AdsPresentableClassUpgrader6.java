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


public class AdsPresentableClassUpgrader6 implements IRadixObjectUpgrader {

    public void firstStep(Element root) {
        processNode(root);
    }

    public void finalStep(RadixObject radixObject) {
    }

    private void processNode(Node e) {
        try {
            for (Node child = e.getFirstChild(); child != null; child = child.getNextSibling()) {
                if ("EditorPages".equals(child.getLocalName())) {
                    for (Node candidate = child.getFirstChild(); candidate != null; candidate = candidate.getNextSibling()) {
                        if ("Page".equals(candidate.getLocalName())) {
                            final Node pageNode = candidate;
                            final NamedNodeMap attributes = pageNode.getAttributes();
                            if (attributes != null) {
                                final Node propListNode = attributes.getNamedItem("PropIds");
                                if (propListNode != null) {
                                    final String propIdsListAsStr = propListNode.getNodeValue();
                                    if (propIdsListAsStr != null) {
                                        final String[] propIdsAsStr = propIdsListAsStr.split(" ");
                                        if (propIdsAsStr.length > 0) {
                                            final Element propsNode = pageNode.getOwnerDocument().createElementNS(pageNode.getNamespaceURI(), "Properties");
                                            pageNode.appendChild(propsNode);
                                            for (int i = 0; i < propIdsAsStr.length; i++) {
                                                final Element propNode = pageNode.getOwnerDocument().createElementNS(pageNode.getNamespaceURI(), "Property");
                                                propsNode.appendChild(propNode);
                                                propNode.setAttribute("Id", propIdsAsStr[i]);
                                                propNode.setAttribute("Row", String.valueOf(i));
                                                propNode.setAttribute("Column", "0");
                                            }
                                        }
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
