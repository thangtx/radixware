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

package org.radixware.kernel.designer.common.tree;

import javax.swing.SwingUtilities;
import org.openide.cookies.EditCookie;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.agents.DefaultAgent;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class RadixObjectEditCookie implements EditCookie {

    private final IObjectAgent<RadixObject> agent;

    public RadixObjectEditCookie(RadixObject radixObject) {
        this.agent = new DefaultAgent<>(radixObject);
    }

    public RadixObjectEditCookie(IObjectAgent<RadixObject> agent) {
        this.agent = agent;
    }

    public RadixObject getRadixObject() {
        return agent.getObject();
    }

    @Override
    public void edit() {
        final RadixObject obj = getRadixObject();
        if (obj == null) {
            DialogUtils.messageError("No object to edit found");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EditorsManager.getDefault().open(obj);
            }
        });

    }
}
