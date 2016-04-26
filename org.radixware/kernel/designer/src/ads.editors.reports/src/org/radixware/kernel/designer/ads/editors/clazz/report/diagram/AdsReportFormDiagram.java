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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class AdsReportFormDiagram extends AbstractAdsReportWidget {

    public static final Color BG_COLOR = Color.LIGHT_GRAY;
    public static final Color FORM_COLOR = new Color(245, 245, 245);
    private final AdsReportForm form;
    private final AdsReportFormUndoRedo undoRedo;
    private final Ruler topRuler, topLeftRuler, bottomLeftRuler;
    private final Map<AdsReportBand, AdsReportBandWidget> band2Widget = new HashMap<>();
    private EIsoLanguage language;
    private AdsReportForm.Mode mode = AdsReportForm.Mode.GRAPHICS;
    private final AdsReportForm.IChangeListener changeListener = new AdsReportForm.IChangeListener() {

        @Override
        public void onEvent(AdsReportForm.ChangedEvent e) {
            updateLater();
        }
    };
    private final IRadixEventListener<RadixEvent> eventListener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(RadixEvent e) {
            updateLater();
        }
    };

    private void updateLater() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                update();
                repaint();
            }
        });
    }
    private final FormMouseListener mouseListener;
    private final RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent> selectionSupport = new RadixEventSource<>();

    public AdsReportFormDiagram(AdsReportForm form, final AdsReportFormUndoRedo undoRedo) {
        super(null, form);
        this.form = form;
        this.undoRedo = undoRedo;
        this.topRuler = new Ruler(this, Ruler.EDirection.HORIZONTAL);
        this.topLeftRuler = new Ruler(this, Ruler.EDirection.VERTICAL);
        this.bottomLeftRuler = new Ruler(this, Ruler.EDirection.VERTICAL);

        this.add(topRuler);
        this.add(topLeftRuler);
        this.add(bottomLeftRuler);

        topRuler.setLocation(Ruler.THICKNESS_PX, 0);
        topLeftRuler.setLocation(0, Ruler.THICKNESS_PX);

        setOpaque(true);
        mouseListener = new FormMouseListener(this);
        addMouseListener(mouseListener);
        form.addChangeListener(changeListener);
        AdsReportFormDiagramOptions.getDefault().addChangeListener(eventListener);
    }

    @Override
    public AdsReportFormDiagram getDiagram() {
        return this;
    }

    private int calcWidthPx() {

        final int pageWidthPx;
        if (mode == AdsReportForm.Mode.GRAPHICS) {
            final double pageWidthMm = form.getPageWidthMm();
            pageWidthPx = MmUtils.mm2px(pageWidthMm);
        } else {
            final int pageWidth = form.getPageWidthCols();
            pageWidthPx = TxtUtils.columns2Px(pageWidth);
        }
        return pageWidthPx + Ruler.THICKNESS_PX;
    }

    private int calcHeightPx() {

        if (mode == AdsReportForm.Mode.GRAPHICS) {
            int result = Ruler.THICKNESS_PX + MmUtils.mm2px(form.getMargin().getTopMm());

            for (AdsReportBand band : form.getBands()) {
                result += calcBandWidgetHeightPx(band);
            }

            return result + MmUtils.mm2px(form.getMargin().getBottomMm());
        } else {
            int result = Ruler.THICKNESS_PX + TxtUtils.rows2Px(form.getMargin().getTopRows());

            for (AdsReportBand band : form.getBands()) {
                result += calcBandWidgetHeightPx(band);
            }

            return result + TxtUtils.rows2Px(form.getMargin().getBottomRows());
        }
    }

    private void updateSize() {
        final int widthPx = calcWidthPx();
        final int heightPx = calcHeightPx();
        final Dimension size = new Dimension(widthPx, heightPx);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        setSize(size);
    }

    private void updateRulers() {
        final int topRulerWidthPx = calcWidthPx() - Ruler.THICKNESS_PX;
        topRuler.setSize(topRulerWidthPx, Ruler.THICKNESS_PX);

        if (mode == AdsReportForm.Mode.GRAPHICS) {
            final int topLeftRulerHeightPx = MmUtils.mm2px(form.getMargin().getTopMm());
            topLeftRuler.setSize(Ruler.THICKNESS_PX, topLeftRulerHeightPx - 1);

            final int bottomLeftRulerHeightPx = MmUtils.mm2px(form.getMargin().getBottomMm());
            bottomLeftRuler.setLocation(0, getHeight() - bottomLeftRulerHeightPx);
            bottomLeftRuler.setSize(Ruler.THICKNESS_PX, bottomLeftRulerHeightPx);
        } else {
            final int topLeftRulerHeightPx = TxtUtils.rows2Px(form.getMargin().getTopRows());
            topLeftRuler.setSize(Ruler.THICKNESS_PX, topLeftRulerHeightPx - 1);

            final int bottomLeftRulerHeightPx = TxtUtils.rows2Px(form.getMargin().getBottomRows());
            bottomLeftRuler.setLocation(0, getHeight() - bottomLeftRulerHeightPx);
            bottomLeftRuler.setSize(Ruler.THICKNESS_PX, bottomLeftRulerHeightPx);
        }
    }

    public AdsReportBandWidget findBandWidget(AdsReportBand band) {
        return band2Widget.get(band);
    }

    private AdsReportBandWidget findOrCreateBandWidget(AdsReportBand band) {
        AdsReportBandWidget result = findBandWidget(band);
        if (result == null) {
            result = new AdsReportBandWidget(this, band);
            band2Widget.put(band, result);
            add(result);
        }
        return result;
    }

    private void removeOldBandWidgets() {
        final Set<AdsReportBand> bands = new HashSet<>(form.getBands());

        for (AdsReportBand band : new HashSet<>(band2Widget.keySet())) {
            if (!bands.contains(band)) {
                final AdsReportBandWidget bandWidget = band2Widget.remove(band);
                remove(bandWidget);
                if (bandWidget.isSelected()) {
                    fireSelectionChanged();
                }
            }
        }
    }

    private int calcBandWidgetHeightPx(AdsReportBand band) {

        if (mode == AdsReportForm.Mode.GRAPHICS) {
            int subReportHeightPx = MmUtils.mm2px(AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_MM);
            return MmUtils.mm2px(band.getHeightMm())
                    + Splitter.HEIGHT_PX
                    + (band.getPreReports().size() + band.getPostReports().size()) * subReportHeightPx;
        } else {
            int subReportHeightPx = TxtUtils.rows2Px(AdsSubReportWidget.SUB_REPORT_WIDGET_HEIGHT_ROWS);
            return TxtUtils.rows2Px(band.getHeightRows())
                    + Splitter.HEIGHT_PX
                    + (band.getPreReports().size() + band.getPostReports().size()) * subReportHeightPx;
        }
    }

      private void updateBandWidgets() {
        int widthPx = calcWidthPx();
        if (mode == AdsReportForm.Mode.GRAPHICS) {
            int bandTopPx = Ruler.THICKNESS_PX + MmUtils.mm2px(form.getMargin().getTopMm());

            for (AdsReportBand band : form.getBands()) {
                final AdsReportBandWidget bandWidget = findOrCreateBandWidget(band);
                bandWidget.setLocation(0, bandTopPx);

                int bandWidgetHeightPx = calcBandWidgetHeightPx(band);
                bandWidget.setSize(widthPx, bandWidgetHeightPx);
                bandWidget.update();

                bandTopPx += bandWidgetHeightPx;
            }
        } else {
            int bandTopPx = Ruler.THICKNESS_PX + TxtUtils.rows2Px(form.getMargin().getTopRows());

            for (AdsReportBand band : form.getBands()) {
                final AdsReportBandWidget bandWidget = findOrCreateBandWidget(band);
                bandWidget.setLocation(0, bandTopPx);

                int bandWidgetHeightPx = calcBandWidgetHeightPx(band);
                bandWidget.setSize(widthPx, bandWidgetHeightPx);
                bandWidget.update();

                bandTopPx += bandWidgetHeightPx;
            }
        }
    }

    public void open(OpenInfo openInfo) {
        update();

        for (RadixObject target = openInfo.getTarget(); target != null; target = target.getContainer()) {
            if (target instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell) target;
                final AdsReportBand band = cell.getOwnerBand();
                final AdsReportBandWidget bandWidget = findBandWidget(band);
                final AdsReportSelectableWidget cellWidget = bandWidget.findCellWidget(cell);
                AdsReportWidgetUtils.selectCell(cellWidget);
                break;
            }
            if (target instanceof AdsReportBand) {
                final AdsReportBand band = (AdsReportBand) target;
                final AdsReportBandWidget bandWidget = findBandWidget(band);
                AdsReportWidgetUtils.selectBand(bandWidget);
                break;
            }
        }

        //for(AdsReportBandWidget bandWidget:band2Widget.values()){
        //   bandWidget.bandSubWidget.getReportLayout().justifyLayout();                    
        //}
    }

    @Override
    public void update() {
        updateSize();
        updateRulers();
        removeOldBandWidgets();
        updateBandWidgets();
    }

    private void paintBackground(Graphics g) {
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintPage(Graphics g) {
        g.setColor(FORM_COLOR);
        final int indent = Ruler.THICKNESS_PX;
        final int formWidthPx;
        final int formHeightPx;
        if (mode == AdsReportForm.Mode.GRAPHICS) {
            formWidthPx = MmUtils.mm2px(form.getPageWidthMm());
            formHeightPx = calcHeightPx() - indent;
        } else {
            formWidthPx = TxtUtils.columns2Px(form.getPageWidthCols());
            formHeightPx = calcHeightPx() - indent;
        }
        g.fillRect(indent, indent, formWidthPx, formHeightPx);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mode == AdsReportForm.Mode.TEXT) {
            TxtUtils.measure((Graphics2D) g);
        }
        super.paintComponent(g);
        paintBackground(g);
        paintPage(g);
    }

    public Collection<AdsReportBandWidget> getBandWidgets() {
        return band2Widget.values();
    }

    public void removeSelectionListener(IRadixEventListener<RadixEvent> listener) {
        selectionSupport.removeEventListener(listener);
    }

    public void addSelectionListener(IRadixEventListener<RadixEvent> listener) {
        selectionSupport.addEventListener(listener);
    }

    @Override
    protected void fireSelectionChanged() {
        selectionSupport.fireEvent(new RadixEvent());
    }

    public List<RadixObject> getSelectedObjects() {
        final List<RadixObject> result = new ArrayList<>();
        for (AdsReportBandWidget bandWidget : getBandWidgets()) {
            if (bandWidget.isSelected()) {
                result.addAll(getSelectedCells(bandWidget.bandSubWidget));

                for (AdsSubReportWidget subReportWidget : bandWidget.getSubReportWidgets()) {
                    if (subReportWidget.isSelected()) {
                        result.add(subReportWidget.getSubReport());
                    }
                }
                if (result.isEmpty()) {
                    result.add((AdsReportBand) bandWidget.getReportWidgetContainer());
                }
            }
        }
        if (result.isEmpty()) {
            result.add(form);
        }
        return result;
    }

    public List<RadixObject> getSelectedCells(AdsReportBaseContainer subBandWidget) {
        final List<RadixObject> result = new ArrayList<>();
        for (AdsReportSelectableWidget cellWidget : subBandWidget.getCellWidgets()) {
            if (cellWidget.isSelected()) {
                result.add(cellWidget.getCell());
            }
            if (cellWidget.getCell().isReportContainer()) {
                result.addAll(getSelectedCells((AdsReportBaseContainer) cellWidget));
            }
        }
        return result;
    }

    public EIsoLanguage getLanguage() {
        return language;
    }

    public void setLanguage(EIsoLanguage language) {
        this.language = language;
    }

    public void setMode(AdsReportForm.Mode mode) {
        if (this.form != null) {
            this.form.convertToTextMode();
        }
        this.mode = mode;
        this.form.setMode(mode);
    }

    public AdsReportForm.Mode getMode() {
        return mode;
    }

    public AdsReportFormUndoRedo getUndoRedo() {
        return undoRedo;
    }
}
