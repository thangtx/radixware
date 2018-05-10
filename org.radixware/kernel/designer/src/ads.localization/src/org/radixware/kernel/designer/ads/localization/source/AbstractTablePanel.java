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
 * AbstractTablePanel.java
 *
 * Created on Oct 26, 2009, 4:29:59 PM
 */

package org.radixware.kernel.designer.ads.localization.source;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.RowString;


public abstract class AbstractTablePanel extends javax.swing.JPanel {

    protected MlsTableUi tableUi;
    protected List<RowString> mlStrings;
    protected boolean setFocusOnTranslation=true;
    protected RowString curMlString=null;
    protected javax.swing.JToolBar toolBar1;
    protected javax.swing.JToolBar filterToolBar;

    /** Creates new form AbstractTablePanel */
    public AbstractTablePanel() {
        initComponents();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if(! e.getValueIsAdjusting()){
                    AbstractTablePanel.this.setCurrentRowString();
                    //JViewport viewport = (JViewport)table.getParent();
                    //Point pt = viewport.getViewPosition();
                    Rectangle rect=table.getCellRect(table.getSelectedRow(), table.getSelectedColumn(), true);
                    //rect.setLocation(rect.x-pt.x, rect.y-pt.y);
                    //if(!(new Rectangle(viewport.getExtentSize())).contains(rect))
                    table.scrollRectToVisible(rect);
                }
            }
        });
        table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(final MouseEvent e) {}

            @Override
            public void mousePressed(final MouseEvent e) {}

            @Override
            public void mouseReleased(final MouseEvent e) {
                if(e.getButton()== MouseEvent.BUTTON1){
                      changeStatus( table.getSelectedRow(),table.getSelectedColumn());
                }
            }

            @Override
            public void mouseEntered(final MouseEvent e) {}

            @Override
            public void mouseExited(final MouseEvent e) {}

        });
        final JPanel panel=new JPanel();
        panel.setLayout(new javax.swing.BoxLayout( panel,javax.swing.BoxLayout.Y_AXIS) );
        add(panel, BorderLayout.NORTH);
        
        toolBar1=new JToolBar();
        toolBar1.setFloatable(true);
        panel.add(toolBar1);
        
        filterToolBar=new JToolBar();
        filterToolBar.setFloatable(true);
        panel.add(filterToolBar);       
        
        tableUi=new MlsTableUi(table);       
    }

    public void setFocusOnTranslation(final boolean b){
        setFocusOnTranslation=b;
    }
    
    protected abstract void selectRow();
    protected abstract void changeStatus(final int row_index, final int col_index);
    protected abstract  void setCurrentRowString();

    public void setNextString(){
        if (table.getRowCount() == 0){
            return;
        }
        int row = table.getSelectedRow();
        if(row+1 < table.getRowCount()){
            row=row+1;
        }else row=0;
        table.getSelectionModel().setSelectionInterval(row, row);
        tableUi.fireTableCellUpdated(row, 0);
    }

    public RowString getCurrentRowString(){
        final int row = table.getSelectedRow();
        if((row>-1)&&(row<table.getRowCount())){
            return tableUi.getRowString(row);
        }
        return null;
    }

    protected void showMsg(final String msg){
         NotifyDescriptor d = new NotifyDescriptor.Message(msg,NotifyDescriptor.INFORMATION_MESSAGE);
         DialogDisplayer.getDefault().notify(d);
    }

    public void search(final FilterSettings filterSettings){
        updateFilter(filterSettings);
    }

    public void updateFilter(final FilterSettings filterSettings){
        tableUi.updateFilter(filterSettings);
        selectRow();
    }

     public abstract List<EIsoLanguage> getTranslatedLags();
     
     public List<EIsoLanguage> getSourceLags(){
         return new ArrayList<>();
     }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablePanel = new javax.swing.JPanel();
        scrollPanel = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        tablePanel.setLayout(new javax.swing.BoxLayout(tablePanel, javax.swing.BoxLayout.X_AXIS));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setRowHeight(24);
        scrollPanel.setViewportView(table);

        tablePanel.add(scrollPanel);

        add(tablePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    protected javax.swing.JTable getTable(){
        return table;
    }

    protected javax.swing.JToolBar getToolBar(){
        return toolBar1;
    }
    
    protected javax.swing.JToolBar getFilterToolBar(){
        return filterToolBar;
    }

    protected javax.swing.JPanel getTablePanel(){
        return tablePanel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JTable table;
    private javax.swing.JPanel tablePanel;
    // End of variables declaration//GEN-END:variables

}
