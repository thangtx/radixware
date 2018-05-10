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

package org.radixware.kernel.explorer.webdriver.elements.finders;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

final class PartialTextFinder extends UIElementFinder{
    
    private final String text;
    
    public PartialTextFinder(final String text){
        this.text = text;
    }
       
    @Override
    protected boolean isTarget(final QWidget widget) {
        Method method = findMethod(widget, "text");        
        if (method==null){
            method = findMethod(widget, "getText");
        }
        if (method==null){
            return false;
        }else{
            final Object result;
            try {
                result = method.invoke(widget);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
            return result instanceof String ? ((String)result).contains(text) : false;
        }
    }
    
    protected static Method findMethod(final QWidget widget, final String methodName){
        final Method method;
        try{
            method = widget.getClass().getMethod(methodName);
        }catch(NoSuchMethodException | SecurityException exception){
            return null;
        }
        if (Modifier.isPublic(method.getModifiers()) && method.getReturnType()==String.class){
            return method;
        }else{
            return null;
        }
    }
    
    @Override
    protected boolean isTarget(final QAction action) {
        final String actionText = action.text();
        return actionText==null || actionText.isEmpty() ? false : actionText.contains(text);
    }
    
    @Override
    protected Collection<ItemViewIndex> findIndexes(QAbstractItemModel model, boolean greedy, boolean useObjNames) {
        return super.findIndexes(model, greedy, true);
    }
    
    @Override
    protected boolean isTargetRow(final QAbstractItemModel model, final QModelIndex index) {
        return false;
    }

    @Override
    protected boolean isTargetCell(final QAbstractItemModel model, final QModelIndex index) {
        final Object data = model.data(index, Qt.ItemDataRole.DisplayRole); 
        return data instanceof String ? ((String)data).contains(text) : false;
    }

    @Override
    protected boolean isTargetTabButton(final QTabBar tabSet, final int index) {
        final String tabText = tabSet.tabText(index);
        return tabText!=null && tabText.contains(text);
    }
    
    
}