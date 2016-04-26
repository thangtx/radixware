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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;

public class AdsTypeBinding extends SourceTypeBinding {

    protected Definition definition;

    public Definition getDefinition() {
        return definition;
    }

    public AdsTypeBinding(AdsTypeDeclaration decl, PackageBinding pkg, AdsDefinitionScope scope) {
        super(CharOperation.arrayConcat(pkg.compoundName, decl.name), pkg, scope);
        this.memberTypes = new ReferenceBinding[0];
        this.sourceName = decl.name;
        this.definition = decl.definition;
    }

    public AdsNestedTypeBinding findMember(AdsPropertyDef prop) {
        for (int i = 0; i < this.memberTypes.length; i++) {
            if ((this.memberTypes[i] instanceof AdsNestedTypeBinding) && ((AdsNestedTypeBinding) this.memberTypes[i]).definition == prop) {
                return (AdsNestedTypeBinding) this.memberTypes[i];
            }
        }
        return null;
    }

    @Override
    public FieldBinding resolveTypeFor(FieldBinding field) {
        if ((field.modifiers & ExtraCompilerModifiers.AccUnresolved) == 0) {
            return field;
        }

        return super.resolveTypeFor(field); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public char[] readableName() {
        return CompilerUtils.getTypeDisplayName(definition, compoundName).toCharArray();
    }

    @Override
    public char[] shortReadableName() {
        return CompilerUtils.getTypeDisplayName(definition, compoundName).toCharArray();
    }

    public void addMethod(AbstractMethodDeclaration method, ClassScope classScope) {
        MethodScope scope = new MethodScope(classScope, method, method.isStatic());
        MethodBinding methodBinding = scope.createMethod(method);

        if (resolveTypesFor(methodBinding) != null) {
            MethodBinding[] methods = this.methods();
            MethodBinding[] methods2 = new MethodBinding[methods.length + 1];
            System.arraycopy(methods, 0, methods2, 0, methods.length);
            methods2[methods.length] = methodBinding;
            ReferenceBinding.sortMethods(methods2, 0, methods2.length);
            setMethods(methods2);
        }
    }
}
