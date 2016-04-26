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

class InverseTypeMapper implements TypeMapper {

    protected TypeMapper other;

    public InverseTypeMapper(final TypeMapper other) {
        this.other = other;
    }

    @Override
    public Object convertFrom(final Object converted) {
        return other.convertTo(converted);
    }

    @Override
    public Object convertTo(final Object origin) {
        return other.convertFrom(origin);
    }
}
