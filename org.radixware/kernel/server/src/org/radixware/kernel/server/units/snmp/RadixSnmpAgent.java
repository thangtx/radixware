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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.schemas.snmpagent.NotifyRq;
import org.snmp4j.PDU;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.mo.DefaultMOMutableTableModel;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.DefaultMOTableModel;
import org.snmp4j.agent.mo.DefaultMOTableRow;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.MOTableSubIndex;
import org.snmp4j.agent.mo.snmp.DateAndTime;
import org.snmp4j.agent.mo.snmp.DisplayString;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.TransportDomains;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.TransportMappings;


public class RadixSnmpAgent extends BaseAgent {

    private static final OID CPLUS_OID = new OID("1.3.6.1.4.1.43238");
    private static final OID RDX_OID = new OID(CPLUS_OID.toDottedString() + ".1");
    //
    private static final String SNMP_CONFIG_FOLDER_NAME = "Snmp";
    private static final String ID_PLACEHOLDER = "#";
    private static final String BOOT_COUNTER_FILE_NAME = "agent_" + ID_PLACEHOLDER + ".bootCounter";
    private static final String CONFIG_FILE_NAME = "agent_" + ID_PLACEHOLDER + ".conf";
    //
    private static final String DEFAULT_MANAGER_LABEL = "DefaultManager";
    private static final String DEFAULT_MANAGER_PARAMETERS_LABEL = "DefaultManagerParameters";
    private static final String TAG_ALL = "all";
    private static final String METRIC_VALUE_NOTIFICATION_LABEL = "MetricSeverityChange";
    //
    private static final String METRICS_SUBTREE_LOCAL_PART = "2";
    private static final String METRIC_TYPE_TABLE_LOCAL_PART = "1";
    private static final String METRIC_STATE_TABLE_LOCAL_PART = "2";
    private static final String KIND_COLUMN_LOCAL_PART = "2";
    private static final String TITLE_COLUMN_LOCAL_PART = "3";
    private static final String TABLE_INSTANCE_LOCAL_PART = "1";
    private static final String NOTIFICATION_SUBTREE_LOCAL_PART = "3";
    private static final String NOTIFICATION_TYPES_LOCAL_PART = "1.0";
    private static final String NOTIFICATION_FIELDS_LOCAL_PART = "2";
    private static final String METRIC_SEVERITY_CHANGE_NOTIFICATION_LOCAL_PART = "1";
    private static final String METRIC_TYPE_ID_LOCAL_PART = "1";
    private static final String METRIC_STATE_ID_LOCAL_PART = "2";
    private static final String OLD_SEVERITY_LOCAL_PART = "3";
    private static final String NEW_SEVERITY_LOCAL_PART = "4";
    private static final String CHANGE_TIME_LOCAL_PART = "5";
    private static final String VALUE_AS_STRING_LOCAL_PART = "6";
    private static final String REASON_LOCAL_PART = "7";
    //
    private static final String DEFAULT_CONTEXT_NAME = "public";
    //
    private final SnmpAgentUnit unit;
    private final OID snmpSystemOID;
    private final OID metricSubtreeOID;
    private final OID notificationsSubtreeOID;
    private final OID metricTypeTableOID;
    private final OID metricTypeTableEntryOID;
    private final OID metricStateTableOID;
    private final OID metricStateTableEntryOID;
    private final OID severityChangeNtfOID;
    private final OID notificationFieldsOID;
    private final OID metricTypeIdOID;
    private final OID metricStateIdOID;
    private final OID oldSeverityOID;
    private final OID newSeverityOID;
    private final OID changeTimeOID;
    private final OID valueAsStringOID;
    private final OID reasonOID;
    private final OID kindColumnOID;
    private final OID titleColumnOID;
    private final DefaultMOTable<DefaultMOTableRow, MOColumn, DefaultMOTableModel<DefaultMOTableRow>> metricTypeTable;
    private final DefaultMOTable<DefaultMOTableRow, MOColumn, DefaultMOTableModel<DefaultMOTableRow>> metricStateTable;

