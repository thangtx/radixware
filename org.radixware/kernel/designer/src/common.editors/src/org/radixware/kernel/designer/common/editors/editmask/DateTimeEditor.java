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
 * DateTimeEditor.java
 *
 * Created on Jul 28, 2009, 11:09:08 AM
 */
package org.radixware.kernel.designer.common.editors.editmask;

import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ToolTipManager;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskDateTime;
import org.radixware.kernel.common.enums.EDateTimeStyle;


class DateTimeEditor extends Editor {

    private boolean readOnly = false;
    private final int defaultTooltipDelay = ToolTipManager.sharedInstance().getDismissDelay();

    /**
     * Creates new form DateTimeEditor
     */
    public DateTimeEditor(EditMaskDateTime editMaskDateTime) {
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
            + "<tr align=\"left\"><td>d</td><td>the day as number without a leading zero (1 to 31)</td></tr>"
            + "<tr align=\"left\"><td>dd</td><td>the day as number with a leading zero (01 to 31)</td></tr>"
            + "<tr align=\"left\"><td>ddd</td><td>the abbreviated localized day name (e.g. 'Mon' to 'Sun')</td></tr>"
            + "<tr align=\"left\"><td>dddd</td><td>the long localized day name (e.g. 'Qt::Monday' to 'Qt::Sunday')</td></tr>"
            + "<tr align=\"left\"><td>M</td><td>the month as number without a leading zero (1-12)</td></tr>"
            + "<tr align=\"left\"><td>MM</td><td>the month as number with a leading zero (01-12)</td></tr>"
            + "<tr align=\"left\"><td>MMM</td><td>the abbreviated localized month name (e.g. 'Jan' to 'Dec')</td></tr>"
            + "<tr align=\"left\"><td>MMMM</td><td>the long localized month name (e.g. 'January' to 'December')</td></tr>"
            + "<tr align=\"left\"><td>yy</td><td>the year as two digit number (00-99)</td></tr>"
            + "<tr align=\"left\"><td>yyyy</td><td>the year as four digit number</td></tr>"
            + "<tr align=\"left\"><td>h</td><td>the hour without a leading zero (0 to 23 or 1 to 12 if AM/PM display)</td></tr>"
            + "<tr align=\"left\"><td>hh</td><td>the hour with a leading zero (00 to 23 or 01 to 12 if AM/PM display)</td></tr>"
            + "<tr align=\"left\"><td>H</td><td>the hour without a leading zero (0 to 23, even with AM/PM display)</td></tr>"
            + "<tr align=\"left\"><td>HH</td><td>the hour with a leading zero (00 to 23, even with AM/PM display)</td></tr>"
            + "<tr align=\"left\"><td>m</td><td>the minute without a leading zero (0 to 59)</td></tr>"
            + "<tr align=\"left\"><td>mm</td><td>the minute with a leading zero (00 to 59)</td></tr>"
            + "<tr align=\"left\"><td>s</td><td>the second without a leading zero (0 to 59)</td></tr>"
            + "<tr align=\"left\"><td>ss</td><td>the second with a leading zero (00 to 59)</td></tr>"
            + "<tr align=\"left\"><td>z</td><td>the milliseconds without leading zeroes (0 to 999)</td></tr>"
            + "<tr align=\"left\"><td>zzz</td><td>the milliseconds with leading zeroes (000 to 999)</td></tr>"
            + "<tr align=\"left\"><td>AP or A</td><td>interpret as an AM/PM time. <i>AP</i> must be either \"AM\" or \"PM\"</td></tr>"
            + "<tr align=\"left\"><td>ap or a</td><td>Interpret as an AM/PM time. <i>ap</i> must be either \"am\" or \"pm\"</td></tr>"
            + "</tbody></table>"
            + //"</body>" +
            "</html>");

//        minValuePanel.add(minValueDatePicker, BorderLayout.CENTER);
//        maxValuePanel.add(maxValueDatePicker, BorderLayout.CENTER);

