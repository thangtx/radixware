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

import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;


public class ValAsStrTableCellEditor extends ValAsStrEditPanel implements TableCellEditor {

    public interface ValAsStrProvider {

        public ValAsStr getValue(int row, int column);

        public EValType getValType(int row, int column);

        public DdsTableDef findTargetTable(int row, int column);

        public void setValue(int row, int column, ValAsStr val);
    }
    private final List<CellEditorListener> listeners = new LinkedList<>();
    private final ValAsStrProvider provider;
    private Object value;
    private int r, c;
    private boolean isActive = false;

    public ValAsStrTableCellEditor(ValAsStrProvider provider) {
        super("<null>");
        this.provider = provider;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = value;
        isActive = true;
        this.setNullAble(true);
        this.setValue(provider.getValType(row, column), provider.findTargetTable(row, column), provider.getValue(row, column));
        r = row;
        c = column;
        return this;

    }

    @Override
    public Object getCellEditorValue() {
        return this.value;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        if (isActive) {
            provider.setValue(r, c, getValue());
            isActive = false;
        }
        List<CellEditorListener> list;
        synchronized (listeners) {
            list = new ArrayList<>(listeners);
        }
        ChangeEvent e = new ChangeEvent(this);
        for (CellEditorListener l : list) {
            l.editingStopped(e);
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        List<CellEditorListener> list;
        synchronized (listeners) {
            list = new ArrayList<>(listeners);
        }
        ChangeEvent e = new ChangeEvent(this);
        for (CellEditorListener l : list) {
            l.editingCanceled(e);
        }
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
}
