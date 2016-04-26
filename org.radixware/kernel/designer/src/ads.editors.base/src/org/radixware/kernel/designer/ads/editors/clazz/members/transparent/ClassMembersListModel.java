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

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;


final class ClassMembersListModel extends AbstractListModel<ClassMemberItem> {

    private final List<ClassMemberItem> items;

    public ClassMembersListModel(List<ClassMemberItem> items) {
        this.items = items;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public ClassMemberItem getElementAt(int index) {
        return items.get(index);
    }

    public List<ClassMemberItem> getSelectedItems() {
        List<ClassMemberItem> selected  = new ArrayList<>();

        for (ClassMemberItem item : items) {
            if (item.isSelected()) {
                selected.add(item);
            }
        }
        return selected;
    }
}
