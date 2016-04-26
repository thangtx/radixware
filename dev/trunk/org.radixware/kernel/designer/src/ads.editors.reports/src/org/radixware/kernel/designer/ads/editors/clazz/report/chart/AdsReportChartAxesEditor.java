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
 * AdsReportChartAxisEditor.java
 *
 * Created on May 29, 2012, 12:43:52 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
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
import org.radixware.kernel.designer.ads.editors.clazz.report.FormattedCellPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


public class AdsReportChartAxesEditor extends ChangeSupportPanel {

    private final LocalizingEditorPanel axisTitle=new LocalizingEditorPanel();
    private final JCheckBox cbAutoRangeIncludesZero=new JCheckBox();
    private final JCheckBox cbVisibility=new JCheckBox();
    private final AxisLocationPanel axisLocationPanel=new AxisLocationPanel();
    private AbstractAxisLabelPositionPanel axisLabelLocationPanel;

    private Map<AdsReportChartAxis,HandleInfo> handleInfoMap=new HashMap<>();
    private final Map<Id,IMultilingualStringDef> mlStrings;
    private final AdsReportClassDef report;
    private AdsReportChartAxis axis;

    /** Creates new form AdsReportChartAxisEditor */
    public AdsReportChartAxesEditor(final AdsReportChartCell cell, final AdsReportClassDef report,final EReportChartAxisType axisType,AdsChartDataInfo chartDataInfo,Map<Id,IMultilingualStringDef> mlStrings) {
        initComponents();
        this.report=report;
        this.mlStrings=mlStrings;
        AdsReportChartAxes axes=axisType==EReportChartAxisType.DOMAIN_AXIS ? cell.getDomainAxes():cell.getRangeAxes();
        int axisIndex=chartDataInfo.getAxisIndex();
        if(axisIndex>=axes.size())
            axisIndex=0;
        this.axis=axes.get(axisIndex);
         
        EValType valType=null;
        AdsPropertyDef prop=chartDataInfo.findProperty();
        if(prop!=null && prop.getValue()!=null && prop.getValue().getType()!=null){
            valType=prop.getValue().getType().getTypeId();
            //if(type==EValType.INT && type==EValType.NUM && type==EValType.DATE_TIME){
            //    valType=prop.getValue().getType().getTypeId();
            //}
        }
        createUi(cell,axisType,valType);
    }

    int getAxisIndex(){
       return axis.getIndex();
    }

