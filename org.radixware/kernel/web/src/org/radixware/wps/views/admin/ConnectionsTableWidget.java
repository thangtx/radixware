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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.Connections;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.Grid.Row;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.text.WpsFont;
import org.radixware.wps.views.RwtAction;

public class ConnectionsTableWidget extends VerticalBox {

    private final static int NAME_COL = 0;
    private final static int SERVERS_COL = 1;
    private final static int STATION_COL = 2;
    private final WpsEnvironment env;
    private final MessageProvider mp;
    private final Grid connectionsContainer = new Grid();
    private final Map<String, ConnectionOptions> connectionOptionsMap = new HashMap<>();
    private final List<ConnectionNameChangedListener> connectionNameChangedListenerList = new LinkedList<>();
    private final List<BeforeDeleteConnectionListener> beforeDeleteConnectionListenerList = new LinkedList<>();
    private final List<DeleteConnectionListener> deleteConnectionListenerList = new LinkedList<>();
    private final List<CreateConnectionListener> createConnectionListenerList = new LinkedList<>();
    private Connections connections;

    public static abstract class ConnectionNameChangedListener {

        public abstract boolean onConnectionNameChanged(String oldName, String newName);
    }

    public static abstract class BeforeDeleteConnectionListener {

        public abstract boolean beforeDeleteConnection(String name);
    }

    public static abstract class DeleteConnectionListener {

        public abstract boolean onDeleteConnection(ConnectionOptions oldConnection, ConnectionOptions newConnection);
    }

    public static abstract class CreateConnectionListener {

        public abstract boolean onCreateConnection(ConnectionOptions newConnection);
    }

    public ConnectionsTableWidget(WpsEnvironment env) {
        super();
        this.env = env;
        mp = env.getMessageProvider();
        connections = env.getConnections();
        setupUI();
    }

