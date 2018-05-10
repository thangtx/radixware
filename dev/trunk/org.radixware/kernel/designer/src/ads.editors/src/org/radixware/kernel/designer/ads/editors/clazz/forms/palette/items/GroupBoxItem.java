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
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class GroupBoxItem extends Item {

    public static final GroupBoxItem DEFAULT = new GroupBoxItem();

   

    public GroupBoxItem() {
        super(Group.GROUP_CONTAINERS, NbBundle.getMessage(GroupBoxItem.class, "Group_Box"), AdsMetaInfo.GROUP_BOX_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("title", AdsUIUtil.createStringDef(context, "GroupBox", "GroupBox")));
        return widget;
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        AdsUIProperty.LocalizedStringRefProperty title = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "title");
        String label = getTextById(node, title.getStringId());

        if (label.length() > 0) {
            return new Dimension(defaultSize.width, DrawUtil.getFontMetrics().getHeight() + defaultSize.height);
        }

        return defaultSize;
    }

    @Override
    public Rectangle adjustLayoutGeometry(RadixObject node, Rectangle defaultRect) {
        Rectangle r = super.adjustLayoutGeometry(node, defaultRect);
        AdsUIProperty.LocalizedStringRefProperty title = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "title");
        String label = getTextById(node, title.getStringId());
        if (label.length() > 0) {
            r.y += DrawUtil.getFontMetrics().getHeight() - DrawUtil.getFontMetrics().getDescent() - 1;
            r.height -= DrawUtil.getFontMetrics().getHeight() - DrawUtil.getFontMetrics().getDescent() - 1;
        }
        return r;
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty flat = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "flat");
        if (flat.value == true) {
            return;
        }

        Rectangle rect = r.getBounds();
        AdsUIProperty.LocalizedStringRefProperty title = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "title");
        String label = getTextById(node, title.getStringId());

        //rect.y -= 1;
        //rect.height += 2;
        //rect.x -= 1;
        //rect.width += 2;
        if (label.length() > 0) {
            Dimension textSize = DrawUtil.textSize(gr, label);
            rect.y += Math.round(textSize.height / 2f);
            rect.height -= Math.round(textSize.height / 2f);
        }

        DrawUtil.drawShadeRect(gr, rect, true, 1, 0, null);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.SetProperty alignment = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "alignment");
        UIEnum[] values = alignment.getValues();
        EAlignment ha = (EAlignment) values[0];
        EAlignment va = (EAlignment) values[1];

        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        AdsUIProperty.BooleanProperty checkable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checkable");
        AdsUIProperty.BooleanProperty checked = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checked");

        AdsUIProperty.LocalizedStringRefProperty title = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "title");
        String label = getTextById(node, title.getStringId());

        if (label.length() > 0) {
            Rectangle textRect = r.getBounds();
            Dimension textSize = DrawUtil.textSize(gr, getTextById(node, title.getStringId()));

            textRect.x += 8;
            textRect.y -= 1;
            textRect.width -= 16;
            textRect.height = textSize.height;
            if (checkable.value) {
                drawCheakableIcon(gr, textRect, checked.value, ha, textSize.width);
                ha = EAlignment.AlignLeft;
            }
            DrawUtil.drawText(gr, textRect, ha, va, enabled.value, DrawUtil.DRAW_LEFT_TO_RIGHT, DrawUtil.COLOR_WINDOW, label);
        }
    }

    private void drawCheakableIcon(Graphics2D gr, Rectangle textRect, boolean checked, EAlignment ha, int textWidth) {
        RadixIcon icon = checked ? RadixWareIcons.WIDGETS.CHECK_BOX_CHECKED : RadixWareIcons.WIDGETS.CHECK_BOX_UNCHECKED;
        Rectangle iconRect = textRect.getBounds();
        iconRect.y += 1;
        iconRect.width = 12;
        iconRect.height = 12;
        if (ha.equals(EAlignment.AlignHCenter)) {
            iconRect.x += (textRect.width - textWidth - iconRect.width) / 2;
        } else if (ha.equals(EAlignment.AlignRight)) {
            iconRect.x += textRect.width - textWidth - iconRect.width;
        }

        Image pixmap = icon.getImage(iconRect.width, iconRect.height);
        //pixmap = pixmap.getScaledInstance(iconRect.width, iconRect.height, 0);

        textRect.x = iconRect.x + iconRect.width + 4;
        textRect.width = (textRect.x + textRect.width) - (iconRect.x + iconRect.width + 4);

        DrawUtil.drawPlainRect(gr, iconRect.x - 2, iconRect.y, iconRect.width + 6, iconRect.height, DrawUtil.COLOR_BUTTON, 1, DrawUtil.COLOR_BUTTON);
        gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
    }
}
