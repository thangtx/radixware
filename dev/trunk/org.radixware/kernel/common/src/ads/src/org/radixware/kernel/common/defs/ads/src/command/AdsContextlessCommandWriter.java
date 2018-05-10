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

package org.radixware.kernel.common.defs.ads.src.command;

import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.RadixType;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.LicenseCodeGenSupport;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;


public class AdsContextlessCommandWriter extends AdsCommandWriter<AdsContextlessCommandDef> {

    private final JavaSourceSupport.CodeWriter defaultXmlWriter;

    public AdsContextlessCommandWriter(JavaSourceSupport support, AdsContextlessCommandDef command, UsagePurpose usagePurpose) {
        super(support, command, usagePurpose);
        defaultXmlWriter = RadixType.Factory.newInstance(EValType.XML).getJavaSourceSupport().getCodeWriter(usagePurpose);
    }

    private boolean typeCastRequired() {
        if (inputType.getTypeId() != EValType.XML || !inputType.isPure()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean writeExecutable(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);

                printer.print("public final class ");
                writeUsage(printer);

                printer.enterBlock(1);
                printer.println('{');

                //RADIX-1831
                //writeMeta(printer);
                if (nature != ECommandNature.FORM_IN_OUT && nature != ECommandNature.LOCAL) {
                    writeCustomMarker(printer, "execute");
                    AdsContextlessCommandDef.Profile profile = def.computeProfile();
                    printer.print(profile.ACCESS_FlAGS + " ");
                    outputTypeWriter.writeCode(printer);
                    printer.print(" " + profile.FUNCTION_NAME + "(");
                    printer.print(profile.ARTE_PARAMETER_TYPE);
                    printer.print(" " + profile.ARTE_PARAMETER_NAME);
                    if (inputType != AdsTypeDeclaration.Factory.voidType()) {
                        printer.print(',');
                        inputTypeWriter.writeCode(printer);
                        printer.print(" " + profile.INPUT_PARAMETER_NAME);
                    }
                    printer.enterBlock(1);
                    printer.println(")throws " + profile.EXEPTION + "{");
                    try {
                        printer.enterCodeSection(def.getLocationDescriptor());
                        WriterUtils.writeProfilerInitialization(printer, def, "arte");
                        writeCode(printer, def.getSource());
                        WriterUtils.writeProfilerFinalization(printer, def, "arte");
                    } finally {
                        printer.leaveCodeSection();
                    }

                    printer.leaveBlock(1);
                    printer.println();
                    printer.println("}");
                }
                printer.println();
                LicenseCodeGenSupport licenseSupport = LicenseCodeGenSupport.get(printer);
                if (licenseSupport != null) {
                    licenseSupport.flushFields(printer);
                }
                printer.leaveBlock(1);
                printer.println();
                printer.println('}');
                return true;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
                printer.print("public class ");
                printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
                printer.print(" extends ");
                printer.print(EXPLORER_COMMAND_CLASS_NAME);

                printer.enterBlock(1);
                printer.println('{');

                writeExplorerClass(printer, false, true);
                printer.leaveBlock(1);
                printer.println('}');
                return true;
            default:
                return false;//super.writeExecutable(printer);
        }
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                /**
                 * public RadContextlessCommandDef( final Id id, final String
                 * name, final ECommandNature nature, final
                 * IRadContextlessCommandExecutor cmdExecutor)
                 */
                //RADIX-1831>>>>>>>>>
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
                printer.println();
                printer.print("public final class ");
                printer.print(JavaSourceSupport.getMetaName(def, printer instanceof IHumanReadablePrinter));
                printer.println("{");
                printer.enterBlock();

                WriterUtils.writeServerArteAccessMethodDeclaration(def, JavaSourceSupport.CodeType.META, printer);

                switch (nature) {
                    case XML_IN_FORM_OUT:
                    case XML_IN_OUT:
                        printer.println("/**Executor implementation*/");

                        printer.println("private static final class InternalExecutor implements org.radixware.kernel.server.meta.presentations.IRadContextlessCommandExecutor{");
                        printer.enterBlock(1);
                        printer.println();
                        if (inputType == AdsTypeDeclaration.Factory.voidType()) {
                            printer.println("@SuppressWarnings(\"unused\")");
                        }

                        printer.print("public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.server.arte.Arte arte,");
                        defaultXmlWriter.writeCode(printer);
                        printer.print(" input,");
                        defaultXmlWriter.writeCode(printer);
                        printer.enterBlock(1);
                        printer.println(" output)throws org.radixware.kernel.common.exceptions.AppException{");

                        if (outputType != AdsTypeDeclaration.Factory.voidType()) {
                            outputTypeWriter.writeCode(printer);
                            printer.print(" result = ");
                        }

                        printer.print(def.getId());
                        printer.print(".execute(arte");

                        if (inputType != AdsTypeDeclaration.Factory.voidType()) {
                            printer.print(',');
                            if (typeCastRequired()) {
                                printer.print("(");
                                inputTypeWriter.writeCode(printer);
                                printer.print(")");
                                printer.print("org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,");
                                inputTypeWriter.writeCode(printer);
                                printer.print(".class)");
                            } else {
                                printer.print("org.radixware.kernel.common.utils.XmlObjectProcessor.getXmlObjectFirstChild(input)");
                            }
                        }
                        printer.print(");\n");
                        if (nature == ECommandNature.XML_IN_OUT) {
                            if (outputType != AdsTypeDeclaration.Factory.voidType()) {
                                printer.println("if(result != null){");
                                printer.enterBlock(1);
                                printer.println("output.set(result);");
                                printer.leaveBlock(1);
                                printer.println("}");
                            }
                            printer.println("return null;");
                            printer.leaveBlock(1);
                        } else {
                            printer.println("return result;");
                            printer.leaveBlock(1);
                        }
                        printer.println('}');
                        printer.leaveBlock(1);
                        printer.println('}');

                    default:
                    //do nothing
                }

                //<<<<<<<<<<RADIX-1831
                printer.print("public static final org.radixware.kernel.server.meta.presentations.RadContextlessCommandDef rdxMeta = new org.radixware.kernel.server.meta.presentations.RadContextlessCommandDef(");
                printer.enterBlock(3);
                WriterUtils.writeReleaseAccessorInMetaClass(printer);
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, nature);
                printer.printComma();
                printer.println();
                if (nature != ECommandNature.FORM_IN_OUT && nature != ECommandNature.LOCAL) {
                    printer.print("new InternalExecutor()");
                } else {
                    printer.print("null");
                }
                printer.printComma();
                printer.print(def.getPresentation().isTraceGuiActivity());
                printer.print(");");
                printer.leaveBlock();
                printer.println('}');
                printer.leaveBlock(3);
                printer.println();
                //RADIX-1831>>>>>>>>>

                //<<<<<<<<<<RADIX-1831
                return true;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);

                printer.print("public class ");
                printer.print(JavaSourceSupport.getMetaName(def, printer instanceof IHumanReadablePrinter));
                printer.enterBlock(1);
                printer.println('{');
                printer.print("public static final ");
                printer.print(EXPLORER_COMMAND_META_CLASS_NAME);
                printer.print(" rdxMeta = new ");
                printer.print(EXPLORER_COMMAND_META_CLASS_NAME);
                printer.print('(');
                super.writeMeta(printer);
                printer.println(");");


                printer.leaveBlock(1);
                printer.println('}');


                return true;

            default:
                return false;
        }

    }
}
