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

import java.awt.datatransfer.Transferable;
import java.util.*;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver.Resolution;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.types.Id;

/**
 * Extendable definition collection.
 *
, abelyaev.
 */
public abstract class ExtendableDefinitions<T extends Definition> extends RadixObject {

    /**
     * Collection mode Use for build element collection and for search engine
     */
    public enum EScope {

        /**
         * Only local elements should appear in operation result
         */
        LOCAL,
        /**
         * Only local elements and elements from overwritten definitions should
         * appear in operation result
         */
        LOCAL_AND_OVERWRITE,
        /**
         * Local elements,elements from overwritten definitions and elements
         * inherited by other ways may apperar in operation result
         */
        ALL
    }
    private final Definitions<T> local;

    protected ExtendableDefinitions(RadixObject container) {
        this(container, new Definitions<T>());
    }

    protected ExtendableDefinitions(RadixObject container, Definitions<T> localCollection) {
        super();
        setContainer(container);
        local = localCollection;
        local.setContainer(this);
        //    local.getContainerChangesSupport().addEventListener(EntireChangesSupport.getInstance(Definition.class));
    }

    protected abstract HierarchyIterator<? extends ExtendableDefinitions<T>> newIterator(EScope scope, HierarchyIterator.Mode mode);

    /**
     * Get collection of local definitions (new or overrided).
     */
    public Definitions<T> getLocal() {
        return local;
    }

    public List<T> get(EScope scope) {
        return get(scope, HierarchyIterator.Mode.FIND_FIRST);
    }

    public List<T> getAll(EScope scope) {
        return get(scope, HierarchyIterator.Mode.FIND_ALL);
    }

    /**
     * Get collection of all definitions (base, new or overrided).
     */
    public List<T> get(EScope scope, HierarchyIterator.Mode mode) {
        if (scope == EScope.LOCAL) {
            return getLocal().list();
        }
        final int size = local.size();
        final Set<Id> ids = new HashSet<>(size);
        final ArrayList<T> listOfElements = new ArrayList<>(size);

        final HierarchyIterator<? extends ExtendableDefinitions<T>> iter = newIterator(scope, mode);

        while (iter.hasNext()) {
            for (T t : iter.next().first().getLocal()) {
                final Id id = t.getId();
                if (!ids.contains(id)) {
                    ids.add(id);
                    listOfElements.add(t);
                }
            }
        }

        return listOfElements;
    }

    public List<T> get(EScope scope, IFilter<T> filter) {
        return get(scope, filter, HierarchyIterator.Mode.FIND_FIRST);
    }

    public List<T> getAll(EScope scope, IFilter<T> filter) {
        return get(scope, filter, HierarchyIterator.Mode.FIND_ALL);
    }

    public List<T> get(EScope scope, IFilter<T> filter, HierarchyIterator.Mode mode) {
        if (filter == null) {
            throw new NullPointerException();
        }
        if (scope == EScope.LOCAL) {
            return getLocal().list(filter);
        }
        final int size = local.size();
        final Set<Id> ids = new HashSet<>(size);
        final ArrayList<T> listOfElements = new ArrayList<>(size);

        final HierarchyIterator<? extends ExtendableDefinitions<T>> iter = newIterator(scope, mode);

        while (iter.hasNext()) {
            final List<T> loc = iter.next().first().
                    getLocal().list(filter);
            for (T t : loc) {
                final Id id = t.getId();
                if (!ids.contains(id)) {
                    ids.add(id);
                    listOfElements.add(t);
                }
            }
        }

        return listOfElements;
    }

    /**
     * Find definition by identifier in local definitions collection or based.
     * return definition or null if not found.
     */
    private class SearchInfo {

        private SearchResult<T> local;
        private SearchResult<T> local_and_ovr;
        private SearchResult<T> all;
    }

    private class Link extends ObjectLink<Map<Id, SearchInfo>> {

        @Override
        protected Map<Id, SearchInfo> search() {
            return new HashMap<>();
        }
    }
    private final Link link = new Link();

