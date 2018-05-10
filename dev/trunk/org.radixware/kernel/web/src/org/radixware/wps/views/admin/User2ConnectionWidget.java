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
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.Grid.Row;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.RwtAction;

public class User2ConnectionWidget extends VerticalBox {

    private final WpsEnvironment env;
    private final Grid users2ConnectionContainer;
    private final ConnectionsTableWidget connectionsTableWidget;
    private final RwtAction createUser2ConnectionAction;
    private final RwtAction deleteUser2ConnectionAction;
    private final MessageProvider mp;

    public User2ConnectionWidget(WpsEnvironment env, ConnectionsTableWidget connectionsTableWidget) {
        super();
        this.env = env;
        mp = env.getMessageProvider();
        this.connectionsTableWidget = connectionsTableWidget;
        users2ConnectionContainer = new Grid();
        createUser2ConnectionAction = new RwtAction(env, ClientIcon.CommonOperations.CREATE);
        deleteUser2ConnectionAction = new RwtAction(env, ClientIcon.CommonOperations.DELETE);
        setupUI();
    }

    private void setupUI() {
        
        users2ConnectionContainer.setSelectionMode(Grid.SelectionMode.ROW);
        users2ConnectionContainer.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        users2ConnectionContainer.addColumn(mp.translate("AdminPanel", "User"));
        users2ConnectionContainer.addColumn(mp.translate("AdminPanel", "Connection"));
        users2ConnectionContainer.getColumn(1).setSizePolicy(IGrid.EColumnSizePolicy.STRETCH);
        users2ConnectionContainer.addCurrentRowListener(new Grid.CurrentRowListener() {

            @Override
            public void currentRowChanged(Row oldRow, Row newRow) {
                deleteUser2ConnectionAction.setEnabled(newRow != null);
            }

            @Override
            public boolean beforeChangeCurrentRow(Row oldRow, Row newRow) {
                return true;
            }
        });
        ToolBar users2ConnectionToolBar = new ToolBar();
        createUser2ConnectionAction.setToolTip(mp.translate("AdminPanel", "Create link"));
        createUser2ConnectionAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                List<String> userNamesList = new LinkedList<>();
                for (int i = 0; i < users2ConnectionContainer.getRowCount(); i++) {
                    userNamesList.add((String) users2ConnectionContainer.getRow(i).getCell(0).getValue());
                }

                CreateUserDialog createUserDlg = new CreateUserDialog(env, connectionsTableWidget.getConnectionsNames(), userNamesList);
                if (createUserDlg.execDialog() == IDialog.DialogResult.ACCEPTED) {
                    Grid.Row row = users2ConnectionContainer.addRow();
                    row.getCell(0).setValue(createUserDlg.getUserName());
                    row.getCell(1).setValue(createUserDlg.getConnection());
                }
            }
        });
        users2ConnectionToolBar.addAction(createUser2ConnectionAction);
        deleteUser2ConnectionAction.setToolTip(mp.translate("AdminPanel", "Delete link"));
        deleteUser2ConnectionAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                Grid.Row row = users2ConnectionContainer.getCurrentRow();
                if (row != null) {
                    users2ConnectionContainer.removeRow(row);
                }
            }
        });
        users2ConnectionToolBar.addAction(deleteUser2ConnectionAction);
        reread();
        add(users2ConnectionToolBar);
        add(users2ConnectionContainer);
    }

    public void reread() {
        users2ConnectionContainer.clearRows();
        updateConnectionsEditMask();
        users2ConnectionContainer.getColumn(1).getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);
        
        for (String userName : connectionsTableWidget.getConnections().getLinkedUserNames()) {
            Grid.Row row = users2ConnectionContainer.addRow();
            row.getCell(0).setValue(userName);
            row.getCell(1).setValue(connectionsTableWidget.getConnections().getConnectionNameByUserName(userName)); //Мб ситуация когда в у линка задан ConnectionName, но в элементе Connections нет Connection-а с таким именем
        }
        deleteUser2ConnectionAction.setEnabled(false);
        createUser2ConnectionAction.setEnabled(connectionsTableWidget.getRowCount() != 0);
    }

    public boolean validate() {
        List<String> connectionNames = connectionsTableWidget.getConnectionsNames();
        for (int i = 0; i < users2ConnectionContainer.getRowCount(); i++) {
            String connectionName = (String) users2ConnectionContainer.getRow(i).getCell(1).getValue();
            if (!connectionNames.contains(connectionName)) {
                env.messageWarning(mp.translate("AdminPanel", "Invalid value"), mp.translate("AdminPanel", "There is a user with invalid connection ") + connectionName);
                return false;
            }
        }
        return true;
    }

    public boolean isExistUserWithSuchConnection(String connectionName) {
        boolean isUserWithSuchConnExists = false;
        for (int i = 0; i < users2ConnectionContainer.getRowCount(); i++) {
            if (connectionName.equals(users2ConnectionContainer.getRow(i).getCell(1).getValue())) {
                isUserWithSuchConnExists = true;
            }
        }
        return isUserWithSuchConnExists;
    }

    Map<String, String> getUserMap() {
        Map<String, String> user2Connection = new HashMap<>();
        for (int i = 0; i < users2ConnectionContainer.getRowCount(); i++) {
            Row row = users2ConnectionContainer.getRow(i);
            user2Connection.put((String) row.getCell(0).getValue(), (String) row.getCell(1).getValue());
        }
        return user2Connection;
    }

    void renameConnection(String oldName, String newName) {
        EditMaskList users2ConnectionEditMask = (EditMaskList) users2ConnectionContainer.getColumn(1).getEditingOptions().getEditMask();
        if (users2ConnectionEditMask != null) {
            users2ConnectionEditMask.removeItem(oldName);
            users2ConnectionEditMask.addItem(newName, newName);
            users2ConnectionContainer.getColumn(1).getEditingOptions().setEditMask(users2ConnectionEditMask);
            for (int i = 0; i < users2ConnectionContainer.getRowCount(); i++) {
                Grid.Cell cell = users2ConnectionContainer.getRow(i).getCell(1);
                if (oldName.equals(cell.getValue())) {
                    cell.setValue(newName);
                }
            }
        }
    }

    void removeConnectionFromList(String connectionName) {
        EditMaskList users2ConnectionEditMask = (EditMaskList) users2ConnectionContainer.getColumn(1).getEditingOptions().getEditMask();
        if (users2ConnectionEditMask != null) {
            users2ConnectionEditMask.removeItem(connectionName);
            users2ConnectionContainer.getColumn(1).getEditingOptions().setEditMask(users2ConnectionEditMask);
            if (users2ConnectionEditMask.getItems().isEmpty()) {
                createUser2ConnectionAction.setEnabled(false);
            }
        }
    }
    
    void updateConnectionsEditMask() {
        EditMaskList users2ConnectionEditMask = new EditMaskList();
        boolean enableCreateUser2ConnectionAction = false;
        for (String name : connectionsTableWidget.getConnectionsNames()) {
            users2ConnectionEditMask.addItem(name, name);
            enableCreateUser2ConnectionAction = true;
        }
        users2ConnectionContainer.getColumn(1).getEditingOptions().setEditMask(users2ConnectionEditMask);
        createUser2ConnectionAction.setEnabled(enableCreateUser2ConnectionAction);
    }
}
