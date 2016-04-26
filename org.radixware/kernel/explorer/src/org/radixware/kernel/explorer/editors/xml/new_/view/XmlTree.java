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

import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlTreeController;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


public final class XmlTree extends QTreeWidget implements IXmlTree {
    
    private final List<IChangeCurrentItemListener> listeners = new LinkedList<>();
    private final XmlValueEditorFactory editorFactory;
    private XmlTreeController treeController;
    private final IClientEnvironment environment;

    @SuppressWarnings("LeakingThisInConstructor")
    public XmlTree(final QWidget parent, final IClientEnvironment environment, XmlValueEditorFactory factory) {
        super(parent);        
        this.editorFactory = factory;
        currentItemChanged.connect(this, "onCurrentItemChanged()");
        itemExpanded.connect(this, "initChildItems(QTreeWidgetItem)");
        this.environment = environment;
    }

    @Override
    public XmlTreeItem getCurrentItem() {
        return (XmlTreeItem) currentItem();
    }

    @Override
    public XmlTreeItem createRootItem(final XmlDocumentItem rootItem,
            final XmlSchemaItem schemaItem,
            final XmlEditorController controller,
            final boolean isConformity) {
        treeController = new XmlTreeController(controller, this);
        clear();       
        final XmlModelItem rootModelItem =
                new XmlModelItem(rootItem, schemaItem, controller.getXmlSchemaProvider());
        rootModelItem.setConformity(isConformity);
        XmlTreeItem rootTreeItem = createTreeItem(rootModelItem, null, -1);
        setSelectedItem(rootTreeItem);
        return rootTreeItem;
    }

