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
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportAddNewItemAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

class BandMouseListener extends WidgetMouseListener {

    private final AdsReportBandWidget bandWidget;

    public BandMouseListener(final AdsReportBandWidget bandWidget) {
        super();
        this.bandWidget = bandWidget;
    }

    @Override
    protected void select(final boolean expand) {
        final AdsReportFormDiagram formDiagram = bandWidget.getOwnerFormDiagram();
        if (formDiagram != null){
            formDiagram.fireSelectionChanged(new SelectionEvent(bandWidget, expand, bandWidget.isSelected()));
        }
    }

    @Override
    protected void edit() {
        bandWidget.edit();
    }

    public static class AddCellAction extends AbstractAction {

        private final AdsReportBandWidget bandWidget;
        private final IReportWidgetContainer band;
        private final int x, y;
        private final EReportCellType cellType;

        public AddCellAction(final AdsReportBandWidget bandWidge, final IReportWidgetContainer band, final EReportCellType cellType, final int x, final int y) {
            super("Add " + cellType.getValue(), AdsDefinitionIcon.getForReportCellType(cellType).getIcon());
            this.bandWidget = bandWidge;
            this.band = band;
            this.cellType = cellType;
            this.x = x;
            this.y = y;
            //final AdsReportBand band = (AdsReportBand)bandWidget.getReportWidgetContainer();
            setEnabled(!((AdsReportBand) bandWidget.getReportWidgetContainer()).isReadOnly());
        }

        @Override
        public boolean isEnabled() {
            if (super.isEnabled()) {
                if (bandWidget.getDiagramMode() == AdsReportForm.Mode.TEXT) {
                    switch (cellType) {
                        case CHART:
                        case DB_IMAGE:
                        case IMAGE:
                            return false;
                        default:
                            return true;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            //bandWidget.bandSubWidget.setCursor(Cursor.getDefaultCursor());
            //SwingUtilities.windowForComponent(bandWidget).requestFocus();
            boolean wasAdd = false;
            //final IReportWidgetContainer band = bandWidget.getReportWidgetContainer();
            try {
                /*if(e.getSource() instanceof JPopupMenu){
                 JPopupMenu m=(JPopupMenu)e.getSource();
                 m.transferFocusBackward();
                 }*/
                bandWidget.bandSubWidget.blockUndoRedo(true);
                final AdsReportAddNewItemAction actionPerformer = AdsReportAddNewItemAction.Factory.newAddCellAction(cellType);
                wasAdd = actionPerformer.addNewItem(band, x, y);
                //SwingUtilities.windowForComponent(bandWidget).set(null, this);
                //EditorsManager.getDefault().open(band.getOwnerForm().getOwnerReport()); // switch focuce to editor
            } finally {
                bandWidget.bandSubWidget.blockUndoRedo(false);
                if (wasAdd) {
                    band.getOwnerForm().setEditState(RadixObject.EEditState.MODIFIED);
                }
            }
            //bandWidget.requestFocus(true);
        }
    }

    public static class AddSubReportAction extends AbstractAction {

        private final RadixObjects<AdsSubReport> subReports;

        public AddSubReportAction(final RadixObjects<AdsSubReport> subReports, final String name) {
            super("Add " + name + " Report", AdsDefinitionIcon.REPORT_SUB_REPORT.getIcon());
            this.subReports = subReports;
            setEnabled(!subReports.isReadOnly());
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(subReports, AdsVisitorProviders.newReportProvider());
            final AdsReportClassDef report = (AdsReportClassDef) ChooseDefinition.chooseDefinition(cfg);
            if (report != null) {
                final AdsSubReport subReport = AdsSubReport.Factory.newInstance();
                subReport.setReportId(report.getId());
                subReports.add(subReport);
                if (!report.getInputParameters().isEmpty()) {
                    EditorsManager.getDefault().open(subReport);
                }
            }
        }
    }

    @Override
    protected void popup(final Component component, final int x, final int y) {
        if (!bandWidget.isSelected()) {
            bandWidget.getDiagram().fireSelectionChanged(new SelectionEvent(bandWidget, true));
        }
        final JPopupMenu popupMenu = AdsReportWidgetUtils.getPopupMenu(bandWidget, x, y);
        popupMenu.show(component, x, y);
    }
}
