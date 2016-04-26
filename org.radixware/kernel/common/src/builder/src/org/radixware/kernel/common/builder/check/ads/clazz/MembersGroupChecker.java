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

package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.MembersGroup;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class MembersGroupChecker extends AdsDefinitionChecker<MembersGroup> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return MembersGroup.class;
    }

    @Override
    public void check(MembersGroup group, IProblemHandler problemHandler) {
        super.check(group, problemHandler);
        @SuppressWarnings("unchecked")
        List<Id> memberIds = group.getMemberIds();
        ExtendableDefinitions set = group instanceof AdsMethodGroup ? group.getOwnerClass().getMethods() : group.getOwnerClass().getProperties();
        for (Id id : memberIds) {
            if (set.findById(id, EScope.ALL).get()== null) {
                error(group, problemHandler, "Unknown member in group: #" + id.toString());
            }
        }
    }
}
