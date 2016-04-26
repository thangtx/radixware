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

import static org.eclipse.jdt.internal.compiler.ast.ASTNode.IsReachable;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.IsUsefulEmptyStatement;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.BranchLabel;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.BOXING;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_JavaLangIterable;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_JavaLangObject;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_JavaUtilCollection;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.UNBOXING;

public class JMLForEachStatement extends ForeachStatement {

    int postCollectionInitStateIndex = -1;
    int mergedInitStateIndex = -1;
    //iterator mode, depends on collection type
    private int mode;
    private static final int MODE_ARRAY = 0;
    private static final int MODE_ITERABLE = 1;
    private static final int MODE_GENERIC_ITERABLE = 2;
    private TypeBinding iteratorRecvType;
    private TypeBinding elementType;
    private static final char[] ITERATOR_VAR_NAME = " iter".toCharArray(); //$NON-NLS-1$
    private static final char[] INDEX_VAR_NAME = " idx".toCharArray(); //$NON-NLS-1$
    private static final char[] COLLECTION_VAR_NAME = " collection".toCharArray(); //$NON-NLS-1$
    private static final char[] MAX_VAR_NAME = " len".toCharArray(); //$NON-NLS-1$
    private BranchLabel breakLabel;
    private BranchLabel continueLabel;

    public JMLForEachStatement(ForeachStatement src) {
        super(src.elementVariable, src.sourceStart);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.action = src.action;
        this.collection = src.collection;
        this.elementVariable = src.elementVariable;
        this.mode = -1;
    }

