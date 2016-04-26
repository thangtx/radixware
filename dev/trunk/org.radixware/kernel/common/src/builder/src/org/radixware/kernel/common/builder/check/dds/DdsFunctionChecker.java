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
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class DdsFunctionChecker<T extends DdsFunctionDef> extends DdsDefinitionChecker<T> {

    public DdsFunctionChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsFunctionDef.class;
    }

    @Override
    public void check(T function, IProblemHandler problemHandler) {
        super.check(function, problemHandler);

        EValType valType = function.getResultValType();
        if (valType != null) {
            if (function.getResultDbType() == null || function.getResultDbType().isEmpty()) {
                error(function, problemHandler, "Database result type not defined");
            }
            if (!ValTypes.DDS_FUNCTION_RESULT_TYPES.contains(valType)) {
                error(function, problemHandler, "Illegal result type");
            }
        }
    }
}
