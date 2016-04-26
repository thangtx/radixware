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
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemFactory;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;

public class JMLSingleNameReference extends SingleNameReference implements IJMLExpression {

    private MessageSend substitution;
    private boolean wasResolved;
    private RadixObjectLocator locator;

    public JMLSingleNameReference(SingleNameReference ref, RadixObjectLocator locator) {
        super(ref.token, 0);
        this.sourceEnd = ref.sourceEnd;
        this.sourceStart = ref.sourceStart;
        this.locator = locator;
    }

    public String getReadableName() {
        if (locator != null) {
            RadixObjectLocator.RadixObjectData[] data = locator.take(sourceStart, sourceEnd);
            if (data != null && data.length > 0) {
                StringBuilder sb = new StringBuilder();

                for (RadixObjectLocator.RadixObjectData d : data) {
                    if (d.radixObject instanceof Jml.Tag) {
                        sb.append('`').append(((Jml.Tag) d.radixObject).getDisplayName()).append('`');
                    } else if (d.radixObject instanceof Scml.Text) {
                        String text = ((Scml.Text) d.radixObject).getText();
                        int startOffset = d.convertSrcPosToObjectOffset(sourceStart);
                        int endOffset = d.convertSrcPosToObjectOffset(sourceEnd);
                        if (startOffset > 0) {
                            text = text.substring(startOffset);
                        }

                        int len = endOffset - startOffset + 1;
                        if (len < text.length()) {
                            text = text.substring(0, len);
                        }

                        sb.append(text.trim());
                    }
                }

                if (sb.length() == 0) {
                    return null;
                } else {
                    return sb.toString();
                }
            }
        }
        return null;
    }

    public JMLSingleNameReference(char[] token, int ss, int se) {
        super(token, 0);
        this.sourceEnd = se;
        this.sourceStart = ss;
    }

    public JMLSingleNameReference(String token) {
        super(token.toCharArray(), 0);
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return substitution;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return this.resolvedType;
        }
        wasResolved = true;

        final ReferenceBinding receiver = scope.enclosingReceiverType();

        Binding binding = JmlStatementUtils.resolveTHIS(token);
        if (binding != null) {
            ((AdsProblemReporter) scope.problemReporter()).THISusage(this);
        } else {
            binding = scope.getBinding(token, Binding.VARIABLE | Binding.FIELD, this, true);
        }
        if (binding == null || !binding.isValidBinding()) {
            final MethodBinding[] getter = JMLPropertyReference.lookForPropAccessor(this, true, receiver, scope, token);
            if (getter != null) {
                final JMLMessageSend ms = new JMLMessageSend(getter);

                ms.nameSourcePosition = this.sourceStart;
                ms.nameSourcePosition <<= 32;
                ms.nameSourcePosition |= this.sourceEnd;
                ms.receiver = ThisReference.implicitThis();
                substitution = ms;
                substitution.sourceStart = this.sourceStart;
                substitution.sourceEnd = this.sourceEnd;
                return resolvedType = substitution.resolveType(scope);
            }
        }
        return super.resolveType(scope);
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
    public IJMLExpression.RadixObjectLocation getRadixObject(int index) {
        return null;
    }
}
