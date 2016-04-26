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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RPCProcessor;
import org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation;


public class AdsRPCMethodWriter extends AdsMethodWriter<AdsRPCMethodDef> {

    public AdsRPCMethodWriter(JavaSourceSupport support, AdsRPCMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
    }

    private void writeCommandInstanceVarName(AdsCommandDef command, CodePrinter printer) {
        printer.print("_cmd_");
        printer.print(command.getId());
        printer.print("_instance_");
    }
    private static final char[] CLIENT_INVOCATION_CLASS_NAME = ClientSideInvocation.class.getName().replace("$", ".").toCharArray();
    private static final char[] RPC_ARGUMENT_INFO = RPCProcessor.ArgumentInfo.class.getName().replace("$", ".").toCharArray();

    @Override
    protected void writeBody(CodePrinter printer) {
        AdsCommandDef command = def.findCommand();
        if (command == null) {
            return;
        }
        if (!def.getUsageEnvironment().isClientEnv()) {
            return;
        }
        printer.enterBlock();

        AdsType type = command.getType(EValType.USER_CLASS, null);
        printer.print("final ");
        writeCode(printer, type, def);
        printer.print(" ");
        writeCommandInstanceVarName(command, printer);
        printer.print(" = (");
        writeCode(printer, type, def);
        printer.print(")getCommand(");
        WriterUtils.writeIdUsage(printer, command.getId());
        printer.println(");");


        printer.print("final java.util.List<");
        printer.print(RPC_ARGUMENT_INFO);
        printer.print("> $remote$call$arg$list$store$ = new java.util.LinkedList<");
        printer.print(RPC_ARGUMENT_INFO);
        printer.println(">();");
        for (MethodParameter p : def.getProfile().getParametersList()) {
            printer.print("$remote$call$arg$list$store$.add(new ");
            printer.print(RPC_ARGUMENT_INFO);
            printer.print('(');

            EValType typeId = p.getType().getTypeId();
            boolean needPidConversion = false;
            int typeState = isNormalType(def, p.getType());
            if (typeState == -1) {
                return;
            } else if (typeState == 1) {
                needPidConversion = true;
                typeId = EValType.STR;
            }

            WriterUtils.writeEnumFieldInvocation(printer, typeId);
            printer.print(',');
            if (needPidConversion) {
                printer.print(p.getName() + " == null ? null : ((");
                printer.print(AdsModelClassDef.ENTITY_MODEL_JAVA_CLASS_NAME);
                printer.print(")" + p.getName() + ").getPid().toString()");
            } else {
                printer.print(p.getName());
            }
            printer.println("));");
        }
        printer.print("final ");
        printer.print(CLIENT_INVOCATION_CLASS_NAME);
        printer.print(" $invoker_instance$ = new ");
        printer.print(CLIENT_INVOCATION_CLASS_NAME);
        printer.println("($remote$call$arg$list$store$){");
        printer.enterBlock();
        printer.print("protected ");
        writeCode(printer, command.getData().getOutType(), def);
        printer.print(" invokeImpl(");
        writeCode(printer, command.getData().getInType(), def);
        printer.println(" rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{");
        printer.enterBlock();
        printer.print("return ");
        writeCommandInstanceVarName(command, printer);
        printer.println(".send(rq);");
        printer.leaveBlock();
        printer.println('}');
        printer.leaveBlock();
        printer.println("};");

        AdsTypeDeclaration retType = def.getProfile().getReturnValue().getType();
        if (retType != null && retType.getTypeId() != null) {
            printer.println("final Object $rpc$call$result$ = $invoker_instance$.invoke();");
            printer.print("return ");
            Id resultTableId = null;
            if (isNormalType(def, retType) == 1) {
                resultTableId = ((AdsEntityObjectClassDef) ((AdsClassType) retType.resolve(def).get()).getSource()).getEntityId();
            } else if (retType.getTypeId() == EValType.PARENT_REF) {
                AdsMethodDef ssMethod = def.findServerSideMethod();
                if (ssMethod == null) {
                    return;
                }
                AdsTypeDeclaration srvRetType = ssMethod.getProfile().getReturnValue().getType();
                if (srvRetType == null) {
                    return;
                }
                if (isNormalType(ssMethod, srvRetType) == 1) {
                    resultTableId = ((AdsEntityObjectClassDef) ((AdsClassType) srvRetType.resolve(ssMethod).get()).getSource()).getEntityId();
                } else {
                    return;
                }
            }
            WriterUtils.writeSimpleRadixTypeValueConversion(this, retType, retType.resolve(def).get(), def, "$rpc$call$result$", "$rpc$call$result$", usagePurpose, printer, true, resultTableId);
        } else {
            printer.println("$invoker_instance$.invoke();");
        }

        printer.leaveBlock();
    }

    public static int isNormalType(Definition context, AdsTypeDeclaration valType) {
        EValType typeId = valType.getTypeId();

        if (typeId == EValType.USER_CLASS) {
            AdsType paramType = valType.resolve(context).get();
            if (paramType instanceof AdsClassType) {
                AdsClassDef clazz = ((AdsClassType) paramType).getSource();
                if (clazz instanceof AdsEntityObjectClassDef) {
                    return 1;
                }
            }

            return -1;

        } else {
            return 0;
        }
    }
}
