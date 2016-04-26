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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem.PushButtonPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem.PushButtonPropertyCollector;


public class RwtActionItem extends WidgetItem {

    public static final RwtActionItem DEFAULT = new RwtActionItem();

    public RwtActionItem() {
        super(Group.GROUP_WEB_ACTIONS, "Action", AdsMetaInfo.RWT_ACTION, new PushButtonPainter(), new PushButtonPropertyCollector());
    }

    public static Dimension calcActionSize(FontMetrics fontMetrics, RadixIcon icon, String text) {

        int iconH = 0;
        int iconW = 0;
        if (icon != null) {
            iconH = icon.getIcon().getIconHeight();
            iconW = icon.getIcon().getIconWidth();
        }

        int textW = 0;
        if (text != null && !text.isEmpty()) {
            textW = fontMetrics.stringWidth(text) + 8;
        }

        final int height = Math.max(30, Math.max(iconH, fontMetrics.getHeight()));
        final int width = Math.max(30, textW + iconW);

        return new Dimension(width, height);
    }

    public static Dimension calcActionSize(FontMetrics fontMetrics, AdsRwtWidgetDef action) {
        final AdsUIProperty.ImageProperty iconProp =
                (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(action, "icon");

        final AdsUIProperty textProp = AdsUIUtil.getUiProperty(action, "text");

        RadixIcon icon = null;
        if (iconProp != null) {
            icon = Item.getIconById(action, iconProp.getImageId());
        }

        final String text;
        if (textProp instanceof AdsUIProperty.LocalizedStringRefProperty) {
            text = getTextById(action, ((AdsUIProperty.LocalizedStringRefProperty) textProp).getStringId());
        } else if (textProp instanceof AdsUIProperty.StringProperty) {
            text = ((AdsUIProperty.StringProperty) textProp).value;
        } else {
            text = null;
        }

        return RwtActionItem.calcActionSize(fontMetrics, icon, text);
    }

    public static AdsRwtWidgetDef findAction(Module module, final Id id) {
        final List<Definition> result = RadixObjectsUtils.collectAllInside(module, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsRwtWidgetDef
                        && Objects.equals(((AdsRwtWidgetDef) radixObject).getId(), id)) {
                    return true;
                }
                return false;
            }
        });
        return !result.isEmpty() ? (AdsRwtWidgetDef) result.get(0) : null;
    }
}
