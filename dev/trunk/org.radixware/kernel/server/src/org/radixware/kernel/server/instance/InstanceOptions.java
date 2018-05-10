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
package org.radixware.kernel.server.instance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.server.utils.IPriorityResourceManager;

final class InstanceOptions {

    private final long profilePeriodMillis;
    private final boolean autoActualizeVer;
    private final Long memoryCheckPeriodMillis;
    private final int minArteInstCount;
    private final int normalArteInstCount;
    private final int aboveNormalArteInstCount;
    private final int highArteInstCount;
    private final int veryHighArteInstCount;
    private final int criticalArteInstCount;
    private final Long arteInstLiveTimeMin;
    private final String scpName;
    private final long sapId;
    private final String httpProxy;
    private final String httpsProxy;
    private final KrbServiceOptions krbServiceOptions;
    private final String priorityMapStr;
    //shouldn't be included in hash calculation and equals() check
    private final Map<Integer, Integer> priorityMap;
    private final boolean useActiveArteLimits;
    private final IPriorityResourceManager.Options activeArteLimits;
    private final int aadcSysMemberId, aadcSysTestedMemberId, aadcInstMemberId;
    private final String aadcDgAddress;
    private final String aadcUnlockTables;
    private final int aadcCommitedLockExp;
    private final int delayBeforeAutoRestartSec;
    private final int threadsStateGatherPeriodSec;
    private final int threadsStateForcedGatherPeriodSec;
    private final int aadcAffinityTimeoutSec;

