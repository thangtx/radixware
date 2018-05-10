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

import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QWidget;
import java.util.Objects;

final class TabButtonDescriptor  implements UIElementDescriptor{        

    private final long tabBarNativeId;
    private final int index;
    private final String objectName;

    public TabButtonDescriptor(final QTabBar tabBar, final int index) {
        tabBarNativeId = tabBar.nativeId();
        this.index = index;
        objectName = null;                    
    }
    
    public TabButtonDescriptor(final QTabBar tabBar, final String objectName) {
        tabBarNativeId = tabBar.nativeId();
        this.objectName = objectName;
        index = -1;
    }            
    
    public int getTabIndex(final QTabBar tabBar){
        if (index<0 
            && objectName!=null 
            && !objectName.isEmpty()
            && tabBar.parentWidget() instanceof QTabWidget){
            final String tabName = 
                objectName.substring(WebDrvUIElementsManager.TAB_BUTTON_OBJECT_NAME_PREFIX.length());
            final QTabWidget tabWidget = (QTabWidget)tabBar.parentWidget();
            for (int i=tabWidget.count()-1; i>=0; i--){
                if (tabName.equals(tabWidget.widget(i).objectName())){
                    return i;
                }
            }
        }
        return index;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.tabBarNativeId ^ (this.tabBarNativeId >>> 32));
        hash = 97 * hash + this.index;
        hash = 97 * hash + Objects.hashCode(this.objectName);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TabButtonDescriptor other = (TabButtonDescriptor) obj;
        if (this.tabBarNativeId != other.tabBarNativeId) {
            return false;
        }
        if (this.index != other.index) {
            return false;
        }
        if (!Objects.equals(this.objectName, other.objectName)) {
            return false;
        }
        return true;
    }
}
