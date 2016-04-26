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

package org.radixware.kernel.common.compiler.lookup;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AdsTextualConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTextualMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.AnnotationMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.parser.RecoveredType;
import static org.eclipse.jdt.internal.compiler.parser.TerminalTokens.TokenNameDOT;
import static org.eclipse.jdt.internal.compiler.parser.TerminalTokens.TokenNamenew;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;


class SourceParser extends Parser {

    private RadixObjectLocator locator;

    public SourceParser(ProblemReporter problemReporter, boolean optimizeStringLiterals, RadixObjectLocator locator) {
        super(problemReporter, optimizeStringLiterals);
        this.locator = locator;
    }

    public CompilationUnitDeclaration parse(AdsCompilationUnitDeclaration declaration, char[] source) {
        CompilationUnitDeclaration unit;
        try {
            /* automaton initialization */
            initialize(true);
            goForCompilationUnit();
            /* unit creation */
            this.referenceContext = this.compilationUnit = declaration;
            /* scanners initialization */
            char[] contents = source;
            this.scanner.setSource(contents);
            this.compilationUnit.sourceEnd = this.scanner.source.length - 1;
            if (this.javadocParser != null && this.javadocParser.checkDocComment) {
                this.javadocParser.scanner.setSource(contents);
            }
            /* run automaton */
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

    @Override
    protected void consumeMethodHeaderName(boolean isAnnotationMethod) {
        // MethodHeaderName ::= Modifiersopt Type 'Identifier' '('
        // AnnotationMethodHeaderName ::= Modifiersopt Type 'Identifier' '('
        // RecoveryMethodHeaderName ::= Modifiersopt Type 'Identifier' '('
        MethodDeclaration md = null;
        if (isAnnotationMethod) {
            md = new AnnotationMethodDeclaration(this.compilationUnit.compilationResult);
            this.recordStringLiterals = false;
        } else {
            md = new AdsTextualMethodDeclaration(this.compilationUnit.compilationResult, locator);
        }

        //name
        md.selector = this.identifierStack[this.identifierPtr];
        long selectorSource = this.identifierPositionStack[this.identifierPtr--];
        this.identifierLengthPtr--;
        //type
        md.returnType = getTypeReference(this.intStack[this.intPtr--]);
        //modifiers
        md.declarationSourceStart = this.intStack[this.intPtr--];
        md.modifiers = this.intStack[this.intPtr--];
        // consume annotations
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(
                    this.expressionStack,
                    (this.expressionPtr -= length) + 1,
                    md.annotations = new Annotation[length],
                    0,
                    length);
        }
        // javadoc
        md.javadoc = this.javadoc;
        this.javadoc = null;

        //highlight starts at selector start
        md.sourceStart = (int) (selectorSource >>> 32);
        pushOnAstStack(md);
        md.sourceEnd = this.lParenPos;
        md.bodyStart = this.lParenPos + 1;
        this.listLength = 0; // initialize this.listLength before reading parameters/throws

        // recovery
        if (this.currentElement != null) {
            if (this.currentElement instanceof RecoveredType
                    //|| md.modifiers != 0
                    || (Util.getLineNumber(md.returnType.sourceStart, this.scanner.lineEnds, 0, this.scanner.linePtr)
                    == Util.getLineNumber(md.sourceStart, this.scanner.lineEnds, 0, this.scanner.linePtr))) {
                this.lastCheckPoint = md.bodyStart;
                this.currentElement = this.currentElement.add(md, 0);
                this.lastIgnoredToken = -1;
            } else {
                this.lastCheckPoint = md.sourceStart;
                this.restartRecovery = true;
            }
        }
    }

    @Override
    protected void consumeMethodHeaderNameWithTypeParameters(boolean isAnnotationMethod) {
        // MethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
        // AnnotationMethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
        // RecoveryMethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
        MethodDeclaration md = null;
        if (isAnnotationMethod) {
            md = new AnnotationMethodDeclaration(this.compilationUnit.compilationResult);
            this.recordStringLiterals = false;
        } else {
            md = new AdsTextualMethodDeclaration(this.compilationUnit.compilationResult, locator);
        }

        //name
        md.selector = this.identifierStack[this.identifierPtr];
        long selectorSource = this.identifierPositionStack[this.identifierPtr--];
        this.identifierLengthPtr--;
        //type
        md.returnType = getTypeReference(this.intStack[this.intPtr--]);

        // consume type parameters
        int length = this.genericsLengthStack[this.genericsLengthPtr--];
        this.genericsPtr -= length;
        System.arraycopy(this.genericsStack, this.genericsPtr + 1, md.typeParameters = new TypeParameter[length], 0, length);

        //modifiers
        md.declarationSourceStart = this.intStack[this.intPtr--];
        md.modifiers = this.intStack[this.intPtr--];
        // consume annotations
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(
                    this.expressionStack,
                    (this.expressionPtr -= length) + 1,
                    md.annotations = new Annotation[length],
                    0,
                    length);
        }
        // javadoc
        md.javadoc = this.javadoc;
        this.javadoc = null;

        //highlight starts at selector start
        md.sourceStart = (int) (selectorSource >>> 32);
        pushOnAstStack(md);
        md.sourceEnd = this.lParenPos;
        md.bodyStart = this.lParenPos + 1;
        this.listLength = 0; // initialize this.listLength before reading parameters/throws

        // recovery
        if (this.currentElement != null) {
            boolean isType;
            if ((isType = this.currentElement instanceof RecoveredType)
                    //|| md.modifiers != 0
                    || (Util.getLineNumber(md.returnType.sourceStart, this.scanner.lineEnds, 0, this.scanner.linePtr)
                    == Util.getLineNumber(md.sourceStart, this.scanner.lineEnds, 0, this.scanner.linePtr))) {
                if (isType) {
                    ((RecoveredType) this.currentElement).pendingTypeParameters = null;
                }
                this.lastCheckPoint = md.bodyStart;
                this.currentElement = this.currentElement.add(md, 0);
                this.lastIgnoredToken = -1;
            } else {
                this.lastCheckPoint = md.sourceStart;
                this.restartRecovery = true;
            }
        }
    }

