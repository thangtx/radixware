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
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;

public class GridLayoutItem extends Item {

    public static final GridLayoutItem DEFAULT = new GridLayoutItem();

    

    public GridLayoutItem() {
        super(Group.GROUP_LAYOUTS, NbBundle.getMessage(GridLayoutItem.class, "Grid_Layout"), AdsMetaInfo.GRID_LAYOUT_CLASS);
    }

    private final static Color BORDER_COLOR = new Color(0, 255, 0);

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        Color oldColor = gr.getColor();
        gr.setColor(BORDER_COLOR);
        //gr.drawRect(r.x - 1, r.y - 1, r.width + 1, r.height + 1);
        gr.drawRect(r.x, r.y, r.width - 1, r.height - 1);
        gr.setColor(oldColor);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
    }
}
