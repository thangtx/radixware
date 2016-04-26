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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.DefinitionsPanel;


public abstract class DefinitionSelector<TContext> extends ContextableItemSelector<Definition, TContext> implements ChangeListener {
    protected final DefinitionsPanel definitionsPanel = new DefinitionsPanel();

    public DefinitionSelector() {
        super();

        EmptyTextFilter<Definition> emptyTextFilter = new EmptyTextFilter<>();

        getSelectorLayout().addFilterComponent(emptyTextFilter);
        getSelectorLayout().setSelectorComponent(definitionsPanel);

        definitionsPanel.attachExternalComponents((JTextField) emptyTextFilter.getComponent(), null);
        definitionsPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                fireSelectionChange(definitionsPanel.getSelected());
            }
        });

    }

    @Override
    public Definition getSelectedItem() {
        return definitionsPanel.getSelected();
    }

    @Override
    public void setSelectedItems(Collection<Definition> item) {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateList();
    }

    public final void addActionListener(ActionListener listener) {
        definitionsPanel.addActionListener(listener);
    }

    public final void removeActionListener(ActionListener listener) {
        definitionsPanel.removeActionListener(listener);
    }
}
