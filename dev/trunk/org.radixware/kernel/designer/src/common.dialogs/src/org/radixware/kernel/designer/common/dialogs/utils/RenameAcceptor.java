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

package org.radixware.kernel.designer.common.dialogs.utils;

import org.radixware.kernel.common.builder.check.common.CheckForDuplicationProvider;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


class RenameAcceptor implements IAdvancedAcceptor<String> {

    private final RadixObject radixObject;

    public RenameAcceptor(RadixObject radixObject) {
        this.radixObject = radixObject;
    }

    @Override
    public IAcceptResult getResult(String candidate) {
        if (radixObject.getNamingPolicy() != ENamingPolicy.FREE && radixObject.getNamingPolicy() != ENamingPolicy.CALC) {
            if (!RadixObjectsUtils.isCorrectName(candidate)) {
                return new DefaultAcceptResult(false, "Invalid name");
            }
        }

        CheckForDuplicationProvider checkForDuplicationProvider = CheckForDuplicationProvider.Factory.newForRenaming(radixObject);

        if (checkForDuplicationProvider != null && checkForDuplicationProvider.findDuplicated(candidate) != null) {
            return new DefaultAcceptResult(false, "Duplicated name");
        }

        return new DefaultAcceptResult(true, "");
    }
}
