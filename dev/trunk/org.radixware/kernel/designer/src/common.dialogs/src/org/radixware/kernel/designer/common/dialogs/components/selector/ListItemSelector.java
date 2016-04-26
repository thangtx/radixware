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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.components.tasks.AbstractTask;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskProcessor;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskState;
import org.radixware.kernel.designer.common.dialogs.utils.SearchFieldAdapter;


public abstract class ListItemSelector<TItem, TContext> extends ContextableItemSelector<TItem, TContext> {

    protected final JList<TItem> itemList = new JList<>();
    private final JLabel waitLable = new JLabel(NbBundle.getMessage(ListItemSelector.class, "ListItemSelector.WaitLable.Text"), JLabel.CENTER);
    private final JLabel notFoundLable = new JLabel(NbBundle.getMessage(ListItemSelector.class, "ListItemSelector.NotFoundLable.Text"), JLabel.CENTER);
    private final JScrollPane scroll = new JScrollPane(itemList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final AbstractTask waitTask = new WaitTask();
    private TaskProcessor updateTaskProcessor;
    private final List<ActionListener> actionListeners = new ArrayList<>();

    protected ListItemSelector(boolean defaultSelectionModel) {
        super();

        if (defaultSelectionModel) {
            itemList.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    fireSelectionChange((TItem) itemList.getSelectedValue());
                }
            });
        }

        itemList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    for (ActionListener listener : actionListeners) {
                        listener.actionPerformed(new ActionEvent(ListItemSelector.this, ActionEvent.ACTION_PERFORMED, null));
                    }
                }
            }
        });
    }

    public ListItemSelector() {
        this(true);
    }

    @Override
    public TItem getSelectedItem() {
        if (itemList.getModel().getSize() > 0) {
            return itemList.getSelectedValue();
        } else {
            return null;
        }
    }

    @Override
    public Collection<TItem> getSelectedItems() {
        if (itemList.getModel().getSize() > 0) {
            return itemList.getSelectedValuesList();
        } else {
            return Collections.<TItem>emptyList();
        }
    }

    @Override
    public void setSelectedItems(Collection<TItem> items) {
        final int[] indices = convertValuesToIndices(items);

        itemList.setSelectedIndices(indices);
    }
    
    protected int[] convertValuesToIndices(Collection<TItem> items) {
        final List<Integer> indicesList = new ArrayList<>(items.size());
        final ListModel<TItem> model = itemList.getModel();
        
        for (int i = 0; i < model.getSize(); i++) {
            final TItem elementAt = model.getElementAt(i);
            for (TItem item : items) {
                if (elementAt == item) {
                    indicesList.add(i);
                }
            }
        }
        final int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }
        return indices;
    }

    @Override
    public final void stateChanged(ChangeEvent e) {
        updateList();
    }

    @Override
    protected final void updateList() {
        assert SwingUtilities.isEventDispatchThread();

        cancel();

        AssemblyModelTask task = getAssemblyModelTask();
        if (task != null) {
            updateTaskProcessor = task.startTask();
        }
    }
    private List<TItem> selected;

    @Override
    protected void openImpl(Collection<TItem> item) {
        getSelectorLayout().setSelectorComponent(waitLable);

        updateList();
        selected = new ArrayList<>(item);
    }

    public final void cancel() {
        assert SwingUtilities.isEventDispatchThread();

        if (updateTaskProcessor != null) {
            updateTaskProcessor.cancelTask();
        }
    }

    public void setListItemRender(ListCellRenderer renderer) {
        itemList.setCellRenderer(renderer);
    }

    public final void delegateNavigationKeys(JComponent source) {
        SearchFieldAdapter.exchangeCommands(SearchFieldAdapter.LIST_NAVIGATION_COMMANDS, itemList, source);
    }

    public final void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public final void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }
    
    protected abstract AssemblyModelTask getAssemblyModelTask();

    public static class SimpleListModel extends AbstractListModel {

        final List<String> items;

        public SimpleListModel(List<String> items) {
            this.items = new ArrayList<>(items);
        }

        public SimpleListModel() {
            this.items = new ArrayList<>();
        }

        @Override
        public int getSize() {
            return items.size();
        }

        @Override
        public Object getElementAt(int index) {
            return items.get(index);
        }

        public void setItems(List<String> items) {
            this.items.clear();
            this.items.addAll(items);

            int size = getSize();

            fireContentsChanged(this, 0, size > 0 ? size - 1 : 0);
        }
    }

    public abstract class AssemblyModelTask extends AbstractTask {

        @Override
        public final synchronized void run() {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (!itemList.isSelectionEmpty()) {
                        selected = itemList.getSelectedValuesList();
                        itemList.clearSelection();
                    }
                }
            });

            final TaskProcessor waitTaskProcessor = waitTask.startTask();

            final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Search...");
            final ListModel<TItem> listModel;
            try {
                progressHandle.start();

                listModel = assemblyModel();
            } finally {
                progressHandle.finish();
            }

            waitTaskProcessor.cancelAndInterruptTask();

            if (getState().get() == TaskState.CANCELLED) {
                return;
            }

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (listModel != null && listModel.getSize() > 0) {

                        itemList.setModel(listModel);
                        getSelectorLayout().setSelectorComponent(scroll);

                        itemList.setSelectedValue(selected, true);

                        if (itemList.isSelectionEmpty() && listModel.getSize() > 0) {
                            itemList.setSelectedIndex(0);
                            itemList.ensureIndexIsVisible(0);
                        }
                    } else if (listModel != null) {
                        getSelectorLayout().setSelectorComponent(notFoundLable);
                    }
                }
            });
        }

        protected abstract ListModel<TItem> assemblyModel();
    }

    private final class WaitTask extends AbstractTask {

        @Override
        public void run() {
            try {
                Thread.sleep(600);
                if (getState().get() == TaskState.RUNNING) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            getSelectorLayout().setSelectorComponent(waitLable);
                        }
                    });
                }
            } catch (InterruptedException ex) {
                getProcessor().cancelTask();
            }
        }
    }

}
