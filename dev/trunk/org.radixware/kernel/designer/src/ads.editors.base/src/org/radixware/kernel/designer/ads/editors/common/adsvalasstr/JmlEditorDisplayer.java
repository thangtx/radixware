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

/*
 * 9/20/11 5:45 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class JmlEditorDisplayer extends ModalDisplayer implements ActionListener {

    public JmlEditorDisplayer(JPanel panel, String title, Object[] options) {
        super(panel, title, options);

        final JButton btnClose = (JButton) options[0];

        btnClose.addActionListener(this);
    }

    public JmlEditorDisplayer(Jml jml, String title) {
        this(new JmlValAsStrEditorPanel(jml), title, new JButton[] { new JButton("Close") });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.close(true);
    }

    public Jml getJml() {
        return ((JmlValAsStrEditorPanel) getComponent()).getValue();
    }
}
