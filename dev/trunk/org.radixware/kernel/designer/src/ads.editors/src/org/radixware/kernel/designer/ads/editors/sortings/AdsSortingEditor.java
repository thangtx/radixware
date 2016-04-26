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

package org.radixware.kernel.designer.ads.editors.sortings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef.Order;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;


public class AdsSortingEditor extends RadixObjectEditor<AdsSortingDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsSortingDef> {

        @Override
        public IRadixObjectEditor<AdsSortingDef> newInstance(AdsSortingDef sorting) {
            return new AdsSortingEditor(sorting);
        }
    }

    private  AdsSortingEditorPanel panel = new AdsSortingEditorPanel();
    /** Creates new form AdsSortingEditor */
    protected AdsSortingEditor(AdsSortingDef sorting) {
        super(sorting);
        initComponents();
        setLayout(new BorderLayout());
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(panel);
        add(scroll, BorderLayout.CENTER);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 655, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public AdsSortingDef getSorting() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {
        panel.open(getSorting());
        return super.open(info);
    }

    @Override
    public void update() {
        panel.update();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}