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
 * NewChartSeriesPanel.java
 *
 * Created on May 17, 2012, 6:12:12 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.enums.EReportChartSeriesType;


public class ChartSeriesEditPanel extends ChangeSupportPanel {
    
    private javax.swing.JLabel lbName = new javax.swing.JLabel(NbBundle.getMessage(ChartSeriesEditPanel.class, "ChartSeriesEditPanel.lbName.text")+":");
    private javax.swing.JLabel lbSeriesType = new javax.swing.JLabel(NbBundle.getMessage(ChartSeriesEditPanel.class, "ChartSeriesEditPanel.lbSeriesType.text")+":");
    private javax.swing.JTextField nameTextField = new javax.swing.JTextField();
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer;
    private List<String> seriesNames;
    private String seriesName;
    private final ChartTypeListPanel chartTypeListPanel;
    /** Creates new form NewChartSeriesPanel */
    public ChartSeriesEditPanel(final AdsReportChartSeries series,final AdsReportChartCell cell,
                                boolean is3D,boolean isPlain) { 
        super();        
        stateDisplayer = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        seriesName=series.getName();
        seriesNames=createNamesList(cell, series);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);        
        setupVerifiers();

        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridx = 0;
        gbl.setConstraints(lbName, c);
        content.add(lbName);

        c.insets = new Insets(10, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;
        gbl.setConstraints(nameTextField, c);
        nameTextField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent de) {
                 changedUpdate(de); 
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                 changedUpdate( de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {                 
                 setSeriesName(nameTextField.getText());
                 changeSupport.fireChange();
            }            
        });
        nameTextField.setText(series.getName());
        checkName(series.getName());
        content.add(nameTextField);       
        
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        c.weightx = 0.0;
        gbl.setConstraints(lbSeriesType, c);
        content.add(lbSeriesType);

        /*c.insets = new Insets(0, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;

        fillSeriesType( cell.getChartType(),is3D,isPlain);
        cbSeriesType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if ("comboBoxChanged".equals(ae.getActionCommand())) {
                    // User has selected an item; it may be the same item
                    seriesType=(EReportChartSeriesType)cbSeriesType.getSelectedItem();
                }
            }
        });
        cbSeriesType.setEditable(false);
        cbSeriesType.setRenderer(new Renderer(cbSeriesType.getRenderer()));
        //cbSeriesType.setPrototypeDisplayValue(EReportChartType.BAR);
        if(series.getSeriesType()!=null){
            cbSeriesType.setSelectedItem(series.getSeriesType());
        }else{
            cbSeriesType.setSelectedIndex(0);
        }
        gbl.setConstraints(cbSeriesType, c);
        content.add(cbSeriesType);*/
        
        c.insets = new Insets(0, 10, 10, 10);
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.gridy = 2;
        chartTypeListPanel=new ChartTypeListPanel();
        chartTypeListPanel.open(series.getSeriesType(), cell.getChartType(),is3D,isPlain);        
        gbl.setConstraints(chartTypeListPanel, c);
        content.add(chartTypeListPanel);
        
        c.insets = new Insets(0, 10, 0, 10);
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.gridy = 3;     
        gbl.setConstraints(stateDisplayer, c);
        content.add(stateDisplayer);
        
        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);
        //stateManager.ok();
    }
    
    private List<String> createNamesList(final AdsReportChartCell cell,final AdsReportChartSeries series){
        List<String> namesList=new ArrayList<>();
        if(cell!=null && series!=null){
            for(AdsReportChartSeries s:cell.getChartSeries()){
                if(s!=null && !s.equals(series)){
                    namesList.add(s.getName());
                }
            }
        }
        return namesList;
    }
    
    private void setSeriesName(final String name){
        if(checkName( name)){
            seriesName=name;
        }
    }    
    
    private boolean checkName(final String name){
        if(name==null || name.isEmpty() ){
            stateManager.error("Wrong series name!");
            return false;
        }else if(seriesNames.contains(name)){
            stateManager.error("Dublicate series name!");
            return false;
        }
        stateManager.ok();
        changeSupport.fireChange();
        return true;        
    }
    
    /*private void fillSeriesType(final EReportChartCellType chartType,boolean is3D,boolean isPlain){
        switch(chartType){
            case CATEGORY:
                if(isPlain){
                    cbSeriesType.addItem(EReportChartSeriesType.AREA);
                    cbSeriesType.addItem(EReportChartSeriesType.AREA_STACKED);
                    cbSeriesType.addItem(EReportChartSeriesType.BAR);                
                    cbSeriesType.addItem(EReportChartSeriesType.BAR_STACKED);                
                    cbSeriesType.addItem(EReportChartSeriesType.LINE);
                }
                if(is3D){
                    cbSeriesType.addItem(EReportChartSeriesType.BAR_3D);
                    cbSeriesType.addItem(EReportChartSeriesType.LINE_3D);
                    cbSeriesType.addItem(EReportChartSeriesType.BAR_STACKED_3D);
                }
                break;
            case XY:
                cbSeriesType.addItem(EReportChartSeriesType.XY_AREA);
                cbSeriesType.addItem(EReportChartSeriesType.XY_AREA_STACKED);
                cbSeriesType.addItem(EReportChartSeriesType.XY_BAR);
                cbSeriesType.addItem(EReportChartSeriesType.XY_BAR_STACKED);
                cbSeriesType.addItem(EReportChartSeriesType.XY_LINE);
                break;
            case PIE:
                if(isPlain){
                    cbSeriesType.addItem(EReportChartSeriesType.PIE); 
                } 
                if(is3D){
                    cbSeriesType.addItem(EReportChartSeriesType.PIE_3D); 
                }
                break;
        }
    }*/
    
    private void setupVerifiers() {
        nameTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(final JComponent input) {
                return nameTextField.getText()!=null && !nameTextField.getText().isEmpty();
            }
        });
    }
    
    EReportChartSeriesType getSeriesType(){
        return chartTypeListPanel.getSelectedSeriesType();
    }
    
    String getSeriesName(){
        return seriesName;
    }

    /*private static class Renderer implements ListCellRenderer<EReportChartSeriesType> {

        ListCellRenderer def;

        private Renderer(ListCellRenderer def) {
            this.def = def;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends EReportChartSeriesType> list, EReportChartSeriesType value, int index, boolean isSelected, boolean cellHasFocus) {
            return def.getListCellRendererComponent(list,  value.getName(), index, isSelected, cellHasFocus);
        }
    }*/

    
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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
