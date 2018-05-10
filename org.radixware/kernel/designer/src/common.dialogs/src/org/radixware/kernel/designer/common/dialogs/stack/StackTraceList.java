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

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.SrcPositionLocator;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class StackTraceList extends JPanel {
    static String PROCESS = "stackAnalizerInProcess";

    private static class StackTreeModel extends DefaultListModel {
        
        StackTreeModel() {
        }
    }

    private final class Renderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList tree, Object value, int index, boolean isSelected, boolean hasFocus) {
            Component c = super.getListCellRendererComponent(tree, value, index, isSelected, hasFocus);
            StackTreeModelItem item = (StackTreeModelItem) value;
            JLabel label = new JLabel(item.text);
            label.setOpaque(true);

            RadixObject object = null;
            String name = "";
            if (!item.locations.isEmpty()){
                object = item.locations.get(0).getRadixObject().getOwnerDefinition();
                if (object != null){
                    if (item.locations.size() > 1){
                        name = object.getName() + "...";
                    } else {
                        name = object.getQualifiedName();
                    }
                }
            } else if (!item.defs.isEmpty()) {
                object = item.defs.get(0);
                if (item.defs.size() > 1){
                    name = object.getName() + "(mispositioning)"+ "...";
                } else {
                    name = object.getQualifiedName() + "(mispositioning)";
                }
            }
            if (object != null) {
                label.setForeground(c.getForeground());
                label.setText(name);
                label.setIcon(object.getIcon().getIcon());
            } else {
                label.setForeground(isSelected ? c.getForeground() : Color.GRAY);
                label.setIcon(RadixWareIcons.JAVA.JAVA.getIcon());
            }
            label.setBackground(c.getBackground());
            return label;
        }
    }
    /**
     * Creates new form StackTraceList
     */
    private Branch branch = null;
    private String traceText = "";
    private final StackTreeModel model;
    private final StackAnalyzer stackAnalyzer = new StackAnalyzer();

    StackAnalyzer.StackChangeListener listener = new StackAnalyzer.StackChangeListener() {

        @Override
        public void onEvent(final StackAnalyzer.StackEvent e) {
            switch (e.getType()) {
                case PROCESS:
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            model.addElement(e.getStackItem());
                        }
                    });
                    break;
                case STOP:
                    firePropertyChange(PROCESS, true, false);
            }
        }
    };

    public StackTraceList() {
        initComponents();

        model = new StackTreeModel();
        stackTree.setModel(model);

        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem copyToClipboard = menu.add(new AbstractAction("Copy to clipboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Object selectedValue = stackTree.getSelectedValue();

                if (selectedValue != null) {
                    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                    final StringSelection stringSelection = new StringSelection(selectedValue.toString());
                    clipboard.setContents(stringSelection, stringSelection);
                }

            }
        });
        final JMenuItem goToSource = menu.add(new AbstractAction("Go to source") {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToSource();
            }
        });

        stackTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final int count = e.getClickCount();
                    if (count == 2) {
                        gotToSelectedItem();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                check(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check(e);
            }

            private void check(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    final int size = stackTree.getModel().getSize();
                    copyToClipboard.setEnabled(size > 0);
                    goToSource.setEnabled(size > 0);

                    stackTree.setSelectedIndex(stackTree.locationToIndex(e.getPoint()));
                    menu.show(stackTree, e.getX(), e.getY());
                }
            }
        });
        stackTree.setCellRenderer(new Renderer());
    }
    
    void addListner() {
        stackAnalyzer.addEventListener(listener);
    }
    
    void removeListener() {
        stackAnalyzer.removeEventListener(listener);
    }
    
    void gotToSelectedItem() {
        if (stackTree.getSelectedIndex() >= 0 && stackTree.getSelectedIndex() < model.getSize()) {
            StackTreeModelItem item = (StackTreeModelItem) model.getElementAt(stackTree.getSelectedIndex());
            final ChooseStackTraceItemPanel panel = new ChooseStackTraceItemPanel();
            ModalDisplayer displayer = new ModalDisplayer(panel, "Choose definition");
            
            if (!item.locations.isEmpty()) {
                SrcPositionLocator.SrcLocation location = null;
                if (item.locations.size() == 1){
                    location = item.locations.get(0);
                } else {
                    panel.open(item.locations);
                    if (displayer.showModal()){
                        location = panel.getSelectedLocation();
                    }
                }
                if (location != null){
                    DialogUtils.goToObject(location.getRadixObject(), new OpenInfo(location.getScml(), Lookups.fixed(location)));
                }
            } else if (!item.defs.isEmpty()) {
                Definition definition = null;
                if (item.defs.size() == 1){
                    definition = item.defs.get(0);
                } else {
                    panel.open(item.defs);
                    if (displayer.showModal()){
                        definition = panel.getSelectedDefinition();
                    }
                }
                
                if (definition != null){
                    DialogUtils.goToObject(definition);
                }
            }
        }
    }

    boolean isExposableSelection() {
        if (stackTree.getSelectedIndex() >= 0 && stackTree.getSelectedIndex() < model.getSize()) {
            StackTreeModelItem item = (StackTreeModelItem) model.getElementAt(stackTree.getSelectedIndex());
            return !item.locations.isEmpty() || !item.defs.isEmpty();
        } else {
            return false;
        }
    }

    void addSelectionListener(ListSelectionListener listener) {
        stackTree.getSelectionModel().addListSelectionListener(listener);
    }

    public void goToSource() {

        if (stackTree.getSelectedIndex() >= 0 && stackTree.getSelectedIndex() < model.getSize()) {
            final StackTreeModelItem item = (StackTreeModelItem) model.getElementAt(stackTree.getSelectedIndex());
            final ChooseStackTraceItemPanel panel = new ChooseStackTraceItemPanel();
            ModalDisplayer displayer = new ModalDisplayer(panel, "Choose definition");
            
            if (item != null && !item.locations.isEmpty()) {
                SrcPositionLocator.SrcLocation location = null;
                if (item.locations.size() == 1){
                    location = item.locations.get(0);
                } else {
                    panel.open(item.locations);
                    if (displayer.showModal()){
                        location = panel.getSelectedLocation();
                    }
                }
                if (location != null){
                    DialogUtils.viewSource(location.getRadixObject(), item.environment, JavaSourceSupport.CodeType.EXCUTABLE, item.lineNumber);
                }
                
            } else if (item != null && !item.defs.isEmpty()) {
                Definition definition = null;
                if (item.defs.size() == 1){
                    definition = item.defs.get(0);
                } else {
                    panel.open(item.defs);
                    if (displayer.showModal()){
                        definition = panel.getSelectedDefinition();
                    }
                }
                
                if (definition != null){
                    DialogUtils.viewSource(definition, item.environment, JavaSourceSupport.CodeType.EXCUTABLE, item.lineNumber);
                }
            }
        }
    }

    Branch getBranch() {
        return branch;
    }

    void setBranch(final Branch branch) {
        this.branch = branch;
    }
    
    void parseStackTrace() {
       firePropertyChange(PROCESS, false, true);
       model.removeAllElements();
       stackAnalyzer.open(branch, traceText);
       RequestProcessor.getDefault().post(stackAnalyzer);
    }

    void parseStackTrace(String traceText) {
        synchronized (this) {
            this.traceText = traceText;
        }
        parseStackTrace();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        stackTree = new javax.swing.JList();

        stackTree.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(stackTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList stackTree;
    // End of variables declaration//GEN-END:variables
}
