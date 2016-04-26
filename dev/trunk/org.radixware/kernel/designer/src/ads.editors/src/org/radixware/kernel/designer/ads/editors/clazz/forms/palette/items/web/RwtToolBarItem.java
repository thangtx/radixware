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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class RwtToolBarItem extends Item {

    public static final RwtToolBarItem DEFAULT = new RwtToolBarItem();

    public RwtToolBarItem() {
        super(Group.GROUP_WEB_ACTIONS, "Tool Bar", AdsMetaInfo.RWT_TOOL_BAR);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        if (node instanceof AdsRwtWidgetDef) {
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) node;
            AdsUIProperty.IdListProperty prop = (AdsUIProperty.IdListProperty) AdsUIUtil.getUiProperty(node, "actions");
            Rectangle r = new Rectangle(rect);
            r.width = 26;
            r.height = 26;
            if (prop != null) {
                AdsRwtUIDef def = (AdsRwtUIDef) w.getOwnerUIDef();

                for (Id id : prop.getIds()) {
                    AdsRwtWidgetDef target = def.getWidget().findWidgetById(id);
                    if (target != null && target.isActionWidget()) {
                        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(target, "icon");
                        RadixIcon image = null;
                        if (icon != null) {
                            image = getIconById(node, icon.getImageId());
                        } else {
                            image = RadixWareDesignerIcon.WIDGETS.ARROW_UP;
                        }
                        PushButtonItem.PushButtonPainter.drawIcon(graphics, r, image, new Dimension(16, 16), "", EToolButtonStyle.ToolButtonIconOnly);
                        r.x += 26;
                    }
                }
            }
            if (r.x != rect.x) {
                DrawUtil.drawPlainRoundRect(graphics, new Rectangle(rect.x, rect.y, r.x - rect.x, r.height), Color.GREEN, 1, null);
            } else {
                DrawUtil.drawPlainRoundRect(graphics, rect, DrawUtil.COLOR_DARK, 1, Color.ORANGE);
            }
        } else {
            super.paintWidget(graphics, rect, node);
        }
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        DrawUtil.drawPlainRect(graphics, rect, DrawUtil.COLOR_BASE, 1, DrawUtil.COLOR_BASE);
    }

    public static EOrientation getOrientation(RadixObject action) {
        final AdsUIProperty property = AdsUIUtil.getUiProperty(action, "orientation");
        if (property instanceof AdsUIProperty.EnumValueProperty) {
            return (EOrientation) ((AdsUIProperty.EnumValueProperty) property).value;
        }
        return null;
    }
}
