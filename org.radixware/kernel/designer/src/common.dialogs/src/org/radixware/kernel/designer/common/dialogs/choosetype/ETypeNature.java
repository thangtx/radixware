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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.EValType;


public enum ETypeNature {

    RADIX_CLASS(NbBundle.getMessage(ETypeNature.class, "ETypeNature-RadixClass")),
    RADIX_ENUM(NbBundle.getMessage(ETypeNature.class, "ETypeNature-RadixEnum")),
    //    RADIX_MODEL(NbBundle.getMessage(ETypeNature.class, "ETypeNature-RadixModel")),
    RADIX_TYPE(NbBundle.getMessage(ETypeNature.class, "ETypeNature-RadixType")),
    RADIX_XML(NbBundle.getMessage(ETypeNature.class, "ETypeNature-RadixXml")),
    JAVA_PRIMITIVE(NbBundle.getMessage(ETypeNature.class, "ETypeNature-JavaType")),
    JAVA_CLASS(NbBundle.getMessage(ETypeNature.class, "ETypeNature-JavaClass")),
    JAVA_ARRAY(NbBundle.getMessage(ETypeNature.class, "ETypeNature-JavaArray")),
    TYPE_PARAMETER(NbBundle.getMessage(ETypeNature.class, "ETypeNature-TypeParameter")),
    UNDEFINED(NbBundle.getMessage(ETypeNature.class, "ETypeNature-Undefined"));
    private String natureName;

    private ETypeNature(String natureName) {
        this.natureName = natureName;
    }

    public String getNatureName() {
        return natureName;
    }

    public static ETypeNature getByName(String name) {
        ETypeNature[] natures = values();

        for (ETypeNature nature : natures) {
            if (nature.getNatureName().equals(name)) {
                return nature;
            }
        }
        return ETypeNature.UNDEFINED;
    }

    public static ETypeNature getByType(AdsTypeDeclaration type, Definition definition) {
        if (type == null || type.isUndefined()) {
            return ETypeNature.UNDEFINED;
        } else if (type.isArray()) {
            return ETypeNature.JAVA_ARRAY;
        } else if (type.isTypeArgument()) {
            return ETypeNature.TYPE_PARAMETER;
        } else if (type.isVoid()) {
            return ETypeNature.JAVA_PRIMITIVE;
        }

        EValType typeid = type.getTypeId();
        AdsType adsType = type.resolve(definition).get();

        return getByType(typeid, adsType);
    }

    private static ETypeNature getByType(EValType valType, AdsType adsType) {
        if (valType == EValType.JAVA_CLASS) {
            return ETypeNature.JAVA_CLASS;
        } else if (valType == EValType.JAVA_TYPE) {
            return ETypeNature.JAVA_PRIMITIVE;
        } else if (valType == EValType.USER_CLASS || valType == EValType.PARENT_REF) {
            if (adsType instanceof AdsClassType) {
                AdsClassType classType = (AdsClassType) adsType;
                if (classType.getSource() instanceof AdsModelClassDef) {
                    return ETypeNature.RADIX_CLASS;//RADIX_MODEL;
                }
            }
            return ETypeNature.RADIX_CLASS;

        } else if (valType == EValType.XML) {
            if (adsType instanceof XmlType) {
                XmlType asXmlType = (XmlType) adsType;
                if (asXmlType.getSource() == null) {
                    return ETypeNature.RADIX_TYPE;
                }
            }
            return ETypeNature.RADIX_XML;
        } else if (adsType instanceof AdsEnumType) {
            AdsEnumType enumType = (AdsEnumType) adsType;
            if (enumType.getSource().getItemType() == valType) {
                return ETypeNature.RADIX_ENUM;
            }
        }
        return ETypeNature.RADIX_TYPE;
    }
}
