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

import org.radixware.kernel.explorer.webdriver.elements.finders.UIElementFinder;
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.webdriver.WebDrvCapabilities;
import org.radixware.kernel.explorer.webdriver.elements.finders.EElementLocationStrategy;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public final class WebDrvUIElementsManager {
    
    public static final String TAB_BUTTON_OBJECT_NAME_PREFIX="rx_tab_button_";
    
    private final Map<UIElementId, UIElementReference> elementById = new HashMap<>(2048);
    private final Map<UIElementDescriptor, UIElementReference> elementByDescriptor = new HashMap<>(2048);
    
    private final WebDrvCapabilities capabilities;
    private final IClientEnvironment environment;
    private QEventLoop localEventLoop;
    private QTimer timer;
    
    public WebDrvUIElementsManager(final WebDrvCapabilities capabilities, IClientEnvironment environment){
        this.capabilities = capabilities;    
        this.environment = environment;
    }    
    
    public UIElementReference getWidgetReference(final QWidget widget){
        if (widget==null || widget.nativeId()==0){
            return null;
        }
        final WidgetDescriptor descriptor = new WidgetDescriptor(widget);
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new QWidgetReference(widget);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }
    
    public UIElementReference getCellReference(final QAbstractItemView view, final QModelIndex index, final boolean useObjNames){
        if (view==null || index==null || view.nativeId()==0){
            return null;
        }
        final ItemViewCellDescriptor descriptor = new ItemViewCellDescriptor(view, index,useObjNames);        
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new ItemViewCellReference(view, descriptor);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }
    
    public UIElementReference getTabButtonReference(final QTabBar tabBar, final int index){
        if (tabBar==null || index<0 || tabBar.nativeId()==0){
            return null;
        }
        final String tabName = getTabName(tabBar, index);        
        final TabButtonDescriptor descriptor;
        if (tabName==null){
            descriptor = new TabButtonDescriptor(tabBar, index);
        }else{
            descriptor = new TabButtonDescriptor(tabBar, tabName);
        }
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new TabButtonReference(tabBar, descriptor);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }
    
    public UIElementReference getTabButtonReference(final QTabBar tabBar, final String objectName){
        if (tabBar==null || objectName==null || objectName.isEmpty() || tabBar.nativeId()==0){
            return null;
        }
        final TabButtonDescriptor descriptor = new TabButtonDescriptor(tabBar, objectName);
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new TabButtonReference(tabBar, descriptor);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }    
    
    public static String getTabName(final QTabBar tabBar, final int index){
        if (tabBar.parentWidget() instanceof QTabWidget){
            final QTabWidget tabWidget = (QTabWidget)tabBar.parentWidget();
            final QWidget tab = index>=0 && index<tabWidget.count() ? tabWidget.widget(index) : null;
            final String tabName = tab==null ? null : tab.objectName();
            return tabName==null || tabName.isEmpty() ? null : TAB_BUTTON_OBJECT_NAME_PREFIX+tabName;
        }else{
            return null;
        }
    }
    
    public UIElementReference getRowReference(final QAbstractItemView view, final QModelIndex index, final boolean useObjNames){
        if (view==null || index==null || view.nativeId()==0){
            return null;
        }        
        final ItemViewRowDescriptor descriptor = new ItemViewRowDescriptor(view, index,useObjNames);
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new ItemViewRowReference(view, descriptor);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }    
    
    public UIElementReference getActionReference(final QWidget widget, final QAction action){
        if (widget==null || action==null || widget.nativeId()==0 || action.nativeId()==0){
            return null;
        }
        final ActionDescriptor descriptor = new ActionDescriptor(widget, action);
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new QActionReference(widget, action);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }
    
    public UIElementReference getSectionReference(final QHeaderView view, final int section){
        if (view==null || view.nativeId()==0 || section<0){
            return null;
        }
        final HeaderViewSectionDescriptor descriptor = new HeaderViewSectionDescriptor(view, section);
        UIElementReference reference = elementByDescriptor.get(descriptor);
        if (reference==null){
            reference = new HeaderViewSectionReference(view, descriptor);
            elementById.put(reference.getElementId(), reference);
            elementByDescriptor.put(descriptor, reference);
        }
        return reference;
    }
    
    public Collection<UIElementReference> findElements(final EElementLocationStrategy locationStrategy,
                                                       final String selector,
                                                       final UIElementId startFrom,
                                                       final boolean greedy) throws WebDrvException{
        final QWidget startFromWidget;
        if (startFrom==null){
            startFromWidget = null;
        }else{
            final UIElementReference ref = getUIElementReference(startFrom);
            if (ref instanceof QWidgetReference){
                startFromWidget = ((QWidgetReference)ref).getWidget();
            }else{
                throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT);
            }
        }
        final UIElementFinder finder = UIElementFinder.Factory.newInstance(locationStrategy, selector);        
        final Collection<UIElementReference> result = finder.findElements(startFromWidget, greedy, this);
        return result;
    }
    
    public UIElementReference getUIElementReference(final UIElementId elementId) throws WebDrvException{
        final UIElementReference ref = elementById.get(elementId);
        if (ref==null){
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT,"element with id \'"+elementId.toString()+"\' does not exist");
        }else{
            return ref;
        }
    }
    
    public UIElementReference getActiveElement(){
        return getWidgetReference(QApplication.focusWidget());
    }    
    
    public void runAsync(final Runnable task){
        environment.runInGuiThreadAsync(task);
    }
        
    public void pause(final int mills){
        if (localEventLoop==null){
            localEventLoop = new QEventLoop();            
            timer = new QTimer(localEventLoop);
            timer.timeout.connect(localEventLoop,"quit()");
        }
        timer.setSingleShot(true);
        timer.setInterval(mills);
        timer.start();
        localEventLoop.exec();        
    }
        
}
