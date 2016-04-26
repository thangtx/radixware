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

package org.radixware.kernel.common.defs.ads.clazz.enumeration;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.scml.CodePrinter;


final class AdsEnumFieldsCodeWriter extends RadixObjectWriter<AdsFields> {

    public AdsEnumFieldsCodeWriter(JavaSourceSupport support, AdsFields target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected boolean writeExecutable(CodePrinter printer) {
        boolean first = true;
        for (final AdsEnumClassFieldDef field : def.get(EScope.LOCAL_AND_OVERWRITE)) {
            if (!first) {
                printer.printComma();
                printer.println();
            } else {
                first = false;
            }
            if (!writeCode(printer, field)) {
                return false;
            }
        }
        printer.printlnSemicolon();
        printer.println();
        return true;
    }

    @Override
    public void writeUsage(CodePrinter printer) {
    }

}
