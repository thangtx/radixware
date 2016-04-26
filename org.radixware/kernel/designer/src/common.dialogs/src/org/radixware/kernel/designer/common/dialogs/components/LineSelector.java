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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class LineSelector<T> extends JPanel {


    public interface IDefinitionItem {

        Definition getDefinition();
    }

    private static class EditDisplayer<T> extends ModalDisplayer {

        private static class ListItem {

            private final Object item;
            private final String name;

            public ListItem(Object item, String name) {
                this.item = item;
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        private static <T> JList<ListItem> createList(IItemProvider<T> itemProvider) {
            final JList<ListItem> list = new JList<>();
            final List<ListItem> items = new ArrayList<>();

            for (final T item : itemProvider.getSelectedItems()) {
                items.add(new ListItem(item, itemProvider.getItemName(item)));
            }

            return new JList<>(items.toArray(new ListItem[0]));
        }
        private final IItemProvider<T> itemProvider;
        private final JList<ListItem> list;

        EditDisplayer(IItemProvider<T> itemProvider) {
            this(createList(itemProvider), itemProvider);
        }

        private EditDisplayer(JList<ListItem> list, IItemProvider<T> itemProvider) {
            super(new JScrollPane(list), "Choose item for editing");
            this.itemProvider = itemProvider;
            this.list = list;

            if (list.getModel().getSize() > 0) {
                list.setSelectedIndex(0);
            }

            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        EditDisplayer.this.close(true);
                    }
                }
            });
        }

        public T chooseItem() {
            if (showModal()) {
                return (T) list.getSelectedValue().item;
            }
            return null;
        }
    }

    public interface IItemProvider<T> {

        Collection<T> getSelectedItems();

        void setSelectedItems(Collection<T> items);

        Collection<T> selectItems();

        String getItemName(T item);

        String getItemToolTip(T item);

        Icon getItemIcon(T item);

        void open(T item);
    }
    private JButton btnSelect;
    private JButton btnClear;
    private JButton btnEdit;
    private IItemProvider<T> itemProvider;
    private Collection<T> selectedItems;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private JPanel content;

    public LineSelector() {
        super.setLayout(new GridBagLayout());

        btnSelect = new JButton(RadixWareDesignerIcon.DIALOG.CHOOSE.getIcon());
        btnClear = new JButton(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        btnEdit = new JButton(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        add(content, constraints);

        final JPanel controllsWrap = new JPanel();
        controllsWrap.setLayout(new BoxLayout(controllsWrap, BoxLayout.X_AXIS));
        final JPanel controlls = new JPanel(new EditorLayout(24));
        controllsWrap.add(controlls);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(controllsWrap, constraints);

        controlls.add(btnSelect);
        controlls.add(btnEdit);
        controlls.add(btnClear);

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedItems != null && selectedItems.size() == 1) {
                    itemProvider.open(selectedItems.iterator().next());
                } else if (!selectedItems.isEmpty()) {
                    final T item = selectedItems.iterator().next();

                    T selectedItem = null;
                    if (item instanceof Definition) {
                        selectedItem = (T) ChooseDefinition.chooseDefinition(ChooseDefinitionCfg.Factory.newInstance((Collection<? extends Definition>) selectedItems));
                    } else if (item instanceof IDefinitionItem) {
                        final List<Definition> objects = new ArrayList<>(selectedItems.size());
                        for (T i : selectedItems) {
                            final Definition def = ((IDefinitionItem) i).getDefinition();
                            if (def != null) {
                                objects.add(def);
                            }
                        }
                        final Definition def = ChooseDefinition.chooseDefinition(ChooseDefinitionCfg.Factory.newInstance((Collection<? extends Definition>) objects));
                        if (def != null) {
                            for (T i : selectedItems) {
                                if (((IDefinitionItem) i).getDefinition() == def) {
                                    selectedItem = i;
                                }
                            }
                        }
                    } else {
                        selectedItem = new EditDisplayer<>(itemProvider).chooseItem();
                    }

                    if (selectedItem != null) {
                        itemProvider.open(selectedItem);
                    }
                }
            }
        });

        changeSupport = new ChangeSupport(this);
        changeSupport.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
    }

    @Override
    public final void setLayout(LayoutManager mgr) {
    }

    public void open(IItemProvider<T> itemProvider) {
        this.itemProvider = itemProvider;

        update();
    }

    public final void update() {
        JPanel line = null;

        selectedItems = itemProvider.getSelectedItems();
        content.removeAll();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            int pos = 0;
            for (final T item : selectedItems) {
                if (pos > 3) {
                    pos = 0;
                }
                if (pos != 0) {
                    final JLabel lblComma = new JLabel(", ");
                    line.add(lblComma);
                } else {
                    line = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
                    content.add(line);
                }

                final JLabel lbl = new JLabel(itemProvider.getItemName(item));
                lbl.setIcon(itemProvider.getItemIcon(item));
                lbl.setToolTipText(itemProvider.getItemToolTip(item));
                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && content.isEnabled()) {
                            itemProvider.open(item);
                        }
                    }
                });
                line.add(lbl);
                ++pos;
            }
            line.revalidate();
        }
        updateButtons();
    }

    public List<T> getSelectedItems() {
        return selectedItems != null ? new ArrayList<>(selectedItems) : Collections.EMPTY_LIST;
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    private void select() {
        final Collection<T> newSelectItems = itemProvider.selectItems();
        if (!Objects.equals(newSelectItems, selectedItems)) {
            itemProvider.setSelectedItems(newSelectItems);
            selectedItems = newSelectItems;
            changeSupport.fireChange();
        }
    }

    private void clear() {
        if (selectedItems != null && !selectedItems.isEmpty()) {
            itemProvider.setSelectedItems(Collections.EMPTY_LIST);
            selectedItems = Collections.EMPTY_LIST;
            changeSupport.fireChange();
        }
    }

    public void setReadonly(boolean readonly) {
        content.setEnabled(!readonly);
        btnSelect.setEnabled(!readonly);
        btnClear.setEnabled(!readonly);
    }
    
    private void updateButtons() {
        if (selectedItems != null && !selectedItems.isEmpty()) {
            btnClear.setVisible(true);
            btnEdit.setVisible(true);
        } else {
            btnClear.setVisible(false);
            btnEdit.setVisible(false);
        }
    }
}
