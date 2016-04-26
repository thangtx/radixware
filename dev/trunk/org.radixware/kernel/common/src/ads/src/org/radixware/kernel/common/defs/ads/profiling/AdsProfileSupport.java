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

package org.radixware.kernel.common.defs.ads.profiling;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.commondef.ProfileInfo;


public class AdsProfileSupport {

    public interface IProfileable {

        /**
         * Returns profile support instance
         */
        public AdsProfileSupport getProfileSupport();

        /**
         * Returns profiling possibilities for IProifleable instance
         * if result is true, getProfileSupport() method must not return null
         */
        public boolean isProfileable();

        public AdsDefinition getAdsDefinition();
    }
    private final IProfileable owner;
    private String timingSectionId = null;

    public AdsProfileSupport(IProfileable owner) {
        this.owner = owner;
    }

    public String getTimingSectionId() {
        return timingSectionId;
    }

    public void setTimingSectionId(String id) {
        if (!Utils.equals(id, this.timingSectionId)) {
            this.timingSectionId = id;
            ((RadixObject) this.owner).setEditState(RadixObject.EEditState.MODIFIED);
        }
    }

    public boolean isProfiled() {
        return timingSectionId != null;
    }

    public void loadFrom(ProfileInfo xDef) {
        if (xDef != null && xDef.isSetTimingSectionId()) {
            this.timingSectionId = xDef.getTimingSectionId();
        }
    }

    public void appendTo(ProfileInfo xDef) {
        if (timingSectionId != null) {
            xDef.setTimingSectionId(timingSectionId);
        }
    }

    public AdsEnumDef findTimingSections() {
        if (!owner.getAdsDefinition().isInBranch()) {
            return null;
        }
        AdsSegment segment = (AdsSegment) owner.getAdsDefinition().getModule().getSegment();

        IPlatformClassPublisher publisher = segment.getBuildPath().getPlatformPublishers().findPublisherByName(ETimingSection.class.getName());
        if (publisher instanceof AdsEnumDef) {
            return (AdsEnumDef) publisher;
        } else {
            return null;
        }
    }
}
