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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.env.session.Connections;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.schemas.connections.ConnectionsDocument;


public class ConnectionsManager extends ExplorerDialog {

    final private Ui_ConnectionsDialog ui = new Ui_ConnectionsDialog();    
    final private Connections connections;
    final private QEventFilter keyListener = new QEventFilter(this){
            @Override
            public boolean eventFilter(final QObject source, final QEvent event) {
                if (event instanceof QKeyEvent) {
                    final QKeyEvent key = (QKeyEvent) event;
                    if ((key.key() == Qt.Key.Key_Enter.value() || key.key() == Qt.Key.Key_Return.value())) {
                        if (key.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)) {
                            onEditConnection();
                        }

                    }
                }
                return false;
            }        
    };

    public ConnectionsManager(final IClientEnvironment environment, final QWidget parent, final Connections connections) {
        super(environment, parent, null);
        this.connections = connections;
        setupUi();
        for (ConnectionOptions connection : connections) {
            addWidgetItemConnection(connection);
        }
        if (ui.connectionsList.count() > 0) {
            ui.connectionsList.setCurrentRow(0);
        }
    }

    private void setupUi() {
        ui.setupUi(this);
        keyListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress, QEvent.Type.KeyRelease));
        dialogLayout().addWidget(ui.contentsWidget);
        addButton(EDialogButtonType.CLOSE);

        ui.addBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));
        ui.cloneBtn.setIcon(ExplorerIcon.getQIcon(Selector.Icons.CLONE));
        ui.removeBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE));
        ui.editorBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));

        ui.connectionsList.installEventFilter(keyListener);
        
        ui.connectionsList.currentRowChanged.connect(this, "onChangeCurrentRow(int)");
        ui.addBtn.clicked.connect(this, "createConnection()");
        ui.cloneBtn.clicked.connect(this, "onCloneConnection()");
        ui.removeBtn.clicked.connect(this, "onRemoveConnection()");
        ui.editorBtn.clicked.connect(this, "onEditConnection()");
        ui.connectionsList.doubleClicked.connect(this, "onEditConnection()");
        rejectButtonClick.connect(this, "reject()");
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.CONNECTIONS_MANAGER));
        if (connections.size() == 0) {
            ui.editorBtn.setEnabled(false);
            ui.removeBtn.setEnabled(false);
            ui.cloneBtn.setEnabled(false);
        }
    }

    @SuppressWarnings("unused")
    private void onChangeCurrentRow(final int row) {
        if (row < 0 && connections.size() == 0) {
            ui.editorBtn.setEnabled(false);
            ui.removeBtn.setEnabled(false);
            ui.cloneBtn.setEnabled(false);
        } else {
            final ConnectionOptions connection =
                    (ConnectionOptions) ui.connectionsList.currentItem().data(Qt.ItemDataRole.UserRole);
            ui.editorBtn.setEnabled(true);
            ui.cloneBtn.setEnabled(true);
            ui.removeBtn.setEnabled(connection.isLocal());
        }
    }

    public void createConnection() {
        final ConnectionOptions connection = connections.create();
        if (connection != null) {
            addWidgetItemConnection(connection);
            ui.connectionsList.setCurrentRow(ui.connectionsList.count() - 1);
        }
    }

    @SuppressWarnings("unused")
    private void onRemoveConnection() {
        final QListWidgetItem item = ui.connectionsList.currentItem();
        if (item != null) {
            final ConnectionOptions connection =
                    (ConnectionOptions) item.data(Qt.ItemDataRole.UserRole);
            if (connections.remove(connection.getName())){
                ui.connectionsList.takeItem(ui.connectionsList.currentRow());
            }
        }
    }

    @SuppressWarnings("unused")
    private void onCloneConnection() {
        final QListWidgetItem item = ui.connectionsList.currentItem();
        if (item != null) {
            final ConnectionOptions sourceConnection =
                    (ConnectionOptions) item.data(Qt.ItemDataRole.UserRole);
            final ConnectionOptions newConnection = connections.createCopy(sourceConnection);
            final List<String> connectionNames = new ArrayList<>(connections.size());
            for (ConnectionOptions c : connections) {
                connectionNames.add(c.getName());
            }
            if (newConnection.edit(connectionNames)) {
                connections.add(newConnection);
                ui.connectionsList.setCurrentItem(addWidgetItemConnection(newConnection));
            }
        }
    }

    @SuppressWarnings("unused")
    private void onEditConnection() {
        final QListWidgetItem item = ui.connectionsList.currentItem();
        if (item != null) {
            final ConnectionOptions connection =
                    (ConnectionOptions) item.data(Qt.ItemDataRole.UserRole);
            final String oldName = item.text();
            final List<String> connectionNames = new ArrayList<>(connections.size());
            for (ConnectionOptions c : connections) {
                if (!c.getName().equals(oldName)) {
                    connectionNames.add(c.getName());
                }
            }
            if (!connection.isLocal()) {
                final ConnectionOptions copy = new org.radixware.kernel.explorer.env.session.ConnectionOptions(getEnvironment(), connection, null);
                if (copy.edit(connectionNames)) {
                    if (!copy.getName().equals(oldName)) {
                        connections.add(copy);
                        addWidgetItemConnection(copy);
                    } else {
                        connections.override(copy);
                        item.setText(copy.getName());
                    }
                }
            } else if (connection.edit(connectionNames)) {
                item.setText(connection.getName());
            }
        }
    }

    private QListWidgetItem addWidgetItemConnection(ConnectionOptions connection) {
        final QFont bold = new QFont(ui.connectionsList.font());
        bold.setBold(true);
        final QListWidgetItem item = new QListWidgetItem(connection.getName(), ui.connectionsList);
        item.setData(Qt.ItemDataRole.UserRole, connection);
        if (connection.isLocal()) {
            item.setFont(bold);
        }
        ui.connectionsList.addItem(item);
        return item;
    }

    public int getCurrentIndex() {
        return ui.connectionsList.currentRow();
    }

    public ConnectionOptions getCurrentConnection() {
        return (ConnectionOptions) ui.connectionsList.currentItem().data(Qt.ItemDataRole.UserRole);
    }

    @Override
    protected void keyPressEvent(QKeyEvent key) {
        if (key.matches(StandardKey.Copy)) {
            copyToClipboard();
        } else if (key.matches(StandardKey.Paste)) {
            pasteFromClipboard();
        } else {
            super.keyPressEvent(key);
        }
    }
    
    private void copyToClipboard() {
        final QListWidgetItem item = ui.connectionsList.currentItem();
        if (item != null) {
            final ConnectionOptions currentConnection =
                    (ConnectionOptions) item.data(Qt.ItemDataRole.UserRole);
            final ConnectionsDocument document = ConnectionsDocument.Factory.newInstance();
            final ConnectionsDocument.Connections.Connection connectionAsXml = document.addNewConnections().addNewConnection(); //NOPMD
            currentConnection.writeToXml(connectionAsXml);
            
            final String connectionAsStr = document.xmlText(XmlUtils.getPrettyXmlOptions());            
            QApplication.clipboard().setText(connectionAsStr);

        }
    }

    private void pasteFromClipboard() {
        final String clipbardText = QApplication.clipboard().text();
        if (clipbardText != null) {
            try {
                final ConnectionsDocument document = ConnectionsDocument.Factory.parse(clipbardText);
                final List<ConnectionsDocument.Connections.Connection> connectionsList =
                        document.getConnections().getConnectionList();
                if (connectionsList != null) {
                    final List<String> connectionNames = new ArrayList<>(connections.size());
                    for (ConnectionOptions c : connections) {
                        connectionNames.add(c.getName());
                    }
                    String connectionName;
                    ConnectionOptions newConnection;
                    for (ConnectionsDocument.Connections.Connection connectionAsXml : connectionsList) { //NOPMD
                        connectionName = connectionAsXml.getName();
                        if (connections.findByName(connectionName) != null) {
                            connectionName = connections.calcCopyConnectionName(connectionName);
                            connectionAsXml.setName(connectionName);
                        }
                        newConnection = new org.radixware.kernel.explorer.env.session.ConnectionOptions(getEnvironment(), connectionAsXml, true);
                        newConnection = new org.radixware.kernel.explorer.env.session.ConnectionOptions(getEnvironment(), newConnection, null);//to generate another Id
                        if (connectionsList.size() == 1) {
                            if (newConnection.edit(connectionNames)) {
                                connections.add(newConnection);
                                ui.connectionsList.setCurrentItem(addWidgetItemConnection(newConnection));
                            }
                        } else {
                            connections.add(newConnection);
                            addWidgetItemConnection(newConnection);
                        }
                    }
                }
            } catch (XmlException exception) {
                //just do nothing on paste
            }
        }
    }
}
