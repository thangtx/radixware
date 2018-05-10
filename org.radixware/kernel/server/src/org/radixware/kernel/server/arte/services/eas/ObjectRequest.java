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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.EntityPropertyPresentationContext;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.Presentation;
import org.radixware.schemas.eas.PropertyList;

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
            final Collection<RadPropDef> propDefs,
            final boolean writeFullObjectValues) throws ServiceProcessFault, InterruptedException {
        for (RadPropDef prop : propDefs) {
            if (prop.getValType()==EValType.OBJECT && writeFullObjectValues){
                final Entity value = (Entity)presEntAdapter.getProp(prop.getId());
                if (value!=null && value.isNewObject() /*RADIX-12963*/ && presEntAdapter.getEntity().getPropHasOwnVal(prop.getId())){
                    final org.radixware.schemas.eas.PresentableObject xmlValue = 
                        writePropertyObjectValue(prop, value, presEntAdapter.getEntity(), pres, context, bWithLob);
                    if (xmlValue!=null){
                        final org.radixware.schemas.eas.PropertyList.Item propertyItem = propsXml.addNewItem();
                        propertyItem.setId(prop.getId());
                        propertyItem.setObj(xmlValue);
                        continue;
                    }
                }
            }
            addPropXml(propsXml, presEntAdapter, pres, context, prop, bWithLob);
        }
    }
    
    private org.radixware.schemas.eas.PresentableObject writePropertyObjectValue(final RadPropDef objectProp, 
                                                                                                                            final Entity value, 
                                                                                                                            final Entity ownerObject,
                                                                                                                            final RadPresentationDef ownerPres,
                                                                                                                            final Context ownerObjectContext,
                                                                                                                            final boolean bWithLob) throws ServiceProcessFault, InterruptedException{
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(value);
        final PresentationContext presCtx = new EntityPropertyPresentationContext(ownerObject, objectProp.getId(), null, null);
        presEntAdapter.setPresentationContext(presCtx);        
        final RadClassDef ownerClassDef = ownerObject.getRadMeta();
        final RadClassPresentationDef ownerClassPres = ownerClassDef.getPresentation();        
        final RadParentTitlePropertyPresentationDef propPres = 
            (RadParentTitlePropertyPresentationDef) ownerPres.getPropPresById(ownerClassPres, objectProp.getId());
        final List<RadEditorPresentationDef> presentations = 
            new LinkedList<>(propPres.getParentEditorPresentations(ownerClassPres));
        final RadEditorPresentationDef pres = getActualEditorPresentation(presEntAdapter, presentations, false);
        if (pres==null){
            return null;
        }else{
            final org.radixware.schemas.eas.PresentableObject xml = org.radixware.schemas.eas.PresentableObject .Factory.newInstance();
            final ObjPropContext propCtx = 
                new ObjPropContext(this, objectProp.getId(), pres.getId(), ownerObject, ownerObjectContext);
            final PresentationOptions options = new PresentationOptions(this, propCtx, null, pres);
            final Collection<RadPropDef> usedPropDefs = pres.getUsedPropDefs(value.getPresentationMeta());
            writePresentableObject(xml, presEntAdapter, options, bWithLob, pres, usedPropDefs, true);
            return xml;
        }
    }

    protected final void writePresentableObject(
            final org.radixware.schemas.eas.PresentableObject dataXml,
            final PresentationEntityAdapter<? extends Entity> presEntAdapter,
            final PresentationOptions context,
            final boolean bWithLob,
            final RadEditorPresentationDef pres,
            final Collection<RadPropDef> propDefs,
            final boolean writeFullObjectValues) throws ServiceProcessFault, InterruptedException {        
        final Entity entity = presEntAdapter.getRawEntity();
        if (entity.isInDatabase(false)) {
            dataXml.setPID(entity.getPid().toString());
            try {
                dataXml.setTitle(entity.calcTitle(pres.getObjectTitleFormat()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't generate the title");
            }
        }
        final Entity srcEntity = entity.getInitSrc();
        if (srcEntity!=null){
            dataXml.setSrcPID(srcEntity.getPid().toString());
        }
        dataXml.setClassId(entity.getRadMeta() != null ? entity.getRadMeta().getId() : RadClassDef.getEntityClassIdByTableId(entity.getDdsMeta().getId()));

        writeProps(dataXml.addNewProperties(), presEntAdapter, bWithLob, pres, context.context, propDefs, writeFullObjectValues);
        final Presentation presXml = dataXml.addNewPresentation();
        presXml.setId(pres.getId());
        presXml.setClassId(pres.getClassPresentation().getClassId());

        try {
            final Actions disActXml = dataXml.addNewDisabledActions();
            if (writeDisbaledObjActions(disActXml, presEntAdapter, pres, context, null) == 0) {
                dataXml.unsetDisabledActions();
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't check object command is disabled");
        }
        
        final Restrictions restr = pres.getTotalRestrictions(entity);
        final Restrictions additionalRestrictions = presEntAdapter.getAdditionalRestrictions(pres);
        final Restrictions totalRestrictions;
        if (additionalRestrictions==Restrictions.ZERO){
            totalRestrictions = restr;
        }else{
            totalRestrictions = Restrictions.Factory.sum(restr, additionalRestrictions);
        }
        final org.radixware.schemas.eas.EditorPages enadledEditorPages = dataXml.addNewEnabledEditorPages();
        if (totalRestrictions.getIsAccessRestricted() || totalRestrictions.getIsViewRestricted()) {
            enadledEditorPages.setAll(false);
        } else if (!totalRestrictions.getIsAllEditPagesRestricted()) {
            enadledEditorPages.setAll(true);
        } else {
            enadledEditorPages.setAll(false);
            final Collection<Id> allowedEditPages = totalRestrictions.getAllowedEditPages();
            for (Id id : allowedEditPages) {
                final org.radixware.schemas.eas.EditorPages.Item item = enadledEditorPages.addNewItem();
                item.setId(id);
            }
        }
        
        if (context.selectorPresentation!=null){                        
            final  ColorScheme colorScheme = presenter.getColorScheme(getArte(), entity.getRadMeta(), context.selectorPresentation.getId());
            ESelectorRowStyle rowStyle = colorScheme==null ? null : colorScheme.apply(entity);
            if (rowStyle==null){
                rowStyle = presEntAdapter.calcSelectorRowStyle();
            }
            dataXml.setRowStyle(rowStyle==null ? ESelectorRowStyle.NORMAL : rowStyle);
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
