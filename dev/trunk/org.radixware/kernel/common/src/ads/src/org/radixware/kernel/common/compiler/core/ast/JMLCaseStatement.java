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

package org.radixware.kernel.common.compiler.core.ast;

import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLCaseStatement extends CaseStatement {

    public JMLCaseStatement(CaseStatement src) {
        super(src.constantExpression, src.sourceEnd, src.sourceStart);
    }

    @Override
    public Constant resolveCase(BlockScope scope, TypeBinding switchExpressionType, SwitchStatement switchStatement) {
        if (this.constantExpression instanceof IJMLExpression) {
            if (switchExpressionType != null && switchExpressionType.isEnum() && (this.constantExpression instanceof SingleNameReference)) {
                ((SingleNameReference) this.constantExpression).setActualReceiverType((ReferenceBinding) switchExpressionType);
            }
            this.constantExpression.resolveType(scope);
            final Expression substitution = ((IJMLExpression) this.constantExpression).getSubstitution(scope);
            if (substitution != null) {
                this.constantExpression = substitution;
            } else {

                if (switchExpressionType != null && switchExpressionType.isEnum() && this.constantExpression instanceof QualifiedNameReference) {
                    QualifiedNameReference ref = (QualifiedNameReference) this.constantExpression;
                    if (ref.binding instanceof FieldBinding && (((FieldBinding) ref.binding).modifiers & ClassFileConstants.AccEnum) != 0) {
                        this.constantExpression = new JMLSingleNameReference(((FieldBinding) ref.binding).name, this.constantExpression.sourceStart, this.constantExpression.sourceEnd);
                    }
                }
            }
        }
        return super.resolveCase(scope, switchExpressionType, switchStatement); //To change body of generated methods, choose Tools | Templates.
    }
}