    public RadixSnmpAgent(final SnmpAgentUnit unit) {
        super(new File(SystemTools.getRadixApplicationDataPath(ERadixApplication.SERVER), SNMP_CONFIG_FOLDER_NAME + "/" + BOOT_COUNTER_FILE_NAME.replace(ID_PLACEHOLDER, getIdForConfigFiles(unit))),
                new File(SystemTools.getRadixApplicationDataPath(ERadixApplication.SERVER), SNMP_CONFIG_FOLDER_NAME + "/" + CONFIG_FILE_NAME.replace(ID_PLACEHOLDER, getIdForConfigFiles(unit))),
                new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        new File(SystemTools.getRadixApplicationDataPath(ERadixApplication.SERVER), SNMP_CONFIG_FOLDER_NAME).mkdirs();
        this.unit = unit;
        snmpSystemOID = new OID(RDX_OID.toDottedString() + "." + unit.getOptions().systemRid);
        metricSubtreeOID = new OID(snmpSystemOID + "." + METRICS_SUBTREE_LOCAL_PART);
        metricTypeTableOID = new OID(metricSubtreeOID.toDottedString() + "." + METRIC_TYPE_TABLE_LOCAL_PART);
        metricTypeTableEntryOID = new OID(metricTypeTableOID.toDottedString() + "." + TABLE_INSTANCE_LOCAL_PART);
        int idx = 0;
        metricTypeTable = new DefaultMOTable<>(
                metricTypeTableEntryOID,
                new MOTableIndex(new MOTableSubIndex[]{new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER)}),
                new MOColumn[]{
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.getInstance(0)),//typeId - index
            new DisplayString(++idx, MOAccessImpl.ACCESS_READ_ONLY, new OctetString()),//kind
            new MOColumn(++idx, VUTF8String.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//title
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//period
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//lowErrorVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//lowWarnVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//highWarnVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//highErrorVal
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//warnDelay
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//errorDelay
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//escalationDelay
        });
        kindColumnOID = new OID(metricTypeTableEntryOID.toDottedString() + "." + KIND_COLUMN_LOCAL_PART);
        titleColumnOID = new OID(metricTypeTableEntryOID.toDottedString() + "." + TITLE_COLUMN_LOCAL_PART);

        metricStateTableOID = new OID(metricSubtreeOID.toDottedString() + "." + METRIC_STATE_TABLE_LOCAL_PART);
        metricStateTableEntryOID = new OID(metricStateTableOID.toDottedString() + "." + TABLE_INSTANCE_LOCAL_PART);
        idx = 0;
        metricStateTable = new DefaultMOTable<>(
                metricStateTableEntryOID,
                new MOTableIndex(new MOTableSubIndex[]{new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER), new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER)}),
                new MOColumn[]{
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.getInstance(0)),//stateId - index
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//instanceId
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//unitId
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//systemId
            new DisplayString(++idx, MOAccessImpl.ACCESS_READ_ONLY, new OctetString()),//serviceUri
            new MOColumn(++idx, VPositive32.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//netChannelId
            new DisplayString(++idx, MOAccessImpl.ACCESS_READ_ONLY, new OctetString()),//timingSection
            new DateAndTime(++idx, MOAccessImpl.ACCESS_READ_ONLY, new OctetString()),//begTime
            new DateAndTime(++idx, MOAccessImpl.ACCESS_READ_ONLY, new OctetString()),//endTime
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//begVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//endVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//minVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//maxVal
            new MOColumn(++idx, VDecimal2.SYNTAX, MOAccessImpl.ACCESS_READ_ONLY),//avgVal
        });

        notificationsSubtreeOID = new OID(snmpSystemOID.toDottedString() + "." + NOTIFICATION_SUBTREE_LOCAL_PART);
        severityChangeNtfOID = new OID(notificationsSubtreeOID.toDottedString() + "." + NOTIFICATION_TYPES_LOCAL_PART + "." + METRIC_SEVERITY_CHANGE_NOTIFICATION_LOCAL_PART);
        notificationFieldsOID = new OID(notificationsSubtreeOID.toDottedString() + "." + NOTIFICATION_FIELDS_LOCAL_PART);
        metricTypeIdOID = new OID(notificationFieldsOID.toDottedString() + "." + METRIC_TYPE_ID_LOCAL_PART);
        metricStateIdOID = new OID(notificationFieldsOID.toDottedString() + "." + METRIC_STATE_ID_LOCAL_PART);
        oldSeverityOID = new OID(notificationFieldsOID.toDottedString() + "." + OLD_SEVERITY_LOCAL_PART);
        newSeverityOID = new OID(notificationFieldsOID.toDottedString() + "." + NEW_SEVERITY_LOCAL_PART);
        changeTimeOID = new OID(notificationFieldsOID.toDottedString() + "." + CHANGE_TIME_LOCAL_PART);
        valueAsStringOID = new OID(notificationFieldsOID.toDottedString() + "." + VALUE_AS_STRING_LOCAL_PART);
        reasonOID = new OID(notificationFieldsOID.toDottedString() + "." + REASON_LOCAL_PART);
    }

    private static String getIdForConfigFiles(final SnmpAgentUnit unit) {
        return unit.getOptions().systemRid + "_" + unit.getId();
    }

    /**
     * Adds community to security name mappings needed for SNMPv1 and SNMPv2c.
     */
    @Override
    protected void addCommunities(SnmpCommunityMIB communityMIB) {
        Variable[] com2sec = new Variable[]{new OctetString(unit.getOptions().communityString),
            new OctetString(unit.getOptions().communityString), // security name
            getAgent().getContextEngineID(), // local engine ID
            new OctetString(DEFAULT_CONTEXT_NAME), // default context name
            new OctetString(), // transport tag
            new Integer32(StorageType.nonVolatile), // storage type
            new Integer32(RowStatus.active) // row status
        };
        MOTableRow row = communityMIB.getSnmpCommunityEntry().createRow(
                new OctetString("public2public").toSubIndex(true), com2sec);
        communityMIB.getSnmpCommunityEntry().addRow(row);
    }

    /**
     * Adds initial notification targets and filters.
     */
    @Override
    protected void addNotificationTargets(SnmpTargetMIB targetMib,
            SnmpNotificationMIB notificationMib) {
        if (!unit.getOptions().useSap) {
            return;
        }
        targetMib.addDefaultTDomains();
        targetMib.addTargetAddress(
                new OctetString(DEFAULT_MANAGER_LABEL),
                TransportDomains.snmpUDPDomain,
                getAddressAsOctetString(unit.getOptions().managerAddress),
                3, 3,
                new OctetString(TAG_ALL),
                new OctetString(DEFAULT_MANAGER_PARAMETERS_LABEL),
                StorageType.readOnly);
        targetMib.addTargetParams(
                new OctetString(DEFAULT_MANAGER_PARAMETERS_LABEL),
                MessageProcessingModel.MPv2c,
                SecurityModel.SECURITY_MODEL_SNMPv2c,
                new OctetString(unit.getOptions().communityString),
                SecurityLevel.NOAUTH_NOPRIV,
                StorageType.readOnly);
        notificationMib.addNotifyEntry(
                new OctetString(METRIC_VALUE_NOTIFICATION_LABEL),
                new OctetString(TAG_ALL),
                PDU.NOTIFICATION,
                StorageType.readOnly);
    }

    private OctetString getAddressAsOctetString(final String address) {
        try {
            final String addressWithoutProtocol = address.substring(address.indexOf(":") + 1);
            final String[] hostAndPort = addressWithoutProtocol.split("/");
            final String[] addressComponents = hostAndPort[0].split("\\.");
            final byte[] addrBytes = new byte[6];
            for (int i = 0; i < 4; i++) {
                addrBytes[i] = (byte) Integer.parseInt(addressComponents[i]);
            }
            int port = Integer.parseInt(hostAndPort[1]);
            addrBytes[4] = (byte) (port >> 8);
            addrBytes[5] = (byte) (port & 0xFF);
            return new OctetString(addrBytes);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to parse manager address '" + address + "'", ex);
        }
    }

    /**
     * Adds all the necessary initial users to the USM.
     */
    @Override
    protected void addUsmUser(USM arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * Adds initial VACM configuration.
     */
    @Override
    protected void addViews(VacmMIB vacm) {
        vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(
                unit.getOptions().communityString), new OctetString("v1v2group"),
                StorageType.nonVolatile);

        vacm.addAccess(new OctetString("v1v2group"), new OctetString(DEFAULT_CONTEXT_NAME),
                SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV,
                MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
                new OctetString("fullWriteView"), new OctetString("fullNotifyView"),
                StorageType.nonVolatile);

        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);
        vacm.addViewTreeFamily(new OctetString("fullNotifyView"), new OID("1.3"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);

    }

    @Override
    protected void unregisterManagedObjects() {
    }

    @Override
    protected void registerManagedObjects() {
        try {
            server.register(metricTypeTable, new OctetString(DEFAULT_CONTEXT_NAME));
            server.register(metricStateTable, new OctetString(DEFAULT_CONTEXT_NAME));
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];
        Address addr = GenericAddress.parse(unit.getOptions().agentAddress);
        if (addr == null) {
            throw new IllegalArgumentException("Unable to parse agent address '" + unit.getOptions().agentAddress + "'");
        }
        TransportMapping tm = TransportMappings.getInstance()
                .createTransportMapping(addr);
        transportMappings[0] = tm;
    }

    /**
     * Start method invokes some initialization methods needed to start the
     * agent
     *
     * @throws IOException
     */
    public void start() throws IOException {
        init();

//        unregisterSnmpMIBs();
        getServer().addContext(new OctetString(DEFAULT_CONTEXT_NAME));
        finishInit();
        try {
            refresh();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
        run();
        sendColdStartNotification();

    }

    public void sendNotifications(final NotifyRq notifyRq) {
        for (NotifyRq.MetricType xMetricType : notifyRq.getMetricTypeList()) {
            for (NotifyRq.MetricType.MetricState xMetricState : xMetricType.getMetricStateList()) {
                getNotificationOriginator().notify(
                        new OctetString(DEFAULT_CONTEXT_NAME),
                        severityChangeNtfOID,
                        new VariableBinding[]{
                    new VariableBinding(metricTypeIdOID, new VPositive32(xMetricType.getId())),
                    new VariableBinding(metricStateIdOID, new VPositive32(xMetricState.getId())),
                    new VariableBinding(kindColumnOID, new OctetString(xMetricType.getKind())),
                    new VariableBinding(titleColumnOID, new VUTF8String(xMetricType.getTitle())),
                    new VariableBinding(oldSeverityOID, new VSeverity(EEventSeverity.getForValue(xMetricState.getOldSeverity()))),
                    new VariableBinding(newSeverityOID, new VSeverity(EEventSeverity.getForValue(xMetricState.getNewSeverity()))),
                    new VariableBinding(changeTimeOID, new VTimestamp(xMetricState.getChangeTime().getTime())),
                    new VariableBinding(valueAsStringOID, new OctetString(xMetricState.getValueAsString())),
                    new VariableBinding(reasonOID, new VReason(xMetricState.getIsEscalation() == Boolean.TRUE))});
            }
        }
    }

    public void refresh() throws SQLException {
        final List<MetricDataRecord> data = unit.getDbQueries().readMetricData();
        DefaultMOTableModel<DefaultMOTableRow> typeModel = new DefaultMOMutableTableModel<>();
        DefaultMOTableModel<DefaultMOTableRow> stateModel = new DefaultMOMutableTableModel<>();

        int currentTypeId = -1;
        for (MetricDataRecord record : data) {
            if (record.typeId.getValue() != currentTypeId) {
                currentTypeId = record.typeId.getValue();
                typeModel.addRow(
                        new DefaultMOTableRow(
                        new OID(String.valueOf(currentTypeId)),
                        new Variable[]{
                    record.typeId,
                    record.kindName,
                    record.title,
                    record.periodSec,
                    record.lowErrorVal,
                    record.lowWarnVal,
                    record.highWarnVal,
                    record.highErrorVal,
                    record.warnDelaySec,
                    record.errorDelaySec,
                    record.escalationDelaySec
                }));
            }
            stateModel.addRow(
                    new DefaultMOTableRow(
                    new OID(currentTypeId + "." + record.stateId.getValue()),
                    new Variable[]{
                record.stateId,
                record.instanceId,
                record.unitId,
                record.systemId,
                record.serviceUri,
                record.netChannelId,
                record.timingSection,
                record.begTime,
                record.endTime,
                record.begVal,
                record.endVal,
                record.minVal,
                record.maxVal,
                record.avgVal
            }));
        }
        metricTypeTable.setModel(typeModel);
        metricStateTable.setModel(stateModel);
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } finally {
            for (TransportMapping tm : transportMappings) {
                try {
                    if (tm != null) {
                        tm.close();
                    }
                } catch (IOException ex) {
                    unit.onExceptionOnStop(ex);
                }
            }
        }
    }
}
