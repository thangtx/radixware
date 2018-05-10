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

package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author npopov
 */
public class TextFieldForHandleSpace extends JTextField implements FocusListener {
    
    private static final String space = " ";
    private static final String spaceReplacement = "<space>";

    public TextFieldForHandleSpace() {
        addFocusListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!this.hasFocus() && space.equals(getText())) {
            int fieldHeight = this.getHeight();
            int fontHeight = g.getFontMetrics().getHeight();
            int textBottom = (fieldHeight - fontHeight) / 2 + fontHeight - 4;
            int x = this.getInsets().left;
            if (!isEnabled()) {
                g.setColor(getDisabledTextColor());
            }
            g.drawString(spaceReplacement, x, textBottom);
        }
    }
    
    @Override
     public void focusGained(FocusEvent e) {
        this.repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.repaint();
    }
    
}
