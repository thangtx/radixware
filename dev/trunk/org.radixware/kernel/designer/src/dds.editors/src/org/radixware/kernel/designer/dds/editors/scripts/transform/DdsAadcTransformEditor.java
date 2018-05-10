/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.dds.editors.scripts.transform;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import org.radixware.kernel.common.components.TreeView.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsAadcTransform;
import org.radixware.kernel.common.repository.dds.DdsAadcTransformColumn;
import org.radixware.kernel.common.repository.dds.DdsAadcTransformTable;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class DdsAadcTransformEditor extends StateAbstractDialog.StateAbstractPanel {
    private final static String WARNINGS = "Warnings";
    private final static String TABLES = "Tables";
    private final TransformNode warningsNode;
    private final TransformNode tablesNode;
    private final AddWarning addWarningsAction = new AddWarning();
    private final AddTable addTableAction = new AddTable();
    private final RemoveAction removeAction = new RemoveAction();
    private final EditAction editAction = new EditAction();
    private DdsAadcTransform transform = null;
    private Layer layer = null;
    TreeSelectionListener treeListener = new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                setEnabledOnSelection();
            }
        };
    
    public static boolean showDialog(StateAbstractDialog.StateAbstractPanel editor, String title, RadixIcon icon) {
        final StateAbstractDialog dg = new StateAbstractDialog(editor, title) {};
        dg.setIcon(icon);
        return dg.showModal();
    }

    /**
     * Creates new form DdsAadcTransformEditor
     */
    public DdsAadcTransformEditor() {
        initComponents();
        warningsNode = new TransformNode(null);
        warningsNode.setDisplayName(WARNINGS);
        warningsNode.setIcon(RadixObjectIcon.getForSeverity(EEventSeverity.WARNING).getIcon());
        treeView1.getRootNode().add(warningsNode, false);
        tablesNode = new TransformNode(null);
        tablesNode.setDisplayName(TABLES);
        tablesNode.setIcon(DdsDefinitionIcon.TABLE.getIcon());
        treeView1.getRootNode().add(tablesNode, false);
        btnAddWarning.setAction(addWarningsAction);
        btnAddTable.setAction(addTableAction);
        btnRemove.setAction(removeAction);
        btnEdit.setAction(editAction);
        btnAddColumn.setAction(new AddColumn(null, null, null));
        btnAddColumn.setEnabled(false);
        btnRemove.setEnabled(false);
        btnEdit.setEnabled(false);
        treeView1.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeView1.addMouseListener(new MouseAdapter() {

            private static final int DOUBLE_CLICK = 2;
            EditAction editAction = new EditAction();

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!treeView1.isEnabled()) {
                    return;
                }
                if (e.getClickCount() == DOUBLE_CLICK) {
                    editAction.actionPerformed(null);
                }
            }
        });
        treeView1.addTreeSelectionListener(treeListener);
        rbNotRequired.setVisible(false);
        rbRequired.setVisible(false);
        
    }
    
    public void open(Layer layer, DdsAadcTransform transform, boolean create) {
        this.transform = transform;
        this.layer = layer;
        
        cbRequired.setVisible(!create);
        rbNotRequired.setVisible(create);
        rbRequired.setVisible(create);
        update();
    }
    
    private void update() {
        updateCb();
        tablesNode.clear();
        DdsAadcTransform.DdsAadcTransformTables aadcTransformTables = transform.getTables();
        if (aadcTransformTables != null) {
            for (DdsAadcTransformTable table : transform.getTables()) {
                addTable(table);
            }
        }
        warningsNode.clear();
        List<String> list = transform.getWarnings();
        if (list != null) {
            for (String warning : list) {
                addWarning(warning);
            }
        }
        check();
    }
    
    public void open(Layer layer, DdsAadcTransform transform) {
        open(layer, transform, false);
    }
    
    private void addColumn(Node parentNode, DdsAadcTransformColumn column){
        TransformNode node = new TransformNode(column);
        node.setIcon(DdsDefinitionIcon.COLUMN.getIcon());
        node.setDisplayName(column.toString());
        parentNode.add(node, false);
        check();
    }
    
    private void addTable(DdsAadcTransformTable table){
        TransformNode node = new TransformNode(table);
        node.setIcon(DdsDefinitionIcon.TABLE.getIcon());
        updateTable(table, node);
        tablesNode.add(node, false);
        check();
    }
    
    private void updateTable(DdsAadcTransformTable table, Node node) {
        node.setDisplayName(table.toString());
        node.clear();
        for (DdsAadcTransformColumn column : table.getColumns()){
            addColumn(node, column);
        }
        check();
    }
    
    private void addWarning(String warning){
        Node node = new Node(warning);
        node.setIcon(RadixObjectIcon.getForSeverity(EEventSeverity.WARNING).getIcon());
        node.setDisplayName(warning);
        warningsNode.add(node, false);
        check();
    }

    private JPopupMenu getPopupMenu(Node node) {
        if (!treeView1.isEnabled()) {
            return null;
        }
        JPopupMenu jPopupMenu = new JPopupMenu();
        if (node != warningsNode && node != tablesNode) {
            jPopupMenu.add(editAction);
            jPopupMenu.add(removeAction);
        }
        if (node == warningsNode) {
            jPopupMenu.add(addWarningsAction);
        } else if (node == tablesNode) {
            jPopupMenu.add(addTableAction);
        } else {
            Object obj = node.getUserObject();
            if (obj instanceof DdsAadcTransformTable) {
                DdsAadcTransformTable table = (DdsAadcTransformTable)obj;
                jPopupMenu.add(new AddColumn(table, table.findTable(layer), node));
            }
        }
        return jPopupMenu;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        cbRequired = new javax.swing.JCheckBox();
        cbForbidden = new javax.swing.JCheckBox();
        rbRequired = new javax.swing.JRadioButton();
        rbNotRequired = new javax.swing.JRadioButton();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddWarning = new javax.swing.JButton();
        btnAddTable = new javax.swing.JButton();
        btnAddColumn = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        treeView1 = new org.radixware.kernel.common.components.TreeView() {
            @Override
            public JPopupMenu getPopupMenu(Node node) {
                return DdsAadcTransformEditor.this.getPopupMenu(node);
            }
        };

        setPreferredSize(new java.awt.Dimension(300, 203));

        org.openide.awt.Mnemonics.setLocalizedText(cbRequired, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.cbRequired.text")); // NOI18N
        cbRequired.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRequiredActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cbForbidden, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.cbForbidden.text")); // NOI18N
        cbForbidden.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbForbiddenItemStateChanged(evt);
            }
        });

        buttonGroup1.add(rbRequired);
        org.openide.awt.Mnemonics.setLocalizedText(rbRequired, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.rbRequired.text")); // NOI18N
        rbRequired.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbRequiredItemStateChanged(evt);
            }
        });

        buttonGroup1.add(rbNotRequired);
        org.openide.awt.Mnemonics.setLocalizedText(rbNotRequired, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.rbNotRequired.text")); // NOI18N
        rbNotRequired.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbNotRequiredItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(rbRequired)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbNotRequired))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cbRequired)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbForbidden))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbRequired)
                    .addComponent(rbNotRequired))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbRequired)
                    .addComponent(cbForbidden))
                .addGap(0, 0, 0))
        );

        jToolBar1.setFloatable(false);

        org.openide.awt.Mnemonics.setLocalizedText(btnAddWarning, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.btnAddWarning.text")); // NOI18N
        btnAddWarning.setFocusable(false);
        btnAddWarning.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAddWarning.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnAddWarning);

        org.openide.awt.Mnemonics.setLocalizedText(btnAddTable, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.btnAddTable.text")); // NOI18N
        btnAddTable.setFocusable(false);
        btnAddTable.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAddTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnAddTable);

        org.openide.awt.Mnemonics.setLocalizedText(btnAddColumn, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.btnAddColumn.text")); // NOI18N
        btnAddColumn.setFocusable(false);
        btnAddColumn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnAddColumn);

        org.openide.awt.Mnemonics.setLocalizedText(btnEdit, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.btnEdit.text")); // NOI18N
        btnEdit.setFocusable(false);
        btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnEdit);

        org.openide.awt.Mnemonics.setLocalizedText(btnRemove, org.openide.util.NbBundle.getMessage(DdsAadcTransformEditor.class, "DdsAadcTransformEditor.btnRemove.text")); // NOI18N
        btnRemove.setFocusable(false);
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnRemove);

        treeView1.setMinimumSize(new java.awt.Dimension(300, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
            .addComponent(treeView1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(treeView1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbRequiredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRequiredActionPerformed
        boolean selected = cbRequired.isSelected();
        if (!selected && !transform.isEmpty()) {
            if (!DialogUtils.messageConfirmation("Are you sure that transformation is not required? All warnings and tables will be removed.")) {
                update();
                return;
            }
        }
        transform.setRequired(selected);
        update();
    }//GEN-LAST:event_cbRequiredActionPerformed
    boolean isUpdate = false;
    private void cbForbiddenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbForbiddenItemStateChanged
        if (isUpdate) {
            return;
        }
        isUpdate = true;
        boolean selected = cbForbidden.isSelected();
        if (selected) {
            if (!DialogUtils.messageConfirmation("Attention! During installation of this update to AADC cluster a complete system stop will be requried! Do you want to proceed?")) {
                updateCb();
                isUpdate = false;
                return;
            }
        }
        transform.setRequired(false);
        transform.setForbidden(selected);
        rbNotRequired.setSelected(!transform.isRequired());
        rbRequired.setSelected(transform.isRequired());
        updateCb();
        isUpdate = false;
    }//GEN-LAST:event_cbForbiddenItemStateChanged

    private void rbRequiredItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbRequiredItemStateChanged
        transform.setRequired(true);
        update();
    }//GEN-LAST:event_rbRequiredItemStateChanged

    private void rbNotRequiredItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbNotRequiredItemStateChanged
        boolean selected = rbNotRequired.isSelected();
        if (selected) {
            if (!transform.isEmpty()) {
                if (!DialogUtils.messageConfirmation("Are you sure that transformation is not required? All warnings and tables will be removed.")) {
                    rbRequired.setSelected(true);
                    return;
                }
            }
            transform.setRequired(false);
        }
        update();
    }//GEN-LAST:event_rbNotRequiredItemStateChanged

    private void updateCb() {
        cbForbidden.setSelected(transform.isForbidden());
        cbRequired.setSelected(transform.isRequired());
        if (cbForbidden.isSelected()) {
            treeView1.getRootNode().clear();
            setEnabled(false);
            cbForbidden.setEnabled(true);
        } else if (!cbRequired.isSelected()) {
            treeView1.getRootNode().clear();
            setEnabled(false);
            cbRequired.setEnabled(true);
            rbNotRequired.setEnabled(true);
            rbRequired.setEnabled(true);
            cbForbidden.setEnabled(true);
        } else {
            if (warningsNode.getParent() == null) {
                treeView1.getRootNode().add(warningsNode, false);
            }
            if (tablesNode.getParent() == null) {
                treeView1.getRootNode().add(tablesNode, false);
            }
            setEnabled(true);
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        btnAddWarning.setEnabled(enabled);
        btnAddTable.setEnabled(enabled);
        cbForbidden.setEnabled(enabled);
        cbRequired.setEnabled(enabled);
        treeView1.setEnabled(enabled);
        rbNotRequired.setEnabled(enabled);
        rbRequired.setEnabled(enabled);
        setEnabledOnSelection();
    }
    
    private void setEnabledOnSelection() {
        Node node = treeView1.getLastSelectedNode();
        boolean isEnabled = treeView1.isEnabled() && node != null && node != tablesNode && node != warningsNode;
        btnEdit.setEnabled(isEnabled);
        btnRemove.setEnabled(isEnabled);

        if (node == null) {
            btnAddColumn.setAction(new AddColumn(null, null, node));
            btnAddColumn.setEnabled(false);
            return;
        }

        Object obj = node.getUserObject();
        if (obj instanceof DdsAadcTransformTable) {
            DdsAadcTransformTable table = (DdsAadcTransformTable) obj;
            btnAddColumn.setAction(new AddColumn(table, table.findTable(layer), node));
            btnAddColumn.setEnabled(treeView1.isEnabled());
        } else {
            btnAddColumn.setAction(new AddColumn(null, null, node));
            btnAddColumn.setEnabled(false);
        }
    }

    @Override
    public void check() {
        if (rbNotRequired.isVisible() && rbRequired.isVisible() && !rbNotRequired.isSelected() && !rbRequired.isSelected()) {
            stateManager.error("Neither required nor not required option was selected");
            changeSupport.fireChange();
            return;
        }
        if (transform.isRequired() && transform.getTables().isEmpty() && transform.getWarnings().isEmpty()) {
            stateManager.error("Transform must contains table or warning");
            changeSupport.fireChange();
            return;
        }
        stateManager.ok();
        changeSupport.fireChange();
    }
    
    private class EditAction extends AbstractAction {
        
        public EditAction(){
            super("Edit", RadixWareIcons.EDIT.EDIT.getIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (transform == null) {
                return;
            }
            Node node = treeView1.getLastSelectedNode();
            if (node == null) {
                return;
            }
            Object obj = node.getUserObject();
            if (obj instanceof String) {
                if (transform == null) {
                    return;
                }
                int index = transform.getWarnings().indexOf(obj);
                if (index < 0) {
                    return;
                }
                DdsAadcWarningPanel warningEditor = new DdsAadcWarningPanel();
                warningEditor.setMessage((String) obj);
                if (showDialog(warningEditor, "Edit Warning", RadixObjectIcon.getForSeverity(EEventSeverity.WARNING))) {
                    String message = warningEditor.getMessage();
                    transform.getWarnings().set(index, message);
                    transform.setEditState(RadixObject.EEditState.MODIFIED);
                    node.setUserObject(message);
                    node.setDisplayName(message);
                }
            } else if (obj instanceof DdsAadcTransformTable) {
                DdsAadcTransformTable table = (DdsAadcTransformTable) obj;
                if (transform == null || layer == null) {
                    return;
                }
                DdsAadcTransformTablePanel tableEditor = new DdsAadcTransformTablePanel();
                tableEditor.open(layer, table);
                if (showDialog(tableEditor, "Edit Table", DdsDefinitionIcon.TABLE)) {
                    DdsAadcTransformTable t = tableEditor.getTable();
                    if (t != null) {
                        table.loadFrom(t);
                        updateTable(table, node);
                    }
                }
            } else if (obj instanceof DdsAadcTransformColumn) {
                DdsAadcTransformColumn column = (DdsAadcTransformColumn) obj;
                DdsAadcTransformTable table = column.getOwnerAadcTransformTable();
                DdsAadcTransformColumnPanel columnEditor = new DdsAadcTransformColumnPanel();
                columnEditor.open(layer, table == null? null : table.findTable(layer), column);
                if (showDialog(columnEditor, "Edit Column", DdsDefinitionIcon.COLUMN)) {
                    DdsAadcTransformColumn c = columnEditor.getColumn();
                    column.loadFrom(c);
                    node.setDisplayName(column.toString());
                }
            }
            node.reload();
        }
        
    }
    
    private class RemoveAction extends AbstractAction {

        public RemoveAction() {
            super("Remove", RadixWareIcons.DELETE.DELETE.getIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (transform == null) {
                return;
            }
            Node node = treeView1.getLastSelectedNode();
            if (node == null) {
                return;
            }
            Object obj = node.getUserObject();
            if (obj instanceof String) {
                transform.getWarnings().remove((String) obj);
                transform.setEditState(RadixObject.EEditState.MODIFIED);
                node.remove();
            } else if (obj instanceof DdsAadcTransformTable) {
                transform.getTables().remove((DdsAadcTransformTable)obj);
                node.remove();
            } else if (obj instanceof DdsAadcTransformColumn) {
                DdsAadcTransformColumn column = (DdsAadcTransformColumn) obj;
                DdsAadcTransformTable table = column.getOwnerAadcTransformTable();
                if (table != null) {
                    table.getColumns().remove(column);
                    node.remove();
                }
            }
            check();
        }
    }
    
    private class AddWarning extends AbstractAction {

        public AddWarning() {
            super("Add Warning", RadixObjectIcon.getForSeverity(EEventSeverity.WARNING).getIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (transform == null) {
                return;
            }
            DdsAadcWarningPanel warningEditor = new DdsAadcWarningPanel();
            if (showDialog(warningEditor, "New Warning", RadixObjectIcon.getForSeverity(EEventSeverity.WARNING))) {
                String message = warningEditor.getMessage();
                transform.getWarnings().add(message);
                transform.setEditState(RadixObject.EEditState.MODIFIED);
                addWarning(message);
            }
        }
    }
    
    private class AddColumn extends AbstractAction {
        private final DdsAadcTransformTable aadcTransformTable;
        private final DdsTableDef table;
        private final Node parentNode;

        public AddColumn(DdsAadcTransformTable aadcTransformTable, DdsTableDef table, Node node) {
            super("Add Column", DdsDefinitionIcon.COLUMN.getIcon());
            this.table = table;
            this.aadcTransformTable = aadcTransformTable;
            this.parentNode = node;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table == null || aadcTransformTable == null) {
                return;
            }
            DdsAadcTransformColumnPanel columnEditor = new DdsAadcTransformColumnPanel();
            columnEditor.open(layer, table, null);
            if (showDialog(columnEditor, "New Column", DdsDefinitionIcon.COLUMN)) {
                DdsAadcTransformColumn column = columnEditor.getColumn();
                if (column != null) {
                    aadcTransformTable.getColumns().add(column);
                    if (parentNode != null) {
                        addColumn(parentNode, column);
                    }
                }
            }
        }
    }
    
    private class AddTable extends AbstractAction {

        public AddTable() {
            super("Add Table", DdsDefinitionIcon.TABLE.getIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (transform == null || layer == null) {
                return;
            }
            DdsAadcTransformTablePanel tableEditor = new DdsAadcTransformTablePanel();
            tableEditor.open(layer, null);
            if (showDialog(tableEditor, "New Table", DdsDefinitionIcon.TABLE)) {
                DdsAadcTransformTable t = tableEditor.getTable();
                if (t != null) {
                    transform.getTables().add(t);
                    addTable(t);
                }
            }
        }
    }
    
    private class TransformNode extends Node {

        public TransformNode(Object userObject) {
            super(userObject);
        }

        @Override
        protected void beforePaint() {
            Object obj = getUserObject();
            if (obj instanceof DdsAadcTransformTable) {
                if (((DdsAadcTransformTable) obj).findTable(layer) == null) {
                    setColor(Color.red);
                } else {
                    setColor(treeView1.getForeground());
                }
            }
            if (obj instanceof DdsAadcTransformColumn) {
                if (((DdsAadcTransformColumn) obj).findColumn(layer) == null) {
                    setColor(Color.red);
                } else {
                    setColor(treeView1.getForeground());
                }
            }
        }
        
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddColumn;
    private javax.swing.JButton btnAddTable;
    private javax.swing.JButton btnAddWarning;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRemove;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cbForbidden;
    private javax.swing.JCheckBox cbRequired;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JRadioButton rbNotRequired;
    private javax.swing.JRadioButton rbRequired;
    private org.radixware.kernel.common.components.TreeView treeView1;
    // End of variables declaration//GEN-END:variables
}
