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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionEvent;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionListener;


abstract class ChooseTypeFromListStep<EditorType extends ItemSelector, TValue>
        extends GeneralChooseTypeStep<JPanel> implements SelectionListener<TValue> {

    TValue currentValue;
    private EditorType editor;

    final ActionListener createSelectActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isComplete()) {
                    doNextOrFinish();
                }
            }
        };
    }

    @Override
    public final void selectionChanged(SelectionEvent<TValue> e) {
        currentValue = e.newValue;

        selectionChanged(currentValue);

        fireChange();
    }

    public final EditorType getEditor() {
        synchronized (this) {
            if (editor == null) {
                editor = createEditor();
            }
        }
        return editor;

    }

    @Override
    protected final JPanel createVisualPanel() {
        return getEditor().getComponent();
    }

    protected abstract EditorType createEditor();

    abstract void selectionChanged(TValue newValue);
}
