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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.PropertyGetterInvocation;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import static org.radixware.kernel.common.enums.EIfParamTagOperator.EMPTY;
import static org.radixware.kernel.common.enums.EIfParamTagOperator.EQUAL;
import static org.radixware.kernel.common.enums.EIfParamTagOperator.NOT_EMPTY;
import static org.radixware.kernel.common.enums.EIfParamTagOperator.NULL;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.sqml.translate.SqmlTagTranslatorFactory;
import org.radixware.kernel.common.types.Id;


public class AdsSqlClassSqlProcessor extends AdsSqlClassSourceProcessor {

    private final CodePrinter cp;
    private final List<Statement> stmts;
    private final EPurpose purpose;
    private final String SQL;

    public class ParamInfo {

        public final AdsDynamicPropertyDef property;
        public final Id pkPropId;

        public ParamInfo(AdsDynamicPropertyDef property, Id pkPropId) {
            this.property = property;
            this.pkPropId = pkPropId;
        }
    }
    private List<ParamInfo> params = null;

    private enum EPurpose {

        SQL,
        DEBUG,
        TEST
    }

    private AdsSqlClassSqlProcessor(List<Statement> stmts, EPurpose purpose) {
        super(CodePrinter.Factory.newNullPrinter());
        this.stmts = stmts;
        this.cp = null;
        this.purpose = purpose;
        this.SQL = (this.purpose == EPurpose.DEBUG ? "$debug" : "$sql");
        if (purpose != EPurpose.TEST) {
            LocalDeclaration decl = new LocalDeclaration(SQL.toCharArray(), 0, 0);
            decl.type = new SingleTypeReference("StringBuffer".toCharArray(), 0);
            decl.initialization = BaseGenerator.createAlloc(new SingleTypeReference("StringBuffer".toCharArray(), 0));
            decl.modifiers = ClassFileConstants.AccFinal;
            stmts.add(decl);
        } else {
            params = new LinkedList<>();
        }
    }

    private AdsSqlClassSqlProcessor(CodePrinter cp, EPurpose purpose) {
        super(cp);
        this.cp = cp;
        this.stmts = null;
        this.purpose = purpose;
        this.SQL = (this.purpose == EPurpose.DEBUG ? "$debug" : "$sql");
        if (purpose != EPurpose.TEST) {
            cp.println("final StringBuffer " + SQL + "=new StringBuffer();");
        } else {
            params = new LinkedList<>();


        }
    }

    public static class Factory {

        public static AdsSqlClassSqlProcessor newInstance(CodePrinter cp) {
            return new AdsSqlClassSqlProcessor(cp, EPurpose.SQL);
        }

        public static AdsSqlClassSqlProcessor newForDebug(CodePrinter cp) {
            return new AdsSqlClassSqlProcessor(cp, EPurpose.DEBUG);
        }

        public static AdsSqlClassSqlProcessor newInstance(List<Statement> stmts) {
            return new AdsSqlClassSqlProcessor(stmts, EPurpose.SQL);
        }

        public static AdsSqlClassSqlProcessor newForDebug(List<Statement> stmts) {
            return new AdsSqlClassSqlProcessor(stmts, EPurpose.DEBUG);
        }

        public static AdsSqlClassSqlProcessor newForTest(CodePrinter cp) {
            return new AdsSqlClassSqlProcessor(cp, EPurpose.TEST);
        }
    }

