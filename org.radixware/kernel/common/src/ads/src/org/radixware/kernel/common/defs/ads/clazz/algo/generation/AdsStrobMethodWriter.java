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

import org.radixware.kernel.common.defs.ads.src.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStrobMethodDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsStrobMethodWriter extends AdsMethodWriter<AdsAlgoStrobMethodDef> {

    public AdsStrobMethodWriter(JavaSourceSupport support, AdsAlgoStrobMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
    }

    @Override
    protected void writeBody(CodePrinter printer) {
        printer.enterBlock();
        printer.print("onStrob(");
        WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
        printer.print(", processId, ");
        WriterUtils.writeIdUsage(printer, def.getId());
        printer.println(");");
        printer.leaveBlock(1);
    }
}