    @Override
    public void addChangeCurrentItemListener(final IChangeCurrentItemListener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unused")
    private void onCurrentItemChanged() {
        for (IChangeCurrentItemListener listener : listeners) {
            listener.currentItemChanged(getCurrentItem());
        }
    }

    @Override
    public XmlTreeItem addChildItem(IXmlTreeItem parentItem, XmlModelItem childXmlItem, int index) {
        final XmlTreeItem parentTreeItem = (XmlTreeItem) parentItem;
        final XmlTreeItem childItem;
        if (parentTreeItem.childCount() == 0) {
            initChildItems((XmlTreeItem) parentItem);
            childItem = parentTreeItem.getChildItem(index);
        } else {
            childItem = createTreeItem(childXmlItem, parentTreeItem, index);
        }
        if (parentTreeItem.childCount() > 0) {
            parentTreeItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
        }
        return childItem;
    }

    private XmlTreeItem createTreeItem(XmlModelItem xmlItem, XmlTreeItem parentTreeItem, int index) {
        final XmlTreeItem newTreeItem = new XmlTreeItem(environment);
        newTreeItem.setData(0, com.trolltech.qt.core.Qt.ItemDataRole.UserRole, xmlItem);
        if (parentTreeItem == null) {
            addTopLevelItem(newTreeItem);
        } else {
            if (index < 0) {
                parentTreeItem.addChild(newTreeItem);
            } else {
                parentTreeItem.insertChild(index, newTreeItem);
            }
        }
        updateTreeItem(newTreeItem, xmlItem);
        treeController.setupXmlTreeItem(newTreeItem, xmlItem);
        return newTreeItem;
    }

    @SuppressWarnings("unchecked")
    public void updateTreeItem(final XmlTreeItem treeItem, final XmlModelItem xmlItem) {
        final Object data = treeItem.data(1, com.trolltech.qt.core.Qt.ItemDataRole.UserRole);
        if (data instanceof IXmlValueEditor) {
            IXmlValueEditor oldEditor = (IXmlValueEditor) data;
            oldEditor.clearValueListeners();
            oldEditor.clearWidgetListeners();
            final QWidget asWidget = (QWidget) oldEditor.asWidget();
            asWidget.close();
        }
        if (xmlItem.isXmlNodeCanHaveValue()) {
            final IXmlValueEditor<QWidget> valEditor =
                    editorFactory.createEditor(environment, treeController.getEditorController().getValueEditingOptionsProvider(), xmlItem, treeController.getEditorController().isReadOnly());
            setItemWidget(treeItem, 1, valEditor.asWidget());
            treeItem.setData(1, com.trolltech.qt.core.Qt.ItemDataRole.UserRole, valEditor);
            valEditor.asWidget().setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            String value = xmlItem.getXmlNode().getValue();
            if (value != null) {
                value = value.trim();
            }
            IXmlValueEditor.ValueListener valueChangeListener = new IXmlValueEditor.ValueListener() {
                @Override
                public void valueChanged(String newValue) {
                    treeController.getEditorController().changeItemValue(treeItem, newValue);
                }
            };
            valEditor.addValueListener(valueChangeListener);
            IXmlValueEditor.WidgetListener<QWidget> widgetChangeListener = new IXmlValueEditor.WidgetListener<QWidget>() {
                @Override
                public void widgetChanged(final QWidget newWidget) {
                    setItemWidget(treeItem, 1, newWidget);
                }
            };
            valEditor.addWidgetListener(widgetChangeListener);
            valEditor.setValue(value);
        } else if (xmlItem.getXmlNode() != null 
                   && xmlItem.getXmlNode().getValue() != null
                   && !xmlItem.getXmlNode().getValue().trim().isEmpty()) {
            treeItem.setUnexpectedText(xmlItem.getXmlNode().getValue().trim());
            treeItem.setData(1, com.trolltech.qt.core.Qt.ItemDataRole.UserRole, null);
        } else {
            setItemWidget(treeItem, 1, null);
            treeItem.setData(1, com.trolltech.qt.core.Qt.ItemDataRole.UserRole, null);
        }
        if (!xmlItem.getVisibleChildItems().isEmpty()) {
            treeItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
        }
    }

    private void initChildItems(final QTreeWidgetItem it) {
        final XmlModelItem modelItem =
                (XmlModelItem) it.data(0, com.trolltech.qt.core.Qt.ItemDataRole.UserRole);
        if (it.childCount() == 0) {
            for (XmlModelItem i : modelItem.getVisibleChildItems()) {
                createTreeItem(i, (XmlTreeItem) it, -1);
            }
        }
    }

    @Override
    public void setColumns(final String[] titles) {
        for (String title : titles) {
            setColumnCount(columnCount() + 1);
            headerItem().setText(columnCount() - 1, title);
            if (columnCount() == 1) {
                setColumnWidth(0, 400);
            } else {
                setColumnWidth(1, width() - columnWidth(0));
            }
        }
    }

    @Override
    public XmlTreeItem findByIndexPath(int[] indexPath, boolean nearest) {
        XmlTreeItem current = (XmlTreeItem) this.topLevelItem(0);
        if (indexPath.length == 0) {
            return current;
        }
        for (int i = 0; i < indexPath.length; i++) {
            if (indexPath[i] >= current.childCount()) {
                return nearest ? current : null;
            } else {
                current = (XmlTreeItem) current.child(indexPath[i]);
            }
        }
        return current;
    }

    @Override
    public void setSelectedItem(IXmlTreeItem currentItem) {
        setCurrentItem((QTreeWidgetItem) currentItem);
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public XmlTreeController getTreeController() {
        return treeController; 
    }

    @Override
    public XmlTreeItem getRootItem() {
        if (topLevelItemCount() > 0) {
            return (XmlTreeItem) topLevelItem(0);
        } else {
            return null;
        }
    }

    @Override
    public void setBackgroundColor(final Color color) {
        final ExplorerTextOptions options = ExplorerTextOptions.Factory.getOptions((Color) null, color);
        {//tree settings:
            final QPalette palette = new QPalette(viewport().palette());
            palette.setColor(viewport().backgroundRole(), options.getBackground());
            viewport().setPalette(palette);
        }
    }        
}
