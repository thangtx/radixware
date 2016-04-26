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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.DefaultRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.XPathUtils;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.XPathTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.sqml.editors.XPathTagEditorUtils.PathNode;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;


public class XPathTagEditor extends TagEditor<XPathTag> implements Lookup.Provider, ExplorerManager.Provider {

    private final int INDEX_COLUMN_WIDTH = 70;
    private final int CONDITION_COLUMN_WIDTH = 150;
    private XPathTag tag;
    private Sqml ownerSqml;
    private AdsXmlSchemeDef currentSchema;
    private ExplorerManager manager = new ExplorerManager();
    private Lookup lookup;
    private Outline outline;
    private OutlineView view;
    private DefinitionLinkEditPanel schemeChooser = new DefinitionLinkEditPanel();
    private javax.swing.JTextField pathField = new javax.swing.JTextField();
    private ChangeListener schemeChooserListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {

            pathField.setText("");
            currentPath.clear();
            currentTargetNamespace = "";

            if (schemeChooser.getDefinitionId() != null) {
                final AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) schemeChooser.getDefinition();
                final XmlObject asXml = scheme.getXmlDocument();

                currentTargetNamespace = XPathUtils.getSchemaNodeTargetNamespace(asXml);
                currentSchema = scheme;

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        XPathTagEditorUtils.WaitNode waitNode = new XPathTagEditorUtils.WaitNode();
                        manager.setRootContext(waitNode);
                        OutlineModel model = DefaultOutlineModel.createOutlineModel(new XPathTagEditorUtils.XPathTreeModel(waitNode), new XPathTagEditorUtils.XPathRowModel(), false, "XPath");
                        outline.setModel(model);
                        //outline.tableChanged(null);
                        
                        //outline.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                LinkedList<Integer> used = new LinkedList<Integer>();
                                final XPathTagEditorUtils.RootNode rootNode = new XPathTagEditorUtils.RootNode(new XPathTagEditorUtils.RootNodeChildren(XPathTagEditor.this, asXml, scheme, outline, used), asXml);
                                Mutex.EVENT.readAccess(new Runnable() {
                                    @Override
                                    public void run() {

                                        manager.setRootContext(rootNode);
                                        manager.setExploredContext(rootNode);

                                        outline.setModel(DefaultOutlineModel.createOutlineModel(new XPathTagEditorUtils.XPathTreeModel(rootNode), new XPathTagEditorUtils.XPathRowModel(), false, "XPath"));
                                        outline.tableChanged(null);
                                        outline.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
                                        outline.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer);
                                        outline.getColumnModel().getColumn(1).setCellEditor(cellEditor);
                                        outline.getColumnModel().getColumn(2).setCellEditor(condititonEditor);
                                        outline.getColumnModel().getColumn(1).setCellRenderer(new XPathTagEditorUtils.PathNodeRenderer());
                                        outline.getColumnModel().getColumn(2).setCellRenderer(new XPathTagEditorUtils.ConditionRenderer());
                                        outline.getColumnModel().getColumn(1).setMaxWidth(INDEX_COLUMN_WIDTH);
                                        outline.getColumnModel().getColumn(2).setPreferredWidth(CONDITION_COLUMN_WIDTH);
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
                XPathTagEditorUtils.EmptyNode emptyNode = new XPathTagEditorUtils.EmptyNode();
                manager.setRootContext(emptyNode);
                outline.setModel(DefaultOutlineModel.createOutlineModel(new XPathTagEditorUtils.XPathTreeModel(emptyNode), new XPathTagEditorUtils.XPathRowModel(), false, "XPath"));
                outline.tableChanged(null);
                outline.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
                outline.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer);
                outline.getColumnModel().getColumn(1).setCellEditor(cellEditor);
                outline.getColumnModel().getColumn(2).setCellEditor(condititonEditor);
                outline.getColumnModel().getColumn(1).setCellRenderer(new XPathTagEditorUtils.PathNodeRenderer());
                outline.getColumnModel().getColumn(2).setCellRenderer(new XPathTagEditorUtils.ConditionRenderer());
                outline.getColumnModel().getColumn(1).setMaxWidth(INDEX_COLUMN_WIDTH);
                outline.getColumnModel().getColumn(2).setPreferredWidth(CONDITION_COLUMN_WIDTH);
            }
        }
    };

    /**
     * Creates new form XPathTagEditor
     */
    public XPathTagEditor() {
        initComponents();
        manager.setRootContext(new XPathTagEditorUtils.EmptyNode());

        view = new OutlineView();
        outline = view.getOutline();

        outline.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2
                        && isCorrect()) {
                    outline.getRootPane().getDefaultButton().doClick();
                } else {
                    super.mouseReleased(e);
                }
            }
        });

        outline.setRootVisible(true);
        outline.setRowSelectionAllowed(true);
        outline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outline.setColumnHidingAllowed(false);

        javax.swing.JLabel label = new javax.swing.JLabel(NbBundle.getMessage(XPathTagEditor.class, "XPathTagEditor-SchemeTip"));

        javax.swing.JPanel content = new javax.swing.JPanel();
        GridBagLayout gbl = new GridBagLayout();
        content.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        gbl.setConstraints(label, c);
        content.add(label);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 0, 10, 10);
        gbl.setConstraints(schemeChooser, c);
        content.add(schemeChooser);
        schemeChooser.addChangeListener(schemeChooserListener);
        schemeChooser.setClearable(false);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 10, 10, 10);

        gbl.setConstraints(view, c);
        content.add(view);

        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.0;
        gbl.setConstraints(pathField, c);
        content.add(pathField);

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        pathField.setEditable(false);

        PropertyChangeListener propertyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("selectedNodes")
                        && !opening) {
                    XPathTagEditor.this.onSelectionChange();
                }
            }
        };
        manager.addPropertyChangeListener(propertyListener);

        cellEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSelectionChange();
            }
        });

        ActionMap map = new ActionMap();
        map.put("Select Path", SystemAction.get(CloseOnOkAction.class));
        this.lookup = ExplorerUtils.createLookup(manager, new ActionMap());
    }

    //getRootPane().getDefaultButton().doClick();
    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
    private LinkedList<Node> currentPath = new LinkedList<Node>();
    private String currentTargetNamespace = "";

    private void onSelectionChange() {
        updateOkState();
        currentPath.clear();
        Node[] selection = manager.getSelectedNodes();
        if (selection != null && selection.length > 0) {
            if (selection[0] instanceof XPathTagEditorUtils.PathNode) {
                XPathTagEditorUtils.PathNode node = (XPathTagEditorUtils.PathNode) selection[0];
                currentPath.add(node);
                Node parent = node.getParentNode();
                while (parent instanceof XPathTagEditorUtils.PathNode) {
                    currentPath.add(parent);
                    parent = parent.getParentNode();
                }

                StringBuilder sb = new StringBuilder();
                for (int i = currentPath.size() - 1; i >= 0; i--) {
                    PathNode iNode = (PathNode) currentPath.get(i);
                    XmlObject obj = iNode.item.node;
                    sb.append("/");
                    if (XPathUtils.isAttribute(obj)) {
                        sb.append("@");
                    }
                    sb.append(XPathUtils.getQName(obj, iNode.item.context.getTargetNamespace()).getLocalPart());
                    if (iNode.index != null && iNode.index > 0) {
                        sb.append("[");
                        sb.append(iNode.index);
                        sb.append("]");
                    }
                }
                pathField.setText(sb.toString());
            } else {
                pathField.setText("");
            }
        } else {
            pathField.setText("");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected boolean tagDefined() {
        return isCorrect();
    }

    @Override
    protected String getTitle() {
        return "XPath Tag Editor";
    }

    @Override
    protected void applyChanges() {
        if (currentPath.size() > 0 && currentSchema != null) {
            RadixObjects<XPathTag.Item> items = tag.getItems();
            items.clear();
            Map<String, Id> ns2schemaid = new HashMap<String, Id>();

            for (int i = currentPath.size() - 1; i >= 0; i--) {
                PathNode node = (PathNode) currentPath.get(i);
                QName qn = XPathUtils.getQName(node.item.node, node.getContext().getTargetNamespace());

                XPathTag.Item newitem = XPathTag.Item.Factory.newInstance();
                newitem.setName(qn.getLocalPart());
                newitem.setIsAttribute(XPathUtils.isAttribute(node.item.node));
                if (node.isEditable() && node.index > 0) {
                    newitem.setIndex(Long.valueOf(node.index.toString()));
                }

                if (node.condition != null) {
                    newitem.setCondition(node.condition);
                }

                if (qn.getNamespaceURI().equals(currentTargetNamespace)) {
                    newitem.setSchemaId(currentSchema.getId());
                } else {
                    Id imported = ns2schemaid.get(qn.getNamespaceURI());
                    if (imported == null) {
                        AdsXmlSchemeDef schemeDef = (AdsXmlSchemeDef) AdsSearcher.Factory.newXmlDefinitionSearcher(currentSchema).findByNs(qn.getNamespaceURI()).get();
                        if (schemeDef != null) {
                            ns2schemaid.put(qn.getNamespaceURI(), schemeDef.getId());


                            ownerSqml.getModule().getDependences().add(schemeDef.getModule());

                        }
                        newitem.setSchemaId(schemeDef.getId());
                    } else {
                        newitem.setSchemaId(imported);
                    }
                }

                items.add(newitem);
            }
            List<Module> modules = ownerSqml.getModule().getDependences().findModuleById(currentSchema.getModule().getId());
            if (modules.isEmpty()) {
                ownerSqml.getModule().getDependences().add(currentSchema.getModule());
            }
        }
    }

    @Override
    public boolean isCorrect() {
        Node[] selection = manager.getSelectedNodes();
        return selection != null
                && selection.length > 0
                && !(selection[0] instanceof XPathTagEditorUtils.RootNode)
                && !(selection[0] instanceof XPathTagEditorUtils.EmptyNode)
                && currentSchema != null;
    }
    private VisitorProvider provider = AdsVisitorProviders.newSchemeVisitorProvider();
    XPathTagEditorUtils.HeaderRenderer headerRenderer = new XPathTagEditorUtils.HeaderRenderer();
    XPathTagEditorUtils.PathNodeCellEditor cellEditor = new XPathTagEditorUtils.PathNodeCellEditor();
    XPathTagEditorUtils.ConditionCellEditor condititonEditor = new XPathTagEditorUtils.ConditionCellEditor();
    private boolean opening = false;

    @Override
    protected void afterOpen() {
        this.opening = true;

        currentPath.clear();
        tag = getObject();

        outline.setRowSorter(null);
        outline.setAutoCreateRowSorter(false);
        

        RadixObjects<XPathTag.Item> items = tag.getItems();

        ownerSqml = getObject().getOwnerSqml();
        if (ownerSqml == null) {
            ownerSqml = getOpenInfo().getLookup().lookup(SqmlEditorPanel.class).getSource();
        }
        Collection<Definition> collection = org.radixware.kernel.designer.common.general.utils.DefinitionsUtils.collectAllAround(ownerSqml,
                provider);
        if (items.size() > 0) {
            Id schemeId = items.get(0).getSchemaId();
            AdsXmlSchemeDef def = (AdsXmlSchemeDef) AdsSearcher.Factory.newAdsDefinitionSearcher(ownerSqml.getDefinition()).findById(schemeId).get();
            schemeChooser.open(ChooseDefinitionCfg.Factory.newInstance(collection), def, schemeId);
            currentSchema = def;

            XPathTagEditorUtils.RootNode rootNode = new XPathTagEditorUtils.RootNode(new XPathTagEditorUtils.RootNodeChildren(this, def.getXmlDocument(), def, outline, new LinkedList<Integer>()), def.getXmlDocument());
            manager.setRootContext(rootNode);
            outline.setModel(DefaultOutlineModel.createOutlineModel(new XPathTagEditorUtils.XPathTreeModel(rootNode), new XPathTagEditorUtils.XPathRowModel(), false, "XPath"));

            outline.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
            outline.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer);
            outline.getColumnModel().getColumn(1).setCellEditor(cellEditor);
            outline.getColumnModel().getColumn(2).setCellEditor(condititonEditor);
            outline.getColumnModel().getColumn(1).setCellRenderer(new XPathTagEditorUtils.PathNodeRenderer());
            outline.getColumnModel().getColumn(2).setCellRenderer(new XPathTagEditorUtils.ConditionRenderer());
            outline.getColumnModel().getColumn(1).setMaxWidth(INDEX_COLUMN_WIDTH);
            outline.getColumnModel().getColumn(2).setPreferredWidth(CONDITION_COLUMN_WIDTH);

            try {
                Node[] selection = findSelectedNode(rootNode, items, items.size() - 1, 0, new Node[items.size()]);
                if (selection != null) {
                    int size = selection.length - 1;

                    manager.setSelectedNodes(new Node[]{selection[size]});

                    TreePath tp = new TreePath(rootNode);
                    for (int i = 0, s = selection.length - 2; i <= s; i++) {
                        tp = tp.pathByAddingChild(selection[i]);
                    }
                    outline.expandPath(tp);
                } else {
                    manager.setSelectedNodes(new Node[]{rootNode});
                }
            } catch (PropertyVetoException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }

        } else {
            schemeChooser.open(ChooseDefinitionCfg.Factory.newInstance(collection), null, null);
            XPathTagEditorUtils.EmptyNode node = new XPathTagEditorUtils.EmptyNode();
            manager.setRootContext(node);
            outline.setModel(DefaultOutlineModel.createOutlineModel(new XPathTagEditorUtils.XPathTreeModel(node), new XPathTagEditorUtils.XPathRowModel(), false, "XPath"));
            outline.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
            outline.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer);
            outline.getColumnModel().getColumn(1).setCellEditor(cellEditor);
            outline.getColumnModel().getColumn(2).setCellEditor(condititonEditor);
            outline.getColumnModel().getColumn(1).setCellRenderer(new XPathTagEditorUtils.PathNodeRenderer());
            outline.getColumnModel().getColumn(2).setCellRenderer(new XPathTagEditorUtils.ConditionRenderer());
            outline.getColumnModel().getColumn(1).setMaxWidth(INDEX_COLUMN_WIDTH);
            outline.getColumnModel().getColumn(2).setPreferredWidth(CONDITION_COLUMN_WIDTH);
        }


        this.opening = false;
        updateOkState();
    }

    private Node[] findSelectedNode(Node root, RadixObjects<XPathTag.Item> items, int selectionLevel, int currentLevel, Node[] result) {
        Children children = root.getChildren();
        if (children != null && children.getNodes().length > 0) {
            Node[] nodes = children.getNodes();
            for (Node n : nodes) {
                if (n.getDisplayName().equals(items.get(currentLevel).getName())) {
                    if (n instanceof PathNode) {
                        if (items.get(currentLevel).getIndex() != null) {
                            ((PathNode) n).index = items.get(currentLevel).getIndex().intValue();
                        }
                        ((PathNode) n).condition = items.get(currentLevel).getCondition();
                    }
                    if (currentLevel < selectionLevel) {
                        result[currentLevel] = n;
                        return findSelectedNode(n, items, selectionLevel, currentLevel + 1, result);
                    } else if (currentLevel == selectionLevel) {
                        result[currentLevel] = n;
                        return result;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void setReadOnly(boolean readOnly) {
        outline.setEnabled(!readOnly);
        schemeChooser.setEnabled(!readOnly);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    static class CloseOnOkAction extends CookieAction {

        static class Cookie implements Node.Cookie {

            private XPathTagEditor editor;

            Cookie(XPathTagEditor editor) {
                this.editor = editor;
            }

            public void openOwnerClassCatalog() {
                if (editor != null
                        && editor.isCorrect()) {
                    editor.getRootPane().getDefaultButton().doClick();
                }
            }
        }

        @Override
        protected void performAction(Node[] activatedNodes) {
            for (int i = 0; i < activatedNodes.length; i++) {
                Cookie cc = activatedNodes[i].getCookie(Cookie.class);

                if (cc != null) {
                    cc.openOwnerClassCatalog();
                }
            }
        }

        @Override
        protected int mode() {
            return MODE_EXACTLY_ONE;
        }

        @Override
        protected Class<?>[] cookieClasses() {
            return new Class[]{CloseOnOkAction.Cookie.class};
        }

        @Override
        public String getName() {
            return "Select Path";
        }

        @Override
        public HelpCtx getHelpCtx() {
            return null;
        }

        @Override
        protected boolean asynchronous() {
            return false;
        }
    }
}
