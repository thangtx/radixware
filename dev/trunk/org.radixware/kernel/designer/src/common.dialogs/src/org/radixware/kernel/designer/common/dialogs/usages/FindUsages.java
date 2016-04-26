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

package org.radixware.kernel.designer.common.dialogs.usages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsRPCCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.dds.DdsPrototypeDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.IdTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.results.RadixObjectsLookuper;
import static org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg.ESearchType.FIND_REPLACERS;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

/**
 * Visual cover for search for usages.
 *
 */
public class FindUsages extends RadixObjectsLookuper {

    private final Definition definition;
    private final Definition rootDefinition;
    private final FindUsagesCfg.ESearchType searchType;
    private final Set<? extends RadixObject> roots; // null - ignored
    private final FindUsages root;
    private final Set<Definition> subTargets;
    private final Set<Definition> used;

    protected FindUsages(FindUsagesCfg cfg) {
        this.searchType = cfg.getSearchType();
        this.definition = (searchType == FindUsagesCfg.ESearchType.FIND_USAGES ? getBaseOver(cfg.getDefinition()) : cfg.getDefinition());
        this.roots = cfg.getRoots();
        this.rootDefinition = this.definition;
        this.root = null;

        subTargets = new HashSet<>();
        used = new HashSet<>();

        addUsed(definition);
    }

    protected FindUsages(FindUsages root, Definition definition) {
        this.root = root;
        this.searchType = root.searchType;
        this.definition = definition;
        this.roots = root.roots;
        this.rootDefinition = root.rootDefinition;

        subTargets = null;
        used = null;

        root.addUsed(definition);
    }

    @Override
    protected String getPreparingProcessName() {
        return "Preparing to search for usages...";
    }

    @Override
    protected String getProcessName() {
        return "Search for usages...";
    }
    private static final Lock lock = new ReentrantLock();

