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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemPresenter;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaTabPresenter;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaController;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.rwt.UIObject;

class TabWidgetContent extends UIObject {

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/widgetsarea.js"};
    }

    private final Map<ItemWidget, IWidgetAreaTabPresenter.ItemListener> lisetnersMap = new HashMap<>();

    public TabWidgetContent() {
        super(new Html("DIV"));
        getHtml().layout("$RWT.widgetsarea.tab.layout");
    }

    public void addItem(UIObject item, IWidgetAreaTabPresenter.ItemListener itemListener, int gridLeft, int gridTop, int gridWidth, int gridHeight) {
        item.getHtml().setAttr("gridTop", gridTop);
        item.getHtml().setAttr("gridLeft", gridLeft);
        item.getHtml().setAttr("gridWidth", gridWidth);
        item.getHtml().setAttr("gridHeight", gridHeight);
        item.getHtml().setCss("position", "absolute");
        getHtml().add(item.getHtml());
        item.setParent(this);
        item.setVisible(true);
        final ItemWidget itemWidget = (ItemWidget)item;
        lisetnersMap.put(itemWidget, itemListener);
        itemWidget.setEditFinishedListener(new ItemWidget.EditFinishedListener() {

            @Override
            public void onEditFinished(int left, int top, int width, int height) {
                lisetnersMap.get(itemWidget).onChangeGeometry(itemWidget, left, top, width, height);
            }
        });
    }

    void setBounds(ItemWidget itemObject, int gridTop, int gridLeft, int gridWidth, int gridHeight) {
        itemObject.getHtml().setAttr("gridTop", gridTop);
        itemObject.getHtml().setAttr("gridLeft", gridLeft);
        itemObject.getHtml().setAttr("gridWidth", gridWidth);
        itemObject.getHtml().setAttr("gridHeight", gridHeight);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else {
            for (ItemWidget item : lisetnersMap.keySet()) {
                obj = item.findObjectByHtmlId(id);
                if (obj != null) {
                    return obj;
                }
            }
            return null;
        }
    }

    void removeItem(IWidgetAreaItemPresenter item) {
        lisetnersMap.remove(item);
        getHtml().remove(((ItemWidget) item).getHtml());
        ((ItemWidget) item).setParent(null);
    }
}
