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
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.ReenterableSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.ARR_BIN;
import static org.radixware.kernel.common.enums.EValType.ARR_BLOB;
import static org.radixware.kernel.common.enums.EValType.ARR_BOOL;
import static org.radixware.kernel.common.enums.EValType.ARR_CHAR;
import static org.radixware.kernel.common.enums.EValType.ARR_CLOB;
import static org.radixware.kernel.common.enums.EValType.ARR_DATE_TIME;
import static org.radixware.kernel.common.enums.EValType.ARR_INT;
import static org.radixware.kernel.common.enums.EValType.ARR_NUM;
import static org.radixware.kernel.common.enums.EValType.ARR_STR;
import static org.radixware.kernel.common.enums.EValType.BIN;
import static org.radixware.kernel.common.enums.EValType.BLOB;
import static org.radixware.kernel.common.enums.EValType.BOOL;
import static org.radixware.kernel.common.enums.EValType.CHAR;
import static org.radixware.kernel.common.enums.EValType.CLOB;
import static org.radixware.kernel.common.enums.EValType.DATE_TIME;
import static org.radixware.kernel.common.enums.EValType.INT;
import static org.radixware.kernel.common.enums.EValType.NUM;
import static org.radixware.kernel.common.enums.EValType.STR;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.types.Id;

public class AdsSqlClassParamsBindingProcessor extends AdsSqlClassSourceProcessor {

    private final CodePrinter cp;
    private final List<Statement> stmts;

    public AdsSqlClassParamsBindingProcessor(CodePrinter cp) {
        super(cp);
        this.cp = cp;
        this.stmts = null;
        cp.println("int $paramCnt=1;");
    }

    public AdsSqlClassParamsBindingProcessor(List<Statement> stmts) {
        super(CodePrinter.Factory.newNullPrinter());
        this.cp = CodePrinter.Factory.newNullPrinter();
        this.stmts = stmts;
        this.stmts.add(BaseGenerator.createDeclaration(SqlClassVariables.PARAM_CNT_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createIntConstant(1)));
    }

    // print $statement.set...
    private void printInputParamSetter(final AdsDynamicPropertyDef param, final Id pkParamPropId) {
        final int propCount;
        final AdsPropertyDef prop;
        final DdsPrimaryKeyDef pk;
        final AdsEntityObjectClassDef entity;
        final AdsTypeDeclaration typeDeclaration = param.getValue().getType();
        final EValType valType = typeDeclaration.getTypeId();
        final boolean isArray = valType.isArrayType();

        if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
            entity = AdsParameterPropertyDef.findEntity(param);
            if (entity == null) {
                cp.printError();
                return;
            }

            if (pkParamPropId != null) {
                prop = entity.getProperties().findById(pkParamPropId, EScope.ALL).get();
                if (prop == null) {
                    cp.printError();
                    return;
                }
                propCount = 1;
                pk = null;
            } else {
                final DdsTableDef table = entity.findTable(param);
                if (table == null) {
                    cp.printError();
                    return;
                }
                prop = null;
                pk = table.getPrimaryKey();
                propCount = pk.getColumnsInfo().size();
            }
        } else {
            propCount = 1;
            prop = null;
            pk = null;
            entity = null;
        }

