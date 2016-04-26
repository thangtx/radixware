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
 * NewParameterDialog.java
 *
 * Created on Feb 13, 2009, 2:56:51 PM
 */
package org.radixware.kernel.designer.ads.method.profile;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ITypeFilter;
import org.radixware.kernel.designer.common.dialogs.choosetype.LastUsedTypeCollection;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 *
 * @deprecated replaced by NewMethodParameterPanel
 */
@Deprecated
public class NewParameterPanel extends javax.swing.JPanel {

    private class NewParameterDisplayer extends ModalDisplayer implements ChangeListener {

        public NewParameterDisplayer(NewParameterPanel panel) {
            super(panel);
            panel.addChangeListener(this);
            getDialogDescriptor().setValid(panel.isComplete());
            setTitle(NbBundle.getMessage(NewParameterPanel.class, "PP-MethodParam"));
            panel.setMinimumSize(panel.getPreferredSize());
        }

        @Override
        protected void apply() {
            Utils.findOrCreatePreferences(LAST_BOX_STATE).putBoolean(BOXSTATE, box.isSelected());
            LastUsedTypeCollection instance = LastUsedTypeCollection.getInstance(TypeEditPanel.LAST_USED_PATH);

            if (value != null) {
                instance.addLastUsed(value.getType());
                instance.saveLastUsed();
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource().equals(getComponent())) {
                getDialogDescriptor().setValid(((NewParameterPanel) getComponent()).isComplete());
            }
        }
    }
    private NewParameterDisplayer displayer;
    private final JButton typeBtn;
    private static final String LAST_BOX_STATE = "NewParameterPanel-LastUsedBoxState";
    private final String BOXSTATE = "BoxState";
    private final AdsDefinition context;
    private final MethodParameter value;

    public boolean edit() {
        displayer = new NewParameterDisplayer(this);
        return displayer.showModal();
    }

    @SuppressWarnings("cast")
    public NewParameterPanel(final AdsDefinition context, final MethodParameter value, final IAdsTypedObject parameter, boolean readonly) {
        this.context = context;
        this.value = value;

        initComponents();
        ActionListener boxListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = box.isSelected();
                Utils.findOrCreatePreferences(LAST_BOX_STATE).putBoolean(BOXSTATE, selected);
                list.setVisible(selected);
                list.updateUI();
                NewParameterPanel.this.updateUI();
                if (selected) {
                    Rectangle currBounds = NewParameterPanel.this.getBounds();
                    Rectangle lastBounds = list.getBounds();

                    if (lastBounds.y == 0 || lastBounds.height == 0) {
                        int newY = box.getY() + box.getHeight() + 10;
                        lastBounds.setBounds(newY, box.getX(), box.getWidth(), list.getPreferredSize().height + 10);
                    }

                    int forCheck = currBounds.height - (lastBounds.y - currBounds.y);
                    boolean check = forCheck < lastBounds.height;

                    if (check) {
                        Rectangle commonBounds = new Rectangle(currBounds.x,
                            currBounds.y,
                            currBounds.width,
                            currBounds.height + (lastBounds.height - forCheck));
                        NewParameterPanel.this.setBounds(commonBounds);
                        Dialog parent = (Dialog) getDialogParent();
                        parent.setSize(parent.getWidth(), parent.getHeight() + (lastBounds.height - forCheck));
                        parent.repaint();
                    }
                }

            }
        };
        box.addActionListener(boxListener);

        final ITypeFilter filter = new ChooseType.DefaultTypeFilter(context, parameter);

        typeBtn = typeField.addButton();
        typeBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        typeBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsTypeDeclaration newType = ChooseType.getInstance().chooseType(filter);
                if (newType != null) {
                    value.setType(newType);
                    String newName = newType.getName(context);
                    typeField.setValue(newName);
                    namePanel.requestFocusInWindow();
                    changeSupport.fireChange();
                }
            }
        });
        typeBtn.setToolTipText(NbBundle.getMessage(NewParameterPanel.class, "ReturnTip"));
        typeBtn.setEnabled(false);
        namePanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                value.setName(namePanel.getCurrentName());
                changeSupport.fireChange();
            }
        });

        namePanel.setCurrentName(value.getName());
        typeField.setValue(value.getType().getName(context));
        typeBtn.setEnabled(!readonly);
        namePanel.setEditable(!readonly);

        try {
            Preferences pref = Utils.findPreferences(LAST_BOX_STATE);
            boolean selection = pref != null ? pref.getBoolean(BOXSTATE, false) : false;
            box.setSelected(selection);
        } catch (BackingStoreException ex) {
            box.setSelected(false);
            DialogUtils.messageError(ex);
        }

        list.setVisible(box.isSelected());
        list.open(TypeEditPanel.LAST_USED_PATH, filter);
        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                AdsTypeDeclaration newtype = list.getValue();
                if (newtype != null) {
                    value.setType(newtype);
                    String newName = newtype.getName(context);
                    typeField.setValue(newName);
                    changeSupport.fireChange();
                }

                if (e.getClickCount() == 2) {
                    if (displayer != null) {
                        displayer.close(true);
                    }
                }
            }
        });

        namePanel.requestFocusInWindow();

        chbVariable.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                value.setVariable(chbVariable.isSelected());
            }
        });
    }

    private Dialog getDialogParent() {
        for (Component component = this.getParent(); component != null; component = component.getParent()) {
            if (component instanceof Dialog) {
                return (Dialog) component;
            }
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        namePanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        typeField = new org.radixware.kernel.common.components.ExtendableTextField();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        box = new javax.swing.JCheckBox();
        list = new org.radixware.kernel.designer.common.dialogs.choosetype.LastUsedTypesPanel(context);
        chbVariable = new javax.swing.JCheckBox();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(NewParameterPanel.class, "NameTip")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(NewParameterPanel.class, "TypeTip")); // NOI18N

        typeField.setReadOnly(true);

        box.setText(org.openide.util.NbBundle.getMessage(NewParameterPanel.class, "NewParameterPanel-UsedBoxTip")); // NOI18N

        chbVariable.setText(org.openide.util.NbBundle.getMessage(NewParameterPanel.class, "NewParameterPanel.chbVariable.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stateDisplayer1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(namePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(typeField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(list, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chbVariable)
                            .addComponent(box))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(namePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(typeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chbVariable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(box)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox box;
    private javax.swing.JCheckBox chbVariable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private org.radixware.kernel.designer.common.dialogs.choosetype.LastUsedTypesPanel list;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel namePanel;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    private org.radixware.kernel.common.components.ExtendableTextField typeField;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public boolean isComplete() {
        return namePanel.isComplete();
    }
}
