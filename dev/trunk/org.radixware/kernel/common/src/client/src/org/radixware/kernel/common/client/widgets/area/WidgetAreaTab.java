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

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;

public class WidgetAreaTab {

    private final static int DEFAULT_ITEM_WIDTH = 3;
    private final static int DEFAULT_ITEM_HEIGHT = 3;

    private final static int MIN_ITEM_WIDTH = 2;
    private final static int MIN_ITEM_HEIGHT = 2;

    public interface CloseListener {

        public boolean onTabClosed(WidgetAreaTab tab, boolean forced);
    }

    private final IWidgetAreaTabPresenter presenter;
    private final List<WidgetAreaItem> itemList = new LinkedList<>();
    private String title;
    private boolean isClosable = true;
    private final Map<WidgetAreaItem, IWidgetAreaItemController> itemToControllerMap = new HashMap<>();
    private final Map<WidgetAreaItem, IWidgetAreaItemPresenter> widgetAreaItem2PresenterMap = new HashMap<>();
    private CloseListener closeListener;

    public WidgetAreaTab(final IWidgetAreaTabPresenter presenter, final String title) {
        this.presenter = presenter;
        this.title = title;
    }

    public void setTilte(String title) {
        this.title = title;
        presenter.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public WidgetAreaItem addItem(final IWidgetAreaItemController controller, WidgetAreaItem.ECreationMode creationMode) {
        return addItem(controller, creationMode, null);
    }

    boolean[][] getFilledItemsMap(WidgetAreaItem excludedItem) {
        IWidgetArea area = getWidgetsArea();
        boolean filledItemsMap[][] = new boolean[area.getGridWidth()][area.getGridHeight()]; //fill the map 
        for (WidgetAreaItem item : itemList) {
            if (excludedItem == null || !item.equals(excludedItem)) {
                for (int i = item.getTop(); i < item.getTop() + item.getHeight(); i++) {
                    for (int j = item.getLeft(); j < item.getLeft() + item.getWidth(); j++) {
                        filledItemsMap[i][j] = true;
                    }
                }
            }
        }
        return filledItemsMap;
    }

    WidgetAreaItem addItem(final IWidgetAreaItemController controller, WidgetAreaItem.ECreationMode creationMode, XmlObject storedItem) {//Может дополнять список, полученный в методе setControllerClasses.        
        IWidgetArea area = getWidgetsArea();
        boolean filledItemsMap[][] = getFilledItemsMap(null);
        boolean canBePlacedMin = false;
        labeInitialCanBePlaced:
        for (int i = 0; i < area.getGridHeight(); i++) {
            for (int j = 0; j < area.getGridWidth(); j++) {
                if (canBePlaced(filledItemsMap, area.getGridWidth(), area.getGridHeight(), j, i, MIN_ITEM_WIDTH, MIN_ITEM_HEIGHT)) {
                    canBePlacedMin = true;
                    break labeInitialCanBePlaced;
                }
            }
        }
        if (canBePlacedMin) {
            final IWidgetAreaItemPresenter itemPresenter = presenter.createItem(controller);
            final WidgetAreaItem widgetAreaItem = new WidgetAreaItem(controller, itemPresenter, presenter.getEnvironment(), this);
            adjustBounds(widgetAreaItem, 0, 0, DEFAULT_ITEM_WIDTH, DEFAULT_ITEM_HEIGHT);
            widgetAreaItem.setTilte(controller.getTitle());
            Action resizeAction = itemPresenter.getResizeAction();
            widgetAreaItem.addAction(resizeAction);
            Action removeAction = itemPresenter.getRemoveAction();
            removeAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    widgetAreaItem.close(false);
                }
            });
            widgetAreaItem.addAction(removeAction);
            final IWidget content;
            try {
                content = controller.createContent(widgetAreaItem, creationMode, storedItem);
                if (!widgetAreaItem.isClosable()) {
                    widgetAreaItem.removeAction(removeAction);
                }
                if (!widgetAreaItem.isResizable()) {
                    widgetAreaItem.removeAction(resizeAction);
                }
                widgetAreaItem.setContent(content);
            } catch (RuntimeException exception) {
                widgetAreaItem.getEnvironment().messageWarning(exception.toString());
                return widgetAreaItem;//FIXME return special WidgetAreaItem
            }
            if (content == null) {
                return null;
            }

