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

package org.radixware.kernel.explorer.models;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.explorer.views.CustomWidget;

public abstract class CustomWidgetModel extends Model {

    public CustomWidgetModel(IClientEnvironment environment,CustomWidget def) {
        super(environment,def);
    }

    public CustomWidget getCustomView() {
        if (getView() != null) {
            return ((CustomWidget) getView());
        }
        return null;
    }

    @Override
    public String getConfigStoreGroupName() {
        return getDefinition().getId().toString();
    }
}