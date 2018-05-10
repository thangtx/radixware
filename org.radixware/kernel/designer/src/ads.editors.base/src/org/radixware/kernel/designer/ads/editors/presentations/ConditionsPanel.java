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

/*
 * ConditionsPanel.java
 *
 * Created on 24.06.2009, 16:02:53
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JTabbedPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.common.IConditionProvider;
import org.radixware.kernel.common.defs.ads.common.Prop2ValueMap;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;

public class ConditionsPanel extends javax.swing.JPanel {

    /**
     * Creates new form ConditionsPanel
     */
    public ConditionsPanel() {
        initComponents();
        defaultForeground = inherit.getForeground();
        ActionListener inheritListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConditionsPanel.this.onInheritChange();
            }
        };
        inherit.addActionListener(inheritListener);

        //  fromPanel.setBorder(BorderFactory.createEtchedBorder());
        //   wherePanel.setBorder(BorderFactory.createEtchedBorder());
        tabs = new JTabbedPane();
        tabs.add(NbBundle.getMessage(ConditionsPanel.class, "ConditionWhere"), wherePanel);
        tabs.add(NbBundle.getMessage(ConditionsPanel.class, "ConditionFrom"), fromPanel);
        tabs.add(NbBundle.getMessage(ConditionsPanel.class, "ConditionProps"), propValMapPanel);
        fromPanel.getPane().getDocument().addDocumentListener(docListener);
        wherePanel.getPane().getDocument().addDocumentListener(docListener);
        // tabs.setBorder(BorderFactory.createEmptyBorder());

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        //  .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inherit)
                                //.addComponent(fromLabel)
                                .addComponent(tabs) //.addComponent(whereLabel)
                        //.addComponent(wherePanel)
                        //  .addComponent(propsLabel)
                        //.addComponent(propValMapPanel)
                        )
                //                .addContainerGap()
                ));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        //      .addContainerGap()
                        .addComponent(inherit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        //.addComponent(fromLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, fromPanel.getPreferredSize().height, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                //.addComponent(whereLabel)
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                //.addComponent(wherePanel, javax.swing.GroupLayout.DEFAULT_SIZE, fromPanel.getPreferredSize().height, Short.MAX_VALUE)
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                // .addComponent(propsLabel)
                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                // .addComponent(propValMapPanel)
                //.addContainerGap()
                ));
    }
    private javax.swing.JCheckBox inherit = new javax.swing.JCheckBox(NbBundle.getMessage(ConditionsPanel.class, "InheritConditions"));
    private javax.swing.JLabel fromLabel = new javax.swing.JLabel(NbBundle.getMessage(ConditionsPanel.class, "ConditionFrom"));
    private javax.swing.JLabel whereLabel = new javax.swing.JLabel(NbBundle.getMessage(ConditionsPanel.class, "ConditionWhere"));
    private javax.swing.JLabel propsLabel = new javax.swing.JLabel(NbBundle.getMessage(ConditionsPanel.class, "ConditionProps"));
    private SqmlEditorPanel fromPanel = new SqmlEditorPanel();
    private SqmlEditorPanel wherePanel = new SqmlEditorPanel();
    private ConditionPropValMapPanel propValMapPanel = new ConditionPropValMapPanel();
    private Color defaultForeground;
    private IConditionProvider presentation;
    private JTabbedPane tabs;
    private final DocumentListener docListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }
        
        private void update(){
            if (presentation != null) {
                updateColor();
            }
        }
    };

    public void open(final IConditionProvider presentation) {
        this.presentation = presentation;
        propValMapPanel.open(presentation.getCondition().getProp2ValueMap());
        update();
    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        if (presentation.canInheritCondition()) {
            inherit.setVisible(true);
            boolean isInherited = presentation.isConditionInherited();
            inherit.setSelected(isInherited);
            boolean erronious = presentation.isConditionInheritanceErrorneous();
            if (erronious) {
                inherit.setForeground(Color.RED);
            } else {
                inherit.setForeground(defaultForeground);
            }
            if (isInherited) {
                inherit.setEnabled(!presentation.isReadOnly());
            } else {
                inherit.setEnabled(!presentation.isReadOnly() && !erronious);
            }
        } else {
            inherit.setVisible(false);
        }

        updateConditions();
        isUpdate = false;
    }
    private boolean externalReadOnly = false;
    
    private void updateColor(){
        Sqml sqml = presentation.getCondition().getFrom();
        if (sqml == null || sqml.getItems().isEmpty()){
            tabs.setForegroundAt(1, Color.GRAY);
        } else {
            tabs.setForegroundAt(1, tabs.getForeground());
        }
        sqml = presentation.getCondition().getWhere();
        if (sqml == null || sqml.getItems().isEmpty()){
            tabs.setForegroundAt(0, Color.GRAY);
        } else {
            tabs.setForegroundAt(0, tabs.getForeground());
        }
        Prop2ValueMap prop2ValueMap = presentation.getCondition().getProp2ValueMap();
        if (prop2ValueMap == null || prop2ValueMap.getItems().isEmpty()){
            tabs.setForegroundAt(2, Color.GRAY);
        } else {
            tabs.setForegroundAt(2, tabs.getForeground());
        }
    }

    private void updateConditions() {
        fromPanel.open(presentation.getCondition().getFrom());
        wherePanel.open(presentation.getCondition().getWhere());
        boolean reaonly = presentation.isReadOnly() || externalReadOnly;
        boolean inherited = presentation.isConditionInherited();
        fromPanel.setEditable(!reaonly && !inherited);
        wherePanel.setEditable(!reaonly && !inherited);
        if (!propValMapPanel.setReadOnly(reaonly || inherited)) {
            propValMapPanel.update();
        }
        updateColor();
    }

    public void setReadOnly(boolean readOnly) {
        externalReadOnly = readOnly;
    }

    private void onInheritChange() {
        if (!isUpdate) {
            boolean isSelected = inherit.isSelected();
            if (presentation != null) {
                presentation.setConditionInherited(isSelected);
                update();
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ConditionsPanel.class, "ConditionLabel");
    }

    @Override
    public void removeNotify() {
        fromPanel.getPane().getDocument().removeDocumentListener(docListener);
        wherePanel.getPane().getDocument().removeDocumentListener(docListener);
        super.removeNotify();
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
