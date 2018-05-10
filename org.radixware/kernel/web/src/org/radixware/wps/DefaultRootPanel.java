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

import java.util.List;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.eas.CreateSessionRs;
import org.radixware.wps.dialogs.AboutDialog;
import org.radixware.wps.dialogs.ChangePasswordDialog;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Banner;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.MainStatusBar;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.RwtMenu;
import org.radixware.wps.rwt.RwtMenuBar;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.settings.SettingsDialog;
import org.radixware.wps.tree.TreeManager;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.decorations.ProductStartPage;


public class DefaultRootPanel extends RootPanel {

    private final static class Icons extends ClientIcon.CommonOperations {
        
        public final static Icons APPEARANCE_SETTINGS = new Icons("classpath:images/appearance_settings.svg");

        private Icons(final String fileName) {
            super(fileName, true);
        }       
    }    
    
    private final WpsEnvironment env;    
    private final Action changePasswordAction;
    private final Action checkForUpdatesAction;
    private final Action appearanceSettingsAction;
    private final Action showAboutDialogAction;    
    private final Banner banner;
    private boolean isFirstShown = true;
    private MainView mainView;
    private UIObject startPage;
    private final VerticalBox layout;
    private MainStatusBar statusBar;
    private RwtMenuBar menuBar;
    private IMenu serverMenu, optionsMenu, helpMenu;
    private ConnectionToolBar connectionToolBar;

