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
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;


public class NonPersistentPropertyGetterGenerator extends PropertyGetterGenerator {

    public NonPersistentPropertyGetterGenerator(Definition referenceContext, CompilationResult result, ReferenceContext context) {
        super(referenceContext, result, context);
    }

    private char[] getValueStoreSelector(AdsPropertyDef property) {
        return ("_nppVal_" + property.getId().toString()).toCharArray();
    }

    @Override
    protected Statement[] createGetterBody(AdsPropertyDef property, AdsPropertyDeclaration propDecl, AdsProblemReporter reporter) {

        FieldReference syntheticFieldAccess = new FieldReference(getValueStoreSelector(property), 0);
        syntheticFieldAccess.receiver = ThisReference.implicitThis();
        return new Statement[]{new ReturnStatement(syntheticFieldAccess, 0, 0)};
    }

    @Override
    public void createBackstoreFields(AdsPropertyDef property, AdsPropertyDeclaration propDecl, List fields) {
        super.createBackstoreFields(property, propDecl, fields);
        char[] bsName = getValueStoreSelector(property);
        FieldDeclaration bs = new FieldDeclaration(bsName, 0, 0);
        bs.type = propDecl.type;
        bs.modifiers = ClassFileConstants.AccPrivate;
        if (propDecl.isStatic()) {
            bs.modifiers |= ClassFileConstants.AccStatic;
        }
        fields.add(bs);
    }
}
