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

package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.util.*;
import javax.swing.JComponent;
import javax.swing.ListModel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.designer.common.dialogs.components.selector.IItemProvider;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.ListItemSelector.AssemblyModelTask;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;


public class ClassMemberSelector extends MultipleListItemSelector<ClassMemberItem, IItemProvider<ClassMemberItem>> {

    private final List<ClassMemberItem> members = new ArrayList<>();
    private final ItemNameFilter<ClassMemberItem> itemNameFilter;
    private Definition contextDefinition;

    public ClassMemberSelector(boolean multipleSelection) {
        super(multipleSelection);

        itemNameFilter = new SignatureFilter();
        getSelectorLayout().addFilterComponent(itemNameFilter);

        if (!multipleSelection) {
            delegateNavigationKeys((JComponent) itemNameFilter.getComponent());
        }
    }

    @Override
    public List<ClassMemberItem> getSelectedItems() {
        final List<ClassMemberItem> selected = new ArrayList<>();

        for (final ClassMemberItem item : members) {
            if (item.isSelected()) {
                selected.add(item);
            }
        }

        return selected;
    }

    @Override
    protected AssemblyModelTask getAssemblyModelTask() {
        return new AssemblyModelTask() {

            @Override
            protected ListModel assemblyModel() {
                final List<ClassMemberItem> filteredItems = new ArrayList<>();

                boolean selectedAll = true;
                synchronized (members) {
                    for (final ClassMemberItem item : members) {
                        if (accept(item)) {
                            filteredItems.add(item);
                            selectedAll = selectedAll && item.isSelected();
                        } else {
                           if (item.isSelected()){
                               item.switchSelection();
                           }
                        }
                    }
                }

                if (isMultipleSelection() && filteredItems.size() > 1) {

                    final AddAllItem addAllItem = new AddAllItem(filteredItems);
                    filteredItems.add(0, addAllItem);

                    if (selectedAll) {
                        addAllItem.switchSelection();
                    }
                }

                return new ClassMembersListModel(filteredItems);
            }

        };
    }

    @Override
    protected void openImpl(Collection<ClassMemberItem> items) {

        synchronized (members) {
            members.clear();
            for (final ClassMemberItem classMemberItem : getContext().getAllItems()) {
                members.add(classMemberItem);
            }
            
            // ordering by name
            Collections.sort(members, new Comparator<ClassMemberItem>() {

                @Override
                public int compare(ClassMemberItem o1, ClassMemberItem o2) {
                    return o1.toString().compareToIgnoreCase(o2.toString());
                }

            });
        }

        super.openImpl(items);
    }

    @Override
    public boolean requestFocusInWindow() {
        return itemNameFilter.getComponent().requestFocusInWindow();
    }

    public void open(Definition context, RadixPlatformClass cl) {
        this.contextDefinition = context;
        open(new ClassMemberProvider(cl, contextDefinition), (ClassMemberItem) null);
    }

    private static final class ClassMemberProvider implements IItemProvider<ClassMemberItem> {

        private final RadixPlatformClass source;
        private final Definition contextDefinition;

        public ClassMemberProvider(RadixPlatformClass source, Definition definitionContext) {
            this.source = source;
            this.contextDefinition = definitionContext;
        }

        @Override
        public Collection<ClassMemberItem> getAllItems() {
            return getItems(null);
        }

        @Override
        public Collection<ClassMemberItem> getItems(Object key) {

            final Collection<ClassMemberItem> members = new ArrayList<>();
            if (key == EClassMemberType.FIELD || key == null) {
                members.addAll(ClassFieldItem.createCollection(PublicationUtils.getAllFieldsForPublishing(source), contextDefinition, source));
            }
            if (key == EClassMemberType.METHOD || key == null) {
                members.addAll(ClassMethodItem.createCollection(PublicationUtils.getAllMethodsForPublishing(source), contextDefinition, source));
            }
            return members;
        }

    }
}
