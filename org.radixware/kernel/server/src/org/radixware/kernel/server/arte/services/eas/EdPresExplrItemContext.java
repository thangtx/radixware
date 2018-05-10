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

import java.util.List;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadParentRefExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.ChildExplorerItemPresentationContext;
import org.radixware.kernel.server.types.presctx.UnknownPresentationContext;


final class EdPresExplrItemContext extends TreeContext{
    private final Id classId;
    private final Id edPresId;
    private final Pid parentPid;

    EdPresExplrItemContext(final SessionRequest rq, final org.radixware.schemas.eas.Context.TreePath.EdPresExplrItem xml, final org.radixware.schemas.eas.PropertyList groupProps){
        this(rq, xml.getClassId(), xml.getEditorPresentationId(), xml.getExplorerItemId(), new Pid(rq.getArte(), rq.getArte().getDefManager().getClassDef(xml.getClassId()).getEntityId(), xml.getParentPid()), groupProps);
    }

    EdPresExplrItemContext(
        final SessionRequest rq,
        final Id classId,
        final Id edPresId,
        final Id explorerItemId,
        final Pid parentPid,
        final org.radixware.schemas.eas.PropertyList groupProps
    ){
      super(rq, explorerItemId, groupProps);
      this.classId = classId;
      this.edPresId = edPresId;
      this.parentPid = parentPid;
    };

    @Override
    protected RadExplorerItemDef getExplorerItem() {
        return rq.getArte().getDefManager().getClassDef(classId).getPresentation().
                getEditorPresentationById(edPresId).findChildExplorerItemById(explorerItemId);
    }

    @Override
    Pid getContextObjectPid() {
        return parentPid;
    }

    @Override
    EContextRefRole getContextReferenceRole() {
        if (getExplorerItem() instanceof RadChildRefExplorerItemDef)
            return EContextRefRole.CHILDREN_SCOPE;
        else if (getExplorerItem() instanceof RadParentRefExplorerItemDef)
            return EContextRefRole.PARENT_REF;
        else
            return EContextRefRole.NONE;
    }

    @Override
    void checkAccessible() {
        final Entity parentObject = getParentObject();
        final List<Id> applicableRoleIds = parentObject.getCurUserApplicableRoleIds();
        final RadEditorPresentationDef parentPresentation = getParentEditorPresentation();
        final Restrictions parentPresRightsRest = parentPresentation.getTotalRestrictions(applicableRoleIds);
        checkParentPresentationAccessible(parentPresentation, parentPresRightsRest);
        
        final PresentationEntityAdapter presEntAdapter = rq.getArte().getPresentationAdapter(parentObject);
        presEntAdapter.setPresentationContext(UnknownPresentationContext.INSTANCE);
        final Restrictions additionalRestrictions = presEntAdapter.getAdditionalRestrictions(parentPresentation);
        checkParentPresentationAccessible(parentPresentation, additionalRestrictions);
    }
    
    private void checkParentPresentationAccessible(final RadEditorPresentationDef parentPresentation, final Restrictions restrictions){
        if (restrictions.getIsAccessRestricted()) {
                throw EasFaults.newDefinitionAccessViolationFault(rq.getArte(), 
                                                                  Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_ED_PRES, 
                                                                  "\"" + parentPresentation.getName() + "\"(#" + parentPresentation.getId() + ")",
                                                                  EDefType.EDITOR_PRESENTATION,
                                                                  new Id[]{parentPresentation.getId()});
        }
        if (restrictions.getIsChildRestricted(getExplorerItem().getId())) {
                throw EasFaults.newDefinitionAccessViolationFault(rq.getArte(), 
                                                                  Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_THIS_EI, 
                                                                  "(#"+String.valueOf(getExplorerItem().getId())+") " + MultilingualString.get(rq.getArte(), Messages.MLS_OWNER_ID, Messages.MLS_ID_IN_PRESENTATION) +" \"" + parentPresentation.getName() + "\"(#" + parentPresentation.getId() + ")",
                                                                  EDefType.EXPLORER_ITEM,
                                                                  new Id[]{parentPresentation.getId(),getExplorerItem().getId()});
        }        
    }

    private RadEditorPresentationDef getParentEditorPresentation() {
        return rq.getArte().getDefManager().getClassDef(classId).getPresentation().getEditorPresentationById(edPresId);
    }

    private Entity getParentObject() {
        return rq.getArte().getEntityObject(getContextObjectPid());
    }

    @Override
    ChildExplorerItemPresentationContext getPresentationContext(final EntityGroup entityGroup) {
        final RadEditorPresentationDef parentPresentation = getParentEditorPresentation();
        final RadSelectorPresentationDef selectorPresentation = getSelectorPresentation();
        final Id selectorPresentationId = selectorPresentation==null ? null : selectorPresentation.getId();
        return new ChildExplorerItemPresentationContext(parentPid, 
                                                                                    parentPresentation, 
                                                                                    explorerItemId,
                                                                                    entityGroup, 
                                                                                    selectorPresentationId);
    }        

    @Override
    public String toString() {
        final Entity parent = getParentObject();
        final RadEditorPresentationDef pres = getParentEditorPresentation();
        final RadExplorerItemDef ei = getExplorerItem();
        return String.valueOf(parent.getRadMeta().getTitle()) + " (#" + parent.getRadMeta().getId().toString() + ") " +
               "PID = " + parent.getPid().toString() + " -> " +
               "Presentation \'" + String.valueOf(pres.getName()) + "\' (#" + pres.getId().toString() + ") -> " +
               "Explorer Item #"+ ei.getId();
    }

}
