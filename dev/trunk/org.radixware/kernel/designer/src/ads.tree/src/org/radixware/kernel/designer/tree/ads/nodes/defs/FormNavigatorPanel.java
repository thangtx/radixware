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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.common.tree.RadixNavigatorPanel;


public class FormNavigatorPanel extends RadixNavigatorPanel {

    public FormNavigatorPanel() {
        super();
    }

    @Override
    protected Definition getDelegateDefinition(RadixObject def) {
        final AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(def);
        if (uiDef != null) {
            return uiDef.getWidget();
        }
        return super.getDelegateDefinition(def);
    }
}
