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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;

public final class ParentRefSetterError extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5847300655192716023L;
    private Model model;

    public ParentRefSetterError(PropertyRef property) {
        super("Can`t set value for property " + property.getDefinition().toString());
    }

    public ParentRefSetterError(PropertyRef property, String err) {
        super("Can`t set value for property " + property.getDefinition().toString() + "\n" + err);
    }

    public ParentRefSetterError(PropertyRef property, Throwable arg1) {
        super("Can`t set value for property " + property.getDefinition().toString(), arg1);
        model = property.getOwner();
    }

    public Model getSourceModel() {
        return model;
    }
    
    public void setSourceModel(final Model model){
        this.model = model;
    }
}
