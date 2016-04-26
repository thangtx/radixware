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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;


public abstract class ItemSelector<TItem> implements ChangeListener {

    private static final class DefaultSelectorLayout implements ISelectorLayout {

        final ItemSelector selector;
        private final Map<IItemFilter, Object> filtersMap = new LinkedHashMap<>();
        private Component selectorComponent;
        private boolean filterChange, selectorChange;
        private final static String[] positions = {
            BorderLayout.PAGE_START, BorderLayout.LINE_END,
            BorderLayout.PAGE_END, BorderLayout.LINE_START
        };
        private final Box[] filterBoxes = {
            Box.createVerticalBox(),
            Box.createHorizontalBox(),
            Box.createVerticalBox(),
            Box.createHorizontalBox()
        };

        public DefaultSelectorLayout(ItemSelector selector) {
            this.selector = selector;
            selector.getComponent().setLayout(new BorderLayout());
        }

        @Override
        public void setSelectorComponent(Component сomponent) {
            if (selectorComponent != сomponent) {
                selectorComponent = сomponent;

                selectorChange = true;

                layout();
            }
        }

        @Override
        public Component getSelectorComponent() {
            return selectorComponent;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void addFilterComponent(Object constraint, IItemFilter filter) {
            if (!filtersMap.containsKey(filter)) {
                filtersMap.put(filter, constraint);
                selector.addFilter(filter);

                filterChange = true;

                layout();
            }
        }

        @Override
        public void addFilterComponent(IItemFilter сomponent) {
            addFilterComponent(null, сomponent);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void removeFilterComponent(IItemFilter filter) {
            if (filtersMap.remove(filter) != null) {
                selector.removeFilter(filter);

                filterChange = true;

                layout();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void layout() {
            if (!filterChange && !selectorChange) {
                return;
            }

            Component fucus = findFucus();
            JPanel selectorPanel = selector.getComponent();

            selectorPanel.removeAll();

            if (filterChange) {
                for (Box box : filterBoxes) {
                    box.removeAll();
                }
            }

            if (selectorComponent != null) {
                selectorPanel.add(selectorComponent, BorderLayout.CENTER);
            }

            for (IItemFilter filter : filtersMap.keySet()) {
                Object constraint = filtersMap.get(filter);

                if (filter != null && filter.getComponent() != null) {
                    getFilterBox(constraint).add(filter.getComponent());
                }
            }

            for (int i = 0; i < filterBoxes.length; ++i) {
                if (filterBoxes[i].getComponentCount() > 0) {
                    selectorPanel.add(filterBoxes[i], positions[i]);
                }
            }

            if (fucus != null && fucus.getParent() != null) {
                fucus.requestFocusInWindow();
            }

            selectorPanel.updateUI();

            filterChange = false;
            selectorChange = false;
        }

        private Box getFilterBox(Object key) {
            int i = 0;
            for (EFilterPosition pos : EFilterPosition.values()) {
                if (pos == key) {
                    return filterBoxes[i];
                }
                ++i;
            }
            return filterBoxes[0];
        }

        private Component findFucus() {
            return FocusManager.getCurrentManager().getFocusOwner();
        }
    }

    private ISelectorLayout selectorLayout;
    protected final List<IItemFilter<TItem>> filters = new LinkedList<>();
    private final JPanel selectorPanel = new JPanel(new BorderLayout());
    private final SelectionChangeSupport<TItem> selectionChangeSupport = new SelectionChangeSupport<>(this);
    private boolean openState = false;

    public ItemSelector() {
        super();

        setSelectorLayout(new DefaultSelectorLayout(this));
    }

    protected ItemSelector(ISelectorLayout layout) {
        super();

        setSelectorLayout(layout);
    }

    public void addSelectionListener(SelectionListener<TItem> listener) {
        selectionChangeSupport.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener<TItem> listener) {
        selectionChangeSupport.removeSelectionListener(listener);
    }

    protected void fireSelectionChange(TItem newValue) {
        selectionChangeSupport.fireSelectionChange(newValue);
    }

    public final void addFilter(IItemFilter<TItem> filter) {

        if (filter != null && !filters.contains(filter)) {
            synchronized (filters) {
                filters.add(filter);
            }
            filter.addChangeListener(this);
            if (isOpened()) {
                updateList();
            }
        }
    }

    public final void removeFilter(IItemFilter<TItem> filter) {
        if (filter != null) {
            filter.removeChangeListener(this);
            synchronized (filters) {
                filters.remove(filter);
            }
            if (isOpened()) {
                updateList();
            }
        }
    }

    public boolean accept(TItem value) {
        for (IItemFilter<TItem> filter : getFilters()) {
            if (!filter.accept(value)) {
                return false;
            }
        }
        return true;
    }

    public Collection<IItemFilter<TItem>> getFilters() {
        synchronized (filters) {
            return new ArrayList<>(filters);
        }
    }

    public final void open(TItem item) {
        resetFilters();
        openImpl(item != null ? Collections.<TItem>singletonList(item) : Collections.<TItem>emptyList());
        openState = true;
    }
    
    public final void open(Collection<TItem> item) {
        resetFilters();
        openImpl(item);
        openState = true;
    }
    
    protected void openImpl(Collection<TItem> items) {
        updateList();
        setSelectedItems(items);
    }

    public boolean isOpened() {
        return openState;
    }

    public boolean isSelected() {
        return getSelectedItem() != getNullItem();
    }

    private TItem getNullItem() {
        return null;
    }

    public Collection<TItem> getSelectedItems() {
        return Collections.<TItem>singletonList(getSelectedItem());
    }

    public final ISelectorLayout getSelectorLayout() {
        return selectorLayout;
    }

    public final void setSelectorLayout(ISelectorLayout selectorLayout) {
        this.selectorLayout = selectorLayout;
    }

    public JPanel getComponent() {
        return selectorPanel;
    }

    private void resetFilters() {
        for (IItemFilter<TItem> filter : getFilters()) {
            filter.reset();
        }
    }

    public boolean requestFocusInWindow() {
        return getComponent().requestFocusInWindow();
    }

    protected abstract void updateList();

    public abstract TItem getSelectedItem();

    public final void setSelectedItem(TItem item) {
        setSelectedItems(Collections.<TItem>singletonList(item));
    }
    
    public abstract void setSelectedItems(Collection<TItem> item);
}