    protected void prepare() {
        if (rootDefinition == definition) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    FindUsagesResults findUsagesResults = FindUsagesResults.findInstance();
                    findUsagesResults.clear(definition);
                }
            });
        }
    }

    protected void complete() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final FindUsagesResults findUsagesResults = FindUsagesResults.findInstance();
                if (!findUsagesResults.isEmpty(rootDefinition)) {
                    findUsagesResults.open();
                    findUsagesResults.requestVisible();
                    findUsagesResults.requestActive();
                } else {
                    DialogUtils.messageInformation("There are no usages of " + rootDefinition.getName() + " found.");
                }
            }
        });
    }

    protected final void find() {
        if (lock.tryLock()) {
            try {
                prepare();
                doFind();
                complete();
            } finally {
                lock.unlock();
            }
        } else {
            DialogUtils.messageError("Search for usages is already running.");
        }
    }

    private void addSubTarget(Definition def) {
        if (root != null) {
            root.addSubTarget(def);
        } else if (!used.contains(def)) {
            subTargets.add(def);
        }
    }

    private void addUsed(Definition def) {
        if (root != null) {
            root.addUsed(def);
        } else {
            used.add(def);
        }
    }

    private void doFind() {
        if (searchType == FindUsagesCfg.ESearchType.FIND_REPLACED) {
            AdsEditorPresentationDef epr = (AdsEditorPresentationDef) definition;
            for(;;) {
                epr = epr.findReplacedEditorPresentation().get();
                if (epr == null)
                    break;
                accept(definition, epr);
            }
            return;
        }
        
        final Set<? extends RadixObject> topObjects;
        if (searchType == FindUsagesCfg.ESearchType.FIND_USED) {
            topObjects = Collections.singleton(definition);
        } else if (roots != null) {
            topObjects = roots;
        } else {
            topObjects = Collections.singleton(getTopObject(definition));
        }

        doFind(topObjects);
    }

    private void doFind(Set<? extends RadixObject> topObjects) {
        lookup(topObjects, VisitorProviderFactory.createDefaultVisitorProvider());

        if (!isRoot()) {
            return;
        }

        while (!subTargets.isEmpty()) {
            final List<Definition> targets = new ArrayList<>(subTargets);
            subTargets.clear();

            for (final Definition def : targets) {
                if (!used.contains(def)) {
                    createFindUsages(this, def).doFind();
                }
                addUsed(def);
            }
        }
    }

    protected FindUsages createFindUsages(FindUsages root, Definition definition) {
        return new FindUsages(root, definition);
    }

    protected void accept(final RadixObject head, final RadixObject usage) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final FindUsagesResults findUsagesResults = FindUsagesResults.findInstance();
                findUsagesResults.add(rootDefinition, head, usage);
            }
        });
    }

    private static boolean isIn(final RadixObject radixObject, final Set<? extends RadixObject> roots) {
        if (roots == null) {
            return true;
        }
        for (RadixObject root : roots) {
            if (root == radixObject || root.isParentOf(radixObject)) {
                return true;
            }
        }
        return false;
    }

    private void findUsed(final RadixObject processedObject) {
        final List<Definition> dependencies = new ArrayList<>();
        processedObject.collectDependences(dependencies);

        for (Definition dependence : dependencies) {
            if (dependence instanceof AdsLocalizingBundleDef || dependence instanceof AdsMultilingualStringDef || !isIn(dependence, roots)) {
                continue;
            }
            if (definition == dependence || definition.isParentOf(dependence)) {
                continue; // do not display internal links
            }
            accept(processedObject, dependence);
        }
    }

    private static boolean canUse(final RadixObject processedObject, final Definition definition) {
        if (processedObject instanceof AdsClassCatalogDef) {
            return (definition instanceof AdsClassDef || definition instanceof AdsClassCatalogDef);
        }

        if (definition instanceof AdsMethodDef) {
            if (processedObject instanceof AdsMethodDef
                    || processedObject instanceof AdsUISignalDef
                    || processedObject instanceof AdsUIConnection
                    || processedObject instanceof AdsRPCCommandDef) {
                return true;
            }

            final Id[] idPath;
            if (processedObject instanceof IdTag) {
                idPath = ((IdTag) processedObject).getPath();
            } else if (processedObject instanceof JmlTagId) {
                final AdsPath adsPath = ((JmlTagId) processedObject).getPath();
                idPath = (adsPath != null ? adsPath.asArray() : null);
            } else if (processedObject instanceof JmlTagInvocation) {
                final AdsPath adsPath = ((JmlTagInvocation) processedObject).getPath();
                idPath = (adsPath != null ? adsPath.asArray() : null);
            } else {
                idPath = null;
            }


            return idPath != null && idPath.length > 0 && Utils.equals(idPath[idPath.length - 1], definition.getId());
        }
        if (processedObject instanceof Module) {
            return definition instanceof Module;
        }
        return true;
    }

    private void findUsages(final RadixObject processedObject) {
        if (!canUse(processedObject, definition)) {
            return;
        }

        final List<Definition> dependencies = new ArrayList<>();
        processedObject.collectDependences(dependencies);


        for (final Definition dependence : dependencies) {
            if (definition == dependence || (Utils.equals(definition.getId(), dependence.getId())
                    && (getBaseOver(dependence) == definition || getMirror(definition) == dependence))
                    || (definition != dependence && getSubstitution(definition) == dependence)) {

                if (searchType == FindUsagesCfg.ESearchType.FIND_USAGES_GET
                        || searchType == FindUsagesCfg.ESearchType.FIND_USAGES_SET || searchType == FindUsagesCfg.ESearchType.FIND_USAGES) {

                    if (processedObject instanceof AdsPropertyPresentationPropertyDef) {
                        addSubTarget((Definition) processedObject);
                    }
                }

                if (searchType == FindUsagesCfg.ESearchType.FIND_USAGES_GET
                        || searchType == FindUsagesCfg.ESearchType.FIND_USAGES_SET) {

                    if (processedObject instanceof AdsParentPropertyDef) {
                        addSubTarget((AdsParentPropertyDef) processedObject);
                    }

                    if (!checkUsage(searchType, processedObject)) {
                        continue;
                    }
                }

                accept(dependence, processedObject);
            }
        }
    }

    private static boolean checkUsage(FindUsagesCfg.ESearchType type, RadixObject processedObject) {
        if (processedObject instanceof Sqml.Tag) {
            return type == FindUsagesCfg.ESearchType.FIND_USAGES_GET;
        }
        if (processedObject instanceof Scml.Tag) {
            final Scml.Tag tag = (Scml.Tag) processedObject;
            final RadixObjects<Scml.Item> items = tag.getOwnerScml().getItems();
            boolean set = false;

            final int index = items.indexOf(tag);

            if (index >= 0 && index < items.size()) {

                final Scml.Item next = items.get(index + 1);

                if (next instanceof Jml.Text) {
                    final Jml.Text text = (Jml.Text) next;
                    final String nextStr = text.getText().trim();

                    if (!nextStr.isEmpty() && nextStr.charAt(0) == '='
                            && (nextStr.length() == 1 || nextStr.charAt(1) != '=')) {
                        set = true;
                    }
                }
            }

            return set == (type == FindUsagesCfg.ESearchType.FIND_USAGES_SET);
        }

        return false;
    }

    private static Definition getBaseOver(Definition definition) {
        final Definition overwrite = DefinitionsUtils.findOverwrite(definition);
        if (overwrite != null) {
            return getBaseOver(overwrite);
        }
        final Definition override = DefinitionsUtils.findOverride(definition);
        if (override != null) {
            return getBaseOver(override);
        }
        return definition;
    }

    private static Definition getMirror(Definition definition) {
        if (definition instanceof AdsPropertyPresentationPropertyDef) {
            return (Definition) ((AdsPropertyPresentationPropertyDef) definition).findServerSideProperty();
        } else {
            return null;
        }
    }

    private static Definition getSubstitution(Definition definition) {
        if (definition instanceof DdsPrototypeDef) {
            return ((DdsPrototypeDef) definition).findFunction();
        } else {
            return null;
        }
    }

    private static List<Definition> findExtend(Definition definition) {
        if (definition instanceof AdsClassDef) {
            List<Definition> result = new LinkedList<>();
            final AdsClassDef classDef = (AdsClassDef) definition;
            final Inheritance inheritance = classDef.getInheritance();
            if (inheritance != null) {
                AdsClassDef superClass = inheritance.findSuperClass().get();
                if (superClass != null) {
                    result.add(superClass);
                }
                for (AdsTypeDeclaration decl : inheritance.getInerfaceRefList(EScope.LOCAL)) {
                    AdsType type = decl.resolve(classDef).get();
                    if (type instanceof AdsClassType) {
                        result.add(((AdsClassType) type).getSource());
                    }
                }
            }
            return result;
        }
        return null;
    }

    private static boolean isDescendantRecurs(Definition candidate, Definition base, Set<Definition> history) {
        if (history.contains(candidate)) {
            return false;
        }
        history.add(candidate);

        if (Utils.equals(candidate.getId(), base.getId())) {
            final Definition overwrite = DefinitionsUtils.findOverwrite(candidate);
            if (overwrite != null) {
                if (overwrite == base || isDescendantRecurs(overwrite, base, history)) {
                    return true;
                }
            }

            final Definition override = DefinitionsUtils.findOverride(candidate);
            if (override != null) {
                if (override == base || isDescendantRecurs(override, base, history)) {
                    return true;
                }
            }
        }

        final List<Definition> extend = findExtend(candidate);
        if (extend != null) {
            for (Definition ext : extend) {
                if (!history.contains(ext)) {
                    if (ext == base || isDescendantRecurs(ext, base, history)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void findReplacingPresentation(final RadixObject processedObject) {
        if (!(definition instanceof AdsEditorPresentationDef)) {
            return;
        }
        if (!(processedObject instanceof AdsEditorPresentationDef)) {
            return;
        }
        AdsEditorPresentationDef epr = (AdsEditorPresentationDef) processedObject;
        if (epr.findReplacedEditorPresentation().get() == definition) {
            accept(definition, processedObject);
        }
    }

    private void findSubpresentation(final RadixObject processedObject) {
        if (!(definition instanceof AdsDefinition)) {
            return;
        }
        final AdsDefinition currentDef = (AdsDefinition) definition;
        if (!(processedObject instanceof AdsPresentationDef)) {
            return;
        }
        AdsPresentationDef pr = (AdsPresentationDef) processedObject;
        if (pr.getDefinitionType() != currentDef.getDefinitionType()) {
            return;
        }
        if (pr.getDefinitionType() == EDefType.EDITOR_PRESENTATION) {
            AdsEditorPresentationDef epr = (AdsEditorPresentationDef) pr;
            final Set<Definition> history = new HashSet<>();
            if (epr.findBaseEditorPresentation().get() == currentDef || (searchType == FindUsagesCfg.ESearchType.FIND_ALL_SUBPRESENTATIONS && isSubpresentationERecurs(epr, currentDef, history))) {
                accept(currentDef, epr);
            }
        } else if (pr.getDefinitionType() == EDefType.SELECTOR_PRESENTATION) {
            AdsSelectorPresentationDef spr = (AdsSelectorPresentationDef) pr;
            final Set<Definition> history = new HashSet<>();
            if (spr.findBaseSelectorPresentation().get() == currentDef || (searchType == FindUsagesCfg.ESearchType.FIND_ALL_SUBPRESENTATIONS && isSubpresentationSRecurs(spr, currentDef, history))) {
                accept(currentDef, spr);
            }
        }
    }

    private static boolean isSubpresentationERecurs(final AdsEditorPresentationDef presentatoon, AdsDefinition target, Set<Definition> history) {
        if (history.contains(presentatoon)) {
            return false;
        }
        history.add(presentatoon);

        AdsEditorPresentationDef epr = presentatoon.findBaseEditorPresentation().get();
        if (epr == null) {
            return false;
        } else {
            return epr == target || isSubpresentationERecurs(epr, target, history);
        }

    }

    private static boolean isSubpresentationSRecurs(final AdsSelectorPresentationDef presentatoon, AdsDefinition target, Set<Definition> history) {
        if (history.contains(presentatoon)) {
            return false;
        }
        history.add(presentatoon);

        AdsSelectorPresentationDef spr = presentatoon.findBaseSelectorPresentation().get();
        if (spr == null) {
            return false;
        } else {
            return spr == target || isSubpresentationSRecurs(spr, target, history);
        }
    }

    private void findDescendant(final RadixObject processedObject) {
        if (!(processedObject instanceof Definition)) {
            return;
        }

        final Definition candidate = (Definition) processedObject;
        final Set<Definition> history = new HashSet<>();
        history.add(candidate);

        if (Utils.equals(candidate.getId(), definition.getId())) {
            final Definition overwrite = DefinitionsUtils.findOverwrite(candidate);
            if (overwrite != null) {
                if (overwrite == definition || searchType == FindUsagesCfg.ESearchType.FIND_ALL_DESCEDANTS && isDescendantRecurs(overwrite, definition, history)) {
                    accept(overwrite, candidate);
                }
            }

            final Definition override = DefinitionsUtils.findOverride(candidate);
            if (override != null) {
                if (override == definition || searchType == FindUsagesCfg.ESearchType.FIND_ALL_DESCEDANTS && isDescendantRecurs(override, definition, history)) {
                    accept(override, candidate);
                }
            }
        }

        final List<Definition> extend = findExtend(candidate);
        if (extend != null) {
            for (Definition ext : extend) {
                if (ext == definition || searchType == FindUsagesCfg.ESearchType.FIND_ALL_DESCEDANTS && isDescendantRecurs(ext, definition, history)) {
                    accept(ext, candidate);
                }
            }
        }
    }

    @Override
    protected final void process(final RadixObject processedObject) {
        RadixMutex.readAccess(new Runnable() {
            @Override
            public void run() {
                if (processedObject.isInBranch()) {
                    switch (searchType) {
                        case FIND_SUBPRESENTATIONS:
                        case FIND_ALL_SUBPRESENTATIONS:
                            findSubpresentation(processedObject);
                            break;
                        case FIND_REPLACERS:
                            findReplacingPresentation(processedObject);
                            break;
                        case FIND_ALL_DESCEDANTS:
                        case FIND_DIRECT_DESCENDANTS_ONLY:
                            findDescendant(processedObject);
                            break;
                        case FIND_USAGES:
                        case FIND_USAGES_GET:
                        case FIND_USAGES_SET:
                            findUsages(processedObject);
                            break;
                        case FIND_USED:
                            findUsed(processedObject);
                    }
                }
            }
        });
    }

    public final boolean isRoot() {
        return root == null;
    }

    private static RadixObject getTopObject(RadixObject object) {
        while (true) {
            RadixObject owner = object.getContainer();
            if (owner != null) {
                object = owner;
            } else {
                return object;
            }
        }
    }

    public static void search(FindUsagesCfg cfg) {
        final FindUsages searcher = new FindUsages(cfg);
        searcher.find();
    }
}
