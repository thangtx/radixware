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

import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.Connections;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.HorizontalBoxContainer;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.Splitter;
import org.radixware.wps.rwt.VerticalBox;

public class ConnectionsWidget extends VerticalBox {

    private final MessageProvider mp;
    private final User2ConnectionWidget user2ConnectionWidget;
    private final ConnectionsTableWidget connectionsTableWidget;
    private final ConnectionsPropertiesWidget connectionsPropertiesWidget;

    public ConnectionsWidget(WpsEnvironment env) {
        super();
        mp = env.getMessageProvider();
        connectionsTableWidget = new ConnectionsTableWidget(env);
        connectionsPropertiesWidget = new ConnectionsPropertiesWidget(env, connectionsTableWidget);
        user2ConnectionWidget = new User2ConnectionWidget(env, connectionsTableWidget);
        setupUI();
    }

    private void setupUI() {
        AbstractContainer fileNameLblContainer = createLabelObject(mp.translate("AdminPanel", "Connections file: ") + ((WpsEnvironment) getEnvironment()).getRunParams().getConnectionsFile());
        add(fileNameLblContainer);

        Splitter mainSplitter = new Splitter();
        mainSplitter.setOrientation(Splitter.Orientation.HORIZONTAL);
        mainSplitter.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        VerticalBox connectionsVB = new VerticalBox();
        AbstractContainer connectionOptionsLblContainer = createLabelObject(mp.translate("AdminPanel", "Connection Options"));
        connectionsVB.add(connectionOptionsLblContainer);
        connectionsVB.addSpace(5);
        Splitter connectionsSplitter = new Splitter();
        connectionsSplitter.setOrientation(Splitter.Orientation.VERTICAL);
        connectionsSplitter.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        connectionsSplitter.setRatioSettingKey(null);
        connectionsSplitter.add(connectionsTableWidget);
        connectionsSplitter.add(connectionsPropertiesWidget);

        connectionsTableWidget.addCurrentRowListener(new Grid.CurrentRowListener() {

            @Override
            public void currentRowChanged(Grid.Row oldRow, Grid.Row newRow) {
                ConnectionOptions currRowConnectionOptions = connectionsTableWidget.getConnectionForRow(newRow);
                if (currRowConnectionOptions != null) {
                    boolean isDefaultConnection = currRowConnectionOptions.getName().equals(connectionsTableWidget.getConnections().getDefaultConnectionName());
                    connectionsPropertiesWidget.refresh(currRowConnectionOptions, connectionsTableWidget.getConnectionsNames(), isDefaultConnection);
                    connectionsPropertiesWidget.getHtml().setCss("display", "block");
                } else {
                    connectionsPropertiesWidget.getHtml().setCss("display", "none");
                }
            }

            @Override
            public boolean beforeChangeCurrentRow(Grid.Row oldRow, Grid.Row newRow) {
                return true;
            }
        });

        connectionsTableWidget.addConnectionNameChangedListener(new ConnectionsTableWidget.ConnectionNameChangedListener() {

            @Override
            public boolean onConnectionNameChanged(String oldName, String newName) {
                user2ConnectionWidget.renameConnection(oldName, newName);
                List<String> connectionNames = connectionsTableWidget.getConnectionsNames();
                connectionsPropertiesWidget.setConnectionNames(connectionNames);
                return true;
            }
        });

        connectionsTableWidget.addBeforeDeleteConnectionListener(new ConnectionsTableWidget.BeforeDeleteConnectionListener() {

            @Override
            public boolean beforeDeleteConnection(String name) {
                return user2ConnectionWidget.isExistUserWithSuchConnection(name);
            }
        });

        connectionsTableWidget.addDeleteConnectionListener(new ConnectionsTableWidget.DeleteConnectionListener() {

            @Override
            public boolean onDeleteConnection(ConnectionOptions oldConnection, ConnectionOptions newConnection) {
                user2ConnectionWidget.removeConnectionFromList(oldConnection.getName());
                return true;
            }
        });

        connectionsTableWidget.addCreateConnectionListener(new ConnectionsTableWidget.CreateConnectionListener() {

            @Override
            public boolean onCreateConnection(ConnectionOptions newConnection) {
                connectionsPropertiesWidget.getHtml().setCss("display", "block");
                user2ConnectionWidget.updateConnectionsEditMask();
                return true;
            }
        });

        connectionsPropertiesWidget.addConnectionOptionsChangedListener(new ConnectionsPropertiesWidget.ConnectionOptionsChangedListener() {

            @Override
            public void onConnectionOptionsChanged(ConnectionOptions connectionOptions) {
                connectionsTableWidget.fillCurrentRowFromConnectionOptions(connectionOptions);
            }
        });
        
        connectionsPropertiesWidget.addDefaultConnectionChangedListener(new ConnectionsPropertiesWidget.DefaultConnectionChangedListener() {

            @Override
            public void onDefaultConnectionChanged(boolean newValue) {
                connectionsTableWidget.setCurrentConnectionDefault(newValue);
            }
        });

        if (connectionsTableWidget.getRowCount() != 0) {
            connectionsTableWidget.setCurrentCell(connectionsTableWidget.getRow(0).getCell(0));
        } else {
            connectionsPropertiesWidget.getHtml().setCss("display", "none");
        }

        connectionsVB.add(connectionsSplitter);
        mainSplitter.add(connectionsVB);

        VerticalBox usersVB = new VerticalBox();
        AbstractContainer connectionToUserLblContainer = createLabelObject(mp.translate("AdminPanel", "Connection to user linkage"));
        usersVB.add(connectionToUserLblContainer);
        usersVB.addSpace(5);
        usersVB.add(user2ConnectionWidget);
        mainSplitter.add(usersVB);
        add(mainSplitter);
        HorizontalBoxContainer bottomBox = new HorizontalBoxContainer();
        bottomBox.setHeight(20);
        bottomBox.setAlignment(Alignment.CENTER);
        PushButton applyButton = new PushButton(mp.translate("AdminPanel", "Apply"));
        applyButton.getHtml().setCss("margin-left", "5px");
        applyButton.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                apply();
            }
        });
        applyButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Dialog.BUTTON_OK));
        bottomBox.add(applyButton);
        PushButton revertButton = new PushButton(mp.translate("AdminPanel", "Revert"));
        revertButton.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                fillTableFromFile();
            }
        });
        revertButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.UNDO));
        bottomBox.addSpace(15);
        bottomBox.add(revertButton);
        addSpace(10);
        add(bottomBox);
        addSpace(10);
    }

    private AbstractContainer createLabelObject(String innerText) {
        Div label = new Div();
        label.setInnerText(innerText);
        AbstractContainer labelContainer = new AbstractContainer(label);
        labelContainer.getHtml().setCss("border-bottom", "1px solid grey");
        labelContainer.getHtml().setCss("text-align", "center");
        return labelContainer;
    }

    private void fillTableFromFile() {
        connectionsTableWidget.fillTableFromFile();
        user2ConnectionWidget.reread();
        if (connectionsTableWidget.getRowCount() != 0) {
            connectionsTableWidget.setCurrentCell(connectionsTableWidget.getRow(0).getCell(0));
        } else {
            connectionsPropertiesWidget.getHtml().setCss("display", "none");
        }
    }

    private void apply() {
        if (user2ConnectionWidget.validate()) {
            Connections connections = connectionsTableWidget.getConnections();
            for (ConnectionOptions connectionOptions : connections.getConnections()) {
                if (connectionOptions.getKerberosOptions() != null) {
                    if (connectionOptions.getKerberosOptions().getServicePrincipalName() == null || connectionOptions.getKerberosOptions().getServicePrincipalName().isEmpty()) {
                        connectionOptions.setKerberosOptions(null);
                    }
                }
            }
            Map<String, String> user2Connection = user2ConnectionWidget.getUserMap();
            connections.setUser2connection(user2Connection);
            connections.store();
        }
    }
}
