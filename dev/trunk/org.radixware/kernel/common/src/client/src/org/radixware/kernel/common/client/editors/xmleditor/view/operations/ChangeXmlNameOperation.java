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

import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class ChangeXmlNameOperation extends XmlOperation {

    private final QName name;
    private final int nodeIndex;
    private final boolean isElement;

    public ChangeXmlNameOperation(final IXmlTreeItem item, final QName name) {
        super(item.getParentItem() == null ? new int[0] : item.getParentItem().getIndexPath());
        isElement = item.getXmlItem().getXmlNode().getType() == EXmlItemType.Element;
        this.name = name;
        final int[] path = item.getParentItem() == null ? new int[0] : item.getIndexPath();
        nodeIndex = path.length == 0 ? -1 : path[path.length - 1];
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
                final QName oldName = childItem.getXmlItem().getXmlNode().getFullName();
                if (childItem.getXmlItem().getXmlNode().getType() == EXmlItemType.Attribute) {
                    childItem.setTextValue(0, name);
                    childItem.getXmlItem().getXmlNode().setName(name);
                } else {
                    childItem.setTextValue(0, "<" + name + ">");
                    childItem.getXmlItem().getXmlNode().setName(name);
                }
                if (childItem.getXmlItem().getParent() != null) {
                    childItem.getXmlItem().getParent().clearCache();//clear logical items cache
                }
                parentItem.getXmlItem().clearCache();//clear xml-document items cache
                return new ChangeXmlNameOperation(childItem, oldName);
            }
        }
        return null;
    }
}
