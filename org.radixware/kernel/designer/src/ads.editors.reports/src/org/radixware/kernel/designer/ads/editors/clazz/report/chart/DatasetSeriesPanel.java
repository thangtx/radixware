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
 * SeriesDataSetPanel.java
 *
 * Created on May 12, 2012, 11:32:46 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsChartDataInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroups;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.report.CsvExportColumnPanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;



public class DatasetSeriesPanel extends javax.swing.JPanel {
    private final JLabel label=new JLabel(NbBundle.getMessage(DatasetSeriesPanel.class, "DatasetSeriesPanel.label.text"));
    private final javax.swing.JLabel propertyLabel = new javax.swing.JLabel(NbBundle.getMessage(CsvExportColumnPanel.class, "CsvExportColumnPanel-PropertyLabel")+":");
    private final javax.swing.JLabel lbDatasetCollectLevel = new javax.swing.JLabel(/*NbBundle.getMessage(CsvExportColumnPanel.class, "CsvExportColumnPanel-ExternalName-Label")*/"Group Level"+":");
    private final DefinitionLinkEditPanel propTextField = new DefinitionLinkEditPanel();
    private final javax.swing.JComboBox<String> cbGroupLevel = new javax.swing.JComboBox<>();
    private AdsReportChartSeries series=null;    
    private AdsReportClassDef report;

    /** Creates new form SeriesDataSetPanel */
    public DatasetSeriesPanel() { 
        initComponents();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gbl);
        TitledBorder border=new TitledBorder("Series info");
        this.setBorder(border);

        setupVerifiers();
        
        //JLabel label=new JLabel("The series property is used for representing several sets of data in chart.");// Series are represented by different colors.
        c.insets = new Insets(5, 10, 0, 10);
        c.gridwidth=2;
        c.fill = GridBagConstraints.HORIZONTAL;       
        gbl.setConstraints(label, c);
        this.add(label);
        
        c.gridx = 0;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.weightx = 0.0;
        gbl.setConstraints(propertyLabel, c);
        this.add(propertyLabel);

        c.insets = new Insets(10, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;
        gbl.setConstraints(propTextField, c);
        this.add(propTextField);
        propTextField.setComboMode();
        propTextField.addChangeListener(new ChangeListener() {

           @Override
            public void stateChanged(ChangeEvent e) {
                if(series!=null && series.getSeriesData()!=null){
                    AdsChartDataInfo seriesDataSetInfo=series.getSeriesData();
                    final Definition selectedDef = propTextField.getDefinition();
                    if (selectedDef == null) {
                         seriesDataSetInfo.setPropId(null);
                    } else {
                         seriesDataSetInfo.setPropId(selectedDef.getId());
                    }
                }
            }
        });
        
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 10, 10, 10);
        c.weightx = 0.0;
        gbl.setConstraints(lbDatasetCollectLevel, c);
        this.add(lbDatasetCollectLevel);

        c.insets = new Insets(0, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;
        
        cbGroupLevel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent ae) {
                if ("comboBoxChanged".equals(ae.getActionCommand())) {
                    // User has selected an item; it may be the same item
                    if(series!=null){
                        series.setGroupIndex(cbGroupLevel.getSelectedIndex()-1);                        
                    }                    
                }
            }
        });

        
        gbl.setConstraints(cbGroupLevel, c);
        this.add(cbGroupLevel);
        setPanelEnabled(false);
    } 
    
    public void open(final AdsReportClassDef report){
        this.report=report;
        List<AdsPropertyDef> list=BaseChartCellEditor.getPropList(report, false);
        propTextField.setComboBoxValues(list, true);  
        propTextField.open(null, null);
        
        for(String s:getGroups(report)){
             cbGroupLevel.addItem(s);
        }
    }    
   
    
    private List<String> getGroups(final AdsReportClassDef report){
        List<String> res=new ArrayList<>();
        res.add("details");
        if(report!=null){
            AdsReportGroups groups=report.getForm().getGroups();
            if(groups!=null && !groups.isEmpty()){
                for(AdsReportGroup g:groups){
                   res.add(g.getName()); 
                }
            }
        }
        return res;
    }
    
    private void setupVerifiers() {
        propTextField.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                return propTextField.getDefinitionId()!=null;
            }
        });
    }
    
    public void setChartSeries(final AdsReportChartSeries series){
        this.series=series;
        AdsChartDataInfo seriesDataSetInfo=series.getSeriesData();
        if(seriesDataSetInfo!=null){
            final Id propId=seriesDataSetInfo.getPropId();
            AdsPropertyDef prop=seriesDataSetInfo.findProperty();
            if(prop==null && report!=null)
                prop=report.getProperties().findById(propId,EScope.LOCAL).get();
            propTextField.open( prop, propId); 
            if(series.getGroupIndex()+1<cbGroupLevel.getItemCount()){
                cbGroupLevel.setSelectedIndex(series.getGroupIndex()+1);
            }
        }
        setPanelEnabled(true);
    }
    
    public final void setPanelEnabled(final boolean enabled){
        if(!enabled){
            propTextField.open(null, null);
        }
        propertyLabel.setEnabled(enabled); 
        lbDatasetCollectLevel.setEnabled(enabled); 
        propTextField.setEnabled(enabled); 
        cbGroupLevel.setEnabled(enabled); 
        label.setEnabled(enabled); 
        this.setEnabled(enabled);
    }
    
    protected void clearPanel(){
        propTextField.open(null, null);
        cbGroupLevel.setSelectedIndex(0);
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
            .addGap(0, 374, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 97, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
