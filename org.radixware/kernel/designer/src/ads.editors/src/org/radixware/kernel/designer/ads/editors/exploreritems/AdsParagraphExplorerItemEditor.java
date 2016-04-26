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

package org.radixware.kernel.designer.ads.editors.exploreritems;

import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsParagraphExplorerItemEditor extends RadixObjectEditor<AdsParagraphExplorerItemDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsParagraphExplorerItemDef> {

        @Override
        public IRadixObjectEditor<AdsParagraphExplorerItemDef> newInstance(AdsParagraphExplorerItemDef paragraph) {
            return new AdsParagraphExplorerItemEditor(paragraph);
        }
    }

    /** Creates new form AdsEnumEditorView */
    public AdsParagraphExplorerItemEditor(AdsParagraphExplorerItemDef paragraph) {
        super(paragraph);

        initComponents();
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(content);
        add(scroll, java.awt.BorderLayout.CENTER);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private ParagraphItemCommonPanel content = new ParagraphItemCommonPanel();

    public AdsParagraphExplorerItemDef getParagraph() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsParagraphExplorerItemDef paragraph = getParagraph();
        content.open(paragraph);
        return super.open(info);
    }

    @Override
    public void update() {
        content.update();
    }
}