    private static void printNTimes(CodePrinter cp, String s, int n) {
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                cp.print(", ");
            }
            cp.print(s);
        }
    }

    private static void printNTimes(StringBuilder cp, String s, int n) {
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                cp.append(", ");
            }
            cp.append(s);
        }
    }

    private SingleNameReference sqlRef() {
        return SqlClassVariables.nameRef(SQL);
    }

    private void debugParamValue(AdsDynamicPropertyDef param, AdsPropertyDef prop, List<Statement> stmts) {
        final EValType paramValType = param.getValue().getType().getTypeId();
        final IEnumDef enumDef;
        final EValType propValType;

        if (prop != null) {
            propValType = prop.getValue().getType().getTypeId();
            enumDef = AdsEnumUtils.findPropertyEnum(prop);
        } else {
            enumDef = AdsEnumUtils.findPropertyEnum(param);
            propValType = paramValType;
        }

        String paramName = param.getName();
        if (stmts != null) {
            Expression nullCheck = new EqualExpression(SqlClassVariables.nameRef(paramName), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
            StringBuilder stringLiteral = new StringBuilder();
            stringLiteral.append("/*:").append(paramName);
            if (paramValType.isArrayType()) {
                stringLiteral.append("[\"+$i+\"]");
                Expression itemNullCheck = new EqualExpression(
                        BaseGenerator.createMessageSend(SqlClassVariables.nameRef(paramName), SqlClassVariables.GET_METHOD_NAME, SqlClassVariables.loopVarRef()), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
                nullCheck = new OR_OR_Expression(nullCheck, itemNullCheck, OperatorIds.OR_OR);
            }
            if (prop != null) {
                stringLiteral.append(".").append(prop.getName());
                PropertyGetterInvocation invoke = new PropertyGetterInvocation(prop);
                invoke.receiver = SqlClassVariables.nameRef(paramName);
                Expression propNullCheck = new EqualExpression(invoke, new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
                nullCheck = new OR_OR_Expression(nullCheck, propNullCheck, OperatorIds.OR_OR);
            }
            stringLiteral.append("{*/ ");
            stmts.add(BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(stringLiteral.toString())));
            IfStatement nullBranch = new IfStatement(nullCheck,
                    BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral("NULL")),
                    BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME,
                    BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.VALUE2SQLCONVERTER_TYPE_NAME), "toSqlDebug",
                    (enumDef == null ? SqlClassVariables.nameRef(paramName) : BaseGenerator.createMessageSend(SqlClassVariables.nameRef(paramName), "getValue")),
                    BaseGenerator.createEnumFieldRef(propValType.isArrayType() ? propValType.getArrayItemType() : propValType))),
                    0, 0);
            stmts.add(nullBranch);
            stmts.add(BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(" /*}*/")));
        } else {

            cp.print(SQL + ".append(\"/*:" + paramName);
            paramName = param.getName();

            String nullCheck = paramName + "==null";
            if (paramValType.isArrayType()) {
                paramName += ".get($i)";
                cp.print("[\"+$i+\"]");
                nullCheck += " || " + paramName + "==null";
            }

            if (prop != null) {
                paramName += ".get" + prop.getId().toString() + "()";
                cp.print("." + prop.getName());
                nullCheck += " || " + paramName + "==null";
            }

            cp.println("{*/ \");");


            cp.print("if (" + nullCheck + ") " + SQL + ".append(\"NULL\"); else "
                    + SQL + ".append(org.radixware.kernel.common.utils.ValueToSqlConverter.toSqlDebug(" + paramName);
            if (enumDef != null) {
                cp.print(".getValue()");
            }
            cp.print(", EValType.");
            cp.print(propValType.isArrayType() ? propValType.getArrayItemType().name() : propValType.name());

            cp.println("));");

            printSql(" /*}*/");
        }
    }

    private void printParamValue(AdsDynamicPropertyDef param, AdsPropertyDef prop, Id pkPropId, List<Statement> stmts) {
        if (purpose == EPurpose.DEBUG) {
            debugParamValue(param, prop, stmts);
        } else {
            if (purpose == EPurpose.TEST) {
                cp.print("?");
                params.add(new ParamInfo(param, pkPropId));
            } else {
                if (stmts != null) {
                    stmts.add(BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral("?")));
                } else {
                    cp.println(SQL + ".append(\"?\");");
                }
            }
        }
    }

    private void printPkProp(AdsDynamicPropertyDef param, Id propId, AdsEntityObjectClassDef entity, DdsPrimaryKeyDef pk, int propCount, List<Statement> stmts) {
        if (propCount > 1) {
            printSql("(", stmts);
        }
        for (int i = 0; i < propCount; i++) {
            if (i > 0) {
                printSql(", ", stmts);
            }
            final Id curPropId = (propId != null ? propId : pk.getColumnsInfo().get(i).getColumnId());
            final AdsPropertyDef prop = entity.getProperties().getById(curPropId, EScope.ALL);
            printParamValue(param, prop, propId, stmts);
        }
        if (propCount > 1) {
            printSql(")", stmts);
        }
    }

    private boolean translatePkParam(AdsDynamicPropertyDef param, Id propId) {
        final AdsEntityObjectClassDef entity = AdsParameterPropertyDef.findEntity(param);
        if (entity == null) {
            return false;
        }
        final DdsTableDef table = entity.findTable(param);
        if (table == null) {
            return false;
        }
        final DdsPrimaryKeyDef pk = table.getPrimaryKey();
        if (pk == null) {
            return false;
        }
        final int propCount = (propId == null ? pk.getColumnsInfo().size() : 1);
        final boolean isArray = param.getValue().getType().getTypeId().isArrayType();

        if (purpose == EPurpose.TEST) {
            if (isArray) {
                final String paramName = param.getName();
                if (isParamNullOrEmpty(param)) {
                    cp.print("(select ");
                    printNTimes(cp, "NULL", propCount);
                    cp.println(" from DUAL where 1=2)");
                } else {
                    printSql("(");
                    for (int $i = 0; $i < getArrayParamSize(paramName); $i++) {
                        if ($i > 0) {
                            printSql(",");
                        }
                        printPkProp(param, propId, entity, pk, propCount, stmts);
                    }
                    printSql(")");
                }
            } else {
                printPkProp(param, propId, entity, pk, propCount, stmts);
            }
        } else {
            if (isArray) {
//          ========== EXAMPLE ===========
//          if (param==null || param.isEmpty())
//              $sql.append("(select NULL, NULL from DUAL where 1=2)");
//          else{
//              int $size=param.size();
//              sql.append("(");
//              for (int $i=0; $i<$size; $i++){
//                  if ($i>0)
//                      $sql.append(", ");
//                  $sql.append("(");
//                  $sql.append("?");
//                  $sql.append(", ");
//                  $sql.append("?");
//                  $sql.append(")");
//              }
//              $sql.append(")");
//          }
                // nothing selected
                final String paramName = param.getName();// (param.getOwnerClass() instanceof AdsReportClassDef) ? 

                if (stmts != null) {
                    //SingleNameReference paramRef = new ReenterableSingleNameReference(paramName.toCharArray());
                    Expression paramNullCheck = new EqualExpression(SqlClassVariables.nameRef(paramName), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
                    Expression paramEmptyCheck = BaseGenerator.createMessageSend(SqlClassVariables.nameRef(paramName), "isEmpty");
                    Expression conditionOnParam = new OR_OR_Expression(paramNullCheck, paramEmptyCheck, OperatorIds.OR_OR);

                    StringBuilder queryString = new StringBuilder();
                    queryString.append("(select ");
                    printNTimes(queryString, "NULL", propCount);
                    queryString.append(" from DUAL where 1=2)\");");

                    Expression actionOnTrue = BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(queryString.toString()));
                    List<Statement> loopStatements = new LinkedList<>();
                    loopStatements.add(new IfStatement(
                            new BinaryExpression(SqlClassVariables.loopVarRef(), BaseGenerator.createIntConstant(0), OperatorIds.GREATER),
                            BaseGenerator.createBlock(BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(", "))), 0, 0));
                    printPkProp(param, propId, entity, pk, propCount, loopStatements);
                    ForStatement loop = new ForStatement(
                            new Statement[]{
                        BaseGenerator.createDeclaration(SqlClassVariables.LOOP_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createIntConstant(0))
                    },
                            new BinaryExpression(SqlClassVariables.loopVarRef(), SqlClassVariables.sizeVarRef(), OperatorIds.LESS),
                            new Statement[]{
                        new PostfixExpression(SqlClassVariables.loopVarRef(), BaseGenerator.createIntConstant(1), OperatorIds.PLUS, 0)
                    },
                            BaseGenerator.createBlock(
                            loopStatements.toArray(new Statement[loopStatements.size()])), true, 0, 0);

                    Statement actionOnFalse = BaseGenerator.createBlock(
                            BaseGenerator.createDeclaration(SqlClassVariables.SIZE_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createMessageSend(SqlClassVariables.nameRef(paramName), "size"), ClassFileConstants.AccFinal),
                            BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral("(")),
                            loop,
                            BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(")")));

                    stmts.add(new IfStatement(conditionOnParam, actionOnTrue, actionOnFalse, 0, 0));

                    //  stmts.add(loop);
                } else {
                    //"get" + param.getId() + "()" : param.getName();
                    // ------------------- parameter is null or empty section ------------------------
                    cp.println("if (" + paramName + "==null || " + paramName + ".isEmpty())");
                    cp.print("\t" + SQL + ".append(\"");
                    cp.print("(select ");
                    printNTimes(cp, "NULL", propCount);
                    cp.println(" from DUAL where 1=2)\");");
                    // selected 1 or more
                    // ------------------- parameter is not null and not empty section ------------------------
                    cp.println(" else {");
                    cp.enterBlock();
                    cp.println("int $size=" + paramName + ".size();");
                    printSql("(");
                    cp.println("for (int $i=0; $i<$size; $i++){");
                    cp.enterBlock();
                    cp.println("if ($i>0)");
                    cp.enterBlock();
                    printSql(", ");
                    cp.leaveBlock();
                }
            } else {
                if (stmts != null) {
                    printPkProp(param, propId, entity, pk, propCount, stmts);
                }
            }

