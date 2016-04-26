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
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;

public class HorizontalSpacerItem extends Item {

    public static final HorizontalSpacerItem DEFAULT = new HorizontalSpacerItem();

    public HorizontalSpacerItem() {
        super(Group.GROUP_SPACERS, NbBundle.getMessage(HorizontalSpacerItem.class, "Horizontal_Spacer"), AdsDefinitionIcon.WIDGETS.HORIZONTAL_SPACER, AdsMetaInfo.SPACER_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsLayout.SpacerItem spacer = (AdsLayout.SpacerItem) super.createObjectUI(context);
        spacer.getProperties().add(new AdsUIProperty.EnumValueProperty("orientation", "Horizontal"));
        spacer.getProperties().add(new AdsUIProperty.SizeProperty("sizeHint", 40, 20));
        return spacer;
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        gr.setColor(new Color(0, 0, 255));
        gr.drawLine(r.x, r.y + r.height / 2 - 10, r.x, r.y + r.height / 2 + 10);
        for (int i = 1; i < r.width; i++) {
            gr.drawLine(r.x + i, r.y + r.height / 2 + 3 - 2 * (i % 3), r.x + i, r.y + r.height / 2 + 3 - 2 * (i % 3) - 1);
        }
        gr.drawLine(r.x + r.width - 1, r.y + r.height / 2 - 10, r.x + r.width - 1, r.y + r.height / 2 + 10);
    }
}
