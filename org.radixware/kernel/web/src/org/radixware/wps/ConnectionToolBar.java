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
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.views.RwtAction;


public final class ConnectionToolBar extends ToolBar {  
    
    private final Action connectAction;
    private final Action disconnectAction;        

    public ConnectionToolBar(final WpsEnvironment env) {
        super();
        connectAction = createAction(env, ClientIcon.Connection.CONNECT, "connect", new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                if (env.getEasSession() == null) {
                    env.checkConnection();
                }
            }
        });
                
        disconnectAction = createAction(env, ClientIcon.Connection.DISCONNECT, "disconnect", new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                final String title
                        = env.getMessageProvider().translate("ExplorerMessage", "Confirm to Disconnect");
                final String message
                        = env.getMessageProvider().translate("ExplorerMessage", "Do you really want to disconnect?");
                if (env.messageConfirmation(title, message)) {
                    env.disconnect();
                }
            }
        });
        addAction(connectAction);
        addAction(disconnectAction);
        setHeight(25);
    }
    
    public Action getConnectAction(){
        return connectAction;
    }
    
    public Action getDisconnectAction(){
        return disconnectAction;
    }

    public void updateNavigationState(List<Action> addActions) {
        this.removeAllActions();
        addAction(connectAction);
        addAction(disconnectAction);        
        for (Action a : addActions) {
            this.addAction(a);
            final IToolButton button = this.getWidgetForAction(a);
            if (button!=null){
                button.setPopupMode(IToolButton.ToolButtonPopupMode.MenuButtonPopup);
            }
        }
    }
    
    public void updateTranslations(final MessageProvider messageProvider){
        connectAction.setToolTip(messageProvider.translate("EnvironmentAction", "Connect..."));
        connectAction.setText(messageProvider .translate("EnvironmentAction", "Connect..."));
        disconnectAction.setToolTip(messageProvider.translate("EnvironmentAction", "Disconnect"));
        disconnectAction.setText(messageProvider.translate("EnvironmentAction", "Disconnect"));        
    }
    
    public void afterConnect(){
        connectAction.setEnabled(false);
        disconnectAction.setEnabled(true);
    }
    
    public void afterDisconnect(){
        connectAction.setEnabled(true);
        disconnectAction.setEnabled(false);        
    }
    
    private static RwtAction createAction(final IClientEnvironment env, 
                                                            final ClientIcon icon, 
                                                            final String name, 
                                                            final Action.ActionListener listener){
        final RwtAction action = new RwtAction(env, icon);
        action.setObjectName(name);
        if (listener!=null){
            action.addActionListener(listener);
        }
        return action;
    }
}
