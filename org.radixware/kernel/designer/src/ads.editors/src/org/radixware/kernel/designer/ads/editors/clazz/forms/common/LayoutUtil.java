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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.EnumValueProperty;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.enums.ELayoutDirection;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.ESizePolicy;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.CustomItem;


public class LayoutUtil {

    final static public int MAX_WIDTH = 16777215;
    final static public int MAX_HEIGHT = 16777215;
    final static public int MIN_WIDTH = 2;
    final static public int MIN_HEIGHT = 2;
    final static public int MIN_MARGIN = 2;
    final static public int MIN_SPACING = 1;
    final static private int ADJUST_DISTANCE = 6;

    public static Rectangle adjustSelection(Rectangle ir, Rectangle r) {
        Rectangle rc = ir.getBounds();

        final int dx = Math.min(ADJUST_DISTANCE, r.width / 2);
        final int dy = Math.min(ADJUST_DISTANCE, r.height / 2);

        int newX = Math.max(rc.x, r.x + dx);
        rc.width -= (newX - rc.x);
        rc.x = newX;

        int newY = Math.max(rc.y, r.y + dy);
        rc.height -= (newY - rc.y);
        rc.y = newY;

        int newWidth = Math.min(rc.width, r.width - (rc.x - r.x) - dx);
        rc.width = newWidth;

        int newHeight = Math.min(rc.height, r.height - (rc.y - r.y) - dy);
        rc.height = newHeight;

        return rc;
    }