        if (editMaskDateTime != null) {
            setupInitialValues(editMaskDateTime);
        }
        updateEnableState();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private void setupInitialValues(EditMaskDateTime editMaskDateTime) {
        if (editMaskDateTime.getMask() != null) {
            qtMaskTextField.setText(editMaskDateTime.getMask());
        }

        getDateTimeModel(cmbDateStyle).setSelectedItemSource(editMaskDateTime.getDateStyle());
        getDateTimeModel(cmbTimeStyle).setSelectedItemSource(editMaskDateTime.getTimeStyle());

//        if (editMaskDateTime.getMinValue() != null) {
//            minimumValueCheckBox.setSelected(true);
//            minValueDatePicker.setDate(editMaskDateTime.getMinValue().getTime());
//        } else {
//            minimumValueCheckBox.setSelected(false);
//        }
//        if (editMaskDateTime.getMaxValue() != null) {
//            maximumValueCheckBox.setSelected(true);
//            maxValueDatePicker.setDate(editMaskDateTime.getMaxValue().getTime());
//        } else {
//            maximumValueCheckBox.setSelected(false);
//        }
    }

    private void updateEnableState() {
//        useSistemQtMaskCheckBox.setEnabled(!readOnly);
//        qtMaskTextField.setEnabled(!readOnly && !useSistemQtMaskCheckBox.isSelected());
//        minimumValueCheckBox.setEnabled(!readOnly);
//        minValueDatePicker.setEnabled(!readOnly && minimumValueCheckBox.isSelected());
//        maximumValueCheckBox.setEnabled(!readOnly);
//        maxValueDatePicker.setEnabled(!readOnly && maximumValueCheckBox.isSelected());

        cmbDateStyle.setEnabled(!readOnly);
        cmbTimeStyle.setEnabled(!readOnly);

        qtMaskTextField.setEnabled(!readOnly && getDateTimeModel(cmbDateStyle).getSelectedItemSource() == EDateTimeStyle.CUSTOM
            && getDateTimeModel(cmbTimeStyle).getSelectedItemSource() == EDateTimeStyle.CUSTOM);
    }

