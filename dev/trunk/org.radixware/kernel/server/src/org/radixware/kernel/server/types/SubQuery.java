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

package org.radixware.kernel.server.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;

public abstract class SubQuery {

    private final String sql;
    private final int paramCount;
    private final Arte arte;

    public SubQuery(
            final Arte arte,
            final String sql,
            final int paramCount) {
        this.arte = arte;
        this.sql = sql;
        this.paramCount = paramCount;
    }

    public Arte getArte() {
        return arte;
    }

    /**
     * Check if object exists that reference VIA childRef to objects wich
     * primary keys are selected by this subquery
     *
     *
     * @param childRef - parentTable should be the same table as getDdsMeta()
     * (reference to detail tables are not supported)
     * @return
     */
    public boolean isChildExists(final DdsReferenceDef childRef) {
        final StringBuffer existSql = new StringBuffer(200);

        existSql.append("select /*+ FIRST_ROWS(1) */ 1 from ");
        existSql.append(getArte().getDefManager().getTableDef(childRef.getChildTableId()).getDbName());
        existSql.append(" where (");
        final StringBuffer sTmp = new StringBuffer();
        boolean isFirst = true;
        for (DdsReferenceDef.ColumnsInfoItem refProp : childRef.getColumnsInfo()) {
            if (isFirst) {
                isFirst = false;
            } else {
                existSql.append(',');
                sTmp.append(',');
            }
            existSql.append(RadClassDef.getPropDbPresentation(getArte(), childRef.getChildTableId(), null, refProp.getChildColumnId()));
            sTmp.append(RadClassDef.getPropDbPresentation(getArte(), childRef.getParentTableId(), null, refProp.getParentColumnId()));
        }
        existSql.append(") in (select ");
        existSql.append(sTmp);
        existSql.append(" from ");
        final DdsTableDef childRefParentTable = getArte().getDefManager().getTableDef(childRef.getParentTableId());
        existSql.append(childRefParentTable.getDbName());
        existSql.append(" where (");
        boolean bComaNeeded = false;
        for (DdsIndexDef.ColumnInfo pkProp : childRefParentTable.getPrimaryKey().getColumnsInfo()) {
            if (bComaNeeded) {
                existSql.append(',');
            }
            existSql.append(pkProp.getColumn().getDbName());
            bComaNeeded = true;
        }
        existSql.append(") in (");
        existSql.append(getSql());
        existSql.append(")) and RowNum < 2");

        final String qrySql = existSql.toString();
        getArte().getTrace().put(EEventSeverity.DEBUG, "Child exists query built: " + qrySql, EEventSource.DB_QUERY_BUILDER);

        try {
            final PreparedStatement qry;
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qry = getArte().getDbConnection().get().prepareStatement(qrySql);
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                if (getParamCount() > 0) {
                    setParams(qry, 1);
                }
                try {
                    final ResultSet rs = qry.executeQuery();
                    try {
                        return rs.next();
                    } finally {
                        rs.close();
                    }
                } finally {
                    qry.close();
                }
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't check child record exists: " + e.getMessage(), e);
        }
    }

    /**
     * Установить значения параметров в указанном PreparedStatement. Первый
     * параметр этого запроса будет установлен по индексу startIdx, последний по
     * индексу (paramCount+startIdx-1). Если paramCount == 0 - метод ничего не
     * делает.
     *
     * @param stmt
     * @param startIdx > 0
     */
    public abstract void setParams(PreparedStatement stmt, int startIdx);

    /**
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * @return the paramCount
     */
    public int getParamCount() {
        return paramCount;
    }
}
