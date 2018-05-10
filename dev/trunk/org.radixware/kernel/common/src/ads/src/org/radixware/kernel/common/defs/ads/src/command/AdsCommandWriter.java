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

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.enums.ECommandNature;
import static org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT;
import static org.radixware.kernel.common.enums.ECommandNature.LOCAL;
import static org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT;
import static org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsCommandWriter<T extends AdsCommandDef> extends AbstractDefinitionWriter<T> {

    protected final JavaSourceSupport.CodeWriter inputTypeWriter;
    protected final JavaSourceSupport.CodeWriter outputTypeWriter;
    protected final AdsTypeDeclaration inputType;
    protected final AdsTypeDeclaration outputType;
    protected final ECommandNature nature;
    public static final char[] EXPLORER_COMMAND_CLASS_NAME = "org.radixware.kernel.common.client.models.items.Command".toCharArray();
    public static final char[] EXPLORER_PROPERTY_ID_ARGUMENT_NAME = "propertyId".toCharArray();
    private JavaSourceSupport.CodeWriter modelInvocationWriter = null;

    public AdsCommandWriter(JavaSourceSupport support, T command, UsagePurpose usagePurpose) {
        super(support, command, usagePurpose);
        nature = command.getData().getNature();
        inputType = command.getData().getInType();
        outputType = command.getData().getOutType();
        inputTypeWriter = getCodeWriter(inputType, command);
        outputTypeWriter = getCodeWriter(outputType, command);
        if (usagePurpose.isExecutable() && usagePurpose.getEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
            if (support.getCurrentRoot() instanceof AdsModelClassDef) {
                modelInvocationWriter = ((AdsModelClassDef) support.getCurrentRoot()).getJavaSourceSupport().getCodeWriter(usagePurpose.forCodeType(JavaSourceSupport.CodeType.INVOKE));
            }
        }
    }

    public boolean writeExplorerClass(CodePrinter printer, boolean withExecutor, boolean withDefaultBaseClass) {
        if (withExecutor || def.getDefinitionType() != EDefType.CONTEXTLESS_COMMAND) {
            if (withExecutor) {
                printer.print("public final class ");
            } else {
                printer.print("public static class ");
            }
            printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
            printer.print(" extends ");
            if (!withDefaultBaseClass) {
                def.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(usagePurpose.getEnvironment(), JavaSourceSupport.CodeType.EXCUTABLE)).writeCode(printer);
            } else {
                printer.print(EXPLORER_COMMAND_CLASS_NAME);
            }
            printer.enterBlock();
            printer.println('{');
        }
        printer.print("protected ");

        printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
        printer.print('(');
        printer.print(AdsClassWriter.EXPLORER_MODEL_CLASS_NAME);
        printer.print(" model,");
        printer.print(EXPLORER_COMMAND_META_CLASS_NAME);
        printer.leaveBlock();
        printer.println(" def){super(model,def);}");

        switch (nature) {
            case LOCAL://no send method,only execute;
                if (withExecutor) {
                    writeCommandExecuteMethod(printer);
                }
                break;
            case FORM_IN_OUT://
                //no send method, no execute method
                break;
            case XML_IN_FORM_OUT://method send returns the form but xml preparation required
            case XML_IN_OUT://traditional command with xml input and output
                if (withExecutor) {
                    writeCommandExecuteMethod(printer);
                }
                if (!withExecutor) {
                    writeCommandXmlSend(printer);
                }
                break;

        }
        printer.println();
        if (withExecutor || def.getDefinitionType() != EDefType.CONTEXTLESS_COMMAND) {
            printer.println('}');
        }

        return true;
    }

    public void writeSendMethodIfNeeded(CodePrinter printer) {
        switch (nature) {
            case XML_IN_FORM_OUT://method send returns the form but xml preparation required
            case XML_IN_OUT://traditional command with xml input and output
                writeCommandXmlSend(printer);
                break;
        }
    }

    @Override
    protected boolean writeAddon(CodePrinter printer) {

        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                writeExplorerClass(printer, true, false);
                return true;
            default:
                return super.writeAddon(printer);
        }

    }

    private void writeCommandExecuteMethod(CodePrinter printer) {
        printer.enterBlock();
        printer.print("@Override\npublic void execute( ");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.print(" ");
        printer.print(EXPLORER_PROPERTY_ID_ARGUMENT_NAME);
        printer.println(" ) {");
//        if (modelInvocationWriter != null) {
//            modelInvocationWriter.writeCode(printer);
//        } else {
//            printer.printError();
//        }
        // printer.print('.');
        printer.enterBlock();
        if (printer instanceof IHumanReadablePrinter) {
            printer.print(AdsCommandHandlerMethodDef.getCommandHandlerName(def));
        } else {
            printer.print(EDefinitionIdPrefix.ADS_CLASS_METHOD.getValue());
            printer.print(def.getId());
        }
        printer.leaveBlock();
        printer.print("( this");
        if (def instanceof AdsPropertyCommandDef) {
            printer.print(", propertyId");
        }
        printer.println(" );");
        printer.println('}');
        printer.leaveBlock();
    }
    private static final char[] CLC_RS_TYPE = org.radixware.schemas.eas.ContextlessCommandRs.class.getName().toCharArray();
    private static final char[] CMD_RS_TYPE = org.radixware.schemas.eas.CommandRs.class.getName().toCharArray();
    private static final char[] EXPLORER_ENV_CLASS_NAME = "org.radixware.kernel.explorer.env.Environment".toCharArray();

    private void writeCommandXmlSend(CodePrinter printer) {
        printer.print("\tpublic ");
        if (nature == ECommandNature.XML_IN_OUT) {
            outputTypeWriter.writeCode(printer);
        } else {
            printer.print("void");
        }

        printer.print(" send(");
        if (inputType != AdsTypeDeclaration.Factory.voidType()) {
            inputTypeWriter.writeCode(printer);
            printer.print(" input ");
            if (def instanceof AdsPropertyCommandDef) {
                printer.print(",");
                printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                printer.print(" ");
                printer.print(EXPLORER_PROPERTY_ID_ARGUMENT_NAME);
            }
        } else if (def instanceof AdsPropertyCommandDef) {
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.print(" ");
            printer.print(EXPLORER_PROPERTY_ID_ARGUMENT_NAME);
        }

        printer.enterBlock();
        printer.enterBlock();

        printer.println(") throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{");

        if (def.getDefinitionType() == EDefType.CONTEXTLESS_COMMAND) {
            printer.print(CLC_RS_TYPE);
            printer.print(" response = ");
            //printer.print(EXPLORER_ENV_CLASS_NAME);
            printer.print("getEnvironment().getEasSession().executeCommand(null,getDefinition().getId(),");
        } else {
            printer.print(CMD_RS_TYPE);
            printer.print(" response = ");
            //printer.print(EXPLORER_ENV_CLASS_NAME);
            printer.print("getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),");
            if (def instanceof AdsPropertyCommandDef) {
                printer.print(EXPLORER_PROPERTY_ID_ARGUMENT_NAME);
            } else {
                printer.print("null");
            }
            printer.print(",");
        }
        if (inputType != AdsTypeDeclaration.Factory.voidType()) {
            printer.print("input);\n");
        } else {
            printer.print("null);\n");
        }

        final String processResponseInvokation =
                "processResponse(response, "
                + (def instanceof AdsPropertyCommandDef ? new String(EXPLORER_PROPERTY_ID_ARGUMENT_NAME) : "null")
                + ");\n";

        if (nature == ECommandNature.XML_IN_OUT) {
            if (outputType != AdsTypeDeclaration.Factory.voidType()) {
                printer.print("org.apache.xmlbeans.XmlObject xmlResponse = ");
                printer.print(processResponseInvokation);
                printer.print("return (");
                outputTypeWriter.writeCode(printer);
                printer.print(')');
                printer.print(WriterUtils.RADIX_XML_OBJECT_PROCESSOR_CLASS_NAME);
                printer.print(".castToXmlClass(getClass().getClassLoader(),xmlResponse,");
                outputTypeWriter.writeCode(printer);
                printer.print(".class);\n");
                printer.leaveBlock();
            } else {
                printer.print(processResponseInvokation);
                printer.leaveBlock();
            }
        } else {
            printer.print(processResponseInvokation);
            printer.leaveBlock();
        }

        printer.println("}");
        printer.leaveBlock();
    }
    public static final char[] SERVER_COMMAND_META_CLASS_NAME = "org.radixware.kernel.server.meta.presentations.RadCommandDef".toCharArray();
    public static final char[] EXPLORER_COMMAND_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadCommandDef".toCharArray(), '.');
    public static final char[] EXPLORER_ENTITY_COMMAND_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadPresentationCommandDef".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        AdsScopeCommandDef scopeCommand = null;
        if (def instanceof AdsScopeCommandDef) {
            scopeCommand = (AdsScopeCommandDef) def;
        }

        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                /**
                 * *
                 * public RadPresentationCommandDef( final Id id, final String
                 * name, final Id ownerId, final Id titleId, final Id iconId,
                 * final Id startFormId, final ECommandNature nature, final
                 * boolean forExplorer, final boolean confirmation, final
                 * ECommandScope scope, final Id[] props, final int
                 * accessibility , final boolean rereadAfterExecute)
                 *
                 * public RadCommandDef( final Id id, final String name, final
                 * Id ownerId, final Id titleId, final Id iconId, final Id
                 * startFormId, final ECommandNature nature, final boolean
                 * visible, final boolean confirmation )
                 */
                printer.enterBlock();
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, scopeCommand == null ? def.getId() : scopeCommand.getOwnerClass().getId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getTitleId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getPresentation().getIconId());
                printer.printComma();
                printer.println();
                AdsType type = inputType.resolve(def).get();
                if (type instanceof AdsClassType) {
                    WriterUtils.writeIdUsage(printer, ((AdsClassType) type).getSource().getId());
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, nature);
                printer.printComma();
                printer.println();
                printer.print(def.getPresentation().getIsVisible());
                printer.printComma();
                printer.println();
                printer.print(def.getPresentation().getIsConfirmationRequired());
                printer.printComma();
                printer.println();
                printer.print(def.getData().isReadOnlyCommand());
                if (scopeCommand != null) {
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeEnumFieldInvocation(printer, scopeCommand.getScope());
                    printer.printComma();
                    printer.println();
                    if (scopeCommand instanceof AdsPropertyCommandDef) {
                        WriterUtils.writeIdArrayUsage(printer, ((AdsPropertyCommandDef) scopeCommand).getUsedPropIds());
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeEnumFieldInvocation(printer, scopeCommand.getPresentation().getAccessebility());
                    AdsScopeCommandDef.CommandPresentation presentation = scopeCommand.getPresentation();
                    if (presentation.canReread() && presentation.getAccessebility() == ECommandAccessibility.ONLY_FOR_FIXED){
                        printer.printComma();
                        printer.println();
                        printer.print(presentation.isRereadAfterExecute());
                    }
                }
                printer.leaveBlock();
                return true;
            case SERVER:
                /**
                 * RadCommandDef( final Id id, final String name, final
                 * ECommandScope scope, final Id[] propIds, final
                 * ECommandAccessibility accessibility, final ECommandNature
                 * nature, final Id classId)
                 */
//                printer.print("new ");
//                printer.print(SERVER_COMMAND_META_CLASS_NAME);
//                printer.print('(');
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                if (scopeCommand != null) {
                    ECommandScope scope = scopeCommand.getScope();
                    if (scope == ECommandScope.RPC) {
                        AdsClassDef ownerClass = scopeCommand.getOwnerClass();
                        if (ownerClass.getClassDefType() == EClassType.ENTITY_GROUP) {
                            scope = ECommandScope.GROUP;
                        } else {
                            scope = ECommandScope.OBJECT;
                        }
                    }
                    WriterUtils.writeEnumFieldInvocation(printer, scope);
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                if (def instanceof AdsPropertyCommandDef) {
                    WriterUtils.writeIdArrayUsage(printer, ((AdsPropertyCommandDef) def).getUsedPropIds());
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                if (scopeCommand != null) {
                    WriterUtils.writeEnumFieldInvocation(printer, ((AdsScopeCommandDef.CommandPresentation) scopeCommand.getPresentation()).getAccessebility());
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                WriterUtils.writeEnumFieldInvocation(printer, nature);
                printer.printComma();
                if (scopeCommand != null) {
                    WriterUtils.writeIdUsage(printer, scopeCommand.getOwnerClass().getId());
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                printer.print(def.getData().isReadOnlyCommand());
                printer.printComma();
                printer.print(def.getPresentation().isTraceGuiActivity());
//                printer.print(')');
                return true;
            default:
                return super.writeMeta(printer);
        }
    }
}
