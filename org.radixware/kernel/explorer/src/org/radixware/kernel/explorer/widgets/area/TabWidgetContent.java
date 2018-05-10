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
package org.radixware.kernel.explorer.widgets.area;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemPresenter;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaTabPresenter;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaController;

class TabWidgetContent extends QWidget implements IWidget {

    private final static QColor ALLOWED_AREA_COLOR = new QColor(0, 255, 0, 75);
    private final static QColor RESTRICTED_AREA_COLOR = new QColor(255, 0, 0, 75);

    private final WidgetAreaController widgetAreaController;
    private boolean itemIsDragging = false;
    private FrameObject frameObject;
    private QWidget draggableObject;
    private boolean isEditingModeEnabled = false;
    private final Map<ItemWidget, IWidgetAreaTabPresenter.ItemListener> listenersMap = new HashMap<>();

    public TabWidgetContent(WidgetAreaController widgetAreaController) {
        this.widgetAreaController = widgetAreaController;
    }

    @Override
    public boolean isDisposed() {
        return nativeId()==0;
    }

    @Override
    public IPeriodicalTask startTimer(TimerEventHandler handler) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void killTimer(IPeriodicalTask task) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getObjectName() {
        return getClass().getSimpleName();
    }

    public void addItem(QWidget item, IWidgetAreaTabPresenter.ItemListener itemListener, int gridLeft, int gridTop, int gridWidth, int gridHeight) {
        int cellsHCount = widgetAreaController.getGridWidth();
        int cellsVCount = widgetAreaController.getGridHeight();
        final int deltaX = width() / cellsHCount;
        final int deltaY = height() / cellsVCount;
        item.setGeometry(Math.round(gridLeft * deltaX), Math.round(deltaY * gridTop), Math.round(deltaX * gridWidth), Math.round(deltaY * gridHeight));
        item.setParent(this);
        item.setVisible(true);
        listenersMap.put((ItemWidget) item, itemListener);
    }

    @Override
    protected void paintEvent(QPaintEvent qpe) {
        if (itemIsDragging) {
            showGrid();
        }
        super.paintEvent(qpe);
    }

    private void showGrid() {
        QPainter painter = new QPainter(this);
        painter.setPen(new QPen(QColor.gray, 1, Qt.PenStyle.DotLine));
        int cellsHCount = widgetAreaController.getGridWidth();
        int cellsVCount = widgetAreaController.getGridHeight();
        final int deltaX = width() / cellsHCount; //Округлить шаг в меньшую сторону
        final int deltaY = height() / cellsVCount;
        int diffX = width() % cellsHCount == 0 ? 0 : 1;
        for (int i = 0; i < cellsHCount + diffX; i++) {
            painter.drawLine(i * deltaX, 0, i * deltaX, height());
        }
        int diffY = height() % cellsVCount == 0 ? 0 : 1;
        for (int i = 0; i < cellsVCount + diffY; i++) {
            painter.drawLine(0, i * deltaY, width(), i * deltaY);
        }
        if (frameObject != null) {
            QRect frameObjectRect = frameObject.geometry();
            boolean isIntersects = false;
            int xPos = frameObjectRect.x();
            int yPos = frameObjectRect.y();
            int x, y;
            if (frameObject.getStartPos() == FrameObject.StartPositions.left || frameObject.getStartPos() == FrameObject.StartPositions.topleft || 
                    frameObject.getStartPos() == FrameObject.StartPositions.bottomleft) {
                x = xPos - xPos / deltaX * deltaX <= (float) deltaX / 2 ? xPos / deltaX * deltaX : xPos / deltaX * deltaX + deltaX;
            } else {
                x = xPos - xPos / deltaX * deltaX < (float) deltaX / 2 ? xPos / deltaX * deltaX : xPos / deltaX * deltaX + deltaX;
            }
            if (frameObject.getStartPos() == FrameObject.StartPositions.top || frameObject.getStartPos() == FrameObject.StartPositions.topleft || 
                    frameObject.getStartPos() == FrameObject.StartPositions.topright) {
                y = yPos - yPos / deltaY * deltaY <= (float) deltaY / 2 ? yPos / deltaY * deltaY : yPos / deltaY * deltaY + deltaY;
            } else {
                y = yPos - yPos / deltaY * deltaY < (float) deltaY / 2 ? yPos / deltaY * deltaY : yPos / deltaY * deltaY + deltaY;
            }
            int w = ((float) frameObject.width() / deltaX) - (frameObject.width() / deltaX) < 0.5 ? frameObject.width() / deltaX * deltaX : frameObject.width() / deltaX * deltaX + deltaX;
            int h = ((float) frameObject.height() / deltaY) - (frameObject.height() / deltaY) < 0.5 ? frameObject.height() / deltaY * deltaY : frameObject.height() / deltaY * deltaY + deltaY;
            QRect rect = new QRect(x, y, w, h);
            for (QObject object : this.children()) {
                if (object instanceof ItemWidget) {
                    if (!object.equals(draggableObject)) {
                        if (rect.intersects(((QWidget) object).geometry())) {
                            isIntersects = true;
                            break;
                        }
                    }
                }
            }
            painter.fillRect(rect, isIntersects ? RESTRICTED_AREA_COLOR : ALLOWED_AREA_COLOR);
        }
    }

