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

import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.AdsLookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsCompilationUnitScope extends CompilationUnitScope {

    private Definition context;
    AdsWorkspace ws;
    private boolean binded = false;

    public AdsCompilationUnitScope(AdsCompilationUnitDeclaration unit, AdsWorkspace ws) {
        super(unit, ws.getLookupEnvironment());
        this.context = unit.contextAdsDefinition;
        this.ws = ws;
        final char[][] basePackage = JavaSourceSupport.getPackageNameComponents((Definition) unit.contextAdsDefinition, unit.getUsagePurpose());
        if (unit.contextAdsDefinition instanceof AbstractXmlDefinition) {
            String prefix = String.valueOf(CharOperations.merge(basePackage, '.'));
            if (unit.extPath.startsWith(prefix + ".impl.")) {
                this.currentPackageName = new char[basePackage.length + 1][];
                System.arraycopy(basePackage, 0, this.currentPackageName, 0, basePackage.length);
                this.currentPackageName[basePackage.length] = "impl".toCharArray();
            } else {
                this.currentPackageName = basePackage;
            }
        } else {
            this.currentPackageName = basePackage;
        }

    }

    public ReferenceBinding findType(Definition classDefinition, boolean meta) {
        return ((AdsLookupEnvironment) this.environment()).findType(classDefinition, this.getEnvType(), meta);
    }

    public ReferenceBinding findType(AdsDefinition classDefinition, String extName) {
        return ((AdsLookupEnvironment) this.environment()).findType(classDefinition, this.getEnvType(), extName);
    }

    public ERuntimeEnvironmentType getEnvType() {
        return ws.getEnvType();
    }

    public AdsWorkspace getWorkspace() {
        return ws;
    }
    private boolean radixTypesCached = false;

    @Override
    void checkAndSetImports() {
        if (radixTypesCached) {
            return;
        }
        radixTypesCached = true;
        super.checkAndSetImports();
        List<ImportBinding> radixBindings = new ArrayList<>();
        final PackageBinding radixTypesPackage = ws.getLookupEnvironment().getRadixTypesPackage();
        if (radixTypesPackage != null) {
            radixBindings.add(new ImportBinding(AdsLookup.RADIX_TYPES_PACKAGE, true, radixTypesPackage, null));
//            for (char[] typeName : AdsLookup.RADIX_TYPES) {
//                final ReferenceBinding type = radixTypesPackage.getType(typeName);
//                if (type != null) {
//                    radixBindings.add(new ImportBinding(new char[][]{typeName}, false, type, null));
//                }
//            }
        }
        if (!radixBindings.isEmpty()) {
            ImportBinding[] arr = new ImportBinding[imports.length + radixBindings.size()];
            System.arraycopy(imports, 0, arr, 0, imports.length);
            for (int i = 0, j = imports.length; i < radixBindings.size(); i++, j++) {
                arr[j] = radixBindings.get(i);
            }
            imports = arr;
        }

    }

    @Override
    void faultInImports() {
        if (this.typeOrPackageCache != null) {
            return;
        }
        super.faultInImports();
        PackageBinding radixTypesPackage = null;
        if (radixTypesCached) {
            if (this.imports == null) {
                radixTypesCached = false;
            } else {
                radixTypesPackage = ws.getLookupEnvironment().getRadixTypesPackage();
                for (int i = 0; i < imports.length; i++) {
                    if (imports[i].resolvedImport == radixTypesPackage) {
                        return;
                    }
                }
            }
        }
        if (radixTypesPackage == null) {
            radixTypesPackage = ws.getLookupEnvironment().getRadixTypesPackage();
        }
        List<ImportBinding> radixBindings = new ArrayList<>();
        if (radixTypesPackage != null) {
            radixBindings.add(new ImportBinding(AdsLookup.RADIX_TYPES_PACKAGE, true, radixTypesPackage, null));
        }
        if (!radixBindings.isEmpty()) {
            ImportBinding[] arr = new ImportBinding[imports.length + radixBindings.size()];
            System.arraycopy(imports, 0, arr, 0, imports.length);
            for (int i = 0, j = imports.length; i < radixBindings.size(); i++, j++) {
                arr[j] = radixBindings.get(i);
            }
            imports = arr;
        }

    }

    public void buildTypeBindings(AccessRestriction accessRestriction) {
        this.topLevelTypes = new SourceTypeBinding[0]; // want it initialized if the package cannot be resolved
        boolean firstIsSynthetic = false;
        if (this.referenceContext.compilationResult.compilationUnit != null) {
            char[][] expectedPackageName = this.referenceContext.compilationResult.compilationUnit.getPackageName();
            if (expectedPackageName != null
                    && !CharOperation.equals(this.currentPackageName, expectedPackageName)) {

                // only report if the unit isn't structurally empty
                if (this.referenceContext.currentPackage != null
                        || this.referenceContext.types != null
                        || this.referenceContext.imports != null) {
                    problemReporter().packageIsNotExpectedPackage(this.referenceContext);
                }
                this.currentPackageName = expectedPackageName.length == 0 ? CharOperation.NO_CHAR_CHAR : expectedPackageName;
            }
        }
        if (this.currentPackageName == CharOperation.NO_CHAR_CHAR) {
            // environment default package is never null
            this.fPackage = this.environment.defaultPackage;
        } else {
            if ((this.fPackage = this.environment.createPackage(this.currentPackageName)) == null) {
                if (this.referenceContext.currentPackage != null) {
                    problemReporter().packageCollidesWithType(this.referenceContext); // only report when the unit has a package statement
                }
                // ensure fPackage is not null
                this.fPackage = this.environment.defaultPackage;
                return;
            } else if (this.referenceContext.isPackageInfo()) {
                // resolve package annotations now if this is "package-info.java".
                if (this.referenceContext.types == null || this.referenceContext.types.length == 0) {
                    this.referenceContext.types = new TypeDeclaration[1];
                    this.referenceContext.createPackageInfoType();
                    firstIsSynthetic = true;
                }
                // ensure the package annotations are copied over before resolution
                if (this.referenceContext.currentPackage != null && this.referenceContext.currentPackage.annotations != null) {
                    this.referenceContext.types[0].annotations = this.referenceContext.currentPackage.annotations;
                }
            }
            recordQualifiedReference(this.currentPackageName); // always dependent on your own package
        }

        // Skip typeDeclarations which know of previously reported errors
        TypeDeclaration[] types = this.referenceContext.types;
        int typeLength = (types == null) ? 0 : types.length;
        this.topLevelTypes = new SourceTypeBinding[typeLength];
        int count = 0;
        nextType:
        for (int i = 0; i < typeLength; i++) {
            TypeDeclaration typeDecl = types[i];
            if (this.environment.isProcessingAnnotations && this.environment.isMissingType(typeDecl.name)) {
                throw new SourceTypeCollisionException(); // resolved a type ref before APT generated the type
            }
            ReferenceBinding typeBinding = this.fPackage.getType0(typeDecl.name);
            recordSimpleReference(typeDecl.name); // needed to detect collision cases
            if (typeBinding != null && typeBinding.isValidBinding() && !(typeBinding instanceof UnresolvedReferenceBinding)) {
                // if its an unresolved binding - its fixed up whenever its needed, see UnresolvedReferenceBinding.resolve()
                if (this.environment.isProcessingAnnotations) {
                    throw new SourceTypeCollisionException(); // resolved a type ref before APT generated the type
                }			// if a type exists, check that its a valid type
                // it can be a NotFound problem type if its a secondary type referenced before its primary type found in additional units
                // and it can be an unresolved type which is now being defined
                problemReporter().duplicateTypes(this.referenceContext, typeDecl);
                continue nextType;
            }
            if (this.fPackage != this.environment.defaultPackage && this.fPackage.getPackage(typeDecl.name) != null) {
                // if a package exists, it must be a valid package - cannot be a NotFound problem package
                // this is now a warning since a package does not really 'exist' until it contains a type, see JLS v2, 7.4.3
                problemReporter().typeCollidesWithPackage(this.referenceContext, typeDecl);
            }

            if ((typeDecl.modifiers & ClassFileConstants.AccPublic) != 0) {
                char[] mainTypeName;
                if ((mainTypeName = this.referenceContext.getMainTypeName()) != null // mainTypeName == null means that implementor of ICompilationUnit decided to return null
                        && !CharOperation.equals(mainTypeName, typeDecl.name)) {
                    problemReporter().publicClassMustMatchFileName(this.referenceContext, typeDecl);
                    // tolerate faulty main type name (91091), allow to proceed into type construction
                }
            }

            AdsDefinitionScope child = new AdsDefinitionScope(this, (AdsTypeDeclaration) typeDecl);
            SourceTypeBinding type = child.buildType(null, this.fPackage, accessRestriction);
            if (firstIsSynthetic && i == 0) {
                type.modifiers |= ClassFileConstants.AccSynthetic;
            }
            if (type != null) {
                this.topLevelTypes[count++] = type;
            }
        }

        // shrink topLevelTypes... only happens if an error was reported
        if (count != this.topLevelTypes.length) {
            System.arraycopy(this.topLevelTypes, 0, this.topLevelTypes = new SourceTypeBinding[count], 0, count);
        }
    }

    public boolean isRadixCompatible(TypeBinding rhsType, TypeBinding lhsType, Expression expression, Scope scope) {
        switch (lhsType.id) {
            case TypeIds.T_JavaLangLong:
                switch (rhsType.id) {
                    case TypeIds.T_int:
                    case TypeIds.T_short:
                    case TypeIds.T_char:
                    case TypeIds.T_byte:
                        return true;
                }
                break;
        }
        return false;
    }
    private static final char[][] TYPE_CONVERSIONS = new char[][]{
        "org".toCharArray(),
        "radixware".toCharArray(),
        "kernel".toCharArray(),
        "common".toCharArray(),
        "lang".toCharArray(),
        "TypeConversions".toCharArray()
    };

    private Expression createTypeConversion(Expression e, BlockScope scope) {
        MessageSend ms = new MessageSend();
        ms.selector = "toLong".toCharArray();
        ms.receiver = new QualifiedNameReference(TYPE_CONVERSIONS, new long[TYPE_CONVERSIONS.length], 0, 0);
        ms.arguments = new Expression[]{e};
        ms.resolveType(scope);
        return ms;
    }

    public Expression convertExpression(TypeBinding rhsType, TypeBinding lhsType, Expression expression, BlockScope scope) {
        return createTypeConversion(expression, scope);
    }
}
