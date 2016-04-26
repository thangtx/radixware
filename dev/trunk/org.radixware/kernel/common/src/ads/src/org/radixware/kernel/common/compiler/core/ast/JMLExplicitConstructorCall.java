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
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.CaptureBinding;
import org.eclipse.jdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;

public class JMLExplicitConstructorCall extends ExplicitConstructorCall implements IJMLExpression {

    private boolean wasResolved = false;

    public JMLExplicitConstructorCall(int accessMode) {
        super(accessMode);
    }

    public JMLExplicitConstructorCall(ExplicitConstructorCall src) {
        super(src.accessMode);
        this.arguments = src.arguments;
        this.qualification = src.qualification;
        this.typeArguments = src.typeArguments;
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return null;
    }

    @Override
    public void resolve(BlockScope scope) {
        if (wasResolved) {
            return;
        }
        wasResolved = true;
        if (this.arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                Expression a = arguments[i];
                if (a instanceof IJMLExpression) {
                    a.resolveType(scope);
                    Expression subst = ((IJMLExpression) a).getSubstitution(scope);
                    if (subst != null) {
                        arguments[i] = subst;
                    }
                }
            }
        }
        //look for coversions
        TypeBinding[] argTypes = resolveArgumentTypes(scope);
        if (argTypes != null) {
            JMLMessageSend.MethodInfo constructorInfo = lookForConstructor(scope.enclosingReceiverType(), argTypes, scope, this);
            if (constructorInfo != null && constructorInfo.method != null && constructorInfo.method.isValidBinding() && constructorInfo.convertedArguments != null) {
                for (int i = 0; i < arguments.length; i++) {
                    final Expression mathOper = JMLMathOperations.computeConversionToReferenceNumeric(constructorInfo.convertedArguments[i], arguments[i], scope);
                    if (mathOper != null) {
                        arguments[i] = mathOper;
                    }
                }
            }
        }

        super.resolve(scope);
    }

    private TypeBinding[] resolveArgumentTypes(BlockScope scope) {
        TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;
        if (this.arguments != null) {
            boolean argHasError = false; // typeChecks all arguments
            final int length = this.arguments.length;
            argumentTypes = new TypeBinding[length];
            for (int i = 0; i < length; i++) {
                Expression argument = this.arguments[i];
                if (argument instanceof CastExpression) {
                    argument.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on        
                }
                if ((argumentTypes[i] = argument.resolveType(scope)) == null) {
                    argHasError = true;
                }
            }
            if (!argHasError) {
                return argumentTypes;
            }
        }
        return null;
    }

    public static JMLMessageSend.MethodInfo lookForConstructor(ReferenceBinding createdType, TypeBinding[] argumentTypes, BlockScope scope, InvocationSite invocationSite) {
        final MethodBinding[] methods = createdType.getMethods(TypeConstants.INIT, argumentTypes.length);
        if (methods == Binding.NO_METHODS) {
            return new JMLMessageSend.MethodInfo(new ProblemMethodBinding(
                    TypeConstants.INIT,
                    argumentTypes,
                    ProblemReasons.NotFound));
        }
        final TypeBinding[] convertedArguments = new TypeBinding[argumentTypes.length];

        for (int i = 0, length = methods.length; i < length; i++) {
            final MethodBinding method = methods[i];
//            if (method.parameters.length != argumentTypes.length && method.parameters.length != varargArgCount) {
//                continue;
//            }
            boolean wasConverted = false;

            int paramCount = method.parameters.length;

            if (argumentTypes.length != paramCount) {
                if (argumentTypes.length > paramCount) {
                    if (method.isVarargs()) {
                        paramCount = argumentTypes.length;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }

            for (int p = 0; p < paramCount; p++) {

                TypeBinding parameterType;

                if (p < method.parameters.length - 1) {
                    parameterType = method.parameters[p];
                } else {
                    if (method.isVarargs()) {
                        parameterType = method.parameters[method.parameters.length - 1].leafComponentType();
                    } else {
                        parameterType = method.parameters[p];
                    }
                }
                while (parameterType instanceof CaptureBinding) {
                    parameterType = ((CaptureBinding) parameterType).superclass;
                }
                if (parameterType != null) {
                    final TypeBinding type = JMLMathOperations.computeConversionToReferenceNumeric(parameterType, argumentTypes[p], scope);
                    if (type != null) {
                        convertedArguments[p] = type;
                        wasConverted = true;
                    } else {
                        convertedArguments[p] = argumentTypes[p];
                    }
                } else {
                    convertedArguments[p] = argumentTypes[p];
                }
            }
            if (wasConverted) {
                final MethodBinding methodBinding = scope.getMethod(createdType, TypeConstants.INIT, convertedArguments, invocationSite);
                if (methodBinding != null && methodBinding.isValidBinding()) {
                    return new JMLMessageSend.MethodInfo(methodBinding, convertedArguments);
                }
            }
        }
        return new JMLMessageSend.MethodInfo(new ProblemMethodBinding(
                TypeConstants.INIT,
                argumentTypes,
                ProblemReasons.NotFound));
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
