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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.enums.EReportSpecialCellType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;


class AdsReportSpecialCellEditor extends JPanel {

    private class Item {

        public final EReportSpecialCellType type;

        public Item(EReportSpecialCellType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type.getValue();
        }

    }

    private volatile boolean updating = false;
    private final AdsReportSpecialCell cell;
    private final Map<EReportSpecialCellType, Item> itemForType = new HashMap<>();
    
    private javax.swing.JLabel fieldLabel=new javax.swing.JLabel();
    private javax.swing.JComboBox<Item> resultComboBox= new javax.swing.JComboBox<>();
    private  FormattedCellPanel formatPanel=null;

    /** Creates new form AdsReportSpecialCellEditor */
    protected AdsReportSpecialCellEditor(AdsReportSpecialCell cell) {
        super();
        this.cell = cell;
        initComponents();
        fieldLabel.setText(org.openide.util.NbBundle.getMessage(AdsReportSpecialCellEditor.class, "AdsReportSpecialCellEditor.jLabel3.text")); 

//        typeComboBox.setModel(new DefaultComboBoxModel(EReportCellType.values()));
        ArrayList<Item> items = new ArrayList<>();
        for (EReportSpecialCellType type : EReportSpecialCellType.values()) {
            Item item = new Item(type);
            itemForType.put(type, item);
            items.add(item);
        }
        Item[] arr=new Item[items.size()];
        items.toArray(arr);
        resultComboBox.setModel(new DefaultComboBoxModel<>(arr));
        resultComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (!updating) {
                    EReportSpecialCellType specislType=((Item) resultComboBox.getSelectedItem()).type;
                    AdsReportSpecialCell spesialCell=AdsReportSpecialCellEditor.this.cell;
                    EReportSpecialCellType oldSpecislType = spesialCell.getSpecialType();
                    spesialCell.setSpecialType(specislType);
                    EValType type=(specislType==EReportSpecialCellType.GENERATION_TIME) ? EValType.DATE_TIME : EValType.INT;
                    spesialCell.getFormat().setUseDefaultFormat(true);
                    AdsReportSpecialCellEditor.this.formatPanel.open(spesialCell.getFormat(),type);
                    if (!Utils.equals(oldSpecislType, specislType)){
                        firePropertyChange(AdsReportWidgetNamePanel.CHANGE_NAME, false, true);
                    }
                }
            }
        });

        createUi();
        setupInitialValues();
    }
    
    private void createUi(){
        JPanel content = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        content.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        gbl.setConstraints(fieldLabel, c);
        content.add(fieldLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(resultComboBox, c);
        content.add(resultComboBox);
        
        c.insets = new Insets(10, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.gridwidth = 2;


        EValType type=(cell.getSpecialType()==EReportSpecialCellType.GENERATION_TIME)? EValType.DATE_TIME: EValType.INT;
        formatPanel =new FormattedCellPanel(cell.getFormat(),type,cell.isReadOnly());
        gbl.setConstraints(formatPanel, c);
        content.add(formatPanel); 

        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);
    }

    private void setupInitialValues() {
        updating = true;
//        typeComboBox.setSelectedItem(cell.getCellType());
        resultComboBox.setSelectedItem(itemForType.get(cell.getSpecialType()));
        //patternEditor.setTextFieldValue(cell.getPattern());
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        boolean enabled = !cell.isReadOnly();
//        typeComboBox.setEnabled(false);
        //patternEditor.setEditable(false);
        if(formatPanel!=null)
            formatPanel.setEnabled(enabled);        
        resultComboBox.setEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
