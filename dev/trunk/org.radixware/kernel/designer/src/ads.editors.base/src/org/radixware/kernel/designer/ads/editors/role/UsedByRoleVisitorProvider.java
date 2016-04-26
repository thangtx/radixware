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

package org.radixware.kernel.designer.ads.editors.role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.repository.Layer;


public class UsedByRoleVisitorProvider extends AdsVisitorProvider {

    private AdsDefinition object;
    private List<AdsRoleDef> finishRoleList;
    private Boolean searchAll;
    private Set<Layer> disabledLayers = new HashSet();
    private Set<Layer> enabledLayers = new HashSet();

    public UsedByRoleVisitorProvider(
            AdsDefinition object,
            List<AdsRoleDef> finishRoleList,
            Boolean searchAll) {
        this.object = object;
        this.finishRoleList = finishRoleList;
        this.searchAll = searchAll;
    }

    @Override
    public boolean isTarget(RadixObject obj) {

        //if ((obj instanceof AdsRoleDef) || (obj instanceof AdsEntityClassDef))
        //    return true;//System.out.println(obj.getQualifiedName());
        if (!(obj instanceof AdsRoleDef))//it's role
        {
            return false;
        }
        if (!(searchAll ? true : !finishRoleList.contains((AdsRoleDef) obj))) {
            return false;
        }


        AdsRoleDef role = (AdsRoleDef) obj;
        if (EDrcPredefinedRoleId.SUPER_ADMIN.getValue().equals(role.getId().toString()))//not SuperAdmin
        {
            return false;
        }

        //not read only
        if (role.isReadOnly()/* && !searchAll*/) {
            return false;
        }

        Layer currLayer = role.getModule().getSegment().getLayer();
        if (!enabledLayers.contains(currLayer)) {
            if (disabledLayers.contains(currLayer)) {
                return false;
            }
            else{
                if (currLayer.listFinalBaseLayers().contains(object.getModule().getSegment().getLayer())){
                    enabledLayers.add(currLayer);
                }
                else{
                    disabledLayers.add(currLayer);
                    return false;
                }
            }
        }


        //role see it's object
//        DefinitionSearcher adsSearcher = AdsSearcher.Factory.newAdsDefinitionSearcher(role);
//        if (adsSearcher.findById(object.getId()) == null) {
//            return false;
//        }

        return true;
    }
}
