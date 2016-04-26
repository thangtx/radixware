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
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemFactory;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;

public class JMLQualifiedNameReference extends QualifiedNameReference implements IJMLExpression {

    private Expression substitution;
    private boolean wasResolved;
    private RadixObjectLocator locator;

    public JMLQualifiedNameReference(QualifiedNameReference src, RadixObjectLocator locator) {
        super(src.tokens, null, src.sourceStart, src.sourceEnd);
        this.sourcePositions = src.sourcePositions;
        this.bits = src.bits;
        this.locator = locator;
    }

    public String getReadableName() {
        if (locator != null) {
            int end = sourceEnd;
            if (indexOfFirstFieldBinding >= 1) {
                end = (int) sourcePositions[indexOfFirstFieldBinding - 1];
            }
            RadixObjectLocator.RadixObjectData[] data = locator.take(sourceStart, end);
            if (data != null && data.length > 0) {
                StringBuilder sb = new StringBuilder();

                for (RadixObjectLocator.RadixObjectData d : data) {
                    if (d.radixObject instanceof Jml.Tag) {
                        sb.append('`').append(((Jml.Tag) d.radixObject).getDisplayName()).append('`');
                    } else if (d.radixObject instanceof Scml.Text) {
                        String text = ((Scml.Text) d.radixObject).getText();

                        int startOffset = d.convertSrcPosToObjectOffset(sourceStart);
                        int endOffset = d.convertSrcPosToObjectOffset(end);
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

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return this.resolvedType;
        }
        wasResolved = true;
        //resolve name references. if no substitutions are detected than continue in normal way
        final ReferenceBinding receiver = scope.enclosingReceiverType();
        //look for first binding

        int mask = Binding.VARIABLE | Binding.FIELD | Binding.TYPE | Binding.PACKAGE;
//        if (this.partialReferences != null && this.partialReferences[0] != null) {
//            if (this.partialReferences[0] instanceof JmlTagInvocation) {
//                mask = Binding.FIELD;
//            } else if (this.partialReferences[0] instanceof JmlTagTypeDeclaration) {
//                mask = Binding.TYPE | Binding.PACKAGE;
//            }
//        }
        Binding binding = JmlStatementUtils.resolveTHIS(tokens[0]);
        if (binding != null) {            
            ((AdsProblemReporter) scope.problemReporter()).THISusage(this);
        } else {
            binding = scope.getBinding(tokens[0], mask, this, true);
        }

        Expression subst = null;
        int currentIndex = 0;
        if (binding == null || !binding.isValidBinding()) {
            final MethodBinding[] getter = JMLPropertyReference.lookForPropAccessor(this, true, receiver, scope, tokens[0]);
            if (getter != null) {
                final JMLMessageSend ms = new JMLMessageSend(getter);
                ms.receiver = ThisReference.implicitThis();
                ms.nameSourcePosition = this.sourcePositions[currentIndex];
                ms.sourceStart = ((int) (this.sourcePositions[currentIndex] >>> 32));
                ms.sourceEnd = (int) (this.sourcePositions[currentIndex] & 0x00000000FFFFFFFFL);
                subst = ms;
                binding = getter[0];
            }
        } else {
            while (binding instanceof PackageBinding && currentIndex < tokens.length - 1) {
                currentIndex++;
                final Binding nextPackage = ((PackageBinding) binding).getTypeOrPackage(tokens[currentIndex]);
                if (nextPackage == null || !nextPackage.isValidBinding()) {
                    if (nextPackage instanceof ProblemReferenceBinding) {
                        this.binding = nextPackage;
                        return this.resolvedType = reportError(scope);
                    }
                    this.binding = new ProblemReferenceBinding(this.tokens, null, ProblemReasons.NotFound);

                    return this.resolvedType = reportError(scope);
                }

                binding = nextPackage;
            }
        }
        if (binding.isValidBinding()) {
            TypeBinding actualType;
            currentIndex++;
            while (currentIndex < this.tokens.length) {
                if (binding instanceof VariableBinding) {
                    actualType = ((VariableBinding) binding).type;
                } else if (binding instanceof TypeBinding) {
                    actualType = (TypeBinding) binding;
                } else if (binding instanceof MethodBinding) {
                    actualType = ((MethodBinding) binding).returnType;
                } else {
                    break;
                }
                if (actualType instanceof ReferenceBinding || actualType instanceof ArrayBinding) {

                    ReferenceBinding reference = null;
                    if (actualType instanceof ReferenceBinding) {
                        reference = (ReferenceBinding) actualType;
                    }
                    Binding nextBinding = scope.findField(actualType, tokens[currentIndex], this, true);//reference.getField(tokens[currentIndex], true);
                    if (nextBinding == null && reference != null && (binding.kind() & Binding.TYPE) != 0) {
                        nextBinding = scope.findMemberType(tokens[currentIndex], reference);
                    }
                    binding = nextBinding;
                    if (binding == null || !binding.isValidBinding() && reference != null) {
                        //TODO: look for method                        
                        final MethodBinding[] getter = JMLPropertyReference.lookForPropAccessor(this, false, reference, scope, tokens[currentIndex]);
                        if (getter != null) {
                            final JMLMessageSend ms = new JMLMessageSend(getter);
                            ms.nameSourcePosition = this.sourcePositions[currentIndex];
                            ms.sourceStart = ((int) (this.sourcePositions[currentIndex] >>> 32));
                            ms.sourceEnd = (int) (this.sourcePositions[currentIndex] & 0x00000000FFFFFFFFL);
                            if (subst != null) {
                                ms.receiver = subst;
                            } else {
                                if (currentIndex == 1) {
                                    ms.receiver = new JMLSingleNameReference(new SingleNameReference(tokens[0], 0), null);
                                    ms.nameSourcePosition = this.sourcePositions[0];
                                    ms.receiver.sourceStart = ((int) (this.sourcePositions[0] >>> 32));
                                    ms.receiver.sourceEnd = (int) (this.sourcePositions[0] & 0x00000000FFFFFFFFL);
                                } else {
                                    final char[][] tokens = new char[currentIndex][];
                                    for (int i = 0; i < currentIndex; i++) {
                                        tokens[i] = this.tokens[i];
                                    }
                                    final long[] positons = new long[tokens.length];
                                    for (int i = 0; i < currentIndex; i++) {
                                        positons[i] = this.sourcePositions[i];
                                    }

                                    int sourceStart = ((int) (this.sourcePositions[currentIndex] >>> 32));
                                    int sourceEnd = (int) (this.sourcePositions[currentIndex] & 0x00000000FFFFFFFFL);
                                    ms.receiver = new JMLQualifiedNameReference(new QualifiedNameReference(tokens, positons, sourceStart, sourceEnd), null);
                                    ms.receiver.sourceStart = ((int) (this.sourcePositions[0] >>> 32));
                                    ms.receiver.sourceEnd = (int) (this.sourcePositions[currentIndex - 1] & 0x00000000FFFFFFFFL);
                                }
                            }
                            subst = ms;
                            binding = getter[0];
                        } else {
                            if (subst != null) {
                                final FieldReference field = new FieldReference(tokens[currentIndex], 0);
                                field.nameSourcePosition = this.sourcePositions[currentIndex];
                                field.sourceStart = ((int) (this.sourcePositions[currentIndex] >>> 32));
                                field.sourceEnd = (int) (this.sourcePositions[currentIndex] & 0x00000000FFFFFFFFL);
                                field.receiver = subst;
                                subst = field;
                            }
                        }
                    } else {
                        if (subst != null) {
                            final FieldReference field = new FieldReference(tokens[currentIndex], 0);
                            field.nameSourcePosition = this.sourcePositions[currentIndex];
                            field.sourceStart = ((int) (this.sourcePositions[currentIndex] >>> 32));
                            field.sourceEnd = (int) (this.sourcePositions[currentIndex] & 0x00000000FFFFFFFFL);
                            field.receiver = subst;
                            subst = field;
                        }
                    }
                }
                currentIndex++;
            }
        }
        if (subst != null) {
            substitution = subst;
            substitution.sourceStart = sourceStart;
            substitution.sourceEnd = sourceEnd;
            return this.resolvedType = substitution.resolveType(scope);
        }

        return super.resolveType(scope);
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
    public IJMLExpression.RadixObjectLocation getRadixObject(int index) {

        return null;
    }
}
