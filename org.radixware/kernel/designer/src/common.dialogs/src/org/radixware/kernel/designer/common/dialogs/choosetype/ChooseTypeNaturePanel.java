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
 * ChooseTypeNaturePanel.java
 *
 * Created on Mar 25, 2009, 11:49:47 AM
 */
package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;


public class ChooseTypeNaturePanel extends javax.swing.JPanel
    implements ItemListener {

    private ButtonGroup group = new ButtonGroup();

    public ChooseTypeNaturePanel() {
        initComponents();
        setupUI();
        setupDimEditor();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ChooseTypeNaturePanel.class, "TypeNaturePanel-Step1-Name");
    }
    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (e.getSource() instanceof JRadioButton) {
                    String action = ((JRadioButton) e.getSource()).getActionCommand();
                    ChooseTypeNaturePanel.this.getDoubleClickEventSupport().fireEvent(new DoubleClickEvent(action));
                }
            }
            super.mouseReleased(e);
        }
    };

    private void setupUI() {
        radixTypeBtn.addItemListener(this);
        javaPTypeBtn.addItemListener(this);
        javaClassBtn.addItemListener(this);
        javaArrBtn.addItemListener(this);
        paramCheck.addItemListener(this);

        radixTypeBtn.addMouseListener(mouseAdapter);
        javaPTypeBtn.addMouseListener(mouseAdapter);
        javaClassBtn.addMouseListener(mouseAdapter);
        javaArrBtn.addMouseListener(mouseAdapter);
        paramCheck.addMouseListener(mouseAdapter);

        radixTypeBtn.setActionCommand(ETypeNature.RADIX_TYPE.getNatureName());
        javaPTypeBtn.setActionCommand(ETypeNature.JAVA_PRIMITIVE.getNatureName());
        javaClassBtn.setActionCommand(ETypeNature.JAVA_CLASS.getNatureName());
        javaArrBtn.setActionCommand(ETypeNature.JAVA_ARRAY.getNatureName());
        paramCheck.setActionCommand(ETypeNature.TYPE_PARAMETER.getNatureName());

        group.add(radixTypeBtn);
        group.add(javaPTypeBtn);
        group.add(javaClassBtn);
        group.add(javaArrBtn);
        group.add(paramCheck);

        enableDimEditing(false);
        paramCheck.setEnabled(false);
    }

    private void setupDimEditor() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, Short.MAX_VALUE, 1);
        dimEditor.setModel(model);
        dimEditor.setEditor(new CheckedNumberSpinnerEditor(dimEditor));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            changeSupport.fireChange();
            if (getSelectedTypeNature().equals(ETypeNature.JAVA_ARRAY)) {
                dimEditor.setEnabled(true);
                dimLabel.setEnabled(true);
            } else {
                dimEditor.setEnabled(false);
                dimLabel.setEnabled(false);
            }
            ((JComponent)e.getSource()).requestFocusInWindow();
        }
    }

    ETypeNature getSelectedTypeNature() {
        return ETypeNature.getByName(group.getSelection().getActionCommand());
    }

//    @Deprecated
//    private void setSelectedTypeNature(ETypeNature nature) {
//        switch (nature) {
//            case RADIX_TYPE:
//                radixTypeBtn.setSelected(true);
//                break;
//            case JAVA_PRIMITIVE:
//                javaPTypeBtn.setSelected(true);
//                break;
//            case JAVA_CLASS:
//                javaClassBtn.setSelected(true);
//                break;
//            case JAVA_ARRAY:
//                javaArrBtn.setSelected(true);
//                break;
//            case TYPE_PARAMETER:
//                paramCheck.setSelected(true);
//                break;
//        }
//    }

    public Integer getArrayDimension() {
        return (Integer) dimEditor.getValue();
    }

    public void enableDimEditing(boolean enable) {
        dimLabel.setEnabled(enable);
        dimEditor.setEnabled(enable);
    }

    public void enableArrayButton(boolean enable) {
        javaArrBtn.setEnabled(enable);
        if (!enable) {
            radixTypeBtn.setSelected(true);
        }
    }

    public void enableJavaPrimitives(boolean enable) {
        javaPTypeBtn.setEnabled(enable);
    }

    public void enableTypeParameter(boolean enable) {
        paramCheck.setEnabled(enable);
    }

    public void enableJavaClass(boolean enable) {
        javaClassBtn.setEnabled(enable);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javaPTypeBtn = new javax.swing.JRadioButton();
        javaClassBtn = new javax.swing.JRadioButton();
        javaArrBtn = new javax.swing.JRadioButton();
        dimEditor = new javax.swing.JSpinner();
        dimLabel = new javax.swing.JLabel();
        radixTypeBtn = new javax.swing.JRadioButton();
        paramCheck = new javax.swing.JRadioButton();

        javaPTypeBtn.setText(org.openide.util.NbBundle.getMessage(ChooseTypeNaturePanel.class, "ETypeNature-JavaType")); // NOI18N

        javaClassBtn.setText(org.openide.util.NbBundle.getMessage(ChooseTypeNaturePanel.class, "ETypeNature-JavaClass")); // NOI18N

        javaArrBtn.setText(org.openide.util.NbBundle.getMessage(ChooseTypeNaturePanel.class, "ETypeNature-JavaArray")); // NOI18N

        dimEditor.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        dimEditor.setEnabled(false);

        dimLabel.setText(org.openide.util.NbBundle.getMessage(ChooseTypeNaturePanel.class, "ChooseType-Common-Dimension")); // NOI18N
        dimLabel.setEnabled(false);

        radixTypeBtn.setSelected(true);
        radixTypeBtn.setText(org.openide.util.NbBundle.getMessage(ChooseTypeNaturePanel.class, "ETypeNature-RadixType")); // NOI18N

        paramCheck.setText(org.openide.util.NbBundle.getMessage(ChooseTypeNaturePanel.class, "ETypeNature-TypeParameter")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radixTypeBtn)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(dimLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dimEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(javaArrBtn)
                    .addComponent(javaPTypeBtn)
                    .addComponent(javaClassBtn)
                    .addComponent(paramCheck))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radixTypeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(javaPTypeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(javaClassBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(javaArrBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dimLabel)
                    .addComponent(dimEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paramCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner dimEditor;
    private javax.swing.JLabel dimLabel;
    private javax.swing.JRadioButton javaArrBtn;
    private javax.swing.JRadioButton javaClassBtn;
    private javax.swing.JRadioButton javaPTypeBtn;
    private javax.swing.JRadioButton paramCheck;
    private javax.swing.JRadioButton radixTypeBtn;
    // End of variables declaration//GEN-END:variables

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private ChangeSupport changeSupport = new ChangeSupport(group);
    private DoubleClickEventSupport doubleClickSupport;

    public static class DoubleClickEvent extends RadixEvent {

        public String selection = "";

        DoubleClickEvent(String selection) {
            this.selection = selection;
        }
    }

    public interface IDoubleClickEventListener extends IRadixEventListener<DoubleClickEvent> {
    }

    public static class DoubleClickEventSupport extends RadixEventSource<IDoubleClickEventListener, DoubleClickEvent> {
    }

    public DoubleClickEventSupport getDoubleClickEventSupport() {
        if (this.doubleClickSupport == null) {
            this.doubleClickSupport = new DoubleClickEventSupport();
        }
        return this.doubleClickSupport;
    }
}
