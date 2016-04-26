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

package org.radixware.kernel.server.reports;

import org.radixware.kernel.common.utils.Utils;

class GroupInfo {

    private boolean opened = false;
    private Object conditionValue = null;
    private final SummaryCollector summary = new SummaryCollector();

    public void open(final Object currentCondition) {
        this.conditionValue = currentCondition;
        this.opened = true;
    }

    public boolean isChanged(final Object currentCondition) {
        if (!this.opened) {
            return true;
        }

        return !Utils.equals(conditionValue, currentCondition);
    }

    public void close() {
        this.conditionValue = null;
        this.opened = false;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public SummaryCollector getSummary() {
        return summary;
    }
}
