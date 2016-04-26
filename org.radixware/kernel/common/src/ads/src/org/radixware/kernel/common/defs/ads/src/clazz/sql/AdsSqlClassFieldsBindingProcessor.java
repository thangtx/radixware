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
import java.util.List;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;


public class AdsSqlClassFieldsBindingProcessor extends AdsSqlClassSourceProcessor {

    private final CodePrinter cp;
    private final String cursorVarName;
    private final List<Id> writtenFields = new ArrayList<>(12);
    private final List<Statement> stmts;

    public AdsSqlClassFieldsBindingProcessor(CodePrinter cp, String cursorVarName) {
        super(cp);
        this.cp = cp;
        this.stmts = null;
        this.cursorVarName = cursorVarName;
        cp.println("int $fieldCount=1;");
    }

    public AdsSqlClassFieldsBindingProcessor(List<Statement> stmts, String cursorVarName) {
        super(CodePrinter.Factory.newNullPrinter());
        this.cp = null;
        this.stmts = stmts;
        this.cursorVarName = cursorVarName;
        this.stmts.add(BaseGenerator.createDeclaration("$fieldCount", new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createIntConstant(1)));
    }

    @Override
    protected void processSqlTag(Scml.Tag tag) {
        if (tag instanceof PropSqlNameTag) {
            final PropSqlNameTag fiedRef = (PropSqlNameTag) tag;
            if (fiedRef.getOwnerType() == PropSqlNameTag.EOwnerType.THIS) {
                Id fieldId = fiedRef.getPropId();
                if (!writtenFields.contains(fieldId)) {
                    if (stmts != null) {
                        FieldReference ref = new FieldReference(getFieldIndexVarName(fieldId).toCharArray(), 0);
                        ref.receiver = "this".equals(cursorVarName) ? ThisReference.implicitThis() : new SingleNameReference(cursorVarName.toCharArray(), 0);
                        stmts.add(new Assignment(ref, SqlClassVariables.nameRef("$fieldCount"), 0));
                        stmts.add(new PostfixExpression(SqlClassVariables.nameRef("$fieldCount"), BaseGenerator.createIntConstant(1), OperatorIds.PLUS, 0));
                    } else {
                        cp.print(cursorVarName);
                        cp.print(".");
                        cp.print(getFieldIndexVarName(fieldId));
                        cp.println("= $fieldCount;");
                        cp.println("$fieldCount++;");
                    }
                    writtenFields.add(fieldId);
                } else {
                }
            }
        }
    }

    public static String getFieldIndexVarName(Id fieldId) {
        return "$field_" + fieldId + "_index";
    }

    @Override
    protected void processParam(AdsDynamicPropertyDef param, Id pkParamPropId, EParamDirection direction, boolean isLiteral) {
    }

    @Override
    protected void processText(Scml.Text text) {
    }
}