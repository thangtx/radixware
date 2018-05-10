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
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;


final class NonPersystentPropertyWriter extends AdsPropertyWriter<AdsPropertyDef> {

    private static final char[] TEXT_VALUE_STORAGE_SUFFIX = "$Val".toCharArray();

    public NonPersystentPropertyWriter(JavaSourceSupport support, AdsPropertyDef property, UsagePurpose usagePurpose) {
        super(support, property, usagePurpose);
    }

    private void printValueStorage(CodePrinter printer) {
        if (printer instanceof IHumanReadablePrinter) {
            printer.print(getProperty().getName());
        } 
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.print(propId);
        printer.print(TEXT_VALUE_STORAGE_SUFFIX);
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }

    private boolean printInitialValue(CodePrinter printer) {
        printer.print('=');
        if (!WriterUtils.writeAdsValAsStr(def.getValue().getInitial(), def.getValue().getInitialValueController(), this, printer)) {
            //if (!WriterUtils.printValAsStrAsJavaInitializerForVariable(def.getValue().getInitial(), def.getValue().getType(), def, this, printer)) {
            return false;
        } else {
            return true;
        }
//        AdsTypeDeclaration decl = def.getValue().getType();
//        ValAsStr initVal = def.getValue().getInitial();
//
//
//        if (decl.getTypeId() == EValType.USER_CLASS || decl.getTypeId() == EValType.JAVA_TYPE || decl.getTypeId() == EValType.JAVA_CLASS) {
//            if (initVal != null) {
//                printer.print('=');
//                printer.print(initVal.toString());
//            } else {
//                if (decl.getTypeId() == EValType.JAVA_TYPE) {
//                    AdsType type = decl.resolve(getSupport().getCurrentRoot());
//                    if (type instanceof JavaType) {
//                        printer.print('=');
//                        printer.print(((JavaType) type).getDefaultInitValAsCharArray());
//                    } else {
//                        return;
//                    }
//                } else {
//                    printer.print(" = null");
//                }
//            }
//        } else {
//            if (initVal != null) {
//                AdsType type = decl.resolve(getSupport().getCurrentRoot());
//                if (type == null) {
//                    return;
//                }
//                printer.print('=');
//                AdsType castType = type;
//                if (type instanceof AdsEnumType) {
//                    EValType typeId = ((AdsEnumType) type).getSource().getItemType();
//                    if (type instanceof AdsEnumType.Array) {
//                        typeId = typeId.getArrayType();
//                    }
//                    castType = RadixType.Factory.newInstance(typeId);
//                    getTypeWriter().writeCode(printer);
//                    printer.print(".getForValue(");
//                }
//                printer.print('(');
//                castType.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
//                printer.print(')');
//                printer.print("org.radixware.kernel.common.defs.value.ValAsStr.fromStr(");
//                printer.printStringLiteral(initVal.toString());
//                printer.printComma();
//                WriterUtils.writeEnumFieldInvocation(printer, def.getValue().getType().getTypeId());
//                printer.print(")");
//                if (type instanceof AdsEnumType) {
//                    printer.print(')');
//                }
//            } else {
//                printer.print(" = null");
//            }
//        }
    }

    @Override
    protected boolean writeInitialization(CodePrinter printer) {
        if (def.isDeprecated()) {
            printer.println("@Deprecated");
        }
        if (def.getAccessFlags().isStatic()) {
            printer.print(TEXT_HIDDEN_STATIC_ACC_PREFIX);
        } else {
            printer.print(TEXT_HIDDEN_ACC_PREFIX);
        }
        if (!getTypeWriter().writeCode(printer)) {
            return false;
        }
        printer.printSpace();
        printValueStorage(printer);
        if (!printInitialValue(printer)) {
            return false;
        }
        printer.printlnSemicolon();
        return true;
    }

    @Override
    protected boolean writeStdGetterBody(CodePrinter printer) {
        printer.print("return ");
        printValueStorage(printer);
        printer.printlnSemicolon();
        return true;
    }

    @Override
    protected boolean writeStdSetterBody(CodePrinter printer) {
        printValueStorage(printer);
        printer.print('=');
        printSetterParamVariable(printer);
        printer.printlnSemicolon();
        return true;
    }

    @Override
    protected void writeAccessMethodName(CodePrinter printer) {
        throw new IllegalUsageError("The method writeAccessMethodName() is not expected to use");
    }

    @Override
    protected boolean ignoreOvrMode() {
        final AdsPropertyDef over = findOverriddenProp();

        if (over == null || over.getAccessFlags().isAbstract()) {
            return true;
        }

        return super.ignoreOvrMode();
    }
}
