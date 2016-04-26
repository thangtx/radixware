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

import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.MainStatusBar;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.decorations.ProductStartPage;


public class DefaultRootPanel extends RootPanel {

    private final WpsEnvironment env;
    private boolean isFirstShown = true;
    private MainView mainView;
    private UIObject startPage;
    private VerticalBox layout;
    private MainStatusBar statusBar;

    public DefaultRootPanel(final WpsEnvironment env) {
        this.env = env;
        layout = new VerticalBox();
        final ToolBar connectionToolBar = new ConnectionToolBar(env, false);
        layout.add(connectionToolBar);
        startPage = new ProductStartPage();
        layout.add(startPage);
        add(layout);
        recreateStatusBar();
    }

    @Override
    protected void setupStatusBarListener(StatusBarListener listener) {
        if (statusBar != null) {
            listener.statusBarOpened(statusBar);
        }
    }

    private void recreateStatusBar() {

        statusBar = new MainStatusBar();
        statusBar.setHeight(20);
        statusBar.getHtml().setCss("border-top", "solid 1px #BBB");
        layout.add(statusBar);
        fireStatusBarOpened(statusBar);
    }

    @Override
    protected Runnable componentRendered(final HttpQuery q) {

        return new Runnable() {

            @Override
            public void run() {
                synchronized (DefaultRootPanel.this) {
                    if (isFirstShown) {
                        isFirstShown = false;
                        connect();
                    }
                }
            }
        };
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    private void connect() {
        loop:
        for (;;) {
            try {
                final WpsEnvironment.CheckConnectionState connectionState = env.checkConnection();
                switch (connectionState) {
                    case DONE:

//                        getExplorerView().open(env);
                        break loop;
                    case CANCELLED:
                        break loop;
                    case FAULT:
                        continue;
                }
            } catch (Throwable e) {
                env.messageException("Error", e.getMessage(), e);
            }
        }
    }

    @Override
    public IMainView getExplorerView() {
        if (mainView == null) {
            mainView = new MainView(env);
            if (statusBar != null) {
                fireStatusBarClosed(statusBar);
                statusBar = null;
            }
            layout.clear();
            layout.add(mainView);
            mainView.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            startPage = null;
            recreateStatusBar();
        }
        return mainView;
    }

    @Override
    public void closeExplorerView() {
        if (mainView != null) {
            if (statusBar != null) {
                fireStatusBarClosed(statusBar);
                statusBar = null;
            }
            layout.clear();
            mainView = null;
            if (!env.context.wasDisposed()){
                final ToolBar connectionToolBar = new ConnectionToolBar(env, false);
                layout.add(connectionToolBar);
                startPage = new ProductStartPage();
                layout.add(startPage);            
                recreateStatusBar();
            }
        }
    }

    @Override
    protected IClientApplication getApplication() {
        return env.getApplication();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }
    
    
}
