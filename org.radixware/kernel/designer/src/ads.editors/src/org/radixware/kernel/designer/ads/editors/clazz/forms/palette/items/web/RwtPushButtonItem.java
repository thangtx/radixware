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
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem.PushButtonPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem.PushButtonPropertyCollector;


public class RwtPushButtonItem extends WidgetItem {

    public static final RwtPushButtonItem DEFAULT = new RwtPushButtonItem();

    public RwtPushButtonItem() {
        super(Group.GROUP_WEB_BUTTONS, "Push Button", AdsMetaInfo.RWT_UI_PUSH_BUTTON, new PushButtonPainter(), new PushButtonPropertyCollector());
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("text", AdsUIUtil.createStringDef(context, "Click Me!", "Нажми!")));
        return widget;
    }
}
