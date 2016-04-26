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
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;


public class AdsReportHLayoutAction extends AdsReportLayoutAction {

    /*private final IRadixEventListener listener = new IRadixEventListener() {

     @Override
     public void onEvent(RadixEvent e) {
     setEnabled(undoRedo.canUndo());
     }
     };*/
    public AdsReportHLayoutAction(final AdsReportFormDiagram diagram) {
        //this.undoRedo = undoRedo;
        // ArrowColorButton button = new ArrowColorButton(
        //         (ImageIcon) AdsDefinitionIcon.WIDGETS.HORIZONTAL_LAYOUT.getIcon(), Color.BLACK);
        // button.setToolTipText("Horizontal layout");
        super(diagram);
        this.putValue(AbstractAction.SMALL_ICON, AdsDefinitionIcon.WIDGETS.HORIZONTAL_LAYOUT.getIcon());
        this.putValue(AbstractAction.NAME, "HorizontalLayout");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Horizontal layout");
        //this.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        //this.undoRedo.addStateListener(listener);
        //this.setEnabled(undoRedo.canUndo());
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        super.actionPerformed(e);
        if (widgetContainer != null) {
            if (widgetContainer.getReportWidgetContainer().getLayout() == EReportLayout.HORIZONTAL) {
                widgetContainer.getReportWidgetContainer().setLayout(EReportLayout.FREE);
            } else {
                widgetContainer.getReportWidgetContainer().setLayout(EReportLayout.HORIZONTAL);
            }
            widgetContainer.updateLayout();
        }
        /* //undoRedo.undo();
         List<AdsReportSelectableWidget> selectedWidgets=new ArrayList();
         //AdsReportBandWidget selectedBand=null;
         for(AdsReportBandWidget band:diagram.getBandWidgets()){
         if(band.isSelected()){
         selectedWidgets.addAll(band.getSelectedWidgets()); 
         if(selectedWidgets.isEmpty()){
         band.getReportWidgetContainer().setLayout(EReportLayout.HORIZONTAL);
         band.updateLayout();
         }
         break;
         }
         }
         if(selectedWidgets.size()==1){
         AdsReportSelectableWidget selectedWidget= selectedWidgets.get(0);
         if(selectedWidget instanceof AdsReportBandSubWidget){
         ((AdsReportWidgetContainer)selectedWidget.getCell()).setLayout(EReportLayout.HORIZONTAL);
         ((AdsReportBandSubWidget)selectedWidget).updateLayout();
         }
         }*/
    }
}
