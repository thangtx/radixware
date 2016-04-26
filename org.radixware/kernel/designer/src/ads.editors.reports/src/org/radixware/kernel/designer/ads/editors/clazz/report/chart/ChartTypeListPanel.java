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

import java.awt.Component;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.EReportChartCellType;
import org.radixware.kernel.common.enums.EReportChartSeriesType;


public class ChartTypeListPanel extends javax.swing.JPanel {

    private EReportChartSeriesType curSeriesType;

    /**
     * Creates new form ChartTypeListPanel
     */
    public ChartTypeListPanel() {
        super();
        initComponents();
        seriesTypeList.setCellRenderer(new MyCellRenderer());
        final DefaultListModel<EReportChartSeriesType> model = new DefaultListModel<>();

        seriesTypeList.setModel(model);
        //jList1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        seriesTypeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        seriesTypeList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        seriesTypeList.setVisibleRowCount(-1);
        seriesTypeList.setFixedCellHeight(140);
        seriesTypeList.setFixedCellWidth(140);
        seriesTypeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent lse) {
                final Object selectedItem = seriesTypeList.getSelectedValue();
                if (selectedItem != null && selectedItem instanceof EReportChartSeriesType) {
                    curSeriesType = (EReportChartSeriesType) selectedItem;
                }
            }
        });
    }

    public EReportChartSeriesType getSelectedSeriesType() {
        return curSeriesType;
    }

    public void open(final EReportChartSeriesType curSeriesType, final EReportChartCellType chartType,final boolean is3D,final boolean isPlain) {
        fillSeriesType(chartType, is3D, isPlain);
        final DefaultListModel<EReportChartSeriesType> model = (DefaultListModel<EReportChartSeriesType>) seriesTypeList.getModel();
        final int index = model.indexOf(curSeriesType);
        if (index != -1) {
            seriesTypeList.setSelectedIndex(index);
        } else {
            seriesTypeList.setSelectedIndex(0);
        }
    }

    private void fillSeriesType(final EReportChartCellType chartType,final boolean is3D,final boolean isPlain) {
       final DefaultListModel<EReportChartSeriesType> model = (DefaultListModel<EReportChartSeriesType>) seriesTypeList.getModel();
        switch (chartType) {
            case CATEGORY:
                if (isPlain) {
                    model.addElement(EReportChartSeriesType.AREA);
                    model.addElement(EReportChartSeriesType.AREA_STACKED);
                    model.addElement(EReportChartSeriesType.BAR);
                    model.addElement(EReportChartSeriesType.BAR_STACKED);
                    model.addElement(EReportChartSeriesType.LINE);
                }
                if (is3D) {
                    model.addElement(EReportChartSeriesType.BAR_3D);
                    model.addElement(EReportChartSeriesType.LINE_3D);
                    model.addElement(EReportChartSeriesType.BAR_STACKED_3D);
                }
                break;
            case XY:
                model.addElement(EReportChartSeriesType.XY_AREA);
                model.addElement(EReportChartSeriesType.XY_AREA_STACKED);
                model.addElement(EReportChartSeriesType.XY_BAR);
                model.addElement(EReportChartSeriesType.XY_BAR_STACKED);
                model.addElement(EReportChartSeriesType.XY_LINE);
                break;
            case PIE:
                if (isPlain) {
                    model.addElement(EReportChartSeriesType.PIE);
                }
                if (is3D) {
                    model.addElement(EReportChartSeriesType.PIE_3D);
                }
                break;
        }
        //if(jList1.getSelectedIndex()<0){
        // seriesTypeList.setSelectedIndex(0);
        // }

    }

    /*class Model extends DefaultListModel{

     @Override
     public int indexOf(Object elem) {
     return vals.indexOf(elem);
     }

     @Override
     public Object lastElement() {
     return super.lastElement();
     }

     @Override
     public Object get(int index) {
     return vals.get(index);
     }
     private final List<EReportChartSeriesType> vals;
        
     Model(){ 
     vals=Arrays.asList(EReportChartSeriesType.values());            
     }
        
     }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        seriesTypeList = new javax.swing.JList<EReportChartSeriesType>();

        jScrollPane1.setViewportView(seriesTypeList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<EReportChartSeriesType> seriesTypeList;
    // End of variables declaration//GEN-END:variables

    /*class SeriesTypeItem  {

     @Override
     public String toString() {
     return chartType.getName();
     }
     private final EReportChartSeriesType chartType;
        
     SeriesTypeItem(final EReportChartSeriesType chartType){
     this.chartType=chartType;
     }
        
     Icon getIcon(){
     return AdsDefinitionIcon.getForReportChartSeriesType(chartType).getIcon(120);
     }
        
     EReportChartSeriesType getChartType(){
     return chartType;
     }
     }*/
    class MyCellRenderer extends JLabel implements ListCellRenderer<EReportChartSeriesType> {

        @Override
        public Component getListCellRendererComponent(
                final JList<? extends EReportChartSeriesType> list,
                final EReportChartSeriesType value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
            //SeriesTypeItem item=(SeriesTypeItem)value;
            //final EReportChartSeriesType item =  value;
            if (value != null) {
                final String s = value.getName();
                setText(s);
                setVerticalTextPosition(JLabel.BOTTOM);
                setHorizontalTextPosition(JLabel.CENTER);
            }
            final Icon icon = AdsDefinitionIcon.getForReportChartSeriesType(value).getIcon(120);
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
            //this.setSize(200, 200);
            return this;
        }
    }
}
