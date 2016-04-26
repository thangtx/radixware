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

package org.radixware.kernel.common.defs.ads.rights;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Map.Entry;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.*;
import static org.radixware.kernel.common.defs.ads.AdsDefinitionProblems.DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.radixdoc.RoleRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.role.AdsRoleWriter;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.acsImpexp.AppRolesDocument;
import org.radixware.schemas.acsImpexp.UserDefinedDefs;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.RoleDefinition;
import org.radixware.schemas.radixdoc.Page;


public class AdsRoleDef extends AdsTitledDefinition implements IJavaSource, IOverwritable<AdsRoleDef>, UdsExportable, IRadixdocProvider {

    public static final long FORMAT_VERSION = 2l;
    
    private AdsRoleProblems warningsSupport = null;
    
    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(final boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = instantiateWarningsSupport(this, null);
            }
            return warningsSupport;
        }
    }
    
    private static AdsRoleProblems instantiateWarningsSupport(final AdsRoleDef role, final List<Integer> list) {
        return new AdsRoleProblems(role, list);
    }
    
    private static void saveWarningsSupport(final AdsRoleDef role, final RoleDefinition xDef) {
        synchronized (role) {
            if (role.warningsSupport != null && !role.warningsSupport.isEmpty()) {
                int[] warnings = role.warningsSupport.getSuppressedWarnings();
                final List<Integer> intList = new ArrayList();
                for (int index = 0; index < warnings.length; index++) {
                    intList.add(warnings[index]);
                }
                Collections.sort(intList);
                xDef.setSuppressedWarnings(intList);
            }
        }
    }

    private static void loadWarningsSupport(final AdsRoleDef role, final RoleDefinition xDef) {
        synchronized (role) {
            if (xDef.isSetSuppressedWarnings()) {
                List<Integer> list = xDef.getSuppressedWarnings();
                if (!list.isEmpty()) {
                    role.warningsSupport = instantiateWarningsSupport(role, list);
                }
            }
        }
    }

    @Override
    public boolean isWarningSuppressed(final int code) {
        if (DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN == code){
            
            SearchResult<AdsDefinition> searchResult =getHierarchy().findOverwritten();
            if (searchResult!=null && !searchResult.isEmpty() && searchResult.get() instanceof AdsRoleDef){
                AdsRoleDef priorRole = (AdsRoleDef)searchResult.get();
                if (priorRole.isAbstract != isAbstract){
                    return true;
                }
            }            
        }
        return super.isWarningSuppressed(code);
    }
    
    
    private void collectAncestorsDependences(List<Definition> list) {

        for (AncestorRef ancestor : this.ancestorRefs) {
            AdsRoleDef def = ancestor.find();
            if (def != null) {
                list.add(def);
            }
        }
    }

    private void collectFamiliesDependences(List<Definition> list) {

        Layer layer = this.getModule().getSegment().getLayer();

        for (APFamilyRef apfId : this.apFamilyRefs) {
            DdsDefinition def = apfId.findAPF(layer);
            if (def != null) {
                list.add(def);
            }
        }
    }

    @Override
    public void collectDirectDependences(List<Definition> list) {
        super.collectDirectDependences(list);

        // called in collectDependences
        collectAncestorsDependences(list);
        collectFamiliesDependences(list);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        Layer layer = this.getModule().getSegment().getLayer();
        for (Resource r : this.getResources()) {
            if (!r.type.equals(EDrcResourceType.SERVER_RESOURCE)) {
                AdsDefinition def = r.findTopLevelDef(layer);
                //AdsDefinition def = currSearcher.findById(r.subDefId != null ? r.subDefId : r.defId);
                if (def != null) {
                    if (r.type.equals(EDrcResourceType.CONTEXTLESS_COMMAND)) {
                        list.add(def);
                    } else if (def instanceof AdsEntityObjectClassDef) {
                        AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) def;
                        EntityObjectPresentations presentations = clazz.getPresentations();
                        if (r.type.equals(EDrcResourceType.EDITOR_PRESENTATION)) {
                            AdsEditorPresentationDef pres =
                                    presentations.getEditorPresentations().
                                    findById(r.subDefId, EScope.LOCAL_AND_OVERWRITE).get();
                            if (pres != null) {
                                list.add(pres);
                            }
                            //if (!r.restrictions.isDenied(ERestriction.ACCESS))
                            {
                                // commands
                                if (!r.restrictions.isDenied(ERestriction.ANY_COMMAND)) {
                                    list.addAll(
                                            presentations.getCommands().
                                            get(EScope.LOCAL_AND_OVERWRITE));
                                } else {
                                    List<AdsScopeCommandDef> cmdLst =
                                            presentations.getCommands().
                                            get(EScope.LOCAL_AND_OVERWRITE);
                                    List<Id> idLst = r.restrictions.getEnabledCommandIds();
                                    if (idLst != null) {
                                        for (Id id : idLst) {
                                            for (AdsScopeCommandDef cmd : cmdLst) {
                                                if (cmd.getId().equals(id)) {
                                                    list.add(cmd);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                //childs
                                if (pres != null) {
                                    List<AdsExplorerItemDef> childList =
                                            collectPresentationExplorerItems(pres);
                                    if (!r.restrictions.isDenied(ERestriction.ANY_CHILD)) {
                                        list.addAll(childList);
                                    } else {
                                        List<Id> idLst = r.restrictions.getEnabledChildIds();
                                        if (idLst != null) {
                                            for (Id id : idLst) {
                                                for (AdsExplorerItemDef item : childList) {
                                                    if (item.getId().equals(id)) {
                                                        list.add(item);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (r.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {
                            AdsSelectorPresentationDef pres =
                                    presentations.getSelectorPresentations().
                                    findById(r.subDefId, EScope.LOCAL_AND_OVERWRITE).get();
                            if (pres != null) {
                                list.add(pres);
                            }
                            //if (!r.restrictions.isDenied(ERestriction.ACCESS))
                            {
                                AdsEntityObjectClassDef currClass = clazz;
                                AdsEntityClassDef entity = null;
                                while (currClass != null) {
                                    if (currClass instanceof AdsEntityClassDef) {
                                        entity = (AdsEntityClassDef) currClass;
                                        break;
                                    }
                                    currClass = currClass.findBasis();
                                }
                                if (entity != null) {
                                    List<AdsScopeCommandDef> cmdLst = entity.getPresentations().getCommands().get(EScope.LOCAL_AND_OVERWRITE);
                                    if (!r.restrictions.isDenied(ERestriction.ANY_COMMAND)) {
                                        list.addAll(cmdLst);
                                    } else {
                                        List<Id> idLst = r.restrictions.getEnabledCommandIds();
                                        if (idLst != null) {
                                            for (Id id : idLst) {
                                                for (AdsScopeCommandDef cmd : cmdLst) {
                                                    if (cmd.getId().equals(id)) {
                                                        list.add(cmd);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        } else // if (r.type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM))
                        {
                            if (def instanceof AdsExplorerItemDef) {
                                if (r.subDefId == null) {
                                    list.add(def);
                                } else {
                                    //r.subDefId
                                    List<AdsExplorerItemDef> owner = new ArrayList<>(30);
                                    AdsExplorerItemDef item = findSubItem(owner, (AdsExplorerItemDef) def, r.subDefId);
                                    if (item != null) {
                                        list.add(item);
                                    }
                                }
                            }
                        }
                    } else if (def instanceof AdsExplorerItemDef) {
                        if (r.subDefId == null) {
                            list.add(def);
                        } else {
                            //r.subDefId
                            List<AdsExplorerItemDef> owner = new ArrayList<>(30);
                            AdsExplorerItemDef item = findSubItem(owner, (AdsExplorerItemDef) def, r.subDefId);
                            if (item != null) {
                                list.add(item);
                            }
                        }
                    }
                }
            }
        }

        collectAncestorsDependences(list);
        collectFamiliesDependences(list);
    }

    @Override
    protected void collectDependencesForModule(List<Definition> list) {
        collectAncestorsDependences(list);
    }

    private static AdsExplorerItemDef findSubItem(List<AdsExplorerItemDef> owner, AdsExplorerItemDef root, Id id) {
        if (root.getId().equals(id)) {
            return root;
        }
        if (owner.contains(root)) {
            return null;
        }
        owner.add(root);
        AdsParagraphExplorerItemDef par = null;
        if (root instanceof AdsParagraphExplorerItemDef) {
            par = (AdsParagraphExplorerItemDef) root;
        } else if (root instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) root;
            par = def.findReferencedParagraph();
        }
        if (par != null) {
            for (AdsExplorerItemDef item : par.getExplorerItems().getChildren().get(EScope.LOCAL_AND_OVERWRITE)) {
                AdsExplorerItemDef item2 = findSubItem(owner, item, id);
                if (item2 != null) {
                    return item2;
                }
            }
        }
        owner.remove(owner.size() - 1);
        return null;
    }

    private static void collectPresentationExplorerItems(List<AdsExplorerItemDef> collectLst, AdsExplorerItemDef explorerItem) {
        if (collectLst.contains(explorerItem)) {
            return;
        }
        collectLst.add(explorerItem);

        if (explorerItem instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) explorerItem;
            List<AdsExplorerItemDef> items = par.getExplorerItems().getChildren().get(EScope.ALL);
            for (AdsExplorerItemDef item : items) {
                collectPresentationExplorerItems(collectLst, item);
            }
        } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
            AdsParagraphExplorerItemDef par2 = def.findReferencedParagraph();
            if (par2 != null) {
                List<AdsExplorerItemDef> items = par2.getExplorerItems().getChildren().get(EScope.ALL);
                for (AdsExplorerItemDef item : items) {
                    collectPresentationExplorerItems(collectLst, item);
                }
            }
        }
    }

    public static List<AdsExplorerItemDef> collectPresentationExplorerItems(AdsEditorPresentationDef pres) {
        List<AdsExplorerItemDef> childList = pres.getExplorerItems().getChildren().
                get(EScope.ALL);
        List<AdsExplorerItemDef> collectLst = new ArrayList<>();
        for (AdsExplorerItemDef item : childList) {
            collectPresentationExplorerItems(collectLst, item);
        }
        return collectLst;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsRoleWriter(this, AdsRoleDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.SERVER);
            }

            @Override
            public Set<JavaSourceSupport.CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                return sc == ERuntimeEnvironmentType.SERVER ? EnumSet.of(CodeType.META) : null;
            }
        };
    }

    @Override
    public void afterOverwrite() {
        resourceRestrictions.clear();
        apFamilyRefs.clear();
        ancestorRefs.clear();
        isOverwrite = true;
    }

    @Override
    public boolean allowOverwrite() {
        return !this.getId().toString().equals(EDrcPredefinedRoleId.SUPER_ADMIN.getValue());
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsRoleDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new RoleRadixdoc(AdsRoleDef.this, page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    public static class Factory {

        public static AdsRoleDef loadFrom(RoleDefinition xDef, long formatVersion) {
            List<Id> list = xDef.getAncestors().getIdList();
            List<Id> ancestorIds = new ArrayList<>(list.size());
            for (Id sId : list) {
                ancestorIds.add(sId);
            }

            list = xDef.getApFamilies().getIdList();

            List<Id> apFamilyIds = new ArrayList<>(list.size());
            for (Id sId : list) {
                apFamilyIds.add(sId);
            }
            
            int i = 0;
            Resource[] resources = new Resource[xDef.getResources().getResourceList().size()];
            for (org.radixware.schemas.adsdef.RoleDefinition.Resources.Resource re : xDef.getResources().getResourceList()) {
                List<Id> enabledCommands = null;
                if (re.getEnabledCommands() != null) {
                    list = re.getEnabledCommands().getIdList();
                    if (!list.isEmpty()) {
                        enabledCommands = new ArrayList<>(list);
                    }
                }
                List<Id> enabledChildren = null;
                if (re.getEnabledChildren() != null) {
                    list = re.getEnabledChildren().getIdList();
                    if (!list.isEmpty()) {
                        enabledChildren = new ArrayList<>(list);
                    }
                }
                List<Id> enabledPages = null;
                if (re.getEnabledPages() != null) {
                    list = re.getEnabledPages().getIdList();
                    if (!list.isEmpty()) {
                        enabledPages = new ArrayList<>(list);
                    }
                }

                long restrictions = re.getRestrictions();


                if (EDrcResourceType.EDITOR_PRESENTATION.getValue() == re.getType()) {
                    if (formatVersion < 1l) {                                       //  RADIX-6065
                        if ((ERestriction.ACCESS.getValue() & restrictions) == 0) {
                            restrictions &= ~ERestriction.VIEW.getValue();
                        } else {
                            restrictions |= ERestriction.VIEW.getValue();
                        }
                    }
                    if (formatVersion < 2l) {                                       //  RADIX-6495
                        if ((ERestriction.ACCESS.getValue() & restrictions) == 0 && (ERestriction.VIEW.getValue() & restrictions) == 0) {
                            restrictions &= ~ERestriction.ANY_PAGES.getValue();
                        } else {
                            restrictions |= ERestriction.ANY_PAGES.getValue();
                        }
                    }
                }
                resources[i++] = new Resource(EDrcResourceType.getForValue(re.getType()),
                        re.getDefId(),
                        re.getSubDefId(),
                        Restrictions.Factory.newInstance(null, RoleRestrictionPrepareSaver.convert(restrictions, EDrcResourceType.getForValue(re.getType())), enabledCommands, enabledChildren, enabledPages));
            }

            final AdsRoleDef role = new AdsRoleDef(xDef.getId(),
                    xDef.getName(),
                    xDef.getDescription(),
                    ancestorIds,
                    apFamilyIds,
                    resources,
                    xDef.getTitleId(), xDef.getDescriptionId(), xDef.getIsOverwrite(),
                    xDef.getAbstract() == null ? false : xDef.getAbstract(),
                    xDef.getDeprecated() == null ? false : xDef.getDeprecated());
            
            loadWarningsSupport(role, xDef);
            return role;

        }

        public static AdsRoleDef newInstance() {
            return new AdsRoleDef("NewRole");
        }

        public static AdsRoleDef newInstance(Id id, String name) {
            return new AdsRoleDef(id, name);
        }
    }

    protected AdsRoleDef(String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.ROLE),
                name);
    }

    protected AdsRoleDef(Id id, String name) {
        this(id,
                name,
                "",
                new ArrayList<Id>(0),
                new ArrayList<Id>(0),
                new Resource[0], null, null, false, false, false);
    }

    protected AdsRoleDef(
            final Id id,
            final String name,
            final String description,
            final List<Id> ancestorIds,
            final List<Id> apFamilyIds,
            final Resource[] resources,
            final Id titledId,
            final Id descriptionId,
            final boolean isOverwrite,
            final boolean isAbstract,
            final boolean isDeprecated) {
        super(id, name, null);

        this.setDescription(description == null ? "" : description);
        this.setOverwrite(isOverwrite);
        this.setAbstract(isAbstract);
        this.setDeprecated(isDeprecated);
        this.setDescriptionId(descriptionId);

        if (ancestorIds != null && !ancestorIds.isEmpty()) {
            for (Id aid : ancestorIds) {
                ancestorRefs.add(new AncestorRef(aid));
            }
        }
        if (apFamilyIds != null && !apFamilyIds.isEmpty()) {
            for (Id aid : apFamilyIds) {
                apFamilyRefs.add(new APFamilyRef(aid));
            }
        }
        this.setTitleId(titledId);
        resourceRestrictions = new HashMap<>();

        if (resources != null) {
            for (Resource res : resources) {
                resourceRestrictions.put(generateResHashKey(res), res);
            }
        }
    }

    public boolean isAppRole() {
        return getId().getPrefix() == EDefinitionIdPrefix.APPLICATION_ROLE;
    }

    public boolean isEmptyRole() {
        return ancestorRefs.isEmpty() && resourceRestrictions.isEmpty() && apFamilyRefs.isEmpty();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        xDefRoot.setFormatVersion(FORMAT_VERSION);
        appendTo(xDefRoot.addNewAdsRoleDefinition(), saveMode);
    }

    public void appendTo(RoleDefinition def, ESaveMode saveMode) {
        super.appendTo(def, saveMode);
        org.radixware.schemas.adsdef.RoleDefinition.ApFamilies apf = def.addNewApFamilies();
        org.radixware.schemas.adsdef.RoleDefinition.Ancestors anc = def.addNewAncestors();
        org.radixware.schemas.adsdef.RoleDefinition.Resources res = def.addNewResources();

        for (APFamilyRef ar : apFamilyRefs) {
            apf.getIdList().add(ar.id);
        }
        for (AncestorRef ar : ancestorRefs) {
            anc.getIdList().add(ar.id);
        }

        def.setIsOverwrite(isOverwrite);
        def.setAbstract(isAbstract);
        def.setDeprecated(isDeprecated);
        def.setDescription(getDescription());



        //sorting ...
        Collection<Resource> collection = this.resourceRestrictions.values();
        List<Resource> list = new ArrayList<>(collection.size());
        for (Resource r : collection) {
            list.add(r);
        }
        RadixObjectsUtils.sortByName(list);
        
        saveWarningsSupport(this, def);

        for (Resource r : list) {
            org.radixware.schemas.adsdef.RoleDefinition.Resources.Resource recurce = res.addNewResource();
            recurce.setDefId(r.defId);
            if (r.subDefId != null) {
                recurce.setSubDefId(r.subDefId);
            }
            // ERestriction.toBitField(r.restrictions.getRestriction()
            recurce.setRestrictions(RoleRestrictionPrepareSaver.convert(r.restrictions, r.type));
            recurce.setType(r.type.getValue().longValue());

            if (r.restrictions.getEnabledCommandIds() != null && r.restrictions.getEnabledCommandIds().size() > 0) {
                org.radixware.schemas.adsdef.RoleDefinition.Resources.Resource.EnabledCommands commans = recurce.addNewEnabledCommands();
                commans.getIdList().addAll(r.restrictions.getEnabledCommandIds());
            }
            if (r.restrictions.getEnabledChildIds() != null && r.restrictions.getEnabledChildIds().size() > 0) {
                org.radixware.schemas.adsdef.RoleDefinition.Resources.Resource.EnabledChildren childs = recurce.addNewEnabledChildren();
                childs.getIdList().addAll(r.restrictions.getEnabledChildIds());
            }
            if (r.restrictions.getEnabledPageIds() != null && r.restrictions.getEnabledPageIds().size() > 0) {
                org.radixware.schemas.adsdef.RoleDefinition.Resources.Resource.EnabledPages pages = recurce.addNewEnabledPages();
                pages.getIdList().addAll(r.restrictions.getEnabledPageIds());
            }
        }
    }

    private static class RoleRestrictionPrepareSaver { // TWRBS-4517 

        static long getAllovedRestrictionBitMask(EDrcResourceType resourceType) {
            long allovedRestrictionBitMask;
            if (EDrcResourceType.EDITOR_PRESENTATION.equals(resourceType)) {
                allovedRestrictionBitMask =
                        ERestriction.ACCESS.getValue()
                        | ERestriction.CREATE.getValue()
                        | ERestriction.DELETE.getValue()
                        | ERestriction.UPDATE.getValue()
                        | ERestriction.VIEW.getValue()
                        | ERestriction.ANY_COMMAND.getValue()
                        | ERestriction.ANY_CHILD.getValue()
                        | ERestriction.ANY_PAGES.getValue();
            } else if (EDrcResourceType.SELECTOR_PRESENTATION.equals(resourceType)) {
                allovedRestrictionBitMask =
                        ERestriction.ACCESS.getValue()
                        | ERestriction.CREATE.getValue()
                        | ERestriction.DELETE_ALL.getValue()
                        | ERestriction.ANY_COMMAND.getValue();
            } else {
                allovedRestrictionBitMask =
                        ERestriction.ACCESS.getValue();
            }
            return allovedRestrictionBitMask;
        }

        static long convert(Restrictions restrictions, EDrcResourceType resourceType) {
            return convert(ERestriction.toBitField(restrictions.getRestriction()), resourceType);
        }

        static long convert(long restrictions, EDrcResourceType resourceType) {
            return restrictions & getAllovedRestrictionBitMask(resourceType);
        }
    }

    public EDefType getType() {
        return EDefType.ROLE;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.ROLE;
    }

    public static final class Resource extends RadixObject {

        public Resource(
                final EDrcResourceType type,
                final Id defId,
                final Id subDefId,
                final Restrictions restrictions) {
            //boolean rr = restrictions.
            this.type = type;
            this.defId = defId;
            this.subDefId = subDefId;
            this.restrictions = restrictions != null ? restrictions : Restrictions.Factory.newInstance(null, 0);
            this.setName(generateResHashKey(this));
        }
        public EDrcResourceType type;
        public Id defId;
        public Id subDefId;
        public Restrictions restrictions;

        private class ResourceLink extends DefinitionLink<AdsDefinition> {

            private final WeakReference<Definition> contextRef;
            private final DefinitionSearcher<AdsDefinition> searcher;

            public ResourceLink(Definition context) {
                this.contextRef = new WeakReference<>(context);
                searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(context);
            }

            @Override
            protected AdsDefinition search() {
                return searcher.findById(defId).get();
            }
        }
        private ResourceLink link = null;

        /**
         * Find definition within context scope.
         *
         * @return Definition or null.
         */
        public Definition resolveResource(Definition context) {
            ResourceLink curLink = link;
            if (curLink == null || curLink.contextRef.get() != context) {
                curLink = new ResourceLink(context);
                link = curLink;
            }
            return curLink.find();
        }

        private class TopLevelDefLink extends DefinitionLink<AdsDefinition> {

            private final WeakReference<Layer> contextRef;

            public TopLevelDefLink(Layer context) {
                this.contextRef = new WeakReference<>(context);
            }

            @Override
            protected AdsDefinition search() {

                return AdsUtils.findTopLevelDefById(contextRef.get(), defId);
            }
        }
        private TopLevelDefLink tldLink = null;

        public AdsDefinition findTopLevelDef(Layer context) {
            TopLevelDefLink curLink = tldLink;
            if (curLink == null || curLink.contextRef.get() != context) {
                curLink = new TopLevelDefLink(context);
                tldLink = curLink;
            }
            return curLink.find();
        }

        public String getResourceContent(Definition context) {
            String ret;
            if (type.equals(EDrcResourceType.CONTEXTLESS_COMMAND)) {
                ret = "CONTEXTLESS_COMMAND ";
                Definition cmd = resolveResource(context);
                if (cmd != null) {
                    ret += cmd.getQualifiedName();
                } else {
                    ret += defId.toString();
                }
            } else if (type.equals(EDrcResourceType.EDITOR_PRESENTATION)) {
                ret = "EDITOR_PRESENTATION ";
                AdsEntityClassDef ecd = (AdsEntityClassDef) resolveResource(context);
                if (ecd != null) {
                    ret += ecd.getQualifiedName();
                    AdsEditorPresentationDef pres = ecd.getPresentations().getEditorPresentations().findById(subDefId, EScope.ALL).get();
                    if (pres != null) {
                        ret += " " + pres.getName();
                    } else {
                        ret += " " + subDefId.toString();
                    }
                } else {
                    ret += defId.toString();
                    if (subDefId != null) {
                        ret += " " + subDefId.toString();
                    }
                }

            } else if (type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM)) {
                ret = "EXPLORER_ROOT_ITEM ";
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) resolveResource(context);
                if (par != null) {
                    ret += par.getQualifiedName();
                    AdsExplorerItemDef ei = par.getExplorerItems().findChildExplorerItem(subDefId);
                    if (ei != null) {
                        String title = ei.getTitle(EIsoLanguage.ENGLISH);
                        if (title == null) {
                            title = ei.getName();
                        }
                        ret += " " + title;
                    } else {
                        ret += " " + subDefId.toString();
                    }
                } else {
                    ret += defId.toString();
                    if (subDefId != null) {
                        ret += " " + subDefId.toString();
                    }
                }
            } else {
                ret = "SERVER_RESOURCE " + defId.toString();
            }
            return ret;
        }
    }
    private boolean isAbstract = false;

    public boolean isAbstract() {
        return isAbstract;
    }

    final public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        this.setEditState(EEditState.MODIFIED);
    }
    private boolean isDeprecated = false;

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    final public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
        this.setEditState(EEditState.MODIFIED);
    }

    private static void collectResourceEntityClasses(Layer layer,
            Map<Id, AdsEntityObjectClassDef> usingClasses,
            Map<Id, Id> usingPresentations,
            AdsRoleDef role) {

        //   DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(role.getModule());
        for (AncestorRef aid : role.getAncestorRefs()) {
            AdsRoleDef aRole = aid.find();
            if (aRole != null) {
                collectResourceEntityClasses(layer, usingClasses, usingPresentations, aRole);
            }
        }
        if (role.isOverwrite()) {
            AdsRoleDef oRole = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
            if (oRole != null) {
                collectResourceEntityClasses(layer, usingClasses, usingPresentations, oRole);
            }
        }


        Iterator<Resource> iter = role.resourceRestrictions.values().iterator();
        while (iter.hasNext()) {
            Resource res = iter.next();
            if (res.type.equals(EDrcResourceType.EDITOR_PRESENTATION)
                    || res.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {
                if (usingClasses.get(res.defId) == null) {
                    if (usingPresentations.get(res.subDefId) == null) {
                        if (!res.restrictions.isDenied(ERestriction.ACCESS)) {
                            AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) res.findTopLevelDef(layer);
                            if (clazz != null) {
                                usingClasses.put(res.defId, clazz);
                            }
                            usingPresentations.put(res.subDefId, res.subDefId);
                        }
                    }
                }
            }
        }


    }

    private static void collectApfList(Layer layer, List<DdsAccessPartitionFamilyDef> list, AdsEntityClassDef entity) {
        EAccessAreaType type = entity.getAccessAreas().getType();
        if (type.equals(EAccessAreaType.NONE)) {
        } else if (type.equals(EAccessAreaType.NOT_OVERRIDDEN)) {
            entity = (AdsEntityClassDef) entity.getHierarchy().findOverwritten().get();
            if (entity != null) {
                collectApfList(layer, list, entity);
            }
        } else {
            for (AccessArea area : entity.getAccessAreas()) {
                for (Partition part : area.getPartitions()) {
                    DdsAccessPartitionFamilyDef apf = part.findApf();
                    if (apf != null && !list.contains(apf)) {
                        list.add(apf);
                    }
                }
            }
            if (type.equals(EAccessAreaType.INHERITED)) {
                DdsReferenceDef ref = entity.getAccessAreas().findInheritReference();
                if (ref != null) {
                    Id id = ref.getParentTableId();
                    id = Id.Factory.changePrefix(id, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
                    AdsEntityClassDef newEntity = (AdsEntityClassDef) AdsUtils.findTopLevelDefById(layer, id);
                    if (newEntity != null) {
                        collectApfList(layer, list, newEntity);
                    }
                }
            }
        }
    }

    public List<DdsAccessPartitionFamilyDef> getDependentAPF() {
        Layer layer = this.getModule().getSegment().getLayer();
        List<DdsAccessPartitionFamilyDef> newList = new ArrayList<>(0);
        List<AdsEntityClassDef> lst = collectResourceEntityClasses();

        for (AdsEntityClassDef entity : lst) {
            collectApfList(layer, newList, entity);
        }
        return newList;
    }

    private List<AdsEntityClassDef> collectResourceEntityClasses() {
        Map<Id, AdsEntityObjectClassDef> usingClasses = new HashMap<>();
        Map<Id, Id> usingPresentations = new HashMap<>();

        collectResourceEntityClasses(this.getModule().getSegment().getLayer(), usingClasses, usingPresentations, this);

        Map<Id, AdsEntityClassDef> usingEntity = new HashMap<>();
        Iterator<AdsEntityObjectClassDef> iter = usingClasses.values().iterator();
        while (iter.hasNext()) {
            AdsEntityObjectClassDef clazz = iter.next();
            AdsEntityClassDef entity = null;
            while (clazz != null) {
                if (clazz instanceof AdsEntityClassDef) {
                    entity = (AdsEntityClassDef) clazz;
                    break;
                }
                clazz = clazz.findBasis();
            }
            if (entity != null && usingEntity.get(entity.getId()) == null) {
                usingEntity.put(entity.getId(), entity);
            }
        }
        Iterator<AdsEntityClassDef> iter2 = usingEntity.values().iterator();

        List<AdsEntityClassDef> list = new ArrayList<>();
        while (iter2.hasNext()) {
            list.add(iter2.next());
        }
        return list;
    }
    private Map<String, Resource> resourceRestrictions;

    public Restrictions getResourceRestrictions(
            final EDrcResourceType type,
            final Id defId,
            final Id subDefId,
            final AdsEditorPresentationDef editorPresentationDef) {

        return getResourceRestrictions(generateResHashKey(type, defId, subDefId), editorPresentationDef);
    }

    public Collection<Resource> getResources() {
        if (resourceRestrictions == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableCollection(resourceRestrictions.values());
        }
    }

    public boolean isAncestor(final AdsRoleDef def, final boolean searchInAncestors) {
        //this.isReadOnly();

        AdsRoleDef curr = this;
        while (curr != null) {
            List<AncestorRef> aId = curr.getAncestorRefs();
            for (AncestorRef ancestorRef : aId) {
                if (ancestorRef.id.equals(def.getId())) {
                    return true;
                }
                if (searchInAncestors) {
                    AdsRoleDef ancestor = ancestorRef.find();
                    if (ancestor != null && ancestor.isAncestor(def, true)) {
                        return true;
                    }
                }
            }
            if (curr.isOverwrite()) {
                curr = (AdsRoleDef) curr.getHierarchy().findOverwritten().get();
            } else {
                break;
            }
        }
        return false;
    }

    public Restrictions getAncestorResourceRestrictions(final String resHashKey,
            AdsEditorPresentationDef editorPresentationDef) {
        AdsEditorPresentationDef editorPresentationDef2 = editorPresentationDef;
        
        
        final Set<AdsEditorPresentationDef> recursionBreaker = new HashSet();
                
                
        if (editorPresentationDef2 != null) {
            while (true) {
                boolean isMustBreak = false;

                if (editorPresentationDef2.isRightsInheritanceModeInherited()) {
                    editorPresentationDef2 = editorPresentationDef2.findRightsInheritanceDefinePresentation().get();
                } else if (editorPresentationDef2.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_REPLACED)) {
                    editorPresentationDef2 = editorPresentationDef2.findReplacedEditorPresentation().get();
                } else if (editorPresentationDef2.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED)) {
                    editorPresentationDef2 = editorPresentationDef2.findRightSourceEditorPresentation();
                } else {
                    isMustBreak = true;
                }

                if (editorPresentationDef2 == null) {
                    break;
                }
                if (isMustBreak) {
                    break;
                }
                
                if (recursionBreaker.contains(editorPresentationDef2)){
                    break;
                }
                recursionBreaker.add(editorPresentationDef2);

                final String hash = AdsRoleDef.generateResHashKey(
                        EDrcResourceType.EDITOR_PRESENTATION,
                        editorPresentationDef2.getOwnerClass().getId(),
                        editorPresentationDef2.getId());
                final Restrictions rest = getOnlyCurrentResourceRestrictions(hash);
                if (rest != null) {
                    return rest;
                }
            }


        }
        long bitField = 0xffffffff;
        final List<Id> cmdList = new ArrayList<>();
        final List<Id> childrenList = new ArrayList<>();
        final List<Id> pagesList = new ArrayList<>();


        for (AncestorRef ancestorRef : getAncestorRefs()) {
            AdsRoleDef ancestor = ancestorRef.find();
            if (ancestor != null) {
                final Restrictions ancRestr = ancestor.getResourceRestrictions(resHashKey, editorPresentationDef);
                if (ancRestr != null) {
                    bitField &= ERestriction.toBitField(ancRestr.getRestriction());
                    if (ancRestr.getEnabledCommandIds() != null) {
                        for (Id id : ancRestr.getEnabledCommandIds()) {
                            if (!cmdList.contains(id)) {
                                cmdList.add(id);
                            }
                        }
                    }
                    if (ancRestr.getEnabledChildIds() != null) {
                        for (Id id : ancRestr.getEnabledChildIds()) {
                            if (!childrenList.contains(id)) {
                                childrenList.add(id);
                            }
                        }
                    }
                    if (ancRestr.getEnabledPageIds() != null) {
                        for (Id id : ancRestr.getEnabledPageIds()) {
                            if (!pagesList.contains(id)) {
                                pagesList.add(id);
                            }
                        }
                    }



                    if (bitField == 0) {
                        return Restrictions.Factory.newInstance(0, cmdList, childrenList, pagesList);
                    }
                }
            }
        }
        return Restrictions.Factory.newInstance(bitField, cmdList, childrenList, pagesList);
    }

    private Restrictions getOverwriteResourceRestrictions(final String resHashKey, boolean checkThis, final AdsEditorPresentationDef editorPresentationDef) {
        if (checkThis) {
            Restrictions rest = getOnlyCurrentResourceRestrictions(resHashKey);
            if (rest != null) {
                return rest;
            }
        }
        if (this.isOverwrite()) {
            AdsRoleDef r = (AdsRoleDef) this.getHierarchy().findOverwritten().get();
            if (r != null) {
                //return r. getOverwriteResourceRestrictions(resHashKey, true);
                return r.getResourceRestrictions(resHashKey, editorPresentationDef);
            }
        }
        return Restrictions.Factory.newInstance(0xffffffff, new ArrayList<Id>(0), new ArrayList<Id>(0), new ArrayList<Id>(0));
    }

    public Restrictions getOverwriteResourceRestrictions(final String resHashKey, final AdsEditorPresentationDef editorPresentationDef) {
        return getOverwriteResourceRestrictions(true, resHashKey, editorPresentationDef);
    }
    
    private Restrictions getOverwriteResourceRestrictions(boolean forEditor, final String resHashKey, final AdsEditorPresentationDef editorPresentationDef) {
        if (forEditor){
            EDrcResourceType type = getTypeByHash(resHashKey);
            if (EDrcResourceType.EDITOR_PRESENTATION.equals(type) 
                    || EDrcResourceType.SELECTOR_PRESENTATION.equals(type)
                    || EDrcResourceType.EXPLORER_ROOT_ITEM.equals(type)
                    ){
                return Restrictions.Factory.newInstance(0xffffffff, null, null, null);
            }
        }        
        return getOverwriteResourceRestrictions(resHashKey, false, editorPresentationDef);
    }
    
    private static EDrcResourceType getTypeByHash(final String hashKey) {
        final int index = hashKey.indexOf(HASH_SEP);
        if (index == -1) {
            throw new RuntimeException("Incorrect hash \'" + hashKey + "\'");
        }
        final String hashAsStr = hashKey.substring(0, index);
        for (EDrcResourceType res : EDrcResourceType.values()) {
            if (res.toString().equals(hashAsStr)) {
                return res;
            }
        }
        throw new RuntimeException("Incorrect hash \'" + hashKey + "\'");
    }
    

    public final void removeAllSubResouses(
            final EDrcResourceType type,
            final Id defId) {
        List<Entry<String, Resource>> list = new ArrayList<>(resourceRestrictions.size());
        Iterator<Entry<String, Resource>> iter = resourceRestrictions.entrySet().iterator();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        boolean isWasRemove = false;
        for (Entry<String, Resource> e : list) {
            if (e.getValue().type.equals(type) && e.getValue().defId.equals(defId)) {
                resourceRestrictions.remove(e.getKey());
                isWasRemove = true;
            }
        }
        if (isWasRemove) {
            setEditState(EEditState.MODIFIED);
        }
    }

    public Restrictions getOnlyOverwriteRestrictionsForEditorPresentation(AdsEditorPresentationDef editorPresentationDef) {
        if (!isOverwrite() || editorPresentationDef == null) {
            return null;
        }

        AdsRoleDef role = this;
        String hash = generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                editorPresentationDef.getOwnerClass().getId(),
                editorPresentationDef.getId());
        role = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
        while (role != null) {
            Restrictions res = role.getOnlyCurrentResourceRestrictions(hash);
            if (res != null) {
                return res;
            }
            if (role.isOverwrite()) {
                role = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
            } else {
                break;
            }
        }
        return null;
    }

    public Restrictions getOnlyCurrentResourceRestrictions(final String resHashKey) {
        if (getId().toString().equals(EDrcPredefinedRoleId.SUPER_ADMIN.getValue())) {
            return Restrictions.Factory.newInstance(0);
        }
        Resource res = resourceRestrictions.get(resHashKey);
        return res != null ? Restrictions.Factory.newInstance(ERestriction.toBitField(res.restrictions.getRestriction()), res.restrictions.getEnabledCommandIds(), res.restrictions.getEnabledChildIds(), res.restrictions.getEnabledPageIds()) : null;
    }

    public void CreateOrReplaceResourceRestrictions(Resource r) {
        String hash = generateResHashKey(r);
        Resource res = resourceRestrictions.get(hash);
        if (res != null) {
            res.restrictions = r.restrictions;
        } else {
            resourceRestrictions.put(hash, r);
        }
        setEditState(EEditState.MODIFIED);
    }

    public void RemoveResourceRestrictions(String hash) {
        resourceRestrictions.remove(hash);
        setEditState(EEditState.MODIFIED);
    }

    public final Restrictions getOverwriteAndAncestordResourceRestrictions(final String resHashKey, final AdsEditorPresentationDef editorPresentationDef) {
        long mask = 0xffffffff;
        final Restrictions overwriteRestrictions = getOverwriteResourceRestrictions(false, resHashKey, editorPresentationDef);
        mask &= ERestriction.toBitField(overwriteRestrictions.getRestriction());
        final Restrictions ancestorRestrictions = getAncestorResourceRestrictions(resHashKey, editorPresentationDef);
        mask &= ERestriction.toBitField(ancestorRestrictions.getRestriction());

        final List<Id> cmdList = new ArrayList<>(overwriteRestrictions.getEnabledCommandIds());
        final List<Id> enCmdLst = ancestorRestrictions.getEnabledCommandIds();
        if (enCmdLst != null) {
            for (Id id : enCmdLst) {
                if (!cmdList.contains(id)) {
                    cmdList.add(id);
                }
            }
        }
        final List<Id> childrenList = new ArrayList<>(overwriteRestrictions.getEnabledChildIds());
        List<Id> ancEnCmdLst = ancestorRestrictions.getEnabledChildIds();
        if (ancEnCmdLst != null) {
            for (Id id : ancEnCmdLst) {
                if (!childrenList.contains(id)) {
                    childrenList.add(id);
                }
            }
        }

        final List<Id> pageList = new ArrayList<>(overwriteRestrictions.getEnabledPageIds());
        List<Id> ancEnPgLst = ancestorRestrictions.getEnabledPageIds();
        if (ancEnPgLst != null) {
            for (Id id : ancEnPgLst) {
                if (!pageList.contains(id)) {
                    pageList.add(id);
                }
            }
        }

        return Restrictions.Factory.newInstance(mask, cmdList, childrenList, pageList);
    }

    public final Restrictions getResourceRestrictions(final String resHashKey, final AdsEditorPresentationDef editorPresentationDef) {
        Restrictions rest = getOnlyCurrentResourceRestrictions(resHashKey);
        if (rest != null) {
            return rest;
        }
        return getOverwriteAndAncestordResourceRestrictions(resHashKey, editorPresentationDef);
    }
    private final DefinitionSearcher<AdsDefinition> currSearcher = AdsSearcher.Factory.newAdsDefinitionSearcher(this);

    public class AncestorRef extends DefinitionLink<AdsRoleDef> {

        private final Id id;

        public AncestorRef(Id id) {
            this.id = id;
        }

        @Override
        protected AdsRoleDef search() {
            return (AdsRoleDef) currSearcher.findById(id).get();
        }
    }
    private final List<AncestorRef> ancestorRefs = new LinkedList<>();

    private class APFamilyLink extends DefinitionLink<DdsAccessPartitionFamilyDef> {

        private final WeakReference<Layer> contextRef;
        private final Id id;

        public APFamilyLink(Layer context, Id id) {
            this.contextRef = new WeakReference<>(context);
            this.id = id;

        }

        @Override
        protected DdsAccessPartitionFamilyDef search() {
            return AdsUtils.findTopLevelApfById(contextRef.get(), id);
        }
    }

    public class APFamilyRef {

        private final Id id;
        private APFamilyLink link = null;

        public APFamilyRef(Id id) {
            this.id = id;
        }

        public DdsAccessPartitionFamilyDef findAPF(Layer context) {
            APFamilyLink curLink = link;
            if (curLink == null || curLink.contextRef.get() != context) {
                curLink = new APFamilyLink(context, id);
                link = curLink;
            }
            return curLink.find();
        }
    }
    /**
     * Find definition within context scope.
     *
     * @return Definition or null.
     */
    private List<APFamilyRef> apFamilyRefs = new LinkedList<>();
    DefinitionSearcher<AdsDefinition> searcher = null;

    public Map<String, Resource> getAllResourceRestrictions() {
        return new HashMap<>(resourceRestrictions);
    }

    public List<Id> getAPFamilieIds() {
        if (apFamilyRefs.isEmpty()) {
            return new ArrayList<>(0);
        } else {
            List<Id> ids = new ArrayList<>(apFamilyRefs.size());
            for (APFamilyRef ref : apFamilyRefs) {
                ids.add(ref.id);
            }
            return ids;
        }
    }

    public List<APFamilyRef> getAPFamilieRefs() {
        return Collections.unmodifiableList(apFamilyRefs);
    }

    public boolean addAPFamilieId(Id id) {

        for (APFamilyRef ref : apFamilyRefs) {
            if (ref.id == id) {
                return false;
            }
        }
        apFamilyRefs.add(new APFamilyRef(id));
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public boolean removeAPFamilieId(Id id) {
        for (int i = 0; i < apFamilyRefs.size(); i++) {
            APFamilyRef ref = apFamilyRefs.get(i);
            if (ref.id == id) {
                apFamilyRefs.remove(i);
                setEditState(EEditState.MODIFIED);
                return true;
            }
        }

        return false;
    }

    public void clearAPFamilieIds() {
        apFamilyRefs.clear();
        setEditState(EEditState.MODIFIED);
    }

    public boolean isEmptyAncestorList() {
        return ancestorRefs.isEmpty();
    }

    public List<Id> getAncestorIds() {
        if (ancestorRefs.isEmpty()) {
            return new ArrayList<>(0);
        } else {
            List<Id> ids = new ArrayList<>(ancestorRefs.size());
            for (AncestorRef ref : ancestorRefs) {
                ids.add(ref.id);
            }
            return ids;
        }
    }

    public List<AncestorRef> getAncestorRefs() {
        return Collections.unmodifiableList(ancestorRefs);
    }

    public boolean addAncestorId(Id id) {

        for (AncestorRef ref : ancestorRefs) {
            if (ref.id == id) {
                return false;
            }
        }
        ancestorRefs.add(new AncestorRef(id));
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public List<Id> collectOverwriteAPF() {
        List<Id> list = getAPFamilieIds();
        if (list == null) {
            list = new ArrayList<>();
        }
        AdsRoleDef role = this;
        while (role.isOverwrite()) {
            role = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
            if (role == null) {
                break;
            }
            list.addAll(role.getAPFamilieIds());
        }
        return list;
    }

    public List<Id> collectOverwriteAncestors() {
        List<Id> list = getAncestorIds();
        if (list == null) {
            list = new ArrayList<>();
        }
        AdsRoleDef role = this;
        while (role.isOverwrite()) {
            role = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
            if (role == null) {
                break;
            }
            List<Id> lst = role.getAncestorIds();
            if (lst != null && !lst.isEmpty()) {
                list.addAll(lst);
            }
        }
        return list;
    }

    public List<AdsRoleDef> collectAllAncestors() {
        List<AdsRoleDef> out = new ArrayList<>();
        collectAllAncestors(out, false);
        return out;
    }

    private void collectAllAncestors(List<AdsRoleDef> out, boolean useThis) {
        if (useThis) {
            if (!out.contains(this)) {
                out.add(this);
            }
        }
        List<AncestorRef> refs = getAncestorRefs();
        for (AncestorRef ref : refs) {
            AdsRoleDef child = ref.search();
            if (child != null && !out.contains(child)) {
                child.collectAllAncestors(out, true);
            }
        }
    }

    public Collection<Resource> collectOverwriteResource() {
        Map<String, Resource> map = getAllResourceRestrictions();
        AdsRoleDef role = this;

        while (role.isOverwrite()) {
            role = (AdsRoleDef) role.getHierarchy().findOverwritten().get();
            if (role == null) {
                break;
            }
            Map<String, Resource> currMap = role.getAllResourceRestrictions();
            Iterator<String> keyIter = currMap.keySet().iterator();

            while (keyIter.hasNext()) {
                String key = keyIter.next();
                if (!map.containsKey(key)) {
                    map.put(key, currMap.get(key));
                }
            }
        }
        return map.values();
    }

    public boolean removeAncestorId(Id id) {
        for (int i = 0; i
                < ancestorRefs.size(); i++) {
            AncestorRef ref = ancestorRefs.get(i);
            if (ref.id == id) {
                ancestorRefs.remove(i);
                setEditState(EEditState.MODIFIED);
                return true;
            }
        }
        return false;
    }

    public void clearAncestorIds() {
        setEditState(EEditState.MODIFIED);
        ancestorRefs.clear();
    }

    public static String generateResHashKey(final Resource res) {
        return generateResHashKey(res.type, res.defId, res.subDefId);
    }
    private static final String HASH_SEP = "~";

    public static String generateResHashKey(
            final EDrcResourceType type,
            final Id defId,
            final Id subDefId) {
        return generateResHashKey(type, defId != null ? defId.toString() : null, subDefId != null ? subDefId.toString() : null);
    }

    public static String generateResHashKey(
            final EDrcResourceType type,
            final EDrcServerResource res) {
        return generateResHashKey(type, res.getValue(), null);
    }

    private static String generateResHashKey(
            final EDrcResourceType type,
            final String defId,
            final String subDefId) {
        final StringBuilder hash = new StringBuilder(type.toString());
        hash.append(HASH_SEP);


        if (defId != null) {
            hash.append(defId);
        }
        hash.append(HASH_SEP);
        if (subDefId != null) {
            hash.append(subDefId);
        }
        return hash.toString();
    }

    /*
     * private static final String getDefIdFromResHahKey(final String hash) {
     * final String defId = hash.split(HASH_SEP)[1]; if (defId.isEmpty()) {
     * return null; } return defId; }
     */
    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public ClipboardSupport<AdsRoleDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsRoleDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                RoleDefinition xDef = RoleDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);

                return xDef;
            }

            @Override
            protected AdsRoleDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof RoleDefinition) {
                    return Factory.loadFrom((RoleDefinition) xmlObject, FORMAT_VERSION);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }
        };


    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ROLE;
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.PUBLIC;
    }

    @Override
    public boolean isPublished() {
        return true;
    }

    @Override
    public boolean canChangePublishing() {
        return false;
    }
    private Id runtimeId = null;

    public Id getRuntimeId() {
        if (isAppRole()) {
            synchronized (this) {
                if (runtimeId == null) {
                    runtimeId = Id.Factory.newInstance(EDefinitionIdPrefix.APPLICATION_ROLE);
                }
            }
            return runtimeId;
        } else {
            return getId();
        }
    }

    public void resetRuntimeId() {
        synchronized (this) {
            runtimeId = null;
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        if (isAppRole()) {
            return ENamingPolicy.FREE;
        } else {
            return super.getNamingPolicy();
        }
    }

    @Override
    public void exportToUds(OutputStream out) throws IOException {
        AppRolesDocument xDoc = AppRolesDocument.Factory.newInstance();

        UserDefinedDefs.UserDefinedDef role = xDoc.addNewAppRoles().addNewUserDefinedDef();
        role.setGuid(this.getId().toString());
        role.setTitle(this.getName());
        this.appendTo(role.addNewDefinition(), ESaveMode.NORMAL);

        role.setDescription(getDescription());

        XmlFormatter.save(xDoc, out);
    }

    public static class RoleResourcesCash {

        public Map<Id, AdsExplorerItemDef> allParagraphsMap = new HashMap<>();

        public void clear() {
            allParagraphsMap.clear();
        }
        
        public AdsParagraphExplorerItemDef findParagraph(final Id id) {
           final AdsExplorerItemDef item = allParagraphsMap.get(id);
           if (item instanceof AdsParagraphExplorerItemDef){
               return (AdsParagraphExplorerItemDef)item;
           }
           return null;
        }

        public static AdsExplorerItemDef findChildExplorerItem(final RoleResourcesCash resourcesCash, /*final ExplorerItems explorerItems, */final Id id) {
            final AdsExplorerItemDef firstPar = resourcesCash.allParagraphsMap.get(id);
            if (firstPar != null) {
                return firstPar;
            }
            /*
            AdsExplorerItemDef ei = explorerItems.getChildren().findById(id, EScope.ALL).get();
            if (ei != null) {
                return ei;
            } else {
                for (AdsExplorerItemDef itemFirst : explorerItems.getChildren().getLocal()) {
                    AdsExplorerItemDef item = resourcesCash.allParagraphsMap.get(itemFirst.getId());
                    if (item == null) {
                        item = itemFirst;
                    }

                    if (item instanceof AdsParagraphExplorerItemDef) {
                        //ei = ((AdsParagraphExplorerItemDef) item).getExplorerItems().findChildExplorerItem(id);
                        ei = findChildExplorerItem(resourcesCash, ((AdsParagraphExplorerItemDef) item).getExplorerItems(), id);
                        if (ei != null) {
                            return ei;
                        }
                    } else if (item instanceof AdsParagraphLinkExplorerItemDef) {
                         AdsExplorerItemDef ei2 = resourcesCash.allParagraphsMap.get(((AdsParagraphLinkExplorerItemDef) item).getReferencedParagraphId());
                         
                        AdsParagraphExplorerItemDef par = null;
                        if (ei2 instanceof AdsParagraphExplorerItemDef){
                             par = (AdsParagraphExplorerItemDef)ei2;
                        }
                        
                        if (par == null) {
                            par = ((AdsParagraphLinkExplorerItemDef) item).findReferencedParagraph();
                        }
                        if (par != null) {
                            if (par.getId() == id) {
                                return par;
                            }
                            //ei = par.getExplorerItems().findChildExplorerItem(id);
                            ei = findChildExplorerItem(resourcesCash, par.getExplorerItems(), id);
                            if (ei != null) {
                                return ei;
                            }
                        }
                    }
                }
            }
            */
            return null;
        }
    }
}