            itemPresenter.setContent(content);
            IWidgetAreaTabPresenter.ItemListener itemListener = new IWidgetAreaTabPresenter.ItemListener() {

                @Override
                public void onChangeGeometry(IWidgetAreaItemPresenter item, int gridLeft, int gridTop, int gridWidth, int gridHeight) {
                    WidgetAreaTab.this.setItemBounds(itemPresenter, gridLeft, gridTop, gridWidth, gridHeight);
                }

                @Override
                public boolean isResizable(IWidgetAreaItemPresenter item) {
                    for (Entry<WidgetAreaItem, IWidgetAreaItemPresenter> entry : widgetAreaItem2PresenterMap.entrySet()) {
                        if (entry.getValue().equals(item)) {
                            return entry.getKey().isResizable();
                        }
                    }
                    return false;
                }

            };
            presenter.addItem(itemPresenter, itemListener, widgetAreaItem.getLeft(), widgetAreaItem.getTop(), widgetAreaItem.getWidth(), widgetAreaItem.getHeight());
            itemToControllerMap.put(widgetAreaItem, controller);
            widgetAreaItem2PresenterMap.put(widgetAreaItem, itemPresenter);
            itemList.add(widgetAreaItem);
            return widgetAreaItem;
        } else {
            presenter.getEnvironment().messageWarning("Item can't be placed!");
            return null;
        }

    }

    void adjustBounds(WidgetAreaItem item, int gridLeft, int gridTop, int gridWidth, int gridHeight) {
        boolean[][] filledItemsMap = getFilledItemsMap(item);
        IWidgetArea area = getWidgetsArea();
        int areaGridWidth = area.getGridWidth();
        int areaGridHeight = area.getGridHeight();
        int itemWidth = gridWidth, itemHeight = gridHeight;
        boolean firstTime = true;
        while (itemWidth != MIN_ITEM_WIDTH && itemHeight != MIN_ITEM_HEIGHT || firstTime) {
            if (itemWidth == MIN_ITEM_WIDTH && itemHeight == MIN_ITEM_HEIGHT) {
                firstTime = false;
            }
            if (canBePlaced(filledItemsMap, areaGridWidth, areaGridHeight, gridLeft, gridTop, itemWidth, itemHeight)) { //good place
                item.setBounds(gridLeft, gridTop, itemWidth, itemHeight);
                break;
            } else { //try to find another place
                boolean canPlace = false;
                label2:
                for (int i = 0; i < area.getGridHeight(); i++) {
                    for (int j = 0; j < area.getGridWidth(); j++) {
                        if (canBePlaced(filledItemsMap, areaGridWidth, areaGridHeight, j, i, itemWidth, itemHeight)) {
                            item.setBounds(j, i, itemWidth, itemHeight);
                            canPlace = true;
                            break label2;
                        }
                    }
                }
                if (canPlace) {
                    break;
                }
            }
            if (itemWidth > MIN_ITEM_WIDTH) {
                itemWidth--;
            }
            if (itemHeight > MIN_ITEM_HEIGHT) {
                itemHeight--;
            }
        }
    }

    public int getItemCount() {
        return itemList.size();
    }

    public WidgetAreaItem getItem(int index) {
        return getItemsByOrder().get(index);
    }

    public void removeItem(int index) {
        List<WidgetAreaItem> list = getItemsByOrder();
        if (!list.isEmpty()) {
            WidgetAreaItem item = list.get(index);
            itemList.remove(item);
            IWidgetAreaItemPresenter itemPresenter = widgetAreaItem2PresenterMap.get(item);
            presenter.removeItem(itemPresenter);
            widgetAreaItem2PresenterMap.remove(item);
            itemToControllerMap.remove(item);
        }
    }

    public IWidgetArea getWidgetsArea() {
        return presenter.getWidgetsArea();
    }

    public boolean isClosable() {//Если метод возвращает true, то пользователь может закрыть вкладку, иначе закрыть ее можно только программно
        return isClosable;
    }

    public void setClosable(boolean isClosable) {
        if (this.isClosable != isClosable) {
            this.isClosable = isClosable;
            presenter.setClosable(isClosable);
        }
    }

    public boolean close(boolean forced) {
        return closeListener.onTabClosed(this, forced);
    }

    boolean internalClose(boolean forced) {
        List<WidgetAreaItem> items = getItemsByOrder();
        List<WidgetAreaItem> modifiedItems = new LinkedList<>();
        if (!forced) {
            for (WidgetAreaItem item : items) {
                if (item.isModified()) {
                    modifiedItems.add(item);
                }
            }
            if (!modifiedItems.isEmpty()) {
                Set<EDialogButtonType> btnSet = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);
                EDialogButtonType result = presenter.getEnvironment().messageBox(
                        title, presenter.getEnvironment().getMessageProvider().translate("WidgetsArea", "Do you want to save results?"), 
                        EDialogIconType.QUESTION, btnSet);
                if (result == EDialogButtonType.YES) {
                    for (WidgetAreaItem modifiedItem : modifiedItems) {
                        if (!modifiedItem.applyChanges()) {
                            return false; 
                        }
                    }
                } else if (result == EDialogButtonType.CANCEL) {
                    return false;
                } 
            } else {
                Set<EDialogButtonType> btnSet = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);
                EDialogButtonType result = presenter.getEnvironment().messageBox(
                        title, 
                        presenter.getEnvironment().getMessageProvider().translate("WidgetsArea", "Do you really want to close tab?"),
                        EDialogIconType.QUESTION, btnSet);
                if (result == EDialogButtonType.NO) {
                    return false;
                }
            }
            for (WidgetAreaItem item : items) {
                if(!item.beforeClose()) {
                    return false;
                }
            }
        }
        
        for (WidgetAreaItem item : items) {
            item.close(true);
        }
        presenter.close();
        return true;
    }

    public void refresh() {
        for (Entry<WidgetAreaItem, IWidgetAreaItemPresenter> entry : widgetAreaItem2PresenterMap.entrySet()) {
            WidgetAreaItem item = entry.getKey();
            entry.getValue().setBounds(item.getTop(), item.getLeft(), item.getWidth(), item.getHeight());
        }
    }

    List<WidgetAreaItem> getItemsByOrder() {
        List<WidgetAreaItem> itemsList = new LinkedList<>(itemList);
        Collections.sort(itemsList, new Comparator<WidgetAreaItem>() {

            @Override
            public int compare(WidgetAreaItem o1, WidgetAreaItem o2) {
                if (o1.getTop() == o2.getTop()) {
                    return Integer.compare(o1.getLeft(), o2.getLeft());
                } else {
                    return Integer.compare(o1.getTop(), o2.getTop());
                }
            }

        });
        return itemsList;
    }

    public List<WidgetAreaItem> getItemsByControllerClass(Class<? extends IWidgetAreaItemController> controllerClass) {
        List<WidgetAreaItem> itemsList = getItemsByOrder();
        List<WidgetAreaItem> itemsByControllerList = new LinkedList<>();
        for (WidgetAreaItem item : itemsList) {
            if (itemToControllerMap.get(item).getClass().equals(controllerClass)) {
                itemsByControllerList.add(item);
            }
        }
        return itemsByControllerList;
    }

    public List<WidgetAreaItem> getItemsByControllerClassId(Id controllerClassId) {
        List<WidgetAreaItem> itemsByControllerList = new LinkedList<>();
        try {
            Class<?> clazz = presenter.getEnvironment().getDefManager().getDynamicClassById(controllerClassId);
            if (IWidgetAreaItemController.class.isAssignableFrom(clazz)) {
                List<WidgetAreaItem> itemsList = getItemsByOrder();

                for (WidgetAreaItem item : itemsList) {
                    if (itemToControllerMap.get(item).getClass().equals(clazz)) {
                        itemsByControllerList.add(item);
                    }
                }
            }
        } catch (DefinitionNotFoundError err) {
        }

        return itemsByControllerList;
    }

    IWidgetAreaItemController getControllerByItem(WidgetAreaItem item) {
        return itemToControllerMap.get(item);
    }

    private void setItemBounds(IWidgetAreaItemPresenter presenter, int gridLeft, int gridTop, int gridWidth, int gridHeight) { //Устанавливает с клиентсткой стороны если виджет растянулся 
        WidgetAreaItem currentItem = null;
        for (Entry<WidgetAreaItem, IWidgetAreaItemPresenter> entry : widgetAreaItem2PresenterMap.entrySet()) {
            if (entry.getValue().equals(presenter)) {
                currentItem = entry.getKey();
                break;
            }
        }
        if (currentItem != null) {
            currentItem.setBounds(gridLeft, gridTop, gridWidth, gridHeight);
        }
    }

    boolean canBePlaced(boolean filledItemsMap[][], int gridWidgth, int gridHeight, int itemLeft, int itemTop, int itemWidth, int itemHeight) {
        boolean canPlace = true;
        if (itemLeft + itemWidth <= gridWidgth
                && itemTop + itemHeight <= gridHeight) {
            label1:
            for (int i = itemTop; i < itemTop + itemHeight; i++) {
                for (int j = itemLeft; j < itemLeft + itemWidth; j++) {
                    if (filledItemsMap[i][j] == true) {
                        canPlace = false;
                        break label1;
                    }
                }
            }
        } else {
            canPlace = false;
        }
        return canPlace;
    }

    void setCloseListener(CloseListener listener) {
        this.closeListener = listener;
    }
}
