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
package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.MethodValue;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.MethodDefinition;

public class MethodReturnValue extends MethodValue {

    private static String getDescription(MethodDefinition.ReturnType xDef) {
        return xDef != null ? xDef.getDescription() : "";
    }

    private static Id getDescriptionId(MethodDefinition.ReturnType xDef) {
        return xDef != null ? xDef.getDescriptionId() : null;
    }
    
    private static boolean getIsDescriptionInherited(MethodDefinition.ReturnType xDef) {
        return xDef != null && xDef.isSetIsDescriptionInherited() ? xDef.getIsDescriptionInherited() : true;
    }

    MethodReturnValue(AdsMethodDef md, MethodDefinition.ReturnType xDef) {
        super(xDef, AdsTypeDeclaration.Factory.voidType(), getDescription(xDef), getDescriptionId(xDef),getIsDescriptionInherited(xDef));
        setContainer(md);
    }

    MethodReturnValue(AdsMethodDef md) {
        super(AdsTypeDeclaration.Factory.voidType());
        setContainer(md);
    }

    void appendTo(AbstractMethodDefinition xDef) {
        String desc = getDescription();
        AdsTypeDeclaration typeDecl = getType();
        if (typeDecl != null && !typeDecl.isVoid() || (desc != null && !desc.isEmpty()) || getDescriptionId() != null) {
            MethodDefinition.ReturnType xType = xDef.addNewReturnType();
            if (typeDecl != null && !typeDecl.isVoid()) {
                typeDecl.appendTo(xType);
            }
            if (desc != null && !desc.isEmpty()) {
                xType.setDescription(desc);
            }

            if (!isDescriptionInherited()) {
                if (isDescriptionIdChanged()) {
                    xType.setDescriptionId(getDescriptionId());
                }
                if (isDescriptionInheritable()){
                    xDef.setIsDescriptionInherited(false);
                }
            }
        }
    }

    @Override
    public boolean needsDocumentation() {
        AdsTypeDeclaration type = getType();
        if (type == null || type.isVoid()) {
            return false;
        }
        return super.needsDocumentation();
    }
    
}
