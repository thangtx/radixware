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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;


class GridBoxContainerTestRootPanel extends RootPanel {

    private WpsEnvironment env;

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

    public GridBoxContainerTestRootPanel(WpsEnvironment env) {
        this.env = env;
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
        PushButton showTestDialog = new PushButton("Show Test Dialog");
        this.add(showTestDialog);
        showTestDialog.setLeft(10);
        showTestDialog.setTop(10);
        showTestDialog.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                Dialog dialog = new Dialog("Test");
                dialog.setWidth(400);
                dialog.setHeight(400);


                GridBoxContainer container = new GridBoxContainer(2,2);

                dialog.add(container);

                container.getAnchors().setRight(new Anchors.Anchor(1, -5));
                container.getAnchors().setBottom(new Anchors.Anchor(1, -5));

                container.setLeft(5);
                container.setTop(5);

                container.setWidth(1);
                container.setHeight(1);

                try {

                    container.add(new PushButton("Long button"), 0, 0);
                   // container.setColSpan(0, 0, 2);
                    container.add(new PushButton("Short button #1"), 1, 0);
                    container.add(new PushButton("Short button #2"), 1, 1);
                } catch (Throwable e) {
                    Logger.getLogger(GridBoxContainerTestRootPanel.class.getName()).log(Level.SEVERE, "Error", e);
                }

                dialog.execDialog();
            }
        });
    }
}