    public InstanceOptions(
            final long profilePeriodMillis,
            final boolean autoActualizeVer,
            final Long memoryCheckPeriodMillis,
            final int minArteInstCount,
            final int normalArteInstCount,
            final int aboveNormalArteInstCount,
            final int highArteInstCount,
            final int veryHighArteInstCount,
            final int criticalArteInstCount,
            final Long arteInstLiveTimeMin,
            final String scpName,
            final long sapId,
            final String httpProxy,
            final String httpsProxy,
            final KrbServiceOptions krbSeviceOptions,
            final String priorityMapStr,
            final boolean useActiveArteLimits,
            final IPriorityResourceManager.Options activeArteLimits,
            final int aadcSysMemberId,
            final int aadcSysTestedMemberId,
            final int aadcInstMemberId,
            final String aadcDgAddress,
            final String aadcUnlockTables,
            final int aadcCommitedLockExp,
            final int delayBeforeAutoRestartSec,
            final int threadsStateGatherPeriodSec,
            final int threadsStateForcedGatherPeriodSec,
            final int aadcAffinityTimeoutSec
    ) {
        this.profilePeriodMillis = profilePeriodMillis;
        this.autoActualizeVer = autoActualizeVer;
        this.memoryCheckPeriodMillis = memoryCheckPeriodMillis;
        this.arteInstLiveTimeMin = arteInstLiveTimeMin;
        this.minArteInstCount = minArteInstCount;
        this.normalArteInstCount = normalArteInstCount;
        this.aboveNormalArteInstCount = aboveNormalArteInstCount;
        this.highArteInstCount = highArteInstCount;
        this.veryHighArteInstCount = veryHighArteInstCount;
        this.criticalArteInstCount = criticalArteInstCount;
        this.scpName = scpName;
        this.sapId = sapId;
        this.httpProxy = httpProxy;
        this.httpsProxy = httpsProxy;
        this.krbServiceOptions = krbSeviceOptions;
        this.priorityMapStr = priorityMapStr;
        this.priorityMap = calcPriorityMap(priorityMapStr);
        this.useActiveArteLimits = useActiveArteLimits;
        this.activeArteLimits = activeArteLimits;
        this.aadcSysMemberId = aadcSysMemberId;
        this.aadcSysTestedMemberId = aadcSysTestedMemberId;
        this.aadcInstMemberId = aadcInstMemberId;
        this.aadcDgAddress = aadcDgAddress;
        this.aadcUnlockTables = aadcUnlockTables;
        this.aadcCommitedLockExp = aadcCommitedLockExp;
        this.delayBeforeAutoRestartSec = delayBeforeAutoRestartSec;
        this.threadsStateGatherPeriodSec = threadsStateGatherPeriodSec;
        this.threadsStateForcedGatherPeriodSec = threadsStateForcedGatherPeriodSec;
        this.aadcAffinityTimeoutSec = aadcAffinityTimeoutSec;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (this.profilePeriodMillis ^ (this.profilePeriodMillis >>> 32));
        hash = 19 * hash + (this.autoActualizeVer ? 1 : 0);
        hash = 19 * hash + Objects.hashCode(this.memoryCheckPeriodMillis);
        hash = 19 * hash + this.minArteInstCount;
        hash = 19 * hash + this.normalArteInstCount;
        hash = 19 * hash + this.aboveNormalArteInstCount;
        hash = 19 * hash + this.highArteInstCount;
        hash = 19 * hash + this.veryHighArteInstCount;
        hash = 19 * hash + this.criticalArteInstCount;
        hash = 19 * hash + Objects.hashCode(this.arteInstLiveTimeMin);
        hash = 19 * hash + Objects.hashCode(this.scpName);
        hash = 19 * hash + (int) (this.sapId ^ (this.sapId >>> 32));
        hash = 19 * hash + Objects.hashCode(this.httpProxy);
        hash = 19 * hash + Objects.hashCode(this.httpsProxy);
        hash = 19 * hash + Objects.hashCode(this.krbServiceOptions);
        hash = 19 * hash + Objects.hashCode(this.priorityMapStr);
        hash = 19 * hash + Objects.hashCode(this.priorityMap);
        hash = 19 * hash + (this.useActiveArteLimits ? 1 : 0);
        hash = 19 * hash + Objects.hashCode(this.activeArteLimits);
        hash = 19 * hash + this.aadcSysMemberId;
        hash = 19 * hash + this.aadcInstMemberId;
        hash = 19 * hash + Objects.hashCode(this.aadcDgAddress);
        hash = 19 * hash + Objects.hashCode(this.aadcUnlockTables);
        hash = 19 * hash + this.aadcCommitedLockExp;
        hash = 19 * hash + this.delayBeforeAutoRestartSec;
        hash = 19 * hash + this.threadsStateGatherPeriodSec;
        hash = 19 * hash + this.threadsStateForcedGatherPeriodSec;
        hash = 19 * hash + this.aadcAffinityTimeoutSec;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InstanceOptions other = (InstanceOptions) obj;
        if (this.profilePeriodMillis != other.profilePeriodMillis) {
            return false;
        }
        if (this.autoActualizeVer != other.autoActualizeVer) {
            return false;
        }
        if (!Objects.equals(this.memoryCheckPeriodMillis, other.memoryCheckPeriodMillis)) {
            return false;
        }
        if (this.minArteInstCount != other.minArteInstCount) {
            return false;
        }
        if (this.normalArteInstCount != other.normalArteInstCount) {
            return false;
        }
        if (this.aboveNormalArteInstCount != other.aboveNormalArteInstCount) {
            return false;
        }
        if (this.highArteInstCount != other.highArteInstCount) {
            return false;
        }
        if (this.veryHighArteInstCount != other.veryHighArteInstCount) {
            return false;
        }
        if (this.criticalArteInstCount != other.criticalArteInstCount) {
            return false;
        }
        if (this.delayBeforeAutoRestartSec != other.delayBeforeAutoRestartSec) {
            return false;
        }
        if (!Objects.equals(this.arteInstLiveTimeMin, other.arteInstLiveTimeMin)) {
            return false;
        }
        if (!Objects.equals(this.scpName, other.scpName)) {
            return false;
        }
        if (this.sapId != other.sapId) {
            return false;
        }
        if (!Objects.equals(this.httpProxy, other.httpProxy)) {
            return false;
        }
        if (!Objects.equals(this.httpsProxy, other.httpsProxy)) {
            return false;
        }
        if (!Objects.equals(this.krbServiceOptions, other.krbServiceOptions)) {
            return false;
        }
        if (!Objects.equals(this.priorityMapStr, other.priorityMapStr)) {
            return false;
        }
        if (!Objects.equals(this.priorityMap, other.priorityMap)) {
            return false;
        }
        if (this.useActiveArteLimits != other.useActiveArteLimits) {
            return false;
        }
        if (!Objects.equals(this.activeArteLimits, other.activeArteLimits)) {
            return false;
        }
        if (!Objects.equals(this.aadcSysMemberId, other.aadcSysMemberId)) {
            return false;
        }
        if (!Objects.equals(this.aadcInstMemberId, other.aadcInstMemberId)) {
            return false;
        }
        if (!Objects.equals(this.aadcDgAddress, other.aadcDgAddress)) {
            return false;
        }
        if (!Objects.equals(this.aadcUnlockTables, other.aadcUnlockTables)) {
            return false;
        }
        if (!Objects.equals(this.aadcCommitedLockExp, other.aadcCommitedLockExp)) {
            return false;
        }
        if (this.threadsStateGatherPeriodSec != other.threadsStateGatherPeriodSec) {
            return false;
        }
        if (this.threadsStateForcedGatherPeriodSec != other.threadsStateForcedGatherPeriodSec) {
            return false;
        }
        if (this.aadcAffinityTimeoutSec != other.aadcAffinityTimeoutSec) {
            return false;
        }
        
        return true;
    }

