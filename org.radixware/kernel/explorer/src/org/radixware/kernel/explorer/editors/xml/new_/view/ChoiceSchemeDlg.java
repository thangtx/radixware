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

package org.radixware.kernel.explorer.editors.xml.new_.view;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaContainerItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementsListItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IChoiceScheme;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


public class ChoiceSchemeDlg extends ExplorerDialog implements IChoiceScheme {

    private QTreeWidget options = new QTreeWidget();
    private int currentChildItemIndex;
    Map<String, String> nameSpaceURI = new HashMap<>();

    public ChoiceSchemeDlg(final IClientEnvironment environment,
            final QWidget parent,
            final XmlSchemaItem choiceItem,
            final int currentChildItemIndex) {
        super(environment, parent);
        this.currentChildItemIndex = currentChildItemIndex;
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Replace Item"));
        options.header().setVisible(false);
        getItems(choiceItem, currentChildItemIndex, null);
        layout().addWidget(new QLabel(environment.getMessageProvider().translate("XmlEditor", "Element for replace:")));
        layout().addWidget(options);
        options.currentItemChanged.connect(this, "chooseName()");
        addButton(EDialogButtonType.OK).setEnabled(false);
        addButton(EDialogButtonType.CANCEL);
        acceptButtonClick.connect(this, "accept()");
        rejectButtonClick.connect(this, "reject()");
    }

    public ChoiceSchemeDlg(final IClientEnvironment environment,
            final QWidget parent,
            final List<XmlSchemaItem> choiceItems) {
        super(environment, parent);
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Replace Item"));
        options.header().setVisible(false);
        getRootItems(choiceItems);
        layout().addWidget(new QLabel(environment.getMessageProvider().translate("XmlEditor", "Element for replace:")));
        layout().addWidget(options);
        options.currentItemChanged.connect(this, "chooseName()");
        addButton(EDialogButtonType.OK).setEnabled(false);
        addButton(EDialogButtonType.CANCEL);
        acceptButtonClick.connect(this, "accept()");
        rejectButtonClick.connect(this, "reject()");
    }

    private List<QName> getItems(final XmlSchemaItem schemaItem, int currentChildItemIndex, QTreeWidgetItem treeItem) {
        List<QName> names = new ArrayList<>();
        int index = 0;
        for (XmlSchemaItem item : schemaItem.getChildItems()) {
            QTreeWidgetItem childItem;
            if ((item instanceof XmlSchemaElementItem || item instanceof XmlSchemaElementsListItem) && index != currentChildItemIndex) {
                childItem = new QTreeWidgetItem();
                final QName name =
                        item instanceof XmlSchemaElementItem ? ((XmlSchemaElementItem) item).getFullName() : ((XmlSchemaElementsListItem) item).getFullName();
                names.add(name);
                childItem.setText(0, name.getLocalPart());
                if (name.getNamespaceURI() != null && !name.getNamespaceURI().isEmpty()) {
                    nameSpaceURI.put(name.getLocalPart(), name.getNamespaceURI());
                }
                if (treeItem == null) {
                    options.addTopLevelItem(childItem);
                } else {
                    treeItem.addChild(childItem);
                }
            } else if (item instanceof XmlSchemaContainerItem && index != currentChildItemIndex) {
                childItem = new QTreeWidgetItem();
                if (treeItem == null) {
                    options.addTopLevelItem(childItem);
                } else {
                    treeItem.addChild(childItem);
                }
                final List<QName> childNames = getItems(item, currentChildItemIndex + item.getChildItems().size(), childItem);
                final StringBuilder nameBuilder = new StringBuilder();
                nameBuilder.append('[');
                for (QName childName : childNames) {
                    if (nameBuilder.length() > 0) {
                        nameBuilder.append(' ');
                    }
                    nameBuilder.append(childName.getLocalPart());
                }
                nameBuilder.append(']');
                childItem.setText(0, nameBuilder.toString());
                childItem.setExpanded(true);
                names.add(new QName(nameBuilder.toString()));
            }
            index++;
        }
        return names;
    }

    private List<QName> getRootItems(final List<XmlSchemaItem> schemaItems) {
        List<QName> names = new ArrayList<>();
        for (XmlSchemaItem item : schemaItems) {
            QTreeWidgetItem childItem;
            if ((item instanceof XmlSchemaElementItem || item instanceof XmlSchemaElementsListItem)) {
                childItem = new QTreeWidgetItem();
                final QName name =
                        item instanceof XmlSchemaElementItem ? ((XmlSchemaElementItem) item).getFullName() : ((XmlSchemaElementsListItem) item).getFullName();
                names.add(name);
                childItem.setText(0, name.getLocalPart());
                if (name.getNamespaceURI() != null && !name.getNamespaceURI().isEmpty()) {
                    nameSpaceURI.put(name.getLocalPart(), name.getNamespaceURI());
                }
                options.addTopLevelItem(childItem);
            }
        }
        return names;
    }

    @SuppressWarnings("unused")
    private void chooseName() {
        if (options.currentItem().childCount() == 0) {
            getButton(EDialogButtonType.OK).setEnabled(true);
        } else {
            getButton(EDialogButtonType.OK).setEnabled(false);
        }
    }

    @Override
    public QName getName() {
        if (!nameSpaceURI.isEmpty() && nameSpaceURI.containsKey(options.currentItem().text(0))) {
            return new QName(nameSpaceURI.get(options.currentItem().text(0)), options.currentItem().text(0));
        } else {
            return new QName(options.currentItem().text(0));
        }
    }

    @Override
    public int getIndexOptions() {
        return currentChildItemIndex;
    }
}