    @Override
    protected void resizeEvent(QResizeEvent qre) { //need to refresh all widgets
        if (frameObject != null) {
            frameObject.close();
            draggableObject = null;
            isEditingModeEnabled = false;
        }
        widgetAreaController.refreshTabs();
        super.resizeEvent(qre);
    }

    void onItemPressEvent(QWidget target, QPoint startPoint, QMouseEvent event) {
        QRect itemRect = target.geometry();
        itemIsDragging = true;
        draggableObject = target;
        int cellsHCount = widgetAreaController.getGridWidth();
        int cellsVCount = widgetAreaController.getGridHeight();
        final int deltaX = width() / cellsHCount;
        final int deltaY = height() / cellsVCount;
        if (frameObject == null && !isEditingModeEnabled) {
            frameObject = new FrameObject(this, target.pos(), target.mapToGlobal(startPoint), deltaX, deltaY, cellsHCount, cellsVCount, false);
            frameObject.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
            frameObject.setGeometry(itemRect);
            frameObject.setVisible(true);
            frameObject.lower();
            com.trolltech.qt.gui.QMouseEvent pressEvent = new com.trolltech.qt.gui.QMouseEvent(event.type(), frameObject.mapFromGlobal(target.mapToGlobal(event.pos())), event.button(), event.buttons(), event.modifiers());
            frameObject.mousePressEvent(pressEvent);
        }
        repaint();
    }

    void onItemReleaseEvent() {
        itemIsDragging = false;
        if (frameObject != null) {
            if (frameObject.nativeId() != 0) {
                QRect frameObjectRect = frameObject.geometry();
                int cellsHCount = widgetAreaController.getGridWidth();
                int cellsVCount = widgetAreaController.getGridHeight();
                final int deltaX = width() / cellsHCount;
                final int deltaY = height() / cellsVCount;
                int x,y;
                if (frameObject.getStartPos() == FrameObject.StartPositions.left || frameObject.getStartPos() == FrameObject.StartPositions.topleft || 
                    frameObject.getStartPos() == FrameObject.StartPositions.bottomleft) {
                    x = frameObjectRect.x() - frameObjectRect.x() / deltaX * deltaX <= (float) deltaX / 2 ? frameObjectRect.x() / deltaX : frameObjectRect.x() / deltaX + 1;
                } else {
                    x = frameObjectRect.x() - frameObjectRect.x() / deltaX * deltaX < (float) deltaX / 2 ? frameObjectRect.x() / deltaX : frameObjectRect.x() / deltaX + 1;
                }
                if (frameObject.getStartPos() == FrameObject.StartPositions.top || frameObject.getStartPos() == FrameObject.StartPositions.topleft || 
                    frameObject.getStartPos() == FrameObject.StartPositions.topright) {
                    y = frameObjectRect.y() - frameObjectRect.y() / deltaY * deltaY <= (float) deltaY / 2 ? frameObjectRect.y() / deltaY : frameObjectRect.y() / deltaY + 1;
                } else {
                    y = frameObjectRect.y() - frameObjectRect.y() / deltaY * deltaY < (float) deltaY / 2 ? frameObjectRect.y() / deltaY : frameObjectRect.y() / deltaY + 1;
                }
                int w = ((float) frameObject.width() / deltaX) - (frameObject.width() / deltaX) < 0.5 ? frameObject.width() / deltaX : frameObject.width() / deltaX + 1;
                int h = ((float) frameObject.height() / deltaY) - (frameObject.height() / deltaY) < 0.5 ? frameObject.height() / deltaY : frameObject.height() / deltaY + 1;
                listenersMap.get((ItemWidget) draggableObject).onChangeGeometry((ItemWidget) draggableObject, x, y, w, h);
                frameObject.setGeometry(draggableObject.geometry());
                if (!isEditingModeEnabled) {
                    frameObject.setParent(null);
                    frameObject.close();
                    frameObject = null;
                    draggableObject = null;
                }
            }
        } else {
            frameObject = null;
        }
        repaint();
    }

