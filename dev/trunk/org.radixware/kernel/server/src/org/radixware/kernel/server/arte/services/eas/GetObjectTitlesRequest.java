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

import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef;
import org.radixware.kernel.server.meta.presentations.RadPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.schemas.eas.GetObjectTitleResultStateEnum;
import org.radixware.schemas.eas.GetObjectTitlesMess;
import org.radixware.schemas.eas.GetObjectTitlesRq;
import org.radixware.schemas.eas.GetObjectTitlesRs;
import org.radixware.schemas.easWsdl.GetObjectTitlesDocument;

final class GetObjectTitlesRequest extends SessionRequest {
    
    private static interface ITitleCalculator{
        String calcTitle(Entity entity);
    }
    
    private static class SimpleTitleCalculator implements ITitleCalculator {
        
        private final RadEntityTitleFormatDef format;
        
        public SimpleTitleCalculator(RadEntityTitleFormatDef titleFormat){
            this.format = titleFormat;
        }

        @Override
        public String calcTitle(final Entity entity) {
            return entity.calcTitle(format);
        }
    }
    
    private class TitleInSelectorPresentation implements ITitleCalculator{
        
        private final RadSelectorPresentationDef selectorPresentation;
        
        public TitleInSelectorPresentation(RadSelectorPresentationDef presentation){
            selectorPresentation = presentation;
        }

