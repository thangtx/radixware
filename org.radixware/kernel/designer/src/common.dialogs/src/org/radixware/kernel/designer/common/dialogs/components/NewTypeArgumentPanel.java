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
 * NewTypeArgumentPanel.java
 *
 * Created on 12.05.2009, 14:23:50
 */

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType.DefaultTypeFilter;
import org.radixware.kernel.designer.common.dialogs.choosetype.ETypeNature;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class NewTypeArgumentPanel extends javax.swing.JPanel {

    private class NewTypeArgumentDisplayer extends ModalDisplayer
                                        implements ChangeListener {

        public NewTypeArgumentDisplayer(NewTypeArgumentPanel panel){
            super(panel);
            panel.addChangeListener(this);
            getDialogDescriptor().setValid(panel.isComplete());
            setTitle(NbBundle.getMessage(NewTypeArgumentPanel.class, "TypeArgumentTip"));
        }

        @Override
        protected void apply() {
            argument.setName(nameEditor.getCurrentName());
            argument.setDerivation((AdsTypeDeclaration.TypeArgument.Derivation)derivationEditor.getSelectedItem());
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource().equals(getComponent())){
                getDialogDescriptor().setValid(((NewTypeArgumentPanel)getComponent()).isComplete());
            }
        }

    }

    public boolean edit(){
        NewTypeArgumentDisplayer displayer = new NewTypeArgumentDisplayer(this);
        return displayer.showModal();
    }


    private AdsTypeDeclaration.TypeArgument argument;
    /** Creates new form NewTypeArgumentPanel */
    public NewTypeArgumentPanel(final AdsDefinition context,final AdsTypeDeclaration.TypeArgument argument) {
        this.argument = argument;
        initComponents();
        DefaultComboBoxModel model = new DefaultComboBoxModel(new Object[]{AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS,
                                                                           AdsTypeDeclaration.TypeArgument.Derivation.SUPER});
        derivationEditor.setModel(model);
        derivationEditor.setSelectedItem(AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS);
        final JButton typeBtn = typeEditor.addButton();
        typeBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        ActionListener btnListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTypeFilter typeFilter = new ChooseType.DefaultTypeFilter(context, null);
                typeFilter.except(ETypeNature.JAVA_PRIMITIVE);
                AdsTypeDeclaration newtype = ChooseType.getInstance().chooseType(typeFilter);

                if (newtype != null){
                    argument.setType(newtype);
                    NewTypeArgumentPanel.this.typeEditor.setValue(newtype.getName(context));
                    NewTypeArgumentPanel.this.changeSupport.fireChange();
                }
            }

        };
        typeBtn.addActionListener(btnListener);
        typeEditor.setReadOnly(true);

        ChangeListener nameListener = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                argument.setName(NewTypeArgumentPanel.this.nameEditor.getCurrentName());
                NewTypeArgumentPanel.this.changeSupport.fireChange();
            }

        };
        String name = argument.getName();
        boolean hasName = name != null && !name.isEmpty() && !name.equals("?");
        nameCheck.setSelected(hasName);
        nameEditor.setCurrentName(name);
        nameLabel.setEnabled(hasName);
        nameEditor.setEnabled(hasName);

        AdsTypeDeclaration argType = argument.getType();
        String typeEditorValue = argType != null ? argType.getName(context) : UNDEFINED;
        boolean hasType = argType != null;
        typeEditor.setValue(typeEditorValue);
        typeCheck.setSelected(hasType);
        typeLabel.setEnabled(hasType);
        typeBtn.setEnabled(hasType);
        typeCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = NewTypeArgumentPanel.this.typeCheck.isSelected();
                NewTypeArgumentPanel.this.typeLabel.setEnabled(isSelected);
                typeBtn.setEnabled(isSelected);
                NewTypeArgumentPanel.this.argument.setType(isSelected ? AdsTypeDeclaration.UNDEFINED : null);
                NewTypeArgumentPanel.this.changeSupport.fireChange();
            }

        });

        derivationEditor.setActionCommand(argument.getDerivation().toString());

        nameEditor.addChangeListener(nameListener);
        nameCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = NewTypeArgumentPanel.this.nameCheck.isSelected();
                NewTypeArgumentPanel.this.nameLabel.setEnabled(isSelected);
                NewTypeArgumentPanel.this.nameEditor.setEnabled(isSelected);
                if (!isSelected){
                    boolean isAnonymus = argument.getName().equals("?");
                    if (isAnonymus){
                        NewTypeArgumentPanel.this.nameEditor.setCurrentName("?");
                    } else  {
                        argument.setName(null);
                        NewTypeArgumentPanel.this.nameEditor.setCurrentName(argument.getName());
                    }
                }else{
                    NewTypeArgumentPanel.this.nameEditor.setCurrentName(argument.getName());
                }
                NewTypeArgumentPanel.this.changeSupport.fireChange();
            }

        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        typeEditor = new org.radixware.kernel.common.components.ExtendableTextField();
        derivationEditor = new javax.swing.JComboBox();
        nameEditor = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        nameCheck = new javax.swing.JCheckBox();
        typeCheck = new javax.swing.JCheckBox();

        setMinimumSize(getPreferredSize());

        nameLabel.setText(org.openide.util.NbBundle.getMessage(NewTypeArgumentPanel.class, "NameTip")); // NOI18N
        nameLabel.setEnabled(false);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(NewTypeArgumentPanel.class, "DerivationTip")); // NOI18N

        typeLabel.setText(org.openide.util.NbBundle.getMessage(NewTypeArgumentPanel.class, "TypeTip")); // NOI18N
        typeLabel.setEnabled(false);

        nameEditor.setEnabled(false);

        nameCheck.setText(org.openide.util.NbBundle.getMessage(NewTypeArgumentPanel.class, "NewTypeArgument-NameCheck")); // NOI18N

        typeCheck.setText(org.openide.util.NbBundle.getMessage(NewTypeArgumentPanel.class, "NewTypeArgument-TypeCheck")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameCheck)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(nameLabel)
                            .addComponent(typeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stateDisplayer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(typeEditor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(nameEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(derivationEditor, 0, 338, Short.MAX_VALUE)))
                    .addComponent(typeCheck))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(derivationEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(typeCheck)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox derivationEditor;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox nameCheck;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameEditor;
    private javax.swing.JLabel nameLabel;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    private javax.swing.JCheckBox typeCheck;
    private org.radixware.kernel.common.components.ExtendableTextField typeEditor;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables

    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    private StateManager state = new StateManager(this);

    private final String UNDEFINED = AdsTypeDeclaration.UNDEFINED.getName();

    public boolean isComplete() {
        if (typeEditor.getValue().equals(UNDEFINED) && typeCheck.isSelected()){
            state.error(NbBundle.getMessage(NewTypeArgumentPanel.class, "TypeArgumentError"));
            return false;
        } else {
            if (nameEditor.isEnabled()){
                String currentName = nameEditor.getCurrentName();
                boolean res = !currentName.isEmpty() && !currentName.equals("?");
                if (res)
                    state.ok();
                else
                    state.error("incorrect name");
                return res;
            }else{
                state.ok();
                return true;
            }

        }
    }


}