    public DefaultRootPanel(final WpsEnvironment env) {
        super();
        this.env = env;
        layout = new VerticalBox();
        
        changePasswordAction = createAction(ClientIcon.Connection.KEY, "change_password", new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                changePasswordAction();
            }
        });
        changePasswordAction.setVisible(false);
        
        checkForUpdatesAction = createAction(ClientIcon.CommonOperations.REFRESH, "update_version", new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                env.checkForUpdates();
            }
        });
        
        appearanceSettingsAction = new RwtAction(env, Icons.APPEARANCE_SETTINGS);
        appearanceSettingsAction.setEnabled(false);
        appearanceSettingsAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                runAppearanceSettingsDialog();
            }
        });        
        
        showAboutDialogAction = createAction(ClientIcon.Dialog.ABOUT, "about", new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                final AboutDialog dialog = new AboutDialog(env);
                dialog.execDialog();
            }
        });        
                
        final Banner.Options bannerOptions = env.getRunParams().getBannerOptions();
        if (bannerOptions==null) {
            banner = null;
        }else{
            banner = new Banner(bannerOptions);
            layout.add(banner);
        }
                
        
        recreateMenuBar();
        layout.add(createConnectionToolBar());
        connectionToolBar.afterDisconnect();
        startPage = new ProductStartPage();
        layout.add(startPage);
        add(layout);
        recreateStatusBar();
    }
    
    private ToolBar createConnectionToolBar(){
        connectionToolBar = new ConnectionToolBar(env);
        initMainMenu(menuBar);
        return connectionToolBar;
    }
    
    @Override
    public void updateTranslations() {
        final MessageProvider mp = env.getMessageProvider();
        connectionToolBar.updateTranslations(mp);
        changePasswordAction.setToolTip(mp.translate("EnvironmentAction", "Change Password..."));
        changePasswordAction.setText(mp.translate("EnvironmentAction", "Change Password..."));
        checkForUpdatesAction.setToolTip(mp.translate("EnvironmentAction", "Check for Updates"));
        checkForUpdatesAction.setText(mp.translate("EnvironmentAction", "Check for Updates"));
        appearanceSettingsAction.setToolTip(mp.translate("EnvironmentAction", "Appearance Settings"));
        appearanceSettingsAction.setText(mp.translate("EnvironmentAction", "Appearance Settings"));
        if (serverMenu!=null){
            serverMenu.setTitle(mp.translate("WpsMenuBar", "Server"));
        }
        if (optionsMenu!=null){
            optionsMenu.setTitle(mp.translate("WpsMenuBar", "Options"));
        }
        if (helpMenu!=null){
            helpMenu.setTitle(mp.translate("MainMenu", "Help"));
        }
        showAboutDialogAction.setText(mp.translate("EnvironmentAction", "About"));
    }    

    private void recreateStatusBar() {
        statusBar = new MainStatusBar();
        statusBar.setHeight(20);
        statusBar.getHtml().setCss("border-top", "solid 1px #BBB");
        layout.add(statusBar);
        statusBar.setText(env.getMessageProvider().translate("StatusBar", "Disconnected"));
        statusBar.add(new TraceTrayItem(env));
        statusBar.addSpace();
    }    

    private void recreateMenuBar() {
        menuBar = new RwtMenuBar(this);
        menuBar.setHeight(20);
        menuBar.getHtml().setCss("border-top", "solid 1px #BBB");
        layout.add(menuBar);        
    }
    
    private void initMainMenu(RwtMenuBar menuBar) {
        final MessageProvider mp = env.getMessageProvider();
        serverMenu = (RwtMenu) menuBar.addSubMenu(mp.translate("MenuBar", "Server"));
        serverMenu.addAction(connectionToolBar.getConnectAction());
        serverMenu.addAction(connectionToolBar.getDisconnectAction());
        serverMenu.addAction(changePasswordAction);
        serverMenu.addSubSeparator();
        serverMenu.addAction(env.showTraceAction);
        if (RunParams.isDevelopmentMode()){
            serverMenu.addAction(checkForUpdatesAction);
        }
        ((TreeManager)env.getTreeManager()).setupMainMenu(menuBar);
        optionsMenu = (RwtMenu) menuBar.addSubMenu(mp.translate("WpsMenuBar", "Options"));
        optionsMenu.addAction(appearanceSettingsAction);
        helpMenu = menuBar.addSubMenu(mp.translate("MainMenu", "Help"));
        helpMenu.addAction(showAboutDialogAction);
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
            } catch (HttpSessionTerminatedError ex) {
                break;
            } catch (Throwable e) {
                env.messageException("Error", e.getMessage(), e);
            }
        }
    }

    @Override
    public IMainView getExplorerView() {
        if (mainView == null) {
            mainView = new MainView(env);
            closeStatusBar();
            clearContent();
            recreateMenuBar();
            initMainMenu(menuBar);
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
            closeStatusBar();
            clearContent();
            mainView = null;
            if (!env.context.wasDisposed()) {
                recreateMenuBar();
                layout.add(createConnectionToolBar());
                startPage = new ProductStartPage();
                layout.add(startPage);
                recreateStatusBar();
                updateTranslations();
            }
        }
    }

    @Override
    public IMainStatusBar getMainStatusBar() {
        return statusBar;
    }

    @Override
    public void cleanup(boolean forced) {
        connectionToolBar.afterDisconnect();
        changePasswordAction.setVisible(false);
        appearanceSettingsAction.setEnabled(false);
    }

    @Override
    public void onConnected(final CreateSessionRs response) {        
        if (!response.isSetCanChangePassword() || response.getCanChangePassword()) {
            changePasswordAction.setVisible(true);
        }
        connectionToolBar.afterConnect();
        {
            final List<UIObject> children = statusBar.getItems();
            TraceTrayItem traceTrayItem;
            for (UIObject child : children) {
                if (child instanceof TraceTrayItem) {
                    traceTrayItem = ((TraceTrayItem) child);
                    if (!traceTrayItem.isVisible()) {
                        traceTrayItem.setVisible(true);
                        traceTrayItem.setIcon(getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.DEBUG));
                    }
                    break;
                }
            }
        }
        appearanceSettingsAction.setEnabled(true);        
    }
    
    
    
    private void closeStatusBar(){
        if (statusBar != null) {
            final List<UIObject> children = statusBar.getItems();
            for (UIObject child : children) {
                if (child instanceof ITrayItem) {
                    ((ITrayItem) child).onTrayClose();
                }
            }
            statusBar = null;
        }        
    }    

    private void clearContent() {
        final List<UIObject> children = layout.getChildren();
        for (UIObject child : children) {
            if (child != banner) {
                layout.remove(child);
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

    private RwtAction createAction(final ClientIcon icon, final String name, final Action.ActionListener listener){
        final RwtAction action = new RwtAction(env, icon);
        action.setObjectName(name);
        if (listener!=null){
            action.addActionListener(listener);
        }
        return action;
    }    
        
    private void changePasswordAction() {
        final MessageProvider mp = env.getMessageProvider();
        final ChangePasswordDialog dialog = new ChangePasswordDialog(env);
        {
            final String title = mp.translate("ChangePasswordDialog", "Change Password for '%s' Account");
            dialog.setTitle(String.format(title, env.getUserName()));
        }
        while (dialog.execDialog() == DialogResult.ACCEPTED) {
            try {
                env.getEasSession().changePassword(dialog.getOldPassword(), dialog.getNewPassword());
                final String title = mp.translate("ExplorerMessage", "Success!");
                final String message = mp.translate("ExplorerMessage", "Your password was successfully updated!");
                env.messageInformation(title, message);
                return;
            } catch (InterruptedException ex) {
            } catch (ServiceCallFault fault) {
                final String title = mp.translate("ChangePasswordDialog", "Can`t Change Password");
                if (org.radixware.schemas.eas.ExceptionEnum.INVALID_PASSWORD.toString().equals(fault.getFaultString())) {
                    final String message = mp.translate("ChangePasswordDialog", "Current password is not correct");
                    env.messageError(title, message);
                } else {
                    env.processException(title, fault);
                    if (!ClientException.isSpecialFault(fault)) {
                        return;
                    }
                }
            } catch (ServiceClientException ex) {
                env.processException(ex);
            } finally {
                dialog.clear();
            }
        }
    }    
    
    private void runAppearanceSettingsDialog() {
        final SettingsDialog dialog = new SettingsDialog(env);
        dialog.execDialog();
    }
}
