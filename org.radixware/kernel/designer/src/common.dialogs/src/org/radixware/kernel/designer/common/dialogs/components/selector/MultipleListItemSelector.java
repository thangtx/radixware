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
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector.ICheckableItem;


public abstract class MultipleListItemSelector<ItemType extends ICheckableItem, ContextType> extends ListItemSelector<ItemType, ContextType> {

    public static interface IItemInfo {

        String getShortName();

        String getHtmlName();

        String getName();

        Icon getIcon();
    }

    public interface ICheckableItem {

        IItemInfo getInfo();

        boolean isSelected();

        void switchSelection();
        
        void afterSwitchSelection();
    }
    
    private final boolean multipleSelection;
    private ItemType lastSelection = null;
    
    public MultipleListItemSelector() {
        this(true);
    }

    public MultipleListItemSelector(boolean multipleSelection) {
        super(!multipleSelection);

        this.multipleSelection = multipleSelection;
        itemList.setCellRenderer(new CellRender(multipleSelection));
        
        if (multipleSelection) {
            itemList.addMouseListener(new MouseAdapter() {
                private int index;

                @Override
                public void mousePressed(MouseEvent e) {
                    index = itemList.locationToIndex(e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (itemList.locationToIndex(e.getPoint()) == index) {
                        click();
                    }
                }

                private void click() {
                    ItemType item = (ItemType) itemList.getModel().getElementAt(index);
                    
                    item.switchSelection();
                    for (int i = 0; i < itemList.getModel().getSize(); i++) {
                        if (index == i){
                            continue;
                        }
                        final ItemType element = (ItemType) itemList.getModel().getElementAt(i);
                        element.afterSwitchSelection();
                    }
                    itemList.repaint();

                    fireSelectionChange(item);
                }
            });
        } else {
            itemList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    ItemType item = itemList.getSelectedValue();
                    if (item != lastSelection && lastSelection != null && lastSelection.isSelected()) {
                        lastSelection.switchSelection();
                    }
                    if (item != null && !item.isSelected()) {
                        item.switchSelection();
                    }
                    lastSelection = item;
                }
            });
        }
    }

    @Override
    public Collection<ItemType> getSelectedItems() {
        final Collection<ItemType> selected = new ArrayList<>();
        final ListModel<ItemType> model = itemList.getModel();
        for (int i = 0; i < itemList.getModel().getSize(); i++) {
            final ItemType elementAt = model.getElementAt(i);
            if (elementAt.isSelected()) {
                selected.add(elementAt);
            }
        }
        return selected;
    }

    @Override
    public ItemType getSelectedItem() {
        final ListModel<ItemType> model = itemList.getModel();
        for (int i = 0; i < itemList.getModel().getSize(); i++) {
            final ItemType elementAt = model.getElementAt(i);
            if (elementAt.isSelected()) {
                return elementAt;
            }
        }
        return null;
    }

    public final boolean isMultipleSelection() {
        return multipleSelection;
    }
    
    private final class CellRender extends JPanel implements ListCellRenderer {

        private final JCheckBox checkBox;
        private final JLabel label;

        public CellRender(boolean chkBox) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            
            if (chkBox) {
                checkBox = new JCheckBox();
                checkBox.setOpaque(false);
                add(checkBox);
            } else {
                checkBox = null;
            }

            label = new JLabel();
            add(label);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            final ICheckableItem item = (ICheckableItem) value;
            if (item != null) {
                setEnabled(list.isEnabled());
                if (checkBox != null) {
                    checkBox.setSelected(item.isSelected());
                }
                label.setText(item.getInfo().getHtmlName());
                label.setIcon(item.getInfo().getIcon());

                if (item.isSelected()) {
                    setOpaque(true);
                    setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    setOpaque(false);
                    label.setForeground(list.getForeground());
                }
            }
            
            final Dimension preferredSize = label.getPreferredSize();
            int width = preferredSize.width + 4;

            if (checkBox != null) {
                width += checkBox.getPreferredSize().width;
            }

            if (getWidth() < width) {
                setPreferredSize(new Dimension(width, preferredSize.height));
            }

            return this;
        }
    }
}
