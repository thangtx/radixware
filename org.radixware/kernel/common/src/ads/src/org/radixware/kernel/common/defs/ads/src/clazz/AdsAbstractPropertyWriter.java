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

import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.scml.CodePrinter;


final class AdsAbstractPropertyWriter extends AdsPropertyWriter<AdsPropertyDef> {

    public AdsAbstractPropertyWriter(JavaSourceSupport support, AdsPropertyDef property, UsagePurpose usagePurpose) {
        super(support, property, usagePurpose);
    }

    @Override
    protected void writeAccessMethodName(CodePrinter printer) {
        throw new IllegalUsageError("The method writeAccessMethodName() is not expected to use");
    }

    @Override
    protected boolean writePropertyIdRef() {
        return false;
    }

    @Override
    protected boolean isWriteHidden() {
        return false;
    }

    @Override
    void writeGetter(CodePrinter printer) {
        writeCustomMarker(printer, "get");

        writeGetterAnnotations(printer);
        writeGetterAccessFlags(printer);

        printer.printSpace();
        getTypeWriter().writeCode(printer);
        printer.printSpace();
        printer.print(TEXT_GET);
        writePropertyName(printer, true);
        printer.println("();");
    }

    @Override
    void writeSetter(CodePrinter printer) {
        if (def.isConst()) {
            return;
        }
        writeCustomMarker(printer, "set");

        writeSetterAnnotations(printer);
        writeSetterAccessFlags(printer);

        printer.printSpace();
        printer.print(TEXT_SETTER_PREFIX);
        writePropertyName(printer, true);
        printer.print('(');
        getTypeWriter().writeCode(printer);
        printer.println(" val);");
    }
    
    @Override
    protected boolean ignoreOvrMode() {
        return true;
    }
}
