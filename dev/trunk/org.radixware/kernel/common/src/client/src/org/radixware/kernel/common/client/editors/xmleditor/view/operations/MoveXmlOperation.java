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

package org.radixware.kernel.common.client.editors.xmleditor.view.operations;

import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class MoveXmlOperation extends XmlOperation {

    private final int nodeIndex;
    private final int targetIndex;

    public MoveXmlOperation(final IXmlTreeItem item, final IXmlTreeItem item2) {
        super(item.getParentItem() == null ? new int[0] : item.getParentItem().getIndexPath());
        final int[] path = item.getIndexPath();
        final int[] path2 = item2.getIndexPath();
        nodeIndex = path[path.length - 1];
        targetIndex = path2[path2.length - 1];
    }

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }

    private void swapTreeItems(final IXmlTreeItem item1, final IXmlTreeItem item2) {
        final XmlModelItem templateItem1 = item1.getXmlItem();
        final XmlModelItem templateItem2 = item2.getXmlItem();
        final XmlDocumentItem templateNode1 = templateItem1.getXmlNode();
        final XmlDocumentItem templateNode2 = templateItem2.getXmlNode();
        final boolean isTemplateItem1ReadOnly = item1.isReadOnly();
        final boolean isTemplateItem2ReadOnly = item2.isReadOnly();
        item1.setTextValue(0, templateNode2.getDisplayString());
        item2.setTextValue(0, templateNode1.getDisplayString());
        item1.setModelValue(1, templateItem2, isTemplateItem2ReadOnly);
        item2.setModelValue(1, templateItem1, isTemplateItem1ReadOnly);
        item1.reinitChildren();
        item2.reinitChildren();
    }

    @Override
    public boolean needToReinitParent() {
        return false;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem parentItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        final XmlDocumentItem parent = parentItem.getXmlItem().getXmlNode();
        if (parentItem != null) {
            final IXmlTreeItem childItem = parentItem.getChildItem(nodeIndex);
            if (childItem != null) {
                final IXmlTreeItem childNeighborItem = parentItem.getChildItem(targetIndex);
                if (childNeighborItem != null) {
                    parentItem.getXmlItem().clearCache();//clear logical items cache 
                    final IXmlTreeItem item1, item2;
                    if (nodeIndex < targetIndex) {
                        parent.moveChild(childItem.getXmlItem().getXmlNode(), childNeighborItem.getXmlItem().getXmlNode());
                        item1 = childItem;
                        item2 = childNeighborItem;                        
                    } else {
                        parent.moveChild(childNeighborItem.getXmlItem().getXmlNode(), childItem.getXmlItem().getXmlNode());
                        item1 = childNeighborItem;
                        item2 = childItem;                        
                    }                    
                    childItem.getXmlItem().getParent().clearCache();//clear logical items cache
                    parentItem.getXmlItem().clearCache();//clear xml-document items cache               
                    childItem.getXmlItem().clearCache();
                    childNeighborItem.getXmlItem().clearCache();
                    swapTreeItems(item1, item2);
                    tree.setSelectedItem(childNeighborItem);
                }
                return new MoveXmlOperation(childNeighborItem, childItem);
            }
        }
        return null;
    }
}
