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

import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class JMLFieldDeclaration extends FieldDeclaration {

    public JMLFieldDeclaration() {

    }

    public JMLFieldDeclaration(FieldDeclaration src) {
        this.sourceEnd = src.sourceEnd;
        this.sourceStart = src.sourceStart;
        this.type = src.type;
        this.name = src.name;
        this.modifiers = src.modifiers;
        this.declarationSourceStart = src.declarationSourceStart;
        this.declarationSourceEnd = src.declarationSourceEnd;
        this.annotations = src.annotations;
        this.initialization = src.initialization;
        if (src.initialization instanceof AllocationExpression) {
            final AllocationExpression alloc = (AllocationExpression) src.initialization;
            if (alloc.enumConstant == src) {
                alloc.enumConstant = this;
            }
        }
    }

    @Override
    public void resolve(MethodScope initializationScope) {
        this.binding.tagBits &= ~TagBits.AnnotationResolved;
        if (this.initialization instanceof IJMLExpression) {
            FieldBinding previousField = initializationScope.initializedField;
            int previousFieldID = initializationScope.lastVisibleFieldID;
            initializationScope.initializedField = this.binding;
            initializationScope.lastVisibleFieldID = this.binding.id;
            this.initialization.resolveType(initializationScope);
            initializationScope.initializedField = previousField;
            initializationScope.lastVisibleFieldID = previousFieldID;
            final Expression subst = ((IJMLExpression) this.initialization).getSubstitution(initializationScope);
            if (subst != null) {
                this.initialization = subst;
            }
        }
        final TypeBinding variableType;
        if ((this.binding.modifiers & ClassFileConstants.AccEnum) != 0) {
            variableType = initializationScope.enclosingSourceType();
        } else {
            variableType = this.type.resolveType(initializationScope);
        }
        this.initialization = JMLMathOperations.computeConversionToReferenceNumeric(variableType, this.initialization, initializationScope);

        super.resolve(initializationScope); //To change body of generated methods, choose Tools | Templates.
    }
}
