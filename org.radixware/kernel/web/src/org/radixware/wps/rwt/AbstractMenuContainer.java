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

package org.radixware.wps.rwt;

import java.util.List;

class AbstractMenuContainer extends Container{
    
    private RootPanel root;
    
    public AbstractMenuContainer(RwtMenu menu, RootPanel root) {
        super();
        this.root = root;
        init(menu);
    }
    
    private void init(RwtMenu menu) {
        this.getHtml().setAttr("menuId", menu.getHtmlId());
        menu.getHtml().setAttr("firstLevelMenu", true);
        menu.getHtml().setAttr("activeMenu", true);
        menu.getHtml().renew();  
        super.add(menu);
        menu.actualize();
        for (RwtMenu subMenu : menu.getMenuList()) {
            add(subMenu);
        }
        this.html.addClass("rwt-menu-container");
        root.add(this);
        this.html.layout("$RWT.menuContainer.layout");
    }
    
    public void destroy() {
        root.remove(this);
        this.clear();
    }
   
    public void add(RwtMenu menu) {
        List<RwtMenu> menuList = menu.getMenuList();        
        menu.getHtml().addClass("rwt-menu");
        menu.getHtml().renew(); 
       
        super.add(menu);
        menu.actualize(); 
        if (!menuList.isEmpty()) {
            for (RwtMenu subMenu : menuList) {
                add(subMenu);
            }
        }
    }
}
