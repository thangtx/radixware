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

package org.radixware.kernel.designer.ads.editors.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.common.EditorPresentationsList;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel;


public abstract class PresentationListCfgPanel extends javax.swing.JPanel {

    private org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel chooser;
    private PresentationsListAdapter adapter;

    public interface PresentationsListAdapter {

        boolean isReadOnly();

        void remove(Id id);

        void add(Id id);

        void moveUp(Id id);

        void moveDn(Id id);

        ExtendableDefinitions<AdsEditorPresentationDef> availablePresentations();

        List<Id> currentlySelectedIds();
    }

    public PresentationListCfgPanel(String title) {
        chooser = new RadixObjectChooserPanel();
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JLabel label = new JLabel(title);
        label.setText(title);
        
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        sep.setAlignmentY(1);
        //sep.setPreferredSize(new Dimension(100, label.getHeight()));
        panel.add(sep, BorderLayout.PAGE_END);



         panel.setLayout(new java.awt.GridBagLayout());

         java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panel.add(label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panel.add(sep, gridBagConstraints);

        add(panel, BorderLayout.NORTH);

        add(chooser, BorderLayout.CENTER);
    }

    @Override
    public Insets getInsets() {
        Insets insets = super.getInsets(null);
        insets.left += 5;
        insets.right += 5;
        insets.bottom += 5;
        return insets;
    }

    public void setReadonly(boolean readonly) {
        chooser.setReadonly(readonly);
    }

    protected void open(final PresentationsListAdapter adapter) {
        this.adapter = adapter;
        EditorPresentationsList leftList = new EditorPresentationsList();
        EditorPresentationsList rightList = new EditorPresentationsList();

        leftList.open(adapter, true);
        rightList.open(adapter, false);
        chooser.open(leftList, rightList);
    }

    public void update() {
        if (adapter != null) {
            chooser.setReadonly(adapter.isReadOnly());
            chooser.update();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension sd = super.getPreferredSize();
        if (sd == null) {
            return new Dimension(50, 150);
        } else {
            return new Dimension(sd.width, 150);
        }
    }
}
