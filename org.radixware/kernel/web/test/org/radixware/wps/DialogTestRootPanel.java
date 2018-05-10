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

import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.*;
import org.radixware.wps.views.RwtAction;


public class DialogTestRootPanel extends RootPanel {

    private WpsEnvironment env;

    public DialogTestRootPanel(WpsEnvironment env) {
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
        PushButton button = new PushButton("Show Dialog");
        add(button);
        button.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                final Dialog dialog = new Dialog("Test");
                dialog.setWidth(300);
                dialog.setHeight(200);
                PushButton showButtonBar = new PushButton("Show Close Buttons");
                dialog.add(showButtonBar);
                final boolean[] shown = new boolean[]{false};
                showButtonBar.addClickHandler(new IButton.ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        dialog.clearCloseActions();
                        if (!shown[0]) {
                            dialog.addCloseAction(EDialogButtonType.CANCEL, null);
                        }
                        shown[0] = !shown[0];

                    }
                });
                PushButton btAddTab = new PushButton("Add");
                dialog.add(btAddTab);
                btAddTab.getAnchors().setLeft(new Anchors.Anchor(1, 10, showButtonBar));
                final TabLayout l = new TabLayout();
                l.getAnchors().setRight(new Anchors.Anchor(1, -10));
                l.getAnchors().setBottom(new Anchors.Anchor(1, -10));
                l.setTop(30);
                l.setLeft(10);
                btAddTab.addClickHandler(new IButton.ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        TabLayout.Tab tab = l.addTab("new tab");
                        PushButton b = new PushButton("aaa" + l.getTabCount());
                        tab.add(b);
                    }
                });
                dialog.add(l);
                PushButton btRemoveTab = new PushButton("Remove");
                dialog.add(btRemoveTab);
                btRemoveTab.getAnchors().setLeft(new Anchors.Anchor(1, 10, btAddTab));
                btRemoveTab.addClickHandler(new IButton.ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        if (l.getTabCount() > 1) {
                            l.removeTab(l.getTabs().get(l.getTabCount() - 1));
                        }
                    }
                });
                PushButton btShowMessageBox = new PushButton("Show message box");
                dialog.add(btShowMessageBox);
                btShowMessageBox.addClickHandler(new IButton.ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        MessageBox.information("Information", "The very very long message; asdhasiodsh oiasj dakosdasd s\nopsjfklsdmd, mds.,fsd.fsd\n wjhiosdn fklsdnfsdf,.sdfmsd.,fmsdl;fmsdl;fmsdl;fmsdl;fmsdklmfl;sdmfsd,fmsdklj;tmw;'kp[gkldf'g\nasfklsdjf klsfjsdklfjklsdjfklsdjfkl\nsmdklfjsdklf msdlfmsdl;\nsdfkjsd;fsdlkf;sd\nklsjdf klsdfmsdl; fksdplf\nasoidfh asljhsdklfnsdklfgnsdklfnlsd\n jdfjsl;fjsdkl;fjsdkl;fj \nsoidfjsdl kfjsdklfjsdklfmsdltkophk[g dflgb fgm,n\n jsoepfjmsd,;cvmsd,.mfsd,./mbdf./vm xcl;vm xklgjdfkl;v\nmklsd;\njv sdl;\n\n\n fopsejfl;sdfml;sdfg\nmnsdk l;vmsdkl;m v;\n").execMessageBox();
                    }
                });
                btShowMessageBox.getAnchors().setLeft(new Anchors.Anchor(1, 10, btRemoveTab));
                dialog.execDialog();
            }
        });

        final ToolBar tb = new ToolBar();
        add(tb);
        tb.setTop(150);
        tb.addAction(new RwtAction(WsIcons.ADD));
        tb.addAction(new RwtAction(WsIcons.CLEAR));
        tb.addAction(new RwtAction(WsIcons.CONNECT));
        button = new PushButton("Switch tool bar orientation");
        add(button);
        button.setLeft(200);
        button.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                try {
                    if (tb.isVertical()) {
                        tb.setHorizontal();
                    } else {
                        tb.setVertical();
                    }
                } catch (Throwable e) {
                    getEnvironment().processException(e);
                }
            }
        });

        final VerticalBoxContainer vbox = new VerticalBoxContainer();
        add(vbox);

        PushButton topButton = new PushButton("TOP");

        vbox.add(1, topButton);
        vbox.setAutoSize(topButton, true);
        final PushButton bottomButton = new PushButton("BOTTOM");
        vbox.add(1, bottomButton);
        topButton.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (bottomButton.getParent() == vbox) {
                    vbox.remove(bottomButton);
                } else {
                    vbox.add(1, bottomButton);
                }
            }
        });

        vbox.setTop(50);
        vbox.setLeft(100);
        vbox.setWidth(300);
        vbox.setHeight(300);

    }
}
