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

package org.radixware.kernel.server.dialogs;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.radixware.kernel.server.dialogs.Login.Messages;


class AsyncComboBoxModel implements ComboBoxModel {

    public static class Item {

        final Integer id;
        final String name;

        Item(final Integer id, final String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id != null ? id.toString() + " - " + name : name;
        }
    }

    public static interface DataProvider {

        List<AsyncComboBoxModel.Item> getData() throws SQLException;
    }
    private static final Item NULL_ITEM = new Item(null, "");
    private final Login dlgLogin;
    private volatile Object selected = null;
    private final DataProvider dataProvider;
    /**
     * accessed only from AWT Thread
     */
    private volatile Thread loadingThread = null;

    AsyncComboBoxModel(final Login dlg, final DataProvider dataProvider) {
        dlgLogin = dlg;
        this.dataProvider = dataProvider;
    }

    Item getSelected() {
        return (Item) selected;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public void setSelectedItem(final Object anItem) {
        selected = anItem;
    }

    @Override
    public Object getElementAt(final int index) {
        synchronized (data) {
            if (index >= 0 && index < data.size()) {
                return data.get(index);
            } else {
                return NULL_ITEM;
            }
        }
    }

    @Override
    public int getSize() {
        return data.size();
    }
    private final List<Item> data = new Vector<Item>();

    void clear() {
        synchronized (data) {
            final ListDataEvent ev = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, 0, data.size());
            data.clear();
            for (ListDataListener list : listeners) {
                list.intervalRemoved(ev);
            }
            selected = null;
        }
    }
    private final List<ListDataListener> listeners = new CopyOnWriteArrayList<ListDataListener>();

    @Override
    public void addListDataListener(final ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(final ListDataListener l) {
        listeners.remove(l);
    }
    //called only from AWT-EventThread
    void load(final boolean isAsync) {
        if (data.isEmpty()) {
            if (loadingThread != null && loadingThread != Thread.currentThread()) {
                final Thread t = loadingThread;
                loadingThread = null;
                t.interrupt();
                try {
                    t.join(1000);
                } catch (InterruptedException ex) {
                    return; //AWT thread is interrupted, we can do nothing
                }
                clear();// in case loadingTask fill data
            }
            if (isAsync) {
                synchronized (data) {
                    for (int i = 0; i < 3; i++) {
                        data.add(NULL_ITEM);
                    }
                    data.add(new Item(null, "   " + Messages.PLEASE_WAIT));
                    for (int i = 0; i < 3; i++) {
                        data.add(NULL_ITEM);
                    }
                    final ListDataEvent ev = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, data.size() - 1);
                    for (ListDataListener list : listeners) {
                        list.intervalAdded(ev);
                    }
                }
            }
            final Runnable loadTask = new Runnable() {

                @Override
                public void run() {
                    final Thread currrentThread = Thread.currentThread();
                    try {
                        final List<Item> loadedData = dataProvider.getData();
                        if (Thread.interrupted())
                            return;
                        synchronized (data) {
                            data.clear();
                            data.addAll(loadedData);
                        }
                    } catch (final SQLException ex) {
                        if (Thread.interrupted())
                            return;
                        synchronized (data) {
                            data.clear();
                            data.add(new Item(null, ex.getMessage()));
                        }
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                if (loadingThread == currrentThread){// is not canceled
                                    dlgLogin.messageError(Messages.TITLE_CONNECTION_ERR, ex.getMessage());
                                }
                            }
                        });
                    }
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            synchronized (data) {
                                if (loadingThread != currrentThread){
                                    return; // is canceled
                                }
                                if (!data.isEmpty()) {
                                    final ListDataEvent ev = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, data.size() - 1);
                                    for (ListDataListener list : listeners) {
                                        list.intervalAdded(ev);
                                    }
                                }
                            }
                        }
                    });
                }
            };
            if (dlgLogin.checkDbConnectionParams(true)) {
                if (isAsync) {
                    loadingThread = new Thread() {

                        {
                            setDaemon(true);
                        }

                        @Override
                        public void run() {
                            loadTask.run();
                        }
                    };
                    loadingThread.start();
                } else {
                    loadingThread = Thread.currentThread();
                    loadTask.run();
                }
            } else {
                clear();
            }
        }
    }

    public List<Item> getData() {
        return data;
    }
}
