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

package org.radixware.kernel.explorer.webdriver.input;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.elements.UIElementId;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class PointerMoveAction extends InputAction<PointerInputSource> implements IPauseAction{

    public static enum EOrigin {VIEWPORT, POINTER, ELEMENT}
    
    private final static QPoint MOVE_POINT = new QPoint();
    
    private final EOrigin origin;
    private final UIElementId elementId;
    private final int duration;
    private final Integer x;
    private final Integer y;
    
    public PointerMoveAction(final PointerInputSource inputSource, 
                             final EOrigin origin, 
                             final Integer x, 
                             final Integer y, 
                             final Integer duration){
        super(inputSource);
        this.origin = origin;
        elementId = null;
        this.x = x;
        this.y = y;
        this.duration = duration==null ? 0 : duration;
    }
    
    public PointerMoveAction(final PointerInputSource inputSource, 
                             final UIElementId elementId, 
                             final Integer x, 
                             final Integer y, 
                             final Integer duration){
        super(inputSource);
        origin = EOrigin.ELEMENT;
        this.elementId = elementId;
        this.x = x;
        this.y = y;
        this.duration = duration==null ? -1 : duration;
    }
    
    @Override
    public int getDuration() {
        return duration;
    }    
    
    @Override
    public void dispatch(final WebDrvSession session, int tickDuration, long tickStart) throws WebDrvException {
        final PointerInputSourceState state = getInputSource().getState();
        final WebDrvInputSourceManager manager = session.getInputSourceManager();
        final int xOffset = x==null ? 0 : x;
        final int yOffset = y==null ? 0 : y;        
        final int targetX;
        final int targetY;
        int startX = state.getX();
        int startY = state.getY();        
        switch(origin){
            case VIEWPORT:
                targetX = xOffset;
                targetY = yOffset;
                break;
            case POINTER:
                targetX = startX + xOffset;
                targetY = startY + yOffset;
                break;                
            case ELEMENT:
                final UIElementReference element = 
                    session.getUIElements().getUIElementReference(elementId);
                final QRect rect = element.getElementRect();
                final QPoint center = rect.center();
                targetX = center.x() + xOffset;
                targetY = center.y() + yOffset;
                break;
            default:
                throw new IllegalArgumentException("Unknown origin "+origin.name());
        }
        final int maxX = QApplication.desktop().availableGeometry().width();                
        if (targetX<0 || targetX>maxX){
            throw new WebDrvException(EWebDrvErrorCode.MOVE_TARGET_OUT_OF_BOUNDS, "x coordinate "+targetX+" is out of bounds [0;"+maxX+"]");
        }        
        final int maxY = QApplication.desktop().availableGeometry().height();
        if (targetY<0 || targetY>maxY){
            throw new WebDrvException(EWebDrvErrorCode.MOVE_TARGET_OUT_OF_BOUNDS, "y coordinate "+targetY+" is out of bounds [0;"+maxY+"]");
        }
        QCursor.setPos(startX, startY);
        float ratio;
        do{
            int effectiveDuration = duration<0 ? tickDuration : duration;
            int timeDelta = (int)(System.currentTimeMillis() - tickStart);
            int nextX, nextY;
            ratio = effectiveDuration==0 ? 1 : (float)timeDelta/duration;            
            if (ratio>=1){
                nextX = targetX;
                nextY = targetY;
            }else{
                if (startX==targetX){
                    nextX = targetX;
                }else{
                    int deltaX = Math.round(ratio * (targetX - startX));
                    nextX = startX + deltaX;                                        
                }
                if (startY==targetY){
                    nextY=targetY;
                }else{
                    int deltaY = Math.round(ratio * (targetY - startY));
                    nextY = startY + deltaY;                    
                }
            }
            if (nextX!=state.getX() || nextY!=state.getY()){
                MOVE_POINT.setX(nextX);
                MOVE_POINT.setY(nextY);
                final QWidget targetWidget = QApplication.widgetAt(MOVE_POINT);
                state.setPos(nextX, nextY);
                if (targetWidget!=null && targetWidget.nativeId()!=0){            
                    final QMouseEvent event = 
                        new QMouseEvent(QEvent.Type.MouseMove, targetWidget.mapFromGlobal(MOVE_POINT), Qt.MouseButton.NoButton, state.getButtons(), manager.getKeyboardModifiers());
                    QApplication.postEvent(targetWidget, event);
                    int pause = Math.round(1/ratio);
                    if (pause==0){
                        pause=1;
                    }
                    session.getUIElements().pause(pause);
                }
                QCursor.setPos(MOVE_POINT);
            }else{
                session.getUIElements().pause(40);
            }
            startX = nextX;
            startY = nextY;            
        }while(startX!=targetX || startY!=targetY);
    }    
}
