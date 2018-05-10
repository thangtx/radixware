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
package org.radixware.kernel.server.aio;

import org.radixware.kernel.server.instance.aadc.AadcManager;

public class AadcAffinity {

    private final int affinityKey;
    private final long expirationDurationMs;
    private final Integer forcedMemberId;


    public AadcAffinity(int affinityKey) {
        this.affinityKey = affinityKey;
        this.expirationDurationMs = AadcManager.DEFAULT_AFFINITY_DURATION_MARKER;
        this.forcedMemberId = null;
    }
    
    public AadcAffinity(int affinityKey, long expirationDurationMs) {
        this.affinityKey = affinityKey;
        this.expirationDurationMs = expirationDurationMs;
        this.forcedMemberId = null;
    }

    private AadcAffinity(int affinityKey, Integer forcedMemberId, long expirationDurationMs) {
        this.affinityKey = affinityKey;
        this.forcedMemberId = forcedMemberId;
        this.expirationDurationMs = expirationDurationMs;
    }
    
    static public AadcAffinity buildNormal(int affinityKey) {
        return new AadcAffinity(affinityKey, null, AadcManager.DEFAULT_AFFINITY_DURATION_MARKER);
    }
    static public AadcAffinity buildNormal(int affinityKey, long expirationDurationMs) {
        return new AadcAffinity(affinityKey, null, expirationDurationMs);
    }
    static public AadcAffinity buildForced(int forcedMemberId) {
        return new AadcAffinity(0, forcedMemberId, AadcManager.DEFAULT_AFFINITY_DURATION_MARKER);
    }
    static public AadcAffinity buildForced(int forcedMemberId, long expirationDurationMs) {
        return new AadcAffinity(0, forcedMemberId, expirationDurationMs);
    }
            
    public int getAffinityKey() {
        return affinityKey;
    }

    public Integer getForcedMemberId() {
        return forcedMemberId;
    }
    
    /**
     *
     * @return expiration duration in ms
     */
    public Long getExpirationDuration() {
        return expirationDurationMs;
    }
}
