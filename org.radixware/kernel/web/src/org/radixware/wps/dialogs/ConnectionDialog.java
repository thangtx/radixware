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

package org.radixware.wps.dialogs;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.eas.EasClient;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.wps.SslContextFactory;

import org.radixware.wps.WpsEnvironment;

import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.DropDownListDelegate;
import org.radixware.wps.rwt.DropDownListDelegate.DropDownListItem;
import org.radixware.wps.rwt.GridLayout;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;


public class ConnectionDialog extends Dialog {

    private final InputBox<String> edUserName, edPassword;
    private final InputBox<ConnectionOptions> edConnection;
    private final Label lbUserName, lbConnection;
    private final InputBox.ValueController<String> converter = new InputBox.ValueController<String>() {

        @Override
        public String getValue(String text) throws InvalidStringValueException {
            return text;
        }
    };
    private String userName = "akrylov", password;
    private ConnectionOptions connection;
    private WpsEnvironment env;

    private enum Mode {

        DEFAULT,
        NAME_AND_PASSWORD,
        PASSWORD
    }
    private Mode mode = Mode.DEFAULT;
    private final GridLayout.Row connectionRow;
    private final GridLayout.Row nameRow;
    private final GridLayout layout;

    public ConnectionDialog(final WpsEnvironment e) {
        super(e.getDialogDisplayer(), "Login");
        this.env = e;
        this.userName = "";
        this.password = "";
        this.connection = null;
        layout = new GridLayout();
        layout.setHCoverage(100);
        GridLayout.Row row = new GridLayout.Row();
        nameRow = row;
        layout.add(row);
        GridLayout.Cell cell = new GridLayout.Cell();
        Label label = lbUserName = new Label("User Name:");
        cell.add(label);
        cell.setFitWidth(true);

        row.add(cell);

        cell = new GridLayout.Cell();
        cell.add(edUserName = new InputBox<>(converter));
        edUserName.setValue(userName);
        edUserName.setHCoverage(100);
        cell.setHCoverage(100);
        row.add(cell);

        row = new GridLayout.Row();
        layout.add(row);
        cell = new GridLayout.Cell();
        row.add(cell);
        label = new Label("Password:");
        cell.add(label);
        cell.setFitWidth(true);
        cell = new GridLayout.Cell();
        cell.add(edPassword = new InputBox<>(converter));
        edPassword.setValue(password);
        cell.setHCoverage(100);
        row.add(cell);
        edPassword.setPassword(true);
        edPassword.setHCoverage(100);

        row = new GridLayout.Row();
        connectionRow = row;
        layout.add(row);
        cell = new GridLayout.Cell();
        lbConnection = label = new Label("Connection:");
        cell.add(label);
        cell.setFitWidth(true);
        row.add(cell);
        cell = new GridLayout.Cell();
        cell.setHCoverage(100);
        row.add(cell);
        cell.add(edConnection = new InputBox<>());
        edConnection.setDisplayController(new InputBox.DisplayController<ConnectionOptions>() {

            @Override
            public String getDisplayValue(final ConnectionOptions value, final boolean isFocused, final boolean isReadOnly) {
                if (value == null) {
                    return "<Choose connection>";
                }
                return value.getName();
            }
        });
        edConnection.addDropDownDelegate(new DropDownListDelegate<ConnectionOptions>() {

            @Override
            protected List<DropDownListItem<ConnectionOptions>> getItems() {
                final List<DropDownListItem<ConnectionOptions>> items = new LinkedList<>();
                final List<ConnectionOptions> connections = e.getConnections().getConnections();
                for (ConnectionOptions connection : connections) {
                    items.add(new DropDownListItem<>(connection.getName(), connection));
                }
                return items;
            }
        });
        ToolButton execCm = new ToolButton();
        edConnection.addCustomButton(execCm);
        execCm.setToolTip("Open connections manager");
        execCm.setText("...");
        execCm.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                e.getConnections().configure();
            }
        });

        add(layout);
        addCloseAction(Dialog.CLOSE_ACTION_OK, DialogResult.ACCEPTED);
        addCloseAction(Dialog.CLOSE_ACTION_CANCEL, DialogResult.REJECTED);

        edUserName.addValueChangeListener((new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                ConnectionDialog.this.userName = newValue;
            }
        }));
        edPassword.addValueChangeListener((new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                ConnectionDialog.this.password = newValue;
            }
        }));
        edConnection.addValueChangeListener(new ValueChangeListener<ConnectionOptions>() {

            @Override
            public void onValueChanged(ConnectionOptions oldValue, ConnectionOptions newValue) {
                connection = newValue;
                if (connection != null) {
                    if (userName == null || userName.isEmpty()) {
                        userName = connection.getUserName();
                        edUserName.setValue(userName);
                    }
                    password = "";
                    edPassword.setValue(password);
                }
            }
        });
        layout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        setHeight(150);
        setWidth(300);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ConnectionOptions getConnection() {
        return connection;
    }

    private void setMode(Mode mode) {
        if (mode == this.mode) {
            return;
        }
        switch (mode) {
            case NAME_AND_PASSWORD:
                if (this.mode == Mode.DEFAULT) {
                    layout.remove(connectionRow);
                } else if (this.mode == Mode.PASSWORD) {
                    layout.add(0, nameRow);
                }
                break;
            case DEFAULT:
                if (this.mode == Mode.PASSWORD) {
                    layout.add(0, nameRow);
                    layout.add(connectionRow);
                } else if (this.mode == Mode.NAME_AND_PASSWORD) {
                    layout.add(connectionRow);
                }
                break;
            case PASSWORD:
                if (this.mode == Mode.DEFAULT) {
                    layout.remove(nameRow);
                    layout.remove(connectionRow);
                } else if (this.mode == Mode.NAME_AND_PASSWORD) {
                    layout.remove(nameRow);
                }
                break;
        }
        this.mode = mode;
    }

    public boolean askUserNameAndPassword() {
        setMode(Mode.NAME_AND_PASSWORD);
        return execDialog() == DialogResult.ACCEPTED;
    }

    public boolean askConnection() {
        setMode(Mode.DEFAULT);
        return execDialog() == DialogResult.ACCEPTED;
    }

    public boolean askPassword() {
        setMode(Mode.PASSWORD);
        return execDialog() == DialogResult.ACCEPTED;
    }

    public boolean checkComplete() {
        boolean result = true;
        if (mode == Mode.NAME_AND_PASSWORD || mode == Mode.DEFAULT) {
            if (userName == null || userName.isEmpty()) {
                result = false;
                lbUserName.setForeground(Color.red);
            } else {
                lbUserName.setForeground(null);
            }
        }
        if (mode == Mode.DEFAULT) {
            if (connection == null) {
                result = false;
                lbConnection.setForeground(Color.red);
            } else {
                lbConnection.setForeground(null);
            }
        }
        return result;
    }

    public EasClient createEasClient() throws IllegalUsageError, KeystoreControllerException, CertificateUtilsException {
        if (getConnection() == null) {
            throw new IllegalUsageError("Connection is not defined");
        }
        final ConnectionOptions c = getConnection();
        if (c.getSslOptions() == null) {
            return new EasClient(env.getMessageProvider(), 
                                 env.getTracer(), 
                                 c.getInitialServerAddresses(), 
                                 c.getStationName(), 
                                 c.getAuthType(), 
                                 null, null);
        } else {            
            final char[] keyStorePassword = c.getSslOptions().useSSLAuth() ? getPassword().toCharArray() : null;
            return new EasClient(env, c.getInitialServerAddresses(),
                                 c.getStationName(),
                                 c.getAuthType(),
                                 new SslContextFactory(c, env),
                                 keyStorePassword);
        }
    }
}
