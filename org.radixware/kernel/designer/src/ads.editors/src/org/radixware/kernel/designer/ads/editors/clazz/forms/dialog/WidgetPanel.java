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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.event.ListSelectionEvent;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;

public class WidgetPanel extends SetPanel<AdsWidgetDef, AdsWidgetDef> {

    private ContainerChangesListener listener;

    public WidgetPanel(AdsAbstractUIDef uiDef, AdsWidgetDef widget, ContainerChangesListener listener) {
        super(uiDef, widget);
        this.listener = listener;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_WidgetPanel");
    }

    @Override
    protected IListModel<AdsWidgetDef> createListModel() {
        return new ListModel();
    }

    @Override
    protected ITableModel<AdsWidgetDef> createTableModel() {
        return new TableModel(uiDef);
    }

    @Override
    protected int getCurrentIndex() {
        final AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, "currentIndex");
        return p.value;
    }

    private class ListModel extends AbstractListModel<Object> implements IListModel<AdsWidgetDef> {

        private final AdsWidgetDef.Widgets ws = widget.getWidgets();

        ListModel() {
        }

        @Override
        public Object getElementAt(int i) {
            return ws.get(i).getName();
        }

        @Override
        public int getSize() {
            return ws.size();
        }

        public AdsWidgetDef.Widgets getWidgets() {
            return ws;
        }

        @Override
        public void fireChanged(int idx) {
            if (idx < 0 || idx >= getSize()) {
                return;
            }

            AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, "currentIndex");

            p.value = idx;
            AdsUIUtil.fire(widget, p, WidgetPanel.this);
        }

        @Override
        public void fireContentChanged(AdsWidgetDef w) {
            super.fireContentsChanged(this, ws.indexOf(w), ws.indexOf(w));
            fireChanged(ws.indexOf(w));
        }

        void edited(AdsWidgetDef w) {
            int idx = ws.indexOf(w);
            if (idx >= 0 && idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }

        public void setName(AdsWidgetDef w, String name) {
            Set<String> ns = new HashSet<>();
            for (AdsWidgetDef widget : ws) {
                if (w != widget) {
                    ns.add(widget.getName());
                }
            }

            String n = name;
            if (ns.contains(n)) {
                for (int i = 2;; i++) {
                    n = name + "_" + String.valueOf(i);
                    if (!ns.contains(n)) {
                        break;
                    }
                }
            }

            w.setName(n);
        }

        @Override
        public void add(int idx, AdsWidgetDef w) {
            setName(w, "page");
            if (idx < 0 || idx > getSize()) {
                idx = 0;
            }
            ws.add(idx, w);
            AdsUIUtil.addContainerListener(w, listener);
            fireIntervalAdded(this, idx, idx);
        }

        @Override
        public void del(int idx) {
            if (idx >= 0 && idx < getSize() && getSize() > 1) {
                AdsWidgetDef w = ws.get(idx);

                // clear childs !!!
                RadixObjects objects = w.getWidgets();
                if (w.getLayout() != null) {
                    objects = w.getLayout().getItems();
                }
                while (objects.size() > 0) {
                    objects.remove(0);
                }

                AdsUIUtil.removeContainerListener(w, listener);

                ws.remove(idx);
                fireIntervalRemoved(this, idx, idx);
            }
        }

        @Override
        public void up(int idx) {
            if (idx > 0 && idx < getSize()) {
                ws.moveUp(idx);
                fireContentsChanged(this, idx - 1, idx);
            }
        }

        @Override
        public void down(int idx) {
            if (idx >= 0 && idx < getSize() - 1) {
                ws.moveDown(idx);
                fireContentsChanged(this, idx, idx + 1);
            }
        }

        @Override
        public AdsWidgetDef getItemAt(int i) {
            return ws.get(i);
        }
    }

    private class TableModel extends DefaultTableModel<AdsWidgetDef> {

        public TableModel(AdsAbstractUIDef uiDef) {
            super(uiDef);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int idx = getItemList().getSelectedIndex();
            item = idx >= 0 ? getListModel().getItemAt(idx) : null;
            if (item == null) {
                return;
            }

            rows = Arrays.asList(
                    (AdsUIProperty) new AdsUIProperty.StringProperty("objectName"));

            UiProperties props = AdsUIUtil.getUiProperties(item);
            for (int i = 0; i < rows.size(); i++) {
                AdsUIProperty p = props.getByName(rows.get(i).getName());
                if (p != null) {
                    rows.set(i, p);
                } else {
                    rows.set(i, rows.get(i).duplicate());
                }
            }

            fireTableStructureChanged();
        }
    }
}
