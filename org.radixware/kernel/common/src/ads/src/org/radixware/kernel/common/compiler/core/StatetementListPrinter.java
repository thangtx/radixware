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

package org.radixware.kernel.common.compiler.core;

import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedSuperReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedThisReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;


public class StatetementListPrinter {

    public static void printStatements(Statement[] statements, BlockScope scope, StringBuilder output) {
        if (statements != null) {
            for (int i = 0; i < statements.length; i++) {
                statements[i].traverse(new StatementVisitor(output), scope);
                if (statements[i] instanceof Expression) {
                    output.append(";\n");
                }

            }
        }
    }

    private static class StatementVisitor extends ADSASTVisitor {

        private final StringBuilder content;
        private int dept;
        private boolean wasEol = false;

        private void printIndent() {
            for (int i = 0; i < dept; i++) {
                content.append(' ');
                content.append(' ');
                content.append(' ');
                content.append(' ');
            }
        }

        private void print(char c) {
            if (wasEol) {
                printIndent();
            }
            content.append(c);
            if (c == '\n') {
                wasEol = true;
            } else {
                wasEol = false;
            }
        }

        private void println(String string) {
            print(string);
            print('\n');
        }

        private void println(char c) {
            print(c);
            print('\n');
        }

        private void println() {
            print('\n');
        }

        private void print(String string) {
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                print(c);
            }
        }

        private void print(char[] string) {
            for (int i = 0; i < string.length; i++) {
                char c = string[i];
                print(c);
            }
        }

        public StatementVisitor(StringBuilder content) {
            this.content = content;
        }

        @Override
        public boolean visit(Block block, BlockScope scope) {
            println('{');
            dept++;
            if (block.statements != null && block.statements.length > 0) {
                for (int i = 0; i < block.statements.length; i++) {
                    block.statements[i].traverse(this, scope);
                    println(';');
                }
            } else {
                println();
            }
            dept--;
            println('}');
            return false;
        }

        @Override
        public boolean visit(IfStatement ifStatement, BlockScope scope) {
            print("if(");
            if (ifStatement.condition != null) {
                ifStatement.condition.traverse(this, scope);
            }
            print(")");
            if (ifStatement.thenStatement != null) {
                ifStatement.thenStatement.traverse(this, scope);
            }
            if (ifStatement.elseStatement != null) {
                print("else");
                ifStatement.elseStatement.traverse(this, scope);
            }
            return false;
        }

        @Override
        public boolean visit(AND_AND_Expression and_and_Expression, BlockScope scope) {
            return processBinaryExpression(and_and_Expression, scope);
        }

        @Override
        public boolean visit(BinaryExpression binaryExpression, BlockScope scope) {
            return processBinaryExpression(binaryExpression, scope);
        }

        @Override
        public boolean visit(ForeachStatement forStatement, BlockScope scope) {
            print("for(");
            if (forStatement.elementVariable != null) {
                forStatement.elementVariable.traverse(this, scope);
            }
            print(" : ");
            if (forStatement.collection != null) {
                forStatement.collection.traverse(this, scope);
            }
            print(")");
            if (forStatement.action != null) {
                forStatement.action.traverse(this, scope);
            }
            return false;
        }

        @Override
        public boolean visit(OR_OR_Expression or_or_Expression, BlockScope scope) {
            return processBinaryExpression(or_or_Expression, scope);
        }

        @Override
        public boolean visit(EqualExpression eq_Expression, BlockScope scope) {
            return processBinaryExpression(eq_Expression, scope);
        }

        @Override
        public boolean visit(QualifiedNameReference qualifiedNameReference, BlockScope scope) {
            printBinding(qualifiedNameReference.binding);
            if (qualifiedNameReference.otherBindings != null) {
                for (int i = 0; i < qualifiedNameReference.otherBindings.length; i++) {
                    print('.');
                    printBinding(qualifiedNameReference.otherBindings[i]);
                }
            }
            return false;
        }

