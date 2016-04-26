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

import java.awt.Dimension;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.EArrowType;
import org.radixware.kernel.common.defs.ads.ui.enums.EPopupMode;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.utils.PropertyStore;

import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

public class ToolButtonItem extends AbstractButtonItem {

    public static final class ToolButtonPropertyCollector extends AbstractButtonPropertyCollector {

        @Override
        public PropertyStore getGeneralProperties(RadixObject node, Rectangle rect) {
            PropertyStore propertyStore = super.getGeneralProperties(node, rect);

            AdsUIProperty.EnumValueProperty popupMode_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "popupMode");
            EPopupMode popupMode = (EPopupMode) popupMode_prop.value;
            propertyStore.set("popupMode", popupMode);

            return propertyStore;
        }

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore propertyStore = super.getWidgetProperties(node, rect);

            AdsUIProperty.EnumValueProperty arrowTypeProperty = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "arrowType");
            propertyStore.set("arrowType", arrowTypeProperty.value);

            AdsUIProperty.EnumValueProperty toolButtonStyle_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "toolButtonStyle");
            propertyStore.set("toolButtonStyle", toolButtonStyle_prop.value);

            return propertyStore;
        }
    }

    public static final class ToolButtonPainter extends AbstractButtonPainter {

        private static final int RIGHT_WIDTH = 16;

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {

            EToolButtonStyle toolButtonStyle = props.get("toolButtonStyle", EToolButtonStyle.ToolButtonIconOnly);
            String text = PropertyStore.getString(props.get("text"));
            RadixIcon icon = props.get("icon");
            Dimension iconSize = props.get("iconSize");
            boolean enabled = PropertyStore.getBool(props.get("enabled"));

            EArrowType arrowType = props.get("arrowType", EArrowType.NoArrow);
            EPopupMode popupMode = props.get("popupMode", EPopupMode.DelayedPopup);

            EAlignment ha = EAlignment.AlignHCenter;
            EAlignment va = EAlignment.AlignVCenter;

            Rectangle textRect;
            if (popupMode == EPopupMode.MenuButtonPopup) {
                textRect = new Rectangle(rect.x, rect.y, rect.width - RIGHT_WIDTH, rect.height);

                Rectangle rectRight = new Rectangle(rect.x + rect.width - RIGHT_WIDTH, rect.y, RIGHT_WIDTH, rect.height);
                RadixIcon i = getIcon(EArrowType.DownArrow, enabled);
                drawIcon(graphics, rectRight, i, new Dimension(16, 16), "", EToolButtonStyle.ToolButtonIconOnly);

            } else {
                textRect = rect.getBounds();
            }

            if (toolButtonStyle != EToolButtonStyle.ToolButtonTextOnly) {
                if (arrowType != EArrowType.NoArrow) {
                    icon = getIcon(arrowType, enabled);
                }
                if (icon != null) {
                    if (toolButtonStyle == EToolButtonStyle.ToolButtonTextBesideIcon) {
                        ha = EAlignment.AlignLeft;
                    }
                    drawIcon(graphics, textRect, icon, iconSize, text, toolButtonStyle);
                }
            }

            if (toolButtonStyle != EToolButtonStyle.ToolButtonIconOnly) {
                Shape clip = graphics.getClip();
                graphics.clipRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
                DrawUtil.drawText(graphics, textRect, ha, va, enabled, text);
                graphics.setClip(clip);
            }
        }

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            EPopupMode popupMode = (EPopupMode) props.get("popupMode");

            if (popupMode == EPopupMode.MenuButtonPopup) {
                Rectangle rectLeft = new Rectangle(rect.x, rect.y, rect.width - RIGHT_WIDTH, rect.height);
                super.paintBackground(graphics, rectLeft, props);

                Rectangle rectRight = new Rectangle(rect.x + rect.width - RIGHT_WIDTH, rect.y, RIGHT_WIDTH, rect.height);
                super.paintBackground(graphics, rectRight, props);
            } else {
                super.paintBackground(graphics, rect, props);
            }
        }
    }
    public static final ToolButtonItem DEFAULT = new ToolButtonItem();

  

    public ToolButtonItem() {
        super(Group.GROUP_BUTTONS, NbBundle.getMessage(ToolButtonItem.class, "Tool_Button"), AdsMetaInfo.TOOL_BUTTON_CLASS,
                new ToolButtonPainter(), new ToolButtonPropertyCollector());
    }

    protected ToolButtonItem(Group group, String title, String clazz) {
        super(group, title, clazz);
    }

    protected ToolButtonItem(Group group, String title, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        super(group, title, clazz, painter, propertyCollector);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "...", "...")));
        return widget;
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        AdsUIProperty.EnumValueProperty toolButtonStyle_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "toolButtonStyle");
        EToolButtonStyle toolButtonStyle = (EToolButtonStyle) toolButtonStyle_prop.value;
        return PushButtonItem.DEFAULT.adjustHintSize(node, defaultSize, toolButtonStyle);
    }

    private static RadixIcon getIcon(EArrowType arrowType, boolean enabled) {
        RadixIcon icon = null;
        if (EArrowType.DownArrow == arrowType) {
            icon = enabled ? RadixWareIcons.WIDGETS.ARROW_DOWN : RadixWareIcons.WIDGETS.ARROW_DOWN_DISABLE;
        } else if (EArrowType.UpArrow == arrowType) {
            icon = enabled ? RadixWareIcons.WIDGETS.ARROW_UP : RadixWareIcons.WIDGETS.ARROW_UP_DISABLE;
        } else if (EArrowType.RightArrow == arrowType) {
            icon = enabled ? RadixWareIcons.WIDGETS.ARROW_RIGHT : RadixWareIcons.WIDGETS.ARROW_RIGHT_DISABLE;
        } else if (EArrowType.LeftArrow == arrowType) {
            icon = enabled ? RadixWareIcons.WIDGETS.ARROW_LEFT : RadixWareIcons.WIDGETS.ARROW_LEFT_DISABLE;
        }
        return icon;
    }
}
