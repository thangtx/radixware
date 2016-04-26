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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtEditorPageItem extends Item {

    public static final RwtEditorPageItem DEFAULT = new RwtEditorPageItem();

    public RwtEditorPageItem() {
        super(Group.GROUP_WEB_RADIX_WIDGETS, "Editor_Page", AdsMetaInfo.RWT_EDITOR_PAGE);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        paintBackground(gr, r.getBounds(), node, Color.WHITE, DrawUtil.COLOR_MID_LIGHT);
    }

    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node, Color c1, Color c2) {
        Rectangle rect = r.getBounds();
        gradientFillRect(gr, rect, c1, c2);
        drawRoundRect(gr, rect);
    }

    public void paint(Graphics2D gr, Rectangle textRect, String label) {
        EAlignment ha = EAlignment.AlignHCenter;
        EAlignment va = EAlignment.AlignVCenter;
        DrawUtil.drawText(gr, textRect, ha, va, true, label);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.EditorPageRefProperty page_prop = (AdsUIProperty.EditorPageRefProperty) AdsUIUtil.getUiProperty(node, "editorPage");

        String label = "Editor Page";
        if (page_prop != null) {
            AdsEditorPageDef page = page_prop.findEditorPage();
            if (page != null) {
                label = page.getName();
            }
        }
        paint(gr, r.getBounds(), label);
    }

    public void drawRoundRect(Graphics2D gr, Rectangle rect) {
        Stroke oldStr = gr.getStroke();
        float[] dashPattern = {1.0f, 2.0f};
        gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        DrawUtil.drawPlainRoundRect(gr, rect, Color.BLACK, 1, null);
        gr.setStroke(oldStr);
    }

    public void gradientFillRect(Graphics2D gr, Rectangle rect, Color c1, Color c2) {
        Paint oldPaint = gr.getPaint();
        gr.setPaint(new GradientPaint(0, 0, c1, 0, rect.height, c2, false));
        gr.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 7, 7);
        gr.setPaint(oldPaint);
    }
}
