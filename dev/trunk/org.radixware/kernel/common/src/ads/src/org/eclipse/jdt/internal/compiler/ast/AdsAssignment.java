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

package org.eclipse.jdt.internal.compiler.ast;

import static org.eclipse.jdt.internal.compiler.ast.Expression.getDirectBinding;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class AdsAssignment extends Assignment {

    public AdsAssignment(Assignment src) {
        super(src.lhs, src.expression, src.sourceEnd);
        this.bits = src.bits;
        this.constant = src.constant;
        this.implicitConversion = src.implicitConversion;
        this.sourceStart = src.sourceStart;
        this.statementEnd = src.statementEnd;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        this.constant = Constant.NotAConstant;
        if (!(this.lhs instanceof Reference) || this.lhs.isThis()) {
            scope.problemReporter().expressionShouldBeAVariable(this.lhs);
            return null;
        }
        TypeBinding lhsType = this.lhs.resolveType(scope);
        this.expression.setExpectedType(lhsType); // needed in case of generic method invocation
        if (lhsType != null) {
            this.resolvedType = lhsType.capture(scope, this.sourceEnd);
        }
        LocalVariableBinding localVariableBinding = this.lhs.localVariableBinding();
        if (localVariableBinding != null) {
            localVariableBinding.tagBits &= ~TagBits.IsEffectivelyFinal;
        }
        TypeBinding rhsType = this.expression.resolveType(scope);
        if (lhsType == null || rhsType == null) {
            return null;
        }
        // check for assignment with no effect
        Binding left = getDirectBinding(this.lhs);
        if (left != null && !left.isVolatile() && left == getDirectBinding(this.expression)) {
            scope.problemReporter().assignmentHasNoEffect(this, left.shortReadableName());
        }

        // Compile-time conversion of base-types : implicit narrowing integer into byte/short/character
        // may require to widen the rhs expression at runtime
        if (lhsType != rhsType) { // must call before computeConversion() and typeMismatchError()
            scope.compilationUnitScope().recordTypeConversion(lhsType, rhsType);
        }
        if (this.expression.isConstantValueOfTypeAssignableToType(rhsType, lhsType)
                || rhsType.isCompatibleWith(lhsType, scope)) {
            this.expression.computeConversion(scope, lhsType, rhsType);
            checkAssignment(scope, lhsType, rhsType);
            if (this.expression instanceof CastExpression
                    && (this.expression.bits & ASTNode.UnnecessaryCast) == 0) {
                CastExpression.checkNeedForAssignedCast(scope, lhsType, (CastExpression) this.expression);
            }
            return this.resolvedType;
        } else if (isBoxingCompatible(rhsType, lhsType, this.expression, scope)) {
            this.expression.computeConversion(scope, lhsType, rhsType);
            if (this.expression instanceof CastExpression
                    && (this.expression.bits & ASTNode.UnnecessaryCast) == 0) {
                CastExpression.checkNeedForAssignedCast(scope, lhsType, (CastExpression) this.expression);
            }
            return this.resolvedType;
        } else if (((AdsCompilationUnitScope) scope.compilationUnitScope()).isRadixCompatible(rhsType, lhsType, expression, scope)) {
            this.expression = ((AdsCompilationUnitScope) scope.compilationUnitScope()).convertExpression(rhsType, lhsType, expression, scope);
            return this.resolvedType;
        }
        scope.problemReporter().typeMismatchError(rhsType, lhsType, this.expression, this.lhs);
        return lhsType;
    }

    @Override
    public void generateCode(BlockScope currentScope, CodeStream codeStream) {
        super.generateCode(currentScope, codeStream); //To change body of generated methods, choose Tools | Templates.
    }
}
