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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class CollapseButton extends JButton implements ActionListener {

    private Collapsible _collapsible;
    private Icon icon;

    public CollapseButton(Collapsible collapsible) {
        super();

        _collapsible = collapsible;

        setRolloverEnabled(true);
        setFocusPainted(false);
        setDefaultCapable(false);
        setBorder(null);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setToolTipText("Collapses/Expands Panel");

        icon = RadixWareIcons.TREE.COLLAPSE.getIcon();
        setIcon(icon);
        setRolloverIcon(icon);
        setPressedIcon(icon);

        addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        if (_collapsible.isCollapsed()) {
            _collapsible.expand();
        } else {
            _collapsible.collapse();
        }
    }
}

