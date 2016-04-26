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

package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;

import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.*;
import org.eclipse.jdt.internal.compiler.ast.Block;


public class AdsSqlClassExecuteWriter extends AdsMethodWriter<AdsMethodDef> {

    private final AdsSqlClassDef sqlClass;

    public AdsSqlClassExecuteWriter(JavaSourceSupport support, AdsMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
        this.sqlClass = (AdsSqlClassDef) method.getOwnerClass();
    }

    private static void writeCloseStatement(CodePrinter cp) {
        cp.println("if ($statement != null) {");
        cp.println("\t$preparedStatementsCache.close($arte, $statement);");
        cp.println("}");
    }

    private static boolean isContainsParamsTransfer(AdsSqlClassDef sqlClass) {
        for (Sqml.Item item : sqlClass.getSource().getItems()) {
            if (item instanceof ParameterTag) {
                return true;
            }
        }
        return false;
    }

    private static boolean isContainsFields(AdsSqlClassDef sqlClass) {
        for (Sqml.Item item : sqlClass.getSource().getItems()) {
            if (item instanceof PropSqlNameTag) {
                final PropSqlNameTag fiedRef = (PropSqlNameTag) item;
                if (fiedRef.getOwnerType() == PropSqlNameTag.EOwnerType.THIS) {
                    return true;
                }
            }
        }
        return false;
    }

