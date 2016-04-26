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

import org.snmp4j.smi.OctetString;


public class MetricDataRecord {

    public final VPositive32 typeId;
    public final OctetString kindName;
    public final VUTF8String title;
    public final VPositive32 periodSec;
    public final VDecimal2 lowErrorVal;
    public final VDecimal2 lowWarnVal;
    public final VDecimal2 highWarnVal;
    public final VDecimal2 highErrorVal;
    public final VPositive32 warnDelaySec;
    public final VPositive32 errorDelaySec;
    public final VPositive32 escalationDelaySec;
    public final VPositive32 stateId;
    public final VPositive32 instanceId;
    public final VPositive32 unitId;
    public final VPositive32 systemId;
    public final OctetString serviceUri;
    public final VPositive32 netChannelId;
    public final OctetString timingSection;
    public final VTimestamp begTime;
    public final VTimestamp endTime;
    public final VDecimal2 begVal;
    public final VDecimal2 endVal;
    public final VDecimal2 minVal;
    public final VDecimal2 maxVal;
    public final VDecimal2 avgVal;

    public MetricDataRecord(VPositive32 typeId, OctetString kindName, VUTF8String title, VPositive32 periodSec, VDecimal2 lowErrorVal, VDecimal2 lowWarnVal, VDecimal2 highWarnVal, VDecimal2 highErrorVal, VPositive32 warnDelaySec, VPositive32 errorDelaySec, VPositive32 escalationDelaySec, VPositive32 stateId, VPositive32 instanceId, VPositive32 unitId, VPositive32 systemId, OctetString serviceUri, VPositive32 netChannelId, OctetString timingSection, VTimestamp begTime, VTimestamp endTime, VDecimal2 begVal, VDecimal2 endVal, VDecimal2 minVal, VDecimal2 maxVal, VDecimal2 avgVal) {
        this.typeId = typeId;
        this.kindName = kindName;
        this.title = title;
        this.periodSec = periodSec;
        this.lowErrorVal = lowErrorVal;
        this.lowWarnVal = lowWarnVal;
        this.highWarnVal = highWarnVal;
        this.highErrorVal = highErrorVal;
        this.warnDelaySec = warnDelaySec;
        this.errorDelaySec = errorDelaySec;
        this.escalationDelaySec = escalationDelaySec;
        this.stateId = stateId;
        this.instanceId = instanceId;
        this.unitId = unitId;
        this.systemId = systemId;
        this.serviceUri = serviceUri;
        this.netChannelId = netChannelId;
        this.timingSection = timingSection;
        this.begTime = begTime;
        this.endTime = endTime;
        this.begVal = begVal;
        this.endVal = endVal;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.avgVal = avgVal;
    }
}
