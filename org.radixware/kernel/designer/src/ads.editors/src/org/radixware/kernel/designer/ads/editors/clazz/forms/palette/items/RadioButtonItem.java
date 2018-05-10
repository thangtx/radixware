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
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class RadioButtonItem extends Item {

    public static final RadioButtonItem DEFAULT = new RadioButtonItem();

    

    public RadioButtonItem() {
        super(Group.GROUP_BUTTONS, NbBundle.getMessage(RadioButtonItem.class, "Radio_Button"), AdsMetaInfo.RADIO_BUTTON_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "RadioButton", "RadioButton")));
        return widget;
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = Item.getTextById(node, text.getStringId());

        AdsUIProperty.RectProperty size = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
        int height = DrawUtil.getFontMetrics().getHeight();
        int width = DrawUtil.getFontMetrics().stringWidth(label) + size.height;
//        if (width != size.height)
//            width += 8;

//        return new Dimension(Math.max(defaultSize.width, width + 2), Math.max(defaultSize.height, height + 2));
        return new Dimension(width + 2, Math.max(defaultSize.height, height + 2));
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty checked = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checked");
        RadixIcon icon = checked.value ? RadixWareIcons.WIDGETS.RADIOBUTTON_CHECKED : RadixWareIcons.WIDGETS.RADIOBUTTON_UNCHECKED;
        CheckBoxItem.DEFAULT.paint(gr, r, node, icon);
    }
}
