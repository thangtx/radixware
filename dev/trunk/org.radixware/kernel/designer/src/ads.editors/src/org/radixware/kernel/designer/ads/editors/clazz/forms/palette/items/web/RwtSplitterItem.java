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
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtSplitterItem extends Item {

    public static final RwtSplitterItem DEFAULT = new RwtSplitterItem();

    public RwtSplitterItem() {
        super(Group.GROUP_WEB_CONTAINERS, "Split Panel", AdsMetaInfo.RWT_SPLITTER);
    }

    @Override
    public Rectangle adjustLayoutGeometry(RadixObject node, Rectangle defaultRect) {
        Rectangle r = defaultRect.getBounds();

        r.x += LayoutUtil.MIN_MARGIN;
        r.y += LayoutUtil.MIN_MARGIN;
        r.width -= LayoutUtil.MIN_MARGIN * 2;
        r.height -= LayoutUtil.MIN_MARGIN * 2;

        return r;
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        super.paintBackground(gr, r, node);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_MID_LIGHT, 1, DrawUtil.COLOR_WINDOW);
    }
}
