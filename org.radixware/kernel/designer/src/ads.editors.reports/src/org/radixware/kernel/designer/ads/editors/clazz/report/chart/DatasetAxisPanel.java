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
 * AxisDatasetPanel.java
 *
 * Created on May 12, 2012, 12:48:47 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.PropertyValue;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsChartDataInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell.AdsReportChartAxes;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EReportChartAxisType;
import org.radixware.kernel.common.enums.EReportChartCellType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.editors.clazz.report.CsvExportColumnPanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;


public class DatasetAxisPanel extends javax.swing.JPanel {    
    private javax.swing.JLabel propertyLabel = new javax.swing.JLabel(NbBundle.getMessage(CsvExportColumnPanel.class, "CsvExportColumnPanel-PropertyLabel")+":");
    private javax.swing.JLabel lbAxis = new javax.swing.JLabel(NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.lbAxis.text"));
    private DefinitionLinkEditPanel propTextField = new DefinitionLinkEditPanel();
    private ExtendableTextField axisTextField = new ExtendableTextField(true);
    
    private AdsChartDataInfo axisDataSetInfo=null;
    private AdsReportClassDef report;
    private AdsReportChartCell cell;
    private final EReportChartAxisType axisType;
    private Map<Id,IMultilingualStringDef> mlStrings=null;


    /** Creates new form AxisDatasetPanel */
    public DatasetAxisPanel(final EReportChartAxisType axisType) {
        initComponents();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gbl);
        this.axisType=axisType;
        final String title=axisType==EReportChartAxisType.DOMAIN_AXIS ? 
                NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.domainValues"):
                NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.rangeValues");
        TitledBorder border=new TitledBorder(title);        
        this.setBorder(border);

        setupVerifiers();

        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
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
           public void stateChanged(final ChangeEvent e) {
                if(axisDataSetInfo==null)
                    return;
                if(axisDataSetInfo!=null){
                    final Definition selectedDef = propTextField.getDefinition();
                    if (selectedDef == null) {
                         axisDataSetInfo.setPropId(null);
                    } else {
                        AdsPropertyDef prop=(AdsPropertyDef)selectedDef;
                        if(axisType== EReportChartAxisType.DOMAIN_AXIS && cell!=null && cell.getDomainAxes()!=null && 
                            cell.getDomainAxes().size()>axisDataSetInfo.getAxisIndex()){
                            AdsReportChartAxis axis=cell.getDomainAxes().get(axisDataSetInfo.getAxisIndex());
                            boolean isDataAxis=prop.getValue()!=null && prop.getValue().getType()!=null && 
                                               prop.getValue().getType().getTypeId()==EValType.DATE_TIME;
                            axis.setIsDateAxis(isDataAxis);
                        }
                        axisDataSetInfo.setPropId(selectedDef.getId());
                    }
                }
            }
         });

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        c.weightx = 0.0;
        gbl.setConstraints(lbAxis, c);
        this.add(lbAxis);

        c.insets = new Insets(0, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;


        axisTextField.setEditable(false);
        axisTextField.addButton("...");
        String text="";
        axisTextField.setTextFieldValue(text);
        axisTextField.getButtons().get(0).addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(final ActionEvent e) {
                AdsReportChartAxesEditor panel=new AdsReportChartAxesEditor(cell,report, axisType, axisDataSetInfo,mlStrings);
                panel.setBorder(new EmptyBorder(10,10,10,10));
                ChartSeriesDialog dialog=new ChartSeriesDialog(panel,title) ;
                dialog.invokeModalDialog();
                boolean res=dialog.isOK();
                if(res){
                    axisDataSetInfo.setAxisIndex(panel.getAxisIndex());
                    cell.setEditState(EEditState.MODIFIED);
                    AdsReportChartAxis axes = getCurrentAxis();
                    axisTextField.setTextFieldValue(axes.getName());
                }
            }
         });
        gbl.setConstraints(axisTextField, c);
        this.add(axisTextField);
        setPanelEnabled(false);
    }
    
    public void open(final AdsReportChartCell cell,final AdsReportClassDef report,
                     final Map<Id,IMultilingualStringDef> mlStrings){
        TitledBorder border=(TitledBorder)this.getBorder();  
        if(cell.getChartType()==EReportChartCellType.CATEGORY){
            String title=axisType==EReportChartAxisType.DOMAIN_AXIS?
                    NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.categories"): 
                    NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.values");
            border.setTitle(title);
        }else{
            String title=axisType==EReportChartAxisType.DOMAIN_AXIS?
                    "X "+NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.values"):
                    "Y "+NbBundle.getMessage(DatasetAxisPanel.class, "DatasetAxisPanel.values");
            border.setTitle(title);
        }
        this.report=report;
        this.cell=cell;
        this.mlStrings=mlStrings;
        List<AdsPropertyDef> list=new ArrayList<>();
        if(report!=null){
            List<AdsPropertyDef> allProps =report.getProperties().get( EScope.LOCAL);
            for(AdsPropertyDef prop:allProps){                
                //if((prop.getNature()==EPropNature.DYNAMIC || prop.getNature()==EPropNature.FIELD ||
                //    prop.getNature()==EPropNature.SQL_CLASS_PARAMETER ) ){
                if(prop.getValue()!=null && checkType( prop.getValue())){
                    list.add(prop);
                }
            }
        }
        RadixObjectsUtils.sortByName(list);
        propTextField.setComboBoxValues(list, true);  
        propTextField.open(null, null);        
    }
    
    private boolean checkType(final PropertyValue val){
        if(cell.getChartType()==EReportChartCellType.XY){
            if(axisType==EReportChartAxisType.DOMAIN_AXIS){
                return (val.getType().getTypeId()==EValType.DATE_TIME || val.getType().getTypeId()==EValType.INT ||
                       val.getType().getTypeId()==EValType.NUM);
            }
            return (val.getType().getTypeId()==EValType.INT || val.getType().getTypeId()==EValType.NUM);
        }else{
            if(axisType==EReportChartAxisType.RANGE_AXIS){
                return (val.getType().getTypeId()==EValType.INT || val.getType().getTypeId()==EValType.NUM);
            }
            return true;           
        }
    }
    
    private void setupVerifiers() {
        propTextField.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                return propTextField.getDefinitionId()!=null;
            }
        });
    }
    
    public void setAxisDataInfo(final AdsChartDataInfo axisDataSetInfo){
        this.axisDataSetInfo=axisDataSetInfo;
        if(axisDataSetInfo!=null){
            final Id propId=axisDataSetInfo.getPropId();
            AdsPropertyDef prop=axisDataSetInfo.findProperty();
            if(prop==null && report!=null)
                prop=report.getProperties().findById(propId,EScope.LOCAL).get();
            propTextField.open(prop, propId);
            
            
            AdsReportChartAxis axes = getCurrentAxis();
            if(axes!=null)
                axisTextField.setTextFieldValue(axes.getName());
        }
        setPanelEnabled(true);
    }
    
    private AdsReportChartAxis getCurrentAxis(){
        AdsReportChartAxis currentAxis=null;
        AdsReportChartAxes axes = axisType==EReportChartAxisType.DOMAIN_AXIS?
                                      cell.getDomainAxes() : cell.getRangeAxes();
        if(axes==null){
            axes=new AdsReportChartAxes();
        }
        if(axes.isEmpty()){
            AdsReportChartAxis axis=new AdsReportChartAxis(cell,axisType.getValue(),axisType); 
            axes.add(axis);
        }
        int axisIndex=axisDataSetInfo.getAxisIndex();
        if(/*axes!=null &&*/ axisIndex<axes.size())
            currentAxis = axes.get(axisIndex);
        return currentAxis;
    }
    
    public final void setPanelEnabled(final boolean enabled){
        if(!enabled)
            propTextField.open(null, null);
        propertyLabel.setEnabled(enabled); 
        propTextField.setEnabled(enabled);
        
        lbAxis.setEnabled(enabled);          
        axisTextField.setEnabled(enabled);
        this.setEnabled(enabled);
    }
    
    public void clearPanel(){
        propTextField.open(null, null);
        axisTextField.setTextFieldValue("");
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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
