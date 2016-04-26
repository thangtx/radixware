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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
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
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.jml.JmlTagId;

public class JMLMessageSend extends MessageSend {

    private boolean isGetter = false;
    private boolean hasSetter = false;
    private boolean isSetter = false;
    private TypeBinding expectedSetterType;
    private boolean wasResolved;

    public JMLMessageSend() {
    }

    public JMLMessageSend(MethodBinding[] getter) {
        this.isGetter = getter != null;
        this.hasSetter = getter.length > 1;
        if (hasSetter) {
            MethodBinding setter = getter[1];
            if (setter.parameters.length > 0) {
                expectedSetterType = setter.parameters[0];
            }
        }
        this.selector = getter[0].selector;
    }

    public boolean isProperty() {
        return isGetter;
    }

    public char[] getPropertyName() {
        if (isProperty()) {
            final char[] propName = new char[selector.length - 3];
            System.arraycopy(selector, 3, propName, 0, selector.length - 3);
            return propName;
        } else {
            return null;
        }
    }

    public RadixObject findRadixObject(AdsCompilationUnitDeclaration cu) {
        if (cu.radixObjectLookup == null) {
            return null;
        }
        final RadixObjectLocator.RadixObjectData[] data = cu.radixObjectLookup.take((int) (this.nameSourcePosition >>> 32), (int) this.nameSourcePosition);
        if (data != null && data[0].radixObject instanceof JmlTagId) {
            return ((JmlTagId) data[0].radixObject).resolve(cu.getContextDefinition());
        } else {
            return null;
        }
    }

