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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;
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
    private final AdsReportSelectionWidget selectionWidget;
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
    private final FormListener mouseListener;
    private final RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent> selectionSupport = new RadixEventSource<>();
    private final RadixEventSource<IRadixEventListener<SelectionEvent>, SelectionEvent> innerSelectionSupport = new RadixEventSource<>();

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
        selectionWidget = new AdsReportSelectionWidget();
        add(selectionWidget);
        selectionWidget.attachTo(this);
        mouseListener = new FormListener(this);
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
            int result = Ruler.THICKNESS_PX + TxtUtils.rows2Px(form.getMarginTxt().getTopRows());

            for (AdsReportBand band : form.getBands()) {
                result += calcBandWidgetHeightPx(band);
            }

            return result + TxtUtils.rows2Px(form.getMarginTxt().getBottomRows());
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
            final int topLeftRulerHeightPx = TxtUtils.rows2Px(form.getMarginTxt().getTopRows());
            topLeftRuler.setSize(Ruler.THICKNESS_PX, topLeftRulerHeightPx - 1);

            final int bottomLeftRulerHeightPx = TxtUtils.rows2Px(form.getMarginTxt().getBottomRows());
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
                if (bandWidget.isSelected()) {
                    fireSelectionChanged(new SelectionEvent(bandWidget, false));
                }
                remove(bandWidget);
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
            int bandTopPx = Ruler.THICKNESS_PX + TxtUtils.rows2Px(form.getMarginTxt().getTopRows());

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
            if (target instanceof AdsReportWidget) {
                final AdsReportWidget cell = (AdsReportWidget) target;
                final AdsReportBand band = cell.getOwnerBand();
                final AdsReportBandWidget bandWidget = findBandWidget(band);
                final AdsReportSelectableWidget cellWidget = bandWidget.findCellWidget(cell);
                fireSelectionChanged(new SelectionEvent(cellWidget, true));
                break;
            }
            if (target instanceof AdsReportBand) {
                final AdsReportBand band = (AdsReportBand) target;
                final AdsReportBandWidget bandWidget = findBandWidget(band);
                fireSelectionChanged(new SelectionEvent(bandWidget, true));
                break;
            }
            if (target instanceof AdsSubReport){
                final AdsSubReport subReport = (AdsSubReport) target;
                final AdsReportBand band = subReport.getOwnerBand();
                final AdsReportBandWidget bandWidget = findBandWidget(band);
                final AdsSubReportWidget subReportWidget = bandWidget.findSubReportWidget(subReport);
                fireSelectionChanged(new SelectionEvent(subReportWidget, true));
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
    protected void fireSelectionChanged(SelectionEvent event) {
        //selected and unselected widgets
        innerSelectionSupport.fireEvent(event);
        //fire all changes
        selectionSupport.fireEvent(event);
    }

    protected void addWidgetSelectionListener(IRadixEventListener<SelectionEvent> listener) {
        innerSelectionSupport.addEventListener(listener);
    }
    
    protected void removeWidgetSelectionListener(IRadixEventListener<SelectionEvent> listener) {
        innerSelectionSupport.removeEventListener(listener);
    }
    
    protected void attachToSelectionWidget(AbstractAdsReportWidget widget) {
        selectionWidget.attachTo(widget);
    }
    
    protected void detachFromSelectionWidget(AbstractAdsReportWidget widget) {
        selectionWidget.detachFrom(widget);
    }
    
    public void setSelectedObjects(List<RadixObject> objects){
        fireSelectionChanged(new SelectionEvent(this, true));
        if (objects == null || objects.isEmpty() || (objects.size() == 1 && objects.contains(getForm()))){
            return;
        }
        for (RadixObject object : objects){
            AbstractAdsReportWidget widget = findWidget((RadixObject) object);
            if (widget instanceof AdsReportAbstractSelectableWidget){
                AdsReportAbstractSelectableWidget selectableWidget = (AdsReportAbstractSelectableWidget) widget;
                fireSelectionChanged(new SelectionEvent(selectableWidget, objects.size() > 1, selectableWidget.isSelected()));
            }
        }
    }

    public List<AdsReportAbstractSelectableWidget> getSelectedWidgets() {
        final List<AdsReportAbstractSelectableWidget> result = new ArrayList<>();
        for (AdsReportBandWidget bandWidget : getBandWidgets()) {
            result.addAll(getSelectedCellWidgets(bandWidget.bandSubWidget));

            for (AdsSubReportWidget subReportWidget : bandWidget.getSubReportWidgets()) {
                if (subReportWidget.isSelected()) {
                    result.add(subReportWidget);
                }
            }
            if (bandWidget.isSelected()) {
                result.add(bandWidget);
            }
        }
        return result;
    }
    
    public List<RadixObject> getSelectedObjects() {
        final List<RadixObject> result = new ArrayList<>();
        List<AdsReportAbstractSelectableWidget> widgets = getSelectedWidgets();
        for (AdsReportAbstractSelectableWidget widget : widgets){
            result.add(widget.getRadixObject());
        }
        if (result.isEmpty()) {
            result.add(form);
        }
        return result;
    }
    
    protected List<AdsReportAbstractSelectableWidget> getSelectedCellWidgets(AdsReportBaseContainer subBandWidget) {
        final List<AdsReportAbstractSelectableWidget> result = new ArrayList<>();
        for (AdsReportSelectableWidget cellWidget : subBandWidget.getCellWidgets()) {
            if (cellWidget.isSelected()) {
                result.add(cellWidget);
            }
            if (cellWidget.getCell().isReportContainer()) {
                result.addAll(getSelectedCellWidgets((AdsReportBaseContainer) cellWidget));
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
        this.mode = mode;
        this.form.setMode(mode);
    }

    public AdsReportForm.Mode getMode() {
        return mode;
    }

    public AdsReportFormUndoRedo getUndoRedo() {
        return undoRedo;
    }
    
    public AdsReportForm getForm(){
        return form;
    }
    
    public AbstractAdsReportWidget findWidget(RadixObject radixObject) {
        if (radixObject instanceof AdsReportForm) {
            return this;
        }

        if (radixObject instanceof AdsReportWidget) {
            final AdsReportWidget cell = (AdsReportWidget) radixObject;
            final AdsReportBand band = cell.getOwnerBand();
            final AdsReportBandWidget bandWidget = findBandWidget(band);
            final AdsReportSelectableWidget cellWidget = bandWidget.findCellWidget(cell);
            return cellWidget;
        }
        if (radixObject instanceof AdsReportBand) {
            final AdsReportBand band = (AdsReportBand) radixObject;
            final AdsReportBandWidget bandWidget = findBandWidget(band);
            return bandWidget;
        }
        if (radixObject instanceof AdsSubReport) {
            final AdsSubReport subReport = (AdsSubReport) radixObject;
            final AdsReportBand band = subReport.getOwnerBand();
            final AdsReportBandWidget bandWidget = findBandWidget(band);
            final AdsSubReportWidget subReportWidget = bandWidget.findSubReportWidget(subReport);
            return subReportWidget;
        }

        return null;
    }

    @Override
    public RadixObject getRadixObject() {
        return form;
    }
}
