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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef.Widgets;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ValEditorLayout;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ValEditorLayoutProcessor.Align;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;

public class CustomItem extends Item {

    public static final CustomItem DEFAULT = new CustomItem();
    private AdsAbstractUIDef ui;

    public CustomItem() {
        super(null, null, null, (String) null);
        this.ui = null;
    }

    public CustomItem(AdsAbstractUIDef ui) {
        super(ui.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER ? Group.GROUP_CUSTOM_WIDGETS : Group.GROUP_WEB_CUSTOM_WIDGETS, ui.getName(), ui.getIcon(), ui.getId().toString());
        this.ui = ui;
    }

    public AdsAbstractUIDef getUI() {
        return ui;
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsUIItemDef widget = (AdsUIItemDef) super.createObjectUI(context);
       
        return widget;

    }

    public static AdsUIItemDef getWidgetByRef(AdsUIItemDef widget) {
        if (AdsUIUtil.isCustomWidget(widget)) {
            AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(widget);
            if (customUI != null) {
                return customUI.getWidget();
            }
        }
        return null;
    }

    private Rectangle calcClip(Rectangle parentClip, Rectangle rect) {
        return parentClip.intersection(rect);
    }

    private void paintWidget(Graphics2D gr, Rectangle rect, AdsUIItemDef widget, Rectangle parentClip) {
        if (AdsUIUtil.isCustomWidget(widget)) {
            widget = getWidgetByRef(widget);
            if (widget == null) {
                paintEmpty(gr, rect);
                return;
            }
        }

        Item item = null;
        String className = null;
        if (widget instanceof AdsWidgetDef) {
            className = ((AdsWidgetDef) widget).getClassName();
        } else if (widget instanceof AdsRwtWidgetDef) {
            className = ((AdsRwtWidgetDef) widget).getClassName();
        }
        Rectangle clipRect = calcClip(parentClip, new Rectangle(rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2));
        if (!AdsMetaInfo.WIDGET_CLASS.equals(className)) {
            item = Item.getItem(widget);
            if (item != null && item != CustomItem.DEFAULT) {
                Shape clip = gr.getClip();
                gr.clipRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
                item.paintBackground(gr, rect, widget);
                item.paintBorder(gr, rect, widget);
                gr.clipRect(rect.x, rect.y, rect.width, rect.height);
                item.paintWidget(gr, rect, widget);
                gr.setClip(clip);
            } else {
                paintEmpty(gr, rect);
                return;
            }
        }

        AdsUIItemDef currentWidget = AdsUIUtil.currentWidget(widget);

        AdsLayout layout = currentWidget instanceof AdsWidgetDef ? ((AdsWidgetDef) currentWidget).getLayout() : null;

        if (layout != null) {
            rect = (item != null) ? item.adjustLayoutGeometry(widget, rect) : adjustLayoutGeometry(widget, rect);
            paintLayout(gr, rect, layout, clipRect);
        } else {
            if (currentWidget instanceof AdsWidgetDef) {
                if (AdsUIUtil.isValEditr(currentWidget)) {
                    Align align = AdsMetaInfo.VAL_BOOL_EDITOR_CLASS.equals(((AdsWidgetDef) currentWidget).getClassName())
                            ? Align.LEFT : Align.RIGHT;

                    List<Rectangle> childGeometries = ValEditorLayout.getChildGeometries((AdsWidgetDef) currentWidget, rect, align);
                    Widgets widgets = ((AdsWidgetDef) currentWidget).getWidgets();
                    for (int i = 0; i < widgets.size(); ++i) {
                        AdsWidgetDef w = widgets.get(i);
                        paintWidget(gr, childGeometries.get(i), w, clipRect);
                    }
                } else if (AdsMetaInfo.SPLITTER_CLASS.equals(((AdsWidgetDef) currentWidget).getClassName()) || AdsMetaInfo.ADVANCED_SPLITTER_CLASS.equals(((AdsWidgetDef) currentWidget).getClassName())) {
                    Rectangle[] justifySplitterLayout = LayoutUtil.justifySplitterLayout((AdsWidgetDef) currentWidget, rect);
                    Widgets widgets = ((AdsWidgetDef) currentWidget).getWidgets();
                    for (int i = 0; i < widgets.size(); ++i) {
                        AdsWidgetDef w = widgets.get(i);
                        paintWidget(gr, justifySplitterLayout[i], w, clipRect);
                    }
                } else {
                    for (AdsWidgetDef w : ((AdsWidgetDef) currentWidget).getWidgets()) {
                        AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(w, "geometry");
                        paintWidget(gr, new Rectangle(rect.x + geometry.x, rect.y + geometry.y, geometry.width, geometry.height), w, clipRect);
                    }
                }
            } else {
                AdsRwtWidgetDef rwt = (AdsRwtWidgetDef) currentWidget;

                for (AdsRwtWidgetDef child : rwt.getWidgets()) {
                    AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(child, "geometry");
                    paintWidget(gr, new Rectangle(rect.x + geometry.x, rect.y + geometry.y, geometry.width, geometry.height), child, clipRect);
                }
            }
        }
    }