        @Override
        public String calcTitle(final Entity entity) {
            final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);
            final RadEntityTitleFormatDef titleFormat = 
                getActualEditorPresentation(presEntAdapter, selectorPresentation.getEditorPresentations(), true).getObjectTitleFormat();
            return entity.calcTitle(titleFormat);
        }        
    }
    
    private class TitleInParentRefProperty implements ITitleCalculator{
        
        private final IRadClassInstance ptOwner;
        private final RadPropDef ptProp;
        private final RadPresentationDef pres;
        
        public TitleInParentRefProperty(final IRadClassInstance ptOwner, final Id propertyId, final RadPresentationDef pres){
            this.ptOwner = ptOwner;
            this.ptProp = ptOwner.getRadMeta().getPropById(propertyId);
            this.pres = pres;
        }

        @Override
        public String calcTitle(final Entity entity) {
            return getParentInfo(ptOwner, ptProp, entity, pres).title;
        }
    }
    

    GetObjectTitlesRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    public final GetObjectTitlesDocument process(final GetObjectTitlesMess request) throws ServiceProcessFault, InterruptedException {        
        final GetObjectTitlesRq rqParams = request.getGetObjectTitlesRq();
        final DdsTableDef tab = getEntityTable(rqParams);
        final ITitleCalculator defaultTitleCalculator = getTitleCalculator(rqParams.getContext(), tab.getId());

        final GetObjectTitlesRq.Objects objectsXml = rqParams.getObjects();

        final GetObjectTitlesDocument res = GetObjectTitlesDocument.Factory.newInstance();
        final GetObjectTitlesRs rsStruct = res.addNewGetObjectTitles().addNewGetObjectTitlesRs();
        final GetObjectTitlesRs.ObjectTitles objectTitlesXml = rsStruct.addNewObjectTitles();

        GetObjectTitlesRq.Objects.Item objXml;        
        Entity entity;
        final Map<Entity,ITitleCalculator> calculators = new HashMap<>();
        for (int i = 0; i < objectsXml.sizeOfItemArray(); i++) {
            objXml = objectsXml.getItemArray(i);
            try {
                entity = getArte().getEntityObject(new Pid(getArte(), tab, objXml.getPID()));
                try {
                    if (!entity.getCurUserApplicableRoleIds().isEmpty()) {                        
                        final ITitleCalculator titleCalculator;
                        if (objXml.isSetContext()){
                            titleCalculator = getTitleCalculator(objXml.getContext(),tab.getId());
                        }else{
                            titleCalculator = defaultTitleCalculator;
                        }
                        calculators.put(entity, titleCalculator);
                    } else {
                        registerErrorState(objectTitlesXml, objXml.getPID(), GetObjectTitleResultStateEnum.ACCESS_DENIED);
                    }
                } catch (EntityObjectNotExistsError e) {
                    registerErrorState(objectTitlesXml, objXml.getPID(), GetObjectTitleResultStateEnum.OBJECT_NOT_FOUND);
                } catch (Throwable e) {
                    throw EasFaults.exception2Fault(getArte(), e, "Can't generate the title");
                }
            } catch (EntityObjectNotExistsError e) {
                registerErrorState(objectTitlesXml, objXml.getPID(), GetObjectTitleResultStateEnum.OBJECT_NOT_FOUND);
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
            }
        }
        getArte().switchToReadonlyTransaction();//to prevent modification on title calculation
        GetObjectTitlesRs.ObjectTitles.Item objTitleXml;
        for (Map.Entry<Entity,ITitleCalculator> entry: calculators.entrySet()){
            final String title = entry.getValue().calcTitle(entry.getKey());
            objTitleXml = objectTitlesXml.addNewItem();
            objTitleXml.setPID(entry.getKey().getPid().toString());
            objTitleXml.setTitle(title);
            objTitleXml.setState(GetObjectTitleResultStateEnum.OK);
        }
        return res;
    }
    
    private static void registerErrorState(final GetObjectTitlesRs.ObjectTitles objectTitlesXml, final String objectPid, final GetObjectTitleResultStateEnum.Enum state){
        final GetObjectTitlesRs.ObjectTitles.Item objTitleXml = objectTitlesXml.addNewItem();
        objTitleXml.setPID(objectPid);
        objTitleXml.setState(state);
    }
    
    private ITitleCalculator getTitleCalculator(final org.radixware.schemas.eas.Context xmlContext, final Id tableId) throws InterruptedException{
        if (xmlContext==null || xmlContext.isSetTreePath()){
            final Id classId = RadClassDef.getEntityClassIdByTableId(tableId);
            final RadEntityTitleFormatDef titleFormat = 
                getArte().getDefManager().getClassDef(classId).getPresentation().getDefaultObjectTitleFormat();
            return new SimpleTitleCalculator(titleFormat);
        }else if (xmlContext.isSetEditor()){
            final RadClassDef classDef = getArte().getDefManager().getClassDef(xmlContext.getEditor().getClassId());
            final RadEditorPresentationDef presentation = 
                classDef.getPresentation().getEditorPresentationById(xmlContext.getEditor().getPresentationId());
            return new SimpleTitleCalculator(presentation.getObjectTitleFormat());
        }else if (xmlContext.isSetSelector()){
            final RadClassDef classDef = getArte().getDefManager().getClassDef(xmlContext.getSelector().getClassId());
            final RadSelectorPresentationDef presentation = 
                classDef.getPresentation().getSelectorPresentationById(xmlContext.getSelector().getPresentationId());
            return new TitleInSelectorPresentation(presentation);
        }else {
            final Context context = getContext(xmlContext);
            context.checkAccessible();
            if (context instanceof PropContext){
                final PropContext propContext = (PropContext)context;
                final RadEditorPresentationDef presentation;
                if (context instanceof ObjPropContext){
                    presentation = ((ObjPropContext)context).getContextPropOwnerEdPres();
                }else{
                    presentation = null;
                }
                final Id propertyId = ((PropContext)context).getPropertyId();
                return new TitleInParentRefProperty(propContext.getContextPropOwner(), propertyId, presentation);
            }else{
                throw new IllegalArgumentException("Unsupported context type "+context.getClass().getName());
            }
        }        
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        GetObjectTitlesDocument doc = null;
        try{
            doc = process((GetObjectTitlesMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getGetObjectTitles().getGetObjectTitlesRs());
        }
        return doc;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((GetObjectTitlesMess) rqXml).getGetObjectTitlesRq());
    }
}
