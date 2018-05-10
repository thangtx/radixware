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

package org.radixware.kernel.common.defs;

import java.util.*;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Modules;
import org.radixware.kernel.common.types.Id;


// TODO: rename to Depencencies
public class Dependences extends RadixObject implements Iterable<Dependences.Dependence> {

    /**
     * Module dependence descriptio
     */
    public class Dependence {

        private Id dependenceModuleId;
        private ERepositorySegmentType segmentType;
        // RADIX-7324
        private boolean isForced;

        public Dependence(Id dependenceModuleId, ERepositorySegmentType segmentType, boolean forced) {
            this.dependenceModuleId = dependenceModuleId;
            this.segmentType = segmentType;
            this.isForced = forced;
        }

        public boolean isForced() {
            return isForced;
        }

        public boolean isReadOnly() {
            return Dependences.this.isReadOnly();
        }
        
        public void setForced(boolean forced) {
            if (isForced != forced) {
                isForced = forced;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return dependence module segment type
         */
        public ERepositorySegmentType getSegmentType() {
            return segmentType;
        }

        /**
         * @return dependence module identifier
         */
        public Id getDependenceModuleId() {
            return dependenceModuleId;
        }

        private class Link extends DefinitionListLink<Module> {

            @Override
            protected List<Module> search() {
                List<Module> list = new ArrayList<>(1);
                doFindDependenceModule(getModule(), list);
                return list;
            }
        }
        private final Link link = new Link();

        /**
         * @return pointer to dependence module
         */
        public List<Module> findDependenceModule(Definition context) {
            final Module contextModule = (context != null ? context.getModule() : getModule());
            if (contextModule == getModule()) {
                return link.find();
            }
            List<Module> result = new LinkedList<>();
            doFindDependenceModule(contextModule, result);
            return result;
        }

        private void doFindDependenceModule(Module contextModule, List<Module> resultSet) {
            final Modules modules = (Modules) contextModule.getContainer();
            if (modules != null) {
                modules.findById(segmentType, dependenceModuleId, false, resultSet);
            }
        }

        public void appendTo(org.radixware.schemas.product.Module.Dependences xDependences) {
            org.radixware.schemas.product.Module.Dependences.Module2 xDep = xDependences.addNewModule();
            xDep.setId(this.getDependenceModuleId().toString());
            if (isForced()) {
                xDep.setIsForced(true);
            }
            if (this.segmentType != null) {
                xDep.setSegment(this.segmentType.getValue());
            }
        }
    }

    protected Dependences(Module owner) {
        super();
        setContainer(owner);
    }
    protected final Map<Id, Dependence> moduleId2Dependence = Collections.synchronizedMap(new HashMap<Id, Dependence>());

    public List<Module> findModuleById(Id moduleId) {
        final Dependence dep = moduleId2Dependence.get(moduleId);
        if (dep != null) {
            return dep.findDependenceModule(getModule());
        } else {
            return Collections.<Module>emptyList();
        }
    }

    @Override
    public Module getModule() {
        return (Module) getContainer();
    }

    public boolean contains(Module module) {
        return moduleId2Dependence.containsKey(module.getId());
    }

    public Set<Id> getModuleIds() {
        return Collections.unmodifiableSet(moduleId2Dependence.keySet());
    }

    void loadFrom(org.radixware.schemas.product.Module.Dependences dependences) {
        this.moduleId2Dependence.clear();
        if (dependences != null) {
            final List<org.radixware.schemas.product.Module.Dependences.Module2> modules = dependences.getModuleList();
            if (modules != null && !modules.isEmpty()) {
                for (org.radixware.schemas.product.Module.Dependences.Module2 xDepend : modules) {
                    final Id id = Id.Factory.loadFrom(xDepend.getId());
                    ERepositorySegmentType st = null;
                    if (xDepend.isSetSegment()) {
                        st = ERepositorySegmentType.getForValue(xDepend.getSegment());
                    }
                    boolean isForced = xDepend.isSetIsForced() && xDepend.getIsForced();
                    this.moduleId2Dependence.put(id, new Dependence(id, st, isForced));
                }
            }
        }
    }

    private static class DependenceComparableAdapter implements Comparable<DependenceComparableAdapter> {

        private final Dependence dependence;

        public DependenceComparableAdapter(Dependence dependence) {
            this.dependence = dependence;
        }

        public Dependence getDependence() {
            return dependence;
        }

        @Override
        public int compareTo(DependenceComparableAdapter to) {
            return dependence.getDependenceModuleId().toString().compareTo(to.dependence.getDependenceModuleId().toString());
        }        
    }

    protected boolean shouldBeSaved() {
        return true;
    }

    void appendTo(org.radixware.schemas.product.Module module) {
        if (!shouldBeSaved()) {
            return;
        }
        if (moduleId2Dependence.size() > 0) {
            final org.radixware.schemas.product.Module.Dependences deps = module.addNewDependences();

            // sort by ID to safe order at several saves.
            final List<DependenceComparableAdapter> adapters = new ArrayList<>();
            for (Dependence dep : moduleId2Dependence.values()) {
                adapters.add(new DependenceComparableAdapter(dep));
            }
            Collections.sort(adapters);

            for (DependenceComparableAdapter adapter : adapters) {
                adapter.getDependence().appendTo(deps);
            }
        }
    }

    public boolean add(Dependence source) {
        if (source == null) {
            return false;
        }

        if (moduleId2Dependence.containsKey(source.dependenceModuleId)) {
            return false;
        }

        final Module thisModule = this.getModule();
        if (thisModule != null && thisModule.getId().equals(source.dependenceModuleId)) {
            return false;
        }

        moduleId2Dependence.put(source.dependenceModuleId, new Dependence(source.dependenceModuleId, source.segmentType, source.isForced));
        setEditState(EEditState.MODIFIED);
        return true;
    }

    /**
     * Add module to dependencies.
     */
    public boolean add(Module module) {
        final ERepositorySegmentType segmentType = module.getSegmentType();
        final Dependence dependence = new Dependence(module.getId(), segmentType, false);
        return add(dependence);
    }
    
    private boolean addAllSilent(Collection<Dependence> dependencies){
        final Module thisModule = this.getModule();

        boolean changed = false;
        for (Dependence dependence : dependencies) {
            final Id moduleId = dependence.getDependenceModuleId();

            if (thisModule != null && thisModule.getId().equals(moduleId)) {
                continue;
            }

            if (moduleId2Dependence.containsKey(moduleId)) {
                continue;
            }

            moduleId2Dependence.put(moduleId, dependence);
            changed = true;
        }
        return changed;
    }

    public void addAll(Collection<Dependence> dependencies) {
        if (addAllSilent(dependencies)) {
            setEditState(EEditState.MODIFIED);
        }
    }

    public void clear() {
        if (!moduleId2Dependence.isEmpty()) {
            moduleId2Dependence.clear();
            setEditState(EEditState.MODIFIED);
        }
    }

    public void remove(Module module) {
        assert module != null;

        final Id moduleId = module.getId();
        remove(moduleId);
    }

    public void remove(Id moduleId) {
        if (moduleId2Dependence.remove(moduleId) != null) {
            setEditState(EEditState.MODIFIED);
        }
    }

    public void removeAll(final Collection<Id> moduleIds) {
        boolean changed = false;
        for (Id moduleId : moduleIds) {
            if (moduleId2Dependence.remove(moduleId) != null) {
                changed = true;
            }
        }
        if (changed) {
            setEditState(EEditState.MODIFIED);
        }
    }

    public List<Dependence> list() {
        return new ArrayList<>(moduleId2Dependence.values());
    }

    @Override
    public Iterator iterator() {
        return moduleId2Dependence.values().iterator();
    }

    private class UsedModuleIdsCollector implements IVisitor {

        final Set<Id> usedModuleIds = new HashSet<>();

        @Override
        public void accept(RadixObject radixObject) {
            final List<Definition> usedDefinitions = new ArrayList<>();
            radixObject.collectDependencesForModule(usedDefinitions);
            packDefinitionModules(Dependences.this.getModule(), usedDefinitions, usedModuleIds);
        }
    }

    private static void packDefinitionModules(Module thisModule, List<Definition> defs, Set<Id> moduleIds) {
        for (Definition usedDefinition : defs) {

            final Module usedModule = usedDefinition.getModule();
            if (usedModule == null || (usedModule.getSegment() != null && usedModule.getSegment().getType() == ERepositorySegmentType.UDS)) {
                continue;
            }
            if (usedModule != thisModule) {
                moduleIds.add(usedModule.getId());
            }
        }
    }

    private Set<Id> collectReallyUsedModuleIds() {
        final UsedModuleIdsCollector collector = new UsedModuleIdsCollector();
        final Module module = getModule();
        List<Definition> additionalUsages = new LinkedList<>();
        module.collectAdditionalDependencies(additionalUsages);
        packDefinitionModules(getModule(), additionalUsages, collector.usedModuleIds);
        module.visitChildren(collector, VisitorProviderFactory.createDefaultVisitorProvider());
        return collector.usedModuleIds;
    }

    /**
     * Add module to dependencies. Do not change edit state.
     *
     * @return true if added, false if already exist
     */
    protected boolean addSilent(Module module) {
        final Id moduleId = module.getId();

        final Module thisModule = this.getModule();
        if (thisModule != null && thisModule.getId().equals(moduleId)) {
            return false;
        }

        if (moduleId2Dependence.containsKey(moduleId)) {
            return false;
        }

        final ERepositorySegmentType segmentType = module.getSegmentType();
        final Dependence dependence = new Dependence(moduleId, segmentType, false);
        moduleId2Dependence.put(moduleId, dependence);
        return true;
    }

    private class AddAllDependenciesVisitor implements IVisitor {
        
        @Override
        public void accept(RadixObject radixObject) {
            Module module = (Module) radixObject;
            if (module.getSegmentType() != ERepositorySegmentType.KERNEL) {
                addSilent(module);
            }
        }
    }

    /**
     * Add all visible modules to dependencies. Do not change edit state.
     */
    private void addAllDependenciesSilent() {
        Layer layer = getModule().getSegment().getLayer();
        final AddAllDependenciesVisitor addAllDependenciesVisitor = new AddAllDependenciesVisitor();

        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                layer.visit(addAllDependenciesVisitor, VisitorProviderFactory.createModuleVisitorProvider());
            }
        });

