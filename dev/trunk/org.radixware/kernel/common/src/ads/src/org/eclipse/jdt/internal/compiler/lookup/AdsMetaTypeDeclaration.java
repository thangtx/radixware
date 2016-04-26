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

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ads.syntetics.AdsDefinitionHelper;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class AdsMetaTypeDeclaration extends AdsTypeDeclaration {

    public AdsMetaTypeDeclaration(AdsCompilationUnitScope scope, Definition definition, ERuntimeEnvironmentType env, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(scope, definition, env, compilationResult, locator);
        this.name = (definition.getId().toString() + "_mi").toCharArray();
        FieldDeclaration rdxMeta = AdsDefinitionHelper.createRadMetaField(definition, env);
        if (rdxMeta != null) {
            this.fields = new FieldDeclaration[]{
                rdxMeta
            };
        }

    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingType) {
        return new AdsMetaTypeBinding(this, packageBinding, scope);
    }
}
