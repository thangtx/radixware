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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.Shape;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtGroupBoxItem extends Item {

    public static final RwtGroupBoxItem DEFAULT = new RwtGroupBoxItem();

    public RwtGroupBoxItem() {
        super(Group.GROUP_WEB_CONTAINERS, "Group Box", AdsMetaInfo.RWT_UI_GROUP_BOX);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("title", AdsUIUtil.createStringDef(context, "Group", "Группа")));
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        DrawUtil.drawPlainRoundRect(graphics, rect, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        Rectangle inner = new Rectangle(rect);

        inner.height -= 20;
        inner.y += 20;
        DrawUtil.drawPlainRoundRect(graphics, inner, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);
        inner = new Rectangle(rect);

        inner.height = 30;

        DrawUtil.drawPlainRoundRect(graphics, inner, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_MID_LIGHT);

        inner.y = 20;
        inner.height = 20;
        inner.x = 1;
        inner.width -= 2;
        DrawUtil.drawPlainRect(graphics, inner, DrawUtil.COLOR_BASE, 1, DrawUtil.COLOR_BASE);
        DrawUtil.drawLine(graphics, inner.x, 20, inner.x + inner.width, 20, DrawUtil.COLOR_DARK);

        UiProperties props = ((AdsRwtWidgetDef) node).getProperties();
        AdsUIProperty prop = props.getByName("title");
        String label = null;
        if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
            Id stringId = ((AdsUIProperty.LocalizedStringRefProperty) prop).getStringId();
            if (stringId != null) {
                label = getTextById(node, stringId);
            }
        }
        if (label != null) {
            EAlignment ha = EAlignment.AlignLeft;
            EAlignment va = EAlignment.AlignVCenter;
            Shape clip = graphics.getClip();
            Rectangle textRext = new Rectangle(rect);
            textRext.x = 10;
            textRext.height = 22;
            textRext.width = 20;
            DrawUtil.drawText(graphics, textRext, ha, va, true, 0, DrawUtil.COLOR_MID_LIGHT, label);
            graphics.setClip(clip);
        }

        // RwtPanelItem.paintPanel(graphics, inner, node);

    }
}
