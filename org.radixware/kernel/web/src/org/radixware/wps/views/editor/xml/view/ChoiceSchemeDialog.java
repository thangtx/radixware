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

package org.radixware.wps.views.editor.xml.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaContainerItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementsListItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IChoiceScheme;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.tree.Node.DefaultNode;
import org.radixware.wps.rwt.tree.Tree;


public class ChoiceSchemeDialog extends Dialog implements IChoiceScheme {

    private Tree options = new Tree();
    private int currentChildItemIndex;
    Map<String, String> nameSpaceURI = new HashMap<>();

    public ChoiceSchemeDialog(final XmlSchemaItem choiceItem,
            final int currentChildItemIndex) {
        setWindowTitle(getEnvironment().getMessageProvider().translate("XmlEditor", "Replace Item"));
        options.getAnchors().setTop(new Anchors.Anchor(0, 1));
        options.getAnchors().setLeft(new Anchors.Anchor(0, 0));
        options.getAnchors().setRight(new Anchors.Anchor(1, -1));
        options.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        final DefaultNode rootNode = new DefaultNode();
        options.setRootNode(rootNode);
        options.setRootVisible(false);
        this.currentChildItemIndex = currentChildItemIndex;
        getItems(choiceItem, currentChildItemIndex, rootNode);
        add(options);
        addCloseAction(EDialogButtonType.OK, new CloseActionHandler() {
            @Override
            public boolean canClose(String action, Object data) {
                if (options.getSelectedNode() == null || options.getSelectedNode().getChildCount() > 0) {
                    return false;
                }
                return true;
            }

            @Override
            public void closed(String action, Object data) {
                if (!canClose(action, data)) {
                    getEnvironment().messageError(getEnvironment().getMessageProvider().translate("XmlEditor", "Empty value!"));
                }
            }
        });
        addCloseAction(EDialogButtonType.CANCEL);
        options.showHeader(false);
        setWidth(300);
        setHeight(150);
        setMinimumHeight(150);
        setMaxHeight(150);
    }

    public ChoiceSchemeDialog(final List<XmlSchemaItem> choiceItem) {
        setWindowTitle(getEnvironment().getMessageProvider().translate("XmlEditor", "Replace Item"));
        options.getAnchors().setTop(new Anchors.Anchor(0, 1));
        options.getAnchors().setLeft(new Anchors.Anchor(0, 0));
        options.getAnchors().setRight(new Anchors.Anchor(1, -1));
        options.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        final DefaultNode rootNode = new DefaultNode();
        options.setRootNode(rootNode);
        options.setRootVisible(false);
        getItem(choiceItem, rootNode);
        add(options);
        addCloseAction(EDialogButtonType.OK, new CloseActionHandler() {
            @Override
            public boolean canClose(String action, Object data) {
                if (options.getSelectedNode() == null || options.getSelectedNode().getChildCount() > 0) {
                    return false;
                }
                return true;
            }

            @Override
            public void closed(String action, Object data) {
                if (!canClose(action, data)) {
                    getEnvironment().messageError(getEnvironment().getMessageProvider().translate("XmlEditor", "Empty value!"));
                }
            }
        });
        addCloseAction(EDialogButtonType.CANCEL);
        options.showHeader(false);
        setWidth(300);
        setHeight(150);
        setMinimumHeight(150);
        setMaxHeight(150);
    }

    private List<QName> getItems(XmlSchemaItem schemaItem, int currentChildItemIndex, DefaultNode treeItem) {
        List<QName> names = new ArrayList<>();
        int index = 0;
        for (XmlSchemaItem item : schemaItem.getChildItems()) {
            DefaultNode childItem;
            if ((item instanceof XmlSchemaElementItem || item instanceof XmlSchemaElementsListItem) && index != currentChildItemIndex) {
                childItem = new DefaultNode();
                final QName name =
                        item instanceof XmlSchemaElementItem ? ((XmlSchemaElementItem) item).getFullName() : ((XmlSchemaElementsListItem) item).getFullName();
                names.add(name);
                childItem.setCellValue(0, name.getLocalPart());
                if (name.getNamespaceURI() != null && !name.getNamespaceURI().isEmpty()) {
                    nameSpaceURI.put(name.getLocalPart(), name.getNamespaceURI());
                }
                treeItem.add(childItem);
            } else if (item instanceof XmlSchemaContainerItem && index != currentChildItemIndex) {
                childItem = new DefaultNode();
                treeItem.add(childItem);
                final List<QName> childNames = getItems(item, currentChildItemIndex + item.getChildItems().size(), childItem);
                final StringBuilder nameBuilder = new StringBuilder();
                nameBuilder.append('[');
                for (QName childName : childNames) {
                    if (nameBuilder.length() > 0) {
                        nameBuilder.append(' ');
                    }
                    nameBuilder.append(childName);
                }
                nameBuilder.append(']');
                childItem.setCellValue(0, nameBuilder.toString());
                childItem.getTree().expandAllNodes();
                names.add(new QName(nameBuilder.toString()));
            }
            index++;
        }
        return names;
    }

    private List<QName> getItem(List<XmlSchemaItem> schemaItems, DefaultNode treeItem) {
        List<QName> names = new ArrayList<>();
        for (XmlSchemaItem item : schemaItems) {
            DefaultNode childItem;
            if ((item instanceof XmlSchemaElementItem || item instanceof XmlSchemaElementsListItem)) {
                childItem = new DefaultNode();
                final QName name =
                        item instanceof XmlSchemaElementItem ? ((XmlSchemaElementItem) item).getFullName() : ((XmlSchemaElementsListItem) item).getFullName();
                names.add(name);
                childItem.setCellValue(0, name.getLocalPart());
                treeItem.add(childItem);
                if (name.getNamespaceURI() != null && !name.getNamespaceURI().isEmpty()) {
                    nameSpaceURI.put(name.getLocalPart(), name.getNamespaceURI());
                }
            }
        }
        return names;
    }

    @Override
    public QName getName() {
        options.addFocusListener(new FocusListener() {
            @Override
            public void focusEvent(UIObject target, boolean focused) {
            }
        });
        if (!nameSpaceURI.isEmpty() && nameSpaceURI.containsKey(options.getSelectedNode().getDisplayName())) {
            return new QName(nameSpaceURI.get(options.getSelectedNode().getDisplayName()), options.getSelectedNode().getDisplayName());
        } else {
            return new QName(options.getSelectedNode().getDisplayName());
        }
    }

    @Override
    public int getIndexOptions() {
        return currentChildItemIndex;
    }
}
