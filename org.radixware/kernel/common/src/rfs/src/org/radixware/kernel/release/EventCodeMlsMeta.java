/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.release;

import java.util.Objects;
import org.radixware.kernel.common.enums.EIsoLanguage;

public class EventCodeMlsMeta {
    public final String bundleId;
    public final String stringId;
    public final EIsoLanguage language;
    public final String value;

    public EventCodeMlsMeta(String bundleId, String stringId, EIsoLanguage language, String value) {
        this.bundleId = bundleId;
        this.stringId = stringId;
        this.language = language;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.bundleId);
        hash = 67 * hash + Objects.hashCode(this.stringId);
        hash = 67 * hash + Objects.hashCode(this.language);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventCodeMlsMeta other = (EventCodeMlsMeta) obj;
        if (!Objects.equals(this.bundleId, other.bundleId)) {
            return false;
        }
        if (!Objects.equals(this.stringId, other.stringId)) {
            return false;
        }
        if (this.language != other.language) {
            return false;
        }
        return true;
    }
    
    
}