    void onItemMove(QMouseEvent event, QWidget target) {
        if (frameObject != null && frameObject.nativeId() != 0) {
            com.trolltech.qt.gui.QMouseEvent moveEvt = new com.trolltech.qt.gui.QMouseEvent(event.type(), frameObject.mapFromGlobal(target.mapToGlobal(event.pos())), event.button(), event.buttons(), event.modifiers());
            frameObject.mouseMoveEvent(moveEvt);
        }
    }

    void onItemDoubleClickEvent(QWidget target) {
        if (frameObject != null) {
            return;
        }
            isEditingModeEnabled = true;
            QRect itemRect = target.geometry();
            draggableObject = target;
            int cellsHCount = widgetAreaController.getGridWidth();
            int cellsVCount = widgetAreaController.getGridHeight();
            final int deltaX = width() / cellsHCount;
            final int deltaY = height() / cellsVCount;
            frameObject = new FrameObject(this, target.pos(), null, deltaX, deltaY, cellsHCount, cellsVCount, true);
            frameObject.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
            frameObject.setGeometry(itemRect);
            frameObject.setVisible(true);
            frameObject.setFocus();
            frameObject.focusedOut.connect(this, "onFocusOut()");
            frameObject.mouseRelease.connect(this, "onItemReleaseEvent()");
            frameObject.mousePress.connect(this, "onMousePress()");
    }

    @SuppressWarnings("unused")
    private void onMousePress() {
        itemIsDragging = true;
    }

    @SuppressWarnings("unused")
    private void onFocusOut() {
        if (isEditingModeEnabled && frameObject != null) {
            frameObject.setParent(null);
            frameObject.close();
            frameObject = null;
            draggableObject = null;
            isEditingModeEnabled = false;
            repaint();
        }
    }

    boolean isInEditingMode() {
        return isEditingModeEnabled;
    }

    @Override
    protected void mousePressEvent(QMouseEvent qme) {
        if (frameObject != null && !itemIsDragging) {
            frameObject.clearFocus();
        }
        super.mousePressEvent(qme);
    }

    void setBounds(ItemWidget itemObject, int gridTop, int gridLeft, int gridWidth, int gridHeight) {
        int cellsHCount = widgetAreaController.getGridWidth();
        int cellsVCount = widgetAreaController.getGridHeight();
        final int deltaX = width() / cellsHCount;
        final int deltaY = height() / cellsVCount;
        itemObject.setGeometry(gridLeft * deltaX, gridTop * deltaY, gridWidth * deltaX, gridHeight * deltaY);
    }

    void removeItem(IWidgetAreaItemPresenter item) {
        listenersMap.remove((ItemWidget) item);
        ((ItemWidget)item).disableGarbageCollection();
        ((ItemWidget) item).setParent(null);
        ((ItemWidget)item).disposeLater();
    }
    
    boolean isResizable(IWidgetAreaItemPresenter item) {
        IWidgetAreaTabPresenter.ItemListener listener = listenersMap.get((ItemWidget)item);
        if (listener != null) {
            return listener.isResizable(item);
        } else {
            return false;
        }
    }
}
