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
 * APIDefinitionEditor.java
 *
 * Created on May 19, 2010, 10:54:50 AM
 */
package org.radixware.kernel.designer.common.editors;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


class APIDefinitionEditor extends RadixObjectEditor<AdsDefinition> {

    /** Creates new form APIDefinitionEditor */
    public APIDefinitionEditor(AdsDefinition def) {
        super(def);
        initComponents();
        toolTip.setContentType("text/html");
        toolTip.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    Definition def = resolveLink(e.getDescription());
                    if (def != null) {
                        DialogUtils.goToObject(def);
                    }
                } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                    Definition def = resolveLink(e.getDescription());
                    if (def != null) {
                        toolTip.setToolTipText("Go to " + def.getQualifiedName());
                    }
                } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                    toolTip.setToolTipText(null);
                }
            }
        });
    }

    private Definition resolveLink(String link) {
        if (definition != null && link != null) {
            String[] idsStr = link.split(":");
            Id[] ids = new Id[idsStr.length];
            for (int i = 0; i < idsStr.length; i++) {
                ids[i] = Id.Factory.loadFrom(idsStr[i]);
            }

            return AdsPath.resolve(definition, ids).get();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        toolTip = new javax.swing.JEditorPane();

        toolTip.setEditable(false);
        jScrollPane1.setViewportView(toolTip);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    private AdsDefinition definition = null;

    @Override
    public boolean open(OpenInfo openInfo) {
        RadixObject t = openInfo.getTarget();
        if (t instanceof AdsDefinition) {
            definition = (AdsDefinition) t;
            update();
            return true;
        } else {
            definition = null;
            return false;
        }
    }

    @Override
    public void update() {
        if (definition != null) {
            toolTip.setText(definition.getToolTip());
        }
    }

    @Override
    public boolean isOpeningAfterNewObjectCreationRequired() {
        return false;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane toolTip;
    // End of variables declaration//GEN-END:variables
}
