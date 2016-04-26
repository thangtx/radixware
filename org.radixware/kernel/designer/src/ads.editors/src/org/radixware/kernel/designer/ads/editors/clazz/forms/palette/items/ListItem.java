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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Items;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.EViewMode;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawItemWidgets;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class ListItem extends Item {

    public static final ListItem DEFAULT = new ListItem();

    private int item_height = 20;
    private int item_width = 0;

    public ListItem() {
        super(Group.GROUP_ITEM_WIDGETS, NbBundle.getMessage(ListItem.class, "List"), AdsMetaInfo.LIST_WIDGET_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawShadePanel(gr, r, false, 1, DrawUtil.COLOR_BASE);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.IntProperty spacing_prop = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "spacing");
        int spacing = 0;
        if (spacing_prop != null) {
            spacing = spacing_prop.value;
        }

        boolean canCatText = false;
        boolean isGrid = false;
        AdsUIProperty.SizeProperty gridSize_prop = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "gridSize");
        if ((gridSize_prop != null) && ((gridSize_prop.width > 0) || (gridSize_prop.height > 0))) {
            item_width = gridSize_prop.width;
            item_height = gridSize_prop.height;
            spacing = 0;
            canCatText = true;
            isGrid = true;
        } else {
            item_width = 0;
            item_height = 20;
        }
        drawItems(gr, r, spacing, isGrid, canCatText, node);
    }

    private void drawItems(Graphics2D gr, Rectangle r, int spacing, boolean isGrid, boolean canCatText, RadixObject node) {
        AdsItemWidgetDef def = (AdsItemWidgetDef) node;
        Items items = def.getItems();
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        AdsUIProperty.EnumValueProperty viewMode_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "viewMode");
        EViewMode viewMode = (EViewMode) viewMode_prop.value;
        AdsUIProperty.IntProperty currentRow = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "currentRow");
        int curItem = currentRow.value;

        Point item_pos = new Point(r.x + DrawItemWidgets.INDENT, r.y + DrawItemWidgets.INDENT + spacing);
        int maxHeight = 0;
        for (int i = 0, size = items.size(); i < size; i++) {
            RadixObject node_item = (RadixObject) items.get(i);

            AdsUIProperty.LocalizedStringRefProperty text_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node_item, "text");
            String label = getTextById(node_item, text_prop.getStringId());

            Dimension textSize = getTextItemSize(gr, label, node_item, viewMode);

            //считаем размер элемента
            Rectangle itemRect = r.getBounds();
            if (EViewMode.ListMode == viewMode) {
                itemRect.height = item_height;
                itemRect.y = item_pos.y + i * (item_height + spacing);
                itemRect.x = item_pos.x + spacing;
                itemRect.width = itemRect.width - DrawItemWidgets.INDENT * 2 - spacing;
            } else {
                int oldPosY = item_pos.y;
                item_pos = setItemRectForIconMode(itemRect, spacing, item_pos.x, item_pos.y, maxHeight, textSize);
                if (item_pos.y == oldPosY) {
                    maxHeight = Math.max(itemRect.height, maxHeight);
                } else {
                    maxHeight = itemRect.height;
                }
            }
            //рисуем элемент
            if (curItem == i) {
                drawSelectedItemBackground(gr, itemRect, textSize, viewMode, isGrid);
            }
            DrawItemWidgets.drawText(gr, itemRect, node_item, enabled.value, canCatText, viewMode, isGrid);
        }
    }

    private void drawSelectedItemBackground(Graphics2D gr, Rectangle itemRect, Dimension textSize, EViewMode viewMode, boolean isGrid) {
        Rectangle selectedRect = itemRect.getBounds();
        if (isGrid) {
            selectedRect.height = Math.max(textSize.height + DrawItemWidgets.INDENT * 2, 20);
            if (EViewMode.ListMode == viewMode) {
                selectedRect.y = itemRect.y + (itemRect.height - selectedRect.height) / 2;
            } else {
                selectedRect.width = textSize.width;
                selectedRect.x = itemRect.x + (itemRect.width - selectedRect.width) / 2;
            }
        }
        DrawUtil.drawPlainRect(gr, selectedRect, new Color(255, 177, 108), 1, new Color(255, 177, 108));
    }

    private Point setItemRectForIconMode(Rectangle itemRect, int spacing, int pos_x, int pos_y, int maxHeight, Dimension textSize) {
        int width = itemRect.width;
        itemRect.height = item_height;

        int label_width = item_width;
        int label_height = item_height;
        if (label_width == 0) {
            label_width = textSize.width + DrawItemWidgets.INDENT;
            label_height = textSize.height + DrawItemWidgets.INDENT;
        } else {
            label_height = item_height + DrawItemWidgets.INDENT;
            label_width = item_width + DrawItemWidgets.INDENT;
        }
        itemRect.width = label_width;
        itemRect.height = label_height;

        if ((width - pos_x > label_width + spacing) || (pos_x == DrawItemWidgets.INDENT)) {
            itemRect.x = itemRect.x + pos_x + spacing;
            itemRect.y = pos_y;
            pos_x = itemRect.x + label_width;
        } else {
            itemRect.x = itemRect.x + spacing + DrawItemWidgets.INDENT;
            itemRect.y = pos_y + maxHeight/*Math.max(itemRect.height,maxHeight)*/ + spacing;//pos_y+itemRect.height + spacing;
            pos_x = itemRect.x + label_width;
            pos_y = itemRect.y;
        }
        return new Point(pos_x, pos_y);
    }

    private Dimension getTextItemSize(Graphics2D gr, String label, RadixObject node_item, EViewMode viewMode) {
        FontMetrics fm = gr.getFontMetrics(DrawUtil.DEFAULT_FONT);
        int label_width = fm.stringWidth(label);
        int label_height = fm.getHeight();

        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node_item, "iconSize");
        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(node_item, "icon");
        RadixIcon item_image = getIconById(node_item, icon.getImageId());

        if (item_image != null) {
            int icon_width = 16;
            if (iconSize != null) {
                icon_width = iconSize.width;
            }
            if (viewMode == EViewMode.ListMode) {
                label_width = label_width + icon_width + DrawItemWidgets.ICON_TEXT_SPACING;
            } else {
                label_height = label_height + icon_width + DrawItemWidgets.ICON_TEXT_SPACING;
            }
        }
        Dimension size = new Dimension(label_width, label_height);
        return size;
    }

}