    private void setupUI() {
        connectionsContainer.setSelectionMode(Grid.SelectionMode.ROW);
        connectionsContainer.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        connectionsContainer.addColumn(mp.translate("AdminPanel", "Name"));
        connectionsContainer.addColumn(mp.translate("AdminPanel", "Servers"));
        connectionsContainer.addColumn(mp.translate("AdminPanel", "Station"));
        connectionsContainer.getColumn(NAME_COL).setSizePolicy(IGrid.EColumnSizePolicy.BY_CONTENT);
        connectionsContainer.getColumn(SERVERS_COL).setSizePolicy(IGrid.EColumnSizePolicy.STRETCH);
        
        ToolBar connectionToolBar = new ToolBar();
        final RwtAction deleteConnectionAction = new RwtAction(env, ClientIcon.CommonOperations.DELETE);
        final RwtAction dublicateAction = new RwtAction(env, ClientIcon.Selector.CLONE);
        connectionsContainer.addCurrentCellListener(new Grid.CurrentCellListener() {

            @Override
            public void currentCellChanged(Grid.Cell oldCell, Grid.Cell newCell) {
                deleteConnectionAction.setEnabled(newCell != null);
                dublicateAction.setEnabled(newCell != null);
            }

            @Override
            public boolean beforeChangeCurrentCell(Grid.Cell oldCell, Grid.Cell newCell) {
                return true;
            }
        });
        
        RwtAction createConnectionAction = new RwtAction(env, ClientIcon.CommonOperations.CREATE);
        createConnectionAction.setToolTip(mp.translate("ConnectionsDialog", "Create Connection"));
        createConnectionAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                ConnectionOptions connectionOptions = connections.create();
                if (connectionOptions != null) {
                    Grid.Row row = connectionsContainer.addRow();
                    row.getCell(NAME_COL).setValue(connectionOptions.getName());
                    row.getCell(SERVERS_COL).setValue(connectionOptions.getInitialAddressesAsStr());
                    connectionOptionsMap.put(connectionOptions.getName(), connectionOptions);
                    Grid.Cell prevCell = connectionsContainer.getCurrentCell();
                    connectionsContainer.setCurrentCell(row.getCell(prevCell == null ? 0 : prevCell.getCellIndex()));
                    for (CreateConnectionListener createConnectionListener : createConnectionListenerList) {
                        createConnectionListener.onCreateConnection(connectionOptions);
                    }
                }
            }
        });

        connectionToolBar.addAction(createConnectionAction);

        dublicateAction.setToolTip(mp.translate("ConnectionsDialog", "Create a Copy of Connection"));
        dublicateAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                Grid.Row currentRow = connectionsContainer.getCurrentRow();
                ConnectionOptions connectionOptionsSourse = connectionOptionsMap.get((String) currentRow.getCell(NAME_COL).getValue());
                ConnectionOptions connectionOptions = connections.createCopy(connectionOptionsSourse);
                if (connectionOptions != null) {
                    if (connectionOptions.edit(connections.getConnectionNames())) {
                        connections.add(connectionOptions);
                        Grid.Row row = connectionsContainer.addRow();
                        row.getCell(NAME_COL).setValue(connectionOptions.getName());
                        row.getCell(SERVERS_COL).setValue(connectionOptions.getInitialAddressesAsStr());
                        row.getCell(STATION_COL).setValue(connectionOptions.getStationName());
                        connectionOptionsMap.put(connectionOptions.getName(), connectionOptions);
                        Grid.Cell prevCell = connectionsContainer.getCurrentCell();
                        connectionsContainer.setCurrentCell(row.getCell(prevCell == null ? 0 : prevCell.getCellIndex()));
                        for (CreateConnectionListener createConnectionListener : createConnectionListenerList) {
                            createConnectionListener.onCreateConnection(connectionOptions);
                        }
                    }
                }
            }
        });
        connectionToolBar.addAction(dublicateAction);
        deleteConnectionAction.setToolTip(mp.translate("ConnectionsDialog", "Delete Connection"));
        deleteConnectionAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                Grid.Row currentRow = connectionsContainer.getCurrentRow();
                String currentConnectionName = (String) currentRow.getCell(NAME_COL).getValue();
                String conformationString = mp.translate("ExplorerMessage", "Do you really want to delete connection %s");
                if (env.messageConfirmation(mp.translate("ConnectionsDialog", "Delete Connection"), String.format(conformationString, currentConnectionName))) {
                    for (BeforeDeleteConnectionListener listener : beforeDeleteConnectionListenerList) {
                        if (listener.beforeDeleteConnection(currentConnectionName)) {
                            env.messageError(mp.translate("AdminPanel", "Can't delete connection"), mp.translate("AdminPanel", "A user with such connection exists. Please delete this user or change it's connection parameter"));
                            return;
                        }
                    }
                    int rowIndex = connectionsContainer.getRowIndex(currentRow);
                    int currentCellIndex = connectionsContainer.getCurrentCell().getCellIndex();
                    if (rowIndex < connectionsContainer.getRowCount() - 1) {
                        Grid.Cell newCell = connectionsContainer.getRow(rowIndex + 1).getCell(currentCellIndex);
                        connectionsContainer.setCurrentCell(newCell);
                    } else if (rowIndex != 0) {
                        Grid.Cell newCell = connectionsContainer.getRow(rowIndex - 1).getCell(currentCellIndex);
                        connectionsContainer.setCurrentCell(newCell);
                    }
                    connectionsContainer.removeRow(currentRow);
                    if (connectionsContainer.getRowCount() != 0) {
                        for (DeleteConnectionListener deleteConnectionListener : deleteConnectionListenerList) {
                            deleteConnectionListener.onDeleteConnection(connectionOptionsMap.get(currentConnectionName),
                                    connectionOptionsMap.get(connectionsContainer.getCurrentRow().getCell(NAME_COL).getValue()));
                        }
                    } else {
                        for (DeleteConnectionListener deleteConnectionListener : deleteConnectionListenerList) {
                            deleteConnectionListener.onDeleteConnection(connectionOptionsMap.get(currentConnectionName), null);
                        }
                    }
                    connections.remove(currentConnectionName);
                    connectionOptionsMap.remove(currentConnectionName);
                }
            }
        });
        connectionToolBar.addAction(deleteConnectionAction);
        add(connectionToolBar);
        add(connectionsContainer);
        fillTableFromFile();
    }

    public void fillTableFromFile() {
        connectionsContainer.clearRows();
        connectionOptionsMap.clear();
        connections = env.getConnections();
        String defaultConnectionName = connections.getDefaultConnectionName();
        for (ConnectionOptions connectionOptions : connections) {
            Grid.Row row = connectionsContainer.addRow();
            row.getCell(NAME_COL).setValue(connectionOptions.getName());
            if (defaultConnectionName != null && connectionOptions.getName().equals(defaultConnectionName)) {
                row.getCell(NAME_COL).getHtml().setCss("font-weight", "bold");
            }
            row.getCell(SERVERS_COL).setValue(connectionOptions.getInitialAddressesAsStr());
            row.getCell(STATION_COL).setValue(connectionOptions.getStationName());
            connectionOptionsMap.put(connectionOptions.getName(), connectionOptions);
        }
    }

    public boolean fillCurrentRowFromConnectionOptions(ConnectionOptions options) {
        Row row = connectionsContainer.getCurrentRow();
        if (row != null && options != null) {
            String oldConnectionName = (String) row.getCell(NAME_COL).getValue();
            String name = options.getName();
            row.getCell(NAME_COL).setValue(name);
            options.setName(name);
            connectionOptionsMap.remove(oldConnectionName);
            connectionOptionsMap.put(name, options);
            for (ConnectionNameChangedListener connectionNameChangedListener : connectionNameChangedListenerList) {
                connectionNameChangedListener.onConnectionNameChanged(oldConnectionName, name);
            }
            row.getCell(SERVERS_COL).setValue(options.getInitialAddressesAsStr());
            row.getCell(STATION_COL).setValue(options.getStationName());
        }
        return true;
    }

    public void addCurrentRowListener(Grid.CurrentRowListener listener) {
        connectionsContainer.addCurrentRowListener(listener);
    }

    public ConnectionOptions getConnectionForRow(Row row) {
        return row == null ? null : connectionOptionsMap.get((String) row.getCell(0).getValue());
    }

    public Connections getConnections() {
        return connections;
    }

    int getRowCount() {
        return connectionsContainer.getRowCount();
    }

    void setCurrentCell(Grid.Cell cell) {
        connectionsContainer.setCurrentCell(cell);
    }
    
    void setCurrentConnectionDefault(boolean isDefault) {
        if (isDefault) {
            for (int i = 0; i < connectionsContainer.getRowCount(); i++) {
                String fontWeight = connectionsContainer.getRow(i).getCell(NAME_COL).getHtml().getCss("font-weight");
                if ("bold".equals(fontWeight)) {
                    connectionsContainer.getRow(i).getCell(NAME_COL).getHtml().setCss("font-weight", "normal");
                }
            }
            connections.setDefaultConnectionName(getCurrentRowConnectionName());
            connectionsContainer.getCurrentRow().getCell(NAME_COL).getHtml().setCss("font-weight", "bold");
        } else {
            connectionsContainer.getCurrentRow().getCell(NAME_COL).getHtml().setCss("font-weight", "normal");
            connections.setDefaultConnectionName(null);
        }
    }

    Row getRow(int row) {
        return connectionsContainer.getRow(row);
    }

    String getCurrentRowConnectionName() {
        return (String) connectionsContainer.getCurrentRow().getCell(NAME_COL).getValue();
    }

    List<String> getConnectionsNames() {
        List<String> list = new LinkedList<>();
        for (int i = 0; i < connectionsContainer.getRowCount(); i++) {
            String connectionName = (String) connectionsContainer.getRow(i).getCell(NAME_COL).getValue();
            list.add(connectionName);
        }
        return list;
    }

    public void addConnectionNameChangedListener(ConnectionNameChangedListener listener) {
        connectionNameChangedListenerList.add(listener);
    }

    public void addBeforeDeleteConnectionListener(BeforeDeleteConnectionListener listener) {
        beforeDeleteConnectionListenerList.add(listener);
    }

    public void addDeleteConnectionListener(DeleteConnectionListener listener) {
        deleteConnectionListenerList.add(listener);
    }

    public void addCreateConnectionListener(CreateConnectionListener listener) {
        createConnectionListenerList.add(listener);
    }
}
