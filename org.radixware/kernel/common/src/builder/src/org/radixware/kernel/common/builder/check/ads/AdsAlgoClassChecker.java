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

package org.radixware.kernel.common.builder.check.ads;

import java.text.MessageFormat;
import org.radixware.kernel.common.builder.check.ads.clazz.AdsClassChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;


@RadixObjectCheckerRegistration
public class AdsAlgoClassChecker<T extends AdsAlgoClassDef> extends AdsClassChecker<T> {

    public AdsAlgoClassChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsAlgoClassDef.class;
    }

    @Override
    public void check(T algo, IProblemHandler problemHandler) {
        super.check(algo, problemHandler);
        if (algo.getProcessId() != null && algo.getProcessDef() == null) {
            error(algo, problemHandler, MessageFormat.format("Process is not found: <#{0}>", algo.getProcessId()));
        }
        if (algo.getReplacementId() != null && algo.getReplacementDef() == null) {
            error(algo, problemHandler, MessageFormat.format("Replacement is algorithm not found: <#{0}>", algo.getReplacementId()));
        }
        if (algo.getReplacedId() != null && algo.getReplacedDef() == null) {
            error(algo, problemHandler, MessageFormat.format("Replaced is algorithm not found: <#{0}>", algo.getReplacedId()));
        }
    }

    @Override
    protected void unresolvedSuperclassReferenceDetails(T clazz, AdsType ref, IProblemHandler problemHandler) {
        super.unresolvedSuperclassReferenceDetails(clazz, ref, problemHandler);
        AdsClassDef anc = findBaseHandler(clazz, AdsAlgoClassDef.PREDEFINED_ID);
        if (anc == null) {
            error(clazz, problemHandler, "Algorithm class must be based on publishing of " + AdsAlgoClassDef.PLATFORM_CLASS_NAME);
        } else {
            AdsUtils.checkAccessibility(clazz, anc, false, problemHandler);
            error(clazz, problemHandler, "Algorithm class must extend " + anc.getQualifiedName());
        }
    }
}
