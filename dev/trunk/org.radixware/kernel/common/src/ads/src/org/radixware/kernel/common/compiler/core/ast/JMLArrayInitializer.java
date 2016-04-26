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

import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLArrayInitializer extends ArrayInitializer {

    private boolean wasResolved = false;

    public JMLArrayInitializer(final ArrayInitializer src) {
        this.expressions = src.expressions;
        this.sourceEnd = src.sourceEnd;
        this.sourceStart = src.sourceStart;
        this.statementEnd = src.statementEnd;
    }

    @Override
    public TypeBinding resolveTypeExpecting(BlockScope scope, TypeBinding expectedType) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;
        if (this.expressions != null && expectedType instanceof ArrayBinding) {
            final ArrayBinding targetType = (ArrayBinding) expectedType;
            if (this.expressions != null) {
                final TypeBinding elementType = targetType.elementsType();
                for (int i = 0, length = this.expressions.length; i < length; i++) {
                    final Expression expression = this.expressions[i];
                    expression.setExpectedType(elementType);
                    if (expression instanceof ArrayInitializer) {
                        expression.resolveTypeExpecting(scope, elementType);
                    } else {
                        expression.resolveType(scope);
                    }
                    if (expression instanceof IJMLExpression) {
                        final Expression substitution = ((IJMLExpression) expression).getSubstitution(scope);
                        if (substitution != null) {
                            expressions[i] = substitution;
                        }
                    }
                    this.expressions[i] = JMLMathOperations.computeConversionToReferenceNumeric(elementType, this.expressions[i], scope);
                }
            }
        }


        return super.resolveTypeExpecting(scope, expectedType);
    }

   
}
