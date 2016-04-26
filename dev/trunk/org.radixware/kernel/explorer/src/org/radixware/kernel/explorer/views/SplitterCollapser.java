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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.QEvent;
import static com.trolltech.qt.core.QEvent.Type.Hide;
import static com.trolltech.qt.core.QEvent.Type.Move;
import static com.trolltech.qt.core.QEvent.Type.Resize;
import static com.trolltech.qt.core.QEvent.Type.Show;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimeLine;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionToolButton;
import com.trolltech.qt.gui.QStylePainter;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;


public final class SplitterCollapser extends QToolButton{

    private static class ArrowType{
        
        private final Qt.ArrowType visible;
        private final Qt.ArrowType invisible;
        
        public ArrowType(final Qt.ArrowType visible, final Qt.ArrowType invisible){
            this.visible = visible;
            this.invisible = invisible;            
        }
        
        public Qt.ArrowType get(final boolean isCollapsed){
            return isCollapsed ? invisible : visible;
        }
    }
    
    private static enum EDirection{
        
        LTR(new ArrowType(Qt.ArrowType.LeftArrow,  Qt.ArrowType.RightArrow)),
        RTL(new ArrowType(Qt.ArrowType.RightArrow,  Qt.ArrowType.LeftArrow)),
        TTB(new ArrowType(Qt.ArrowType.UpArrow,  Qt.ArrowType.DownArrow)),
        BTT(new ArrowType(Qt.ArrowType.DownArrow,  Qt.ArrowType.UpArrow));
        
        private final ArrowType arrowType;
        
        private EDirection(final ArrowType arrowType){
            this.arrowType = arrowType;
        }
        
        public Qt.ArrowType getArrowType(final boolean isCollapsed){
            return arrowType.get(isCollapsed);
        }
    }
    
    private static EnumSet<QEvent.Type> PROCESSABLE_EVENTS = 
        EnumSet.of(QEvent.Type.Resize, QEvent.Type.Move, QEvent.Type.Show, QEvent.Type.Hide, QEvent.Type.MouseMove);
    
    private static int TIMELINE_DURATION = 500;

    private static double MINIMUM_OPACITY = 0.3;    
        
    private class EventListener extends QEventFilter{
                
        public EventListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(PROCESSABLE_EVENTS);
        }