    private void paintLayout(Graphics2D gr, Rectangle r, AdsLayout layout, Rectangle parentClip) {
        String clazz = layout.getClassName();

        Rectangle clipRect = calcClip(parentClip, r);

        if (AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS.equals(clazz) || AdsMetaInfo.VERTICAL_LAYOUT_CLASS.equals(clazz)) {
            Rectangle[] ir = null;
            if (AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS.equals(clazz)) {
                ir = LayoutUtil.justifyHorizontalLayout(layout, r);
            } else {
                ir = LayoutUtil.justifyVerticalLayout(layout, r);
            }

            if (ir == null) {
                return;  // empty
            }
            AdsLayout.Items items = layout.getItems();
            for (int idx = 0; idx < ir.length; idx++) {
                RadixObject object = AdsUIUtil.getItemNode(items.get(idx));
                assert object != null;

                if (object instanceof AdsWidgetDef) {
                    paintWidget(gr, ir[idx], (AdsWidgetDef) object, clipRect);
                }

                if (object instanceof AdsLayout) {
                    Item item = Item.getItem(object);
                    paintLayout(gr, item.adjustLayoutGeometry(object, ir[idx]), (AdsLayout) object, clipRect);
                }

                if (object instanceof AdsLayout.SpacerItem) {
                    paintSpacer(gr, ir[idx], (AdsLayout.SpacerItem) object);
                }
            }
        }

        if (AdsMetaInfo.GRID_LAYOUT_CLASS.equals(clazz)) {
            Rectangle[][] ir = LayoutUtil.justifyGridLayout(layout, r);
            if (ir == null) // empty
            {
                return;
            }

            AdsLayout.Item[][] items = layout.getItemsAsArray();
            int rows = items.length;
            int cols = items[0].length;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (items[i][j] == null) {
                        continue;
                    }

                    RadixObject object = AdsUIUtil.getItemNode(items[i][j]);
                    assert object != null;

                    if (object instanceof AdsWidgetDef) {
                        paintWidget(gr, ir[i][j], (AdsWidgetDef) object, clipRect);
                    }

                    if (object instanceof AdsLayout) {
                        Item item = Item.getItem(object);
                        paintLayout(gr, item.adjustLayoutGeometry(object, ir[i][j]), (AdsLayout) object, clipRect);
                    }

                    if (object instanceof AdsLayout.SpacerItem) {
                        paintSpacer(gr, ir[i][j], (AdsLayout.SpacerItem) object);
                    }
                }
            }
        }
    }

    private void paintSpacer(Graphics2D gr, Rectangle r, AdsLayout.SpacerItem spacer) {
        //gr.setClip(r);
        //Item item = Item.getItem(spacer);
        //item.paintBackground(gr, r, spacer);
        //item.paint(gr, r, spacer);
    }

    private void paintEmpty(Graphics2D gr, Rectangle r) {
        DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_MID_LIGHT, 1, DrawUtil.COLOR_WINDOW);
        DrawUtil.drawText(gr, r, EAlignment.AlignHCenter, EAlignment.AlignVCenter, true, "<Undefined>");
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIItemDef def = (AdsUIItemDef) node;
        paintWidget(gr, r, def, r);
    }
}
