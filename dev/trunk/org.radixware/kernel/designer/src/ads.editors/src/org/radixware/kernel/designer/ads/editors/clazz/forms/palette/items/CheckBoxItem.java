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
import java.awt.Image;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.register;

public class CheckBoxItem extends Item {

    public static final CheckBoxItem DEFAULT = new CheckBoxItem();

  
    public CheckBoxItem() {
        super(Group.GROUP_BUTTONS, NbBundle.getMessage(CheckBoxItem.class, "Check_Box"), AdsMetaInfo.CHECK_BOX_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "CheckBox", "CheckBox")));
        return widget;
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        //AdsUIProperty.BooleanProperty checked = (AdsUIProperty.BooleanProperty)AdsUtil.getUiProperty(node, "checked");
        //String icon_path=checked.value ? "org/radixware/kernel/common/resources/widgets/checkbox_checked.svg" : "org/radixware/kernel/common/icons/widgets/checkbox_unchecked.svg";
        //RadixIcon image=new RadixIcon(SvgImageLoaderManager.getDefaultSvgLoader().getClass().getClassLoader().getResource(icon_path));

        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = Item.getTextById(node, text.getStringId());

        AdsUIProperty.RectProperty size = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
        int height = DrawUtil.getFontMetrics().getHeight();
        int width = DrawUtil.getFontMetrics().stringWidth(label) + size.height;
//        if (width != size.height)
//            width += 4;

        //if (image != null)
        //    width += 4 + iconSize.width;
//        return new Dimension(Math.max(defaultSize.width, width + 2), Math.max(defaultSize.height, height + 2));
        return new Dimension(width + 2, Math.max(defaultSize.height, height + 2));
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty checked = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checked");
        RadixIcon icon = checked.value ? RadixWareIcons.WIDGETS.CHECK_BOX_CHECKED : RadixWareIcons.WIDGETS.CHECK_BOX_UNCHECKED;
        paint(gr, r, node, icon);
    }

    public void paint(Graphics2D gr, Rectangle r, RadixObject node, RadixIcon image) {
        Rectangle textRect = r.getBounds();
        //gr.clipRect(textRect.x + 1, textRect.y + 1, textRect.width - 2, textRect.height - 2);

        EAlignment ha = EAlignment.AlignLeft;
        EAlignment va = EAlignment.AlignVCenter;

        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");

        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = getTextById(node, text.getStringId());

        if (image != null) {
            AdsUIProperty.RectProperty defaultSize = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);

            int width = defaultSize.height - 6;
            int height = defaultSize.height - 6;
            int spacing = 4;

            Image pixmap = image.getImage(width, height);
            //pixmap = pixmap.getScaledInstance(width, height, 0);

            Rectangle iconRect = new Rectangle(textRect.x, textRect.y + (textRect.height - height) / 2, width, height);

            textRect.x = iconRect.x + iconRect.width + spacing;
            textRect.width = (textRect.x + textRect.width) - (iconRect.x + iconRect.width + spacing);
            gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
            drawImage(gr, textRect, node);
        }
        DrawUtil.drawText(gr, textRect, ha, va, enabled.value, label);
    }

    private void drawImage(Graphics2D gr, Rectangle textRect, RadixObject node) {
        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(node, "icon");
        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
        RadixIcon image = getIconById(node, icon.getImageId());

        if (image != null) {
            int width = iconSize.width;
            int height = iconSize.height;
            int spacing = 4;

            Image pixmap = image.getImage(width, height);
            Rectangle iconRect = new Rectangle(
                    textRect.x + spacing,
                    textRect.y + (textRect.height - height) / 2,
                    iconSize.width, iconSize.height);

            textRect.x = iconRect.x + iconRect.width + spacing;
            textRect.width = (textRect.x + textRect.width) - (iconRect.x + iconRect.width + spacing);
            gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
        }
    }
}
