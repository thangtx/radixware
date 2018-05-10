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
package org.radixware.kernel.designer.common.dialogs.stack;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.actions.PasteAction;
import org.openide.util.Exceptions;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

public class StackTraceParserToolBar extends javax.swing.JPanel {

    private class BranchComboBoxModel implements ComboBoxModel {

        private final Object branchesLock = new Object();
        private List<Branch> branches = null;
        private ScheduledFuture f;

        BranchComboBoxModel() {
            final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (branchesLock) {
                        if (branches == null || branches.isEmpty()) {
                            getBranches();
                        } else {
                            if (f != null) {
                                f.cancel(true);
                            }
                        }
                    }
                }
            };

            f = service.scheduleWithFixedDelay(runnable, 1000, 1000, TimeUnit.MILLISECONDS);
        }

        private List<Branch> getBranches() {
            boolean shouldFire = false;
            try {
                boolean isUserMode = BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null;
                synchronized (branchesLock) {
                    if (branches == null || branches.isEmpty()) {
                        Collection<Branch> loadedBranches = RadixFileUtil.getOpenedBranches();
                        if (isUserMode){
                                try{
                                    Branch b = UserExtensionManagerCommon.getInstance().getBranch();
                                    loadedBranches.add(b);
                                } catch (IOException e){
                                }
                        }
                        
                        if (loadedBranches.isEmpty()) {
                            return Collections.emptyList();
                        } else {
                            branches = new ArrayList<>(loadedBranches);
                            setSelectedItem(branches.get(0));
                            shouldFire = true;
                        }
                    }
                    return branches;
                }
            } finally {
                if (shouldFire) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            fireChange();
                        }
                    });

                }
            }
        }

        @Override
        public void setSelectedItem(Object anItem) {
            traceList.setBranch((Branch) anItem);
            traceList.parseStackTrace();
        }

        @Override
        public Object getSelectedItem() {
            return traceList.getBranch();
        }

        @Override
        public int getSize() {
            return getBranches().size();
        }

        @Override
        public Object getElementAt(int index) {
            return getBranches().get(index);
        }
        private final LinkedList<ListDataListener> listeners = new LinkedList<ListDataListener>();

        private void fireChange() {
            List<ListDataListener> list;
            synchronized (listeners) {
                list = new ArrayList<>(listeners);
            }
            final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
            for (ListDataListener l : list) {
                l.contentsChanged(e);
            }
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            synchronized (listeners) {
                listeners.add(l);
            }
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }
    }

    private class CellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final String text;
            if (value == null) {
                text = "<Not specified>";
            } else {
                final Branch b = (Branch) value;
                File dir = b.getDirectory();
                text = b.getName() + (dir == null ? "" : "(" + dir.getPath() + ")");
            }
            this.setText(text);
            return this;
        }
    }
    /**
     * Creates new form StackTraceParserToolBar
     */
    private final StackTraceList traceList;

    private void paste(String stack) {
        final List<Branch> branches = new ArrayList<>(RadixFileUtil.getOpenedBranches());
        if (!branches.isEmpty()) {
            traceList.setBranch(branches.get(0));
        }
        traceList.parseStackTrace(stack);
    }

    public StackTraceParserToolBar(final StackTraceList traceList) {
        initComponents();
        this.traceList = traceList;
        btParse.setIcon(RadixWareIcons.CHECK.STACK.getIcon());
        btParse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final EnterStackTracePanel panel = new EnterStackTracePanel();
                ModalDisplayer displayer = new ModalDisplayer(panel, "Enter stack trace report");
                if (displayer.showModal()) {
                    paste(panel.getText());
                }
            }
        });
        btPaste.setIcon(SystemAction.get(PasteAction.class).getIcon());
        btPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ClipboardUtils.getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    try {
                        final String string = (String) ClipboardUtils.getClipboard().getData(DataFlavor.stringFlavor);
                        paste(string);
                    } catch (UnsupportedFlavorException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });
        btGo.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
        btGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traceList.gotToSelectedItem();
            }
        });
        cbBranch.setModel(new BranchComboBoxModel());
        cbBranch.setRenderer(new CellRenderer());

        cmdGoToSource.setIcon(RadixWareIcons.JAVA.JAVA.getIcon());
        cmdGoToSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traceList.goToSource();
            }
        });

        traceList.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btGo.setEnabled(traceList.isExposableSelection());
                cmdGoToSource.setEnabled(traceList.isExposableSelection());
            }
        });
        
        traceList.addPropertyChangeListener(StackTraceList.PROCESS, new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        setInProcess((boolean) evt.getNewValue());
                    }
                });
            }
        });

    }

    public void setInProcess(boolean readonly) {
        btPaste.setEnabled(!readonly);
        btParse.setEnabled(!readonly);
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btGo = new javax.swing.JButton();
        cmdGoToSource = new javax.swing.JButton();
        btParse = new javax.swing.JButton();
        btPaste = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        cbBranch = new javax.swing.JComboBox();

        jToolBar1.setFloatable(false);

        btGo.setText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.btGo.text")); // NOI18N
        btGo.setToolTipText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.btGo.toolTipText")); // NOI18N
        btGo.setEnabled(false);
        btGo.setFocusable(false);
        btGo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btGo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btGo);

        cmdGoToSource.setText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.cmdGoToSource.text")); // NOI18N
        cmdGoToSource.setToolTipText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.cmdGoToSource.toolTipText")); // NOI18N
        cmdGoToSource.setEnabled(false);
        cmdGoToSource.setFocusable(false);
        cmdGoToSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdGoToSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdGoToSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGoToSourceActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdGoToSource);

        btParse.setText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.btParse.text")); // NOI18N
        btParse.setToolTipText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.btParse.toolTipText")); // NOI18N
        btParse.setFocusable(false);
        btParse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btParse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btParse);

        btPaste.setText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.btPaste.text")); // NOI18N
        btPaste.setToolTipText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.btPaste.toolTipText")); // NOI18N
        btPaste.setFocusable(false);
        btPaste.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btPaste.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btPaste);
        jToolBar1.add(jSeparator1);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);

        cbBranch.setToolTipText(org.openide.util.NbBundle.getMessage(StackTraceParserToolBar.class, "StackTraceParserToolBar.cbBranch.toolTipText")); // NOI18N
        cbBranch.setPreferredSize(new java.awt.Dimension(150, 24));
        jToolBar1.add(cbBranch);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdGoToSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGoToSourceActionPerformed
    }//GEN-LAST:event_cmdGoToSourceActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btGo;
    private javax.swing.JButton btParse;
    private javax.swing.JButton btPaste;
    private javax.swing.JComboBox cbBranch;
    private javax.swing.JButton cmdGoToSource;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
