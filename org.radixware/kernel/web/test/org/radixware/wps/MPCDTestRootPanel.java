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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;


class MPCDTestRootPanel extends RootPanel {

    private WpsEnvironment env;

    public MPCDTestRootPanel(WpsEnvironment env) {
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
        final List<String> propertyTitles = new LinkedList<>();
        for (int i=1; i<=4; i++){
            propertyTitles.add("Property "+String.valueOf(i));
        }
        final PushButton messageBoxRunner = new PushButton("Show Mandatory Properties Confirmation");        
        messageBoxRunner.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(final IButton source) {
                IDialog dialog = 
                    env.getApplication().getDialogFactory().newMandatoryPropertiesConfirmationDialog(env, null, propertyTitles);
                dialog.setWindowTitle("Confirm to Close Editor");
                dialog.execDialog();
            }
        });
        add(messageBoxRunner);
    }
}
