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

import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.lookup.AdsProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLQualifiedTypeReference extends QualifiedTypeReference {

    public JMLQualifiedTypeReference(QualifiedTypeReference src) {
        super(src.tokens, src.sourcePositions);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope, boolean checkBounds) {
        return super.resolveType(scope, checkBounds);
    }

    @Override
    public TypeBinding resolveType(ClassScope scope) {
        return super.resolveType(scope);
    }

    @Override
    protected TypeBinding getTypeBinding(Scope scope) {
        if (this.resolvedType != null) {
            return this.resolvedType;
        }
        final Binding binding = scope.getPackage(this.tokens);
        if (binding != null && !binding.isValidBinding()) {
            if (binding instanceof AdsProblemReferenceBinding) {
                return (AdsProblemReferenceBinding) binding;
            } else if (binding instanceof ProblemReferenceBinding && ((ProblemReferenceBinding) binding).closestMatch() instanceof AdsProblemReferenceBinding) {
                return (AdsProblemReferenceBinding) ((ProblemReferenceBinding) binding).closestMatch();
            } else {
             //   System.out.println("");
            }
        }

        return super.getTypeBinding(scope);
    }
}
