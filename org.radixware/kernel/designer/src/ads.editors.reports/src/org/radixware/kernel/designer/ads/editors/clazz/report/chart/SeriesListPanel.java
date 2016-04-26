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
 * AdsChartSeriesPanel.java
 *
 * Created on May 11, 2012, 5:48:24 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell.ChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class SeriesListPanel extends javax.swing.JPanel {
    private  AdsReportChartCell cell;
    
    /** Creates new form AdsChartSeriesPanel */
    public SeriesListPanel(final AdsReportChartCellEditor cellEditor) {
        initComponents();
        
        btnAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon(20));
        btnDelete.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(20));
        btnDelete.setEnabled(false);
       
        btnEdit.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(20));
        btnEdit.setEnabled(false);
        
        btnUp.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(20));
        btnUp.setEnabled(false);
        
        btnDown.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(20));
        btnDown.setEnabled(false);
        seriesList.setCellRenderer(new MyCellRenderer());

        seriesList.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                AdsReportChartSeries series=getCurrentChartSeries();
                cellEditor.selectSeries(series);
                boolean isEnable=series!=null;
                btnDelete.setEnabled(isEnable);
                btnEdit.setEnabled(isEnable);
                //btnUp.setEnabled(isEnable);
                //btnDown.setEnabled(isEnable);
                int index=seriesList.getSelectedIndex();
                btnUp.setEnabled(isEnable && index>0);
                btnDown.setEnabled(isEnable && index<seriesList.getModel().getSize()-1);
            }
            
        });
        
        seriesList.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount()==2 && me.getButton()==MouseEvent.BUTTON1){
                    btnEditActionPerformed(null);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
                
            }
        
        });
        DefaultListModel<SeriesItem> model=new DefaultListModel<>(); 
        seriesList.setModel(model);
              
    }
    
    public void open(final AdsReportChartCell cell){  
        this.cell=cell;
        if(cell!=null){
            ChartSeries series=cell.getChartSeries();
            if(series!=null){
                for(AdsReportChartSeries s:series){
                    addItemToList(s);
                }
                if(!series.isEmpty()){
                    seriesList.setSelectedIndex(0); 
                }
            }
        }         
    }
    
    private AdsReportChartSeries getCurrentChartSeries(){
        Object selectedItem=seriesList.getSelectedValue();
        if(selectedItem!=null && selectedItem instanceof SeriesItem)
            return ((SeriesItem)selectedItem).getChartSeries();
        return null;
    }
     
     private void addItemToList(AdsReportChartSeries series){
        DefaultListModel<SeriesItem> model=(DefaultListModel<SeriesItem>)seriesList.getModel();
        SeriesItem item=new SeriesItem(series);
        model.addElement(item);
        seriesList.setSelectedIndex(model.indexOf(item)); 
     }
     
     private boolean editSeries(AdsReportChartSeries series,String title){
        boolean is3D=true;
        boolean isPlain=true;
        if(!cell.getChartSeries().isEmpty() && 
           !(cell.getChartSeries().size()==1 && cell.getChartSeries().get(0).equals(series))){
            is3D=cell.getChartSeries().is3D();
            isPlain=!is3D;
        }        

        ChartSeriesEditPanel panel=new ChartSeriesEditPanel(series, cell,is3D,isPlain);
        ChartSeriesDialog dialog=new ChartSeriesDialog(panel,title) ;
        dialog.invokeModalDialog();
        boolean res=dialog.isOK();
        if(res){
            series.setSeriesType(panel.getSeriesType());
            series.setName(panel.getSeriesName());
            cell.setEditState(EEditState.MODIFIED);
        }
        return res;
     }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        seriesList = new javax.swing.JList<SeriesItem>();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();

        jScrollPane1.setViewportView(seriesList);

        btnAdd.setText(org.openide.util.NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel.btnAdd.text")); // NOI18N
        btnAdd.setMaximumSize(new java.awt.Dimension(28, 28));
        btnAdd.setMinimumSize(new java.awt.Dimension(28, 28));
        btnAdd.setPreferredSize(new java.awt.Dimension(28, 28));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText(org.openide.util.NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel.btnDelete.text")); // NOI18N
        btnDelete.setMaximumSize(new java.awt.Dimension(28, 28));
        btnDelete.setMinimumSize(new java.awt.Dimension(28, 28));
        btnDelete.setPreferredSize(new java.awt.Dimension(28, 28));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setText(org.openide.util.NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel.btnEdit.text")); // NOI18N
        btnEdit.setMaximumSize(new java.awt.Dimension(28, 28));
        btnEdit.setMinimumSize(new java.awt.Dimension(28, 28));
        btnEdit.setPreferredSize(new java.awt.Dimension(28, 28));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnUp.setText(org.openide.util.NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel.btnUp.text")); // NOI18N
        btnUp.setMaximumSize(new java.awt.Dimension(28, 28));
        btnUp.setMinimumSize(new java.awt.Dimension(28, 28));
        btnUp.setPreferredSize(new java.awt.Dimension(28, 28));
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setText(org.openide.util.NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel.btnDown.text")); // NOI18N
        btnDown.setMaximumSize(new java.awt.Dimension(28, 28));
        btnDown.setMinimumSize(new java.awt.Dimension(28, 28));
        btnDown.setPreferredSize(new java.awt.Dimension(28, 28));
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDelete});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        AdsReportChartSeries series=new AdsReportChartSeries(cell,"ChartSeries"+cell.getChartSeries().size());   
        final String title="Add chart series";
        if (editSeries(series,title)) {
            cell.getChartSeries().add(series);
            addItemToList(series);
        } 
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        String frameHeader = NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel-RemoveDialog-Header");
        String frameQuest =NbBundle.getMessage(SeriesListPanel.class, "SeriesListPanel-RemoveDialog-Quest");
        if (JOptionPane.showConfirmDialog(new JFrame(), frameQuest, frameHeader, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            int index=seriesList.getSelectedIndex();
            DefaultListModel<SeriesItem> model=(DefaultListModel<SeriesItem>)seriesList.getModel();
            model.remove(index);
            cell.getChartSeries().remove(index);
            cell.setEditState(EEditState.MODIFIED);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        AdsReportChartSeries series=getCurrentChartSeries();
        final String title="Edit chart series";
        if(editSeries(series,title)){
            seriesList.validate();
            seriesList.repaint();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        moveSeries(true);
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        moveSeries(false);
    }//GEN-LAST:event_btnDownActionPerformed

    private void moveSeries(boolean isMoveUp){
        DefaultListModel<SeriesItem> model=(DefaultListModel<SeriesItem>)seriesList.getModel();
        SeriesItem selectedItem=seriesList.getSelectedValue();
        int index=seriesList.getSelectedIndex();
        model.remove(index);
        index=isMoveUp ? index-1 : index+1;
        model.insertElementAt(selectedItem, index);
        seriesList.setSelectedIndex(index);
        
        AdsReportChartSeries series=selectedItem.getChartSeries();
        cell.getChartSeries().remove(series);
        cell.getChartSeries().add(index, series);
        cell.setEditState(EEditState.MODIFIED);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnUp;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<SeriesItem> seriesList;
    // End of variables declaration//GEN-END:variables

    static class SeriesItem {

        @Override
        public String toString() {
            return series.getName();
        }
        private final AdsReportChartSeries series;
        
        SeriesItem(final AdsReportChartSeries series){
            this.series=series;
        }
        
        Icon getIcon(){
            return AdsDefinitionIcon.getForReportChartSeriesType(series.getSeriesType()).getIcon(100);
        }
        
        AdsReportChartSeries getChartSeries(){
            return series;
        }
    }
    
    static class MyCellRenderer extends JLabel implements ListCellRenderer<SeriesItem>{

        @Override
         public Component getListCellRendererComponent(
           JList<? extends SeriesItem> list,              
           SeriesItem value,            
           int index,               
           boolean isSelected,      
           boolean cellHasFocus)   
         {
             SeriesItem item=value;
             if(item.getChartSeries().getSeriesType()!=null){
                 String s = item.getChartSeries().getName();
                 setText(s);
             }
             Icon icon = item.getIcon();
             setIcon(icon);
             if (isSelected) {
                 setBackground(list.getSelectionBackground());
                 setForeground(list.getSelectionForeground());
             } else {
                 setBackground(list.getBackground());
                 setForeground(list.getForeground());
             }
             setEnabled(list.isEnabled());
             setFont(list.getFont());
             setOpaque(true);
             return this;
         }
     }


}
