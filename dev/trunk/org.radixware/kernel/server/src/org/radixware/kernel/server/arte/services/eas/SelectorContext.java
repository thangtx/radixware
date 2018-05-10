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

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.SelectorPresentationContext;


final class SelectorContext extends Context{
    
    static final private Restrictions CONTEXTLESS_SEL_RESTR = Restrictions.Factory.newInstance(ERestriction.UPDATE.getValue().longValue() | ERestriction.CREATE.getValue().longValue() | ERestriction.DELETE.getValue().longValue() | ERestriction.DELETE_ALL.getValue().longValue() | ERestriction.NOT_READ_ONLY_COMMANDS.getValue().longValue(), null, null, null);
    
    public final Id selectorPresentationId;
    public final Id classId;
    
    public SelectorContext(final SessionRequest rq, 
                           final org.radixware.schemas.eas.Context.Selector selCtx,
                           final org.radixware.schemas.eas.PropertyList groupProps
                           ){
        this(rq, selCtx.getClassId(), selCtx.getPresentationId(), groupProps);
    }
    
    public SelectorContext(final SessionRequest rq,                           
                           final Id classId, 
                           final Id selectorPresentationId,
                           final org.radixware.schemas.eas.PropertyList groupProps
                           ){
        super(rq,groupProps);
        this.classId = classId;
        this.selectorPresentationId = selectorPresentationId;
    }

    @Override
    RadClassDef getClassDef() {
        return rq.getArte().getDefManager().getClassDef(classId);
    }

    @Override
    RadClassPresentationDef.ClassCatalog getClassCatalog() {
        return null;
    }

    @Override
    List<Id> getCreationEditorPresentationIds() {
        return Collections.<Id>emptyList();
    }

    @Override
    Restrictions getRestrictions() {
        return CONTEXTLESS_SEL_RESTR;
    }

    @Override
    RadSelectorPresentationDef getSelectorPresentation() {
        return getClassDef().getPresentation().getSelectorPresentationById(selectorPresentationId);
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
        return null;
    }

    @Override
    void checkAccessible() {        
    }

    @Override
    EntityGroup.Context buildEntGroupContext() {
        return new EntityGroup.EmptyContext(getClassDef());
    }

    @Override
    SelectorPresentationContext getPresentationContext(EntityGroup entityGroup) {
        return new SelectorPresentationContext(entityGroup, selectorPresentationId);
    }
    
}
