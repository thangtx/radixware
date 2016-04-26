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

package org.radixware.kernel.server.meta.clazzes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Release;

final class RadClassApJoinItem {

    private static final String DRC_JOIN_TAB_ALIAS_PREFIX = "DRC_T";
    private final String alias;
    private final DdsReferenceDef reference;
    private final DdsTableDef table;
    private final Map<Id, RadClassApJoinItem> joinsByRefId;
    private final Release release;

    protected RadClassApJoinItem(final Release release, final DdsReferenceDef reference, final String alias) {
        super();
        this.reference = reference;
        this.table = release.getTableDef(reference.getParentTableId());
        this.alias = alias;
        this.joinsByRefId = new HashMap<>();
        this.release = release;
    }

    protected RadClassApJoinItem(final Release release, final DdsTableDef table, final String alias) {
        super();
        this.reference = null;
        this.table = table;
        this.alias = alias;
        this.joinsByRefId = new HashMap<>();
        this.release = release;
    }

    protected final void appendPropDbPresentationSql(final StringBuilder sql, final Id propId) {
        sql.append(RadClassDef.getPropDbPresentation(release, getTable().getId(), getAlias(), propId));
    }

    protected RadClassApJoinItem join(final Id referenceId, final int[] joinsCounter) {
            RadClassApJoinItem res = joinsByRefId.get(referenceId);
            if (res != null) {
                return res;
            }
            res = new RadClassApJoinItem(release, release.getReferenceDef(referenceId), DRC_JOIN_TAB_ALIAS_PREFIX + String.valueOf(++joinsCounter[0]));
            joinsByRefId.put(referenceId, res);
            return res;
    }

    protected RadClassApJoinItem join(final List<Id> referenceIds, final int startIdx, final int[] joinsCounter) {
        if (startIdx == referenceIds.size()) {
            return this;
        }
        final RadClassApJoinItem res = join(referenceIds.get(startIdx), joinsCounter);
        return res.join(referenceIds, startIdx + 1, joinsCounter);
    }

    protected void appendJoinsSql(final StringBuilder sql, final String childTabAlias) {
        sql.append(" LEFT OUTER JOIN ");
        sql.append(release.getTableDef(getReference().getParentTableId()).getDbName());
        sql.append(' ');
        sql.append(getAlias());
        sql.append(" ON ");
        boolean isFirstRefProp = true;
        for (DdsReferenceDef.ColumnsInfoItem refProp : getReference().getColumnsInfo()) {
            if (isFirstRefProp) {
                isFirstRefProp = false;
            } else {
                sql.append(" AND ");
            }
            sql.append(RadClassDef.getPropDbPresentation(release, getReference().getChildTableId(), childTabAlias, refProp.getChildColumnId()));
            sql.append('=');
            sql.append(RadClassDef.getPropDbPresentation(release, getReference().getParentTableId(), getAlias(), refProp.getParentColumnId()));
        }
        for (RadClassApJoinItem join : joinsByRefId.values()) {
            join.appendJoinsSql(sql, getAlias());
        }
    }

    /**
     * @return the alias
     */
    String getAlias() {
        return alias;
    }

    /**
     * @return the reference
     */
    DdsReferenceDef getReference() {
        return reference;
    }

    /**
     * @return the table
     */
    DdsTableDef getTable() {
        return table;
    }

    /**
     * @return the joinsByRefId
     */
    Map<Id, RadClassApJoinItem> getJoinsByRefId() {
        return Collections.unmodifiableMap(joinsByRefId);
    }
}
