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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public final class ItemClusterizator<T> extends JPanel {

    public static <T> List<T> cluster(IClusterizatorModel<T> model) {
        final ItemClusterizator<T> clusterizator = new ItemClusterizator<>(model);

        clusterizator.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        final ModalDisplayer displayer = new ModalDisplayer(clusterizator, "Select items");
        if (displayer.showModal()) {
            return clusterizator.getModel().getSelected();
        }
        return null;
    }

    public static interface IClusterizatorItemProvider<T> {

        List<T> getAllAvaliable();

        List<T> getInherited();

        List<T> getSelected();

        /**
         * Returns immutable items. They cannot be removed from selected.
         */
        List<T> getConstSelected();

        /**
         * Indicates that selected items is inherited.
         */
        boolean isInherit();
    }

    public static interface IClusterizatorModel<T> {

        boolean isInheritable();

        boolean isSortable();

        boolean isInherit();

        void setInherit(boolean inherit);

        List<T> getAvaliable();

        List<T> getSelected();

        /**
         * Returns all items, including inherited and const.
         */
        List<T> getAllSelected();

        void add(T item);

        void remove(T item);

        void up(T item);

        void down(T item);

        /**
         * Indicates that <tt>item</tt> is immutable. It cannot be removed from
         * selected.
         */
        boolean isConst(T item);

        boolean isInvalid(T item);

        String getName(T item);

        Icon getIcon(T item);
    }

    public static abstract class ClusterizatorModel<T> implements IClusterizatorModel<T> {

        private final boolean inheritable;
        private boolean inherit;
        private final boolean sortable;
        private final List<T> selected;
        private final IClusterizatorItemProvider<T> provider;

        public ClusterizatorModel(IClusterizatorItemProvider<T> provider, boolean inheritable, boolean sortable) {
            this.inheritable = inheritable;
            this.sortable = sortable;
            this.inherit = provider.isInherit();

            this.selected = new ArrayList<>(provider.getSelected());
            this.provider = provider;
        }

        @Override
        public boolean isInheritable() {
            return inheritable;
        }

        @Override
        public boolean isSortable() {
            return sortable;
        }

        @Override
        public boolean isInherit() {
            return isInheritable() && inherit;
        }

        @Override
        public void add(T item) {
            if (!isInherit()) {
                selected.add(item);
            }
        }

        @Override
        public void remove(T item) {
            if (!isInherit()) {
                selected.remove(item);

//                if (selected.isEmpty() && isInheritable()) {
//                    setInherit(true);
//                }
            }
        }

        @Override
        public void up(T item) {
            if (!isSortable()) {
                assert false;
                return;
            }
            int index = selected.indexOf(item);
            if (index > 0) {
                T prev = selected.get(index - 1);
                selected.set(index - 1, item);
                selected.set(index, prev);
            }
        }

        @Override
        public void down(T item) {
            if (!isSortable()) {
                assert false;
                return;
            }

            int index = selected.indexOf(item);
            if (index < selected.size() - 1) {
                T next = selected.get(index + 1);
                selected.set(index + 1, item);
                selected.set(index, next);
            }
        }

        @Override
        public List<T> getSelected() {
            return selected;
        }

        @Override
        public List<T> getAvaliable() {
            final List<T> avaliable = new ArrayList<>();
            final List<T> allSelected = getAllSelected();
            for (T item : provider.getAllAvaliable()) {
                if (!allSelected.contains(item)) {
                    avaliable.add(item);
                }
            }

            return avaliable;
        }

        @Override
        public void setInherit(boolean inherit) {
            if (isInheritable()) {
                this.inherit = inherit;
                if (inherit) {
                    selected.clear();
                } else {
                    selected.addAll(provider.getInherited());
                }
            }
        }

        @Override
        public boolean isInvalid(T item) {
            return !provider.getAllAvaliable().contains(item);
        }

        @Override
        public boolean isConst(T item) {
            return provider.getConstSelected().contains(item);
        }

        @Override
        public List<T> getAllSelected() {
            final List<T> all = new ArrayList<>();

            final List<T> constSelected = provider.getConstSelected();
            if (constSelected != null) {
                all.addAll(constSelected);
            }


            if (isInherit()) {
                final List<T> inherited = provider.getInherited();
                if (inherited != null) {
                    all.addAll(inherited);
                }
            }

            all.addAll(getSelected());

            return all;
        }
    }

    public static class DefaultClusterizatorModel<T extends RadixObject> extends ClusterizatorModel<T> {

        public DefaultClusterizatorModel(IClusterizatorItemProvider<T> provider, boolean inheritable, boolean sortable) {
            super(provider, inheritable, sortable);
        }

        @Override
        public String getName(T item) {
            return item.getQualifiedName();
        }

        @Override
        public Icon getIcon(T item) {
            return item.getIcon().getIcon();
        }
    }

    public interface IItem {

        String getName();

        Icon getIcon();
    }

    public static class AbstractClusterizatorModel<T extends IItem> extends ClusterizatorModel<T> {

        public AbstractClusterizatorModel(IClusterizatorItemProvider<T> provider, boolean inheritable, boolean sortable) {
            super(provider, inheritable, sortable);
        }

        @Override
        public String getName(T item) {
            return item.getName();
        }

        @Override
        public Icon getIcon(T item) {
            return item.getIcon();
        }
    }
    
    private IClusterizatorModel<T> model;

    public ItemClusterizator(IClusterizatorModel<T> model) {
        setLayout(new BorderLayout());

        final ClusterizatorPanel panel = new ClusterizatorPanel(model);
        add(panel, BorderLayout.CENTER);

        this.model = model;
    }

    public final IClusterizatorModel<T> getModel() {
        return model;
    }
}