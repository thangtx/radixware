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

package org.radixware.kernel.common.defs.dds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixObjectError;

/**
 * Коллекция расширяемых DDS дефиниций. Реализован поиск по имени в базе данных.
 *
 */
public abstract class DdsExtendableDefinitions<T extends DdsDefinition & IDdsTableItemDef> extends ExtendableDefinitions<T> {

    protected DdsExtendableDefinitions(DdsTableDef ownerTable, DdsDefinitions<T> localCollection) {
        super(ownerTable, localCollection);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Find definition in the list by database name.
     *
     * @param dbName database name of definition, which it is required to find.
     * @return definition of null, if definition not found.
     */
    public T findByDbName(String dbName) {
        for (T dbDefinition : this.get(EScope.LOCAL_AND_OVERWRITE)) {
            if (dbName.equals(dbDefinition.getDbName())) {
                return dbDefinition;
            }
        }
        return null;
    }

    /**
     * Find definition in the list by database name.
     *
     * @param dbName database name of definition, which it is required to find.
     * @return definition.
     * @throws DefinitionError
     */
    public T getByDbName(String dbName) {
        T dbDefinition = findByDbName(dbName);
        if (dbDefinition == null) {
            throw new RadixObjectError("Definition with database name '" + String.valueOf(dbName) + "' is not found.", this);
        }
        return null;
    }

    /**
     * @return owner table for the collection.
     */
    public DdsTableDef getOwnerTable() {
        return (DdsTableDef) getContainer();
    }

    /**
     * Find overwritten table for the collection.
     *
     * @return overwritten table or null if not found.
     */
    public DdsTableDef findOverwrittenTable() {
        DdsTableDef ownerTable = getOwnerTable();
        if (ownerTable != null) {
            return ownerTable.findOverwritten();
        } else {
            return null;
        }
    }

    // for DDS it is required to display columns like 'id' firstly.
    private List<T> orderFromBaseToCurrent(List<T> list, EScope scope) {
        if (scope == EScope.LOCAL) {
            return list;
        }
        final HierarchyIterator<? extends ExtendableDefinitions<T>> iter = newIterator(scope, HierarchyIterator.Mode.FIND_FIRST);
        final List<Definitions<T>> fromBaseToCurrent = new ArrayList<>(1);
        while (iter.hasNext()) {
            fromBaseToCurrent.add(0, iter.next().first().getLocal());
        }

        if (fromBaseToCurrent.size() <= 1) {
            return list;
        }

        final Set<T> set = new HashSet<T>(list);
        final ArrayList<T> result = new ArrayList<T>(list.size());

        for (Definitions<T> defs : fromBaseToCurrent) {
            for (T t : defs) {
                if (set.contains(t)) {
                    result.add(t);
                }
            }
        }

        return result;
    }

    /**
     * Get collection of all definitions (base, new or overrided).
     */
    @Override
    public List<T> get(EScope scope) {
        final List<T> list = super.get(scope);
        return orderFromBaseToCurrent(list, scope);
    }

    @Override
    public List<T> get(EScope scope, IFilter<T> filter) {
        final List<T> list = super.get(scope, filter);
        return orderFromBaseToCurrent(list, scope);
    }
}
