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


public class NavigationToolBarUi {
    private final javax.swing.JToolBar toolBar;
    private  JButton btnGoToPrev;//(Ctrl+K)
    private  JButton btnGoToNext;//(Ctrl+L)
    private  JButton btnGoToPrevUnchecked;//(Ctrl+<)
    private  JButton btnGoToNextUnchecked;//(Ctrl+>)
    private final MlsTablePanel panel;   
    private static final String STR_RELEASE="RELEASE";

    public  NavigationToolBarUi(final javax.swing.JToolBar toolBar,final MlsTablePanel panel) {
        this.toolBar=toolBar;
        this.toolBar.setFloatable(false);
        this.panel=panel;
        createToolBarUi();
    }

    private void createToolBarUi() {
        Icon icon = RadixWareIcons.MLSTRING_EDITOR.LEFT.getIcon(20);
        final Action actGoToPrev = new AbstractAction("GoToPrev", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                panel.goToPreviousTranslation();
            }
        };
        icon = RadixWareIcons.MLSTRING_EDITOR.RIGHT.getIcon(20);
        final Action actGoToNext = new AbstractAction("GoToNext", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                panel.goToNextTranslation();
            }
        };

        //icon = RadixWareDesignerIcon.WIDGETS.PREV_CHECKED_STRING.getIcon(20);
        icon = RadixWareIcons.MLSTRING_EDITOR.PREV_CHECKED_STRING.getIcon(20);
        final Action actGoToPrevUnch = new AbstractAction("GoToPrevUnchecked", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                panel.goToPreviousUncheckedTranslation();
            }
        };

        //icon = RadixWareDesignerIcon.WIDGETS.NEXT_CHECKED_STRING.getIcon(20);
        icon =  RadixWareIcons.MLSTRING_EDITOR.NEXT_CHECKED_STRING.getIcon(20);
        final Action  actGoToNextUnch= new AbstractAction("GoToNextUnchecked", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                panel.goToNextUncheckedTranslation();
            }
        };        
        
        btnGoToPrev = new JButton(actGoToPrev);
        KeyStroke release = KeyStroke.getKeyStroke(KeyEvent.VK_K,KeyEvent.CTRL_MASK,true);
        InputMap map = btnGoToPrev.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(release,STR_RELEASE);
        btnGoToPrev.getActionMap().put(STR_RELEASE, actGoToPrev);
        setButton(btnGoToPrev,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_PREV_TRANSLATION")+ "(Ctrl+K)");

        btnGoToNext = new JButton(actGoToNext);
        release = KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_MASK,true );
        map = btnGoToNext.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(release,STR_RELEASE);
        btnGoToNext.getActionMap().put(STR_RELEASE, actGoToNext);
        setButton(btnGoToNext,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_NEXT_TRANSLATION")+ "(Ctrl+L)");

        btnGoToPrevUnchecked = new JButton(actGoToPrevUnch);
        release = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK,true);
        map = btnGoToPrevUnchecked.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(release,STR_RELEASE);
        btnGoToPrevUnchecked.getActionMap().put(STR_RELEASE, actGoToPrevUnch);
        setButton(btnGoToPrevUnchecked,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_PREV_UNCHECKED_TRANSLATION")+"(Ctrl+<)");

        btnGoToNextUnchecked = new JButton(actGoToNextUnch);
        release = KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,KeyEvent.CTRL_DOWN_MASK,true);
        map = btnGoToNextUnchecked.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(release,STR_RELEASE);
        btnGoToNextUnchecked.getActionMap().put(STR_RELEASE, actGoToNextUnch);
        setButton(btnGoToNextUnchecked,NbBundle.getMessage(NavigationToolBarUi.class, "GO_TO_NEXT_UNCHECKED_TRANSLATION")+"(Ctrl+>)");
        
        
        toolBar.addSeparator();
    }

    private void setButton(final AbstractButton btn,final String toolTip) {
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
