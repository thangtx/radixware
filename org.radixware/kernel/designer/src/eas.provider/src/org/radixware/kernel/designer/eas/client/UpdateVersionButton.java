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

package org.radixware.kernel.designer.eas.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;

import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


@ServiceProvider(service = StatusLineElementProvider.class, position = 10)
public class UpdateVersionButton implements StatusLineElementProvider {

    public interface Status {

        public boolean isVisible();

        public void apply();
    }
    private JButton label = new JButton();

    public UpdateVersionButton() {
        label.setIcon(RadixWareDesignerIcon.ARROW.CIRCLE.getIcon());
        label.setToolTipText("Update to new version");
        label.setVisible(false);
    }

    @Override
    public Component getStatusLineElement() {
        return label;
    }

    public void refresh(final Status status) {
        label.setVisible(status.isVisible());
        if (status.isVisible()) {
            label.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    status.apply();
                }
            });
        } else {
            for (ActionListener l : Arrays.asList(label.getActionListeners())) {
                label.removeActionListener(l);
            }
        }
    }
}
