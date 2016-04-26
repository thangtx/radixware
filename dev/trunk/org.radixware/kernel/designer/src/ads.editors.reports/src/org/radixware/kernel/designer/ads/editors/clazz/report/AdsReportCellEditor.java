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

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportExpressionCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportTextCell;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.chart.AdsReportChartAppearance;
import org.radixware.kernel.designer.ads.editors.clazz.report.chart.AdsReportChartCellEditor;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


class AdsReportCellEditor extends RadixObjectModalEditor<AdsReportCell> {

    private enum ETab {

        CELL, CHART_APPEARANCE, APPEARANCE, ADDING
    };
    private final AdsReportCellAppearanceEditor appearanceEditor;
    private AdsReportChartAppearance chartAppearancePanel=null;
    private final JmlEditor jmlEditor;
    private final JPanel cellEditor;
    private static ETab tab = ETab.CELL;
    private volatile boolean init = false;

    /** Creates new form AdsReportCellEditor */
    protected AdsReportCellEditor(final AdsReportCell cell) {
        super(cell);
        init = true;
        initComponents();
        
        appearanceEditor = new AdsReportCellAppearanceEditor(cell);
        jmlEditor = new JmlEditor();
        switch (cell.getCellType()) {
            case PROPERTY:
                cellEditor = new AdsReportPropertyCellEditor((AdsReportPropertyCell) cell);
                break;
            case TEXT:
                cellEditor = new AdsReportTextCellEditor((AdsReportTextCell) cell);
                break;
            case SPECIAL:
                cellEditor = new AdsReportSpecialCellEditor((AdsReportSpecialCell) cell);
                break;
            case SUMMARY:
                cellEditor = new AdsReportSummaryCellEditor((AdsReportSummaryCell) cell);
                break;
            case EXPRESSION:
                cellEditor = new AdsReportExpressionCellEditor((AdsReportExpressionCell) cell);
                break;
            case IMAGE:
                cellEditor = new AdsReportImageCellEditor((AdsReportImageCell) cell);
                break;
            case DB_IMAGE:
                cellEditor = new AdsReportDbImageCellEditor((AdsReportDbImageCell) cell);
                break;
            case CHART:
                //AdsReportChartCell chartCell=(AdsReportChartCell)cell;
                //if(chartCell.getChartType()==EReportChartCellType.PIE){
                //    cellEditor = new AdsReportPieChartPanel((AdsReportChartCell)cell);
                //}else{
                    cellEditor = new AdsReportChartCellEditor((AdsReportChartCell)cell);
                //}
                break;
            default:
                cellEditor = new JPanel();
                break;
        }

        if (cell.getCellType() == EReportCellType.EXPRESSION) {
            tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_EXPRESSION"),
                    RadixWareIcons.JAVA.JAVA.getIcon(), cellEditor);
        } else if(cell.getCellType() == EReportCellType.CHART/* && ((AdsReportChartCell)cell).getChartType()!=EReportChartCellType.PIE*/){
            tabbedPane.addTab("Chart Dataset"/*NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_GENERAL")*/,
                    RadixWareIcons.DATABASE.DB_NAME.getIcon(), cellEditor);
            chartAppearancePanel= new AdsReportChartAppearance();
            chartAppearancePanel.open((AdsReportChartCell)cell,null,cell.getOwnerReport(),null);
            chartAppearancePanel.setBorder(new EmptyBorder(10,10,10,10));
            tabbedPane.addTab("Chart Appearance"/*NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_GENERAL")*/,                    
                    RadixWareIcons.EDIT.PROPERTIES.getIcon(),chartAppearancePanel);
        }else {
            tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_GENERAL"),
                    RadixWareIcons.EDIT.PROPERTIES.getIcon(), cellEditor);
        }
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_APPEARANCE"),
                AdsDefinitionIcon.COLOR_SCHEME.getIcon(), appearanceEditor);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_ADDING"),
                RadixWareIcons.JAVA.JAVA.getIcon(), jmlEditor);

        //jmlEditor.open(cell.getOnAdding());

        switch (tab) {
            case CELL:
                tabbedPane.setSelectedComponent(cellEditor);
                break;
            case CHART_APPEARANCE:
                if(chartAppearancePanel==null){
                    tabbedPane.setSelectedComponent(appearanceEditor);                                    
                }else{
                    tabbedPane.setSelectedComponent(chartAppearancePanel);    
                }  
                break;
            case APPEARANCE:
                tabbedPane.setSelectedComponent(appearanceEditor);
                break;
            case ADDING:
                tabbedPane.setSelectedComponent(jmlEditor);
                break;
        }

        this.setComplete(true);
        init = false;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public boolean open(final OpenInfo openInfo) {
        jmlEditor.open(getRadixObject().getOnAdding(), openInfo);
        if (cellEditor instanceof AdsReportExpressionCellEditor) {
            ((AdsReportExpressionCellEditor) cellEditor).open(openInfo);
        }
        return super.open(openInfo);
    }

    @Override
    public String getTitle() {
        return getRadixObject().getTypeTitle() + " Editor";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        if (init) {
            return;
        }
        if (tabbedPane.getSelectedComponent() == cellEditor) {
            tab = ETab.CELL;
        }else if(tabbedPane.getSelectedComponent() == chartAppearancePanel){
            tab = ETab.CHART_APPEARANCE;
            chartAppearancePanel.updateFonts(null);
        }else if (tabbedPane.getSelectedComponent() == appearanceEditor) {
            tab = ETab.APPEARANCE;
        } else if (tabbedPane.getSelectedComponent() == jmlEditor) {
            tab = ETab.ADDING;
        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    @Override
    protected void apply() {
//        appearanceEditor.apply();
    }

    @Override
    public void onClosed() {
        EditorsManager.getDefault().updateEditorIfOpened(getRadixObject().getOwnerReport()); // apply changes in MLS
    }

    @Override
    public void update() {
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsReportCell> {

        @Override
        public RadixObjectEditor<AdsReportCell> newInstance(final AdsReportCell cell) {
            return new AdsReportCellEditor(cell);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
