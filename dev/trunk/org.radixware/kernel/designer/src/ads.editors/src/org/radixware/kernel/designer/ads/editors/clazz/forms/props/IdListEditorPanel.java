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
 * IdListEditorPanel.java
 *
 * Created on Jan 16, 2012, 10:23:27 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;

public class IdListEditorPanel extends javax.swing.JPanel implements PropertyChangeListener {

    /**
     * Creates new form IdListEditorPanel
     */
    private final IdListEditor editor;
    private final ListModel model = new ListModel();

    private final class ListItem {

        final Id id;

        public ListItem(Id id) {
            this.id = id;
        }

        @Override
        public String toString() {
            AdsUIItemDef item = getWidget().getOwnerUIDef().getWidget().findWidgetById(id);
            if (item != null) {
                return item.getQualifiedName(getWidget());
            } else {
                return id.toString();
            }
        }
    }

    private final class ListModel extends DefaultListModel {

        public ListModel() {
        }
    }

    public AdsUIProperty.IdListProperty getProperty() {
        return (AdsUIProperty.IdListProperty) editor.getValue();
    }

    public IdListEditorPanel(IdListEditor editor, PropertyEnv env) {
        initComponents();
        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);
        this.editor = editor;

        AdsUIProperty.IdListProperty prop = (AdsUIProperty.IdListProperty) editor.getValue();

        for (Id id : prop.getIds()) {
            model.add(model.getSize(), new ListItem(id));
        }

        lstObjects.setModel(model);

        lstObjects.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtons();
            }
        });
        btAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        btRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });
        btMoveUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                move(-1);
            }
        });
        btMoveDown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                move(1);
            }
        });
        updateButtons();

        btAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btRemove.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        btMoveUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        btMoveDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
    }

    private void updateSelection(Id idToSelect) {
        ListItem select = null;

        if (idToSelect != null) {
            for (int i = 0; i < model.getSize(); i++) {
                if (select == null) {
                    select = (ListItem) model.get(i);
                }
                ListItem item = (ListItem) model.get(i);
                if (item.id == idToSelect) {
                    select = item;
                    break;
                }
            }
        }
        if (select != null) {
            lstObjects.setSelectedValue(select, true);
        }
    }

    private class Cfg extends ChooseDefinitionCfg {

        public Cfg() {
            super(getWidget().getOwnerUIDef(), new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsUIItemDef && ((AdsUIItemDef) radixObject).isActionWidget();
                }
            });
        }
    }

    private void add() {
        Definition def = ChooseDefinition.chooseDefinition(new Cfg());
        if (def != null) {
            ListItem item = new ListItem(def.getId());
            model.add(model.getSize(), item);
            updateSelection(def.getId());
        }
    }

    private void remove() {
        int index = getCurrentIndex();
        ListItem current = getCurrentItem();
        if (current != null) {
            model.removeElement(current);
        }
        if (index >= model.getSize()) {
            index = model.getSize() - 1;
        }
        if (index >= 0) {
            lstObjects.setSelectedIndex(index);
        }
        updateButtons();

    }

    private void move(int dir) {
        int current = getCurrentIndex();
        if (current >= 0 && current < model.getSize()) {
            if (current + dir >= 0 && current + dir < model.getSize()) {
                Object obj = model.get(current);
                Object obj2 = model.get(current + dir);
                model.setElementAt(obj, current + dir);
                model.setElementAt(obj2, current);
                updateSelection(((ListItem) obj).id);
            }
        }
    }

    private AdsUIItemDef getWidget() {
        return (AdsUIItemDef) editor.getNode();
    }

    private ListItem getCurrentItem() {
        int index = getCurrentIndex();
        if (index >= 0 && index < model.getSize()) {
            return (ListItem) model.get(index);
        } else {
            return null;
        }
    }

    private int getCurrentIndex() {
        return lstObjects.getSelectedIndex();
    }

    private int getItemCount() {
        return lstObjects.getModel().getSize();
    }

    private void updateButtons() {
        boolean isReadOnly = !((UIPropertySupport) editor.getSource()).canWrite();
        int selected = getCurrentIndex();
        int count = getItemCount();
        btAdd.setEnabled(!isReadOnly);
        btRemove.setEnabled(!isReadOnly && selected >= 0 && selected < count);
        btMoveUp.setEnabled(!isReadOnly && btRemove.isEnabled() && selected > 0);
        btMoveDown.setEnabled(!isReadOnly && btRemove.isEnabled() && selected < count - 1);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            AdsUIProperty.IdListProperty prop = getProperty();

            List<Id> ids = new LinkedList<>();
            for (int i = 0; i < model.getSize(); i++) {
                ids.add(((ListItem) model.get(i)).id);
            }
            prop.setIds(ids);

            editor.setValue(prop);
            try {
                ((UIPropertySupport) editor.getSource()).setValue(prop);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(IdListEditorPanel.class.getName()).log(Level.WARNING, null, ex);
            }
        }
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
        lstObjects = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btMoveUp = new javax.swing.JButton();
        btMoveDown = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(lstObjects);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btAdd.setText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btAdd.text")); // NOI18N
        btAdd.setToolTipText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btAdd.toolTipText")); // NOI18N
        btAdd.setFocusable(false);
        btAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btAdd);

        btRemove.setText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btRemove.text")); // NOI18N
        btRemove.setToolTipText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btRemove.toolTipText")); // NOI18N
        btRemove.setFocusable(false);
        btRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btRemove);
        jToolBar1.add(jSeparator1);

        btMoveUp.setText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btMoveUp.text")); // NOI18N
        btMoveUp.setToolTipText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btMoveUp.toolTipText")); // NOI18N
        btMoveUp.setFocusable(false);
        btMoveUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btMoveUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btMoveUp);

        btMoveDown.setText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btMoveDown.text")); // NOI18N
        btMoveDown.setToolTipText(org.openide.util.NbBundle.getMessage(IdListEditorPanel.class, "IdListEditorPanel.btMoveDown.toolTipText")); // NOI18N
        btMoveDown.setFocusable(false);
        btMoveDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btMoveDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btMoveDown);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btMoveDown;
    private javax.swing.JButton btMoveUp;
    private javax.swing.JButton btRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList lstObjects;
    // End of variables declaration//GEN-END:variables
}
