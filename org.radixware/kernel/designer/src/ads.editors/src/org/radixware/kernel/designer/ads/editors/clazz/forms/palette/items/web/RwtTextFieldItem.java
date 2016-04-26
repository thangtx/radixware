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
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtTextFieldItem extends Item {

    public static final RwtTextFieldItem DEFAULT = new RwtTextFieldItem();

    public RwtTextFieldItem() {
        super(Group.GROUP_WEB_INPUTS, "Text Field", AdsMetaInfo.RWT_UI_TEXT_FIELD);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "Edit text", "Текст")));
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        DrawUtil.drawPlainRect(graphics, rect, DrawUtil.COLOR_DARK, 1, Color.WHITE);
    }

    @Override
    public void paintBorder(Graphics2D graphics, Rectangle rect, RadixObject node) {
        super.paintBorder(graphics, rect, node);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        if (node instanceof AdsRwtWidgetDef) {
            UiProperties props = ((AdsRwtWidgetDef) node).getProperties();
            AdsUIProperty prop = props.getByName("text");
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
                graphics.clipRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
                DrawUtil.drawText(graphics, rect, ha, va, true, label);
                graphics.setClip(clip);
            }

        }

        super.paintWidget(graphics, rect, node);
    }
}
