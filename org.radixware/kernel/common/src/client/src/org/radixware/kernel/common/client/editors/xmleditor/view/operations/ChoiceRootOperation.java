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
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;


public class ChoiceRootOperation extends XmlOperation {

    private final QName newName;
    private List<XmlOperation> listOperations;
    private final XmlEditorController editorController;
    private final SchemaType schemaType;

    public ChoiceRootOperation(final int[] parentItemIndexPath, List<XmlOperation> listOperations, final QName newName, final SchemaType schemaType, final XmlEditorController editorController) {
        super(new int [0]);
        this.newName = newName;
        this.listOperations = listOperations;
        SchemaType choiceType = schemaType;
        for (SchemaType itemType : schemaType.getTypeSystem().documentTypes()) {
            if (itemType.getDocumentElementName().equals(newName)) {
                choiceType = itemType;
                break;
            }
        }
        this.editorController = editorController;
        this.schemaType = choiceType;
    }

    public ChoiceRootOperation(final IXmlTreeItem item, final QName newName, final SchemaType schemaType, final XmlEditorController editorController) {
        this(item.getIndexPath(), Collections.<XmlOperation>emptyList(), newName, schemaType, editorController);
    }

    private ChoiceRootOperation(final int[] parentItemIndexPath, final List<XmlOperation> listOperations, final SchemaType schemaType, final XmlEditorController editorController) {
        this(parentItemIndexPath, listOperations, new QName(""), schemaType, editorController);
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
        final IXmlTreeItem rootItem = tree.findByIndexPath(getParentItemIndexPath(), false);
        if (rootItem != null) {
            if (!listOperations.isEmpty()) {
                final List<XmlOperation> undoOperations = new LinkedList<>();
                CreateRootDocumentOperation createOperation = null;
                for (XmlOperation operation : listOperations) {
                    final XmlOperation undoOperation = operation.execute(tree);
                    if (undoOperation == null) {
                        return null;
                    }
                    if (undoOperation instanceof CreateRootDocumentOperation) {
                        createOperation = (CreateRootDocumentOperation) undoOperation;
                    } else {
                        undoOperations.add(undoOperation);
                    }
                }
                undoOperations.add(createOperation);
                return new ChoiceRootOperation(getParentItemIndexPath(), undoOperations, newName, schemaType, editorController);
            }
            final List<XmlOperation> undoOperations = new LinkedList<>();
            final XmlOperation createOperation = new DeleteRootDocumentOperation(rootItem, editorController).execute(tree);
            if (createOperation == null) {
                return null;
            }
            undoOperations.add(0, createOperation);
            final XmlOperation deleteOperation =
                    new CreateRootDocumentOperation(newName, schemaType, editorController, (String) null).execute(tree);
            if (deleteOperation == null) {
                createOperation.execute(tree);
                return null;
            }
            undoOperations.add(0, deleteOperation);
            return new ChoiceRootOperation(getParentItemIndexPath(), undoOperations, schemaType, editorController);
        }
        return null;
    }
}
