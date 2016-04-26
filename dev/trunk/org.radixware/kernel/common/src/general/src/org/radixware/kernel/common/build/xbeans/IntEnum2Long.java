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

import java.lang.reflect.InvocationTargetException;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;

import org.radixware.kernel.common.types.IKernelIntEnum;

class IntEnum2Long implements TypeMapper {

    Class cls;

    private IKernelIntEnum valueOf(Long v) {
        try {
            return (IKernelIntEnum) cls.getMethod("getForValue", new Class[]{Long.class}).invoke(null, new Object[]{v});            
        } catch (IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchMethodException e) {
            throw new RadixError("Xml type mapper error", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NoConstItemWithSuchValueError) {
                throw new RadixError("Xml type mapper error: " + cause.getMessage(), cause);
            } else {
                throw new RadixError("Xml type mapper error", e);
            }
        }
    }

    IntEnum2Long(Class cls) {
        this.cls = cls;
    }

    @Override
    public Object convertFrom(Object converted) {
        return valueOf((Long) converted);
    }

    @Override
    public Object convertTo(Object origin) {
        return ((IKernelIntEnum) origin).getValue();
    }
}
