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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class AdsReportGroups extends RadixObjects<AdsReportGroup> {

    protected AdsReportGroups(AdsReportForm ownerForm) {
        super(ownerForm);
    }

    public AdsReportGroup addNew() {
        final AdsReportGroup group = new AdsReportGroup();
        add(group);
        return group;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_GROUP;
    }

    @Override
    protected void onModified() {
        final AdsReportForm form = getOwnerForm();
        if (form != null) {
            form.onModified();
        }
    }

    public AdsReportForm getOwnerForm() {
        for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportForm) {
                return (AdsReportForm) container;
            }
        }
        return null;
    }
}
