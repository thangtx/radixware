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
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.checkInvocationArguments;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsDefinitionScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsSourceTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.ImplicitNullAnnotationVerifier;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.RawTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;


public class JMLQualifiedAllocationExpression extends QualifiedAllocationExpression implements IJMLExpression {

    private boolean wasResolved = false;

    public JMLQualifiedAllocationExpression(QualifiedAllocationExpression src) {
        this.arguments = src.arguments;
        this.anonymousType = src.anonymousType;
        this.enclosingInstance = src.enclosingInstance;
        this.enumConstant = src.enumConstant;
        this.type = src.type;
        this.typeArguments = src.typeArguments;
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return null;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;
        if (this.anonymousType == null && this.enclosingInstance == null) {
            return super.resolveType(scope);
        }

        if (this.anonymousType != null && !(this.anonymousType instanceof AdsTypeDeclaration)) {
            this.anonymousType = new AdsSourceTypeDeclaration(this, scope, this.anonymousType);
        }

        JMLAllocationExpression.computeSubstitutions(this, scope);
        if (this.enclosingInstance instanceof IJMLExpression) {
            this.enclosingInstance.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.enclosingInstance).getSubstitution(scope);
            if (subst != null) {
                this.enclosingInstance = subst;
            }
        }
        return resolveQAEFeatures(scope);
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

