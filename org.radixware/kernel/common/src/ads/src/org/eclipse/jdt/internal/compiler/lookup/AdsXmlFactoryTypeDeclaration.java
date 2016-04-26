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
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ads.syntetics.XBeansFactoryGenerator;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansInterface;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsXmlFactoryTypeDeclaration extends AdsTypeDeclaration {

    public AdsXmlFactoryTypeDeclaration(AdsCompilationUnitScope scope, AbstractXmlDefinition definition, XBeansInterface iface, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(scope, definition, ERuntimeEnvironmentType.COMMON, compilationResult, locator);
        this.name = "Factory".toCharArray();
        this.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccPublic | ClassFileConstants.AccFinal;
        final List<AbstractMethodDeclaration> methods = new LinkedList<>();
        TypeReference ownerTypeRef = new AdsTypeReference(definition, org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.Factory.newXml(definition, iface.getQName()));
        this.annotations = new Annotation[]{
            BaseGenerator.createSuppressWarningsAnnotation("deprecation")
        };
        new XBeansFactoryGenerator(compilationResult, this).createFactoryMethods(
                ownerTypeRef,
                iface.getFactoryInfo().isFullFactory(),
                iface.getFactoryInfo().isSimpleTypeFactory(), iface.getFactoryInfo().isAbstractTypeFactory(), methods);
        this.methods = methods.toArray(new AbstractMethodDeclaration[methods.size()]);

    }

    @Override
    public SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingType) {
        char[][] compoundName = new char[enclosingType.compoundName.length][];
        System.arraycopy(enclosingType.compoundName, 0, compoundName, 0, compoundName.length);
        compoundName[compoundName.length - 1] = CharOperations.merge(compoundName[compoundName.length - 1], name, '$');
        return new MemberTypeBinding(compoundName, scope, enclosingType);
    }
}
