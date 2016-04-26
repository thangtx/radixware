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

import com.trolltech.qt.core.Qt;
import org.radixware.kernel.explorer.editors.xml.new_.view.editors.XmlValueEdit;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

public class XmlTreeItem extends QTreeWidgetItem implements IXmlTreeItem {

    private final class UnexpectedTextWidget extends ValStrEditor {

        @SuppressWarnings("LeakingThisInConstructor")
        public UnexpectedTextWidget(final String text, final boolean isReadOnly) {
            super(environment, null);
            setValue(text);
            setMandatory(false);
            setValidationResult(ValidationResult.Factory.newInvalidResult(environment.getMessageProvider().translate("XmlEditor", "Current item can't have value!")));
            setReadOnly(isReadOnly);
            getLineEdit().setReadOnly(true);
            if (!isReadOnly) {
                clearBtn.clicked.connect(this, "onClearBtn()");
            }
        }

        @SuppressWarnings("unused")
        private void onClearBtn() {
            XmlTreeItem.this.getXmlTree().getTreeController().getEditorController().changeItemValue(XmlTreeItem.this, null);
            XmlTreeItem.this.getXmlTree().setItemWidget(XmlTreeItem.this, 1, null);
        }
    }
    private final IClientEnvironment environment;

    public XmlTreeItem(final IClientEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void refresh() {
        final XmlModelItem modelItem = getXmlItem();
        if (treeWidget() != null && modelItem.isXmlNodeCanHaveValue()) {
            final XmlValueEdit lineEdit = (XmlValueEdit) data(1, Qt.ItemDataRole.UserRole);
            if (lineEdit != null) {
                final String nodeValue = modelItem.getXmlNode().getValue() == null ? modelItem.getXmlNode().getValue() : modelItem.getXmlNode().getValue().trim();
                if (!Objects.equals(nodeValue, lineEdit.getValue())) {
                    lineEdit.setValue(nodeValue);
                }
                lineEdit.setReadOnly(isReadOnly());
            }
        } else if (modelItem.getXmlNode() != null
                && modelItem.getXmlNode().getValue() != null
                && !modelItem.getXmlNode().getValue().trim().isEmpty()) {
            setUnexpectedText(modelItem.getXmlNode().getValue().trim());
        } else if (getXmlTree().itemWidget(this, 1) != null) {
            getXmlTree().setItemWidget(this, 1, null);
        }
        setText(0, modelItem.getXmlNode().getDisplayString());
        final ExplorerTextOptions options =
                (ExplorerTextOptions) modelItem.getNameTextOptions(getEnvironment(), isReadOnly());
        options.applyTo(this, 0);
    }

    public void setUnexpectedText(final String text) {
        final UnexpectedTextWidget itemWidget = new UnexpectedTextWidget(text, isReadOnly());
        treeWidget().setItemWidget(this, 1, itemWidget);
    }

    @Override
    public boolean isReadOnly() {
        return getXmlTree().getTreeController().isItemReadOnly(this);
    }

    @Override
    public XmlModelItem getXmlItem() {
        return (XmlModelItem) data(0, com.trolltech.qt.core.Qt.ItemDataRole.UserRole);
    }

    @Override
    public void expand() {
        treeWidget().expandItem(this);
    }

    @Override
    public void delete() {
        final QTreeWidgetItem parentItem = parent();
        final int index = parentItem == null ? treeWidget().indexOfTopLevelItem(this) : parentItem.indexOfChild(this);
        if (parentItem != null) {
            parentItem.takeChild(index);
            if (parentItem.childCount() == 0) {
                parentItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.DontShowIndicator);
            }
        } else {
            treeWidget().takeTopLevelItem(index);
        }
    }

    @Override
    public XmlTreeItem getNextSiblingItem() {
        final QTreeWidgetItem parentItem = this.parent();
        if (parentItem == null) {
            final int index = treeWidget().indexOfTopLevelItem(this);
            if (index >= 0 && index < treeWidget().topLevelItemCount() - 1) {
                return (XmlTreeItem) treeWidget().topLevelItem(index + 1);
            }
        } else {
            final int index = parentItem.indexOfChild(this);
            if (index < parentItem.childCount() - 1) {
                return (XmlTreeItem) parentItem.child(index + 1);
            }
        }
        return null;
    }

    @Override
    public XmlTreeItem getPreviousSiblingItem() {
        final QTreeWidgetItem parentItem = this.parent();
        if (parentItem == null) {
            final int index = treeWidget().indexOfTopLevelItem(this);
            if (index > 0) {
                return (XmlTreeItem) treeWidget().topLevelItem(index - 1);
            }
        } else {
            final int index = parentItem.indexOfChild(this);
            if (index > 0) {
                return (XmlTreeItem) parentItem.child(index - 1);
            }
            getXmlItem().isXmlNodeCanHaveValue();
        }
        return null;
    }

    @Override
    public void reinitChildren() {
        final boolean wasExpanded = isExpanded();
        treeWidget().collapseItem(this);
        takeChildren();
        if (getXmlItem().getXmlNode().getChildItems().isEmpty()) {
            setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.DontShowIndicator);
        } else {
            setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
        }
        if (wasExpanded) {
            treeWidget().expandItem(this);
        }
    }

    @Override
    public int[] getIndexPath() {
        final Stack<Integer> indexStack = new Stack<>();
        for (QTreeWidgetItem item = this; item.parent() != null; item = item.parent()) {
            indexStack.push(item.parent().indexOfChild(item));
        }
        final int[] path = new int[indexStack.size()];
        for (int i = 0; !indexStack.isEmpty(); i++) {
            path[i] = indexStack.pop().intValue();
        }
        return path;
    }

    @Override
    public int getIndexInParent() {
        return parent() != null ? parent().indexOfChild(this) : 0;
    }

    @Override
    public XmlTreeItem getParentItem() {
        return (XmlTreeItem) parent();
    }

    @Override
    public XmlTreeItem getChildItem(final int index) {
        if (index < 0 || index >= childCount()) {
            return null;
        }
        return (XmlTreeItem) child(index);
    }

    @Override
    public int getChildCount() {
        return childCount();
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    private XmlTree getXmlTree() {
        return (XmlTree) this.treeWidget();
    }

    @Override
    public void setModelValue(final int column, final Object value, final boolean isReadOnly) {
        setData(0, com.trolltech.qt.core.Qt.ItemDataRole.UserRole, value);
        final XmlTree tree = getXmlTree();
        final XmlModelItem xmlItem = (XmlModelItem) value;
        if (tree != null) {
            tree.updateTreeItem(this, xmlItem);
        }
        final ExplorerTextOptions options =
                (ExplorerTextOptions) xmlItem.getNameTextOptions(getEnvironment(), isReadOnly);
        options.applyTo(this, 0);
    }

    @Override
    public void setTextValue(final int column, final Object value) {
        setText(column, String.valueOf(value));
    }
}