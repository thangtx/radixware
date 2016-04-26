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

import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSizeF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsItem;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QLineF;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;


final class SelectionItem extends QGraphicsItem{
    
    private static final double SEL_MARGIN = 4.0;
    private static final QPointF BOUND_MARGIN = new QPointF(SEL_MARGIN,SEL_MARGIN);
    private static final double ADD_REM_MARGIN = 8.0;
    private static final QPointF ADD_REM_MARGIN_POINT = new QPointF(ADD_REM_MARGIN, ADD_REM_MARGIN);    
    private static final QBrush WHITE_BRUSH = new QBrush(QColor.white);    
    
    public static enum EIntersects{None,Top,TopRight,Right,BottomRight,Bottom,BottomLeft,Left,TopLeft,Move,AddRemove};
    
    private final QPen penDark = new QPen();
    private final QPen penLight = new QPen();
    private final QPen penAddRemFg = new QPen();
    private QRectF rect;
    private double  maxX;
    private double  maxY;
    private boolean hasMaxX;
    private boolean hasMaxY;
    private boolean hasMax;
    private boolean isSaved;
    private boolean showAddRem;
    private double  invZoom = 1.0;
    private double  selMargin = SEL_MARGIN;
    private QRectF  addRemRect = new QRectF(0, 0, 0, 0);
    private final boolean singleSelectionMode;
    
    public SelectionItem(final QRectF rect, final boolean singleSelection){
        setRect(rect);
        this.singleSelectionMode = singleSelection;
        penDark.setColor(QColor.black);
        penDark.setStyle(Qt.PenStyle.SolidLine);
        penLight.setColor(QColor.white);
        penLight.setStyle(Qt.PenStyle.DashLine);
        penAddRemFg.setColor(QColor.darkGreen);
        penAddRemFg.setStyle(Qt.PenStyle.SolidLine);
        penAddRemFg.setWidth(3);
    }

    @Override
    public QRectF boundingRect() {
        final QRectF tmp = new QRectF(rect.topLeft().clone().subtract(BOUND_MARGIN), rect.bottomRight().clone().add(BOUND_MARGIN));
        if (tmp.top() > addRemRect.top()) {
            tmp.setTop(addRemRect.top());
        }
        if (tmp.left() > addRemRect.left()) {
            tmp.setLeft(addRemRect.left());
        }

        if (tmp.bottom() < addRemRect.bottom()) {
            tmp.setBottom(addRemRect.bottom());
        }

        if (tmp.right() < addRemRect.right()) {
            tmp.setRight(addRemRect.right());
        }
        return tmp;
    }

    @Override
    public void paint(final QPainter painter, final QStyleOptionGraphicsItem qsogi, final QWidget qw) {
        painter.setPen(penDark);
        painter.drawRect(rect);

        painter.setPen(penLight);
        painter.drawRect(rect);
        
        if (showAddRem) {
            painter.fillRect(addRemRect, WHITE_BRUSH);
            final QLineF minus = new QLineF(addRemRect.left()+3*invZoom, 
                                            rect.center().y(),
                                            addRemRect.right()-3*invZoom,
                                            rect.center().y());
            painter.setPen(penAddRemFg);
            painter.drawLine(minus);

            if (!isSaved) {
                final QLineF plus = new QLineF(rect.center().x(), 
                                               addRemRect.top()+3*invZoom,
                                               rect.center().x(),
                                               addRemRect.bottom()-3*invZoom);
                painter.drawLine(plus);
            }
        }
    }
    
    public void saveZoom(double zoom)
    {
        if (zoom < 0.00001) 
            zoom = 0.00001;
        
        invZoom = 1/zoom;
        selMargin = SEL_MARGIN * invZoom;

        final double margin = ADD_REM_MARGIN * invZoom;
        final QPointF pMargin =  ADD_REM_MARGIN_POINT.clone().multiply(invZoom);
        addRemRect = new QRectF(rect.center().clone().subtract(pMargin), new QSizeF(margin*2.0, margin*2.0));
        penAddRemFg.setWidthF(3.0 * invZoom);
    }

