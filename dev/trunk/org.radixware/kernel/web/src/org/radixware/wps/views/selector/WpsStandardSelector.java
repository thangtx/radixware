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

package org.radixware.wps.views.selector;

import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.wps.WpsEnvironment;


public class WpsStandardSelector extends RwtSelector {

    public WpsStandardSelector(WpsEnvironment env) {
        super(env);
    }

    @Override
    public void open(Model model) {
        super.open(model);
        RwtSelectorGrid grid = new RwtSelectorGrid(this, (GroupModel) model);
        setSelectorWidget(grid);
    }
}
