/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.views.admin;

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.HorizontalBox;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.UIObject;


public final class AdminPanel extends RootPanel{
    
    private final WpsEnvironment environment;
    private final ListBox.ListBoxItem configWebParams = new ListBox.ListBoxItem();
    private final ListBox.ListBoxItem configConnectionParams = new ListBox.ListBoxItem();
    private final ListBox.ListBoxItem serverInformation = new ListBox.ListBoxItem();
    private ConnectionsWidget connectionsWidget;
    private WSConfigWidget configWidget;
    private ServerInformationWidget serverInformationWidget;
    private UIObject currentWidget;
    private static class Icon extends ClientIcon.CommonOperations{
        
        public static final Icon WEB_SERVER_PARAMS = new Icon("classpath:images/web_server_parameters.svg") {};
        
        private Icon(final String fileName){
            super(fileName);
        }
        
    } 
    
    public AdminPanel(final WpsEnvironment env) {
        super();
        environment = env;
        MessageProvider mp = env.getMessageProvider();
        final HorizontalBox layout = new HorizontalBox();
        ListBox listBox = new ListBox();
        listBox.setWidth(150);
        serverInformation.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Dialog.ABOUT));
        serverInformation.setText(mp.translate("AdminPanel", "Server information"));
        serverInformation.setIconSize(128, 128);
        listBox.add(serverInformation);
        configConnectionParams.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Dialog.CONNECTIONS_MANAGER));
        configConnectionParams.setText(mp.translate("AdminPanel", "Connection options"));
        configConnectionParams.setIconSize(128, 128);
        listBox.add(configConnectionParams);
        configWebParams.setIcon(env.getApplication().getImageManager().getIcon(Icon.WEB_SERVER_PARAMS));
        configWebParams.setIconSize(128, 128);
        configWebParams.setText(mp.translate("AdminPanel", "Configure Web server params"));
        listBox.add(configWebParams);
        for (ListBox.ListBoxItem item : listBox.getItems()) {
            configListBoxItem(item);
            item.getHtml().setCss("outline", "none");
        }
        layout.add(listBox);
        listBox.addCurrentItemListener(new ListBox.CurrentItemListener() {

            @Override
            public void currentItemChanged(ListBox.ListBoxItem currentItem) {
                if (currentItem != null) {
                    if (currentItem.equals(configWebParams)) {
                          if (currentWidget != null) {
                              layout.remove(currentWidget);
                          }
                          if (configWidget == null) {
                              configWidget = new WSConfigWidget(env);
                          }
                          layout.add(configWidget);
                          currentWidget = configWidget;
                    } else if (currentItem.equals(configConnectionParams)) {
                        if (currentWidget != null) {
                            layout.remove(currentWidget);
                        }
                        if (connectionsWidget == null) {
                            connectionsWidget = new ConnectionsWidget(env);
                            connectionsWidget.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
                        }
                        layout.add(connectionsWidget);
                        connectionsWidget.setParent(layout);
                        currentWidget = connectionsWidget;
                    } else if (currentItem.equals(serverInformation)) {
                        if (currentWidget != null) {
                            layout.remove(currentWidget);
                        }
                        if (serverInformationWidget == null) {
                            serverInformationWidget = new ServerInformationWidget(env);
                        }
                        layout.add(serverInformationWidget);
                        currentWidget = serverInformationWidget;
                    } 
                }
            }
        });
        add(layout);
        listBox.setCurrentRow(0);
     }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return environment.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {        
    }

    @Override
    public IMainStatusBar getMainStatusBar() {
        return null;
    }

    @Override
    public void updateTranslations() {
        super.updateTranslations();
    }     
    
    private void configListBoxItem(ListBox.ListBoxItem listBoxItem) {
        Html imgHtml = listBoxItem.getHtml().getChildAt(0);
        imgHtml.setCss("display", "block");
        imgHtml.setCss("margin", "auto");
        Html textHtml = listBoxItem.getHtml().getChildAt(1);
        textHtml.setCss("display", "block");
        textHtml.setCss("text-align", "center");
        textHtml.setCss("font-size", "16px");
        textHtml.setCss("font-weight", "bold");
    }
}
