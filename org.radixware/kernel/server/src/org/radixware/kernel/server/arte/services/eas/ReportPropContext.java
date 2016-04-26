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
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntityGroup.Context;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.Report;
import org.radixware.kernel.server.types.presctx.ReportPropertyPresentationContext;
import org.radixware.schemas.eas.ExceptionEnum;


final class ReportPropContext  extends PropContext{
    
    private final Report report;
    
    ReportPropContext(final SessionRequest rq, 
                      final org.radixware.schemas.eas.Context.ReportProperty xml,
                      final org.radixware.schemas.eas.PropertyList groupProps) throws InterruptedException{
        this(rq, rq.getReport(xml.getReport(), true), xml.getPropertyId(), groupProps);
    }

    ReportPropContext(final SessionRequest rq, 
                      final Report report, 
                      final Id propId,
                      final org.radixware.schemas.eas.PropertyList groupProps) {
        super(rq, propId, groupProps);
        this.report = report;
    };    
    
    /**
     * @return the report
     */
    Report getReport() {
        return report;
    }
    
    @Override
    Report getContextPropOwner(){
        return report;
    }

    @Override
    RadClassDef getParentTitlePropClassDef() {
        return getReport().getRadMeta();
    }

    @Override
    IRadClassInstance getChildInstForParentTitleRef() {
        return getReport();
    }

    @Override
    RadParentTitlePropertyPresentationDef getParentTitlePropPresentation() {
        try {
            return (RadParentTitlePropertyPresentationDef)getReport().getRadMeta().getPresentation().getPropPresById(getPropertyId());
        } catch (DefinitionNotFoundError | ClassCastException e) {
            final String preprocessedExStack = getReport().getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Parent title #" + getPropertyId() + " is not defined in report #" + getReport().getRadMeta().getId(), e, preprocessedExStack);
        }
    }

    @Override
    ClassCatalog getClassCatalog() {
        final Id classCatalogId = getParentTitlePropPresentation().getParentClassCatalogId(getParentTitlePropClassDef().getPresentation());
        if (classCatalogId == null)
            return null;
        else
            return getClassDef().getPresentation().getClassCatalogById(classCatalogId);
    }

    @Override
    EContextRefRole getContextReferenceRole() {
        return EContextRefRole.NONE;
    }

    @Override
    DdsReferenceDef getContextReference() {
        return null;
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

    @Override
    void checkAccessible() {
        //no restrictions
    }

    @Override
    Context buildEntGroupContext() {
        return new EntityGroup.PropContext(getClassDef(), getContextRefProperty(), getParentTitlePropClassDef().getPresentation(), getParentTitlePropPresentation(), getChildInstForParentTitleRef(), null);
    }

    @Override
    public RadConditionDef.Prop2ValueCondition getContextProperties() {
        return super.getContextProperties();
    }    

    @Override
    ReportPropertyPresentationContext getPresentationContext(final EntityGroup entityGroup) {
        return new ReportPropertyPresentationContext(report,getPropertyId(),entityGroup);
    }
        
}
