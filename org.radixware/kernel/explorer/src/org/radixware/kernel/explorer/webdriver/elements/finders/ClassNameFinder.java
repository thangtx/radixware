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
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;

final class ClassNameFinder extends UIElementFinder{
    
    private final static String QACTION_CLASS_NAME = QAction.class.getName();
    
    private final String className;
    
    public ClassNameFinder(final String className){
        this.className = className;
    }
       
    @Override
    protected boolean isTarget(final QWidget widget) {
        for (Class cl=widget.getClass(); cl!=null; cl=cl.getSuperclass()){
            if (cl.getName().equals(className)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isTarget(final QAction action) {
        return QACTION_CLASS_NAME.equals(className);
    }        

    @Override
    protected Collection<ItemViewIndex> findIndexes(final QAbstractItemModel model, final boolean greedy, final boolean useObjNames) {
        if (!"Row".equals(className) && !"Cell".equals(className)){
            return Collections.emptyList();
        }
        return super.findIndexes(model, greedy, false);
    }
        
    @Override
    protected boolean isTargetRow(final QAbstractItemModel model, final QModelIndex index) {
        return "Row".equals(className);
    }

    @Override
    protected boolean isTargetCell(final QAbstractItemModel model, final QModelIndex index) {
        return "Cell".equals(className);
    }

    @Override
    protected boolean isTargetTabButton(final QTabBar tabSet, final int index) {
        return "TabBar".equals(className);
    }        
}
