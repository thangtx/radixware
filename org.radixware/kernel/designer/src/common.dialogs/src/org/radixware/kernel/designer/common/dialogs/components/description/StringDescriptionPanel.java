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

package org.radixware.kernel.designer.common.dialogs.components.description;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;


final class StringDescriptionPanel extends JPanel implements IDescriptionEditor {

    private IDescriptionHandleInfo handleInfo;
    private DescriptionEditPanel editorPanel;
    private JCheckBox chkInherit = new JCheckBox("Inherit");

    public StringDescriptionPanel() {
        this(NbBundle.getMessage(DescriptionPanel.class, "DescriptionPanel.border.title"));
    }

    public StringDescriptionPanel(String title) {

        editorPanel = new DescriptionEditPanel(6);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
        titlePanel.add(new JLabel(title));
        titlePanel.add(chkInherit);
        chkInherit.setVisible(false);
        titlePanel.add(Box.createHorizontalGlue());
//        ComponentTitledBorder titledBorder = new ComponentTitledBorder(titlePanel, this, BorderFactory.createTitledBorder(""));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(1, 8, 3, 0)); // NOI18N
//        setBorder(titledBorder);
        add(titlePanel);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final JScrollPane scrollPane = new JScrollPane(editorPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
        
        editorPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                handleInfo.setStringDescription(editorPanel.getText());
            }
        });
        
        //setBorder(BorderFactory.createEtchedBorder());
    }

    @Override
    public void open(IDescriptionHandleInfo handleInfo) {
        this.handleInfo = handleInfo;
        update();
    }

    @Override
    public void update() {
        editorPanel.open(handleInfo.getStringDescription());

        if (handleInfo.getDescribable() instanceof Definition) {
            editorPanel.setReadOnly(((Definition) handleInfo.getDescribable()).isReadOnly());
        } else if (handleInfo.getDescribable() instanceof IReadOnly) {
            editorPanel.setReadOnly(((IReadOnly) handleInfo.getDescribable()).isReadOnly());
        } else {
            editorPanel.setReadOnly(true);
        }
    }

    @Override
    public void setReadonly(boolean readonly) {
        editorPanel.setReadOnly(readonly);
    }

    @Override
    public String getDescription() {
        return handleInfo.getStringDescription();
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        editorPanel.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        editorPanel.removeChangeListener(listener);
    }

    @Override
    public Dimension getPreferredSize() {
        if (editorPanel != null) {
            final Dimension size =  editorPanel.calcPreferredSize();
            final Insets insets = getInsets();
            
            size.width = 100;
            
            if (insets != null) {
//                size.width += insets.left + insets.right;
                size.height += insets.bottom + insets.top;
            }
            return size;
        } else {
            return super.getPreferredSize();
        }
    }

    @Override
    public void initComponentsForInheriting(ItemListener listener) {
    }

    @Override
    public boolean isInhereted() {
        return false;
    }

    @Override
    public void setInherit(boolean inherit) {
    }
}
