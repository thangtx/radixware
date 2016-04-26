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

import org.radixware.schemas.eas.Actions;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.schemas.eas.Presentation;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.ReadRs;

abstract class ObjectRequest extends SessionRequest {

    ObjectRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    protected final Entity getObject(final RadClassDef def, final org.radixware.schemas.eas.ObjectRequest rq) throws ServiceProcessFault, InterruptedException {
        try {
            final Entity object = getArte().getEntityObject(new Pid(getArte(), def.getEntityId(), rq.getPID()));
            object.setReadRights(true);
            return object;
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }
    }

    protected final void writeProps(
            final PropertyList propsXml, final PresentationEntityAdapter<? extends Entity> presEntAdapter,
            final boolean bWithLob, final RadPresentationDef pres,
            final Context context,
            final Collection<RadPropDef> propDefs) {
        for (RadPropDef prop : propDefs) {
            if (presEntAdapter.getRawEntity().isInDatabase(false) || prop.getValType() != EValType.OBJECT) // Object properties are not supported before object created
            {
                addPropXml(propsXml, presEntAdapter, pres, context, prop, bWithLob);
            }
        }
    }

    protected final void writeReadResponse(
            final ReadRs to,
            final PresentationEntityAdapter<? extends Entity> presEntAdapter,
            final PresentationOptions context,
            final boolean bWithLob,
            final RadEditorPresentationDef pres,
            final Collection<RadPropDef> propDefs) throws ServiceProcessFault, InterruptedException {
        final org.radixware.schemas.eas.Object dataXml = to.addNewData();
        final Entity entity = presEntAdapter.getRawEntity();
        if (entity.isInDatabase(false)) {
            dataXml.setPID(entity.getPid().toString());
            try {
                dataXml.setTitle(entity.calcTitle(pres.getObjectTitleFormat()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't generate the title");
            }
        }
        dataXml.setClassId(entity.getRadMeta() != null ? entity.getRadMeta().getId() : RadClassDef.getEntityClassIdByTableId(entity.getDdsMeta().getId()));

        writeProps(dataXml.addNewProperties(), presEntAdapter, bWithLob, pres, context.context, propDefs);
        if (pres != null) {
            final Presentation presXml = to.addNewPresentation();
            presXml.setId(pres.getId());
            presXml.setClassId(pres.getClassPresentation().getClassId());
        }

        try {
            final Actions disActXml = to.addNewDisabledActions();
            if (writeDisbaledObjActions(disActXml, presEntAdapter, pres, context, null) == 0) {
                to.unsetDisabledActions();
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't check object command is disabled");
        }
        
        if (context.selectorPresentation!=null){
            
            final ESelectorRowStyle rowStyle;
            final  ColorScheme colorScheme = presenter.getColorScheme(getArte(), entity.getRadMeta(), context.selectorPresentation.getId());
            if (colorScheme==null){
                rowStyle = presEntAdapter.calcSelectorRowStyle();
            }else{
                rowStyle = colorScheme.apply(entity);
            }
            if (rowStyle!=null){
                dataXml.setRowStyle(rowStyle);
            }            
        }
    }

    protected final void checkPropsAccessibleInPres(final List<RadPropDef> props, final RadEditorPresentationDef pres) throws ServiceProcessClientFault {
        for (RadPropDef prop : props) {
            if (pres.isPropertyForbidden(prop.getId())) {
                throw EasFaults.newDefinitionAccessViolationFault(getArte(), 
                                                                  Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_PROPERTY, 
                                                                  "\"" + prop.getName() + "\" (#" + prop.getId() + ") " + MultilingualString.get(getArte(), Messages.MLS_OWNER_ID, Messages.MLS_ID_IN_PRESENTATION) + " \"" + pres.getName() + "\" (#" + pres.getId() + ")",
                                                                  EDefType.CLASS_PROPERTY,
                                                                  new Id[]{pres.getClassPresentation().getId(),prop.getId()});
            }
        }
    }
}