    public void setSaved(final boolean isSaved)
    {
        if (isSaved) {
            penDark.setColor(QColor.darkBlue);
            penLight.setColor(QColor.red);
            penAddRemFg.setColor(QColor.darkRed);
            this.isSaved = true;
        }
        else {
            penDark.setColor(QColor.black);
            penLight.setColor(QColor.white);
            penAddRemFg.setColor(QColor.darkGreen);
            this.isSaved = false;
        }
    }

    public void setMaxRight(final double maxX)
    {
        this.maxX = maxX;
        hasMaxX = true;
        if (hasMaxY){ 
            hasMax = true;
        }
    }

    public void setMaxBottom(final double maxY)
    {
        this.maxY = maxY;
        hasMaxY = true;
        if (hasMaxX){
            hasMax = true;
        }
    }
    
    public void setRect(final QRectF rect)
    {
        prepareGeometryChange();
        this.rect = rect.normalized();
        if (hasMax) {
            if (this.rect.top() < 0) this.rect.setTop(0);
            if (this.rect.left() < 0) this.rect.setLeft(0);
            if (this.rect.right() > maxX) this.rect.setRight(maxX);
            if (this.rect.bottom() > maxY) this.rect.setBottom(maxY);
        }

        // calculate the add/remove rectangle
        final double margin = ADD_REM_MARGIN * invZoom;
        final QPointF pMargin = ADD_REM_MARGIN_POINT.clone().multiply(invZoom);
        addRemRect = new QRectF(this.rect.center().clone().subtract(pMargin), new QSizeF(margin*2, margin*2));
    }    

    public EIntersects intersects(final QPointF point)
    {
        final boolean oldState = showAddRem;
        showAddRem = false;

        if ((point.x() < (rect.left()-selMargin)) ||
            (point.x() > (rect.right()+selMargin)) ||
            (point.y() < (rect.top()-selMargin)) ||
            (point.y() > (rect.bottom()+selMargin)))
        {
            if (oldState != showAddRem) {
                repaint();
            }
            return EIntersects.None;
        }

        if (point.x() < (rect.left()+selMargin)) {
            if (oldState != showAddRem){
                repaint();
            }
            if (point.y() < (rect.top()+selMargin)){
                return EIntersects.TopLeft;
            }
            if (point.y() > (rect.bottom()-selMargin)){
                return EIntersects.BottomLeft;
            }
            return EIntersects.Left;
        }

        if (point.x() > (rect.right()-selMargin)) {
            if (oldState != showAddRem) {
                repaint();
            }
            if (point.y() < (rect.top()+selMargin)){
                return EIntersects.TopRight;
            }
            if (point.y() > (rect.bottom()-selMargin)){
                return EIntersects.BottomRight;
            }   
            return EIntersects.Right;
        }

        if (point.y() < (rect.top()+selMargin)) {
            if (oldState != showAddRem){
                repaint();
            }
            return EIntersects.Top;
        }
        if (point.y() > (rect.bottom()-selMargin)) {
            if (oldState != showAddRem){
                repaint();
            }            
            return EIntersects.Bottom;
        }

        if (singleSelectionMode){
            return EIntersects.Move;
        }else{
            showAddRem = true;        
            if (oldState != showAddRem){
                repaint();
            }

            if ((point.x() > addRemRect.left()) &&
                (point.x() < addRemRect.right()) &&
                (point.y() > addRemRect.top()) &&
                (point.y() < addRemRect.bottom())){
                return EIntersects.AddRemove;
            }
            return EIntersects.Move;
        }
    }
    
    private void repaint(){//update() does not work for unknown reasons
        if (!scene().views().isEmpty()){
            for (QGraphicsView view: scene().views()){
                view.viewport().repaint();
            }
        }
    }
    
    public QPointF fixTranslation(final QPointF dp)
    {
        if ((rect.left() + dp.x()) < 0) {
            dp.setX(-rect.left());
        }
        if ((rect.top() + dp.y()) < 0) {
            dp.setY(-rect.top());
        }
        if ((rect.right() + dp.x()) > maxX) {
            dp.setX(maxX - rect.right());
        }
        if ((rect.bottom() + dp.y()) > maxY){ 
            dp.setY(maxY - rect.bottom());
        }
        return dp;
    }

    public QRectF rect(){
        return rect;
    }    
}
