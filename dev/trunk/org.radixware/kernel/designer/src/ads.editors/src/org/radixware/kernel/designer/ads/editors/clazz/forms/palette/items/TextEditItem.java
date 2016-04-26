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

import java.awt.FontMetrics;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class TextEditItem extends Item {

    public static final TextEditItem DEFAULT = new TextEditItem();

   

    public TextEditItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(TextEditItem.class, "Text_Edit"), AdsMetaInfo.TEXT_EDIT_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawShadePanel(gr, r, false, 1, DrawUtil.COLOR_BASE);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle rec = r.getBounds();
        rec.x += 4;
        rec.y += 4;
        rec.width -= 8;
        rec.height -= 5;

        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        AdsUIProperty.StringProperty text = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(node, "html");
        String label = text.value;

        Shape clip = gr.getClip();
        gr.clipRect(rec.x, rec.y, rec.width, rec.height);
        drawText(gr, rec, label, enabled.value);
        gr.setClip(clip);
    }

    private void drawText(Graphics2D gr, Rectangle rec, String label, boolean enabled) {
        EAlignment ha = EAlignment.AlignLeft;
        EAlignment va = EAlignment.AlignVCenter;

        FontMetrics fm = gr.getFontMetrics(DrawUtil.DEFAULT_FONT);
        rec.height = fm.getHeight();
        while (label.length() > 0) {
            int n = 0, j, size = label.length();
            for (j = 0; j < size; j++) {
                n += fm.charWidth(label.charAt(j));
                if (n >= rec.width) {
                    break;
                }
            }
            if (j == 0) {
                break;
            }
            String line = label.substring(0, j);
            label = label.substring(j);
            DrawUtil.drawText(gr, rec, ha, va, enabled, line);
            rec.y += rec.height;
        }
    }
}
