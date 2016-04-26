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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.editor.BaseAction;
import org.openide.awt.Actions;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;

/**
 * This action act as container for some other actions.
 */
public abstract class ScmlToolBarDropdownButton extends ScmlToolBarAction {

    private PopupButton button;

    public ScmlToolBarDropdownButton(String name, ScmlEditor editor) {
        super(name, editor);
        PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof Boolean) {
                    updateState();
                }
            }
        };
        editor.getPane().addPropertyChangeListener(ScmlEditorPane.CURRENT_POSITION_EDITABLE, listener);
    }

    /**
     * Actions that would be presented in PopupMenu.
     * Note, that this method is called many times and does not store any cache,
     * @return
     */
    protected abstract ScmlToolBarAction[] getActions();

    @Override
    public void updateState() {
        boolean hasEnabledActions = false;

        Scml source = getEditor().getSource();
        for (ScmlToolBarAction a : getActions()) {
            a.updateState();

            if (a.isAvailable(source) && a.isEnabled()) {
                hasEnabledActions = true;
            }
        }

        setEnabled(hasEnabledActions);
    }

    @Override
    public Component getToolbarPresenter() {
        if (button != null) {
            return button;
        }
        final ScmlToolBarAction[] actions = getActions();
        Arrays.sort(actions, new Comparator<ScmlToolBarAction>() {

            @Override
            public int compare(ScmlToolBarAction a1, ScmlToolBarAction a2) {
                if (a1 == null && a2 == null) {
                    throw new NullPointerException("Can't compare null actions");
                }
                if (a1.getGroupType() == a2.getGroupType()) {
                    return a1.getPosition() - a2.getPosition();
                }
                return a1.getGroupType() - a2.getGroupType();
            }
        });

        JPopupMenu dropdownPopup = new ScmlToolBarDropdownPopup(actions);

        button = new PopupButton(dropdownPopup);
        button.setIcon(getIcon());

        String tooltip = getTooltip();

        if (tooltip != null) {
            button.setToolTipText(getTooltip());
        }

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent evt, ScmlEditorPane pane) {
    }

    @Override
    public String getAcceleratorKey() {
        return null;
    }

    private static class ScmlToolBarDropdownPopup extends JPopupMenu {

        private ScmlToolBarAction[] actions;
        private JMenuItem[] items;
        private SeparatorPool separatorPool = new SeparatorPool();

        public ScmlToolBarDropdownPopup(ScmlToolBarAction[] actions) {
            this.actions = actions;
            items = new JMenuItem[actions.length];
            for (int i = 0; i < actions.length; i++) {
                items[i] = createDropdownItem(actions[i]);
            }
        }

        @Override
        public void setVisible(boolean visible) {
            removeAll();
            separatorPool.restore();
            ScmlToolBarAction prevAction = null;
            for (int i = 0; i < actions.length; i++) {
                actions[i].updateState();
                if ((actions[i].isAvailable(actions[i].getEditor().getSource()) && actions[i].isEnabled())) {
                    if (prevAction != null && prevAction.getGroupType() != actions[i].getGroupType()) {
                        add(separatorPool.getNextSeparator());
                    }
                    add(items[i]);
                    prevAction = actions[i];
                }
            }
            super.setVisible(visible);
        }

        private static class SeparatorPool {

            private static int INIT_COUNT = 7;
            private Vector<JSeparator> separators;
            private int nextIndex;

            public SeparatorPool() {
                separators = new Vector<JSeparator>(INIT_COUNT, INIT_COUNT);
                for (int i = 0; i < INIT_COUNT; i++) {
                    separators.add(new JSeparator(JSeparator.HORIZONTAL));
                }
                nextIndex = 0;
            }

            public JSeparator getNextSeparator() {
                if (nextIndex >= separators.size()) {
                    for (int i = 0; i < INIT_COUNT; i++) {
                        separators.add(new JSeparator(JSeparator.HORIZONTAL));
                    }
                }
                return separators.elementAt(nextIndex++);
            }

            public void restore() {
                nextIndex = 0;
            }
        }

        private JMenuItem createDropdownItem(final Action action) {
            if (action instanceof Presenter.Popup) {
                return ((Presenter.Popup) action).getPopupPresenter();
            }
            JMenuItem item = new JMenuItem(Actions.cutAmpersand((String) action.getValue(NAME)));
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerformed(e);
                }
            });
            Object icon = action.getValue(BaseAction.SMALL_ICON);
            if (icon != null) {
                item.setIcon((Icon) icon);
            }
            Object title = action.getValue(BaseAction.POPUP_MENU_TEXT);
            if (item != null) {
                item.setText((String) title);
            }

            Object tooltip = action.getValue(BaseAction.SHORT_DESCRIPTION);
            if (tooltip != null) {
                item.setToolTipText((String) tooltip);
            }
            return item;
        }
    }
}
