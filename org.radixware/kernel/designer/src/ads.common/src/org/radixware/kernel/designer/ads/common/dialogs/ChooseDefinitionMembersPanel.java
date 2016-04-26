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
package org.radixware.kernel.designer.ads.common.dialogs;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
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
import org.radixware.kernel.common.defs.ads.clazz.AdsEmbeddedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IClassInclusive;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers.ChooseDefinitionMembersCfg;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class ChooseDefinitionMembersPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static class Node extends AbstractNode implements CheckableNode {

        protected static final boolean QNAME = true;
        private final transient AdsDefinition definition;
        protected transient final TreeView tree;
        private transient boolean isSelected;

        private Node(final TreeView tree, final AdsDefinition definition, final Children children, final InstanceContent content) {
            super(children, new AbstractLookup(content));
            content.add(this);
            this.definition = definition;
            this.tree = tree;
        }

        public Node(final TreeView tree, final AdsDefinition definition, final Children children) {
            this(tree, definition, children, new InstanceContent());
        }

        @Override
        public boolean isCheckable() {
            return true;
        }

        @Override
        public boolean isCheckEnabled() {
            return true;
        }

        @Override
        public Boolean isSelected() {
            return Boolean.valueOf(isSelected);
        }

        @Override
        public void setSelected(final Boolean selected) {
            isSelected = selected.booleanValue();
            if (this.getChildren() == Children.LEAF) {
                if (isSelected) {
                    tree.selectedDefinitions.add(definition);
                } else {
                    tree.selectedDefinitions.remove(definition);
                }
            }
            tree.nodeSelected(this);
        }

        @Override
        public Image getIcon(final int type) {
            return getDefinition().getIcon().getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(final int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return ((getDefinition() instanceof AdsMethodDef) ? ((AdsMethodDef) getDefinition()).getProfile().getName() : (QNAME ? getDefinition().getQualifiedName() : getDefinition().getName()));
        }

        private AdsDefinition getDefinition() {

            if (definition instanceof AdsEmbeddedClassDef) {
                final AdsEmbeddedClassDef embeddedClass = (AdsEmbeddedClassDef) definition;
                final IClassInclusive hostObject = embeddedClass.getHostObject();

                if (hostObject instanceof AdsDefinition) {
                    return (AdsDefinition) hostObject;
                }
                return embeddedClass.getOwnerDef();
            }

            return definition;
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

    private static class ItemNodeChildren extends Children.Array {

        private static Collection<org.openide.nodes.Node> createNodes(final TreeView tree, final List<? extends AdsDefinition> members) {
            final ArrayList<org.openide.nodes.Node> nodes = new ArrayList<>();
            Collections.sort(members, COMPARATOR);
            for (AdsDefinition def : members) {
                nodes.add(new ItemNode(tree, def));
            }
            return nodes;
        }

        public ItemNodeChildren(final TreeView tree, final List<? extends AdsDefinition> members) {
            super(createNodes(tree, members));
        }
    }

    private static class ItemNode extends Node {

        public ItemNode(final TreeView tree, final AdsDefinition definition) {
            super(tree, definition, Children.LEAF);
        }

        public ItemNode(final TreeView tree, final AdsDefinition definition, final List<? extends AdsDefinition> members) {
            super(tree, definition, new ItemNodeChildren(tree, members));
        }

        @Override
        public void setSelected(final Boolean selected) {
            super.setSelected(selected);
            if (this.getChildren() instanceof ItemNodeChildren) {
                for (org.openide.nodes.Node node : this.getChildren().getNodes()) {
                    if (node instanceof Node) {
                        ((Node) node).setSelected(selected);
                    }
                }
            }
        }
    }

    private static class RootNodeChildren extends Children.Array {

        private final transient ChooseDefinitionMembersCfg cfg;

        RootNodeChildren(final TreeView tree, final ChooseDefinitionMembersCfg cfg) {
            final ArrayList<Node> newNodes = new ArrayList<>();
            this.cfg = cfg;
            if (cfg.additionalDefinitions != null) {
                for (int i = 0, len = cfg.additionalDefinitions.size(); i < len; i++) {
                    newNodes.add(new ItemNode(tree, cfg.additionalDefinitions.get(i)));
                }
            }
            final HashMap<Id, Object> addedItems = new HashMap<>();
            if (cfg.selfDisplaying) {
                @SuppressWarnings("unchecked")
                List<? extends AdsDefinition> members = cfg.listMembers(cfg.initialDef, false);
                if (members != null && !members.isEmpty()) {
                    for (AdsDefinition member : members) {
                        addedItems.put(member.getId(), member);
                    }
                }
                if (!members.isEmpty()) {
                    newNodes.add(new ItemNode(tree, cfg.initialDef, members));
                }
            }
            buildTree(tree, cfg.initialDef, addedItems, newNodes, new HashSet<AdsDefinition>());
            if (this.nodes == null) {
                this.nodes = new ArrayList<>();
            } else {
                this.nodes.clear();
            }
            this.nodes.addAll(newNodes);
        }

        private void buildTree(final TreeView tree, final AdsDefinition context, final HashMap<Id, Object> addedItems, final List<Node> newNodes, Collection<AdsDefinition> seen) {

            if (cfg.breakHierarchy(context)) {
                return;
            }
            @SuppressWarnings("unchecked")
            final List<? extends AdsDefinition> defs = cfg.listBaseDefinitions(context, seen);

            final ArrayList<AdsDefinition> defs2 = new ArrayList<>();

            for (AdsDefinition def : defs) {
                @SuppressWarnings("unchecked")
                final List<? extends AdsDefinition> members = cfg.listMembers(def, def.getId() == context.getId());
                final List<AdsDefinition> unexposed = new ArrayList<>();
                if (members != null && !members.isEmpty()) {
                    for (AdsDefinition member : members) {
                        if (!addedItems.containsKey(member.getId())) {
                            unexposed.add(member);
                            addedItems.put(member.getId(), member);
                        }
                    }
                    if (!unexposed.isEmpty()) {
                        newNodes.add(new ItemNode(tree, def, unexposed));
                    }
                }
                defs2.add(def);

            }
            if (cfg.incremental()) {
                for (AdsDefinition def : defs2) {
                    if (cfg.breakHierarchy(def)) {
                        continue;
                    }
                    buildTree(tree, def, addedItems, newNodes, seen);
                }
            }

        }
    }

    private class RootNode extends AbstractNode {

        public RootNode(final TreeView tree) {
            super(new RootNodeChildren(tree, cfg));
        }
    }
    private transient ChooseDefinitionMembersCfg cfg = null;
    final transient ChangeSupport changeSupport = new ChangeSupport(this);
    private final transient TreeView beanView = new TreeView();
    private transient final ExplorerManager manager = new ExplorerManager();

    private class TreeView extends OutlineView {

        private final List<AdsDefinition> selectedDefinitions = new ArrayList<>();

        public TreeView() {
            super();
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

    public ChooseDefinitionMembersPanel() {
        super();
        initComponents();
        setLayout(new BorderLayout(3, 3));
        add(this.beanView, BorderLayout.CENTER);

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

        panel.setVisible(false);
        add(panel, BorderLayout.SOUTH);
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
        if (node.getDefinition().getName().startsWith(matchString)) {
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

    public List<AdsDefinition> selection() {
        return Collections.unmodifiableList(beanView.selectedDefinitions);
    }

    public void open(final ChooseDefinitionMembersCfg cfg) {
        this.cfg = cfg;
        org.openide.nodes.Node node = new RootNode(beanView);
        final org.openide.nodes.Node root = node;
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

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
