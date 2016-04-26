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
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class DdsPlSqlObjectChecker<T extends DdsPlSqlObjectDef> extends DdsDefinitionChecker<T> {

    public DdsPlSqlObjectChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsPlSqlObjectDef.class;
    }

    @Override
    public void check(T plSqlObject, IProblemHandler problemHandler) {
        super.check(plSqlObject, problemHandler);

        checkForDbNameDuplication(plSqlObject, problemHandler);
    }
}

