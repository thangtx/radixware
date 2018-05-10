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
package org.radixware.kernel.designer.ads.editors.xml;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;

public class XmlTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Color DOCUMENTED_NODE = new Color(35, 140, 35);
    private static final Color UNDOCUMENTED_NODE = new Color(161, 37, 37);
    static final Color SERVICE_NODE = new Color(91, 91, 91);

    private final TreeNodePanel nodePanel;

    private Map<String, AdsMultilingualStringDef> documentedNodes;
    private final List<EIsoLanguage> langs;

    public XmlTreeCellRenderer(List<EIsoLanguage> langs, Map<String, AdsMultilingualStringDef> documentedNodes) {
        this.documentedNodes = new HashMap<>(documentedNodes);
        this.langs = new ArrayList<>(langs);
        nodePanel = new TreeNodePanel(this, langs);
    }

    Map<String, AdsMultilingualStringDef> getDocumentedNodes() {
        return new HashMap<>(documentedNodes);
    }

    public void setDocumentedNodes(Map<String, AdsMultilingualStringDef> documentedNodes) {
        this.documentedNodes = new HashMap<>(documentedNodes);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        XmlTreeNode node = (XmlTreeNode) value;
        final boolean hasDocumentation = documentedNodes != null && documentedNodes.containsKey(node.getXPath());
        final boolean needsDocumentation = XmlTreeUtils.needsDocumentation(node);
        nodePanel.setIconsVisible(false);

        if (hasDocumentation) {
            setForeground(DOCUMENTED_NODE);

            AdsMultilingualStringDef mlString = documentedNodes.get(node.getXPath());
            for (EIsoLanguage lang : langs) {
                if (Utils.emptyOrNull(mlString.getValue(lang))) {
                    nodePanel.setWarningIconVisible(true);
                    nodePanel.setLangIconVisible(lang, true);
                }
            }
        } else {
            if (needsDocumentation) {
                setForeground(UNDOCUMENTED_NODE);
            } else {
                setForeground(SERVICE_NODE);
            }
        }

        if (sel) {
            setForeground(Color.WHITE);
        }

        nodePanel.setAttributesLabelVisible(needsDocumentation && !"[]".equals(node.getAttributes()));
        nodePanel.setAttributesLabelText(node.getAttributes());
        setToolTipText(!"[]".equals(node.getAttributes()) && !needsDocumentation ? node.getAttributes() : null);

        if (null != node.getElement().getNodeName()) {
            switch (node.getElement().getLocalName()) {
                case "all":
                    setIcon(RadixWareIcons.XML.ALL.getIcon());
                    break;
                case "attribute":
                    setIcon(RadixWareIcons.XML.ATTRIBUTE.getIcon());
                    break;
                case "attributeGroup":
                    setIcon(RadixWareIcons.XML.ATTRIBUTE_GROUP.getIcon());
                    break;
                case "choice":
                    setIcon(RadixWareIcons.XML.CHOICE.getIcon());
                    break;
                case "complexType":
                    setIcon(RadixWareIcons.XML.COMPLEX_TYPE.getIcon());
                    break;
                case "element":
                    setIcon(RadixWareIcons.XML.ELEMENT.getIcon());
                    break;
                case "enumeration":
                    setIcon(RadixWareIcons.XML.ENUM.getIcon());
                    break;
                case "import":
                    setIcon(RadixWareIcons.XML.IMPORTED_SCHEMA.getIcon());
                    break;
                case "restriction":
                    setIcon(RadixWareIcons.XML.RESTRICTION.getIcon());
                    break;
                case "schema":
                    setIcon(RadixWareIcons.XML.SCHEMA.getIcon());
                    break;
                case "sequence":
                    setIcon(RadixWareIcons.XML.SEQUENCE.getIcon());
                    break;
                case "simpleType":
                    setIcon(RadixWareIcons.XML.SIMPLE_TYPE.getIcon());
                    break;
                default:
                    setIcon(leafIcon);
            }
        }

        nodePanel.revalidate();
        return nodePanel;
    }

    @Override
    public void setToolTipText(String text) {
        nodePanel.setToolTipText(text);
    }
}
