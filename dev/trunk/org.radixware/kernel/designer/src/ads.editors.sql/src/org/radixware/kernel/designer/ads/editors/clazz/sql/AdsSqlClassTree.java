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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef.IUsedTable;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTreeActions.RemoveSelectedObjects;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagBounds;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagMapper;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagMapperListener;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsSqlClassTree extends JTree {

    public static final String PARAMETERS_NODE = "parmeters-node";
    public static final String FIELDS_NODE = "fields-node";
    public static final String DYNAMIC_PROPERTIES_NODE = "dynamic-properties-node";
//    public static final String PROPERTIES_NODE = "properties-node";
    public static final String TABLES_NODE = "tables-node";
    public static final String TABLE_REFERENCES_NODE = "table-references-node";
    public static final String TABLE_PROPERTIES_NODE = "table-properties-node";
    public static final String TABLE_INDICIES_NODE = "table-indicies-node";
    private static final String GLOBAL_TREE_ACTION = "global-tree-action";
    private static final String SELECTION_AWARE_TREE_ACTION = "selection-aware-tree-action";
    private static final String DELETE_KEY_ACTION = "delete-key-action";
    private final Map<Object, DefaultMutableTreeNode> object2node = new HashMap<>();
    private DefaultMutableTreeNode root;
    private final AdsSqlClassCodeEditor editor;
    private NodeFactory factory;
    private ISqlDef sqlDef;
    private UsedTablesChangeListener usedTablesListener;
    private boolean initied = false;

    public AdsSqlClassTree(AdsSqlClassCodeEditor editor) {
        this(editor, new DefaultNodeFactory());
    }

    public AdsSqlClassTree(final AdsSqlClassCodeEditor editor, final NodeFactory factory) {
        super();
        setModel(null);
        this.editor = editor;
        this.factory = factory;
        setCellRenderer(new Renderer());
        addMouseListener(new MouseListenerImpl());
        ToolTipManager.sharedInstance().registerComponent(this);
        addTreeSelectionListener(new SelectionListener(this));
        this.toggleClickCount = Integer.MAX_VALUE;
        TagMapper.getInstance(editor.getPane().getDocument()).addListener(new TreeUpdater());
        initKeyMap();
        requestFocusInWindow();
//        setRowHeight(18);
    }

    private class TreeUpdater implements TagMapperListener {

        @Override
        public void tagRemoved(TagBounds tagBounds) {
        }

        @Override
        public void tagAdded(TagBounds tagBounds) {
            if (sqlDef == null) {
                return;
            }
            if (tagBounds.getVTag().getTag() instanceof TableSqlNameTag) {
                final TableSqlNameTag tag = (TableSqlNameTag) tagBounds.getVTag().getTag();
                boolean isUsed = false;
                for (IUsedTable usedTable : sqlDef.getUsedTables().list()) {
                    if (Utils.equals(usedTable.getTableId(), tag.getTableId()) && Utils.aliasesAreEqual(usedTable.getAlias(), tag.getTableAlias())) {
                        isUsed = true;
                        break;
                    }
                }
                if (!isUsed) {
                    sqlDef.getUsedTables().add(tag.getTableId(), tag.getTableAlias());
                }
            } else if (tagBounds.getVTag().getTag() instanceof DbFuncCallTag) {
                update();
            }
        }

        @Override
        public void tagUpdated(TagBounds tagBounds) {
        }
    }

    private void initKeyMap() {
        addGlobalAction(new DeleteKeyAction());
    }

    public void addGlobalAction(Action a) {
        a.putValue(GLOBAL_TREE_ACTION, Boolean.TRUE);
        String command = (String) a.getValue(Action.ACTION_COMMAND_KEY);
        String name = (String) a.getValue(Action.NAME);
        getInputMap().put(KeyStroke.getKeyStroke(command), name);
        getActionMap().put(name, a);
    }

    public void addCurrentSelectionAction(Action a) {
        a.putValue(SELECTION_AWARE_TREE_ACTION, Boolean.TRUE);
        String command = (String) a.getValue(Action.ACTION_COMMAND_KEY);
        String name = (String) a.getValue(Action.NAME);
        getInputMap().put(KeyStroke.getKeyStroke(command), name);
        getActionMap().put(name, a);
    }

    private static abstract class GlobalTreeAction extends AbstractAction {

        public GlobalTreeAction(String name) {
            super(name);
            putValue(ACTION_COMMAND_KEY, getActionCommand());
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(getActionCommand()));
        }

        protected abstract String getActionCommand();
    }

    private static class DeleteKeyAction extends GlobalTreeAction {

        public DeleteKeyAction() {
            super(DELETE_KEY_ACTION);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AdsSqlClassTreeActions.RemoveSelectedObjects action = new RemoveSelectedObjects();
            AdsSqlClassTree tree = (AdsSqlClassTree) e.getSource();
            if (action.isAvailable(tree)) {
                action.actionPerformed(e);
            } else {
                tree.getToolkit().beep();
            }
        }

        @Override
        protected String getActionCommand() {
            return "DELETE";
        }
    }

    public void open(ISqlDef sqlDef, OpenInfo info) {
        this.sqlDef = sqlDef;
        if (usedTablesListener != null) {
            usedTablesListener.invalidate();
        }
        usedTablesListener = new UsedTablesChangeListener();
        sqlDef.getUsedTables().getContainerChangesSupport().addEventListener(usedTablesListener);
        rebuildTreeWithStatePersistance();
        initied = true;
    }

    public boolean isReadOnly() {
        return (sqlDef == null) || sqlDef.isReadOnly();
    }

    private class UsedTablesChangeListener implements ContainerChangesListener {

        private boolean valid = true;

        public void invalidate() {
            valid = false;
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (valid) {
                AdsSqlClassTree.this.rebuildTree();
            }
        }
    }

    private void rebuildTree() {
        root = factory.create(sqlDef, this);
        DefaultTreeModel model = new DefaultTreeModel(root);

        setRootVisible(false);
        setModel(model);

        for (int i = 0; i < root.getChildCount(); i++) {
            TreePath path = new TreePath(model.getPathToRoot(root.getChildAt(i)));
            expandPath(path);
        }
    }

    public void update() {
        rebuildTreeWithStatePersistance();
    }

    void rebuildTreeWithStatePersistance() {
        boolean persistant = false;
        Set<Object> openedObjects = null;
        Set<Object> selectedObjects = null;
        if (initied) {
            selectedObjects = new HashSet<>();
            openedObjects = new HashSet<>();
            Enumeration<TreeNode> nodes = root.preorderEnumeration();
            while (nodes.hasMoreElements()) {
                TreeNode treeNode = nodes.nextElement();
                if (treeNode instanceof Node) {
                    Node node = (Node) treeNode;
                    if (isVisible(buildPathForNode(node))) {
                        openedObjects.add(node.getNodeInfo().getObject());
                    }
                }
            }
            if (getSelectionPaths() != null) {
                for (TreePath selectedPath : getSelectionPaths()) {
                    if (selectedPath.getLastPathComponent() instanceof Node) {
                        selectedObjects.add(((Node) selectedPath.getLastPathComponent()).getNodeInfo().getObject());
                    }
                }
            }
            persistant = true;
        }
        rebuildTree();
        if (persistant) {
            for (Object o : openedObjects) {
                TreeNode treeNode = findNodeByObject(o);
                if (treeNode != null) {
                    TreePath path = buildPathForNode(treeNode);
                    if (selectedObjects.contains(o)) {
                        addSelectionPath(path);
                    }
                    expandPath(path.getParentPath());
                }
            }
        }
    }

    public ISqlDef getSqlDef() {
        return sqlDef;
    }

    private JPopupMenu buildPopupMenu(DefaultMutableTreeNode node) {
        JPopupMenu popup = new JPopupMenu();
        List<PopupMenuAction> actions = collectActionsForNode(node);
        Collections.sort(actions, new ActionComparator());
        PopupMenuAction prevAction = null;
        for (PopupMenuAction action : actions) {
            if (prevAction != null && prevAction.getType() != action.getType()) {
                popup.addSeparator();
            }
            if (action.isAvailable(this)) {
                popup.add(action.getPopupPresenter());
                prevAction = action;
            }
        }
        return popup;
    }

    private List<PopupMenuAction> collectActionsForNode(DefaultMutableTreeNode node) {
        List<PopupMenuAction> list = new LinkedList<>();
        NodeInfo info = (NodeInfo) node.getUserObject();
        if (info.getActions() != null) {
            for (PopupMenuAction action : info.getActions()) {
                if (action.isAvailable(this)) {
                    list.add(action);
                }
            }
        }
        return list;

    }

    public DefaultMutableTreeNode findNodeByObject(DefaultMutableTreeNode root, Object object) {
        if (object2node.containsKey(object)) {
            return object2node.get(object);
        }
        assert object != null : "Don't try to find null.";
        DefaultMutableTreeNode node = findNodeByObjectImlp(root, object);
        return node;
    }

    public DefaultMutableTreeNode findNodeByObject(Object object) {
        return findNodeByObject(root, object);
    }

    public Node findNodeByGroupName(DefaultMutableTreeNode root, String groupName) {
        Group group = new Group(groupName, null, null, null, null);
        return (Node) findNodeByObjectImlp(root, group);
    }

    public Node findNodeByGroupName(String groupName) {
        return findNodeByGroupName(root, groupName);
    }

    private DefaultMutableTreeNode findNodeByObjectImlp(DefaultMutableTreeNode node, Object object) {
        Enumeration nodes = node.preorderEnumeration();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) nodes.nextElement();
            if (currentNode.getUserObject() instanceof NodeInfo) {
                NodeInfo info = (NodeInfo) currentNode.getUserObject();
                if (object.equals(info.getObject())) {
                    return currentNode;
                }
            } else if (object.equals(currentNode.getUserObject())) {
                return currentNode;
            }
        }
        return null;
    }

    public boolean setSelectedObject(Object object) {
        DefaultMutableTreeNode node = findNodeByObject(object);
        if (node == null) {
            return false;
        }
        return setSelectedNode(node);
    }

    public boolean setSelectedNode(TreeNode node) {
        if (node == null) {
            return false;
        }
        setSelectionPath(buildPathForNode(node));
        return true;
    }

    public TreePath buildPathForNode(TreeNode node) {
        List<Object> list = new LinkedList<>();
        while (node != null) {
            list.add(0, node);
            node = node.getParent();
        }
        return new TreePath(list.toArray());
    }

    private static class ActionComparator implements Comparator<PopupMenuAction> {

        @Override
        public int compare(PopupMenuAction o1, PopupMenuAction o2) {
            if (o1.getType() == o2.getType()) {
                return compareIntValues(o1.getPriority(), o2.getPriority());
            }
            return compareIntValues(o1.getType(), o2.getType());
        }

        private int compareIntValues(int a, int b) {
            if (a == b) {
                return 0;
            }
            if (a < b) {
                return -1;
            }
            return 1;
        }
    }

    public AdsSqlClassCodeEditor getEditor() {
        return editor;
    }

    public NodeFactory getNodeFactory() {
        return factory;
    }

    private class MouseListenerImpl extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                performDefaultAction(e);
            }
        }

        private void performDefaultAction(MouseEvent e) {
            TreePath path = AdsSqlClassTree.this.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                NodeInfo info = (NodeInfo) node.getUserObject();
                Action defaultAction = info.getDefultAction();
                if (defaultAction instanceof PopupMenuAction) {
                    defaultAction.setEnabled(((PopupMenuAction) defaultAction).isAvailable(AdsSqlClassTree.this));
                }
                if (defaultAction != null && defaultAction.isEnabled()) {
                    info.getDefultAction().actionPerformed(AdsSqlClassTree.this);
                    e.consume();
                }
            }
        }

        private void showPopup(MouseEvent e) {
            TreePath path = AdsSqlClassTree.this.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                setSelectionPath(path);
                JPopupMenu popup = buildPopupMenu(node);
                if (popup.getComponentCount() > 0) {
                    popup.show((Component) e.getSource(), e.getX(), e.getY());
                }
            }
        }
    }

    public static abstract class PopupMenuAction extends AbstractAction implements Presenter.Popup {

        public static final String POPUP_MENU_TEXT = "popup-menu-text";
        public static final int TOP = 10;
        public static final int ADD = 20;
        public static final int MIDDLE = 30;
        public static final int EDIT = 40;
        public static final int BOTTOM = 50;

        public PopupMenuAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AdsSqlClassTree tree = findTree(e);
            if (isAvailable(tree)) {
                actionPerformed(tree);
            }
        }

        private AdsSqlClassTree findTree(ActionEvent e) {
            AdsSqlClassTree tree = null;
            Component source = (Component) e.getSource();
            while (source != null) {
                if (source instanceof AdsSqlClassTree) {
                    return (AdsSqlClassTree) source;
                }
                if (source instanceof AdsSqlClassTreePanel) {
                    return ((AdsSqlClassTreePanel) source).getTree();
                }

                if (source instanceof JPopupMenu) {
                    source = ((JPopupMenu) source).getInvoker();
                    continue;
                }

                if (source instanceof AdsSqlClassBodyPanel) {
                    return ((AdsSqlClassBodyPanel) source).treePanel.getTree();
                }

                source = source.getParent();
            }
            return null;
        }

        protected abstract void actionPerformed(AdsSqlClassTree tree);

        protected abstract boolean isAvailable(AdsSqlClassTree tree);

        @Override
        public JMenuItem getPopupPresenter() {
            if (getValue(ACTION_COMMAND_KEY) instanceof String) {
                String command = (String) getValue(ACTION_COMMAND_KEY);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(command));
            }
            JMenuItem presenter = new JMenuItem(this);
            presenter.setText((String) getValue(POPUP_MENU_TEXT));
            presenter.setEnabled(isEnabled());
            return presenter;
        }

        protected abstract int getType();

        protected abstract int getPriority();
    }

    private static class Renderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            setBorderSelectionColor(super.getBackgroundSelectionColor());
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            Object object = ((DefaultMutableTreeNode) value).getUserObject();
            if (object instanceof NodeInfo) {
                NodeInfo info = (NodeInfo) object;
                final Object source = info.getObject();
                String label = info.getText();
                if (source instanceof DdsColumnDef) {
                    final DdsColumnDef col = (DdsColumnDef) source;
                    if (col.isNotNull()) {
                        label = HtmlUtils.bold(label);
                    }
                    
                    if (col.isPrimaryKey()) {
                        label = HtmlUtils.underline(label);
                    }
                    label = HtmlUtils.html(label);
                }
                setText(label);
                setIcon(info.getIcon());
                setToolTipText(info.getToolTip());
            }

            setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            return this;
        }
    }

    private static class HtmlUtils {
        public static String bold(String source) {
            return "<b>" + source + "</b>";
        }
        
        public static String underline(String source) {
            return "<u>" + source + "</u>";
        }
        
        public static String html(String source) {
            return "<html><body>" + source + "</body></html>";
        }
    }
    
    private static class SelectionListener implements TreeSelectionListener {

        private List<Action> overridenGlobalActions = new LinkedList<>();
        private AdsSqlClassTree tree;

        public SelectionListener(AdsSqlClassTree tree) {
            this.tree = tree;
        }

        private void addOverridenGlobalAction(Action a) {
            overridenGlobalActions.add(a);
        }

        private void restoreOverridenGlobalActions() {
            while (overridenGlobalActions.size() > 0) {
                tree.addGlobalAction(overridenGlobalActions.get(0));
                overridenGlobalActions.remove(0);
            }
        }

        private void cleanActions() {
            for (KeyStroke key : tree.getInputMap().allKeys()) {
                Action a = tree.getActionMap().get(tree.getInputMap().get(key));
                if (isCurrentSelectionAction(a)) {
                    tree.getActionMap().remove(tree.getInputMap().get(key));
                    tree.getInputMap().remove(key);
                }
            }
        }

        private boolean isCurrentSelectionAction(Action a) {
            return Boolean.TRUE.equals(a.getValue(SELECTION_AWARE_TREE_ACTION));
        }

        private boolean isGlobal(Action a) {
            return Boolean.TRUE.equals(a.getValue(GLOBAL_TREE_ACTION));
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            //Restore global actions that can be overriden by previos selection
            restoreOverridenGlobalActions();
            //Remove actions from previos selection
            cleanActions();
            if (tree.getSelectionPaths() != null && tree.getSelectionPaths().length == 1) {
                TreePath path = tree.getSelectionPath();
                if (path.getLastPathComponent() instanceof Node) {
                    NodeInfo info = ((Node) path.getLastPathComponent()).getNodeInfo();
                    PopupMenuAction[] actions = info.getActions();
                    if (actions != null) {
                        for (PopupMenuAction action : actions) {
                            String key = (String) action.getValue(AbstractAction.ACTION_COMMAND_KEY);
                            if (key != null) {
                                KeyStroke keystroke = KeyStroke.getKeyStroke(key);
                                if (tree.getInputMap().get(keystroke) != null) {
                                    Action a = tree.getActionMap().get(tree.getInputMap().get(keystroke));
                                    if (a != null && isGlobal(a)) {
                                        addOverridenGlobalAction(a);
                                    }
                                }
                                tree.addCurrentSelectionAction(action);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class NodeInfo<T extends Object> {

        private T object;
        private Icon icon;
        private String text;
        private String toolTip;
        private PopupMenuAction[] actions;

        public NodeInfo(T object) {
            this.object = object;
        }

        public NodeInfo(Icon icon, String text, String toolTip) {
            this.icon = icon;
            this.text = text;
            this.toolTip = toolTip;
        }

        public NodeInfo(T object, Icon icon, String text, String toolTip) {
            this(icon, text, toolTip);
            this.object = object;
        }

        public T getObject() {
            return object;
        }

        /**
         * If Node is leaf, then this action will be performed
         * on double click by it.
         * @return
         */
        public PopupMenuAction getDefultAction() {
            return null;
        }

        public boolean canEdit(AdsSqlClassTree tree) {
            return false;
        }

        public boolean edit(AdsSqlClassTree tree) {
            throw new UnsupportedOperationException("This operation is not available for selected object");
        }

        public boolean canRemove(AdsSqlClassTree tree) {
            return false;
        }

        /**
         * Called when user remove some object from tree. Note, that selected Node would be
         * removed automatically, so you shouldn't remove it by yourself.
         */
        public void remove(AdsSqlClassTree tree) {
            throw new UnsupportedOperationException("This operation is not available for selected object");
        }

        public boolean canGoTo() {
            return false;
        }

        public void performGoTo() {
            throw new UnsupportedOperationException("This operation is not available for selected object");
        }

        public PopupMenuAction[] getActions() {
            return actions;
        }

        public boolean canInsertTag(AdsSqlClassTree tree) {
            return false;
        }

        public void insertTag(AdsSqlClassTree tree) {
            Scml.Tag tag = createTag(tree);
            if (tag != null) {
                tree.getEditor().getPane().replaceSelection("");
                tree.getEditor().getPane().insertTag(tag);
            }
        }

        protected Scml.Tag createTag(AdsSqlClassTree tree) {
            throw new UnsupportedOperationException("This operation is not available for selected object");
        }

        public Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getToolTip() {
            return toolTip;
        }

        public void setToolTip(String tooltip) {
            this.toolTip = tooltip;
        }
    }

    public static class Node extends DefaultMutableTreeNode {

        public Node(Object userObject) {
            super(userObject);
        }

        public NodeInfo getNodeInfo() {
            return (NodeInfo) getUserObject();
        }
    }

    public static class Group {

        private String name;
        private String title;
        private Icon icon;
        private String toolTip;
        private PopupMenuAction[] actions;

        public Group(String name, String title, Icon icon, String toolTip, PopupMenuAction[] actions) {
            this.name = name;
            this.title = title;
            this.icon = icon;
            this.toolTip = toolTip;
            this.actions = actions;
        }

        public String getName() {
            return name;
        }

        public Icon getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }

        public String getToolTip() {
            return toolTip;
        }

        public PopupMenuAction[] getActions() {
            return actions;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Group) {
                return name.equals(((Group) obj).getName());
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }
    }

    public static interface NodeInfoFactory {

        /**
         * Creates {@link NodeInfo} instance for specified object.<br>
         * In common case, it will be used by NodeFactory.
         * @param object
         * @return {@link NodeInfo}
         */
        public NodeInfo create(Object object);
    }

    public static interface NodeFactory {

        /**
         * Creates {@link Node} instance for specified object
         * @param object
         * @return {@link Node}
         */
        public Node create(Object object, AdsSqlClassTree tree);
    }
}