    public JMLMessageSend(MessageSend ms) {
        this.typeArguments = ms.typeArguments;
        this.selector = ms.selector;
        this.receiver = ms.receiver;
        this.arguments = ms.arguments;
        this.nameSourcePosition = ms.nameSourcePosition;
        this.sourceEnd = ms.sourceEnd;
        this.sourceStart = ms.sourceStart;
        this.constant = Constant.NotAConstant;
        this.bits = ms.bits;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return this.resolvedType;
        }
        wasResolved = true;
        if (this.receiver instanceof IJMLExpression) {
            this.receiver.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.receiver).getSubstitution(scope);
            if (subst != null) {
                this.receiver = subst;
            }
        }
        if (this.arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                final Expression a = arguments[i];
                if (a instanceof IJMLExpression) {
                    if (isSetter && expectedSetterType != null) {
                        a.setExpectedType(expectedSetterType);
                    }
                    a.resolveType(scope);
                    final Expression subst = ((IJMLExpression) a).getSubstitution(scope);
                    if (subst != null) {
                        arguments[i] = subst;
                    }
                }
            }
        }

        final TypeBinding[] argTypes = resolveArgumentTypes(scope);
        if (argTypes != null && argTypes.length > 0) {
            final TypeBinding recvType = this.receiver.resolveType(scope);

            MethodBinding targetMethod = this.receiver.isImplicitThis()
                    ? scope.getImplicitMethod(this.selector, argTypes, this)
                    : recvType == null ? null : scope.getMethod(recvType, this.selector, argTypes, this);
            if (targetMethod != null) {
                if (!targetMethod.isValidBinding() && recvType instanceof ReferenceBinding) {
                    final ReferenceBinding ref = this.receiver.isImplicitThis() ? scope.enclosingSourceType() : (ReferenceBinding) recvType;
                    MethodInfo methodInfo = lookForMethod(ref, ref, argTypes, scope, this);
                    if (methodInfo != null && methodInfo.method.isValidBinding() && methodInfo.convertedArguments != null && methodInfo.convertedArguments.length == arguments.length) {
                        for (int i = 0; i < arguments.length; i++) {
                            final Expression mathOper = JMLMathOperations.computeConversionToReferenceNumeric(methodInfo.convertedArguments[i], arguments[i], scope);
                            if (mathOper != null) {
                                arguments[i] = mathOper;
                            }
                        }
                    }
                } else {
                    if (targetMethod.parameters.length == arguments.length) {
                        for (int i = 0; i < arguments.length; i++) {
                            final Expression mathOper = JMLMathOperations.computeConversionToReferenceNumeric(targetMethod.parameters[i], arguments[i], scope);
                            if (mathOper != null) {
                                arguments[i] = mathOper;
                            }
                        }
                    }
                }
            }
        }

        super.resolveType(scope);
        return resolvedType;
    }

    private MethodInfo lookForMethod(ReferenceBinding originalRecvType, ReferenceBinding recvType, TypeBinding[] argumentTypes, BlockScope scope, InvocationSite invocationSite) {
        MethodInfo methodInfo = lookForMethodInType(originalRecvType, recvType, argumentTypes, scope, this);

        if (methodInfo != null) {
            if (methodInfo.method.isValidBinding()) {
                return methodInfo;
            }
        }
        if (!recvType.isInterface()) {
            if (recvType.superclass() != null) {
                methodInfo = lookForMethod(originalRecvType, recvType.superclass(), argumentTypes, scope, invocationSite);
                if (methodInfo != null) {
                    if (methodInfo.method.isValidBinding()) {
                        return methodInfo;
                    }
                }
            }
        }
        if (recvType.superInterfaces() != null) {
            for (ReferenceBinding iface : recvType.superInterfaces()) {
                methodInfo = lookForMethod(originalRecvType, iface, argumentTypes, scope, invocationSite);
                if (methodInfo != null) {
                    if (methodInfo.method.isValidBinding()) {
                        return methodInfo;
                    }
                }
            }
        }
        if (!originalRecvType.isStatic() && originalRecvType.enclosingType() != null) {
            methodInfo = lookForMethod(originalRecvType.enclosingType(), originalRecvType.enclosingType(), argumentTypes, scope, this);
            if (methodInfo != null && methodInfo.method.isValidBinding()) {
                return methodInfo;
            }
        }
        return null;
    }

    public static class MethodInfo {

        MethodBinding method;
        TypeBinding[] convertedArguments;

        public MethodInfo(MethodBinding method, TypeBinding[] convertedArguments) {
            this.method = method;
            this.convertedArguments = convertedArguments;
        }

        public MethodInfo(MethodBinding method) {
            this.method = method;
        }

    }

    private MethodInfo lookForMethodInType(ReferenceBinding originalRecvType, ReferenceBinding recvType, TypeBinding[] argumentTypes, BlockScope scope, InvocationSite invocationSite) {
        final MethodBinding[] methods = recvType.getMethods(selector, argumentTypes.length);
        if (methods == Binding.NO_METHODS) {
            return new MethodInfo(new ProblemMethodBinding(
                    selector,
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
                final MethodBinding methodBinding = scope.getMethod(originalRecvType, selector, convertedArguments, invocationSite);
                if (methodBinding != null && methodBinding.isValidBinding()) {
                    return new MethodInfo(methodBinding, convertedArguments);
                }
            }
        }
        return new MethodInfo(new ProblemMethodBinding(
                selector,
                argumentTypes,
                ProblemReasons.NotFound));
    }

    public JMLMessageSend convertToSetter(BlockScope scope) {
        if (!hasSetter) {
            //TODO: report problem
            ((AdsProblemReporter) scope.problemReporter()).readOnlyProperty(this, this.binding);
            return null;
        }
        final JMLMessageSend setter = new JMLMessageSend();
        setter.nameSourcePosition = this.nameSourcePosition;
        setter.sourceStart = sourceStart;
        setter.sourceEnd = sourceEnd;
        setter.selector = Arrays.copyOf(selector, selector.length);
        setter.selector[0] = 's';
        setter.isSetter = true;
        setter.expectedSetterType = expectedSetterType;
        setter.receiver = this.receiver;
        return setter;
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
}
