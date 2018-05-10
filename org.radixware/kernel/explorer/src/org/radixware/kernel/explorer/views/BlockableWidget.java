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
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ThreadDumper;
import org.radixware.kernel.common.client.widgets.IBlocakbleWidget;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;

/**
 * Умеет показывать "старое" состояние виджета на время пока в нем происходят промежуточные изменения, показ которых нежелателен.
 * Использование метода setUpdatesEnabled() часто бывает недостаточно (не работает когда сверху показывается другое окно, например, окно ожидания)
 * или нежелательно (блокировка перерисовки всего окна приводит к нежелательным эффектам, например, TWRBS-1247).
 * Данный класс сохраняет виджет в картинку и показывает ее во время длительных операций.
 */
public class BlockableWidget extends ExplorerWidget implements IBlocakbleWidget {
    
    private static final class RepaintMainWidgetEvent extends QEvent{
        public RepaintMainWidgetEvent(){
            super(QEvent.Type.User);
        }
    }

    private static final QPoint START_POINT = new QPoint(0, 0);
    
    private final QVBoxLayout mainLayout = WidgetUtils.createVBoxLayout(this);
    private final QStackedLayout stackedLayout = new QStackedLayout(mainLayout);    
    private QPixmap pixmap;
    private final QWidget snapshot = new QWidget(this) {

        @Override
        protected void paintEvent(final QPaintEvent event) {
            super.paintEvent(event);
            if (pixmap != null) {
                final QPainter painter = new QPainter();
                painter.begin(this);
                painter.drawPixmap(this.rect(), pixmap);
                painter.end();
            }
        }

        @Override
        protected void customEvent(final QEvent event) {
            if (event instanceof FilteredMouseEvent){
                event.accept();
                filteredMouseEvent((FilteredMouseEvent)event);
            }
            super.customEvent(event);
        }
    };
    private boolean internalPainting;
    private boolean switchingWidget;
    private int blockRedraw = 0;    

    public BlockableWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        stackedLayout.setContentsMargins(0, 0, 0, 0);
        stackedLayout.setWidgetSpacing(0);
        stackedLayout.addStackedWidget(snapshot);
    }

    public BlockableWidget(final IClientEnvironment environment) {
        super(environment);
        stackedLayout.setContentsMargins(0, 0, 0, 0);
        stackedLayout.setWidgetSpacing(0);
        stackedLayout.addStackedWidget(snapshot);
    }

    public final void setWidget(final QWidget widget) {
        if (widget != null) {
            if (mainWidget() != null) {
                stackedLayout.removeWidget(mainWidget());
            }
            stackedLayout.addStackedWidget(widget);
            switchWidget(1);
        }
    }

    public final QWidget getWidget() {
        return mainWidget();
    }

    private QWidget mainWidget() {
        return stackedLayout.count() > 1 ? stackedLayout.widget(1) : null;
    }        

    @Override
    public final void blockRedraw() {
        if (switchingWidget){
            return;
        }
        if (blockRedraw == 0 && mainWidget().isVisible()) {            
            internalPainting = true;
            try {
                final QWidget widget = mainWidget();
                final QWidget window = widget.window();
                if (window!=null && window.isActiveWindow()){
                    final QPoint startPoint = widget.mapTo(window, START_POINT);
                    pixmap = 
                        QPixmap.grabWindow(window.winId(), startPoint.x(), startPoint.y(), widget.width(), widget.height());
                }else{
                    pixmap = QPixmap.grabWidget(widget);
                }
            } finally {
                internalPainting = false;
            }
            switchWidget(0);
        }        
        blockRedraw++;
    }
    
    @Override
    public final void unblockRedraw() {        
        if (blockRedraw > 0 && !switchingWidget) {            
            blockRedraw--;          
            if (blockRedraw == 0) {
                pixmap = null;
                switchWidget(1);
                QApplication.postEvent(this, new RepaintMainWidgetEvent());
            }
        }
    }

    public final void forceUnblockRedraw() {
        if (switchingWidget){
            getEnvironment().getTracer().warning("Unable to unblock redraw:\n"+ThreadDumper.dumpSync());
        }else{
            blockRedraw = 0;
            switchWidget(1);
            QApplication.postEvent(this, new RepaintMainWidgetEvent());
        }
    }
    
    private void switchWidget(final int index){
        switchingWidget = true;
        try{
            stackedLayout.setCurrentIndex(index);
        }finally{
            switchingWidget = false;
        }
    }
    
    protected void filteredMouseEvent(final FilteredMouseEvent event){
        final QWidget widget = getWidget();
        if (widget!=null && widget.nativeId()!=0){            
            final QRect rect =  widget.rect();
            rect.moveTopLeft(widget.mapToGlobal(WidgetUtils.ZERO_POINT));
            if (rect.contains(event.getGlobalPos())){
                QApplication.sendEvent(widget, new FilteredMouseEvent(event, widget));
            }
        }
    }

    @Override
    public boolean isInternalPaintingActive() {
        return internalPainting;
    }

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof RepaintMainWidgetEvent){
            qevent.accept();
            final QWidget widget = getWidget();
            if (widget!=null && widget.nativeId()!=0){
                widget.repaint();
            }
            return;
        }
        super.customEvent(qevent);
    }        
}