//          ========== COMMON (NON ARRAY PART) EXAMPLE ===========
//          $sql.append("(?, ?)");
            if (stmts == null) {
                printPkProp(param, propId, entity, pk, propCount, stmts);

                if (isArray) {
                    cp.leaveBlock();
                    cp.println("}");
                    printSql(")");
                    cp.leaveBlock();
                    cp.println("}");
                }
            }
        }
        return true;
    }

    private void translateLiteralParam(AdsDynamicPropertyDef param) {
        final String paramName = param.getName();
        if (purpose == EPurpose.TEST) {
            if (!isParamNullOrEmpty(param)) {
                cp.print(paramName);
            }
        } else {
            if (stmts != null) {
                stmts.add(BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME,
                        new ConditionalExpression(
                        new EqualExpression(
                        SqlClassVariables.nameRef(paramName),
                        new NullLiteral(0, 0), OperatorIds.NOT_EQUAL), SqlClassVariables.nameRef(paramName), BaseGenerator.createStringLiteral(""))));
            } else {
                cp.print(SQL + ".append(" + paramName + "!=null ? " + paramName + " : \"\");");
            }
        }
    }

    private boolean isParamNullOrEmpty(AdsDynamicPropertyDef param) {
        return false;//params.get(param).booleanValue();
    }

    private int getArrayParamSize(String name) {
        return 2;
    }

    private void translateValueParam(AdsDynamicPropertyDef param) {
        if (param.getValue().getType().getTypeId().isArrayType()) {
//          ========== EXAMPLE ===========
//          if (param==null || param.isEmpty()){
//              $sql.append("(select NULL from DIAL where 1=2)");
//          } else {
//              $sql.append("(");
//              for (int $i=0; $i<param.size(); $i++){
//                  if ($i>0)
//                      $sql.append(", ");
//                  #sql.append("?");
//              }
//              $sql.append(")");
//          }
            final String paramName = param.getName();//(param.getOwnerClass() instanceof AdsReportClassDef) ? 
            if (purpose == EPurpose.TEST) {
                if (isParamNullOrEmpty(param)) {
                    printSql("(select NULL from DUAL where 1=2)");
                } else {
                    printSql("(");
                    for (int i = 0; i < getArrayParamSize(paramName); i++) {
                        if (i > 0) {
                            printSql(",");
                        }
                        printParamValue(param, null, null, stmts);
                    }
                    printSql(")");
                }
            } else {
                if (stmts != null) {

                    Expression paramNullCheck = new EqualExpression(SqlClassVariables.nameRef(paramName), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
                    Expression paramEmptyCheck = BaseGenerator.createMessageSend(SqlClassVariables.nameRef(paramName), "isEmpty");
                    Expression conditionOnParam = new OR_OR_Expression(paramNullCheck, paramEmptyCheck, OperatorIds.OR_OR);
                    Expression actionOnTrue = BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral("(select NULL from dual where 1=2)"));


                    printSql("(");


                    List<Statement> loopStatements = new LinkedList<>();

                    loopStatements.add(new IfStatement(new BinaryExpression(SqlClassVariables.loopVarRef(), BaseGenerator.createIntConstant(0), OperatorIds.GREATER), BaseGenerator.createBlock(
                            BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(", "))), 0, 0));
                    printParamValue(param, null, null, loopStatements);

                    ForStatement loop = new ForStatement(
                            new Statement[]{
                        BaseGenerator.createDeclaration(SqlClassVariables.LOOP_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createIntConstant(0))
                    },
                            new BinaryExpression(SqlClassVariables.loopVarRef(), SqlClassVariables.sizeVarRef(), OperatorIds.LESS),
                            new Statement[]{
                        new PostfixExpression(SqlClassVariables.loopVarRef(), BaseGenerator.createIntConstant(1), OperatorIds.PLUS, 0)
                    },
                            BaseGenerator.createBlock(loopStatements.toArray(new Statement[loopStatements.size()])), true, 0, 0);
                    //stmts.add(loop);
                    Statement actionOnFalse = BaseGenerator.createBlock(
                            BaseGenerator.createDeclaration(SqlClassVariables.SIZE_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createMessageSend(SqlClassVariables.nameRef(paramName), "size"), ClassFileConstants.AccFinal),
                            loop);

                    stmts.add(new IfStatement(conditionOnParam, actionOnTrue, actionOnFalse, 0, 0));
                } else {

                    //"get" + param.getId() + "()" : param.getName();
                    cp.println("if (" + paramName + "==null || " + paramName + ".isEmpty()){");
                    cp.enterBlock();
                    printSql("(select NULL from DUAL where 1=2)");
                    cp.leaveBlock();
                    cp.println("} else {");
                    cp.enterBlock();
                    printSql("(");
                    cp.println("for (int $i=0; $i<" + paramName + ".size(); $i++){");
                    cp.enterBlock();
                    cp.println("if ($i>0)");
                    cp.enterBlock();
                    printSql(", ");
                    cp.leaveBlock();
                    printParamValue(param, null, null, stmts);
                    cp.leaveBlock();
                    cp.println("}");
                    printSql(")");
                    cp.leaveBlock();
                    cp.println("}");
                }
            }
        } else {
            printParamValue(param, null, null, stmts);
        }
    }

    @Override
    protected void processParam(AdsDynamicPropertyDef param, Id pkParamPropId, EParamDirection direction, boolean isLiteral) {
        if (isLiteral) {
            translateLiteralParam(param);
        } else if (param.getValue().getType().getTypeId() == EValType.PARENT_REF || param.getValue().getType().getTypeId() == EValType.ARR_REF) {
            translatePkParam(param, pkParamPropId);
        } else {
            translateValueParam(param);
        }
    }

    private void printSql(final String sql) {
        printSql(sql, stmts);
    }

    private void printSql(final String sql, List<Statement> stmts) {
        if (sql != null && !sql.isEmpty()) {
            if (purpose == EPurpose.TEST) {
                cp.print(sql);
            } else {
                if (stmts != null) {
                    stmts.add(BaseGenerator.createMessageSend(sqlRef(), SqlClassVariables.APPEND_METHOD_NAME, BaseGenerator.createStringLiteral(sql)));
                } else {
                    cp.print(SQL + ".append(");
                    cp.printStringLiteral(sql);
                    cp.println(");");
                }
            }
        }
    }
    private final SqmlTagTranslatorFactory tagTranslatorFactory = SqmlTagTranslatorFactory.Factory.newInstance(new IProblemHandler() {
        @Override
        public void accept(RadixProblem problem) {
            if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                throw new RadixError(problem.getMessage());


            }
        }
    });

    private class IfInfo {

        Scml.Tag tag;
        boolean inverted = false;

        public IfInfo(Scml.Tag tag) {
            this.tag = tag;
        }

        public boolean isTrue() {
            boolean curVal = getIfTagValue(tag);
            return inverted ? !curVal : curVal;
        }
    }
    private Stack<IfInfo> ifStack = null;
    private Map<Scml.Tag, Boolean> ifTagValues = null;

    private boolean getIfTagValue(Scml.Tag ifTag) {
        boolean result;
        if (ifTagValues.containsKey(ifTag)) {
            result = ifTagValues.get(ifTag);
        } else {
            ifTagValues.put(ifTag, Boolean.TRUE);
            result = true;
        }
        return result;
    }

    @Override
    protected void processTag(Scml.Tag tag) {
        if (purpose == EPurpose.TEST) {
            if (tag instanceof IfParamTag || tag instanceof TargetDbPreprocessorTag) {
                if (ifStack == null) {
                    ifStack = new Stack<>();
                }
                ifStack.push(new IfInfo(tag));
                return;
            }
            if (tag instanceof ElseIfTag) {
                if (ifStack.isEmpty()) {
                    throw new RadixError("No if for endif");
                } else {
                    ifStack.peek().inverted = true;
                }
                return;
            }
            if (tag instanceof EndIfTag) {
                if (ifStack.isEmpty()) {
                    throw new RadixError("No if for endif");
                }
                ifStack.pop();
                return;
            }
            if (ifStack != null && !ifStack.isEmpty()) {
                for (int i = ifStack.size() - 1; i >= 0; i--) {
                    if (!ifStack.get(i).isTrue()) {
                        return;
                    }
                }
            }
        }

        super.processTag(tag); //To change body of generated methods, choose Tools | Templates.        
    }

    @Override
    protected void processSqlTag(Scml.Tag tag) {
        final CodePrinter sql = CodePrinter.Factory.newSqlPrinter();
        final ITagTranslator<Scml.Tag> tagTranslator = tagTranslatorFactory.findTagTranslator(tag);
        if (tagTranslator != null) {
            try {
                tagTranslator.translate(tag, sql);
            } catch (RadixError cause) {
                sql.printError();
            }
        } else {
            sql.printError();
        }

        printSql(sql.toString());
    }

    @Override
    protected void processText(Text text) {
        if (purpose == EPurpose.TEST) {
            if (ifStack != null && !ifStack.isEmpty()) {
                for (int i = ifStack.size() - 1; i >= 0; i--) {
                    if (!ifStack.get(i).isTrue()) {
                        return;
                    }
                }
            }
        }
        final String sql = text.getText();
        printSql(sql);
    }

    private static String ppTagToString(IfParamTag tag) {
        StringBuilder sb = new StringBuilder();
        IParameterDef param = tag.findParameter();
        if (param != null && param.getDefinition() != null) {
            sb.append(param.getDefinition().getId().toString()).append("-");
        }
        if (tag.getOperator() != null) {
            sb.append(tag.getOperator().getValue());
        }
        sb.append("-");
        if (tag.getValue() != null) {
            sb.append(tag.getValue().toString());
        }
        sb.append(";");
        return sb.toString();
    }

    private static String ppInvertedTagToString(IfParamTag tag) {
        StringBuilder sb = new StringBuilder();
        IParameterDef param = tag.findParameter();
        if (param != null && param.getDefinition() != null) {
            sb.append(param.getDefinition().getId().toString()).append("-");
        }

        if (tag.getOperator() != null) {
            EIfParamTagOperator oper = tag.getOperator();
            switch (tag.getOperator()) {
                case EMPTY:
                    oper = EIfParamTagOperator.NOT_EMPTY;
                    break;
                case NOT_EMPTY:
                    oper = EIfParamTagOperator.EMPTY;
                    break;
                case EQUAL:
                    oper = EIfParamTagOperator.NOT_EQUAL;
                    break;
                case NOT_EQUAL:
                    oper = EIfParamTagOperator.EQUAL;
                    break;
                case NULL:
                    oper = EIfParamTagOperator.NOT_NULL;
                    break;
                case NOT_NULL:
                    oper = EIfParamTagOperator.NULL;
                    break;
            }
            sb.append(oper.getValue());
        }
        sb.append("-");
        if (tag.getValue() != null) {
            sb.append(tag.getValue().toString());
        }
        sb.append(";");
        return sb.toString();
    }

    private static String ppDbTagToString(TargetDbPreprocessorTag tag) {
        StringBuilder sb = new StringBuilder();
        if (tag.getDbTypeName() != null) {
            sb.append(tag.getDbTypeName()).append(";");
        }
        if (tag.getDbVersion() != null) {
            sb.append(tag.getDbVersion()).append(";");
        }
        if (tag.getDbOptions() != null) {
            for (DbOptionValue dep : tag.getDbOptions()) {
                sb.append(dep.getOptionName()).append("-");
                sb.append(dep.getMode()).append(";");
            }
        }

        return sb.toString();


    }

    public static class QueryInfo {

        public final String query;
        public final List<ParamInfo> params = new LinkedList<>();

        public QueryInfo(String query, List<ParamInfo> params) {
            this.query = query;
            if (params != null) {
                this.params.addAll(params);
            }
        }
    }

    public static List<QueryInfo> getVariants(Sqml sqml, int maxCount) {
        CodePrinter printer = CodePrinter.Factory.newSqlPrinter();
        AdsSqlClassSqlProcessor processor = Factory.newForTest(printer);

        List<Scml.Tag> ppTags = new LinkedList<>();

        List<String> tagTranslations = new LinkedList<>();

        for (Scml.Item item : sqml.getItems()) {
            if (item instanceof IfParamTag) {
                IfParamTag ifTag = (IfParamTag) item;
                ppTags.add(ifTag);
                String tagAsStr = ppTagToString(ifTag);
                String tagAsStrInverted = ppInvertedTagToString(ifTag);
                if (!tagTranslations.contains(tagAsStr) && !tagTranslations.contains(tagAsStrInverted)) {
                    tagTranslations.add(tagAsStr);
                }
            } else if (item instanceof TargetDbPreprocessorTag) {
                TargetDbPreprocessorTag dbtag = (TargetDbPreprocessorTag) item;
                ppTags.add(dbtag);
                String tagAsStr = ppDbTagToString(dbtag);
                if (!tagTranslations.contains(tagAsStr)) {
                    tagTranslations.add(tagAsStr);
                }
            }
        }

        boolean[] preprocessorStates = new boolean[tagTranslations.size()];

        Map<String, QueryInfo> results = new HashMap<>();
        BinaryTicker ppTicker;

        long totalVariantsCount = Math.round(Math.pow(2, preprocessorStates.length));
        if (totalVariantsCount > maxCount) {
            ppTicker = new BinaryTicker(preprocessorStates, maxCount);
        } else {
            ppTicker = new BinaryTicker(preprocessorStates, -1);
        }

        if (preprocessorStates.length > 0) {
            processor.ifTagValues = new HashMap<>();
        }
        do {

            for (Scml.Tag tag : ppTags) {
                String translation;
                String invertedTranslation = null;
                if (tag instanceof IfParamTag) {
                    translation = ppTagToString((IfParamTag) tag);
                    invertedTranslation = ppInvertedTagToString((IfParamTag) tag);;
                } else {
                    translation = ppDbTagToString((TargetDbPreprocessorTag) tag);
                }
                boolean originalFound = false;
                for (int i = 0; i < tagTranslations.size(); i++) {
                    if (translation.equals(tagTranslations.get(i))) {
                        processor.ifTagValues.put(tag, preprocessorStates[i]);
                        originalFound = true;
                        break;
                    }
                }
                if (!originalFound && invertedTranslation != null) {
                    for (int i = 0; i < tagTranslations.size(); i++) {
                        if (invertedTranslation.equals(tagTranslations.get(i))) {
                            processor.ifTagValues.put(tag, !preprocessorStates[i]);
                            break;
                        }
                    }
                }
            }


            processor.process(sqml);
            String code = printer.toString();

            printer.reset();
            results.put(code, new QueryInfo(code, processor.params));

            //} while (paramsTicker.tick());

        } while (ppTicker.tick());

        return new ArrayList<>(results.values());


    }

    private static class BinaryTicker {

        private class Item {

            int index;
            private Item next;

            public Item(int index) {
                this.index = index;
            }

            private void reset() {
                result[index] = false;
                if (next != null) {
                    next.reset();
                }
            }

            private boolean tick() {
                if (result.length == 0) {
                    return false;
                }
                if (!result[index]) {
                    if (next != null) {
                        if (next.tick()) {
                            return true;
                        } else {
                            result[index] = true;
                            next.reset();
                            return true;
                        }
                    } else {
                        result[index] = true;
                        return true;
                    }
                } else {
                    if (next == null) {
                        return false;
                    } else {
                        return next.tick();
                    }
                }
            }
        }
        private Item root;
        private int dept;
        private boolean[] result;
        private int limit;
        private Random random;

        public BinaryTicker(boolean[] result, int limit) {
            root = new Item(0);
            if (limit > 0) {
                this.limit = limit;
                this.random = new Random();
            } else {
                this.limit = -1;
                Item cur = root;
                for (int i = 1; i < result.length; i++) {
                    cur.next = new Item(i);
                    cur = cur.next;
                }
            }
            this.result = result;
        }

        public boolean tick() {
            if (this.limit > 0) {
                this.limit--;
                for (int i = 0; i < result.length; i++) {
                    result[i] = random.nextBoolean();
                }
                return true;
            } else if (this.limit == 0) {
                return false;
            } else {
                return root.tick();
            }
        }
    }
}