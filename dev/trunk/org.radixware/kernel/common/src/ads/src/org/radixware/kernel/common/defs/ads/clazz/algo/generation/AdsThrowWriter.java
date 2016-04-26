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

package org.radixware.kernel.common.defs.ads.clazz.algo.generation;

import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsThrowObject;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsThrowWriter extends AdsBaseWriter<AdsThrowObject> {

    public AdsThrowWriter(JavaSourceSupport support, AdsThrowObject th, UsagePurpose usagePurpose) {
        super(support, th, usagePurpose);
    }

    @Override
    protected void writeBody(CodePrinter printer) {
        AdsThrowObject th = def;
        printer.print("throw new ");
        writeUsage(printer, AdsTypeDeclaration.Factory.newInstance(th.getExceptionDef()),def);
        printer.print("(");
        writeCode(printer, th.getParameters());
        printer.println(");");
    }
}
