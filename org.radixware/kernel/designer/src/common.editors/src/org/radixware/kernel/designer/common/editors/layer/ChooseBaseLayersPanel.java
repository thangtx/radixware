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

package org.radixware.kernel.designer.common.editors.layer;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import org.netbeans.swing.etable.QuickFilter;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.CheckableNode;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class ChooseBaseLayersPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static class Node extends AbstractNode implements CheckableNode {

        protected static final boolean QNAME = true;
        protected transient final Layer layer;
        protected transient final TreeView tree;

        private Node(final TreeView tree, final Layer layer, final Children children, final InstanceContent content) {
            super(children, new AbstractLookup(content));
            content.add(this);
            this.layer = layer;
            this.tree = tree;
        }

        public Node(final TreeView tree, final Layer layer, final Children children) {
            this(tree, layer, children, new InstanceContent());
        }

        @Override
        public boolean isCheckable() {
            return true;
        }

        @Override
        public boolean isCheckEnabled() {
            return isSelected() || tree.isLayerCanBeSelected(layer);
        }

        @Override
        public Boolean isSelected() {
            return tree.selectedLayers.contains(layer);
        }

        @Override
        public void setSelected(final Boolean selected) {


            tree.selectLayer(layer, selected);

            tree.nodeSelected(this);
        }

        @Override
        public Image getIcon(final int type) {
            return layer.getIcon().getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(final int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return layer.getName() + " (" + layer.getURI() + ")";
        }
    }
    private static final Comparator<AdsDefinition> COMPARATOR = new Comparator<AdsDefinition>() {
        @Override
        public int compare(final AdsDefinition t, final AdsDefinition t1) {
            final String name1 = t.getName();
            final String name2 = t1.getName();
            if (name1 == null) {
                return 1;
            }

            if (name2 == null) {
                return -1;
            }
            return name1.compareTo(name2);
        }
    };

    private static class LayerChildren extends Children.Array {

        private static Collection<org.openide.nodes.Node> createNodes(final TreeView tree, List<Layer> layers) {
            final ArrayList<org.openide.nodes.Node> nodes = new ArrayList<>();

            for (Layer layer : layers) {
                List<Layer> baseLayers = layer.listBaseLayers();
                Children children = Children.LEAF;
                if (!baseLayers.isEmpty()) {
                    children = new LayerChildren(tree, baseLayers);
                }
                nodes.add(new Node(tree, layer, children));
            }
            return nodes;
        }

        public LayerChildren(final TreeView tree, List<Layer> layers) {
            super(createNodes(tree, layers));
        }
    }

    private static class RootNodeChildren extends Children.Array {

        private final transient Branch branch;

        RootNodeChildren(final TreeView tree, final Branch cfg) {
            this.branch = cfg;

            List<Layer> tops = this.branch.getLayers().findTop();

            this.nodes = new LinkedList<>();
            for (Layer layer : tops) {
                List<Layer> baseLayers = layer.listBaseLayers();
                Children children = Children.LEAF;
                if (!baseLayers.isEmpty()) {
                    children = new LayerChildren(tree, baseLayers);
                }
                this.nodes.add(new Node(tree, layer, children));
            }
        }
    }

    private class RootNode extends AbstractNode {

        public RootNode(final TreeView tree, Branch branch) {
            super(new RootNodeChildren(tree, branch));
        }
    }

    private class LayerItem {

        private final Layer layer;
        private final Layer currentLayer;

        public LayerItem(Layer currentLayer, Layer layer) {
            this.layer = layer;
            this.currentLayer = currentLayer;
        }

        @Override
        public String toString() {
            return layer.getName() + " (" + layer.getURI() + ")";
        }
    }

    private class LayerListModel implements TableModel {

        private TreeView tree;
        private final List<LayerItem> items = new LinkedList<>();

        LayerListModel(TreeView tree) {
            this.tree = tree;
            fireChange();
        }

        private void fireChange() {
            this.items.clear();
            for (Layer l : tree.selectedLayers) {
                items.add(new LayerItem(tree.currentLayer, l));
            }
            TableModelEvent e = new TableModelEvent(this, 0, items.size() - 1);
            for (TableModelListener l : listeners) {
                l.tableChanged(e);
            }
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Layer";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex > 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            LayerItem item = items.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.toString();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            
        }
        private final List<TableModelListener> listeners = new LinkedList<>();

        @Override
        public void addTableModelListener(TableModelListener l) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }
    }
    final transient ChangeSupport changeSupport = new ChangeSupport(this);
    private final TreeView beanView;
    private transient final ExplorerManager manager = new ExplorerManager();
    private Layer currentLayer;

    private boolean isSubLayerOf(Layer check, Layer layer) {
        List<Layer> base = check.listBaseLayers();
        for (Layer l : base) {
            if (l == layer || isSubLayerOf(l, layer)) {
                return true;
            }
        }
        return false;
    }

    private List<Layer> listBaseLayers(Layer layer) {
        if (layer == currentLayer) {
            return selection();
        } else {
            return layer.listBaseLayers();
        }

    }

    private class TreeView extends OutlineView {

        private final List<Layer> selectedLayers = new ArrayList<>();
        private final LayerListModel listModel = new LayerListModel(this);
        private final Layer currentLayer;

        public TreeView(Layer currentLayer) {
            super();
            this.currentLayer = currentLayer;
            getOutline().setRootVisible(false);
            getOutline().setShowGrid(false);

            getOutline().setRowSelectionAllowed(true);
            getOutline().setColumnHidingAllowed(false);
            setColumnHeader(null);
            getOutline().setRequestFocusEnabled(false);
            getOutline().setTableHeader(null);
            setRequestFocusEnabled(true);
            getOutline().setSurrendersFocusOnKeystroke(true);
            getOutline().putClientProperty("JTable.autoStartsEdit", true);
            getOutline().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        getOutline().requestFocus();
                    }
                }
            });

            //getOutline().getColumnModel().getColumn(0).setHeaderValue("");
        }

        public void nodeSelected(Node node) {
            revalidate();
            repaint();
        }

        private boolean isLayerCanBeSelected(Layer candidate) {
            if (candidate == currentLayer) {
                return false;
            } else {
                return !selectedLayers.contains(candidate) && !isSubLayerOf(candidate, currentLayer);
            }
        }

        private void selectLayer(Layer layer, boolean select) {
            if (select) {
                selectedLayers.add(layer);
            } else {
                selectedLayers.remove(layer);
            }
            listModel.fireChange();
            updateButtonsState();
        }
    }

    /**
     * Creates new form ChooseClassMembersPanel
     */
    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
    private String matchString = "";
    private final JTextField textField = new JTextField();
    ModalDisplayer displayer;
    JPanel panel = new JPanel(new BorderLayout());

    class TreePanel extends JPanel implements ExplorerManager.Provider {

        @Override
        public ExplorerManager getExplorerManager() {
            return manager;
        }
    }
    private JPanel treePanel = new TreePanel();

    private void updateButtonsState() {
        int selection = lstSelectedLayers.getSelectedRow();
        if (selection < 0 || selection >= beanView.listModel.getRowCount()) {
            btMoveUp.setEnabled(false);
            btMoveDown.setEnabled(false);
        } else {
            btMoveUp.setEnabled(selection > 0);
            btMoveDown.setEnabled(selection < beanView.listModel.getRowCount() - 1);
        }
    }

    private void move(int dir) {
        int selection = lstSelectedLayers.getSelectedRow();
        if (selection < 0 || selection >= beanView.listModel.getRowCount()) {
            return;
        } else {
            int nextIndex = selection + dir;
            if (nextIndex < 0 || nextIndex >= beanView.listModel.getRowCount()) {
                return;
            } else {
                Layer l = beanView.selectedLayers.get(selection);
                beanView.selectedLayers.set(selection, beanView.selectedLayers.get(nextIndex));
                beanView.selectedLayers.set(nextIndex, l);
                beanView.listModel.fireChange();
                lstSelectedLayers.getSelectionModel().setSelectionInterval(nextIndex, nextIndex);
            }
        }
    }

    public ChooseBaseLayersPanel(Layer currentLayer) {
        super();
        initComponents();
        treePanel.setLayout(new BorderLayout(3, 3));
        EmptyBorder border = new EmptyBorder(new Insets(10, 10, 10, 10));
        treePanel.setBorder(border);
        this.beanView = new TreeView(currentLayer);
        treePanel.add(this.beanView, BorderLayout.CENTER);
        jSplitPane1.setTopComponent(treePanel);
        lstSelectedLayers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonsState();
            }
        });

        final JLabel label = new JLabel("Search:");

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                quickSearchContentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                quickSearchContentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                quickSearchContentChanged();
            }
        });
        lstSelectedLayers.setModel(beanView.listModel);
        panel.setVisible(false);
        treePanel.add(panel, BorderLayout.SOUTH);
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    beanView.requestFocus();
                    beanView.dispatchEvent(e);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    beanView.requestFocus();
                    beanView.dispatchEvent(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        beanView.getOutline().setQuickFilter(0, new QuickFilter() {
            @Override
            public boolean accept(Object aValue) {
                if (matchString.isEmpty()) {
                    return true;
                }
                if (aValue instanceof Node) {
                    Node node = (Node) aValue;
                    return nodeIsMatchesToFilter(node);
                } else {
                    return true;
                }
            }
        });

        final KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // processKey(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                processKey(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        beanView.addKeyListener(keyListener);
        beanView.getOutline().addKeyListener(keyListener);
        beanView.requestFocusInWindow();
    }

    protected void processKey(KeyEvent e) {
        if (e.isConsumed()) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            e.consume();
            String text = textField.getText();
            if (!text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
            }
            textField.setText(text);
            quickSearchContentChanged();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            e.consume();
            if (selection().isEmpty()) {
                return;
            } else {
                displayer.close(true);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!matchString.isEmpty()) {
                e.consume();
                textField.setText("");
                quickSearchContentChanged();
            }
        } else {
            char c = e.getKeyChar();
            if (c != ' ' && Character.isLetterOrDigit(c)) {
                e.consume();
                String text = textField.getText();
                text += String.valueOf(c);
                textField.setText(text);
                quickSearchContentChanged();

            } else if (c == ' ') {
                e.consume();
                org.openide.nodes.Node[] nodes = manager.getSelectedNodes();
                for (org.openide.nodes.Node node : nodes) {
                    if (node instanceof Node) {
                        Node n = (Node) node;
                        n.setSelected(!n.isSelected());
                    }
                }
            }
        }
    }

    private boolean nodeIsMatchesToFilter(Node node) {
        if (node.layer.getName().startsWith(matchString)) {
            return true;
        } else {
            org.openide.nodes.Node[] nodes = node.getChildren().getNodes();
            for (org.openide.nodes.Node c : nodes) {
                if (c instanceof Node) {
                    if (nodeIsMatchesToFilter((Node) c)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    private void quickSearchContentChanged() {
        matchString = this.textField.getText();
        panel.setVisible(!matchString.isEmpty());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean wasFocused = beanView.getOutline().isFocusOwner();
                org.openide.nodes.Node[] selectedNode = manager.getSelectedNodes();
                beanView.getOutline().tableChanged(new TableModelEvent(beanView.getOutline().getModel()));
                try {
                    manager.setSelectedNodes(selectedNode);
                } catch (PropertyVetoException ex) {
                }
                if (wasFocused) {
                    beanView.getOutline().requestFocus();
                }
            }
        });
    }

    public List<Layer> selection() {
        return Collections.unmodifiableList(beanView.selectedLayers);
    }

    public void open(final Branch branch, Layer currentLayer) {
        this.currentLayer = currentLayer;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jSplitPane1.setDividerLocation(0.6);
            }
        });


        beanView.selectedLayers.clear();
        beanView.selectedLayers.addAll(currentLayer.listBaseLayers());
        beanView.listModel.fireChange();
        org.openide.nodes.Node node = new RootNode(beanView, branch);
        final org.openide.nodes.Node root = node;

        lstSelectedLayers.setModel(beanView.listModel);
        manager.setRootContext(node);
        final org.openide.nodes.Node[] rootChildren = node.getChildren().getNodes();
        node = null;
        for (int i = 0; i < rootChildren.length; i++) {
            if (rootChildren[i].getChildren().getNodesCount() > 0) {
                node = rootChildren[i].getChildren().getNodeAt(0);
                break;
            }
        }
        final org.openide.nodes.Node selection = node;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (selection != null) {
                    try {
                        manager.setExploredContextAndSelection(root, new org.openide.nodes.Node[]{selection});

                    } catch (PropertyVetoException ex) {
                    }
                }
            }
        });


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        btMoveUp = new javax.swing.JButton();
        btMoveDown = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSelectedLayers = new javax.swing.JTable();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(5);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        btMoveUp.setText(org.openide.util.NbBundle.getMessage(ChooseBaseLayersPanel.class, "ChooseBaseLayersPanel.btMoveUp.text")); // NOI18N
        btMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMoveUpActionPerformed(evt);
            }
        });

        btMoveDown.setText(org.openide.util.NbBundle.getMessage(ChooseBaseLayersPanel.class, "ChooseBaseLayersPanel.btMoveDown.text")); // NOI18N
        btMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMoveDownActionPerformed(evt);
            }
        });

        lstSelectedLayers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(lstSelectedLayers);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btMoveDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btMoveUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btMoveUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btMoveDown)
                        .addGap(0, 169, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jSplitPane1.setBottomComponent(jPanel1);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void btMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMoveDownActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                move(1);
            }
        });
    }//GEN-LAST:event_btMoveDownActionPerformed

    private void btMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMoveUpActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                move(-1);
            }
        });
    }//GEN-LAST:event_btMoveUpActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btMoveDown;
    private javax.swing.JButton btMoveUp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable lstSelectedLayers;
    // End of variables declaration//GEN-END:variables
}
