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

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ItemNameRenderer;


class DependentItemsPanel extends JPanel {

    public static interface IDependenciesProvider {

        List<AdsDefinition> getAvaliableItems();

        List<AdsDefinition> getDependentItems(ExtendableDefinitions.EScope scope);

        void remove(Id id);

        void add(Id id);

        boolean isLocal(Id id);
    }

    private static class DependentModel extends AbstractListModel<AdsDefinition> {

        private final IDependenciesProvider provider;
        private final ExtendableDefinitions.EScope scope;

        public DependentModel(IDependenciesProvider provider, ExtendableDefinitions.EScope scope) {
            this.provider = provider;
            this.scope = scope;
        }

        @Override
        public int getSize() {
            return provider.getDependentItems(scope).size();
        }

        @Override
        public AdsDefinition getElementAt(final int index) {
            return provider.getDependentItems(scope).get(index);
        }

        void update() {
            fireContentsChanged(this, 0, getSize());
        }
    }

    final ListDataListener dataListener = new ListDataListener() {

        @Override
        public void intervalAdded(ListDataEvent e) {
            contentsChanged(e);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            contentsChanged(e);
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            if (list.getModel().getSize() <= list.getSelectedIndex()) {
                list.clearSelection();
            }

            updateActionsState();
        }
    };
    private final JList<AdsDefinition> list = new JList<>();
    private final JButton addButton, removeButton;
    private IDependenciesProvider provider;

    public DependentItemsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        final JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        addButton = new JButton(RadixWareIcons.CREATE.ADD.getIcon());
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addDependent();
            }
        });
        addButton.setFocusable(false);
        addButton.setToolTipText("Add depenedent item");
        bar.add(addButton);

        removeButton = new JButton(RadixWareIcons.DELETE.DELETE.getIcon());
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (list.getSelectedValue() != null) {
                    removeDependent(list.getSelectedValue().getId());
                }
            }
        });
        removeButton.setFocusable(false);
        removeButton.setToolTipText("Remove depenedent item");
        removeButton.setEnabled(false);
        bar.add(removeButton);
        bar.add(Box.createGlue());

        final JCheckBox chbLocal = new JCheckBox("Show only local items");
        chbLocal.setSelected(true);
        chbLocal.setFocusable(false);
        bar.add(chbLocal);
        chbLocal.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel(provider, chbLocal.isSelected() ? ExtendableDefinitions.EScope.LOCAL : ExtendableDefinitions.EScope.ALL);
            }
        });

        bar.add(Box.createHorizontalStrut(8));

        add(bar, BorderLayout.PAGE_START);
        add(new JScrollPane(list), BorderLayout.CENTER);

        list.setCellRenderer(new ItemNameRenderer(list));

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateActionsState();
            }
        });
    }

    public void open(IDependenciesProvider provider) {
        this.provider = provider;
        updateModel(provider, ExtendableDefinitions.EScope.LOCAL);
    }

    private void updateModel(IDependenciesProvider provider, ExtendableDefinitions.EScope scope) {
        final DependentModel dependentModel = new DependentModel(provider, scope);
        dependentModel.addListDataListener(dataListener);
        list.setModel(dependentModel);
    }

    private void addDependent() {
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(provider.getAvaliableItems());
        final List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
        if (definitions != null) {
            for (final Definition definition : definitions) {
                provider.add(definition.getId());
            }
        }
        getListModel().update();
    }

    private void removeDependent(Id id) {
        provider.remove(id);
        getListModel().update();
    }

    private void updateActionsState() {
        if (list.getSelectedIndex() == -1) {
            removeButton.setEnabled(false);
        } else if (list.getSelectedValue() != null) {
            removeButton.setEnabled(provider.isLocal(list.getSelectedValue().getId()));
        }
    }

    private DependentModel getListModel() {
        return (DependentModel) list.getModel();
    }
}
