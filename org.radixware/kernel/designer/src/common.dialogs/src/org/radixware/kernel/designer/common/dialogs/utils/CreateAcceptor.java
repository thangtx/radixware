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
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


class CreateAcceptor implements IAdvancedAcceptor<String> {

    private final CheckForDuplicationProvider provider;

    public CreateAcceptor(final RadixObjects container, EDefType defType) {
        provider = CheckForDuplicationProvider.Factory.newForCreation(container, defType);
    }

    @Override
    public IAcceptResult getResult(String candidate) {
        if (!RadixObjectsUtils.isCorrectName(candidate)) {
            return new DefaultAcceptResult(false, "Invalid name");
        }

        if (provider != null && provider.findDuplicated(candidate) != null) {
            return new DefaultAcceptResult(false, "Duplicated name");
        }

        return new DefaultAcceptResult(true, "");
    }
}
