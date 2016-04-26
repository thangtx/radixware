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
 * BaseChartCellEditor.java
 *
 * Created on Jun 15, 2012, 11:48:38 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.PropertyValue;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


public abstract class BaseChartCellEditor extends javax.swing.JPanel {

    protected LocalizingEditorPanel seriesTitle = new LocalizingEditorPanel();
    protected JCheckBox cbShowItemLabels = new JCheckBox();
    private Map<AdsReportChartSeries, HandleInfo> handleInfoMap = new HashMap<>();
    private Map<Id, IMultilingualStringDef> mlStrings = null;
    protected AdsReportClassDef report;
    protected AdsReportChartSeries series = null;

    /**
     * Creates new form BaseChartCellEditor
     */
    public BaseChartCellEditor() {
        initComponents();
        String title = NbBundle.getMessage(BaseChartCellEditor.class, "BaseChartCellEditor.cbShowItemLabels.text");
        cbShowItemLabels.setText(title);
        cbShowItemLabels.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (series != null) {
                    series.setIsShowItemLabel(cbShowItemLabels.isSelected());
                }
            }
        });
    }

    public void open(final AdsReportChartCell cell, final AdsReportClassDef report, final Map<Id, IMultilingualStringDef> mlStrings) {
        this.report = report;
        this.mlStrings = mlStrings;
        openTitlePanel();
    }

    @SuppressWarnings("deprecation")
    protected void selectSeries(final AdsReportChartSeries series) {
        this.series = series;
        String title = NbBundle.getMessage(BaseChartCellEditor.class, "BaseChartCellEditor.seriesTitle.text");
        seriesTitle.setTitle(title);
        openTitlePanel();
        setPanelEnabled(series != null);
        if (series != null) {
            cbShowItemLabels.setSelected(series.isShowItemLabel());
        }
    }

    public static List<AdsPropertyDef> getPropList(final AdsReportClassDef report, boolean checkType) {
        List<AdsPropertyDef> list = new ArrayList<>();
        if (report != null) {
            List<AdsPropertyDef> allProps = report.getProperties().get(EScope.LOCAL);
            for (AdsPropertyDef prop : allProps) {
                //if((prop.getNature()==EPropNature.DYNAMIC || prop.getNature()==EPropNature.FIELD ||
                //    prop.getNature()==EPropNature.SQL_CLASS_PARAMETER ) ){
                if (prop.getValue() != null && (!checkType || checkType && checkType(prop.getValue()))) {
                    list.add(prop);
                }
            }
        }
        RadixObjectsUtils.sortByName(list);
        return list;
    }

    private static boolean checkType(PropertyValue val) {
        return (val.getType().getTypeId() == EValType.INT || val.getType().getTypeId() == EValType.NUM);
    }

    protected void clearPanel() {
        cbShowItemLabels.setSelected(false);
    }

    protected void setPanelEnabled(boolean enable) {
        seriesTitle.setReadonly(!enable);
        cbShowItemLabels.setEnabled(enable);
    }

    private void openTitlePanel() {
        boolean isReadonly = series == null;
        seriesTitle.setReadonly(isReadonly);
        if (handleInfoMap.containsKey(series)) {
            seriesTitle.open(handleInfoMap.get(series));
        } else {
            HandleInfo handleInfo = createTitleHandleInfo(series);
            seriesTitle.open(handleInfo);
            handleInfoMap.put(series, handleInfo);
        }
    }

    private HandleInfo createTitleHandleInfo(final AdsReportChartSeries series) {
        return new ChartEditorHandleInfo(report, mlStrings) {
            @Override
            public Id getTitleId() {
                return series != null ? series.getTitleId() : null;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (series != null) {
                    if (multilingualStringDef != null) {
                        series.setTitleId(multilingualStringDef.getId());
                    } else {
                        series.setTitleId(null);
                    }
                }
            }
        };
        /*  @Override
         public AdsMultilingualStringDef getAdsMultilingualStringDef(){
         AdsMultilingualStringDef res=super.getAdsMultilingualStringDef();
         if(res==null && mlStrings!=null)
         res=mlStrings.get(getTitleId());
         return  res;
         }

         @Override
         public AdsDefinition getAdsDefinition() {
         return report;
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
