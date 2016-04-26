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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openide.awt.TabbedPaneFactory;
import org.openide.windows.TopComponent;
import org.radixware.kernel.designer.common.dialogs.components.TabbedTopComponent.ITabPanel;


public abstract class TabbedTopComponent<TPanel extends JPanel & ITabPanel> extends TopComponent implements PropertyChangeListener {

    public interface ITabPanel {

        String getTitle();

        String getToolTipText();

        Icon getIcon();
    }

    private JTabbedPane tabbedPane = TabbedPaneFactory.createCloseButtonTabbedPane();
    private final List<TPanel> panels = new ArrayList<>();

    public TabbedTopComponent() {
        setLayout(new BorderLayout());

        tabbedPane.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TabbedPaneFactory.PROP_CLOSE.equals(evt.getPropertyName())) {
            closePage((TPanel) evt.getNewValue());
        }
    }

    @Override
    public void componentClosed() {
        panels.clear();
        tabbedPane.removeAll();
        removeAll();
    }

    protected final JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    private void removeTabbedPane() {

        remove(tabbedPane);

        assert tabbedPane.getTabCount() == 1;

        if (tabbedPane.getTabCount() > 0) {
            TPanel panel = (TPanel) tabbedPane.getComponentAt(0);
            tabbedPane.removeAll();
            add(panel, BorderLayout.CENTER);

            updateTitle();
        }
    }

    private void initTabbedPane() {
        if (tabbedPane.getTabCount() == 0) {

            if (getComponentCount() > 0) {
                TPanel panel = (TPanel) getComponent(0);
                remove(panel);
            }

            for (final TPanel panel : panels) {
                addTabToPane(panel);
            }

            add(tabbedPane, BorderLayout.CENTER);
        }
    }

    protected final void updateTitle() {
        if (useSinglePanelName() && panels.size() == 1) {
            setDisplayName(formatName(panels.get(0).getTitle()));
        } else {
            setDisplayName(formatName(getDefaultTitle()));
        }
    }

    protected void addTab(TPanel panel) {
        if (panels.size() == 1) {
            initTabbedPane();

        }

        panels.add(panel);
        if (panels.size() > 1) {
            addTabToPane(panel);
        } else {
            add(panel, BorderLayout.CENTER);
        }
        updateTitle();
    }

    protected void addTabToPane(TPanel panel) {
        tabbedPane.addTab(formatName(panel.getTitle()), panel.getIcon(), panel, panel.getToolTipText());
    }

    protected void closePage(TPanel panel) {
        panels.remove(panel);
        tabbedPane.remove(panel);

        if (panels.size() < 2) {
            removeTabbedPane();
        }
    }

    protected void activatePage(TPanel panel) {
        if (panels.size() > 1 && panels.indexOf(panel) > -1) {
            tabbedPane.setSelectedComponent(panel);
        }
    }

    protected String formatName(String name) {
        return name;
    }

    protected final List<TPanel> getPanels() {
        return Collections.unmodifiableList(panels);
    }

    protected boolean useSinglePanelName() {
        return false;
    }

    protected abstract String getDefaultTitle();
}
