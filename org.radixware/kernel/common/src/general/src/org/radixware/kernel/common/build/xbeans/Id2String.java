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

package org.radixware.kernel.common.build.xbeans;

import org.radixware.kernel.common.types.Id;


public class Id2String implements TypeMapper {

    @Override
    public Object convertTo(Object origin) {
        if (origin instanceof Id) {
            return origin.toString();
        } else {
            return null;
        }
    }

    @Override
    public Object convertFrom(Object converted) {
        if (converted instanceof String) {
            return Id.Factory.loadFrom((String) converted);
        } else {
            return null;
        }
    }
}
