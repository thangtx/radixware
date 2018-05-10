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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.common.types.Id;


class AdsReportContainer extends RadixObject{
    private final ReportWidgets reportWidgets = new ReportWidgets(this);
    private EReportLayout layout=EReportLayout.FREE; 
    
    public AdsReportContainer(AdsReportAbstractAppearance owner) {
        super();
        this.setContainer(owner);
    }
     
    protected AdsReportContainer(org.radixware.schemas.adsdef.ReportWidgetContainer xCellContainer, AdsReportAbstractAppearance owner) {
        this.setContainer(owner);     
        if (xCellContainer.isSetCells()) {
            fillCells(xCellContainer.getCells().getCellList());
        }
        //if (xCellContainer.isSetWidgetContainers()) {
        //    fillCellContainers(xCellContainer.getWidgetContainers().getWidgetContainerList());
        //}        
        if(xCellContainer.getLayout()!=null){
            layout=xCellContainer.getLayout();
        }
    }
    
    
    private void fillCells(List<org.radixware.schemas.adsdef.ReportCell> xCells){
        for (org.radixware.schemas.adsdef.ReportCell xCell : xCells) {
            if(xCell.isSetReportWidgetContainer()){
                final AdsReportWidgetContainer cell =new AdsReportWidgetContainer(xCell);
                reportWidgets.add(cell);
            }else{
                final AdsReportCell cell = AdsReportCellFactory.loadFrom(xCell);
                reportWidgets.add(cell);
            }
                //if (xCell.isSetReportWidgetContainer()) {
                //    fillCellContainers(xCell.getReportWidgetContainer());
               //}
        }
    }
    
   /* private void fillCellContainers(List<org.radixware.schemas.adsdef.ReportWidgetContainer> xCells){
        for (org.radixware.schemas.adsdef.ReportWidgetContainer xCell : xCells) {
                final AdsReportWidgetContainer cell =new AdsReportWidgetContainer(xCell);
                reportWidgets.add(cell);
        }
    }*/
    
    protected void appendTo(org.radixware.schemas.adsdef.ReportWidgetContainer xReportContainer, AdsDefinition.ESaveMode saveMode) {
        if (!reportWidgets.isEmpty()) {
            org.radixware.schemas.adsdef.ReportWidgetContainer.Cells xCells = xReportContainer.addNewCells();
            //org.radixware.schemas.adsdef.ReportContainer.WidgetContainers xWidgetContainers = xReportContainer.addNewWidgetContainers();
            for (AdsReportWidget reportWidget : reportWidgets) {
                if(reportWidget instanceof AdsReportCell)
                    ((AdsReportCell)reportWidget).appendTo(xCells.addNewCell(),saveMode);
                if(reportWidget instanceof AdsReportWidgetContainer)
                    ((AdsReportWidgetContainer)reportWidget).appendTo(xCells.addNewCell(),saveMode);
            }
        } 
        if(layout==null){
            xReportContainer.setLayout(EReportLayout.FREE);
        }else{
            xReportContainer.setLayout(layout);
        }
    }
    
    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        reportWidgets.visit(visitor, provider);
    }
    
    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return new ClipboardSupport<AdsReportContainer>(this) {

            @Override
            public boolean canCopy() {
                return false;
            }

            @Override
            public ClipboardSupport.CanPasteResult canPaste(List<ClipboardSupport.Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
                return reportWidgets.getClipboardSupport().canPaste(transfers, resolver);
            }

            @Override
            public void paste(List<ClipboardSupport.Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
                for(AdsReportWidget w:reportWidgets){
                    w.setColumn(-1);
                    w.setRow(-1);
                }
                reportWidgets.getClipboardSupport().paste(transfers, resolver);
            }
        };
    }
    
    EReportLayout getLayout() {
        return layout;
    }
    
     void setLayout(EReportLayout layout) {
         if(layout!=null)
            this.layout=layout;
    }
    
    RadixObjects<AdsReportWidget> getCells() {
        return reportWidgets;
    }
    
   /* int getResizingItemCnt(){
        return resizingItemCnt;
    }
    
    void setResizingItemCnt(int resizingItemCnt){
        this.resizingItemCnt=resizingItemCnt;
    }*/
    
    private static class ReportWidgets extends RadixObjects<AdsReportWidget> {

        protected ReportWidgets(AdsReportContainer owner) {
            super(owner);
        }

        @Override
        public ClipboardSupport.CanPasteResult canPaste(List<ClipboardSupport.Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
            if (isReadOnly()) {
                return ClipboardSupport.CanPasteResult.NO;
            }

            for (ClipboardSupport.Transfer transfer : transfers) {
                final RadixObject objectInClipboard = transfer.getObject();
                if (!(objectInClipboard instanceof AdsReportWidget)) {
                    return ClipboardSupport.CanPasteResult.NO;
                }
            }
            return ClipboardSupport.CanPasteResult.YES;
        }

        @Override
        protected void onRemove(AdsReportWidget object) {
            final AdsReportForm.ChangedEvent changedEvent = new AdsReportForm.ChangedEvent(object, AdsReportForm.ChangedEvent.ChangeEventType.REMOVE);
            fireEvent(changedEvent);
        }

        @Override
        protected void onAdd(AdsReportWidget object) {
            if (object.isNewStyle()) {
                AdsReportDefaultStyle.setDefaultWidgetStyle(object);
            }
            final AdsReportForm.ChangedEvent changedEvent = new AdsReportForm.ChangedEvent(object, AdsReportForm.ChangedEvent.ChangeEventType.ADD);
            fireEvent(changedEvent);
        }
        
        void fireEvent(AdsReportForm.ChangedEvent changedEvent) {
            final AdsReportContainer container = (AdsReportContainer) getContainer();
            if (container != null) {
                container.fireEvent(changedEvent);
            }
        }
    }
    
     @Override
     protected void onModified() {
        final AdsReportForm.ChangedEvent changedEvent = new AdsReportForm.ChangedEvent(this, AdsReportForm.ChangedEvent.ChangeEventType.NONE);
        fireEvent(changedEvent);
    }
        

    void fireEvent(AdsReportForm.ChangedEvent changedEvent) {
        final IReportWidgetContainer container = (IReportWidgetContainer) getContainer();
        if (container.getOwnerForm() != null) {
            container.getOwnerForm().fireEvent(changedEvent);
        }
    }
    
    public AdsReportWidget findWidgetById(Id widgetId) {
        for (AdsReportWidget widget : reportWidgets) {                        
            if(widget instanceof AdsReportWidgetContainer) {
                ((AdsReportWidgetContainer) widget).findWidgetById(widgetId);
            } else if(widget instanceof AdsReportCell) {
                AdsReportCell cell = (AdsReportCell) widget;
                if (cell.getId() == widgetId) {
                    return widget;
                }
            }
        }
        return null;
    }
}
