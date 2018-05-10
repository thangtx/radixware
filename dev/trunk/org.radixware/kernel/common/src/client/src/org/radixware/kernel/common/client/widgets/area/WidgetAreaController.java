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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.widgetsarea.WidgetsAreaDocument;

public final class WidgetAreaController {

    private final IWidgetAreaPresenter presenter;
    private final IClientEnvironment env;
    private int width, height;
    private boolean isTabsEnabled = true;
    private int currentTab;
    private final List<IWidgetAreaItemController> controllersList = new ArrayList<>();
    private final List<WidgetAreaTab> widgetAreaTabList = new ArrayList<>();
    private final Map<WidgetAreaTab, IWidgetAreaTabPresenter> widgetAreaItem2PresenterMap = new HashMap<>();

    public WidgetAreaController(IWidgetAreaPresenter presenter, IClientEnvironment env) {
        this.presenter = presenter;
        this.env = env;
    }

    public void setGridSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getGridWidth() {
        return width;
    }

    public int getGridHeight() {
        return height;
    }

    public WidgetAreaTab addTab(String title) {
        IWidgetAreaTabPresenter tabPresenter = presenter.addTab(title);
        WidgetAreaTab createdTab = new WidgetAreaTab(tabPresenter, title);
        createdTab.setCloseListener(new WidgetAreaTab.CloseListener() {

            @Override
            public boolean onTabClosed(WidgetAreaTab tab, boolean forced) {
                return WidgetAreaController.this.removeTab(widgetAreaTabList.indexOf(tab), forced);
            }
        });
        widgetAreaTabList.add(createdTab);
        setCurrentTab(widgetAreaTabList.size() - 1);
        widgetAreaItem2PresenterMap.put(createdTab, tabPresenter);
        presenter.refreshGui();
        return createdTab;
    }

    public WidgetAreaTab getTab(int index) {
        if (!widgetAreaTabList.isEmpty() && widgetAreaTabList.size() > index) {
            return widgetAreaTabList.get(index);
        } else {
            return null;
        }
    }

    public boolean removeTab(int index, boolean forced) {
        WidgetAreaTab tab = widgetAreaTabList.get(index);
        if (tab.internalClose(forced)) {
            widgetAreaItem2PresenterMap.remove(tab);
            widgetAreaTabList.remove(index);
            if (widgetAreaTabList.size() > index) {
                setCurrentTab(index);
            } else {
                setCurrentTab(index - 1);
            }
            presenter.refreshGui();
            return true;
        } else {
            return false;
        }
    }

    public int getTabCount() {
        if (!widgetAreaTabList.isEmpty()) {
            return widgetAreaTabList.size();
        } else {
            return 0;
        }
    }

    public WidgetAreaTab getCurrentTab() {
        if (!widgetAreaTabList.isEmpty()) {
            return widgetAreaTabList.get(currentTab);
        } else {
            return null;
        }
    }

    public void setCurrentTab(int index) {
        if (widgetAreaTabList != null && !widgetAreaTabList.isEmpty() && index < widgetAreaTabList.size() && widgetAreaTabList.get(index) != null) {
            currentTab = index;
            presenter.setCurrentTab(index);
        }
    }

    public boolean isTabsEnabled() {
        return isTabsEnabled;
    }

    public void setTabsEnabled(boolean isTabsEnabled) {
        if (this.isTabsEnabled != isTabsEnabled) {
            this.isTabsEnabled = isTabsEnabled;
            presenter.setTabsEnabled(isTabsEnabled);
        }
    }

    public void refreshTabs() {
        for (WidgetAreaTab widgetAreaTabList1 : widgetAreaTabList) {
            widgetAreaTabList1.refresh();
        }
    }

