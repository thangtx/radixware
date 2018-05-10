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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.radixware.kernel.server.arte.Arte;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.*;
import org.radixware.kernel.server.types.EntityGroup;

abstract class EasRequest {

    protected ExplorerAccessService presenter;
    private final Map<String,List<Id>> userRoleIdsCache = new HashMap<>(4);
    private List<Id> cachedCurUserAllRolesInAllAreas;

    protected EasRequest() {}
    
    EasRequest(final ExplorerAccessService presenter) {
        this.presenter = presenter;
    }
    
    protected final Arte getArte() {
        return presenter.getArte();
    }
    
    protected final List<Id> getAccessibleExplorerItems(final RadExplorerRootDef root) {
        final List<Id> result = new LinkedList<>();
        final List<RadParagraphExplorerItemDef> processedParags = new ArrayList<>();
        collectAccessibleExplorerItems(root, root, result, processedParags, getCurUserAllRolesInAllAreas());
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
                        final RadSelectorExplorerItemDef childSelExplItem = (RadSelectorExplorerItemDef) child;
                        final List<Id> applicableRoles = calcExplorerItemApplicableRoles(childSelExplItem, allUserRoles);
                        final RadSelectorPresentationDef selPres = childSelExplItem.getSelectorPresentation();
                        if (selPres.getTotalRestrictions(applicableRoles).getIsAccessRestricted()){
                            continue;
                        }
                        final Id entityId = selPres.getClassPresentation().getClassDef().getEntityId();
                        final EntityGroup entGrp = getArte().getGroupHander(entityId);
                        final EntityGroup.Context entGrpContext = 
                            new EntityGroup.TreeContext(childSelExplItem.getSelectionClassDef(), childSelExplItem, null);
                        entGrp.set(entGrpContext, selPres, null, null, null, null, null);
                        if (entGrp.getAdditionalRestrictions(selPres, applicableRoles).getIsAccessRestricted()){
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
        if (someChildAccessible || root==parag) {
            accessibleExpItems.add(parag.getId());
            return true;
        }
        return false;
    }    
    
    private List<Id> calcExplorerItemApplicableRoles(final RadSelectorExplorerItemDef explorerItem, final List<Id> allUserRoles){
        final RadConditionDef.Prop2ValueCondition contextProperties = RadConditionDef.Prop2ValueCondition.fromExplorerItem(explorerItem);
        if (contextProperties.hasAccessAreas(getArte(), explorerItem.getSelectionClassDef())){
            return getCurUserRoleIds(contextProperties, explorerItem.getSelectionClassDef());
        }else{
            return allUserRoles;
        }
    }
    
    protected final List<Id> getCurUserRoleIds(final RadConditionDef.Prop2ValueCondition contextProperties, final RadClassDef classDef){
        if (!contextProperties.hasAccessAreas(getArte(), classDef)){
            return getCurUserAllRolesInAllAreas();
        }
        final String cacheKey=classDef.getId()+"-"+contextProperties.getCacheKey();
        List<Id> roleIds = userRoleIdsCache.get(cacheKey);
        if (roleIds==null){
            roleIds = contextProperties.getCurUserRoleIds(getArte(), classDef);
            if (roleIds==null){
                userRoleIdsCache.put(cacheKey, Collections.<Id>emptyList());
            }else{
                userRoleIdsCache.put(cacheKey, roleIds);
            }
        }
        return roleIds;
    }    

    protected final List<Id> getCurUserAllRolesInAllAreas() {
        if (cachedCurUserAllRolesInAllAreas == null) {
            cachedCurUserAllRolesInAllAreas = getArte().getRights().getCurUserAllRolesInAllAreas();
        }
        return cachedCurUserAllRolesInAllAreas;
    }
    
    abstract protected String getUsrDbTraceProfile();

    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault{
        cachedCurUserAllRolesInAllAreas = null;
        userRoleIdsCache.clear();
    }

    abstract XmlObject process(XmlObject rq) throws ServiceProcessFault, InterruptedException;
    
    protected void postProcess(final XmlObject request, final XmlObject response){
        cachedCurUserAllRolesInAllAreas = null;
        userRoleIdsCache.clear();
    }
//Constants
    protected static final String XSD = "http://schemas.radixware.org/eas.xsd";
}
