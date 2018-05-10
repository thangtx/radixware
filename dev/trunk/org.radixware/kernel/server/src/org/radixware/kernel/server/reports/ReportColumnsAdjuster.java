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
package org.radixware.kernel.server.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportColumnsUserResizeMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.reports.ColumnSettings;
import org.radixware.schemas.reports.ReportColumnsList;

public class ReportColumnsAdjuster {

    private final CellWrapperFactory cellWrapperFactory;

    private final EReportColumnsUserResizeMode resizeMode;

    private final Map<Id, ReportColumnsList.Column> visibleColumns = new HashMap<>();
    private final List<ReportCellsLine> cellsLines = new ArrayList<>();

    public ReportColumnsAdjuster(AdsReportBand band, ColumnSettings settings, CellWrapperFactory cellWrapperFactory) {
        this.cellWrapperFactory = cellWrapperFactory;
        this.resizeMode = settings.getResizeMode();
        
        loadVisibleColumns(settings);
        
        Set<AdsReportCell> processedCells = new HashSet<>();
        loadCellsFromContainer(band, processedCells);
    }

    public void adjustColumnsBySettings() {
        for (ReportCellsLine line : cellsLines) {
            if (resizeMode == EReportColumnsUserResizeMode.MANUAL) {
                line.applyManualWidth();
            }
            
            for (AdsReportCell cell : line.getCells()) {
                if (visibleColumns.isEmpty() || !visibleColumns.containsKey(cell.getAssociatedColumnId())) {
                    line.removeCell(cell);
                }
            }

            if (resizeMode == EReportColumnsUserResizeMode.ADJUST_WIDTH) {
                line.adjustElementsWidth();
            }
        }
    }

    private void loadVisibleColumns(ColumnSettings settings) {
        if (settings.getVisibleColumns() != null) {
            for (ReportColumnsList.Column xColumn : settings.getVisibleColumns().getColumnList()) {
                visibleColumns.put(xColumn.getColumnId(), xColumn);
            }
        }
    }

    private void loadCellsFromContainer(IReportWidgetContainer container, Set<AdsReportCell> processedCells) {
        for (AdsReportWidget widget : container.getWidgets()) {
            if (widget instanceof IReportWidgetContainer) {
                loadCellsFromContainer(container, processedCells);
            } else if (widget instanceof AdsReportCell) {
                AdsReportCell cell = (AdsReportCell) widget;
                if (processedCells.contains(cell)) {
                    continue;
                }

                processCell(cell, processedCells);
            }
        }
    }

    private void processCell(AdsReportCell cell, Set<AdsReportCell> processedCells) {
        if (cell.getLeftCellId() == null && (cell.getRightCellIdList() == null || cell.getRightCellIdList().isEmpty())) {
            processedCells.add(cell);
            return;
        }

        ReportCellsLine cellsLine = new ReportCellsLine();

        AdsReportCell tmpCell = cell;

        while (tmpCell.getLeftCellId() != null) {
            IReportWidgetContainer container = cell.getOwnerWidget();
            tmpCell = (AdsReportCell) container.findWidgetById(tmpCell.getLeftCellId());
        }
        
        addRightCells(tmpCell, cellsLine, processedCells);
    }
    
    private void addRightCells(AdsReportCell cell, ReportCellsLine line, Set<AdsReportCell> processedCells) {        
        line.addCell(createCellWrapper(cell));
        processedCells.add(cell);
        
        if (cell.getRightCellIdList() == null || cell.getRightCellIdList().isEmpty()) {
            cellsLines.add(line);
            return;
        }
        
        ReportCellsLine lineCopy = new ReportCellsLine(line);                
        for (Id rightCellId : cell.getRightCellIdList()) {
            IReportWidgetContainer container = cell.getOwnerWidget();
            AdsReportCell tmpCell = (AdsReportCell) container.findWidgetById(rightCellId);
            
            addRightCells(tmpCell, new ReportCellsLine(lineCopy), processedCells);
        }
    }
    
    private CellWrapper createCellWrapper(AdsReportCell cell) {
        if (resizeMode == EReportColumnsUserResizeMode.MANUAL) {
            if (!visibleColumns.isEmpty() && visibleColumns.containsKey(cell.getAssociatedColumnId())) {
                return cellWrapperFactory.newInstance(cell, visibleColumns.get(cell.getAssociatedColumnId()).getWidth());
            }
        }
         
        return cellWrapperFactory.newInstance(cell);
    }
}