    private TypeBinding resolveQAEFeatures(BlockScope scope) {
        this.constant = Constant.NotAConstant;
        TypeBinding enclosingInstanceType = null;
        ReferenceBinding enclosingInstanceReference = null;
        TypeBinding receiverType = null;
        boolean hasError = false;
        boolean enclosingInstanceContainsCast = false;
        boolean argsContainCast = false;

        boolean needResolveEnclosingInstance = true;
        if (this.enclosingInstance instanceof IJMLExpression) {
            enclosingInstanceType = this.enclosingInstance.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.enclosingInstance).getSubstitution(scope);
            if (subst != null) {
                this.enclosingInstance = subst;
            } else {
                needResolveEnclosingInstance = false;
            }
        }
        if (this.enclosingInstance != null) {
            if (this.enclosingInstance instanceof CastExpression) {
                this.enclosingInstance.bits |= ASTNode.DisableUnnecessaryCastCheck;
                enclosingInstanceContainsCast = true;
            }
            if (needResolveEnclosingInstance) {
                enclosingInstanceType = this.enclosingInstance.resolveType(scope);
            }
            if (enclosingInstanceType == null) {
                hasError = true;
            } else if (enclosingInstanceType.isBaseType() || enclosingInstanceType.isArrayType()) {
                scope.problemReporter().illegalPrimitiveOrArrayTypeForEnclosingInstance(enclosingInstanceType, this.enclosingInstance);
                hasError = true;
            } else if (this.type instanceof QualifiedTypeReference) {
                scope.problemReporter().illegalUsageOfQualifiedTypeReference((QualifiedTypeReference) this.type);
                hasError = true;
            } else {
                enclosingInstanceReference = (ReferenceBinding) enclosingInstanceType;
                if (!enclosingInstanceReference.canBeSeenBy(scope)) {
                    enclosingInstanceType = new ProblemReferenceBinding(
                            enclosingInstanceReference.compoundName,
                            enclosingInstanceReference,
                            ProblemReasons.NotVisible);
                    scope.problemReporter().invalidType(this.enclosingInstance, enclosingInstanceType);
                    hasError = true;
                } else {
                    receiverType = ((SingleTypeReference) this.type).resolveTypeEnclosing(scope, (ReferenceBinding) enclosingInstanceType);
                    if (receiverType != null && enclosingInstanceContainsCast) {
                        CastExpression.checkNeedForEnclosingInstanceCast(scope, this.enclosingInstance, enclosingInstanceType, receiverType);
                    }
                }
            }
        } else {
            if (this.type == null) {
                receiverType = scope.enclosingSourceType();
            } else {
                receiverType = this.type.resolveType(scope, true /* check bounds*/);
                checkParameterizedAllocation:
                {
                    if (receiverType == null || !receiverType.isValidBinding()) {
                        break checkParameterizedAllocation;
                    }
                    if (this.type instanceof ParameterizedQualifiedTypeReference) { // disallow new X<String>.Y<Integer>()
                        ReferenceBinding currentType = (ReferenceBinding) receiverType;
                        do {
                            // isStatic() is answering true for toplevel types
                            if ((currentType.modifiers & ClassFileConstants.AccStatic) != 0) {
                                break checkParameterizedAllocation;
                            }
                            if (currentType.isRawType()) {
                                break checkParameterizedAllocation;
                            }
                        } while ((currentType = currentType.enclosingType()) != null);
                        final ParameterizedQualifiedTypeReference qRef = (ParameterizedQualifiedTypeReference) this.type;
                        for (int i = qRef.typeArguments.length - 2; i >= 0; i--) {
                            if (qRef.typeArguments[i] != null) {
                                scope.problemReporter().illegalQualifiedParameterizedTypeAllocation(this.type, receiverType);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (receiverType == null || !receiverType.isValidBinding()) {
            hasError = true;
        }

        // resolve type arguments (for generic constructor call)
        final boolean isDiamond = this.type != null && (this.type.bits & ASTNode.IsDiamond) != 0;
        if (this.typeArguments != null) {
            int length = this.typeArguments.length;
            boolean argHasError = scope.compilerOptions().sourceLevel < ClassFileConstants.JDK1_5;
            this.genericTypeArguments = new TypeBinding[length];
            for (int i = 0; i < length; i++) {
                TypeReference typeReference = this.typeArguments[i];
                if ((this.genericTypeArguments[i] = typeReference.resolveType(scope, true /* check bounds*/)) == null) {
                    argHasError = true;
                }
                if (argHasError && typeReference instanceof Wildcard) {
                    scope.problemReporter().illegalUsageOfWildcard(typeReference);
                }
            }
            if (isDiamond) {
                scope.problemReporter().diamondNotWithExplicitTypeArguments(this.typeArguments);
                return null;
            }
            if (argHasError) {
                if (this.arguments != null) { // still attempt to resolve arguments
                    for (int i = 0, max = this.arguments.length; i < max; i++) {
                        this.arguments[i].resolveType(scope);
                    }
                }
                return null;
            }
        }

        // will check for null after args are resolved
        TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;
        if (this.arguments != null) {
            int length = this.arguments.length;
            argumentTypes = new TypeBinding[length];
            for (int i = 0; i < length; i++) {
                Expression argument = this.arguments[i];
                if (argument instanceof CastExpression) {
                    argument.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
                    argsContainCast = true;
                }
                if ((argumentTypes[i] = argument.resolveType(scope)) == null) {
                    hasError = true;
                }
            }
        }
        if (hasError) {
            if (isDiamond) {
                return null;
            }
            if (receiverType instanceof ReferenceBinding) {
                ReferenceBinding referenceReceiver = (ReferenceBinding) receiverType;
                if (receiverType.isValidBinding()) {
                    // record a best guess, for clients who need hint about possible contructor match
                    int length = this.arguments == null ? 0 : this.arguments.length;
                    TypeBinding[] pseudoArgs = new TypeBinding[length];
                    for (int i = length; --i >= 0;) {
                        pseudoArgs[i] = argumentTypes[i] == null ? TypeBinding.NULL : argumentTypes[i]; // replace args with errors with null type
                    }
                    this.binding = scope.findMethod(referenceReceiver, TypeConstants.INIT, pseudoArgs, this);
                    if (this.binding != null && !this.binding.isValidBinding()) {
                        MethodBinding closestMatch = ((ProblemMethodBinding) this.binding).closestMatch;
                        // record the closest match, for clients who may still need hint about possible method match
                        if (closestMatch != null) {
                            if (closestMatch.original().typeVariables != Binding.NO_TYPE_VARIABLES) { // generic method
                                // shouldn't return generic method outside its context, rather convert it to raw method (175409)
                                closestMatch = scope.environment().createParameterizedGenericMethod(closestMatch.original(), (RawTypeBinding) null);
                            }
                            this.binding = closestMatch;
                            MethodBinding closestMatchOriginal = closestMatch.original();
                            if (closestMatchOriginal.isOrEnclosedByPrivateType() && !scope.isDefinedInMethod(closestMatchOriginal)) {
                                // ignore cases where method is used from within inside itself (e.g. direct recursions)
                                closestMatchOriginal.modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
                            }
                        }
                    }
                }
                if (this.anonymousType != null) {
                    addAnonymousType(scope, (AdsTypeDeclaration) this.anonymousType, referenceReceiver);
                    this.anonymousType.resolve(scope);
                    return this.resolvedType = this.anonymousType.binding;
                }
            }
            return this.resolvedType = receiverType;
        }
        if (this.anonymousType == null) {
            // qualified allocation with no anonymous type
            if (!receiverType.canBeInstantiated()) {
                scope.problemReporter().cannotInstantiate(this.type, receiverType);
                return this.resolvedType = receiverType;
            }
            if (isDiamond) {
                TypeBinding[] inferredTypes = inferElidedTypes(((ParameterizedTypeBinding) receiverType).genericType(), receiverType.enclosingType(), argumentTypes, scope);
                if (inferredTypes == null) {
                    scope.problemReporter().cannotInferElidedTypes(this);
                    return this.resolvedType = null;
                }
                receiverType = this.type.resolvedType = scope.environment().createParameterizedType(((ParameterizedTypeBinding) receiverType).genericType(), inferredTypes, ((ParameterizedTypeBinding) receiverType).enclosingType());
            }
            ReferenceBinding allocationType = (ReferenceBinding) receiverType;
            if ((this.binding = scope.getConstructor(allocationType, argumentTypes, this)).isValidBinding()) {
                if (isMethodUseDeprecated(this.binding, scope, true)) {
                    scope.problemReporter().deprecatedMethod(this.binding, this);
                }
                if (checkInvocationArguments(scope, null, allocationType, this.binding, this.arguments, argumentTypes, argsContainCast, this)) {
                    this.bits |= ASTNode.Unchecked;
                }
                if (this.typeArguments != null && this.binding.original().typeVariables == Binding.NO_TYPE_VARIABLES) {
                    scope.problemReporter().unnecessaryTypeArgumentsForMethodInvocation(this.binding, this.genericTypeArguments, this.typeArguments);
                }
            } else {
                if (this.binding.declaringClass == null) {
                    this.binding.declaringClass = allocationType;
                }
                if (this.type != null && !this.type.resolvedType.isValidBinding()) {
                    // problem already got signaled on type reference, do not report secondary problem
                    return null;
                }
                scope.problemReporter().invalidConstructor(this, this.binding);
                return this.resolvedType = receiverType;
            }
            if ((this.binding.tagBits & TagBits.HasMissingType) != 0) {
                scope.problemReporter().missingTypeInConstructor(this, this.binding);
            }
            if (!isDiamond && receiverType.isParameterizedTypeWithActualArguments()) {
                checkTypeArgumentRedundancy((ParameterizedTypeBinding) receiverType, receiverType.enclosingType(), argumentTypes, scope);
            }
            // The enclosing instance must be compatible with the innermost enclosing type
            ReferenceBinding expectedType = this.binding.declaringClass.enclosingType();
            if (expectedType != enclosingInstanceType) // must call before computeConversion() and typeMismatchError()
            {
                scope.compilationUnitScope().recordTypeConversion(expectedType, enclosingInstanceType);
            }
            if (enclosingInstanceType.isCompatibleWith(expectedType) || scope.isBoxingCompatibleWith(enclosingInstanceType, expectedType)) {
                this.enclosingInstance.computeConversion(scope, expectedType, enclosingInstanceType);
                return this.resolvedType = receiverType;
            }
            scope.problemReporter().typeMismatchError(enclosingInstanceType, expectedType, this.enclosingInstance, null);
            return this.resolvedType = receiverType;
        } else {
            if (isDiamond) {
                scope.problemReporter().diamondNotWithAnoymousClasses(this.type);
                return null;
            }
        }
        ReferenceBinding superType = (ReferenceBinding) receiverType;
        if (superType.isTypeVariable()) {
            superType = new ProblemReferenceBinding(new char[][]{superType.sourceName()}, superType, ProblemReasons.IllegalSuperTypeVariable);
            scope.problemReporter().invalidType(this, superType);
            return null;
        } else if (this.type != null && superType.isEnum()) { // tolerate enum constant body
            scope.problemReporter().cannotInstantiate(this.type, superType);
            return this.resolvedType = superType;
        }
        // anonymous type scenario
        // an anonymous class inherits from java.lang.Object when declared "after" an interface
        final ReferenceBinding anonymousSuperclass = superType.isInterface() ? scope.getJavaLangObject() : superType;
        // insert anonymous type in scope
        addAnonymousType(scope, (AdsTypeDeclaration) this.anonymousType, superType);
        this.anonymousType.resolve(scope);

        // find anonymous super constructor
        this.resolvedType = this.anonymousType.binding; // 1.2 change
        if ((this.resolvedType.tagBits & TagBits.HierarchyHasProblems) != 0) {
            return null; // stop secondary errors
        }
        MethodBinding inheritedBinding = scope.getConstructor(anonymousSuperclass, argumentTypes, this);
        if (!inheritedBinding.isValidBinding()) {
            if (inheritedBinding.declaringClass == null) {
                inheritedBinding.declaringClass = anonymousSuperclass;
            }
            if (this.type != null && !this.type.resolvedType.isValidBinding()) {
                // problem already got signaled on type reference, do not report secondary problem
                return null;
            }
            scope.problemReporter().invalidConstructor(this, inheritedBinding);
            return this.resolvedType;
        }
        if ((inheritedBinding.tagBits & TagBits.HasMissingType) != 0) {
            scope.problemReporter().missingTypeInConstructor(this, inheritedBinding);
        }
        if (this.enclosingInstance != null) {
            final ReferenceBinding targetEnclosing = inheritedBinding.declaringClass.enclosingType();
            if (targetEnclosing == null) {
                scope.problemReporter().unnecessaryEnclosingInstanceSpecification(this.enclosingInstance, superType);
                return this.resolvedType;
            } else if (!enclosingInstanceType.isCompatibleWith(targetEnclosing) && !scope.isBoxingCompatibleWith(enclosingInstanceType, targetEnclosing)) {
                scope.problemReporter().typeMismatchError(enclosingInstanceType, targetEnclosing, this.enclosingInstance, null);
                return this.resolvedType;
            }
            this.enclosingInstance.computeConversion(scope, targetEnclosing, enclosingInstanceType);
        }
        if (this.arguments != null) {
            if (checkInvocationArguments(scope, null, anonymousSuperclass, inheritedBinding, this.arguments, argumentTypes, argsContainCast, this)) {
                this.bits |= ASTNode.Unchecked;
            }
        }
        if (this.typeArguments != null && inheritedBinding.original().typeVariables == Binding.NO_TYPE_VARIABLES) {
            scope.problemReporter().unnecessaryTypeArgumentsForMethodInvocation(inheritedBinding, this.genericTypeArguments, this.typeArguments);
        }
        // Update the anonymous inner class : superclass, interface
        this.binding = this.anonymousType.createDefaultConstructorWithBinding(inheritedBinding, (this.bits & ASTNode.Unchecked) != 0 && this.genericTypeArguments == null);
        return this.resolvedType;
    }

    private void addAnonymousType(BlockScope scope, AdsTypeDeclaration anonymousType, ReferenceBinding superBinding) {
        final AdsDefinitionScope anonymousClassScope = new AdsDefinitionScope(scope, anonymousType);
        anonymousClassScope.buildAnonymousType(scope.enclosingSourceType(), superBinding);
    }
}
