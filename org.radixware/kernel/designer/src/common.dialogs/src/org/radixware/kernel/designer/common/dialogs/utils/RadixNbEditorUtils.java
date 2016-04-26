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
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.settings.KeyBindingSettings;
import org.netbeans.api.editor.settings.MultiKeyBinding;
import org.openide.awt.Actions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.types.Id;


public class RadixNbEditorUtils {

    private static final Insets BUTTON_INSETS = new Insets(2, 1, 0, 1);//from netbeans sources 

    private RadixNbEditorUtils() {
    }

    public static List<KeyStroke> getKeyStrokeList(Action action, String mimeType) {
        Lookup mimeLookup = MimeLookup.getLookup(mimeType);
        KeyBindingSettings keyBindings = mimeLookup.lookup(KeyBindingSettings.class);
        if (keyBindings != null) {
            for (MultiKeyBinding mkb : keyBindings.getKeyBindings()) {
                if (mkb.getActionName().equals(action.getValue(Action.NAME))) {
                    return mkb.getKeyStrokeList();
                }
            }
        }
        return null;
    }

    public static void connect(AbstractButton button, Action action, String mimeType) {
        installKeyStroke(action, mimeType);
        Actions.connect(button, action);
    }

    public static void installKeyStroke(Action action, String mimeType) {
        if (mimeType == null) {
            return;
        }
        if (action.getValue(Action.ACCELERATOR_KEY) == null) {
            List<KeyStroke> keyStrokeList = getKeyStrokeList(action, mimeType);
            if (keyStrokeList != null && !keyStrokeList.isEmpty()) {
                action.putValue(Action.ACCELERATOR_KEY, keyStrokeList.get(0));
            }
        }
    }

    public static JButton createToolbarButton(Action a, String mimeType) {
        JButton button = new JButton();
        connect(button, a, mimeType);
        return button;
    }

    public static JMenuItem createPopupMenuItem(Action action, String mimeType) {
        JMenuItem item = new JMenuItem();
        installKeyStroke(action, mimeType);
        Actions.connect(item, action, true);
        return item;
    }

    /**
     * Apply NetBeans style to the ToolBar button
     * @param button
     */
    public static void processToolbarButton(AbstractButton button) {
        removeButtonContentAreaAndBorder(button);
        button.setMargin(BUTTON_INSETS);
        if (button instanceof AbstractButton) {
            button.addMouseListener(sharedMouseListener);
        }
        button.setFocusable(false);
    }

    //taken from netbeans sources to apply netbeans style for our toolbar buttons
    private static void removeButtonContentAreaAndBorder(AbstractButton button) {
        boolean canRemove = true;
        if (button instanceof JToggleButton) {
            canRemove = !button.isSelected();
        }
        if (canRemove) {
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
        }
    }
    //taken from netbeans sources to apply netbeans style for our toolbar buttons
    private static final MouseListener sharedMouseListener = new org.openide.awt.MouseUtils.PopupMouseAdapter() {

        public
        @Override
        void mouseEntered(MouseEvent evt) {
            Object src = evt.getSource();

            if (src instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) evt.getSource();
                if (button.isEnabled()) {
                    button.setContentAreaFilled(true);
                    button.setBorderPainted(true);
                }
            }
        }

        public
        @Override
        void mouseExited(MouseEvent evt) {
            Object src = evt.getSource();
            if (src instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) evt.getSource();
                removeButtonContentAreaAndBorder(button);
            }
        }

        @Override
        protected void showPopup(MouseEvent evt) {
        }
    };

    /**
     * Returns title of the action from the bundle of the action's class package
     * with {@link RadixNbEditorUtils#createTitleKey(java.lang.String) } key
     * @param action
     * @return
     */
    public static String getTitle(Action action) {
        if (action == null) {
            throw new NullPointerException();
        }
        return NbBundle.getMessage(action.getClass(), createTitleKey(action.getValue(Action.NAME).toString()));
    }

    /**
     * Returns ToolTip for the action from the bundle of the action's class package
     * with {@link RadixNbEditorUtils#createTooltipKey(java.lang.String) } key
     * @param action
     * @return
     */
    public static String getTooltip(Action action) {
        if (action == null) {
            throw new NullPointerException();
        }
        return NbBundle.getMessage(action.getClass(), createTitleKey(action.getValue(Action.NAME).toString()));
    }

    /**
     * Creates key for action title in the bundle by action name.
     * @param actionName
     * @return {@code  actionName + "-title";}
     */
    public static String createTitleKey(String actionName) {
        return actionName + "-title";
    }

    /**
     * Creates key for action ToolTip in the bundle by action name.
     * @param actionName
     * @return {@code actionName + "-tooltip";}
     */
    public static String createTooltipKey(String actionName) {
        return actionName + "-tooltip";
    }

    public static void copyIdToClipboard(final Id id) {
        final String idString = id.toString();
        Transferable t = new Transferable() {

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.stringFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor == DataFlavor.stringFlavor;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return idString;
            }
        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(t, null);
    }
}
