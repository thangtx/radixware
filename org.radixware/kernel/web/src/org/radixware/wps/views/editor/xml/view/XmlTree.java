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
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;
import org.radixware.wps.rwt.tree.Tree;

final class XmlTree extends Tree implements IXmlTree {
    
    private static class XmlValueChangeListener implements IXmlValueEditor.ValueListener {

        private final XmlTreeItem xmlTreeItem;

        public XmlValueChangeListener(final XmlTreeItem item) {
            xmlTreeItem = item;
        }

        @Override
        public void valueChanged(String newValue) {
            final XmlTree tree = xmlTreeItem.getTree();
            tree.getTreeController().getEditorController().changeItemValue(xmlTreeItem, newValue);
        }
    }

    private static class UnexpectedTextClearController implements InputBox.ClearController<String> {

        private final XmlTreeItem xmlTreeItem;

        public UnexpectedTextClearController(final XmlTreeItem item) {
            xmlTreeItem = item;
        }

        @Override
        public String clear() {
            final XmlTree tree = xmlTreeItem.getTree();
            tree.getTreeController().getEditorController().changeItemValue(xmlTreeItem, null);
            return null;
        }
    }

    @Override
    public XmlTreeItem findByIndexPath(int[] indexPath, boolean nearest) {
        XmlTreeItem current = (XmlTreeItem) getRootNode().getChildAt(0);
        if (indexPath.length <= 1) {
            return current;
        }
        XmlTreeItem child = null;
        for (int i = 1; i < indexPath.length; i++) {
            child = (XmlTreeItem) current.getChildAt(indexPath[i]);
            if (child == null) {
                return nearest ? current : null;
            } else {
                current = child;
            }
        }
        return child;
    }

    @Override
    public void setSelectedItem(IXmlTreeItem currentItem) {
        setSelectedNode((Node) currentItem);
    }

    private static class XmlTreeValueCellRenderer implements INodeCellRenderer {

        private final XmlValueEditorFactory editorFactory;
        private final IClientEnvironment environment;
        private final XmlTreeController treeController;
        private IXmlValueEditor.ValueListener valueChangeListener;
        private final AbstractContainer dummy = new AbstractContainer(new Div());
        private IXmlValueEditor<UIObject> valEditor;
        private InputBox<String> unexptectedTextEditor;
        private UIObject editorWidget;

