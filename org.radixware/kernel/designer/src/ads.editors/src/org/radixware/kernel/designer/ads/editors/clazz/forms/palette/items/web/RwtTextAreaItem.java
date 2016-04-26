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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtTextAreaItem extends Item {

    public static final RwtTextAreaItem DEFAULT = new RwtTextAreaItem();

    public RwtTextAreaItem() {
        super(Group.GROUP_WEB_INPUTS, "Text Area", AdsMetaInfo.RWT_UI_TEXT_AREA);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.StringProperty("text", "Text"));
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {

        DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);

    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle rec = r.getBounds();
        rec.x = rec.x + 4;
        rec.width = rec.width - 8;

        //AdsUIProperty.SetProperty alignment = (AdsUIProperty.SetProperty)AdsUIUtil.getUiProperty(node, "alignment");
        //UIEnum[] values = alignment.getValues();
        EAlignment ha = EAlignment.AlignLeft;//(EAlignment)values[0];
        EAlignment va = EAlignment.AlignTop;//(EAlignment)values[1];

        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");

        AdsUIProperty.StringProperty text = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = text.value;

        DrawUtil.drawText(gr, rec, ha, va, enabled.value, label);
    }
}
