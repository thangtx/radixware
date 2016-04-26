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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.EventQueue;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class TabManager {

    public static abstract class TabAdapter {

        private boolean init;

        public void initTab() {
        }

        public void updateTab() {
        }

        /**
         *
         * @return unique tab key
         */
        public String getTabKey() {
            return getTabName();
        }

        public void openTab() {
            if (!isInited()) {
                initTab();
                init = true;
            } else {
                updateTab();
            }
            opened();
        }

        public boolean isInited() {
            return init;
        }

        public void setReadonlyTab(boolean readonly) {
        }

        public abstract String getTabName();

        public abstract JComponent getTabComponent();

        protected void opened() {
            final JComponent component = getTabComponent();
            if (component != null) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        component.requestFocusInWindow();
                    }
                });
            }
        }

        protected void added() {
        }

        public Icon getIcon() {
            return null;
        }

        public String getToolTip() {
            return null;
        }
    }

    private final List<TabAdapter> tabs;
    private final JTabbedPane tabbedPane;

    public TabManager(JTabbedPane tabbedPane) {
        this(tabbedPane, new LinkedList<TabAdapter>());
    }

    public TabManager(JTabbedPane tabbedPane, List<TabAdapter> tabs) {
        this.tabbedPane = tabbedPane;
        this.tabs = tabs;

        getTabbedPane().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                openSelectedTab();
            }
        });
    }

    public void addTab(TabAdapter tab) {
        addTab(tab, tabs.size());
    }

    public void addTab(TabAdapter tab, int index) {
        assert tab != null;
        assert index >= 0 && index <= tabs.size();

        tabs.add(index, tab);

        getTabbedPane().insertTab(tab.getTabName(), tab.getIcon(), tab.getTabComponent(), tab.getToolTip(), index);

        tab.added();
    }

    public void removeTab(String key) {
        final TabAdapter tab = getTab(key);

        tabs.remove(tab);
        getTabbedPane().remove(tab.getTabComponent());
    }

    public int indexOf(String key) {
        int index = 0;
        for (final TabAdapter tabAdapter : tabs) {
            if (Objects.equals(tabAdapter.getTabKey(), key)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    public TabAdapter getTab(String key) {
        for (final TabAdapter tabAdapter : tabs) {
            if (Objects.equals(tabAdapter.getTabKey(), key)) {
                return tabAdapter;
            }
        }
        return null;
    }

    public TabAdapter getTab(int index) {
        return tabs.get(index);
    }

    public final JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     *
     * @deprecated
     */
    @Deprecated
    public void assembly(boolean clear) {
    }

    public boolean setSelectedTab(String key) {
        final int tabIndex = indexOf(key);
        if (tabIndex != -1) {
            getTabbedPane().setSelectedIndex(tabIndex);
            return true;
        }
        return false;
    }

    public void updateSelectedTab() {
        final TabAdapter selectedTab = getSelectedTab();

        if (selectedTab != null) {
            selectedTab.updateTab();
        }
    }

    public void openSelectedTab() {
        final TabAdapter selectedTab = getSelectedTab();

        if (selectedTab != null) {
            selectedTab.openTab();
        }
    }

    public TabAdapter getSelectedTab() {
        final int selectedIndex = getTabbedPane().getSelectedIndex();

        if (selectedIndex >= 0) {
            return getTab(selectedIndex);
        }

        if (!tabs.isEmpty()) {
            return getTab(0);
        }
        return null;
    }

    public TabAdapter getNextTab(boolean cycle) {
        final int selectedIndex = getTabbedPane().getSelectedIndex();

        if (selectedIndex >= 0) {
            if (cycle || selectedIndex < getTabCount() - 1) {
                return getTab((selectedIndex + 1) % getTabCount());
            }
        }

        return null;
    }

    public TabAdapter getPrevTab(boolean cycle) {
        final int selectedIndex = getTabbedPane().getSelectedIndex();

        if (selectedIndex <= getTabCount() - 1) {
            if (cycle || selectedIndex > 0) {
                return getTab((getTabCount() + selectedIndex - 1) % getTabCount());
            }
        }

        return null;
    }

    public List<TabAdapter> getTabs() {
        return Collections.unmodifiableList(tabs);
    }

    public int getTabCount() {
        return tabs.size();
    }
}
