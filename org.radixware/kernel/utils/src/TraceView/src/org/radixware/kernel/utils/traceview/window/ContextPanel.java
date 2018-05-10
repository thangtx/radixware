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
package org.radixware.kernel.utils.traceview.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.radixware.kernel.utils.traceview.TraceViewSettings;

public class ContextPanel extends JPanel implements ActionListener {

    private final JCheckBox checkBox;
    private final String contextName;
    private final FilterPanelRight parentPanel;

    public void setBackgroundCheckBox(Color bg) {
        checkBox.setBackground(bg);
    }

    public ContextPanel(String contextName, FilterPanelRight parentPanel) {
        checkBox = new JCheckBox(contextName);
        this.contextName = contextName;
        this.parentPanel = parentPanel;
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ContextPanel.this.parentPanel.clickContextCheckBox(ContextPanel.this.contextName);
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(checkBox);
        add(createRemoveButton(this));
        setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    }

    private static JButton createRemoveButton(ActionListener listener) {
        JButton button = new JButton(TraceViewSettings.EIcon.CONTEXT_REMOVE.getIcon());
        button.setPreferredSize(new Dimension(20, 20));
        button.setMaximumSize(new Dimension(20, 20));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(listener);
        return button;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parentPanel.removeContextPanel(contextName, checkBox.isSelected());
    }
}
