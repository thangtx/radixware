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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.server.arte.Arte;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.*;

abstract class EasRequest {

    protected ExplorerAccessService presenter;

    EasRequest(final ExplorerAccessService presenter) {
        this.presenter = presenter;
    }
    
    protected final Arte getArte() {
        return presenter.getArte();
    }
    
    protected final List<Id> getAccessibleExplorerItems(final RadExplorerRootDef root) {
        final List<Id> result = new LinkedList<>();
        final List<RadParagraphExplorerItemDef> processedParags = new ArrayList<>();
        collectAccessibleExplorerItems(root, root, result, processedParags, getArte().getRights().getCurUserAllRolesInAllAreas());
        return result;
    }
    
    private boolean collectAccessibleExplorerItems(
            final RadExplorerRootDef root,
            final RadParagraphExplorerItemDef parag,
            final List<Id> accessibleExpItems,
            final List<RadParagraphExplorerItemDef> processedParags,
            final List<Id> allUserRoles) {
        if (processedParags.contains(parag)) // защита от рекурсии
        {
            return true;
        }
        processedParags.add(parag);
        
        boolean someChildAccessible = false;
        for (RadExplorerItemDef child : parag.getChildren()) {
            if (child instanceof RadParagraphExplorerItemDef &&
                collectAccessibleExplorerItems(root, ((RadParagraphExplorerItemDef) child), accessibleExpItems, processedParags, allUserRoles)
               ){
                someChildAccessible = true;
                continue; // уже добавлен
            }
            if (root.getCurUserCanAccessItemById(child.getId())) {
                if (child instanceof RadSelectorExplorerItemDef) {
                    try {
                        final List<Id> applicableRoles = calcExplorerItemApplicableRoles((RadSelectorExplorerItemDef) child, allUserRoles);
                        final RadSelectorPresentationDef selPres = ((RadSelectorExplorerItemDef) child).getSelectorPresentation();
                        if (selPres.getTotalRestrictions(applicableRoles).getIsAccessRestricted()){
                            continue;
                        }
                    } catch (DefinitionNotFoundError e) {
                        continue;//RADIX-4918
                    }
                }
                someChildAccessible = true;
                accessibleExpItems.add(child.getId());
            }
        }
        if (someChildAccessible) {
            accessibleExpItems.add(parag.getId());
            return true;
        }
        return false;
    }    
    
    private List<Id> calcExplorerItemApplicableRoles(final RadSelectorExplorerItemDef explorerItem, final List<Id> allUserRoles){
        final RadConditionDef.Prop2ValueCondition contextProperties = RadConditionDef.Prop2ValueCondition.fromExplorerItem(explorerItem);
        if (contextProperties.hasAccessAreas(getArte(), explorerItem.getSelectionClassDef())){
            return contextProperties.getCurUserRoleIds(getArte(), explorerItem.getSelectionClassDef());
        }else{
            return allUserRoles;
        }
    }

    abstract protected String getUsrDbTraceProfile();

    abstract void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault;

    abstract XmlObject process(XmlObject rq) throws ServiceProcessFault, InterruptedException;
//Constants
    protected static final String XSD = "http://schemas.radixware.org/eas.xsd";
}
