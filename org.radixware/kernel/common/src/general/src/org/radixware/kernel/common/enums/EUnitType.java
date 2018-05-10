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
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;

public enum EUnitType implements IKernelIntEnum {

    ARTE(3001, "Arte"),
    DPC_SMPP(4001, "DpcSmpp"),
    DPC_SMTP(4002, "DpcSmtp"),
    DPC_SMSVIA_SMTP(4003, "DpcSmsViaSmtp"),
    DPC_FILE(4004, "DpcFile"),
    DPC_GSM_MODEM(4005, "DpcGsmModem"),
    DPC_SERVICE_BUS(4006, "DpcServiceBus"),
    DPC_TWITTER(4010, "DpcTwitter"),
    DPC_GCM(4011, "DpcGcm"),
    DPC_APNS(4012, "DpcApns"),
    DPC_WNS(4013, "DpcWns"),
    DPC_DELIVERY_ACK(4014, "DpcDeliveryAck"),
    JOB_EXECUTOR(2001, "JobExecutor"),
    JOB_SCHEDULER(1001, "JobScheduler"),
    NET_PORT_HANDLER(201, "NetPortHandler"),
    @Deprecated
    JMS_HANDLER(202, "JmsHandler"),
    NET_HUB(2010, "NetHub"),
    SNMP_AGENT(5001, "SnmpAgent"),
    MQ_HANDLER(250, "MqHandler");
    
    private final Long val;
    private final String title;

    private EUnitType(long val, String title) {
        this.val = Long.valueOf(val);
        this.title = title;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return title;
    }

    public static EUnitType getForValue(final Long val) {
        for (EUnitType t : EUnitType.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EUnitType has no item with value: " + String.valueOf(val),val);
    }

    public boolean isArray() {
        return (this.getValue() > 200 && this.getValue() < 300);
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
