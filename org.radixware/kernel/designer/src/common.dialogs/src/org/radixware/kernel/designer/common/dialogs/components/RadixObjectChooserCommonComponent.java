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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel.DoubleClickChooserEvent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel.DoublieClickChooserSupport;


public class RadixObjectChooserCommonComponent implements IRadixObjectChooserComponent, CommonRadixObjectPopupMenu.IRadixObjectPopupMenuOwner {

    protected javax.swing.JList list;

    public RadixObjectChooserCommonComponent() {
        list = new javax.swing.JList();
        ListSelectionListener listListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                RadixObjectChooserCommonComponent.this.changeSupport.fireChange();
            }
        };
        list.addListSelectionListener(listListener);

        CommonRadixObjectPopupMenu menu = new CommonRadixObjectPopupMenu(this);
        list.setComponentPopupMenu(menu);

        component = new JScrollPane();
        component.setViewportView(list);
        list.setModel(new DefaultListModel());
        list.setCellRenderer(new AbstractItemRenderer(list) {
            @Override
            public String getObjectName(Object object) {
                return object != null ? ((RadixObject) object).getName() : "<Not Defined>";
            }

            @Override
            public String getObjectLocation(Object object) {
                return "";
            }

            @Override
            public RadixIcon getObjectIcon(Object object) {
                if (object != null) {
                    return ((RadixObject) object).getIcon();
                }
                return RadixObjectIcon.UNKNOWN;
            }

            @Override
            public RadixIcon getObjectLocationIcon(Object object) {
                return null;
            }
        });

        MouseAdapter listMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int click = e.getClickCount();
                if (click == 2) {
                    int cellIndex = list.locationToIndex(e.getPoint());
                    if (cellIndex >= 0 && cellIndex < list.getModel().getSize()) {
                        getDoubleClickSupport().fireEvent(new DoubleClickChooserEvent(cellIndex));
                    }
                }
            }
        };
        list.addMouseListener(listMouseAdapter);
    }

    @Override
    public boolean isPopupMenuAvailable() {
        return hasSelection();
    }

    @Override
    public RadixObject getSelectedRadixObject() {
        Object[] selection = getSelectedItems();
        if (selection != null && selection.length > 0) {
            return (RadixObject) selection[0];
        }
        return null;
    }

    @Override
    public void addSelectionEventListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @Override
    public void removeSelectionEventListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    @Override
    public void addAllItems(Object[] objects) {
        DefaultListModel dm = (DefaultListModel) list.getModel();
        for (Object r : objects) {
            dm.addElement(r);
        }
    }

    @Override
    public void removeAll(Object[] objects) {
        DefaultListModel dm = (DefaultListModel) list.getModel();
        for (Object r : objects) {
            dm.removeElement(r);
        }
    }

    @Override
    public Integer getItemCount() {
        return list.getModel().getSize();
    }

    @Override
    public Integer getSelectedIndex() {
        return list.getSelectedIndex();
    }

    @Override
    public void setSelectedItem(Integer index) {
        list.setSelectedIndex(index);
    }

    @Override
    public void setSelectedItems(Object[] items) {
        final List<Integer> indexes = new ArrayList<>(items.length);
        for (int i = 0; i < list.getModel().getSize(); i++) {
            final Object curr = list.getModel().getElementAt(i);
            for (final Object selected : items) {
                if (Objects.equals(curr, selected)) {
                    indexes.add(i);
                }
            }
        }

        final int[] selected = new int[items.length];
        for (int i = 0; i < indexes.size(); i++) {
            selected[i] = indexes.get(i);
        }
        list.setSelectedIndices(selected);
        if (selected.length > 0) {
            list.ensureIndexIsVisible(selected[0]);
        }
    }

    @Override
    public Object[] getSelectedItems() {
        Object[] selection = list.getSelectedValues();
        return selection;
    }

    @Override
    public boolean hasSelection() {
        return list.getModel().getSize() > 0 && list.getSelectedIndex() >= 0;
    }
    private javax.swing.JScrollPane component;

    @Override
    public JComponent getVisualComponent() {
        return component;
    }

    @Override
    public JComponent getLabelComponent() {
        return new javax.swing.JLabel("");
    }

    @Override
    public boolean isReadonly() {
        return !list.isEnabled();
    }

    @Override
    public void setReadonly(boolean readonly) {
        this.list.setEnabled(!readonly);
    }

    @Override
    public void updateContent() {
        Collection<? extends Definition> defs = cfg.collectAllowedDefinitions();//DefinitionsUtils.collectAround(context, cfg.getProvider());
        if (defs != null) {
            DefaultListModel dm = new DefaultListModel();
            for (Definition d : defs) {
                dm.addElement(d);
            }
            list.setModel(dm);
        }
    }
    protected ChooseDefinitionCfg cfg;
    protected Definition context;

    public void open(Definition context, ChooseDefinitionCfg cfg) {
        this.cfg = cfg;
        this.context = context;
        updateContent();
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    class TestItemRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof RadixObject) {
                RadixObject r = (RadixObject) value;
                setText(r.getName());
                setIcon(r.getIcon() != null ? r.getIcon().getIcon(16, 16) : null);
            } else {
                setText(value.toString());
            }

            return this;
        }
    }
    protected DoublieClickChooserSupport doubleClickSupport;

    @Override
    public synchronized DoublieClickChooserSupport getDoubleClickSupport() {
        if (doubleClickSupport == null) {
            doubleClickSupport = new DoublieClickChooserSupport();
        }
        return doubleClickSupport;
    }
}