    // static for report class writer
    public static void writeBody(AdsSqlClassDef sqlClass, CodePrinter cp, boolean execute) {
        final EClassType classType = sqlClass.getClassDefType();
        final Sqml sqml = sqlClass.getSource();

        cp.enterBlock();

        // calculate and define dynamic parameters, only for reports, because method cursor.open is static
        if (classType == EClassType.REPORT) {
            for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                cp.println("set" + param.getId() + "$$$(" + param.getName() + ");");
            }

            cp.println("beforeExecute();");

            final AdsSqlClassDynamicParamsProcessor dynamicParamsProcessor = new AdsSqlClassDynamicParamsProcessor(cp);
            dynamicParamsProcessor.process(sqml);

            for (AdsParameterPropertyDef p : sqlClass.getInputParameters()) {
                cp.print(p.getName());
                cp.print(" = ");
                p.getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_EXECUTABLE).writeUsage(cp);
                cp.println(";");
            }
        }

        // sql build
        final AdsSqlClassSqlProcessor sqmlProcessor = AdsSqlClassSqlProcessor.Factory.newInstance(cp);
        sqmlProcessor.process(sqml);

        // initialization
        cp.print("org.radixware.kernel.server.arte.Arte $arte = ");
        WriterUtils.writeServerArteAccessMethodInvocation(sqlClass, cp);
        cp.println(";");

        // debug
        if (classType == EClassType.REPORT) {
            cp.println("if ($arte.getTrace().getMinSeverity(EEventSource.ARTE_REPORTS)<=EEventSeverity.DEBUG.getValue().longValue()) {");
            cp.enterBlock();
            final AdsSqlClassSqlProcessor debugProcessor = AdsSqlClassSqlProcessor.Factory.newForDebug(cp);
            debugProcessor.process(sqml);
            cp.println("$arte.getTrace().put(EEventSeverity.DEBUG, $debug.toString(), EEventSource.ARTE_REPORTS);");
            cp.leaveBlock();
            cp.println("}");
        }

        // statement creation
        final String statementJavaClassName = "java.sql." + (classType == EClassType.SQL_PROCEDURE ? "CallableStatement" : "PreparedStatement");
        cp.println(statementJavaClassName + " $statement = null;");

        cp.println("try{");
        cp.enterBlock();


        cp.println("$statement = (" + statementJavaClassName + ")$preparedStatementsCache.findOrCreateStatementBySql($arte, $sql.toString());");
        //calc actual indices of cursor fields

        // calc indexes of output parameters
        if (sqlClass instanceof AdsProcedureClassDef) {
            final AdsSqlClassOutParamsIdxProcessor outParamsIdxProcessor = new AdsSqlClassOutParamsIdxProcessor(cp);
            outParamsIdxProcessor.process(sqml);
        }

        // input parameters setters, register out params
        if (isContainsParamsTransfer(sqlClass)) {
            final AdsSqlClassParamsBindingProcessor paramsBindingProcessor = new AdsSqlClassParamsBindingProcessor(cp);
            paramsBindingProcessor.process(sqml);
        }

        final String classId = String.valueOf(sqlClass.getId());
        //final String exceptionMessage;
        if (execute) {
            switch (classType) {
                case SQL_CURSOR: {
                    cp.println(classId + " $result = new " + classId + "();");


                    if (isContainsFields(sqlClass)) {
                        //write field bindings
                        final AdsSqlClassFieldsBindingProcessor fieldBindingsProcessor = new AdsSqlClassFieldsBindingProcessor(cp, "$result");
                        fieldBindingsProcessor.process(sqml);
                    }
                    cp.println("$result.executeQuery($statement);");


                    for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                        cp.println("$result.set" + param.getId() + "$$$(" + param.getName() + ");");
                    }
                    cp.println("return $result;");
                    //exceptionMessage = "SQL cursor open failure";
                }
                break;
                case REPORT: {
                    if (isContainsFields(sqlClass)) {
                        final AdsSqlClassFieldsBindingProcessor fieldBindingsProcessor = new AdsSqlClassFieldsBindingProcessor(cp, "this");
                        fieldBindingsProcessor.process(sqml);
                    }

                    cp.println("executeQuery($statement);");


                    //exceptionMessage = "Report execute failure";
                }
                break;
                case SQL_PROCEDURE:
                    cp.println(classId + " $result = new " + classId + "();");
                    cp.println("$result.executeQuery($statement);");

                    final AdsSqlClassOutParamsLookupProcessor outParamsLookupProcessor = new AdsSqlClassOutParamsLookupProcessor(cp);
                    outParamsLookupProcessor.process(sqml);
                    for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                        if (param.calcDirection() == EParamDirection.IN) {
                            cp.println("$result.set" + param.getId() + "$$$(" + param.getName() + ");");
                        }
                    }
                    cp.println("return $result;");
                    //exceptionMessage = "SQL procedure execute failure";
                    break;
                case SQL_STATEMENT:
                    cp.print("new ");
                    cp.print(classId);
                    cp.println("().executeQuery($statement);");
                    //exceptionMessage = "SQL statement execute failure";
                    break;
                default:
                    throw new IllegalStateException();
            }
        } else {
            switch (classType) {
                case SQL_CURSOR:
                case SQL_PROCEDURE:
                    cp.println("return null;");
            }
        }
        cp.leaveBlock();
        cp.println("} catch(java.sql.SQLException cause){");
        cp.enterBlock();
        if (classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) {
            writeCloseStatement(cp);
        }
        //cp.println("throw new org.radixware.kernel.server.exceptions.DatabaseError(\"" + exceptionMessage + ": #" + sqlClass.getId() + "\", cause);");
        cp.println("throw new org.radixware.kernel.server.exceptions.DatabaseError(cause);"); // RADIX-3994
        cp.leaveBlock();
        cp.print("}");
        if ((classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) && execute) {
            cp.println();
        } else {
            cp.println(" finally {");
            cp.enterBlock();
            writeCloseStatement(cp);
            cp.leaveBlock();
            cp.print("}");
        }
        cp.leaveBlock();
    }

    @Override
    protected void writeBody(CodePrinter cp) {
        writeBody(sqlClass, cp, true);
    }

    public static Statement[] createStatements(AdsSqlClassDef sqlClass, boolean execute) {
        List<Statement> stmts = new LinkedList<>();
        final EClassType classType = sqlClass.getClassDefType();
        final Sqml sqml = sqlClass.getSource();

        // calculate and define dynamic parameters, only for reports, because method cursor.open is static
        if (classType == EClassType.REPORT) {
            for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                stmts.add(createSetValue("set" + param.getId() + "$$$", SqlClassVariables.nameRef(param.getName())));
            }
            stmts.add(createMessageSend(ThisReference.implicitThis(), "beforeExecute"));

            final AdsSqlClassDynamicParamsProcessor dynamicParamsProcessor = new AdsSqlClassDynamicParamsProcessor(stmts);
            dynamicParamsProcessor.process(sqml);

            for (AdsParameterPropertyDef p : sqlClass.getInputParameters()) {
                stmts.add(new Assignment(SqlClassVariables.nameRef(p.getName()), new TaggedSingleNameReference(sqlClass, p, false), 0));
            }
        }

        // sql build
        final AdsSqlClassSqlProcessor sqmlProcessor = AdsSqlClassSqlProcessor.Factory.newInstance(stmts);
        sqmlProcessor.process(sqml);
        String arteVarName = "$arte";
        stmts.add(createArtePtrDeclaration(sqlClass, arteVarName));
        //SingleNameReference arteVarRef = new ReenterableSingleNameReference(arteVarName.toCharArray());
        // debug
        if (classType == EClassType.REPORT) {
            List<Statement> debugTraceStmts = new LinkedList<>();

            final AdsSqlClassSqlProcessor debugProcessor = AdsSqlClassSqlProcessor.Factory.newForDebug(debugTraceStmts);
            debugProcessor.process(sqml);

            debugTraceStmts.add(createMessageSend(createMessageSend(SqlClassVariables.nameRef(arteVarName), "getTrace"), "put",
                    createEnumFieldRef(EEventSeverity.DEBUG),
                    createMessageSend(SqlClassVariables.nameRef("$debug"), "toString"),
                    createEnumFieldRef(EEventSource.ARTE_REPORTS)));
            IfStatement traceCondition = new IfStatement(
                    new BinaryExpression(
                    createMessageSend(
                    createMessageSend(SqlClassVariables.nameRef(arteVarName), "getTrace"),
                    "getMinSeverity",
                    createEnumFieldRef(EEventSource.ARTE_REPORTS)),
                    createMessageSend(createMessageSend(createEnumFieldRef(EEventSeverity.DEBUG), "getValue"), "longValue"),
                    OperatorIds.LESS_EQUAL),
                    createBlock(debugTraceStmts.toArray(new Statement[debugTraceStmts.size()])), 0, 0);
            stmts.add(traceCondition);
        }

        // statement creation
        LocalDeclaration statementVar = createDeclaration("$statement", createQualifiedType(classType == EClassType.SQL_PROCEDURE
                ? new char[][]{JAVA, SQL, "CallableStatement".toCharArray()} : new char[][]{JAVA, SQL, "PreparedStatement".toCharArray()}), new NullLiteral(0, 0));
        stmts.add(statementVar);


        FieldReference stmtsCacheRef = new FieldReference("$preparedStatementsCache".toCharArray(), 0);
        stmtsCacheRef.receiver = ThisReference.implicitThis();
        List<Statement> tryStatements = new LinkedList<>();
        tryStatements.add(createAssignment(SqlClassVariables.stmtVarRef(),
                new CastExpression(createMessageSend(stmtsCacheRef, "findOrCreateStatementBySql", SqlClassVariables.nameRef(arteVarName), createMessageSend(SqlClassVariables.nameRef("$sql"), "toString")), statementVar.type)));

        //calc actual indices of cursor fields

        // calc indexes of output parameters
        if (sqlClass instanceof AdsProcedureClassDef) {
            final AdsSqlClassOutParamsIdxProcessor outParamsIdxProcessor = new AdsSqlClassOutParamsIdxProcessor(tryStatements);
            outParamsIdxProcessor.process(sqml);
        }

        // input parameters setters, register out params
        if (isContainsParamsTransfer(sqlClass)) {
            final AdsSqlClassParamsBindingProcessor paramsBindingProcessor = new AdsSqlClassParamsBindingProcessor(tryStatements);
            paramsBindingProcessor.process(sqml);
        }

        final String classId = String.valueOf(sqlClass.getId());
        //final String exceptionMessage;
        final char[] resultVarName = "$result".toCharArray();
        if (execute) {
            switch (classType) {
                case SQL_CURSOR: {
                    tryStatements.add(createDeclaration("$result", new SingleTypeReference(classId.toCharArray(), 0), createAlloc(new SingleTypeReference(classId.toCharArray(), 0))));

                    if (isContainsFields(sqlClass)) {
                        //write field bindings
                        final AdsSqlClassFieldsBindingProcessor fieldBindingsProcessor = new AdsSqlClassFieldsBindingProcessor(tryStatements, "$result");
                        fieldBindingsProcessor.process(sqml);
                    }
                    tryStatements.add(createMessageSend(SqlClassVariables.nameRef(resultVarName), "executeQuery", SqlClassVariables.stmtVarRef()));



                    for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                        tryStatements.add(createMessageSend(SqlClassVariables.nameRef(resultVarName), "set" + param.getId() + "$$$", SqlClassVariables.nameRef(param.getName())));
                    }
                    tryStatements.add(new ReturnStatement(SqlClassVariables.nameRef(resultVarName), 0, 0));
                }
                break;
                case REPORT: {
                    if (isContainsFields(sqlClass)) {
                        final AdsSqlClassFieldsBindingProcessor fieldBindingsProcessor = new AdsSqlClassFieldsBindingProcessor(tryStatements, "this");
                        fieldBindingsProcessor.process(sqml);
                    }
                    tryStatements.add(createMessageSend(ThisReference.implicitThis(), "executeQuery", SqlClassVariables.stmtVarRef()));
                }
                break;
                case SQL_PROCEDURE:
                    tryStatements.add(createDeclaration("$result", new SingleTypeReference(classId.toCharArray(), 0), createAlloc(new SingleTypeReference(classId.toCharArray(), 0))));
                    tryStatements.add(createMessageSend(SqlClassVariables.nameRef(resultVarName), "executeQuery", SqlClassVariables.stmtVarRef()));

                    final AdsSqlClassOutParamsLookupProcessor outParamsLookupProcessor = new AdsSqlClassOutParamsLookupProcessor(stmts);
                    outParamsLookupProcessor.process(sqml);
                    for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                        if (param.calcDirection() == EParamDirection.IN) {
                            tryStatements.add(createMessageSend(SqlClassVariables.nameRef(resultVarName), "set" + param.getId() + "$$$", SqlClassVariables.nameRef(param.getName())));
                        }
                    }
                    tryStatements.add(new ReturnStatement(SqlClassVariables.nameRef(resultVarName), 0, 0));
                    //exceptionMessage = "SQL procedure execute failure";
                    break;
                case SQL_STATEMENT:
                    tryStatements.add(createMessageSend(createAlloc(new SingleTypeReference(classId.toCharArray(), 0)), "executeQuery", SqlClassVariables.stmtVarRef()));
                    break;
                default:
                    throw new IllegalStateException();
            }
        } else {
            switch (classType) {
                case SQL_CURSOR:
                case SQL_PROCEDURE:
                    tryStatements.add(new ReturnStatement(new NullLiteral(0, 0), 0, 0));
            }
        }
        TryStatement openStatement = new TryStatement();
        stmts.add(openStatement);
        openStatement.tryBlock = createBlock(tryStatements.toArray(new Statement[tryStatements.size()]));
        openStatement.catchArguments = new Argument[]{new Argument("cause".toCharArray(), 0, createQualifiedType(SQLEXCEPTION_TYPE_NAME), 0)};
        tryStatements.clear();


        if (classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) {
            writeCloseStatement(arteVarName, tryStatements);
        }
        tryStatements.add(createThrow(DATABASEERROR_TYPE_NAME, null, "cause".toCharArray()));
        openStatement.catchBlocks = new Block[]{createBlock(tryStatements.toArray(new Statement[tryStatements.size()]))};
        if ((classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) && execute) {
        } else {
            tryStatements.clear();

            writeCloseStatement(arteVarName, tryStatements);
            openStatement.finallyBlock = createBlock(tryStatements.toArray(new Statement[tryStatements.size()]));
        }

        return stmts.toArray(new Statement[stmts.size()]);
    }

    private static void writeCloseStatement(String argeVarName, List<Statement> stmts) {
        stmts.add(new IfStatement(new EqualExpression(SqlClassVariables.stmtVarRef(), new NullLiteral(0, 0), OperatorIds.NOT_EQUAL), createMessageSend(
                SqlClassVariables.nameRef("$preparedStatementsCache"), "close", SqlClassVariables.nameRef(argeVarName), SqlClassVariables.stmtVarRef()), 0, 0));

    }
}
