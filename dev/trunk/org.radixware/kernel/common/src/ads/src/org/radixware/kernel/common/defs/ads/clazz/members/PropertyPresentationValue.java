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

import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.ARR_BLOB;
import static org.radixware.kernel.common.enums.EValType.ARR_CLOB;
import static org.radixware.kernel.common.enums.EValType.BLOB;
import static org.radixware.kernel.common.enums.EValType.CLOB;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class PropertyPresentationValue extends PropertyValue {

    PropertyPresentationValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    PropertyPresentationValue(AdsPropertyDef context) {
        super(context);
    }

    PropertyPresentationValue(AdsPropertyDef context, PropertyPresentationValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        if (type == null) {
            return false;
        }
        switch (type) {
            case JAVA_CLASS:
            case JAVA_TYPE:
            case USER_CLASS:
                return false;
        }
        if (getProperty().isLocal()) {
            switch (type) {
                case ARR_BLOB:
                case ARR_CLOB:
                case BLOB:
                case CLOB:
                    return false;
            }
            return type.isAllowedForPresentationProperty();
        } else {
            IModelPublishableProperty prop = findSourceProperty();
            if (prop != null) {
                return prop.getTypedObject().getType().isBasedOn(type);
            } else {
                return type.isAllowedForPresentationProperty();
            }
        }
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        AdsPropertyPresentationPropertyDef prop = getProperty();
        if (prop.getEmbeddedProperty() != null) {

            return prop.getEmbeddedProperty().getValue().isTypeAllowed(type);
        } else {
            return type.isEnumAssignableType() || type == EValType.USER_CLASS;
        }
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        AdsPropertyPresentationPropertyDef prop = getProperty();
        if (prop.getEmbeddedProperty() != null) {
            return prop.getEmbeddedProperty().getValue().getTypeSourceProvider(toRefine);
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }

    @Override
    public AdsTypeDeclaration getType() {
        return getTypeImpl(false);
    }

    @Override
    public AdsTypeDeclaration getTypeForCodeWriter() {
        return getTypeImpl(true);
    }

    @Override
    protected boolean isJmlInitValAllowed() {
        AdsPropertyPresentationPropertyDef prop = getProperty();
        if (prop.getEmbeddedProperty() != null) {
            return prop.getEmbeddedProperty().getValue().isJmlInitValAllowed();
        } else {
            return super.isJmlInitValAllowed();
        }
    }

    private AdsTypeDeclaration getTypeImpl(boolean forCodeWriter) {
        IModelPublishableProperty prop = findSourceProperty();
        if (prop != null) {
            AdsTypeDeclaration decl = prop.getTypedObject().getType();
            if (decl == null || decl.getTypeId() == null) {
                return AdsTypeDeclaration.UNDEFINED;
            }
            switch (decl.getTypeId()) {
                case PARENT_REF:
                case ARR_REF:
                case OBJECT:
                    if (getProperty().isLocal() && !forCodeWriter) {
                        return decl;
                    } else {
                        return AdsTypeDeclaration.Factory.newInstance(decl.getTypeId());
                    }
                case USER_CLASS:
                case JAVA_CLASS:
                case JAVA_TYPE:
                    return AdsTypeDeclaration.Factory.undefinedType();
                default:
                    return decl;
            }
        } else {
            return AdsTypeDeclaration.Factory.undefinedType();
        }
    }

    @Override
    protected AdsPropertyPresentationPropertyDef getProperty() {
        return (AdsPropertyPresentationPropertyDef) super.getProperty(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setType(AdsTypeDeclaration type) {
        AdsPropertyPresentationPropertyDef prop = getProperty();
        if (prop.getEmbeddedProperty() != null) {
            prop.getEmbeddedProperty().getValue().setType(type);
        } else {
            throw new RadixObjectError("Type of property presentation depends on source property type and cannot be modified.", this);
        }
    }

    public IModelPublishableProperty findSourceProperty() {
        return getProperty().findServerSideProperty();
    }
}
