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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette;

import java.awt.Dialog;
import java.awt.Dimension;
import java.util.Map;
import org.openide.DialogDisplayer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCellFactory;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportExpressionCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.report.chart.Wizard;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.MmUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

/**
 * Factory to create report item (cell of subreport) from Palette node
 *
 */
public abstract class AdsReportAddNewItemAction {

    public abstract boolean addNewItem(IReportWidgetContainer band, int x, int y);

    public static class Factory {
        
        private Factory() {
        }

        public static AdsReportAddNewItemAction newAddCellAction(final EReportCellType cellType) {
            return new AddNewCellAction(cellType);
        }

        public static AdsReportAddNewItemAction newAddPropertyCellAction(final Id propId) {
            return new AddNewCellAction(EReportCellType.PROPERTY, propId);
        }

        public static AdsReportAddNewItemAction newAddSubreportAction() {
            return new AddNewSubreportAction();
        }
        public static AdsReportAddNewItemAction newAddPrereportAction() {
            return new AddNewSubreportAction();
        }
        public static AdsReportAddNewItemAction newAddPostreportAction() {
            return new AddNewSubreportAction();
        }

        public static AdsReportAddNewItemAction newAddReportContainerAction() {
            return new AddNewReportContainerAction();
        }
    }

    public static class AddNewCellAction extends AdsReportAddNewItemAction {

        private final EReportCellType cellType;
        private final Id id;

        public AddNewCellAction(final EReportCellType cellType) {
            super();
            this.cellType = cellType;
            this.id = null;
        }

        public AddNewCellAction(final EReportCellType cellType, final Id id) {
            super();
            this.cellType = cellType;
            this.id = id;
        }

        public EReportCellType getCellType() {
            return cellType;
        }
        

        @Override
        public boolean addNewItem(final IReportWidgetContainer band, final int x, final int y) {
            AdsReportCell cell;
            switch (cellType) {
                case PROPERTY:
                    final AdsReportPropertyCell propertyCell = AdsReportCellFactory.newPropertyCell();
                    propertyCell.setPropertyId(id);
                    cell = propertyCell;
                    break;
                case IMAGE:
                    final AdsReportImageCell imageCell = AdsReportCellFactory.newImageCell();
                    imageCell.setImageId(id);
                    cell = imageCell;
                    break;
                case DB_IMAGE:
                    final AdsReportDbImageCell dbImageCell = AdsReportCellFactory.newDbImageCell();
                    dbImageCell.setDataPropertyId(id);
                    cell = dbImageCell;
                    break;
                case TEXT:
                    cell = AdsReportCellFactory.newTextCell();
                    break;
                case SPECIAL:
                    cell = AdsReportCellFactory.newSpecialCell();
                    break;
                case SUMMARY:
                    final AdsReportSummaryCell summaryCell = AdsReportCellFactory.newSummaryCell();
                    summaryCell.setPropertyId(id);
                    cell = summaryCell;
                    break;
                case EXPRESSION:
                    final AdsReportExpressionCell expressionCell = AdsReportCellFactory.newExpressionCell();
                    cell = expressionCell;
                    break;
                case CHART:
                    final AdsReportChartCell chartCell = AdsReportCellFactory.newChartCell();
                    cell = chartCell;
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (cellType == EReportCellType.CHART) {
                final AdsReportChartCell chartCell = (AdsReportChartCell) cell;
                final AdsReportClassDef report = band.getOwnerForm().getOwnerReport();
                final Wizard wizard = new Wizard(chartCell, band);
                final WizardDescriptor desc = new WizardDescriptor(wizard);
                final Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);

                dialog.setMinimumSize(new Dimension(450, 350));
                dialog.setVisible(true);

                final Object descValue = desc.getValue();
                if (descValue != null && descValue.equals(WizardDescriptor.FINISH_OPTION)) {
                    chartCell.setLeftMm(MmUtils.snapToGrid(MmUtils.px2mm(x)));
                    chartCell.setTopMm(MmUtils.snapToGrid(MmUtils.px2mm(y)));
                    final double bandFontSize = band.getFont().getSizeMm();
                    final double heightMm = AdsReportBand.GRID_SIZE_MM * (int) (1 + (bandFontSize / AdsReportBand.GRID_SIZE_MM));
                    chartCell.setHeightMm(heightMm);

                    setTitle(chartCell.getTitleId(), report, wizard.getMlStrings());
                    for (AdsReportChartSeries series : chartCell.getChartSeries()) {
                        final Id titleId = series.getTitleId();
                        setTitle(titleId, report, wizard.getMlStrings());
                    }
                    band.getWidgets().add(chartCell);
                }
            } else {
                cell.setLeftMm(MmUtils.snapToGrid(MmUtils.px2mm(x)));
                cell.setTopMm(MmUtils.snapToGrid(MmUtils.px2mm(y)));
                final double bandFontSize = band.getFont().getSizeMm();
                final double heightMm = AdsReportBand.GRID_SIZE_MM * (int) (1 + (bandFontSize / AdsReportBand.GRID_SIZE_MM));
                cell.setHeightMm(heightMm);
                band.getWidgets().add(cell);
                if (id == null) {
                    EditorsManager.getDefault().open(cell);
                }
            }
            return true;
        }
    }

