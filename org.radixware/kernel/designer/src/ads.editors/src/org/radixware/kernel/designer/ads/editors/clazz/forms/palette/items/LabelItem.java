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
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class LabelItem extends Item {

    public static final LabelItem DEFAULT = new LabelItem();

    

    public LabelItem() {
        super(Group.GROUP_DISPLAY_WIDGETS, NbBundle.getMessage(LabelItem.class, "Label"), AdsMetaInfo.LABEL_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "TextLabel", "TextLabel")));
        return widget;
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = Item.getTextById(node, text.getStringId());
        return adjustHintSize(node, defaultSize, label);
    }
    
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize, String label) {
        
        int height = DrawUtil.getFontMetrics().getHeight();
        int width = DrawUtil.getFontMetrics().stringWidth(label == null? "": label);

        //return new Dimension(Math.max(defaultSize.width, width), Math.max(defaultSize.height, height));
        return new Dimension(width + 2, Math.max(defaultSize.height, height + 2));
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = getTextById(node, text.getStringId());
        paint(gr, r, node, label);
    }

    public void paint(Graphics2D gr, Rectangle r, RadixObject node, String label) {
        Rectangle textRect = r.getBounds();

        EAlignment ha = EAlignment.AlignLeft;
        EAlignment va = EAlignment.AlignVCenter;
        AdsUIProperty.SetProperty alignment = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "alignment");
        if (alignment != null) {
            UIEnum[] values = alignment.getValues();
            ha = (EAlignment) values[0];
            va = (EAlignment) values[1];
        }
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");

        DrawUtil.drawText(gr, textRect, ha, va, enabled.value, label);
    }
}
