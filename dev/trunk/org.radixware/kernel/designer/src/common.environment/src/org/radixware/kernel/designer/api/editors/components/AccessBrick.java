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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.designer.ads.common.dialogs.AccessPanel;
import org.radixware.kernel.designer.api.editors.SimpleBrick;


@NbBundle.Messages({
    "AccessBrick_Accessibility=Accessibility:"
})
public class AccessBrick extends SimpleBrick<AdsDefinition> {

    public AccessBrick(AdsDefinition source, GridBagConstraints constraints) {
        super(source, new JPanel(), constraints, "access", null);

        component.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets.right = 4;
        component.add(new JLabel(Bundle.AccessBrick_Accessibility()), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;

        final AccessPanel accessPanel = new AccessPanel();
        component.add(accessPanel, c);
        accessPanel.open(getSource());

        final JCheckBox chbDeprecated = accessPanel.addCheckBox("Deprecated");

        chbDeprecated.setSelected(getSource().isDeprecated());
        chbDeprecated.setEnabled(getSource() instanceof IAccessible && !getSource().isReadOnly());
        chbDeprecated.setFocusable(false);

        chbDeprecated.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getSource() instanceof IAccessible) {
                    ((IAccessible) getSource()).getAccessFlags().setDeprecated(chbDeprecated.isSelected());
                }
            }
        });

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        component.add(new JLabel(), c);
    }

    @Override
    protected void setDefaultInsets(GridBagConstraints constraints) {
        // has no insets
    }
}
