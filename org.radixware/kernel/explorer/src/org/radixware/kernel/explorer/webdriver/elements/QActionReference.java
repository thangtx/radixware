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

package org.radixware.kernel.explorer.webdriver.elements;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QWidget;
import java.lang.ref.WeakReference;
import org.radixware.kernel.explorer.webdriver.WebDrvServer;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class QActionReference extends UIElementReference{

    private final WeakReference<QWidget> ownerWidgetRef;
    private final WeakReference<QAction> actionRef;    
    
    public QActionReference(final QWidget widget, final QAction action){
        super(UIElementId.newInstance());
        ownerWidgetRef = new WeakReference<>(widget);        
        actionRef = new WeakReference<>(action);
    }
    

    public QWidget getWidget() throws WebDrvException{
        final QWidget result = ownerWidgetRef.get();
        if (result==null || result.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }else{
            return result;
        }
    }
    
    private QAction getAction() throws WebDrvException{
        final QWidget widget = ownerWidgetRef.get();
        if (widget==null || widget.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        final QAction action = actionRef.get();
        if (action==null || action.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return action;
    }

    @Override
    public boolean isSelected() throws WebDrvException {
        final QAction action = getAction();
        return action.isCheckable() && action.isChecked();
    }

    @Override
    protected UIElementPropertySet getProperties() throws WebDrvException {
        return new UIElementPropertySet(getAction());
    }    

    @Override
    public String getDisplayedText() throws WebDrvException {
        final QWidget widget = getWidget();
        if (!widget.isVisible()){
            return "";
        }
        return getAction().text();
    } 

    @Override
    public String getSimpleClassName() throws WebDrvException {
        return "QAction";
    }        

    @Override
    public QRect getElementRect() throws WebDrvException {
        return getWidgetRect(getWidget());
    }        

    @Override
    public boolean isEnabled() throws WebDrvException {
        return getWidget().isEnabled() && getAction().isEnabled();
    }

    @Override
    public void clickAtCenterPoint(final WebDrvUIElementsManager manager) throws WebDrvException {
        final QWidget widget = getWidget();
        ensureInViewArea(widget);

        final QAction action = getAction();
        if (action.isVisible() && action.isEnabled()){
            QPoint p;
            if(widget instanceof QMenu) {
                p = ((QMenu)widget).actionGeometry(action).center();
            }
            else {
                p = getClickPoint(widget);
            }
            QPoint pGlobal = widget.mapToGlobal(p);
            WebDrvServer.postGlobalClick(pGlobal.x(), pGlobal.y());
        }else{
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
        }
    } 

    @Override
    public void clearText() throws WebDrvException {
        throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
    }

    @Override
    public void sendKeys(String keyEventsAsStr) throws WebDrvException {
        throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
    }        

	@Override
	public void scrollToView() throws WebDrvException {
		ensureInViewArea(this.getWidget());
	}

	@Override
	public boolean isDisplayed() throws WebDrvException {
		return this.getWidget().isVisible();
	}

    @Override
    public QPixmap getPixmap() throws WebDrvException {
        throw new WebDrvException(EWebDrvErrorCode.UNABLE_TO_CAPTURE_SCREEN);
    }

    @Override
    public boolean isInteractiveNow() throws WebDrvException {
        QWidget w = this.getWidget();
        return w.window() == findActiveWindow() && w.isEnabled() && w.isVisibleTo(w.window());
    }
}