    @Override
    public void resolve(BlockScope upperScope) {
        this.scope = new BlockScope(upperScope);
        this.elementVariable.resolve(this.scope);
        final TypeBinding elementType = this.elementVariable.type.resolvedType;

        if (elementType == null) {
            Scope scope = upperScope;
            while (scope != null) {
                if (scope instanceof MethodScope) {
                    MethodScope method = (MethodScope) scope;
                    //System.out.println(method.referenceContext.toString());
                } else if (scope instanceof ClassScope) {
                    ClassScope clazz = (ClassScope) scope;
                    //System.out.println(clazz.referenceContext.toString());
                } else if (scope instanceof CompilationUnitScope) {
                    CompilationUnitScope cu = (CompilationUnitScope) scope;
                    //System.out.println(cu.referenceContext.toString());
                }
                scope = scope.parent;
            }
        }

        if (this.collection instanceof IJMLExpression) {
            this.collection.resolveType(this.scope);
            final Expression subst = ((IJMLExpression) this.collection).getSubstitution(scope);
            if (subst != null) {
                this.collection = subst;
            }
        }

        final TypeBinding collectionType = this.collection == null ? null : this.collection.resolveType(this.scope);

        TypeBinding expectedCollectionType = null;
        if (elementType != null && collectionType != null) {
            boolean isTargetJsr14 = this.scope.compilerOptions().targetJDK == ClassFileConstants.JDK1_4;
            if (collectionType.isArrayType()) { // for(E e : E[])
                this.mode = MODE_ARRAY;
                this.elementType = ((ArrayBinding) collectionType).elementsType();
                if (!this.elementType.isCompatibleWith(elementType)
                        && !this.scope.isBoxingCompatibleWith(this.elementType, elementType)) {
                    this.scope.problemReporter().notCompatibleTypesErrorInForeach(this.collection, this.elementType, elementType);
                } else if (this.elementType.needsUncheckedConversion(elementType)) {
                    this.scope.problemReporter().unsafeElementTypeConversion(this.collection, this.elementType, elementType);
                }

                int compileTimeTypeID = this.elementType.id;
                if (elementType.isBaseType()) {
                    this.collection.computeConversion(this.scope, collectionType, collectionType);
                    if (!this.elementType.isBaseType()) {
                        compileTimeTypeID = this.scope.environment().computeBoxingType(this.elementType).id;
                        this.elementVariableImplicitWidening = UNBOXING;
                        if (elementType.isBaseType()) {
                            this.elementVariableImplicitWidening |= (elementType.id << 4) + compileTimeTypeID;
                            this.scope.problemReporter().autoboxing(this.collection, this.elementType, elementType);
                        }
                    } else {
                        this.elementVariableImplicitWidening = (elementType.id << 4) + compileTimeTypeID;
                    }
                } else if (this.elementType.isBaseType()) {
                    this.collection.computeConversion(this.scope, collectionType, collectionType);
                    int boxedID = this.scope.environment().computeBoxingType(this.elementType).id;
                    this.elementVariableImplicitWidening = BOXING | (compileTimeTypeID << 4) | compileTimeTypeID; // use primitive type in implicit conversion
                    compileTimeTypeID = boxedID;
                    this.scope.problemReporter().autoboxing(this.collection, this.elementType, elementType);
                } else {
                    expectedCollectionType = upperScope.createArrayType(elementType, 1);
                    this.collection.computeConversion(this.scope, expectedCollectionType, collectionType);
                }
            } else if (collectionType instanceof ReferenceBinding) {
                ReferenceBinding iterableType = ((ReferenceBinding) collectionType).findSuperTypeOriginatingFrom(T_JavaLangIterable, false /*Iterable is not a class*/);
                if (iterableType == null && isTargetJsr14) {
                    iterableType = ((ReferenceBinding) collectionType).findSuperTypeOriginatingFrom(T_JavaUtilCollection, false /*Iterable is not a class*/);
                }
                checkIterable:
                {
                    if (iterableType == null) {
                        break checkIterable;
                    }

                    this.iteratorRecvType = collectionType.erasure();
                    if (isTargetJsr14) {
                        if (((ReferenceBinding) this.iteratorRecvType).findSuperTypeOriginatingFrom(T_JavaUtilCollection, false) == null) {
                            this.iteratorRecvType = iterableType; // handle indirect inheritance thru variable secondary bound
                            this.collection.computeConversion(this.scope, iterableType, collectionType);
                        } else {
                            this.collection.computeConversion(this.scope, collectionType, collectionType);
                        }
                    } else if (((ReferenceBinding) this.iteratorRecvType).findSuperTypeOriginatingFrom(T_JavaLangIterable, false) == null) {
                        this.iteratorRecvType = iterableType; // handle indirect inheritance thru variable secondary bound
                        this.collection.computeConversion(this.scope, iterableType, collectionType);
                    } else {
                        this.collection.computeConversion(this.scope, collectionType, collectionType);
                    }

                    TypeBinding[] arguments = null;
                    switch (iterableType.kind()) {
                        case Binding.RAW_TYPE: // for(Object o : Iterable)
                            this.mode = MODE_ITERABLE;
                            this.elementType = this.scope.getJavaLangObject();
                            if (!this.elementType.isCompatibleWith(elementType)
                                    && !this.scope.isBoxingCompatibleWith(this.elementType, elementType)) {
                                this.scope.problemReporter().notCompatibleTypesErrorInForeach(this.collection, this.elementType, elementType);
                            }
                            // no conversion needed as only for reference types
                            break checkIterable;

                        case Binding.GENERIC_TYPE: // for (T t : Iterable<T>) - in case used inside Iterable itself
                            arguments = iterableType.typeVariables();
                            break;

                        case Binding.PARAMETERIZED_TYPE: // for(E e : Iterable<E>)
                            arguments = ((ParameterizedTypeBinding) iterableType).arguments;
                            break;

                        default:
                            break checkIterable;
                    }
                    // generic or parameterized case
                    if (arguments.length != 1) {
                        break checkIterable; // per construction can only be one
                    }
                    this.mode = MODE_GENERIC_ITERABLE;

                    this.elementType = arguments[0];
                    if (!this.elementType.isCompatibleWith(elementType)
                            && !this.scope.isBoxingCompatibleWith(this.elementType, elementType)) {
                        this.scope.problemReporter().notCompatibleTypesErrorInForeach(this.collection, this.elementType, elementType);
                    } else if (this.elementType.needsUncheckedConversion(elementType)) { // https://bugs.eclipse.org/bugs/show_bug.cgi?id=393719
                        this.scope.problemReporter().unsafeElementTypeConversion(this.collection, this.elementType, elementType);
                    }
                    int compileTimeTypeID = this.elementType.id;
                    // no conversion needed as only for reference types
                    if (elementType.isBaseType()) {
                        if (!this.elementType.isBaseType()) {
                            compileTimeTypeID = this.scope.environment().computeBoxingType(this.elementType).id;
                            this.elementVariableImplicitWidening = UNBOXING;
                            if (elementType.isBaseType()) {
                                this.elementVariableImplicitWidening |= (elementType.id << 4) + compileTimeTypeID;
                            }
                        } else {
                            this.elementVariableImplicitWidening = (elementType.id << 4) + compileTimeTypeID;
                        }
                    } else {
                        if (this.elementType.isBaseType()) {
                            this.elementVariableImplicitWidening = BOXING | (compileTimeTypeID << 4) | compileTimeTypeID; // use primitive type in implicit conversion
                        }
                    }
                }
            }
            switch (this.mode) {
                case MODE_ARRAY:
                    // allocate #index secret variable (of type int)
                    this.indexVariable = new LocalVariableBinding(INDEX_VAR_NAME, TypeBinding.INT, ClassFileConstants.AccDefault, false);
                    this.scope.addLocalVariable(this.indexVariable);
                    this.indexVariable.setConstant(Constant.NotAConstant); // not inlinable
                    // allocate #max secret variable
                    this.maxVariable = new LocalVariableBinding(MAX_VAR_NAME, TypeBinding.INT, ClassFileConstants.AccDefault, false);
                    this.scope.addLocalVariable(this.maxVariable);
                    this.maxVariable.setConstant(Constant.NotAConstant); // not inlinable
                    // add #array secret variable (of collection type)
                    if (expectedCollectionType == null) {
                        this.collectionVariable = new LocalVariableBinding(COLLECTION_VAR_NAME, collectionType, ClassFileConstants.AccDefault, false);
                    } else {
                        this.collectionVariable = new LocalVariableBinding(COLLECTION_VAR_NAME, expectedCollectionType, ClassFileConstants.AccDefault, false);
                    }
                    this.scope.addLocalVariable(this.collectionVariable);
                    this.collectionVariable.setConstant(Constant.NotAConstant); // not inlinable
                    break;
                case MODE_ITERABLE:
                case MODE_GENERIC_ITERABLE:
                    // allocate #index secret variable (of type Iterator)
                    this.indexVariable = new LocalVariableBinding(ITERATOR_VAR_NAME, this.scope.getJavaUtilIterator(), ClassFileConstants.AccDefault, false);
                    this.scope.addLocalVariable(this.indexVariable);
                    this.indexVariable.setConstant(Constant.NotAConstant); // not inlinable
                    break;
                default:
                    if (isTargetJsr14) {
                        this.scope.problemReporter().invalidTypeForCollectionTarget14(this.collection);
                    } else {
                        this.scope.problemReporter().invalidTypeForCollection(this.collection);
                    }
            }
        }
        if (this.action != null) {
            this.action.resolve(this.scope);
            if (this.action instanceof IJMLExpression) {
                final Expression substitution = ((IJMLExpression) this.action).getSubstitution(scope);
                if (substitution != null) {
                    this.action = substitution;
                }
            }
        }
    }

