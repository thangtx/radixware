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

package org.radixware.kernel.designer.ads.localization;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.ads.localization.source.MlsTablePanel;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.localization.actions.EditorAction;


public class NavigationToolBarUi {
    private final javax.swing.JToolBar toolBar;
    private  JButton btnGoToPrev;
    private  JButton btnGoToNext;
    private  JButton btnGoToPrevUnchecked;
    private  JButton btnGoToNextUnchecked;
    private final MlsTablePanel panel;   

    public  NavigationToolBarUi(final javax.swing.JToolBar toolBar,final MlsTablePanel panel) {
        this.toolBar=toolBar;
        this.toolBar.setFloatable(false);
        this.panel=panel;
        createToolBarUi();
    }

    private void createToolBarUi() {
        
        btnGoToPrev = new JButton(panel.getActionByKey(MultilingualEditorUtils.GO_TO_PREV));
        setButton(btnGoToPrev, NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_PREV_TRANSLATION"));

        btnGoToNext = new JButton(panel.getActionByKey(MultilingualEditorUtils.GO_TO_NEXT));
        setButton(btnGoToNext,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_NEXT_TRANSLATION"));

        
        btnGoToPrevUnchecked = new JButton(panel.getActionByKey(MultilingualEditorUtils.GO_TO_PREV_UNCHECKED));
        setButton(btnGoToPrevUnchecked,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_PREV_UNCHECKED_TRANSLATION"));

        btnGoToNextUnchecked = new JButton(panel.getActionByKey(MultilingualEditorUtils.GO_TO_NEXT_UNCHECKED));
        setButton(btnGoToNextUnchecked,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_NEXT_UNCHECKED_TRANSLATION"));
        
        
        toolBar.addSeparator();
    }

    private void setButton(final AbstractButton btn, final String toolTip) {
        btn.setFocusable(false);
        btn.setText(null);
        btn.setToolTipText(toolTip);
        btn.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(btn);
    }

    public void setReadOnly(final boolean readOnly) {
        btnGoToPrev.setEnabled(!readOnly);
        btnGoToNext.setEnabled(!readOnly);
        btnGoToPrevUnchecked.setEnabled(!readOnly);
        btnGoToNextUnchecked.setEnabled(!readOnly);
    }

   /* private abstract class KeyClickListener extends AbstractAction implements ActionListner{
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e)
        {
        doAction();
        }

        public void actionPerformed(ActionEvent e)
        {
        doAction();
        }
        public abstract void doAction();
    }

    private class Button1Listener extends KeyClickListener {
        JPanel parent;
        public Button1Listener(JPanel pParent)
        {
        parent = pParent;
        }
        public void doAction()
        {
        JOptionPane.showMessageDialog(parent, "You clicked button 1");
        }
    }*/

}
