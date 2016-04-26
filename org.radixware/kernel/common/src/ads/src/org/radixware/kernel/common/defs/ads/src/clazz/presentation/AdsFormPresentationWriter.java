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

import java.util.Set;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.FormPresentations;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsFormPresentationWriter extends AbstractFormPresentationWriter<FormPresentations> {

    public static final char[] EXPLORER_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadFormDef".toCharArray(), '.');

    AdsFormPresentationWriter(final JavaSourceSupport support, final FormPresentations presentations, final UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }

    @Override
    public char[] getExplorerMetaClassName() {
        return EXPLORER_META_CLASS_NAME;
    }

    @Override
    protected boolean writeBaseFormId(final AdsClassDef clazz, final CodePrinter printer) {
        final AdsClassDef superClass = clazz.getInheritance().findSuperClass().get();
        if (superClass != null && superClass.getClassDefType() == EClassType.FORM_HANDLER) {
            WriterUtils.writeIdUsage(printer, superClass.getId());
        } else {
            WriterUtils.writeNull(printer);
        }
        printer.printComma();
        printer.println();
        return true;
    }

    @Override
    protected boolean writeInheritanceMask(final CodePrinter printer, final Set<EPresentationAttrInheritance> mask) {
        printer.print(EPresentationAttrInheritance.toBitField(mask));
        printer.printComma();
        printer.println();
        return true;
    }

    @Override
    protected boolean writeContextMeta(AdsClassDef thisClazz, CodePrinter printer) {
        return true;
    }
}