    public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
        // initialize break and continue labels
        this.breakLabel = new BranchLabel();
        this.continueLabel = new BranchLabel();
        int initialComplaintLevel = (flowInfo.reachMode() & FlowInfo.UNREACHABLE) != 0 ? Statement.COMPLAINED_FAKE_REACHABLE : Statement.NOT_COMPLAINED;

        // process the element variable and collection
        this.collection.checkNPE(currentScope, flowContext, flowInfo);
        flowInfo = this.elementVariable.analyseCode(this.scope, flowContext, flowInfo);
        FlowInfo condInfo = this.collection.analyseCode(this.scope, flowContext, flowInfo.copy());
        LocalVariableBinding elementVarBinding = this.elementVariable.binding;

        // element variable will be assigned when iterating
        condInfo.markAsDefinitelyAssigned(elementVarBinding);

        this.postCollectionInitStateIndex = currentScope.methodScope().recordInitializationStates(condInfo);

        // process the action
        LoopingFlowContext loopingContext = new LoopingFlowContext(flowContext, flowInfo, this, this.breakLabel,
                this.continueLabel, this.scope, true);
        UnconditionalFlowInfo actionInfo
                = condInfo.nullInfoLessUnconditionalCopy();
        actionInfo.markAsDefinitelyUnknown(elementVarBinding);
        if (currentScope.compilerOptions().isAnnotationBasedNullAnalysisEnabled) {
            // this currently produces an unavoidable warning against all @NonNull element vars:
            int nullStatus = /*this.elementVariable.*/ checkAssignmentAgainstNullAnnotation(currentScope, flowContext,
                            elementVarBinding, FlowInfo.UNKNOWN, this.collection, this.elementType);
            // TODO (stephan): 	once we have JSR 308 fetch nullStatus from the collection element type
            //              	and feed the result into the above check (instead of FlowInfo.UNKNOWN)
            if ((elementVarBinding.type.tagBits & TagBits.IsBaseType) == 0) {
                actionInfo.markNullStatus(elementVarBinding, nullStatus);
            }
        }
        FlowInfo exitBranch;
        if (!(this.action == null || (this.action.isEmptyBlock()
                && currentScope.compilerOptions().complianceLevel <= ClassFileConstants.JDK1_3))) {

            if (this.action.complainIfUnreachable(actionInfo, this.scope, initialComplaintLevel, true) < Statement.COMPLAINED_UNREACHABLE) {
                actionInfo = this.action.analyseCode(this.scope, loopingContext, actionInfo).unconditionalCopy();
            }

            // code generation can be optimized when no need to continue in the loop
            exitBranch = flowInfo.unconditionalCopy().
                    addInitializationsFrom(condInfo.initsWhenFalse());
            // TODO (maxime) no need to test when false: can optimize (same for action being unreachable above)
            if ((actionInfo.tagBits & loopingContext.initsOnContinue.tagBits
                    & FlowInfo.UNREACHABLE_OR_DEAD) != 0) {
                this.continueLabel = null;
            } else {
                actionInfo = actionInfo.mergedWith(loopingContext.initsOnContinue);
                loopingContext.complainOnDeferredFinalChecks(this.scope, actionInfo);
                exitBranch.addPotentialInitializationsFrom(actionInfo);
            }
        } else {
            exitBranch = condInfo.initsWhenFalse();
        }

