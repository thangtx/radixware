/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.webdriver.elements;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QTabBar;


public final class TabButtonInfo {

    private final QTabBar tabBar;
    private final int index;
    
    public TabButtonInfo(final QTabBar tabBar, final int index){
        this.tabBar = tabBar;
        this.index = index;
    }
    
    public String getObjectName(){
        return WebDrvUIElementsManager.getTabName(tabBar, index);
    }
    
    public int getIndex(){
        return index;
    }
    
    public String getText(){
        return tabBar.tabText(index);
    }
    
    public String getToolTip(){
        return tabBar.tabToolTip(index);
    }
  
    public String getWhatsThis(){
        return tabBar.tabWhatsThis(index);
    }
    
    public QRect getRect(){
        return tabBar.tabRect(index);
    }
    
    public boolean isEnabled(){
        return tabBar.isTabEnabled(index);
    }
    
    public UIElementReference getTabButtonReference(final WebDrvUIElementsManager manager){
        return manager.getTabButtonReference(tabBar, index);
    }
}
