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

package org.radixware.kernel.server.units.arte;

import java.util.Objects;
import org.radixware.kernel.server.sap.SapOptions;

final class ArteUnitOptions {
//TODO make it @Immutable

    volatile long sapId = 0;
    volatile int defaultPriority = 0;
    volatile int highArteInstCount = 1;
    volatile SapOptions sapOptions;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArteUnitOptions other = (ArteUnitOptions) obj;
        if (this.sapId != other.sapId) {
            return false;
        }
        if (this.defaultPriority != other.defaultPriority) {
            return false;
        }
        if (this.highArteInstCount != other.highArteInstCount) {
            return false;
        }
        if (!Objects.equals(this.sapOptions, other.sapOptions)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.sapId ^ (this.sapId >>> 32));
        hash = 71 * hash + this.defaultPriority;
        hash = 71 * hash + this.highArteInstCount;
        hash = 71 * hash + Objects.hashCode(this.sapOptions);
        return hash;
    }

    @Override
    public String toString() {
        return "{\n\t"
                + ArteUnitMessages.HIGH_ARTE_INST_COUNT + String.valueOf(highArteInstCount) + "; \n\t"
                + ArteUnitMessages.THREAD_PRIORITY + String.valueOf(defaultPriority) + "; \n\t"
                + sapOptions.toString()
                + "}";
    }
}
