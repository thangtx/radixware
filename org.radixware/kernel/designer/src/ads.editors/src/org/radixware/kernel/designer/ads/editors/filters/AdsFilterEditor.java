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

package org.radixware.kernel.designer.ads.editors.filters;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.designer.ads.editors.exploreritems.UsedContextlessCommandsListView;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsFilterEditor extends RadixObjectEditor<AdsFilterDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsFilterDef> {

        @Override
        public IRadixObjectEditor<AdsFilterDef> newInstance(AdsFilterDef filter) {
            return new AdsFilterEditor(filter);
        }
    }

    private BasePropsPanel basePropsPanel;
    private AdsFilterTabbedPanel adsFilterTabbedPanel;
    private UsedContextlessCommandsListView usedContextlessCommands = new UsedContextlessCommandsListView();

    /**
     * Creates new form AdsFilterEditorView
     */
    protected AdsFilterEditor(AdsFilterDef filter) {
        super(filter);
        initComponents();
        initAdditionalComponents();
    }

    public AdsFilterDef getFilter() {
        return getRadixObject();
    }

    private void initAdditionalComponents() {
        final JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new GridBagLayout());

        this.add(new JScrollPane(bodyPanel));

        basePropsPanel = new BasePropsPanel();
        basePropsPanel.setAlignmentX(0.0f);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        bodyPanel.add(basePropsPanel, constraints);
        usedContextlessCommands.setAlignmentX(0.0f);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        bodyPanel.add(usedContextlessCommands, constraints);

        //tabbed pane
        adsFilterTabbedPanel = new AdsFilterTabbedPanel();
        adsFilterTabbedPanel.setAlignmentX(0.0f);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        bodyPanel.add(adsFilterTabbedPanel, constraints);
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsFilterDef filter = getFilter();
        basePropsPanel.open(filter);
        adsFilterTabbedPanel.open(filter);
        update();

        return super.open(info);
    }

    @Override
    public void update() {
        adsFilterTabbedPanel.update();
        setReadonly(getFilter().isReadOnly());
        usedContextlessCommands.open(getFilter());
        basePropsPanel.update();
    }

    public void setReadonly(boolean readonly) {

        if (!readonly) {
            //try to enable
            if (!getFilter().isReadOnly()) {
                adsFilterTabbedPanel.setReadonly(false);
            }
        } else {
            adsFilterTabbedPanel.setReadonly(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setPreferredSize(new java.awt.Dimension(100, 100));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
