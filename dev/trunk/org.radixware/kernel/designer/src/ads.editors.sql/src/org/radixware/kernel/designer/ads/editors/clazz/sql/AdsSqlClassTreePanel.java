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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.PopupMenuAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.PopupButton;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;


public class AdsSqlClassTreePanel extends JPanel {

    private AdsSqlClassCodeEditor editor;
    private AdsSqlClassTree tree;
    private JToolBar toolbar = new JToolBar();
    private ISqlDef sqlClass;
    public static final String ADD_OBJECT_TO_TREE_POPUP = "add-object-to-tree-popup";

    public void update() {
        tree.update();
        setReadOnly(sqlClass.isReadOnly());
    }

    public void open(ISqlDef sqlClass, OpenInfo info) {
        ISqlDef oldValue = this.sqlClass;
        this.sqlClass = sqlClass;
        tree.open(sqlClass, info);
        firePropertyChange("sqlClass", oldValue, sqlClass);
        setReadOnly(sqlClass.isReadOnly());
    }

    /**
     * Creates new form AdsSqlClassTreePanel
     */
    public AdsSqlClassTreePanel(AdsSqlClassCodeEditor editor) {
        this.editor = editor;
        tree = new AdsSqlClassTree(editor);

        initComponents();
        initToolbar();

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);

        JScrollPane jsp = new JScrollPane(tree);
        add(jsp, BorderLayout.CENTER);

    }

    private void initToolbar() {
        toolbar.setFloatable(false);
        toolbar.setBorder(BorderFactory.createLineBorder(this.getBackground().darker()));

        JButton bt;

        final JPopupMenu menu = new JPopupMenu();
        menu.add(new AdsSqlClassTreeActions.AddUsedTableToTree().getPopupPresenter());
        final JMenuItem customParameterPresenter = new AdsSqlClassTreeActions.AddCustomParameter().getPopupPresenter();
        menu.add(customParameterPresenter);
        final JMenuItem newFieldActionPresenter = new AdsSqlClassTreeActions.AddNewFieldAction().getPopupPresenter();
        menu.add(newFieldActionPresenter);

        this.addPropertyChangeListener("sqlClass", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (tree.getSqlDef() instanceof AdsSqlClassDef) {
                    if (tree.getSqlDef() instanceof AdsReportClassDef && menu.getSubElements().length < 5) {
                        menu.add(new AdsSqlClassTreeActions.AddDynamicProperty().getPopupPresenter());
                    }
                    if (tree.getSqlDef() instanceof AdsStatementClassDef || tree.getSqlDef() instanceof AdsProcedureClassDef) {
                        newFieldActionPresenter.setVisible(false);
                    } else {
                        newFieldActionPresenter.setVisible(true);
                    }
                } else {
                    newFieldActionPresenter.setVisible(false);
                    customParameterPresenter.setVisible(false);
                }
            }
        });



        bt = new PopupButton(menu);
        bt.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        bt.setName(ADD_OBJECT_TO_TREE_POPUP);
        toolbar.add(bt);

        toolbar.add(new TreeDelegateAction(tree, new AdsSqlClassTreeActions.InsertTagToEditor()).getToolbarPresenter());
        toolbar.add(new TreeDelegateAction(tree, new AdsSqlClassTreeActions.EditObjectInTree()).getToolbarPresenter());
        toolbar.add(new TreeDelegateAction(tree, new AdsSqlClassTreeActions.RemoveSelectedObjects()).getToolbarPresenter());
        toolbar.add(new TreeDelegateAction(tree, new AdsSqlClassTreeActions.MoveParameterUp()).getToolbarPresenter());
        toolbar.add(new TreeDelegateAction(tree, new AdsSqlClassTreeActions.MoveParameterDown()).getToolbarPresenter());

        for (Component component : toolbar.getComponents()) {
            if (component instanceof JButton) {
                final JButton button = (JButton) component;
                RadixNbEditorUtils.processToolbarButton(button);
            }
        }
    }

    public AdsSqlClassCodeEditor getEditor() {
        return editor;
    }

    public AdsSqlClassTree getTree() {
        return tree;
    }

    private void setReadOnly(boolean readOnly) {
        for (Component c : toolbar.getComponents()) {
            if (ADD_OBJECT_TO_TREE_POPUP.equals(c.getName())) {
                c.setEnabled(!readOnly);
            }
        }
    }

    public static class TreeDelegateAction extends AbstractAction implements TreeSelectionListener, Presenter.Toolbar {

        AdsSqlClassTree tree;
        PopupMenuAction delegate;

        public TreeDelegateAction(AdsSqlClassTree tree, PopupMenuAction delegate) {
            this.tree = tree;
            tree.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {
                    updateState();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    updateState();
                }
            });
            tree.getEditor().getPane().addCaretListener(new CaretListener() {

                @Override
                public void caretUpdate(CaretEvent e) {
                    updateState();
                }
            });
            this.delegate = delegate;
            tree.addTreeSelectionListener(this);
            updateState();
        }

        public final void updateState() {
            setEnabled(delegate.isAvailable(tree));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            e.setSource(tree);
            delegate.actionPerformed(e);
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getNewLeadSelectionPath();
            if (path != null && path.getLastPathComponent() instanceof AdsSqlClassTree.Node) {
                setEnabled(delegate.isAvailable(tree));
            } else {
                setEnabled(false);
            }

        }

        @Override
        public Component getToolbarPresenter() {
            JButton bt = new JButton(this);
            bt.setText("");
            Object val = delegate.getValue(SMALL_ICON);
            if (val instanceof Icon) {
                bt.setIcon((Icon) val);
            }
            val = delegate.getValue(SHORT_DESCRIPTION);
            if (val instanceof String) {
                bt.setToolTipText((String) val);
            }
            return bt;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The contesnt of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
