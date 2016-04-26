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
 * TypeArgumentsEditor.java
 *
 * Created on 12.05.2009, 14:06:31
 */

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditor.CommonParametersEditorEvent;


public class TypeArgumentsEditor extends javax.swing.JPanel {

    private AdsTypeDeclaration.TypeArguments arguments;
    private AdsDefinition context;
    private boolean readonly;

    private ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
                int row = content.getCurrentRow();
                int argsSize = arguments.getArgumentList().size();
                if (row > -1 && row < argsSize){
                    AdsTypeDeclaration.TypeArgument current = arguments.getArgumentList().get(row);
                    String name = content.getCurrentRowName();
                    AdsTypeDeclaration.TypeArgument.Derivation derivation = content.getCurrentRowDerivation();
                    AdsTypeDeclaration type = content.getCurrentRowType();
                    AdsTypeDeclaration.TypeArgument newArgument = AdsTypeDeclaration.TypeArgument.Factory.newInstance(name, type, derivation);

                    arguments.remove(current);
                    arguments.add(row, newArgument);
                }
        }
    };

    /** Creates new form TypeArgumentsEditor */
    public TypeArgumentsEditor() {
        initComponents();
        content.addChangeListener(changeListener);
        content.addNameChangeListener(changeListener);
        CommonParametersEditor.ICommonParametersEditorEventListener contentListener  = new CommonParametersEditor.ICommonParametersEditorEventListener() {

            @Override
            public void onEvent(CommonParametersEditorEvent e) {
                if (!TypeArgumentsEditor.this.isUpdate){
                    if (e.type == CommonParametersEditor.ADD_BUTTON_PRESSED){
                        TypeArgumentsEditor.this.addButtonPressed();
                    } else if (e.type == CommonParametersEditor.REMOVE_BUTTON_PRESSED){
                        TypeArgumentsEditor.this.removeButtonPressed();
                    } else if (e.type == CommonParametersEditor.UP_BUTTON_PRESSED){
                        TypeArgumentsEditor.this.upButtonPressed();
                    } else if (e.type == CommonParametersEditor.DOWN_BUTTON_PRESSED){
                        TypeArgumentsEditor.this.downButtonPressed();
                    }
                }
            }

        };
        content.getEditorStateSupport().addEventListener(contentListener);
    }

    private void addButtonPressed(){
        AdsTypeDeclaration.TypeArgument a = AdsTypeDeclaration.TypeArgument.Factory.newInstance();
        NewTypeArgumentPanel panel = new NewTypeArgumentPanel(context, a);
        if (panel.edit()){
            arguments.add(a);
            content.addRow(a);
            changeSupport.fireChange();
        }
    }

    private void removeButtonPressed(){
        int row = content.getCurrentRow();
        if (row != -1){
            arguments.remove(row);
            content.removeRow(row);
            changeSupport.fireChange();
        }
    }

    private void upButtonPressed(){
        int row = content.getCurrentRow();
        if (row != -1){
            AdsTypeDeclaration.TypeArgument a = arguments.getArgumentList().get(row);
            arguments.remove(a);
            arguments.add(row-1, a);
        }
    }

    private void downButtonPressed(){
        int row = content.getCurrentRow();
        if (row != -1){
            AdsTypeDeclaration.TypeArgument a = arguments.getArgumentList().get(row);
            arguments.remove(a);
            arguments.add(row+1, a);
        }
    }

    public void open(AdsDefinition context, AdsTypeDeclaration.TypeArguments arguments, boolean readonly){
        this.context = context;
        this.arguments = arguments;
        this.readonly = readonly;
        content.open(context, arguments, readonly); 
    }

    private boolean isUpdate = false;
    public void update(){
        isUpdate = true;
        content.open(context, arguments, readonly);
        isUpdate = false;
    }

    public void setReadonly(boolean readonly){
        this.readonly = readonly;
        update();
    }

    @Override
    public void setBorder(Border border) {
        if (content != null)
            content.setBorder(border);
    }

    @Override
    public Border getBorder() {
        if (content != null)
           return content.getBorder();
        return super.getBorder();
    }

    @Override
    public boolean requestFocusInWindow() {
        return content.requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return content.getPreferredSize();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize != null)
            content.setPreferredSize(preferredSize);
        super.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getMaximumSize() {
        return content.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return content.getMinimumSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize != null)
            content.setMaximumSize(maximumSize);
        super.setMaximumSize(maximumSize);
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize != null)
            content.setMinimumSize(minimumSize); 
        super.setMinimumSize(minimumSize);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        content = new org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditor();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditor content;
    // End of variables declaration//GEN-END:variables

    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public boolean isComplete() {
        return content.isComplete();
    }

}
