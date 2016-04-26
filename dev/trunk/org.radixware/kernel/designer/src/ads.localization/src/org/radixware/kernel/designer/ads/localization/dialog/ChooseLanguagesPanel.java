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
 * ChooseLanguagesPanel.java
 *
 * Created on Sep 6, 2009, 7:00:53 PM
 */

package org.radixware.kernel.designer.ads.localization.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.LanguagesTableModel;


public class ChooseLanguagesPanel extends javax.swing.JPanel {

    private final List<EIsoLanguage> selectedLangs;
     //private ChooseLanguagesDialog parent;

    private final TableModelListener modelListener = new TableModelListener() {

        @Override
        public void tableChanged(final TableModelEvent e) {

            final int column = e.getColumn();
            if (column == LanguagesTableModel.CHECK_BOX_COLUMN) {
                final TableModel model = (TableModel) e.getSource();
                final int row = e.getFirstRow();
                final Object data = model.getValueAt(row, LanguagesTableModel.CHECK_BOX_COLUMN);
                final Object obj = model.getValueAt(row, LanguagesTableModel.LANGUAGE_NAME_COLUMN);
                if (((Boolean) data)) {
                    selectedLangs.add((EIsoLanguage) obj);
                } else {
                    selectedLangs.remove((EIsoLanguage) obj);
                }
                if(ChooseLanguagesPanel.this.getParent() instanceof ChooseLanguagesDialog){
                    ((ChooseLanguagesDialog)ChooseLanguagesPanel.this.getParent()).check();
                    //((ChooseLanguagesDialog)ChooseLanguagesPanel.this.getParent()).update(ChooseLanguagesPanel.this);
                }
            }
        }
     };



    /** Creates new form ChooseLanguagesPanel */
    public ChooseLanguagesPanel(/*final ChooseLanguagesDialog parent,*/final List<EIsoLanguage> langs, final List<EIsoLanguage> initSelectedLangs, final String listTitle) {
        this.selectedLangs=new ArrayList<>();
        initComponents();
        this.setBorder(new EmptyBorder(0, 0, 10, 0));
        lbListTitle.setText(listTitle);

        /*list=new CheckBoxJList();
        list.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (! e.getValueIsAdjusting()) {
                    if(ChooseLanguagesPanel.this.getParent() instanceof ChooseLanguagesDialog){
                        ((ChooseLanguagesDialog)ChooseLanguagesPanel.this.getParent() ).check();
                    }
                }
            }
        });
        list.addListSelectionListener(list);

        defModel = new DefaultListModel();
        list.setModel (defModel);
        boolean hasSelection=selectedLangs!=null;
        for(EIsoLanguage lang : langs){
            defModel.addElement (lang);
            if((hasSelection)&&(selectedLangs.contains(lang)))
               list.getSelectionModel().setSelectionInterval(defModel.getSize()-1, defModel.getSize()-1);
        }
        
        JScrollPane scroller =new JScrollPane (list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scroller);*/
        createTable( langs, initSelectedLangs);
    }

    //private Container getParentDialog(){
    //    return this.getParent();
    //}

    public void update(final List<EIsoLanguage> langs, final List<EIsoLanguage> initSelectedLangs){
        createTable( langs, initSelectedLangs);
    }

    private void createTable(final List<EIsoLanguage> langs, final List<EIsoLanguage> initSelectedLangs) {
        //final EIsoLanguage[] langsArr = new EIsoLanguage[langs.size()];
        languagesTable.setModel(new LanguagesTableModel(langs.toArray(new EIsoLanguage[langs.size()])) );
        languagesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        languagesTable.setAutoscrolls(false);
        languagesTable.setFocusable(false);
        languagesTable.setSurrendersFocusOnKeystroke(true);
         final TableColumnModel columnModel = languagesTable.getColumnModel();
        final TableColumn checkboxesColumn = columnModel.getColumn(LanguagesTableModel.CHECK_BOX_COLUMN);
        checkboxesColumn.setMaxWidth(10);

        final TableColumn languagesColumn = columnModel.getColumn(LanguagesTableModel.LANGUAGE_NAME_COLUMN);
        final int languagesColumnWidth = getWidth() - 10;
        languagesColumn.setMinWidth(languagesColumnWidth);
        languagesColumn.setMinWidth(languagesColumnWidth);
        languagesColumn.setPreferredWidth(languagesColumnWidth);

        javax.swing.ToolTipManager.sharedInstance().unregisterComponent(languagesTable);

        languagesTable.setTableHeader(null);
        jScrollPane2.setColumnHeaderView(null);
        languagesTable.getModel().addTableModelListener(modelListener);
        fillTable(langs,initSelectedLangs);
        

    }

    private void fillTable(final List<EIsoLanguage> langs,final List<EIsoLanguage> initSelectedLangs) {
        if ((initSelectedLangs!=null)&&(!initSelectedLangs.isEmpty())) {
            final TableModel tableModel = languagesTable.getModel();

            for (int i = 0, len = langs.size(); i < len; ++i) {
                if (initSelectedLangs.contains(langs.get(i))) {
                    tableModel.setValueAt(true, i, LanguagesTableModel.CHECK_BOX_COLUMN);
                }
            }
        }
    }

    public List<EIsoLanguage> getSelection() {
       /* List<EIsoLanguage> selectedLangs=new ArrayList();

        int size=list.getSelection().size();
        final Integer[] langsArr = new Integer[size];
        list.getSelection().toArray(langsArr);
         for(int i=0;i<size;i++){
                selectedLangs.add((EIsoLanguage) defModel.getElementAt(langsArr[i]));
         }
        return selectedLangs;*/
        return selectedLangs;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        languagesTable = new javax.swing.JTable();
        lbListTitle = new javax.swing.JLabel();

        languagesTable.setBackground(java.awt.Color.white);
        languagesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        languagesTable.setShowHorizontalLines(false);
        languagesTable.setShowVerticalLines(false);
        jScrollPane2.setViewportView(languagesTable);

        lbListTitle.setText(org.openide.util.NbBundle.getMessage(ChooseLanguagesPanel.class, "ChooseLanguagesPanel.lbListTitle.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
            .addComponent(lbListTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbListTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
        );

        lbListTitle.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ChooseLanguagesPanel.class, "ChooseLanguagesPanel.jLabel1.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable languagesTable;
    private javax.swing.JLabel lbListTitle;
    // End of variables declaration//GEN-END:variables


}
