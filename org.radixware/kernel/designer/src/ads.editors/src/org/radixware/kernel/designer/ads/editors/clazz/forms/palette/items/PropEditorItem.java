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

import java.awt.Dimension;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class PropEditorItem extends Item {

    public static final PropEditorItem DEFAULT = new PropEditorItem();

    

    public PropEditorItem() {
        super(Group.GROUP_RADIX_WIDGETS, NbBundle.getMessage(PropEditorItem.class, "Prop_Editor"), AdsMetaInfo.PROP_EDITOR_CLASS);
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        return super.adjustHintSize(node, defaultSize);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle rect = r.getBounds();
        DrawUtil.drawPlainRoundRect(gr, rect, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_LIGHT);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle textRect = r.getBounds();
        EAlignment ha = EAlignment.AlignHCenter;
        EAlignment va = EAlignment.AlignVCenter;

        AdsUIProperty.PropertyRefProperty property_prop = (AdsUIProperty.PropertyRefProperty) AdsUIUtil.getUiProperty(node, "property");
        IModelPublishableProperty property = property_prop.findProperty();
        String label = NbBundle.getMessage(PropEditorItem.class, "Unknown");
        if (property != null) {
            label = property.getName();
        }
        DrawUtil.drawText(gr, textRect, ha, va, false, label);
    }
}
