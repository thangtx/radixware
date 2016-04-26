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

import org.eclipse.jdt.internal.compiler.CompilationResult;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.UndocumentedEmptyBlock;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.radixware.kernel.common.compiler.core.ast.IJMLExpression;

import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;

public class AdsTextualMethodDeclaration extends MethodDeclaration {

    private static final boolean DEBUG = false;
    private final RadixObjectLocator locator;

    public AdsTextualMethodDeclaration(CompilationResult compilationResult, RadixObjectLocator locator) {
        super(compilationResult);
        this.locator = locator;
    }

    @Override
    public void resolve(ClassScope scope) {
        super.resolve(scope);
    }

    public void resolveStatements() {
        if (DEBUG) {
            System.out.println("-----------------------  ORIGINAL  -------------------------");
            StringBuffer buffer = new StringBuffer();
            this.print(5, buffer);
            System.out.println(buffer);
        }
        if (this.statements != null) {
            for (int i = 0, length = this.statements.length; i < length; i++) {
                this.statements[i].resolve(this.scope);
                if (this.statements[i] instanceof IJMLExpression) {
                    final Expression subst = ((IJMLExpression) this.statements[i]).getSubstitution(scope);
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
        if (DEBUG) {
            System.out.println("-----------------------  CONVERTED  -------------------------");
            StringBuffer buffer = new StringBuffer();
            this.print(5, buffer);
            System.out.println(buffer);
        }
    }
}