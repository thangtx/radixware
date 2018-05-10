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

import org.eclipse.jdt.internal.codeassist.complete.CompletionOnFieldType;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnLocalName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMemberAccess;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMessageSend;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AssertStatement;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.BreakStatement;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.CombinedBinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ContinueStatement;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.LabeledStatement;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.PrefixExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SubRoutineStatement;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.lookup.AdsSourceTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;

public class Java2JmlConverter extends ASTVisitor {

    private RadixObjectLocator locator;

    public Java2JmlConverter(RadixObjectLocator locator) {
        this.locator = locator;
    }

    public void convertToJML(Statement[] statements, BlockScope scope) {
        if (statements == null) {
            return;
        }
        for (int i = 0; i < statements.length; i++) {
            statements[i] = convertToJML(statements[i], scope);
        }
    }

    private Statement convertStatement(Statement stmt, BlockScope scope) {
        if (stmt instanceof CompletionOnLocalName) {
            return new JMLCompletionOnLocalName((CompletionOnLocalName) stmt);
        }
        if (stmt instanceof TypeDeclaration && !(stmt instanceof AdsTypeDeclaration)) {
            final TypeDeclaration src = (TypeDeclaration) stmt;
            if (src.allocation instanceof QualifiedAllocationExpression) {
                if (src.allocation instanceof JMLQualifiedAllocationExpression) {
                    return new AdsSourceTypeDeclaration((JMLQualifiedAllocationExpression) src.allocation, scope, src);
                } else {
                    return stmt;
                }
            } else {
                if (scope == null) {
                    return stmt;
                }
                return new AdsSourceTypeDeclaration(null, scope, src);
            }

        } else if (stmt instanceof Expression) {
            return convertExpression((Expression) stmt, scope);
        } else if (stmt instanceof Argument) {
            final Argument ld = (Argument) stmt;
            return new JMLArgument(ld);
        } else if (stmt instanceof LocalDeclaration) {
            final LocalDeclaration ld = (LocalDeclaration) stmt;
            return new JMLLocalDeclaration(ld);
        } else if (stmt instanceof Block) {
            return new JMLBlock((Block) stmt);
        } else if (stmt instanceof ReturnStatement) {
            return new JMLReturnStatement((ReturnStatement) stmt);
        } else if (stmt instanceof IfStatement) {
            return new JMLIfStatement((IfStatement) stmt);
        } else if (stmt instanceof ForeachStatement) {
            return new JMLForEachStatement((ForeachStatement) stmt);
        } else if (stmt instanceof ExplicitConstructorCall) {
            return new JMLExplicitConstructorCall((ExplicitConstructorCall) stmt);
        } else if (stmt instanceof ThrowStatement) {
            return new JMLThrowStatement((ThrowStatement) stmt);
        } else if (stmt instanceof ForStatement) {
            return new JMLForStatement((ForStatement) stmt);
        } else if (stmt instanceof DoStatement) {
            return new JMLDoStatement((DoStatement) stmt);
        } else if (stmt instanceof WhileStatement) {
            return new JMLWhileStatement((WhileStatement) stmt);
        } else if (stmt instanceof SwitchStatement) {
            return new JMLSwitchStatement((SwitchStatement) stmt);
        } else if (stmt instanceof CaseStatement) {
            return new JMLCaseStatement((CaseStatement) stmt);
        } else if (stmt instanceof Initializer) {
            return new JMLInitializer((Initializer) stmt);
        } else if (stmt instanceof CompletionOnFieldType) {
            return new JMLCompletionOnFieldType((CompletionOnFieldType) stmt);
        } else if (stmt instanceof FieldDeclaration) {
            return new JMLFieldDeclaration((FieldDeclaration) stmt);
        } else if (stmt instanceof LabeledStatement) {
            return new JMLLabeledStatement((LabeledStatement) stmt);
        }

        return stmt;
    }

    public Statement convertToJML(Statement stmt, BlockScope scope) {
        final Statement result = convertStatement(stmt, scope);
        if (result instanceof FieldDeclaration && scope instanceof MethodScope) {
            ((FieldDeclaration) result).traverse(this, (MethodScope) scope);
        } else {
            result.traverse(this, scope);
        }

        return result;
    }
    
