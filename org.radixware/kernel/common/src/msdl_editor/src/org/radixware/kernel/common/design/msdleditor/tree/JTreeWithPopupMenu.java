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

package org.radixware.kernel.common.design.msdleditor.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class JTreeWithPopupMenu extends JTree implements ActionListener {
    final JPopupMenu popup;
    JMenuItem mi;
    Tree tree;
    enum EEventKind {
        NONE,
        EPRESS,
        ERELEASE
    }
    
    private EEventKind popupActivationEvent = EEventKind.NONE;

    public JTreeWithPopupMenu(final Tree tree) {
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.tree = tree;
        popup = new JPopupMenu();
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);

        addMouseListener(
            new MouseAdapter() {
                
            @Override
            public void mouseReleased(final MouseEvent e) {
                super.mouseReleased(e);
                processPopup(e, EEventKind.EPRESS);
            }
            
            @Override
            public void mousePressed(final MouseEvent e) {
                super.mousePressed(e);
                processPopup(e, EEventKind.EPRESS);
            }

            private void processPopup(final MouseEvent e, EEventKind from) {
                if (e.isPopupTrigger() && (from == popupActivationEvent || popupActivationEvent == EEventKind.NONE)) {
                    popupActivationEvent = from;
                    if (JTreeWithPopupMenu.this.updatedPopupMenu())
                        SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            popup.show((JComponent)e.getSource(), e.getX(), e.getY() );
                        }
                    });
                }
            }

        }
        );
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_DELETE) {
                    tree.delField();
                    return;
                }
                /*
                if (e.getKeyChar() == KeyEvent.VK_ADD) {
                    tree.addField();
                    return;
                }
                 */
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private ItemNode getCurrentNode() {
        TreePath path = this.getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (node != null)
                return (ItemNode) node;
        }
        return null;
    }

    public boolean updatedPopupMenu() {
        popup.removeAll();
        ItemNode node = getCurrentNode();
        if (node != null) {
            if (node instanceof HeaderFieldsNode || node instanceof FieldsNode || node instanceof VariantsNode) {
                JMenuItem item = new JMenuItem("Add New Field");
                item.addActionListener(this);
                item.setActionCommand("add");
                popup.add(item);
            }
            if (node instanceof FieldNode && !(node instanceof RootStructureNode) && !(node instanceof SequenceItemNode)) {
                JMenuItem item = new JMenuItem("Delete Selected Field");
                item.addActionListener(this);
                item.setActionCommand("delete");
                popup.add(item);
            }
            JMenuItem item = new JMenuItem("Check");
            item.addActionListener(this);
            item.setActionCommand("check");
            popup.add(item);
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ItemNode node = getCurrentNode();
        if (node!= null) {
            if (e.getActionCommand().equals("add")) {
                tree.addField();
                return;
            }
            if (e.getActionCommand().equals("delete")) {
                tree.delField();
                return;
            }
            if (e.getActionCommand().equals("check")) {
                tree.showErrors(node);
                return;
            }
        }
    }

}
