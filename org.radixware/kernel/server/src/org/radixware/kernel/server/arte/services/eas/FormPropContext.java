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

package org.radixware.kernel.server.arte.services.eas;

import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.FormHandler;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.presctx.FormPropertyPresentationContext;
import org.radixware.schemas.eas.ExceptionEnum;


final class FormPropContext extends PropContext{
    
    private final FormHandler form;
    private final RadConditionDef.Prop2ValueCondition contextProperties;
    
    FormPropContext(final SessionRequest rq, 
                    final org.radixware.schemas.eas.Context.FormProperty xml,
                    final org.radixware.schemas.eas.PropertyList groupProps) throws InterruptedException{
        this(rq, rq.getFormHandler(xml.getForm(), true), xml.getPropertyId(), groupProps);
    }

    FormPropContext(final SessionRequest rq, 
                    final FormHandler form, 
                    final Id propId,
                    final org.radixware.schemas.eas.PropertyList groupProps) {
        super(rq, propId,groupProps);
        this.form = form;
        contextProperties = calcContextProperties();
    };

    @Override
    RadClassPresentationDef.ClassCatalog getClassCatalog(){
        final Id classCatalogId = getParentTitlePropPresentation().getParentClassCatalogId(getParentTitlePropClassDef().getPresentation());
        if (classCatalogId == null)
            return null;
        else
            return getClassDef().getPresentation().getClassCatalogById(classCatalogId);
    }

    @Override
    DdsReferenceDef getContextReference() {
        return null;
    }

    @Override
    EContextRefRole getContextReferenceRole() {
        return EContextRefRole.NONE;
    }

    @Override
    Pid getContextObjectPid() {
        return null;
    }

    @Override
    IRadRefPropertyDef getContextRefProperty() {
        RadPropDef prop = getParentTitlePropClassDef().getPropById(getPropertyId());
        while (prop instanceof RadParentPropDef) {
            final RadParentPropDef parentProp = (RadParentPropDef) prop;
            prop = rq.getArte().getDefManager().getClassDef(parentProp.getJoinedClassId()).getPropById(parentProp.getJoinedPropId());
        }
        return (IRadRefPropertyDef) prop;        
    }

    /**
     * @return the form
     */
    FormHandler getForm() {
        return form;
    }
    
    @Override
    FormHandler getContextPropOwner(){
        return form;
    }

    @Override
    RadParentTitlePropertyPresentationDef getParentTitlePropPresentation() {
        try {
            return (RadParentTitlePropertyPresentationDef)getForm().getRadMeta().getPresentation().getPropPresById(getPropertyId());
        } catch (DefinitionNotFoundError | ClassCastException e) {
            final String preprocessedExStack = getForm().getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Parent title #" + getPropertyId() + " is not defined in form #" + getForm().getRadMeta().getId(), e, preprocessedExStack);
        }
    }

    @Override
    IRadClassInstance getChildInstForParentTitleRef() {
        return getForm();
    }

    @Override
    void checkAccessible() {
        //no restrictions
    }

    @Override
    EntityGroup.Context buildEntGroupContext() {
        return new EntityGroup.PropContext(getClassDef(), getContextRefProperty(), getParentTitlePropClassDef().getPresentation(), getParentTitlePropPresentation(), getChildInstForParentTitleRef(), null);
    }

    @Override
    RadClassDef getParentTitlePropClassDef() {
        return getForm().getRadMeta();
    }

    @Override
    public RadConditionDef.Prop2ValueCondition getContextProperties() {
        return contextProperties;
    }   

    @Override
    FormPropertyPresentationContext getPresentationContext(final EntityGroup entityGroup) {
        final RadSelectorPresentationDef selectorPresentation = getSelectorPresentation();
        final Id selectorPresentationId = selectorPresentation==null ? null : selectorPresentation.getId();
        return new FormPropertyPresentationContext(form, getPropertyId(), entityGroup, selectorPresentationId);
    }        

    @Override
    public String toString() {
        final RadClassDef formClassDef = getParentTitlePropClassDef();
        final RadPropDef prop = formClassDef.getPropById(getContextRefProperty().getId());
        return String.valueOf(formClassDef.getTitle()) + " (#" + formClassDef.getId().toString() + ") -> " +
               "Property \'" + String.valueOf(prop.getName()) + "\' (#" + prop.getId().toString() + ")";
    }
}
