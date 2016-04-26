/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.build.xbeans;

/**
 *
 * @author akrylov
 */
public class XBeansValueGetter {
    
    public static final Long getLongValue(org.apache.xmlbeans.SimpleValue target) {
        return target == null || target.isNil() ? null : new Long(target.getLongValue());
    }
    
    public static final Boolean getBooleanValue(org.apache.xmlbeans.SimpleValue target) {
        return target == null || target.isNil() ? null : (target.getBooleanValue() ? Boolean.TRUE : Boolean.FALSE);
    }

    public static final Character getCharValue(org.apache.xmlbeans.SimpleValue target) {
        return target == null || target.isNil() ? null : (target.getStringValue().isEmpty() ? null : target.getStringValue().charAt(0));
    }
}
