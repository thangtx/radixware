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
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;

public class TabPanel extends SetPanel<AdsUIItemDef, AdsUIItemDef> {

    private ContainerChangesListener listener;

    public TabPanel(AdsAbstractUIDef uiDef, AdsUIItemDef widget, ContainerChangesListener listener) {
        super(uiDef, widget);

        this.listener = listener;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_TabPanel");
    }

    @Override
    protected IListModel<AdsUIItemDef> createListModel() {
        return new ListModel();
    }

    @Override
    protected ITableModel<AdsUIItemDef> createTableModel() {
        return new TableModel(uiDef);
    }

    @Override
    protected int getCurrentIndex() {
        AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, "currentIndex");
        return p.value;
    }

    private class ListModel extends AbstractListModel<Object> implements IListModel<AdsUIItemDef> {

        private final Definitions ws = widget instanceof AdsWidgetDef ? ((AdsWidgetDef) widget).getWidgets() : ((AdsRwtWidgetDef) widget).getWidgets();

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

        @Override
        public void fireChanged(int idx) {
            if (idx < 0 || idx >= getSize()) {
                return;
            }

            AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, "currentIndex");

            p.value = idx;
            AdsUIUtil.fire(widget, p, TabPanel.this);
        }

        @Override
        public void fireContentChanged(AdsUIItemDef w) {
            super.fireContentsChanged(this, indexOf(w), indexOf(w));
            fireChanged(indexOf(w));
        }

        @Override
        public void add(int idx, AdsUIItemDef w) {
            setName(w, "tab");
            if (idx < 0 || idx > getSize()) {
                idx = 0;
            }
            ws.add(idx, w);
            AdsUIUtil.addContainerListener(w, listener);
            fireIntervalAdded(this, idx, idx);
        }

        @Override
        public void del(int idx) {
            if (idx >= 0 && idx < getSize() && getSize() > 0) {
                AdsUIItemDef w = (AdsUIItemDef) ws.get(idx);

                // clear childs !!!
                RadixObjects objects;
                if (w instanceof AdsWidgetDef) {
                    AdsWidgetDef adsw = (AdsWidgetDef) w;

                    if (adsw.getLayout() != null) {
                        objects = adsw.getLayout().getItems();
                    } else {
                        objects = adsw.getWidgets();
                    }
                } else {
                    objects = ((AdsRwtWidgetDef) w).getWidgets();
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
        public AdsUIItemDef getItemAt(int i) {
            return (AdsUIItemDef) ws.get(i);
        }

        public Definitions<? extends AdsUIItemDef> getWidgets() {
            return ws;
        }

        private int indexOf(AdsUIItemDef w) {
            if (w instanceof AdsWidgetDef) {
                return ws.indexOf((AdsWidgetDef) w);
            } else {
                return ws.indexOf((AdsRwtWidgetDef) w);
            }
        }

        public void edited(AdsUIItemDef w) {
            int idx = indexOf(w);
            if (idx >= 0 && idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }

        public void setName(AdsUIItemDef w, String name) {
            Set<String> ns = new HashSet<>();
            for (Object widget : ws) {
                if (w != widget) {
                    ns.add(((Definition) widget).getName());
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
    }

    private final class TableModel extends DefaultTableModel<AdsUIItemDef> {

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
                    (AdsUIProperty) new AdsUIProperty.StringProperty("objectName"),
                    (AdsUIProperty) new AdsUIProperty.LocalizedStringRefProperty("title"),
                    (AdsUIProperty) new AdsUIProperty.ImageProperty("icon"));

            UiProperties props = AdsUIUtil.getUiProperties(item);
            for (int i = 0; i < rows.size(); i++) {
                AdsUIProperty p = props.getByName(rows.get(i).getName());
                if (p != null) {
                    rows.set(i, p);
                } else {
                    final AdsUIProperty duplicate = rows.get(i).duplicate();
                    rows.set(i, duplicate);
                    props.add(duplicate);
                }
            }

            fireTableStructureChanged();
        }
    }
}
