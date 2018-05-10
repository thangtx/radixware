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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItem;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.EntityPropertyPresentationContext;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.kernel.server.types.presctx.UnknownPresentationContext;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.PropertyList;

final class ObjPropContext extends PropContext {

    private final Id edPresId;
    private final Entity object;
    private final List<Id> readOnlyPropIds = new LinkedList<>();
    private final Context context;
    private final RadConditionDef.Prop2ValueCondition contextProperties;

    ObjPropContext(final SessionRequest rq,
            final org.radixware.schemas.eas.Context.ObjectProperty xml,
            final org.radixware.schemas.eas.PropertyList groupProps) throws InterruptedException {
        super(rq, xml.getPropertyId(), groupProps);
        edPresId = xml.getEditorPresentationId();
        context = rq.getContext(xml.getObjectContext());
        final Arte arte = rq.getArte();
        final RadClassDef classDef = arte.getDefManager().getClassDef(xml.getObject().getClassId());
        if (xml.getObject().isSetPID()) {
            object = arte.getEntityObject(new Pid(arte, classDef.getEntityId(), xml.getObject().getPID()));
        } else {
            object = (Entity) arte.newObject(classDef.getId());
            final RadEditorPresentationDef edPres = classDef.getPresentation().getEditorPresentationById(edPresId);
            Entity src = null;
            if (xml.getObject().isSetSrcPID()) {
                src = arte.getEntityObject(new Pid(arte, classDef.getEntityId(), xml.getObject().getSrcPID()));
            }

            final List<Id> pkPropIds = getPkPropIds(object);
            final PropValHandlersByIdMap pkValsMap = new PropValHandlersByIdMap();
            rq.writeCurData2Map(classDef, edPres, xml.getObject(), pkValsMap, new SessionRequest.PropValLoadFilter() {

                @Override
                public boolean skip(SessionRequest.PropVal val) {
                    return !pkPropIds.contains(val.prop.getId());
                }
            });

            rq.initNewObject(object, context, edPres, pkValsMap, src, EEntityInitializationPhase.TEMPLATE_EDITING);
        }
        final PresentationEntityAdapter presAdapter = arte.getPresentationAdapter(object);
        object.setReadRights(true);//RADIX-14087 - read rights immediately bacause later we will need them anyway
        rq.writeCurData2Entity(presAdapter, object.getPresentationMeta().getEditorPresentationById(edPresId), xml.getObject(), readOnlyPropIds, SessionRequest.NULL_PROP_LOAD_FILTER);
        if (context != null && context.getContextReferenceRole() == EContextRefRole.CHILDREN_SCOPE && context.getContextReference() != null) {
            for (DdsReferenceDef.ColumnsInfoItem refProp : context.getContextReference().getColumnsInfo()) {
                readOnlyPropIds.add(refProp.getChildColumnId());
            }
        }
        if (getContextRefProperty().getValType() == EValType.OBJECT) {
            contextProperties = RadConditionDef.Prop2ValueCondition.EMPTY_CONDITION;
        } else {
            contextProperties = calcContextProperties();
        }
    }

    ObjPropContext(final SessionRequest rq,
            final Id propertyId,
            final Id edPresId,
            final Entity ownerObject,
            final Context ownerObjectContext) {
        super(rq, propertyId, null);
        this.edPresId = edPresId;
        this.object = ownerObject;
        this.contextProperties = RadConditionDef.Prop2ValueCondition.EMPTY_CONDITION;
        context = ownerObjectContext;
    }

    private List<Id> getPkPropIds(final Entity entity) {
        final List<Id> pkPropIds = new ArrayList<>();

        final DdsPrimaryKeyDef pkDef = entity.getRadMeta().getTableDef().getPrimaryKey();
        if (pkDef == null) {
            return pkPropIds;
        }

        for (DdsIndexDef.ColumnInfo info : pkDef.getColumnsInfo()) {
            pkPropIds.add(info.getColumnId());
        }

        return pkPropIds;
    }

    protected RadEditorPresentationDef getContextPropOwnerEdPres() {
        return getContextPropOwner().getPresentationMeta().getEditorPresentationById(edPresId);
    }

    @Override
    RadClassPresentationDef.ClassCatalog getClassCatalog() {
        final RadClassPresentationDef ptPropOwnerClassPres = getContextPropOwner().getPresentationMeta();
        final Id classCatalogId;
        if (getContextRefProperty().getValType() == EValType.OBJECT) {
            classCatalogId = getContextPropOwnerEdPres().getPropClassCatalogIdByPropId(ptPropOwnerClassPres, getPropertyId());
        } else {
            classCatalogId = getSelectorPresentation().getClassCatalogId();
        }
        if (classCatalogId == null) {
            return null;
        }
        return getClassDef().getPresentation().getClassCatalogById(classCatalogId);
    }

