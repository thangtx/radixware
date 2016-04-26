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

package org.radixware.kernel.common.builder.check.ads.role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.repository.Layer;


@RadixObjectCheckerRegistration
public class AdsRoleChecker extends AdsDefinitionChecker<AdsRoleDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsRoleDef.class;
    }

    protected void fillExplorerItemHashMap(Map<Id, Integer> explorerItems, AdsExplorerItemDef item) {
        if (explorerItems.containsKey(item.getId())) {
            // recursion, don't swear, explorer items error
            return;
        }
        explorerItems.put(item.getId(), null);

        if (item instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) item;
            for (AdsExplorerItemDef it : par.getExplorerItems().getChildren().getLocal()) {
                fillExplorerItemHashMap(explorerItems, it);
            }
        }
    }

    class ParagraphAndMap {

        ParagraphAndMap(AdsParagraphExplorerItemDef par, Map<Id, Integer> map) {
            this.par = par;
            this.map = map;
        }
        Map<Id, Integer> map;
        AdsParagraphExplorerItemDef par;
    }

    private void checkUsesIncorrectTestResources(AdsRoleDef role, Definition obj, IProblemHandler problemHandler) {
        if (role.isAppRole()) {
            return;
        }
        if (currentRoleIsInTestModule) {
            return;
        }
        if (obj.getModule().isTest()) {
            error(role, problemHandler, "Role uses resource of the test module. id - #" + obj.getId().toString());
        }
    }
    private boolean currentRoleIsInTestModule = false;
    
    private static class AdsRoleCheckerCash{
        Layer curLayer;
        AdsRoleDef.RoleResourcesCash currentCash;
    }
    private AdsRoleDef.RoleResourcesCash currentResourcesCash;
    
    
    
    private static void  processExplorerItem(final AdsRoleDef.RoleResourcesCash currentCash, final AdsDefinition def, final Set<AdsDefinition> anitRecursion){
            if (def instanceof AdsParagraphExplorerItemDef) {
                if (anitRecursion.contains(def)){
                    return;
                }
                anitRecursion.add(def);
                final AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) def;
                final List<AdsExplorerItemDef> childsAndThis = new ArrayList<>(par.getExplorerItems().getChildren().get(EScope.ALL));
                childsAndThis.add(par);
                for (AdsExplorerItemDef item : childsAndThis) {
                    processExplorerItem(currentCash, item, anitRecursion);
                    AdsExplorerItemDef oldParagraph = currentCash.allParagraphsMap.get(item.getId());
                    if (oldParagraph == null) {
                        currentCash.allParagraphsMap.put(item.getId(), item);
                    } else {
                        if (AdsDefinition.Hierarchy.isOverwrittenDef(oldParagraph, item)) {
                            currentCash.allParagraphsMap.put(item.getId(), item);
                        }
                    }
                }
            }        
    }
    
    private static void fillCash(final AdsRoleDef.RoleResourcesCash currentCash, final Layer layer) {
    final Set<AdsDefinition> anitRecursion = new HashSet<>(1024);
        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {

            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                for (Module m : layer.getAds().getModules()) {
                    for (AdsDefinition def : ((AdsModule) m).getDefinitions().list()) {
                        processExplorerItem(currentCash, def, anitRecursion);
                    }
                }
            }
        });

    }
    
    private void checkCash(final AdsRoleDef role){
        AdsRoleCheckerCash curCheckerCash = getHistory().findItemByClass(AdsRoleCheckerCash.class);
        Layer curLayer = role.getModule().getSegment().getLayer();
        if (curCheckerCash==null || curCheckerCash.curLayer != curLayer){
            curCheckerCash = new AdsRoleCheckerCash();
            curCheckerCash.curLayer = curLayer;
            curCheckerCash.currentCash = new AdsRoleDef.RoleResourcesCash();
            fillCash(curCheckerCash.currentCash, curCheckerCash.curLayer);
        }
        currentResourcesCash = curCheckerCash.currentCash;
    }

    @Override
    public void check(AdsRoleDef role, IProblemHandler problemHandler) {
        super.check(role, problemHandler);
        currentRoleIsInTestModule = role.getModule().isTest();

        AdsRoleDef overwriteRole = null;
        boolean isFindOverwrite = false;
        
        checkCash(role); 

        // common
//        List<AdsModule> adsModules = new ArrayList<AdsModule>();
//        // List<DdsModule> ddsModules = new ArrayList<DdsModule>();
//        AdsModule module = role.getModule();
//        adsModules.add(module);
//        Definition contextAsDefinition = module;
//
//        List<Dependence> deps = new ArrayList<Dependence>();
//        contextAsDefinition.getDependenceProvider().collect(deps);
//
//        for (Dependence dep : deps) {
//            Module m = dep.findDependenceModule(contextAsDefinition);
//            if (m != null) {
//                if (m instanceof AdsModule && adsModules.indexOf(m) < 0) {
//                    adsModules.add((AdsModule) m);
//                }
////                if (m instanceof DdsModule && ddsModules.indexOf(m) < 0) {
////                    ddsModules.add((DdsModule) m);
////                }
//            }
//        }
        DefinitionSearcher<AdsDefinition> adsSearcher = AdsSearcher.Factory.newAdsDefinitionSearcher(role);
        Map<String, AdsRoleDef.Resource> resources = role.getAllResourceRestrictions();




//        Map<Id, ParagraphAndMap> explorerRoots = new HashMap<Id, ParagraphAndMap>();
//        for (AdsModule m : adsModules) {
//            for (AdsDefinition def : m.getDefinitions().list()) {
//                if (def instanceof AdsParagraphExplorerItemDef) {
//                    Map<Id, Integer> explorerItems = new HashMap<Id, Integer>();
//                    fillExplorerItemHashMap(explorerItems, (AdsParagraphExplorerItemDef) def);
//                    explorerRoots.put(def.getId(), new ParagraphAndMap((AdsParagraphExplorerItemDef) def, explorerItems));
//                }
//            }
//        }
        List<DdsAccessPartitionFamilyDef> lst = role.getDependentAPF();
        // APF
        List<Id> apfList = role.collectOverwriteAPF();

        for (Id id : apfList) {
            boolean isFind = false;
            for (DdsAccessPartitionFamilyDef apf : lst) {
                if (apf.getId().equals(id)) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                error(role, problemHandler, "Not synchronized Access Partition Family list");
                break;
            }
        }


        for (int k = 0; k < apfList.size(); k++) {
            Id id = apfList.get(k);
            DdsAccessPartitionFamilyDef apf = AdsUtils.findTopLevelApfById(role.getModule().getSegment().getLayer(), id);

            if (apf == null) {
                error(role, problemHandler, "Incorrect Access Partition Family id - \"" + id.toString() + "\"");
            } else {
                for (int j = 0; j < k; j++) {
                    if (apfList.get(j).equals(id)) {
                        error(role, problemHandler, "Dublicate Access Partition Family \"" + apf.getQualifiedName() + "\"");
                    }
                }
                checkUsesIncorrectTestResources(role, apf, problemHandler);
            }

        }

        //Ancesstors
        List<Id> ancestorsList = role.collectOverwriteAncestors();
        for (int k = 0; k < ancestorsList.size(); k++) {
            Id id = ancestorsList.get(k);
            AdsRoleDef anc = (AdsRoleDef) adsSearcher.findById(id).get();
            if (anc == null) {
                error(role, problemHandler, "Incorrect ancestor role id - \"" + id.toString() + "\"");
            } else {
                checkUsesIncorrectTestResources(role, anc, problemHandler);
                for (int j = 0; j < k; j++) {
                    if (ancestorsList.get(j).equals(id)) {
                        error(role, problemHandler, "Dublicate ancestor role  \"" + anc.getQualifiedName() + "\"");
                    }
                }
            }
        }


        //Resources
        final Layer layer = role.getModule().getSegment().getLayer();
        layer.getBranch();
        for (AdsRoleDef.Resource res : resources.values()) {
            if (isFindOverwrite) {
                final String hash = AdsRoleDef.generateResHashKey(res);
                final Restrictions curRest = role.getOnlyCurrentResourceRestrictions(hash);
                final Restrictions overwriteRest = overwriteRole.getOnlyCurrentResourceRestrictions(hash);
                if (!Restrictions.contains(overwriteRest, curRest)) {
                    error(role, problemHandler, "Restriction of the rights at role overwriting - \""
                            + res.getResourceContent(role) + "\"");
                }
            }

            if (res.defId == null) {
                error(role, problemHandler, "resource id is null");
            } else if (res.type.equals(EDrcResourceType.SERVER_RESOURCE)) {//NOPMD
                //do nothing
            } else if (res.type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM)) {


                final AdsParagraphExplorerItemDef root = (AdsParagraphExplorerItemDef) res.findTopLevelDef(layer);

                if (root == null) {
                    error(role, problemHandler, "Explorer root #" + res.defId + " not found.");
                } else {
                    if (!root.isRoot()) {
                        error(role, problemHandler, "Explorer paragraph #" + res.defId + " is not root.");
                    }
                    checkUsesIncorrectTestResources(role, root, problemHandler);
                    if (res.subDefId != null) {
                        final AdsDefinition ei = root.getExplorerItems().findChildExplorerItem(res.subDefId);
                        
                        if (ei == null && AdsRoleDef.RoleResourcesCash.findChildExplorerItem(currentResourcesCash, /*root.getExplorerItems(),*/ res.subDefId) == null){
                            error(role, problemHandler,
                                    "Explorer item #"
                                    + res.defId.toString()
                                    + "." + res.subDefId.toString() + " not found.");

                        
                        }
                    }
                }
            } else if (res.type.equals(EDrcResourceType.EDITOR_PRESENTATION)) {

                AdsDefinition def = res.findTopLevelDef(layer);
                if (def == null) {
                    error(role, problemHandler, "Entity class #" + res.defId.toString() + " not found.");
                } else {
                    checkUsesIncorrectTestResources(role, def, problemHandler);
                    AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;
                    


                    AdsEditorPresentationDef editorPresentationDef =
                            classDef.getPresentations().getEditorPresentations().findById(res.subDefId, EScope.ALL).get();
                    if (editorPresentationDef == null) {
                        error(
                                role, problemHandler,
                                "Editor presentation #"
                                + res.defId.toString()
                                + "."
                                + res.subDefId.toString()
                                + " not found.");
                    } else {
                        
                        {
                            ExtendableDefinitions<AdsScopeCommandDef> cmdList = classDef.getPresentations().getCommands();
                            List<Id> idCommandsList = res.restrictions.getEnabledCommandIds();
                            if (idCommandsList != null && !idCommandsList.isEmpty()) {
                                for (Id id : idCommandsList) {
                                    if (cmdList.findById(id, EScope.ALL).get() == null) {
                                        error(
                                                role, problemHandler,
                                                "Editor presentation enable command #"
                                                + res.defId.toString() + "."
                                                + res.subDefId.toString() + "."
                                                + id.toString() + " not found.");
                                    }
                                }
                            }
                        }
                        {
                            List<AdsExplorerItemDef> childList = AdsRoleDef.collectPresentationExplorerItems(editorPresentationDef);
                            List<Id> idList = res.restrictions.getEnabledChildIds();
                            if (idList != null && !idList.isEmpty()) {
                                //role.
                                for (Id id : idList) {
                                    boolean isFind = false;
                                    for (AdsExplorerItemDef ei : childList) {
                                        if (ei.getId().equals(id)) {
                                            isFind = true;
                                            break;
                                        }
                                    }
                                    if (!isFind) {
                                        error(
                                                role, problemHandler,
                                                "Editor presentation enable child #"
                                                + res.defId.toString() + "."
                                                + res.subDefId.toString() + "."
                                                + id.toString() + " not found.");
                                    }
                                }
                            }
                        }
                       {
                            List<Id> idList = res.restrictions.getEnabledPageIds();
                            List<AdsEditorPageDef> pagesList = new ArrayList();
                            if (editorPresentationDef.getEditorPages()!=null){
                                pagesList = editorPresentationDef.getEditorPages().get(EScope.ALL);
                            }
                            
                            if (idList != null && !idList.isEmpty()) {
                                for (Id id : idList) {
                                    
                                    boolean isFind = false;
                                    for (AdsEditorPageDef page : pagesList) {
                                        if (page.getId().equals(id)) {
                                            isFind = true;
                                            break;
                                        }
                                    }
                                    if (!isFind) {
                                        error(
                                                role, problemHandler,
                                                "Editor presentation enable page #"
                                                + res.defId.toString() + "."
                                                + res.subDefId.toString() + "."
                                                + id.toString() + " not found.");
                                    }
                                }
                            }
                        }                        
                        
                        

                        //
                    }

                }
            } else if (res.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {

                AdsDefinition def = res.findTopLevelDef(layer);
                if (def == null) {
                    error(role, problemHandler, "Entity class #" + res.defId.toString() + " not found.");
                } else {
                    AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;

                    AdsSelectorPresentationDef editorSelectorDef =
                            classDef.getPresentations().getSelectorPresentations().findById(res.subDefId, EScope.ALL).get();
                    if (editorSelectorDef == null) {
                        error(
                                role, problemHandler,
                                "Selector presentation #"
                                + res.defId.toString()
                                + "."
                                + res.subDefId.toString()
                                + " not found.");
                    } else {
                        List<Id> idList = res.restrictions.getEnabledCommandIds();
                        if (idList != null && !idList.isEmpty()) {
                            AdsEntityObjectClassDef basic = classDef;
                            while (basic != null) {
                                AdsEntityObjectClassDef basic2 = basic.findBasis();
                                if (basic2 == null) {
                                    break;
                                }
                                basic = basic2;
                            }
                            if (basic == null || !(basic instanceof AdsEntityClassDef)) {
                                error(
                                        role, problemHandler,
                                        "Basic entity for class #" + res.defId.toString() + " not found.");
                            } else {
                                AdsEntityClassDef basicEnt = (AdsEntityClassDef) basic;
                                AdsEntityGroupClassDef group = basicEnt.findEntityGroup();
                                if (group == null) {
                                    error(
                                            role, problemHandler,
                                            "Basic entity group for class #" + res.defId.toString() + " not found.");
                                } else {
                                    ExtendableDefinitions<AdsScopeCommandDef> cmdList = group.getPresentations().getCommands();
                                    for (Id id : idList) {
                                        if (cmdList.findById(id, EScope.ALL).get() == null) {
                                            error(
                                                    role, problemHandler,
                                                    "Selector presentation enable command #"
                                                    + res.defId.toString() + "."
                                                    + res.subDefId.toString() + "."
                                                    + id.toString() + " not found.");
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            } else if (//res.type.equals(EDrcResourceType.CLASS_INSTANTIATOR) ||
                    res.type.equals(EDrcResourceType.CONTEXTLESS_COMMAND)) {
                AdsDefinition def = res.findTopLevelDef(layer);
                if (def == null) {
//                    if (res.type.equals(EDrcResourceType.CLASS_INSTANTIATOR))
//                    {
//                    error(role, problemHandler, "Class #" + res.defId.toString() + " not found.");
//                    }
//                    else
//                    {
                    error(role, problemHandler, "Contextless command #" + res.defId.toString() + " not found.");
//                    }
                } else {
                    checkUsesIncorrectTestResources(role, def, problemHandler);
                }
            } else {
                error(role, problemHandler, "Incorrect resource type  - " + String.valueOf(res.type.getValue()));
            }
        }
    }
}
