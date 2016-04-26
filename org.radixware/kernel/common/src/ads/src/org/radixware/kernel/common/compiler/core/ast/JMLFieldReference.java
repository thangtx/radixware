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

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class JMLFieldReference extends FieldReference implements IJMLExpression {

    private boolean wasResolved = false;
    private Expression substitution;

    public JMLFieldReference(FieldReference src) {
        super(src.token, 0);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
        this.nameSourcePosition = src.nameSourcePosition;
        this.receiver = src.receiver;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        if (this.receiver instanceof IJMLExpression) {
            this.receiver.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.receiver).getSubstitution(scope);
            if (subst != null) {
                this.receiver = subst;
            }
        }
        final TypeBinding recvType = this.receiver.resolveType(scope);
        final FieldBinding fieldBinding = recvType == null ? null : scope.getField(recvType, this.token, this);
        if (fieldBinding == null || !fieldBinding.isValidBinding() && recvType instanceof ReferenceBinding) {
            final MethodBinding[] getter = JMLPropertyReference.lookForPropAccessor(this, false, (ReferenceBinding) recvType, scope, this.token);
            if (getter != null && getter[0].isValidBinding()) {
                final JMLMessageSend ms = new JMLMessageSend(getter);
                ms.receiver = this.receiver;
                ms.sourceStart = this.sourceStart;
                ms.sourceEnd = this.sourceEnd;
                ms.nameSourcePosition = this.nameSourcePosition;
                this.substitution = ms;
                return resolvedType = this.substitution.resolveType(scope);
            }
        }
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return substitution;
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
