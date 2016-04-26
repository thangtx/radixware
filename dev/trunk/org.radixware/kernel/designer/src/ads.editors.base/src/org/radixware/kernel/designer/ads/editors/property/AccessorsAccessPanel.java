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

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.LayoutManager;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


final class AccessorsAccessPanel extends javax.swing.JPanel {

    public AccessorsAccessPanel() {
        initComponents();

        getReadAccessPanel().addValueChangeListener(new ValueChangeListener<EAccess>() {

            @Override
            public void valueChanged(ValueChangeEvent<EAccess> e) {
                if (property != null) {
                    property.setReadAccess(e.newValue);
                }
            }
        });

        getWriteAccessPanel().addValueChangeListener(new ValueChangeListener<EAccess>() {

            @Override
            public void valueChanged(ValueChangeEvent<EAccess> e) {
                if (property != null) {
                    property.setWriteAccess(e.newValue);
                }
            }
        });
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (mgr != null) {
            super.setLayout(mgr);
        }
    }
    private AdsPropertyDef property;

    public void open(AdsPropertyDef property) {
        this.property = property;

        update();
    }

    public void update() {
        if (property != null) {
            final EAccess[] deny = calculateAccessDeny(property);

            final EAccess readAccess = property.isSetReadAccess()
                ? property.getReadAccess() : null;
            final EAccess writeAccess = property.isSetWriteAccess()
                ? property.getWriteAccess() : null;

            final boolean readOnly = property.isReadOnly();

            getReadAccessPanel().open(readAccess, deny);
            getWriteAccessPanel().open(writeAccess, deny);

            getReadAccessPanel().setEnabled(!readOnly);
            getWriteAccessPanel().setEnabled(!readOnly && !property.isConst());
        }
    }

    private AccessPanel getReadAccessPanel() {
        return (AccessPanel) readAccessPanel;
    }

    private AccessPanel getWriteAccessPanel() {
        return (AccessPanel) writeAccessPanel;
    }

    private static EAccess[] calculateAccessDeny(AdsPropertyDef property) {
        final AdsDefinition owner = property != null ? property.getOwnerDef() : null;

        if (owner instanceof AdsInterfaceClassDef) {
            return new EAccess[]{ EAccess.PROTECTED, EAccess.DEFAULT, EAccess.PRIVATE };
        }

        final Set<EAccess> deny = new HashSet<>();
        if (property != null) {
            final EAccess ownerAccess = property.getAccessMode();
            for (final EAccess access : EAccess.values()) {
                if (ownerAccess.isLess(access)) {
                    deny.add(access);
                }
            }
        }

        if (property instanceof AdsPropertyDef) {
            final AdsPropertyDef prop = (AdsPropertyDef) property;

            if (prop.getAccessFlags().isAbstract()) {
                deny.add(EAccess.PRIVATE);
            }
        }

        return deny.toArray(new EAccess[0]);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        readAccessPanel = new AccessPanel("Read Access:", "Read Access");
        writeAccessPanel = new AccessPanel("Write Access:", "Write Access");

        readAccessPanel.setLayout(null);

        writeAccessPanel.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(readAccessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(writeAccessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(readAccessPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(writeAccessPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel readAccessPanel;
    private javax.swing.JPanel writeAccessPanel;
    // End of variables declaration//GEN-END:variables
}
