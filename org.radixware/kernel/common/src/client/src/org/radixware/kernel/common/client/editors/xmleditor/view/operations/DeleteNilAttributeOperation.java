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

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


final class DeleteNilAttributeOperation extends XmlOperation {
    
    private final List<QName> removeNamespaces;
    private final QName attrName;
    
    public DeleteNilAttributeOperation(final IXmlTreeItem item, final QName nilAttrName, final List<QName> removeNamespaces) {
        super(item.getIndexPath());
        this.removeNamespaces = new ArrayList<>(removeNamespaces);
        attrName = nilAttrName;
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
        final IXmlTreeItem treeItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        if (treeItem != null) {
            final XmlModelItem modelItem = treeItem.getXmlItem();
            final XmlDocumentItem xmlNode = modelItem.getXmlNode();
            XmlDocumentItem nilAttribute = null;
            for (XmlDocumentItem item : xmlNode.getChildItems()) {
                if (item.getType() == EXmlItemType.Attribute && attrName.equals(item.getFullName())) {
                    nilAttribute = item;
                    break;
                }
            }
            if (nilAttribute==null){
                return null;
            }
            final String prevValue = nilAttribute.getValue();
            nilAttribute.delete();
            for (QName namespaceName: removeNamespaces){
                xmlNode.removeNamespaceUri(namespaceName.getLocalPart());
            }
            return new CreateNilAttributeOperation(treeItem,prevValue);
        }
        return null;
    }
}
