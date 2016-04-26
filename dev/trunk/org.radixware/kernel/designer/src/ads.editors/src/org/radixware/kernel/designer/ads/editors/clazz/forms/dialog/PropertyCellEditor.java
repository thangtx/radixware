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

package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.openide.explorer.propertysheet.PropertyPanel;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;


abstract class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor {
    private AdsUIProperty prop;
    private PropertyPanel editor;

    public PropertyCellEditor() {
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
        prop = (AdsUIProperty) value;

        editor = new PropertyPanel(getPropertySupport(prop), PropertyPanel.PREF_TABLEUI);
        editor.setChangeImmediate(false);
        return editor;
    }

    @Override
    public boolean stopCellEditing() {
        if (editor != null) {
            editor.updateValue();
        }
        return super.stopCellEditing();
    }

    @Override
    public Object getCellEditorValue() {
        return prop;
    }

    protected abstract  UIPropertySupport getPropertySupport(AdsUIProperty prop);
}
