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
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class LineEditItem extends Item {

    public static final LineEditItem DEFAULT = new LineEditItem();

  
    public LineEditItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(LineEditItem.class, "Line_Edit"), AdsMetaInfo.LINE_EDIT_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty frame_prop = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "frame");
        boolean frame = frame_prop.value;

        AdsUIProperty.BooleanProperty enabled_prop = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");

        Color bColor = enabled_prop == null || enabled_prop.value ? DrawUtil.COLOR_BASE : DrawUtil.COLOR_MID_LIGHT;

        if (frame) {
            DrawUtil.drawPlainRoundRect(gr, r, DrawUtil.COLOR_DARK, 1, bColor);
        } else {
            DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_BASE, 1, bColor);
        }
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle rec = r.getBounds();
        rec.x = rec.x + 4;
        rec.width = rec.width - 8;

        AdsUIProperty.SetProperty alignment = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "alignment");
        UIEnum[] values = alignment.getValues();
        EAlignment ha = (EAlignment) values[0];
        EAlignment va = (EAlignment) values[1];

        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");

        AdsUIProperty.StringProperty text = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(node, "text");
        String label = text.value;

        DrawUtil.drawText(gr, rec, ha, va, enabled.value, label);
    }
}
