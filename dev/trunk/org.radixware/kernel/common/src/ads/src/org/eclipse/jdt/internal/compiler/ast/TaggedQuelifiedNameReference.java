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

package org.eclipse.jdt.internal.compiler.ast;

import java.util.Arrays;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;

import org.eclipse.jdt.internal.compiler.lookup.AdsDefinitionScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsLookupOnScope;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.ProblemFieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;


public class TaggedQuelifiedNameReference extends ReenterableQualifiedNameReference {

    public Jml.Tag[] referencedTags;
    public Definition referenceContext;

    public TaggedQuelifiedNameReference(QualifiedNameReference src, Definition referenceContext, Jml.Tag[] referencedTags) {
        super(Arrays.copyOf(src.tokens, src.tokens.length), src.sourcePositions, src.sourceStart, src.sourceEnd);
        this.referencedTags = referencedTags;
        this.referenceContext = referenceContext;
        this.bits = src.bits;
    }

    @Override
    protected TypeBinding resolveTypeImpl(BlockScope scope) {
        this.actualReceiverType = scope.enclosingReceiverType();
        this.constant = Constant.NotAConstant;

        if (referencedTags[0] instanceof JmlTagTypeDeclaration) {
            if ((this.binding = getBinding(scope)).isValidBinding()) {
                if (this.binding instanceof FieldBinding) {
                    return getOtherFieldBindings(scope);
                } else if (this.binding instanceof TypeBinding) {
                    return this.resolvedType = (TypeBinding) this.binding;
                } else {
                    return this.resolvedType = reportError(scope);
                }
            } else {
                return this.resolvedType = reportError(scope);
            }
        } else if (referencedTags[0] instanceof JmlTagInvocation) {
            this.binding = AdsLookupOnScope.getField(scope, this.referenceContext, this.actualReceiverType.capture(scope, (int) this.sourcePositions[0]), this.referencedTags[0], this);
            if (this.binding instanceof FieldBinding) {
                this.setFieldIndex(1);
                return getOtherFieldBindings(scope);
            } else if (this.binding instanceof TypeBinding) {
                return this.resolvedType = (TypeBinding) this.binding;
            } else {
                return this.resolvedType = reportError(scope);
            }
        } else {
            return super.resolveTypeImpl(scope);
        }
    }

    private Binding getBinding(BlockScope scope) {
        int index = 0;
        JmlTagTypeDeclaration typeDecl = (JmlTagTypeDeclaration) referencedTags[index];
        Binding binding = ((AdsDefinitionScope) scope.classScope()).getType(referenceContext, typeDecl.getType());
        Binding problemFieldBinding = null;
        ReferenceBinding referenceBinding = null;
        index++;
        if (binding instanceof ReferenceBinding) {
            while (index < tokens.length) {
                referenceBinding = (ReferenceBinding) binding;
                char[] nextName = tokens[index];
                this.setFieldIndex(index);
                this.setActualReceiverType(referenceBinding);
                if (referencedTags[index] == null) {

                    if ((binding = scope.findField(referenceBinding, nextName, this, true)) != null) {
                        if (binding.isValidBinding()) {
                            break; // binding is now a field
                        }
                        problemFieldBinding = new ProblemFieldBinding(
                                ((ProblemFieldBinding) binding).closestMatch,
                                ((ProblemFieldBinding) binding).declaringClass,
                                CharOperation.concatWith(CharOperation.subarray(tokens, 0, index), '.'),
                                binding.problemId());
                        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=317858 : If field is inaccessible,
                        // don't give up yet, continue to look for a visible member type 
                        if (binding.problemId() != ProblemReasons.NotVisible) {
                            return problemFieldBinding;
                        }
                    }
                    if ((binding = scope.findMemberType(nextName, referenceBinding)) == null) {
                        if (problemFieldBinding != null) {
                            return problemFieldBinding;
                        }

                        return new ProblemFieldBinding(
                                null,
                                referenceBinding,
                                nextName,
                                ProblemReasons.NotFound);
                    }
                } else if (this.referencedTags[index] instanceof JmlTagTypeDeclaration) {
                    if ((binding = AdsLookupOnScope.findMemberType(referenceContext, referenceBinding, ((JmlTagTypeDeclaration) this.referencedTags[index]).getType(), scope)) == null) {
                        if (problemFieldBinding != null) {
                            return problemFieldBinding;
                        }

                        return new ProblemFieldBinding(
                                null,
                                referenceBinding,
                                nextName,
                                ProblemReasons.NotFound);
                    }
                } else if (this.referencedTags[index] instanceof JmlTagInvocation) {
                    if ((binding = scope.findField(referenceBinding, nextName, this, true)) != null) {
                        if (binding.isValidBinding()) {
                            break; // binding is now a field
                        }
                        problemFieldBinding = new ProblemFieldBinding(
                                ((ProblemFieldBinding) binding).closestMatch,
                                ((ProblemFieldBinding) binding).declaringClass,
                                CharOperation.concatWith(CharOperation.subarray(tokens, 0, index), '.'),
                                binding.problemId());
                        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=317858 : If field is inaccessible,
                        // don't give up yet, continue to look for a visible member type 
                        if (binding.problemId() != ProblemReasons.NotVisible) {
                            return problemFieldBinding;
                        }
                    }

                }
                if (!binding.isValidBinding()) {
                    if (problemFieldBinding != null) {
                        return problemFieldBinding;
                    }
                    return new ProblemReferenceBinding(
                            CharOperation.subarray(tokens, 0, index),
                            (ReferenceBinding) ((ReferenceBinding) binding).closestMatch(),
                            binding.problemId());
                }
                if (this.isTypeUseDeprecated(referenceBinding, scope)) {
                    scope.problemReporter().deprecatedType(referenceBinding, this);
                }
                index++;
            }

        }
        return binding;
    }

