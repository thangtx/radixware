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

package org.radixware.kernel.common.client.editors.xmleditor.view;

import java.util.EnumSet;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;


public class XmlTreeController {

    private final XmlEditorController editorController;
    private final IXmlTree tree;

    public XmlTreeController(final XmlEditorController controller, final IXmlTree tree) {
        this.editorController = controller;
        this.tree = tree;
        updateBackground();
    }

    public void setupXmlTreeItem(final IXmlTreeItem treeItem, final XmlModelItem xmlItem) {
        treeItem.setTextValue(0, xmlItem.getXmlNode().getDisplayString());
        treeItem.setModelValue(1, xmlItem, isXmlItemReadOnly(xmlItem));
    }

    private boolean isXmlItemReadOnly(final XmlModelItem modelItem) {
        if (editorController.isReadOnly()) {
            return true;
        }
        if (modelItem == null) {
            return false;
        } else {
            final IXmlValueEditingOptionsProvider provider = editorController.getValueEditingOptionsProvider();
            final XmlValueEditingOptions options = modelItem.getValueEditingOptions(provider);
            return options == null ? false : options.isReadOnly();
        }
    }

    public boolean isItemReadOnly(final IXmlTreeItem treeItem) {
        return isXmlItemReadOnly(treeItem.getXmlItem());
    }

    public XmlEditorController getEditorController() {
        return editorController;
    }

    public IClientEnvironment getEnvironment() {
        return editorController.getEnvironment();
    }

    private void updateBackground() {
        final EnumSet<ETextOptionsMarker> markers = EnumSet.of(ETextOptionsMarker.EDITOR);
        if (editorController.isReadOnly()) {
            markers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        final ITextOptions options =
                getEnvironment().getTextOptionsProvider().getOptions(markers, null);
        tree.setBackgroundColor(options.getBackgroundColor()); 
    }

    public void refreshAllItems() {
        updateBackground();
        final Stack<IXmlTreeItem> items = new Stack<>();
        items.push(tree.getRootItem());
        if (!(items.size() == 1 && items.get(0) == null)) {
            IXmlTreeItem curItem;
            while (!items.isEmpty()) {
                curItem = items.pop();
                for (int i = 0, c = curItem.getChildCount(); i < c; i++) {
                    items.push(curItem.getChildItem(i));
                }
                curItem.refresh();
            }
        }
    }
}
