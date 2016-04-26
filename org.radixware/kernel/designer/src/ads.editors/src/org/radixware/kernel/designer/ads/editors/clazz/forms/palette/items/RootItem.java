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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.defs.ads.ui.rwt.*;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;


public class RootItem extends Item {

    public static final RootItem DEFAULT = new RootItem();

    public RootItem() {
        super(null, null, null, (String) null);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_MID_LIGHT, 1, DrawUtil.COLOR_WINDOW);
        if (node instanceof AdsRwtWidgetDef) {
            AdsRwtUIDef uiDef = (AdsRwtUIDef) AdsUIUtil.getUiDef(node);
            if ((uiDef instanceof AdsRwtCustomDialogDef || uiDef instanceof AbstractRwtCustomFormDialogDef) && !(uiDef instanceof AdsRwtCustomWidgetDef)) {
                AdsUIProperty.BooleanProperty visible = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "buttonBoxVisible");
                AdsUIProperty.SetProperty standard_buttons = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "standardButtons");

                UIEnum[] buttons = standard_buttons.getValues();
                boolean isNoBtn = isNoButton(buttons);

                if (visible.value) {
                    if (!isNoBtn) {
                        Rectangle area = new Rectangle(0, r.height - 30, r.width, 30);
                        DrawUtil.drawPlainRect(gr, area, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_DARK);
                        DialogButtonBoxItem.paint(gr, area, node, true);
                    } else {
                        AdsUIUtil.setUiProperty(node, visible);
                    }
                } 
            }
        }
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        if (node instanceof AdsRwtWidgetDef) {
            AdsRwtUIDef uiDef = (AdsRwtUIDef) AdsUIUtil.getUiDef(node);
            if ((uiDef instanceof AdsRwtCustomDialogDef || uiDef instanceof AbstractRwtCustomFormDialogDef) && !(uiDef instanceof AdsRwtCustomWidgetDef)) {
                AdsUIProperty.BooleanProperty visible = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "buttonBoxVisible");
                AdsUIProperty.SetProperty standard_buttons = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "standardButtons");
                UIEnum[] buttons = standard_buttons.getValues();
                boolean isNoBtn = isNoButton(buttons);

                if (visible.value) {
                    if (!isNoBtn) {
                        Rectangle area = new Rectangle(0, r.height - 30, r.width, 30);
                        DialogButtonBoxItem.paint(gr, area, node, false);
                    } else {
                        AdsUIUtil.setUiProperty(node, visible);
                    }
                }
            }
        }
//        DrawUtil.drawShadeRect(gr, r, false, 0, 0, DrawUtil.COLOR_WINDOW);
    }

    private static boolean isNoButton(UIEnum[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == EStandardButton.NoButton) {
                return true;
            }
        }
        return false;
    }
}