    public static Dimension getHintSize(RadixObject node) {
        Item it = Item.getItem(node);
        if (node instanceof AdsUIActionDef) {
            AdsUIProperty.RectProperty rp = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
            assert rp != null : "geometry cann't be null";
            Dimension sz = new Dimension(rp.width, rp.height);
            return it != null ? it.adjustHintSize(node, sz) : sz;
        } else if (node instanceof AdsRwtWidgetDef) {
            AdsUIProperty.RectProperty rp = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
            assert rp != null : "geometry cann't be null";
            Dimension sz = new Dimension(rp.width, rp.height);
            return it != null ? it.adjustHintSize(node, sz) : sz;
        }
        if (node instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) node;

            if (AdsUIUtil.isCustomWidget(node)) {
                AdsUIItemDef w = CustomItem.getWidgetByRef(widget);
                if (w instanceof AdsWidgetDef) {
                    widget = (AdsWidgetDef) w;
                }
            }

            if (widget.getLayout() != null) {
                return it.adjustHintSize(node, getHintSize(widget.getLayout()));
            }

            AdsUIProperty.RectProperty rp = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
            assert rp != null : "geometry cann't be null";
            Dimension sz = new Dimension(rp.width, rp.height);
            return it != null ? it.adjustHintSize(node, sz) : sz;
        }
        if (node instanceof AdsLayout.SpacerItem) {

            AdsUIProperty.SizeProperty sp = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "sizeHint");
            assert sp != null : "size cann't be null";
            return it.adjustHintSize(node, new Dimension(sp.width, sp.height));
        }

        assert node instanceof AdsLayout;
        AdsLayout layout = (AdsLayout) node;

        int left = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutLeftMargin")).value;
        int top = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutTopMargin")).value;
        int right = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutRightMargin")).value;
        int bottom = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutBottomMargin")).value;

        left = Math.max(left, LayoutUtil.MIN_MARGIN);
        top = Math.max(top, LayoutUtil.MIN_MARGIN);
        right = Math.max(right, LayoutUtil.MIN_MARGIN);
        bottom = Math.max(bottom, LayoutUtil.MIN_MARGIN);

        int width = 0, height = 0;
        switch (layout.getClassName()) {
            case AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS:
                {
                    for (AdsLayout.Item item : layout.getItems()) {
                        Dimension sz = getHintSize(AdsUIUtil.getItemNode(item));
                        width += sz.width;
                        height = Math.max(height, sz.height);
                    }
                    int c = layout.getItems().size();
                    if (c > 0) {
                        int spacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutSpacing")).value;
                        spacing = Math.max(spacing, MIN_SPACING);
                        width += (c - 1) * spacing;
                    }
                    break;
                }
            case AdsMetaInfo.VERTICAL_LAYOUT_CLASS:
                {
                    for (AdsLayout.Item item : layout.getItems()) {
                        Dimension sz = getHintSize(AdsUIUtil.getItemNode(item));
                        height += sz.height;
                        width = Math.max(width, sz.width);
                    }
                    int c = layout.getItems().size();
                    if (c > 0) {
                        int spacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutSpacing")).value;
                        spacing = Math.max(spacing, MIN_SPACING);
                        height += (c - 1) * spacing;
                    }
                    break;
                }
            case AdsMetaInfo.GRID_LAYOUT_CLASS:
                AdsLayout.Item[][] items = layout.getItemsAsArray();
                if (items.length > 0) {
                    for (int i = 0; i < items.length; i++) {
                        int h = 0;
                        for (int j = 0; j < items[i].length; j++) {
                            if (items[i][j] != null) {
                                h = Math.max(h, getHintSize(AdsUIUtil.getItemNode(items[i][j])).height);
                            }
                        }
                        height += h;
                    }
                    for (int j = 0; j < items[0].length; j++) {
                        int w = 0;
                        for (int i = 0; i < items.length; i++) {
                            if (items[i][j] != null) {
                                w = Math.max(w, getHintSize(AdsUIUtil.getItemNode(items[i][j])).width);
                            }
                        }
                        width += w;
                    }

                    int hspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutHorizontalSpacing")).value;
                    hspacing = Math.max(hspacing, MIN_SPACING);
                    width += (items[0].length - 1) * hspacing;

                    int vspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutVerticalSpacing")).value;
                    vspacing = Math.max(vspacing, MIN_SPACING);
                    height += (items.length - 1) * vspacing;
                }
                break;
        }
        return it.adjustHintSize(node, new Dimension(Math.max(left + width + right, MIN_WIDTH), Math.max(top + height + bottom, MIN_HEIGHT)));
    }

    public static ESizePolicy getHSizePolicy(RadixObject node) {
        if (node instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) node;

            if (AdsUIUtil.isCustomWidget(widget)) {
                AdsUIItemDef w = CustomItem.getWidgetByRef(widget);
                if (w instanceof AdsWidgetDef) {
                    w = widget;
                }
            }

            AdsUIProperty.SizePolicyProperty sp = (AdsUIProperty.SizePolicyProperty) AdsUIUtil.getUiProperty(node, "sizePolicy");
            assert sp != null : "size policy cann't be null";
            return sp.hSizeType;
        }
        if (node instanceof AdsLayout.SpacerItem) {
            AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "orientation");
            assert orientation != null : "orientation cann't be null";
            AdsUIProperty.EnumValueProperty sp = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "sizeType");
            assert sp != null : "size policy cann't be null";
            return EOrientation.Horizontal.equals(orientation.value)
                    ? (ESizePolicy) sp.value : ESizePolicy.Preferred;
        }

        assert node instanceof AdsLayout;
        AdsLayout layout = (AdsLayout) node;

        ESizePolicy hsp = ESizePolicy.Minimum;
        switch (layout.getClassName()) {
            case AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS:
                for (AdsLayout.Item item : layout.getItems()) {
                    hsp = ESizePolicy.max(hsp, getHSizePolicy(AdsUIUtil.getItemNode(item)));
                }
                break;
            case AdsMetaInfo.VERTICAL_LAYOUT_CLASS:
                for (AdsLayout.Item item : layout.getItems()) {
                    hsp = ESizePolicy.max(hsp, getHSizePolicy(AdsUIUtil.getItemNode(item)));
                }
                break;
            case AdsMetaInfo.GRID_LAYOUT_CLASS:
                AdsLayout.Item[][] items = layout.getItemsAsArray();
                if (items.length > 0) {
                    ESizePolicy sp[] = new ESizePolicy[items[0].length];
                    for (int j = 0; j < items[0].length; j++) {
                        sp[j] = ESizePolicy.Minimum;
                        for (int i = 0; i < items.length; i++) {
                            if (items[i][j] != null) {
                                sp[j] = ESizePolicy.grid_max(sp[j], getHSizePolicy(AdsUIUtil.getItemNode(items[i][j])));
                            }
                        }
                    }
                    for (int i = 0; i < sp.length; i++) {
                        hsp = ESizePolicy.max(hsp, sp[i]);
                    }
                }
                break;
        }
        return hsp;
    }

    public static ESizePolicy getVSizePolicy(RadixObject node) {
        if (node instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) node;

            if (AdsUIUtil.isCustomWidget(widget)) {
                AdsUIItemDef w = CustomItem.getWidgetByRef(widget);
                if (w instanceof AdsWidgetDef) {
                    w = widget;
                }
            }

            AdsUIProperty.SizePolicyProperty sp = (AdsUIProperty.SizePolicyProperty) AdsUIUtil.getUiProperty(node, "sizePolicy");
            assert sp != null : "size policy cann't be null";
            return sp.vSizeType;
        }
        if (node instanceof AdsLayout.SpacerItem) {
            AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "orientation");
            assert orientation != null : "orientation cann't be null";
            AdsUIProperty.EnumValueProperty sp = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "sizeType");
            assert sp != null : "size policy cann't be null";
            return EOrientation.Vertical.equals(orientation.value)
                    ? (ESizePolicy) sp.value : ESizePolicy.Preferred;
        }

        assert node instanceof AdsLayout;
        AdsLayout layout = (AdsLayout) node;

        ESizePolicy vsp = ESizePolicy.Minimum;
        switch (layout.getClassName()) {
            case AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS:
                for (AdsLayout.Item item : layout.getItems()) {
                    vsp = ESizePolicy.max(vsp, getVSizePolicy(AdsUIUtil.getItemNode(item)));
                }
                break;
            case AdsMetaInfo.VERTICAL_LAYOUT_CLASS:
                for (AdsLayout.Item item : layout.getItems()) {
                    vsp = ESizePolicy.max(vsp, getVSizePolicy(AdsUIUtil.getItemNode(item)));
                }
                break;
            case AdsMetaInfo.GRID_LAYOUT_CLASS:
                AdsLayout.Item[][] items = layout.getItemsAsArray();
                if (items.length > 0) {
                    ESizePolicy sp[] = new ESizePolicy[items.length];
                    for (int i = 0; i < items.length; i++) {
                        sp[i] = ESizePolicy.Minimum;
                        for (int j = 0; j < items[i].length; j++) {
                            if (items[i][j] != null) {
                                sp[i] = ESizePolicy.grid_max(sp[i], getHSizePolicy(AdsUIUtil.getItemNode(items[i][j])));
                            }
                        }
                    }
                    for (int i = 0; i < sp.length; i++) {
                        vsp = ESizePolicy.max(vsp, sp[i]);
                    }
                }
                break;
        }
        return vsp;
    }

    public static ELayoutDirection getLayoutDirection(RadixObject node) {
        ELayoutDirection dr = ELayoutDirection.LeftToRight;
        AdsUIProperty.EnumValueProperty pdr = (EnumValueProperty) AdsUIUtil.getUiProperty(node, "layoutDirection");
        if (pdr != null) {
            dr = (ELayoutDirection) pdr.value;
        }
        return dr;
    }

