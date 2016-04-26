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
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class JMLLocalDeclaration extends LocalDeclaration {

    private boolean wasResolved;

    public JMLLocalDeclaration(char[] name, int sourceStart, int sourceEnd) {
        super(name, sourceStart, sourceEnd);
    }

    public JMLLocalDeclaration(LocalDeclaration src) {
        super(src.name, src.sourceStart, src.sourceEnd);
        this.declarationEnd = src.declarationEnd;
        this.type = src.type;
        this.initialization = src.initialization;
        this.annotations = src.annotations;
        this.declarationSourceStart = src.declarationSourceStart;
        this.declarationSourceEnd = src.declarationSourceEnd;
        this.modifiers = src.modifiers;
        this.modifiersSourceStart = src.modifiersSourceStart;
        this.bits = src.bits;
    }

    @Override
    public void resolve(BlockScope scope) {
        if (wasResolved) {
            return;
        }
        wasResolved = true;

        final TypeBinding variableType = this.type.resolveType(scope);

        if (initialization instanceof IJMLExpression) {
            if (this.initialization instanceof ArrayInitializer) {
                initialization.resolveTypeExpecting(scope, variableType);
            } else {
                initialization.setExpectedType(variableType);
                initialization.resolveType(scope);
            }

            final Expression substitution = ((IJMLExpression) initialization).getSubstitution(scope);
            if (substitution != null) {
                this.initialization = substitution;
            }
        }

        this.initialization = JMLMathOperations.computeConversionToReferenceNumeric(variableType, this.initialization, scope);

        super.resolve(scope);

    }
}
