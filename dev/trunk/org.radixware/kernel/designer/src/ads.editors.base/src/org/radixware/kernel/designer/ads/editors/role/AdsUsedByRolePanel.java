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

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


public abstract class AdsUsedByRolePanel extends JPanel {

    protected Color backgroundColor;
    protected AdsDefinition object;
    protected Map<AdsRoleDef, Restrictions> startHashMap = new HashMap();
    protected List<Restrictions> finishRestrictionList = new ArrayList();
    protected List<AdsRoleDef> finishRoleList = new ArrayList();
    protected List<Boolean> mayModify = new ArrayList();
    protected String hash;
    protected EDrcResourceType type = null;
    protected Id defId = null;
    protected Id subDefId = null;

    public AdsUsedByRolePanel(RadixObject obj) {
        super();
        backgroundColor = javax.swing.UIManager.getDefaults().getColor("TableHeader.background");
        object = (AdsDefinition) obj;
    }

    public AdsDefinition getObject() {
        return object;
    }

    public void apply() {
        Object[] arr = startHashMap.keySet().toArray();
        for (int i = 0; i < arr.length; i++) {
            AdsRoleDef role = (AdsRoleDef) arr[i];
            if (!finishRoleList.contains(role)) {
                role.RemoveResourceRestrictions(hash);
            }
        }

        int i = 0;
        for (AdsRoleDef role : finishRoleList) {
            Restrictions oldValue = startHashMap.get(role);
            Restrictions newValue = finishRestrictionList.get(i);
            boolean mustCreateOrReplace = true;
            if (oldValue != null) {
                mustCreateOrReplace = !oldValue.equals(newValue);
            }
            if (mustCreateOrReplace) {
                role.CreateOrReplaceResourceRestrictions(
                        new AdsRoleDef.Resource(
                        type,
                        defId, subDefId,
                        newValue));
            }
            i++;
        }

    }

    private static boolean isFirstDefinition(Definition x, Definition y, boolean isName) {
        if (x == null || y == null) {
            return false;
        }
        if (isName) {
            return x.getName().compareTo(y.getName()) < 0;
        }
        return x.getQualifiedName().compareTo(y.getQualifiedName()) < 0;
    }


    private static boolean isEqualDefinition(Definition x, Definition y, boolean isName) {
        if (x == null || y == null) {
            return false;
        }
        if (isName) {
            return x.getName().compareTo(y.getName()) == 0;
        }
        return x.getQualifiedName().compareTo(y.getQualifiedName()) == 0;
    }    
    
    public static <T> int findPlace(final List<T> list, final Definition def, final boolean isName) {
        int cnt = list.size();
        if (cnt == 0) {
            return 0;
        }
        Definition firstDef = (Definition) list.get(0);
        if (isEqualDefinition(def, firstDef, isName)){
            return 0;
        }
        if (isFirstDefinition(def, firstDef, isName)) {
            return 0;
        }
        Definition lastDef = (Definition) list.get(cnt - 1);
        
        if (isEqualDefinition(lastDef, def, isName)){
            return cnt;
        }
        
        if (isFirstDefinition(lastDef, def, isName)) {
            return cnt;
        }
        int a = 0;
        int b = cnt - 1;

        while (true) {
            if (b - a < 2) {
                return a + 1;
            }
            int c = (a + b) / 2;
            Definition currDef = (Definition) list.get(c);
//            if (isEqualDefinition(currDef, def, isName)){
//                return c+1;
//            }
            if (isFirstDefinition(currDef, def, isName)) {
                a = c;
            } else {
                b = c;
            }
        }
    }

    private int findRole(final AdsRoleDef role) {
        int i = 0;
        Iterator<AdsRoleDef> it = finishRoleList.iterator();
        while (it.hasNext()) {
            AdsRoleDef curRole = it.next();
            if (curRole == role) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private Restrictions getOverwriteResourceRestrictions(final AdsRoleDef role, final String resHashKey, boolean checkThis) {
        if (checkThis) {
            int ind = findRole(role);
            Restrictions rest = null;
            if (ind != -1) {
                rest = finishRestrictionList.get(ind);
            } else {
                rest = role.getOnlyCurrentResourceRestrictions(resHashKey);
            }
            if (rest != null) {
                return rest;
            }
        }
        if (role.isOverwrite()) {
            AdsRoleDef r = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
            if (r != null) {
                return getOverwriteResourceRestrictions(r, resHashKey, true);
            }
        }
        return Restrictions.Factory.newInstance(0xffffffff);
    }

    protected Restrictions getOverwriteResourceRestrictions(final AdsRoleDef role, final String resHashKey) {
        return getOverwriteResourceRestrictions(role, resHashKey, false);
    }

    protected void refreshList(AdsDefinition obj) {

//        List<Definition> list = org.radixware.kernel.designer.common.general.utils.DefinitionsUtils.collectTopInside(
//                obj.getModule().getSegment().getLayer().getBranch(),
//                new UsedByRoleVisitorProvider(obj, finishRoleList, true));

        Id superAdminId = Id.Factory.loadFrom(EDrcPredefinedRoleId.SUPER_ADMIN.getValue());
        List<Definition> list = new ArrayList();
        
        final Layer definitionLayer = obj.getModule().getSegment().getLayer();
        
        final Branch branch = definitionLayer.getBranch();
        
        for (Layer layer : branch.getLayers()) {            
            List<Layer>  fi = layer.listFinalBaseLayers();
            if (/*layer != definitionLayer || */ !layer.listFinalBaseLayers().contains(definitionLayer)){
                continue;
            }
            
            for (Module module : layer.getAds().getModules()) {
                AdsModule adsModule = (AdsModule) module;
                for (AdsDefinition def : adsModule.getDefinitions()) {
                    if (def instanceof AdsRoleDef && !superAdminId.equals(def.getId())) {
                        list.add(def);
                        // Роль должна быть из слоя который видит слой текущей  дефиниции RRRRRRRRrrrrrrrrrrrrrrrrrrrrrrrrr
                    }
                }
            }
        }




        startHashMap.clear();
        finishRoleList.clear();
        finishRestrictionList.clear();
        //finishOverwriteRestrictionList.clear();
        mayModify.clear();


        RadixObjectsUtils.sortByQualifiedName(list);
        for (Object r : list) //if (r instanceof AdsRoleDef)
        {
            AdsRoleDef role = (AdsRoleDef) r;



            //Restrictions overvriteRestr = role.getOverwriteResourceRestrictions(hash);

            if (role != null) {
                Restrictions restr = role.getOnlyCurrentResourceRestrictions(hash);
                if (restr != null) {
                    Restrictions newRestr =
                            Restrictions.Factory.newInstance(role, restr.toBitField(),
                            new ArrayList<>(restr.getEnabledCommandIds()),
                            new ArrayList<>(restr.getEnabledChildIds()),
                            new ArrayList<>(restr.getEnabledPageIds()));

                    finishRoleList.add(role);
                    finishRestrictionList.add(newRestr);
                    mayModify.add(!role.isReadOnly());
                    startHashMap.put(role, restr);
                    //break;
                }

            }
        }
    }
}
