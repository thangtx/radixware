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

import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;

/**
 * Usually attached to module, allows to search definition by identified inside
 * or outside of the attached definition.
 */
public abstract class DefinitionSearcher<T extends Definition> {

    /**
     * Delegated class, allows to rename searchByKey to searchById.
     */
    private class IdSearchEngine extends SearchEngine<T, Id> {

        public IdSearchEngine(Definition context) {
            super(context);
        }

        @Override
        protected T findInsideByKey(Id key) {
            return findInsideById(key);
        }

        @Override
        protected SearchEngine<T, Id> findEngine(Module module) {
            DefinitionSearcher<T> searcher = findSearcher(module);
            if (searcher != null) {
                return searcher.searchEngine;
            } else {
                return null;
            }
        }

        @Override
        protected boolean shouldVisitDependence(Id moduleId, Id key) {
            return DefinitionSearcher.this.shouldVisitDependence(moduleId, key);
        }
    }
    private final IdSearchEngine searchEngine;

    protected final Definition getContext() {
        return searchEngine.context;
    }

    protected DefinitionSearcher(Definition context) {
        this.searchEngine = new IdSearchEngine(context);
    }

    protected boolean shouldVisitDependence(Id moduleId, Id key) {
        return true;
    }

    public abstract DefinitionSearcher<T> findSearcher(Module module);

    /**
     * Find definition by identified inside of the attached definition.
     */    
    public abstract T findInsideById(Id id);

    /**
     * Find definition by identified in scope of the attached definition.
     *
     * @definition or null if not found.
     */
    public SearchResult<T> findById(Id id) {
        return searchEngine.findByKey(id);
    }

    /**
     * Find definition by identified in scope of the attached definition.
     *
     * @return definition.
     * @throws DefinitionNotFoundError.
     */
    public T getById(Id id) {
        SearchResult<T> definition = findById(id);
        if (definition.get() == null) {
            throw new DefinitionNotFoundError(id);
        }
        return definition.get();
    }
}
