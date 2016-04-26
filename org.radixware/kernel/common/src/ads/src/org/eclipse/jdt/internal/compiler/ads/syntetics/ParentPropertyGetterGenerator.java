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

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


public class ParentPropertyGetterGenerator extends PersistentPropertyGetterGenerator<AdsParentPropertyDef> {

    public ParentPropertyGetterGenerator(Definition referenceContext, CompilationResult result, ReferenceContext context) {
        super(referenceContext, result, context);
    }

    private Expression createParentCalculation(AdsParentPropertyDef property, boolean write, List<Statement> stmts, AdsProblemReporter reporter) {
        AdsParentPropertyDef.ParentPath parentPath = property.getParentInfo().getParentPath();
        AdsClassDef propOwner = property.getOwnerClass();

        Expression recv = ThisReference.implicitThis();


        int counter = 0;

        for (Id id : parentPath.getRefPropIds()) {
            AdsPropertyDef ref = propOwner.getProperties().findById(id, ExtendableDefinitions.EScope.ALL).get();
            if (ref == null) {
                return null;
            }
            //if (ref.getValue().getType().getTypeId() == EValType.PARENT_REF) {
                AdsType type = ref.getValue().getType().resolve(contextDefinition).get();
                if (type instanceof AdsClassType.EntityObjectType) {
                    propOwner = ((AdsClassType.EntityObjectType) type).getSource();
                    if (propOwner == null) {
                        reporter.unresolveableParentPropertyParentPath(property, compilationResult, referenceContext);
                        return null;
                    }
                    AdsTypeReference reference = new AdsTypeReference(contextDefinition, propOwner);
                    String newVarName = "p" + String.valueOf(counter);
                    LocalDeclaration varDecl = new LocalDeclaration(newVarName.toCharArray(), 0, 0);
                    stmts.add(varDecl);
                    varDecl.type = reference;

                    FieldReference fieldRef = new FieldReference(id.toCharArray(), 0);
                    fieldRef.receiver = recv;
                    varDecl.initialization = fieldRef;
                    IfStatement condition = new IfStatement(createNullCheck(varDecl.name), new ReturnStatement(write ? new NullLiteral(0, 0) : null, 0, 0), 0, 0);
                    stmts.add(condition);
                    recv = new SingleNameReference(varDecl.name, 0);
                    counter++;
                } else {
                    return null;
                }
//            } else {
//                return null;
//            }
        }
        return recv;
    }

    @Override
    protected Statement[] createGetterBody(AdsParentPropertyDef property, AdsPropertyDeclaration propDecl, AdsProblemReporter reporter) {
        TryStatement root = new TryStatement();


        List<Statement> stmts = new LinkedList<>();
        Expression recv = createParentCalculation(property, true, stmts, reporter);
        if (recv == null) {
            return null;
        }
        //write parent calcilation
        FieldReference originalRef = new FieldReference(property.getParentInfo().getOriginalPropertyId().toCharArray(), 0);
        originalRef.receiver = recv;
        stmts.add(new ReturnStatement(new ConditionalExpression(createNullCheck(recv), new NullLiteral(0, 0), originalRef), 0, 0));
        root.tryBlock = createBlock(stmts.toArray(new Statement[stmts.size()]));
        root.catchArguments = new Argument[]{
            new Argument("ex".toCharArray(), 0, new QualifiedTypeReference(ENTITYOBJECTNOTEXISTERROR_TYPE_NAME, new long[ENTITYOBJECTNOTEXISTERROR_TYPE_NAME.length]), 0)
        };
        root.catchBlocks = new Block[]{
            createBlock(new ReturnStatement(new NullLiteral(0, 0), 0, 0))
        };
        return new Statement[]{root};
    }
}
