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

import org.eclipse.jdt.internal.compiler.ads.syntetics.AdsPropertyHelper;
import org.eclipse.jdt.internal.compiler.ast.AdsArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;

import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import static org.radixware.kernel.common.enums.EAccess.PUBLIC;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class AdsPropertyDeclaration extends FieldDeclaration {

    private final AdsPropertyDef property;
    private final ERuntimeEnvironmentType env;

    public AdsPropertyDeclaration(AdsPropertyDef prop, ERuntimeEnvironmentType env) {
        this.property = prop;
        this.env = env;
        if (prop instanceof AdsPropertyPresentationPropertyDef) {
            this.type = new AdsTypeReference(property, property);
        } else {
            if (prop.getValue().getType().getArrayDimensionCount() > 0) {
                this.type = new AdsArrayTypeReference(property, prop.getValue().getType());
            } else {
                this.type = new AdsTypeReference(property, prop.getValue().getType());
            }
        }
        this.modifiers = 0;
        switch (prop.getAccessFlags().getAccessMode()) {
            case PUBLIC:
                modifiers = ClassFileConstants.AccPublic;
                break;
            case PRIVATE:
                modifiers = ClassFileConstants.AccPrivate;
                break;
            case PROTECTED:
                modifiers = ClassFileConstants.AccProtected;
                break;
            case DEFAULT:
                modifiers = ClassFileConstants.AccDefault;
                break;
        }
        if (prop.isDeprecated()) {
            modifiers |= ClassFileConstants.AccDeprecated;
        }
        if (prop.getAccessFlags().isStatic()) {
            modifiers |= ClassFileConstants.AccStatic;
        }
        this.name = prop.getId().toCharArray();

    }
    private FieldDeclaration[] backstoreFields = null;
    private static final FieldDeclaration[] NO_FIELDS = new FieldDeclaration[0];

    public FieldDeclaration[] backstoreFields(Scope scope) {
        if (backstoreFields == null) {
            if (property == null) {
                backstoreFields = NO_FIELDS;
            } else {
                backstoreFields = AdsPropertyHelper.createBackstoreFields(property, this, ((TypeDeclaration) scope.classScope().referenceContext()).compilationResult, scope.referenceContext());
            }
        }
        return backstoreFields;
    }

    public AdsPropertyDeclaration(char[] name, ERuntimeEnvironmentType env, TypeReference type) {
        this.property = null;
        this.name = name;
        this.type = type;
        this.env = env;
    }

    public AdsPropertyDef getProperty() {
        return property;
    }

    public ERuntimeEnvironmentType getEnvironmentType() {
        return env;
    }

    @Override
    public void resolve(MethodScope initializationScope) {
        super.resolve(initializationScope); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resolve(BlockScope scope) {
        super.resolve(scope); //To change body of generated methods, choose Tools | Templates.
    }
}
