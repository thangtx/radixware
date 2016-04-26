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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EEventSource implements IKernelStrEnum {

    AAS("Arte.Aas"),
    AAS_CLIENT("Server.AasClient"),
    ALGO_EXECUTOR("Arte.AlgorithmExecutor"),
    APP("App"),
    APP_CLASS("App.Class"),
    APP_DB("App.Db"),
    SYSTEM_MONITORING("App.SystemMonitoring"),
    TODO("App.Todo"),
    ARTE("Arte"),
    ARTE_POOL("Arte.Pool"),
    ARTE_PROFILER("Arte.Profiler"),
    ARTE_REPORTS("Arte.Reports"),
    ARTE_COMMUNICATOR("Arte.Communicator"),
    ARTE_DB("Arte.Db"),
    ARTE_TRACE("Arte.Trace"),
    ARTE_UNIT("Server.Unit.Arte"),
    WORKFLOW("App.Workflow"),
    APP_AUDIT("App.Audit"),
    CLIENT("Client"),
    CLIENT_SESSION("Client.Session"),
    PERSOCOMM_UNIT("Server.Unit.PersoComm"),
    EAS("Arte.Eas"),
    DB_QUERY_BUILDER("Arte.Dbqb"),
    CLIENT_DEF_MANAGER("Client.Explorer.DefManager"),
    EXPLORER("Client.Explorer"),
    IAD("Client.Explorer.IAD"),
    DEF_MANAGER("Arte.DefManager"),
    ENTITY("Arte.Entity"),
    INSTANCE("Server.Instance"),
    INSTANCE_SERVICE("Server.Instance.Service"),
    JOB_EXECUTOR("Server.Unit.JobExecutor"),
    JOB_QUEUE("Arte.JobQueue"),
    JOB_SCHEDULER("Server.Unit.JobScheduler"),
    NET_PORT_HANDLER("Server.Unit.NetPortHandler"),
    NET_PORT_HANDLER_SERVICE("Server.Unit.NetPortHandler.Service"),
    UNIT("Server.Unit"),
    UNIT_PORT("Server.Unit.NetPortHandler.Port"),
    NET_HUB_HANDLER("Server.Unit.NetHubHandler"),
    JMS_HANDLER("Server.Unit.JmsHandler"),
    SERVER_CHANNEL_PORT("Server.ChannelPort"),
    SNMP_AGENT_UNIT("Server.Unit.SnmpAgent"),
    MQ_HANDLER_UNIT("Server.Unit.MqHandler");
    
    private final String value;

    private EEventSource(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return value;
    }

    public static EEventSource getForValue(final String value) {
        for (EEventSource val : EEventSource.values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("EEventSource has no item with value: " + String.valueOf(value),value);
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }

}