        if (stmts != null) {
            List<Statement> statements;
            Block mainBody = null;
            //SingleNameReference paramNameRef = new ReenterableSingleNameReference(param.getName().toCharArray());
            Expression getParamExpression;
            if (isArray) {
                mainBody = new Block(0);
                stmts.add(new IfStatement(new EqualExpression(new ReenterableSingleNameReference(param.getName().toCharArray()), new NullLiteral(0, 0), OperatorIds.NOT_EQUAL), BaseGenerator.createBlock(
                        BaseGenerator.createDeclaration(SqlClassVariables.SIZE_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createMessageSend(new ReenterableSingleNameReference(param.getName().toCharArray()), "size")),
                        new ForStatement(new Statement[]{
                            BaseGenerator.createDeclaration(SqlClassVariables.LOOP_VAR, new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createIntConstant(0)),},
                        new BinaryExpression(SqlClassVariables.loopVarRef(), SqlClassVariables.sizeVarRef(), OperatorIds.LESS),
                        new Statement[]{
                            new PostfixExpression(SqlClassVariables.loopVarRef(), BaseGenerator.createIntConstant(1), OperatorIds.PLUS, 0)
                        }, mainBody, true, 0, 0)), 0, 0));
                statements = new LinkedList<>();
                getParamExpression = BaseGenerator.createMessageSend(SqlClassVariables.nameRef(param.getName()), "get", SqlClassVariables.loopVarRef());
            } else {
                getParamExpression = new ReenterableSingleNameReference(param.getName().toCharArray());
                statements = stmts;
            }

            for (int i = 0; i < propCount; i++) {
                Expression nullCheck = new EqualExpression(getParamExpression, new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);

                final IEnumDef enumDef;
                final EValType propValType;

                if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
                    final AdsPropertyDef curProp;
                    if (prop != null) {
                        curProp = prop;
                    } else {
                        final Id columnId = pk.getColumnsInfo().get(i).getColumnId();
                        curProp = entity.getProperties().findById(columnId, EScope.ALL).get();
                    }
                    if (curProp != null) {
                        propValType = curProp.getValue().getType().getTypeId();
                        enumDef = AdsEnumUtils.findPropertyEnum(curProp);
                        getParamExpression = BaseGenerator.createMessageSend(getParamExpression, "get" + curProp.getId().toString());
                        nullCheck = new OR_OR_Expression(nullCheck, new EqualExpression(getParamExpression, new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL), OperatorIds.OR_OR);
                    } else {
                        propValType = null;
                        enumDef = null;
                    }
                } else {
                    enumDef = AdsEnumUtils.findPropertyEnum(param);
                    propValType = valType;
                }

                if (propValType != null) {
                    switch (propValType) {
                        case INT:
                        case ARR_INT:
                            Expression getVal = getParamExpression;
                            if (enumDef != null) {
                                getVal = BaseGenerator.createMessageSend(getParamExpression, "getValue");
                            }
                            getVal = BaseGenerator.createMessageSend(getVal, "longValue");
                            statements.add(new IfStatement(nullCheck,
                                    BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setNull", SqlClassVariables.paramCntVarRef(), createSQLTypeConstant("java.sql.Types.BIGINT")),
                                    BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setLong", SqlClassVariables.paramCntVarRef(), getVal),
                                    0, 0));

                            break;
                        case NUM:
                        case ARR_NUM:
                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setBigDecimal", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), getParamExpression)));
                            break;
                        case STR:
                        case ARR_STR:
                            if (enumDef != null) {
                                getVal = BaseGenerator.createMessageSend(getParamExpression, "getValue");
                            } else {
                                getVal = BaseGenerator.createMessageSend(new SingleNameReference("String".toCharArray(), 0), "valueOf", getParamExpression);
                            }
                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setString", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), getVal)));
                            break;
                        case CHAR:
                        case ARR_CHAR:
                            getVal = getParamExpression;
                            if (enumDef != null) {
                                getVal = BaseGenerator.createMessageSend(getVal, "getValue");
                            }
                            getVal = BaseGenerator.createMessageSend(new SingleNameReference("String".toCharArray(), 0), "valueOf", getVal);

                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setString", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), getVal)));
                            break;
                        case DATE_TIME:
                        case ARR_DATE_TIME:
                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setTimestamp", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), getParamExpression)));
                            break;
                        case CLOB:
                        case ARR_CLOB:
                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setClob", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), getParamExpression)));
                            break;
                        case BLOB:
                        case ARR_BLOB:
                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setBlob", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), getParamExpression)));
                            break;
                        case BIN:
                        case ARR_BIN:
                            statements.add(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setBytes", SqlClassVariables.paramCntVarRef(), new ConditionalExpression(nullCheck, new NullLiteral(0, 0), BaseGenerator.createMessageSend(getParamExpression, "getBytes"))));
                            break;
                        case BOOL:
                        case ARR_BOOL:
                            getVal = BaseGenerator.createMessageSend(getParamExpression, "booleanValue");
                            statements.add(new IfStatement(nullCheck,
                                    BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setNull", SqlClassVariables.paramCntVarRef(), createSQLTypeConstant("java.sql.Types.BIT")),
                                    BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "setBoolean", SqlClassVariables.paramCntVarRef(), getVal),
                                    0, 0));
                            break;
                        default:

                            break;
                    }
                }
                statements.add(new PostfixExpression(SqlClassVariables.paramCntVarRef(), BaseGenerator.createIntConstant(1), OperatorIds.PLUS, 0));
            }

