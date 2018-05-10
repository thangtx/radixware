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
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.DisableUnnecessaryCastCheck;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedGenericMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;


public class JMLAllocationExpression extends AllocationExpression implements IJMLExpression {

    private boolean wasResolved = false;
    private Expression substitution;

    public JMLAllocationExpression(AllocationExpression src) {
        this.arguments = src.arguments;
        this.enumConstant = src.enumConstant;
        this.type = src.type;
        this.typeArguments = src.typeArguments;
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }

        wasResolved = true;
        substitution = computeSubstitutions(this, scope);
        if (substitution != null) {
            return substitution.resolveType(scope);
        }
        if (this.type instanceof ParameterizedQualifiedTypeReference && 
                (this.type.bits & ASTNode.IsDiamond) != 0 && 
                !(this.type.resolvedType instanceof ParameterizedTypeBinding)) {
            return null;
        }
        return super.resolveType(scope);
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return substitution;
    }

    static Expression computeSubstitutions(AllocationExpression alloc, BlockScope scope) {
        if (alloc.type != null && (alloc.typeArguments == null || alloc.typeArguments.length == 0) && (alloc.arguments != null && alloc.arguments.length == 1 && alloc.arguments[0] != null)) {
            if (!(alloc instanceof QualifiedAllocationExpression)) {
                final TypeBinding allocatedType = alloc.type.resolveType(scope);
                if (allocatedType != null && allocatedType.isValidBinding()) {
                    final Expression conversion = JMLMathOperations.computeConversionToReferenceNumeric(allocatedType, alloc.arguments[0], scope);
                    if (conversion != alloc.arguments[0]) {
                        return conversion;
                    }
                }
            }
        }

        if (alloc.arguments != null) {
            for (int i = 0; i < alloc.arguments.length; i++) {
                if (alloc.arguments[i] instanceof IJMLExpression) {
                    alloc.arguments[i].resolve(scope);
                    final Expression subst = ((IJMLExpression) alloc.arguments[i]).getSubstitution(scope);
                    if (subst != null) {
                        alloc.arguments[i] = subst;
                    }
                }
            }
        }
        if (alloc.typeArguments != null) {
            for (int i = 0; i < alloc.typeArguments.length; i++) {
                if (alloc.typeArguments[i] instanceof IJMLExpression) {
                    alloc.typeArguments[i].resolve(scope);
                    final Expression subst = ((IJMLExpression) alloc.typeArguments[i]).getSubstitution(scope);
                    if (subst != null) {
                        alloc.typeArguments[i] = (TypeReference) subst;
                    }
                }
            }
        }
        final TypeBinding allocatedType = resolveAllocationType(alloc, alloc instanceof QualifiedAllocationExpression ? ((QualifiedAllocationExpression) alloc).enclosingInstance : null, scope);
        TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;

        boolean argHasErrors = false;
        if (alloc.arguments != null) {
            argumentTypes = new TypeBinding[alloc.arguments.length];
            for (int i = 0; i < argumentTypes.length; i++) {
                argumentTypes[i] = alloc.arguments[i].resolveType(scope);
                if (argumentTypes[i] == null) {
                    argHasErrors = true;
                }
            }
        }

        if (!argHasErrors && allocatedType instanceof ReferenceBinding) {
            MethodBinding constructor = scope.getConstructor((ReferenceBinding) allocatedType, argumentTypes, alloc);
            if (constructor != null && !constructor.isValidBinding()) {
                constructor = lookForConstructor((ReferenceBinding) allocatedType, argumentTypes, scope, alloc);
                if (constructor != null && constructor.isValidBinding()) {
                    for (int i = 0; i < argumentTypes.length; i++) {
                        alloc.arguments[i] = JMLMathOperations.computeConversionToReferenceNumeric(constructor.parameters[i], alloc.arguments[i], scope);
                    }
                }
            }
        }
        return null;
    }

    public static MethodBinding lookForConstructor(ReferenceBinding allocatedType, TypeBinding[] argumentTypes, BlockScope scope, InvocationSite invocationSite) {
        final MethodBinding[] methods = allocatedType.getMethods(TypeConstants.INIT, argumentTypes.length);
        if (methods == Binding.NO_METHODS) {
            return new ProblemMethodBinding(
                    TypeConstants.INIT,
                    argumentTypes,
                    ProblemReasons.NotFound);
        }
        final TypeBinding[] convertedArguments = new TypeBinding[argumentTypes.length];
        for (int i = 0, length = methods.length; i < length; i++) {
            final MethodBinding method = methods[i];
            if (method.parameters.length != argumentTypes.length) {
                continue;
            }
            boolean wasConverted = false;
            for (int p = 0; p < method.parameters.length; p++) {
                TypeBinding type = JMLMathOperations.computeConversionToReferenceNumeric(method.parameters[p], argumentTypes[p], scope);
                if (type != null) {
                    convertedArguments[p] = type;
                    wasConverted = true;
                } else {
                    convertedArguments[p] = argumentTypes[p];
                }
            }
            if (wasConverted) {
                MethodBinding binding = scope.getConstructor(allocatedType, convertedArguments, invocationSite);
                if (binding != null && binding.isValidBinding()) {
                    return binding;
                }
            }
        }
        return new ProblemMethodBinding(
                TypeConstants.INIT,
                argumentTypes,
                ProblemReasons.NotFound);
    }

    @Override
    public Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        return JmlStatementUtils.resolveCase(this, scope, testType, switchStatement);
    }

    @Override
    public Constant resolveCaseOriginal(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        return super.resolveCase(scope, testType, switchStatement);
    }

    private static TypeBinding resolveAllocationType(AllocationExpression alloc, Expression enclosingInstance, BlockScope scope) {
        TypeBinding allocType;
        if (alloc.type == null) {
            allocType = scope.enclosingReceiverType();
        } else {
            if (enclosingInstance != null) {
                TypeBinding enclosingInstanceType = enclosingInstance.resolveType(scope);
                if (enclosingInstanceType instanceof ReferenceBinding) {
                    if (alloc.type instanceof SingleTypeReference) {
                        allocType = ((SingleTypeReference) alloc.type).resolveTypeEnclosing(scope, (ReferenceBinding) enclosingInstanceType);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                allocType = alloc.type.resolveType(scope, true);
                checkParameterizedAllocation: {
                    if (alloc.type instanceof ParameterizedQualifiedTypeReference) { // disallow new X<String>.Y<Integer>()
                        ReferenceBinding currentType = (ReferenceBinding) alloc.resolvedType;
                        if (currentType == null) {
                            return currentType;
                        }
                        do {
                            // isStatic() is answering true for toplevel types
                            if ((currentType.modifiers & ClassFileConstants.AccStatic) != 0) {
                                break checkParameterizedAllocation;
                            }
                            if (currentType.isRawType()) {
                                break checkParameterizedAllocation;
                            }
                        } while ((currentType = currentType.enclosingType()) != null);
                        ParameterizedQualifiedTypeReference qRef = (ParameterizedQualifiedTypeReference) alloc.type;
                        for (int i = qRef.typeArguments.length - 2; i >= 0; i--) {
                            if (qRef.typeArguments[i] != null) {
                                scope.problemReporter().illegalQualifiedParameterizedTypeAllocation(alloc.type, alloc.resolvedType);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (allocType == null || !allocType.isValidBinding()) {
            return null;
        }
        final boolean isDiamond = alloc.type != null && (alloc.type.bits & ASTNode.IsDiamond) != 0;

        TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;
        if (alloc.arguments != null) {
            boolean argHasError = false;
            final int length = alloc.arguments.length;
            argumentTypes = new TypeBinding[length];
            for (int i = 0; i < length; i++) {
                Expression argument = alloc.arguments[i];
                if (argument instanceof CastExpression) {
                    argument.bits |= DisableUnnecessaryCastCheck; // will check later on                    
                }
                argumentTypes[i] = argument.resolveType(scope);
                if (argumentTypes[i] == null) {
                    argHasError = true;
                }
            }
            if (argHasError) {
                if (isDiamond) {
                    return null; // not the partially cooked this.resolvedType
                }
                if (allocType instanceof ReferenceBinding) {
                    // record a best guess, for clients who need hint about possible constructor match
                    TypeBinding[] pseudoArgs = new TypeBinding[length];
                    for (int i = length; --i >= 0;) {
                        pseudoArgs[i] = argumentTypes[i] == null ? TypeBinding.NULL : argumentTypes[i]; // replace args with errors with null type
                    }
                }
                return allocType;
            }
        }

        if (isDiamond) {
            final TypeBinding[] inferredTypes = inferElidedTypes(alloc, ((ParameterizedTypeBinding) allocType).genericType(), null, argumentTypes, scope);
            if (inferredTypes == null) {
                return null;
            }
            allocType = scope.environment().createParameterizedType(((ParameterizedTypeBinding) allocType).genericType(), inferredTypes, ((ParameterizedTypeBinding) allocType).enclosingType());
        }

        return allocType;
    }

    private static TypeBinding[] inferElidedTypes(AllocationExpression alloc, ReferenceBinding allocationType, ReferenceBinding enclosingType, TypeBinding[] argumentTypes, final BlockScope scope) {
        final MethodBinding factory = scope.getStaticFactory(allocationType, enclosingType, argumentTypes, alloc);
        if (factory instanceof ParameterizedGenericMethodBinding && factory.isValidBinding()) {
            final ParameterizedGenericMethodBinding genericFactory = (ParameterizedGenericMethodBinding) factory;
            return ((ParameterizedTypeBinding) factory.returnType).arguments;
        } else {
            return null;
        }
    }

    @Override
    public RadixObjectLocation getRadixObject(int index) {
        return null;
    }
}
