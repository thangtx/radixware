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
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtContainerItem extends Item {

    public static final RwtContainerItem DEFAULT = new RwtContainerItem();

    public RwtContainerItem() {
        super(Group.GROUP_WEB_CONTAINERS, "Container with Anchors", AdsMetaInfo.RWT_UI_CONTAINER);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);

        return widget;
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        DrawUtil.drawPlainRect(graphics, rect, DrawUtil.COLOR_BASE, 1, DrawUtil.COLOR_BASE);
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) node;
    }
}