    public void convertFields(FieldDeclaration[] fields, BlockScope scope) {
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                if (!(fields[i] instanceof JMLFieldDeclaration)) {
                    fields[i] = (FieldDeclaration) convertToJML(fields[i], scope);
                }
            }
        }
    }
    
    public void convertMethod(AbstractMethodDeclaration methodDecl, MethodScope scope) {
        if (methodDecl.thrownExceptions != null) {
            convertToJML(methodDecl.thrownExceptions, scope);
        }
        if (methodDecl.annotations != null) {
            convertToJML(methodDecl.annotations, scope);
        }
        if (methodDecl.arguments != null) {
            convertToJML(methodDecl.arguments, scope);
        }
        if (methodDecl instanceof MethodDeclaration) {
            if (((MethodDeclaration) methodDecl).typeParameters != null) {
                convertToJML(((MethodDeclaration) methodDecl).typeParameters, scope);
            }
        }
        if (methodDecl instanceof ConstructorDeclaration) {
            ConstructorDeclaration init = (ConstructorDeclaration) methodDecl;
            if (init.constructorCall != null) {
                init.constructorCall = (ExplicitConstructorCall) convertToJML(init.constructorCall, scope);
            }
        }

        if (methodDecl.statements != null) {
            convertToJML(methodDecl.statements, scope);
        } 
    }

    public static TypeReference convertTypeReference(TypeReference reference, RadixObjectLocator locator) {
        if (reference == null) {
            return null;
        }
        if (reference instanceof Wildcard) {
            return reference;
        }
        if (reference instanceof ParameterizedQualifiedTypeReference) {
            return new JMLParameterizedQualifiedTypeReference((ParameterizedQualifiedTypeReference) reference, locator);
        } else if (reference instanceof ParameterizedSingleTypeReference) {
            return new JMLParameterizedSingleTypeReference((ParameterizedSingleTypeReference) reference, locator);
        } else if (reference instanceof ArrayTypeReference) {
            return new JMLArrayTypeReference((ArrayTypeReference) reference);
        } else if (reference instanceof CompletionOnSingleTypeReference) {
            return new JMLCompletionOnSingleTypeReference((CompletionOnSingleTypeReference) reference);
        } else if (reference instanceof SingleTypeReference) {
            return new JMLSingleTypeReference((SingleTypeReference) reference, locator);
        } else if (reference instanceof ArrayQualifiedTypeReference) {
            return new JMLArrayQualifiedTypeReference((ArrayQualifiedTypeReference) reference);
        } else if (reference instanceof QualifiedTypeReference) {
            return new JMLQualifiedTypeReference((QualifiedTypeReference) reference);
        } else {
            return reference;
        }
    }

    protected Expression convertExpression(Expression expression, Scope scope) {
        Expression result = expression;
        if (expression instanceof CompletionOnSingleNameReference) {
            result = new JMLCompletionOnSingleNameReference((CompletionOnSingleNameReference) expression);
        } else if (expression instanceof CompletionOnMessageSend) {
            result = new JMLCompletionOnMessageSend((CompletionOnMessageSend) expression);
        } else if (expression instanceof CompletionOnMemberAccess) {
            result = new JMLCompletionOnMemberAccess((CompletionOnMemberAccess) expression);
        } else if (expression instanceof CompletionOnQualifiedNameReference) {
            result = new JMLCompletionOnQualifiedNameReference((CompletionOnQualifiedNameReference) expression);
        } else if (expression instanceof CastExpression) {
            result = new JMLCastExpression((CastExpression) expression);
        } else if (expression instanceof EqualExpression) {
            final EqualExpression aae = (EqualExpression) expression;
            final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            result = new JMLEqualExpression(aae.left, aae.right, operator);
        } else if (expression instanceof AND_AND_Expression) {
            final AND_AND_Expression aae = (AND_AND_Expression) expression;
            final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            result = new JMLAndAndExpression(aae.left, aae.right, operator);
        } else if (expression instanceof OR_OR_Expression) {
            final OR_OR_Expression aae = (OR_OR_Expression) expression;
            final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            result = new JMLORORExpression(aae.left, aae.right, operator);
        } else if (expression instanceof CombinedBinaryExpression) {
            result = new JMLCombinedBinaryExpression((CombinedBinaryExpression) expression);
        } else if (expression instanceof BinaryExpression) {
            final BinaryExpression aae = (BinaryExpression) expression;
            final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            result = new JMLBinaryExpression(aae.left, aae.right, operator);
        } else if (expression instanceof PostfixExpression) {
            result = new JMLPostfixExpression((PostfixExpression) expression);
        } else if (expression instanceof PrefixExpression) {
            result = new JMLPrefixExpression((PrefixExpression) expression);
        } else if (expression instanceof CompoundAssignment) {
            final CompoundAssignment aae = (CompoundAssignment) expression;
            result = new JMLCompoundAssignment(aae.lhs, aae.expression, aae.operator, aae.sourceEnd);
        } else if (expression instanceof Assignment) {
            final Assignment aae = (Assignment) expression;
            result = new JMLAssignment(aae.lhs, aae.expression, aae.sourceEnd);
        } else if (expression instanceof UnaryExpression) {
            final UnaryExpression aae = (UnaryExpression) expression;
            final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            result = new JMLUnaryExpression(aae.expression, operator);
        } else if (expression instanceof SingleNameReference) {
            result = new JMLSingleNameReference((SingleNameReference) expression, locator);
        } else if (expression instanceof MessageSend) {
            result = new JMLMessageSend((MessageSend) expression);
        } else if (expression instanceof QualifiedNameReference) {
            result = new JMLQualifiedNameReference((QualifiedNameReference) expression, locator);
        } //        else if (expression instanceof FieldReference) {
        //            result = new JMLPropertyReference((FieldReference) expression);
        //        } 
        else if (expression instanceof ArrayInitializer) {
            result = new JMLArrayInitializer((ArrayInitializer) expression);
        } else if (expression instanceof ArrayAllocationExpression) {
            result = new JMLArrayAllocationExpression((ArrayAllocationExpression) expression);
        } else if (expression instanceof QualifiedAllocationExpression) {
            result = new JMLQualifiedAllocationExpression((QualifiedAllocationExpression) expression);
        } else if (expression instanceof AllocationExpression) {
            result = new JMLAllocationExpression((AllocationExpression) expression);
        } else if (expression instanceof ArrayReference) {
            result = new JMLArrayReference((ArrayReference) expression);
        } else if (expression instanceof ConditionalExpression) {
            result = new JMLConditionalExpression((ConditionalExpression) expression);
        } else if (expression instanceof FieldReference) {
            result = new JMLFieldReference((FieldReference) expression);
        } else if (expression instanceof InstanceOfExpression) {
            result = new JMLInstanceOfExpression((InstanceOfExpression) expression);
        } else if (expression instanceof TypeReference) {
            result = convertTypeReference((TypeReference) expression, locator);
        }
        return result;
    }

    @Override
    public void endVisit(AND_AND_Expression and_and_Expression, BlockScope scope) {
        if (and_and_Expression.left != null) {
            and_and_Expression.left = convertExpression(and_and_Expression.left, scope);
        }
        if (and_and_Expression.right != null) {
            and_and_Expression.right = convertExpression(and_and_Expression.right, scope);
        }
    }

    @Override
    public void endVisit(Assignment assignment, BlockScope scope) {
        if (assignment.lhs != null) {
            assignment.lhs = convertExpression(assignment.lhs, scope);
        }
        if (assignment.expression != null) {
            assignment.expression = convertExpression(assignment.expression, scope);
        }
    }

    @Override
    public void endVisit(BinaryExpression binaryExpression, BlockScope scope) {
        if (binaryExpression.left != null) {
            binaryExpression.left = convertExpression(binaryExpression.left, scope);
        }
        if (binaryExpression.right != null) {
            binaryExpression.right = convertExpression(binaryExpression.right, scope);
        }
        if (binaryExpression instanceof CombinedBinaryExpression) {
            final BinaryExpression[] table = ((CombinedBinaryExpression) binaryExpression).referencesTable;
            if (table != null) {
                for (int i = 0; i < table.length; i++) {
                    Expression conversionResult = convertExpression(table[i], scope);
                    if (conversionResult instanceof BinaryExpression) {
                        table[i] = (BinaryExpression) conversionResult;
                    }
                }
            }
        }
    }

    @Override
    public void endVisit(CompoundAssignment compoundAssignment, BlockScope scope) {
        if (compoundAssignment.lhs != null) {
            compoundAssignment.lhs = convertExpression(compoundAssignment.lhs, scope);
        }
        if (compoundAssignment.expression != null) {
            compoundAssignment.expression = convertExpression(compoundAssignment.expression, scope);
        }
    }

    @Override
    public void endVisit(EqualExpression equalExpression, BlockScope scope) {
        if (equalExpression.left != null) {
            equalExpression.left = convertExpression(equalExpression.left, scope);
        }
        if (equalExpression.right != null) {
            equalExpression.right = convertExpression(equalExpression.right, scope);
        }
    }

    @Override
    public void endVisit(OR_OR_Expression or_or_Expression, BlockScope scope) {
        if (or_or_Expression.left != null) {
            or_or_Expression.left = convertExpression(or_or_Expression.left, scope);
        }
        if (or_or_Expression.right != null) {
            or_or_Expression.right = convertExpression(or_or_Expression.right, scope);
        }
    }

    @Override
    public void endVisit(UnaryExpression unaryExpression, BlockScope scope) {
        if (unaryExpression.expression != null) {
            unaryExpression.expression = convertExpression(unaryExpression.expression, scope);
        }
    }

    @Override
    public void endVisit(LocalDeclaration localDeclaration, BlockScope scope) {
        if (localDeclaration.initialization != null) {
            localDeclaration.initialization = convertExpression(localDeclaration.initialization, scope);
        }
        if (localDeclaration.type != null) {
            localDeclaration.type = convertTypeReference(localDeclaration.type, locator);
        }
    }

    @Override
    public void endVisit(IfStatement ifStatement, BlockScope scope) {
        if (ifStatement.condition != null) {
            ifStatement.condition = convertExpression(ifStatement.condition, scope);
        }
        if (ifStatement.thenStatement != null) {
            ifStatement.thenStatement = convertStatement(ifStatement.thenStatement, scope);
        }
        if (ifStatement.elseStatement != null) {
            ifStatement.elseStatement = convertStatement(ifStatement.elseStatement, scope);
        }
    }

    @Override
    public void endVisit(CastExpression castExpression, BlockScope scope) {
        if (castExpression.expression != null) {
            castExpression.expression = convertExpression(castExpression.expression, scope);
        }
    }

    @Override
    public void endVisit(MessageSend messageSend, BlockScope scope) {
        if (messageSend.receiver != null) {
            messageSend.receiver = convertExpression(messageSend.receiver, scope);
        }
        if (messageSend.typeArguments != null) {
            for (int i = 0; i < messageSend.typeArguments.length; i++) {
                messageSend.typeArguments[i] = (TypeReference) convertExpression(messageSend.typeArguments[i], scope);
            }
        }
        if (messageSend.arguments != null) {
            for (int i = 0; i < messageSend.arguments.length; i++) {
                messageSend.arguments[i] = convertExpression(messageSend.arguments[i], scope);
            }
        }
    }

    @Override
    public void endVisit(Block block, BlockScope scope) {
        if (block.statements != null) {
            for (int i = 0; i < block.statements.length; i++) {
                block.statements[i] = convertToJML(block.statements[i], scope);
            }
        }
    }

    @Override
    public void endVisit(AssertStatement assertStatement, BlockScope scope) {
        if (assertStatement.assertExpression != null) {
            assertStatement.assertExpression = convertExpression(assertStatement.assertExpression, scope);
        }
        if (assertStatement.exceptionArgument != null) {
            assertStatement.exceptionArgument = convertExpression(assertStatement.exceptionArgument, scope);
        }
    }

    @Override
    public void endVisit(BreakStatement breakStatement, BlockScope scope) {
        if (breakStatement.subroutines != null) {
            for (int i = 0; i < breakStatement.subroutines.length; i++) {
                breakStatement.subroutines[i] = (SubRoutineStatement) convertStatement(breakStatement.subroutines[i], scope);
            }
        }
    }

    @Override
    public void endVisit(CaseStatement caseStatement, BlockScope scope) {
        if (caseStatement.constantExpression != null) {
            caseStatement.constantExpression = convertExpression(caseStatement.constantExpression, scope);
        }

    }

    @Override
    public void endVisit(ConditionalExpression conditionalExpression, BlockScope scope) {
        if (conditionalExpression.condition != null) {
            conditionalExpression.condition = convertExpression(conditionalExpression.condition, scope);
        }
        if (conditionalExpression.valueIfTrue != null) {
            conditionalExpression.valueIfTrue = convertExpression(conditionalExpression.valueIfTrue, scope);
        }
        if (conditionalExpression.valueIfFalse != null) {
            conditionalExpression.valueIfFalse = convertExpression(conditionalExpression.valueIfFalse, scope);
        }
    }

    @Override
    public void endVisit(ContinueStatement continueStatement, BlockScope scope) {
        if (continueStatement.subroutines != null) {
            for (int i = 0; i < continueStatement.subroutines.length; i++) {
                continueStatement.subroutines[i] = (SubRoutineStatement) convertStatement(continueStatement.subroutines[i], scope);
            }
        }
    }

    @Override
    public void endVisit(DoStatement doStatement, BlockScope scope) {
        if (doStatement.action != null) {
            doStatement.action = convertStatement(doStatement.action, scope);
        }
        if (doStatement.condition != null) {
            doStatement.condition = convertExpression(doStatement.condition, scope);
        }
    }

    @Override
    public void endVisit(FieldDeclaration fieldDeclaration, MethodScope scope) {
        if (fieldDeclaration.initialization != null) {
            fieldDeclaration.initialization = convertExpression(fieldDeclaration.initialization, scope);
        }
        if (fieldDeclaration.type != null) {
            fieldDeclaration.type = convertTypeReference(fieldDeclaration.type, locator);
        }
    }

    @Override
    public void endVisit(ForeachStatement forStatement, BlockScope scope) {
        if (forStatement.action != null) {
            forStatement.action = convertStatement(forStatement.action, scope);
        }
        if (forStatement.collection != null) {
            forStatement.collection = convertExpression(forStatement.collection, scope);
        }
        if (forStatement.elementVariable != null) {
            forStatement.elementVariable = (LocalDeclaration) convertStatement(forStatement.elementVariable, scope);
        }
    }

    @Override
    public void endVisit(ForStatement forStatement, BlockScope scope) {
        if (forStatement.action != null) {
            forStatement.action = convertStatement(forStatement.action, scope);
        }
        if (forStatement.condition != null) {
            forStatement.condition = convertExpression(forStatement.condition, scope);
        }
        if (forStatement.increments != null) {
            for (int i = 0; i < forStatement.increments.length; i++) {
                forStatement.increments[i] = convertStatement(forStatement.increments[i], scope);
            }
        }
        if (forStatement.initializations != null) {
            for (int i = 0; i < forStatement.initializations.length; i++) {
                forStatement.initializations[i] = convertStatement(forStatement.initializations[i], scope);
            }
        }
    }

    @Override
    public void endVisit(Initializer initializer, MethodScope scope) {
        if (initializer.block != null) {
            initializer.block = (Block) convertStatement(initializer.block, scope);
        }
    }

    @Override
    public void endVisit(InstanceOfExpression instanceOfExpression, BlockScope scope) {
        if (instanceOfExpression.expression != null) {
            instanceOfExpression.expression = convertExpression(instanceOfExpression.expression, scope);
        }
    }

    @Override
    public void endVisit(TryStatement tryStatement, BlockScope scope) {
        if (tryStatement.catchArguments != null) {
            for (int i = 0; i < tryStatement.catchArguments.length; i++) {
                tryStatement.catchArguments[i] = (Argument) convertStatement(tryStatement.catchArguments[i], scope);
            }
        }
        if (tryStatement.catchBlocks != null) {
            for (int i = 0; i < tryStatement.catchArguments.length; i++) {
                tryStatement.catchBlocks[i] = (Block) convertStatement(tryStatement.catchBlocks[i], scope);
            }
        }
        if (tryStatement.finallyBlock != null) {
            tryStatement.finallyBlock = (Block) convertStatement(tryStatement.finallyBlock, scope);
        }
        if (tryStatement.resources != null) {
            for (int i = 0; i < tryStatement.resources.length; i++) {
                tryStatement.resources[i] = (LocalDeclaration) convertStatement(tryStatement.resources[i], scope);
            }
        }
        if (tryStatement.tryBlock != null) {
            tryStatement.tryBlock = (Block) convertStatement(tryStatement.tryBlock, scope);
        }
    }

    @Override
    public void endVisit(SwitchStatement switchStatement, BlockScope scope) {
        if (switchStatement.expression != null) {
            switchStatement.expression = convertExpression(switchStatement.expression, scope);
        }
        if (switchStatement.statements != null) {
            for (int i = 0; i < switchStatement.statements.length; i++) {
                switchStatement.statements[i] = convertStatement(switchStatement.statements[i], scope);
            }
        }
    }

    @Override
    public void endVisit(ReturnStatement returnStatement, BlockScope scope) {
        if (returnStatement.expression != null) {
            returnStatement.expression = convertExpression(returnStatement.expression, scope);
        }
    }

    @Override
    public void endVisit(ArrayInitializer arrayInitializer, BlockScope scope) {
        if (arrayInitializer.expressions != null) {
            for (int i = 0; i < arrayInitializer.expressions.length; i++) {
                arrayInitializer.expressions[i] = convertExpression(arrayInitializer.expressions[i], scope);
            }
        }
    }

    @Override
    public void endVisit(ArrayAllocationExpression arrayAllocationExpression, BlockScope scope) {
        if (arrayAllocationExpression.type != null) {
            arrayAllocationExpression.type = (TypeReference) convertExpression(arrayAllocationExpression.type, scope);
        }
        if (arrayAllocationExpression.initializer != null) {
            arrayAllocationExpression.initializer = (ArrayInitializer) convertExpression(arrayAllocationExpression.initializer, scope);
        }
        if (arrayAllocationExpression.dimensions != null) {
            for (int i = 0; i < arrayAllocationExpression.dimensions.length; i++) {
                arrayAllocationExpression.dimensions[i] = convertExpression(arrayAllocationExpression.dimensions[i], scope);
            }
        }
    }

    @Override
    public void endVisit(QualifiedAllocationExpression qualifiedAllocationExpression, BlockScope scope) {
        if (qualifiedAllocationExpression.anonymousType != null) {
            qualifiedAllocationExpression.anonymousType = (TypeDeclaration) convertStatement(qualifiedAllocationExpression.anonymousType, scope);
        }
        if (qualifiedAllocationExpression.enclosingInstance != null) {
            qualifiedAllocationExpression.enclosingInstance = convertExpression(qualifiedAllocationExpression.enclosingInstance, scope);
        }
        if (qualifiedAllocationExpression.arguments != null) {
            for (int i = 0; i < qualifiedAllocationExpression.arguments.length; i++) {
                qualifiedAllocationExpression.arguments[i] = convertExpression(qualifiedAllocationExpression.arguments[i], scope);
            }
        }
        if (qualifiedAllocationExpression.typeArguments != null) {
            for (int i = 0; i < qualifiedAllocationExpression.typeArguments.length; i++) {
                qualifiedAllocationExpression.typeArguments[i] = (TypeReference) convertExpression(qualifiedAllocationExpression.typeArguments[i], scope);
            }
        }
    }

    @Override
    public void endVisit(AllocationExpression allocationExpression, BlockScope scope) {
        if (allocationExpression.arguments != null) {
            for (int i = 0; i < allocationExpression.arguments.length; i++) {
                allocationExpression.arguments[i] = convertExpression(allocationExpression.arguments[i], scope);
            }
        }
        if (allocationExpression.typeArguments != null) {
            for (int i = 0; i < allocationExpression.typeArguments.length; i++) {
                allocationExpression.typeArguments[i] = (TypeReference) convertExpression(allocationExpression.typeArguments[i], scope);
            }
        }
    }

    @Override
    public void endVisit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
        super.endVisit(constructorDeclaration, scope); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void endVisit(ExplicitConstructorCall explicitConstructor, BlockScope scope) {
        if (explicitConstructor.arguments != null) {
            for (int i = 0; i < explicitConstructor.arguments.length; i++) {
                explicitConstructor.arguments[i] = convertExpression(explicitConstructor.arguments[i], scope);
            }
        }
        if (explicitConstructor.typeArguments != null) {
            for (int i = 0; i < explicitConstructor.typeArguments.length; i++) {
                explicitConstructor.typeArguments[i] = (TypeReference) convertExpression(explicitConstructor.typeArguments[i], scope);
            }
        }
    }

    @Override
    public void endVisit(PostfixExpression postfixExpression, BlockScope scope) {
        if (postfixExpression.expression != null) {
            postfixExpression.expression = convertExpression(postfixExpression.expression, scope);
        }
        if (postfixExpression.lhs != null) {
            postfixExpression.lhs = convertExpression(postfixExpression.lhs, scope);
        }
    }

    @Override
    public void endVisit(PrefixExpression prefixExpression, BlockScope scope) {
        if (prefixExpression.expression != null) {
            prefixExpression.expression = convertExpression(prefixExpression.expression, scope);
        }
        if (prefixExpression.lhs != null) {
            prefixExpression.lhs = convertExpression(prefixExpression.lhs, scope);
        }
    }

    @Override
    public void endVisit(ArrayReference arrayReference, BlockScope scope) {
        if (arrayReference.receiver != null) {
            arrayReference.receiver = convertExpression(arrayReference.receiver, scope);
        }
        if (arrayReference.position != null) {
            arrayReference.position = convertExpression(arrayReference.position, scope);
        }
    }

    @Override
    public void endVisit(FieldReference fieldReference, BlockScope scope) {
        if (fieldReference.receiver != null) {
            fieldReference.receiver = convertExpression(fieldReference.receiver, scope);
        }
    }

    @Override
    public void endVisit(FieldReference fieldReference, ClassScope scope) {
        if (fieldReference.receiver != null) {
            fieldReference.receiver = convertExpression(fieldReference.receiver, scope);
        }
    }

    @Override
    public void endVisit(ThrowStatement throwStatement, BlockScope scope) {
        if (throwStatement.exception != null) {
            throwStatement.exception = convertExpression(throwStatement.exception, scope);
        }
    }

    @Override
    public void endVisit(WhileStatement whileStatement, BlockScope scope) {
        if (whileStatement.condition != null) {
            whileStatement.condition = convertExpression(whileStatement.condition, scope);
        }
        if (whileStatement.action != null) {
            whileStatement.action = convertStatement(whileStatement.action, scope);
        }
    }

    @Override
    public void endVisit(LabeledStatement labeledStatement, BlockScope scope) {
        if (labeledStatement.statement != null) {
            labeledStatement.statement = convertStatement(labeledStatement.statement, scope);
        }
    }

    @Override
    public void endVisit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
        if (!(localTypeDeclaration instanceof AdsTypeDeclaration)) {//Otherwise, the type has already been converted.
            convertFields(localTypeDeclaration.fields, scope);
            if (localTypeDeclaration.methods != null) {
                for (AbstractMethodDeclaration methodDecl : localTypeDeclaration.methods) {
                    if (!methodDecl.isClinit()) {
                        convertMethod(methodDecl, methodDecl.scope);
                    }
                }
            }
        }
    }

    @Override
    public void endVisit(SynchronizedStatement synchronizedStatement, BlockScope scope) {
        if (synchronizedStatement.block != null) {
            synchronizedStatement.block = (Block) convertStatement(synchronizedStatement.block, scope);
        }
    }
    
    
}
