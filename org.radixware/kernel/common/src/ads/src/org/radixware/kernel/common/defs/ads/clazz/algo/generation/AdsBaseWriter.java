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

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public abstract class AdsBaseWriter<T extends AdsDefinition & IJavaSource & IProfileable> extends AbstractDefinitionWriter<T> {

    public AdsBaseWriter(JavaSourceSupport support, T def, UsagePurpose usagePurpose) {
        super(support, def, usagePurpose);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
    }

    @Override
    public boolean writeExecutable(CodePrinter printer) {
        final Id id = def.getId();

        printer.println("public int executeBlock" + id + "() throws java.lang.Throwable {");
        printer.enterBlock();

        WriterUtils.writeProfilerInitialization(printer, def);
        writeBody(printer);
        WriterUtils.writeProfilerFinalization(printer, def);

        printer.leaveBlock();
        printer.println("};");

        return true;
    }

    protected void writeBody(CodePrinter printer) {
    }
}
