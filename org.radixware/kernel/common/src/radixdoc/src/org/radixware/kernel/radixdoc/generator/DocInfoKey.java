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
package org.radixware.kernel.radixdoc.generator;

import java.util.Objects;
import org.radixware.kernel.common.types.Id;

public class DocInfoKey {
    private final Id definitionId;
    private final String definitionLayer;

    public DocInfoKey(Id definitionId, String definitionLayer) {
        this.definitionId = definitionId;
        this.definitionLayer = definitionLayer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.definitionId);
        hash = 67 * hash + Objects.hashCode(this.definitionLayer);
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
        final DocInfoKey other = (DocInfoKey) obj;
        if (!Objects.equals(this.definitionId, other.definitionId)) {
            return false;
        }
        if (!Objects.equals(this.definitionLayer, other.definitionLayer)) {
            return false;
        }
        return true;
    }   
}
