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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QWidget;
import java.lang.ref.WeakReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class ItemViewRowReference extends UIElementReference {

    private final WeakReference<QAbstractItemView> viewRef;
    private final ItemViewRowDescriptor rowDescriptor;

    public ItemViewRowReference(final QAbstractItemView view, final ItemViewRowDescriptor rowDescriptor) {
        super(UIElementId.newInstance());
        viewRef = new WeakReference<>(view);
        this.rowDescriptor = rowDescriptor;
    }
    
    private QAbstractItemView getView() throws WebDrvException{
        final QAbstractItemView view = viewRef.get();
        if (view==null || view.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return view;
    }        
    
    private QModelIndex getModelIndex() throws WebDrvException{
        final QAbstractItemView view = getView();
        final QModelIndex index = rowDescriptor.resolveToIndex(view.model());
        if (index==null){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }        
        return index;        
    }
    
    private int getColumnCount(final QAbstractItemModel model){
        return model instanceof QAbstractListModel ? 1 : model.columnCount();
    }
    
    private QModelIndex getFirstVisibleIndexInRow() throws WebDrvException{
        final QAbstractItemView view = getView();
        final QModelIndex index = getModelIndex();
        final QHeaderView header = getHorizontalHeader(view);
        if (header==null){
            return index;
        }else{
            final QAbstractItemModel model = view.model();            
            int logicalIndex;
            for (int i=0,count=getColumnCount(model); i<count; i++){
                logicalIndex = header.logicalIndex(i);
                if (!header.isSectionHidden(logicalIndex)){
                    if (logicalIndex==index.column()){
                        return index;
                    }else{
                        final QModelIndex parentIndex = model instanceof QAbstractListModel ? null : index.parent();
                        return model.index(index.row(), logicalIndex, parentIndex);
                    }
                }
            }
            return index;
        }        
    }
    
    private QModelIndex getLastVisibleIndexInRow() throws WebDrvException{
        final QAbstractItemView view = getView();
        final QModelIndex index = getModelIndex();
        final QHeaderView header = getHorizontalHeader(view);
        final QAbstractItemModel model = view.model();
        final int columnCount = getColumnCount(model);
        final QModelIndex parentIndex = model instanceof QAbstractListModel ? null : index.parent();
        if (header==null){
            return model.index(index.row(), columnCount-1, parentIndex);
        }else{
            int logicalIndex;
            for (int i=columnCount-1; i>=0; i--){
                logicalIndex = header.logicalIndex(i);
                if (!header.isSectionHidden(logicalIndex)){
                    if (logicalIndex==index.column()){
                        return index;
                    }else{                        
                        return model.index(index.row(), logicalIndex, parentIndex);
                    }
                }
            }
            return index;
        }
    }
    
    @Override
    public boolean isSelected() throws WebDrvException {
        final QAbstractItemView view = getView();
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex==null){
            return false;
        }
        final QModelIndex index = getModelIndex();
        if (currentIndex.row()!=index.row()){
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
        return new UIElementPropertySet(new ItemViewRowInfo(getView(), getModelIndex()));
    }      

    @Override
    public String getDisplayedText() throws WebDrvException {
        return "";
    }        

    @Override
    public String getSimpleClassName() throws WebDrvException {
        return "Row";
    }        

    @Override
    public QRect getElementRect() throws WebDrvException {
        final QAbstractItemView view = getView();
        final QRect firstCellRect = view.visualRect(getFirstVisibleIndexInRow());
        if (!firstCellRect.isValid() || firstCellRect.isNull() || firstCellRect.isEmpty()){
            return firstCellRect;
        }
        final QRect lastCellRect = view.visualRect(getLastVisibleIndexInRow());
        if (!lastCellRect.isValid() || lastCellRect.isNull() || lastCellRect.isEmpty()){
            return lastCellRect;
        }
        final int rowWidth = lastCellRect.x() + lastCellRect.width() - firstCellRect.height();
        TMP_RECT.setTopLeft(view.mapToGlobal(firstCellRect.topLeft()));
        TMP_RECT.setWidth(rowWidth);
        TMP_RECT.setHeight(firstCellRect.height());
        return TMP_RECT;
    }

    @Override
    public boolean isEnabled() throws WebDrvException {
        final QAbstractItemView view = getView();
        final QAbstractItemModel model = view.model();
        final QHeaderView header = getHorizontalHeader(view);
        final QModelIndex index = getModelIndex();
        final QModelIndex parentIndex = model instanceof QAbstractListModel ? null : index.parent();
        final int row = index.row();        
        QModelIndex cellIndex;
        for (int column=0,count=getColumnCount(model); column<count; column++){
            if (header==null || !header.isSectionHidden(column)){                
                cellIndex = model.index(row, column, parentIndex);
                if (model.flags(cellIndex).isSet(Qt.ItemFlag.ItemIsEnabled)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void clickAtCenterPoint(final WebDrvUIElementsManager manager) throws WebDrvException {
        final QAbstractItemView view = getView();
        clickAtPoint(view, getClickPoint(view, getFirstVisibleIndexInRow(), manager));
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