    private void createUi(final AdsReportChartCell cell,final EReportChartAxisType axisType,EValType valType){
        JPanel rightPanel=new JPanel();
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));

        content = new javax.swing.JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);
        c.insets = new Insets(0, 2, 10, 2);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        gbl.setConstraints(axisTitle, c);
        content.add(axisTitle);

        
        c.gridx = 0;
        c.gridy = 1;
        if(!(axisType==EReportChartAxisType.DOMAIN_AXIS && cell.getChartType()==EReportChartCellType.CATEGORY)){            
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 1.0;
            FormattedCellPanel formatPanel =new FormattedCellPanel(axis.getFormat(),valType,cell.isReadOnly());            
            gbl.setConstraints(formatPanel, c);
            content.add(formatPanel);
            c.gridy = 2;
        }
        
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 1.0;
        gbl.setConstraints(axisLocationPanel, c);
        content.add(axisLocationPanel);

        if(axisType==EReportChartAxisType.DOMAIN_AXIS && cell.getChartType()==EReportChartCellType.CATEGORY){
            axisLabelLocationPanel=new CategoryAxisLabelPosition();
        }else{
            axisLabelLocationPanel=new ValueAxisLabelPosition();
        }
        //c.gridx = 0;
        c.gridy = c.gridy+1;
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 1.0;
        gbl.setConstraints(axisLabelLocationPanel, c);
        content.add(axisLabelLocationPanel);

        int gridy=c.gridy+1;
        if(cell.getChartType()==EReportChartCellType.XY || axisType==EReportChartAxisType.RANGE_AXIS){
            c.gridx = 0;
            c.gridy = gridy;
            gridy++;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 1.0;
            String title=NbBundle.getMessage(AdsReportChartAxesEditor.class, "AdsReportChartAxesEditor.cbAutoRangeIncludesZero.text");
            cbAutoRangeIncludesZero.setText(title);
            cbAutoRangeIncludesZero.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(axis!=null){
                        axis.setIsAutoRangeIncludeZero(cbAutoRangeIncludesZero.isSelected());
                    }
                }
            });
            gbl.setConstraints(cbAutoRangeIncludesZero, c);
            content.add(cbAutoRangeIncludesZero);
        }

        //c.gridx = 0;
        c.gridy = gridy;
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 1.0;
        String title=NbBundle.getMessage(AdsReportChartAxesEditor.class, "AdsReportChartAxesEditor.cbVisibility.text");
        cbVisibility.setText(title);
        cbVisibility.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(axis!=null){
                        axis.setIsVisible(cbVisibility.isSelected());
                    }
                }
        });
        gbl.setConstraints(cbVisibility, c);
        content.add(cbVisibility);

        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(content, BorderLayout.NORTH);

        splitter.setRightComponent(rightPanel);

        AxisListPanel seriesPanel=new AxisListPanel(this, cell, axisType, axis.getIndex());
        seriesPanel.setBorder(new EmptyBorder(10,10,10,10));
        splitter.setLeftComponent(seriesPanel);
    }

    @SuppressWarnings("deprecation")
    public void setAxis(final AdsReportChartAxis axis){
        this.axis=axis;
        String title=NbBundle.getMessage(AdsReportChartAxesEditor.class, "AdsReportChartAxesEditor.axisTitle.text");
        axisTitle.setTitle(title);
        openTitlePanel(axis);
        if(axis!=null){
            setPanelEnabled(true);
            cbAutoRangeIncludesZero.setSelected(axis.isAutoRangeIncludeZero());
            cbVisibility.setSelected(axis.isVisible());
            axisLocationPanel.setAxis(axis);
            axisLabelLocationPanel.setAxis(axis);
            stateManager.ok();
        }else{
            setPanelEnabled(false);
            stateManager.error("select axis");
        }
        changeSupport.fireChange();
    }

    private void setPanelEnabled(boolean enable){
        axisTitle.setReadonly(!enable);
    }

    private void openTitlePanel(final AdsReportChartAxis axis){
        boolean isReadonly=axis==null;
        axisTitle.setReadonly(isReadonly);
        if(handleInfoMap.containsKey(axis)){
            axisTitle.open(handleInfoMap.get(axis));
        }else{
            HandleInfo handleInfo=createTitleHandleInfo(axis);
            axisTitle.open(handleInfo);
            handleInfoMap.put(axis, handleInfo);
        }
    }

    private HandleInfo createTitleHandleInfo(final AdsReportChartAxis axis){
        return new ChartEditorHandleInfo(report,mlStrings) {

        @Override
        public Id getTitleId() {
            return axis!=null ? axis.getTitleId():null;
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (axis != null) {
               if (multilingualStringDef != null) {
                   axis.setTitleId(multilingualStringDef.getId());
               }else{
                   axis.setTitleId(null);
               }
            }
        }

        /*@Override
        public AdsDefinition getAdsDefinition() {
            return report;
        }

        @Override
         public AdsMultilingualStringDef getAdsMultilingualStringDef(){
             if(mlStrings!=null && axis!=null && axis.getTitleId()!=null){
                 return mlStrings.get(axis.getTitleId());
             }
             return super.getAdsMultilingualStringDef();
         }

        @Override
        protected void addAdsMultilingualStringDef(AdsMultilingualStringDef adsMultilingualStringDef) {
            if(adsMultilingualStringDef!=null){
                if(mlStrings!=null) {
                    mlStrings.put(adsMultilingualStringDef.getId(), adsMultilingualStringDef);
                }else {
                    super.addAdsMultilingualStringDef(adsMultilingualStringDef);
                }
            }
        }

        @Override
        public void removeAdsMultilingualStringDef() {
            AdsMultilingualStringDef stringDef = this.getAdsMultilingualStringDef();
            if(stringDef != null && mlStrings!=null && mlStrings.containsKey(stringDef.getId())){
                mlStrings.remove(stringDef.getId());
            }
            super.removeAdsMultilingualStringDef();
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
        }*/
        };
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitter = new javax.swing.JSplitPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitter, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitter, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane splitter;
    // End of variables declaration//GEN-END:variables
}
