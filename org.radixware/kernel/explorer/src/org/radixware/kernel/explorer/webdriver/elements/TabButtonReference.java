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
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QTabBar;
import java.lang.ref.WeakReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class TabButtonReference extends UIElementReference {

    private final WeakReference<QTabBar> tabBarRef;
    private final TabButtonDescriptor tabDescriptor;

    public TabButtonReference(final QTabBar tabBar, final TabButtonDescriptor descriptor) {
        super(UIElementId.newInstance());
        tabBarRef = new WeakReference<>(tabBar);
        this.tabDescriptor = descriptor;
    }

    private QTabBar getTabBar() throws WebDrvException{
        final QTabBar tabBar = tabBarRef.get();
        if (tabBar==null || tabBar.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return tabBar;
    } 
    
    private int getTabIndex() throws WebDrvException{
        final int index = tabDescriptor.getTabIndex(getTabBar());
        if (index<0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }
        return index;
    }    
    
    @Override
    public boolean isSelected() throws WebDrvException {
        return getTabBar().currentIndex()==getTabIndex();
    }

    @Override
    protected UIElementPropertySet getProperties() throws WebDrvException {
        return new UIElementPropertySet(new TabButtonInfo(getTabBar(), getTabIndex()));
    }        

    @Override
    public String getDisplayedText() throws WebDrvException {
        final QTabBar tabBar = getTabBar();
        if (!tabBar.isVisible()){
            return "";
        }
        return tabBar.tabText(getTabIndex());
    }

    @Override
    public String getSimpleClassName() throws WebDrvException {
        return "TabButton";
    }

    @Override
    public QRect getElementRect() throws WebDrvException {
        final QTabBar tabBar = getTabBar();
        final QRect rect = tabBar.tabRect(getTabIndex());
        final QSize size = rect.size();
        TMP_RECT.setTopLeft(tabBar.mapToGlobal(rect.topLeft()));
        TMP_RECT.setSize(size);                
        return TMP_RECT;
    }        

    @Override
    public boolean isEnabled() throws WebDrvException {
        final QTabBar tabBar = getTabBar();
        return tabBar.isTabEnabled(getTabIndex());
    }
    
    @Override
    public void clickAtCenterPoint(final WebDrvUIElementsManager manager) throws WebDrvException {
        final QTabBar tabBar = getTabBar();
        clickAtPoint(tabBar, getClickPoint(tabBar, getTabIndex(), manager));
    }

    @Override
    public void clearText() throws WebDrvException {
        throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE);
    }
    
    @Override
    public void sendKeys(final String keyEventsAsStr) throws WebDrvException {
        final QTabBar tabBar = getTabBar();
        ensureInteractable(tabBar);
        final WebDrvKeyEvents events = WebDrvKeyEvents.parse(keyEventsAsStr);
        events.post(tabBar);
    } 

    @Override
    public void scrollToView() throws WebDrvException {
        ensureInViewArea(getTabBar());
    }

    @Override
    public boolean isDisplayed() throws WebDrvException {
        return getTabBar().isVisible();
    }
    
    @Override
    public QPixmap getPixmap() throws WebDrvException{
        final QTabBar tabBar = getTabBar();
        final QRect rect = tabBar.tabRect(getTabIndex());
        return QPixmap.grabWidget(tabBar, rect);
    }

    @Override
    public boolean isInteractiveNow() throws WebDrvException {
        final QTabBar tabBar = getTabBar();
        return tabBar.window() == findActiveWindow() && tabBar.isEnabled() && tabBar.isVisible();
    }   
}
