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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;


final class SqmlTableIndices implements ISqmlTableIndices {

    private final List<ISqmlTableIndexDef> indices = new ArrayList<ISqmlTableIndexDef>();
    private final ISqmlTableIndexDef primaryIndex;

    public SqmlTableIndices(final IClientEnvironment environment, final DdsTableDef tableDef) {
        final List<DdsIndexDef> indeces = tableDef.getIndices().get(EScope.ALL);
        for (DdsIndexDef index : indeces) {
            if (index.isSecondaryKey()) {
                indices.add(new SqmlTableIndexImpl(environment, index));
            }
        }
        primaryIndex = new SqmlTableIndexImpl(environment, tableDef.getPrimaryKey());
    }

    @Override
    public int size() {
        return indices.size();
    }

    @Override
    public ISqmlTableIndexDef getIndexById(Id indexId) {
        for (ISqmlTableIndexDef index : indices) {
            if (index.getId().equals(indexId)) {
                return index;
            }
        }
        return null;
    }

    @Override
    public ISqmlTableIndexDef get(int idx) {
        return indices.get(idx);
    }

    @Override
    public Iterator<ISqmlTableIndexDef> iterator() {
        return indices.iterator();
    }

    @Override
    public ISqmlTableIndexDef getPrimaryIndex() {
        return primaryIndex;
    }
}
