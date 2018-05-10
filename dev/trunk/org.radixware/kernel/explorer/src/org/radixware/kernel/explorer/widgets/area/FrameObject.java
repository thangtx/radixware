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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QWidget;

class FrameObject extends QWidget {

    public static final int EDGE_SIZE_PX = 8;
    int frameBorderWidth = 6;
    private QPoint dragStartPosition = null;
    private QRect dragStartGeometry = null;
    Signal0 focusedOut = new Signal0();
    Signal1<QEvent> mousePress = new Signal1<>();
    Signal0 mouseRelease = new Signal0();
    private final QWidget parent;
    private final int deltaX;
    private final int deltaY;
    private final int cellsHCount;
    private final int cellsVCount;

    enum StartPositions {

        topleft, left, bottomleft, bottom, bottomright, right, topright, top, move
    }
    private StartPositions startPos = null;
    private final boolean isInEditingMode;

    FrameObject(QWidget parent, QPoint itemPos, QPoint startPos, int deltaX, int deltaY, int cellsHCount, int cellsVCount, boolean isInEditingMode) {
        super(parent);
        this.parent = parent;
        this.isInEditingMode = isInEditingMode;
        if (isInEditingMode) {
            setMouseTracking(true);
        }
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.cellsHCount = cellsHCount;
        this.cellsVCount = cellsVCount;
    }

    @Override
    protected void focusOutEvent(QFocusEvent qfe) {
        focusedOut.emit();
        super.focusOutEvent(qfe);
    }

    @Override
    protected void paintEvent(QPaintEvent qpe) {
        super.paintEvent(qpe);
        com.trolltech.qt.gui.QPainter p = new com.trolltech.qt.gui.QPainter(this);
        p.setPen(com.trolltech.qt.gui.QColor.gray);
        paintBorder(p);
    }

    protected void paintBorder(final com.trolltech.qt.gui.QPainter p) {
        final int width = width() - 1;
        final int height = height() - 1;
        p.drawRect(EDGE_SIZE_PX / 2, EDGE_SIZE_PX / 2, width - EDGE_SIZE_PX + 1, height - EDGE_SIZE_PX + 1);
        com.trolltech.qt.gui.QColor color = this.palette().color(com.trolltech.qt.gui.QPalette.ColorRole.Window);
        // top
        drawEdge(p, (width - EDGE_SIZE_PX) / 2, 0, color);
        // right
        drawEdge(p, width - EDGE_SIZE_PX, (height - EDGE_SIZE_PX) / 2, color);
        // bottom
        drawEdge(p, (width - EDGE_SIZE_PX) / 2, height - EDGE_SIZE_PX, color);
        // left
        drawEdge(p, 0, (height - EDGE_SIZE_PX) / 2, color);
        // top left
        drawEdge(p, 0, 0, color);
        // top right
        drawEdge(p, width - EDGE_SIZE_PX, 0, color);
        // bottom left
        drawEdge(p, 0, height - EDGE_SIZE_PX, color);
        // bottom right
        drawEdge(p, width - EDGE_SIZE_PX, height - EDGE_SIZE_PX, color);
    }

    private static void drawEdge(com.trolltech.qt.gui.QPainter p, int x, int y, com.trolltech.qt.gui.QColor color) {
        p.drawRect(x, y, EDGE_SIZE_PX, EDGE_SIZE_PX);
        p.fillRect(x + 1, y + 1, EDGE_SIZE_PX - 1, EDGE_SIZE_PX - 1, color);
    }

    @Override
    protected void mousePressEvent(QMouseEvent qme) {
        dragStartPosition = qme.pos();
        dragStartGeometry = geometry();
        mousePress.emit(qme);
        parent.repaint();
    }

