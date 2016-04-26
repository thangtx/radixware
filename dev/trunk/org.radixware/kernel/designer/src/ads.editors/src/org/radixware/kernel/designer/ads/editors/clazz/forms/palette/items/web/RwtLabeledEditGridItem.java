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
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.RwtLabeledEditGridLayout;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.RwtLabeledEditGridLayout.CellInfo;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtLabeledEditGridItem extends Item {

    public static final RwtLabeledEditGridItem DEFAULT = new RwtLabeledEditGridItem();

    public RwtLabeledEditGridItem() {
        super(Group.GROUP_WEB_CONTAINERS, "Labeled Grid", AdsMetaInfo.RWT_LABELED_EDIT_GRID);
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        DrawUtil.drawPlainRect(graphics, rect, DrawUtil.COLOR_BASE, 1, DrawUtil.COLOR_BASE);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        List<CellInfo> cells = RwtLabeledEditGridLayout.computeCellInfo((AdsRwtWidgetDef) node,rect);

        for (CellInfo cell : cells) {
            Rectangle textRect = new Rectangle(cell.bounds);
            textRect.width = cell.labelWidth;
            DrawUtil.drawText(graphics, textRect, EAlignment.AlignLeft, EAlignment.AlignVCenter, true, cell.label);
        }

    }
}
