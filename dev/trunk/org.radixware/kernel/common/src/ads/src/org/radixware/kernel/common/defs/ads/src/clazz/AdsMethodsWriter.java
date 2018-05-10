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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Methods;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsRPCCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.command.AdsCommandWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation;


public class AdsMethodsWriter extends RadixObjectWriter<Methods> {

    public AdsMethodsWriter(JavaSourceSupport support, Methods methods, UsagePurpose usagePurpose) {
        super(support, methods, usagePurpose);
    }
    private static final char[] SERVER_METHOD_META_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.RadMethodDef".toCharArray();
    private static final char[] SERVER_METHOD_PARAM_META_CLASS_NAME = CharOperations.merge(SERVER_METHOD_META_CLASS_NAME, "Parameter".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        ERuntimeEnvironmentType targetEnv = usagePurpose.getEnvironment();
        switch (targetEnv) {
            case SERVER:
                new WriterUtils.SameObjectArrayWriter<AdsMethodDef>(SERVER_METHOD_META_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsMethodDef item) {
                        WriterUtils.writeIdUsage(printer, item.getId());
                        printer.printComma();
                        printer.printStringLiteral(item.getName());
                        printer.printComma();
                        printer.print(item.getAccessFlags().isStatic());
                        printer.printComma();
                        printer.print(item.getAccessFlags().isAbstract());
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, item.getAccessMode());
                        printer.printComma();
                        new WriterUtils.SameObjectArrayWriter<MethodParameter>(SERVER_METHOD_PARAM_META_CLASS_NAME) {
                            @Override
                            public void writeItemConstructorParams(CodePrinter printer, MethodParameter item) {
                                printer.printStringLiteral(item.getName());
                                printer.printComma();
                                WriterUtils.writeEnumFieldInvocation(printer, item.getType().getTypeId());
                                printer.printComma();
                                WriterUtils.writeIdUsage(printer, item.getId());
                            }
                        }.write(printer, item.getProfile().getParametersList().list());
                        printer.printComma();
                        if (item.getProfile().getReturnValue() != null && item.getProfile().getReturnValue().getType() != null) {
                            WriterUtils.writeEnumFieldInvocation(printer, item.getProfile().getReturnValue().getType().getTypeId());
                        } else {
                            WriterUtils.writeEnumFieldInvocation(printer, EValType.JAVA_TYPE);
                        }
                    }
                }.write(printer, def.get(EScope.LOCAL_AND_OVERWRITE));
                return true;
            default:
                return super.writeMeta(printer); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public boolean writeExecutable(CodePrinter printer) {

        ERuntimeEnvironmentType targetEnv = usagePurpose.getEnvironment();
        boolean isClientEnv = targetEnv.isClientEnv();
        for (AdsMethodDef method : def.get(EScope.LOCAL_AND_OVERWRITE)) {
            if (isClientEnv) {
                if (targetEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (method.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        if (method instanceof AdsUserMethodDef && ((AdsUserMethodDef) method).getSources().isSourcesSeparated()) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else if (targetEnv == ERuntimeEnvironmentType.EXPLORER) {
                    if (method.getUsageEnvironment() == ERuntimeEnvironmentType.WEB) {
                        continue;
                    }
                } else if (targetEnv == ERuntimeEnvironmentType.WEB) {
                    if (method.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                        continue;
                    }
                }
            }
            if (!writeCode(printer, method)) {
                return false;
            }
        }

        return writeCommandHandlerExecutors(printer);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //dont use in code directly
    }

    /**
     * EntityEvents public FormHandler.NextDialogsRequest execCommand(Id cmdId,
     * Id propId, org.apache.xmlbeans.XmlObject input, Map<Id, Object>
     * newPropValsById, org.apache.xmlbeans.XmlObject output) throws
     * AppException, InterruptedException {return null;} EntityGroup public
     * FormHandler.NextDialogsRequest execCommand(final Id cmdId, final
     * org.apache.xmlbeans.XmlObject input, final org.apache.xmlbeans.XmlObject
     * output) throws AppException, InterruptedException {
     */
    private class MCInfo {

        AdsMethodDef method;
        AdsCommandDef command;

        private MCInfo(AdsMethodDef m, AdsCommandDef c) {
            this.method = m;
            this.command = c;
        }
    }

    private class CCInfo {

        AdsCommandModelClassDef clazz;
        AdsCommandDef command;

        private CCInfo(AdsCommandModelClassDef cc, AdsCommandDef c) {
            this.clazz = cc;
            this.command = c;
        }
    }

    private void writeCommandIdsCache(CodePrinter printer, List<AdsCommandDef> commands) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.println("/**Executable command ids cache*/");

        for (AdsCommandDef info : commands) {
            printer.print("private static final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.printSpace();
            WriterUtils.writeAutoVariable(printer, info.getId().toCharArray());
            printer.print(" = ");
            WriterUtils.writeIdUsage(printer, info.getId());
            printer.printlnSemicolon();
        }
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }

    private void writeCommandIdsCacheByMCInfo(CodePrinter printer, List<MCInfo> commands) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.println("/**Executable command ids cache*/");

        for (MCInfo info : commands) {
            printer.print("private static final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.printSpace();
            WriterUtils.writeAutoVariable(printer, info.command.getId().toCharArray());
            printer.print(" = ");
            WriterUtils.writeIdUsage(printer, info.command.getId());
            printer.printlnSemicolon();
        }
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }

    // by KAV
    private void printCommandInput(final CodePrinter printer, final AdsTypeDeclaration inType, final AdsMethodDef currentMethod) {
        printer.print("(");
        writeCode(printer, inType, currentMethod);
        printer.print(")");
        printer.print("org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(");
        printer.print("getClass().getClassLoader(),input,");
        writeCode(printer, inType, currentMethod);
        printer.print(".class)");
    }
    private static final char[] INTERRUPTED_CLASS_NAME = InterruptedException.class.getName().toCharArray();

    private static final class MCComparator implements Comparator<MCInfo> {

        @Override
        public int compare(MCInfo o1, MCInfo o2) {
            return o1.command.getId().compareTo(o2.command.getId());
        }
    }

    private static final class CComparator implements Comparator<AdsCommandDef> {

        @Override
        public int compare(AdsCommandDef o1, AdsCommandDef o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }

    private boolean writeCommandHandlerExecutors(CodePrinter printer) {
        AdsClassDef ownerClass = def.getOwnerClass();

        final CComparator comparator = new CComparator();
        ArrayList<AdsCommandDef> commands = new ArrayList<>();
        Map<AdsCommandDef, Object> command2handler = new HashMap<>();

        switch (usagePurpose.getEnvironment()) {

            case WEB:
            case EXPLORER:

                switch (ownerClass.getClassDefType()) {
                    case ENTITY_MODEL:
                    case GROUP_MODEL:
                    case FORM_MODEL:
                    case PARAGRAPH_MODEL:
                    case DIALOG_MODEL:
                    case PROP_EDITOR_MODEL:
                        break;
                    default:
                        return true;
                }

                for (AdsClassDef clazz : def.getOwnerClass().getNestedClasses().get(EScope.LOCAL)) {
                    if (clazz.getClassDefType() == EClassType.COMMAND_MODEL && AdsClassWriter.checkEnv(clazz.getClientEnvironment(), usagePurpose)) {
                        AdsCommandModelClassDef cmc = (AdsCommandModelClassDef) clazz;
                        AdsCommandDef command = cmc.findCommand();
                        if (command != null && !command2handler.containsKey(command)) {
                            commands.add(command);
                            command2handler.put(command, cmc);
                        }
                    }
                }

                for (AdsMethodDef method : def.get(EScope.LOCAL_AND_OVERWRITE)) {
                    if (AdsClassWriter.checkEnv(method.getUsageEnvironment(), usagePurpose)) {
                        if (method instanceof AdsCommandHandlerMethodDef && !method.isOverride()) {
                            AdsCommandDef command = ((AdsCommandHandlerMethodDef) method).findCommand();
                            if (command != null && !command2handler.containsKey(command)) {
                                commands.add(command);
                                command2handler.put(command, method);
                            }
                        } else if (method instanceof AdsRPCMethodDef && !method.isOverride()) {
                            AdsCommandDef command = ((AdsRPCMethodDef) method).findCommand();
                            if (command != null && !command2handler.containsKey(command)) {
                                commands.add(command);
                                command2handler.put(command, method);
                            }
                        }
                    }
                }

                if (commands.isEmpty()) {
                    return true;
                }
                Collections.sort(commands, comparator);

                for (AdsCommandDef info : commands) {
                    Object handler = command2handler.get(info);
                    if (handler instanceof AdsMethodDef) {
                        boolean withExecutor = handler instanceof AdsMethodDef && ((AdsMethodDef) handler).getNature() != EMethodNature.RPC;
                        ((AdsCommandWriter) info.getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(usagePurpose.getEnvironment(), JavaSourceSupport.CodeType.ADDON))).writeExplorerClass(printer, withExecutor, false);
                        printer.println();
                    }
                }

                writeCommandIdsCache(printer, commands);
                WriterUtils.enterHumanUnreadableBlock(printer);
                printer.print("\n@Override\nprotected ");
                printer.print(AdsCommandWriter.EXPLORER_COMMAND_CLASS_NAME);
                printer.print(" createCommand(");
                printer.print(AdsCommandWriter.EXPLORER_COMMAND_META_CLASS_NAME);
                printer.enterBlock();
                printer.println(" def){");
                printer.println("@SuppressWarnings(\"unused\")");
                printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                printer.println(" commandId = def.getId();");
                for (AdsCommandDef info : commands) {
                    printer.print("if(");
                    WriterUtils.writeAutoVariable(printer, info.getId().toCharArray());
                    printer.print(" == commandId) return new ");

                    Object handler = command2handler.get(info);
                    if (handler instanceof AdsMethodDef) {
                        writeUsage(printer, info);
                    } else {
                        writeUsage(printer, (AdsClassDef) handler);
                    }
                    printer.println("(this,def);");
                    printer.print("else ");
                }
                boolean superclasswritten = false;
                if (def.getOwnerClass() instanceof AdsModelClassDef) {
                    AdsClassDef superclass = def.getOwnerClass().getInheritance().findSuperClass().get();
                    if (superclass == null || !(superclass instanceof AdsModelClassDef)) {
                        AdsClassDef ssc = ((AdsModelClassDef) def.getOwnerClass()).findServerSideClasDef();
                        if (ssc != null) {
                            printer.print("return super.createCommand(def)");
                            superclasswritten = true;
                        }
                    }
                }
                if (!superclasswritten) {
                    printer.print("return super.createCommand(def)");
                }
                printer.leaveBlock();
                printer.printlnSemicolon();
                printer.println("}");
                WriterUtils.leaveHumanUnreadableBlock(printer);
                return true;




            case SERVER:
                List<MCInfo> methods = new ArrayList<>();
                List<MCInfo> rpcCalls = new LinkedList<MCInfo>();
                switch (ownerClass.getClassDefType()) {
                    case APPLICATION:
                    case ENTITY:
                    case ENTITY_GROUP:
                    case FORM_HANDLER:
                        break;
                    default:
                        return true;
                }

                for (AdsMethodDef method : def.get(EScope.LOCAL_AND_OVERWRITE)) {
                    if (method instanceof AdsCommandHandlerMethodDef) {
                        AdsCommandDef command = ((AdsCommandHandlerMethodDef) method).findCommand();
                        if (command instanceof AdsScopeCommandDef && command.getData().getNature() != ECommandNature.LOCAL) {
                            methods.add(new MCInfo(method, (AdsScopeCommandDef) command));
                        }
                    }
                }
                //also looks for rpc commands to add synthetic handlers
                if (ownerClass instanceof IAdsPresentableClass) {
                    for (AdsCommandDef cmd : ((IAdsPresentableClass) ownerClass).getPresentations().getCommands().get(EScope.LOCAL_AND_OVERWRITE)) {
                        if (cmd instanceof AdsScopeCommandDef && ((AdsScopeCommandDef) cmd).getScope() == ECommandScope.RPC) {
                            methods.add(new MCInfo(null, cmd));
                        }
                    }
                }
                Collections.sort(methods, new MCComparator());

                writeCommandIdsCacheByMCInfo(printer, methods);

                CodeWriter xmlTypeWriter = XmlType.Factory.getDefault().getJavaSourceSupport().getCodeWriter(usagePurpose);
                writeCustomMarker(ownerClass, printer, "command_executor");

                printer.println("@Override");

                printer.print("public ");
                printer.print(WriterUtils.FORM_HANDLER_NEXT_DIALOG_REQUEST_CLASS_NAME);
                printer.print(" execCommand(");
                printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                printer.print(" cmdId, ");
                EClassType classType = ownerClass.getClassDefType();
                if (classType == EClassType.ENTITY || classType == EClassType.APPLICATION || classType == EClassType.FORM_HANDLER) {
                    printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                    printer.print(" propId, ");
                    xmlTypeWriter.writeCode(printer);
                    if (classType == EClassType.FORM_HANDLER) {
                        printer.print(" input,");
                    } else {
                        printer.print(" input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, ");
                    }
                    xmlTypeWriter.writeCode(printer);
                    printer.print(" output");
                } else if (classType == EClassType.ENTITY_GROUP) {
                    xmlTypeWriter.writeCode(printer);
                    printer.print(" input, ");
                    xmlTypeWriter.writeCode(printer);
                    printer.print(" output");
                }
                printer.print(") throws ");
                printer.print(WriterUtils.RADIX_APP_EXCEPTION_CLASS_NAME);
                printer.printComma();
                printer.print(INTERRUPTED_CLASS_NAME);

                printer.enterBlock(1);
                printer.println('{');
                //  boolean first = true;
                for (MCInfo info : methods) {
                    //      if (first) {
                    //           first = false;
                    //          } else {
                    //          printer.print(" else ");
                    //         }
                    printer.print("if(cmdId == ");
                    WriterUtils.writeAutoVariable(printer, info.command.getId().toCharArray());
                    printer.println("){");


                    if (info.method != null) {
                        printer.enterBlock();
                        Profile profile = info.method.getProfile();

                        boolean resultRequired = false;
                        if (profile.getReturnValue() != null && !profile.getReturnValue().getType().isVoid()) {
                            writeCode(printer, profile.getReturnValue().getType(), info.method);
                            printer.print(" result = ");
                            resultRequired = true;
                        }
                        writeUsage(printer, info.method);
                        ECommandScope scope = ((AdsScopeCommandDef) info.command).getScope();
                        final AdsTypeDeclaration inType = info.command.getData().getInType();
                        switch (scope) {
                            case GROUP:
                                if (!inType.isVoid()) {
                                    printer.print("(");
                                    printCommandInput(printer, inType, info.method);
                                    printer.print(");");
                                } else {
                                    printer.println("();");
                                }
                                break;
                            case OBJECT:
                                if (!inType.isVoid()) {
                                    printer.print("(");
                                    printCommandInput(printer, inType, info.method);
                                    if (classType != EClassType.FORM_HANDLER) {
                                        printer.print(",");
                                    }
                                } else {
                                    printer.print('(');
                                }
                                if (classType == EClassType.FORM_HANDLER) {
                                    printer.println(");");
                                } else {

                                    printer.println("newPropValsById);");
                                }
                                break;
                            case PROPERTY:
                                printer.print("(propId");
                                if (!inType.isVoid()) {
                                    printer.print(",");
                                    printCommandInput(printer, inType, info.method);
                                }
                                if (classType == EClassType.FORM_HANDLER) {
                                    printer.println(");");
                                } else {
                                    printer.print(",");
                                    printer.println("newPropValsById);");
                                }
                                break;
                        }

                        if (resultRequired) {
                            switch (info.command.getData().getNature()) {
                                case XML_IN_OUT:

                                    printer.println("if(result != null)");
                                    printer.enterBlock(1);
                                    printer.println("output.set(result);");
                                    printer.leaveBlock(1);
                                    printer.println("return null;");
                                    break;
                                default:
                                    printer.leaveBlock(1);
                                    printer.println("return result;");
                            }
                        } else {
                            printer.println("return null;");
                        }
                        printer.leaveBlock();
                    } else {
                        AdsRPCCommandDef cmd = (AdsRPCCommandDef) info.command;
                        AdsMethodDef calledMethod = cmd.findMethod();
                        if (calledMethod == null) {
                            return false;
                        }
                        info.method = calledMethod;
                        rpcCalls.add(info);
                        if (!writeRpcCallExecutorServerInvocation(info, printer)) {
                            return false;
                        }
                    }
                    printer.print("} else ");
                }


                printer.enterBlock();
                printer.println();

                if (classType == EClassType.ENTITY || classType == EClassType.APPLICATION) {

                    printer.println("return super.execCommand(cmdId,propId,input,newPropValsById,output);");
                } else if (classType == EClassType.FORM_HANDLER) {
                    printer.println("return super.execCommand(cmdId,propId,input,output);");
                } else {
                    printer.println("return super.execCommand(cmdId,input,output);");
                }
                printer.leaveBlock(2);
                printer.println('}');
                if (!rpcCalls.isEmpty()) {

                    Collections.sort(rpcCalls, new MCComparator());
                    for (MCInfo info : rpcCalls) {
                        if (!writeRpcCallExecutorServerImplementation(info, printer, xmlTypeWriter)) {
                            return false;
                        }
                    }
                }
                return true;
            default:
                return false;
        }
    }
    private static final char[] RPC_SERVER_SIDE_INVOCATION_CLASS_NAME = ServerSideInvocation.class.getName().replace("$", ".").toCharArray();

    private boolean writeRpcCallExecutorServerInvocation(MCInfo info, CodePrinter printer) {
        printer.enterBlock();
        printer.print("final ");
        writeCode(printer, info.command.getData().getOutType(), info.method);
        printer.print(" result = ");
        writeRpcCallExecutorServerNameWithFirstParenthesis(info, printer);
        printCommandInput(printer, info.command.getData().getInType(), info.method);
        printer.println(");");
        printer.println("if(result != null)");
        printer.enterBlock(1);
        printer.println("output.set(result);");
        printer.leaveBlock(1);
        printer.println("return null;");
        printer.leaveBlock();
        return true;
    }

    private boolean writeRpcCallExecutorServerImplementation(MCInfo info, CodePrinter printer, CodeWriter xmlTypeWriter) {
        printer.print("private final ");
        writeCode(printer, info.command.getData().getOutType(), info.method);
        writeRpcCallExecutorServerNameWithFirstParenthesis(info, printer);
        writeCode(printer, info.command.getData().getInType(), info.method);
        printer.println(" input){");
        printer.enterBlock();
        printer.print("final ");
        printer.print(RPC_SERVER_SIDE_INVOCATION_CLASS_NAME);
        printer.print(" invoker = new ");
        printer.print(RPC_SERVER_SIDE_INVOCATION_CLASS_NAME);
        printer.println("(input){");
        printer.enterBlock();
        printer.println("@Override");
        printer.print("protected  ");
        printer.print(WriterUtils.RADIX_VAL_TYPE_CLASS_NAME);
        AdsClassDef ownerClass = def.getOwnerClass();

        printer.println(" getReturnType(){");
        printer.enterBlock();
        boolean isVoidMethod = false;
        AdsTypeDeclaration rd = info.method.getProfile().getReturnValue().getType();
        int returnTypeState = AdsRPCMethodWriter.isNormalType(ownerClass, rd);
        if (rd == null || rd.getTypeId() == null) {
            isVoidMethod = true;
            printer.println("return null;");
        } else {
            printer.print("return ");
            if (returnTypeState == 1) {
                WriterUtils.writeEnumFieldInvocation(printer, EValType.STR);
            } else {
                WriterUtils.writeEnumFieldInvocation(printer, rd.getTypeId());
            }
            printer.printlnSemicolon();
        }
        printer.leaveBlock();
        printer.println('}');

        printer.println("protected  Object invokeImpl(Object[] arguments){");
        printer.enterBlock();
        //prepare argument and their types
        int index = 0;



        for (MethodParameter p : info.method.getProfile().getParametersList()) {
            AdsType type = p.getType().resolve(ownerClass).get();
            if (type == null) {
                return false;
            }
            printer.print("final ");
            writeCode(printer, p.getType(), info.method);
            printer.print(" p");
            printer.print(index);
            printer.print(" = ");

            int typeState = AdsRPCMethodWriter.isNormalType(info.method, p.getType());
            String varName;
            String testVarName = "arguments[" + index + "]";
            if (typeState == -1) {
                return false;
            } else if (typeState == 1) {
                CodePrinter stubPrinter = CodePrinter.Factory.newJavaPrinter(printer);
                WriterUtils.writeServerArteAccessMethodInvocation(info.method, stubPrinter);
                stubPrinter.print(".getEntityObject(new ");
                stubPrinter.print(WriterUtils.RADIX_PID_CLASS_NAME);
                stubPrinter.print("(");
                WriterUtils.writeServerArteAccessMethodInvocation(info.method, stubPrinter);
                stubPrinter.printComma();
                AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) (((AdsClassType) type).getSource());
                WriterUtils.writeIdUsage(stubPrinter, clazz.getEntityId());
                stubPrinter.printComma();
                stubPrinter.print("(Str)arguments[" + index + "]");
                stubPrinter.print("))");
                varName = stubPrinter.toString();
            } else {
                varName = testVarName;
            }

            if (!WriterUtils.writeSimpleRadixTypeValueConversion(this, p.getType(), type, ownerClass, testVarName, varName, usagePurpose, printer, false, null)) {
                return false;
            }
            index++;
        }

        String returnValStr;

        if (isVoidMethod) {
            returnValStr = "null";
            ownerClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
            printer.print('.');
            if (!info.method.getAccessFlags().isStatic()) {
                printer.print("this.");
            }
            printer.print(info.method.getId());
            printer.print('(');
            for (int i = 0; i < index; i++) {
                if (i > 0) {
                    printer.printComma();
                }
                printer.print('p');
                printer.print(i);
            }
            printer.println(");");
        } else {
            returnValStr = "$res$";
            printer.print("Object $res$ =");
            ownerClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
            printer.print('.');
            if (!info.method.getAccessFlags().isStatic()) {
                printer.print("this.");
            }
            printer.print(info.method.getId());
            printer.print('(');
            for (int i = 0; i < index; i++) {
                if (i > 0) {
                    printer.printComma();
                }
                printer.print('p');
                printer.print(i);
            }
            printer.println(");");
            if (returnTypeState == 1) {
                printer.print("$res$ = $res$==null?null:((");
                printer.print(JavaSourceSupport.RADIX_SERVER_TYPES_PACKAGE_NAME);
                printer.print('.');
                printer.print(JavaSourceSupport.RADIX_TYPE_ENTITY);
                printer.println(")$res$).getPid().toString();");
            }
        }


        printer.print("return ");
        printer.print(returnValStr);
        printer.println(';');

        printer.leaveBlock();
        printer.println('}');


        printer.leaveBlock();
        printer.println("};");
        printer.println("return invoker.invoke();");
        printer.leaveBlock();
        printer.println("}");
        return true;
    }

    private void writeRpcCallExecutorServerNameWithFirstParenthesis(MCInfo info, CodePrinter printer) {
        printer.print(" rpc_call");
        printer.print(info.command.getId());
        printer.print("_implementation(");
    }
}
