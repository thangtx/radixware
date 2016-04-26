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

package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.openide.util.Exceptions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel;


public class BuddyPanel extends JPanel implements PropertyChangeListener {

    private BuddyEditor editor;
    private ChooseDefinitionPanel panel = new ChooseDefinitionPanel();

    BuddyPanel(BuddyEditor editor, PropertyEnv env) {
        this.editor = editor;

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);

        initComponents();

        setLayout(new java.awt.BorderLayout());
        add(panel, BorderLayout.CENTER);

        final AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(getNode());
        VisitorProvider provider = new AdsVisitorProvider() {

            @Override
            public boolean isContainer(RadixObject object) {
                if (uiDef == null) {
                    return false;
                }
                if (object instanceof AdsUIDef && object != uiDef) {
                    return false;
                }
                return super.isContainer(object);
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsWidgetDef) {
                    AdsWidgetDef widget = (AdsWidgetDef) object;
                    return !(widget.getOwnerDefinition() instanceof AdsWidgetDef && AdsMetaInfo.TAB_WIDGET_CLASS.equals(((AdsWidgetDef) widget.getOwnerDefinition()).getClassName()));
                }
                return false;
            }
        };

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(getNode(), provider);
        panel.open(cfg, false);
    }

    private RadixObject getNode() {
        return ((UIPropertySupport) editor.getSource()).getNode();
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
            Definition def = panel.getSelected();
            String value = (def != null) ? String.valueOf(def.getId()) : null;

            editor.setValue(value);
            try {
                ((UIPropertySupport) editor.getSource()).setValue(value);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
