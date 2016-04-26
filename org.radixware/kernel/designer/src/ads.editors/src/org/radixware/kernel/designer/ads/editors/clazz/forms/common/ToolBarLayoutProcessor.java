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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.netbeans.api.visual.graph.GraphScene;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ActionItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class ToolBarLayoutProcessor extends LayoutProcessor {

    private AdsWidgetDef toolbar;

    public ToolBarLayoutProcessor(BaseWidget wg, AdsWidgetDef toolbar) {
        super(wg);
        this.toolbar = toolbar;
        wg.setLayout(new ToolBarLayout());
        wg.revalidate();
    }

    public ToolBarLayoutProcessor(BaseWidget wg) {
        this(wg, (AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) wg.getSceneImpl().findObject(wg)));
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        final AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(toolbar, "orientation");

        if (orientation.value == EOrientation.Horizontal) {
            locateHorizontal(locator, localPoint);
        } else {
            locateVertical(locator, localPoint);
        }
    }

    private void locateHorizontal(DragDropLocator locator, Point localPoint) {

        int hPos = 0;
        for (final BaseWidget wdg : getWidgets(toolbar, wg.getSceneImpl())) {
            final int h = wdg.getGeometry().width;
            hPos += h;
        }

        final Rectangle geometry = wg.getLayoutGeometry();
        locator.locate(Arrays.asList(
                new Point(geometry.x + hPos, geometry.y),
                new Point(geometry.x + hPos, geometry.y + geometry.height)));
    }
    public final static int INTERVAL = 4;

    private void locateVertical(DragDropLocator locator, Point localPoint) {

        int vPos = 0;
        for (final BaseWidget wdg : getWidgets(toolbar, wg.getSceneImpl())) {
            final int h = wdg.getGeometry().height;
            vPos += INTERVAL + h;
        }

        final Rectangle geometry = wg.getLayoutGeometry();
        locator.locate(Arrays.asList(
                new Point(geometry.x, geometry.y + vPos),
                new Point(geometry.x + geometry.width, geometry.y + vPos)));
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {

        final AdsWidgetDef.Widgets widgets = toolbar.getWidgets();
        widgets.add((AdsWidgetDef) node);
        return node;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        node.delete();
        wg.revalidate();
        return node;
    }

    public static List<BaseWidget> getWidgets(AdsWidgetDef node, GraphScene scene) {
        final List<BaseWidget> items = new ArrayList<>();

        final AdsUIProperty.IdListProperty actionsProp = (AdsUIProperty.IdListProperty) AdsUIUtil.getUiProperty(node, "actions");
        if (actionsProp != null) {

            for (final Id id : actionsProp.getIds()) {
                final AdsWidgetDef action = ActionItem.findAction(node.getModule(), id);

                if (action != null) {
                    items.add((BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(action)));
                }
            }
        }

        for (final AdsWidgetDef child : node.getWidgets()) {
            items.add((BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(child)));
        }

        return items;
    }


}
