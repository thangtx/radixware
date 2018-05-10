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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;

public final class NumFormatPanel extends AbstractFormatPanel {

    public NumFormatPanel(final AdsReportFormat cell, boolean isReadOnly) {
        super(cell, isReadOnly);

        initComponents();
        decimalDelimiterEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent de) {
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(final DocumentEvent de) {
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(final DocumentEvent de) {
                if (!updating && cell != null && decimalDelimiterCheckBox.isSelected()) {
                    cell.setDesimalDelimeter(decimalDelimiterEditor.getText());
                }
            }
        });
        precisionSpinner.setEditor(new CheckedNumberSpinnerEditor(precisionSpinner));
        setupInitialValues();
        updateEnableState();
    }
    private boolean updating = false;

    @Override
    public void setupInitialValues() {
        ((IntFormatPanel) intFormatPanel).setupInitialValues();

        updating = true;
        if (cell.getPrecission() != -1) {
            precisionSpinner.setValue(Long.valueOf(cell.getPrecission()));
            precisionCheckBox.setSelected(true);
        } else {
            precisionSpinner.setValue(Long.valueOf(0L));
            precisionCheckBox.setSelected(false);
        }
        
        final String decimalDelimiter = cell.getDesimalDelimeter();
        if (decimalDelimiter == null) {
            decimalDelimiterCheckBox.setSelected(false);
        } else {
            decimalDelimiterCheckBox.setSelected(true);
            decimalDelimiterEditor.setText(decimalDelimiter);
        }
        updating = false;
    }

    private void updateEnableState() {
        precisionCheckBox.setEnabled(!isReadOnly);
        precisionSpinner.setEnabled(!isReadOnly && precisionCheckBox.isSelected());
        decimalDelimiterCheckBox.setEnabled(!isReadOnly);
        decimalDelimiterEditor.setEnabled(!isReadOnly && decimalDelimiterCheckBox.isSelected());
    }
    
    private void initComponents() {
        numParametersPanel = new JPanel();
        precisionCheckBox = new javax.swing.JCheckBox();
        precisionSpinner = new javax.swing.JSpinner();
        decimalDelimiterCheckBox = new javax.swing.JCheckBox();
        decimalDelimiterEditor = new javax.swing.JFormattedTextField();
        intFormatPanel = new IntFormatPanel(cell, isReadOnly);

        precisionCheckBox.setText("Precision:");
        precisionCheckBox.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                precisionCheckBoxItemStateChanged(evt);
            }
        });

        precisionSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(127L), Long.valueOf(1L)));
        precisionSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                precisionSpinnerStateChanged(evt);
            }
        });

        decimalDelimiterCheckBox.setText("Decimal Delimiter:");
        decimalDelimiterCheckBox.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbDecimDelimItemStateChanged(evt);
            }
        });

        try {
            decimalDelimiterEditor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*")));
        } catch (java.text.ParseException ex) {
            Logger.getLogger(NumFormatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        numParametersPanel.setLayout(new MigLayout("fill", "[shrink][grow]", "[][][][]"));
        numParametersPanel.add(precisionCheckBox, "shrinky, wrap");
        numParametersPanel.add(precisionSpinner, "shrinky, growx, span 2, wrap");
        numParametersPanel.add(decimalDelimiterCheckBox, "shrinky, wrap");
        numParametersPanel.add(decimalDelimiterEditor, "shrinky, growx, span 2, wrap");        
        
        this.setLayout(new MigLayout("fill", "[]", "[shrink][shrink]"));
        this.add(numParametersPanel, "growx, shrinky, wrap");
        this.add(intFormatPanel, "growx, shrinky");
    }                      

    private void precisionCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        precisionSpinnerStateChanged(null);       
        updateEnableState();
    }

    private void chbDecimDelimItemStateChanged(java.awt.event.ItemEvent evt) {
        if (!updating && !decimalDelimiterCheckBox.isSelected()) {
            cell.setDesimalDelimeter(null);
        }
        updateEnableState();
    }

    private void precisionSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        if (cell != null && !updating) {
            if (precisionCheckBox.isSelected()) {
                cell.setPrecission(((Long) precisionSpinner.getValue()).intValue());
            } else {
                cell.setPrecission(-1);
            }
        }
    }
    
    private javax.swing.JCheckBox decimalDelimiterCheckBox;
    private IntFormatPanel intFormatPanel;
    private javax.swing.JCheckBox precisionCheckBox;
    private javax.swing.JSpinner precisionSpinner;
    private javax.swing.JFormattedTextField decimalDelimiterEditor;
    private JPanel numParametersPanel;
}
