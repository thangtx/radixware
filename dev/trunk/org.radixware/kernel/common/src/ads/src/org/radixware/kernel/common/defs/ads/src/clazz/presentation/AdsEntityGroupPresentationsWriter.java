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

package org.radixware.kernel.common.defs.ads.src.clazz.presentation;

import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityGroupPresentations;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


class AdsEntityGroupPresentationsWriter extends AdsEntityBasedPresentationsWriter<EntityGroupPresentations> {

    public AdsEntityGroupPresentationsWriter(JavaSourceSupport support, EntityGroupPresentations presentations, UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }
    private static final char[] EXPLORER_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadGroupHandlerDef".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                /**
                 * RadGroupHandlerDef(
                Id id,
                Id classId,
                final RadCommandDef[] commands
                )
                 */
                printer.print("new ");
                printer.print(EXPLORER_META_CLASS_NAME);
                printer.print('(');
                printer.enterBlock(2);
                AdsEntityGroupClassDef clazz = def.getOwnerClass();
                WriterUtils.writeIdUsage(printer, clazz.getId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, clazz.getBasisId());
                printer.printComma();
                printer.println();
                writeCommandsMeta(printer);
                printer.printComma();
                printer.println();
                writeCode(printer, clazz.getProperties());
                printer.leaveBlock();
                printer.print(')');
                printer.printlnSemicolon();
                return true;
            default:
                return super.writeMeta(printer);
        }

    }

    @Override
    public char[] getExplorerMetaClassName() {
        return EXPLORER_META_CLASS_NAME;
    }
}
