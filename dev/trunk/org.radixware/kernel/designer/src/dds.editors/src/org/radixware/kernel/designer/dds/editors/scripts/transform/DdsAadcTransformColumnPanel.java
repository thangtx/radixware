/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.dds.editors.scripts.transform;

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAadcTransformColumnSourceType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsAadcTransformColumn;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;

/**
 *
 * @author avoloshchuk
 */
public class DdsAadcTransformColumnPanel extends StateAbstractDialog.StateAbstractPanel {
    
    private DdsAadcTransformColumn column;

    /**
     * Creates new form DdsAadcColumnPanel
     */
    public DdsAadcTransformColumnPanel() {
        initComponents();
        setLayout(new MigLayout("hidemode 3", "[shrink][grow]", "[shrink][shrink][shrink][grow, align top]"));
        sourceType.setEditorType(ExtendableTextField.EDITOR_COMBO);
        sourceType.setComboBoxModel(new javax.swing.DefaultComboBoxModel(EAadcTransformColumnSourceType.values()));
        sourceNamePanel.setPlaceholder(org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "Source.placeholder"));
        sourceNamePanel.setVisibleCopyButton(false);
        add(jLabel1, "shrink");
        add(definitionLinkEditPanel1, "shrinky, growx, wrap");
        add(jLabel2, "shrink");
        add(targetNamePanel, "shrinky, growx, wrap");
        add(jLabel3, "shrink");
        add(sourceType, "shrinky, growx, wrap");
        add(sourceLabel, "shrink");
        add(sourceNamePanel, "shrinky, growx");
        add(valueLabel, "shrink");
        add(sourceConstantPanel, "shrinky, growx");
        add(oggExLabel, "shrink");
        add(jScrollPane1, "grow");
        
    }
    
    public void open(Layer layer, DdsTableDef table, DdsAadcTransformColumn c){
        column = new DdsAadcTransformColumn();
        DdsColumnDef def = null;
        if (c != null) {
            this.column.loadFrom(c);
            def = c.findColumn(layer);
            
        }
        definitionLinkEditPanel1.open(ChooseDefinitionCfg.Factory.newInstance(table == null ? new ArrayList<Definition>() : table.getColumns().get(ExtendableDefinitions.EScope.ALL)), def, column.getTargetId());
        definitionLinkEditPanel1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Definition def = definitionLinkEditPanel1.getDefinition();
                if (def == null) {
                    column.setTargetDbName("");
                    column.setTargetId(null);
                } else if (def instanceof DdsColumnDef) {
                    column.setTargetDbName(((DdsColumnDef) def).getDbName());
                    column.setTargetId(def.getId());
                }
                update();
            }
        });
        sourceType.setValue(column.getType());
        sourceType.addChangeListener(new ExtendableTextField.ExtendableTextChangeListener() {

            @Override
            public void onEvent(ExtendableTextField.ExtendableTextChangeEvent e) {
                Object obj = sourceType.getValue();
                if (obj instanceof EAadcTransformColumnSourceType) {
                    column.setType((EAadcTransformColumnSourceType) obj);
                }
                update();
            }
        });
        sourceNamePanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                column.setSource(sourceNamePanel.getDbName());
            }
        });
        sourceConstantPanel.addChangeListener(new ExtendableTextField.ExtendableTextChangeListener() {
            @Override
            public void onEvent(ExtendableTextField.ExtendableTextChangeEvent e) {
                column.setSource((String) sourceConstantPanel.getValue());
            }
        });
        jEditorPane1.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                column.setSource(jEditorPane1.getText());
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                column.setSource(jEditorPane1.getText());
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                column.setSource(jEditorPane1.getText());
                check();
            }
        });
        targetNamePanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                column.setTargetDbName(targetNamePanel.getDbName());
                check();
            }
        });
        update();
    }
    
    private void update() {
        if (column.getTargetDbName() != null) {
            targetNamePanel.setDbName(column.getTargetDbName());
        }
        sourceNamePanel.setVisible(sourceType.getValue() == EAadcTransformColumnSourceType.COLUMN);
        sourceLabel.setVisible(sourceType.getValue() == EAadcTransformColumnSourceType.COLUMN);
        sourceConstantPanel.setVisible(sourceType.getValue() == EAadcTransformColumnSourceType.CONST);
        valueLabel.setVisible(sourceType.getValue() == EAadcTransformColumnSourceType.CONST);
        jScrollPane1.setVisible(sourceType.getValue() == EAadcTransformColumnSourceType.EXPRESSION);
        oggExLabel.setVisible(sourceType.getValue() == EAadcTransformColumnSourceType.EXPRESSION);
        if (sourceType.getValue() == EAadcTransformColumnSourceType.COLUMN) {
            if (column.getSource() != null) {
                sourceNamePanel.setDbName(column.getSource());
            }
        } else if (sourceType.getValue() == EAadcTransformColumnSourceType.CONST) {
            sourceConstantPanel.setValue(column.getSource());
        } else if (sourceType.getValue() == EAadcTransformColumnSourceType.EXPRESSION) {
            jEditorPane1.setText(column.getSource());
        }
        check();            
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        definitionLinkEditPanel1 = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        targetNamePanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        jLabel2 = new javax.swing.JLabel();
        sourceType = new org.radixware.kernel.common.components.ExtendableTextField();
        jLabel3 = new javax.swing.JLabel();
        sourceNamePanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        sourceLabel = new javax.swing.JLabel();
        sourceConstantPanel = new org.radixware.kernel.common.components.ExtendableTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        oggExLabel = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "DdsAadcTransformColumnPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "DdsAadcTransformColumnPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "DdsAadcTransformColumnPanel.jLabel3.text")); // NOI18N

        sourceNamePanel.setVisibleCopyButton(false);

        org.openide.awt.Mnemonics.setLocalizedText(sourceLabel, org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "DdsAadcTransformColumnPanel.sourceLabel.text")); // NOI18N

        jScrollPane1.setViewportView(jEditorPane1);

        org.openide.awt.Mnemonics.setLocalizedText(oggExLabel, org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "DdsAadcTransformColumnPanel.oggExLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(valueLabel, org.openide.util.NbBundle.getMessage(DdsAadcTransformColumnPanel.class, "DdsAadcTransformColumnPanel.valueLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2))
                            .addComponent(jLabel3))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(definitionLinkEditPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(targetNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addComponent(sourceType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sourceLabel)
                            .addComponent(oggExLabel)
                            .addComponent(valueLabel))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(sourceConstantPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sourceNamePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(definitionLinkEditPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(targetNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(sourceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sourceNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceConstantPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(oggExLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel definitionLinkEditPanel1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel oggExLabel;
    private org.radixware.kernel.common.components.ExtendableTextField sourceConstantPanel;
    private javax.swing.JLabel sourceLabel;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel sourceNamePanel;
    private org.radixware.kernel.common.components.ExtendableTextField sourceType;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel targetNamePanel;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void check() {
        if (definitionLinkEditPanel1.getDefinitionId() == null){
            stateManager.error("Choose target table");
            changeSupport.fireChange();
            return;
        }
        if (!targetNamePanel.isComplete()){
            stateManager.error("Invalid target table name");
            changeSupport.fireChange();
            return;
        }
        
        stateManager.ok();
        changeSupport.fireChange();
    }

    public DdsAadcTransformColumn getColumn() {
        return column;
    }
}
