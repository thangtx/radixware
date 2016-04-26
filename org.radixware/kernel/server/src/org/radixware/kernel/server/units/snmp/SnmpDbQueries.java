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

package org.radixware.kernel.server.units.snmp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.sap.ReadSapOptionsQuery;
import org.snmp4j.smi.OctetString;


public class SnmpDbQueries implements IDbQueries {

    private final SnmpAgentUnit unit;
    private PreparedStatement qryReadMetricData;
    private PreparedStatement qryReadOptions;
    private ReadSapOptionsQuery qryReadSapOptions;

    public SnmpDbQueries(SnmpAgentUnit unit) {
        this.unit = unit;
    }

    public SnmpAgentUnit.Options readOptions() throws SQLException {
        if (qryReadOptions == null) {
            qryReadOptions = unit.getDbConnection().prepareStatement("select "
                    + "a.agentAddress, "
                    + "(select extSystemCode from rdx_system where id = 1) systemRid, "
                    + "a.sapId, "
                    + "a.useNotificationService, "
                    + "a.communityString, "
                    + "m.id managerId, "
                    + "m.title managerTitle, "
                    + "m.address managerAddress "
                    + "from rdx_sm_snmpagentunit a left join rdx_sm_snmpmanager m on a.managerId = m.id "
                    + "where a.id = ?");
            qryReadOptions.setLong(1, unit.getId());

            qryReadSapOptions = new ReadSapOptionsQuery(unit.getDbConnection());
        }
        try (ResultSet rs = qryReadOptions.executeQuery()) {
            rs.next();
            final boolean useSap = rs.getBoolean("useNotificationService");
            return new SnmpAgentUnit.Options(
                    rs.getString("agentAddress"),
                    rs.getString("communityString"),
                    rs.getLong("systemRid") == 0 ? 1 : rs.getLong("systemRid"),
                    useSap,
                    rs.getLong("sapId"),
                    useSap ? rs.getLong("managerId") : null,
                    useSap ? rs.getString("managerTitle") : null,
                    useSap ? rs.getString("managerAddress") : null);
        }
    }

    public List<MetricDataRecord> readMetricData() throws SQLException {
        if (qryReadMetricData == null) {
            qryReadMetricData = unit.getDbConnection().prepareStatement("select "
                    + "t.id typeId,"
                    + "t.kind, "
                    + "t.title, "
                    + "t.period, "
                    + "t.lowErrorVal, "
                    + "t.lowWarnVal, "
                    + "t.highWarnVal, "
                    + "t.highErrorVal, "
                    + "t.warnDelay, "
                    + "t.errorDelay, "
                    + "t.escalationDelay, "
                    + "t.timingSection, "
                    + "s.id stateId, "
                    + "s.instanceId, "
                    + "s.unitId, "
                    + "s.systemId, "
                    + "s.serviceUri, "
                    + "s.netChannelId, "
                    + "s.begTime, "
                    + "s.endTime, "
                    + "s.begVal, "
                    + "s.endVal, "
                    + "s.minVal, "
                    + "s.maxVal, "
                    + "s.avgVal "
                    + "from rdx_sm_metricstate s inner join rdx_sm_metrictype t on s.typeId = t.id order by t.id");
        }
        final List<MetricDataRecord> result = new ArrayList<>();
        try (ResultSet rs = qryReadMetricData.executeQuery()) {
            while (rs.next()) {
                result.add(new MetricDataRecord(
                        getPositive32(rs, "typeId"),
                        new OctetString(rs.getString("kind")),
                        new VUTF8String(rs.getString("title")),
                        getPositive32(rs, "period"),
                        getDecimal2(rs, "lowErrorVal"),
                        getDecimal2(rs, "lowWarnVal"),
                        getDecimal2(rs, "highWarnVal"),
                        getDecimal2(rs, "highErrorVal"),
                        getPositive32(rs, "warnDelay"),
                        getPositive32(rs, "errorDelay"),
                        getPositive32(rs, "escalationDelay"),
                        getPositive32(rs, "stateId"),
                        getPositive32(rs, "instanceId"),
                        getPositive32(rs, "unitId"),
                        getPositive32(rs, "systemId"),
                        new OctetString(rs.getString("serviceUri") == null ? "" : rs.getString("serviceUri")),
                        getPositive32(rs, "netChannelId"),
                        new OctetString(rs.getString("timingSection") == null ? "" : rs.getString("timingSection")),
                        getTimestamp(rs, "begTime"),
                        getTimestamp(rs, "endTime"),
                        getDecimal2(rs, "begVal"),
                        getDecimal2(rs, "endVal"),
                        getDecimal2(rs, "minVal"),
                        getDecimal2(rs, "maxVal"),
                        getDecimal2(rs, "avgVal")));
            }
        }
        return result;
    }

    private VPositive32 getPositive32(final ResultSet rs, final String columnName) throws SQLException {
        final long val = rs.getLong(columnName);
        return new VPositive32(rs.wasNull() ? null : val);
    }

    private VDecimal2 getDecimal2(final ResultSet rs, final String columnName) throws SQLException {
        final double val = rs.getDouble(columnName);
        return new VDecimal2(rs.wasNull() ? null : val);
    }

    private VTimestamp getTimestamp(final ResultSet rs, final String columnName) throws SQLException {
        final Timestamp val = rs.getTimestamp(columnName);
        return new VTimestamp(val == null ? 0 : val.getTime());
    }

    @Override
    public void closeAll() {
        if (qryReadMetricData != null) {
            try {
                qryReadMetricData.close();
            } catch (Throwable ex) {
                //ignore
            } finally {
                qryReadMetricData = null;
            }
        }
        if (qryReadOptions != null) {
            try {
                qryReadOptions.close();
            } catch (Throwable ex) {
                //ignore
            } finally {
                qryReadOptions = null;
            }
        }
        if (qryReadSapOptions != null) {
            try {
                qryReadSapOptions.close();
            } finally {
                qryReadSapOptions = null;
            }
        }
    }
}