    private static void setTitle(final Id titleId, final AdsReportClassDef report, final Map<Id, IMultilingualStringDef> mlStrings) {
        if (titleId != null) {
            final IMultilingualStringDef mlString = mlStrings.get(titleId);
            report.findLocalizingBundle().getStrings().getLocal().add((AdsMultilingualStringDef) mlString);
        }
    }

    private static class AddNewSubreportAction extends AdsReportAddNewItemAction {

        @Override
        public boolean addNewItem(final IReportWidgetContainer widgetContainer, final int x, final int y) {
            boolean result=false;
            if (widgetContainer instanceof AdsReportBand) {
                final AdsReportBand band = (AdsReportBand) widgetContainer;
                final AdsSubReport subReport = AdsSubReport.Factory.newInstance();
                final Double topMm = MmUtils.snapToGrid(MmUtils.px2mm(y));
                if (topMm <= band.getHeightMm() / 2) {
                    band.getPreReports().add(subReport);
                } else {
                    band.getPostReports().add(subReport);
                }
                EditorsManager.getDefault().open(subReport);
                result = true;
            }
            return result;
        }
    }
    
     private static class AddNewPrereportAction extends AdsReportAddNewItemAction {

        @Override
        public boolean addNewItem(final IReportWidgetContainer widgetContainer, final int x, final int y) {
            boolean result=false;
            if (widgetContainer instanceof AdsReportBand) {
                final AdsReportBand band = (AdsReportBand) widgetContainer;
                final AdsSubReport subReport = AdsSubReport.Factory.newInstance();
                //final Double topMm = MmUtils.snapToGrid(MmUtils.px2mm(y));
                //if (topMm <= band.getHeightMm() / 2) {
                    band.getPreReports().add(subReport);
//                } else {
//                    band.getPostReports().add(subReport);
//                }
                EditorsManager.getDefault().open(subReport);
                result = true;
            }
            return result;
        }
    }
     
     private static class AddNewPostreportAction extends AdsReportAddNewItemAction {

        @Override
        public boolean addNewItem(final IReportWidgetContainer widgetContainer, final int x, final int y) {
            boolean result=false;
            if (widgetContainer instanceof AdsReportBand) {
                final AdsReportBand band = (AdsReportBand) widgetContainer;
                final AdsSubReport subReport = AdsSubReport.Factory.newInstance();
                //final Double topMm = MmUtils.snapToGrid(MmUtils.px2mm(y));
                //if (topMm <= band.getHeightMm() / 2) {
              //      band.getPreReports().add(subReport);
//                } else {
                    band.getPostReports().add(subReport);
//                }
                EditorsManager.getDefault().open(subReport);
                result = true;
            }
            return result;
        }
    }
    
    

    private static class AddNewReportContainerAction extends AdsReportAddNewItemAction {

        @Override
        public boolean addNewItem(final IReportWidgetContainer widgetContainer, final int x, final int y) {
            //if (widgetContainer instanceof IReportWidgetContainer) {
                AdsReportBand band;
                if (widgetContainer instanceof AdsReportBand) {
                    band = (AdsReportBand) widgetContainer;
                } else {
                    band = ((AdsReportWidgetContainer) widgetContainer).getOwnerBand();
                }
                final AdsReportWidgetContainer cellContainer = new AdsReportWidgetContainer();
                cellContainer.setLeftMm(MmUtils.snapToGrid(MmUtils.px2mm(x)));
                cellContainer.setTopMm(MmUtils.snapToGrid(MmUtils.px2mm(y)));
                final double bandFontSize = band.getFont().getSizeMm();
                final double heightMm = AdsReportBand.GRID_SIZE_MM * (int) (1 + (bandFontSize / AdsReportBand.GRID_SIZE_MM));
                cellContainer.setHeightMm(heightMm);
                widgetContainer.getWidgets().add(cellContainer);
              //  return true;
            //}
            return true;
        }
    }
}
