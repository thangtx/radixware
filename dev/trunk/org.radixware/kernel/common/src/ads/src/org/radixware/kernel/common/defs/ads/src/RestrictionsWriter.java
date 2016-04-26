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

package org.radixware.kernel.common.defs.ads.src;

import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class RestrictionsWriter extends RadixObjectWriter<Restrictions> {

    private static final char[] RESTRICTIONS_SERVER_CLASS_NAME = CharOperations.merge(JavaSourceSupport.RADIX_SERVER_TYPES_PACKAGE_NAME, "Restrictions".toCharArray(), '.');
    private static final char[] RESTRICTIONS_FACTORY_SERVER_CLASS_NAME = CharOperations.merge(RESTRICTIONS_SERVER_CLASS_NAME, "Factory".toCharArray(), '.');

    public RestrictionsWriter(JavaSourceSupport support, Restrictions restrictions, UsagePurpose usagePurpose) {
        super(support, restrictions, usagePurpose);
    }

    @Override
    public boolean writeCode(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                printer.print(RESTRICTIONS_FACTORY_SERVER_CLASS_NAME);
                printer.print(".newInstance(");
                printer.print(ERestriction.toBitField(def.getRestriction()));
                printer.printComma();
                WriterUtils.writeIdArrayUsage(printer, def.getEnabledCommandIds());
                printer.printComma();
                WriterUtils.writeIdArrayUsage(printer, def.getEnabledChildIds());
                printer.printComma();
                WriterUtils.writeIdArrayUsage(printer, def.getEnabledPageIds());
                printer.print(')');
                return true;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                printer.print(ERestriction.toBitField(def.getRestriction()));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //dont use in code directly
    }
}
