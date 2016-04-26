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
 * PresentationsForEditing.java
 *
 * Created on 23.06.2009, 11:33:26
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.common.PresentationListCfgPanel;


public class PresentationsForEditing extends PresentationListCfgPanel {

    private class Adapter implements PresentationListCfgPanel.PresentationsListAdapter {

        private final AdsSelectorPresentationDef presentation;

        public Adapter(AdsSelectorPresentationDef presentation) {
            this.presentation = presentation;
        }

        @Override
        public boolean isReadOnly() {
            return presentation.isReadOnly();
        }

        @Override
        public void remove(Id id) {
            presentation.getEditorPresentations().remove(id);
        }

        @Override
        public void add(Id id) {
            presentation.getEditorPresentations().add(id);
        }

        @Override
        public void moveUp(Id id) {
            presentation.getEditorPresentations().moveUp(id);
        }

        @Override
        public void moveDn(Id id) {
            presentation.getEditorPresentations().moveDown(id);
        }

        @Override
        public ExtendableDefinitions<AdsEditorPresentationDef> availablePresentations() {
            return presentation.getOwnerClass().getPresentations().getEditorPresentations();
        }

        @Override
        public List<Id> currentlySelectedIds() {
            return presentation.getEditorPresentations().getIds();
        }
    }

    /** Creates new form PresentationsForEditing */
    public PresentationsForEditing() {
        super("Editor presentations for editing");
    }

    public void open(final AdsSelectorPresentationDef presentation) {
        Adapter adapter = new Adapter(presentation);
        open(adapter);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
