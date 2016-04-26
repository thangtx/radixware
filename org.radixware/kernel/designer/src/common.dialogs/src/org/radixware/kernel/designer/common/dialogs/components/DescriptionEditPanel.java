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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class DescriptionEditPanel extends SizableTextArea {

    private class DescriptionDisplayer extends ModalDisplayer implements ChangeListener {

        public DescriptionDisplayer(DescriptionEditPanel panel) {
            super(panel);
            panel.addChangeListener(this);
            setTitle(NbBundle.getMessage(DescriptionEditPanel.class, "PP-Column-Description"));
        }

        @Override
        public Object[] getOptions() {
            if (!((DescriptionEditPanel) getComponent()).isEditable()) {
                javax.swing.JButton closeBtn = new javax.swing.JButton("Close");
                closeBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DescriptionDisplayer.this.close(false);
                    }
                });
                return new Object[]{closeBtn};
            }
            return super.getOptions();
        }
        private boolean isEdited = false;

        @Override
        protected void apply() {
            if (isEdited && definition != null) {
                definition.setDescription(getText());
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource().equals(getComponent())) {
                isEdited = true;
            }
        }
    }

    public boolean edit(IDescribable definition) {
        open(definition);
        DescriptionDisplayer displayer = new DescriptionDisplayer(this);
        setPreferredSize(new Dimension(200, 100));
        setMinimumSize(new Dimension(200, 100));
        return displayer.showModal();
    }

    public String edit(String description) {
        setText(description);
        DescriptionDisplayer displayer = new DescriptionDisplayer(this);
        setPreferredSize(new Dimension(200, 100));
        setMinimumSize(new Dimension(200, 100));
        if (displayer.showModal()) {
            return getText();
        }
        return description != null ? description : "";
    }
    private IDescribable definition;

    private void init(IDescribable definition) {
        this.definition = definition;
        setText(definition != null ? definition.getDescription() : "");
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!isUpdate) {
                    changeSupport.fireChange();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }
        });
    }

    public DescriptionEditPanel() {
        this(3);
    }
    public DescriptionEditPanel(int rowCount) {
        super(rowCount);
        
        setWrapStyleWord(true);
        setLineWrap(true);
        init(null);
    }
    
    private boolean isUpdate = false;

    public void open(IDescribable definition) {
        this.definition = definition;
        if (definition != null) {
            isUpdate = true;
            setText(definition.getDescription());
            isUpdate = false;
        }
    }

    public void open(String description) {
        this.definition = null;

        isUpdate = true;
        setText(description);
        isUpdate = false;
    }

    public void apply() {
        if (definition != null) {
            definition.setDescription(getText());
        }
    }

    public void setDescriptionText(String text) {
        setText(text);
    }

    public void setReadOnly(boolean readonly) {
        setEditable(!readonly);
    }

    public boolean getReadOnly() {
        return !isEditable();
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
}
