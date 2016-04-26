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

package org.radixware.kernel.designer.ads.method.profile;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.MethodValue;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.AdsMethodProfileSettings;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditor;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditor.CommonParametersEditorEvent;

/**
 *
 * @deprecated 
 */
@Deprecated
public class ParametersPanel extends javax.swing.JPanel {

    private AdsMethodDef context;
    private AdsClassDef owner;
    private boolean readonly = false;

    private boolean isEscapePressed = false;
    private boolean isNameChanged = false;

   private CommonParametersEditor.ICommonParametersEditorEventListener contentListener  = new CommonParametersEditor.ICommonParametersEditorEventListener() {

            @Override
            public void onEvent(CommonParametersEditorEvent e) {
                if (!ParametersPanel.this.isUpdate){
                    if (e.type == CommonParametersEditor.ADD_BUTTON_PRESSED){
                        ParametersPanel.this.addButtonPressed();
                    } else if (e.type == CommonParametersEditor.REMOVE_BUTTON_PRESSED){
                        ParametersPanel.this.removeButtonPressed();
                    } else if (e.type == CommonParametersEditor.ESCAPE_BUTTON_PRESSED) {
                        ParametersPanel.this.isEscapePressed = true;
                    }
                }
            }

        };


    /** Creates new form ParametersPanel */
    public ParametersPanel() {
        initComponents();
        content.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }

        });
        content.addNameChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                ParametersPanel.this.isNameChanged = true;
                changeSupport.fireChange();
            }

        });
        content.getEditorStateSupport().addEventListener(contentListener);
    }

    public boolean isParameterNameChanged(){
        return this.isNameChanged;
    }

    public boolean isEscapeButtonPressed(){
        return this.isEscapePressed;
    }

    private void addButtonPressed(){
        MethodParameter newparam = MethodParameter.Factory.newTemporaryInstance(context);
        MethodValue returnValue = context.getProfile().getReturnValue() != null ? context.getProfile().getReturnValue() : newparam;
        NewParameterPanel panel = new NewParameterPanel(context, newparam, returnValue,readonly);
        if (panel.edit()){
            content.addRow(newparam);
        }
    }

    private void removeButtonPressed(){
        int row = content.getCurrentRow();
        if (row != -1){
            content.removeRow(row);
        }
    }

    public void open(AdsMethodDef method, AdsClassDef owner, boolean readonly) {
        this.readonly = readonly;
        this.context = method;
        this.owner = owner;
        this.isEscapePressed = false;
        boolean isTransparent = context instanceof AdsTransparentMethodDef;
        boolean isPresentationSlot = context instanceof AdsPresentationSlotMethodDef;
        boolean isOveride = context.getHierarchy().findOverridden().get() != null;
        AdsMethodProfileSettings settings = new AdsMethodProfileSettings(method.isReadOnly(), readonly, isTransparent || isPresentationSlot || isOveride, isPresentationSlot);
        content.open(context, method.getProfile().getParametersList(), settings);
    }

    private boolean isUpdate = false;
    void update() {
        isUpdate = true;
        boolean isTransparent = context instanceof AdsTransparentMethodDef;
        boolean isPresentationSlot = context instanceof AdsPresentationSlotMethodDef;
        boolean isOveride = context.getHierarchy().findOverridden().get() != null;
        AdsMethodProfileSettings settings = new AdsMethodProfileSettings(context.isReadOnly(), readonly, isTransparent || isPresentationSlot || isOveride, isPresentationSlot);
        content.open(context, context.getProfile().getParametersList(), settings);
        isUpdate = false;
    }

    public void apply() {
        List<MethodParameter> result = getCurrentParameters();
        AdsMethodParameters params = context.getProfile().getParametersList();
        params.clear();
        for (MethodParameter mp : result){
            params.add(mp);
        }
    }

    List<AdsTypeDeclaration> getCurrentTypes(){
        return content.getCurrentTypes();
    }

    public List<MethodParameter> getCurrentParameters() {
        List<MethodParameter> result = new ArrayList<>();
        List<AdsTypeDeclaration> types = content.getCurrentTypes();
        List<String> names = content.getCurrentNames();
        List<String> descs = content.getCurrentDescriptions();
        for (int i = 0, size = types.size()-1; i <= size; i++){
            MethodParameter mp = MethodParameter.Factory.newInstance(names.get(i), types.get(i));
            mp.setDescription(descs.get(i));
            result.add(mp);
        }
        return result;
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
            .addComponent(content, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
