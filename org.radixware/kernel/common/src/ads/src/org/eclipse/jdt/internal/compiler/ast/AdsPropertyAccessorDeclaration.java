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
import org.eclipse.jdt.internal.compiler.ads.syntetics.AdsPropertyHelper;
import org.eclipse.jdt.internal.compiler.ads.syntetics.PropertyGeneratorFlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;


public class AdsPropertyAccessorDeclaration extends MethodDeclaration {

    private AdsPropertyDeclaration declaration;
    private Definition contextDefinition;
    private final PropertyGeneratorFlowInfo flowInfo;
    private final boolean isHidden;

    public AdsPropertyAccessorDeclaration(Definition contextDefinition, AdsPropertyDeclaration propDecl, CompilationResult compilationResult, PropertyGeneratorFlowInfo flowInfo) {
        this(contextDefinition, propDecl, compilationResult, flowInfo, false);
    }

    public AdsPropertyAccessorDeclaration(Definition contextDefinition, AdsPropertyDeclaration propDecl, CompilationResult compilationResult, PropertyGeneratorFlowInfo flowInfo, boolean isHidden) {
        super(compilationResult);
        this.isHidden = isHidden;
        this.flowInfo = flowInfo;
        this.contextDefinition = contextDefinition;
        this.declaration = propDecl;
        String accessorSelector = ((flowInfo.isGetter() ? "get" : "set") + propDecl.getProperty().getId());
        if (isHidden) {
            accessorSelector += "$$$";
        }
        this.selector = accessorSelector.toCharArray();
        this.modifiers = propDecl.modifiers;
        this.returnType = flowInfo.isGetter() ? propDecl.type : TypeReference.baseTypeReference(TypeIds.T_void, 0);
        if (!flowInfo.isGetter()) {
            this.arguments = new Argument[]{new Argument("var".toCharArray(), 0, propDecl.type, 0)};
        }
    }

    public MethodDeclaration getHiddenAccessorDeclaration() {
        if (!isHidden && flowInfo.isWriteHidden()) {
            return new AdsPropertyAccessorDeclaration(contextDefinition, declaration, compilationResult, flowInfo, true);
        } else {
            return null;
        }
    }

    @Override
    public void resolveStatements() {
        if (statements == null) {
            if (flowInfo.isGetter()) {
                if (isHidden) {
                    statements = AdsPropertyHelper.createGetterBody(contextDefinition, declaration, compilationResult, this, (AdsProblemReporter) scope.problemReporter());
                } else {
                    statements = AdsPropertyHelper.createVisibleGetterBody(this.scope.compilationUnitScope(), contextDefinition, flowInfo, declaration, compilationResult, this, (AdsProblemReporter) scope.problemReporter());
                }
            } else {
                statements = new Statement[0];//TODO:
            }
        }
        super.resolveStatements(); //To change body of generated methods, choose Tools | Templates.
    }
}
