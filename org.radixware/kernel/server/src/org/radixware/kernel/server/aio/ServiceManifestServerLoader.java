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
package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.radixware.kernel.common.enums.EChannelType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.enums.EServiceAccessibility;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.SoapServiceOptions;
import org.radixware.kernel.common.sc.WsdlSource;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;
import org.radixware.kernel.common.utils.net.JmsUtils;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.sap.ReadSapOptionsQuery;
import org.radixware.kernel.server.soap.ServerSoapUtils;

public abstract class ServiceManifestServerLoader implements ServiceManifestLoader {

    public static final int SELFCHECK_TIMEOUT_SECONDS = 60 * 5;//5 minutes
    private static final String EXPLICIT_SAPS = SystemPropUtils.getStringSystemProp("rdx.explicit.saps", null);

    private static final String readScpWithout1StmtSQL
            = "select sap.id, "
            + "sap.name, "
            + "nvl(s2s.extAddress, sap.address) address, "
            + "sap.securityProtocol, "
            + "sap.clientKeyAliases, "
            + "sap.serverCertAliases, "
            + "sap.cipherSuites, "
            + "sap.channelType, "
            + "s2s.sapPriority, "
            + "s2s.blockingPeriod, "
            + "s2s.connectTimeout, "
            + "sap.clientAttrs, "
            + "sap.serviceQName, "
            + "sap.portQName, "
            + "sap.serviceLastUpdateTime, "
            + "sap.useWsSecurity, "
            + "sap.selfCheckTime, "
            + "sap.selfCheckTimeMillis, "
            + "RDX_Utils.getUnixEpochMillis() dbCurMillis, "
            + "nvl2(sap.serviceWsdl, 1, 0) wsdlPresent, "
            + "dsm.uri dsmWsdlUri, "
            + "dsm.lastUpdateTime dsmWsdlLastUpdateTime, "
            + "(select inst.aadcMemberId from rdx_instance inst where sap.systemInstanceId=inst.id) aadcMemberId "
            + "from "
            + "rdx_system sys, "
            + "rdx_scp2sap s2s, "
            + "rdx_sap sap, "
            + "rdx_service svc left join rdx_sb_datascheme dsm on dsm.uri = svc.wsdlUri "
            + "where "
            + "sys.id=? "
            + "and sap.systemId=sys.id "
            + "and sap.uri=? "
            + "and sap.uri = svc.uri "
            + "and sap.systemId = svc.systemId "
            + "and sap.isActive=1 "
            + "and s2s.systemId=sap.systemId "
            + "and s2s.sapId=sap.id "
            + " and (sap.channelType != '" + EChannelType.INTERNAL_PIPE.getValue() + "' or ? = sap.systemInstanceId)"
            + " and s2s.scpName=?";
    private static final Stmt readScpWithout1Stmt = new Stmt(readScpWithout1StmtSQL, Types.BIGINT, Types.VARCHAR, Types.BIGINT, Types.VARCHAR);

    private static final String readScpWith1StmtSQL
            = readScpWithout1StmtSQL
            + " and sap.accessibility != '" + EServiceAccessibility.INTER_SYSTEM.getValue() + "'";
    private static final Stmt readScpWith1Stmt = new Stmt(readScpWith1StmtSQL, Types.BIGINT, Types.VARCHAR, Types.BIGINT, Types.VARCHAR);

    private static final String readNullScpWithout1StmtSQL
            = "select sap.id, "
            + "sap.name, "
            + "sap.address, "
            + "sap.securityProtocol, "
            + "sap.clientKeyAliases, "
            + "sap.serverCertAliases, "
            + "sap.cipherSuites, "
            + "sap.channelType, "
            + "sap.clientAttrs, "
            + "sap.serviceQName, "
            + "sap.portQName, "
            + "sap.serviceLastUpdateTime, "
            + "sap.useWsSecurity, "
            + "sap.selfCheckTime, "
            + "sap.selfCheckTimeMillis, "
            + "RDX_Utils.getUnixEpochMillis() dbCurMillis, "
            + "nvl2(sap.serviceWsdl, 1, 0) wsdlPresent, "
            + "dsm.uri dsmWsdlUri, "
            + "dsm.lastUpdateTime dsmWsdlLastUpdateTime, "
            + "(select inst.aadcMemberId from rdx_instance inst where sap.systemInstanceId=inst.id) aadcMemberId "
            + "from "
            + "rdx_sap sap, "
            + "rdx_service svc left join rdx_sb_datascheme dsm on dsm.uri = svc.wsdlUri "
            + "where "
            + "sap.systemId=? "
            + "and sap.uri=? "
            + "and sap.uri = svc.uri "
            + "and sap.systemId = svc.systemId "
            + "and sap.isActive=1 "
            + " and (sap.channelType != '" + EChannelType.INTERNAL_PIPE.getValue() + "' or ? = sap.systemInstanceId)";
    private static final Stmt readNullScpWithout1Stmt = new Stmt(readNullScpWithout1StmtSQL, Types.BIGINT, Types.VARCHAR, Types.BIGINT);

