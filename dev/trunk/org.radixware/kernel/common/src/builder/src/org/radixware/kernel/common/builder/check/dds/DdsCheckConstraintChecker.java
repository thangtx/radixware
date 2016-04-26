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

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class DdsCheckConstraintChecker<T extends DdsCheckConstraintDef> extends DdsConstraintChecker<T> {

    public DdsCheckConstraintChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsCheckConstraintDef.class;
    }

    @Override
    public void check(T constraint, IProblemHandler problemHandler) {
        super.check(constraint, problemHandler);

        checkForDbNameDuplication(constraint, problemHandler);
    }
}
