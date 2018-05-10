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

package org.radixware.wps;

import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.ButtonBox;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.RootPanel;


public class ButtonBoxTestRootPanel extends RootPanel {

    private final WpsEnvironment env;

    public ButtonBoxTestRootPanel(WpsEnvironment env) {
        this.env = env;
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {
    }

    @Override
    protected Runnable componentRendered(HttpQuery query) {
        return new Runnable() {
            @Override
            public void run() {
                createTestUI();
            }
        };
    }

    private void createTestUI() {
        ButtonBox bb = new ButtonBox();

        Container container = new Container();
        container.setWidth(500);


        add(container);

        bb.addButton(EDialogButtonType.OK, Alignment.LEFT);
        bb.addButton(EDialogButtonType.CANCEL, Alignment.CENTER);
        bb.addButton(EDialogButtonType.CANCEL, Alignment.CENTER);
        bb.addButton(EDialogButtonType.CLOSE, Alignment.RIGHT);

        container.add(bb);
    }
}
