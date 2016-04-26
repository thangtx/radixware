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

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.*;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ReenterableSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsSystemMethodWriter extends AdsMethodWriter<AdsSystemMethodDef> {

    public AdsSystemMethodWriter(JavaSourceSupport support, AdsSystemMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
    }

    protected void writePidLoader(CodePrinter printer, AdsSystemMethodDef.ArgumentsProvider p) {
        printer.print(WriterUtils.RADIX_PID_CLASS_NAME);
        printer.print(" pid = new ");
        printer.print(WriterUtils.RADIX_PID_CLASS_NAME);
        printer.print('(');
        WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
        printer.printComma();
        WriterUtils.writeIdUsage(printer, p.tableAssoc.getId());
        printer.printComma();
        printer.print(p.getParameterInfos().get(0).name);
        printer.println(");");
        printer.enterBlock(1);
        printer.println("try{");
        printer.println("return (");
        writeUsage(printer, def.getProfile().getReturnValue().getType(), def);
        printer.print(") ");
        WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
        printer.print(".getEntityObject(pid,null,");
        printer.leaveBlock(1);
        printer.print(p.getParameterInfos().get(1).name);
        printer.println(");");
        printer.print("}catch(");
        printer.print(WriterUtils.ENTITY_OBJECT_NOT_EXISTS_ERROR_CLASS_NAME);
        printer.enterBlock(1);
        printer.println(" e){");
        printer.leaveBlock(1);
        printer.println("return null;");
        printer.println('}');
    }

    protected void writePKLoader(CodePrinter printer, AdsSystemMethodDef.ArgumentsProvider p) {
        printer.print("final java.util.HashMap<");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.print(",Object> pkValsMap = new java.util.HashMap<");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.print(",Object>(");
        List<AdsSystemMethodDef.ArgumentsProvider.ArgumentInfo> params = p.getParameterInfos();
        printer.print(params.size() * 2 + 1);
        printer.print(");\n");

        for (AdsSystemMethodDef.ArgumentsProvider.ArgumentInfo info : params) {
            if (info.columnAssoc == null) {
                break;
            }
            printer.print("\t\tif(");
            printer.print(info.name);
            printer.println("==null) return null;");     //  throw new ");
            //printer.print(WriterUtils.ILLEGAL_USAGE_ERROR_CLASS_NAME);
            //printer.print("(");
            //printer.printStringLiteral("Value for primary key column '" + info.columnAssoc.getName() + "' ("+ info.columnAssoc.getDbName() +") passed through parameter '" + info.name + "' must not be null");
            //printer.println(");");
            printer.print("\t\tpkValsMap.put(");
            WriterUtils.writeIdUsage(printer, info.columnAssoc.getId());
            printer.printComma();
            printer.print(info.name);
            printer.print(");\n");
        }

        printer.print(WriterUtils.RADIX_PID_CLASS_NAME);
        printer.print(" pid = new ");
        printer.print(WriterUtils.RADIX_PID_CLASS_NAME);
        printer.print('(');
        WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
        printer.printComma();
        WriterUtils.writeIdUsage(printer, p.tableAssoc.getId());
        printer.println(",pkValsMap);");
        printer.enterBlock(1);
        printer.println("try{");
        printer.println("return (");
        writeUsage(printer, def.getProfile().getReturnValue().getType(), def);
        printer.print(") ");
        WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
        printer.print(".getEntityObject(pid,null,");
        printer.leaveBlock(1);
        printer.print(params.get(params.size() - 1).name);
        printer.println(");");
        printer.print("}catch(");
        printer.print(WriterUtils.ENTITY_OBJECT_NOT_EXISTS_ERROR_CLASS_NAME);
        printer.enterBlock(1);
        printer.println(" e){");
        printer.leaveBlock(1);
        printer.println("return null;");
        printer.println('}');
    }

    @Override
    protected void writeBody(CodePrinter printer) {
        AdsSystemMethodDef.ArgumentsProvider p = ((AdsSystemMethodDef) def).getArgumentsProvider();
        if (p.isValid()) {
            if (def.getId() == AdsSystemMethodDef.ID_LOAD_BY_PID_STR) {
                writePidLoader(printer, p);
            } else if (def.getId() == AdsSystemMethodDef.ID_LOAD_BY_PK) {
                writePKLoader(printer, p);
            }
            // see AdsSqlClassOpenWriter, AdsSqlClassCloseWriter, etc.
        } else {
            printer.print("throw new IllegalArgumentException();");
        }
    }

    private static Statement[] createPidLLoaderStatements(AdsSystemMethodDef def, AdsSystemMethodDef.ArgumentsProvider p) {
        LocalDeclaration decl = createDeclaration("pid", createQualifiedType(SERVERPIDID_TYPE_NAME), createAlloc(createQualifiedType(SERVERPIDID_TYPE_NAME),
                createArteAccessMethodInvocation(def), createIdInvocation(p.tableAssoc.getId()), new ReenterableSingleNameReference(p.getParameterInfos().get(0).name.toCharArray())));

        return new Statement[]{
            decl, createLoadTryBlock(def, p.getParameterInfos().get(1).name)
        };
    }

    private static TryStatement createLoadTryBlock(AdsSystemMethodDef def, String paramName) {
        TryStatement tryStmt = new TryStatement();

        tryStmt.tryBlock = createBlock(
                new ReturnStatement(createMessageSend(createArteAccessMethodInvocation(def), "getEntityObject", new ReenterableSingleNameReference("pid".toCharArray()), new NullLiteral(0, 0), new ReenterableSingleNameReference(paramName.toCharArray())), 0, 0));
        tryStmt.catchArguments = new Argument[]{
            new Argument("e".toCharArray(), 0, createQualifiedType(ENTITYOBJECTNOTEXISTERROR_TYPE_NAME), 0)
        };
        tryStmt.catchBlocks = new Block[]{
            createBlock(new ReturnStatement(new NullLiteral(0, 0), 0, 0))
        };
        return tryStmt;
    }

    private static Statement[] createPKLLoaderStatements(AdsSystemMethodDef def, AdsSystemMethodDef.ArgumentsProvider p) {
        List<Statement> stmts = new LinkedList<>();
        List<AdsSystemMethodDef.ArgumentsProvider.ArgumentInfo> params = p.getParameterInfos();
        LocalDeclaration mapDecl = createDeclaration("pkValsMap".toCharArray(), createQualifiedType(JAVAUTILMAP_TYPE_NAME, createQualifiedType(ID_TYPE_NAME), createQualifiedType(JAVALANGOBJECT_TYPENAME)),
                createAlloc(createQualifiedType(JAVAUTILHASHMAP_TYPE_NAME, createQualifiedType(ID_TYPE_NAME), createQualifiedType(JAVALANGOBJECT_TYPENAME)), createIntConstant(params.size() * 2 + 1)));

        stmts.add(mapDecl);

        for (AdsSystemMethodDef.ArgumentsProvider.ArgumentInfo info : params) {
            if (info.columnAssoc == null) {
                break;
            }
            IfStatement ifStmt = new IfStatement(new EqualExpression(new ReenterableSingleNameReference(info.name.toCharArray()), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL), new ReturnStatement(new NullLiteral(0, 0), 0, 0), 0, 0);
            stmts.add(ifStmt);
            stmts.add(createMessageSend(new ReenterableSingleNameReference("pkValsMap".toCharArray()), "put", createIdInvocation(info.columnAssoc.getId()), new ReenterableSingleNameReference(info.name.toCharArray())));
        }
        LocalDeclaration pidDecl = createDeclaration("pid", createQualifiedType(SERVERPIDID_TYPE_NAME), createAlloc(createQualifiedType(SERVERPIDID_TYPE_NAME),
                createArteAccessMethodInvocation(def),
                createIdInvocation(p.tableAssoc.getId()),
                new ReenterableSingleNameReference("pkValsMap".toCharArray())));
        stmts.add(pidDecl);
        stmts.add(createLoadTryBlock(def, params.get(params.size() - 1).name));
        return stmts.toArray(new Statement[stmts.size()]);

    }

    public static Statement[] createStatements(AdsSystemMethodDef def) {
        AdsSystemMethodDef.ArgumentsProvider p = def.getArgumentsProvider();
        if (p.isValid()) {
            if (def.getId() == AdsSystemMethodDef.ID_LOAD_BY_PID_STR) {
                return createPidLLoaderStatements(def, p);
            } else if (def.getId() == AdsSystemMethodDef.ID_LOAD_BY_PK) {
                return createPKLLoaderStatements(def, p);
            }
        } else {
            return new Statement[]{
                createThrow("IllegalArgumentException".toCharArray(), null, false)
            };
        }

        return new Statement[0];
    }
}