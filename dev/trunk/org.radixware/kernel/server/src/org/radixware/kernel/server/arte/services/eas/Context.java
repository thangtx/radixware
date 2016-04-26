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
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.services.eas.SessionRequest.PropVal;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PropValHandler;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.PresentationContext;


abstract class Context {
    
    protected final SessionRequest rq;
    private final org.radixware.schemas.eas.PropertyList groupPropsXml;

    Context(final SessionRequest rq, final org.radixware.schemas.eas.PropertyList groupProperties){
        this.rq = rq;
        this.groupPropsXml = groupProperties;
    }
    
    /**
     * Request objects class determined from context
     * @return
     */
	abstract RadClassDef getClassDef();

	abstract RadClassPresentationDef.ClassCatalog getClassCatalog();
	abstract List<Id> getCreationEditorPresentationIds();

    abstract Restrictions getRestrictions();

    abstract RadSelectorPresentationDef getSelectorPresentation();
    
    public RadConditionDef.Prop2ValueCondition getContextProperties(){        
        final RadSelectorPresentationDef presentationDef = getSelectorPresentation();
        if (presentationDef==null || presentationDef.getCondition()==null){
            return RadConditionDef.Prop2ValueCondition.EMPTY_CONDITION;
        }else{
            return presentationDef.getCondition().getProp2ValueCondition();
        }
    }
    
    public final void writeGroupProperties(final EntityGroup group){
        if (groupPropsXml!=null && groupPropsXml.getItemList()!=null && !groupPropsXml.getItemList().isEmpty()){
            final PropValHandlersByIdMap propVals = new PropValHandlersByIdMap();
            for (org.radixware.schemas.eas.PropertyList.Item propValXml : groupPropsXml.getItemList()) {
                final PropVal propVal = rq.getPropVal(propValXml, group.getRadMeta());
                propVal.writeTo(propVals);
            }
            for (Map.Entry<Id, PropValHandler> e : propVals.entrySet()) {
                group.setProp(e.getKey(), e.getValue().getValue());
            }                        
        }
    }    

    static enum EContextRefRole{
        CHILDREN_SCOPE,
        PARENT_REF,
        NONE
    };
    abstract EContextRefRole getContextReferenceRole();
    /**
     * valid if getContextReferenceRole()!= null
     * @return
     */
    abstract DdsReferenceDef getContextReference();

    abstract Pid getContextObjectPid();

    abstract IRadRefPropertyDef getContextRefProperty();

    abstract void checkAccessible();
    
    abstract EntityGroup.Context buildEntGroupContext();
    
    abstract PresentationContext getPresentationContext(final EntityGroup entityGroup);

    final static EntityGroup.Context asEntGroupContext(final Context context){
        if (context == null)
            return null;
        return context.buildEntGroupContext();
    }
}
