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

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.IsStrictlyAssigned;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.NeedReceiverGenericCast;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.MissingTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemFieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLPropertyReference extends FieldReference implements IJMLExpression {

    private Expression substitution;

    public JMLPropertyReference(char[] source, long pos) {
        super(source, pos);
    }

    public JMLPropertyReference(FieldReference src) {
        super(src.token, 0);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
        this.bits = src.bits;
        this.receiver = src.receiver;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (this.resolvedType != null) {
            return this.resolvedType;
        }
        if (this.receiver instanceof IJMLExpression) {
            this.receiver.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.receiver).getSubstitution(scope);
            if (subst != null) {
                this.receiver = subst;
            }
        }

        boolean receiverCast = false;
        if (this.receiver instanceof CastExpression) {
            this.receiver.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
            receiverCast = true;
        }
        this.actualReceiverType = this.receiver.resolveType(scope);
        if (this.actualReceiverType == null) {
            this.constant = Constant.NotAConstant;
            return null;
        }
        if (receiverCast) {
            // due to change of declaring class with receiver type, only identity cast should be notified
            if (((CastExpression) this.receiver).expression.resolvedType == this.actualReceiverType) {
                scope.problemReporter().unnecessaryCast((CastExpression) this.receiver);
            }
        }

        // the case receiverType.isArrayType and token = 'length' is handled by the scope API
        FieldBinding fieldBinding = this.binding = scope.getField(this.actualReceiverType, this.token, this);
        if (!fieldBinding.isValidBinding()) {
            this.constant = Constant.NotAConstant;
            if (this.receiver.resolvedType instanceof ProblemReferenceBinding) {
                // problem already got signaled on receiver, do not report secondary problem
                return null;
            }
            // https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
            // missing super type for anonymous classes ... 
            ReferenceBinding declaringClass = fieldBinding.declaringClass;
            boolean avoidSecondary = declaringClass != null
                    && declaringClass.isAnonymousType()
                    && declaringClass.superclass() instanceof MissingTypeBinding;
            if (!avoidSecondary) {
                scope.problemReporter().invalidField(this, this.actualReceiverType);
            }
            if (fieldBinding instanceof ProblemFieldBinding) {
                ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) fieldBinding;
                FieldBinding closestMatch = problemFieldBinding.closestMatch;
                switch (problemFieldBinding.problemId()) {
                    case ProblemReasons.InheritedNameHidesEnclosingName:
                    case ProblemReasons.NotVisible:
                    case ProblemReasons.NonStaticReferenceInConstructorInvocation:
                    case ProblemReasons.NonStaticReferenceInStaticContext:
                        if (closestMatch != null) {
                            fieldBinding = closestMatch;
                        }
                }
            }
            if (!fieldBinding.isValidBinding()) {
                return null;
            }
        }
        TypeBinding oldReceiverType = this.actualReceiverType;
        this.actualReceiverType = this.actualReceiverType.getErasureCompatibleType(fieldBinding.declaringClass);
        this.receiver.computeConversion(scope, this.actualReceiverType, this.actualReceiverType);
        if (this.actualReceiverType != oldReceiverType && this.receiver.postConversionType(scope) != this.actualReceiverType) { // record need for explicit cast at codegen since receiver could not handle it
            this.bits |= NeedReceiverGenericCast;
        }
        if (isFieldUseDeprecated(fieldBinding, scope, this.bits)) {
            scope.problemReporter().deprecatedField(fieldBinding, this);
        }
        boolean isImplicitThisRcv = this.receiver.isImplicitThis();
        this.constant = isImplicitThisRcv ? fieldBinding.constant() : Constant.NotAConstant;
        if (fieldBinding.isStatic()) {
            if (!(isImplicitThisRcv
                    || (this.receiver instanceof NameReference
                    && (((NameReference) this.receiver).bits & Binding.TYPE) != 0))) {
                scope.problemReporter().nonStaticAccessToStaticField(this, fieldBinding);
            }
            ReferenceBinding declaringClass = this.binding.declaringClass;
            if (!isImplicitThisRcv
                    && declaringClass != this.actualReceiverType
                    && declaringClass.canBeSeenBy(scope)) {
                scope.problemReporter().indirectAccessToStaticField(this, fieldBinding);
            }
            // check if accessing enum static field in initializer
            if (declaringClass.isEnum()) {
                MethodScope methodScope = scope.methodScope();
                SourceTypeBinding sourceType = scope.enclosingSourceType();
                if (this.constant == Constant.NotAConstant
                        && !methodScope.isStatic
                        && (sourceType == declaringClass || sourceType.superclass == declaringClass) // enum constant body
                        && methodScope.isInsideInitializerOrConstructor()) {
                    scope.problemReporter().enumStaticFieldUsedDuringInitialization(this.binding, this);
                }
            }
        }
        TypeBinding fieldType = fieldBinding.type;
        if (fieldType != null) {
            if ((this.bits & ASTNode.IsStrictlyAssigned) == 0) {
                fieldType = fieldType.capture(scope, this.sourceEnd);	// perform capture conversion if read access
            }
            this.resolvedType = fieldType;
            if ((fieldType.tagBits & TagBits.HasMissingType) != 0) {
                scope.problemReporter().invalidType(this, fieldType);
                return null;
            }
        }
        return fieldType;
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return substitution;
    }

    protected static char[] createSelector(char[] token) {
        char[] selector = new char[token.length + 3];
        selector[0] = 'g';
        selector[1] = 'e';
        selector[2] = 't';
        System.arraycopy(token, 0, selector, 3, token.length);
        return selector;
    }

    protected static MethodBinding[] lookForPropAccessor(InvocationSite is, boolean implicit, ReferenceBinding recvType, Scope scope, char[] token) {
        final char[] selector = createSelector(token);

        final MethodBinding getter =
                implicit
                ? scope.getImplicitMethod(selector, new TypeBinding[0], is)
                : recvType == null ? null : scope.findMethod(recvType, selector, new TypeBinding[0], is);

        if (getter == null) {
            return null;
        }
        final TypeBinding returnType;
        if (!getter.isValidBinding()) {
            final ProblemMethodBinding problemMethod = (ProblemMethodBinding) getter;
            if (problemMethod.closestMatch == null) {
                return null;
            }
            switch (problemMethod.problemId()) {
                case ProblemReasons.NotVisible:
                case ProblemReasons.NonStaticReferenceInConstructorInvocation:
                case ProblemReasons.NonStaticReferenceInStaticContext:
                case ProblemReasons.ReceiverTypeNotVisible:
                case ProblemReasons.ParameterBoundMismatch:
                    returnType = problemMethod.closestMatch.returnType;
                    break;
                default:
                    return null;
            }
        } else {
            returnType = getter.returnType;
        }
        selector[0] = 's';
        final MethodBinding setter = implicit
                ? scope.getImplicitMethod(selector, new TypeBinding[]{returnType}, is)
                : scope.findMethod(recvType, selector, new TypeBinding[]{returnType}, is);
        if (setter != null && setter.isValidBinding()) {
            return new MethodBinding[]{getter, setter};
        } else {
            return new MethodBinding[]{getter};
        }
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
