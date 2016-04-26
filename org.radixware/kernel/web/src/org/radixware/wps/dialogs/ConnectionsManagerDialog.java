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

import java.util.List;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.Action.ActionListener;
import org.radixware.wps.WpsEnvironment;

import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.HorizontalBox;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.ListBox.ListBoxItem;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.views.RwtAction;


public class ConnectionsManagerDialog extends Dialog {

    private ListBox list;
    private ConnectionOptions currentConnection = null;
    private WpsEnvironment env;
    private RwtAction editAction, deleteAction;

    public ConnectionsManagerDialog(final WpsEnvironment e) {
        super(e.getDialogDisplayer(), "Connections Manager");
        this.env = e;
        addCloseAction("Ok", DialogResult.ACCEPTED);
        addCloseAction("Cancel", DialogResult.REJECTED);

        HorizontalBox hbox = new HorizontalBox();
        this.add(hbox);
        list = new ListBox();
        hbox.add(list);
        list.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        setWidth(300);
        setHeight(200);
        ToolBar panel = new ToolBar();
        panel.setVertical();
        hbox.add(panel);

        RwtAction addAction = new RwtAction(e.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CREATE), "Add...");
        addAction.setEnabled(true);
        addAction.addActionListener(new ActionListener() {

            @Override
            public void triggered(Action action) {
                ConnectionOptions opts = env.getConnections().create();
                if (opts != null) {
                    currentConnection = opts;

                    updateListContent();
                }
            }
        });
        panel.addAction(addAction);


        editAction = new RwtAction(e.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT), "Edit...");
        editAction.setEnabled(false);
        editAction.addActionListener(new ActionListener() {

            @Override
            public void triggered(Action action) {
                if (currentConnection != null && currentConnection.edit(env.getConnections().getConnectionNames())) {

                    updateListContent();
                }
            }
        });
        panel.addAction(editAction);

        deleteAction = new RwtAction(e.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.DELETE), "Delete...");
        deleteAction.setEnabled(false);
        deleteAction.addActionListener(new ActionListener() {

            @Override
            public void triggered(Action action) {
                if (currentConnection != null) {
                    List<ConnectionOptions> connections = env.getConnections().getConnections();
                    int index = connections.indexOf(currentConnection);
                    env.getConnections().remove(currentConnection.getName());
                    currentConnection = null;
                    connections = env.getConnections().getConnections();
                    if (index < 0 || index > connections.size()) {
                        index = connections.size() - 1;
                    }
                    if (index >= 0 && index < connections.size()) {
                        currentConnection = connections.get(index);
                    }
                    updateListContent();
                }
            }
        });
        panel.addAction(deleteAction);


        updateListContent();
        list.addCurrentItemListener(new ListBox.CurrentItemListener() {

            @Override
            public void currentItemChanged(ListBoxItem currentItem) {
                currentConnection = ((ListItem) currentItem).connection;
                updateButtonsState();
            }
        });
    }

    private static class ListItem extends ListBox.ListBoxItem {

        private final ConnectionOptions connection;

        public ListItem(ConnectionOptions connection) {
            this.connection = connection;
            setText(connection.getName());
        }
    }

    private void updateListContent() {
        list.clear();
        for (ConnectionOptions opts : env.getConnections()) {
            ListItem item = new ListItem(opts);
            list.add(item);
            if (opts == currentConnection) {
                list.setCurrent(item);
            }
        }
        updateButtonsState();
    }

    private void updateButtonsState() {
        editAction.setEnabled(currentConnection != null);
        deleteAction.setEnabled(currentConnection != null);
    }
}
