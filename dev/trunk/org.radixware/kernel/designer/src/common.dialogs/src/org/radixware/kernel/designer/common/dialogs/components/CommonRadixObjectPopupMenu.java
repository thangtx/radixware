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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class CommonRadixObjectPopupMenu extends javax.swing.JPopupMenu {

    public CommonRadixObjectPopupMenu(final IRadixObjectPopupMenuOwner owner){
        final JMenuItem openEditorItem = new JMenuItem(NbBundle.getMessage(CommonRadixObjectPopupMenu.class, "ChooserPopup-OpenEditorTip"));
        ActionListener openEditorListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Object selected = owner.getSelectedRadixObject();
                if (selected != null)
                    EditorsManager.getDefault().open((RadixObject)selected);
            }

        };
        openEditorItem.addActionListener(openEditorListener);

        final JMenuItem selectInTreeItem = new JMenuItem(NbBundle.getMessage(CommonRadixObjectPopupMenu.class, "ChooserPopup-GoToObjectTip"));
        ActionListener selectInTreeListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Object selected = owner.getSelectedRadixObject();
                if (selected != null)
                   NodesManager.selectInProjects((RadixObject)selected);
            }

        };
        selectInTreeItem.addActionListener(selectInTreeListener);

        add(openEditorItem);
        add(selectInTreeItem);
        
        PopupMenuListener menuListener = new PopupMenuListener(){

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                boolean hasSelection = owner.isPopupMenuAvailable();
                openEditorItem.setEnabled(hasSelection);
                selectInTreeItem.setEnabled(hasSelection);
            }

        };
        addPopupMenuListener(menuListener);
    }

    public interface IRadixObjectPopupMenuOwner {

        public RadixObject getSelectedRadixObject();

        public boolean isPopupMenuAvailable();

    }

}
