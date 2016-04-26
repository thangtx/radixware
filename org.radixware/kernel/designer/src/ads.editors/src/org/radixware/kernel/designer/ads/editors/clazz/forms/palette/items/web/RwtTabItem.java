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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TabItem;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;

public class RwtTabItem extends TabItem {

    public static final RwtTabItem DEFAULT = new RwtTabItem();

    public RwtTabItem() {
        super(Group.GROUP_WEB_CONTAINERS, AdsMetaInfo.RWT_TAB_SET);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef tab;
        AdsRwtWidgetDef widget = new AdsRwtWidgetDef(AdsMetaInfo.RWT_TAB_SET);

        tab = new AdsRwtWidgetDef(AdsMetaInfo.RWT_TAB_SET_TAB);
        tab.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("title", AdsUIUtil.createStringDef(context, "Tab 1", "Tab 1")));
        tab.setName("tab");
        widget.getWidgets().add(tab);

        tab = new AdsRwtWidgetDef(AdsMetaInfo.RWT_TAB_SET_TAB);
        tab.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("title", AdsUIUtil.createStringDef(context, "Tab 2", "Tab 2")));
        tab.setName("tab_2");
        widget.getWidgets().add(tab);

        widget.getProperties().add(new AdsUIProperty.IntProperty("currentIndex", 0));
        return widget;
    }
}
