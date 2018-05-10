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

package org.radixware.kernel.common.client.widgets.area;

import java.util.List;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.widgetsarea.WidgetsAreaDocument;

public interface IWidgetArea extends IWidget{        
    
    public void setGridSize(int width, int height);
    
    public int getGridWidth();
    
    public int getGridHeight();
    
    public WidgetAreaTab addTab(String title);
    
    public int getTabCount();
    
    public WidgetAreaTab getTab(int index);
    
    public void removeTab(int index);
    
    public WidgetAreaTab getCurrentTab();
    
    public void setCurrentTab(int index);
    
    public boolean isTabsEnabled(); //Если метод возвращает true, то пользователь может создавать новые вкладки, иначе вкладки можно создавать только программно

    public void setTabsEnabled(boolean isEnabled);
    
    public boolean close(boolean forced);
    
    public void setControllerClasses(List<Class<? extends IWidgetAreaItemController>> controllerClasses);//Метод должен создать инстанции контроллеров и сохранить их
    
    public void setControllerClassIds(Id... controllerClassIds);//Метод должен создать инстанции контроллеров и сохранить их
    
    public WidgetsAreaDocument saveToXml();
    
    public void loadFromXml(WidgetsAreaDocument xml);
    
    public List<WidgetAreaItem> getItemsByOrder();//Возвращает список всех инстанций WidgetAreaItem со всех вкладок в порядке следования вкладок.
    
    public List<WidgetAreaItem> getItemsByController(Class<? extends IWidgetAreaItemController> controllerClass);//Возвращает список всех инстанций WidgetAreaItem, ассоциированных с указанным контроллером, со всех вкладок в порядке следования вкладок.
    
    public List<WidgetAreaItem> getItemsByController(Id controllerClassId);//Возвращает список всех инстанций WidgetAreaItem, ассоциированных с указанным контроллером, со всех вкладок в порядке следования вкладок.
    
    public IToolBar getToolBar();
    
    public boolean applyChanges();
    
    public void cancelChanges();
    
    public List<WidgetAreaItem> getModifiedItems();
}