        // we need the variable to iterate the collection even if the
        // element variable is not used
        final boolean hasEmptyAction = this.action == null
                || this.action.isEmptyBlock()
                || ((this.action.bits & IsUsefulEmptyStatement) != 0);

        switch (this.mode) {
            case MODE_ARRAY:
                if (!hasEmptyAction
                        || elementVarBinding.resolvedPosition != -1) {
                    this.collectionVariable.useFlag = LocalVariableBinding.USED;
                    if (this.continueLabel != null) {
                        this.indexVariable.useFlag = LocalVariableBinding.USED;
                        this.maxVariable.useFlag = LocalVariableBinding.USED;
                    }
                }
                break;
            case MODE_ITERABLE:
            case MODE_GENERIC_ITERABLE:
                this.indexVariable.useFlag = LocalVariableBinding.USED;
                break;
        }
        //end of loop
        loopingContext.complainOnDeferredNullChecks(currentScope, actionInfo);

        FlowInfo mergedInfo = FlowInfo.mergedOptimizedBranches(
                (loopingContext.initsOnBreak.tagBits
                & FlowInfo.UNREACHABLE) != 0
                        ? loopingContext.initsOnBreak
                        : flowInfo.addInitializationsFrom(loopingContext.initsOnBreak), // recover upstream null info
                false,
                exitBranch,
                false,
                true /*for(;;){}while(true); unreachable(); */);
        mergedInfo.resetAssignmentInfo(this.elementVariable.binding);
        this.mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
        return mergedInfo;
    }

    /**
     * For statement code generation
     *
     * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
     * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
     */
    public void generateCode(BlockScope currentScope, CodeStream codeStream) {
        if ((this.bits & IsReachable) == 0) {
            return;
        }
        int pc = codeStream.position;
        final boolean hasEmptyAction = this.action == null
                || this.action.isEmptyBlock()
                || ((this.action.bits & IsUsefulEmptyStatement) != 0);

        if (hasEmptyAction
                && this.elementVariable.binding.resolvedPosition == -1
                && this.mode == MODE_ARRAY) {
            this.collection.generateCode(this.scope, codeStream, false);
            codeStream.exitUserScope(this.scope);
            if (this.mergedInitStateIndex != -1) {
                codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
                codeStream.addDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
            }
            codeStream.recordPositionsFrom(pc, this.sourceStart);
            return;
        }

        // generate the initializations
        switch (this.mode) {
            case MODE_ARRAY:
                this.collection.generateCode(this.scope, codeStream, true);
                codeStream.store(this.collectionVariable, true);
                codeStream.addVariable(this.collectionVariable);
                if (this.continueLabel != null) {
                    // int length = (collectionVariable = [collection]).length;
                    codeStream.arraylength();
                    codeStream.store(this.maxVariable, false);
                    codeStream.addVariable(this.maxVariable);
                    codeStream.iconst_0();
                    codeStream.store(this.indexVariable, false);
                    codeStream.addVariable(this.indexVariable);
                } else {
                    // leave collectionVariable on execution stack (will be consumed when swapping condition further down)
                }
                break;
            case MODE_ITERABLE:
            case MODE_GENERIC_ITERABLE:
                this.collection.generateCode(this.scope, codeStream, true);
                // declaringClass.iterator();
                codeStream.invokeIterableIterator(this.iteratorRecvType);
                codeStream.store(this.indexVariable, false);
                codeStream.addVariable(this.indexVariable);
                break;
        }
        // label management
        BranchLabel actionLabel = new BranchLabel(codeStream);
        actionLabel.tagBits |= BranchLabel.USED;
        BranchLabel conditionLabel = new BranchLabel(codeStream);
        conditionLabel.tagBits |= BranchLabel.USED;
        this.breakLabel.initialize(codeStream);
        if (this.continueLabel == null) {
            // generate the condition (swapped for optimizing)
            conditionLabel.place();
            int conditionPC = codeStream.position;
            switch (this.mode) {
                case MODE_ARRAY:
                    // inline the arraylength call
                    // collectionVariable is already on execution stack
                    codeStream.arraylength();
                    codeStream.ifeq(this.breakLabel);
                    break;
                case MODE_ITERABLE:
                case MODE_GENERIC_ITERABLE:
                    codeStream.load(this.indexVariable);
                    codeStream.invokeJavaUtilIteratorHasNext();
                    codeStream.ifeq(this.breakLabel);
                    break;
            }
            codeStream.recordPositionsFrom(conditionPC, this.elementVariable.sourceStart);
        } else {
            this.continueLabel.initialize(codeStream);
            this.continueLabel.tagBits |= BranchLabel.USED;
            // jump over the actionBlock
            codeStream.goto_(conditionLabel);
        }

        // generate the loop action
        actionLabel.place();

        // generate the loop action
        switch (this.mode) {
            case MODE_ARRAY:
                if (this.elementVariable.binding.resolvedPosition != -1) {
                    codeStream.load(this.collectionVariable);
                    if (this.continueLabel == null) {
                        codeStream.iconst_0(); // no continue, thus simply hardcode offset 0
                    } else {
                        codeStream.load(this.indexVariable);
                    }
                    codeStream.arrayAt(this.elementType.id);
                    if (this.elementVariableImplicitWidening != -1) {
                        codeStream.generateImplicitConversion(this.elementVariableImplicitWidening);
                    }
                    codeStream.store(this.elementVariable.binding, false);
                    codeStream.addVisibleLocalVariable(this.elementVariable.binding);
                    if (this.postCollectionInitStateIndex != -1) {
                        codeStream.addDefinitelyAssignedVariables(
                                currentScope,
                                this.postCollectionInitStateIndex);
                    }
                }
                break;
            case MODE_ITERABLE:
            case MODE_GENERIC_ITERABLE:
                codeStream.load(this.indexVariable);
                codeStream.invokeJavaUtilIteratorNext();
                if (this.elementVariable.binding.type.id != T_JavaLangObject) {
                    if (this.elementVariableImplicitWidening != -1) {
                        codeStream.checkcast(this.elementType);
                        codeStream.generateImplicitConversion(this.elementVariableImplicitWidening);
                    } else {
                        codeStream.checkcast(this.elementVariable.binding.type);
                    }
                }
                if (this.elementVariable.binding.resolvedPosition == -1) {
                    switch (this.elementVariable.binding.type.id) {
                        case TypeIds.T_long:
                        case TypeIds.T_double:
                            codeStream.pop2();
                            break;
                        default:
                            codeStream.pop();
                            break;
                    }
                } else {
                    codeStream.store(this.elementVariable.binding, false);
                    codeStream.addVisibleLocalVariable(this.elementVariable.binding);
                    if (this.postCollectionInitStateIndex != -1) {
                        codeStream.addDefinitelyAssignedVariables(
                                currentScope,
                                this.postCollectionInitStateIndex);
                    }
                }
                break;
        }

        if (!hasEmptyAction) {
            this.action.generateCode(this.scope, codeStream);
        }
        codeStream.removeVariable(this.elementVariable.binding);
        if (this.postCollectionInitStateIndex != -1) {
            codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.postCollectionInitStateIndex);
        }
        // continuation point
        if (this.continueLabel != null) {
            this.continueLabel.place();
            int continuationPC = codeStream.position;
            // generate the increments for next iteration
            switch (this.mode) {
                case MODE_ARRAY:
                    if (!hasEmptyAction || this.elementVariable.binding.resolvedPosition >= 0) {
                        codeStream.iinc(this.indexVariable.resolvedPosition, 1);
                    }
                    // generate the condition
                    conditionLabel.place();
                    codeStream.load(this.indexVariable);
                    codeStream.load(this.maxVariable);
                    codeStream.if_icmplt(actionLabel);
                    break;
                case MODE_ITERABLE:
                case MODE_GENERIC_ITERABLE:
                    // generate the condition
                    conditionLabel.place();
                    codeStream.load(this.indexVariable);
                    codeStream.invokeJavaUtilIteratorHasNext();
                    codeStream.ifne(actionLabel);
                    break;
            }
            codeStream.recordPositionsFrom(continuationPC, this.elementVariable.sourceStart);
        }
        switch (this.mode) {
            case MODE_ARRAY:
                codeStream.removeVariable(this.indexVariable);
                codeStream.removeVariable(this.maxVariable);
                codeStream.removeVariable(this.collectionVariable);
                break;
            case MODE_ITERABLE:
            case MODE_GENERIC_ITERABLE:
                // generate the condition
                codeStream.removeVariable(this.indexVariable);
                break;
        }
        codeStream.exitUserScope(this.scope);
        if (this.mergedInitStateIndex != -1) {
            codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
            codeStream.addDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
        }
        this.breakLabel.place();
        codeStream.recordPositionsFrom(pc, this.sourceStart);
    }
}
