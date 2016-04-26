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

package org.radixware.kernel.designer.common.general.displaying;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.events.RadixEventSource;

/**
 * Icon support allows you to handle definition icon changes
 * Default implementstion does not listen for any of events and never changes the icon
 */
public class IconSupport extends RadixEventSource<IIconChangeListener, IconChangedEvent> {

    public static class Factory implements IIconSupportFactory<RadixObject> {

        @Override
        public IconSupport newInstance(RadixObject radixObject) {
            return new IconSupport(radixObject);
        }
    }
    private RadixObject radixObject;

    protected IconSupport(RadixObject radixObject) {
        this.radixObject = radixObject;
    }

    public RadixIcon getIcon() {
        return radixObject.getIcon();
    }

    protected void notifyChanged() {
        this.fireEvent(new IconChangedEvent());
    }
}
