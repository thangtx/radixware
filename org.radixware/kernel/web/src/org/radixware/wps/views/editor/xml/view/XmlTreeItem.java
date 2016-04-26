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
package org.radixware.wps.views.editor.xml.view;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.text.WpsTextOptions;

public class XmlTreeItem extends Node implements IXmlTreeItem {

    @Override
    public XmlModelItem getXmlItem() {
        return (XmlModelItem) getCellValue(1);
    }

    @Override
    public XmlTree getTree() {
        return (XmlTree) super.getTree();
    }

    @Override
    public void delete() {
        getTree().beforeDeleteNode(this);
        remove();
    }

    @Override
    public XmlTreeItem getNextSiblingItem() {
        final Node parentNode = getParentNode();
        final int index = parentNode.getChildNodes().getCreatedNodes().indexOf(this);
        if (index < parentNode.getChildCount() - 1) {
            return (XmlTreeItem) parentNode.getChildAt(index + 1);
        }
        return null;
    }

    @Override
    public XmlTreeItem getPreviousSiblingItem() {
        final Node parentNode = getParentNode();
        final int index = parentNode.getChildNodes().getCreatedNodes().indexOf(this);
        if (index > 0) {
            return (XmlTreeItem) parentNode.getChildAt(index - 1);
        }
        return null;
    }

    @Override
    public void reinitChildren() {
        final boolean wasExpanded = isExpanded();
        collapse();
        if (getXmlItem().getVisibleChildItems().isEmpty()) {
            setChildNodes(Children.LEAF);
        } else {
            setChildNodes(new ChildXmlItems(getXmlItem(), getTree().getTreeController()));
        }
        if (wasExpanded) {
            expand();
        }
    }

    @Override
    public XmlTreeItem getParentItem() {
        return getParentNode() instanceof XmlTreeItem ? (XmlTreeItem) getParentNode() : null;
    }

    @Override
    public XmlTreeItem getChildItem(int index) {
        return (XmlTreeItem) getChildAt(index);
    }

    @Override
    public int getIndexInParent() {
        return getParentNode() == null ? 0 : getParentNode().getChildNodes().getNodes().indexOf(this);
    }

    @Override
    public void refresh() {
        if (getTree() != null) {
            final XmlModelItem modelItem = getXmlItem();
            getTree().getTreeController().setupXmlTreeItem(this, modelItem);
            final ITextOptions options = modelItem.getNameTextOptions(getEnvironment(), isReadOnly());
            setTextOptions(0,(WpsTextOptions)options);
            //setBackground(options.getBackgroundColor());
        }
    }
    
    @Override
    public IClientEnvironment getEnvironment() {
        return getEnvironmentStatic();
    }

    @Override
    public boolean isReadOnly() {
        return getTree().getTreeController().isItemReadOnly(this);
    }

    @Override
    public void setModelValue(int column, Object value, final boolean isReadOnly) {
        setCellValue(1, value);
        final XmlModelItem xmlItem = (XmlModelItem) value;
        setTextOptions(0, (TextOptions) xmlItem.getNameTextOptions(getEnvironment(), isReadOnly));
    }

    @Override
    public void setTextValue(int column, Object value) {
        setCellValue(0, value);
    }
}