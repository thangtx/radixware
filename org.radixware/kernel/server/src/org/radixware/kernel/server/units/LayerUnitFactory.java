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
package org.radixware.kernel.server.units;

import java.math.BigDecimal;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.arte.ArteUnit;
import org.radixware.kernel.server.units.job.executor.JobExecutorUnit;
import org.radixware.kernel.server.units.job.scheduler.JobSchedulerUnit;
import org.radixware.kernel.server.units.netport.NetPortHandlerUnit;
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
            return new SmppUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_TWITTER.getValue().longValue()) {
            return new TwitterUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_SERVICE_BUS.getValue().longValue()) {
            return new ServiceBusUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_SMTP.getValue().longValue()) {
            return new MailUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_SMSVIA_SMTP.getValue().longValue()) {
            return new SmsMailUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_FILE.getValue().longValue()) {
            return new FileUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_GSM_MODEM.getValue().longValue()) {
            return new GsmModemUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_GCM.getValue()) {
            return new GcmUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_APNS.getValue()) {
            return new ApnsUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_WNS.getValue()) {
            return new WnsUnit(instance, id, title);
        }
        if (unitType.longValue() == EUnitType.DPC_DELIVERY_ACK.getValue()) {
            return new DeliveryAckUnit(instance, id, title);
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
