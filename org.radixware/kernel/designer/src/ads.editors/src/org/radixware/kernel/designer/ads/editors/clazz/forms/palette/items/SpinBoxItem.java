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

import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class SpinBoxItem extends Item {

    public static final SpinBoxItem DEFAULT = new SpinBoxItem();

    

    public SpinBoxItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(SpinBoxItem.class, "Spin_Box"), AdsMetaInfo.SPIN_BOX_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty frame_prop = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "frame");
        boolean frame = frame_prop.value;
        if (frame) {
            DrawUtil.drawPlainRoundRect(gr, r, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);
        } else {
            DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_BASE, 1, DrawUtil.COLOR_BASE);
        }
        drawButtonRect(gr, r.getBounds(), node, frame);
    }

    private void drawButtonRect(Graphics2D gr, Rectangle rect, RadixObject node, boolean frame) {
        AdsUIProperty.RectProperty defaultSize = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
        int button_width = defaultSize.height - defaultSize.height / 3;

        Rectangle buttonRect = rect.getBounds();
        buttonRect.width = defaultSize.height - defaultSize.height / 3;
        if (frame) {
            buttonRect.x = rect.x + rect.width - button_width - 1;
            buttonRect.y = buttonRect.y + 1;
            buttonRect.height = buttonRect.height - 2;
            Rectangle buttonRect1 = buttonRect.getBounds();
            buttonRect1.width = buttonRect1.width - 3;
            DrawUtil.drawPlainRect(gr, buttonRect1, DrawUtil.COLOR_BUTTON, 1, DrawUtil.COLOR_BUTTON);
            DrawUtil.drawPlainRoundRect(gr, buttonRect, DrawUtil.COLOR_BUTTON, 1, DrawUtil.COLOR_BUTTON);
        } else {
            buttonRect.x = rect.x + rect.width - button_width;
            DrawUtil.drawPlainRect(gr, buttonRect, DrawUtil.COLOR_BUTTON, 1, DrawUtil.COLOR_BUTTON);
        }

        DrawUtil.drawLine(gr, buttonRect.x, buttonRect.y, buttonRect.x, buttonRect.y + buttonRect.height - 1, DrawUtil.COLOR_DARK);
        DrawUtil.drawLine(gr, buttonRect.x + 1, buttonRect.y, buttonRect.x + 1, buttonRect.y + buttonRect.height - 1, DrawUtil.COLOR_BASE);
        DrawUtil.drawLine(gr, buttonRect.x + 1, buttonRect.y + buttonRect.height / 2, buttonRect.x + buttonRect.width - 1, buttonRect.y + buttonRect.height / 2, DrawUtil.COLOR_DARK);
        DrawUtil.drawLine(gr, buttonRect.x + 1, buttonRect.y + buttonRect.height / 2 + 1, buttonRect.x + buttonRect.width - 1, buttonRect.y + buttonRect.height / 2 + 1, DrawUtil.COLOR_BASE);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.IntProperty value = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "value");

        AdsUIProperty.IntProperty minimum = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "minimum");
        boolean isCurValMin = (value.value <= minimum.value);

        AdsUIProperty.IntProperty maximum = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "maximum");
        boolean isCurValMax = (value.value >= maximum.value);

        String label = getLabel(node, Integer.valueOf(value.value).toString());
        paint(gr, r, node, label, isCurValMin, isCurValMax);
    }

    private String getLabel(RadixObject node, String strValue) {
        AdsUIProperty.LocalizedStringRefProperty suffix_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "suffix");
        String suffix = getTextById(node, suffix_prop.getStringId());

        AdsUIProperty.LocalizedStringRefProperty prefix_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "prefix");
        String prefix = getTextById(node, prefix_prop.getStringId());

        return prefix + strValue + suffix;
    }

    public void paint(Graphics2D gr, Rectangle r, RadixObject node, String text, boolean isCurValMin, boolean isCurValMax) {
        Rectangle textRect = r.getBounds();
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        AdsUIProperty.BooleanProperty readOnly = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "readOnly");
        if (readOnly.value) {
            isCurValMin = true;
            isCurValMax = true;
        }

        AdsUIProperty.RectProperty defaultSize = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
        int icon_width = defaultSize.height - defaultSize.height / 3;
        int icon_height = defaultSize.height - defaultSize.height / 3;

        drawButtonIcons(gr, textRect, icon_width, icon_height, enabled.value, isCurValMin, isCurValMax);

        AdsUIProperty.SetProperty alignment = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "alignment");
        UIEnum[] values = alignment.getValues();
        EAlignment ha = (EAlignment) values[0];
        EAlignment va = (EAlignment) values[1];

        textRect.x = textRect.x + 4;
        textRect.width = textRect.width - icon_width - 4;
        Shape clip = gr.getClip();
        gr.clipRect(textRect.x, textRect.y, textRect.width, textRect.height);
        DrawUtil.drawText(gr, textRect, ha, va, enabled.value, text);
        gr.setClip(clip);
    }

    private void drawButtonIcons(Graphics2D gr, Rectangle textRect, int icon_width, int icon_height, boolean enabled, boolean isCurValMin, boolean isCurValMax) {
        RadixIcon image = RadixWareIcons.WIDGETS.ARROW_UP;
        if ((!enabled) || (isCurValMax)) {
            image = RadixWareIcons.WIDGETS.ARROW_UP_DISABLE;
        }

        int icon_pos_x = textRect.x + textRect.width - icon_width;
        int icon_pos_y = textRect.y + textRect.height / 4 - icon_height / 2;

        drawIcon(gr, image, icon_width, icon_height, icon_pos_x, icon_pos_y);

        image = RadixWareIcons.WIDGETS.ARROW_DOWN;
        if ((!enabled) || (isCurValMin)) {
            image = RadixWareIcons.WIDGETS.ARROW_DOWN_DISABLE;
        }
        icon_pos_y = textRect.y + (textRect.height) * 3 / 4 - icon_height / 2;
        drawIcon(gr, image, icon_width, icon_height, icon_pos_x, icon_pos_y);
    }

    private void drawIcon(Graphics2D gr, RadixIcon image, int width, int height, int x, int y) {
        Image pixmap = image.getImage(width, height);
        Rectangle iconRect = new Rectangle(x, y, width, height);
        gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
    }
}