    public TypeBinding getOtherFieldBindings(BlockScope scope) {
        // At this point restrictiveFlag may ONLY have two potential value : FIELD LOCAL (i.e cast <<(VariableBinding) binding>> is valid)
        int length = this.tokens.length;
        FieldBinding field = ((this.bits & Binding.FIELD) != 0) ? (FieldBinding) this.binding : null;
        TypeBinding type = ((VariableBinding) this.binding).type;
        int index = this.indexOfFirstFieldBinding;
        if (index == length) { //	restrictiveFlag == FIELD
            this.constant = ((FieldBinding) this.binding).constant();
            // perform capture conversion if read access
            return (type != null && (this.bits & ASTNode.IsStrictlyAssigned) == 0)
                    ? type.capture(scope, this.sourceEnd)
                    : type;
        }
        // allocation of the fieldBindings array	and its respective constants
        int otherBindingsLength = length - index;
        this.otherBindings = new FieldBinding[otherBindingsLength];
        this.otherDepths = new int[otherBindingsLength];

        // fill the first constant (the one of the binding)
        this.constant = ((VariableBinding) this.binding).constant();
        // save first depth, since will be updated by visibility checks of other bindings
        int firstDepth = (this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT;
        // iteration on each field
        while (index < length) {
            char[] token = this.tokens[index];
            if (type == null) {
                return null; // could not resolve type prior to this point
            }
            this.bits &= ~ASTNode.DepthMASK; // flush previous depth if any
            FieldBinding previousField = field;
            if (this.referencedTags[index] != null) {
                field = AdsLookupOnScope.getField(scope, this.referenceContext, type.capture(scope, (int) this.sourcePositions[index]), this.referencedTags[index], this);
            } else {
                field = scope.getField(type.capture(scope, (int) this.sourcePositions[index]), token, this);
            }
            int place = index - this.indexOfFirstFieldBinding;
            this.otherBindings[place] = field;
            this.otherDepths[place] = (this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT;
            if (field.isValidBinding()) {
                // set generic cast of for previous field (if any)
                if (previousField != null) {
                    TypeBinding fieldReceiverType = type;
                    TypeBinding oldReceiverType = fieldReceiverType;
                    fieldReceiverType = fieldReceiverType.getErasureCompatibleType(field.declaringClass);// handle indirect inheritance thru variable secondary bound
                    FieldBinding originalBinding = previousField.original();
                    if (fieldReceiverType != oldReceiverType || originalBinding.type.leafComponentType().isTypeVariable()) { // record need for explicit cast at codegen
                        setGenericCast(index - 1, originalBinding.type.genericCast(fieldReceiverType)); // type cannot be base-type even in boxing case
                    }
                }
                // only last field is actually a write access if any
                if (isFieldUseDeprecated(field, scope, index + 1 == length ? this.bits : 0)) {
                    scope.problemReporter().deprecatedField(field, this);
                }
                // constant propagation can only be performed as long as the previous one is a constant too.
                if (this.constant != Constant.NotAConstant) {
                    this.constant = field.constant();
                }

                if (field.isStatic()) {
                    if ((field.modifiers & ClassFileConstants.AccEnum) != 0) { // enum constants are checked even when qualified)
                        ReferenceBinding declaringClass = field.original().declaringClass;
                        MethodScope methodScope = scope.methodScope();
                        SourceTypeBinding sourceType = methodScope.enclosingSourceType();
                        if ((this.bits & ASTNode.IsStrictlyAssigned) == 0
                                && sourceType == declaringClass
                                && methodScope.lastVisibleFieldID >= 0
                                && field.id >= methodScope.lastVisibleFieldID
                                && (!field.isStatic() || methodScope.isStatic)) {
                            scope.problemReporter().forwardReference(this, index, field);
                        }
                        // check if accessing enum static field in initializer
                        if ((sourceType == declaringClass || sourceType.superclass == declaringClass) // enum constant body
                                && field.constant() == Constant.NotAConstant
                                && !methodScope.isStatic
                                && methodScope.isInsideInitializerOrConstructor()) {
                            scope.problemReporter().enumStaticFieldUsedDuringInitialization(field, this);
                        }
                    }
                    // static field accessed through receiver? legal but unoptimal (optional warning)
                    scope.problemReporter().nonStaticAccessToStaticField(this, field, index);
                    // indirect static reference ?
                    if (field.declaringClass != type) {
                        scope.problemReporter().indirectAccessToStaticField(this, field);
                    }
                }
                type = field.type;
                index++;
            } else {
                this.constant = Constant.NotAConstant; //don't fill other constants slots...
                scope.problemReporter().invalidField(this, field, index, type);
                setDepth(firstDepth);
                return null;
            }
        }
        setDepth(firstDepth);
        type = (this.otherBindings[otherBindingsLength - 1]).type;
        // perform capture conversion if read access
        return (type != null && (this.bits & ASTNode.IsStrictlyAssigned) == 0)
                ? type.capture(scope, this.sourceEnd)
                : type;
    }
}
