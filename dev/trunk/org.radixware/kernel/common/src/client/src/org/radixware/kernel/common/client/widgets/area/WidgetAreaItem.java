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

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;

public class WidgetAreaItem {

    public static enum ECreationMode {

        LOAD_FROM_XML, //Создание происходит во время восстановления из Xml 
        PROGRAMMED_CREATION,//Программное создание путем вызова addItem у WidgetAreaTab 
        MANUAL_CREATION//Создание по запросу пользователя
    }; 

    private boolean isResizable = true;
    private String title;
    private TextOptions textOptions;
    private int gridLeft, gridTop, gridWidth, gridHeight;
    private boolean isClosable = true;
    private final IClientEnvironment environment;
    private final List<Action> actions = new LinkedList<>();

    private IWidget contentWidget;
    private final IWidgetAreaItemController controller;
    private final IWidgetAreaItemPresenter presenter;
    private final WidgetAreaTab ownerTab;

    WidgetAreaItem(final IWidgetAreaItemController controller,
            final IWidgetAreaItemPresenter presenter,
            final IClientEnvironment environment,
            final WidgetAreaTab tab) {
        this.environment = environment;
        this.controller = controller;
        this.presenter = presenter;
        this.ownerTab = tab;
    }

    public void setTilte(String title) {
        this.title = title;
        presenter.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitleTextOptions(TextOptions textOptions) {
        this.textOptions = textOptions;
    }

    public TextOptions getTitleTextOptions() {
        return textOptions;
    }

    public boolean setBounds(int gridLeft, int gridTop, int gridWidth, int gridHeight) {
        IWidgetArea area = ownerTab.getWidgetsArea();
        if (ownerTab.canBePlaced(ownerTab.getFilledItemsMap(this), area.getGridWidth(), area.getGridHeight(), gridLeft, gridTop, gridWidth, gridHeight)) {
            this.gridLeft = gridLeft;
            this.gridTop = gridTop;
            this.gridWidth = gridWidth;
            this.gridHeight = gridHeight;
            presenter.setBounds(gridTop, gridLeft, gridWidth, gridHeight);
            return true;
        } else {
            return false;
        }
    }

    public void adjustBounds(int gridLeft, int gridTop, int gridWidth, int gridHeight) {
        ownerTab.adjustBounds(this, gridLeft, gridTop, gridWidth, gridHeight);
    }

    public int getLeft() {
        return gridLeft;
    }

    public int getTop() {
        return gridTop;
    }

    public int getWidth() {
        return gridWidth;
    }

    public int getHeight() {
        return gridHeight;
    }

    public void setResizable(boolean isResizable) {
        if (isResizable() != isResizable) {
            this.isResizable = isResizable;
            if (isResizable) {
                presenter.addAction(presenter.getResizeAction());
            } else {
                presenter.removeAction(presenter.getResizeAction());
            }
        }
    }

    public boolean isResizable() {
        return isResizable;
    }

    public WidgetAreaTab getOwnerTab() {
        return ownerTab;
    }

    public IWidgetArea getWidgetsArea() {
        return ownerTab.getWidgetsArea();
    }

    public boolean isClosable() {//Если метод возвращает true, то пользователь может закрыть элемент, иначе закрыть его можно только программно
        return isClosable;
    }

    public void setClosable(boolean isClosable) {
        if (isClosable() != isClosable) {
            this.isClosable = isClosable;
            if (isClosable) {
                presenter.addAction(presenter.getRemoveAction());
            } else {
                presenter.removeAction(presenter.getRemoveAction());
            }
        }
    }

    public boolean close(boolean forced) {
        List<WidgetAreaItem> list = ownerTab.getItemsByOrder();
        if (list.contains(this)) {
            if (!forced) {
                if (isModified()) {
                    Set<EDialogButtonType> btnSet = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);
                    EDialogButtonType result = getEnvironment().messageBox(
                            title,
                            getEnvironment().getMessageProvider().translate("WidgetsArea", "Do you want to save results?"),
                            EDialogIconType.QUESTION, btnSet);
                    if (result == EDialogButtonType.YES) {
                        if (!applyChanges()) {
                            return false;
                        }
                    } else if (result == EDialogButtonType.CANCEL) { 
                        return true;
                    }
                } else {
                    Set<EDialogButtonType> btnSet = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);
                    EDialogButtonType result = getEnvironment().messageBox(
                            title, 
                            getEnvironment().getMessageProvider().translate("WidgetsArea", "Do you really want to remove widget?"), 
                            EDialogIconType.QUESTION, btnSet);
                    if (result == EDialogButtonType.NO) {
                        return true;
                    }
                }
                if (!beforeClose()) {
                    return false;
                }
            }
            ownerTab.removeItem(list.indexOf(this));
        }
        return true;
    }

    public IWidget getContent() {
        return contentWidget;
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    //методы для работы с кнопками в заголовке
    public void addAction(Action action) {
        presenter.addAction(action);
        actions.add(action);
    }

    public void addAction(int pos, Action action) {
        if (action != null) {
            actions.add(pos, action);
            presenter.addAction(pos, action);
        }
    }

    public void removeAction(Action action) {
        if (action != null) {
            presenter.removeAction(action);
            actions.remove(action);
        }
    }

    public List<Action> getActions() {
        return actions;
    }

    void setContent(IWidget contentWidget) {
        this.contentWidget = contentWidget;
    }

    public boolean isModified() {
        return controller.isModified(this);
    }

    public boolean applyChanges() {
        return controller.applyChanges(this);
    }
    
    public void cancelChanges() {
        controller.cancelChanges(this);
    }

    public boolean beforeClose() {
        return controller.beforeClose(this);
    }

    XmlObject store() {
        return controller.store(this);
    }
}
