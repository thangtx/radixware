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

package org.radixware.kernel.server.monitoring;

import org.radixware.kernel.common.enums.EMetricKind;


public enum EMetric {

    @Deprecated
    UNIT_ARTE_INST_CNT(EMetricKind.UNIT_ARTE_INSTCNT.getValue(), EMetricType.EVENT),
    UNIT_ARTE_SESSION_CNT(EMetricKind.UNIT_ARTE_SESSCNT.getValue(), EMetricType.STATISTIC),
    UNIT_JOB_EXEC_CNT(EMetricKind.UNIT_JOB_EXECCNT.getValue(), EMetricType.STATISTIC),
    UNIT_JOB_WAIT_CNT(EMetricKind.UNIT_JOB_WAITCNT.getValue(), EMetricType.STATISTIC),
    UNIT_HANG(EMetricKind.UNIT_HANG.getValue(), EMetricType.EVENT),
    INSTANCE_STOPPED(EMetricKind.INST_STOP.getValue(), EMetricType.EVENT),
    INSTANCE_ARTE_SESSION_CNT(EMetricKind.INST_ARTE_SESSCNT.getValue(), EMetricType.STATISTIC),
    INSTANCE_ARTE_INST_CNT(EMetricKind.INST_ARTE_INSTCNT.getValue(), EMetricType.EVENT),
    INSTANCE_SERVICE_ARTE_SESS_CNT(EMetricKind.INST_SERVICE_SESSCNT.getValue(), EMetricType.STATISTIC),
    INSTANCE_CPU_USAGE(EMetricKind.INST_CPU_USAGE.getValue(), EMetricType.STATISTIC),
    INSTANCE_MEMORY_CODE_CACHE(EMetricKind.INST_MEMORY_CODE_CACHE.getValue(), EMetricType.STATISTIC),
    INSTANCE_MEMORY_PERM_GEN(EMetricKind.INST_MEMORY_PERM_GEN.getValue(), EMetricType.STATISTIC),
    INSTANCE_MEMORY_META_SPACE(EMetricKind.INST_MEMORY_META_SPACE.getValue(), EMetricType.STATISTIC),
    INSTANCE_MEMORY_HEAP(EMetricKind.INST_MEMORY_HEAP.getValue(), EMetricType.STATISTIC),
    INSTANCE_AADC_LAG(EMetricKind.INST_AADC_LAG.getValue(), EMetricType.STATISTIC),
    PROFILING_DURATION(EMetricKind.PROFILING_DURATION.getValue(), EMetricType.STATISTIC),
    PROFILING_CNT(EMetricKind.PROFILING_CNT.getValue(), EMetricType.STATISTIC),
    PROFILING_FREQ(EMetricKind.PROFILING_FREQ.getValue(), EMetricType.STATISTIC),
    PROFILING_PERCENT_CPU(EMetricKind.PROFILING_PERCENT_CPU.getValue(), EMetricType.STATISTIC),
    PROFILING_PERCENT_DB(EMetricKind.PROFILING_PERCENT_DB.getValue(), EMetricType.STATISTIC),
    PROFILING_PERCENT_EXT(EMetricKind.PROFILING_PERCENT_EXT.getValue(), EMetricType.STATISTIC),
    NET_SERVER_CONNECT(EMetricKind.NET_SERVER_CONNECT.getValue(), EMetricType.STATISTIC),
    NET_CLIENT_CONNECT_TIME_PERCENT(EMetricKind.NET_CLIENT_CONNECT_TIME_PERCENT.getValue(), EMetricType.STATISTIC),
    NET_CLIENT_CONNECT(EMetricKind.NET_CLIENT_CONNECT.getValue(), EMetricType.EVENT),
    NET_SYNC_BUSY_CONN_CNT(EMetricKind.NET_SYNC_BUSY_CONNECTIONS.getValue(), EMetricType.STATISTIC),
    NET_AAS_QUEUE_SIZE(EMetricKind.NET_AAS_QUEUE_SIZE.getValue(), EMetricType.STATISTIC),
    NET_AAS_QUEUE_WAIT_DURATION(EMetricKind.NET_AAS_QUEUE_WAIT_DURATION.getValue(), EMetricType.STATISTIC),
    NET_AAS_PROC_DURATION_PURE(EMetricKind.NET_AAS_PROC_DURATION_PURE.getValue(), EMetricType.STATISTIC),
    NET_AAS_PROC_DURATION_TOTAL(EMetricKind.NET_AAS_PROC_DURATION_TOTAL.getValue(), EMetricType.STATISTIC),
    NET_AAS_INVOCATIONS_PER_SEC(EMetricKind.NET_AAS_INVOCATIONS_PER_SEC.getValue(), EMetricType.STATISTIC),
    UNIT_MQ_MESSAGE_COUNT(EMetricKind.UNIT_MQ_PROC_MESSAGES_PER_SEC.getValue(), EMetricType.STATISTIC),
    UNIT_MQ_PROC_DURATION(EMetricKind.UNIT_MQ_PROC_DURATION.getValue(), EMetricType.STATISTIC),
    UNIT_MQ_AAS_SEANCES_COUNT(EMetricKind.UNIT_MQ_AAS_SEANCES_COUNT.getValue(), EMetricType.STATISTIC),
    UNIT_PC_OUTGOING_QUEUE_SIZE(EMetricKind.UNIT_PC_OUTGOING_QUEUE_SIZE.getValue(), EMetricType.EVENT),
    UNIT_PC_MESSAGES_PER_SEC_SENT(EMetricKind.UNIT_PC_MESSAGES_PER_SEC_SENT.getValue(), EMetricType.STATISTIC),
    UNIT_PC_MESSAGE_SEND_DURATION(EMetricKind.UNIT_PC_MESSAGE_SEND_DURATION.getValue(), EMetricType.STATISTIC),
    UNIT_PC_MESSAGES_SENT_CNT(EMetricKind.UNIT_PC_MESSAGES_SENT_CNT.getValue(), EMetricType.STATISTIC),
    UNIT_PC_MESSAGES_NOT_SENT_CNT(EMetricKind.UNIT_PC_MESSAGES_NOT_SENT_CNT.getValue(), EMetricType.STATISTIC),
    UNIT_PC_MESSAGES_RECEIVED_CNT(EMetricKind.UNIT_PC_MESSAGES_RECEIVED_CNT.getValue(), EMetricType.STATISTIC),
    UNIT_PC_MESSAGE_RECEIVED_PROC_DURATION(EMetricKind.UNIT_PC_MESSAGE_RECEIVED_PROC_DURATION.getValue(), EMetricType.STATISTIC),
    ;
    
    private final String kindName;
    private final EMetricType metricType;

    private EMetric(final String kind, final EMetricType metricType) {
        this.kindName = kind;
        this.metricType = metricType;
    }

    public String getKindName() {
        return kindName;
    }

    public EMetricType getMetricType() {
        return metricType;
    }

    public static EMetric findForKind(final String kind) {
        for (EMetric metric : EMetric.values()) {
            if (metric.getKindName().equals(kind)) {
                return metric;
            }
        }
        return null;
    }
}