    @Override
    protected void mouseMoveEvent(QMouseEvent evt) {
        QCursor cursor = cursor();
        if (!evt.buttons().isSet(com.trolltech.qt.core.Qt.MouseButton.LeftButton)) {
            if (evt.x() <= frameBorderWidth && evt.y() <= frameBorderWidth) {
                startPos = StartPositions.topleft;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeFDiagCursor);
            } else if (evt.x() <= frameBorderWidth && evt.y() >= height() - frameBorderWidth) {
                startPos = StartPositions.bottomleft;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeBDiagCursor);
            } else if (evt.x() >= width() - frameBorderWidth && evt.y() <= frameBorderWidth) {
                startPos = StartPositions.topright;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeBDiagCursor);
            } else if (evt.x() >= width() - frameBorderWidth && evt.y() >= height() - frameBorderWidth) {
                startPos = StartPositions.bottomright;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeFDiagCursor);
            } else if (evt.x() <= frameBorderWidth) {
                startPos = StartPositions.left;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeHorCursor);
            } else if (evt.x() >= width() - frameBorderWidth) {
                startPos = StartPositions.right;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeHorCursor);
            } else if (evt.y() <= frameBorderWidth) {
                startPos = StartPositions.top;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeVerCursor);
            } else if (evt.y() >= height() - frameBorderWidth) {
                startPos = StartPositions.bottom;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeVerCursor);
            } else {
                startPos = StartPositions.move;
                cursor.setShape(com.trolltech.qt.core.Qt.CursorShape.SizeAllCursor);
            }
            setCursor(cursor);
            return;
        }
        if (evt.buttons().isSet(Qt.MouseButton.LeftButton)) {
            if ((startPos == null || dragStartGeometry == null) && isInEditingMode) {
                return;
            } else if (!isInEditingMode) {
                startPos = StartPositions.move;
            }

            int posX = mapToParent(evt.pos()).x(),
                    posY = mapToParent(evt.pos()).y(),
                    width, height,
                    dX = dragStartPosition.x() - evt.x(),
                    dY = dragStartPosition.y() - evt.y(),
                    mousePosX = evt.x(),
                    mousePosY = evt.y();

            if (posX <= 0) {
                dX = dragStartGeometry.x();
            }
            if (posY <= 0) {
                dY = dragStartGeometry.y();
            }
            if (posX >= cellsHCount * deltaX) {
                mousePosX = cellsHCount * deltaX - dragStartGeometry.x();
            }
            if (posY >= cellsVCount * deltaY) {
                mousePosY = cellsVCount * deltaY - dragStartGeometry.y();
            }

            startPos = startPos != null ? startPos : StartPositions.move;
            switch (startPos) {
                case topleft:
                    posX = dragStartGeometry.left() - dX;
                    posY = dragStartGeometry.top() - dY;
                    width = dragStartGeometry.width() + dX;
                    height = height() + dY;
                    if (width < deltaX * 2) { //min width
                        posX = dragStartGeometry.left() + dragStartGeometry.width() - 2 * deltaX; //need to do left + width because of bag in QT with right() 
                        width = deltaX * 2;                                                       //it's actually right() - 1
                    }
                    if (height < deltaY * 2) { //min height
                        posY = dragStartGeometry.top() + dragStartGeometry.height() - 2 * deltaY;
                        height = deltaY * 2;
                    }
                    break;

                case bottomleft:
                    posX = dragStartGeometry.left() - dX;
                    posY = dragStartGeometry.top();
                    width = dragStartGeometry.width() + dX;
                    height = mousePosY;
                    if (width < deltaX * 2) { //min width
                        posX = dragStartGeometry.left() + dragStartGeometry.width() - 2 * deltaX;
                        width = deltaX * 2;
                    }
                    break;

                case topright:
                    posX = dragStartGeometry.left();
                    posY = dragStartGeometry.top() - dY;
                    width = mousePosX;
                    height = height() + dY;
                    if (height < deltaY * 2) { //min height
                        posY = dragStartGeometry.top() + dragStartGeometry.height() - 2 * deltaY;
                        height = deltaY * 2;
                    }
                    break;

                case bottomright:
                    posX = dragStartGeometry.left();
                    posY = dragStartGeometry.top();
                    width = mousePosX;
                    height = mousePosY;
                    break;

                case left:
                    posX = dragStartGeometry.left() - dX;
                    posY = dragStartGeometry.top();
                    width = dragStartGeometry.width() + dX;
                    height = height();
                    if (width < deltaX * 2) { //min width
                        posX = dragStartGeometry.left() + dragStartGeometry.width() - 2 * deltaX;
                        width = deltaX * 2;
                    }
                    break;

                case right:
                    posX = dragStartGeometry.left();
                    posY = dragStartGeometry.top();
                    width = mousePosX;
                    height = height();
                    break;

                case top:
                    posX = dragStartGeometry.left();
                    posY = dragStartGeometry.top() - dY;
                    width = dragStartGeometry.width();
                    height = height() + dY;
                    if (height < deltaY * 2) { //min height
                        posY = dragStartGeometry.top() + dragStartGeometry.height() - 2 * deltaY;
                        height = deltaY * 2;
                    }
                    break;

                case bottom:
                    posX = dragStartGeometry.left();
                    posY = dragStartGeometry.top();
                    width = width();
                    height = mousePosY;
                    break;

                case move:
                    posX = dragStartGeometry.left() - dX;
                    posY = dragStartGeometry.top() - dY;
                    width = width();
                    height = height();

                    posX = posX >= 0 ? posX : 0;
                    posY = posY >= 0 ? posY : 0;
                    int maxX = cellsHCount * deltaX - width;
                    posX = posX >= maxX ? maxX : posX;
                    int maxY = cellsVCount * deltaY - height;
                    posY = posY >= maxY ? maxY : posY;
                    break;

                default:
                    return;
            }
 
            if (width < deltaX * 2) {
                width = deltaX * 2;
            }
            if (height < deltaY * 2) {
                height = deltaY * 2;
            }
            setGeometry(posX, posY, width, height);
            dragStartGeometry = geometry();
            parent.repaint();
        } else {
            super.mouseMoveEvent(evt);
        }
    }
    
    StartPositions getStartPos() {
        return startPos;
    }

    @Override
    protected void mouseReleaseEvent(QMouseEvent qme) {
        mouseRelease.emit();
        super.mouseReleaseEvent(qme);
    }

    @Override
    protected void keyPressEvent(com.trolltech.qt.gui.QKeyEvent ev) {
        if (ev.key() == com.trolltech.qt.core.Qt.Key.Key_Escape.value()
                || ev.key() == com.trolltech.qt.core.Qt.Key.Key_Return.value()
                || ev.key() == com.trolltech.qt.core.Qt.Key.Key_Enter.value()) {
            clearFocus();
        }
    }

}
