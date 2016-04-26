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

import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLArgument extends Argument implements IJMLExpression {

    private boolean wasResolved;

    public JMLArgument(Argument src) {
        super(src.name, 0, src.type, src.modifiers);
        this.declarationEnd = src.declarationEnd;
        this.type = src.type;
        this.initialization = src.initialization;
        this.annotations = src.annotations;
        this.modifiers = src.modifiers;
        this.modifiersSourceStart = src.modifiersSourceStart;
        this.bits = src.bits;
    }

    public JMLArgument(TypeReference type, String name) {
        super(name.toCharArray(), 0, type, 0);
    }
    public JMLArgument(TypeReference type, char[] name) {
        super(name, 0, type, 0);
    }


    @Override
    public void resolve(BlockScope scope) {
        if (wasResolved) {
            return;
        }
        wasResolved = true;
        if (initialization instanceof IJMLExpression) {
            initialization.resolveType(scope);
            final Expression substitution = ((IJMLExpression) initialization).getSubstitution(scope);
            if (substitution != null) {
                this.initialization = substitution;
            }
        }
        super.resolve(scope); //To change body of generated methods, choose Tools | Templates.
        this.initialization = JMLMathOperations.computeConversionToReferenceNumeric(this.type.resolvedType, this.initialization, scope);

    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return null;
    }

    @Override
    public Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        return JmlStatementUtils.resolveCase(this, scope, testType, switchStatement);
    }

    @Override
    public Constant resolveCaseOriginal(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        return super.resolveCase(scope, testType, switchStatement);
    }

    @Override
    public RadixObjectLocation getRadixObject(int index) {
        return null;
    }
}
