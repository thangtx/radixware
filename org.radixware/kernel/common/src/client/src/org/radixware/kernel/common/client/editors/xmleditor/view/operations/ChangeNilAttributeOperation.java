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



final class ChangeNilAttributeOperation extends XmlOperation {

    private final boolean value;

    public ChangeNilAttributeOperation(final IXmlTreeItem item, final boolean value) {
        super(item.getIndexPath());
        this.value = value;
    }

    @Override
    public boolean needToReinitParent() {
        return false;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem treeItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        if (treeItem != null) {
            final XmlModelItem modelItem = treeItem.getXmlItem();
            final XmlDocumentItem xmlNode = modelItem.getXmlNode();
            XmlDocumentItem nilAttribute = null;
            for (XmlDocumentItem item : xmlNode.getChildItems()) {
                if (item.getType() == EXmlItemType.Attribute && XmlDocumentItem.XSI_NIL.equals(item.getFullName())) {
                    nilAttribute = item;
                    break;
                }
            }
            if (nilAttribute == null) {
                return null;
            }
            final boolean oldValue = "1".equals(nilAttribute.getValue()) || "true".equals(nilAttribute.getValue());
            nilAttribute.setValue(String.valueOf(value));
            treeItem.getXmlItem().getParent().clearCache();//clear logical items cache
            treeItem.getXmlItem().clearCache();//clear xml-document items cache
            return new ChangeNilAttributeOperation(treeItem, oldValue);
        }
        return null;
    }

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }
}
