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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * SQML в DDS дефинициях.
 * Служит для автоматизации и инкапсуляции установки владельца, задает {@linkplain ISqmlEnvironment}.
 */
class DdsSqml extends Sqml {

    public DdsSqml(DdsDefinition owner) {
        super(owner);

        if (owner == null) {
            throw new NullPointerException("DdsSqml owner must not be null.");
        }

        final DdsSqmlEnvironment environment = new DdsSqmlEnvironment(owner);
        setEnvironment(environment);
    }

    public void unsetOwner() {
        setContainer(null);
        setEnvironment(null);
    }

}
