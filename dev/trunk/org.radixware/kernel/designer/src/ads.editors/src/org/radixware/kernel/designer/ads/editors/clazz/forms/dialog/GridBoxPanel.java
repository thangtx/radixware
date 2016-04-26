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

import java.util.LinkedList;
import javax.swing.AbstractListModel;
import javax.swing.event.ListSelectionEvent;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.GridBoxWebLayout;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.GridBoxWebLayout.CellInfo;


public class GridBoxPanel extends EditGridBoxPanel<RadixObjects, AdsUIItemDef> {

    private ContainerChangesListener listener;
    private LinkedList<LinkedList> rowsList = GridBoxWebLayout.getRowsList();//list of rows in the grid
    private LinkedList<LinkedList> colsList = GridBoxWebLayout.getColumnsList();

    public GridBoxPanel(AdsAbstractUIDef uiDef,AdsRwtWidgetDef widget, ContainerChangesListener listener) {
        super(uiDef,widget);
        this.listener = listener;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_GridBoxPanel");
    }

    @Override
    protected IListModel<RadixObjects> createRowsListModel() {
        return new RowListModel();//model for Rows tab
    }

    @Override
    protected ITableModel<RadixObjects> createRowTableModel() {
        return new TableModel();//rowTableModel
    }

    @Override
    protected IListModel<RadixObjects> createColsListModel() {
        return new ColsListModel();//model for Columns tab 
    }

    @Override
    protected ITableModel<RadixObjects> createColTableModel() {
        return new ColTableModel();  //colTabelModel
    }

    //rows model
    private class RowListModel extends AbstractListModel<Object> implements IListModel<RadixObjects> {

        LinkedList<CellInfo> list;//list of items inside rows

        RowListModel() {
        }

        @Override
        public void fireChanged(int idx) {
            if (idx < 0 || idx >= getSize()) {
                return;
            }
            list = GridBoxWebLayout.getRowItemsAt(idx);
            if (!list.isEmpty()) {
                for (CellInfo item : list) {
                    AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty((AdsRwtWidgetDef) item.widget, "row");

                    p.value = idx;
                    AdsUIUtil.fire(widget, p, GridBoxPanel.this);
                }
            }
        }

        @Override
        public String getElementAt(int i) {
            return i + " row";
        }

        @Override
        public int getSize() {
            return rowsList.size();
        }

        @Override
        public boolean add(int idx) {
            if (idx < 0 || idx > getSize()) {
                return false;
            }
            boolean added;
            GridBoxWebLayout.insertEmptyRow(idx);
            list = GridBoxWebLayout.getRowItemsAt(idx);
            if (list.isEmpty()) {
                added = false;
            } else {
                for (CellInfo w : list) {
                    AdsUIUtil.addContainerListener(w.widget, listener);
                }
                LinkedList empty = new LinkedList();
                if (idx < rowsList.size()) {
                    rowsList.add(idx + 1, empty);
                } else {
                    rowsList.addLast(empty);
                }
                fireIntervalAdded(this, idx, idx + 1);
                added = true;
            }

            fireContentsChanged(this, idx, idx);
            fireChanged(idx);
            return added;
        }

        @Override
        public boolean del(int idx) {
            boolean deleted;
            if (idx < 0 || idx >= getSize()) {
                return false;
            }

            list = GridBoxWebLayout.getRowItemsAt(idx);
            GridBoxWebLayout.delRow(idx);
            if (list.isEmpty()) {

                deleted = false;
            } else {
                for (CellInfo w : list) {
                    AdsUIUtil.removeContainerListener(w.widget, listener);
                }
                fireIntervalRemoved(this, idx, idx);
                deleted = true;

            }

            fireContentsChanged(this, idx, idx);
            fireChanged(idx);
            return deleted;
        }

        @Override
        public void up(int idx) {
            if (idx > 0 && idx < getSize()) {
                GridBoxWebLayout.swapRows(idx, idx - 1);
            }
            fireChanged(idx);
            fireContentsChanged(this, idx - 1, idx);
        }

        @Override
        public void down(int idx) {
            if (idx >= 0 && idx < getSize() - 1) {
                GridBoxWebLayout.swapRows(idx, idx + 1);
            }
            fireContentsChanged(this, idx, idx + 1);
            fireChanged(idx);
        }