            if (isArray) {
                mainBody.statements = statements.toArray(new Statement[statements.size()]);
            }
        } else {

            final String sParamNameSuffix;

            if (isArray) {
                String paramName = param.getName();//(param.getOwnerClass() instanceof AdsReportClassDef) ? 
                //"get" + param.getId() + "()" : param.getName();
                cp.print("if (" + paramName + "!=null");
                cp.println("){");
                cp.enterBlock(1);
                cp.println("int $size = " + paramName + ".size();");
                cp.println("for (int $i=0; $i<$size; $i++){");
                cp.enterBlock(1);
                sParamNameSuffix = ".get($i)";
            } else {
                sParamNameSuffix = "";
            }

            for (int i = 0; i < propCount; i++) {
                String paramName = param.getName();//(param.getOwnerClass() instanceof AdsReportClassDef) ? 
                //"get" + param.getId() + "()" : param.getName();
                paramName += sParamNameSuffix;
                String nullCheck = paramName + "==null";
                final IEnumDef enumDef;
                final EValType propValType;

                if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
                    final AdsPropertyDef curProp;
                    if (prop != null) {
                        curProp = prop;
                    } else {
                        final Id columnId = pk.getColumnsInfo().get(i).getColumnId();
                        curProp = entity.getProperties().findById(columnId, EScope.ALL).get();
                    }
                    if (curProp != null) {
                        propValType = curProp.getValue().getType().getTypeId();
                        enumDef = AdsEnumUtils.findPropertyEnum(curProp);
                        paramName += ".get" + curProp.getId().toString() + "()";
                        nullCheck += " || " + paramName + "==null";
                    } else {
                        propValType = null;
                        enumDef = null;
                    }
                } else {
                    enumDef = AdsEnumUtils.findPropertyEnum(param);
                    propValType = valType;
                }

                switch (propValType) {
                    case INT:
                    case ARR_INT:
                        cp.print("if (" + nullCheck + ") $statement.setNull($paramCnt, java.sql.Types.BIGINT); else $statement.setLong($paramCnt, ");
                        if (enumDef != null) {
                            cp.print(paramName + ".getValue().longValue()");
                        } else {
                            cp.print(paramName + ".longValue()");
                        }
                        cp.println(");");
                        break;
                    case NUM:
                    case ARR_NUM:
                        cp.println("$statement.setBigDecimal($paramCnt, " + nullCheck + " ? null : " + paramName + ");");
                        break;
                    case STR:
                    case ARR_STR:
                        cp.print("$statement.setString($paramCnt, " + nullCheck + " ? null : ");
                        if (enumDef != null) {
                            cp.print(paramName + ".getValue()");
                        } else {
                            cp.print("java.lang.String.valueOf(" + paramName + ")");
                        }
                        cp.println(");");
                        break;
                    case CHAR:
                    case ARR_CHAR:
                        cp.print("$statement.setString($paramCnt, " + nullCheck + " ? null : ");
                        if (enumDef != null) {
                            cp.print("java.lang.String.valueOf(" + paramName + ".getValue())");
                        } else {
                            cp.print("java.lang.String.valueOf(" + paramName + ")");
                        }
                        cp.println(");");
                        break;
                    case DATE_TIME:
                    case ARR_DATE_TIME:
                        cp.println("$statement.setTimestamp($paramCnt, " + nullCheck + " ? null : " + paramName + ");");
                        break;
                    case CLOB:
                    case ARR_CLOB:
                        cp.println("$statement.setClob($paramCnt, " + nullCheck + " ? null : " + paramName + ");");
                        break;
                    case BLOB:
                    case ARR_BLOB:
                        cp.println("$statement.setBlob($paramCnt, " + nullCheck + " ? null : " + paramName + ");");
                        break;
                    case BIN:
                    case ARR_BIN:
                        cp.println("$statement.setBytes($paramCnt, " + nullCheck + " ? null : " + paramName + ".getBytes());");
                        break;
                    case BOOL:
                    case ARR_BOOL:
                        cp.println("if(" + nullCheck + ") $statement.setNull($paramCnt, java.sql.Types.BIT); else $statement.setBoolean($paramCnt, " + paramName + ".booleanValue());");
                        break;
                    default:
                        cp.printError();
                        cp.println();
                        break;
                }

                cp.println("$paramCnt++;");
            }

            if (valType.isArrayType()) {
                cp.leaveBlock(1);
                cp.println("}");
                cp.leaveBlock(1);
                cp.println("}");
            }
        }
    }

    private FieldReference createSQLTypeConstant(String constant) {
        String[] names = constant.split("\\.");
        FieldReference ref = new FieldReference(names[names.length - 1].toCharArray(), 0);
        char[][] nc = new char[names.length - 1][];
        for (int i = 0; i < nc.length; i++) {
            nc[i] = names[i].toCharArray();
        }
        ref.receiver = BaseGenerator.createQualifiedName(nc);
        return ref;
    }

    private static String valType2JavaOutType(EValType valType) {
        switch (valType) {
            case INT:
                return "java.sql.Types.INTEGER";
            case NUM:
                return "java.sql.Types.DOUBLE";
            case CHAR:
            case STR:
                return "java.sql.Types.VARCHAR";
            case DATE_TIME:
                return "java.sql.Types.TIMESTAMP";
            case CLOB:
                return "java.sql.Types.CLOB";
            case BLOB:
                return "java.sql.Types.BLOB";
            case BIN:
                return "java.sql.Types.BINARY";
            case BOOL:
                return "java.sql.Types.BIT";
            default:
                return "???";
        }
    }

    @Override
    protected void processParam(AdsDynamicPropertyDef propertyAsParameter, Id pkParamPropId, EParamDirection direction, boolean isLiteral) {
        if (isLiteral) {
            return;
        }

        if (direction == EParamDirection.OUT || direction == EParamDirection.BOTH) {
            final EValType valType = propertyAsParameter.getValue().getType().getTypeId();
            final String javaOutType = valType2JavaOutType(valType);
            if (stmts != null) {
                stmts.add(BaseGenerator.createMessageSend(SqlClassVariables.nameRef("$statement"), "registerOutParameter", SqlClassVariables.paramCntVarRef(), createSQLTypeConstant(javaOutType)));
                stmts.add(BaseGenerator.createAssignment(SqlClassVariables.nameRef("$" + propertyAsParameter.getId() + "_Idx"), SqlClassVariables.paramCntVarRef()));
                if (direction == EParamDirection.OUT) {
                    stmts.add(new PostfixExpression(SqlClassVariables.paramCntVarRef(), BaseGenerator.createIntConstant(1), OperatorIds.PLUS, 0));
                }
            } else {

                cp.print("$statement.registerOutParameter($paramCnt, ");
                cp.print(javaOutType);
                cp.println(");");

                cp.println("$" + propertyAsParameter.getId() + "_Idx=$paramCnt;");

                if (direction == EParamDirection.OUT) {
                    cp.println("$paramCnt++;");
                }
            }
        }

        if (direction == EParamDirection.IN || direction == EParamDirection.BOTH) {
            printInputParamSetter(propertyAsParameter, pkParamPropId);
        }
    }

    @Override
    protected void processText(Text text) {
        // NOTHING
    }
}
