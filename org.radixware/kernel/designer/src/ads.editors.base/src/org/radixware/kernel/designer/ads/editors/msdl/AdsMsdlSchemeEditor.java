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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.design.msdleditor.field.RootPanel;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsMsdlSchemeEditor extends RadixObjectEditor<AdsMsdlSchemeDef> {

    AccessEditPanel aep;

    protected AdsMsdlSchemeEditor(final AdsMsdlSchemeDef msdlScheme) {
        super(msdlScheme);
        initComponents();
        JPanel headerPanel = new JPanel();
        aep = new AccessEditPanel();
        GridBagLayout gbl = new GridBagLayout();
        headerPanel.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(aep, c);
        headerPanel.add(aep);
        headerPanel.setBorder(new EmptyBorder(12, 12, 0, 12));
        final JCheckBox cbDeprecated = new JCheckBox();
        cbDeprecated.setText("Deprecated");
        c.gridx++;
        c.fill = GridBagConstraints.REMAINDER;
        c.weightx = 1;

        gbl.setConstraints(cbDeprecated, c);
        headerPanel.add(cbDeprecated);
        cbDeprecated.setEnabled(!msdlScheme.isReadOnly());
        cbDeprecated.setSelected(msdlScheme.isDeprecated());
        if (!msdlScheme.isReadOnly()) {
            cbDeprecated.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    msdlScheme.setDeprecated(cbDeprecated.isSelected());
                }
            });
        }
        //headerPanel.setPreferredSize(aep.getPreferredSize());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsMsdlSchemeDef> {

        @Override
        public IRadixObjectEditor newInstance(AdsMsdlSchemeDef msdlScheme) {
            return new AdsMsdlSchemeEditor(msdlScheme);
        }
    }

    public AdsMsdlSchemeDef getMsdlScheme() {
        return getRadixObject();
    }
    private PreprocessorClassPanel preprocessorClassPanel = new PreprocessorClassPanel();

    @Override
    public boolean open(OpenInfo openInfo) {
        final AdsMsdlSchemeDef scheme = getMsdlScheme();
        aep.open(scheme);
        getRootPanel().open(scheme.getRootMsdlScheme(), preprocessorClassPanel);
        preprocessorClassPanel.open(getMsdlScheme());
        return super.open(openInfo);
    }

    @Override
    public boolean isOpeningAfterNewObjectCreationRequired() {
        return true;
    }
    private RootPanel rootPanel = null;

    public RootPanel getRootPanel() {
        if (rootPanel == null) {
            rootPanel = new RootPanel();
            rootPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
            mainPanel.add(rootPanel, BorderLayout.CENTER);
        }
        return rootPanel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        mainPanel.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(mainPanel);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void update() {
        getRootPanel().update();
        preprocessorClassPanel.update();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
