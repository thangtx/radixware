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

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionButton;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.webdriver.WebDrvServer;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.widgets.propeditors.PropBoolEditor;

public abstract class UIElementReference {
    
    protected final static QRect TMP_RECT = new QRect();
    protected final static QPoint TMP_POINT = new QPoint();
    private final static QPoint ZERO_POINT = new QPoint(0, 0);
    
    private final UIElementId elementId;
    
    protected UIElementReference(final UIElementId elementId){
        this.elementId = elementId;
    }
    
    public UIElementId getElementId(){
        return elementId;                
    }
    
    @SuppressWarnings("unchecked")
    public void writeToJsonList(final JSONArray list){        
        list.add(asJson());
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject asJson(){
        final JSONObject json = new JSONObject();
        json.put(UIElementId.JSON_KEY, elementId.toString());
        return json;
    }
    
    protected static QHeaderView getHorizontalHeader(final QAbstractItemView view){
        if (view instanceof QTableView){            
            return ((QTableView) view).horizontalHeader();
        }else if (view instanceof QTreeView){
            return ((QTreeView)view).header();
        }else{
            return null;
        }
    }

    protected static QRect getWidgetRect(final QWidget widget){
        final QRect rect = widget.frameGeometry();
        if (widget.isWindow()){
            return rect;
        }else{
            final QPoint pos = widget.mapToGlobal(ZERO_POINT);
            final QSize size = rect.size();
            TMP_RECT.setTopLeft(pos);
            TMP_RECT.setSize(size);
            return TMP_RECT;
        }
    }
    
    protected static QWidget findActiveWindow(){
        QWidget activeWindow = QApplication.activePopupWidget();        
        if (activeWindow==null){
            activeWindow = QApplication.activeModalWidget();
        }
        if (activeWindow==null){
            activeWindow=QApplication.activeWindow();
        }
        return activeWindow==null ? null : activeWindow.window();
    }
    
    protected static void ensureInViewArea(final QWidget widget) throws WebDrvException {
		if (!widget.isVisible()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE, "Element is not visible");
        }
        //check if widget window is an active window    
        final QWidget activeWindow = findActiveWindow();        
        final long activeWindowId = activeWindow==null ? 0 : activeWindow.nativeId();
        if (activeWindowId!=0 && widget.window().nativeId()!=activeWindowId){            
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE, "Element in background window");
        }
        //auto scroll to widget
        final List<QWidget> parents = new LinkedList<>();
        for (QWidget parent=widget; parent!=null && !parent.isWindow(); parent=parent.parentWidget()){
            parents.add(parent);
        }        
        for (int i=parents.size()-1; i>=0; i--){
            if (parents.get(i) instanceof QScrollArea && i>2){
                //i-1 = scrollarea viewport
                //i-2 = scrollarea.widget()
                //i-3 = target vidget to scroll
                ((QScrollArea)parents.get(i)).ensureWidgetVisible(parents.get(i-3));                
            }
        }        
    }
    
    protected static void ensureInteractable(final QWidget widget) throws WebDrvException{
        if (!widget.isEnabled()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE, "Element is not enabled");
        }     
        ensureInViewArea(widget);
        final QRect visibleRect = widget.visibleRegion().boundingRect().intersected(widget.rect());
        if (visibleRect.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
        }
    }
    
    /**
     * 
     */
    protected static QPoint getClickPoint(final QWidget widget) throws WebDrvException{
        final QWidget targetWidget;
        if (widget instanceof ValBoolEditor || widget instanceof AdvancedValBoolEditor || widget instanceof PropBoolEditor){
            targetWidget = (QWidget)widget.findChild(QCheckBox.class);
        }else{
            targetWidget = widget;
        }
        final QStyle.SubElement subElement;
        if (targetWidget instanceof QCheckBox){
            subElement = QStyle.SubElement.SE_CheckBoxClickRect;
        }else if (targetWidget instanceof QRadioButton){
            subElement = QStyle.SubElement.SE_RadioButtonClickRect;
        }else{
            subElement = null;
        }
        final QRect clickRect;
        if (subElement==null){
            clickRect = targetWidget.rect();
        }else{
            final QStyleOptionButton styleOption = new QStyleOptionButton();
            styleOption.initFrom(targetWidget);
            clickRect = targetWidget.style().subElementRect(subElement, styleOption, targetWidget);
        }
        final QRect visibleClickRect = targetWidget.visibleRegion().boundingRect().intersected(clickRect);
        if (visibleClickRect.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
        }else{
            return visibleClickRect.center();
        }
    }

    /**
     * Возвращает точку для клика по элементу index в координатах view.
     */
    protected final QPoint getClickPoint(final QAbstractItemView view, final QModelIndex index, final WebDrvUIElementsManager manager) throws WebDrvException{
        ensureInViewArea(view);
        view.scrollTo(index, QAbstractItemView.ScrollHint.EnsureVisible);
        final QRect elementGlobalRect = getElementRect();
        if (!elementGlobalRect.isValid() || elementGlobalRect.isNull() || elementGlobalRect.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
        }
        
        final QSize size = elementGlobalRect.size();
        TMP_RECT.setTopLeft(view.viewport().mapFromGlobal(elementGlobalRect.topLeft()));
        TMP_RECT.setSize(size);
        final QRect visibleClickRect = view.viewport().visibleRegion().boundingRect().intersected(TMP_RECT);
        if (visibleClickRect.isEmpty()) {
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
        } else {
            // здесь visibleClickRect в координатах view.viewport(), а не view.
            // отображаю точку в координаты view
            QPoint p = visibleClickRect.center();
            return view.mapFromGlobal(view.viewport().mapToGlobal(p));
        }
    }
	
    /**
     * Кликнуть на widget в точке point. point в координатах виджета.
     */
    protected static void clickAtPoint(final QWidget widget, final QPoint point) throws WebDrvException{
        postClick(widget, point);
    }

    private static void postClick(QWidget w, QPoint p) throws WebDrvException{
        try {
            // это работает, только если наше окно наверху.
            QPoint pGlobal = w.mapToGlobal(p);
            WebDrvServer.postGlobalClick(pGlobal.x(), pGlobal.y());
        }
        catch(Exception ex) {
            throw new WebDrvException(ex.getMessage(), ex);
        }
    }
    
    protected final QPoint getClickPoint(final QTabBar tabBar, final int index, final WebDrvUIElementsManager manager) throws WebDrvException{               
        ensureInViewArea(tabBar);
        final QRect tabRect = scrollToTab(tabBar, index);
        if (tabRect.width()<=0 || tabRect.height()<=0){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
        }
        final QRect visibleClickRect = tabBar.visibleRegion().boundingRect().intersected(tabRect);
        if (visibleClickRect.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_CLICK_INTERCEPTED);
        }else{
            return visibleClickRect.center();
        }
    }    
    
    private static QRect scrollToTab(final QTabBar tabBar, final int tabIndex){
        final QTabBar.Shape shape = tabBar.shape();
        final boolean isVertical = shape == QTabBar.Shape.RoundedWest
                                           || shape == QTabBar.Shape.RoundedEast
                                           || shape == QTabBar.Shape.TriangularWest
                                           || shape == QTabBar.Shape.TriangularEast;
        int tabButtonsSpace = isVertical ? tabBar.height() : tabBar.width();
        final List<QObject> childButtons = tabBar.findChildren(QToolButton.class);
        QToolButton toolButton;
        for (QObject childBtn: childButtons){
            toolButton = (QToolButton)childBtn;
            if (toolButton.isVisible()){
                tabButtonsSpace -= isVertical ? toolButton.height() : toolButton.width();
            }
        }
        int tabMax = getTabMax(tabBar, tabIndex, isVertical);
        if (tabMax<0){
            if (childButtons.size()<2){
                TMP_RECT.setRect(0, 0, 0, 0);
                return TMP_RECT;
            }
            final QToolButton moveButton = (QToolButton)childButtons.get(0);
            int prevTabMax;
            do{
                prevTabMax = tabMax;
                moveButton.click();
                tabMax = getTabMax(tabBar, tabIndex, isVertical);
            }while(tabMax>prevTabMax && tabMax<0);
            if (tabMax<0){
                TMP_RECT.setRect(0, 0, 0, 0);
                return TMP_RECT;                
            }
            final QRect tabRect = tabBar.tabRect(tabIndex);            
            if (isVertical){
                tabRect.setY(0);
            }else{
                tabRect.setX(0);
            }
            return tabRect;
        }else{
            int tabMin = getTabMin(tabBar, tabIndex, isVertical);
            if (tabMin>tabButtonsSpace){
                if (childButtons.size()<2){
                    TMP_RECT.setRect(0, 0, 0, 0);
                    return TMP_RECT;
                }
                final QToolButton moveButton = (QToolButton)childButtons.get(1);
                int prevTabMin;
                do{
                    prevTabMin = tabMin;
                    moveButton.click();
                    tabMin = getTabMin(tabBar, tabIndex, isVertical);
                }while(tabMin<prevTabMin && tabMin>tabButtonsSpace);
                if (tabMin>tabButtonsSpace){
                    TMP_RECT.setRect(0, 0, 0, 0);
                    return TMP_RECT;                
                }
                final QRect tabRect = tabBar.tabRect(tabIndex);
                final QPoint bottomRight = tabRect.bottomRight();                
                if (isVertical){
                    bottomRight.setY(tabButtonsSpace);
                }else{
                    bottomRight.setX(tabButtonsSpace);
                }
                tabRect.setBottomRight(bottomRight);
                return tabRect;
            }
        }
        return tabBar.tabRect(tabIndex);
    }
    
    private static int getTabMax(final QTabBar tabBar, final int tabIndex, final boolean isVertical){
        final QRect tabRect = tabBar.tabRect(tabIndex);
        return isVertical ? tabRect.y() + tabRect.height() : tabRect.x() + tabRect.width();
    }
    
    private static int getTabMin(final QTabBar tabBar, final int tabIndex, final boolean isVertical){
        final QRect tabRect = tabBar.tabRect(tabIndex);
        return isVertical ? tabRect.y() : tabRect.x();
    }
    
    
    protected abstract UIElementPropertySet getProperties() throws WebDrvException;
    
    public abstract boolean isSelected() throws WebDrvException;
        
    public final String getPropertyValue(final String propertyName) throws WebDrvException {
        final UIElementPropertySet properties = getProperties();
        if (properties.containsProperty(propertyName)){
            try{
                return properties.getValue(propertyName);
            }catch(IllegalAccessException exception){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown property \'"+propertyName+"\'");
            }catch(IllegalArgumentException | InvocationTargetException exception){
                throw new WebDrvException("Failed to get value of \'"+propertyName+"\' property", exception);
            }
        }
        throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown property \'"+propertyName+"\'");
    }
    
    public abstract String getDisplayedText() throws WebDrvException;
    
    public abstract String getSimpleClassName() throws WebDrvException;
    
    /**
     * Возвращает положение элемента на экране. Глобальные координаты.
     */
    public abstract QRect getElementRect() throws WebDrvException;
    
    public abstract boolean isEnabled() throws WebDrvException;       
    
    public abstract void clickAtCenterPoint(WebDrvUIElementsManager manager) throws WebDrvException;
    
    public abstract void clearText() throws WebDrvException;
    
    public abstract void sendKeys(final String keyEventsAsStr) throws WebDrvException;

    public abstract void scrollToView() throws WebDrvException;

    public abstract boolean isDisplayed() throws WebDrvException;

    public abstract QPixmap getPixmap() throws WebDrvException;
    
    public abstract boolean isInteractiveNow() throws WebDrvException;
}