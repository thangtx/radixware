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

import java.util.List;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;


public class AdsPropertyClassDeclaration extends AdsTypeDeclaration {

    private AdsPropertyDef determinant;
    private AdsPropertyDeclaration valueProp;

    public AdsPropertyClassDeclaration(AdsCompilationUnitScope unitScope, AdsClassDeclaration enclosingType, AdsPropertyDef determinant, ERuntimeEnvironmentType env, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(unitScope, determinant, env, compilationResult, locator);
        this.enclosingType = enclosingType;
        this.determinant = determinant;
        this.superclass = new AdsTypeReference(enclosingType.getDefinition(), createSuperClassRef(determinant), 0, 0);
    }

    @Override
    protected void initializeOtherDeclarations(Definition definition, List<AdsPropertyDeclaration> proplist) {
        super.initializeOtherDeclarations(definition, proplist);
        proplist.add(valueProp = new AdsPropertyDeclaration("Value".toCharArray(), this.env, new AdsTypeReference(definition, getProperty().getValue().getType(), 0, 0)));
    }

    @Override
    protected void initializeOtherMemberTypesAndMethods(Definition definition, CompilationUnitScope scope, Object flowInfo, List<AbstractMethodDeclaration> methodsList, List<FieldDeclaration> fields, List<TypeDeclaration> innerTypes) {
        super.initializeOtherMemberTypesAndMethods(definition, scope, flowInfo, methodsList, fields, innerTypes);
        if (definition != null) {
            MethodDeclaration getter = new MethodDeclaration(compilationResult);
            getter.selector = "getValue".toCharArray();
            getter.returnType = valueProp.type;
            methodsList.add(getter);
            if (!((AdsPropertyDef) definition).isConst()) {
                MethodDeclaration setter = new MethodDeclaration(compilationResult);
                setter.selector = "setValue".toCharArray();
                setter.returnType = TypeReference.baseTypeReference(TypeIds.T_void, 0);
                setter.arguments = new Argument[]{new Argument("val".toCharArray(), 0, valueProp.type, 0)};
                methodsList.add(setter);
            }
        }
    }

    public AdsPropertyDef getProperty() {
        return (AdsPropertyDef) getDefinition();
    }

    @Override
    protected AdsClassDef lookForClassDefinition() {
        if (getDefinition() instanceof AdsPropertyPresentationPropertyDef) {
            return ((AdsPropertyPresentationPropertyDef) getDefinition()).getLocalEmbeddedClass(false);
        }
        return null;
    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingType) {
        return new AdsPropertyTypeBinding(this, scope, enclosingType);
    }

    private static org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration createSuperClassRef(AdsPropertyDef property) {
        String className = "org.radixware.kernel.common.client.models.items.properties.Property";
        if (property.getValue().getType() == null) {
            return null;
        }
        switch (property.getValue().getType().getTypeId()) {
            case OBJECT:
                className += "Object";
                break;
            case PARENT_REF:
                className += "Ref";
                break;
            case ARR_BIN:
                className += "ArrBin";
                break;
            case ARR_BLOB:
                className += "ArrBlob";
                break;
            case ARR_BOOL:
                className += "ArrBool";
                break;
            case ARR_CHAR:
                className += "ArrChar";
                break;
            case ARR_DATE_TIME:
                className += "ArrDateTime";
                break;
            case ARR_INT:
                className += "ArrInt";
                break;
            case ARR_NUM:
                className += "ArrNum";
                break;
            case ARR_REF:
                className += "ArrRef";
                break;
            case ARR_STR:
                className += "ArrStr";
                break;
            case XML:
                className += "Xml";
                break;
            case STR:
                className += "Str";
                break;
            case NUM:
                className += "Num";
                break;
            case INT:
                className += "Int";
                break;
            case DATE_TIME:
                className += "DateTime";
                break;
            case CHAR:
                className += "Char";
                break;
            case BOOL:
                className += "Bool";
                break;
            case BLOB:
                className += "Blob";
                break;
            case CLOB:
                className += "Clob";
                break;
            case BIN:
                className += "Bin";
                break;
            default:
                return null;
        }
        return org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.Factory.newPlatformClass(className);

    }
}
