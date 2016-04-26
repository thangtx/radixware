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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QSizeF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMatrix;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public final class ImageViewWidget extends QGraphicsView{
    
    private final static QColor BACKGROUND_COLOR = new QColor(112,112,112);
    private final static QSizeF ZERO_SIZE = new QSizeF(0,0);
    private final static QSize SIZE_HINT = new QSize(250, 300);
    // The change trigger before adding to the sum
    private final static int DIFF_TRIGGER = 8;
    // The selection start/stop level trigger
    private final static int SUM_TRIGGER = 4;
    // The selection start/stop level trigger for the floating  average
    private final static int AVERAGE_TRIGGER = 7;
    // The selection start/stop margin
    private final static int SEL_MARGIN = 3;
    // Maximum number of allowed selections (this could be a settable variable)
    private final static int MAX_NUM_SELECTIONS = 8;
    // floating average 'div' must be one less than 'count'
    private final static int AVERAGE_COUNT = 50;
    private final static int AVERAGE_MULT = 49;
    // Minimum selection area compared to the whole image
    private final static float MIN_AREA_SIZE = (float)0.01;
    
    private static class Icon extends ClientIcon.CommonOperations{
        
        private Icon(final String fileName){
            super(fileName);
        }
        
        public static final Icon ZOOM_IN = new Icon("classpath:images/zoomIn.svg");
        public static final Icon ZOOM_OUT = new Icon("classpath:images/zoomOut.svg");  
        public static final Icon ZOOM_SEL = new Icon("classpath:images/zoomSelection.svg");
        public static final Icon ZOOM_2_FIT = new Icon("classpath:images/zoom2Fit.svg");
    }    
    
    private static class ResizeImageEvent extends QEvent{
    
        public ResizeImageEvent(){
            super(QEvent.Type.User);
        }
    
    }    
    
    private final QGraphicsScene scene = new QGraphicsScene();
    private final MessageProvider messageProvider;
    private final boolean singleSelectionMode;
    private SelectionItem  selection;
    private QImage img;

    private final List<SelectionItem> selectionList = new ArrayList<>();
    private SelectionItem.EIntersects change = SelectionItem.EIntersects.None;

    private QPointF lastSPoint;
    private int m_left_last_x;
    private int m_left_last_y;
    private boolean resizeImageScheduled;

    public final QAction zoomInAction;
    public final QAction zoomOutAction;
    public final QAction zoomSelAction;
    public final QAction zoom2FitAction;
    public final QAction clrSelAction;
    
    private final QGraphicsRectItem hideLeft = new QGraphicsRectItem();
    private final QGraphicsRectItem hideRight = new QGraphicsRectItem();
    private final QGraphicsRectItem hideTop = new QGraphicsRectItem();
    private final QGraphicsRectItem hideBottom = new QGraphicsRectItem();
    private final QGraphicsRectItem hideArea = new QGraphicsRectItem();    
    
    public final Signal1<QRectF> newSelection = new Signal1<>();
    
    public ImageViewWidget(final QImage image, final MessageProvider mp, final boolean singleSelection, final QWidget parent){
        super(parent);
        
        img = image;
        messageProvider = mp;
        singleSelectionMode = singleSelection;

        setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn);
        setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn);
        setMouseTracking(true);

        // Init the scene
        scene.setSceneRect(0, 0, img.width(), img.height());
        setScene(scene);

        selection = new SelectionItem(new QRectF(),singleSelection);
        selection.setZValue(10);
        selection.setSaved(false);
        selection.setMaxRight(img.width());
        selection.setMaxBottom(img.height());
        selection.setRect(scene.sceneRect());
        selection.setVisible(false);

        hideTop.setOpacity(0.4);
        hideBottom.setOpacity(0.4);
        hideRight.setOpacity(0.4);
        hideLeft.setOpacity(0.4);
        hideArea.setOpacity(0.6);

        hideTop.setPen(QPen.NoPen);
        hideBottom.setPen(QPen.NoPen);
        hideRight.setPen(QPen.NoPen);
        hideLeft.setPen(QPen.NoPen);
        hideArea.setPen(QPen.NoPen);

        final QBrush blackBrush = new QBrush(QColor.black);
        hideTop.setBrush(blackBrush);
        hideBottom.setBrush(blackBrush);
        hideRight.setBrush(blackBrush);
        hideLeft.setBrush(blackBrush);

        scene.addItem(selection);
        scene.addItem(hideLeft);
        scene.addItem(hideRight);
        scene.addItem(hideTop);
        scene.addItem(hideBottom);
        scene.addItem(hideArea);

        selectionList.clear();

        // create context menu
        zoomInAction = createAction(Icon.ZOOM_IN,messageProvider.translate("IAD", "Zoom In"),"zoomIn()");
        zoomOutAction = createAction(Icon.ZOOM_OUT,messageProvider.translate("IAD", "Zoom Out"),"zoomOut()");
        zoomSelAction = createAction(Icon.ZOOM_SEL,messageProvider.translate("IAD", "Zoom to Selection"),"zoomSel()");
        zoom2FitAction = createAction(Icon.ZOOM_2_FIT,messageProvider.translate("IAD", "Zoom to Fit"), "zoom2Fit()");
        clrSelAction = createAction(null,messageProvider.translate("IAD", "Clear Selections"), "clearSelections()");

        addAction(zoomInAction);
        addAction(zoomOutAction);
        addAction(zoomSelAction);
        addAction(zoom2FitAction);
        addAction(clrSelAction);
        setContextMenuPolicy(Qt.ContextMenuPolicy.ActionsContextMenu);
    }
    
    private QAction createAction(final ClientIcon icon, final String title, final String slot){
        final QAction action = new QAction(ExplorerIcon.getQIcon(icon), title, this);
        action.triggered.connect(this,slot);
        return action;
    }
    
    
    @Override
    protected void drawBackground(final QPainter painter, final QRectF rect){
        painter.fillRect(rect, BACKGROUND_COLOR);
        painter.drawImage(rect, img, rect);
    }

    public void setQImage(final QImage img){
        if (img == null){
            return;
        }

        // remove selections
        clearSelections();

        // clear zoom
        setMatrix(new QMatrix());

        scene.setSceneRect(0, 0, img.width(), img.height());
        selection.setMaxRight(img.width());
        selection.setMaxBottom(img.height());
        this.img = img;
    }
    
    public void updateImage(){
        setCacheMode(QGraphicsView.CacheModeFlag.CacheNone);
        repaint();
        setCacheMode(QGraphicsView.CacheModeFlag.CacheBackground);
    }    
        
    public void setTLX(final double ratio){
        if (selection.isVisible()){
            final QRectF rect = selection.rect().clone();
            rect.setLeft(ratio * img.width());
            selection.setRect(rect);
            updateSelVisibility();
        }
    }
   
    public void setTLY(final double ratio)
    {
        if (selection.isVisible()){
            final QRectF rect = selection.rect().clone();
            rect.setTop(ratio * img.height());
            selection.setRect(rect);
            updateSelVisibility();
        }
    }
    
    public void setBRX(final double ratio)
    {
        if (selection.isVisible()){
            final QRectF rect = selection.rect().clone();
            rect.setRight(ratio * img.width());
            selection.setRect(rect);
            updateSelVisibility();
        }
    }
    
    public void setBRY(final double ratio)
    {
        if (selection.isVisible()){
            final QRectF rect = selection.rect().clone();
            rect.setBottom(ratio * img.height());
            selection.setRect(rect);
            updateSelVisibility();
        }
    }
    
    public void clearHighlight()
    {
        hideLeft.hide();
        hideRight.hide();
        hideTop.hide();
        hideBottom.hide();
        hideArea.hide();
    }    
    
    
    public int selListSize() {
        return selectionList.size() + (selection.isVisible() ? 1 : 0);
    }

    
    public boolean selectionAt(final int index, final QRectF outRect){
        if ((index < 0) || (index > selectionList.size())) {
            activeSelection(outRect);
            return false;
        }
        if  (index == selectionList.size()) {
            return activeSelection(outRect);
        }

        final double tl_x = selectionList.get(index).rect().left()   / img.width();
        final double tl_y = selectionList.get(index).rect().top()    / img.height();
        final double br_x = selectionList.get(index).rect().right()  / img.width();
        final double br_y = selectionList.get(index).rect().bottom() / img.height();
        outRect.setTopLeft(new QPointF(tl_x, tl_y));
        outRect.setBottomRight(new QPointF(br_x, br_y));
        return true;
    }
    
    public void setHighlightArea(final QRectF area){
        final QRectF rect = new QRectF(0,0, area.topLeft().x() * img.width(), img.height());

        hideLeft.setRect(rect);

        // Right
        rect.setCoords(area.bottomRight().x() * img.width(), 
                       0,
                       img.width(), 
                       img.height());
        hideRight.setRect(rect);

        // Top
        rect.setCoords(area.topLeft().x() * img.width(), 
                       0,
                       area.bottomRight().x() * img.width(), 
                       area.topLeft().y() * img.height());
        hideTop.setRect(rect);

        // Bottom
        rect.setCoords(area.topLeft().x() * img.width(), 
                       area.bottomRight().y() * img.height(),
                       area.bottomRight().x() * img.width(), 
                       img.height());
        hideBottom.setRect(rect);

        // hide area
        rect.setCoords(area.topLeft().x() * img.width(), 
                       area.topLeft().y() * img.height(),
                       area.bottomRight().x() * img.width(), 
                       area.bottomRight().y() * img.height());

        hideArea.setRect(rect);

        hideLeft.show();
        hideRight.show();
        hideTop.show();
        hideBottom.show();
        // the hide area is hidden until setHighlightShown is called.
        hideArea.hide();
    }    
    
    private boolean activeSelection(final QRectF outRect){
        if (selection.isVisible()) {
            final double tl_x = selection.rect().left() / img.width();
            final double tl_y = selection.rect().top() / img.height();
            final double br_x = selection.rect().right() / img.width();
            final double br_y = selection.rect().bottom() / img.height();

            if ((tl_x == br_x) || (tl_y == br_y)) {
                outRect.setTopLeft(new QPointF(0.0, 0.0));
                outRect.setBottomRight(new QPointF(1.0,1.0));
                return false;
            }
            outRect.setTopLeft(new QPointF(tl_x, tl_y));
            outRect.setBottomRight(new QPointF(br_x, br_y));
            return true;
        }else{
            outRect.setTopLeft(new QPointF(0.0, 0.0));
            outRect.setBottomRight(new QPointF(1.0,1.0));
            return true;
        }
    }
    
    public void setSelection(float tl_x, float tl_y, float br_x, float br_y)
    {
        final QRectF rect = new QRectF();
        rect.setCoords(tl_x * img.width(),
                        tl_y * img.height(),
                        br_x * img.width(),
                        br_y * img.height());

        selection.setRect(rect);
        updateSelVisibility();
    }    
        
    private void clearActiveSelection(){
        selection.setRect(new QRectF(0,0,0,0));
        selection.intersects(new QPointF(100,100));
        selection.setVisible(false);
    }

    private void clearSavedSelections(){
        for (SelectionItem selectionItem: selectionList){
            scene.removeItem(selectionItem);
            selectionItem.dispose();
        }
        selectionList.clear();
    }
    
    private void clearSelections(){
        clearActiveSelection();
        clearSavedSelections();
    }   
    
    
    @SuppressWarnings("unused")
    private void zoomIn(){
        scale(1.5, 1.5);
        saveZoom();
    }
    
    @SuppressWarnings("unused")
    private void zoomOut(){
        scale(1.0 / 1.5, 1.0 / 1.5);
        saveZoom();
    }

    @SuppressWarnings("unused")
    private void zoomSel(){
        if (selection.isVisible()) {
            fitInView(selection.boundingRect(), Qt.AspectRatioMode.KeepAspectRatio);
            saveZoom();
        }
        else {
            zoom2Fit();
        }
    }
    
    private void zoom2Fit(){
        fitInView(new QRectF(img.rect()), Qt.AspectRatioMode.KeepAspectRatio);
        saveZoom();
    }    
    
    private void saveZoom(){
        final double transformM11 = transform().m11();
        selection.saveZoom(transformM11);
        for (SelectionItem item: selectionList) {
            item.saveZoom(transformM11);
        }                    
    }
    
    private void updateSelVisibility(){
        if ((selection.rect().width() >0.001) &&
            (selection.rect().height() > 0.001) &&
            ((img.width() - selection.rect().width() > 0.1) ||
            (img.height() - selection.rect().height() > 0.1))){
            selection.setVisible(true);
        }
        else {
            selection.setVisible(false);
        }
        updateHighlight();
    }    
    
    private void updateHighlight()
    {
        if (selection.isVisible()) {
            final QRectF rect = new QRectF();
            rect.setCoords(0,0, selection.rect().left(), img.height());
            hideLeft.setRect(rect);

            // Right
            rect.setCoords(selection.rect().right(), 
                           0,
                           img.width(), 
                           img.height());
            hideRight.setRect(rect);

            // Top
            rect.setCoords(selection.rect().left(), 
                           0,
                           selection.rect().right(),
                           selection.rect().top());
            hideTop.setRect(rect);

            // Bottom
            rect.setCoords(selection.rect().left(), 
                           selection.rect().bottom(),
                           selection.rect().right(),
                           img.height());
            hideBottom.setRect(rect);

            hideLeft.show();
            hideRight.show();
            hideTop.show();
            hideBottom.show();
            hideArea.hide();
        }
        else {
            hideLeft.hide();
            hideRight.hide();
            hideTop.hide();
            hideBottom.hide();
            hideArea.hide();
        }
    }    
    
    @Override
    protected void wheelEvent(final QWheelEvent event) {
        if(event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)) {
            if(event.delta() > 0) {
                zoomIn();
            } else {
                zoomOut();
            }
        } else {
            super.wheelEvent(event);
        }        
    }
    

    @Override
    protected void mousePressEvent(final QMouseEvent event) {
        if (event.button()== Qt.MouseButton.LeftButton){
            m_left_last_x = event.x();
            m_left_last_y = event.y();
            final QPointF scenePoint = mapToScene(m_left_last_x, m_left_last_y);            
            lastSPoint = scenePoint;
            if (!event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)) {
                if (!selection.isVisible()) {
                    selection.setVisible(true);
                    selection.setRect(new QRectF(scenePoint, ZERO_SIZE));
                    selection.intersects(scenePoint); // just to disable add/remove
                    change = SelectionItem.EIntersects.BottomRight;
                }else if (selection.intersects(scenePoint) == SelectionItem.EIntersects.None) {
                    selection.setRect(new QRectF(scenePoint, ZERO_SIZE));
                    change = SelectionItem.EIntersects.BottomRight;
                }
                updateHighlight();
            }
        }
        super.mousePressEvent(event);
    }
    
    @Override
    protected void mouseReleaseEvent(final QMouseEvent event) {
        boolean removed = false;
        if (event.button() == Qt.MouseButton.LeftButton) {
            if ((selection.rect().width() < 0.001) ||
                (selection.rect().height() < 0.001))
            {
                newSelection.emit(new QRectF(0, 0, 0, 0));
                clearActiveSelection();
            }

            final QPointF scenePoint = mapToScene(event.x(),event.y());
            for (int i=0; i<selectionList.size(); i++) {
                final SelectionItem item = selectionList.get(i);
                if (item.intersects(scenePoint) == SelectionItem.EIntersects.AddRemove) {
                    scene.removeItem(item);                    
                    selectionList.remove(i);
                    selection.setVisible(true);
                    selection.setRect(item.rect().clone());
                    selection.intersects(scenePoint); // just to enable add/remove
                    item.dispose();
                    removed = true;
                    break;
                }
            }
            if (!removed && (selection.intersects(scenePoint) == SelectionItem.EIntersects.AddRemove)) {
                // add the current selection
                final SelectionItem item = new SelectionItem(selection.rect().clone(),singleSelectionMode);
                selectionList.add(item);
                item.setSaved(true);
                item.saveZoom(transform().m11());
                scene.addItem(item);
                item.setZValue(9);
                item.intersects(scenePoint);

                // clear the old one
                newSelection.emit(new QRectF(0, 0, 0, 0));
                clearActiveSelection();
            }
        }

        if ((!event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)) &&
            (selection.isVisible()) &&
            (img.width() > 0.001) &&
            (img.height() > 0.001))
        {
            final double tlx = selection.rect().left()   / img.width();
            final double tly = selection.rect().top()    / img.height();
            final double brx = selection.rect().right()  / img.width();
            final double bry = selection.rect().bottom() / img.height();

            newSelection.emit(new QRectF(new QPointF(tlx, tly), new QPointF(brx, bry)));
        }
        updateHighlight();
        super.mouseReleaseEvent(event);
    }

    @Override
    protected void mouseMoveEvent(final QMouseEvent event) {
        final QPointF scenePoint = mapToScene(event.x(),event.y());

        if (event.buttons().isSet(Qt.MouseButton.LeftButton)){
            if (event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)){
                final int dx = event.x() - m_left_last_x;
                final int dy = event.y() - m_left_last_y;
                verticalScrollBar().setValue(verticalScrollBar().value()-dy);
                horizontalScrollBar().setValue(horizontalScrollBar().value()-dx);
                m_left_last_x = event.x();
                m_left_last_y = event.y();
            }else{
                ensureVisible(new QRectF(scenePoint, ZERO_SIZE), 1, 1);
                final QRectF rect = selection.rect().clone();
                switch (change)
                {
                    case None:
                        // should not be here :)
                        break;
                    case Top:{
                        if (scenePoint.y() < rect.bottom()) {
                            rect.setTop(scenePoint.y());
                        } else {
                            change = SelectionItem.EIntersects.Bottom;
                            rect.setBottom(scenePoint.y());
                        }
                        break;
                    }
                    case TopRight:{
                        if (scenePoint.x() > rect.left()) {
                            rect.setRight(scenePoint.x());
                        } else {
                            rect.setLeft(scenePoint.x());
                            change = SelectionItem.EIntersects.TopLeft;
                        }
                        if (scenePoint.y() < rect.bottom()) {
                            rect.setTop(scenePoint.y());
                        } else {
                            rect.setBottom(scenePoint.y());
                            change = SelectionItem.EIntersects.BottomLeft;
                        }
                        break;
                    }
                    case Right:{
                        if (scenePoint.x() > rect.left()){
                            rect.setRight(scenePoint.x());
                        } else {
                            rect.setLeft(scenePoint.x());
                            change = SelectionItem.EIntersects.Left;
                        }
                        break;
                    }
                    case BottomRight:{
                        if (scenePoint.x() > rect.left()){
                            rect.setRight(scenePoint.x());
                        }else {                        
                            rect.setLeft(scenePoint.x());
                            change = SelectionItem.EIntersects.BottomLeft;
                        }
                        if (scenePoint.y() > rect.top())  {
                            rect.setBottom(scenePoint.y());
                        } else {
                            rect.setTop(scenePoint.y());
                            change = SelectionItem.EIntersects.TopRight;
                        }
                        break;
                    }
                    case Bottom:{
                        if (scenePoint.y() > rect.top()){
                            rect.setBottom(scenePoint.y());
                        } else {
                            change = SelectionItem.EIntersects.Top;
                            rect.setTop(scenePoint.y());
                        }
                        break;
                    }
                    case BottomLeft:{
                        if (scenePoint.x() < rect.right()){
                            rect.setLeft(scenePoint.x());
                        } else {
                            rect.setRight(scenePoint.x());
                            change = SelectionItem.EIntersects.BottomRight;
                        }
                        if (scenePoint.y() > rect.top()) rect.setBottom(scenePoint.y());
                        else {
                            rect.setTop(scenePoint.y());
                            change = SelectionItem.EIntersects.TopLeft;
                        }
                        break;
                    }
                    case Left:{
                        if (scenePoint.x() < rect.right()){
                            rect.setLeft(scenePoint.x());
                        }else {
                            rect.setRight(scenePoint.x());
                            change = SelectionItem.EIntersects.Right;
                        }
                        break;
                    }
                    case TopLeft:{
                        if (scenePoint.x() < rect.right()){
                            rect.setLeft(scenePoint.x());
                        } else {
                            rect.setRight(scenePoint.x());
                            change = SelectionItem.EIntersects.TopRight;
                        }
                        if (scenePoint.y() < rect.bottom()){
                            rect.setTop(scenePoint.y());
                        } else {
                            rect.setBottom(scenePoint.y());
                            change = SelectionItem.EIntersects.BottomLeft;
                        }
                        break;
                    }
                    case Move:{
                        rect.translate(selection.fixTranslation(scenePoint.clone().subtract(lastSPoint)));
                        break;
                    }
                    case AddRemove:
                        // do nothing
                        break;
                }
                selection.setRect(rect);
            }
        } else if (selection.isVisible()) {
            change = selection.intersects(scenePoint);

            switch (change){
                case None:
                    viewport().setCursor(new QCursor(Qt.CursorShape.CrossCursor));
                    break;
                case Top:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeVerCursor));
                    break;
                case TopRight:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeBDiagCursor));
                    break;
                case Right:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeHorCursor));
                    break;
                case BottomRight:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeFDiagCursor));
                    break;
                case Bottom:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeVerCursor));
                    break;
                case BottomLeft:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeBDiagCursor));
                    break;
                case Left:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeHorCursor));
                    break;
                case TopLeft:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeFDiagCursor));
                    break;
                case Move:
                    viewport().setCursor(new QCursor(Qt.CursorShape.SizeAllCursor));
                    break;
                case AddRemove:
                    viewport().setCursor(new QCursor(Qt.CursorShape.ArrowCursor));
                    break;
            }
        } else {
            viewport().setCursor(new QCursor(Qt.CursorShape.CrossCursor));
        }

        // now check the selection list
        for (SelectionItem selectionItem: selectionList) {
            if (selectionItem.intersects(scenePoint) == SelectionItem.EIntersects.AddRemove) {
                viewport().setCursor(new QCursor(Qt.CursorShape.ArrowCursor));
            }
        }

        lastSPoint = scenePoint;
        updateHighlight();
        super.mouseMoveEvent(event);
    }

    @Override
    protected void keyPressEvent(QKeyEvent event) {
        if (event.key()==Qt.Key.Key_Escape.value()){
            event.accept();
            clearActiveSelection();
            viewport().repaint();
        }else{
            super.keyPressEvent(event);
        }
    }    
    
    
    @Override
    protected void resizeEvent(QResizeEvent event) {
        super.resizeEvent(event);
        if (!resizeImageScheduled){
            resizeImageScheduled = true;
            QApplication.postEvent(this, new ResizeImageEvent());
        }
    }
    
    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ResizeImageEvent){
            event.accept();
            resizeImageScheduled = false;
            zoom2FitAction.trigger();
        }else{
            super.customEvent(event);
        }
    }    
    

    @Override
    public QSize sizeHint(){
        return SIZE_HINT;
    }    
    
    public void findSelections(float area){
        // Reduce the size of the image to decrease noise and calculation time
        final double multiplier = Math.sqrt(area/(img.height() * img.width()));

        int width  = (int)(img.width() * multiplier);
        int height = (int)(img.height() * multiplier);

        final QImage image = img.scaled(width, height, Qt.AspectRatioMode.KeepAspectRatio);
        height = image.height(); // the size was probably not exact
        width  = image.width();

        final long[] colSums = new long[width + SEL_MARGIN + 1];
        Arrays.fill(colSums, 0);
        long rowSum;        
        int pix;
        int diff;
        int hSelStart=-1;
        int hSelEnd=-1;
        int hSelMargin = 0;
        int wSelStart=-1;
        int wSelEnd=-1;
        int wSelMargin = 0;
        for (int h=1; h<height; h++) {
            rowSum = 0;
            if (h<height-1) {
                // Special case for the left most pixel
                pix = gray(img.pixel(0, h));
                diff  = Math.abs(pix - gray(img.pixel(1, h)));
                diff += Math.abs(pix - gray(img.pixel(0, h-1)));
                diff += Math.abs(pix - gray(img.pixel(0, h+1)));
                if (diff > DIFF_TRIGGER) {
                    colSums[0] += diff;
                    rowSum += diff;
                }

                // Special case for the right most pixel
                pix = gray(img.pixel(width - 1, h));
                diff  = Math.abs(pix - gray(img.pixel(width - 2, h)));
                diff += Math.abs(pix - gray(img.pixel(width - 1, h-1)));
                diff += Math.abs(pix - gray(img.pixel(width - 1, h+1)));
                if (diff > DIFF_TRIGGER) {
                    colSums[width - 1] += diff;
                    rowSum += diff;
                }

                for (int w=1; w < (width - 1); w++) {
                    pix = gray(img.pixel(w, h));
                    diff = 0;
                    // how much does the pixel differ from the surrounding
                    diff += Math.abs(pix - gray(img.pixel(w - 1, h)));
                    diff += Math.abs(pix - gray(img.pixel(w + 1, h)));
                    diff += Math.abs(pix - gray(img.pixel(w, h - 1)));
                    diff += Math.abs(pix - gray(img.pixel(w, h + 1)));
                    if (diff > DIFF_TRIGGER) {
                        colSums[w] += diff;
                        rowSum += diff;
                    }
                }
            }

            if ((rowSum/width) > SUM_TRIGGER) {
                if (hSelStart < 0) {
                    if (hSelMargin < SEL_MARGIN) hSelMargin++;
                    if (hSelMargin == SEL_MARGIN) hSelStart = h - SEL_MARGIN + 1;
                }
            }
            else {
                if (hSelStart >= 0) {
                    if (hSelMargin > 0) hSelMargin--;
                }
                if ((hSelStart > -1) && ((hSelMargin == 0) || (h==height-1))) {
                    if (h==height-1) {
                        hSelEnd = h - hSelMargin;
                    }
                    else {
                        hSelEnd = h - SEL_MARGIN;
                    }
                    // We have the end of the vertical selection
                    // now figure out the horizontal part of the selection
                    for (int w=0; w <= width; w++) { // colSums[width] will be 0
                        if ((colSums[w]/(h - hSelStart)) > SUM_TRIGGER) {
                            if (wSelStart < 0) {
                                if (wSelMargin < SEL_MARGIN) wSelMargin++;
                                if (wSelMargin == SEL_MARGIN) wSelStart = w - SEL_MARGIN + 1;
                            }
                        }
                        else {
                            if (wSelStart >= 0) {
                                if (wSelMargin > 0) wSelMargin--;
                            }
                            if ((wSelStart >= 0) && ((wSelMargin == 0) || (w == width))) {
                                if (w == width) {
                                    wSelEnd = width;
                                }
                                else {
                                    wSelEnd = w - SEL_MARGIN +1;
                                }

                                // we have the end of a horizontal selection
                                if ((wSelEnd-wSelStart) < width) {
                                    // skip selections that span the whole width
                                    // calculate the coordinates in the original size
                                    double x1 = wSelStart / multiplier;
                                    double y1 = hSelStart / multiplier;
                                    double x2 = wSelEnd / multiplier;
                                    double y2 = hSelEnd / multiplier;
                                    float selArea = (float)(wSelEnd - wSelStart) * (float)(hSelEnd-hSelStart);
                                    if (selArea > (area * MIN_AREA_SIZE)) {
                                        final SelectionItem item = 
                                            new SelectionItem(new QRectF(x1,y1,x2,y2),singleSelectionMode);
                                        selectionList.add(item);
                                        item.setSaved(true);
                                        item.saveZoom(transform().m11());
                                        scene.addItem(item);
                                        item.setZValue(9);
                                    }
                                }
                                wSelStart = -1;
                                wSelEnd = -1;
                                wSelMargin = 0;
                            }
                        }
                    }
                    hSelStart = -1;
                    hSelEnd = -1;
                    hSelMargin = 0;
                    Arrays.fill(colSums, 0);
                }
            }
        }

        if (selectionList.size() > MAX_NUM_SELECTIONS) {
            // smaller area or should we give up??
            clearSavedSelections(); 
            //findSelections(area/2); 
            // instead of trying to find probably broken selections just give up 
            // and do not force broken selections on the user.
        }
        else {
            // 1/multiplier is the error margin caused by the resolution reduction
            refineSelections(Math.round(1/(float)multiplier));
            // check that the selections are big enough
            float minArea = img.height() * img.width() * MIN_AREA_SIZE;

            for (int i=selectionList.size()-1; i>=0; i--){
                if ((selectionList.get(i).rect().width() * selectionList.get(i).rect().height()) < minArea) {
                    scene().removeItem(selectionList.get(i));
                    selectionList.remove(i);
                }
            }
        }
    }
    
    private void refineSelections(final int pixelMargin)
    {
       // The end result
        int hSelStart;
        int hSelEnd;
        int wSelStart;
        int wSelEnd;

        for (int i=0; i<selectionList.size(); i++) {
            QRectF selRect = selectionList.get(i).rect();

            // original values
            hSelStart = (int)selRect.top();
            hSelEnd = (int)selRect.bottom();
            wSelStart = (int)selRect.left();
            wSelEnd = (int)selRect.right();

            // Top
            // Too long iteration should not be a problem since the loop should be interrupted by the limit 
            hSelStart = refineRow(hSelStart - pixelMargin, hSelEnd, wSelStart, wSelEnd);

            // Bottom (from the bottom up wards)
            hSelEnd = refineRow(hSelEnd + pixelMargin, hSelStart, wSelStart, wSelEnd);

            // Left
            wSelStart = refineColumn(wSelStart - pixelMargin, wSelEnd, hSelStart, hSelEnd);

            // Right
            wSelEnd = refineColumn(wSelEnd + pixelMargin, wSelStart, hSelStart, hSelEnd);

            // Now update the selection
            selectionList.get(i).setRect(new QRectF(new QPointF(wSelStart, hSelStart), new QPointF(wSelEnd, hSelEnd)));
        }
    }    
    
    private int refineRow(int fromRow, int toRow, int colStart, int colEnd)
    {
        int pix;
        int diff;
        float rowTrigger;
        int row;
        int addSub = (fromRow < toRow) ? 1 : -1;

        colStart -= 2; //add some margin
        colEnd += 2; //add some margin

        if (colStart < 1) {
            colStart = 1;
        }
        if (colEnd >= img.width()-1){
            colEnd = img.width() - 2;
        }
        if (fromRow < 1) {
            fromRow = 1;
        }
        if (fromRow >= img.height()-1){
            fromRow = img.height() - 2;
        }
        if (toRow < 1) {
            toRow = 1;
        }
        if (toRow >= img.height()-1){
            toRow = img.height() - 2;
        }
        row = fromRow;
        while (row != toRow) {
            rowTrigger = 0;
            for (int w=colStart; w<colEnd; w++) {
                diff = 0;
                pix = gray(img.pixel(w, row));
                // how much does the pixel differ from the surrounding
                diff += Math.abs(pix - gray(img.pixel(w-1, row)));
                diff += Math.abs(pix - gray(img.pixel(w+1, row)));
                diff += Math.abs(pix - gray(img.pixel(w, row-1)));
                diff += Math.abs(pix - gray(img.pixel(w, row+1)));
                if (diff <= DIFF_TRIGGER) {
                    diff = 0;
                }
                rowTrigger = ((rowTrigger * AVERAGE_MULT) + diff) / AVERAGE_COUNT;
                if (rowTrigger > AVERAGE_TRIGGER) {
                    break;
                }
            }

            if (rowTrigger > AVERAGE_TRIGGER) {
                // row == 1 _probably_ means that the selection should start from 0
                // but that can not be detected if we start from 1 => include one extra column
                if (row == 1) row = 0;
                if (row == (img.width() -2)){
                    row = img.width();
                }
                return row;
            }
            row += addSub;
        }
        return row;
    }

    private int refineColumn(int fromCol, int toCol, int rowStart, int rowEnd)
    {
        int pix;
        int diff;
        float colTrigger;
        int col;
        int count;
        int addSub = (fromCol < toCol) ? 1 : -1;

        rowStart -= 2; //add some margin
        rowEnd += 2; //add some margin

        if (rowStart < 1){
            rowStart = 1;
        }
        if (rowEnd >= img.height()-1) {
            rowEnd = img.height() - 2;
        }
        if (fromCol < 1){
            fromCol = 1;
        }
        if (fromCol >= img.width()-1){
            fromCol = img.width() - 2;
        }
        if (toCol < 1){
            toCol = 1;
        }
        if (toCol >= img.width()-1){
            toCol = img.width() - 2;
        }
        col = fromCol;
        while (col != toCol) {
            colTrigger = 0;
            count = 0;
            for (int row=rowStart; row<rowEnd; row++) {
                count++;
                diff = 0;
                pix = gray(img.pixel(col, row));
                // how much does the pixel differ from the surrounding
                diff += Math.abs(pix - gray(img.pixel(col-1, row)));
                diff += Math.abs(pix - gray(img.pixel(col+1, row)));
                diff += Math.abs(pix - gray(img.pixel(col, row-1)));
                diff += Math.abs(pix - gray(img.pixel(col, row+1)));
                if (diff <= DIFF_TRIGGER) diff = 0;

                colTrigger = ((colTrigger * AVERAGE_MULT) + diff) / AVERAGE_COUNT;

                if (colTrigger > AVERAGE_TRIGGER) {
                    break;
                }
            }

            if (colTrigger > AVERAGE_TRIGGER) {
                // col == 1 _probably_ means that the selection should start from 0
                // but that can not be detected if we start from 1 => include one extra column
                if (col == 1) col = 0;
                if (col == (img.width() -2)){
                    col = img.width();
                }
                return col;
            }
            col += addSub;
        }
        return col;
    }    
    
    private static int gray(final int rgb){
        final Color color = new Color(rgb);
        return (color.getRed() * 11 + color.getGreen() * 16 + color.getBlue()* 5)/32;
    }
}
