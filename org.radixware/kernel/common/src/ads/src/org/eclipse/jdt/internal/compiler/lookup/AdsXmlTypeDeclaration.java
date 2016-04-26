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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ads.syntetics.XBeansInterfaceGenerator;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansIfaceProp;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansInterface;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;


public class AdsXmlTypeDeclaration extends AdsTypeDeclaration {

    boolean isRoot = false;

    public AdsXmlTypeDeclaration(AdsCompilationUnitScope scope, AbstractXmlDefinition definition, String topLevelTypeName, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(scope, definition, definition.getSchemaTypeSystem().findType(topLevelTypeName), compilationResult, locator);
        isRoot = true;
    }

    public AdsXmlTypeDeclaration(AdsXmlTypeDeclaration enclosingType, AdsCompilationUnitScope scope, AbstractXmlDefinition definition, XBeansInterface iface, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(scope, definition, iface, compilationResult, locator);
        this.enclosingType = enclosingType;
    }

    public TypeReference createXmlTypeReference(String qname) {
        String[] interfaceNames = qname.split("\\.");
        char[][] interfaceNamesChar = new char[interfaceNames.length][];
        for (int n = 0; n < interfaceNames.length; n++) {
            interfaceNamesChar[n] = interfaceNames[n].toCharArray();
        }
        return new QualifiedTypeReference(interfaceNamesChar, new long[interfaceNamesChar.length]);
    }

    @Override
    protected void finalizeInitialization(Scope scope, Object flowInfo) {
        if (flowInfo instanceof XBeansInterface) {
            init((AdsCompilationUnitScope) scope, (XBeansInterface) flowInfo);
        }
    }

    private void init(AdsCompilationUnitScope scope, XBeansInterface iface) {
        this.name = iface.getName().toCharArray();
        this.modifiers = ClassFileConstants.AccInterface | ClassFileConstants.AccPublic | ClassFileConstants.AccAbstract;
        String[] extInterfaces = iface.getExtInterfaces();
        if (extInterfaces != null) {
            this.superInterfaces = new TypeReference[extInterfaces.length + 1];
            for (int i = 0; i < extInterfaces.length; i++) {
                this.superInterfaces[i] = createXmlTypeReference(extInterfaces[i]);
            }
            if (iface.getBaseInterface() != null) {
                this.superInterfaces[this.superInterfaces.length - 1] = createXmlTypeReference(iface.getBaseInterface());
            } else {
                this.superInterfaces[this.superInterfaces.length - 1] = new QualifiedTypeReference(BaseGenerator.XMLOBJECT_TYPE_NAME, new long[BaseGenerator.XMLOBJECT_TYPE_NAME.length]);
            }
        }
        List<TypeDeclaration> members = new LinkedList<>();
        if (iface.getFactoryInfo() != null) {
            members.add(new AdsXmlFactoryTypeDeclaration(scope, (AbstractXmlDefinition) definition, iface, compilationResult, locator));
        }
        XBeansInterface.Content content = iface.getContent();
        if (content instanceof XBeansInterface.ComplexContent) {
            XBeansInterface.ComplexContent cc = (XBeansInterface.ComplexContent) content;
            this.methods = new XBeansInterfaceGenerator(compilationResult).createXmlProperties(cc.getPropertyList());
        } else if (content instanceof XBeansInterface.SimpleContent) {
            XBeansInterface.SimpleContent sc = (XBeansInterface.SimpleContent)content;
            
        }
        List<XBeansInterface> inners = iface.getInnerInterfaces();
        if (inners != null) {
            for (XBeansInterface inner : inners) {
                members.add(new AdsXmlTypeDeclaration(this, scope, (AbstractXmlDefinition) definition, inner, compilationResult, locator));
            }
        }
        if (memberTypes == null || memberTypes.length == 0) {
            this.memberTypes = members.toArray(new TypeDeclaration[members.size()]);
        } else {
            TypeDeclaration[] newCopy = new TypeDeclaration[this.memberTypes.length + members.size()];
            System.arraycopy(this.memberTypes, 0, newCopy, 0, this.memberTypes.length);
            int index = this.memberTypes.length;
            for (TypeDeclaration d : members) {
                newCopy[index++] = d;
            }
            this.memberTypes = newCopy;
        }
    }

    @Override
    protected void initializeOtherMemberTypesAndMethods(Definition definition, CompilationUnitScope scope, Object flowInfo, List<AbstractMethodDeclaration> methods, List<FieldDeclaration> fields, List<TypeDeclaration> innerTypes) {
        if (flowInfo instanceof XBeansInterface) {
        }
        super.initializeOtherMemberTypesAndMethods(definition, scope, flowInfo, methods, fields, innerTypes);
    }

    @Override
    char[][] getCompoundName() {
        if (isRoot) {
            return super.getCompoundName();
        } else {
            char[][] ownerName = ((AdsXmlTypeDeclaration) this.enclosingType).getCompoundName();
            char[][] result = new char[ownerName.length + 1][];
            System.arraycopy(ownerName, 0, result, 0, ownerName.length);
            result[ownerName.length] = name;
            return result;
        }
    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingType) {
        if (isRoot) {
            return new AdsXmlRootTypeBinding(this, packageBinding, scope);
        } else {

            char[][] compoundName = new char[enclosingType.compoundName.length][];
            System.arraycopy(enclosingType.compoundName, 0, compoundName, 0, compoundName.length);
            compoundName[compoundName.length - 1] = CharOperations.merge(compoundName[compoundName.length - 1], name, '$');
            return new MemberTypeBinding(compoundName, scope, enclosingType);
        }
    }
}
