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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;


class LabeledEditGridTestRootPanel extends RootPanel {

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

    public LabeledEditGridTestRootPanel(WpsEnvironment env) {
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
        Map<UIObject, String> editor2Label = new HashMap<>();

        final PushButton button1 = new PushButton("Button #1");
        final PushButton button2 = new PushButton("Button #2");
        final PushButton button3 = new PushButton("Button #3");
        final PushButton button4 = new PushButton("Button #4");


        editor2Label.put(button1, button1.getText());
        editor2Label.put(button2, button2.getText());
        editor2Label.put(button3, button3.getText());
        editor2Label.put(button4, button4.getText());
        button1.getAnchors().setRight(new Anchors.Anchor(1, 0));

        final LabeledEditGrid grid = new LabeledEditGrid(new LabeledEditGrid.DefaultEditor2LabelMatcher(editor2Label));

        grid.addEditor(button1, 0, 0);
        grid.addEditor(button2, 1, 0);
        grid.addEditor(button3, 0, 1);
        grid.addEditor(button4, 1, 1);

        button1.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                button2.setVisible(!button2.isVisible());
            }
        });
        
        grid.setLeft(10);
        grid.setTop(50);
        grid.setWidth(400);
        grid.setHeight(100);
        this.add(grid);

        button3.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                MessageBox.information("Test message","TestMessage").execDialog();
            }
        });

        PushButton showTestDialog = new PushButton("SwapEditors");
        this.add(showTestDialog);
        showTestDialog.setLeft(10);
        showTestDialog.setTop(10);
        showTestDialog.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.addEditor(button3, 0, 0);
            }
        });
    }
}
