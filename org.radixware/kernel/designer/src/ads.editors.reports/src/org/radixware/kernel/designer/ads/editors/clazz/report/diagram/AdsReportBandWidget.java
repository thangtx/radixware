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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.Splitter.DragEvent;

public class AdsReportBandWidget extends AbstractAdsReportWidget implements IReportCellContainer {

    private final AdsReportBand band;
    final AdsReportBandSubWidget bandSubWidget;
    private boolean selected = false;

    private final Ruler ruler;

    private final Splitter splitter;
    private final Splitter.DragListener splitterDragListener = new Splitter.DragListener() {

        @Override
        public void onDrag(final DragEvent e) {
            if (!band.isReadOnly()) {
                if (getDiagram().getMode() == AdsReportForm.Mode.GRAPHICS) {
                    double dYMm = MmUtils.px2mm(e.getdY());
                    final double bandHeightMm = band.getHeightMm();
                    final double newHeightMm = MmUtils.roundToTenth(Math.max(AdsReportBand.GRID_SIZE_MM, bandHeightMm + dYMm));
                    band.setHeightMm(newHeightMm);
                } else {
                    int dYRows = TxtUtils.px2Rows(e.getdY());
                    final int bandHeightRows = band.getHeightRows();
                    final int newHeightRows = Math.max(AdsReportBand.GRID_SIZE_SYMBOLS, bandHeightRows + dYRows);
                    System.out.println(newHeightRows);
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
                            AdsReportWidgetUtils.selectSubReport(subReportWidget);
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

        final AdsReportSelectionWidget selectionWidget = new AdsReportSelectionWidget();
        bandSubWidget = new AdsReportBandSubWidget(diagram, band);
        ruler = new Ruler(diagram, Ruler.EDirection.VERTICAL);
        splitter = new Splitter();

        add(selectionWidget);
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

        selectionWidget.attachTo(this);
        selectionWidget.attachTo(bandSubWidget);
    }

    private void removeOldSubReports() {
        for (AdsSubReport subReport : new HashSet<>(subReport2Widget.keySet())) {
            if (!band.getPreReports().contains(subReport) && !band.getPostReports().contains(subReport)) {
                final AdsSubReportWidget subReportWidget = subReport2Widget.remove(subReport);
                remove(subReportWidget);
                if (subReportWidget.isSelected()) {
                    fireSelectionChanged();
                }
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
        AdsReportForm.Mode mode = getDiagram().getMode();
        final int pageWidthPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(form.getPageWidthMm()) : TxtUtils.columns2Px(form.getPageWidthCols());
        final int leftMarginPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(form.getMargin().getLeftMm()) : TxtUtils.columns2Px(form.getMargin().getLeftCols());
        final int rightMarginPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(form.getMargin().getRightMm()) : TxtUtils.columns2Px(form.getMargin().getRightCols());
        final int leftPx = Ruler.THICKNESS_PX + leftMarginPx;
        final int widthPx = pageWidthPx - leftMarginPx - rightMarginPx;

        final int subReportHeightPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM) : TxtUtils.rows2Px(AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_ROWS);

        int yPx = 0;
        for (AdsSubReport subReport : band.getPreReports()) {
            final AdsSubReportWidget subReportWidget = findOrCreateSubReportWidget(subReport);
            subReportWidget.setLocation(leftPx, yPx);
            subReportWidget.setSize(widthPx, subReportHeightPx);
            yPx += subReportHeightPx;
        }

        final int bandSubWidgetHeightPx = mode == AdsReportForm.Mode.GRAPHICS ? MmUtils.mm2px(band.getHeightMm()) : TxtUtils.rows2Px(band.getHeightRows());
        bandSubWidget.setLocation(leftPx, yPx);
        bandSubWidget.setSize(widthPx, bandSubWidgetHeightPx);
        bandSubWidget.update();

        yPx += bandSubWidgetHeightPx;

        for (AdsSubReport subReport : band.getPostReports()) {
            final AdsSubReportWidget subReportWidget = findOrCreateSubReportWidget(subReport);
            subReportWidget.setLocation(leftPx, yPx);
            subReportWidget.setSize(widthPx, subReportHeightPx);
            yPx += subReportHeightPx;
        }

        ruler.setSize(Ruler.THICKNESS_PX, yPx);
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

    public boolean isSelected() {
        return selected;
    }
    private static final Color RULER_SELECTED_COLOR = new Color(176, 214, 225);

    public void setSelected(final boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            ruler.setBgColor(selected ? RULER_SELECTED_COLOR : Color.WHITE);
            if (selected) {
                requestFocus();
            }
            fireSelectionChanged();
        }
    }

    public AdsReportFormDiagram getOwnerFormDiagram() {
        return (AdsReportFormDiagram) getParent();
    }

    public AdsReportSelectableWidget findCellWidget(final AdsReportCell cell) {
        return bandSubWidget.findCellWidget(cell);
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
}
