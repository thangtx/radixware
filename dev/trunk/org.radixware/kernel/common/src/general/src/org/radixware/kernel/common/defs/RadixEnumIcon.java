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

package org.radixware.kernel.common.defs;

import org.radixware.kernel.common.resources.icons.*;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.IKernelEnum;


final class RadixEnumIcon extends RadixIcon {

    private static final Map<IKernelEnum, RadixEnumIcon> instances = new HashMap<>();

    private RadixEnumIcon(IKernelEnum value) {
        super(value.getClass().getSimpleName().substring(1).toLowerCase() + "/" + ((Enum) value).name().toLowerCase() + ".svg");
    }

    static RadixEnumIcon getForValue(IKernelEnum val) {
        synchronized (instances) {
            RadixEnumIcon icon = instances.get(val);
            if (icon == null) {
                icon = new RadixEnumIcon(val);
                instances.put(val, icon);
            }
            return icon;
        }
    }
}
