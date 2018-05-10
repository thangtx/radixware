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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import javax.swing.JComboBox;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;

public final class DateTimeFormatPanel extends AbstractFormatPanel {

    private boolean readOnly;
    private final int defaultTooltipDelay = ToolTipManager.sharedInstance().getDismissDelay();    

    /**
     * Creates new form DateTimeFormatPanel
     */
    public DateTimeFormatPanel(final AdsReportFormat cell, boolean readOnly) {
        super(cell, readOnly);
        
        initComponents();
        qtMaskTextField.setToolTipText(
                "<html>"
                + "<table align=\"center\" border=\"1\" cellpadding=\"2\" cellspacing=\"0\">"
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
                + "</html>");

        qtMaskTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                if (cell != null) {
                    EDateTimeStyle dateStyle = getDateTimeModel(cmbDateStyle).getSelectedItemSource();
                    EDateTimeStyle timeStyle = getDateTimeModel(cmbTimeStyle).getSelectedItemSource();
                    if (dateStyle == EDateTimeStyle.CUSTOM && timeStyle == EDateTimeStyle.CUSTOM) {
                        cell.setPattern(qtMaskTextField.getText());
                    }
                }
            }
        });
        if (cell != null) {
            setupInitialValues();
        }
        updateEnableState();
    }

    @Override
    public void setupInitialValues() {
        if (cell.getPattern() != null) {
            qtMaskTextField.setText(cell.getPattern());
        }
        EDateTimeStyle dateStyle = cell.getDateStyle() == null ? EDateTimeStyle.DEFAULT : cell.getDateStyle();
        EDateTimeStyle timeStyle = cell.getTimeStyle() == null ? EDateTimeStyle.DEFAULT : cell.getTimeStyle();
        getDateTimeModel(cmbDateStyle).setSelectedItemSource(dateStyle);
        getDateTimeModel(cmbTimeStyle).setSelectedItemSource(timeStyle);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDateTime = new javax.swing.JLabel();
        cmbDateStyle = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>>();
        lblTimeType = new javax.swing.JLabel();
        cmbTimeStyle = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>>();
        lblCustomMask = new javax.swing.JLabel();
        qtMaskTextField = new javax.swing.JTextField();

        lblDateTime.setText(org.openide.util.NbBundle.getMessage(DateTimeFormatPanel.class, "DateTimeFormatPanel.lblDateTime.text")); // NOI18N

        cmbDateStyle.setModel(new org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel<>(EDateTimeStyle.class, EDateTimeStyle.DEFAULT));
        cmbDateStyle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDateStyleItemStateChanged(evt);
            }
        });

        lblTimeType.setText(org.openide.util.NbBundle.getMessage(DateTimeFormatPanel.class, "DateTimeFormatPanel.lblTimeType.text")); // NOI18N

        cmbTimeStyle.setModel(new org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel<>(EDateTimeStyle.class, EDateTimeStyle.DEFAULT));
        cmbTimeStyle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTimeStyleItemStateChanged(evt);
            }
        });

        lblCustomMask.setText(org.openide.util.NbBundle.getMessage(DateTimeFormatPanel.class, "DateTimeFormatPanel.lblCustomMask.text")); // NOI18N

        qtMaskTextField.setText(org.openide.util.NbBundle.getMessage(DateTimeFormatPanel.class, "DateTimeFormatPanel.qtMaskTextField.text")); // NOI18N
        qtMaskTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                qtMaskTextFieldMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                qtMaskTextFieldMouseExited(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbDateStyle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTimeStyle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTimeType)
                            .addComponent(lblDateTime)
                            .addComponent(lblCustomMask))
                        .addGap(0, 202, Short.MAX_VALUE))
                    .addComponent(qtMaskTextField))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qtMaskTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDateStyleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDateStyleItemStateChanged
        updateStyles(cmbDateStyle, cmbTimeStyle);
        if (cell != null) {
            cell.setDateStyle(getDateTimeModel(cmbDateStyle).getSelectedItemSource());
        }
        updateEnableState();
    }//GEN-LAST:event_cmbDateStyleItemStateChanged

    private void cmbTimeStyleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTimeStyleItemStateChanged
        updateStyles(cmbTimeStyle, cmbDateStyle);
        cell.setTimeStyle(getDateTimeModel(cmbTimeStyle).getSelectedItemSource());
        updateEnableState();
    }//GEN-LAST:event_cmbTimeStyleItemStateChanged

    private void qtMaskTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtMaskTextFieldMouseEntered
        ToolTipManager.sharedInstance().setDismissDelay(60000);
    }//GEN-LAST:event_qtMaskTextFieldMouseEntered

    private void qtMaskTextFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtMaskTextFieldMouseExited
        ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDelay);
    }//GEN-LAST:event_qtMaskTextFieldMouseExited

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> cmbDateStyle;
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> cmbTimeStyle;
    private javax.swing.JLabel lblCustomMask;
    private javax.swing.JLabel lblDateTime;
    private javax.swing.JLabel lblTimeType;
    private javax.swing.JTextField qtMaskTextField;
    // End of variables declaration//GEN-END:variables

    private void updateEnableState() {
        cmbDateStyle.setEnabled(!readOnly);
        cmbTimeStyle.setEnabled(!readOnly);

        qtMaskTextField.setEnabled(!readOnly && getDateTimeModel(cmbDateStyle).getSelectedItemSource() == EDateTimeStyle.CUSTOM
                && getDateTimeModel(cmbTimeStyle).getSelectedItemSource() == EDateTimeStyle.CUSTOM);
    }

    private KernelEnumComboBoxModel<EDateTimeStyle> getDateTimeModel(JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> comboBox) {
        return ((KernelEnumComboBoxModel<EDateTimeStyle>) comboBox.getModel());
    }

   // if (dateStyle == EDateTimeStyle.CUSTOM && timeStyle == EDateTimeStyle.CUSTOM) {
    //         editMaskDateTime.setMask(qtMaskTextField.getText());
    //     }
    private boolean updateStylesProcess = false;

    private void updateStyles(JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> changed, JComboBox<KernelEnumComboBoxModel.Item<EDateTimeStyle>> dependent) {
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
}
