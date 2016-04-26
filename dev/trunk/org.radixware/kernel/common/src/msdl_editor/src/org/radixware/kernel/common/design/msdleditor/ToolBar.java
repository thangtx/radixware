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

package org.radixware.kernel.common.design.msdleditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import org.radixware.kernel.common.design.msdleditor.tree.FieldNode;
import org.radixware.kernel.common.design.msdleditor.tree.IFieldsOwner;
import org.radixware.kernel.common.design.msdleditor.tree.SequenceItemNode;


public class ToolBar extends JToolBar {
    private Editor editor;
    private JButton buttonPlus, buttonMinus, buttonUp, buttonDown, buttonSave;
    
    public ToolBar() {
        buttonPlus = new JButton();
        buttonPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/plus.png"))); 
        buttonPlus.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/plus_disabled.png"))); 
        buttonPlus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                editor.getTree().addField();
            }
        });
        add(buttonPlus);
        setPlusEnabled(false);
        
        
        buttonMinus = new JButton();
        buttonMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/minus.png"))); 
        buttonMinus.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/minus_disabled.png"))); 
        buttonMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.getTree().delField();
            }
        });
        add(buttonMinus);
        setMinusEnabled(false);

        buttonSave = new JButton();
        buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/save.png"))); 
        buttonSave.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/save_disabled.png"))); 
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               editor.save();
            }
        });
        add(buttonSave);
        setSaveEnabled(true);
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setPlusEnabled(boolean state) {
        buttonPlus.getModel().setEnabled(state);
    }

    public void setMinusEnabled(boolean state) {
        buttonMinus.getModel().setEnabled(state);
    }

//    public void setUpEnabled(boolean state) {
//        buttonUp.getModel().setEnabled(state);
//    }

//    public void setDownEnabled(boolean state) {
//        buttonDown.getModel().setEnabled(state);
//    }

    public void setSaveEnabled(boolean state) {
        buttonSave.getModel().setEnabled(state);
    }

    public void setSaveModified(boolean state) {
        if (state)
            buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/save_changed.png")));
        else
            buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/save.png")));
    }

    public void setEnabled(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
        setMinusEnabled(node instanceof FieldNode && !(node instanceof SequenceItemNode));
        setPlusEnabled(node instanceof IFieldsOwner);
    }
}
