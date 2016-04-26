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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openide.awt.DropDownButtonFactory;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class DropDownButton {
    
    private class PopupAction extends AbstractAction {
        
        private final Action action;
        
        public PopupAction(final Action action) {
            super((String) action.getValue(Action.NAME), (Icon) action.getValue(Action.SMALL_ICON));
            this.action = action;
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            action.actionPerformed(e);
            button.setAction(action);
        }
        
    }
    
    private final JButton button;
    private final JPopupMenu popupMenu;
    private final List<PopupAction> actions = new LinkedList<>();
    
    public DropDownButton() {
        popupMenu = new JPopupMenu();
        button = DropDownButtonFactory.createDropDownButton(
                RadixWareIcons.EDIT.NO_ICON.getIcon(), popupMenu);
        button.setBorderPainted(false);
        button.setMargin(new Insets(2, 2, 2, 2));
        button.setFocusable(false);
//        button.setAction(null);
    }
    
    public JButton getButton() {
        return button;
    }
    
    public void addAction(final Action action) {
        PopupAction a = new PopupAction(action);
        popupMenu.add(a);
        actions.add(a);
        if (button.getAction() == null) {
            button.setAction(action);
        }
    }
    
    public void addSeparator() {
        popupMenu.addSeparator();
    }
    
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        if (popupMenu != null) {
            for (PopupAction a : actions) {
                a.setEnabled(enabled);
            }
        }
    }
    
}
