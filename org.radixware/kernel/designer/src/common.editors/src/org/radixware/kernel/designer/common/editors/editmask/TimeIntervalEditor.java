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

package org.radixware.kernel.designer.common.editors.editmask;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ToolTipManager;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;


class TimeIntervalEditor extends Editor {

    private class ScaleComboBoxModel extends DefaultComboBoxModel {

        @Override
        public int getSize() {
            return EditMaskTimeInterval.Scale.values().length;
        }

        @Override
        public Object getElementAt(int index) {
            return EditMaskTimeInterval.Scale.values()[index];
        }
    }
    private boolean readOnly = false;
    private final int defaultTooltipDelay = ToolTipManager.sharedInstance().getDismissDelay();

    /** Creates new form TimeIntervalEditor */
    public TimeIntervalEditor(EditMaskTimeInterval editMaskTimeInterval) {
        initComponents();

        qtMaskTextField.setToolTipText(
                //"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">" +
                "<html>"
                + //"<head>" +
                //"<title></title>" +
                //"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                //"</head>" +
                //"<body>" +
                "<table align=\"center\" border=\"1\" cellpadding=\"2\" cellspacing=\"0\">"
                + "<thead><tr align=\"center\" bgcolor=\"#CCCCCC\"><th>Expression</th><th>Output</th></tr></thead>"
                + "<tbody>"
                + "<tr align=\"left\"><td>d</td><td>the days</td></tr>"
                + "<tr align=\"left\"><td>h</td><td>the hours</td></tr>"
                + "<tr align=\"left\"><td>m</td><td>the minutes</td></tr>"
                + "<tr align=\"left\"><td>s</td><td>the seconds</td></tr>"
                + "<tr align=\"left\"><td>z</td><td>the milliseconds</td></tr>"
                + "</tbody></table>"
                + //"</body>" +
                "</html>");

        scaleComboBox.setModel(new ScaleComboBoxModel());
        minValueSpinner.setEditor(new CheckedNumberSpinnerEditor(minValueSpinner));
        maxValueSpinner.setEditor(new CheckedNumberSpinnerEditor(maxValueSpinner));

        if (editMaskTimeInterval != null) {
            setupInitialValues(editMaskTimeInterval);
        } else {
            scaleComboBox.setSelectedItem(EditMaskTimeInterval.Scale.SECOND);
        }
        updateLabels();
        updateEnableState();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private String getDefaultMask(EditMaskTimeInterval.Scale scale) {
        /* chenged by yremiozv: sync with explorer default mask
        switch (scale) {
            case MILLIS:
                return "hh:mm:ss:zzz";
            case SECOND:
                return "hh:mm:ss";
            case MINUTE:
                return "hh:mm";
            case HOUR:
                return "hh";
            default:
                return "";
        }*/
        return scale.defaultMask();
    }

    private String getScaleName() {
        switch ((EditMaskTimeInterval.Scale) scaleComboBox.getSelectedItem()) {
            case MILLIS:
                return " in milliseconds";
            case SECOND:
                return " in seconds";
            case MINUTE:
                return " in minutes";
            case HOUR:
                return " in hours";
            default:
                return "";
        }
    }

    private void updateLabels() {
        minValueCheckBox.setText("Minimum value" + getScaleName() + ":");
        maxValueCheckBox.setText("Maximum value" + getScaleName() + ":");
    }

    private void setupInitialValues(EditMaskTimeInterval editMaskTimeInterval) {
        scaleComboBox.setSelectedItem(editMaskTimeInterval.getScale());
        if (editMaskTimeInterval.getMask() != null) {
            autoQtMaskCheckBox.setSelected(false);
            qtMaskTextField.setText(editMaskTimeInterval.getMask());
        } else {
            autoQtMaskCheckBox.setSelected(true);
            qtMaskTextField.setText(getDefaultMask(editMaskTimeInterval.getScale()));
        }
        if (editMaskTimeInterval.getMinValue() != null) {
            minValueCheckBox.setSelected(true);
            minValueSpinner.setValue(editMaskTimeInterval.getMinValue());
        } else {
            minValueCheckBox.setSelected(false);
        }
        if (editMaskTimeInterval.getMaxValue() != null) {
            maxValueCheckBox.setSelected(true);
            maxValueSpinner.setValue(editMaskTimeInterval.getMaxValue());
        } else {
            maxValueCheckBox.setSelected(false);
        }

    }

    private void updateEnableState() {
        qtMaskLabel.setEnabled(!readOnly);
        autoQtMaskCheckBox.setEnabled(!readOnly);
        qtMaskTextField.setEnabled(!readOnly && !autoQtMaskCheckBox.isSelected());
        minValueCheckBox.setEnabled(!readOnly);
        minValueSpinner.setEnabled(!readOnly && minValueCheckBox.isSelected());
        maxValueCheckBox.setEnabled(!readOnly);
        maxValueSpinner.setEnabled(!readOnly && maxValueCheckBox.isSelected());
        scaleLabel.setEnabled(!readOnly);
        scaleComboBox.setEnabled(!readOnly);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        qtMaskLabel = new javax.swing.JLabel();
        autoQtMaskCheckBox = new javax.swing.JCheckBox();
        qtMaskTextField = new javax.swing.JTextField();
        minValueCheckBox = new javax.swing.JCheckBox();
        minValueSpinner = new javax.swing.JSpinner();
        maxValueCheckBox = new javax.swing.JCheckBox();
        maxValueSpinner = new javax.swing.JSpinner();
        scaleLabel = new javax.swing.JLabel();
        scaleComboBox = new javax.swing.JComboBox();

        qtMaskLabel.setText(org.openide.util.NbBundle.getMessage(TimeIntervalEditor.class, "TimeIntervalEditor.qtMaskLabel.text")); // NOI18N

        autoQtMaskCheckBox.setText(org.openide.util.NbBundle.getMessage(TimeIntervalEditor.class, "TimeIntervalEditor.autoQtMaskCheckBox.text")); // NOI18N
        autoQtMaskCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoQtMaskCheckBoxItemStateChanged(evt);
            }
        });

        qtMaskTextField.setText(org.openide.util.NbBundle.getMessage(TimeIntervalEditor.class, "TimeIntervalEditor.qtMaskTextField.text")); // NOI18N
        qtMaskTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                qtMaskTextFieldMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                qtMaskTextFieldMouseExited(evt);
            }
        });

        minValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                minValueCheckBoxItemStateChanged(evt);
            }
        });

        minValueSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), null, null, Long.valueOf(1L)));

        maxValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                maxValueCheckBoxItemStateChanged(evt);
            }
        });

        maxValueSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), null, null, Long.valueOf(1L)));

        scaleLabel.setText(org.openide.util.NbBundle.getMessage(TimeIntervalEditor.class, "TimeIntervalEditor.scaleLabel.text")); // NOI18N

        scaleComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        scaleComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scaleComboBoxItemStateChanged(evt);
            }
        });
        scaleComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scaleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scaleComboBox, 0, 209, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(autoQtMaskCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(qtMaskTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                            .addComponent(qtMaskLabel))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(maxValueSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                                .addComponent(maxValueCheckBox)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(minValueSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(minValueCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scaleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scaleLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(qtMaskLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoQtMaskCheckBox)
                    .addComponent(qtMaskTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(minValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minValueSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(maxValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxValueSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void autoQtMaskCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_autoQtMaskCheckBoxItemStateChanged
        if (autoQtMaskCheckBox.isSelected()) {
            qtMaskTextField.setText(getDefaultMask((EditMaskTimeInterval.Scale) scaleComboBox.getSelectedItem()));
        }
        updateEnableState();
    }//GEN-LAST:event_autoQtMaskCheckBoxItemStateChanged

    private void minValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_minValueCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_minValueCheckBoxItemStateChanged

    private void maxValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_maxValueCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_maxValueCheckBoxItemStateChanged

    private void scaleComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scaleComboBoxItemStateChanged
        updateLabels();
        if (autoQtMaskCheckBox.isSelected()) {
            qtMaskTextField.setText(getDefaultMask((EditMaskTimeInterval.Scale) scaleComboBox.getSelectedItem()));
        }
    }//GEN-LAST:event_scaleComboBoxItemStateChanged

    private void qtMaskTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtMaskTextFieldMouseEntered
        ToolTipManager.sharedInstance().setDismissDelay(60000);
    }//GEN-LAST:event_qtMaskTextFieldMouseEntered

    private void qtMaskTextFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtMaskTextFieldMouseExited
        ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDelay);
    }//GEN-LAST:event_qtMaskTextFieldMouseExited

    private void scaleComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleComboBoxActionPerformed
        if (((EditMaskTimeInterval.Scale) scaleComboBox.getSelectedItem()) == EditMaskTimeInterval.Scale.NONE) {
            scaleComboBox.setSelectedItem(EditMaskTimeInterval.Scale.SECOND);
        }
    }//GEN-LAST:event_scaleComboBoxActionPerformed

    @Override
    public void apply(EditMask editMask) {
        if (!(editMask instanceof EditMaskTimeInterval)) {
            return;
        }
        EditMaskTimeInterval editMaskTimeInterval = (EditMaskTimeInterval) editMask;

        if (autoQtMaskCheckBox.isSelected()) {
            editMaskTimeInterval.setMask(null);
        } else {
            editMaskTimeInterval.setMask(qtMaskTextField.getText());
        }
        if (minValueCheckBox.isSelected()) {
            editMaskTimeInterval.setMinValue((Long) minValueSpinner.getValue());
        } else {
            editMaskTimeInterval.setMinValue(null);
        }
        if (maxValueCheckBox.isSelected()) {
            editMaskTimeInterval.setMaxValue((Long) maxValueSpinner.getValue());
        } else {
            editMaskTimeInterval.setMaxValue(null);
        }
        editMaskTimeInterval.setScale((EditMaskTimeInterval.Scale) scaleComboBox.getSelectedItem());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoQtMaskCheckBox;
    private javax.swing.JCheckBox maxValueCheckBox;
    private javax.swing.JSpinner maxValueSpinner;
    private javax.swing.JCheckBox minValueCheckBox;
    private javax.swing.JSpinner minValueSpinner;
    private javax.swing.JLabel qtMaskLabel;
    private javax.swing.JTextField qtMaskTextField;
    private javax.swing.JComboBox scaleComboBox;
    private javax.swing.JLabel scaleLabel;
    // End of variables declaration//GEN-END:variables
}
