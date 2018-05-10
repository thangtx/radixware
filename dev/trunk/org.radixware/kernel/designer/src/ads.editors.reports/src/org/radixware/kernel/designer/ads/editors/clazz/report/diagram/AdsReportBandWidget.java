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

import java.awt.Color;
import java.util.*;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.Splitter.DragEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;

public class AdsReportBandWidget extends AdsReportAbstractSelectableWidget implements IReportCellContainer {

    private final AdsReportBand band;
    final AdsReportBandSubWidget bandSubWidget;

    private final Ruler ruler;

    private final Splitter splitter;
    private final Splitter.DragListener splitterDragListener = new Splitter.DragListener() {

        @Override
        public void onDrag(final DragEvent e) {
            if (!band.isReadOnly()) {
                if (getDiagram().getMode() == AdsReportForm.Mode.GRAPHICS) {
                    double dYMm = MmUtils.px2mm(e.getdY());
                    final double bandHeightMm = band.getHeightMm();
                    final AdsReportForm form = band.getOwnerForm();
                    final double gridSizeMm = form != null? form.getGridSizeMm() : AdsReportForm.DEFAULT_GRID_SIZE_MM;
                    final double newHeightMm = MmUtils.roundToTenth(Math.max(gridSizeMm, bandHeightMm + dYMm));
                    band.setHeightMm(newHeightMm);
                } else {
                    int dYRows = TxtUtils.px2Rows(e.getdY());
                    final int bandHeightRows = band.getHeightRows();
                    final int newHeightRows = Math.max(AdsReportBand.GRID_SIZE_SYMBOLS, bandHeightRows + dYRows);
                    band.setHeightRows(newHeightRows);
                }
                bandSubWidget.getReportLayout().justifyLayout();
            }
        }
    };
    private final BandMouseListener mouseListener;
    private final Map<AdsSubReport, AdsSubReportWidget> subReport2Widget = new HashMap<>();
    private final ContainerChangesListener containerChangeListener = new ContainerChangesListener() {

        @Override
        public void onEvent(final ContainerChangedEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    update();

                    if (e.changeType == EChangeType.ENLARGE) {
                        final AdsSubReport subReport = (AdsSubReport) e.object;
                        final AdsSubReportWidget subReportWidget = findSubReportWidget(subReport);
                        if (subReportWidget != null) {
                            fireSelectionChanged(new SelectionEvent(subReportWidget, true));
                        }
                    }
                    repaint();
                }
            });
        }
    };
   
    @Override
    public void updateLayout() {
        bandSubWidget.updateLayout();
    }

    public AdsReportBandWidget(AdsReportFormDiagram diagram, final AdsReportBand band) {
        super(diagram, band);
        this.band = band;

        bandSubWidget = new AdsReportBandSubWidget(diagram, band);
        ruler = new Ruler(diagram, Ruler.EDirection.VERTICAL);
        splitter = new Splitter();

        add(bandSubWidget);
        add(ruler);
        add(splitter);

        splitter.addDragListener(splitterDragListener);

        mouseListener = new BandMouseListener(this);
        bandSubWidget.addMouseListener(mouseListener);
        ruler.addMouseListener(mouseListener);
        ruler.setToolTipText(band.getName());

        band.getPreReports().getContainerChangesSupport().addEventListener(containerChangeListener);
        band.getPostReports().getContainerChangesSupport().addEventListener(containerChangeListener);

    }

    private void removeOldSubReports() {
        for (AdsSubReport subReport : new HashSet<>(subReport2Widget.keySet())) {
            if (!band.getPreReports().contains(subReport) && !band.getPostReports().contains(subReport)) {
                final AdsSubReportWidget subReportWidget = subReport2Widget.remove(subReport);
                if (subReportWidget.isSelected()) {
                    fireSelectionChanged(new SelectionEvent(subReportWidget, false));
                }
                remove(subReportWidget);
            }
        }
    }

    public AdsSubReportWidget findSubReportWidget(final AdsSubReport subReport) {
        return subReport2Widget.get(subReport);
    }

    private AdsSubReportWidget findOrCreateSubReportWidget(final AdsSubReport subReport) {
        AdsSubReportWidget result = findSubReportWidget(subReport);
        if (result == null) {
            result = new AdsSubReportWidget(getDiagram(), subReport);
            subReport2Widget.put(subReport, result);
            add(result, 0);
        }
        return result;
    }

    private void updateBandSubWidgets() {
        final AdsReportForm form = band.getOwnerForm();
        if (form == null){
            return;
        }
        AdsReportForm.Mode mode = getDiagram().getMode();
        final int pageWidthPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(form.getPageWidthMm()) : TxtUtils.columns2Px(form.getPageWidthCols());
        final int leftMarginPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(form.getMargin().getLeftMm()) : TxtUtils.columns2Px(form.getMarginTxt().getLeftCols());
        final int rightMarginPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(form.getMargin().getRightMm()) : TxtUtils.columns2Px(form.getMarginTxt().getRightCols());
        final int leftPx = Ruler.THICKNESS_PX + leftMarginPx;
        final int widthPx = pageWidthPx - leftMarginPx - rightMarginPx;

        final int subReportHeightPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM) : TxtUtils.rows2Px(AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_ROWS);

        int yPx = 0;
        for (AdsSubReport subReport : band.getPreReports()) {
            updateSubReportWidget(subReport, mode, pageWidthPx, widthPx, subReportHeightPx, leftPx, yPx);
            yPx += subReportHeightPx;
        }

        final int bandSubWidgetHeightPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(band.getHeightMm()) : TxtUtils.rows2Px(band.getHeightRows());
        bandSubWidget.setLocation(leftPx, yPx);
        bandSubWidget.setSize(widthPx, bandSubWidgetHeightPx);
        bandSubWidget.update();

        yPx += bandSubWidgetHeightPx;

        for (AdsSubReport subReport : band.getPostReports()) {
            updateSubReportWidget(subReport, mode, pageWidthPx, widthPx, subReportHeightPx, leftPx, yPx);
            yPx += subReportHeightPx;
        }

        ruler.setSize(Ruler.THICKNESS_PX, yPx);
    }
    
    private void updateSubReportWidget(AdsSubReport subReport, AdsReportForm.Mode mode, final int pageWidthPx, final int widthPx, final int subReportHeightPx, final int leftPx, int yPx){
        final AdsSubReportWidget subReportWidget = findOrCreateSubReportWidget(subReport);
        int left = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(subReport.getMarginMm().getLeftMm()) : TxtUtils.columns2Px(subReport.getMarginTxt().getLeftCols());
        int right = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(subReport.getMarginMm().getRightMm()) : TxtUtils.columns2Px(subReport.getMarginTxt().getRightCols());;
        int width = pageWidthPx - left - right;
        left += Ruler.THICKNESS_PX;
        subReportWidget.setLocation(left, yPx);
        subReportWidget.setSize(width, subReportHeightPx);
    }

    private void updateSplitter() {
        final int widthPx = getWidth();
        final int heightPx = getHeight();
        splitter.setLocation(0, heightPx - Splitter.HEIGHT_PX);
        splitter.setSize(widthPx, Splitter.HEIGHT_PX);
    }

    @Override
    public void update() {
        removeOldSubReports();
        updateBandSubWidgets();
        updateSplitter();
    }

    private static final Color RULER_SELECTED_COLOR = new Color(176, 214, 225);

    @Override
    protected void onSelected(boolean selected) {
        ruler.setBgColor(selected ? RULER_SELECTED_COLOR : Color.WHITE);
    }
    
    public AdsReportFormDiagram getOwnerFormDiagram() {
        return (AdsReportFormDiagram) getParent();
    }

    public AdsReportSelectableWidget findCellWidget(final AdsReportWidget cell) {
        return bandSubWidget.findCellWidget(cell, true);
    }

    @Override
    public Collection<AdsReportSelectableWidget> getCellWidgets() {
        return bandSubWidget.getCellWidgets();
    }

    @Override
    public IReportWidgetContainer getReportWidgetContainer() {
        return band;
    }

    public Collection<AdsSubReportWidget> getSubReportWidgets() {
        return subReport2Widget.values();
    }

    @Override
    public Collection<AdsReportSelectableWidget> getSelectedWidgets() {
        List<AdsReportSelectableWidget> result = new ArrayList<>();
        for (AdsReportSelectableWidget widget : getCellWidgets()) {
            if (widget.isSelected()) {
                result.add(widget);
            }
            if (widget instanceof AdsReportBaseContainer) {
                result.addAll(((AdsReportBaseContainer) widget).getSelectedWidgets());
            }
        }
        return result;
    }

    @Override
    public ReportLayoutProcessor getReportLayout() {
        return bandSubWidget.getReportLayout();
    }

    public AdsReportBandSubWidget getBandSubWidget() {
        return bandSubWidget;
    }    
}
