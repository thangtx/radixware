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
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public abstract class UIElementFinder {
    
    public final static class Factory{
        
        private final static Pattern NAME_SELECTOR_PATTERN = Pattern.compile("\\*\\[name='(.*)'\\]");
        
        private Factory(){            
        }
        
        public static UIElementFinder newInstance(final EElementLocationStrategy using, final String selector) throws WebDrvException{
            switch(using){
                case NAME:
                    return new ObjectNameFinder(selector);
                case TAG_NAME:
                    return new SimpleClassNameFinder(selector);
                case CLASS_NAME:
                    return new ClassNameFinder(selector);
                case LINK_TEXT:
                    return new TextFinder(selector);
                case PARTIAL_LINK_TEXT:
                    return new PartialTextFinder(selector);
                case XPATH:
                    return new XPathFinder(selector);
                case CSS_SELECTOR:
                    final Matcher nameMatcher = NAME_SELECTOR_PATTERN.matcher(selector);
                    if (nameMatcher.find()){
                        return new ObjectNameFinder(nameMatcher.group(1));
                    }else{
                        throw new WebDrvException(EWebDrvErrorCode.UNSUPPORTED_OPERATION, "css location strategy is not supported with \'"+selector+"\' selector");
                    }           
                default:
                    throw new WebDrvException(EWebDrvErrorCode.UNSUPPORTED_OPERATION, using.name()+" location strategy is not supported");
            }
        }
    }
    
    protected interface ItemViewIndex{
        UIElementReference createReference(QAbstractItemView view, WebDrvUIElementsManager manager);
    };
    
    private final static class ItemViewRowIndex implements ItemViewIndex{
        
        private final QModelIndex index;
        private final boolean useObjNames;
        
        public ItemViewRowIndex(final QModelIndex index, final boolean useObjNames){
            this.index = index;
            this.useObjNames = useObjNames;
        }

        @Override
        public UIElementReference createReference(final QAbstractItemView view, final WebDrvUIElementsManager manager) {
            return manager.getRowReference(view, index, useObjNames);
        }
    }
    
    private final static class ItemViewCellIndex implements ItemViewIndex{
        
        private final QModelIndex index;
        private final boolean useObjNames;
        
        public ItemViewCellIndex(final QModelIndex index, final boolean useObjNames){
            this.index = index;
            this.useObjNames = useObjNames;
        }

        @Override
        public UIElementReference createReference(final QAbstractItemView view, final WebDrvUIElementsManager manager) {
            return manager.getCellReference(view, index, useObjNames);
        }
    }    
    
    protected final ItemViewIndex createRowIndex(final QModelIndex index, final boolean useObjNames){
        return new ItemViewRowIndex(index, useObjNames);
    }
    
    protected final ItemViewIndex createCellIndex(final QModelIndex index, final boolean useObjNames){
        return new ItemViewCellIndex(index, useObjNames);
    }
    
    public Collection<UIElementReference> findElements(final QWidget startFrom, final boolean greedy, final WebDrvUIElementsManager manager) throws WebDrvException{
        final Queue<QWidget> searchQueue = new LinkedList<>();
        final Collection<UIElementReference> result = new LinkedList<>();
        if (startFrom==null){
            final List<QWidget> widgets = QApplication.topLevelWidgets();
            for (QWidget widget: widgets) {
                if (widget!=null
                    && widget.nativeId()!=0
                    && widget.parent()==null
                    && (widget instanceof ICachableWidget==false || !((ICachableWidget)widget).isInCache())
                   ){
                   searchQueue.add(widget);
                }
            }
        }else{
            if (startFrom instanceof QAbstractItemView){
                result.addAll(findElements((QAbstractItemView)startFrom, greedy, manager));
                if (!greedy && !result.isEmpty()){
                    return result;
                }
            }else if (startFrom instanceof QTabBar){
                result.addAll(findElements((QTabBar)result, greedy, manager));
                if (!greedy && !result.isEmpty()){
                    return result;
                }
            }
            final List<QObject> children = startFrom.children();
            for (QObject child: children){
                if (child instanceof QWidget && child.nativeId()!=0){
                    searchQueue.add((QWidget)child);
                }
            }
        }                        
        List<QObject> children;
        QWidget currentWidget;
        while(!searchQueue.isEmpty()){
            currentWidget = searchQueue.poll();
            if (isTarget(currentWidget)){
                result.add(manager.getWidgetReference(currentWidget));
                if (!greedy){
                    break;
                }
            }
            final List<QAction> actions = currentWidget.actions();
            if (actions!=null && !actions.isEmpty()){
                result.addAll(findElements(currentWidget, actions, greedy, manager));
                if (!greedy && !result.isEmpty()){
                    break;
                }                
            }
            if (currentWidget instanceof QAbstractItemView){
                result.addAll(findElements((QAbstractItemView)currentWidget, greedy, manager));
                if (!greedy && !result.isEmpty()){
                    break;
                }
            }else if (currentWidget instanceof QTabBar){
                result.addAll(findElements((QTabBar)currentWidget, greedy, manager));
                if (!greedy && !result.isEmpty()){
                    break;
                }                
            }
            children = currentWidget.children();
            for (QObject child: children){
                if (child instanceof QWidget && child.nativeId()!=0){
                    searchQueue.add((QWidget)child);
                }
            }
        }
        return result;
    }
    
    private Collection<UIElementReference> findElements(final QWidget owner, final List<QAction> actions, final boolean greedy, final WebDrvUIElementsManager manager){         
        final Stack<QAction> stack = new Stack<>();
        stack.addAll(actions);
        final Collection<UIElementReference> result = new LinkedList<>();
        QAction action;
        QMenu menu;
        while (!stack.isEmpty()){
            action = stack.pop();
            if (action!=null && action.nativeId()!=0 && isTarget(action)){
                result.add(manager.getActionReference(owner, action));
                if (!greedy){
                    return result;
                }
            }
            menu = action.menu();
            if (menu!=null){
                stack.addAll(menu.actions());
            }
        }
        return result;
    }
    
    private Collection<UIElementReference> findElements(final QAbstractItemView view, final boolean greedy, final WebDrvUIElementsManager manager){ 
        final Collection<ItemViewIndex> indexes = findIndexes(view.model(), greedy, false);
        if (indexes==null || indexes.isEmpty()){
            return Collections.emptyList();
        }else{
            final Collection<UIElementReference> result = new LinkedList<>();
            for (ItemViewIndex index: indexes){
                result.add(index.createReference(view, manager));
            }
            return result;
        }
    }    
    
    private Collection<UIElementReference> findElements(final QTabBar tabSet, final boolean greedy, final WebDrvUIElementsManager manager){
        final Collection<UIElementReference> result = new LinkedList<>();
        for (int i=0,count=tabSet.count(); i<count; i++){
            if (isTargetTabButton(tabSet, i)){
                result.add(manager.getTabButtonReference(tabSet, i));
                if (!greedy){
                    break;
                }
            }
        }
        return result;
    }
    
    protected Collection<ItemViewIndex> findIndexes(final QAbstractItemModel model, final boolean greedy, final boolean useObjNames){
        final int columnsCount = model instanceof QAbstractListModel ? 1 : model.columnCount();
        final Queue<QModelIndex> searchQueue = new LinkedList<>();
        final Collection<ItemViewIndex> result = new LinkedList<>();
        for (int row=0, count = model.rowCount(); row<count; row++){
            searchQueue.add(model.index(row, 0));
        }
        QModelIndex parent, nextParent, cellIndex, rowIndex;
        while(!searchQueue.isEmpty()){
            parent = searchQueue.poll();
            nextParent = model instanceof QAbstractListModel ? null : parent.parent();
            rowIndex = model.index(parent.row(), 0, nextParent);
            if (isTargetRow(model, rowIndex)){
                result.add(createRowIndex(rowIndex, useObjNames));
                if (!greedy){
                    break;
                }
            }
            for (int col=0; col<columnsCount; col++){                
                cellIndex = model.index(parent.row(), col, nextParent);
                if (isTargetCell(model, cellIndex)){
                    result.add(createCellIndex(cellIndex, useObjNames));
                    if (!greedy){
                        return result;
                    }
                }
            }            
            for (int row=0, count = model.rowCount(parent); row<count; row++){
                rowIndex = model.index(row, 0, parent);
                if (rowIndex!=null){
                    searchQueue.add(rowIndex);
                }
            }
        }
        return result;
    }
    
    protected abstract boolean isTarget(final QAction action);
    
    protected abstract boolean isTarget(final QWidget widget);

    protected abstract boolean isTargetRow(final QAbstractItemModel model, final QModelIndex index);
    
    protected abstract boolean isTargetCell(final QAbstractItemModel model, final QModelIndex index);    
    
    protected abstract boolean isTargetTabButton(final QTabBar tabSet, final int index);
}
