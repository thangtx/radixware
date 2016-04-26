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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPainterAdapter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

public abstract class AbstractButtonItem extends WidgetItem {

    protected static class AbstractButtonPropertyCollector extends WidgetPropertyCollector {

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);

            boolean isChecked = false;
            AdsUIProperty.BooleanProperty checkable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checkable");
            if ((checkable != null) && (checkable.value == true)) {
                AdsUIProperty.BooleanProperty checked = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checked");
                isChecked = checked == null ? false : checked.value;
            }
            props.set("checked", isChecked);

            return props;
        }

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsUIProperty.LocalizedStringRefProperty textProperty = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
            String text = getTextById(node, textProperty.getStringId());

            if (node instanceof AdsRwtWidgetDef && ((AdsRwtWidgetDef) node).isActionWidget()) {
                props.set("text", "");
            } else {
                props.set("text", text);
            }

            AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
            props.set("enabled", enabled == null ? true : enabled.value);

            AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
            if (iconSize != null) {
                props.set("iconSize", new Dimension(iconSize.width, iconSize.height));
            }

            AdsUIProperty.ImageProperty iconProperty = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(node, "icon");
            RadixIcon icon = null;
            if (iconProperty != null) {
                icon = getIconById(node, iconProperty.getImageId());
            }
            props.set("icon", icon);

            return props;
        }
    }

    protected static class AbstractButtonPainter extends WidgetPainterAdapter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {

            String label = PropertyStore.getString(props.get("text"));
            RadixIcon image = props.get("icon");
            Dimension iconSize = props.get("iconSize");
            boolean enabled = PropertyStore.getBool(props.get("enabled"));

            Rectangle textRect = rect.getBounds();

            EAlignment ha = EAlignment.AlignHCenter;
            EAlignment va = EAlignment.AlignVCenter;

            if (image != null) {
                drawIcon(graphics, textRect, image, iconSize, label, EToolButtonStyle.ToolButtonTextBesideIcon);
                ha = EAlignment.AlignLeft;
            }

            Shape clip = graphics.getClip();
            graphics.clipRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            DrawUtil.drawText(graphics, textRect, ha, va, enabled, label);
            graphics.setClip(clip);
        }

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {

            final boolean isChecked = PropertyStore.getBool(props.get("checked"));

            Rectangle rectangle = rect.getBounds();
            fillRect(graphics, rectangle, isChecked, false);
            DrawUtil.drawPlainRoundRect(graphics, rectangle, DrawUtil.COLOR_DARK, 1, null);
        }

        protected static void fillRect(Graphics2D graphics, Rectangle rect, boolean isChecked, boolean isFlat) {
            Color c1 = DrawUtil.COLOR_BUTTON, c2 = DrawUtil.COLOR_MID_LIGHT;
            if (isFlat) {
                c2 = DrawUtil.COLOR_BUTTON;
            }
            if (isChecked) {
                c1 = DrawUtil.COLOR_MID_LIGHT;
                c2 = DrawUtil.COLOR_MID_LIGHT;
            }
            gradientFillRect(graphics, rect, c1, c2);
        }

        protected static void gradientFillRect(Graphics2D graphics, Rectangle rect, Color color1, Color color2) {
            Paint oldPaint = graphics.getPaint();
            graphics.setPaint(new GradientPaint(rect.x, rect.y, color1, rect.x, rect.y + rect.height, color2, false));
            graphics.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 7, 7);
            graphics.setPaint(oldPaint);
        }

        public static void drawIcon(Graphics2D graphics, Rectangle textRect, RadixIcon image, Dimension iconSize, String label, EToolButtonStyle toolButtonStyle) {
            int spacing = 4;
            int icon_height = 16;
            int icon_width = 16;
            if (iconSize != null) {
                icon_height = iconSize.height;
                icon_width = iconSize.width;
            }

            Dimension textSize = DrawUtil.textSize(graphics, label);
            if ((label == null) || ("".equals(label))) {
                spacing = 0;
            }
            Rectangle iconRect = null;
            if (toolButtonStyle == EToolButtonStyle.ToolButtonIconOnly) {
                iconRect = new Rectangle(
                        textRect.x + (textRect.width - icon_width) / 2,
                        textRect.y + (textRect.height - icon_height) / 2,
                        icon_width, icon_height);
            } else if (toolButtonStyle == EToolButtonStyle.ToolButtonTextUnderIcon) {
                iconRect = new Rectangle(
                        textRect.x + (textRect.width - icon_width) / 2,
                        textRect.y + (textRect.height - icon_height - spacing - textSize.height) / 2,
                        icon_width, icon_height);
            } else if (toolButtonStyle == EToolButtonStyle.ToolButtonTextBesideIcon) {
                iconRect = new Rectangle(
                        textRect.x + (textRect.width - icon_width - textSize.width - spacing) / 2,
                        textRect.y + (textRect.height - icon_height) / 2,
                        icon_width, icon_height);
            }
            if (image != null) {
                Image pixmap = image.getImage(icon_width, icon_height);
                if (toolButtonStyle != EToolButtonStyle.ToolButtonIconOnly) {
                    calcTextRectSize(textRect, iconRect, toolButtonStyle, spacing, textSize);
                }
                graphics.drawImage(pixmap, iconRect.x, iconRect.y, null);
            } else {
                graphics.drawRect(iconRect.x, iconRect.y, iconRect.width, iconRect.height);
            }
        }

        protected static void calcTextRectSize(Rectangle textRect, Rectangle iconRect, EToolButtonStyle toolButtonStyle, int spacing, Dimension textSize) {
            if (toolButtonStyle == EToolButtonStyle.ToolButtonTextUnderIcon) {
                textRect.x = textRect.x + (textRect.width - textSize.width) / 2;
                textRect.y = iconRect.y + iconRect.height + spacing;
                textRect.width = textSize.width;
                textRect.height = textSize.height;
            } else if (toolButtonStyle == EToolButtonStyle.ToolButtonTextBesideIcon) {
                textRect.x = iconRect.x + iconRect.width + spacing;
                textRect.width = (textRect.x + textRect.width) - (iconRect.x + iconRect.width + spacing);
            }
        }
    }

    public AbstractButtonItem(Group group, String title, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        super(group, title, clazz, painter, propertyCollector);
    }

    public AbstractButtonItem(Group group, String title, String clazz) {
        super(group, title, clazz);
    }
}
