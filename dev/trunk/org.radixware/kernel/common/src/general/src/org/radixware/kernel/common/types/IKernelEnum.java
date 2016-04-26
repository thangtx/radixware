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

package org.radixware.kernel.common.types;

import java.util.List;

/**
 * This is the base interface for all Radix enums: Kernel, ADS, DDS
 */
public interface IKernelEnum<T extends Comparable> {
    // overrided in each const

    public String getName();

    public T getValue();

    public boolean isInDomain(Id id);

    public boolean isInDomains(List<Id> ids);
}
