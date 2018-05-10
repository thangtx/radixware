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

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.radixware.kernel.common.utils.XPathUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author dlastochkin
 */
public class XmlTreeNode extends DefaultMutableTreeNode {

    private final Element element;
    private boolean visible = true;

    public XmlTreeNode(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getXPath() {
        return XPathUtils.getXPath(element);
    }

    public String getAttributes() {
        StringBuilder attributes = new StringBuilder();
        attributes.append("[");
        boolean isFirst = true;
        for (int i = 0; i < element.getAttributes().getLength(); i++) {            
            Node curAttribute = element.getAttributes().item(i);
            if (curAttribute.getNodeName().equals("name")) {
                continue;
            }
            
            attributes.append(isFirst ? "" : "; ");
            attributes.append(curAttribute.getNodeName());
            attributes.append("='");
            attributes.append(curAttribute.getNodeValue());
            attributes.append("'");
            isFirst = false;
        }
        attributes.append("]");

        return attributes.toString();
    }

    @Override
    public String toString() {
        if (!"".equals(element.getAttribute("targetNamespace"))) {
            return element.getAttribute("targetNamespace");
        } else if (!"".equals(element.getAttribute("name"))) {
            return element.getAttribute("name");
        } else if (!"".equals(element.getAttribute("value"))) {
            return element.getAttribute("value");
        } else if (!"".equals(element.getAttribute("namespace"))) {
            return element.getNodeName() + " " + element.getAttribute("namespace");
        } else {
            return element.getNodeName();
        }
    }

    @Override
    public TreeNode getChildAt(int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }

        int realIndex = -1;
        int visibleIndex = -1;
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            XmlTreeNode node = (XmlTreeNode) e.nextElement();
            if (node.isVisible()) {
                visibleIndex++;
            }
            realIndex++;
            if (visibleIndex == index) {
                return (TreeNode) children.elementAt(realIndex);
            }
        }

        throw new ArrayIndexOutOfBoundsException("index unmatched");
    }

    @Override
    public int getChildCount() {
        if (children == null) {
            return 0;
        }

        int count = 0;
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            XmlTreeNode node = (XmlTreeNode) e.nextElement();
            if (node.isVisible()) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int getIndex(TreeNode aChild) {
        int result = super.getIndex(aChild);
        if (result == -1) {
            return result;
        } else {
            result = -1;
        }
        
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            XmlTreeNode node = (XmlTreeNode) e.nextElement();
            if (node.isVisible()) {
                result++;
                if (node == aChild) {
                    return result;
                }
            }
        }
        
        return result;
    }
}
