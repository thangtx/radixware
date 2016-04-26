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

package org.radixware.kernel.designer.ads.editors.msdl;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.design.msdleditor.field.SortPanel;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


@Deprecated // use drag&drop to order fields.
public class AdsMsdlSortFields extends RadixObjectEditor {

    public static final class Factory implements IEditorFactory {

        @Override
        public IRadixObjectEditor newInstance(RadixObject radixObject) {
            return new AdsMsdlSortFields(radixObject);
        }
    }

    /** Creates new form AdsMsdlEditorView */
    public AdsMsdlSortFields(RadixObject radixObject) {
        super(radixObject);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public boolean open(OpenInfo info) {
        removeAll();
        repaint();
        SortPanel panel = new SortPanel();
        panel.open((RadixObjects) getRadixObject());
        add(panel);
        return super.open(info);
    }

    @Override
    public void update() {
        // TODO: update editor when focused
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
} 
