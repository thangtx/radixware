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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.UnitView;


public class SnmpAgentUnit extends AsyncEventHandlerUnit {

    public static final String RADIXWARE_MIB_FILE_NAME = "RADIXWARE-MIB";
    public static final String SNMP_SYSTEM_ID_PLACEHOLDER = "${snmpSystemId}";
    private static final long SAP_SELFCHECK_PERIOD_MILLIS = 60000;
    private static final long REFRESH_PERIOD_MILLIS = 10000;
    private final int TIC_MILLIS = 1000;
    private volatile Options options;
    private RadixSnmpAgent snmpAgent;
    public int ticCount;
    private final SnmpDbQueries dbQueries;
    private SnmpAgentSap sap;

    public SnmpAgentUnit(final Instance instance, final long id, final String title) {
        super(instance, id, title);
        dbQueries = new SnmpDbQueries(this);
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }
        options = dbQueries.readOptions();
        logOptionsChanged(options.toString());
        snmpAgent = new RadixSnmpAgent(this);
        ticCount = 0;
        snmpAgent.start();

        if (options.useSap) {
            sap = new SnmpAgentSap(this);
            sap.start(getDbConnection());
        }

        waitNextTic();
        return true;
    }

    @Override
    protected void stopImpl() {
        if (sap != null) {
            try {
                sap.stop();
            } catch (Exception ex) {
                logErrorOnStop(ex);
            } finally {
                sap = null;
            }
        }
        if (snmpAgent != null) {
            try {
                snmpAgent.stop();
            } catch (Exception ex) {
                logErrorOnStop(ex);
            } finally {
                snmpAgent = null;
            }
        }
        try {
            dbQueries.closeAll();
        } catch (Exception ex) {
            logErrorOnStop(ex);
        }
        super.stopImpl();
    }

    @Override
    protected void setDbConnection(RadixConnection dbConnection) {
        super.setDbConnection(dbConnection);
        dbQueries.closeAll();
        if (sap != null) {
            sap.setDbConnection(dbConnection);
        }
    }

    public SnmpDbQueries getDbQueries() {
        return dbQueries;
    }

    private void waitNextTic() {
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
    }

    public RadixSnmpAgent getAgent() {
        return snmpAgent;
    }

    @Override
    public String getUnitTypeTitle() {
        return SnmpMessages.SNMP_AGENT_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.SNMP_AGENT.getValue();
    }

    @Override
    protected UnitView newUnitView() {
        return new UnitView(this);
    }

    @Override
    public void onEvent(Event ev) {
        if (ev instanceof EventDispatcher.TimerEvent) {
            if (!isShuttingDown()) {
                waitNextTic();
            }
        } else {
            throw new IllegalStateException("Unsupported event: " + ev);
        }
    }
    private long lastSapDbSelfCheckMillis = 0;
    private long lastRefreshMillis = 0;

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        super.maintenanceImpl();
        final long curMillis = System.currentTimeMillis();
        if (sap != null && curMillis > lastSapDbSelfCheckMillis + SAP_SELFCHECK_PERIOD_MILLIS) {
            sap.dbSapSelfCheck();
            lastSapDbSelfCheckMillis = curMillis;
        }
        if (snmpAgent != null && curMillis > lastRefreshMillis + REFRESH_PERIOD_MILLIS) {
            try {
                snmpAgent.refresh();
                lastRefreshMillis = curMillis;
            } catch (SQLException ex) {
                getTrace().put(EEventSeverity.ERROR, "Error: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.SNMP_AGENT_UNIT);
            }
        }
    }

    void onExceptionOnStop(final Exception ex) {
        logErrorOnStop(ex);
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        Options newOptions = dbQueries.readOptions();
        if (!Objects.equals(newOptions, options)) {
            requestStopAndPostponedRestart("Options chanaged", System.currentTimeMillis());
        }
        if (sap != null) {
            sap.rereadOptions();
        }
    }

    public Options getOptions() {
        return options;
    }

    @Override
    public String getEventSource() {
        return EEventSource.SNMP_AGENT_UNIT.getValue();
    }

    public static class Options {

        public final String agentAddress;
        public final String communityString;
        public final long systemRid;
        public final boolean useSap;
        public final long sapId;
        public final Long managerId;
        public final String managerTitle;
        public final String managerAddress;

        public Options(String agentAddress, String communityString, long systemRid, boolean useSap, long sapId, Long managerId, String managerTitle, String managerAddress) {
            this.agentAddress = agentAddress;
            this.communityString = communityString;
            this.systemRid = systemRid;
            this.useSap = useSap;
            this.sapId = sapId;
            this.managerId = managerId;
            this.managerTitle = managerTitle;
            this.managerAddress = managerAddress;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + Objects.hashCode(this.agentAddress);
            hash = 73 * hash + Objects.hashCode(this.communityString);
            hash = 73 * hash + (int) (this.systemRid ^ (this.systemRid >>> 32));
            hash = 73 * hash + (this.useSap ? 1 : 0);
            hash = 73 * hash + (int) (this.sapId ^ (this.sapId >>> 32));
            hash = 73 * hash + Objects.hashCode(this.managerId);
            hash = 73 * hash + Objects.hashCode(this.managerTitle);
            hash = 73 * hash + Objects.hashCode(this.managerAddress);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Options other = (Options) obj;
            if (!Objects.equals(this.agentAddress, other.agentAddress)) {
                return false;
            }
            if (!Objects.equals(this.communityString, other.communityString)) {
                return false;
            }
            if (this.systemRid != other.systemRid) {
                return false;
            }
            if (this.useSap != other.useSap) {
                return false;
            }
            if (this.sapId != other.sapId) {
                return false;
            }
            if (!Objects.equals(this.managerId, other.managerId)) {
                return false;
            }
            if (!Objects.equals(this.managerTitle, other.managerTitle)) {
                return false;
            }
            if (!Objects.equals(this.managerAddress, other.managerAddress)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "\nAgent address: " + agentAddress + "\n"
                    + "System RID: " + systemRid + "\n"
                    + "Enable notification service: " + useSap + "\n"
                    + (useSap ? ("Sap ID: " + sapId + "\n"
                    + "Manager: #" + managerId + " '" + managerTitle + "'\n"
                    + "Manager address: " + managerAddress + "\n") : "");
        }
    }
}
