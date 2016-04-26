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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

class ChooseRadixObjectModalDisplayer extends ModalDisplayer implements IChooseRadixObjectModalDisplayer {

    private final ChooseRadixObjectPanel panel;
    private final ChooseRadixObjectCfg cfg;
    private List<RadixObject> result = null;

    public ChooseRadixObjectModalDisplayer(final ChooseRadixObjectPanel panel, ChooseRadixObjectCfg cfg, boolean multipleSelectionAllowed) {
        super(panel);

        this.cfg = cfg;
        String title = "Select " + (multipleSelectionAllowed ? cfg.getTypesTitle() : cfg.getTypeTitle());
        setTitle(title);

        this.panel = panel;
        panel.open(cfg, multipleSelectionAllowed);

        getDialogDescriptor().setValid(panel.hasSelection());
        panel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                getDialogDescriptor().setValid(panel.hasSelection());
            }
        });

        panel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close(true);
            }
        });
    }

    @Override
    protected void apply() {
        result = panel.getSelection();
    }

    @Override
    public boolean showModal() {
        if (super.showModal()) {
            return (result != null);
        } else {
            return false;
        }
    }

    @Override
    public List<RadixObject> getResult() {
        return result;
    }
}
