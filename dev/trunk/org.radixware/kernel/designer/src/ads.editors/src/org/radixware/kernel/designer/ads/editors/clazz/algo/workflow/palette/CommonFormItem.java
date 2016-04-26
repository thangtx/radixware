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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.ObjectFactory;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPin;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.resources.icons.RadixIcon;


abstract class CommonFormItem extends AppItem {

    public CommonFormItem(String clazz, Group_ group, String title, RadixIcon icon) {
        super(clazz, group, title, icon);
    }

    @Override
    public boolean init(AdsAppObject node) {
        super.init(node);
        
        List<AdsPin> pins = new ArrayList<AdsPin>();
        AdsAppObject.Prop submitVariants = node.getPropByName("submitVariants");

        ValAsStr submitValue = submitVariants.getValue();
        StringTokenizer t = new StringTokenizer(submitValue == null ? "" : String.valueOf(submitValue), ";");
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            if (token != null && !token.isEmpty()) {
                pins.add(ObjectFactory.createPin(token));
            }
        }

        AdsAppObject.Prop timeout = node.getPropByName("timeout");
        if (timeout.getValue() != null) {
            pins.add(ObjectFactory.createPin(Item.ICON_TIMEOUT));
        }

        node.setSourcePins(pins);
        return true;
    }

    @Override
    public void sync(AdsAppObject node) {
        Map<String, AdsPin> icon2Pin = new HashMap<String, AdsPin>();

        for (AdsPin pin : node.getSourcePins())
            icon2Pin.put(pin.getIconName(), pin);

        AdsAppObject.Prop submitVariants = node.getPropByName("submitVariants");
        List<AdsPin> pins = new ArrayList<AdsPin>();

        ValAsStr value = submitVariants.getValue();
        StringTokenizer t = new StringTokenizer(value == null ? "" : String.valueOf(value), ";");
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            if (token != null && !token.isEmpty()) {
                AdsPin pin = icon2Pin.get(token);
                pins.add(pin != null ? pin : ObjectFactory.createPin(token));
            }
        }

        AdsAppObject.Prop timeout = node.getPropByName("timeout");
        if (timeout.getValue() != null) {
            AdsPin pin = icon2Pin.get(Item.ICON_TIMEOUT);
            pins.add(pin != null ? pin : ObjectFactory.createPin(Item.ICON_TIMEOUT));
        }

        node.setSourcePins(pins);
    }
}