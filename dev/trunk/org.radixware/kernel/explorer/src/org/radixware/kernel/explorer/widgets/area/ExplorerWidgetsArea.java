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
package org.radixware.kernel.explorer.widgets.area;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSignalMapper;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QInputDialog;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.area.IWidgetArea;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemController;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemPresenter;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaPresenter;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaTabPresenter;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaController;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaItem;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaTab;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerListDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;
import org.radixware.schemas.widgetsarea.WidgetsAreaDocument;

public class ExplorerWidgetsArea extends ExplorerFrame implements IWidgetArea {

     private final static class AdminPanelIcons extends ClientIcon {

        private AdminPanelIcons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon ADD_TAB = new AdminPanelIcons("classpath:images/addEmpty.svg");
    }
    
    private class WidgetAreaPresenter implements IWidgetAreaPresenter {

        private final QVBoxLayout layout = new QVBoxLayout();
        private final ExplorerToolBar toolBar;
        private final QTabWidget tabWidget;
        private final QAction createNewPageAction;
        private final QAction addWidgetAction;
        private final QTabBar tabBar;

        public WidgetAreaPresenter() {
            MessageProvider mp = getEnvironment().getMessageProvider();
            tabWidget = new QTabWidget(ExplorerWidgetsArea.this);
            tabWidget.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
            tabWidget.customContextMenuRequested.connect(this, "onTabBarMenuRequest(QPoint)");
            tabWidget.setTabsClosable(true);
            tabWidget.tabCloseRequested.connect(this, "onTabCloseRequest(int)");
            tabBar = ((QTabBar) tabWidget.findChild(QTabBar.class));
            QToolButton createNewTabButton;createNewTabButton = createToolButton(true, mp.translate("WidgetsArea", "Add Page"),
                    ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));
            createNewTabButton.setFixedSize(17, 17);
            createNewTabButton.clicked.connect(this, "onCreateNewTabButtonClick()");
            createNewTabButton.setParent(tabBar);
            tabBar.setTabEnabled(0, false);
            toolBar = new ExplorerToolBar(ExplorerWidgetsArea.this);
            QWidget spacer = new QWidget();
            spacer.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            toolBar.addWidget(spacer);
            layout.addWidget(toolBar);
            QToolButton createNewPageBtn = createToolButton(true, mp.translate("WidgetsArea", "Add Page"),
                    ExplorerIcon.getQIcon(AdminPanelIcons.ADD_TAB));
            createNewPageBtn.clicked.connect(this, "onCreateNewTabButtonClick()");
            createNewPageAction = toolBar.addWidget(createNewPageBtn);
            QToolButton createNewItemBtn = createToolButton(true, mp.translate("WidgetsArea", "Add widget"),
                    ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));
            createNewItemBtn.clicked.connect(this, "addNewItemButtonClicked()");
            addWidgetAction = toolBar.addWidget(createNewItemBtn);
            layout.addWidget(tabWidget);
            int iconedTab = tabBar.addTab("");
            tabBar.setTabButton(iconedTab, QTabBar.ButtonPosition.RightSide, createNewTabButton);
            ExplorerWidgetsArea.this.setLayout(layout);
            tabWidget.currentChanged.connect(this, "onChangeCurrentTab(int)");
            addWidgetAction.setEnabled(false);
        }

        @Override
        public IWidgetAreaTabPresenter addTab(String title) {
            final TabWidgetContent tabContent = new TabWidgetContent(widgetAreaController);
            final TabPresenter tabPresenter = new TabPresenter(tabContent, getEnvironment(), tabWidget);
            WidgetAreaTab firstTab = getTab(0);
            if (firstTab != null && firstTab.isClosable()) {
                QWidget button = tabBar.tabButton(0, QTabBar.ButtonPosition.RightSide);
                if (!button.isEnabled()) {
                    button.setEnabled(true);
                }
                if (!button.isVisible()) {
                    button.setVisible(true);
                }
            }
            int newTabIdx = tabWidget.insertTab(tabWidget.count() - 1, tabContent, title);
            tabWidget.setCurrentIndex(newTabIdx);
            tabWidget.setTabEnabled(tabWidget.count() - 1, false);
            if (tabWidget.count() > 2 && !tabBar.isVisible()) { 
                tabBar.setVisible(true);
            }
            return tabPresenter;
        }

        @Override
        public void removeTab(int index) {
            tabWidget.removeTab(index);
            if (tabWidget.count() <= 2) { 
                if (isTabsEnabled()) {
                    WidgetAreaTab firstTab = getTab(0);
                    if (firstTab != null && firstTab.isClosable()) {
                        QWidget button = tabBar.tabButton(0, QTabBar.ButtonPosition.RightSide);
                        button.setEnabled(false);
                        button.setVisible(false);
                    }
                } else {
                    tabBar.setVisible(false);
                }
            }
        }

        public void setWidgetAreaTabTitle(final TabWidgetContent layout, final String title) {
            final int index = tabWidget.indexOf(layout);
            if (index > -1) {
                tabWidget.setTabText(index, title);
            }
        }

        @Override
        public void setCurrentTab(int index) {
            tabWidget.setCurrentIndex(index);
        }

        @SuppressWarnings("unused")
        private void onTabCloseRequest(int tabIdx) {
            widgetAreaController.removeTab(tabIdx, false);
        }

        @SuppressWarnings("unused")
        private void onChangeCurrentTab(int tabIdx) {
            ExplorerWidgetsArea.this.setCurrentTab(tabIdx);
        }

        @SuppressWarnings("unused")
        private void onCreateNewTabButtonClick() {
            String title = QInputDialog.getText(null, getEnvironment().getMessageProvider().translate("WidgetsArea", "Create Page"),
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "Name:"));
            if (title != null) {
                widgetAreaController.addTab(title);
            }
        }

        @SuppressWarnings("unused")
        private void addNewItemButtonClicked() {
            ExplorerListDialog itemControllersListDialog = new ExplorerListDialog(getEnvironment(), ExplorerWidgetsArea.this);
            itemControllersListDialog.setWindowTitle(getEnvironment().getMessageProvider().translate("WidgetsArea", "Select widget"));
            List<ListWidgetItem> itemControllersList = new LinkedList<>();
            for (IWidgetAreaItemController controller : widgetAreaController.getControllersList()) {
                if (controller.isCreationEnabled(ExplorerWidgetsArea.this)) {
                    itemControllersList.add(new ListWidgetItem(controller.getTitle(), controller));
                }
            }
            itemControllersListDialog.setItems(itemControllersList);
            if (itemControllersListDialog.exec() == QDialog.DialogCode.Accepted.value()) {
                getCurrentTab().addItem((IWidgetAreaItemController) itemControllersListDialog.getSelectedItems().get(0).getValue(), WidgetAreaItem.ECreationMode.MANUAL_CREATION);
            }
        }

        @Override
        public void setTabClosable(int index, boolean isClosable) {
            QWidget tabButton = tabBar.tabButton(index, QTabBar.ButtonPosition.RightSide); 
            if (isClosable) {
                tabButton.resize(16, 16);
            } else {
                tabButton.resize(0, 0);
            }
        }

        @SuppressWarnings("unused")
        private void onTabBarMenuRequest(QPoint point) {
            if (point != null) {
                int tabIndex = tabBar.tabAt(point);
                if (tabIndex > -1 && tabIndex < tabBar.count() - 1) { //we dont need to get menu on the "create new tab" tab
                    com.trolltech.qt.gui.QMenu menu = new com.trolltech.qt.gui.QMenu(tabWidget);
                    com.trolltech.qt.gui.QAction renameAct = new com.trolltech.qt.gui.QAction(
                            getEnvironment().getMessageProvider().translate("WidgetsArea", "Rename"), menu);
                    QSignalMapper signalMapper = new QSignalMapper(ExplorerWidgetsArea.this);
                    signalMapper.mappedInteger.connect(widgetAreaController, "renameTab(int)");
                    signalMapper.setMapping(renameAct, tabIndex);
                    renameAct.triggered.connect(signalMapper, "map()");
                    menu.addAction(renameAct);
                    menu.exec(tabWidget.mapToGlobal(point));
                }
            }
        }

        @Override
        public String getTabTitle() {
            return QInputDialog.getText(ExplorerWidgetsArea.this, 
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "Enter Tab Title"),
                    getEnvironment().getMessageProvider().translate("WidgetsArea", "New tab title"));
        }

        @Override
        public void setTabsEnabled(boolean isEnabled) {
            createNewPageAction.setVisible(isEnabled);
            createNewPageAction.setEnabled(isEnabled);
            if (isEnabled == false) {
                tabBar.removeTab(tabBar.count() - 1);
            } else {
                tabBar.addTab("");
                QToolButton tb = createToolButton(true, getEnvironment().getMessageProvider().translate("WidgetsArea", "create tab"),
                        ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));
                tb.setParent(tabBar);
                tb.setFixedSize(17, 17);
                tb.clicked.connect(this, "onCreateNewTabButtonClick()");
                tabBar.setTabEnabled(tabBar.count() - 1, false);
                QWidget widget = tabBar.tabButton(tabBar.count() - 1, QTabBar.ButtonPosition.RightSide);
                tabBar.setTabButton(tabBar.count() - 1, QTabBar.ButtonPosition.RightSide, tb);
                if (widget != null) { //qt hides a remove button widget (from setTabsClosable()) on the right side if we add new tab button there. So need to delete it.
                    widget.disableGarbageCollection();
                    widget.setParent(null);
                    widget.disposeLater();
                }
            }
            refreshGui();
        }

        @Override
        public void refreshGui() {
            if (tabWidget.count() <= (isTabsEnabled() ? 2 : 1)) { 
                if (isTabsEnabled()) {
                    WidgetAreaTab firstTab = getTab(0);
                    if (firstTab != null && firstTab.isClosable()) {
                        QWidget button = tabBar.tabButton(0, QTabBar.ButtonPosition.RightSide);
                        button.setEnabled(false);
                        button.setVisible(false);
                    }
                    tabBar.setVisible(true);
                } else {
                    tabBar.setVisible(false);
                }
                if (tabWidget.count() == (isTabsEnabled() ? 1 : 0)) {
                    addWidgetAction.setEnabled(false);
                } else {
                    addWidgetAction.setEnabled(true);
                }
            } else if (isTabsEnabled()) {
                WidgetAreaTab firstTab = getTab(0);
                if (firstTab != null && firstTab.isClosable()) {
                    QWidget button = tabBar.tabButton(0, QTabBar.ButtonPosition.RightSide);
                    button.setEnabled(true);
                    button.setVisible(true);
                }
                tabBar.setVisible(true);
            } else {
                tabBar.setVisible(true);
            }
        }
    }

    private class TabPresenter extends QEventFilter implements IWidgetAreaTabPresenter {

        private final TabWidgetContent tabWidgetContent;
        private final IClientEnvironment environment;
        private final QTabWidget tabWidget;

        public TabPresenter(TabWidgetContent content, IClientEnvironment environment, QTabWidget tabWidget) {
            super(content);
            this.setProcessableEventTypes(EnumSet.of(QEvent.Type.MouseMove, QEvent.Type.MouseButtonPress, QEvent.Type.MouseButtonRelease, QEvent.Type.MouseButtonDblClick));
            this.tabWidgetContent = content;
            this.environment = environment;
            this.tabWidget = tabWidget;
        }

        @Override
        public void setTitle(String title) {
            ExplorerWidgetsArea.this.widgetAreaPresenter.setWidgetAreaTabTitle(tabWidgetContent, title);
        }

        @Override
        public void removeItem(IWidgetAreaItemPresenter item) {
            tabWidgetContent.removeItem(item);
        }

        @Override
        public IWidgetArea getWidgetsArea() {
            return ExplorerWidgetsArea.this;
        }

        @Override
        public void setClosable(boolean isClosable) {
            widgetAreaPresenter.setTabClosable(tabWidget.indexOf(tabWidgetContent), isClosable);
        }

        @Override
        public void close() {
            widgetAreaPresenter.removeTab(tabWidget.indexOf(tabWidgetContent));
        }

        @Override
        public IWidgetAreaItemPresenter createItem(IWidgetAreaItemController itemController) {
            return new ItemWidget(tabWidgetContent, ExplorerWidgetsArea.this.getEnvironment(), itemController.getTitle());
        }

        @Override
        public void addItem(IWidgetAreaItemPresenter presenter, ItemListener listener, int gridLeft, int gridTop, int gridWidth, int gridHeight) {
            final ItemWidget itemObject = (ItemWidget) presenter;
            this.tabWidgetContent.addItem(itemObject, listener, gridLeft, gridTop, gridWidth, gridHeight);
            itemObject.installEventFilter(this);
            itemObject.setVisible(true);
        }

        void setBounds(ItemWidget itemObject, int gridTop, int gridLeft, int gridWidth, int gridHeight) {
            tabWidgetContent.setBounds(itemObject, gridTop, gridLeft, gridWidth, gridHeight);
        }

        @Override
        public boolean eventFilter(QObject target, QEvent event) {
            if (target instanceof ItemWidget) {
                if (event instanceof QMouseEvent) {
                    if (event.type() == QEvent.Type.MouseButtonDblClick && ((ItemWidget) target).getHeaderCoordinates().contains(((QMouseEvent) event).pos()) && tabWidgetContent.isResizable((ItemWidget) target)) {
                        tabWidgetContent.onItemDoubleClickEvent((QWidget) target);
                    } else if (event.type() == QEvent.Type.MouseButtonPress && ((ItemWidget) target).getHeaderCoordinates().contains(((QMouseEvent) event).pos())) {
                        if (!tabWidgetContent.isInEditingMode()) {
                            tabWidgetContent.onItemPressEvent((QWidget) target, ((QMouseEvent) event).pos(), (QMouseEvent) event);
                        }
                    }
                    if (event.type() == QEvent.Type.MouseMove) {
                        tabWidgetContent.onItemMove((QMouseEvent) event, (QWidget) target);
                    } else if (event.type() == QEvent.Type.MouseButtonRelease) {
                        tabWidgetContent.onItemReleaseEvent();
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public IClientEnvironment getEnvironment() {
            return environment;
        }
    }

    private final WidgetAreaController widgetAreaController;
    private final WidgetAreaPresenter widgetAreaPresenter = new WidgetAreaPresenter();

    public ExplorerWidgetsArea(IClientEnvironment environment, IWidget parent) {
        super(environment);
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
                controllerClasses.add((Class<? extends IWidgetAreaItemController>)clazz);
            }
        }
        widgetAreaController.setControllerClasses(controllerClasses);
    }

    @Override
    public WidgetsAreaDocument saveToXml() {
        return widgetAreaController.saveToXml();
    }

    @Override
    public void loadFromXml(WidgetsAreaDocument widgetsAreaDocument) {
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

    private QToolButton createToolButton(boolean isAutoRaise, String toolTip, QIcon icon) {
        QToolButton toolButton = new QToolButton();
        toolButton.setToolTip(toolTip);
        toolButton.setAutoRaise(isAutoRaise);
        toolButton.setIcon(icon);
        return toolButton;
    }

}
