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

package org.radixware.kernel.common.compiler.core.completion;

import org.eclipse.jdt.internal.codeassist.complete.CompletionParser;
import org.eclipse.jdt.internal.codeassist.complete.CompletionScanner;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;


public class AdsCompletionParser extends CompletionParser {

    public AdsCompletionParser(ProblemReporter problemReporter, boolean storeExtraSourceEnds) {
        super(problemReporter, storeExtraSourceEnds);
    }

    public CompilationUnitDeclaration parse(AdsCompilationUnitDeclaration unitDecl, char[] source, int cursorLoc) {
        this.cursorLocation = cursorLoc;
        final CompletionScanner completionScanner = (CompletionScanner) this.scanner;
        completionScanner.completionIdentifier = null;
        completionScanner.cursorLocation = cursorLoc;
        return this.parse(unitDecl, source);
    }

    public CompilationUnitDeclaration dietParse(AdsCompilationUnitDeclaration unitDecl, char[] source, int cursorLoc) {
        final boolean old = this.diet;
        this.diet = true;
        try {
            return parse(unitDecl, source, cursorLoc);
        } finally {
            this.diet = old;
        }
    }

    private CompilationUnitDeclaration parse(AdsCompilationUnitDeclaration declaration, char[] source) {
        CompilationUnitDeclaration unit;
        try {
            /* automaton initialization */
            initialize(true);
            goForCompilationUnit();
            /* unit creation */
            this.referenceContext = this.compilationUnit = declaration;
            /* scanners initialization */
            final char[] contents = source;
            this.scanner.setSource(contents);
            this.compilationUnit.sourceEnd = this.scanner.source.length - 1;
            if (this.javadocParser != null && this.javadocParser.checkDocComment) {
                this.javadocParser.scanner.setSource(contents);
            }
            parse();
        } finally {
            unit = this.compilationUnit;
            this.compilationUnit = null; // reset parser
            // tag unit has having read bodies
            if (!this.diet) {
                unit.bits |= ASTNode.HasAllMethodBodies;
            }
        }
        return unit;
    }
    
}
