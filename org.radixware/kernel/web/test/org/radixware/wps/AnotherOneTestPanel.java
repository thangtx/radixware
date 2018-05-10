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

import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Tree;


class AnotherOneTestPanel extends RootPanel {

    private WpsEnvironment env;

    public AnotherOneTestPanel(WpsEnvironment env) {
        this.env = env;
        this.env.checkThreadState();
        ((WebServer.WsThread) Thread.currentThread()).userSession = env;
        this.createTestUI();
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        throw new UnsupportedOperationException("Explorer view is not supported by TestRoot");
    }

    @Override
    public void closeExplorerView() {
        throw new UnsupportedOperationException("Explorer view is not supported by TestRoot");
    }

    private void createTestUI() {
        PushButton b = new PushButton("Show Dialog");
        b.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                Dialog d = new Dialog("test show");
                d.addCloseAction(EDialogButtonType.CLOSE);
                d.showDialog();
                d.addCloseButtonListener(new Dialog.CloseButtonListener() {
                    @Override
                    public void onClose(EDialogButtonType button, DialogResult result) {
                        MessageBox.information("Dialog closed", "Dialog closed").execMessageBox();
                    }
                });
                MessageBox.information("Dialog Shown", "Dialog Shown").execMessageBox();
            }
        });
        add(b);
        PushButton b2 = new PushButton("Exec Dialog");
        b2.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                Dialog d = new Dialog("test exec");
                d.addCloseAction(EDialogButtonType.CLOSE);
                d.execDialog();
            }
        });
        add(b2);
        b2.getAnchors().setLeft(new Anchors.Anchor(1, 5, b));

        Tree tree = new Tree();

        add(tree);
        tree.setTop(300);
        tree.setWidth(400);
        tree.setHeight(500);

        Node.DefaultNode node = new Node.DefaultNode();

        class CBR extends CheckBox implements IGrid.CellRenderer {

            public CBR() {
                addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(IButton source) {
                        getEnvironment().messageInformation("One", "Two");
                    }
                });
            }

            @Override
            public void update(int r, int c, Object value) {
            }

            @Override
            public void selectionChanged(int r, int c, Object value, IGrid.ICell cell, boolean isSelected) {
            }

            @Override
            public void rowSelectionChanged(boolean isRowSelected) {
            }

            @Override
            public UIObject getUI() {
                return this;
            }
        }

        tree.addColumn("One");
        tree.addColumn("Two");

        node.setDisplayName("AAA");
        tree.setRootNode(node);

    }
}
