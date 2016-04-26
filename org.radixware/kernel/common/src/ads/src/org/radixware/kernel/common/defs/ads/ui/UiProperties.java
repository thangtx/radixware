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

package org.radixware.kernel.common.defs.ads.ui;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.schemas.ui.Property;


public class UiProperties extends RadixObjects<AdsUIProperty> {

    public UiProperties(RadixObject container) {
        super(container);
    }

    public void loadFrom(List<Property> list) {
        for (Property p : list) {
            AdsUIProperty wp = AdsUIProperty.Factory.loadFrom(p);
            if (wp != null) {
                add(wp);
            }
        }

    }

    public Property[] toXml() {
        ArrayList<Property> props = new ArrayList<Property>();
        for (AdsUIProperty p : this) {
            if (p.shouldStore()) {
                Property pp = Property.Factory.newInstance();
                p.appendTo(pp);
                props.add(pp);
            }
        }
        Property[] arr = new Property[props.size()];
        props.toArray(arr);

        return arr;
    }

    public AdsUIProperty getByName(String name) {
        if (name.equals(AdsUIProperty.AccessProperty.NAME)) {
            if (getContainer() instanceof AdsWidgetDef) {
                return new AdsUIProperty.AccessProperty((AdsWidgetDef) getContainer());
            }
        }
        for (AdsUIProperty prop : this) {
            if (name.equals(prop.getName())) {
                return prop;
            }
        }
        return null;
    }
}