        globalChangeVersion.incrementAndGet();
    }

    /**
     * Remove unused dependencies. Do not change edit state.
     */
    private void removeUnusedDependenciesSilent(Set<Id> ignored) {
        final Set<Id> reallyUsedModuleIds = collectReallyUsedModuleIds();

        final List<Id> moduleIdsToRemove = new ArrayList<>();
        for (Dependence dependence : moduleId2Dependence.values()) {
            final Id moduleId = dependence.getDependenceModuleId();
            if (!dependence.isForced() && !reallyUsedModuleIds.contains(moduleId) && (ignored == null || !ignored.contains(moduleId))) {
                moduleIdsToRemove.add(moduleId);
            }
        }

        for (Id unusedModuleId : moduleIdsToRemove) {
            Dependence dep = moduleId2Dependence.get(unusedModuleId);
            if (dep != null && dep.segmentType == ERepositorySegmentType.KERNEL && dep.findDependenceModule(getModule()) != null) {
                continue;
            }
            moduleId2Dependence.remove(unusedModuleId);
        }
    }

    /**
     * Add required dependencies.
     */
    public void addRequired() {
        final Map<Id, Dependence> snapshot = new HashMap<>(moduleId2Dependence);

        addAllDependenciesSilent();
        final Set<Id> ignored = snapshot.keySet();
        removeUnusedDependenciesSilent(ignored);

        if (!moduleId2Dependence.equals(snapshot)) {
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Remove unused dependencies.
     */
    public void removeUnused() {
        final Map<Id, Dependence> snapshot = new HashMap<>(moduleId2Dependence);

        removeUnusedDependenciesSilent(null);

        if (!moduleId2Dependence.equals(snapshot)) {
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Add required and remove unused dependencies.
     *
     * @return true if some changes occured.
     */
    public boolean actualize() {
        return actualize(false);
    }
    
    private Map<Id, Dependence> moduleId2DependenceSnapshot = null; 
    public boolean actualize(boolean needConfirmation){
        final Map<Id, Dependence> snapshot = new HashMap<>(moduleId2Dependence);
        addAllDependenciesSilent();
        removeUnusedDependenciesSilent(null);
        if (!moduleId2Dependence.equals(snapshot)) {
            if (needConfirmation){
                moduleId2DependenceSnapshot = new HashMap<>(snapshot);
            } else {
                confirmationOfActualization(true);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public void confirmationOfActualization(boolean result){
        if (moduleId2DependenceSnapshot != null){
            if (!result){
                moduleId2Dependence.clear();
                addAllSilent(moduleId2DependenceSnapshot.values());              
            }
            moduleId2DependenceSnapshot = null; 
        } 
        
        if (result){
            setEditState(EEditState.MODIFIED);
        }
    }
    // for test
    public int size() {
        return moduleId2Dependence.size();
    }
}
