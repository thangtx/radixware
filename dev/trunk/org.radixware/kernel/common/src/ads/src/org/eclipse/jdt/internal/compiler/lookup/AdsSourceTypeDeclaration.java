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
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.radixware.kernel.common.compiler.core.ast.JMLQualifiedAllocationExpression;
import org.radixware.kernel.common.compiler.core.ast.Java2JmlConverter;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsSourceTypeDeclaration extends AdsTypeDeclaration {

    public AdsSourceTypeDeclaration(AdsTypeDeclaration enclosingType, TypeDeclaration src) {
        super(enclosingType);
        init(src, (AdsCompilationUnitScope) enclosingType.scope.compilationUnitScope());
    }

    public AdsSourceTypeDeclaration(AdsCompilationUnitScope scope, AdsTypeDeclaration enclosingType, TypeDeclaration src) {
        super(scope, enclosingType);
        init(src, scope);
    }

    public AdsSourceTypeDeclaration(JMLQualifiedAllocationExpression alloc, Scope scope, TypeDeclaration src) {
        super(alloc, scope);
        init(src, (AdsCompilationUnitScope) scope.compilationUnitScope());
    }

    public AdsSourceTypeDeclaration(AdsCompilationUnitScope scope, Definition definition, ERuntimeEnvironmentType env, TypeDeclaration src, RadixObjectLocator locator) {
        super(scope, definition, env, src.compilationResult, locator);
        init(src, scope);
    }

    private void init(TypeDeclaration src, AdsCompilationUnitScope compilationUnitScope) {
        this.modifiers = src.modifiers;
        this.modifiersSourceStart = src.modifiersSourceStart;
        this.annotations = src.annotations;
        //this.headerStart = src.headerStart;
        this.name = src.name;
        this.superclass = Java2JmlConverter.convertTypeReference(src.superclass, locator);
        if (src.superInterfaces != null) {
            this.superInterfaces = new TypeReference[src.superInterfaces.length];
            for (int i = 0; i < src.superInterfaces.length; i++) {
                this.superInterfaces[i] = Java2JmlConverter.convertTypeReference(src.superInterfaces[i], locator);
            }
        }
        this.fields = src.fields;
        this.methods = src.methods;
        if (src.memberTypes != null) {
            this.memberTypes = new TypeDeclaration[src.memberTypes.length];
            for (int i = 0; i < this.memberTypes.length; i++) {
                this.memberTypes[i] = new AdsSourceTypeDeclaration(compilationUnitScope, this, src.memberTypes[i]);
            }
        }

        this.binding = src.binding;
        this.scope = src.scope;
        this.initializerScope = src.initializerScope;
        this.staticInitializerScope = src.staticInitializerScope;
        this.ignoreFurtherInvestigation = src.ignoreFurtherInvestigation;
        this.maxFieldCount = src.maxFieldCount;
        this.declarationSourceStart = src.declarationSourceStart;
        this.declarationSourceEnd = src.declarationSourceEnd;
        this.bodyEnd = src.bodyEnd; // doesn't include the trailing comment if any.
        this.bits = src.bits;
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.missingAbstractMethods = src.missingAbstractMethods;
        this.javadoc = src.javadoc;

        this.allocation = src.allocation; // for anonymous only
        this.enclosingType = src.enclosingType;

        this.enumValuesSyntheticfield = src.enumValuesSyntheticfield;
        this.enumConstantsCounter = src.enumConstantsCounter;

        // 1.5 support
        this.typeParameters = src.typeParameters;
    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingType) {
        if (allocation != null) {
            return new LocalTypeBinding(scope, enclosingType, null);
        }
        if (enclosingType != null) {
            char[][] compoundName = new char[enclosingType.compoundName.length][];
            System.arraycopy(enclosingType.compoundName, 0, compoundName, 0, compoundName.length);
            compoundName[compoundName.length - 1] = CharOperations.merge(compoundName[compoundName.length - 1], name, '$');
            return new MemberTypeBinding(compoundName, scope, enclosingType);
        }

        return new AdsTypeBinding(this, packageBinding, scope);
    }

}
