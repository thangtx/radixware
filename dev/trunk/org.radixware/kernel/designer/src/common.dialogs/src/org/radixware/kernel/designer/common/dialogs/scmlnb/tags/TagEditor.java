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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;


public abstract class TagEditor<T extends Tag> extends ModalObjectEditor<T> {

    /**
     * Must return true if all necessary elements for tag are defined, false
     * otherwise. It would be called from {@code  updateOkState()} to update
     * state of the OK button.
     *
     * @return
     */
    protected abstract boolean tagDefined();

    public T getTag() {
        return getObject();
    }

    /**
     * Call this function when some changes that may result into OK button state
     * changing occurred.
     */
    protected void updateOkState() {
        if (tagDefined()) {
            setCorrect(true);
        } else {
            setCorrect(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
