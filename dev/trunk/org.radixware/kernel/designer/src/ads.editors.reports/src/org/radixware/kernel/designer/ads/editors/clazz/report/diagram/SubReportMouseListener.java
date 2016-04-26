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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


class SubReportMouseListener extends WidgetMouseListener {

    private final AdsSubReportWidget subReportWidget;

    public SubReportMouseListener(final AdsSubReportWidget subReportWidget) {
        super();
        this.subReportWidget = subReportWidget;
    }

    @Override
    protected void select(final boolean expand) {
        final AdsReportBandWidget bandWidget = subReportWidget.getOwnerBandWidget();
        if (!bandWidget.isSelected()) {
            AdsReportWidgetUtils.selectBand(bandWidget);
        }

        if (expand) {
            subReportWidget.setSelected(!subReportWidget.isSelected());
        } else {
            AdsReportWidgetUtils.unselectCellsAndSubReports(bandWidget);
            subReportWidget.setSelected(true);
        }
    }

    @Override
    protected void edit() {
        subReportWidget.edit();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        super.mouseDragged(e);

        if (!subReportWidget.isSelected()) {
            return;
        }

        final AdsSubReport subReport = subReportWidget.getSubReport();
        final AdsReportBandWidget bandWidget = subReportWidget.getOwnerBandWidget();        
        
        double posY=MmUtils.px2mm(e.getY());  
        final AdsReportBand band=(AdsReportBand)bandWidget.getReportWidgetContainer();
        final double bandHeightMm = band.getHeightMm();
        final double preReportsHeightMm = AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM * band.getPreReports().size();
        
        if(band.getPreReports().contains(subReport)){
           posY=posY+ band.getPreReports().indexOf(subReport)*AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM ;
        }else {
           posY=posY+ bandHeightMm+preReportsHeightMm+band.getPostReports().indexOf(subReport)*AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM ;
        }
        
        boolean isInsToPrePerorts=true;
        if((bandHeightMm/2)<(posY-preReportsHeightMm)){//переносим в postSubReports
           posY = posY-(band.getHeightMm()+preReportsHeightMm);
           isInsToPrePerorts=false;            
        }
        int insertionIndex = Math.abs((int)(posY/AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM));        
        
        int i=0;
        for (AdsSubReportWidget subRepot : bandWidget.getSubReportWidgets()) {            
            if (subRepot.isSelected()) {
                if(band.getPreReports().contains(subReport)){
                    if(isInsToPrePerorts ){
                        if(insertionIndex!= band.getPreReports().indexOf(subReport)){
                            band.getPreReports().remove(subReport);
                            band.getPreReports().add(insertionIndex,subReport); 
                            if(i<insertionIndex){
                                i--;
                            }
                            insertionIndex++;
                        }
                    }else{
                        band.getPreReports().remove(subReport);
                        band.getPostReports().add(insertionIndex,subReport); 
                        insertionIndex++;                        
                    }
                }else if(band.getPostReports().contains(subReport)){
                    if(!isInsToPrePerorts ){
                        if(insertionIndex!= band.getPostReports().indexOf(subReport)){
                            band.getPostReports().remove(subReport);
                            band.getPostReports().add(insertionIndex,subReport); 
                            if(i<insertionIndex){
                                i--;
                            }
                            insertionIndex++;
                        }
                    }else{
                        band.getPostReports().remove(subReport);
                        band.getPreReports().add(insertionIndex,subReport); 
                        insertionIndex++;                        
                    }
                }                    
                        
            }
            i++; 
        }        
    }

    @Override
    protected void popup(final Component component, final int x,final int y) {
        if (!subReportWidget.isSelected()) {
            AdsReportWidgetUtils.selectSubReport(subReportWidget);
        }
        final JPopupMenu popupMenu = DialogUtils.createPopupMenu(subReportWidget);
        popupMenu.show(component, x, y);
    }
}

