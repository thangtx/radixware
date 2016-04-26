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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import java.util.List;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class AdsDefinitionHelper {

    public static void generateAdditionalMethods(Definition referenceContext, AdsTypeDeclaration decl, ERuntimeEnvironmentType env, List<AbstractMethodDeclaration> list) {

        Definition clazz = (Definition) decl.getDefinition();

        switch (env) {
            case SERVER:
                if (clazz instanceof AdsClassDef || clazz instanceof AdsEnumDef) {
                    createRadMeta(referenceContext, decl, clazz, list);
                }
                break;
        }

    }
    private static final char[][] RAD_META_CLASS_NAME = new char[][]{
        "org".toCharArray(),
        "radixware".toCharArray(),
        "kernel".toCharArray(),
        "server".toCharArray(),
        "meta".toCharArray(),
        "clazzes".toCharArray(),
        "RadClassDef".toCharArray()};

    private static FieldReference createStaticFieldReference(NameReference recv, char[] selector) {
        FieldReference field = new FieldReference(selector, 0);
        field.receiver = recv;
        return field;
    }

    private static void createRadMeta(Definition referenceContext, AdsTypeDeclaration decl, Definition clazz, List<AbstractMethodDeclaration> list) {
        MethodDeclaration getRadMeta = new MethodDeclaration(decl.compilationResult);
        getRadMeta.returnType = createRadClassDefType();
        getRadMeta.modifiers |= ClassFileConstants.AccPublic;
        getRadMeta.selector = "getRadMeta".toCharArray();
        getRadMeta.statements = new Statement[]{
            new ReturnStatement(createStaticFieldReference(new TaggedSingleNameReference(referenceContext, clazz, true), "rdxMeta".toCharArray()), 0, 0)
        };
        list.add(getRadMeta);
    }

    private static QualifiedTypeReference createRadClassDefType() {
        return new QualifiedTypeReference(RAD_META_CLASS_NAME, new long[RAD_META_CLASS_NAME.length]);
    }

    public static FieldDeclaration createRadMetaField(Definition definition, ERuntimeEnvironmentType env) {
        if (definition instanceof AdsClassDef) {
            if (env == ERuntimeEnvironmentType.SERVER) {
                FieldDeclaration field = new FieldDeclaration("rdxMeta".toCharArray(), 0, 0);
                field.modifiers = ClassFileConstants.AccPublic | ClassFileConstants.AccStatic | ClassFileConstants.AccFinal;
                field.type = createRadClassDefType();
                field.initialization = new NullLiteral(0, 0);
                return field;
            }
        }
        return null;
    }
}
