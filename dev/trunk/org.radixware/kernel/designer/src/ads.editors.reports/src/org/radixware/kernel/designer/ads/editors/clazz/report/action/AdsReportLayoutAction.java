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

package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportBandWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportBaseContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportSelectableWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.IReportCellContainer;


public class AdsReportLayoutAction extends AbstractAction {
    protected final AdsReportFormDiagram diagram;
    protected IReportCellContainer widgetContainer;

    public AdsReportLayoutAction(final AdsReportFormDiagram diagram) {
        this.diagram=diagram;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final List<AdsReportSelectableWidget> selectedWidgets=new ArrayList<>();
        for(AdsReportBandWidget band:diagram.getBandWidgets()){
            if(band.isSelected()){
                selectedWidgets.addAll(band.getSelectedWidgets()); 
                if(selectedWidgets.isEmpty()){
                    widgetContainer=band;
                }
                break;
            }
        }
        if(selectedWidgets.size()==1){
           AdsReportSelectableWidget selectedWidget= selectedWidgets.get(0);
           while(!(selectedWidget instanceof AdsReportBaseContainer) && selectedWidget!=null){
               selectedWidget=(AdsReportSelectableWidget)selectedWidget.getParent();
           }
           widgetContainer=(AdsReportBaseContainer)selectedWidget;
        }
    }
    
}
