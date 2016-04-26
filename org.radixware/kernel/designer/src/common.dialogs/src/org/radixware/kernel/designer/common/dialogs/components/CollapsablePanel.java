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
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;


public class CollapsablePanel extends JPanel {

    public interface ITopComponent {

        JPanel getComponent();

        void addActionListener(ActionListener listener);

        void removeActionListener(ActionListener listener);

        boolean isExpanded();

        void setExpanded(boolean expand);

        void setTitle(String title);

        void setEnabled(boolean enabled);
    }

    public interface IContentProvider {

        JPanel getContent();
    }

    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final JPanel contentPanel;
    private final IContentProvider contentProvider;
    private final ITopComponent topComponent;
    //
    private boolean isExpanded = false;
    private volatile boolean force = false;
    private boolean freeze = false;

    public CollapsablePanel(ITopComponent component, IContentProvider contentProvider, boolean expand) {
        super();
        super.setLayout(new BorderLayout());

        this.contentProvider = contentProvider;
        this.contentPanel = new JPanel(new BorderLayout());
        this.topComponent = component;

        super.addImpl(topComponent.getComponent(), BorderLayout.PAGE_START, 0);
        super.addImpl(contentPanel, BorderLayout.CENTER, 1);

        expand(expand);
        topComponent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!force) {
                    expand(topComponent.isExpanded());
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        topComponent.getComponent().setEnabled(!enabled ? false : enabled && !freeze);
        super.setEnabled(enabled);
        final JPanel panel = getContentProvider().getContent();
        if (panel != null) {
            panel.setEnabled(enabled);
        }
    }

    public final boolean isExpanded() {
        return isExpanded;
    }

    public final void expand(boolean expand) {
        if (expand) {
            expand();
        } else {
            collapse();
        }
    }

    public void collapse() {
        switchState(null, false);
    }

    public void expand() {
        switchState(contentProvider.getContent(), true);
    }

    protected final void switchState(JPanel panel, boolean expand) {
        if (freeze || expand == isExpanded()) {
            return;
        }

        installPanel(panel, expand);

        if (topComponent.isExpanded() != expand) {
            force = true;
            topComponent.setExpanded(expand);
            force = false;
        }

        this.isExpanded = expand;

        if (expand) {
            expanded();
        } else {
            collapsed();
        }

        changeSupport.fireChange();

        revalidate();
        repaint();
    }

    protected void installPanel(JPanel panel, boolean expanded) {
        contentPanel.removeAll();

        if (panel != null) {
            contentPanel.add(panel, BorderLayout.CENTER);
        }
    }

    protected void collapsed() {
    }

    protected void expanded() {
    }

    public IContentProvider getContentProvider() {
        return contentProvider;
    }

    public final void setTitle(String title) {
        topComponent.setTitle(title);
    }

    public final void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public final void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public final void freeze(boolean freeze) {
        this.freeze = freeze;
        topComponent.setEnabled(!freeze);
    }

    public final boolean isFreeze() {
        return freeze;
    }

    public final JPanel getContentPanel() {
        return contentPanel;
    }

    public final ITopComponent getTopComponent() {
        return topComponent;
    }

    //
    @Override
    public final void removeAll() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public final void remove(int index) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public final void setLayout(LayoutManager mgr) {
    }

    @Override
    protected final void addImpl(Component comp, Object constraints, int index) {
        throw new UnsupportedOperationException("");
    }

    public JPanel getContent() {
        return contentProvider.getContent();
    }
}
