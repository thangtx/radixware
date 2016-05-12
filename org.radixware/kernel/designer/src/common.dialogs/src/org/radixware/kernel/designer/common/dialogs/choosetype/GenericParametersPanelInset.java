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
 * GenericParametersPanelInset.java
 *
 * Created on Mar 26, 2009, 12:25:00 PM
 */

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class GenericParametersPanelInset extends javax.swing.JPanel {

    private AdsDefinition context;
    private IAdsTypedObject parameter;

    private AdsTypeDeclaration.TypeArguments arguments;

    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints c = new GridBagConstraints();
    private JPanel innerpane;
    /** Creates new form GenericParametersPanelInset */
    public GenericParametersPanelInset(AdsDefinition context, IAdsTypedObject parameter, AdsTypeDeclaration.TypeArguments arguments){
        setupUI();
        this.context = context;
        this.parameter = parameter;
        this.arguments = arguments;
        if (arguments != null && !arguments.isEmpty()){
            for (int i = 0; i <= arguments.getArgumentList().size()-1; i++){
                addParamEditor(arguments.getArgumentList().get(i), i);
            }
        }
    }

    private void setupUI(){
        initComponents();
        innerpane = new JPanel();
        innerpane.setLayout(gbl);
        setLayout(new BorderLayout());
        add(innerpane, BorderLayout.NORTH);
    }

    private void addParamEditor(final AdsTypeDeclaration.TypeArgument arg, final int row){
		arg.setName("?"); //by BAO temp solution for RADIX-1131
        JLabel name = new JLabel(arg.getName() + " " + arg.getDerivation().toString());
        final ExtendableTextField field = new ExtendableTextField(true);
        AdsTypeDeclaration argType = arg.getType();
        if (argType != null) {
            field.setValue(argType.getName(context));
        } else {
            field.setValue(NbBundle.getMessage(GenericParametersPanelInset.class, "ChooseGenericType-Tip"));
        }
        javax.swing.JButton btn = field.addButton();
        btn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        ActionListener btnListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AdsTypeDeclaration type = ChooseType.getInstance().chooseType(new ChooseType.DefaultTypeFilter(context, parameter));
                if (type != null){
                    arg.setType(type);
                    field.setValue(type.getName(context));
                }
                changeSupport.fireChange();
            }

        };
        btn.addActionListener(btnListener);

        c.gridx = 0; c.gridy = row;
        c.insets = new Insets(0, 0, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;c.weighty = 0.0;
        gbl.setConstraints(name, c);
        innerpane.add(name);

        c.gridx = 1; c.gridy = row;
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 1.0;c.weighty = 0.0;
        gbl.setConstraints(field, c);
        innerpane.add(field);
    }

    public LinkedList<AdsTypeDeclaration.TypeArgument> getGenericParameters(){
        LinkedList<AdsTypeDeclaration.TypeArgument> copy = new LinkedList<AdsTypeDeclaration.TypeArgument>();
        List<AdsTypeDeclaration.TypeArgument> old = arguments.getArgumentList();
        for (AdsTypeDeclaration.TypeArgument arg : old){
            copy.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance(arg.getName(), arg.getType(), arg.getDerivation()));
        }
        return copy;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 217, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    private StateManager state = new StateManager(this);

    public boolean isComplete(){
        List<AdsTypeDeclaration.TypeArgument> list = arguments.getArgumentList();
        for (int i = 0; i<= list.size()-1; i++){
            if (list.get(i).getType() == null){
                state.error(NbBundle.getMessage(GenericParametersPanelInset.class, "ChooseGenericType-Error")+list.get(i).getName(context));
                return false;
            }
        }
        state.ok();
        return true;
    }
}