    private static final String readNullScpWith1StmtSQL
            = readNullScpWithout1StmtSQL
            + " and sap.accessibility != '" + EServiceAccessibility.INTER_SYSTEM.getValue() + "'";
    private static final Stmt readNullScpWith1Stmt = new Stmt(readNullScpWith1StmtSQL, Types.BIGINT, Types.VARCHAR, Types.BIGINT);

    public static enum EOnSqlExceptionAction {

        THROW_DATABASE_ERROR,
        RETURN_CACHE_OR_THROW,
        RETURN_CACHE_OR_NOTHING,
        RETRY;
    }
    private Cache cache = null;
    private IDbQueries delegate = new DelegateDbQueries(this, ServiceManifestServerLoader.class, null);
    private final AadcManager aadcManager;

    protected ServiceManifestServerLoader() {
        final Instance curInst = Instance.get();
        aadcManager = curInst != null ? curInst.getAadcManager() : null;
    }

    protected abstract Connection getDbConnection();

    /**
     * Optional reference to ARTE. Used for profiling.
     *
     * @return
     */
    protected Arte getArte() {
        return null;
    }

    protected LocalTracer getTracer() {
        return null;
    }

    protected Integer getTargetAadcMemberId() {
        return null;
    }

    protected EOnSqlExceptionAction onSqlException(final SQLException exception, final EOnSqlExceptionAction prevAction) {
        final LocalTracer tracer = getTracer();
        if (tracer != null) {
            tracer.put(EEventSeverity.WARNING, "Sql exception while updating SAP list: " + ExceptionTextFormatter.throwableToString(exception), null, null, false);
        }
        return EOnSqlExceptionAction.RETURN_CACHE_OR_NOTHING;
    }

