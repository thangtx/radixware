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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.UndocumentedEmptyBlock;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.radixware.kernel.common.compiler.core.ast.IJMLExpression;
import org.radixware.kernel.common.compiler.core.ast.Java2JmlConverter;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;


public class AdsTextualConstructorDeclaration extends ConstructorDeclaration {

    private static final boolean DEBUG = true;
    private final RadixObjectLocator locator;

    public AdsTextualConstructorDeclaration(CompilationResult compilationResult, RadixObjectLocator locator) {
        super(compilationResult);
        this.locator = locator;
    }

    @Override
    public void resolve(ClassScope scope) {
        /*if (DEBUG) {
         System.out.println("-----------------------  ORIGINAL  -------------------------");
         StringBuffer buffer = new StringBuffer();
         this.print(5, buffer);
         System.out.println(buffer);
         }
         Java2JmlConverter java2Jml = new Java2JmlConverter();
         java2Jml.convertToJML(statements, this.scope);
         super.resolve(scope);
         if (DEBUG) {
         System.out.println("-----------------------  CONVERTED TO JML -------------------------");
         StringBuffer buffer = new StringBuffer();
         this.print(5, buffer);
         System.out.println(buffer);
         System.out.println("------------------------------------------------------------");
         }
         JML2JavaConverter jml2Java = new JML2JavaConverter();
         this.scope.locals = new LocalVariableBinding[5];
         this.scope.localIndex = 0;
         this.scope.analysisIndex = 0;
         this.scope.subscopes = new Scope[1];
         this.scope.subscopeCount = 0;
        
         jml2Java.convertToJava(statements, this.scope);


         super.resolve(scope);
         if (DEBUG) {
         System.out.println("-----------------------  RESTORED FROM JML -------------------------");
         StringBuffer buffer = new StringBuffer();
         this.print(5, buffer);
         System.out.println(buffer);
         System.out.println("------------------------------------------------------------");
         }*/

//        if (DEBUG) {
//            System.out.println("-----------------------  ORIGINAL  -------------------------");
//            StringBuffer buffer = new StringBuffer();
//            this.print(5, buffer);
//            System.out.println(buffer);
//        }
//        final Java2JmlConverter converter = new Java2JmlConverter(locator);
//        if (this.constructorCall != null) {
//            this.constructorCall = (ExplicitConstructorCall) converter.convertToJML(this.constructorCall, this.scope);
//        }
//        if (this.thrownExceptions != null) {
//            converter.convertToJML(this.thrownExceptions, this.scope);
//        }
//        if (this.annotations != null) {
//            converter.convertToJML(this.annotations, this.scope);
//        }
//        if (this.arguments != null) {
//            converter.convertToJML(this.arguments, this.scope);
//        }
//        if (this.typeParameters != null) {
//            converter.convertToJML(this.typeParameters, this.scope);
//        }
//        converter.convertToJML(statements, this.scope);


        super.resolve(scope);
//        if (DEBUG) {
//            System.out.println("-----------------------  RESTORED FROM JML -------------------------");
//            StringBuffer buffer = new StringBuffer();
//            this.print(5, buffer);
//            System.out.println(buffer);
//            System.out.println("------------------------------------------------------------");
//        }

    }

    @Override
    public void resolveStatements() {
        SourceTypeBinding sourceType = this.scope.enclosingSourceType();
        if (!CharOperation.equals(sourceType.sourceName, this.selector)) {
            this.scope.problemReporter().missingReturnType(this);
        }
        if (this.typeParameters != null) {
            for (int i = 0, length = this.typeParameters.length; i < length; i++) {
                this.typeParameters[i].resolve(this.scope);
            }
        }
        if (this.binding != null && !this.binding.isPrivate()) {
            sourceType.tagBits |= TagBits.HasNonPrivateConstructor;
        }
        // if null ==> an error has occurs at parsing time ....
        if (this.constructorCall != null) {
            if (sourceType.id == TypeIds.T_JavaLangObject
                    && this.constructorCall.accessMode != ExplicitConstructorCall.This) {
                // cannot use super() in java.lang.Object
                if (this.constructorCall.accessMode == ExplicitConstructorCall.Super) {
                    this.scope.problemReporter().cannotUseSuperInJavaLangObject(this.constructorCall);
                }
                this.constructorCall = null;
            } else {
                this.constructorCall.resolve(this.scope);
            }
        }
        if ((this.modifiers & ExtraCompilerModifiers.AccSemicolonBody) != 0) {
            this.scope.problemReporter().methodNeedBody(this);
        }



        if (this.statements != null) {
            for (int i = 0, length = this.statements.length; i < length; i++) {
                this.statements[i].resolve(this.scope);
                if (this.statements[i] instanceof IJMLExpression) {
                    Expression subst = ((IJMLExpression) this.statements[i]).getSubstitution(scope);
                    if (subst != null) {
                        this.statements[i] = subst;
                    }
                }
            }
        } else if ((this.bits & UndocumentedEmptyBlock) != 0) {
            if (!this.isConstructor() || this.arguments != null) { // https://bugs.eclipse.org/bugs/show_bug.cgi?id=319626
                this.scope.problemReporter().undocumentedEmptyBlock(this.bodyStart - 1, this.bodyEnd + 1);
            }
        }
    }
}
