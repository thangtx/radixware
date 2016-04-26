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

package org.radixware.kernel.designer.api.editors.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel;


public class BorderedRollUpPanel extends BorderedCollapsablePanel {

    private static final class ExpandButton extends TopComponent {

        private final JLabel label;
        private final JLabel iconLabel;
        private final Map<ActionListener, MouseAdapter> listeners;
        private boolean isExpand;

        public ExpandButton(String title, Icon icon, boolean expand) {
            super();

            iconLabel = new JLabel();
            this.label = new JLabel();
            this.label.setIcon(icon);
            this.label.setText(title);
            addComponent(Box.createHorizontalStrut(4), 0);
            addComponent(iconLabel, 1);
            addComponent(Box.createHorizontalStrut(4), 2);
            addComponent(label, 3);
            addComponent(Box.createHorizontalStrut(4), 4);

            this.listeners = new HashMap<>();

            setExpanded(expand);
            setTitle(title);
        }

        @Override
        public void addActionListener(final ActionListener listener) {
            final MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        setExpanded(!isExpanded());
                        listener.actionPerformed(new ActionEvent(e, e.getID(), "click"));
                    }
                }
            };

            label.addMouseListener(mouseAdapter);
            iconLabel.addMouseListener(mouseAdapter);
            getContentContainer().addMouseListener(mouseAdapter);
            listeners.put(listener, mouseAdapter);
        }

        @Override
        public void removeActionListener(ActionListener listener) {
            final MouseAdapter mouseAdapter = listeners.get(listener);
            if (mouseAdapter != null) {
                label.removeMouseListener(mouseAdapter);
                iconLabel.removeMouseListener(mouseAdapter);
            }
        }

        @Override
        public boolean isExpanded() {
            return isExpand;
        }

        @Override
        public final void setExpanded(boolean expand) {
            isExpand = expand;

            iconLabel.setIcon((isExpand ? UIManager.getIcon("Tree.expandedIcon") : UIManager.getIcon("Tree.collapsedIcon")));
        }

        @Override
        public void setTitle(String title) {
            this.label.setText(title);
        }

        @Override
        public void setEnabled(boolean enabled) {
        }
    }

    public BorderedRollUpPanel(JPanel panel, String title, Icon icon, boolean expand) {
        super(new ExpandButton(title, icon, expand), new ContentProvider(panel), expand);
    }
}
