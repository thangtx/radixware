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
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.radixware.kernel.common.compiler.core.JmlHelper;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;


public class AdsClassDeclaration extends AdsTypeDeclaration {

    public AdsClassDeclaration(AdsCompilationUnitScope unitScope, AdsClassDef clazz, ERuntimeEnvironmentType env, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(unitScope, clazz, env, compilationResult, locator);
        if (clazz.getAccessFlags().isAbstract()) {
            this.modifiers |= ClassFileConstants.AccAbstract;
        }
        if (clazz.getAccessFlags().isFinal()) {
            this.modifiers |= ClassFileConstants.AccFinal;
        }
        switch (clazz.getAccessFlags().getAccessMode()) {
            case PUBLIC:
                this.modifiers |= ClassFileConstants.AccPublic;
                break;
            case PROTECTED:
                this.modifiers |= ClassFileConstants.AccProtected;
                break;
            case PRIVATE:
                this.modifiers |= ClassFileConstants.AccPrivate;
                break;
        }
        List<TypeDeclaration> memberTypesList = new LinkedList<>();
        List<AdsPropertyDef> props = clazz.getProperties().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
        if (!props.isEmpty()) {

            for (AdsPropertyDef prop : props) {
                if (prop.getNature() == EPropNature.PROPERTY_PRESENTATION && prop.getOwnerClass() == clazz) {//local property                
                    memberTypesList.add(new AdsPropertyClassDeclaration(unitScope, this, prop, env, compilationResult, locator));
                }
            }

        }
        if (clazz.getTransparence() != null && clazz.getTransparence().isTransparent()) {
            if (clazz.getTransparence().isExtendable()) {
                String[] names = clazz.getTransparence().getPublishedName().split("\\.");
                long positions[] = new long[names.length];
                char[][] tokens = new char[names.length][];
                int start = 0;
                for (int i = 0; i < names.length; i++) {
                    tokens[i] = names[i].toCharArray();
                    int end = start + tokens[i].length;
                    positions[i] = ((long) start) << 32 | end;
                    start = end + 1;
                }
                this.superclass = new QualifiedTypeReference(tokens, positions);
            }
        } else {
            org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration superRef = clazz.getInheritance().getSuperClassRef();
            if (superRef != null) {
                this.superclass = new AdsTypeReference(clazz, superRef);
            }
        }
        if (memberTypes == null || memberTypes.length == 0) {
            this.memberTypes = memberTypesList.toArray(new TypeDeclaration[memberTypesList.size()]);
        } else {
            TypeDeclaration[] newCopy = new TypeDeclaration[this.memberTypes.length + memberTypesList.size()];
            System.arraycopy(this.memberTypes, 0, newCopy, 0, this.memberTypes.length);
            int index = this.memberTypes.length;
            for (TypeDeclaration d : memberTypesList) {
                newCopy[index++] = d;
            }
            this.memberTypes = newCopy;
        }
    }

    @Override
    protected void initializeOtherMemberTypesAndMethods(Definition definition, CompilationUnitScope scope, Object flowInfo, List<AbstractMethodDeclaration> methodsList, List<FieldDeclaration> fields, List<TypeDeclaration> innerTypes) {
        super.initializeOtherMemberTypesAndMethods(definition, scope, flowInfo, methodsList, fields, innerTypes);
    }

    @Override
    protected void attachOuterDeclarations(AdsCompilationUnitDeclaration cu, Scope scope) {
        JmlHelper.attachOuterDeclarations(cu, scope, this);
        /* if (definition instanceof AdsReportClassDef) {
         ImportReference[] extendedImports = new ImportReference[]{
         new ImportReference(BaseGenerator.RADIX_ENUMS_PACKAGE_NAME, new long[BaseGenerator.RADIX_ENUMS_PACKAGE_NAME.length], true, 0),
         new ImportReference(ReportClassGenerator.ADSREPORTS_PACJAGE_NAME, new long[ReportClassGenerator.ADSREPORTS_PACJAGE_NAME.length], true, 0),
         new ImportReference(BaseGenerator.JAVAAWTCOLOR_TYPE_NAME, new long[BaseGenerator.JAVAAWTCOLOR_TYPE_NAME.length], false, 0),};
         if (cu.imports == null || cu.imports.length == 0) {
         cu.imports = extendedImports;
         } else {
         ImportReference[] newImports = new ImportReference[extendedImports.length + cu.imports.length];
         System.arraycopy(cu.imports, 0, newImports, 0, cu.imports.length);
         System.arraycopy(extendedImports, 0, newImports, cu.imports.length, extendedImports.length);
         cu.imports = newImports;
         }
         }*/
    }

    @Override
    protected void attachInnerDeclarations(Scope scope) {
        JmlHelper.attachInnerDeclarations(this, scope);
    }

    @Override
    protected AdsClassDef lookForClassDefinition() {
        return (AdsClassDef) getDefinition();
    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingClass) {
        return new AdsClassTypeBinding(this, packageBinding, scope);
    }
}
