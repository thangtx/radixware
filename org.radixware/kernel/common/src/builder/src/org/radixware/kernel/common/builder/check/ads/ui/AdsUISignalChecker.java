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

package org.radixware.kernel.common.builder.check.ads.ui;

import org.radixware.kernel.common.builder.check.common.DefinitionChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsUISignalChecker<T extends AdsUISignalDef> extends DefinitionChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsUISignalDef.class;
    }

    @Override
    public void check(T signal, IProblemHandler problemHandler) {
        super.check(signal, problemHandler);
        for (AdsTypeDeclaration type : signal.getTypes()) {
            type.check(signal, problemHandler);
        }
    }
}
