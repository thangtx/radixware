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

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsInterfaceWriter<T extends AdsClassDef> extends AdsClassWriter<T> {

    public AdsInterfaceWriter(JavaSourceSupport support, T target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected void writeClassDefinitionMethod(CodePrinter printer) {
        printer.print(TEXT_INTERFACE);
    }

    @Override
    protected void writeSuperInterfaceUsageMethod(CodePrinter printer) {
        printer.print(TEXT_EXTENDS);
    }
}
