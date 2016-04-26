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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class ChoiceItemOperation extends XmlOperation {

    private QName newName;
    private final int currentItemIndex;
    private List<XmlOperation> listOperations;

    private ChoiceItemOperation(final int[] parentItemIndexPath, final int currentItemIndex, List<XmlOperation> listOperations, final QName newName) {
        super(parentItemIndexPath);
        this.newName = newName;
        this.listOperations = listOperations;
        this.currentItemIndex = currentItemIndex;
    }

    public ChoiceItemOperation(final IXmlTreeItem item, final QName newName) {
        this(item.getParentItem().getIndexPath(), item.getIndexInParent(), Collections.<XmlOperation>emptyList(), newName);
    }

    private ChoiceItemOperation(final int[] parentItemIndexPath, final List<XmlOperation> listOperations) {
        this(parentItemIndexPath, -1, listOperations, new QName(""));
    }

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }

    @Override
    public boolean needToReinitParent() {
        return false;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem parentItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        if (parentItem != null) {
            if (!listOperations.isEmpty()) {
                final List<XmlOperation> undoOperations = new LinkedList<>();
                final List<XmlOperation> createOperations = new LinkedList<>();
                for (XmlOperation operation : listOperations) {
                    final XmlOperation undoOperation = operation.execute(tree);
                    if (undoOperation == null) {
                        execOperations(undoOperations, tree);
                        return null;
                    }
                    if (undoOperation instanceof CreateChildElementOperation) {
                        createOperations.add(0, undoOperation);
                    } else {
                        undoOperations.add(undoOperation);
                    }
                }
                undoOperations.addAll(createOperations);
                return new ChoiceItemOperation(getParentItemIndexPath(), undoOperations);
            }
            final IXmlTreeItem currentItem = currentItemIndex >= 0 ? parentItem.getChildItem(currentItemIndex) : parentItem;
            if (currentItem != null) {
                final XmlModelItem modelItem = currentItem.getXmlItem();
                final XmlModelItem choiceItem = modelItem.findParentChoiceItem();
                final List<IXmlTreeItem> itemsInChoice = new LinkedList<>();
                int firstItemInChoice = -1;
                int elementsCounter = -1;
                for (int i = 0, count = parentItem.getChildCount(); i < count; i++) {
                    final IXmlTreeItem childItem = parentItem.getChildItem(i);
                    if (childItem.getXmlItem().getXmlNode().getType() == EXmlItemType.Element) {
                        elementsCounter++;
                        if (childItem.getXmlItem().isValid() && childItem.getXmlItem().isConformity()
                                && childItem.getXmlItem().findParentChoiceItem() != null
                                && childItem.getXmlItem().findParentChoiceItem().getSchemaItem() == choiceItem.getSchemaItem()) {
                            if (firstItemInChoice < 0) {
                                firstItemInChoice = elementsCounter;
                            }
                            itemsInChoice.add(childItem);
                        }
                    }
                }
                final List<XmlOperation> undoOperations = new LinkedList<>();
                for (IXmlTreeItem itemInChoice : itemsInChoice) {
                    final XmlOperation undoOperation = new DeleteXmlNodeOperation(itemInChoice).execute(tree);
                    if (undoOperation == null) {
                        execOperations(undoOperations, tree);
                        return null;
                    }
                    undoOperations.add(0, undoOperation);
                }
                final XmlOperation undoOperation
                        = new CreateChildElementOperation(parentItem, newName, (String) null, firstItemInChoice).execute(tree);
                if (undoOperation == null) {
                    execOperations(undoOperations, tree);
                    return null;
                }
                undoOperations.add(0, undoOperation);
                return new ChoiceItemOperation(getParentItemIndexPath(), undoOperations);
            }
        }
        return null;
    }

    private static void execOperations(final List<XmlOperation> operations, final IXmlTree tree) {
        for (XmlOperation operation : operations) {
            operation.execute(tree);
        }
    }
}
