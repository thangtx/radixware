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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;

final class JmlStatementUtils {

    private static final char[] THIS = new char[]{'T', 'H', 'I', 'S'};

    private JmlStatementUtils() {
    }

    public static int checkAssignmentAgainstNullAnnotation(BlockScope currentScope, FlowContext flowContext, VariableBinding var, int nullStatus, Expression expression, TypeBinding providedType) {
        if ((var.tagBits & TagBits.AnnotationNonNull) != 0
                && nullStatus != FlowInfo.NON_NULL) {
            flowContext.recordNullityMismatch(currentScope, expression, providedType, var.type, nullStatus);
            return FlowInfo.NON_NULL;
        } else if ((var.tagBits & TagBits.AnnotationNullable) != 0
                && nullStatus == FlowInfo.UNKNOWN) {	// provided a legacy type?
            return FlowInfo.POTENTIALLY_NULL;			// -> use more specific info from the annotation
        }
        return nullStatus;
    }

    public static Constant resolveCase(IJMLExpression expression, BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        final Constant result = expression.resolveCaseOriginal(scope, testType, switchStatement);
        final Expression substitution = expression.getSubstitution(scope);
        if (substitution != null) {
            for (int i = 0; i < switchStatement.statements.length; i++) {
                if (switchStatement.statements[i] == expression) {
                    switchStatement.statements[i] = substitution;
                }
            }
        }
        return result;
    }

    public static Binding resolveTHIS(char[] token) {
        if (CharOperation.equals(THIS, token)) {
            return null;
        }else{
            return null;
        }
    }
}
