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
package org.radixware.wps.views.widgetsarea;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.area.IWidgetArea;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemController;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemPresenter;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaPresenter;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaTabPresenter;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaController;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaItem;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaTab;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.dialogs.InputValueDialog;
import org.radixware.wps.dialogs.RwtListDialog;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.RwtMenu;
import org.radixware.wps.rwt.TabLayout;
import org.radixware.wps.rwt.TabLayout.Tab;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.views.RwtAction;

public class WebWidgetsArea extends Container implements IWidgetArea {

    private final static class AdminPanelIcons extends ClientIcon {

        private AdminPanelIcons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon ADD_TAB = new AdminPanelIcons("classpath:images/addEmpty.svg");
    }

    private class TabLayoutExt extends TabLayout {

        private Tab createNewTab = null;

        @Override
        public void setCurrentTab(Tab tab) {
            if (tab != null && !tab.equals(createNewTab)) {
                super.setCurrentTab(tab);
            }
        }

        void setCreateNewTab(Tab createNewTab) {
            this.createNewTab = createNewTab;
        }
    }

    private class WidgetAreaPresenter implements IWidgetAreaPresenter {

        private final TabLayoutExt tabWidget;
        private final VerticalBoxContainer layout = new VerticalBoxContainer();
        private final ToolBar toolBar = new ToolBar();
        private ToolButton createNewTabButton;
        private final ToolButton createNewPageBtn;
        private Tab createNewTab;

        public WidgetAreaPresenter() {
            tabWidget = new TabLayoutExt(); 
            MessageProvider mp = getEnvironment().getMessageProvider();
            createNewPageBtn = createToolButton(true, mp.translate("WidgetsArea", "Add Page"), 
                    getEnvironment().getApplication().getImageManager().getIcon(AdminPanelIcons.ADD_TAB));
            createNewPageBtn.addClickHandler(new IButton.ClickHandler() {

                @Override
                public void onClick(IButton source) {
                    WidgetAreaPresenter.this.onCreateNewTabButtonClick();
                }
            });
            Div spacer = new Div();
            spacer.setCss("width", "100%");
            toolBar.getHtml().add(spacer);
            toolBar.add(createNewPageBtn);
            ToolButton createNewItemBtn = createToolButton(true, mp.translate("WidgetsArea", "Add widget"), 
                    getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CREATE));
            createNewItemBtn.addClickHandler(new IButton.ClickHandler() {

                @Override
                public void onClick(IButton source) {
                    WidgetAreaPresenter.this.addNewItemButtonClicked();
                }
            });
            toolBar.add(createNewItemBtn);
            layout.add(toolBar);
            createNewTabButton = createToolButton(true, mp.translate("WidgetsArea", "Add Page"), 
                    getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CREATE));
            createNewTabButton.setIconHeight(16);
            createNewTabButton.setIconWidth(16);
            createNewTabButton.setHeight(17);
            createNewTabButton.setWidth(17);
            createNewTabButton.getHtml().removeClass("rwt-tool-button");
            createNewTabButton.getHtml().setCss("outline", "none");
            createNewTabButton.addClickHandler(new IButton.ClickHandler() {

                @Override
                public void onClick(IButton source) {
                    onCreateNewTabButtonClick();
                }
            });
            createNewTab = tabWidget.addTab("");
            tabWidget.setCreateNewTab(createNewTab);
            createNewTab.setRightTabButton(createNewTabButton);
            tabWidget.addTabListener(new TabLayout.TabListener() {

                @Override
                public void onCurrentTabChange(Tab oldTab, Tab newTab) {
                    WebWidgetsArea.this.setCurrentTab(tabWidget.getTabIndex(newTab));
                }
            });

