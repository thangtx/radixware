/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
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


public enum EMetricKind implements IKernelStrEnum {

    PROFILING_CNT("Profiling.Cnt"),
    USER("User"),
    PROFILING_FREQ("Profiling.Freq"),
    PROFILING_DURATION("Profiling.Duration"),
    PROFILING_PERCENT_DB("Profiling.Percent.Db"),
    PROFILING_PERCENT_CPU("Profiling.Percent.Cpu"),
    PROFILING_PERCENT_EXT("Profiling.Percent.Ext"),
    INST_STOP("Inst.Stop"),
    INST_ARTE_SESSCNT("Inst.Arte.SessCnt"),
    INST_ARTE_INSTCNT("Inst.Arte.InstCnt"),
    INST_SERVICE_SESSCNT("Inst.Service.SessCnt"),
    INST_CPU_USAGE("Inst.CpuUsage"),
    INST_MEMORY_CODE_CACHE("Inst.Memory.CodeCache"),
    INST_MEMORY_PERM_GEN("Inst.Memory.PermGen"),
    INST_MEMORY_META_SPACE("Inst.Memory.MetaSpace"),
    INST_MEMORY_HEAP("Inst.Memory.Heap"),
    INST_AADC_LAG("Inst.Aadc.Lag"),
    UNIT_STOP("Unit.Stop"),
    UNIT_HANG("Unit.Hang"),
    UNIT_ARTE_SESSCNT("Unit.Arte.SessionCnt"),
    @Deprecated
    UNIT_ARTE_INSTCNT("Unit.Arte.InstCnt"),
    UNIT_JOB_EXECCNT("Unit.Job.ExecCnt"),
    UNIT_JOB_WAITCNT("Unit.Job.WaitCnt"),
    UNIT_ISO8583_CONNECT("Unit.Iso8583.Connect"),
    UNIT_ISO8583_LOGON("Unit.Iso8583.Logon"),
    UNIT_MQ_PROC_MESSAGES_PER_SEC("Unit.Mq.MessagesPerSec"),
    UNIT_MQ_PROC_DURATION("Unit.Mq.ProcDuration"),
    UNIT_MQ_AAS_SEANCES_COUNT("Unit.Mq.UsedArteCnt"),
    UNIT_PC_OUTGOING_QUEUE_SIZE("Unit.PersoComm.OutgoingQueueSize"),
    UNIT_PC_MESSAGES_PER_SEC_SENT("Unit.PersoComm.MessagesPerSecSent"),
    UNIT_PC_MESSAGE_SEND_DURATION("Unit.PersoComm.MessageSendDuration"),
    UNIT_PC_MESSAGES_SENT_CNT("Unit.PersoComm.MessagesSentCnt"),
    UNIT_PC_MESSAGES_NOT_SENT_CNT("Unit.PersoComm.MessagesNotSentCnt"),
    UNIT_PC_MESSAGES_RECEIVED_CNT("Unit.PersoComm.MessagesReceivedCnt"),
    UNIT_PC_MESSAGE_RECEIVED_PROC_DURATION("Unit.PersoComm.MessageReceivedProcDuration"),
    NET_SERVER_CONNECT("Net.ServerConnect"),
    NET_CLIENT_CONNECT("Net.ClientConnect"),
    NET_CLIENT_CONNECT_TIME_PERCENT("Net.ClientConnectTimePercent"),
    NET_SYNC_BUSY_CONNECTIONS("Net.Sync.BusyConnCnt"),
    NET_AAS_QUEUE_SIZE("Net.Aas.QueueSize"),
    NET_AAS_QUEUE_WAIT_DURATION("Net.Aas.QueueWaitDuration"),
    NET_AAS_PROC_DURATION_PURE("Net.Aas.ProcDurationPure"),
    NET_AAS_PROC_DURATION_TOTAL("Net.Aas.ProcDurationTotal"),
    NET_AAS_INVOCATIONS_PER_SEC("Net.Aas.InvocationsPerSec"),
    ;
    
    private final String value;

    private EMetricKind(final String value) {
        this.value = value;
    }

    public static EMetricKind getForValue(final String value) {
        for (EMetricKind dbOption : EMetricKind.values()) {
            if (dbOption.getValue().equals(value)) {
                return dbOption;
            }
        }
        throw new NoConstItemWithSuchValueError("ETimingSection has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
