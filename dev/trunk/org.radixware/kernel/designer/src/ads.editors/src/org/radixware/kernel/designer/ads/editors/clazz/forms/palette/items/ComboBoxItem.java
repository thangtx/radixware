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
import java.awt.GradientPaint;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Items;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPainterAdapter;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

public class ComboBoxItem extends WidgetItem {

    public static final class ComboBoxPropertyCollector extends WidgetPropertyCollector {

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);

            AdsUIProperty.BooleanProperty editable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "editable");
            assert editable != null;
            props.set("editable", editable.value);

            AdsUIProperty.RectProperty defoultSize = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
            assert defoultSize != null;
            props.set("geometry", defoultSize.getRectangle());

            return props;
        }

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {

            assert node instanceof AdsItemWidgetDef;

            AdsItemWidgetDef def = (AdsItemWidgetDef) node;
            Items items = def.getItems();

            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsUIProperty.IntProperty currentIndex = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "currentIndex");
            AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
            AdsUIProperty.BooleanProperty editable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "editable");
            AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
            AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
            AdsUIProperty.SetProperty alignment = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "alignment");

            RadixIcon image = null;
            String label = "";

            if ((items != null) && (items.size() > currentIndex.value) && (currentIndex.value >= 0)) {
                RadixObject nodeItem = (RadixObject) items.get(currentIndex.value);
                AdsUIProperty.LocalizedStringRefProperty labelProp = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(nodeItem, "text");
                label = Item.getTextById(node, labelProp.getStringId());

                AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(nodeItem, "icon");
                image = getIconById(nodeItem, icon.getImageId());
            }

            EAlignment halignment = EAlignment.AlignLeft;
            EAlignment valignment = EAlignment.AlignVCenter;
            if (alignment != null) {
                UIEnum[] values = alignment.getValues();
                halignment = (EAlignment) values[0];
                valignment = (EAlignment) values[1];
            }

            assert Utils.isNotNull(currentIndex, enabled, editable, iconSize, geometry) : "Some of properties are null";

            props.set("currentIndex", currentIndex.value);
            props.set("label", label);
            props.set("image", image);
            props.set("enabled", enabled.value);
            props.set("editable", editable.value);
            props.set("iconSize", iconSize.getDimension());
            props.set("widgetHeight", geometry.height);
            props.set("halignment", halignment);
            props.set("valignment", valignment);

            return props;
        }
    }

    public static final class ComboBoxPainter extends WidgetPainterAdapter {

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            boolean editable = PropertyStore.getBool(props.get("editable"));

            paintBackground(graphics, rect, editable);
        }

        @Override
        public void paintWidget(Graphics2D graphics, final Rectangle rect, PropertyStore props) {

            assert props.checkRequirement("enabled", "editable", "iconSize", "widgetHeight", "halignment", "valignment", "label", "image") : "Some of properties are null";

            boolean enabled = PropertyStore.getBool(props.get("enabled"));
            boolean editable = PropertyStore.getBool(props.get("editable"));
            Dimension iconSize = props.get("iconSize");
            Integer widgetHeight = props.get("widgetHeight");
            EAlignment halignment = props.get("halignment");
            EAlignment valignment = props.get("valignment");
            String label = PropertyStore.getString(props.get("label"));
            RadixIcon image = props.get("image");

            paintWidget(graphics, rect, enabled, editable, iconSize, widgetHeight, halignment, valignment, label, image);
        }

        public void paintBackground(Graphics2D graphics, final Rectangle rect, boolean editable) {
            if (editable) {
                DrawUtil.drawPlainRoundRect(graphics, rect, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);
            } else {
                gradientFillRoundRect(graphics, rect, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_MID_LIGHT);
                DrawUtil.drawPlainRoundRect(graphics, rect, DrawUtil.COLOR_DARK, 1, null);
            }
        }

        private void gradientFillRoundRect(Graphics2D graphics, final Rectangle rect, Color c1, Color c2) {
            Paint oldPaint = graphics.getPaint();
            graphics.setPaint(new GradientPaint(0, 0, c1, 0, rect.height, c2, false));
            graphics.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 7, 7);
            graphics.setPaint(oldPaint);
        }

        private void gradientFillRect(Graphics2D graphics, final Rectangle rect, Color c1, Color c2) {
            Paint oldPaint = graphics.getPaint();
            graphics.setPaint(new GradientPaint(0, 0, c1, 0, rect.height, c2, false));
            graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
            graphics.setPaint(oldPaint);
        }

        private void drawButtonRect(Graphics2D graphics, final Rectangle rect, Rectangle buttonRect, int button_width, boolean editable) {
            buttonRect.width = button_width;
            buttonRect.x = rect.x + rect.width - button_width - 1;
            buttonRect.y = buttonRect.y + 1;
            buttonRect.height = buttonRect.height - 2;

            if (editable) {
                Rectangle buttonRect1 = buttonRect.getBounds();
                buttonRect1.width = buttonRect1.width - 3;
                gradientFillRect(graphics, buttonRect1, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_MID_LIGHT);
                gradientFillRoundRect(graphics, buttonRect, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_MID_LIGHT);
                DrawUtil.drawLine(graphics, buttonRect.x, buttonRect.y, buttonRect.x, buttonRect.y + buttonRect.height - 1, DrawUtil.COLOR_DARK);
                DrawUtil.drawLine(graphics, buttonRect.x + 1, buttonRect.y, buttonRect.x + 1, buttonRect.y + buttonRect.height - 1, DrawUtil.COLOR_BASE);
            } else {
                DrawUtil.drawLine(graphics, buttonRect.x, buttonRect.y + 2, buttonRect.x, buttonRect.y + buttonRect.height - 3, DrawUtil.COLOR_DARK);
                DrawUtil.drawLine(graphics, buttonRect.x + 1, buttonRect.y + 2, buttonRect.x + 1, buttonRect.y + buttonRect.height - 3, DrawUtil.COLOR_BASE);
            }
        }

        public void paintWidget(Graphics2D graphics, final Rectangle rect, boolean enabled, boolean editable, Dimension iconSize, int widgetHeight,
                EAlignment ha, EAlignment va, String label, RadixIcon image) {

            Rectangle rectangle = rect.getBounds();

            int buttonWidth = widgetHeight * 3 / 4;
            drawButtonRect(graphics, rectangle, rectangle.getBounds(), buttonWidth, editable);

            int icon_width = widgetHeight * 3 / 4;
            int icon_height = widgetHeight * 3 / 4;
            drawButtonImage(graphics, rectangle, icon_width, icon_height, enabled);

            rectangle.x = rectangle.x + 4;
            rectangle.width = rectangle.width - icon_width - 4;

            Shape clip = graphics.getClip();
            graphics.clipRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            if (image != null) {
                if (iconSize != null) {
                    icon_width = iconSize.width;
                    icon_height = iconSize.height;
                }
                drawItemIcon(graphics, rectangle, image, icon_width, icon_height);
            }

            //Shape clip = graphics.getClip();
            //graphics.clipRect(textRect.x,textRect.y,textRect.width,textRect.height);
            DrawUtil.drawText(graphics, rectangle, ha, va, enabled, label);
            graphics.setClip(clip);
        }

        private void drawItemIcon(Graphics2D graphics, Rectangle textRect, RadixIcon image, int icon_width, int icon_height) {
            int icon_pos_x = textRect.x;
            int icon_pos_y = textRect.y + (textRect.height - icon_height) / 2;

            Image pixmap = image.getImage(icon_width, icon_height);
            //pixmap = pixmap.getScaledInstance(icon_width, icon_height, 0);

            Rectangle iconRect = new Rectangle(icon_pos_x, icon_pos_y, icon_width, icon_height);
            textRect.x = iconRect.x + iconRect.width + 4;
            textRect.width = textRect.width - iconRect.width - 4;
            graphics.drawImage(pixmap, iconRect.x, iconRect.y, null);
        }

        private void drawButtonImage(Graphics2D graphics, Rectangle textRect, int icon_width, int icon_height, boolean enabled) {
            RadixIcon image = RadixWareIcons.WIDGETS.ARROW_DOWN;
            if (!enabled) {
                image = RadixWareIcons.WIDGETS.ARROW_DOWN_DISABLE;
            }

            int icon_pos_x = textRect.x + textRect.width - icon_width;
            int icon_pos_y = textRect.y + (textRect.height - icon_height) / 2/* + icon_width/4-1*/;

            Image pixmap = image.getImage(icon_width, icon_height);
            //pixmap = pixmap.getScaledInstance(icon_width, icon_height, 0);

            Rectangle iconRect = new Rectangle(icon_pos_x, icon_pos_y, icon_width, icon_height);
            graphics.drawImage(pixmap, iconRect.x, iconRect.y, null);
        }
    }

    public static final ComboBoxItem DEFAULT = new ComboBoxItem();

  

    public ComboBoxItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ComboBoxItem.class, "Combo_Box"), AdsMetaInfo.COMBO_BOX_CLASS,
                new ComboBoxPainter(), new ComboBoxPropertyCollector());
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        return new AdsItemWidgetDef(getClazz());
    }

    @Override
    public final ComboBoxPainter getPainter() {
        return (ComboBoxPainter) super.getPainter();
    }
}
