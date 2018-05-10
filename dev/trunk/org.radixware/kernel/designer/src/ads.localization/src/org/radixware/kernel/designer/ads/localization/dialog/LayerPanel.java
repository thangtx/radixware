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
 * LayerPanel.java
 *
 * Created on Oct 6, 2009, 6:05:23 PM
 */
package org.radixware.kernel.designer.ads.localization.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.border.EmptyBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;

public class LayerPanel extends javax.swing.JPanel {

    private Map<Layer, List<Module>> selectedLayers;
    private boolean isOpened = false;

    /**
     * Creates new form LayerPanel
     */
    public LayerPanel() {
        initComponents();
    }

    public void open(final Map<Layer, List<Module>> selectedLayers) {
        isOpened = true;
        this.selectedLayers = selectedLayers;
        createUi(true);
    }

    public void open(final Map<Layer, List<Module>> selectedLayers, boolean showDdsSegment) {
        isOpened = true;
        if (selectedLayers != null && !showDdsSegment){
            Map<Layer, List<Module>> filteredMap = new HashMap<>();
            for (Layer l : selectedLayers.keySet()){
                List<Module> result = new ArrayList<>();
                List<Module> actualList = selectedLayers.get(l);
                if (actualList != null){
                    for (Module m : actualList){
                        if (!(m instanceof DdsModule)){
                            result.add(m);
                        }
                    }
                }
                
                if (!result.isEmpty()){
                    filteredMap.put(l, result);
                } else if (actualList == null){
                    filteredMap.put(l, null);
                }
            }
            if (filteredMap.isEmpty()){
                this.selectedLayers = null;
            } else{
                this.selectedLayers = filteredMap;
            }
        } else {
            this.selectedLayers = selectedLayers;
        }
        createUi(showDdsSegment);
    }

    private void createUi() {
        createUi(false);
    }

    private void createUi(final boolean showDdsSegment) {
        extendableTextField.setEditable(false);
        extendableTextField.addButton("...");
        final String text = getTextFromResult();
        extendableTextField.setTextFieldValue(text);
        extendableTextField.getButtons().get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {

                final ChooseLayersDialog dialog = new ChooseLayersDialog(selectedLayers, showDdsSegment);
                dialog.check();
                dialog.setBorder(new EmptyBorder(10, 10, 10, 10));
                final StateAbstractDialog mDialog = new StateAbstractDialog(dialog, NbBundle.getMessage(LayerPanel.class, "CHOOSE_LAYERS")) {
                };
                if (mDialog.showModal()) {
                    final Map<Layer, List<Module>> newSelectedLayers = dialog.getSelectedLayers();
                    selectedLayers = dialog.isAllSelected() ? null : newSelectedLayers;
                    final String str = getTextFromResult();
                    extendableTextField.setTextFieldValue(str);
                }
            }
        });
    }

    private String getTextFromResult() {
        if (!isOpened) {
            return "";
        }

        if (selectedLayers == null) {
            return NbBundle.getMessage(LayerPanel.class, "ALL_LAYERS");
        }

        final StringBuilder sb = new StringBuilder();
        for (Layer node : selectedLayers.keySet()) {
            if (node != null && node.getBranch() != null) {
                if (selectedLayers.get(node) == null) {
                    sb.append(node.getName());
                    sb.append(" (");
                    sb.append(node.getBranch().getName());
                    sb.append(")");
                    //text+=node.getName()+" ("+node.getBranch().getName()+")";//.getLayerName();
                } else {
                    sb.append("some modules of ");
                    sb.append(node.getName());
                    sb.append(" (");
                    sb.append(node.getBranch().getName());
                    sb.append(")");
                    //text+="some modules of "+node.getName()+" ("+node.getBranch().getName()+")";
                }
                sb.append(", ");
                //text+=", ";               
            }
        }
        String text = sb.toString();
        return !text.isEmpty() ? text.substring(0, text.length() - 2) : text;
    }

    /* private String getInitText() {
     String text="";
     if(selectedLayers==null) 
     return NbBundle.getMessage(LayerPanel.class, "ALL_LAYERS");
     for(Layer layer: selectedLayers){
     text+=layer.getName()+ " ("+layer.getBranch().getName() +")";
     text+=", ";
     }
     return text.substring(0,text.length()-2);
     }*/
    public Map<Layer, List<Module>> getSelectedLayers() {
        return selectedLayers;
    }

    public void changeGap(int width) {
        filler2.changeShape(new java.awt.Dimension(width, 0), new java.awt.Dimension(width, 0), new java.awt.Dimension(width, getMaximumSize().height));
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
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(18, 0), new java.awt.Dimension(18, 0), new java.awt.Dimension(18, 32767));
        extendableTextField = new org.radixware.kernel.common.components.ExtendableTextField(true);

        setMaximumSize(new java.awt.Dimension(32767, 27));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(LayerPanel.class, "LayerPanel.jLabel1.text")); // NOI18N
        add(jLabel1);
        jLabel1.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(LayerPanel.class, "LayerPanel.jLabel1.AccessibleContext.accessibleName")); // NOI18N

        add(filler2);
        add(extendableTextField);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.components.ExtendableTextField extendableTextField;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
