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

package org.radixware.kernel.designer.ads.editors.scopecommand;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.designer.ads.editors.command.components.AdsCommandCommonPropertiesPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsScopeCommandEditor extends RadixObjectEditor<AdsScopeCommandDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsScopeCommandDef> {

        @Override
        public IRadixObjectEditor<AdsScopeCommandDef> newInstance(AdsScopeCommandDef command) {
            return new AdsScopeCommandEditor(command);
        }
    }

    /** Creates new form AdsScopeCommandEditorView */
    public AdsScopeCommandEditor(AdsScopeCommandDef command) {
        super(command);
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

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);

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

    private javax.swing.JTabbedPane mainPane;
    private javax.swing.JScrollPane scopeScroll;
    private javax.swing.JScrollPane propScroll;
    private AdsCommandCommonPropertiesPanel commonProps;
    private AdsPropertyCommandDefPanel classProps;

    public AdsScopeCommandDef getCommand() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsScopeCommandDef command = getCommand();

        removeAll();
        mainPane = new javax.swing.JTabbedPane();
        setLayout(new BorderLayout());

        commonProps = new AdsCommandCommonPropertiesPanel();
        scopeScroll = new javax.swing.JScrollPane();
        JPanel propContainer = new JPanel();
        propContainer.setLayout(new BoxLayout(propContainer, BoxLayout.X_AXIS));
        propContainer.add(Box.createRigidArea(new Dimension(10, getPreferredSize().height)));
        commonProps.setAlignmentX(0);
        commonProps.setAlignmentY(0);
        propContainer.add(commonProps);
        propContainer.add(Box.createRigidArea(new Dimension(10, getPreferredSize().height)));
        scopeScroll.setViewportView(propContainer);
        commonProps.open(command);

        if (command instanceof AdsPropertyCommandDef) {
            add(mainPane, BorderLayout.CENTER);
            mainPane.add(NbBundle.getMessage(AdsScopeCommandEditor.class, "CommonProps-Tip"), scopeScroll);

            classProps = new AdsPropertyCommandDefPanel();
            classProps.open((AdsPropertyCommandDef) command);
            propScroll = new javax.swing.JScrollPane();
            propScroll.setViewportView(classProps);
            mainPane.add(classProps.getName(), propScroll);
        } else {
            add(scopeScroll, BorderLayout.CENTER);
        }

        return super.open(info);
    }

    @Override
    public void update() {
        commonProps.update();
        if (classProps != null) {
            classProps.update();
        }
    }
}
