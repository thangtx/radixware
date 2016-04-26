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

import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.radixware.kernel.designer.common.dialogs.components.UtilTabbedTopComponent.UtilTabPanel;


public abstract class UtilTabbedTopComponent<ObjectType> extends TabbedTopComponent<UtilTabPanel<ObjectType>> {

    public static abstract class UtilTabPanel<ObjectType> extends TabPanel {

        private ObjectType source;
        private final JPanel content;

        public UtilTabPanel() {
            this(new JPanel());
        }

        public UtilTabPanel(JPanel content) {

            this.content = content;
            setLayout(new java.awt.BorderLayout());
            add(content, java.awt.BorderLayout.CENTER);

            final JToolBar toolBar = createToolBar();

            if (toolBar != null) {
                toolBar.setFloatable(false);
                toolBar.setRollover(true);
                toolBar.setBorderPainted(true);
                if (toolBar.getOrientation() == JToolBar.VERTICAL) {
                    add(toolBar, java.awt.BorderLayout.LINE_START);
                } else {
                    add(toolBar, java.awt.BorderLayout.PAGE_START);
                }
            }
        }

        public final void open(ObjectType object) {
            this.source = object;
            update();
        }

        public final ObjectType getObject() {
            return source;
        }

        public final JPanel getContentPanel() {
            return content;
        }

        public abstract void update();

        public abstract JToolBar createToolBar();
    }

    private void openTab(ObjectType object) {
        UtilTabPanel<ObjectType> panel = findPanel(object);

        if (panel == null) {
            panel = createPanel(object);
            panel.open(object);
            addTab(panel);
        } else {
            panel.open(object);
        }
        activatePage(panel);
    }

    private UtilTabPanel<ObjectType> findPanel(ObjectType object) {
        for (final UtilTabPanel<ObjectType> panel : getPanels()) {
            if (Objects.equals(panel.getObject(), object)) {
                return panel;
            }
        }
        return null;
    }

    public void open(ObjectType object) {
        if (object != null) {
            open();
            openTab(object);
        }
    }

    public abstract UtilTabPanel<ObjectType> createPanel(ObjectType object);
}