    private KernelEnumComboBoxModel<EDateTimeStyle> getDateTimeModel(JComboBox comboBox) {
        return ((KernelEnumComboBoxModel<EDateTimeStyle>) comboBox.getModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        qtMaskTextField = new javax.swing.JTextField();
        cmbDateStyle = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>>();
        lblDateTime = new javax.swing.JLabel();
        cmbTimeStyle = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>>();
        lblTimeType = new javax.swing.JLabel();
        lblCustomMask = new javax.swing.JLabel();

        qtMaskTextField.setText(org.openide.util.NbBundle.getMessage(DateTimeEditor.class, "DateTimeEditor.qtMaskTextField.text")); // NOI18N
        qtMaskTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                qtMaskTextFieldMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                qtMaskTextFieldMouseExited(evt);
            }
        });

        cmbDateStyle.setModel(new org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel(EDateTimeStyle.class, EDateTimeStyle.DEFAULT));
        cmbDateStyle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDateStyleItemStateChanged(evt);
            }
        });

        lblDateTime.setText(org.openide.util.NbBundle.getMessage(DateTimeEditor.class, "DateTimeEditor.lblDateTime.text")); // NOI18N

        cmbTimeStyle.setModel(new org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel(EDateTimeStyle.class, EDateTimeStyle.DEFAULT));
        cmbTimeStyle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTimeStyleItemStateChanged(evt);
            }
        });

        lblTimeType.setText(org.openide.util.NbBundle.getMessage(DateTimeEditor.class, "DateTimeEditor.lblTimeType.text")); // NOI18N

        lblCustomMask.setText(org.openide.util.NbBundle.getMessage(DateTimeEditor.class, "DateTimeEditor.lblCustomMask.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(qtMaskTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addComponent(cmbDateStyle, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTimeStyle, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCustomMask)
                            .addComponent(lblTimeType)
                            .addComponent(lblDateTime))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDateTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDateStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTimeType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbTimeStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCustomMask)
                .addGap(5, 5, 5)
                .addComponent(qtMaskTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void qtMaskTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtMaskTextFieldMouseEntered
        ToolTipManager.sharedInstance().setDismissDelay(60000);
    }//GEN-LAST:event_qtMaskTextFieldMouseEntered

    private void qtMaskTextFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtMaskTextFieldMouseExited
        ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDelay);
    }//GEN-LAST:event_qtMaskTextFieldMouseExited

    private void cmbDateStyleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDateStyleItemStateChanged
        updateStyles(cmbDateStyle, cmbTimeStyle);
        updateEnableState();
    }//GEN-LAST:event_cmbDateStyleItemStateChanged

    private void cmbTimeStyleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTimeStyleItemStateChanged
        updateStyles(cmbTimeStyle, cmbDateStyle);
        updateEnableState();
    }//GEN-LAST:event_cmbTimeStyleItemStateChanged
    private boolean updateStylesProcess = false;

    private void updateStyles(JComboBox changed, JComboBox dependent) {
        if (updateStylesProcess) {
            return;
        }
        updateStylesProcess = true;
        if (getDateTimeModel(changed).getSelectedItemSource() == EDateTimeStyle.CUSTOM) {
            getDateTimeModel(dependent).setSelectedItemSource(EDateTimeStyle.CUSTOM);
        } else if (getDateTimeModel(dependent).getSelectedItemSource() == EDateTimeStyle.CUSTOM) {
            getDateTimeModel(dependent).setSelectedItemSource(EDateTimeStyle.DEFAULT);
        }
        updateStylesProcess = false;
    }

    @Override
    public void apply(EditMask editMask) {
        if (!(editMask instanceof EditMaskDateTime)) {
            return;
        }
        EditMaskDateTime editMaskDateTime = (EditMaskDateTime) editMask;

        final EDateTimeStyle dateStyle = getDateTimeModel(cmbDateStyle).getSelectedItemSource();
        final EDateTimeStyle timeStyle = getDateTimeModel(cmbTimeStyle).getSelectedItemSource();

        editMaskDateTime.setDateStyle(dateStyle);
        editMaskDateTime.setTimeStyle(timeStyle);

        if (dateStyle == EDateTimeStyle.CUSTOM && timeStyle == EDateTimeStyle.CUSTOM) {
            editMaskDateTime.setMask(qtMaskTextField.getText());
        }

//        if (!useSistemQtMaskCheckBox.isSelected())
//            editMaskDateTime.setMask(qtMaskTextField.getText());
//        else
//            editMaskDateTime.setMask(null);
//        if (minimumValueCheckBox.isSelected()) {
//            Calendar calendar = Calendar.getInstance();
//            Date date = minValueDatePicker.getDate();
//            if (date != null) {
//                calendar.setTime(date);
//            } else {
//                date = new Date();
//                calendar.setTime(date);
//            }
//            editMaskDateTime.setMinValue(calendar);
//        } else {
//            editMaskDateTime.setMinValue(null);
//        }
//        if (maximumValueCheckBox.isSelected()) {
//            Calendar calendar = Calendar.getInstance();
//            Date date = maxValueDatePicker.getDate();
//            if (date != null) {
//                calendar.setTime(date);
//            } else {
//                date = new Date();
//                calendar.setTime(date);
//            }
//            editMaskDateTime.setMaxValue(calendar);
//        } else {
//            editMaskDateTime.setMaxValue(null);
//        }
    }
//    private JXDatePicker minValueDatePicker = new JXDatePicker();
//    private JXDatePicker maxValueDatePicker = new JXDatePicker();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> cmbDateStyle;
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> cmbTimeStyle;
    private javax.swing.JLabel lblCustomMask;
    private javax.swing.JLabel lblDateTime;
    private javax.swing.JLabel lblTimeType;
    private javax.swing.JTextField qtMaskTextField;
    // End of variables declaration//GEN-END:variables
}