        @Override
        public boolean visit(FieldReference fieldReference, BlockScope scope) {
            if (fieldReference.receiver != null) {
                fieldReference.receiver.traverse(this, scope);
            }
            printBinding(fieldReference.binding);
            return false;
        }

        @Override
        public boolean visit(FalseLiteral falseLiteral, BlockScope scope) {
            print("false");
            return false;
        }

        @Override
        public boolean visit(NullLiteral nullLiteral, BlockScope scope) {
            print("null");
            return false;
        }

        @Override
        public boolean visit(QualifiedSuperReference qualifiedSuperReference, BlockScope scope) {
            qualifiedSuperReference.qualification.traverse(this, scope);
            print('.');
            print("super");
            return false;
        }

        @Override
        public boolean visit(QualifiedThisReference qualifiedThisReference, BlockScope scope) {
            qualifiedThisReference.qualification.traverse(this, scope);
            print('.');
            print("this");
            return false;
        }

        @Override
        public boolean visit(ReturnStatement returnStatement, BlockScope scope) {
            print("return");
            if (returnStatement.expression != null) {
                print(' ');
                returnStatement.expression.traverse(this, scope);
            }
            println(';');
            return false;
        }

        @Override
        public boolean visit(ThisReference thisReference, BlockScope scope) {
            if (thisReference.isImplicitThis()) {
                return false;
            }
            print("this");
            return false;
        }

        @Override
        public boolean visit(TrueLiteral trueLiteral, BlockScope scope) {
            print("true");
            return false;
        }

        @Override
        public boolean visit(MessageSend messageSend, BlockScope scope) {
            if (messageSend.receiver != null) {
                messageSend.receiver.traverse(this, scope);
            }
            if (messageSend.receiver instanceof ThisReference && ((ThisReference) messageSend.receiver).isImplicitThis()) {
            } else {
                print('.');
            }
            print(messageSend.binding.selector);
            print('(');
            if (messageSend.arguments != null) {
                for (int i = 0; i < messageSend.arguments.length; i++) {
                    if (i > 0) {
                        print(',');
                    }
                    messageSend.arguments[i].traverse(this, scope);
                }
            }
            print(')');
            return false;
        }

        @Override
        public boolean visit(QualifiedTypeReference qualifiedTypeReference, BlockScope scope) {
            if (qualifiedTypeReference.resolvedType != null) {
                printType(qualifiedTypeReference.resolvedType);
            }
            return false;
        }

        @Override
        public boolean visit(SingleNameReference singleNameReference, BlockScope scope) {
            if (singleNameReference.binding != null) {
                printBinding(singleNameReference.binding);
            }
            return false;
        }

        @Override
        public boolean visit(SingleTypeReference singleTypeReference, BlockScope scope) {
            if (singleTypeReference.resolvedType != null) {
                printType(singleTypeReference.resolvedType);
            }
            return false;
        }

        @Override
        public boolean visit(AdsTypeReference adsTypeReference, BlockScope scope) {
            if (adsTypeReference.resolvedType != null) {
                printType(adsTypeReference.resolvedType);
            }
            return false;
        }

        @Override
        public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {
            localDeclaration.type.traverse(this, scope);
            print(' ');
            print(localDeclaration.name);
            if (localDeclaration.initialization != null) {
                print(" = ");
                localDeclaration.initialization.traverse(this, scope);
            }
            return false;
        }

        private boolean processBinaryExpression(BinaryExpression exp, BlockScope scope) {
            if (exp.left != null) {
                exp.left.traverse(this, scope);
            }
            print(" " + exp.operatorToString() + " ");
            if (exp.right != null) {
                exp.right.traverse(this, scope);
            }
            return false;
        }

        private void printBinding(Binding binding) {
            if (binding instanceof FieldBinding) {
                FieldBinding field = (FieldBinding) binding;
                if (field.isStatic()) {
                    printType(field.declaringClass);
                    print('.');
                }
                print(field.name);
            } else if (binding instanceof VariableBinding) {
                print(((VariableBinding) binding).name);
            }
        }

        private void printType(TypeBinding binding) {
            print(binding.qualifiedSourceName());
        }
    }
}
