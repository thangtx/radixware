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
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Restrictions;


abstract class PropContext extends Context {

    private final Id propId;

    PropContext(final SessionRequest rq, final Id propId, final org.radixware.schemas.eas.PropertyList groupProps) {
        super(rq,groupProps);
        this.propId = propId;
    }

    protected final RadConditionDef.Prop2ValueCondition calcContextProperties(){
        return RadConditionDef.Prop2ValueCondition.fromParentRefProperty(getContextRefProperty(), 
                                                                         getParentTitlePropClassDef(), 
                                                                         getParentTitlePropPresentation(), 
                                                                         getSelectorPresentation());        
    }

    /**
     * @return the propId
     */
    Id getPropertyId() {
        return propId;
    }

    @Override
    final Restrictions getRestrictions() {
        return Restrictions.ZERO;
    }

    @Override
    RadClassDef getClassDef() {
        return rq.getArte().getDefManager().getClassDef(getContextRefProperty().getDestinationClassId());
    }

    @Override
    final List<Id> getCreationEditorPresentationIds() {
        return getParentTitlePropPresentation().getParentCreationEditorPresentationIds(
                getParentTitlePropClassDef().getPresentation());
    }

    abstract RadClassDef getParentTitlePropClassDef();

    abstract IRadClassInstance getChildInstForParentTitleRef();

    abstract RadParentTitlePropertyPresentationDef getParentTitlePropPresentation();
    
    abstract IRadClassInstance getContextPropOwner();

    @Override
    RadSelectorPresentationDef getSelectorPresentation() {
        final RadParentTitlePropertyPresentationDef parentTitlePropPres = getParentTitlePropPresentation();
        final RadClassDef ownerClassDef = getParentTitlePropClassDef();
        final IRadRefPropertyDef refProp = getContextRefProperty();
        return calcSelectorPresentation(refProp, ownerClassDef, parentTitlePropPres);
    }

    static RadSelectorPresentationDef calcSelectorPresentation(final IRadRefPropertyDef refProp, final RadClassDef ownerClassDef, final RadParentTitlePropertyPresentationDef parentTitlePropPres) {
        final RadClassDef destClassDef = ownerClassDef.getRelease().getClassDef(refProp.getDestinationClassId());
        final Id selPresId = parentTitlePropPres.getParentSelectorPresentationId(ownerClassDef.getPresentation());
        if (selPresId != null) {
            return destClassDef.getPresentation().getSelectorPresentationById(selPresId);
        } else if (destClassDef.getPresentation().isDefaultSelectorPresentationDefined()) {
            return destClassDef.getPresentation().getDefaultSelectorPresentation();
        } else {
            return null;
        }
    }
}
