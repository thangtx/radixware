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

package org.radixware.kernel.designer.ads.editors.enumeration;

import org.radixware.kernel.common.defs.ads.enumeration.ValueRange;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRanges;
import org.radixware.kernel.common.enums.EValType;


public class StateValueRangeCreationDialog extends StateAbstractValueRangeSetupDialog{

    public StateValueRangeCreationDialog(ValueRanges valueRanges, EValType eValType) {
        super(new ValueRangePanel(eValType), valueRanges, "New Value Range");
    }

    @Override
    protected void apply() {

        final ValueRange newValueRange = ((ValueRangePanel) getComponent()).getCreatedValueRange();
        valueRanges.add(newValueRange);
    }
}
