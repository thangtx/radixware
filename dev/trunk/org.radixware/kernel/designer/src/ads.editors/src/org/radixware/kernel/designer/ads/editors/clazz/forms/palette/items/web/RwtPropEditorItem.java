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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtPropEditorItem extends Item {

    public static final RwtPropEditorItem DEFAULT = new RwtPropEditorItem();

    public RwtPropEditorItem() {
        super(Group.GROUP_WEB_RADIX_WIDGETS, "Property Editor", AdsMetaInfo.RWT_PROP_EDITOR);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        DrawUtil.drawPlainRoundRect(graphics, rect, DrawUtil.COLOR_DARK, 1, Color.WHITE);
    }

    @Override
    public void paintBorder(Graphics2D graphics, Rectangle rect, RadixObject node) {
        super.paintBorder(graphics, rect, node);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        if (node instanceof AdsRwtWidgetDef) {
            AdsUIProperty prop = AdsUIUtil.getUiProperty(node, "property");
            String label = null;
            if (prop instanceof AdsUIProperty.PropertyRefProperty) {
                IModelPublishableProperty property = ((AdsUIProperty.PropertyRefProperty) prop).findProperty();
                if (property instanceof AdsDefinition) {
                    label = ((AdsDefinition) property).getQualifiedName();
                } else {
                    label = "<Property not specified>";
                }
            }
            if (label != null) {
                EAlignment ha = EAlignment.AlignLeft;
                EAlignment va = EAlignment.AlignVCenter;
                Shape clip = graphics.getClip();
                Rectangle textRect = new Rectangle(rect.x + 5, rect.y + 1, rect.width - 5, rect.height - 2);
                graphics.clipRect(rect.x + 5, rect.y + 1, rect.width - 5, rect.height - 2);
                DrawUtil.drawText(graphics, textRect, ha, va, true, label);
                graphics.setClip(clip);
            }

        }

        super.paintWidget(graphics, rect, node);
    }
}
