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
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.utils.PropertyStore;


public class PushButtonItem extends AbstractButtonItem {

    public static final class PushButtonPropertyCollector extends AbstractButtonPropertyCollector {

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);

            AdsUIProperty.BooleanProperty isFlat = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "flat");
            props.set("flat", isFlat == null ? false : isFlat.value);

            AdsUIProperty.BooleanProperty isDefault = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "default");
            props.set("default", isDefault == null ? false : isDefault.value);

            return props;
        }
    }

    public static final class PushButtonPainter extends AbstractButtonPainter {

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {

            final boolean isFlat = PropertyStore.getBool(props.get("flat")),
                    isDefault = PropertyStore.getBool(props.get("default")),
                    isChecked = PropertyStore.getBool(props.get("checked"));

            Rectangle rectangle = rect.getBounds();
            fillRect(graphics, rectangle, isChecked, isFlat);
            if ((!isFlat) || (isChecked)) {
                if (isDefault) {
                    DrawUtil.drawPlainRoundRect(graphics, rectangle, Color.BLACK, 1, null);
                } else {
                    DrawUtil.drawPlainRoundRect(graphics, rectangle, DrawUtil.COLOR_DARK, 1, null);
                }
            }
        }
    }
    public static final PushButtonItem DEFAULT = new PushButtonItem();

  

    public PushButtonItem() {
        super(Group.GROUP_BUTTONS, NbBundle.getMessage(PushButtonItem.class, "Push_Button"), AdsMetaInfo.PUSH_BUTTON_CLASS,
                new PushButtonPainter(), new PushButtonPropertyCollector());
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "PushButton", "PushButton")));
        return widget;
    }

    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize, EToolButtonStyle toolButtonStyle) {
        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(node, "icon");
        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
        RadixIcon image = Item.getIconById(node, icon.getImageId());

        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = Item.getTextById(node, text.getStringId());

        int height = DrawUtil.getFontMetrics().getHeight();
        int width = DrawUtil.getFontMetrics().stringWidth(label);

        if (image != null) {
            if (toolButtonStyle == EToolButtonStyle.ToolButtonIconOnly) {
                height = iconSize.height;
                width = iconSize.width;
            } else if (toolButtonStyle == EToolButtonStyle.ToolButtonTextBesideIcon) {
                height = Math.max(iconSize.height, height);
                width += 4 + iconSize.width;
            } else if (toolButtonStyle == EToolButtonStyle.ToolButtonTextUnderIcon) {
                height += 4 + iconSize.height;
                width = Math.max(iconSize.width, width);
            }
        }

        return new Dimension(width + 8, Math.max(defaultSize.height, height + 2));
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        return adjustHintSize(node, defaultSize, EToolButtonStyle.ToolButtonTextBesideIcon);
    }

    @Override
    public final PushButtonPainter getPainter() {
        return (PushButtonPainter) super.getPainter();
    }
}