    private Map<Integer, Integer> calcPriorityMap(final String mapString) {
        if (mapString == null || mapString.isEmpty()) {
            return Collections.emptyMap();
        }
        final String[] entries = mapString.split(",");
        final Map<Integer, Integer> map = new HashMap<>();
        for (String mapEntryStr : entries) {
            final String[] radixAndJavaPriorities = mapEntryStr.split("=");
            if (radixAndJavaPriorities.length != 2) {//wrong format
                continue;
            }
            try {
                final int radixPriority = Integer.parseInt(radixAndJavaPriorities[0]);
                final int javaPriority = Integer.parseInt(radixAndJavaPriorities[1]);
                map.put(radixPriority, javaPriority);
            } catch (NumberFormatException ex) {
                //continue
            }
        }
        return map;
    }

    private void printKrbOptions(final StringBuilder printer) {
        printer.append("{\n\t\t");
        printer.append(Messages.KEYTAB_PATH);
        printer.append(": ");
        final String keyTabPath = krbServiceOptions.getKeyTabPath();
        printer.append(keyTabPath == null ? Messages.KRB_DEFAULT : keyTabPath);
        printer.append("; \n\t\t");
        printer.append(Messages.KRB_PRINCIPAL);
        printer.append(": ");
        final String principalName = krbServiceOptions.getPrincipalName();
        printer.append(principalName == null ? Messages.KRB_DEFAULT : principalName);
        printer.append(";\n\t}");
    }

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{\n\t");
        strBuilder.append(Messages.PROFILE_PERIOD_SEC);
        strBuilder.append(": ");
        strBuilder.append(String.valueOf(profilePeriodMillis / 1000));
        strBuilder.append("; \n\t");

        strBuilder.append(autoActualizeVer ? Messages.AUTO_ACTUALIZE_ON : Messages.AUTO_ACTUALIZE_OFF);
        strBuilder.append("; \n\t");

        if (autoActualizeVer) {
            strBuilder.append(Messages.AUTO_RESTART_DELAY);
            strBuilder.append(": ");
            strBuilder.append(String.valueOf(delayBeforeAutoRestartSec));
            strBuilder.append("; \n\t");
        }

        strBuilder.append(Messages.MEMORY_CHECK_PERIOD_SEC);
        strBuilder.append(": ");
        strBuilder.append(memoryCheckPeriodMillis == null ? Messages.CHECK_IS_OFF : String.valueOf(memoryCheckPeriodMillis / 1000));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.MIN_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(minArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.NORMAL_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(normalArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.ABOVE_NORMAL_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(aboveNormalArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.HIGH_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(highArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.VERY_HIGH_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(veryHighArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.CRITICAL_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(criticalArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.MAX_TOTAL_ARTE_INST_COUNT);
        strBuilder.append(String.valueOf(normalArteInstCount + aboveNormalArteInstCount + highArteInstCount + veryHighArteInstCount + criticalArteInstCount));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.ARTE_INST_LIVE_TIME_MIN);
        strBuilder.append(String.valueOf(arteInstLiveTimeMin));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.SCP_NAME);
        strBuilder.append(String.valueOf(scpName));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.HTTP_PROXY);
        strBuilder.append(String.valueOf(httpProxy));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.HTTPS_PROXY);
        strBuilder.append(String.valueOf(httpsProxy));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.PRIORITY_MAP_STR);
        strBuilder.append(String.valueOf(priorityMapStr));
        strBuilder.append("; \n\t");

        strBuilder.append(Messages.KRB_OPTIONS);
        strBuilder.append(": ");
        printKrbOptions(strBuilder);
        strBuilder.append(";\n\t");

        Instance runningInstance = Instance.get();
        if (runningInstance != null) {
            strBuilder.append(Messages.USE_ORA_IMPL_STMT_CACHE);
            strBuilder.append(runningInstance.isOraImplicitCacheEnabled());
            strBuilder.append(";");
            if (runningInstance.isOraImplicitCacheEnabled()) {
                strBuilder.append("\n\t");
                strBuilder.append(Messages.ORA_IMPL_STMT_CACHE_SIZE);
                strBuilder.append(runningInstance.getOraImplicitCacheSize());
            }
        }
        strBuilder.append(";\n\t");

        strBuilder.append(Messages.USE_ACTIVE_ARTE_CONSTRAINTS);
        strBuilder.append(useActiveArteLimits);
        if (useActiveArteLimits) {
            strBuilder.append(" ");
            strBuilder.append(activeArteLimits.toString());
        }
        strBuilder.append(";\n\t");

        strBuilder.append(Messages.AADC_MEMBER);
        strBuilder.append(": ");
        strBuilder.append(String.valueOf(aadcInstMemberId));
        strBuilder.append(";\n\t");

        strBuilder.append(Messages.AADC_DG_ADDRESS);
        strBuilder.append(": ");
        strBuilder.append(aadcDgAddress);
        strBuilder.append(";\n\t");

        strBuilder.append("\n}");

        return strBuilder.toString();
    }

