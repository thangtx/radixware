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

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.api.ApiEditorManager;
import org.radixware.kernel.designer.common.dialogs.components.ScalableRuler;


public class RefLabel extends JLabel {

    RadixObject ref;

    public RefLabel(RadixObject ref, String title, String tooltip, RadixIcon icon) {
        super(title);

        this.ref = ref;
        final Font font = getFont();
        final ScalableRuler ruler = new ScalableRuler(12, font.getSize());

        setIcon(icon.getIcon(ruler.scale(14), ruler.scale(14)));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    open(e.isControlDown());
                }
            }
        });

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(tooltip);
    }

    public RefLabel(RadixObject ref) {
        this(ref, ref.getQualifiedName());
    }

    public RefLabel(RadixObject ref, String title) {
        this(ref, title, ref.getToolTip(), ref.getIcon());
    }

    public void open(final boolean newBrowser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (newBrowser) {
                    ApiEditorManager.create().open(ref);
                } else {
                    ApiEditorManager.find(RefLabel.this).open(ref);
                }
            }
        });
    }
}