    public void setControllerClasses(List<Class<? extends IWidgetAreaItemController>> controllerClasses) {
        Iterator<IWidgetAreaItemController> it = controllersList.iterator();
        while (it.hasNext()) {
            IWidgetAreaItemController controller = it.next();
            boolean isExists = false;
            for (WidgetAreaTab tab : widgetAreaTabList) {
                if (!tab.getItemsByControllerClass(controller.getClass()).isEmpty()) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                it.remove();
            }
        }
        for (Class<? extends IWidgetAreaItemController> itemControllerClass : controllerClasses) {
            boolean isExists = false;
            for (IWidgetAreaItemController controller : controllersList) {
                if (controller.getClass().equals(itemControllerClass)) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                try {
                    controllersList.add(itemControllerClass.newInstance());
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(WidgetAreaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<WidgetAreaItem> getItemsByControllerClass(Class<? extends IWidgetAreaItemController> controllerClass) {
        List<WidgetAreaItem> list = new ArrayList<>();
        for (WidgetAreaTab tab : widgetAreaTabList) {
            List<WidgetAreaItem> itemsList = tab.getItemsByControllerClass(controllerClass);
            if (!itemsList.isEmpty()) {
                list.addAll(itemsList);
            }
        }
        return list;
    }

    public List<WidgetAreaItem> getItemsByOrder() {
        List<WidgetAreaItem> itemsList = new ArrayList<>();
        for (WidgetAreaTab widgetAreaTab : widgetAreaTabList) {
            itemsList.addAll(widgetAreaTab.getItemsByOrder());
        }
        return itemsList;
    }

    public List<WidgetAreaItem> getItemsByContollerClassId(Id controllerClassId) {
        List<WidgetAreaItem> list = new ArrayList<>();
        for (WidgetAreaTab tab : widgetAreaTabList) {
            List<WidgetAreaItem> itemsList = tab.getItemsByControllerClassId(controllerClassId);
            if (!itemsList.isEmpty()) {
                list.addAll(itemsList);
            }
        }
        return list;
    }

    public List<IWidgetAreaItemController> getControllersList() {
        return new ArrayList<>(controllersList);
    }

    public WidgetsAreaDocument saveToXml() {
        WidgetsAreaDocument widgetsAreaDocument = WidgetsAreaDocument.Factory.newInstance();
        org.radixware.schemas.widgetsarea.WidgetsArea area = widgetsAreaDocument.addNewWidgetsArea();
        area.setGridHeight(height);
        area.setGridWidth(width);
        for (WidgetAreaTab tab : widgetAreaTabList) {
            org.radixware.schemas.widgetsarea.Tab tabXML = area.addNewTab();
            tabXML.setName(tab.getTitle());
            for (WidgetAreaItem item : tab.getItemsByOrder()) {
                org.radixware.schemas.widgetsarea.Widget widget = tabXML.addNewWidget();
                widget.setGridHeight(item.getHeight());
                widget.setGridWidth(item.getWidth());
                widget.setGridLeft(item.getLeft());
                widget.setGridTop(item.getTop());
                widget.setControllerClassName(tab.getControllerByItem(item) == null ? null : tab.getControllerByItem(item).getClass().getName());
                XmlObject content = item.store();
                if (content != null) {
                    widget.addNewContext().set(content);
                }
            }
        }
        area.setActiveTabIndex(currentTab);
        return widgetsAreaDocument;
    }

    public boolean close(boolean forced) {
        if (!forced) {
            boolean firstTime = true;
            EDialogButtonType result = EDialogButtonType.OK;
            for (WidgetAreaTab tab : widgetAreaTabList) {
                for (WidgetAreaItem item : tab.getItemsByOrder()) {
                    if (item.isModified()) {
                        if (firstTime) {
                            firstTime = false;
                            Set<EDialogButtonType> btnSet = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);
                            result = item.getEnvironment().messageBox(env.getMessageProvider().translate("WidgetsArea", "Close widgets area"),
                                    env.getMessageProvider().translate("WidgetsArea", "Do you want to save results?"), EDialogIconType.QUESTION, btnSet);
                        }
                        if (result == EDialogButtonType.OK) {
                            if (!item.applyChanges()) {
                                setCurrentTab(widgetAreaTabList.indexOf(tab));
                                return false;
                            }
                        } else if (result == EDialogButtonType.CANCEL) {
                            return false;
                        }
                    }
                }
            }
            if (firstTime == true) {
                Set<EDialogButtonType> btnSet = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);
                result = env.messageBox(env.getMessageProvider().translate("WidgetsArea", "Close widgets area"),
                        env.getMessageProvider().translate("WidgetsArea", "Do you want to close all tabs?"), EDialogIconType.QUESTION, btnSet);
                if (result == EDialogButtonType.NO) {
                    return false;
                }
            }
            for (WidgetAreaTab tab : widgetAreaTabList) {
                for (WidgetAreaItem item : tab.getItemsByOrder()) {
                    if (!item.beforeClose()) {
                        return false;
                    }
                }
            }
        }
        for (int i = widgetAreaTabList.size() - 1; i >= 0; i--) {
            if (!removeTab(i, true)) {
                return false;
            }
        }

        return true;
    }

    public boolean applyChanges() {
        for (WidgetAreaTab tab : widgetAreaTabList) {
            for (WidgetAreaItem item : tab.getItemsByOrder()) {
                if (item.isModified()) {
                    if (!item.applyChanges()) {
                        setCurrentTab(widgetAreaTabList.indexOf(tab));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<WidgetAreaItem> getModifiedItems() {
        List<WidgetAreaItem> modifiedItemsList = new ArrayList<>();
        for (WidgetAreaTab tab : widgetAreaTabList) {
            for (WidgetAreaItem item : tab.getItemsByOrder()) {
                if (item.isModified()) {
                    modifiedItemsList.add(item);
                }
            }
        }
        return modifiedItemsList;
    }

    public void cancelChanges() {
        for (WidgetAreaTab tab : widgetAreaTabList) {
            for (WidgetAreaItem item : tab.getItemsByOrder()) {
                if (item.isModified()) {
                    item.cancelChanges();
                }
            }
        }
    }

    public void loadFromXml(WidgetsAreaDocument widgetsAreaDocument) {
        org.radixware.schemas.widgetsarea.WidgetsArea widgetsArea = widgetsAreaDocument.getWidgetsArea();
        setGridSize(widgetsArea.getGridWidth(), widgetsArea.getGridHeight());
        for (org.radixware.schemas.widgetsarea.Tab tabXml : widgetsArea.getTabList()) {
            WidgetAreaTab tab = addTab(tabXml.getName());
            for (org.radixware.schemas.widgetsarea.Widget widget : tabXml.getWidgetList()) {
                IWidgetAreaItemController controllerItem = null;
                for (IWidgetAreaItemController controller : controllersList) {
                    if (controller.getClass().getName().equals(widget.getControllerClassName())) {
                        controllerItem = controller;
                    }
                }
                final XmlObject content = widget.getContext() == null ? null : XmlObjectProcessor.getXmlObjectFirstChild(widget.getContext());
                WidgetAreaItem item = tab.addItem(controllerItem, WidgetAreaItem.ECreationMode.LOAD_FROM_XML, content);
                item.setBounds(widget.getGridLeft(), widget.getGridTop(), widget.getGridWidth(), widget.getGridHeight());
            }
        }
        setCurrentTab(widgetsArea.getActiveTabIndex());
    }

    public void setTabClosable(IWidgetAreaTabPresenter tabPresenter, boolean isClosable) {
        WidgetAreaTab tab = null;
        for (Map.Entry<WidgetAreaTab, IWidgetAreaTabPresenter> entry : widgetAreaItem2PresenterMap.entrySet()) {
            if (entry.getValue().equals(tabPresenter)) {
                tab = entry.getKey();
                break;
            }
        }
        if (tab != null) {
            tab.setClosable(isClosable);
        }
    }

    public void renameTab(int tabIndex) {
        if (tabIndex < widgetAreaTabList.size()) {
            WidgetAreaTab tab = widgetAreaTabList.get(tabIndex);
            String newTabTitle = presenter.getTabTitle();
            if (newTabTitle != null && !newTabTitle.isEmpty()) {
                tab.setTilte(newTabTitle);
            }
        }
    }
}
