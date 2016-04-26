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

package org.radixware.kernel.server.monitoring;

import org.radixware.kernel.common.enums.ESensorType;
import org.radixware.kernel.common.utils.Utils;

/**
 * Describes object for monitoring.
 *
 */
public class SensorCoordinates {

    private static final SensorCoordinates NONE_INSTANCE = new SensorCoordinates(ESensorType.NONE, null, null, null, null, null, null);
    private final ESensorType sensorKind;
    private final Long instanceId;
    private final Long unitId;
    private final Long systemId;
    private final String serviceUri;
    private final String sectionName;
    private final Long netChannelId;

    protected SensorCoordinates(final ESensorType sensorKind,
            final Long instanceId,
            final Long unitId,
            final Long systemId,
            final String serviceUri,
            final String sectionName,
            final Long netListenerId) {
        this.sensorKind = sensorKind;
        this.instanceId = instanceId;
        this.unitId = unitId;
        this.systemId = systemId;
        this.serviceUri = serviceUri;
        this.sectionName = sectionName;
        this.netChannelId = netListenerId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public Long getNetChannelId() {
        return netChannelId;
    }

    public ESensorType getSensorType() {
        return sensorKind;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public Long getSystemId() {
        return systemId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public String getSectionName() {
        return sectionName;
    }

    @Override
    public String toString() {
        switch (sensorKind) {
            case INSTANCE:
                return "inst#" + instanceId;
            case TIMING_SECTION:
                return "inst#" + instanceId + "/sect#" + sectionName;
            case INSTANCE_SERVICE:
                return "inst#" + instanceId + "/servUri#" + serviceUri;
            case NET_CHANNEL:
                return "netChannel#" + netChannelId;
            case UNIT:
                return "unit#" + unitId;
            case NONE:
                return "none";
            default:
                throw new IllegalArgumentException("Unsupported sensor kind");
        }
    }

    /**
     * Indicates that this coordinates define a group of metrics (e.g., when
     * instanceId == null for instance coordinates, it means that the metric
     * should be calculated for each instance)
     *
     * @return
     */
    public boolean isGroup() {
        switch (sensorKind) {
            case INSTANCE:
                return instanceId == null;
            case TIMING_SECTION:
                return instanceId == null;
            case INSTANCE_SERVICE:
                return instanceId == null || serviceUri == null;
            case NET_CHANNEL:
                return netChannelId == null;
            case UNIT:
                return unitId == null;
            default:
                throw new IllegalArgumentException("Unsupported sensor kind");
        }
    }

    public boolean belongsToGroup(final SensorCoordinates groupCoordinates) {
        if (groupCoordinates.getSensorType() != sensorKind || !groupCoordinates.isGroup()) {
            return false;
        }
        switch (sensorKind) {

            case TIMING_SECTION:
                return Utils.equals(sectionName, groupCoordinates.getSectionName());
            case INSTANCE_SERVICE:
                return (groupCoordinates.getInstanceId() == null || Utils.equals(groupCoordinates.instanceId, instanceId))
                        && (groupCoordinates.getServiceUri() == null || Utils.equals(groupCoordinates.getServiceUri(), serviceUri));
            case NET_CHANNEL:
            case INSTANCE:
            case UNIT:
                return true;
            default:
                throw new IllegalArgumentException("Unsupported sensor kind");
        }
    }

    public static SensorCoordinates forUnit(final Long unitId) {
        return new SensorCoordinates(
                ESensorType.UNIT, null, unitId, null, null, null, null);
    }

    public static SensorCoordinates forInstance(final long instanceId) {
        return new SensorCoordinates(
                ESensorType.INSTANCE, instanceId, null, null, null, null, null);
    }

    public static SensorCoordinates forInstanceSection(final long instanceId, final String sectionName) {
        return new SensorCoordinates(
                ESensorType.TIMING_SECTION, instanceId, null, null, null, sectionName, null);
    }

    public static SensorCoordinates forInstanceService(final Long instanceId, final String serviceUri) {
        return new SensorCoordinates(
                ESensorType.INSTANCE_SERVICE, instanceId, null, null, serviceUri, null, null);
    }

    public static SensorCoordinates forNetChannel(final Long channelId) {
        return new SensorCoordinates(
                ESensorType.NET_CHANNEL, null, null, null, null, null, channelId);
    }

    public static SensorCoordinates none() {
        return NONE_INSTANCE;
    }
}
