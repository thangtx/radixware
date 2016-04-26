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
    INST_MEMORY_HEAP("Inst.Memory.Heap"),
    UNIT_STOP("Unit.Stop"),
    UNIT_HANG("Unit.Hang"),
    UNIT_ARTE_SESSCNT("Unit.Arte.SessionCnt"),
    @Deprecated
    UNIT_ARTE_INSTCNT("Unit.Arte.InstCnt"),
    UNIT_JOB_EXECCNT("Unit.Job.ExecCnt"),
    UNIT_JOB_WAITCNT("Unit.Job.WaitCnt"),
    UNIT_ISO8583_CONNECT("Unit.Iso8583.Connect"),
    UNIT_ISO8583_LOGON("Unit.Iso8583.Logon"),
    NET_SERVER_CONNECT("Net.ServerConnect"),
    NET_CLIENT_CONNECT("Net.ClientConnect"),
    NET_CLIENT_CONNECT_TIME_PERCENT("Net.ClientConnectTimePercent");
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
