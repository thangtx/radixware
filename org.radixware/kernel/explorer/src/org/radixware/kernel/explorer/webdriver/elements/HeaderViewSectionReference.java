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

import org.radixware.kernel.explorer.webdriver.WebDrvKeyEvents;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QScrollBar;
import com.trolltech.qt.gui.QWidget;
import java.lang.ref.WeakReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class HeaderViewSectionReference extends UIElementReference {

    private final WeakReference<QHeaderView> viewRef;
    private final HeaderViewSectionDescriptor sectionDescriptor;

    public HeaderViewSectionReference(final QHeaderView view, final HeaderViewSectionDescriptor sectionDescriptor) {
        super(UIElementId.newInstance());
        viewRef = new WeakReference<>(view);
        this.sectionDescriptor = sectionDescriptor;
    }

    private QHeaderView getView() throws WebDrvException{
        final QHeaderView view = viewRef.get();
        if (view==null || view.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return view;
    }  
    
    @Override
    public boolean isSelected() throws WebDrvException {
        return false;
    }      

    @Override
    protected UIElementPropertySet getProperties() throws WebDrvException {
        return new UIElementPropertySet(new HeaderViewSectionInfo(getView(), sectionDescriptor.getSectionIndex()));
    }        

    @Override
    public String getDisplayedText() throws WebDrvException {
        final QHeaderView view = getView();
        if (!view.isVisible()){
            return "";
        }
        final int index = sectionDescriptor.getSectionIndex();
        if (view.isSectionHidden(index)){
            return "";
        }
        return (String)view.model().headerData(index, view.orientation(), Qt.ItemDataRole.DisplayRole);
    }   

    @Override
    public String getSimpleClassName() throws WebDrvException {
        return "Section";
    }

    @Override
    public QRect getElementRect() throws WebDrvException {
        final QHeaderView view = getView();
        final QRect rect = view.rect();
        final int section = sectionDescriptor.getSectionIndex();
        TMP_RECT.setRect(0, 0, 0, 0);
        if (view.orientation()==Qt.Orientation.Horizontal){
            TMP_POINT.setX(view.sectionViewportPosition(section));
            TMP_POINT.setY(0);
            TMP_RECT.setWidth(view.sectionSize(section));
            TMP_RECT.setHeight(rect.height());
        }else{
            TMP_POINT.setX(0);
            TMP_POINT.setY(view.sectionViewportPosition(section));            
            TMP_RECT.setWidth(rect.width());
            TMP_RECT.setHeight(view.sectionSize(section));
        }
        TMP_RECT.moveTopLeft(view.mapToGlobal(TMP_POINT));
        return TMP_RECT;
    }

    @Override
    public boolean isEnabled() throws WebDrvException {
        return true;
    }
    
    @Override
    public void clickAtCenterPoint(final WebDrvUIElementsManager manager) throws WebDrvException {
        final QHeaderView view = getView();
        final int section = sectionDescriptor.getSectionIndex();
        final int visualIndex = view.visualIndex(section);
        if (visualIndex<0){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
        }
        ensureInViewArea(view);
        final int startPos = view.sectionPosition(section);
        final int endPos = startPos+view.sectionSize(section);
        final int delta;
        if (view.offset()>startPos){
            delta = startPos - view.offset();
        }else{
            final int offset = endPos - view.width() - view.offset();
            delta = offset>0 ? offset : 0;
        }
        if (delta!=0){
            QAbstractItemView ownerView = null;
            for (QWidget parent=view.parentWidget(); parent!=null && !parent.isWindow(); parent=parent.parentWidget()){
                if (parent instanceof QAbstractItemView){
                    ownerView = (QAbstractItemView)parent;
                    break;
                }
            }
            if (ownerView==null){
                view.setOffset(view.offset()+delta);
            }else {
                final QScrollBar scrollBar;
                final Qt.ScrollBarPolicy scrollPolicy;
                final QAbstractItemView.ScrollMode scrollMode;
                final Qt.Orientation orientation = view.orientation();
                if (orientation==Qt.Orientation.Vertical){
                    scrollPolicy = ownerView.verticalScrollBarPolicy();
                    scrollBar = ownerView.verticalScrollBar();   
                    scrollMode = ownerView.verticalScrollMode();
                    ownerView.setVerticalScrollMode(QAbstractItemView.ScrollMode.ScrollPerPixel);
                }else{
                    scrollPolicy = ownerView.horizontalScrollBarPolicy();
                    scrollBar = ownerView.horizontalScrollBar();
                    scrollMode = ownerView.horizontalScrollMode();
                    ownerView.setHorizontalScrollMode(QAbstractItemView.ScrollMode.ScrollPerPixel);
                }
                if (scrollPolicy==Qt.ScrollBarPolicy.ScrollBarAlwaysOff ||
                    scrollBar==null || scrollBar.nativeId()==0 || !scrollBar.isVisible()){
                    throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
                }
                if (scrollMode!=QAbstractItemView.ScrollMode.ScrollPerPixel){
                    setScrollMode(ownerView, orientation, QAbstractItemView.ScrollMode.ScrollPerPixel);
                }
                try{
                    scrollBar.setValue(scrollBar.value()+delta);                
                }finally{
                    if (scrollMode!=QAbstractItemView.ScrollMode.ScrollPerPixel){
                        setScrollMode(ownerView, orientation, scrollMode);
                    }                    
                }
            }
        }
        final QRect elementGlobalRect = getElementRect();
        if (!elementGlobalRect.isValid() || elementGlobalRect.isNull() || elementGlobalRect.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
        }
        final QSize size = elementGlobalRect.size();
        TMP_RECT.setTopLeft(view.mapFromGlobal(elementGlobalRect.topLeft()));
        TMP_RECT.setSize(size);
        final QRect visibleClickRect = view.viewport().visibleRegion().boundingRect().intersected(TMP_RECT);
        if (visibleClickRect.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
        }else{
            final QPoint clickPoint = visibleClickRect.center();
            clickAtPoint(view, clickPoint);
        }
    }
    
    private static void setScrollMode(final QAbstractItemView view, 
                                      final Qt.Orientation orientation, 
                                      final QAbstractItemView.ScrollMode mode){
        if (orientation==Qt.Orientation.Horizontal){
            view.setHorizontalScrollMode(mode);
        }else{
            view.setVerticalScrollMode(mode);
        }
    }

    @Override
    public void clearText() throws WebDrvException {
        throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
    }
    
    @Override
    public void sendKeys(final String keyEventsAsStr) throws WebDrvException {
        final QHeaderView view = getView();
        ensureInteractable(view);
        final WebDrvKeyEvents events = WebDrvKeyEvents.parse(keyEventsAsStr);
        events.post(view.viewport());
    } 

    @Override
    public void scrollToView() throws WebDrvException {
        ensureInViewArea(this.getView());
    }

    @Override
    public boolean isDisplayed() throws WebDrvException {
        return this.getView().isVisible();
    }

    @Override
    public QPixmap getPixmap() throws WebDrvException {
        QRect r = this.getElementRect();
        QPoint p = this.getView().mapFromGlobal(r.topLeft());
        r.adjust(p.x()-r.left(), p.y()-r.top(), p.x()-r.left(), p.y()-r.top());
        return QPixmap.grabWidget(this.getView(), r);
    }

    @Override
    public boolean isInteractiveNow() throws WebDrvException {
        QWidget w = this.getView();
        return w.window() == findActiveWindow() && w.isEnabled() && w.isVisibleTo(w.window());
    }
}
