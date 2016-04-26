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

import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class DeleteXmlNodeOperation extends XmlOperation {

    private final int nodeIndex;
    private final boolean isElement;

    public DeleteXmlNodeOperation(final IXmlTreeItem item) {
        super(item.getParentItem().getIndexPath());
        isElement = item.getXmlItem().getXmlNode().getType() == EXmlItemType.Element;
        final int[] path = item.getIndexPath();
        nodeIndex = path[path.length - 1];
    }

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }

    @Override
    public boolean needToReinitParent() {
        return isElement;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem parentItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        if (parentItem != null) {
            final IXmlTreeItem childItem = nodeIndex >= 0 ? parentItem.getChildItem(nodeIndex) : parentItem;
            if (childItem != null) {
                final XmlDocumentItem childItemNode = childItem.getXmlItem().getXmlNode();
                final boolean isAttribute = childItemNode.getType() == EXmlItemType.Attribute;
                final XmlOperation createOperation;
                if (isAttribute) {
                    createOperation
                            = new CreateChildAttributeOperation(parentItem, childItemNode.getFullName(), childItemNode.getValue(), nodeIndex);
                } else {
                    final int elementIndex = nodeIndex - parentItem.getXmlItem().getXmlNode().getAttributesCount();
                    createOperation
                            = new CreateChildElementOperation(parentItem, childItemNode.getFullName(), childItemNode.getXmlObject(), elementIndex);
                }
                childItemNode.delete();
                childItem.getXmlItem().getParent().clearCache();//clear logical items cache
                parentItem.getXmlItem().clearCache();//clear xml-document items cache
                childItem.delete();
                XmlModelItem newCurrentItem = tree.getCurrentItem().getXmlItem();
                if (newCurrentItem != null) {
                    if (newCurrentItem.getParent() != null) {
                        newCurrentItem.getParent().clearCache();
                    }
                    parentItem.getXmlItem().clearCache();
                }
                return createOperation;
            }
        }
        return null;
    }
}
