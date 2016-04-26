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
 * MergePanel.java
 *
 * Created on 6 Ноябрь 2008 г., 14:25
 */

package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel;


public class EditorPageRefPanel extends JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private EditorPageRefEditor editor;
    private ChooseDefinitionPanel panel = new ChooseDefinitionPanel();

    EditorPageRefPanel(EditorPageRefEditor editor, PropertyEnv env) {
        this.env = env;
        this.editor = editor;

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);

        initComponents();

        setLayout(new java.awt.BorderLayout());
        add(panel, BorderLayout.CENTER);

        AdsClassDef clazz = AdsUIUtil.getOwnerClassDef(getNode());

        final List<Definition> list = new ArrayList<>();
        final Set<Id> used = new HashSet<>();
        
        if (clazz != null) {
            if (clazz instanceof AdsEntityObjectClassDef) {
                for (AdsEditorPresentationDef ep : ((AdsEntityObjectClassDef) clazz).getPresentations().getEditorPresentations().get(EScope.ALL)) {
                    for (AdsEditorPageDef page : ep.getEditorPages().get(EScope.ALL)) {
                        if (!used.contains(page.getId())) {
                            list.add(page);
                            used.add(page.getId());
                        }
                    }
                }
            } else if (clazz instanceof AdsFormHandlerClassDef) {
                    for (AdsEditorPageDef page : ((AdsFormHandlerClassDef) clazz).getPresentations().getEditorPages().get(EScope.ALL))
                        list.add(page);
            }
        }

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(list);
        panel.open(cfg, false);
    }

    private AdsUIProperty.EditorPageRefProperty getProperty() {
        return (AdsUIProperty.EditorPageRefProperty)editor.getValue();
    }

    private RadixObject getNode() {
        return ((UIPropertySupport)editor.getSource()).getNode();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(200, 140));
        setPreferredSize(new java.awt.Dimension(320, 260));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            AdsUIProperty.EditorPageRefProperty prop = getProperty();
            
            Definition def = panel.getSelected();
            prop.setEditorPageId(def != null ? def.getId() : null);
            
            editor.setValue(prop);
            try {
                ((UIPropertySupport)editor.getSource()).setValue(prop);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
