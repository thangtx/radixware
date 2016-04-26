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

/*
 * EmbeddedSourceEditor.java
 *
 * Created on Nov 21, 2011, 11:56:42 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef.ClassSource;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef.SourcePart;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

class EmbeddedSourceEditor extends javax.swing.JPanel {

    private EmbeddedSourceEditorCodePanel editor;
    private boolean isEditable = true;

    private static class Item {

        private final SourcePart part;

        public Item(SourcePart part) {
            this.part = part;
        }

        @Override
        public String toString() {
            return this.part.getDescription().isEmpty() ? "Part #" + getPartIndex() : this.part.getDescription();
        }

        private int getPartIndex() {
            if (part.getContainer() != null) {
                return ((RadixObjects) part.getContainer()).indexOf(part);
            } else {
                return -1;
            }
        }
    }

    private static class ListModel extends DefaultListModel {

        private final JList list;

        private ListModel(JList list, ClassSource src) {
            this.list = list;
            setSize(src.size());
            int index = 0;
            for (SourcePart part : src) {

                set(index, new Item(part));
                index++;

            }
        }

        private void fireChange() {
            if (this.listenerList != null) {
                final ListDataEvent e = new ListDataEvent(list, ListDataEvent.CONTENTS_CHANGED, 0, getSize() - 1);
                for (ListDataListener l : this.listenerList.getListeners(ListDataListener.class)) {
                    l.contentsChanged(e);
                }
            }
        }
    }
    private ListModel model = null;
    private ClassSource src;

    /**
     * Creates new form EmbeddedSourceEditor
     */
    public EmbeddedSourceEditor() {
        initComponents();
        btAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        btAdd.setIcon(RadixWareDesignerIcon.EDIT.CREATE.ADD.getIcon());
        btAdd.setToolTipText("Add new source part");
        btAdd.setText("");
        btDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        btDelete.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btDelete.setToolTipText("Delete source part");
        btDelete.setText("");
        btMoveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveUp();
            }
        });
        btMoveUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        btMoveUp.setToolTipText("Move source part up");
        btMoveUp.setText("");
        btMoveDn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDn();
            }
        });
        btMoveDn.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        btMoveDn.setToolTipText("Move source part down");
        btMoveDn.setText("");
    }

    public void open(ClassSource src, OpenInfo openInfo) {
        this.src = src;
        update(openInfo);
    }

    public void update() {
        update((OpenInfo) null);
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        update();
    }

    private void add() {
        if (src != null) {
            final SourcePart part = src.addPart();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                    int index = src.indexOf(part);
                    if (index >= 0 && index < model.getSize()) {
                        lstParts.setSelectedIndex(index);
                    }
                }
            });

        }
    }

    private void delete() {
        SourcePart c = getCurrent();
        if (c != null && DialogUtils.messageConfirmation("Delete selected part?")) {
            c.delete();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });

        }
    }

    private void moveUp() {
        move(-1);
    }

    private void moveDn() {
        move(1);
    }

    private void move(int dir) {
        final SourcePart c = getCurrent();
        if (c != null) {
            int idx = src.indexOf(c);
            src.swap(idx, idx + dir);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                    int idx2 = src.indexOf(c);
                    if (idx2 >= 0 && idx2 < src.size()) {
                        lstParts.setSelectedIndex(idx2);
                    }
                }
            });
        }

    }

    private SourcePart getCurrent() {
        if (model == null) {
            return null;
        }
        int index = lstParts.getSelectedIndex();
        if (index >= 0 && index < model.getSize()) {
            return ((Item) model.get(index)).part;
        } else {
            return null;
        }
    }

    private void update(OpenInfo openInfo) {
        try {
            lstParts.removeListSelectionListener(listListener);
            if (src != null) {
                this.model = new ListModel(lstParts, src);
                int index = lstParts.getSelectedIndex();
                lstParts.setModel(model);

                if (index < 0 || index > model.getSize()) {
                    index = model.getSize() - 1;
                }
                if (index >= 0) {
                    lstParts.setSelectedIndex(index);
                }
                if (openInfo != null) {
                    RadixObject obj = openInfo.getTarget();
                    for (int i = 0; i < model.getSize(); i++) {
                        if (((Item) model.get(i)).part == obj || ((Item) model.get(i)).part.isParentOf(obj)) {
                            lstParts.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                onListSelectionChanged(openInfo);
            }
        } finally {
            lstParts.addListSelectionListener(listListener);
        }
    }

    @Override
    public boolean requestFocusInWindow() {
        if (editor != null) {
            return editor.requestFocusInWindow();
        } else {
            return super.requestFocusInWindow();
        }
    }

    private void updateButtonsState() {
        int index = lstParts.getSelectedIndex();

        if (src != null) {
            btAdd.setEnabled(isEditable && !src.isReadOnly());
            if (index >= 0 && index < model.getSize()) {
                btDelete.setEnabled(isEditable && !src.isReadOnly() && model.getSize() > 1);
                btMoveUp.setEnabled(isEditable && !src.isReadOnly() && index > 0);
                btMoveDn.setEnabled(isEditable && !src.isReadOnly() && index < model.getSize() - 1);
            } else {
                btDelete.setEnabled(false);
                btMoveUp.setEnabled(false);
                btMoveDn.setEnabled(false);
            }
        } else {
            btAdd.setEnabled(false);
            btDelete.setEnabled(false);
            btMoveUp.setEnabled(false);
            btMoveDn.setEnabled(false);
        }
    }
    private final ListSelectionListener listListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            onListSelectionChanged(null);
        }
    };

    private void onListSelectionChanged(OpenInfo openInfo) {
        boolean editorOpened = false;
        int index = lstParts.getSelectedIndex();
        if (index < 0) {
            if (model.getSize() > 0) {
                index = 0;
                lstParts.setSelectedIndex(0);
            }
        }

        if (index >= 0 && index < model.getSize()) {
            Item item = (Item) model.get(index);
            if (item != null) {
                editorOpened = openEditor(item, openInfo);
            }
        }
        if (!editorOpened) {
            lbNoSelected.setVisible(true);
            if (editor != null) {
                editorPanel.remove(editor);
                editor.removeChangeListener(cl);
                editor = null;
            }
            if (lbNoSelected.getParent() != editorPanel) {
                editorPanel.add(lbNoSelected, BorderLayout.CENTER);
            }
        }
        updateButtonsState();
    }
    private final ChangeListener cl = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            listNamesUpdate();
        }
    };

    private void listNamesUpdate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                model.fireChange();
            }
        });
    }

    private boolean openEditor(Item item, OpenInfo openInfo) {
        if (item != null) {
            if (editor == null) {
                editor = new EmbeddedSourceEditorCodePanel();
                if (lbNoSelected.getParent() != null) {
                    editorPanel.remove(lbNoSelected);
                }
                editorPanel.add(editor, BorderLayout.CENTER);
            }
            editor.open(item.part, openInfo);
            editor.setEditable(isEditable);
            editor.addChangeListener(cl);

            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstParts = new javax.swing.JList();
        jToolBar2 = new javax.swing.JToolBar();
        btAdd = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btMoveUp = new javax.swing.JButton();
        btMoveDn = new javax.swing.JButton();
        editorPanel = new javax.swing.JPanel();
        lbNoSelected = new javax.swing.JLabel();

        jToolBar1.setRollover(true);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(lstParts);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar2.setFloatable(false);

        btAdd.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditor.class, "EmbeddedSourceEditor.btAdd.text")); // NOI18N
        btAdd.setFocusable(false);
        btAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(btAdd);

        btDelete.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditor.class, "EmbeddedSourceEditor.btDelete.text")); // NOI18N
        btDelete.setFocusable(false);
        btDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(btDelete);
        jToolBar2.add(jSeparator1);

        btMoveUp.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditor.class, "EmbeddedSourceEditor.btMoveUp.text")); // NOI18N
        btMoveUp.setFocusable(false);
        btMoveUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btMoveUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMoveUpActionPerformed(evt);
            }
        });
        jToolBar2.add(btMoveUp);

        btMoveDn.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditor.class, "EmbeddedSourceEditor.btMoveDn.text")); // NOI18N
        btMoveDn.setFocusable(false);
        btMoveDn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btMoveDn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(btMoveDn);

        jPanel1.add(jToolBar2, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setLeftComponent(jPanel1);

        editorPanel.setLayout(new java.awt.BorderLayout());

        lbNoSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbNoSelected.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditor.class, "EmbeddedSourceEditor.lbNoSelected.text")); // NOI18N
        editorPanel.add(lbNoSelected, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(editorPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMoveUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btMoveUpActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btMoveDn;
    private javax.swing.JButton btMoveUp;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lbNoSelected;
    private javax.swing.JList lstParts;
    // End of variables declaration//GEN-END:variables
}
