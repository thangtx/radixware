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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;

public class PropLabelItem extends Item {

    public static final PropLabelItem DEFAULT = new PropLabelItem();

  
    public PropLabelItem() {
        super(Group.GROUP_RADIX_WIDGETS, NbBundle.getMessage(PropLabelItem.class, "Prop_Label"), AdsMetaInfo.PROP_LABEL_CLASS);
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        return super.adjustHintSize(node, defaultSize);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.PropertyRefProperty property_prop = (AdsUIProperty.PropertyRefProperty) AdsUIUtil.getUiProperty(node, "property");
        IModelPublishableProperty property = property_prop.findProperty();
        String label = NbBundle.getMessage(PropLabelItem.class, "Title_Not_Specified");
        if (property instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) property).isLocal()) {
            property = ((AdsPropertyPresentationPropertyDef) property).findServerSideProperty();
        }

        if (property instanceof IAdsPresentableProperty) {
            IAdsPresentableProperty sProp = (IAdsPresentableProperty) property;
            ServerPresentationSupport ps = sProp.getPresentationSupport();
            if (ps != null) {
                label = ps.getPresentation().getTitle(getLanguage());
            }
        }
        LabelItem.DEFAULT.paint(gr, r, node, label);

    }
}
