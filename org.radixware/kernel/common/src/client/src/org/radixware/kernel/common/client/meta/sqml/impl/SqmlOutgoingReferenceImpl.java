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
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlOutgoingReference;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.types.Id;


final class SqmlOutgoingReferenceImpl extends SqmlReferenceImpl implements ISqmlOutgoingReference {

    public SqmlOutgoingReferenceImpl(final IClientEnvironment environment, final DdsReferenceDef refDef) {
        super(environment, refDef);
    }

    @Override
    public String getTitle() {
        return getReferenceDef().getName();
    }

    @Override
    public Id getReferencedTableId() {
        return getReferenceDef().getParentTableId();
    }

    @Override
    public List<String> getChildColumnNames() {
        final List<String> result = new ArrayList<>(8);
        for (DdsReferenceDef.ColumnsInfoItem columnInfo : getReferenceDef().getColumnsInfo()) {
            result.add(columnInfo.findChildColumn() == null ? "#" + columnInfo.getChildColumnId().toString() : columnInfo.findChildColumn().getName());
        }
        return result;
    }
}