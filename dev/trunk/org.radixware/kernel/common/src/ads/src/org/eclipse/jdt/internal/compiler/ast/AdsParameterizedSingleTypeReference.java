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

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.IAdsScope;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class AdsParameterizedSingleTypeReference extends ParameterizedSingleTypeReference {

    public final Definition referenceContext;
    public final Definition referencedDef;
    public final AdsTypeDeclaration typeDeclaration;

    public AdsParameterizedSingleTypeReference(Definition referenceContext, ParameterizedSingleTypeReference src, TypeReference[] arguments, AdsTypeDeclaration token) {
        super(("`" + String.valueOf(token.getQualifiedName(referenceContext.getDefinition())) + "`").toCharArray(), arguments, src.dimensions, (long) src.sourceStart << 32 | src.sourceEnd);
        this.referenceContext = referenceContext;
        this.referencedDef = null;
        this.typeDeclaration = token;
    }

    public AdsParameterizedSingleTypeReference(Definition referenceContext, TypeReference[] arguments, int dimensions, AdsTypeDeclaration token) {
        super(("`" + String.valueOf(token.getQualifiedName(referenceContext.getDefinition())) + "`").toCharArray(), arguments, dimensions, 0);
        this.referenceContext = referenceContext;
        this.referencedDef = null;
        this.typeDeclaration = token;
    }

    public AdsParameterizedSingleTypeReference(Definition referenceContext, ParameterizedSingleTypeReference src, TypeReference[] arguments, Definition referencedDef) {
        super(("`" + String.valueOf(referencedDef.getQualifiedName(referenceContext.getDefinition())) + "`").toCharArray(), arguments, src.dimensions, (long) src.sourceStart << 32 | src.sourceEnd);
        this.referenceContext = referenceContext;
        this.referencedDef = referencedDef;
        this.typeDeclaration = null;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope, boolean checkBounds) {
        return resolveTypeImpl(scope, null, true);
    }

    @Override
    public TypeBinding resolveType(ClassScope scope) {
        return resolveTypeImpl(scope, null, true);
    }

    @Override
    public TypeBinding resolveTypeEnclosing(BlockScope scope, ReferenceBinding enclosingType) {
        return resolveTypeImpl(scope, enclosingType, true);
    }

    private TypeBinding resolveTypeImpl(Scope scope, ReferenceBinding enclosingType, boolean checkBounds) {
        // handle the error here
        this.constant = Constant.NotAConstant;
        if ((this.bits & ASTNode.DidResolve) != 0) { // is a shared type reference which was already resolved
            if (this.resolvedType != null) { // is a shared type reference which was already resolved
                if (this.resolvedType.isValidBinding()) {
                    return this.resolvedType;
                } else {
                    switch (this.resolvedType.problemId()) {
                        case ProblemReasons.NotFound:
                        case ProblemReasons.NotVisible:
                        case ProblemReasons.InheritedNameHidesEnclosingName:
                            TypeBinding type = this.resolvedType.closestMatch();
                            return type;
                        default:
                            return null;
                    }
                }
            }
        }
        this.bits |= ASTNode.DidResolve;
        TypeBinding type = resolveLeafTypeImpl(scope, enclosingType, checkBounds);
        // handle three different outcomes:
        if (type == null) {
            this.resolvedType = createArrayType(scope, this.resolvedType);
            return null;							// no useful type, but still captured dimensions into this.resolvedType
        } else {
            type = createArrayType(scope, type);
            if (!this.resolvedType.isValidBinding()) {
                return type;						// found some error, but could recover useful type (like closestMatch)
            } else {
                return this.resolvedType = type; 	// no complaint, keep fully resolved type (incl. dimensions)
            }
        }
    }

    protected IAdsScope findAdsScope(Scope scope) {
        Scope s = scope;
        while (s != null) {
            if (s instanceof IAdsScope) {
                return (IAdsScope) s;
            }
            s = s.parent;
        }
        return null;
    }

    private TypeBinding resolveLeafTypeImpl(Scope scope, ReferenceBinding enclosingType, boolean checkBounds) {
        ReferenceBinding currentType;
        if (enclosingType == null) {
            IAdsScope ads = findAdsScope(scope);

            this.resolvedType = typeDeclaration != null
                    ? ads.getType(referenceContext, typeDeclaration)
                    : ads.getType(referencedDef, false);

            if (this.resolvedType.isValidBinding()) {
                currentType = (ReferenceBinding) this.resolvedType;
            } else {
                reportInvalidType(scope);
                switch (this.resolvedType.problemId()) {
                    case ProblemReasons.NotFound:
                    case ProblemReasons.NotVisible:
                    case ProblemReasons.InheritedNameHidesEnclosingName:
                        TypeBinding type = this.resolvedType.closestMatch();
                        if (type instanceof ReferenceBinding) {
                            currentType = (ReferenceBinding) type;
                            break;
                        }
                    //$FALL-THROUGH$ - unable to complete type binding, but still resolve type arguments
                    default:
                        boolean isClassScope = scope.kind == Scope.CLASS_SCOPE;
                        int argLength = this.typeArguments.length;
                        for (int i = 0; i < argLength; i++) {
                            TypeReference typeArgument = this.typeArguments[i];
                            if (isClassScope) {
                                typeArgument.resolveType((ClassScope) scope);
                            } else {
                                typeArgument.resolveType((BlockScope) scope, checkBounds);
                            }
                        }
                        return null;
                }
                // be resilient, still attempt resolving arguments
            }
            enclosingType = currentType.enclosingType(); // if member type
            if (enclosingType != null) {
                enclosingType = currentType.isStatic()
                        ? (ReferenceBinding) scope.environment().convertToRawType(enclosingType, false /*do not force conversion of enclosing types*/)
                        : scope.environment().convertToParameterizedType(enclosingType);
                currentType = scope.environment().createParameterizedType((ReferenceBinding) currentType.erasure(), null /* no arg */, enclosingType);
            }
        } else { // resolving member type (relatively to enclosingType)
            this.resolvedType = currentType = scope.getMemberType(this.token, enclosingType);
            if (!this.resolvedType.isValidBinding()) {
                scope.problemReporter().invalidEnclosingType(this, currentType, enclosingType);
                return null;
            }
            if (isTypeUseDeprecated(currentType, scope)) {
                scope.problemReporter().deprecatedType(currentType, this);
            }
            ReferenceBinding currentEnclosing = currentType.enclosingType();
            if (currentEnclosing != null && currentEnclosing.erasure() != enclosingType.erasure()) {
                enclosingType = currentEnclosing; // inherited member type, leave it associated with its enclosing rather than subtype
            }
        }

        // check generic and arity
        boolean isClassScope = scope.kind == Scope.CLASS_SCOPE;
        TypeReference keep = null;
        if (isClassScope) {
            keep = ((ClassScope) scope).superTypeReference;
            ((ClassScope) scope).superTypeReference = null;
        }
        int argLength = this.typeArguments.length;
        TypeBinding[] argTypes = new TypeBinding[argLength];
        boolean argHasError = false;
        ReferenceBinding currentOriginal = (ReferenceBinding) currentType.original();
        for (int i = 0; i < argLength; i++) {
            TypeReference typeArgument = this.typeArguments[i];
            TypeBinding argType = isClassScope
                    ? typeArgument.resolveTypeArgument((ClassScope) scope, currentOriginal, i)
                    : typeArgument.resolveTypeArgument((BlockScope) scope, currentOriginal, i);
            if (argType == null) {
                argHasError = true;
            } else {
                argTypes[i] = argType;
            }
        }
        if (argHasError) {
            return null;
        }
        if (isClassScope) {
            ((ClassScope) scope).superTypeReference = keep;
            if (((ClassScope) scope).detectHierarchyCycle(currentOriginal, this)) {
                return null;
            }
        }

        final boolean isDiamond = (this.bits & ASTNode.IsDiamond) != 0;
        TypeVariableBinding[] typeVariables = currentOriginal.typeVariables();
        if (typeVariables == Binding.NO_TYPE_VARIABLES) { // non generic invoked with arguments
            boolean isCompliant15 = scope.compilerOptions().originalSourceLevel >= ClassFileConstants.JDK1_5;
            if ((currentOriginal.tagBits & TagBits.HasMissingType) == 0) {
                if (isCompliant15) { // below 1.5, already reported as syntax error
                    this.resolvedType = currentType;
                    scope.problemReporter().nonGenericTypeCannotBeParameterized(0, this, currentType, argTypes);
                    return null;
                }
            }
            // resilience do not rebuild a parameterized type unless compliance is allowing it
            if (!isCompliant15) {
                if (!this.resolvedType.isValidBinding()) {
                    return currentType;
                }
                return this.resolvedType = currentType;
            }
            // if missing generic type, and compliance >= 1.5, then will rebuild a parameterized binding
        } else if (argLength != typeVariables.length) {
            if (!isDiamond) { // check arity, IsDiamond never set for 1.6-
                scope.problemReporter().incorrectArityForParameterizedType(this, currentType, argTypes);
                return null;
            }
        } else if (!currentType.isStatic()) {
            ReferenceBinding actualEnclosing = currentType.enclosingType();
            if (actualEnclosing != null && actualEnclosing.isRawType()) {
                scope.problemReporter().rawMemberTypeCannotBeParameterized(
                        this, scope.environment().createRawType(currentOriginal, actualEnclosing), argTypes);
                return null;
            }
        }

        ParameterizedTypeBinding parameterizedType = scope.environment().createParameterizedType(currentOriginal, argTypes, enclosingType);
        // check argument type compatibility for non <> cases - <> case needs no bounds check, we will scream foul if needed during inference.
        if (!isDiamond) {
            if (checkBounds) // otherwise will do it in Scope.connectTypeVariables() or generic method resolution
            {
                parameterizedType.boundCheck(scope, this.typeArguments);
            } else {
                scope.deferBoundCheck(this);
            }
        }
        if (isTypeUseDeprecated(parameterizedType, scope)) {
            reportDeprecatedType(parameterizedType, scope);
        }

        if (!this.resolvedType.isValidBinding()) {
            return parameterizedType;
        }
        return this.resolvedType = parameterizedType;
    }

    private TypeBinding createArrayType(Scope scope, TypeBinding type) {
        if (this.dimensions > 0) {
            if (this.dimensions > 255) {
                scope.problemReporter().tooManyDimensions(this);
            }
            return scope.createArrayType(type, this.dimensions);
        }
        return type;
    }
}
