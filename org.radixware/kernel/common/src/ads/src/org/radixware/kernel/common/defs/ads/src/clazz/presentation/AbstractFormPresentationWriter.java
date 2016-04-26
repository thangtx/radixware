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

import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.IModalDisplayable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;

public abstract class AbstractFormPresentationWriter<T extends AbstractFormPresentations> extends AdsClassPresentationsWriter<T> {

    AbstractFormPresentationWriter(final JavaSourceSupport support, final T presentations, final UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }

    protected abstract boolean writeBaseFormId(final AdsClassDef thisClazz, final CodePrinter printer);

    protected abstract boolean writeContextMeta(final AdsClassDef thisClazz, final CodePrinter printer);

    protected abstract boolean writeInheritanceMask(final CodePrinter printer, Set<EPresentationAttrInheritance> mask);

    @Override
    protected boolean writeMeta(final CodePrinter printer) {
        /*RadFormDef(final Id id,
         final String name,
         final Id baseFormId,
         final Id titleId,
         final Id iconId,
         final long inheritanceMask,
         RadPropertyDef[] properties,
         RadEditorPageDef[] editorPages,
         RadEditorPages.PageOrder[] pageOrder,
         RadCommandDef[] commands,
         Id[] commandOrder) */
        final FormPresentationFinalAttributes attributes = new FormPresentationFinalAttributes((IAdsFormPresentableClass) def.getOwnerClass());
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                printer.print("new ");
                printer.print(getExplorerMetaClassName());
                printer.print('(');
                printer.enterBlock(2);
                final AdsClassDef clazz = def.getOwnerClass();
                WriterUtils.writeIdUsage(printer, clazz.getId());

                printer.printComma();
                printer.println();

                printer.printStringLiteral(clazz.getName());

                printer.printComma();
                printer.println();

                if (!writeBaseFormId(clazz, printer)) {
                    return false;
                }

                WriterUtils.writeIdUsage(printer, clazz.getTitleId());

                printer.printComma();
                printer.println();

                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.ICON) || attributes.finalIconId == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, attributes.finalIconId);//icon id
                }
                printer.printComma();
                printer.println();

                if (!writeInheritanceMask(printer, attributes.finalInheritanceMask)) {
                    return false;
                }

                writeCode(printer, clazz.getProperties());
                printer.printComma();
                printer.println();

                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.PAGES) || attributes.finalEditorPages == null) {
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeNull(printer);

                } else {
                    writeCode(printer, attributes.finalEditorPages);
                }
                printer.printComma();
                printer.println();

                writeCommandsMeta(printer);

                printer.printComma();
                printer.println();
                @SuppressWarnings("unchecked") List<Id> ids = def.getCommandsOrder();
                WriterUtils.writeIdArrayUsage(printer, ids);

                writeContextMeta(clazz, printer);

                final IModalDisplayable.ModialViewSizeInfo sizeInfo = def.getModialViewSizeInfo(usagePurpose.getEnvironment());
                if (sizeInfo != null) {
                    printer.printComma();
                    printer.print(sizeInfo.getWidth());
                    printer.printComma();
                    printer.print(sizeInfo.getHeight());
                }
//
                printer.leaveBlock();
                printer.print(')');
                if (!writeCustomBody(printer)) {
                    return false;
                }
                printer.printlnSemicolon();

                return true;
            default:
                return super.writeMeta(printer);
        }
    }

    protected boolean writeCustomBody(CodePrinter printer) {
        return true;
    }

    @Override
    protected void writeCommandsMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                super.writeCommandsMeta(printer);
                break;
            case SERVER:
                if (def.getOwnerClass().getClassDefType() == EClassType.FORM_HANDLER) {
                    super.writeCommandsMeta(printer);
                } else {
                    WriterUtils.writeNull(printer);
                }
                break;
        }
    }
}
