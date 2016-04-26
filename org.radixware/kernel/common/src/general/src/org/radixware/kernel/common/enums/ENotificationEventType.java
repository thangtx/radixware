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


public enum ENotificationEventType implements IKernelStrEnum {

    DATABASE_UPGRADED("Database Upgraded", "DatabaseUpgraded"),
    //
    DISTRIBUTIVE_PREPARED("Distribution Kit Prepared", "DistribPrepared"), // created new distributive in client entry
    DISTRIBUTIVE_EXPORTED("Distrion Kit Exported", "DistribExported"), // distributive exported to zip file
    DISTRIBUTIVE_IMPORTED("Distribution Kit Imported", "DistribImported"), // distributive loaded from zip file
    //SEND_DISTRIB_TO_RELEASES("Distribution Kit Sent to Releases", "SendDistribToReleases"),
    SEND_DISTRIB_TO_DEVELOPMENT("Distribution Kit Sent to Development", "SendDistribToDevelopment"),
    //
    PATCH_PREPARED("Patch Prepared", "PatchPrepared"), // created new patch in client entry
    PATCH_EXPORTED("Patch Exported", "PatchExported"), // patch exported to zip file
    PATCH_APPLIED_TO_DEVELOPMENT("Patch Applied for Development", "PatchAppliedToDevelopment"), // patch loaded from zip file to development entry (trunk, offshoot, patch...)
    PATCH_APPLIED_TO_TEST("Patch Applied for Test", "PatchAppliedToTest"), // patch loaded from zip file to test entry
    PATCH_APPLIED_TO_PRODUCTION("Patch Applied for Production", "PatchAppliedToProduction"), // patch loaded from zip file to production entry
    //
    RELEASE_CREATED("Release Created", "ReleaseToTest"),
    //
    RELEASE_TO_TESTING("Release Sent to Test", "SendReleaseToTest"),
    RELEASE_TO_PRODUCTION("Release Sent to Production", "SendReleaseToProduction"),
    //
    RELEASE_STATUS_INVALID("Release Status Set to Invalid", "ReleaseStatusInvalid"),
    RELEASE_STATUS_EXPIRED("Release status Set to Expired", "ReleaseStatusExpired"),
    RELEASE_STATUS_TEST("Release Status Set to Test", "ReleaseStatusTest"),
    RELEASE_STATUS_URGENT("Release Status Set to Urgent", "ReleaseStatusUrgent"),
    RELEASE_STATUS_PRODUCTION("Release Status Set to Production", "ReleaseStatusProduction"),
    //
    RELEASE_TRANSLATED("Release Description Translated", "ReleaseTranslated");
    private final String name;
    private final String value;

    private ENotificationEventType(final String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public boolean useRelease(){
            return
                RELEASE_CREATED.equals(this) ||
                RELEASE_TO_TESTING.equals(this) ||
                RELEASE_TO_PRODUCTION.equals(this) || 
                RELEASE_STATUS_INVALID.equals(this) ||
                RELEASE_STATUS_EXPIRED.equals(this) ||
                RELEASE_STATUS_TEST.equals(this) ||
                RELEASE_STATUS_PRODUCTION.equals(this) ||                
                RELEASE_TRANSLATED.equals(this);
    }
    
    
    public boolean isDeprecated(){
        return  PATCH_PREPARED.equals(this) ||
                PATCH_EXPORTED.equals(this) ||
                PATCH_APPLIED_TO_DEVELOPMENT.equals(this) ||
                PATCH_APPLIED_TO_TEST.equals(this) ||
                PATCH_APPLIED_TO_PRODUCTION.equals(this);
    }

    @Override
    public String getName() {
        return name;
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

    public static ENotificationEventType getForValue(final String val) {
        for (ENotificationEventType e : ENotificationEventType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("ENotificationEventType has no item with value: " + String.valueOf(val), val);
    }
}