    @Override
    public List<SapClientOptions> readSaps(final Long systemId, final Long thisInstanceId, final String serviceUri, final String scpName, final long maxCachedValAgeMillis) {
        if (systemId == null) {
            throw new IllegalArgumentException("SystemId is null");
        }
        if (EXPLICIT_SAPS != null) {
            final String[] addressLists = EXPLICIT_SAPS.split(";");
            for (String addressList : addressLists) {
                final String[] uriPatternAndAddresses = addressList.split("=");
                if (uriPatternAndAddresses != null && uriPatternAndAddresses.length == 2 && uriPatternAndAddresses[0] != null && serviceUri.contains(uriPatternAndAddresses[0]) && uriPatternAndAddresses[1] != null) {
                    final String[] addresses = uriPatternAndAddresses[1].split(",");
                    final List<SapClientOptions> result = new ArrayList<>();
                    for (String address : addresses) {
                        final CompositeInetSocketAddress addr = ValueFormatter.parseCompositeInetSocketAddress(address);
                        SapClientOptions opts = new SapClientOptions();
                        opts.setName("ExplicitSap_" + address);
                        opts.setAddress(new SapAddress(addr));
                        opts.setBlockingPeriodMillis(100);
                        opts.setSecurityProtocol(EPortSecurityProtocol.NONE);
                        opts.setId(1);
                        opts.setPriority(10);
                        result.add(opts);
                    }
                    return result;
                }
            }
        }
        EOnSqlExceptionAction onSqlErrorAction = null;
        while (true) {
            final Connection db = getDbConnection();
            if (db == null) {
                return Collections.emptyList();
            }
            if (cache != null && cache.isSuitable(db, systemId, thisInstanceId, serviceUri, scpName, maxCachedValAgeMillis)) {
                return cache.list;
            }
            PreparedStatement stmt = null;
            ResultSet rset = null;

            try {
                if (getArte() != null) {
                    getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
                try {
                    stmt = ((RadixConnection) db).prepareStatement(scpName != null ? (systemId == 1 ? readScpWith1Stmt : readScpWithout1Stmt) : (systemId == 1 ? readNullScpWith1Stmt : readNullScpWithout1Stmt));
                } finally {
                    if (getArte() != null) {
                        getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                }
                if (getArte() != null) {
                    getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    stmt.setLong(1, systemId.longValue());
                    stmt.setString(2, serviceUri);
                    if (thisInstanceId != null) {
                        stmt.setLong(3, thisInstanceId);
                    } else {
                        stmt.setNull(3, Types.INTEGER);
                    }
                    if (scpName != null) {
                        stmt.setString(4, scpName);
                    }
                    rset = stmt.executeQuery();
                } finally {
                    if (getArte() != null) {
                        getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    }
                }
                final List<SapClientOptions> tmp_saps = new ArrayList<>();
                while (rset.next()) {
                    final SapClientOptions sap = new SapClientOptions();
                    if (getTargetAadcMemberId() != null && getTargetAadcMemberId() != rset.getInt("aadcMemberId")) {
                        continue;
                    }

                    sap.setId(rset.getLong("id"));

                    if (systemId == 1 && !isSapActiveBySelfCheck(sap.getId(), rset.getLong("selfCheckTimeMillis"), rset.getTimestamp("selfCheckTime"), rset.getLong("dbCurMillis"), aadcManager)) {
                        continue;
                    }

                    sap.setName(rset.getString("name"));
                    if (EChannelType.INTERNAL_PIPE.getValue().equals(rset.getString("channelType"))) {
                        sap.setAddress(new SapAddress(new PipeAddress(rset.getString("address"))));
                    } else {
                        sap.setAddress(new SapAddress(ValueFormatter.parseCompositeInetSocketAddress(rset.getString("address"))));
                    }
                    sap.setSecurityProtocol(EPortSecurityProtocol.getForValue(rset.getLong("securityProtocol")));
                    final String clientKeyAliases = rset.getString("clientKeyAliases");
                    sap.setClientKeyAliases(clientKeyAliases != null ? Collections.unmodifiableList(ArrStr.fromValAsStr(clientKeyAliases)) : null);
                    final String serverCertAliases = rset.getString("serverCertAliases");
                    sap.setServerCertAliases(serverCertAliases != null ? Collections.unmodifiableList(ArrStr.fromValAsStr(serverCertAliases)) : null);
                    final String cipherSuites = rset.getString("cipherSuites");
                    if (cipherSuites == null) {
                        sap.setCipherSuites(SocketServerChannel.SUITE_ANY_STRONG);
                    } else {
                        final List<String> suites = Collections.unmodifiableList(ArrStr.fromValAsStr(cipherSuites));
                        if (suites.isEmpty()) {
                            sap.setCipherSuites(SocketServerChannel.SUITE_ANY);
                        } else {
                            sap.setCipherSuites(suites);
                        }
                    }
                    if (scpName == null) {
                        sap.setPriority(50);
                        sap.setBlockingPeriodMillis(600000);
                        sap.setConnectTimeoutMillis(10000);
                    } else {
                        sap.setPriority(rset.getInt("sapPriority"));
                        sap.setBlockingPeriodMillis(rset.getLong("blockingPeriod") * 1000);
                        sap.setConnectTimeoutMillis(rset.getInt("connectTimeout") * 1000);
                    }

                    if (rset.getBoolean("useWsSecurity")) {
                        if (rset.getInt("wsdlPresent") == 0) {
                            sap.setSoapServiceOptions(new SoapServiceOptions(
                                    new WsdlSource(rset.getString("dsmWsdlUri")),
                                    ReadSapOptionsQuery.qnameFromString(rset.getString("serviceQName")),
                                    ReadSapOptionsQuery.qnameFromString(rset.getString("portQName")),
                                    rset.getTimestamp("dsmWsdlLastUpdateTime") == null ? 0 : rset.getTimestamp("dsmWsdlLastUpdateTime").getTime()));
                        } else {
                            sap.setSoapServiceOptions(new SoapServiceOptions(
                                    new WsdlSource(sap.getId()),
                                    ReadSapOptionsQuery.qnameFromString(rset.getString("serviceQName")),
                                    ReadSapOptionsQuery.qnameFromString(rset.getString("portQName")),
                                    rset.getTimestamp("serviceLastUpdateTime") == null ? 0 : rset.getTimestamp("serviceLastUpdateTime").getTime()));
                        }
                    }
                    sap.setAdditionalAttrs(parseAdditionalAttrs(rset.getString("clientAttrs")));
                    int aadcMemberId = rset.getInt("aadcMemberId");
                    if (!rset.wasNull()) {
                        sap.setAadcMemberId(aadcMemberId);
                    }
                    tmp_saps.add(sap);
                }
                cache = new Cache(db, systemId, thisInstanceId, serviceUri, scpName, tmp_saps);
                return tmp_saps;
            } catch (SQLException e) {
                onSqlErrorAction = onSqlException(e, onSqlErrorAction);
                if (onSqlErrorAction == EOnSqlExceptionAction.RETRY) {
                    continue;
                }
                //ignore database and time to live for cache
                final boolean canUseCache = cache != null && cache.isSuitable(cache.db, systemId, thisInstanceId, serviceUri, scpName, Long.MAX_VALUE);
                if (onSqlErrorAction == EOnSqlExceptionAction.RETURN_CACHE_OR_THROW) {
                    if (canUseCache) {
                        return cache.list;
                    }
                    throw new DatabaseError(e);
                }
                if (onSqlErrorAction == EOnSqlExceptionAction.RETURN_CACHE_OR_NOTHING) {
                    if (canUseCache) {
                        return cache.list;
                    }
                    return Collections.emptyList();
                }
                throw new DatabaseError(e);
            } finally {
                if (rset != null) {
                    try {
                        rset.close();
                    } catch (SQLException e) {
                        //do nothing
                    }
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        //do nothing
                    }
                }
            }
        }
    }

    public static boolean isSapActiveBySelfCheck(final long sapId, final long dbCheckTimeMillis, final Timestamp dbCheckTime, final long dbCurMillis, final AadcManager aadcManager) {
        if (dbCurMillis - Math.max(dbCheckTimeMillis, dbCheckTime == null ? 0l : dbCheckTime.getTime()) > SELFCHECK_TIMEOUT_SECONDS * 1000) {
            final Timestamp dgCheckTime = aadcManager == null ? null : aadcManager.getSapSelfCheckTime(sapId);
            if (dgCheckTime == null || System.currentTimeMillis() - dgCheckTime.getTime() > SELFCHECK_TIMEOUT_SECONDS * 1000) {
                return false;
            }
        }
        return true;
    }

    @Override
    public URL getWsdlUrl(SapClientOptions SapClientOptions) throws IOException {
        return ServerSoapUtils.getWsdlUrl(SapClientOptions.getSoapServiceOptions(), getDbConnection(), getTracer());
    }

    private Map<String, String> parseAdditionalAttrs(final String attrsAsString) {
        if (attrsAsString == null) {
            return null;
        }
        return JmsUtils.parseProps(attrsAsString);
    }

    private static final class Cache {

        final Long systemId;
        final Long thisInstanceId;
        final String serviceUri;
        final String scpName;
        final List<SapClientOptions> list;
        final long timestamp;
        final Connection db;

        Cache(final Connection db, final Long systemId, final Long thisInstanceId, final String serviceUri, final String scpName, final List<SapClientOptions> list) {
            this.systemId = systemId;
            this.serviceUri = serviceUri;
            this.thisInstanceId = thisInstanceId;
            this.scpName = scpName;
            this.list = list;
            this.timestamp = System.currentTimeMillis();
            this.db = db;
        }

        final boolean isSuitable(final Connection db, final Long systemId, final Long thisInstanceId, final String serviceUri, final String scpName, final long maxCachedValAgeMillis) {
            if (maxCachedValAgeMillis == 0 || db != this.db) {
                return false;
            }
            return System.currentTimeMillis() - timestamp < maxCachedValAgeMillis
                    && this.systemId.equals(systemId)
                    && Utils.equals(this.serviceUri, serviceUri)
                    && Utils.equals(this.scpName, scpName)
                    && Utils.equals(this.thisInstanceId, thisInstanceId);
        }
    }

}