            tabWidget.setCloseListener(new TabLayout.CloseListener() {

                @Override
                public void onClose(int tabIndex) {
                    widgetAreaController.removeTab(tabIndex, false);
                }
            });
            layout.add(tabWidget);
            layout.setAutoSize(tabWidget, true);
            layout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            add(layout);
        }

        @Override
        public IWidgetAreaTabPresenter addTab(String title) {
            TabWidgetContent tabContent = new TabWidgetContent();
            int cellsHCount = widgetAreaController.getGridWidth();
            int cellsVCount = widgetAreaController.getGridHeight();
            tabContent.getHtml().setAttr("cellsHCount", cellsHCount);
            tabContent.getHtml().setAttr("cellsVCount", cellsVCount);
            TabPresenter tabPresenter = new TabPresenter(tabContent, getEnvironment(), tabWidget);
            final Tab newTab = tabWidget.addTab(tabWidget.getTabCount() - 1, title);
            RwtMenu menu = new RwtMenu();
            RwtAction renameAction = new RwtAction(getEnvironment());
            renameAction.setText(getEnvironment().getMessageProvider().translate("WidgetsArea", "Rename"));
            renameAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    widgetAreaController.renameTab(tabWidget.getTabIndex(newTab));
                }
            });
            menu.addAction(renameAction);
            newTab.setTabHandleContextMenu(menu);
            newTab.setIcon(getEnvironment().getApplication().getImageManager().getIcon(AdminPanelIcons.ADD_TAB));
            newTab.add(tabContent);
            newTab.setClosable(true);
            tabContent.setTop(5);
            tabContent.setLeft(5);
            tabContent.getAnchors().setRight(new Anchors.Anchor(1, -5));
            tabContent.getAnchors().setBottom(new Anchors.Anchor(1, -5));
            tabWidget.setCurrentIndex(newTab.getTabIndex());
            if (tabWidget.getTabCount() > 2 && !tabWidget.getChildren().get(0).isVisible()) { 
                tabWidget.getChildren().get(0).setVisible(true);
            }
            return tabPresenter;
        }

        @Override
        public void removeTab(int index) {
            tabWidget.removeTab(tabWidget.getTab(index));
            if (tabWidget.getTabCount() <= 2) {
                if (isTabsEnabled()) {
                    WidgetAreaTab firstTab = getTab(0);
                    if (firstTab != null && firstTab.isClosable()) {
                        tabWidget.setClosable(0, false);
                    }
                } else {
                    tabWidget.getChildren().get(0).setVisible(false);
                }
            }
        }

        @Override
        public void setCurrentTab(int index) {
            tabWidget.setCurrentTab(index);
        }

        @Override
        public void setTabClosable(int index, boolean isClosable) {
            tabWidget.getTab(index).setClosable(isClosable);
        }

        @Override
        public void setTabsEnabled(boolean isEnabled) {
            createNewPageBtn.setVisible(isEnabled);
            createNewPageBtn.setEnabled(isEnabled);
            if (isEnabled == false) {
                tabWidget.removeTab(tabWidget.getTab(tabWidget.getTabCount() - 1));
                tabWidget.setCreateNewTab(null);
            } else {
                createNewTab = tabWidget.addTab("");
                tabWidget.setCreateNewTab(createNewTab);
                ToolButton tb = createToolButton(true, getEnvironment().getMessageProvider().translate("WidgetsArea", "Add Page"), 
                        getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CREATE));
                tb.setIconHeight(16);
                tb.setIconWidth(16);
                tb.setHeight(17);
                tb.setWidth(17);
                tb.getHtml().removeClass("rwt-tool-button");
                tb.getHtml().setCss("outline", "none");
                tb.addClickHandler(new IButton.ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        onCreateNewTabButtonClick();
                    }
                });
                UIObject widget = tabWidget.getTab(tabWidget.getTabCount() - 1).getRightTabButton();
                tabWidget.getTab(tabWidget.getTabCount() - 1).setRightTabButton(null);
                tabWidget.getTab(tabWidget.getTabCount() - 1).setRightTabButton(tb);
                if (widget != null) {
                    widget.setParent(null);
                }
            }
            refreshGui();
        }

        @Override
        public String getTabTitle() {
            InputValueDialog dlg = new InputValueDialog(EValType.STR, new EditMaskStr(),
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "Enter Tab Title"),
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "New tab title"), getEnvironment());
            return (String) dlg.getValue();
        }

        @Override
        public void refreshGui() {
            if (tabWidget.getTabCount() <= (isTabsEnabled() ? 2 : 1)) {
                if (isTabsEnabled()) {
                    WidgetAreaTab firstTab = getTab(0);
                    if (firstTab != null && firstTab.isClosable()) {
                        tabWidget.getTab(0).setClosable(false);
                    }
                    tabWidget.getChildren().get(0).setVisible(true);
                } else {
                    tabWidget.getChildren().get(0).setVisible(false);
                }
                if (tabWidget.getTabCount() == (isTabsEnabled() ? 1 : 0)) {
                    createNewTabButton.setEnabled(false);
                } else {
                    createNewTabButton.setEnabled(true);
                }
            } else if (isTabsEnabled()) {
                WidgetAreaTab firstTab = getTab(0);
                if (firstTab != null && firstTab.isClosable()) {
                    tabWidget.getTab(0).setClosable(true);
                }
                tabWidget.getChildren().get(0).setVisible(true);
            } else {
                tabWidget.getChildren().get(0).setVisible(true);
            }
        }

        private void onCreateNewTabButtonClick() {
            InputValueDialog dlg = new InputValueDialog(EValType.STR, new EditMaskStr(), 
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "Create page"), 
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "Name:"), getEnvironment());
            Object val = dlg.getValue();
            if (val != null) {
                widgetAreaController.addTab((String) val);
            }
        }

        private void addNewItemButtonClicked() {
            RwtListDialog itemControllersListDialog = new RwtListDialog(getEnvironment(), null);
            itemControllersListDialog.setWindowTitle(getEnvironment().getMessageProvider().translate("WidgetsArea", "Select widget"));
            List<ListWidgetItem> itemControllersList = new LinkedList<>();
            for (IWidgetAreaItemController controller : widgetAreaController.getControllersList()) {
                if (controller.isCreationEnabled(WebWidgetsArea.this)) {
                    itemControllersList.add(new ListWidgetItem(controller.getTitle(), controller));
                }
            }
            itemControllersListDialog.setItems(itemControllersList);
            if (itemControllersListDialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                getCurrentTab().addItem((IWidgetAreaItemController) itemControllersListDialog.getSelectedItems().get(0).getValue(), WidgetAreaItem.ECreationMode.MANUAL_CREATION);
            }
        }
    }

    private class TabPresenter implements IWidgetAreaTabPresenter {

        private final TabWidgetContent tabWidgetContent;
        private final IClientEnvironment environment;
        private final TabLayout tabWidget;

        public TabPresenter(TabWidgetContent content, IClientEnvironment environment, TabLayout tabWidget) {
            this.tabWidgetContent = content;
            this.environment = environment;
            this.tabWidget = tabWidget;
        }

        @Override
        public void setTitle(String title) {
            UIObject tab = tabWidgetContent.getParent();
            if (tab instanceof Tab) {
                ((Tab) tab).setTitle(title);
            }
        }

        @Override
        public void removeItem(IWidgetAreaItemPresenter item) {
            tabWidgetContent.removeItem(item);
        }

        @Override
        public IWidgetArea getWidgetsArea() {
            return WebWidgetsArea.this;
        }

        @Override
        public void setClosable(boolean isClosable) {
            UIObject tab = tabWidgetContent.getParent();
            if (tab instanceof Tab) {
                widgetAreaPresenter.setTabClosable(tabWidget.getTabIndex((Tab) tab), isClosable);
            }
        }

        @Override
        public void close() {
            UIObject tab = tabWidgetContent.getParent();
            if (tab instanceof Tab) {
                widgetAreaPresenter.removeTab(tabWidget.getTabIndex((Tab) tab));
            }
        }

        @Override
        public IWidgetAreaItemPresenter createItem(IWidgetAreaItemController itemController) {
            return new ItemWidget(tabWidgetContent, WebWidgetsArea.this.getEnvironment(), itemController.getTitle());
        }

        @Override
        public void addItem(IWidgetAreaItemPresenter presenter, ItemListener listener, int gridLeft, int gridTop, int gridWidth, int gridHeight) {
            final ItemWidget itemObject = (ItemWidget) presenter;
            this.tabWidgetContent.addItem(itemObject, listener, gridLeft, gridTop, gridWidth, gridHeight);
            itemObject.setVisible(true);
        }

        @Override
        public IClientEnvironment getEnvironment() {
            return environment;
        }
    }

    private final WidgetAreaController widgetAreaController;
    private final WidgetAreaPresenter widgetAreaPresenter = new WidgetAreaPresenter();

    public WebWidgetsArea(IClientEnvironment environment, IWidget parent) {
        widgetAreaController = new WidgetAreaController(widgetAreaPresenter, environment);
    }

    @Override
    public void setGridSize(int width, int height) {
        widgetAreaController.setGridSize(width, height);
    }

    @Override
    public int getGridWidth() {
        return widgetAreaController.getGridWidth();
    }

    @Override
    public int getGridHeight() {
        return widgetAreaController.getGridHeight();
    }

    @Override
    public WidgetAreaTab addTab(String title) {
        return widgetAreaController.addTab(title);
    }

    @Override
    public int getTabCount() {
        return widgetAreaController.getTabCount();
    }

    @Override
    public WidgetAreaTab getTab(int index) {
        return widgetAreaController.getTab(index);
    }

    @Override
    public void removeTab(int index) {
        widgetAreaController.removeTab(index, false);
    }

    @Override
    public WidgetAreaTab getCurrentTab() {
        return widgetAreaController.getCurrentTab();
    }

    @Override
    public void setCurrentTab(int index) {
        widgetAreaController.setCurrentTab(index);
    }

    @Override
    public boolean isTabsEnabled() {
        return widgetAreaController.isTabsEnabled();
    }

    @Override
    public void setTabsEnabled(boolean isEnabled) {
        widgetAreaController.setTabsEnabled(isEnabled);
    }

    @Override
    public boolean close(boolean forced) {
        return widgetAreaController.close(forced);
    }
    
    @Override
    public boolean applyChanges() {
        return widgetAreaController.applyChanges();
    }
    
    @Override
    public void cancelChanges() {
        widgetAreaController.cancelChanges();
    }

    @Override
    public List<WidgetAreaItem> getModifiedItems() {
        return widgetAreaController.getModifiedItems();
    }


    @Override
    public void setControllerClasses(List<Class<? extends IWidgetAreaItemController>> controllerClasses) {
        widgetAreaController.setControllerClasses(controllerClasses);
    }

    @Override
    public void setControllerClassIds(Id... controllerClassIds) {
        List<Class<? extends IWidgetAreaItemController>> controllerClasses = new LinkedList<>();
        for (Id id : controllerClassIds) {
            Class<?> clazz = getEnvironment().getDefManager().getDynamicClassById(id);
            if (IWidgetAreaItemController.class.isAssignableFrom(clazz)) {
                controllerClasses.add((Class<? extends IWidgetAreaItemController>) clazz);
            }
        }
        widgetAreaController.setControllerClasses(controllerClasses);
    }

    @Override
    public org.radixware.schemas.widgetsarea.WidgetsAreaDocument saveToXml() {
        return widgetAreaController.saveToXml();
    }

    @Override
    public void loadFromXml(org.radixware.schemas.widgetsarea.WidgetsAreaDocument widgetsAreaDocument) {
        widgetAreaController.loadFromXml(widgetsAreaDocument);
    }

    @Override
    public List<WidgetAreaItem> getItemsByOrder() {
        return widgetAreaController.getItemsByOrder();
    }

    @Override
    public List<WidgetAreaItem> getItemsByController(Class<? extends IWidgetAreaItemController> controllerClass) {
        return widgetAreaController.getItemsByControllerClass(controllerClass);
    }

    @Override
    public List<WidgetAreaItem> getItemsByController(Id controllerClassId) {
        return widgetAreaController.getItemsByContollerClassId(controllerClassId);
    }

    @Override
    public IToolBar getToolBar() {
        return widgetAreaPresenter.toolBar;
    }

    private ToolButton createToolButton(boolean isAutoRaise, String toolTip, Icon icon) {
        ToolButton toolButton = new ToolButton();
        toolButton.setToolTip(toolTip);
        toolButton.setAutoRaise(isAutoRaise);
        toolButton.setIcon(icon);
        return toolButton;
    }
}
