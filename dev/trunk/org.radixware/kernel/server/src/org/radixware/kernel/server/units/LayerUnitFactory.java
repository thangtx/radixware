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
package org.radixware.kernel.server.units;

import java.math.BigDecimal;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.arte.ArteUnit;
import org.radixware.kernel.server.units.job.executor.JobExecutorUnit;
import org.radixware.kernel.server.units.job.scheduler.JobSchedulerUnit;
import org.radixware.kernel.server.units.netport.NetPortHandlerUnit;
import org.radixware.kernel.server.units.persocomm.FileUnit;
import org.radixware.kernel.server.units.persocomm.MailUnit;
import org.radixware.kernel.server.units.persocomm.SMPPUnit;
import org.radixware.kernel.server.units.persocomm.SMSMailUnit;
import org.radixware.kernel.server.units.persocomm.GSMModemUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.exceptions.UnsupportedUnitTypeException;
import org.radixware.kernel.server.units.jms.JmsHandlerUnit;
import org.radixware.kernel.server.units.mq.MqUnit;
import org.radixware.kernel.server.units.nethub.NetHubUnit;
import org.radixware.kernel.server.units.persocomm.*;
import org.radixware.kernel.server.units.snmp.SnmpAgentUnit;

public final class LayerUnitFactory implements ILayerUnitFactory {
    
    private final boolean USE_OLD_PERSO_UNITS = SystemPropUtils.getBooleanSystemProp("rdx.use.old.perso.modules", false);

    public LayerUnitFactory() {
    }

    @Override
    public final Unit newUnit(final Instance instance, final Long unitType, final Long id, final String title) throws UnsupportedUnitTypeException {
        if (unitType.longValue() == EUnitType.ARTE.getValue().longValue()) {
            return new ArteUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.JOB_EXECUTOR.getValue().longValue()) {
            return new JobExecutorUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.JOB_SCHEDULER.getValue().longValue()) {
            return new JobSchedulerUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.NET_PORT_HANDLER.getValue().longValue()) {
            return new NetPortHandlerUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.JMS_HANDLER.getValue().longValue()) {
            return new JmsHandlerUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.NET_HUB.getValue().longValue()) {
            return new NetHubUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_SMPP.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new SMPPUnit(instance, id, title);
            } else {
                return new NewSMPPUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_TWITTER.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new TwitterUnit(instance, id, title);
            } else {
                return new NewTwitterUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_SERVICE_BUS.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new ServiceBusUnit(instance, id, title);
            } else {
                return new NewServiceBusUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_SMTP.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new MailUnit(instance, id, title);
            } else {
                return new NewMailUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_SMSVIA_SMTP.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new SMSMailUnit(instance, id, title);
            } else {
                return new NewSMSMailUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_FILE.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new FileUnit(instance, id, title);
            } else {
                return new NewFileUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_GSM_MODEM.getValue().longValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new GSMModemUnit(instance, id, title);
            } else {
                return new NewGSMModemUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_GCM.getValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new GcmUnit(instance, id, title);
            } else {
                return new NewGcmUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.DPC_APNS.getValue()) {
            if (USE_OLD_PERSO_UNITS) {
                return new ApnsUnit(instance, id, title);
            } else {
                return new NewApnsUnit(instance, id, title);
            }
        }
        if (unitType.longValue() == EUnitType.SNMP_AGENT.getValue().longValue()) {
            return new SnmpAgentUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.MQ_HANDLER.getValue().longValue()) {
            return new MqUnit(instance, id, title);
        }
        throw new UnsupportedUnitTypeException(unitType, "Unsupported unit type " + String.valueOf(unitType));
    }

    private static final Map<Long, BigDecimal> unitLoadOrderByType;

    static {
        final Map<Long, BigDecimal> unitLoadOrderByTypeContent = new HashMap<Long, BigDecimal>();
        unitLoadOrderByTypeContent.put(EUnitType.ARTE.getValue(), BigDecimal.valueOf(1));
        unitLoadOrderByTypeContent.put(EUnitType.NET_PORT_HANDLER.getValue(), BigDecimal.valueOf(10));
        unitLoadOrderByTypeContent.put(EUnitType.NET_HUB.getValue(), BigDecimal.valueOf(10));
        unitLoadOrderByTypeContent.put(EUnitType.MQ_HANDLER.getValue(), BigDecimal.valueOf(10));
        unitLoadOrderByType = Collections.unmodifiableMap(unitLoadOrderByTypeContent);
    }

    @Override
    public final Map<Long, BigDecimal> getUnitLoadOrderByType() {
        return unitLoadOrderByType;
    }
}
