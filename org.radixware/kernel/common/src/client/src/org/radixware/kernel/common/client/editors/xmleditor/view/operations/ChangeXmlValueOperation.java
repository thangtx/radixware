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

import java.util.Arrays;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class ChangeXmlValueOperation extends XmlOperation {

    private String value;
    private final int nodeIndex;
    private final XmlOperation nilAttrOperation;

    public ChangeXmlValueOperation(final IXmlTreeItem item, final String value) {
        this(item, value, null);
    }

    private ChangeXmlValueOperation(final IXmlTreeItem item, final String value, final XmlOperation nilAttrOperation) {
        super(item.getParentItem() == null ? new int[0] : item.getParentItem().getIndexPath());
        this.value = value;
        final int[] path = item.getParentItem() == null ? new int[0] : item.getIndexPath();
        nodeIndex = path.length == 0 ? -1 : path[path.length - 1];
        this.nilAttrOperation = nilAttrOperation;
    }

    @Override
    public boolean merge(final XmlOperation other) {
        if (other instanceof ChangeXmlValueOperation) {
            return Arrays.equals(other.parentItemIndexPath, parentItemIndexPath)
                    && nodeIndex == ((ChangeXmlValueOperation) other).nodeIndex;
        } else {
            return false;
        }
    }

    @Override
    public boolean needToReinitParent() {
        return false;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem parentItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        if (parentItem != null) {
            final IXmlTreeItem childItem = nodeIndex >= 0 ? parentItem.getChildItem(nodeIndex) : parentItem;
            if (childItem != null) {
                XmlOperation nilAttrOper = null;
                final XmlModelItem childModelItem = childItem.getXmlItem();
                final XmlSchemaItem childSchemaItem = childModelItem.getSchemaItem();
                final XmlDocumentItem xmlNode = childModelItem.getXmlNode();
                if (nilAttrOperation == null
                        && childSchemaItem instanceof XmlSchemaElementItem
                        && ((XmlSchemaElementItem) childSchemaItem).isNillable()) {
                    XmlDocumentItem nilAttribute = null;
                    for (XmlDocumentItem item : xmlNode.getChildItems()) {
                        if (item.getType() == EXmlItemType.Attribute && XmlDocumentItem.XSI_NIL.equals(item.getFullName())) {
                            nilAttribute = item;
                            break;
                        }
                    }
                    if (value == null) {
                        if (nilAttribute == null) {
                            nilAttrOper = new CreateNilAttributeOperation(childItem, "true");
                        } else if (!"1".equals(nilAttribute.getValue()) && !"true".equals(nilAttribute.getValue())) {
                            nilAttrOper = new ChangeNilAttributeOperation(childItem, true);
                        }
                    } else if (nilAttribute != null && !"0".equals(nilAttribute.getValue()) && !"false".equals(nilAttribute.getValue())) {
                        nilAttrOper = new ChangeNilAttributeOperation(childItem, false);
                    }
                } else {
                    nilAttrOper = nilAttrOperation;
                }
                if (nilAttrOper != null) {
                    nilAttrOper = nilAttrOper.execute(tree);
                    if (nilAttrOper == null) {
                        return null;
                    }
                }
                final String oldValue = xmlNode.getValue();
                xmlNode.setValue(value);
                childItem.refresh();
                return new ChangeXmlValueOperation(childItem, oldValue, nilAttrOper);
            }
        }
        return null;
    }
}
