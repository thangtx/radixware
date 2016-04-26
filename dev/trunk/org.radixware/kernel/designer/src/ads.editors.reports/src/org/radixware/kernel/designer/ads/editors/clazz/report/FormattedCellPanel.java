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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;

public class FormattedCellPanel extends javax.swing.JPanel {

    private JCheckBox cbDefaultFormat;
    private JPanel formattedPanel;
    private final JPanel emptyPanel = new JPanel();
    private final boolean isReadOnly;
    private AdsReportFormat format;
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * Creates new form FormattedCellPanel
     */
    /*public FormattedCellPanel(final AdsReportFormattedCell cell,EValType type) {
     initComponents();
     String cbLabel=NbBundle.getMessage(AdsReportCsvExportPanel.class, "FormattedCellPanel-CheckBox-Label");
     cbDefaultFormat = new JCheckBox(cbLabel);
     cbDefaultFormat.setBorder(null);         
     cbDefaultFormat.addItemListener(new ItemListener() {

     @Override
     public void itemStateChanged(ItemEvent e) {
     cell.setUseDefaultFormat(cbDefaultFormat.isSelected());
     processCheckBox();
     }
     });
     cbDefaultFormat.setEnabled(!cell.isReadOnly());
     //cbExportEnabled.setSelected(cell.getUseDefaultFormat());
     open(cell,type);
     //processCheckBox();
     this.setBorder(new ComponentTitledBorder(cbDefaultFormat, this, new TitledBorder(""))); 
     }*/
    public FormattedCellPanel(final AdsReportFormat format, final EValType type, final boolean isReadOnly) {
        this(isReadOnly);
        this.format = format;
        open(format, type);
    }

    @SuppressWarnings("deprecation")
    public FormattedCellPanel(final boolean isReadOnly) {
        initComponents();
        final String cbLabel = NbBundle.getMessage(AdsReportCsvExportPanel.class, "FormattedCellPanel-CheckBox-Label");
        cbDefaultFormat = new JCheckBox(cbLabel);
        this.isReadOnly = isReadOnly;
        cbDefaultFormat.setBorder(null);
        cbDefaultFormat.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (FormattedCellPanel.this.format != null) {
                    FormattedCellPanel.this.format.setUseDefaultFormat(cbDefaultFormat.isSelected());
                }
                processCheckBox();
            }
        });
        this.setBorder(new ComponentTitledBorder(cbDefaultFormat, this, new TitledBorder("")));
    }

    public final void open(final AdsReportFormat format, final EValType type) {
        this.format = format;
        if (type != null) {
            cbDefaultFormat.setEnabled(true);
            if (type == EValType.DATE_TIME) {
                if (formattedPanel == null || !(formattedPanel instanceof DateTimeFormatPanel)) {
                    formattedPanel = new DateTimeFormatPanel(format);
                }
            } else if (type == EValType.NUM) {
                if (formattedPanel == null || !(formattedPanel instanceof NumFormatPanel)) {
                    final JPanel numPanel = new NumFormatPanel(format, isReadOnly);
                    final JPanel intPanel = new IntFormatPanel(format, isReadOnly);
                    formattedPanel = new JPanel();

                    final BoxLayout layout = new BoxLayout(formattedPanel, BoxLayout.Y_AXIS);
                    formattedPanel.setLayout(layout);
                    formattedPanel.add(numPanel);
                    formattedPanel.add(intPanel);
                }
            } else if (type == EValType.INT) {
                if (formattedPanel == null || !(formattedPanel instanceof IntFormatPanel)) {
                    formattedPanel = new IntFormatPanel(format, isReadOnly);
                }
            } else {
                cbDefaultFormat.setEnabled(false);
            }
        }
        cbDefaultFormat.setSelected(format.getUseDefaultFormat());
        processCheckBox();
    }

    private void processCheckBox() {
        final boolean userDefaultFormat = cbDefaultFormat.isSelected();
        if (!userDefaultFormat && formattedPanel != null) {
            removeAll();
            this.add(formattedPanel);

        } else {
            removeAll();
            this.add(emptyPanel);

        }
        setSize(getWidth(), getPreferredSize().height);

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                getParent().revalidate();                
            }
        });
        changeSupport.fireChange();

    }

    int getPanelHeight() {
        final boolean userDefaultFormat = cbDefaultFormat.isSelected();
        if (!userDefaultFormat && formattedPanel != null) {
            return formattedPanel.getHeight();
        } else {
            return emptyPanel.getHeight();
        }
    }

    public void addChangeListener(final ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(final ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