        private int indexOf(RadixObjects w) {
            int index = 0;
            if (!rowsList.isEmpty()) {
                index = rowsList.indexOf(w);
            }
            return index;
        }

        public void edited(RadixObjects w) {
            int idx = indexOf(w);
            if (idx >= 0 && idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }
    }
    //end rows model

    //columns model
    private class ColsListModel extends AbstractListModel<Object> implements IListModel<RadixObjects> {

        LinkedList<CellInfo> list;

        ColsListModel() {
        }

        @Override
        public String getElementAt(int i) {
            return i + " column";
        }

        @Override
        public int getSize() {
            return colsList.size();
        }

        @Override
        public boolean add(int idx) {
            boolean added;
            if (idx < 0 || idx > getSize()) {
                idx = 0;
            }
            GridBoxWebLayout.insertEmptyColumn(idx);

            list = GridBoxWebLayout.getColumnItemsAt(idx);
            if (list.isEmpty()) {
                added = false;
            } else {
                for (CellInfo w : list) {
                    AdsUIUtil.addContainerListener(w.widget, listener);

                }
                LinkedList empty = new LinkedList();

                if (idx < colsList.size()) {
                    colsList.add(idx + 1, empty);
                } else {
                    colsList.addLast(empty);
                }
                added = true;
                fireIntervalAdded(this, idx, idx + 1);
            }
            fireChanged(idx);
            fireContentsChanged(this, idx, idx + 1);
            return added;
        }

        @Override
        public boolean del(int idx) {

            boolean deleted;
            if (idx < 0 || idx >= getSize()) {
                return false;
            }

            list = GridBoxWebLayout.getColumnItemsAt(idx);
            GridBoxWebLayout.delColumn(idx);
            if (list.isEmpty()) {
                deleted = false;
            } else {
                for (CellInfo w : list) {
                    AdsUIUtil.removeContainerListener(w.widget, listener);
                }
                fireIntervalRemoved(this, idx, idx);
                deleted = true;

            }
            fireChanged(idx);
            fireContentsChanged(this, idx, idx);
            return deleted;
        }

        @Override
        public void up(int idx) {
            if (idx >= 0 && idx < getSize()) {
                GridBoxWebLayout.swapColumns(idx, idx + 1);
            }
            fireChanged(idx);
            fireContentsChanged(this, idx, idx + 1);
        }

        @Override
        public void down(int idx) {

            if (idx > 0 && idx < getSize()) {
                GridBoxWebLayout.swapColumns(idx, idx - 1);
            }
            fireChanged(idx);
            fireContentsChanged(this, idx, idx - 1);
        }

        private int indexOf(RadixObjects w) {
            int index = 0;
            if (!colsList.isEmpty()) {
                index = colsList.indexOf(w);
            }
            return index;
        }

        public void edited(RadixObjects w) {
            int idx = indexOf(w);
            if (idx >= 0 && idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }

        @Override
        public void fireChanged(int idx) {
            if (idx < 0 || idx >= getSize()) {
                return;
            }
            list = GridBoxWebLayout.getColumnItemsAt(idx);
            if (!list.isEmpty()) {
                for (final CellInfo item : list) {
                    final AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty((AdsRwtWidgetDef) item.widget, "col");
                    p.value = idx;
                    AdsUIUtil.fire(widget, p, GridBoxPanel.this);
                }
            }

        }
    }
    //end columns model

    //table scheme for rows edit
    private final class TableModel extends TableSchemeModel<RadixObjects> {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (e.getValueIsAdjusting()) {//если изменения все еще выполняются return;

                return;
            }
            fireTableDataChanged();
            fireTableStructureChanged();
        }

        @Override
        public LinkedList getRows() {
            return rowsList;
        }

        @Override
        public LinkedList getColumns() {
            return colsList;
        }
    }
    //end table scheme for rows edit

    //table scheme for cols edit
    private final class ColTableModel extends TableSchemeModel<RadixObjects> {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (e.getValueIsAdjusting()) {//если изменения все еще выполняются return;

                return;
            }
            fireTableDataChanged();
            fireTableStructureChanged();

        }

        @Override
        public LinkedList getRows() {
            return rowsList;
        }

        @Override
        public LinkedList getColumns() {
            return colsList;
        }
    }
}
