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

public class VerticalSpacerItem extends Item {

    public static final VerticalSpacerItem DEFAULT = new VerticalSpacerItem();

    public VerticalSpacerItem() {
        super(Group.GROUP_SPACERS, NbBundle.getMessage(VerticalSpacerItem.class, "Vertical_Spacer"), AdsDefinitionIcon.WIDGETS.VERTICAL_SPACER, AdsMetaInfo.SPACER_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsLayout.SpacerItem spacer = (AdsLayout.SpacerItem) super.createObjectUI(context);
        spacer.getProperties().add(new AdsUIProperty.EnumValueProperty("orientation", "Vertical"));
        spacer.getProperties().add(new AdsUIProperty.SizeProperty("sizeHint", 20, 40));
        return spacer;
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        gr.setColor(new Color(0, 0, 255));
        gr.drawLine(r.x + r.width / 2 - 10, r.y, r.x + r.width / 2 + 10, r.y);
        for (int i = 1; i < r.height; i++) {
            gr.drawLine(r.x + r.width / 2 + 3 - 2 * (i % 3), r.y + i, r.x + r.width / 2 + 3 - 2 * (i % 3) - 1, r.y + i);
        }
        gr.drawLine(r.x + r.width / 2 - 10, r.y + r.height - 1, r.x + r.width / 2 + 10, r.y + r.height - 1);
    }
}
