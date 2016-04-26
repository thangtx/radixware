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

import java.awt.Component;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.JXSearchField;


public class EmptyTextFilter<TValue> extends ItemFilter<TValue> {

    protected final JXSearchField field;

    public EmptyTextFilter() {
        field = new JXSearchField() {
            @Override
            public void add(Component comp, Object constraints) {
                Object c = constraints;
                if (c instanceof String) {

                    if ("LEFT".equals(c)) {
                        c = "West";
                    } else if ("RIGHT".equals(c)) {
                        c = "East";
                    }
                }
                super.add(comp, c);
            }
        };
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePattern();
            }
        });
    }

    @Override
    public boolean accept(TValue value) {
        return true;
    }

    @Override
    public final Component getComponent() {
        return field;
    }

    @Override
    public void reset() {
        field.setText("");
    }

    protected void updatePattern() {
    }

    public String getText() {
        return field.getText();
    }

    public final JXSearchField getSearchField() {
        return field;
    }
}
