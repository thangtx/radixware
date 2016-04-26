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

package org.radixware.kernel.designer.ads.editors.clazz.sql.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Label and TextField
 */
public class LbTextField extends JPanel {

    private JTextField textField;
    private JLabel label;

    public LbTextField() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        label = new JLabel("Label:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 5);
        add(label, constraints);

        textField = new JTextField("Text");
        textField.setMaximumSize(new Dimension(Short.MAX_VALUE, getPreferredHeight()));//NOPMD
        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(textField, constraints);
    }

    public JTextField getTextField() {
        return textField;
    }

    public JLabel getLabel() {
        return label;
    }

    public int getPreferredHeight() {
        return textField.getFont().getSize();
    }
}
