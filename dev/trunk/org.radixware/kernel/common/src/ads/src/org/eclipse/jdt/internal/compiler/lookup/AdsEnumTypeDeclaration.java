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

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ads.syntetics.EnumMethodsGenerator;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.compiler.core.AdsLookup;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;


public class AdsEnumTypeDeclaration extends AdsTypeDeclaration {

    public AdsEnumTypeDeclaration(AdsCompilationUnitScope scope, AdsEnumDef definition, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(scope, definition, definition.getUsageEnvironment(), compilationResult, locator);
        this.modifiers |= ClassFileConstants.AccEnum;
        List<FieldDeclaration> list = new LinkedList<>();
        for (AdsEnumItemDef item : ((AdsEnumDef) this.definition).getItems().list(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE)) {
            FieldDeclaration field = new FieldDeclaration(item.getId().toCharArray(), 0, 0);
            field.type = null;
            list.add(field);
        }
        this.modifiers |= ClassFileConstants.AccPublic;

        this.fields = list.toArray(new FieldDeclaration[list.size()]);

        char[][] superInterfaceName = null;
        long[] positions = new long[5];
        switch (definition.getItemType()) {
            case INT:
                superInterfaceName = AdsLookup.RADIX_INT_ENUM;
                break;
            case STR:
                superInterfaceName = AdsLookup.RADIX_STR_ENUM;
                break;
            case CHAR:
                superInterfaceName = AdsLookup.RADIX_CHAR_ENUM;
                break;
        }
        if (superInterfaceName != null) {
            this.superInterfaces = new TypeReference[]{
                new QualifiedTypeReference(superInterfaceName, positions)
            };
        }
        List<AbstractMethodDeclaration> methods = new LinkedList<>();
        new EnumMethodsGenerator(compilationResult, this).createDefaultMethods(definition, methods);
        this.methods = methods.toArray(new AbstractMethodDeclaration[methods.size()]);
    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingClass) {
        return new AdsEnumTypeBinding(this, packageBinding, scope);
    }
}