    public int getDelayBeforeAutoRestartSec() {
        return delayBeforeAutoRestartSec;
    }
    
    boolean getAutoActualizeVer() {
        return autoActualizeVer;
    }

    long getProfilePeriodMillis() {
        return profilePeriodMillis;
    }

    long getMemCheckPeriodMillis() {
        return memoryCheckPeriodMillis.longValue();
    }

    boolean isMemCheckOn() {
        return memoryCheckPeriodMillis != null;
    }

    public long getSapId() {
        return sapId;
    }

    public Long getArteInstLiveTimeMin() {
        return arteInstLiveTimeMin;
    }

    public int getMinArteInstCount() {
        return minArteInstCount;
    }

    public int getNormalArteInstCount() {
        return normalArteInstCount;
    }

    public int getAboveNormalArteInstCount() {
        return aboveNormalArteInstCount;
    }

    public int getHighArteInstCount() {
        return highArteInstCount;
    }

    public int getVeryHighArteInstCount() {
        return veryHighArteInstCount;
    }

    public int getCriticalArteInstCount() {
        return criticalArteInstCount;
    }

    public String getScpName() {
        return scpName;
    }

    public String getHttpProxy() {
        return httpProxy;
    }

    public String getHttpsProxy() {
        return httpsProxy;
    }

    public KrbServiceOptions getKerberosOptions() {
        return krbServiceOptions;
    }

    public boolean isUseActiveArteLimits() {
        return useActiveArteLimits;
    }

    public IPriorityResourceManager.Options getActiveArteLimitsOptions() {
        return activeArteLimits;
    }

    public int convertRadixPriorityToSystemPriority(int radixPriority) {
        if (priorityMap == null || !priorityMap.containsKey(radixPriority)) {
            return Thread.NORM_PRIORITY;
        }
        return priorityMap.get(radixPriority);
    }

    public Integer getAadcSysMemberId() {
        return aadcSysMemberId > 0 ? aadcSysMemberId : null;
    }

    public Integer getAadcSysTestedMemberId() {
        return aadcSysTestedMemberId > 0 ? aadcSysTestedMemberId : null;
    }

    public Integer getAadcInstMemberId() {
        return aadcInstMemberId > 0 ? aadcInstMemberId : null;
    }

    public String getAadcDgAddress() {
        return aadcDgAddress;
    }

    public String getAadcUnlockTables() {
        return aadcUnlockTables;
    }
    
    public int getAadcCommitedLockExp() {
        return aadcCommitedLockExp;
    }

    public int getThreadsStateGatherPeriodSec() {
        return threadsStateGatherPeriodSec;
    }

    public int getThreadsStateForcedGatherPeriodSec() {
        return threadsStateForcedGatherPeriodSec;
    }

    public int getAadcAffinityTimeoutSec() {
        return aadcAffinityTimeoutSec;
    }
    
}