        public XmlTreeValueCellRenderer(final XmlTreeController treeController, final XmlValueEditorFactory factory) {
            this.editorFactory = factory;
            this.treeController = treeController;
            this.environment = treeController.getEnvironment();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void update(final Node node, final int c, final Object value) {
            if (value instanceof XmlModelItem) {
                final boolean isReadOnly = treeController.getEditorController().isReadOnly();
                final XmlModelItem xmlItem = (XmlModelItem) value;
                final UIObject parent;
                if (valEditor != null && valEditor.getModelItem() != xmlItem) {
                    valEditor.clearWidgetListeners();
                    valEditor.clearValueListeners();
                    final UIObject asWidget = valEditor.asWidget();
                    dummy.remove(asWidget);
                    valEditor = null;
                    editorWidget = null;
                } else if (valEditor != null) {
                    valEditor.setReadOnly(isReadOnly);
                } else if (unexptectedTextEditor != null) {
                    unexptectedTextEditor.setReadOnly(isReadOnly);
                }
                if (xmlItem.isXmlNodeCanHaveValue()) {
                    parent = dummy.getParent();
                    if (unexptectedTextEditor != null) {
                        dummy.remove(unexptectedTextEditor);
                        unexptectedTextEditor = null;
                    }
                    if (valEditor == null) {
                        valEditor = editorFactory.createEditor(environment, treeController.getEditorController().getValueEditingOptionsProvider(), xmlItem);
                        if (isReadOnly) {
                            valEditor.setReadOnly(true);
                        }
                        editorWidget = valEditor.asWidget();
                        dummy.add(editorWidget);
                        valEditor.addWidgetListener(new IXmlValueEditor.WidgetListener<UIObject>() {
                            @Override
                            public void widgetChanged(final UIObject newWidget) {
                                if (editorWidget != null) {
                                    dummy.remove(editorWidget);
                                }
                                editorWidget = newWidget;
                                dummy.add(editorWidget);
                            }
                        });
                        String valueStr = xmlItem.getXmlNode().getValue();
                        if (valueStr != null) {
                            valueStr = valueStr.trim();
                        }
                        valEditor.setValue(valueStr);
                    }
                } else if (xmlItem.getXmlNode() != null
                        && xmlItem.getXmlNode().getValue() != null
                        && !xmlItem.getXmlNode().getValue().trim().isEmpty()) {
                    parent = dummy.getParent();
                    if (valEditor != null) {
                        valEditor.clearWidgetListeners();
                        valEditor.clearValueListeners();
                        final UIObject asWidget = valEditor.asWidget();
                        dummy.remove(asWidget);
                        valEditor = null;
                        editorWidget = null;
                    }
                    if (unexptectedTextEditor == null) {
                        unexptectedTextEditor = new InputBox<>();
                        unexptectedTextEditor.setValue(xmlItem.getXmlNode().getValue().trim());
                        if (isReadOnly) {
                            unexptectedTextEditor.setReadOnly(true);
                        }
                        dummy.add(unexptectedTextEditor);
                    }
                } else {
                    if (unexptectedTextEditor != null) {
                        dummy.remove(unexptectedTextEditor);
                        unexptectedTextEditor = null;
                    }
                    parent = null;
                }
                if (parent != null) {
                    if (valEditor != null) {
                        parent.getHtml().addClass("editor-cell");
                        valEditor.removeValueListener(valueChangeListener);
                        String valueStr = xmlItem.getXmlNode().getValue();
                        if (valueStr != null) {
                            valueStr = valueStr.trim();
                        }
                        valEditor.setValue(valueStr);
                        if (parent.getParent() instanceof XmlTreeItem) {
                            final XmlTreeItem treeItem = (XmlTreeItem) parent.getParent();
                            valueChangeListener = new XmlValueChangeListener(treeItem);
                            valEditor.addValueListener(valueChangeListener);
                        }
                    } else if (unexptectedTextEditor != null) {
                        parent.getHtml().addClass("editor-cell");
                        String valueStr = xmlItem.getXmlNode().getValue();
                        if (valueStr != null) {
                            valueStr = valueStr.trim();
                        }
                        unexptectedTextEditor.setValue(valueStr);
                        if (isReadOnly) {
                            unexptectedTextEditor.setValidationMessage(null);
                        } else {
                            final String message
                                    = environment.getMessageProvider().translate("XmlEditor", "Current item can't have value!");
                            unexptectedTextEditor.setValidationMessage(message);
                        }
                        if (parent.getParent() instanceof XmlTreeItem) {
                            final XmlTreeItem treeItem = (XmlTreeItem) parent.getParent();
                            if (isReadOnly) {
                                unexptectedTextEditor.setClearController(null);
                            } else {
                                unexptectedTextEditor.setClearController(new UnexpectedTextClearController(treeItem));
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void selectionChanged(Node node, int c, Object value, IGrid.ICell cell, boolean isSelected) {
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
        }

        @Override
        public UIObject getUI() {
            return dummy;
        }
    }
    private final List<IChangeCurrentItemListener> listeners = new LinkedList<>();
    private final XmlValueEditorFactory valueEditorFactory;
    private final Node.DefaultNode rootNode;
    private XmlTreeController treeController;
 
    public XmlTree(final XmlValueEditorFactory valueEditorFactory) {
        this.valueEditorFactory = valueEditorFactory;
        addSelectionListener(new NodeListener() {
            @Override
            public void selectionChanged(Node oldSelection, Node newSelection) {
                for (IChangeCurrentItemListener listener : listeners) {
                    listener.currentItemChanged((XmlTreeItem) newSelection);
                }
            }
        });
        setHeight(500);
        rootNode = new Node.DefaultNode();
        setRootVisible(false);
        setRootNode(rootNode);
    }

    @Override
    public XmlTreeItem getCurrentItem() {
        return (XmlTreeItem) getSelectedNode();
    }

    @Override
    public XmlTreeItem createRootItem(final XmlDocumentItem rootItem,
            final XmlSchemaItem schemaItem,
            final XmlEditorController controller,
            final boolean isConformity) {
        if (!rootNode.getChildNodes().isEmpty()) {
            rootNode.getChildNodes().reset();
        }
        treeController = new XmlTreeController(controller, this);
        updateColumnRenderer();
        final XmlTreeItem root = new XmlTreeItem();
        XmlModelItem rootModel = new XmlModelItem(rootItem, schemaItem, controller.getXmlSchemaProvider());
        rootModel.setConformity(isConformity);
        if (!rootModel.getVisibleChildItems().isEmpty()) {
            ChildXmlItems children = new ChildXmlItems(rootModel, treeController);
            root.setChildNodes(children);
        }
        rootNode.add(root);
        treeController.setupXmlTreeItem(root, rootModel);
        setSelectedItem(root);
        return root;
    }

    private void updateColumnRenderer() {
        getColumn(1).setCellRendererProvider(new ICellRendererProvider() {
            @Override
            public INodeCellRenderer newCellRenderer(final Node n, final int c) {
                return new XmlTreeValueCellRenderer(treeController, XmlTree.this.valueEditorFactory);
            }
        });
    }

    @Override
    public XmlTreeItem addChildItem(final IXmlTreeItem parentItem, final XmlModelItem childXmlItem, final int index) {
        final XmlTreeItem currentNode = (XmlTreeItem) parentItem;
        final ChildXmlItems children;
        if (currentNode.isLeaf()) {
            children = new ChildXmlItems(currentNode.getXmlItem(), treeController);
            currentNode.setChildNodes(children);
            currentNode.expand();
        } else {
            children = (ChildXmlItems) currentNode.getChildNodes();
            if (!children.getCreatedNodes().isEmpty()) {
                return children.addNewXmlItem(childXmlItem, index);
            }
        }
        return currentNode.getChildItem(index);
    }

    @Override
    public void setColumns(final String[] titles) {
        for (int i = 0; i < titles.length; i++) {
            if (i == 0) {
                getColumn(i).setTitle(titles[i]);
                getColumn(i).setWidth(400);
            } else {
                addColumn(titles[i]);
            }
        }
        setHeaderAlignment(Alignment.CENTER);
    }

    @Override
    public void addChangeCurrentItemListener(IChangeCurrentItemListener listener) {
        listeners.add(listener);
    }

    void beforeDeleteNode(final XmlTreeItem node) {
        Node parentNode = node.getParentNode();
        if (parentNode instanceof XmlTreeItem) {
            final int index = parentNode.indexOfChild(node);
            if (index == (parentNode.getChildCount() - 1) && index != 0) {
                setSelectedNode(parentNode.getChildAt(index - 1));
            } else if (index == (parentNode.getChildCount() - 1) && index == 0) {
                setSelectedNode(parentNode);
            } else {
                setSelectedNode(parentNode.getChildAt(index + 1));
            }
        } else {
            setSelectedItem(null);
        }
    }

    @Override
    public XmlTreeController getTreeController() {
        return treeController;
    }

    @Override
    public XmlTreeItem getRootItem() {
        if (rootNode.getChildCount() > 0) {
            return (XmlTreeItem) rootNode.getChildAt(0);
        } else {
            return null;
        }
    }

    @Override
    public void setBackgroundColor(final Color color) {
    }

    @Override
    public void setUpdatesEnabled(boolean enabled) {
        //empty implementation
    }        
}
