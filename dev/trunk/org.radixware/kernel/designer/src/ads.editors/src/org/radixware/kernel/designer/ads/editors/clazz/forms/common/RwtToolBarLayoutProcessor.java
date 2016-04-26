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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtActionItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class RwtToolBarLayoutProcessor extends LayoutProcessor{

    private AdsRwtWidgetDef toolbar;

    public RwtToolBarLayoutProcessor(BaseWidget wg, AdsRwtWidgetDef toolbar) {
        super(wg);
        this.toolbar = toolbar;
        wg.setLayout(new RwtToolBarLayout());
        wg.revalidate();
    }

    public RwtToolBarLayoutProcessor(BaseWidget wg) {
        this(wg, (AdsRwtWidgetDef) AdsUIUtil.currentWidget((AdsRwtWidgetDef) wg.getSceneImpl().findObject(wg)));
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        final AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(toolbar, "orientation");

        if (orientation.value!=null && orientation.value == EOrientation.Horizontal) {
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

        final AdsDefinitions<AdsRwtWidgetDef> widgets = toolbar.getWidgets();
        widgets.add((AdsRwtWidgetDef) node);
        return node;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        node.delete();
        wg.revalidate();
        return node;
    }

    public static List<BaseWidget> getWidgets(AdsRwtWidgetDef node, GraphScene scene) {
        final List<BaseWidget> items = new ArrayList<>();

        final AdsUIProperty.IdListProperty actionsProp = (AdsUIProperty.IdListProperty) AdsUIUtil.getUiProperty(node, "actions");
        if (actionsProp != null) {

            for (final Id id : actionsProp.getIds()) {
                final AdsRwtWidgetDef action = RwtActionItem.findAction(node.getModule(), id);

                if (action != null) {
                    items.add((BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(action)));
                }
            }
        }

        for (final AdsRwtWidgetDef child : node.getWidgets()) {
            items.add((BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(child)));
        }

        return items;
    }
}
