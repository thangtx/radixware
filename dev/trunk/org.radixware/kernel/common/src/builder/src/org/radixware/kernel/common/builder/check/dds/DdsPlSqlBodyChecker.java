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

package org.radixware.kernel.common.builder.check.dds;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;


@RadixObjectCheckerRegistration
public class DdsPlSqlBodyChecker<T extends DdsPlSqlBodyDef> extends DdsDefinitionChecker<T> {

    public DdsPlSqlBodyChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsPlSqlBodyDef.class;
    }

    @Override
    public void check(T header, IProblemHandler problemHandler) {
        // NOTHING
    }
}

