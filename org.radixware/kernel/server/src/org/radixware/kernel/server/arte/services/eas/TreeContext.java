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
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadParentRefExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Restrictions;


abstract class TreeContext extends Context{
    
    protected final Id explorerItemId;
    private RadConditionDef.Prop2ValueCondition contextProperties;

    TreeContext(final SessionRequest rq, final Id explorerItemId, final org.radixware.schemas.eas.PropertyList groupProps) {
        super(rq,groupProps);
        this.explorerItemId = explorerItemId;        
    };
    
    private RadConditionDef.Prop2ValueCondition calcFinalCondition(){
        final RadExplorerItemDef explorerItem = getExplorerItem();
        if (explorerItem instanceof RadSelectorExplorerItemDef){
            return RadConditionDef.Prop2ValueCondition.fromExplorerItem((RadSelectorExplorerItemDef)explorerItem);
        }else{
            return RadConditionDef.Prop2ValueCondition.EMPTY_CONDITION;
        }
    }

    @Override
    final RadClassDef getClassDef(){
        final RadExplorerItemDef explorerItem = getExplorerItem();
        if (explorerItem instanceof RadSelectorExplorerItemDef) {
            return ((RadSelectorExplorerItemDef) explorerItem).getSelectionClassDef();
        }else if (explorerItem instanceof RadParentRefExplorerItemDef){
            return ((RadParentRefExplorerItemDef) explorerItem).getParentClassDef();
        } else
            return null;
    }


    @Override
    final RadClassPresentationDef.ClassCatalog getClassCatalog(){
        final RadExplorerItemDef explorerItem = getExplorerItem();
        if (explorerItem instanceof RadSelectorExplorerItemDef) {
            return ((RadSelectorExplorerItemDef) explorerItem).getClassCatalog();
        }
        return null;
    }
	@Override
    final List<Id> getCreationEditorPresentationIds(){
        final RadExplorerItemDef explorerItem = getExplorerItem();
        if (explorerItem instanceof RadSelectorExplorerItemDef) {
            return ((RadSelectorExplorerItemDef) explorerItem).getSelectorPresentation().getCreationEditorPresentationIds();
        }
        return null;
    }

    @Override
    Restrictions getRestrictions() {
        return getExplorerItem().getRestrictions();
    }

    @Override
    DdsReferenceDef getContextReference() {
        return getExplorerItem().getContextReference();
    }
    @Override
    IRadRefPropertyDef getContextRefProperty(){
        return null;
    }

    @Override
    public final RadConditionDef.Prop2ValueCondition getContextProperties() {
        if (contextProperties==null){
            contextProperties = calcFinalCondition();
        }
        return contextProperties;
    }    

    @Override
    final RadSelectorPresentationDef getSelectorPresentation(){
        final RadExplorerItemDef ei = getExplorerItem();
        if (ei instanceof RadSelectorExplorerItemDef)
            return ((RadSelectorExplorerItemDef)ei).getSelectorPresentation();
        return null;
    }

    abstract protected RadExplorerItemDef getExplorerItem();

    @Override
    EntityGroup.Context buildEntGroupContext() {
        return new EntityGroup.TreeContext(getClassDef(), getExplorerItem(), getContextObjectPid());
    }
}
