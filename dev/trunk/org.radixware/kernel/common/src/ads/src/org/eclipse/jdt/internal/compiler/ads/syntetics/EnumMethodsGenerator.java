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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import java.util.List;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class EnumMethodsGenerator extends BaseGenerator {

    public EnumMethodsGenerator(CompilationResult compilationResult, ReferenceContext referenceContext) {
        super(compilationResult, referenceContext);
    }

    public void createDefaultMethods(AdsEnumDef enumeration, List<AbstractMethodDeclaration> store) {
        store.add(createGetForValueMethod(enumeration));
    }

    private MethodDeclaration createGetForValueMethod(AdsEnumDef def) {
        MethodDeclaration decl = new MethodDeclaration(compilationResult);
        decl.selector = "getForValue".toCharArray();
        decl.returnType = new AdsTypeReference(def, def);
        decl.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccPublic | ClassFileConstants.AccFinal;
        decl.arguments = new Argument[]{
            new Argument("val".toCharArray(), 0, new AdsTypeReference(def, AdsTypeDeclaration.Factory.newInstance(def.getItemType())), 0)
        };
        LocalDeclaration loopVar = new LocalDeclaration("t".toCharArray(), 0, 0);
        loopVar.type = decl.returnType;
        ForeachStatement enumerator = new ForeachStatement(loopVar, 0);
        MessageSend collection = new MessageSend();
        collection.selector = "values".toCharArray();
        collection.receiver = new TaggedSingleNameReference(def, def, false);
        enumerator.collection = collection;

        MessageSend getValue = new MessageSend();
        getValue.selector = "getValue".toCharArray();
        getValue.receiver = new SingleNameReference("t".toCharArray(), 0);
        Expression getValueNullCheck = createNullCheck(getValue);
        Expression valNullCheck = createNotNullCheck("val".toCharArray());
        Expression valEqGetVal = createEqualsCall(getValue, new SingleNameReference("val".toCharArray(), 0));
        Expression and = new AND_AND_Expression(getValueNullCheck, valNullCheck, OperatorIds.AND_AND);
        Expression condition = new OR_OR_Expression(and, valEqGetVal, OperatorIds.OR_OR);


        enumerator.action = new IfStatement(condition, new ReturnStatement(new SingleNameReference("t".toCharArray(), 0), 0, 0), 0, 0);


        decl.statements = new Statement[]{
            enumerator,
            createThrow(NOCONSTITEMVITHSUCHVALUEERROR_TYPE_NAME,
            new BinaryExpression(
            new StringLiteral(("Enumeration " + def.getQualifiedName() + " has no item with value: ").toCharArray(), 0, 0, 0),
            createStringValueOf("val".toCharArray()), OperatorIds.PLUS), false)};


        return decl;
    }
}
