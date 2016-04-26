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

package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsChartDataInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;


public class DatasetPieChartPanel extends javax.swing.JPanel {
    private DatasetSeriesPanel seriesInfoPanel;
    private javax.swing.JPanel datasetPanel;
    private final javax.swing.JLabel keyLabel = new javax.swing.JLabel(NbBundle.getMessage(DatasetPieChartPanel.class, "DatasetPieChartPanel.keyLabel.text"));
    private final javax.swing.JLabel valLabel = new javax.swing.JLabel(NbBundle.getMessage(DatasetPieChartPanel.class, "DatasetPieChartPanel.valLabel.text"));
    private final DefinitionLinkEditPanel keyPropTextField = new DefinitionLinkEditPanel();
    private final DefinitionLinkEditPanel valPropTextField = new DefinitionLinkEditPanel();    
    private AdsReportClassDef report;
    private AdsReportChartSeries series;

    /**
     * Creates new form DatasetPieChartPanel
     */
    public DatasetPieChartPanel() {
        super();
        initComponents();
        createUi();
    }
    
    private void createUi(){
        final GridBagLayout gbl = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gbl);
        
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 1.0;
        seriesInfoPanel=new DatasetSeriesPanel(); 
        gbl.setConstraints(seriesInfoPanel, c);
        this.add(seriesInfoPanel);        
          
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 1.0;
        datasetPanel=createDatasetPanel();
        gbl.setConstraints(datasetPanel, c);
        this.add(datasetPanel);
        setPanelEnabled(false);
    }
    
    private javax.swing.JPanel createDatasetPanel(){
        final javax.swing.JPanel panel=new javax.swing.JPanel();
        final TitledBorder border=new TitledBorder(NbBundle.getMessage(DatasetPieChartPanel.class, "DatasetPieChartPanel.dataset"));
        panel.setBorder(border);
        
        final GridBagLayout gbl = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gbl);
        
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        gbl.setConstraints(keyLabel, c);
        panel.add(keyLabel);

        c.insets = new Insets(10, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;
        gbl.setConstraints(keyPropTextField, c);
        panel.add(keyPropTextField);
        keyPropTextField.setComboMode();
        keyPropTextField.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                if(series!=null){
                    if(series.getDomainData()!=null){
                        final Definition selectedDef = keyPropTextField.getDefinition();
                        if (selectedDef == null) {
                            series.getDomainData().setPropId(null);
                        } else {
                            series.getDomainData().setPropId(selectedDef.getId());
                        }
                    }
                 }
             }
         });              
        
        c.insets = new Insets(0, 10, 10, 10);
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        gbl.setConstraints(valLabel, c);
        panel.add(valLabel);

        c.insets = new Insets(0, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;
        gbl.setConstraints(valPropTextField, c);
        panel.add(valPropTextField);
        valPropTextField.setComboMode();
        valPropTextField.addChangeListener(new ChangeListener() {

           @Override
           public void stateChanged(final ChangeEvent e) {
                if(series!=null){
                    if(series.getRangeData()!=null){
                        final Definition selectedDef = valPropTextField.getDefinition();
                        if (selectedDef == null) {
                             series.getRangeData().setPropId(null);
                        } else {
                             series.getRangeData().setPropId(selectedDef.getId());
                        }
                    }
                }
            }
         });
        
        return panel;        
    }
    
    
    public void open(final AdsReportClassDef report){
        this.report=report;
        List<AdsPropertyDef> list=BaseChartCellEditor.getPropList(report,false);
        keyPropTextField.setComboBoxValues(list, true);  
        keyPropTextField.open(null, null); 
        
        list=BaseChartCellEditor.getPropList(report,true);       
        valPropTextField.setComboBoxValues(list, true);  
        valPropTextField.open(null, null);        
        seriesInfoPanel.open(report);
    }
    
    protected void selectSeries(final AdsReportChartSeries series){
       this.series=series;
       if(series.getDomainData()!=null){
           final Id propId=series.getDomainData().getPropId();
           keyPropTextField.open(getProp(series.getDomainData()), propId);
       }
       if(series.getRangeData()!=null){
           final Id propId=series.getRangeData().getPropId();
           valPropTextField.open(getProp(series.getRangeData()), propId);
       } 
       seriesInfoPanel.setChartSeries(series);
    }
    
    protected void clearPanel() {
        keyPropTextField.open(null, null);
        valPropTextField.open(null, null);
        seriesInfoPanel.clearPanel();
    }
      
    private AdsPropertyDef getProp(final AdsChartDataInfo  dataInfo){
        final Id propId=dataInfo.getPropId();
        AdsPropertyDef prop=dataInfo.findProperty();
        if(prop==null && report!=null)
             prop=report.getProperties().findById(propId,ExtendableDefinitions.EScope.LOCAL).get();
        return prop;
    }
    
    protected final void setPanelEnabled(final boolean enable){
        keyLabel.setEnabled(enable);
        valLabel.setEnabled(enable);
        keyPropTextField.setEnabled(enable);        
        valPropTextField.setEnabled(enable);
        seriesInfoPanel.setPanelEnabled(enable);
        datasetPanel.setEnabled(enable);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
