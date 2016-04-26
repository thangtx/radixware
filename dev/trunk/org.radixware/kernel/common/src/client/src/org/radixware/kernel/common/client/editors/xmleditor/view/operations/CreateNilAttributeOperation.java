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

import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


final class CreateNilAttributeOperation extends XmlOperation {

    private final String value;

    public CreateNilAttributeOperation(final IXmlTreeItem parentItem,
            final String value) {
        super(parentItem.getIndexPath());
        this.value = value;
    }

    @Override
    public boolean needToReinitParent() {
        return false;
    }

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem treeItem = tree.findByIndexPath(parentItemIndexPath, false);
        if (treeItem == null) {
            return null;
        }
        final QName xsi_nil = new QName(XmlDocumentItem.XSI_NIL.getNamespaceURI(),"nil",XmlDocumentItem.XSI_NIL.getPrefix());
        final XmlModelItem modelItem = treeItem.getXmlItem();
        final XmlDocumentItem nodeItem = modelItem.getXmlNode();
        final List<QName> existingNamespaces = nodeItem.getNamespaces();       
        final XmlDocumentItem nilItem = nodeItem.createAttribute(xsi_nil, value, -1);
        final List<QName> newNamespaces = nodeItem.getNamespaces();
        newNamespaces.removeAll(existingNamespaces);
        treeItem.getXmlItem().clearCache();
        return new DeleteNilAttributeOperation(treeItem, nilItem.getFullName(), newNamespaces);
    }
}