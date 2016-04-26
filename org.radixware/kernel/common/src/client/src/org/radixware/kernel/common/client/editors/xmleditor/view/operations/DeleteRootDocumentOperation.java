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

import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;


public class DeleteRootDocumentOperation extends XmlOperation {

    private final XmlEditorController controller;

    public DeleteRootDocumentOperation(final IXmlTreeItem item, final XmlEditorController editorController) {
        super(item.getIndexPath());
        controller = editorController;
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
        final IXmlTreeItem treeItem = tree.getCurrentItem();
        if (treeItem != null) {
            final CreateRootDocumentOperation createRoot = new CreateRootDocumentOperation(controller.getDocument(), controller);
            treeItem.delete();
            controller.setDocument(null);
            return createRoot;
        }
        return null;
    }
}