    Collection<Id> getContextPropOwnerReadolyPropIds() {
        return Collections.unmodifiableCollection(readOnlyPropIds);
    }

    Context getContextObjContext() {
        return context;
    }

    @Override
    EContextRefRole getContextReferenceRole() {
        return EContextRefRole.NONE;
    }

    @Override
    DdsReferenceDef getContextReference() {
        return null; // as context ref role is EContextRefRole.NONE
    }

    @Override
    Pid getContextObjectPid() {
        return null; // as context ref role is EContextRefRole.NONE
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

    final List<ColumnsInfoItem> getFixedParentRefProps() {
        List<DdsReferenceDef.ColumnsInfoItem> fixedParentRefProps = null;
        //строим часть ключа ParentTitle которая не должна измениться (поля ReadOnly или прав нет)
        final IRadRefPropertyDef dacRefProp = getContextRefProperty();
        if (getContextPropertyDef() == dacRefProp) { // own or detail's ref prop (not parent's)
            final DdsReferenceDef ptRef = dacRefProp.getReference();
            if (ptRef != null) {
                final Entity ptChildEnt = getContextPropOwner();
                final RadEditorPresentationDef ptChildPres = getContextPropOwnerEdPres();
                //Vector<DdsReferenceDef.ColumnsInfoItem> fixedRefProps = null;
                for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
                    final EEditPossibility refPropEdPsblty;
                    if (dacRefProp instanceof RadDetailPropDef) {
                        final RadDetailPropDef dacDetRefProp = (RadDetailPropDef) dacRefProp;
                        final DdsReferenceDef mdRef = dacDetRefProp.getDetailReference();
                        final Id refPropPresPropId = ptChildEnt.getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, ptChildEnt.getRadMeta(), refProp.getChildColumnId());
                        if (refPropPresPropId != null) {
                            refPropEdPsblty = ptChildPres.getPropEditPossibilityByPropId(ptChildEnt.getPresentationMeta(), refPropPresPropId);
                        } else {
                            refPropEdPsblty = EEditPossibility.ALWAYS;
                        }
                    } else {
                        refPropEdPsblty = ptChildPres.getPropEditPossibilityByPropId(ptChildEnt.getPresentationMeta(), refProp.getChildColumnId());
                    }
                    if (readOnlyPropIds.contains(refProp.getChildColumnId()) || refPropEdPsblty == EEditPossibility.NEVER || ptChildEnt.isInDatabase(false) ? refPropEdPsblty == EEditPossibility.ON_CREATE : refPropEdPsblty == EEditPossibility.ONLY_EXISTING) {
                        if (fixedParentRefProps == null) {
                            fixedParentRefProps = new ArrayList<>();
                        }
                        fixedParentRefProps.add(refProp);
                    }
                }
            }
        }
        if (fixedParentRefProps == null) {
            fixedParentRefProps = Collections.emptyList();
        }
        return Collections.unmodifiableList(fixedParentRefProps);
    }

    RadPropDef getContextPropertyDef() {
        return getContextPropOwner().getRadMeta().getPropById(getPropertyId());
    }

    @Override
    final RadParentTitlePropertyPresentationDef getParentTitlePropPresentation() {
        try {
            return (RadParentTitlePropertyPresentationDef) getContextPropOwnerEdPres().getPropPresById(getParentTitlePropClassDef().getPresentation(), getPropertyId());
        } catch (DefinitionNotFoundError | ClassCastException e) {
            final String preprocessedExStack = rq.getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Parent title #" + getPropertyId() + " is not defined in presentation #" + getContextPropOwnerEdPres().getId(), e, preprocessedExStack);
        }
    }

    @Override
    final IRadClassInstance getChildInstForParentTitleRef() {
        final RadPropDef prop = getContextPropertyDef();
        Entity childInst = getContextPropOwner();
        if (childInst != null && prop instanceof RadParentPropDef) {
            for (Id refPropId : ((RadParentPropDef) prop).getRefPropIds()) {
                childInst = (Entity) childInst.getProp(refPropId);
                if (childInst == null) {
                    break;
                }
            }
        }
        return childInst;
    }

    @Override
    void checkAccessible() {
        final Entity propOwner = getContextPropOwner();
        final List<Id> applicableRoleIds = propOwner.getCurUserApplicableRoleIds();
        final RadEditorPresentationDef parentPresentation = getContextPropOwnerEdPres();
        final Restrictions parentPresRightsRest = parentPresentation.getTotalRestrictions(applicableRoleIds);
        if (parentPresRightsRest.getIsAccessRestricted()) {
            throw EasFaults.newAccessViolationFault(rq.getArte(), Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_CNTX_ED_PRES, "\"" + parentPresentation.getName() + "\"(#" + parentPresentation.getId() + ")");
        }
        if (getOwnerObjectPresentationAdapter().getAdditionalRestrictions(parentPresentation).getIsAccessRestricted()) {
            throw EasFaults.newAccessViolationFault(rq.getArte(), Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_CNTX_ED_PRES, "\"" + parentPresentation.getName() + "\"(#" + parentPresentation.getId() + ")");
        }
    }

    PresentationEntityAdapter getOwnerObjectPresentationAdapter() {
        final PresentationEntityAdapter presEntAdapter = rq.getArte().getPresentationAdapter(getContextPropOwner());
        if (context == null) {
            presEntAdapter.setPresentationContext(UnknownPresentationContext.INSTANCE);
        } else {
            final PresentationContext ownerPresContext = SessionRequest.getPresentationContext(rq.getArte(), context, null);
            presEntAdapter.setPresentationContext(ownerPresContext);
        }
        return presEntAdapter;
    }

    void assertOwnerPropIsEditable() {
        final RadEditorPresentationDef edPres = getContextPropOwnerEdPres();
        if (edPres.getTotalRestrictions(getContextPropOwner()).getIsUpdateRestricted()) {
            throw EasFaults.newAccessViolationFault(rq.getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_CONTEXT_OBJ, MultilingualString.get(rq.getArte(), Messages.MLS_OWNER_ID, Messages.MLS_ID_IN_PRESENTATION) + " \"" + edPres.getName() + "\"(#" + edPres.getId() + ")");
        }
        final EEditPossibility propEdPossibility = edPres.getPropEditPossibilityByPropId(getContextPropOwner().getPresentationMeta(), getPropertyId());
        if (propEdPossibility == EEditPossibility.NEVER || propEdPossibility == EEditPossibility.ON_CREATE && getContextPropOwner().isInDatabase(false)) {
            throw EasFaults.newAccessViolationFault(rq.getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_PROPERTY, "#" + getPropertyId());
        }
    }

    void assertOwnerPropIsEditable(final PresentationEntityAdapter adapter) {
        final RadEditorPresentationDef edPres = getContextPropOwnerEdPres();
        if (adapter.getAdditionalRestrictions(edPres).getIsUpdateRestricted()) {
            throw EasFaults.newAccessViolationFault(rq.getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_CONTEXT_OBJ, MultilingualString.get(rq.getArte(), Messages.MLS_OWNER_ID, Messages.MLS_ID_IN_PRESENTATION) + " \"" + edPres.getName() + "\"(#" + edPres.getId() + ")");
        }
    }

    @Override
    EntityGroup.Context buildEntGroupContext() {
        return new EntityGroup.PropContext(getClassDef(), getContextRefProperty(), getParentTitlePropClassDef().getPresentation(), getParentTitlePropPresentation(), getChildInstForParentTitleRef(), getFixedParentRefProps());
    }

    @Override
    RadClassDef getParentTitlePropClassDef() {
        return getContextPropOwner().getRadMeta();
    }

    @Override
    public RadConditionDef.Prop2ValueCondition getContextProperties() {
        return contextProperties;
    }

    /**
     * @return the object
     */
    @Override
    Entity getContextPropOwner() {
        return object;
    }

    @Override
    EntityPropertyPresentationContext getPresentationContext(final EntityGroup entityGroup) {
        final RadSelectorPresentationDef selectorPresentation = getSelectorPresentation();
        final Id selectorPresentationId = selectorPresentation == null ? null : selectorPresentation.getId();
        return new EntityPropertyPresentationContext(object, getPropertyId(), entityGroup, selectorPresentationId);
    }

    @Override
    public String toString() {
        final RadEditorPresentationDef pres = getContextPropOwnerEdPres();
        final RadPropDef prop = getContextPropertyDef();
        return context == null ? "" : context.toString() + " -> "
                + String.valueOf(object.getRadMeta().getTitle()) + " (#" + object.getRadMeta().getId().toString() + ") "
                + "PID = " + object.getPid().toString() + " -> "
                + "Presentation \'" + String.valueOf(pres.getName()) + "\' (#" + pres.getId().toString() + ") -> "
                + "Property \'" + String.valueOf(prop.getName()) + "\' (#" + prop.getId().toString() + ")";
    }
}
