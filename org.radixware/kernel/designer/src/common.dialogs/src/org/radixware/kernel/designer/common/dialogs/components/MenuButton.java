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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class MenuButton extends JButton {

    public static final String MENU_KEY = "MenuButton.PopupMenu";

    public MenuButton(JPopupMenu menu) {
        this(null, null, menu);
    }

    public MenuButton(String text, Icon icon, JPopupMenu menu) {
        super(text, icon);

        putClientProperty(MENU_KEY, menu);

        addMouseListener(new MouseAdapter() {

            boolean menuAppearance = false;
            boolean defAction = false;

            @Override
            public void mousePressed(MouseEvent e) {
                menuAppearance = false;
                defAction = false;

                if (!MenuButton.this.isEnabled()) {
                    return;
                }

                final JPopupMenu menu = getPopupMenu();

                if (menu != null) {
                    if (menu.getSubElements().length == 1) {
                        defAction = true;
                        return;
                    }

                    menu.show(MenuButton.this, 0, getHeight());
                    menuAppearance = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (MenuButton.this.contains(e.getPoint()) && defAction) {
                    defAction = false;
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            ((AbstractButton) getPopupMenu().getSubElements()[0].getComponent()).doClick();
                        }
                    });
                }
                if (menuAppearance) {
                    menuAppearance = false;
                    e.consume();
                }
            }
        });
    }

    protected JPopupMenu getPopupMenu() {
        return (JPopupMenu) getClientProperty(MENU_KEY);
    }
}