    public final SearchResult<T> findById(Id id, EScope scope) {
        T localResult = getLocal().findById(id);
        if (localResult != null || scope == EScope.LOCAL) {
            return new SearchResult.Single<>(localResult);
        }

//        SearchResult<T> existingResult = null;
//
//        if (existingResult == null) {
        try {
            HierarchyIterator<? extends ExtendableDefinitions<T>> iter = newIterator(scope, HierarchyIterator.Mode.FIND_FIRST);
            if (iter != null) {
                List<T> result = new ArrayList(1);
                final Set<ExtendableDefinitions> lookup = new HashSet<>();//KAA: cycle inheritance protection (RADIX-2409)
                loop:
                while (iter.hasNext()) {
                    HierarchyIterator.Chain<? extends ExtendableDefinitions<T>> chain = iter.next();

                    boolean chainCompleted = true;

                    for (ExtendableDefinitions<T> ed : chain) {
                        if (lookup.contains(ed)) {//inheritance possible
                            continue loop;
                        }
                        T definition = ed.getLocal().findById(id);

                        if (definition != null) {
                            result.add(definition);
                        } else {
                            chainCompleted = false;
                        }
                        lookup.add(ed);
                    }
                    if (chainCompleted) {
                        if (result.isEmpty()) {
                            return SearchResult.empty();
                        } else {
                            if (result.size() == 1) {
                                return SearchResult.single(result.get(0));
                            } else {
                                return SearchResult.list(result);
                            }
                        }
                    }
                }
                if (result.isEmpty()) {
                    return SearchResult.empty();

                } else {
                    if (result.size() == 1) {
                        return SearchResult.single(result.get(0));
                    } else {
                        return SearchResult.list(result);
                    }
                }

            } else {
                //localResult = getLocal().findById(id);
                return localResult == null ? SearchResult.<T>empty() : SearchResult.single(localResult);
            }
        } finally {
        }
//        } else {
//            return existingResult;
//        }
    }

    /**
     * Find definition by identifier in local definitions collection or based.
     *
     * @throws DefinitionNotFoundError
     */
    public T getById(Id id, EScope scope) {
        SearchResult<T> definition = findById(id, scope);
        if (definition.get() == null) {
            throw new DefinitionNotFoundError(id);
        }
        return definition.get();
    }

    /**
     * @return true if specified object exist in collection or based, false
     * otherwise.
     */
    public final boolean contains(T definition, EScope scope) {
        if (scope == EScope.LOCAL) {
            return getLocal().contains(definition);
        }
        HierarchyIterator<? extends ExtendableDefinitions<T>> iter = newIterator(scope, HierarchyIterator.Mode.FIND_FIRST);
        if (iter != null) {
            while (iter.hasNext()) {
                if (iter.next().first().getLocal().contains(definition)) {
                    return true;
                }
            }
            return false;
        } else {
            return getLocal().contains(definition);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        local.visit(visitor, provider);
    }

    public T overwrite(T sourceDef) {
        synchronized (this) {
            return getLocal().overwrite(sourceDef);
        }
    }

//    @Override
//    public void collectDependences(List<Definition> list) {
//        super.collectDependences(list);
//        List<T> items = get(EScope.LOCAL_AND_OVERWRITE);
//        for (T item : items) {
//            item.collectDependences(list);
//        }
//    }
    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return getLocal().getClipboardSupport();
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    /**
     * Looks for same extendable definition set inside given definition
     */
    public ExtendableDefinitions<T> findInstance(Definition owner) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public T override(IOverridable<T> source) {
        if (source == null) {
            throw new NullPointerException();
        }

        Definition sourceDef = (Definition) source;
        Definition sourceOwner = sourceDef.getOwnerDefinition();


        if (sourceOwner == null) {
            throw new RadixObjectError("Contextless definition can not be overriden.", this);
        }

        Definition thisOwner = (Definition) getOwnerDefinition();

        if (thisOwner == null) {
            throw new RadixObjectError("Contextless collection can not override.", this);
        }

        if (thisOwner == sourceOwner) {
            throw new RadixObjectError("Can not override.", this);
        }

        ClipboardSupport cs = ((Definition) source).getClipboardSupport();

        if (cs == null) {
            throw new RadixObjectError(((Definition) source).getQualifiedName() + " can not be overrided (no clipboard support found).", this);
        }


        Transferable t = cs.createTransferable(ETransferType.COPY);
        this.getClipboardSupport().paste(t, new ClipboardSupport.DuplicationResolver() {

            @Override
            public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
                return Resolution.CANCEL;
            }
        });
        T result = findById(sourceDef.getId(), EScope.LOCAL).get();

        if (result != null) {
            ((IOverridable) result).afterOverride();
        }

        return result;
    }
}
