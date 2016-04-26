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
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocument;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;


public class CreateRootDocumentOperation extends XmlOperation {

    private final XmlDocument document;
    private final XmlEditorController controller;

    public CreateRootDocumentOperation(final QName choosenName, final SchemaType rootType, final XmlEditorController controller, final String value) {
        super(new int[0]);
        this.controller = controller;
        this.document = (rootType == null || rootType.isNoType()) ? new XmlDocument(choosenName, value) : new XmlDocument(rootType);
    }

    public CreateRootDocumentOperation(final XmlDocument document, final XmlEditorController controller) {
        super(new int[0]);
        this.controller = controller;
        this.document = document;
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
        final XmlDocumentItem nodeItem = document.getRootElement();
        final boolean isConformity = controller.isConform();
        final IXmlTreeItem rootItem = tree.createRootItem(nodeItem, document.getRootSchemaItem(), controller, isConformity);
        tree.setSelectedItem(rootItem);
        controller.setDocument(document); 
        return new DeleteRootDocumentOperation(rootItem, controller);
    }
}