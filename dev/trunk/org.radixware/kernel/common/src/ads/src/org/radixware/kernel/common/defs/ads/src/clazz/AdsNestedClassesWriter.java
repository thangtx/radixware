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

package org.radixware.kernel.common.defs.ads.src.clazz;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.NestedClasses;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsNestedClassesWriter extends RadixObjectWriter<NestedClasses> {

    public AdsNestedClassesWriter(JavaSourceSupport support, NestedClasses classes, JavaSourceSupport.UsagePurpose usagePurpose) {
        super(support, classes, usagePurpose);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
    }

    @Override
    public boolean writeExecutable(CodePrinter printer) {
        for (final AdsClassDef inner : def.get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE)) {
            if (ERuntimeEnvironmentType.compatibility(usagePurpose.getEnvironment(), inner.getUsageEnvironment())) {
                if (!writeCode(printer, inner)) {
                    return false;
                }
            }
        }
        return true;
    }
}