// grid layout
    private static ESizePolicy max(ESizePolicy[] spp) {
        ESizePolicy sp = spp[0];
        for (int i = 1; i < spp.length; i++) {
            sp = ESizePolicy.max(sp, spp[i]);
        }
        return sp;
    }

    static void calcGridWidth(
            final AdsLayout layout,
            final Rectangle r,
            int minWidth[],
            int maxWidth[],
            int hintWidth[],
            double itemWidth[],
            ESizePolicy hsp[]) {

        AdsLayout.Item[][] items = layout.getItemsAsArray();
        int rows = items.length;
        int cols = items[0].length;

        int hspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutHorizontalSpacing")).value;
        hspacing = Math.max(hspacing, MIN_SPACING);

        for (int j = 0; j < cols; j++) {
            hintWidth[j] = 0;
            hsp[j] = ESizePolicy.Minimum;
            for (int i = 0; i < rows; i++) {
                if (items[i][j] != null) {
                    hintWidth[j] = Math.max(hintWidth[j], LayoutUtil.getHintSize(AdsUIUtil.getItemNode(items[i][j])).width);
                    hsp[j] = ESizePolicy.grid_max(hsp[j], LayoutUtil.getHSizePolicy(AdsUIUtil.getItemNode(items[i][j])));
                }
            }

            switch (hsp[j]) {
                case Fixed:
                    minWidth[j] = hintWidth[j];
                    maxWidth[j] = hintWidth[j];
                    break;
                case Minimum:
                    minWidth[j] = hintWidth[j];
                    maxWidth[j] = LayoutUtil.MAX_WIDTH;
                    break;
                case Maximum:
                    minWidth[j] = LayoutUtil.MIN_WIDTH;//0;
                    maxWidth[j] = hintWidth[j];
                    break;
                case Preferred:
                    minWidth[j] = hintWidth[j];//LayoutUtil.MIN_WIDTH;//0;
                    maxWidth[j] = LayoutUtil.MAX_WIDTH;
                    break;
                case MinimumExpanding:
                    minWidth[j] = hintWidth[j];
                    maxWidth[j] = LayoutUtil.MAX_WIDTH;
                    break;
                case Expanding:
                    minWidth[j] = hintWidth[j];
                    maxWidth[j] = LayoutUtil.MAX_WIDTH;
                    break;
                case Ignored:
                    minWidth[j] = LayoutUtil.MIN_WIDTH;//0;
                    maxWidth[j] = LayoutUtil.MAX_WIDTH;
                    break;
            }

        }

        ESizePolicy hsp_max = max(hsp);

        if (hsp_max.equals(ESizePolicy.MinimumExpanding)
                || hsp_max.equals(ESizePolicy.Expanding)
                || hsp_max.equals(ESizePolicy.Ignored)) {

            List<Integer> exp = new ArrayList<>();

            double simpleWidth = 0;
            double expandedMinWidth = 0;
            for (int i = 0; i < cols; i++) {
                itemWidth[i] = minWidth[i];
                if (hsp[i].equals(ESizePolicy.Expanding) || hsp[i].equals(ESizePolicy.MinimumExpanding) || hsp[i].equals(ESizePolicy.Ignored)) {
                    expandedMinWidth += itemWidth[i];
                    exp.add(i);
                } else {
                    simpleWidth += itemWidth[i];
                }
            }

            if (simpleWidth + expandedMinWidth > r.width - hspacing * (cols - 1)) {
                double scale = (double) (r.width - hspacing * (cols - 1)) / (simpleWidth + expandedMinWidth);
                for (int i = 0; i < cols; i++) {
                    itemWidth[i] = itemWidth[i] * scale;
                }
            } else {
                List<Integer> minExp = new ArrayList<>();
                List<Integer> avrExp = new ArrayList<>();

                double expWidth = r.width - simpleWidth - hspacing * (cols - 1);
                double avrWidth = expWidth / exp.size();

                for (int i : exp) {
                    int min = minWidth[i];
                    if (min > avrWidth) {
                        minExp.add(i);
                        expWidth -= min;
                    } else {
                        avrExp.add(i);
                    }
                }

                if (avrExp.size() > 0) {
                    avrWidth = expWidth / avrExp.size();
                    for (int i : avrExp) {
                        itemWidth[i] = avrWidth;
                    }
                }
            }
        } else {
            double simpleWidth = 0;
            double growableMinWidth = 0;

            List<Integer> grow = new ArrayList<>();

            for (int i = 0; i < cols; i++) {
                int max = maxWidth[i];
                if (max != LayoutUtil.MAX_WIDTH) {
                    itemWidth[i] = max;
                    simpleWidth += max;
                } else {
                    itemWidth[i] = minWidth[i];
                    growableMinWidth += minWidth[i];
                    grow.add(i);
                }
            }

            if (simpleWidth + growableMinWidth > r.width - hspacing * (cols - 1)) {
                double scale = (double) (r.width - hspacing * (cols - 1)) / (simpleWidth + growableMinWidth);
                for (int i = 0; i < cols; i++) {
                    itemWidth[i] = itemWidth[i] * scale;
                }
            } else if (grow.size() > 0) {
                List<Integer> minGrow = new ArrayList<>();
                List<Integer> avrGrow = new ArrayList<>();

                double growWidth = r.width - simpleWidth - hspacing * (cols - 1);
                double avrWidth = growWidth / grow.size();

                for (int i : grow) {
                    int min = minWidth[i];
                    if (min > avrWidth) {
                        minGrow.add(i);
                        growWidth -= min;
                    } else {
                        avrGrow.add(i);
                    }
                }

                if (avrGrow.size() > 0) {
                    avrWidth = growWidth / avrGrow.size();
                    for (int i : avrGrow) {
                        itemWidth[i] = avrWidth;
                    }
                }
            }
        }
    }

    static void calcGridHeight(
            final AdsLayout layout,
            final Rectangle r,
            int minHeight[],
            int maxHeight[],
            int hintHeight[],
            double itemHeight[],
            ESizePolicy vsp[]) {

        AdsLayout.Item[][] items = layout.getItemsAsArray();
        int rows = items.length;
        int cols = items[0].length;

        int vspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutVerticalSpacing")).value;
        vspacing = Math.max(vspacing, MIN_SPACING);

        for (int i = 0; i < rows; i++) {
            hintHeight[i] = 0;
            vsp[i] = ESizePolicy.Minimum;
            for (int j = 0; j < cols; j++) {
                if (items[i][j] != null) {
                    hintHeight[i] = Math.max(hintHeight[i], LayoutUtil.getHintSize(AdsUIUtil.getItemNode(items[i][j])).height);
                    vsp[i] = ESizePolicy.grid_max(vsp[i], LayoutUtil.getVSizePolicy(AdsUIUtil.getItemNode(items[i][j])));
                }
            }

            switch (vsp[i]) {
                case Fixed:
                    minHeight[i] = hintHeight[i];
                    maxHeight[i] = hintHeight[i];
                    break;
                case Minimum:
                    minHeight[i] = hintHeight[i];
                    maxHeight[i] = LayoutUtil.MAX_HEIGHT;
                    break;
                case Maximum:
                    minHeight[i] = LayoutUtil.MIN_HEIGHT;//0;
                    maxHeight[i] = hintHeight[i];
                    break;
                case Preferred:
                    minHeight[i] = hintHeight[i];//LayoutUtil.MIN_HEIGHT;//0;
                    maxHeight[i] = LayoutUtil.MAX_HEIGHT;
                    break;
                case MinimumExpanding:
                    minHeight[i] = hintHeight[i];
                    maxHeight[i] = LayoutUtil.MAX_HEIGHT;
                    break;
                case Expanding:
                    minHeight[i] = hintHeight[i];
                    maxHeight[i] = LayoutUtil.MAX_HEIGHT;
                    break;
                case Ignored:
                    minHeight[i] = LayoutUtil.MIN_HEIGHT;//0;
                    maxHeight[i] = LayoutUtil.MAX_HEIGHT;
                    break;
            }

        }

        ESizePolicy vsp_max = max(vsp);

        if (vsp_max.equals(ESizePolicy.MinimumExpanding)
                || vsp_max.equals(ESizePolicy.Expanding)
                || vsp_max.equals(ESizePolicy.Ignored)) {

            List<Integer> exp = new ArrayList<>();

            double simpleHeight = 0;
            double expandedMinHeight = 0;
            for (int i = 0; i < rows; i++) {
                itemHeight[i] = minHeight[i];
                if (vsp[i].equals(ESizePolicy.Expanding) || vsp[i].equals(ESizePolicy.MinimumExpanding) || vsp[i].equals(ESizePolicy.Ignored)) {
                    expandedMinHeight += itemHeight[i];
                    exp.add(i);
                } else {
                    simpleHeight += itemHeight[i];
                }
            }

            if (simpleHeight + expandedMinHeight > r.height - vspacing * (rows - 1)) {
                double scale = (double) (r.height - vspacing * (rows - 1)) / (simpleHeight + expandedMinHeight);
                for (int i = 0; i < rows; i++) {
                    itemHeight[i] = itemHeight[i] * scale;
                }
            } else {
                List<Integer> minExp = new ArrayList<>();
                List<Integer> avrExp = new ArrayList<>();

                double expHeight = r.height - simpleHeight - vspacing * (rows - 1);
                double avrHeight = expHeight / exp.size();

                for (int i : exp) {
                    int min = minHeight[i];
                    if (min > avrHeight) {
                        minExp.add(i);
                        expHeight -= min;
                    } else {
                        avrExp.add(i);
                    }
                }

                if (avrExp.size() > 0) {
                    avrHeight = expHeight / avrExp.size();
                    for (int i : avrExp) {
                        itemHeight[i] = avrHeight;
                    }
                }
            }
        } else {
            double simpleHeight = 0;
            double growableMinHeight = 0;

            List<Integer> grow = new ArrayList<>();

            for (int i = 0; i < rows; i++) {
                int max = maxHeight[i];
                if (max != LayoutUtil.MAX_HEIGHT) {
                    itemHeight[i] = max;
                    simpleHeight += max;
                } else {
                    itemHeight[i] = minHeight[i];
                    growableMinHeight += minHeight[i];
                    grow.add(i);
                }
            }

            if (simpleHeight + growableMinHeight > r.height - vspacing * (rows - 1)) {
                double scale = (double) (r.height - vspacing * (rows - 1)) / (simpleHeight + growableMinHeight);
                for (int i = 0; i < rows; i++) {
                    itemHeight[i] = itemHeight[i] * scale;
                }
            } else if (grow.size() > 0) {
                List<Integer> minGrow = new ArrayList<>();
                List<Integer> avrGrow = new ArrayList<>();

                double growHeight = r.height - simpleHeight - vspacing * (rows - 1);
                double avrHeight = growHeight / grow.size();

                for (int i : grow) {
                    int min = minHeight[i];
                    if (min > avrHeight) {
                        minGrow.add(i);
                        growHeight -= min;
                    } else {
                        avrGrow.add(i);
                    }
                }

                if (avrGrow.size() > 0) {
                    avrHeight = growHeight / avrGrow.size();
                    for (int i : avrGrow) {
                        itemHeight[i] = avrHeight;
                    }
                }
            }
        }
    }

    public static Rectangle[][] justifyGridLayout(AdsLayout layout, Rectangle r) {
        assert layout != null : "layout cann't be null";
        if (layout.getItems().size() == 0) {
            return null;
        }

        int hspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutHorizontalSpacing")).value;
        hspacing = Math.max(hspacing, MIN_SPACING);

        int vspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutVerticalSpacing")).value;
        vspacing = Math.max(vspacing, MIN_SPACING);

        layout.adjustItems(); // ???

        AdsLayout.Item[][] items = layout.getItemsAsArray();
        int rows = items.length;
        int cols = items[0].length;

        int minHeight[] = new int[rows];
        int maxHeight[] = new int[rows];
        int hintHeight[] = new int[rows];
        double itemHeight[] = new double[rows];
        ESizePolicy vsp[] = new ESizePolicy[rows];
        calcGridHeight(layout, r, minHeight, maxHeight, hintHeight, itemHeight, vsp);

        int minWidth[] = new int[cols];
        int maxWidth[] = new int[cols];
        int hintWidth[] = new int[cols];
        double itemWidth[] = new double[cols];
        ESizePolicy hsp[] = new ESizePolicy[cols];
        calcGridWidth(layout, r, minWidth, maxWidth, hintWidth, itemWidth, hsp);

        Rectangle[][] ir = new Rectangle[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ir[i][j] = new Rectangle();
            }
        }

        double totalHeight = 0;
        for (int i = 0; i < rows; i++) {
            totalHeight += itemHeight[i];
        }

        double vmargin = (r.height - totalHeight - vspacing * (rows - 1)) / (rows * 2);
        double vsep = vspacing + 2 * vmargin;
        double voffs = vmargin;

        for (int i = 0; i < rows; i++) {
            double delta = itemHeight[i] + vsep;
            for (int j = 0; j < cols; j++) {
                if (items[i][j] == null) {
                    continue;
                }
                double height = Math.round(voffs + delta) - Math.round(voffs) - Math.round(vsep);
                ESizePolicy sp = LayoutUtil.getVSizePolicy(AdsUIUtil.getItemNode(items[i][j]));
                if (sp.equals(ESizePolicy.Fixed) || sp.equals(ESizePolicy.Maximum) && items[i][j].columnSpan == 1) {
                    int h = LayoutUtil.getHintSize(AdsUIUtil.getItemNode(items[i][j])).height;
                    if (h < height) {
                        ir[i][j].y = (int) Math.round(r.y + voffs + (height - h) / 2);
                        ir[i][j].height = h;
                        continue;
                    }
                }
                ir[i][j].y = (int) Math.round(r.y + voffs);
                ir[i][j].height = (int) Math.round(height);
            }
            voffs += delta;
        }

        double totalWidth = 0;
        for (int i = 0; i < cols; i++) {
            totalWidth += itemWidth[i];
        }

        double hmargin = (r.width - totalWidth - hspacing * (cols - 1)) / (cols * 2);
        double hsep = hspacing + 2 * hmargin;
        double hoffs = hmargin;

        for (int j = 0; j < cols; j++) {
            double delta = itemWidth[j] + hsep;
            for (int i = 0; i < rows; i++) {
                if (items[i][j] == null) {
                    continue;
                }
                double width = Math.round(hoffs + delta) - Math.round(hoffs) - Math.round(hsep);
                ESizePolicy sp = LayoutUtil.getHSizePolicy(AdsUIUtil.getItemNode(items[i][j]));
                if (sp.equals(ESizePolicy.Fixed) || sp.equals(ESizePolicy.Maximum) && items[i][j].rowSpan == 1) {
                    int w = LayoutUtil.getHintSize(AdsUIUtil.getItemNode(items[i][j])).width;
                    if (w < width) {
                        ELayoutDirection dr = LayoutUtil.getLayoutDirection(AdsUIUtil.getItemNode(items[i][j]));
                        ir[i][j].x = (int) Math.round(r.x + hoffs + (dr.equals(ELayoutDirection.LeftToRight) ? 0 : width - w));
                        ir[i][j].width = w;
                        continue;
                    }
                }
                ir[i][j].x = (int) Math.round(r.x + hoffs);
                ir[i][j].width = (int) Math.round(width);
            }
            hoffs += delta;
        }




        for (AdsLayout.Item item : layout.getItems()) {
            if (item.rowSpan == 1) {
                continue;
            }

            double delta = 0;
            for (int i = 0; i < item.rowSpan; i++) {
                delta += itemHeight[item.row + i] + vsep;
            }

            Rectangle rc = new Rectangle(ir[item.row][item.column]);
            rc.height = (int) (Math.round(delta) - Math.round(vsep));

            ESizePolicy sp = LayoutUtil.getVSizePolicy(AdsUIUtil.getItemNode(item));
            if (sp.equals(ESizePolicy.Fixed) || sp.equals(ESizePolicy.Maximum)) {
                int h = LayoutUtil.getHintSize(AdsUIUtil.getItemNode(item)).height;
                if (h < rc.height) {
                    rc.y = Math.round(rc.y + (rc.height - h) / 2);
                    rc.height = h;
                }
            }

            for (int i = 0; i < item.rowSpan; i++) {
                for (int j = 0; j < item.columnSpan; j++) {
                    ir[item.row + i][item.column + j].y = rc.y;
                    ir[item.row + i][item.column + j].height = rc.height;
                }
            }
        }

        for (AdsLayout.Item item : layout.getItems()) {
            if (item.columnSpan == 1) {
                continue;
            }

            double delta = 0;
            for (int i = 0; i < item.columnSpan; i++) {
                delta += itemWidth[item.column + i] + hsep;
            }

            Rectangle rc = new Rectangle(ir[item.row][item.column]);
            rc.width = (int) (Math.round(delta) - Math.round(hsep));

            ESizePolicy sp = LayoutUtil.getHSizePolicy(AdsUIUtil.getItemNode(item));
            if (sp.equals(ESizePolicy.Fixed) || sp.equals(ESizePolicy.Maximum)) {
                int w = LayoutUtil.getHintSize(AdsUIUtil.getItemNode(item)).width;
                if (w < rc.width) {
                    ELayoutDirection dr = LayoutUtil.getLayoutDirection(AdsUIUtil.getItemNode(item));
                    rc.x = Math.round(rc.x + (dr.equals(ELayoutDirection.LeftToRight) ? 0 : rc.width - w));
                    rc.width = w;
                }
            }

            for (int i = 0; i < item.columnSpan; i++) {
                for (int j = 0; j < item.rowSpan; j++) {
                    ir[item.row + j][item.column + i].x = rc.x;
                    ir[item.row + j][item.column + i].width = rc.width;
                }
            }
        }

        return ir;
    }

    public static int[] getSplitterControlPoints(AdsUIItemDef splitter, Rectangle r) {
        assert splitter != null : "splitter cann't be null";

        final int size;
        Definitions<? extends AdsUIItemDef> widgets;
        if (splitter instanceof AdsWidgetDef) {
            widgets = ((AdsWidgetDef) splitter).getWidgets();
        } else {
            widgets = ((AdsRwtWidgetDef) splitter).getWidgets();
        }

        if ((size = widgets.size()) <= 1) {
            return null;
        }
        AdsUIProperty.IntProperty hw = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(splitter, "handleWidth");

        int handleWidth = hw != null ? hw.value : 5;
        handleWidth = Math.max(handleWidth, MIN_SPACING);

        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(splitter, "orientation");
        Rectangle[] ir = justifySplitterLayout(splitter, r);

        int[] ps = new int[size - 1];
        if (EOrientation.Horizontal.equals(orientation.value)) {
            for (int i = 0; i < size - 1; i++) {
                ps[i] = ir[i].x + ir[i].width + handleWidth / 2;
            }
        } else {
            for (int i = 0; i < size - 1; i++) {
                ps[i] = ir[i].y + ir[i].height + handleWidth / 2;
            }
        }

        return ps;
    }

    public static double getSplitterItemWeight(AdsUIItemDef def) {
        if (def instanceof AdsWidgetDef) {
            return ((AdsWidgetDef) def).getWeight();
        } else {
            return ((AdsRwtWidgetDef) def).getWeight();
        }
    }

    public static void setSplitterItemWeight(AdsUIItemDef def, double weight) {
        if (def instanceof AdsWidgetDef) {
            ((AdsWidgetDef) def).setWeight(weight);
        } else {
            ((AdsRwtWidgetDef) def).setWeight(weight);
        }
    }

    public static Rectangle[] justifySplitterLayout(AdsUIItemDef splitter, Rectangle r) {
        assert splitter != null : "splitter cann't be null";

        final int size;
        Definitions<? extends AdsUIItemDef> widgets;
        if (splitter instanceof AdsWidgetDef) {
            widgets = ((AdsWidgetDef) splitter).getWidgets();
        } else {
            widgets = ((AdsRwtWidgetDef) splitter).getWidgets();
        }
        if ((size = widgets.size()) == 0) {
            return null;
        }

        AdsUIProperty.IntProperty hw = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(splitter, "handleWidth");

        int handleWidth = hw != null ? hw.value : 5;
        handleWidth = Math.max(handleWidth, MIN_SPACING);

        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(splitter, "orientation");

        Rectangle[] ir = new Rectangle[size];
        if (EOrientation.Horizontal.equals(orientation.value)) {
            double itemWidth[] = new double[size];
            for (int i = 0; i < size; i++) {
                itemWidth[i] = (r.width - (size - 1) * handleWidth) * getSplitterItemWeight(widgets.get(i));
            }
            double offs = 0;
            for (int i = 0; i < size; i++) {
                ir[i] = new Rectangle(
                        (int) Math.round(r.x + offs),
                        r.y,
                        (int) Math.round(itemWidth[i]),
                        r.height);

                offs += itemWidth[i] + handleWidth;
            }
        } else {
            double itemHeight[] = new double[size];
            for (int i = 0; i < size; i++) {
                itemHeight[i] = (r.height - (size - 1) * handleWidth) * getSplitterItemWeight(widgets.get(i));
            }
            double offs = 0;
            for (int i = 0; i < size; i++) {
                ir[i] = new Rectangle(
                        r.x,
                        (int) Math.round(r.y + offs),
                        r.width,
                        (int) Math.round(itemHeight[i]));
                offs += itemHeight[i] + handleWidth;
            }
        }

        return ir;
    }

    public static Rectangle[] justifyHorizontalLayout(AdsLayout layout, Rectangle r) {
        assert layout != null : "layout cann't be null";

        final int size;
        AdsLayout.Items items = layout.getItems();
        if ((size = items.size()) == 0) {
            return null;
        }

        int spacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutSpacing")).value;
        spacing = Math.max(spacing, MIN_SPACING);

        Dimension minSize[] = new Dimension[size];
        Dimension maxSize[] = new Dimension[size];
        Dimension hintSize[] = new Dimension[size];

        double itemWidth[] = new double[size];

        List<AdsLayout.Item> exp = new ArrayList<>();
        List<AdsLayout.Item> sim = new ArrayList<>();
        List<AdsLayout.Item> grow = new ArrayList<>();

        for (AdsLayout.Item item : items) {
            int idx = items.indexOf(item);
            RadixObject elem = AdsUIUtil.getItemNode(item);

            ESizePolicy hsp = LayoutUtil.getHSizePolicy(elem);
            ESizePolicy vsp = LayoutUtil.getVSizePolicy(elem);

            hintSize[idx] = LayoutUtil.getHintSize(elem);
            minSize[idx] = new Dimension();
            maxSize[idx] = new Dimension();

            itemWidth[idx] = 0;

            switch (hsp) {
                case Fixed:
                    minSize[idx].width = hintSize[idx].width;
                    maxSize[idx].width = hintSize[idx].width;
                    sim.add(item);
                    break;
                case Minimum:
                    minSize[idx].width = hintSize[idx].width;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    sim.add(item);
                    break;
                case Maximum:
                    minSize[idx].width = hintSize[idx].width;//0
                    maxSize[idx].width = hintSize[idx].width;
                    sim.add(item);
                    break;
                case Preferred:
                    minSize[idx].width = hintSize[idx].width;//0
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    sim.add(item);
                    break;
                case MinimumExpanding:
                    minSize[idx].width = hintSize[idx].width;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    exp.add(item);
                    break;
                case Expanding:
                    minSize[idx].width = hintSize[idx].width;//0;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    exp.add(item);
                    break;
                case Ignored:
                    minSize[idx].width = LayoutUtil.MIN_WIDTH;//0;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    exp.add(item);
                    break;
            }

            switch (vsp) {
                case Fixed:
                    minSize[idx].height = hintSize[idx].height;
                    maxSize[idx].height = hintSize[idx].height;
                    break;
                case Minimum:
                    minSize[idx].height = hintSize[idx].height;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    break;
                case Maximum:
                    minSize[idx].height = LayoutUtil.MIN_HEIGHT;//0;
                    maxSize[idx].height = hintSize[idx].height;
                    break;
                case Preferred:
                    minSize[idx].height = hintSize[idx].height;//LayoutUtil.MIN_HEIGHT;//0;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    break;
                case MinimumExpanding:
                    minSize[idx].height = hintSize[idx].height;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    break;
                case Expanding:
                    minSize[idx].height = hintSize[idx].height; //0;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    break;
                case Ignored:
                    minSize[idx].height = LayoutUtil.MIN_HEIGHT;//0;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    break;
            }
        }

        if (exp.size() > 0) {
            double simpleWidth = 0;
            for (AdsLayout.Item item : sim) {
                int idx = items.indexOf(item);
                itemWidth[idx] = minSize[idx].width;
                simpleWidth += minSize[idx].width;
            }

            double expandedMinWidth = 0;
            for (AdsLayout.Item item : exp) {
                int idx = items.indexOf(item);
                itemWidth[idx] = minSize[idx].width;
                expandedMinWidth += minSize[idx].width;
            }

            if (simpleWidth + expandedMinWidth > r.width - spacing * (size - 1)) {
                double scale = (r.width - spacing * (size - 1)) / (simpleWidth + expandedMinWidth);
                for (int idx = 0; idx < size; idx++) {
                    itemWidth[idx] = itemWidth[idx] * scale;
                }
            } else {
                List<AdsLayout.Item> minExp = new ArrayList<>();
                List<AdsLayout.Item> avrExp = new ArrayList<>();

                double expWidth = r.width - simpleWidth - spacing * (size - 1);
                double avrWidth = expWidth / exp.size();

                for (AdsLayout.Item item : exp) {
                    int idx = items.indexOf(item);
                    int min = minSize[idx].width;
                    if (min > avrWidth) {
                        minExp.add(item);
                        expWidth -= min;
                    } else {
                        avrExp.add(item);
                    }
                }

                if (avrExp.size() > 0) {
                    avrWidth = expWidth / avrExp.size();
                    for (AdsLayout.Item item : avrExp) {
                        int idx = items.indexOf(item);
                        itemWidth[idx] = avrWidth;
                    }
                }
            }
        } else {
            double simpleWidth = 0;
            double growableMinWidth = 0;

            for (int idx = 0; idx < size; idx++) {
                int max = maxSize[idx].width;
                if (max != LayoutUtil.MAX_WIDTH) {
                    itemWidth[idx] = max;
                    simpleWidth += max;
                } else {
                    itemWidth[idx] = minSize[idx].width;
                    grow.add(items.get(idx));
                    growableMinWidth += minSize[idx].width;
                }
            }

            if (simpleWidth + growableMinWidth > r.width - spacing * (size - 1)) {
                double scale = (r.width - spacing * (size - 1)) / (simpleWidth + growableMinWidth);
                for (int idx = 0; idx < size; idx++) {
                    itemWidth[idx] = itemWidth[idx] * scale;
                }
            } else if (grow.size() > 0) {
                List<AdsLayout.Item> minGrow = new ArrayList<>();
                List<AdsLayout.Item> avrGrow = new ArrayList<>();

                double growWidth = r.width - simpleWidth - spacing * (size - 1);
                double avrWidth = growWidth / grow.size();

                for (AdsLayout.Item item : grow) {
                    int idx = items.indexOf(item);
                    int min = minSize[idx].width;
                    if (min > avrWidth) {
                        minGrow.add(item);
                        growWidth -= min;
                    } else {
                        avrGrow.add(item);
                    }
                }

                if (avrGrow.size() > 0) {
                    avrWidth = growWidth / avrGrow.size();
                    for (AdsLayout.Item item : avrGrow) {
                        int idx = items.indexOf(item);
                        itemWidth[idx] = avrWidth;
                    }
                }
            }
        }

        double totalWidth = 0;
        for (int idx = 0; idx < size; idx++) {
            totalWidth += itemWidth[idx];
        }

        double sep = spacing, offs = 0;
        if (exp.size() == 0 && grow.size() == 0) {
            double delta = (r.width - totalWidth - spacing * (size - 1)) / (size * 2);
            sep = spacing + 2 * delta;
            offs = delta;
        }

        Rectangle[] ir = new Rectangle[size];
        for (int idx = 0; idx < size; idx++) {
            double height = Math.min(hintSize[idx].height, r.height);
            if (maxSize[idx].height == LayoutUtil.MAX_HEIGHT) {
                height = r.height;
            }

            double delta = itemWidth[idx] + sep;
            double width = Math.round(offs + delta) - Math.round(offs) - Math.round(sep);
            ir[idx] = new Rectangle(
                    (int) Math.round(r.x + offs),
                    (int) Math.round(r.y + (r.height - height) / 2),
                    (int) Math.round(width),
                    (int) Math.round(height));
            offs += delta;
        }

        return ir;
    }

    public static Rectangle[] justifyVerticalLayout(AdsLayout layout, Rectangle r) {
        assert layout != null : "layout cann't be null";

        final int size;
        AdsLayout.Items items = layout.getItems();
        if ((size = items.size()) == 0) {
            return null;
        }

        int spacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutSpacing")).value;
        spacing = Math.max(spacing, MIN_SPACING);

        Dimension minSize[] = new Dimension[size];
        Dimension maxSize[] = new Dimension[size];
        Dimension hintSize[] = new Dimension[size];
        double itemHeight[] = new double[size];

        List<AdsLayout.Item> exp = new ArrayList<>();
        List<AdsLayout.Item> sim = new ArrayList<>();
        List<AdsLayout.Item> grow = new ArrayList<>();

        for (AdsLayout.Item item : items) {
            int idx = items.indexOf(item);
            RadixObject elem = AdsUIUtil.getItemNode(item);

            ESizePolicy hsp = LayoutUtil.getHSizePolicy(elem);
            ESizePolicy vsp = LayoutUtil.getVSizePolicy(elem);

            hintSize[idx] = LayoutUtil.getHintSize(elem);
            minSize[idx] = new Dimension();
            maxSize[idx] = new Dimension();

            itemHeight[idx] = 0;

            switch (vsp) {
                case Fixed:
                    minSize[idx].height = hintSize[idx].height;
                    maxSize[idx].height = hintSize[idx].height;
                    sim.add(item);
                    break;
                case Minimum:
                    minSize[idx].height = hintSize[idx].height;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    sim.add(item);
                    break;
                case Maximum:
                    minSize[idx].height = hintSize[idx].height;//0
                    maxSize[idx].height = hintSize[idx].height;
                    sim.add(item);
                    break;
                case Preferred:
                    minSize[idx].height = hintSize[idx].height;//0
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    sim.add(item);
                    break;
                case MinimumExpanding:
                    minSize[idx].height = hintSize[idx].height;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    exp.add(item);
                    break;
                case Expanding:
                    minSize[idx].height = hintSize[idx].height;//0;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    exp.add(item);
                    break;
                case Ignored:
                    minSize[idx].height = LayoutUtil.MIN_HEIGHT;//0;
                    maxSize[idx].height = LayoutUtil.MAX_HEIGHT;
                    exp.add(item);
                    break;
            }

            switch (hsp) {
                case Fixed:
                    minSize[idx].width = hintSize[idx].width;
                    maxSize[idx].width = hintSize[idx].width;
                    break;
                case Minimum:
                    minSize[idx].width = hintSize[idx].width;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    break;
                case Maximum:
                    minSize[idx].width = LayoutUtil.MIN_WIDTH; //0
                    maxSize[idx].width = hintSize[idx].width;
                    break;
                case Preferred:
                    minSize[idx].width = hintSize[idx].width;//LayoutUtil.MIN_WIDTH;//0;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    break;
                case MinimumExpanding:
                    minSize[idx].width = hintSize[idx].width;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    break;
                case Expanding:
                    minSize[idx].width = hintSize[idx].width; //0;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    break;
                case Ignored:
                    minSize[idx].width = LayoutUtil.MIN_WIDTH;//0;
                    maxSize[idx].width = LayoutUtil.MAX_WIDTH;
                    break;
            }

        }

        if (exp.size() > 0) {
            double simpleHeight = 0;
            for (AdsLayout.Item item : sim) {
                int idx = items.indexOf(item);
                itemHeight[idx] = minSize[idx].height;
                simpleHeight += minSize[idx].height;
            }

            double expandedMinHeight = 0;
            for (AdsLayout.Item item : exp) {
                int idx = items.indexOf(item);
                itemHeight[idx] = minSize[idx].height;
                expandedMinHeight += minSize[idx].height;
            }

            if (simpleHeight + expandedMinHeight > r.height - spacing * (size - 1)) {
                double scale = (r.height - spacing * (size - 1)) / (simpleHeight + expandedMinHeight);
                for (int idx = 0; idx < size; idx++) {
                    itemHeight[idx] = itemHeight[idx] * scale;
                }
            } else {
                List<AdsLayout.Item> minExp = new ArrayList<>();
                List<AdsLayout.Item> avrExp = new ArrayList<>();

                double expHeight = r.height - simpleHeight - spacing * (size - 1);
                double avrHeight = expHeight / exp.size();

                for (AdsLayout.Item item : exp) {
                    int idx = items.indexOf(item);
                    int min = minSize[idx].height;
                    if (min > avrHeight) {
                        minExp.add(item);
                        expHeight -= min;
                    } else {
                        avrExp.add(item);
                    }
                }

                if (avrExp.size() > 0) {
                    avrHeight = expHeight / avrExp.size();
                    for (AdsLayout.Item item : avrExp) {
                        int idx = items.indexOf(item);
                        itemHeight[idx] = avrHeight;
                    }
                }
            }
        } else {
            double simpleHeight = 0;
            double growableMinHeight = 0;

            for (int idx = 0; idx < size; idx++) {
                int max = maxSize[idx].height;
                if (max != LayoutUtil.MAX_HEIGHT) {
                    itemHeight[idx] = max;
                    simpleHeight += max;
                } else {
                    itemHeight[idx] = minSize[idx].height;
                    grow.add(items.get(idx));
                    growableMinHeight += minSize[idx].height;
                }
            }

            if (simpleHeight + growableMinHeight > r.height - spacing * (size - 1)) {
                double scale = (double) (r.height - spacing * (size - 1)) / (simpleHeight + growableMinHeight);
                for (int idx = 0; idx < size; idx++) {
                    itemHeight[idx] = itemHeight[idx] * scale;
                }
            } else if (grow.size() > 0) {
                List<AdsLayout.Item> minGrow = new ArrayList<>();
                List<AdsLayout.Item> avrGrow = new ArrayList<>();

                double growHeight = r.height - simpleHeight - spacing * (size - 1);
                double avrHeight = growHeight / grow.size();

                for (AdsLayout.Item item : grow) {
                    int idx = items.indexOf(item);
                    int min = minSize[idx].height;
                    if (min > avrHeight) {
                        minGrow.add(item);
                        growHeight -= min;
                    } else {
                        avrGrow.add(item);
                    }
                }

                if (avrGrow.size() > 0) {
                    avrHeight = growHeight / avrGrow.size();
                    for (AdsLayout.Item item : avrGrow) {
                        int idx = items.indexOf(item);
                        itemHeight[idx] = avrHeight;
                    }
                }
            }
        }

        double totalHeight = 0;
        for (int idx = 0; idx < size; idx++) {
            totalHeight += itemHeight[idx];
        }

        double sep = spacing, offs = 0;
        if (exp.size() == 0 && grow.size() == 0) {
            double delta = (r.height - totalHeight - spacing * (size - 1)) / (size * 2);
            sep = spacing + 2 * delta;
            offs = delta;
        }

        Rectangle[] ir = new Rectangle[size];
        for (int idx = 0; idx < size; idx++) {
            double width = Math.min(hintSize[idx].width, r.width);
            if (maxSize[idx].width == LayoutUtil.MAX_WIDTH) {
                width = r.width;
            }

            double delta = itemHeight[idx] + sep;
            double height = Math.round(offs + delta) - Math.round(offs) - Math.round(sep);

            ELayoutDirection ld = ELayoutDirection.LeftToRight;
            AdsUIProperty.EnumValueProperty ldp = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(AdsUIUtil.getItemNode(items.get(idx)), "layoutDirection");
            if (ldp != null) {
                ld = (ELayoutDirection) ldp.value;
            }

            ir[idx] = new Rectangle(
                    (int) Math.round(r.x + (ld.equals(ELayoutDirection.LeftToRight) ? 0 : r.width - width)),
                    (int) Math.round(r.y + offs),
                    (int) Math.round(width),
                    (int) Math.round(height));

            offs += delta;
        }

        return ir;
    }
}
