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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EReportLayout;

public class AdsReportWidgetContainer extends AdsReportWidget implements IReportWidgetContainer {

    private final AdsReportContainer reportContainer;

    public AdsReportWidgetContainer() {
        reportContainer = new AdsReportContainer(this);
    }

    protected AdsReportWidgetContainer(org.radixware.schemas.adsdef.ReportCell xCellContainer) {
        super(xCellContainer);
        if (xCellContainer.isSetReportWidgetContainer()) {
            reportContainer = new AdsReportContainer(xCellContainer.getReportWidgetContainer(), this);
        } else {
            reportContainer = new AdsReportContainer(this);
        }
    }

    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xWidget, AdsDefinition.ESaveMode saveMode) {
        super.appendTo(xWidget);
        reportContainer.appendTo(xWidget.addNewReportWidgetContainer(), saveMode);
    }

    /*private AdsReportBand getOwnerBand() {
     for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
     if (container instanceof AdsReportBand) {
     return (AdsReportBand) container;
     }
     }
     return null;
     }*/
    @Override
    public RadixObjects<AdsReportWidget> getWidgets() {
        return reportContainer.getCells();
    }

    @Override
    public EReportLayout getLayout() {
        return reportContainer.getLayout();
    }

    @Override
    public void setLayout(EReportLayout layout) {
        if (reportContainer.getLayout() != layout) {
            reportContainer.setLayout(layout);
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override // switch to visibility in current package
    protected void setContainer(RadixObject container) {
        super.setContainer(container);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        reportContainer.visit(visitor, provider);
    }

    /*  @Override
     public void collectDependences(List<Definition> list) {         
     reportContainer.collectDependences(list);                  
     }*/
    @Override
    protected void onModified() {
        final AdsReportBand container = getOwnerBand();
        if (container != null) {
            container.onModified();
        }
    }

    @Override
    public boolean isReportContainer() {
        return true;
    }

    @Override
    public AdsReportForm getOwnerForm() {
        for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportForm) {
                return (AdsReportForm) container;
            }
        }
        return null;
    }

    private static class AdsReportCellClipboardSupport extends AdsClipboardSupport<AdsReportWidgetContainer> {

        private static class AdsReportCellTransfer extends AdsClipboardSupport.AdsTransfer<AdsReportWidgetContainer> {

            public AdsReportCellTransfer(AdsReportWidgetContainer cell) {
                super(cell);
            }

            @Override
            protected void afterDuplicate() {
                super.afterDuplicate();
                final AdsReportWidgetContainer cell = getObject();
                cell.setLeftMm(cell.getLeftMm() + AdsReportBand.GRID_SIZE_MM);
                cell.setTopMm(cell.getTopMm() + AdsReportBand.GRID_SIZE_MM);
            }
        }

        public AdsReportCellClipboardSupport(AdsReportWidgetContainer cell) {
            super(cell);
        }

        @Override
        protected XmlObject copyToXml() {
            final org.radixware.schemas.adsdef.ReportCell xCell = org.radixware.schemas.adsdef.ReportCell.Factory.newInstance();
            radixObject.appendTo(xCell, AdsDefinition.ESaveMode.NORMAL);
            return xCell;
        }

        @Override
        protected AdsReportWidgetContainer loadFrom(XmlObject xmlObject) {
            final org.radixware.schemas.adsdef.ReportCell xCell = (org.radixware.schemas.adsdef.ReportCell) xmlObject;
            return new AdsReportWidgetContainer(xCell);
        }

        @Override
        protected AdsReportWidgetContainer.AdsReportCellClipboardSupport.AdsReportCellTransfer newTransferInstance() {
            return new AdsReportWidgetContainer.AdsReportCellClipboardSupport.AdsReportCellTransfer(radixObject);
        }
    }

    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return new AdsReportWidgetContainer.AdsReportCellClipboardSupport(this);
    }

    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return true;
    }
}
