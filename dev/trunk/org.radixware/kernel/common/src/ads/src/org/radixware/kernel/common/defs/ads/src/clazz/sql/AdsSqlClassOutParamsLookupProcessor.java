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
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
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


public class AdsSqlClassOutParamsLookupProcessor extends AdsSqlClassSourceProcessor {

    private final CodePrinter cp;
    private final List<Statement> stmts;

    public AdsSqlClassOutParamsLookupProcessor(CodePrinter cp) {
        super(cp);
        this.cp = cp;
        this.stmts = null;
    }

    public AdsSqlClassOutParamsLookupProcessor(List<Statement> stmts) {
        super(CodePrinter.Factory.newNullPrinter());
        this.cp = null;
        this.stmts = stmts;
    }

    private void printOutputParamGetter(AdsDynamicPropertyDef param) {
        final String varName = "$result." + param.getId();// + "_Val";
        final String paramIdxName = "$" + param.getId() + "_Idx";
        final AdsEnumDef enumDef = AdsEnumUtils.findPropertyEnum(param);
        if (stmts != null) {
            List<Statement> elseStatements = new LinkedList<>();
            if (enumDef != null) {
                final EValType enumValType = enumDef.getItemType();

                String typeName = "Object";
                switch (enumValType) {
                    case INT:
                        typeName = "long";
                        break;
                    case STR:
                        typeName = "String";
                        break;
                    case CHAR:
                        typeName = "Character";
                        break;
                }

                String selector = "get";
                // get simple value into temporary variable
                final String valueParamName = "$" + param.getId() + "_Val";

                switch (enumValType) {
                    case INT:
                        selector = "getLong";
                        break;
                    case STR:
                        selector = "getString";
                        break;
                    case CHAR:
                        selector = "getChar";
                        break;
                }

                elseStatements.add(BaseGenerator.createDeclaration(valueParamName, new SingleTypeReference(typeName.toCharArray(), 0), BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), selector, SqlClassVariables.nameRef(paramIdxName))));
                elseStatements.add(BaseGenerator.createAssignment(SqlClassVariables.nameRef(varName), BaseGenerator.createMessageSend(new TaggedSingleNameReference(param, enumDef, false), "getForValue", SqlClassVariables.nameRef(valueParamName))));
            } else {
                final EValType paramValType = param.getValue().getType().getTypeId();
                Expression init;
                switch (paramValType) {
                    case INT:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.nameRef("Long"), "valueOf", BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getLong", SqlClassVariables.nameRef(paramIdxName)));
                        break;
                    case NUM:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getBigDecimal", SqlClassVariables.nameRef(paramIdxName));
                        break;
                    case STR:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getString", SqlClassVariables.nameRef(paramIdxName));
                        break;
                    case DATE_TIME:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getTimestamp", SqlClassVariables.nameRef(paramIdxName));
                        break;
                    case BLOB:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getBlob", SqlClassVariables.nameRef(paramIdxName));
                        break;
                    case CLOB:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getClob", SqlClassVariables.nameRef(paramIdxName));
                        break;
                    case BOOL:
                        init = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getBoolean", SqlClassVariables.nameRef(paramIdxName));
                        break;
                    case BIN:
                        LocalDeclaration decl = new LocalDeclaration("bytes".toCharArray(), 0, 0);
                        decl.modifiers = ClassFileConstants.AccFinal;
                        decl.type = new ArrayTypeReference("byte".toCharArray(), 1, 0);
                        decl.initialization = BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "getBytes", SqlClassVariables.nameRef(paramIdxName));
                        elseStatements.add(decl);
                        init = new ConditionalExpression(new EqualExpression(SqlClassVariables.nameRef("bytes"), new NullLiteral(0, 0), OperatorIds.EQUAL), new NullLiteral(0, 0), BaseGenerator.createAlloc(new AdsTypeReference(param, AdsTypeDeclaration.Factory.newInstance(EValType.BIN)), SqlClassVariables.nameRef("bytes")));
                        break;
                    case CHAR:

                        decl = new LocalDeclaration("string".toCharArray(), 0, 0);
                        decl.modifiers = ClassFileConstants.AccFinal;
                        decl.type = new SingleTypeReference("String".toCharArray(), 0);
                        decl.initialization = BaseGenerator.createMessageSend(SqlClassVariables.nameRef("resultSet"), "getString", SqlClassVariables.nameRef(paramIdxName));
                        elseStatements.add(decl);
                        init = new ConditionalExpression(
                                new EqualExpression(SqlClassVariables.nameRef("string"), new NullLiteral(0, 0), OperatorIds.EQUAL),
                                new NullLiteral(0, 0),
                                BaseGenerator.createAlloc(new SingleTypeReference("Character".toCharArray(), 0), BaseGenerator.createMessageSend(SqlClassVariables.nameRef("string"), "charAt", BaseGenerator.createIntConstant(0))));
                        break;
                    default:
                        init = new NullLiteral(0, 0);
                        break;
                }
                elseStatements.add(new Assignment(SqlClassVariables.nameRef(varName), init, 0));
                elseStatements.add(new IfStatement(BaseGenerator.createMessageSend(SqlClassVariables.stmtVarRef(), "wasNull"),
                        new Assignment(SqlClassVariables.nameRef(varName), new NullLiteral(0, 0), 0), 0, 0));
            }
            stmts.add(new IfStatement(new EqualExpression(SqlClassVariables.nameRef(paramIdxName), BaseGenerator.createIntConstant(0), OperatorIds.EQUAL),
                    BaseGenerator.createAssignment(SqlClassVariables.nameRef(varName), new NullLiteral(0, 0)),
                    BaseGenerator.createBlock(elseStatements.toArray(new Statement[elseStatements.size()])),
                    0, 0));
        } else {
            cp.println("if (" + paramIdxName + "==0) {");
            cp.println("\t" + varName + "=null;");
            cp.println("} else {");
            cp.enterBlock();


            if (enumDef != null) {
                final EValType enumValType = enumDef.getItemType();
                switch (enumValType) {
                    case INT:
                        cp.print("long ");
                        break;
                    case STR:
                        cp.print("String ");
                        break;
                    case CHAR:
                        cp.print("Character ");
                        break;
                }

                // get simple value into temporary variable
                final String valueParamName = "$" + param.getId() + "_Val";
                cp.print(valueParamName + "=$statement.get");

                switch (enumValType) {
                    case INT:
                        cp.print("Long");
                        break;
                    case STR:
                        cp.print("String");
                        break;
                    case CHAR:
                        cp.print("Char");
                        break;
                }

                cp.println("(" + paramIdxName + ");");

                // convert to enum
                final AdsEnumType type = (AdsEnumType) enumDef.getType(enumDef.getItemType(), null);
                cp.print(varName + "=");
                type.getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_EXECUTABLE).writeUsage(cp);
                cp.println(".getForValue(" + valueParamName + ");");

            } else { // no enum
                final EValType paramValType = param.getValue().getType().getTypeId();
                switch (paramValType) {
                    case INT:
                        cp.println(varName + "=Long.valueOf($statement.getLong(" + paramIdxName + "));");
                        break;
                    case NUM:
                        cp.println(varName + "=$statement.getBigDecimal(" + paramIdxName + ");");
                        break;
                    case STR:
                        cp.println(varName + "=$statement.getString(" + paramIdxName + ");");
                        break;
                    case DATE_TIME:
                        cp.println(varName + "=$statement.getTimestamp(" + paramIdxName + ");");
                        break;
                    case BLOB:
                        cp.println(varName + "=$statement.getBlob(" + paramIdxName + ");");
                        break;
                    case CLOB:
                        cp.println(varName + "=$statement.getClob(" + paramIdxName + ");");
                        break;
                    case BOOL:
                        cp.println(varName + "=Boolean.valueOf($statement.getBoolean(" + paramIdxName + "));");
                        break;
                    case BIN:
                        cp.println("final byte[] bytes = $statement.getBytes(" + paramIdxName + ");");
                        cp.println(varName + "=(bytes==null ? null : new org.radixware.kernel.common.types.Bin(bytes));");
                        break;
                    case CHAR:
                        cp.println("final String string = resultSet.getString(" + paramIdxName + ");");
                        cp.println(varName + "=(string==null ? null : new Character(string.charAt(0));");
                        break;
                    default:
                        cp.println(varName + "=???");
                        break;
                }

                cp.println("if ($statement.wasNull()){"); // it is possible to ask was null only after get
                cp.println("\t" + varName + "=null;");
                cp.println("}");
            }

            cp.leaveBlock();
            cp.println('}');
        }
    }

    @Override
    protected void processParam(AdsDynamicPropertyDef param, Id pkParamPropId, EParamDirection direction, boolean isLiteral) {
        if (direction != EParamDirection.IN) {
            printOutputParamGetter(param);
        }
    }

    @Override
    protected void processText(Text text) {
        // NOTHING
    }
}