    @Override
    public MethodDeclaration convertToMethodDeclaration(ConstructorDeclaration c, CompilationResult compilationResult) {
        MethodDeclaration m = new AdsTextualMethodDeclaration(compilationResult, locator);
        m.typeParameters = c.typeParameters;
        m.sourceStart = c.sourceStart;
        m.sourceEnd = c.sourceEnd;
        m.bodyStart = c.bodyStart;
        m.bodyEnd = c.bodyEnd;
        m.declarationSourceEnd = c.declarationSourceEnd;
        m.declarationSourceStart = c.declarationSourceStart;
        m.selector = c.selector;
        m.statements = c.statements;
        m.modifiers = c.modifiers;
        m.annotations = c.annotations;
        m.arguments = c.arguments;
        m.thrownExceptions = c.thrownExceptions;
        m.explicitDeclarations = c.explicitDeclarations;
        m.returnType = null;
        m.javadoc = c.javadoc;
        return m;
    }

    @Override
    protected void consumeConstructorHeaderName() {

        /* recovering - might be an empty message send */
        if (this.currentElement != null) {
            if (this.lastIgnoredToken == TokenNamenew) { // was an allocation expression
                this.lastCheckPoint = this.scanner.startPosition; // force to restart at this exact position
                this.restartRecovery = true;
                return;
            }
        }

        // ConstructorHeaderName ::=  Modifiersopt 'Identifier' '('
        ConstructorDeclaration cd = new AdsTextualConstructorDeclaration(this.compilationUnit.compilationResult, locator);

        //name -- this is not really revelant but we do .....
        cd.selector = this.identifierStack[this.identifierPtr];
        long selectorSource = this.identifierPositionStack[this.identifierPtr--];
        this.identifierLengthPtr--;

        //modifiers
        cd.declarationSourceStart = this.intStack[this.intPtr--];
        cd.modifiers = this.intStack[this.intPtr--];
        // consume annotations
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(
                    this.expressionStack,
                    (this.expressionPtr -= length) + 1,
                    cd.annotations = new Annotation[length],
                    0,
                    length);
        }
        // javadoc
        cd.javadoc = this.javadoc;
        this.javadoc = null;

        //highlight starts at the selector starts
        cd.sourceStart = (int) (selectorSource >>> 32);
        pushOnAstStack(cd);
        cd.sourceEnd = this.lParenPos;
        cd.bodyStart = this.lParenPos + 1;
        this.listLength = 0; // initialize this.listLength before reading parameters/throws

        // recovery
        if (this.currentElement != null) {
            this.lastCheckPoint = cd.bodyStart;
            if ((this.currentElement instanceof RecoveredType && this.lastIgnoredToken != TokenNameDOT)
                    || cd.modifiers != 0) {
                this.currentElement = this.currentElement.add(cd, 0);
                this.lastIgnoredToken = -1;
            }
        }
    }

    @Override
    protected void consumeConstructorHeaderNameWithTypeParameters() {

        /* recovering - might be an empty message send */
        if (this.currentElement != null) {
            if (this.lastIgnoredToken == TokenNamenew) { // was an allocation expression
                this.lastCheckPoint = this.scanner.startPosition; // force to restart at this exact position
                this.restartRecovery = true;
                return;
            }
        }

        // ConstructorHeaderName ::=  Modifiersopt TypeParameters 'Identifier' '('
        ConstructorDeclaration cd = new AdsTextualConstructorDeclaration(this.compilationUnit.compilationResult, locator);

        //name -- this is not really revelant but we do .....
        cd.selector = this.identifierStack[this.identifierPtr];
        long selectorSource = this.identifierPositionStack[this.identifierPtr--];
        this.identifierLengthPtr--;

        // consume type parameters
        int length = this.genericsLengthStack[this.genericsLengthPtr--];
        this.genericsPtr -= length;
        System.arraycopy(this.genericsStack, this.genericsPtr + 1, cd.typeParameters = new TypeParameter[length], 0, length);

        //modifiers
        cd.declarationSourceStart = this.intStack[this.intPtr--];
        cd.modifiers = this.intStack[this.intPtr--];
        // consume annotations
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(
                    this.expressionStack,
                    (this.expressionPtr -= length) + 1,
                    cd.annotations = new Annotation[length],
                    0,
                    length);
        }
        // javadoc
        cd.javadoc = this.javadoc;
        this.javadoc = null;

        //highlight starts at the selector starts
        cd.sourceStart = (int) (selectorSource >>> 32);
        pushOnAstStack(cd);
        cd.sourceEnd = this.lParenPos;
        cd.bodyStart = this.lParenPos + 1;
        this.listLength = 0; // initialize this.listLength before reading parameters/throws

        // recovery
        if (this.currentElement != null) {
            this.lastCheckPoint = cd.bodyStart;
            if ((this.currentElement instanceof RecoveredType && this.lastIgnoredToken != TokenNameDOT)
                    || cd.modifiers != 0) {
                this.currentElement = this.currentElement.add(cd, 0);
                this.lastIgnoredToken = -1;
            }
        }
    }
}
