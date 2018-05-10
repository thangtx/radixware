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
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QWidget;
import java.lang.ref.WeakReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class ItemViewCellReference extends UIElementReference {

    private final WeakReference<QAbstractItemView> viewRef;
    private final ItemViewCellDescriptor cellDescriptor;

    public ItemViewCellReference(final QAbstractItemView view, final ItemViewCellDescriptor cellDescriptor) {
        super(UIElementId.newInstance());
        viewRef = new WeakReference<>(view);
        this.cellDescriptor = cellDescriptor;
    }

    private QAbstractItemView getView() throws WebDrvException{
        final QAbstractItemView view = viewRef.get();
        if (view==null || view.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return view;
    }    
    
    private QModelIndex getModelIndex() throws WebDrvException{
        final QModelIndex index = cellDescriptor.resolveToIndex(getView().model());
        if (index==null){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return index;
    }    
    
    @Override
    public boolean isSelected() throws WebDrvException {
        final QAbstractItemView view = getView();
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex==null){
            return false;
        }
        final QModelIndex index = getModelIndex();
        if (currentIndex.row()!=index.row() || currentIndex.column()!=index.column()){
            return false;
        }
        if (view.model() instanceof QAbstractListModel){
            return true;
        }
        if (currentIndex.parent()==null){
            return index.parent()==null;
        }
        if (index.parent()==null){
            return false;
        }
        return currentIndex.parent().internalId()==index.parent().internalId();
    }      

    @Override
    protected UIElementPropertySet getProperties() throws WebDrvException {
        return new UIElementPropertySet(new ItemViewCellInfo(getView(), getModelIndex()));
    }        

    @Override
    public String getDisplayedText() throws WebDrvException {
        final QAbstractItemView view = getView();
        if (!view.isVisible()){
            return "";
        }
        final QModelIndex index = cellDescriptor.resolveToIndex(view.model());
        final QHeaderView horizontalHeader = getHorizontalHeader(view);
        if (horizontalHeader.isSectionHidden(index.column())){
            return "";
        }
        return (String)view.model().data(index, Qt.ItemDataRole.DisplayRole);
    }   

    @Override
    public String getSimpleClassName() throws WebDrvException {
        return "Cell";
    }

    @Override
    public QRect getElementRect() throws WebDrvException {
        final QAbstractItemView view = getView();
        final QRect rect = view.visualRect(getModelIndex());
        final QSize size = rect.size();
        TMP_RECT.setTopLeft(view.viewport().mapToGlobal(rect.topLeft()));
        TMP_RECT.setSize(size);

        return TMP_RECT;
    }

    @Override
    public boolean isEnabled() throws WebDrvException {
        final QAbstractItemModel model = getView().model();
        final QModelIndex index = getModelIndex();
        return model.flags(index).isSet(Qt.ItemFlag.ItemIsEnabled);
    }
    
    @Override
    public void clickAtCenterPoint(final WebDrvUIElementsManager manager) throws WebDrvException {
        final QAbstractItemView view = getView();
        clickAtPoint(view, getClickPoint(view, getModelIndex(), manager));
    }

    @Override
    public void clearText() throws WebDrvException {
        throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
    }
    
    @Override
    public void sendKeys(final String keyEventsAsStr) throws WebDrvException {
        final QAbstractItemView view = getView();
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
