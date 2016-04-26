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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class ValTypeComboBox extends javax.swing.JPanel {

    private javax.swing.JComboBox combo;
    private static final Icon NONE_ICON = RadixWareIcons.METHOD.VOID.getIcon(16, 16);
    private static final Icon SMALL_NONE_ICON = RadixWareIcons.METHOD.VOID.getIcon(13, 13);

    class ValTypeListItem {

        private EValType item;

        public ValTypeListItem(EValType item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return item != null ? item.getName() : "None";
        }

        public EValType getItem() {
            return item;
        }

        public RadixIcon getIcon() {
            return item != null ? RadixObjectIcon.getForValType(item) : null;
        }
    }
    private boolean isModifing = true;
    private ValTypeListItem valTypeItem;
    private Set<EValType> filter;
    private HashMap<EValType, ValTypeListItem> filterMap = new HashMap<>();
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private final StateManager typeManager;

    public class IconListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null
                    && value instanceof ValTypeListItem) {
                ValTypeListItem type = (ValTypeListItem) value;
                RadixIcon icon = type.getIcon();
                if (ValTypeComboBox.this.combo.isPopupVisible()) {
                    setIcon(icon != null ? icon.getIcon(16, 16) : NONE_ICON);
                } else {
                    setIcon(icon != null ? icon.getIcon(13, 13) : SMALL_NONE_ICON);
                }
                setText(type.toString());
            } else {
                setText("<Not defined>");
            }

            return this;
        }
    }
    private int defaultHeight = 10;

    public ValTypeComboBox() {
        super();
        combo = new javax.swing.JComboBox();
        setLayout(new BorderLayout());
        add(combo, BorderLayout.CENTER);
        defaultHeight = combo.getPreferredSize().height;

        combo.setRenderer(new IconListRenderer());
        typeManager = new StateManager(this);
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isModifing) {
                    setValTypeItem((ValTypeListItem) combo.getSelectedItem());
                    changeSupport.fireChange();
                }
            }
        };
        combo.addActionListener(listener);
    }

    private ValTypeListItem getValTypeItem() {
        if (combo.getSelectedItem() != null) {
            return (ValTypeListItem) combo.getSelectedItem();
        }
        return valTypeItem;
    }

    private void setValTypeItem(ValTypeListItem valTypeItem) {
        this.valTypeItem = valTypeItem;
    }

    public javax.swing.JComboBox getComboComponent() {
        return combo;
    }

    public ComboBoxEditor getEditor() {
        return combo.getEditor();
    }

    @Override
    public int getBaseline(int width, int height) {
        return combo.getBaseline(width, height);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize != null) {
            combo.setPreferredSize(new Dimension(preferredSize.width, defaultHeight));
            super.setPreferredSize(new Dimension(preferredSize.width, defaultHeight));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(combo.getPreferredSize().width, defaultHeight);
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize != null) {
            combo.setMaximumSize(maximumSize);
        }
        super.setMaximumSize(maximumSize);
    }

    @Override
    public Dimension getMaximumSize() {
        return combo.getMaximumSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize != null) {
            combo.setMinimumSize(minimumSize);
        }
        super.setMinimumSize(minimumSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return combo.getMinimumSize();
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public boolean isComplete() {
        if (filter.contains(getValTypeItem().getItem())) {
            typeManager.ok();
            return true;
        } else {
            typeManager.error(NbBundle.getMessage(ValTypeComboBox.class, "Error-Invalid-ValType"));
            return false;
        }
    }

    public Set<EValType> getFilter() {
        return filter;
    }

    public void setFilter(Set<EValType> filter) {
        isModifing = false;
        this.filter = filter;
        combo.removeAllItems();
        if (filter != null) {
            //**** sorting for correct displaying
            List<EValType> displayList = new ArrayList<>();
            if (filter.contains(null)) {
                for (EValType evt : filter) {
                    if (evt != null) {
                        displayList.add(evt);
                    }
                }
                Collections.sort(displayList);
                displayList.add(0, null);
            } else {
                displayList.addAll(filter);
                Collections.sort(displayList);
            }
            //****

            filterMap = new HashMap<>();
            combo.setForeground(Color.BLACK);
            for (EValType type : displayList) {
                ValTypeListItem item = new ValTypeListItem(type);
                if (filterMap.get(type) == null) {
                    filterMap.put(type, item);
                }
                combo.addItem(item);
            }
            if (getValTypeItem() == null) {
                setValTypeItem(filterMap.get((EValType) filter.toArray()[0]));
            } else {
                if (filter.contains(getValTypeItem().getItem())) {
                    setValTypeItem(filterMap.get(getValTypeItem().getItem()));
                } else {
                    setValTypeItem(filterMap.get((EValType) filter.toArray()[0]));
                }
            }
        } else if (getValTypeItem() != null) {
            combo.setForeground(Color.RED);
            combo.addItem(getValTypeItem());
        }
        combo.setSelectedItem(getValTypeItem());
        isModifing = true;
    }

    public EValType getValType() {
        return getValTypeItem().getItem();
    }

    public void setValType(EValType valType) {
        this.setValTypeItem(filterMap.get(valType));
        if (this.getValTypeItem() == null) {
            this.setValTypeItem(new ValTypeListItem(valType));
        }
        if (filter == null) {
            combo.removeAllItems();
            combo.setForeground(Color.RED);
            combo.addItem(this.getValTypeItem());
        } else {
            combo.setForeground(Color.BLACK);
        }
        combo.setSelectedItem(valTypeItem);
    }

    @Override
    public void setEnabled(boolean enabled) {
        combo.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return combo.isEnabled();
    }
}
