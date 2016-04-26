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
 * DemoFormatPanel.java
 *
 * Created on Apr 7, 2009, 2:44:19 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.Date;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.TitleItemFormatter;
import org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel;


public class DemoFormatPanel extends javax.swing.JPanel implements ChangeListener, CaretListener {

    private EValType valType;
    private String groupSeparator_;
    private int groupSize_;

    /**
     * Creates new form DemoFormatPanel
     */
    public DemoFormatPanel(EValType valType, DdsTableDef targetTable, String groupSeparator, int groupSize) {
        super();
        groupSeparator_ = groupSeparator;
        groupSize_ = groupSize;
        initComponents();
        if (valType != null) {
            sourceEditor.addChangeListener(this);
        }
        this.valType = valType;

        if (valType == EValType.BOOL) {
            patternTextField.setText("{0}");
            sourceEditor.setValue(valType, ValAsStr.Factory.newInstance(Boolean.TRUE, valType));
        } else if (valType == EValType.CHAR) {
            patternTextField.setText("{0}");
            sourceEditor.setValue(valType, ValAsStr.Factory.newInstance(java.lang.Character.valueOf('a'), valType));
        } else if (valType == EValType.STR) {
            patternTextField.setText("{0}");
            sourceEditor.setValue(valType, ValAsStr.Factory.newInstance(String.valueOf("test"), valType));
        } else if (valType == EValType.INT || valType == EValType.NUM) {
            patternTextField.setText("{0,number}");
            sourceEditor.setValue(valType, ValAsStr.Factory.newInstance(Long.valueOf(123), valType));
        } else if (valType == EValType.DATE_TIME) {
            patternTextField.setText("test {0, date}");
            sourceEditor.setValue(valType, ValAsStr.Factory.newInstance(new Date(), valType));
        } else if (valType == EValType.PARENT_REF) {
            if (targetTable == null) {
                return;
            }
            patternTextField.setText("{0}");
            sourceEditor.setValue(valType, targetTable, null);
        } else {
            patternTextField.setText("{0}");
            sourceEditor.setValue(valType, null);
        }

        updateResult();
    }

    public void setPatternValue(String value) {
        patternTextField.setText(value);
        updateResult();
    }

    public void setPatternValue(String value, String groupSeparator, int groupSize) {
        patternTextField.setText(value);
        groupSeparator_ = groupSeparator;
        groupSize_ = groupSize;
        updateResult();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        updateResult();
    }

    private void updateResult() {

        final ValAsStr sourceValAsStr = sourceEditor.getValue();
        try {
            boolean isEmptyValue = sourceValAsStr == null
                    || (valType.equals(EValType.DATE_TIME)
                    && (sourceEditor.getValue() == null || sourceEditor.getValue().toString().isEmpty()));
//                                   sourceEditor.getVisibleValue().toString().isEmpty());

            final Object source = isEmptyValue ? "" : valType == EValType.PARENT_REF ? sourceValAsStr.toString() : sourceValAsStr.toObject(valType);
            final String pattern = patternTextField.getText();

            if (groupSeparator_ != null) {
                resultTextField.setText(TitleItemFormatter.format(pattern, source, groupSeparator_, groupSize_));
            } else {
                resultTextField.setText(TitleItemFormatter.format(pattern, source));
            }
            currentError = "";
        } catch (NumberFormatException e) {
            resultTextField.setText("");
            currentError = "";
        } catch (IllegalArgumentException ex) {
            resultTextField.setText("");
            currentError = ex.getLocalizedMessage();
        }
    }

    public String getCurrentError() {
        return this.currentError;
    }
    private String currentError = "";

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        patternTextField = new javax.swing.JTextField();
        resultTextField = new javax.swing.JTextField();
        sourceEditor = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DemoFormatPanel.class, "DemoFormatPanel.border.title"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(32767, 70));
        setMinimumSize(new java.awt.Dimension(0, 70));
        setPreferredSize(new java.awt.Dimension(480, 70));

        patternTextField.setEditable(false);
        patternTextField.setText(org.openide.util.NbBundle.getMessage(DemoFormatPanel.class, "DemoFormatPanel.patternTextField.text")); // NOI18N

        resultTextField.setEditable(false);
        resultTextField.setText(org.openide.util.NbBundle.getMessage(DemoFormatPanel.class, "DemoFormatPanel.resultTextField.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sourceEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(patternTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resultTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    //private  String groupSeparator;
    @Override
    public void stateChanged(ChangeEvent e) {

        final Object source = e.getSource();
        assert (source instanceof FormatStringEditorPanel || source instanceof ValAsStrEditPanel);

        if (source instanceof FormatStringEditorPanel) {

            final FormatStringEditorPanel formatStringEditorPanel = (FormatStringEditorPanel) source;

            if (formatStringEditorPanel.isOk()) {
                final String pattern = formatStringEditorPanel.getPattern();
                groupSeparator_ = formatStringEditorPanel.getGroupSeparator();
                groupSize_ = formatStringEditorPanel.getGroupSize();
                if (pattern != null) {
                    patternTextField.setText(pattern);
                    updateResult();
                }
            }
        } /*else if(source instanceof FormatGroupSeparatorPanel){
         final FormatGroupSeparatorPanel formatStringEditorPanel = (FormatGroupSeparatorPanel) source;

         if (formatStringEditorPanel.isSetGroupSeparator()) {
         //final String pattern = formatStringEditorPanel.getPattern();
         groupSeparator_= formatStringEditorPanel.getGroupSeparator();
         groupSize_= formatStringEditorPanel.getGroupSize();
         //if (pattern != null) {
         //   patternTextField.setText(pattern);
         updateResult();
         //}       
         }
         }*/ else {
            updateResult();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField patternTextField;
    private javax.swing.JTextField resultTextField;
    private org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel sourceEditor;
    // End of variables declaration//GEN-END:variables
}
