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
import java.awt.GradientPaint;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class ProgressBarItem extends Item {

    public static final ProgressBarItem DEFAULT = new ProgressBarItem();


    public ProgressBarItem() {
        super(Group.GROUP_DISPLAY_WIDGETS, NbBundle.getMessage(ProgressBarItem.class, "Progress_Bar"), AdsMetaInfo.PROGRESS_BAR_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawShadePanel(gr, r, false, 1, DrawUtil.COLOR_BASE);
        AdsUIProperty.IntProperty maximum = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "maximum");
        AdsUIProperty.IntProperty minimum = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "minimum");
        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "orientation");
        int cur_val = getValue(node, maximum.value, minimum.value);
        if (cur_val > 0) {
            Rectangle blue_rec = getRect(r, (EOrientation) orientation.value, cur_val);
            gradientFillRect(gr, blue_rec);
            DrawUtil.drawShadePanel(gr, blue_rec, false, 1, null);
        }
    }

    private void gradientFillRect(Graphics2D gr, Rectangle rect) {
        Paint oldPaint = gr.getPaint();
        gr.setPaint(new GradientPaint(rect.x, rect.y, new Color(255, 208, 162), rect.x + 10,
                rect.y + 10, new Color(255, 177, 108), true));
        gr.fill(rect);
        gr.setPaint(oldPaint);
    }

    private Rectangle getRect(Rectangle rect, EOrientation orientation, int cur_val) {
        Rectangle blue_rec = rect.getBounds();
        if (orientation == EOrientation.Horizontal) {
            blue_rec.width = rect.width * cur_val / 100;
            blue_rec.height = rect.height - 1;
            blue_rec.y = rect.y + 1;
        } else if (orientation == EOrientation.Vertical) {

            blue_rec.height = rect.height * cur_val / 100;
            blue_rec.width = rect.width - 1;
            blue_rec.x = rect.x + 1;
            blue_rec.y = rect.height - blue_rec.height;
        }
        return blue_rec;
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle text_rec = r.getBounds();
        text_rec.y = text_rec.y + 1;
        AdsUIProperty.BooleanProperty textVisible = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "textVisible");
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        if (textVisible.value) {
            EAlignment ha = EAlignment.AlignHCenter;
            EAlignment va = EAlignment.AlignVCenter;
            AdsUIProperty.IntProperty maximum = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "maximum");
            AdsUIProperty.IntProperty minimum = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "minimum");
            int value = getValue(node, maximum.value, minimum.value);
            if (value > 0) {
                String label = String.valueOf(value) + "%";
                DrawUtil.drawText(gr, text_rec, ha, va, enabled.value, label);
            }
        }
    }

    private int getValue(RadixObject node, int max, int min) {
        AdsUIProperty.IntProperty val = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "value");
        if ((max - min) > 0) {
            int cur_val = val.value;
            cur_val = 100 * (cur_val - min) / (max - min);
            return cur_val;
        }
        return 0;
    }
}
