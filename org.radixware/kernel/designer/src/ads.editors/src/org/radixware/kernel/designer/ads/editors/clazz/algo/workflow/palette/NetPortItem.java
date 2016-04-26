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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.ObjectFactory;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPin;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;


class NetPortItem extends AppItem {

    public static final String CLASS_NAME = AdsAppObject.NET_FORM_CLASS_NAME;

    public NetPortItem() {
        super(CLASS_NAME, Group_.GROUP_APPBLOCKS, RadixResourceBundle.getMessage(NetPortItem.class, "NetPort"), AdsDefinitionIcon.WORKFLOW.NETPORT);
    }

    @Override
    public boolean init(AdsAppObject node) {
        super.init(node);
        node.setSourcePins(Arrays.asList(
                ObjectFactory.createPin(Item.ICON_DISCONNECT),
                ObjectFactory.createPin(Item.ICON_RECV),
                ObjectFactory.createPin(Item.ICON_NONE)
                ));
        return true;
    }

    @Override
    public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        return new TabbedPanel(node,
                Arrays.asList(
                (EditorDialog.EditorPanel)new PreExecutePanel(node),
                (EditorDialog.EditorPanel)new PostExecutePanel(node),
                (EditorDialog.EditorPanel)new NetPortPanel(node)
                ));
    }

    @Override
    public void sync(AdsAppObject node) {
        List<AdsPin> pins = node.getSourcePins();
        AdsAppObject.Prop timeout = node.getPropByName("recvTimeout");
        if (timeout.getValue() != null) {
            if (pins.size() < 4) {
                pins.add(ObjectFactory.createPin(Item.ICON_TIMEOUT));
                node.setSourcePins(pins);
            }
        } else {
            if (pins.size() == 4) {
                pins.remove(3);
                node.setSourcePins(pins);
            }
        }
    }
}