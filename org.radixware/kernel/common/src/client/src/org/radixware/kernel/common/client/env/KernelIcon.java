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

package org.radixware.kernel.common.client.env;

import java.util.WeakHashMap;
import org.radixware.kernel.common.resources.RadixWareIcons;

import org.radixware.kernel.common.resources.icons.RadixIcon;


public class KernelIcon extends ClientIcon {

    private static final WeakHashMap<RadixIcon, KernelIcon> cache = new WeakHashMap<RadixIcon, KernelIcon>();

    private KernelIcon(RadixIcon icon) {
        super(icon.getResourceUri(), icon.getResourceUri().endsWith(".svg"));
    }

    public static KernelIcon getInstance(RadixIcon icon) {
        synchronized (cache) {
            RadixIcon theIcon = icon == null ? RadixWareIcons.WORKFLOW.WIDGET_THROW : icon;
            KernelIcon kicon = cache.get(theIcon);
            if (kicon == null) {
                kicon = new KernelIcon(theIcon);
                cache.put(theIcon, kicon);
            }
            return kicon;
        }
    }
}