        @Override
        public boolean eventFilter(final QObject object, final QEvent event) {
            final QEvent.Type eventType = event==null ? null : event.type();
            if (eventType!=null && object == ownWidget) {//NOPMD
                switch (eventType) {
                        case Resize:
                            updatePosition();
                            updateOpacity();
                            break;
                        case Move:
                        case Show:
                        case Hide:
                            updatePosition();
                            updateOpacity();
                            updateArrow();
                            break;
                }
            } else { /* app */
                if (eventType == QEvent.Type.MouseMove) {
                    updateOpacity();
                }
            }
            return false;
        }
        
    }    
        
    private final QSplitter splitter;
    private final QWidget ownWidget;
    private final QTimeLine opacityTimeLine = new QTimeLine(TIMELINE_DURATION, this);
    private final EDirection direction;
    private final EventListener eventListener = new EventListener(this);
    private int mSizeAtCollaps;
    
    
    SplitterCollapser(final QSplitter splitter, final int index){
        super();
        this.splitter = splitter;
        this.ownWidget = splitter.widget(index);
        setAttribute(Qt.WidgetAttribute.WA_NoChildEventsForParent);
        
        opacityTimeLine.setFrameRange((int)(MINIMUM_OPACITY * 1000), 1000);        
        opacityTimeLine.valueChanged.connect(this,"update()");
        
        setParent(splitter);
        ownWidget.installEventFilter(eventListener);
        QApplication.instance().installEventFilter(eventListener);
        
        final boolean isVertical = splitter.orientation() == Qt.Orientation.Vertical;
        
	if (splitter.indexOf(ownWidget) < splitter.count() / 2) {            
            direction = isVertical ? EDirection.TTB : EDirection.LTR;
	} else {
            direction = isVertical ? EDirection.BTT : EDirection.RTL;
	}

        clicked.connect(this,"slotClicked()");	
	show();
    }
    
    public int getIndex(){
        return splitter.indexOf(ownWidget);
    }
    
    public void collapse() {
        if (!isCollapsed()) {
            slotClicked();
        }
    }

    public void restore() {
        if (isCollapsed()){
            slotClicked();
        }
    }

    public void setCollapsed(boolean isCollapsed) {
        if (isCollapsed != isCollapsed()){
            slotClicked();
        }
    }    
    
    private boolean isVertical() {
            return direction==EDirection.BTT || direction==EDirection.TTB;
    }
    
    @SuppressWarnings("unused")
    private void slotClicked() {
        final List<Integer> sizes = splitter.sizes();
        int index = splitter.indexOf(ownWidget);
        if (isCollapsed()) {
            if (mSizeAtCollaps != 0) {
                sizes.set(index, mSizeAtCollaps);
            } else {
                if (isVertical()) {
                    sizes.set(index, ownWidget.sizeHint().height());
                } else {
                    sizes.set(index, ownWidget.sizeHint().width());
                }
            }          
        }else {
            mSizeAtCollaps = sizes.get(index);
            sizes.set(index, 0);            
        }
        splitter.setSizes(sizes);
    }
    
    @Override
    public void paintEvent(final QPaintEvent event) {        
        event.accept();
        final QStylePainter painter = new QStylePainter(this);
        try{
            final double opacity = opacityTimeLine.currentFrame() / 1000.;
            painter.setOpacity(opacity);

            final QStyleOptionToolButton opt = new QStyleOptionToolButton();
            initStyleOption(opt);
            if (direction == EDirection.LTR) {
                opt.rect().setLeft(-width());
            } else {
                opt.rect().setWidth(width() * 2);
            }
            painter.drawPrimitive(QStyle.PrimitiveElement.PE_PanelButtonTool, opt);

            final QStyleOptionToolButton opt2 = new QStyleOptionToolButton();
            initStyleOption(opt2);
            painter.drawControl(QStyle.ControlElement.CE_ToolButtonLabel, opt2);
        }finally{
            painter.end();
        }
    }
    
    @Override
    public QSize sizeHint(){
        int extent = style().pixelMetric(QStyle.PixelMetric.PM_ScrollBarExtent);
        final QSize sh = new QSize(extent * 3 / 4, extent * 240 / 100);
        if (isVertical()) {
            sh.transpose();
        }
        return sh;
    }
    
    private void updatePosition() {
        int x, y;
        final QRect widgetRect = ownWidget.geometry();
        int splitterWidth = splitter.width();
        int handleWidth = splitter.handleWidth();
        int width = width();

        if (!isVertical()) {
            y = 30;
            if (direction==EDirection.LTR) {
                x = isCollapsed() ? 0 : widgetRect.right() + handleWidth;
            } else { // RTL
                x = isCollapsed() ? (splitterWidth - handleWidth - width) : (widgetRect.left() - handleWidth - width);
            }
        } else {
            x = 0;
            y = 0;
        }
        move(x, y);
    }

    private boolean isCollapsed(){
        if (ownWidget.isVisible()) {
            final QRect widgetRect = ownWidget.geometry();
            final QPoint br = widgetRect.bottomRight();
            return (br.x() <= 0) || (br.y() <=0);
        }
        return true;
    }    

    private void updateArrow() {
        setArrowType(direction.getArrowType(isCollapsed()));
    }
    
    private void updateOpacity() {
        final QPoint pos = parentWidget().mapFromGlobal(QCursor.pos());
        final QRect opaqueRect = geometry();
        final boolean opaqueCollapser = opaqueRect.contains(pos);
        final int frame = opacityTimeLine.currentFrame();
        if (opaqueCollapser && frame == opacityTimeLine.startFrame()) {
            opacityTimeLine.setDirection(QTimeLine.Direction.Forward);
            startTimeLine();
        } else if (!opaqueCollapser && frame == opacityTimeLine.endFrame()) {
            opacityTimeLine.setDirection(QTimeLine.Direction.Backward);
            startTimeLine();
        }
    }
    
    private void startTimeLine() {
        if (opacityTimeLine.state() != QTimeLine.State.Running) {
            opacityTimeLine.start();
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (ownWidget.nativeId()!=0){
            ownWidget.removeEventFilter(eventListener);
        }
        QApplication.instance().removeEventFilter(eventListener);
        super.closeEvent(event);
    }
    
    
    
    
}
