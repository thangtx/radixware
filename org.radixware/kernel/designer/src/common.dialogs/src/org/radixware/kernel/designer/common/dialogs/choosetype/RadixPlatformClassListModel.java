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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.util.*;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
import org.radixware.kernel.designer.common.dialogs.utils.SearchFieldAdapter;


class RadixPlatformClassListModel extends AbstractListModel {

    private static final Pattern INNER_CLASS_NAME = Pattern.compile("[1-9].*");
    private List<RadixPlatformClassListItem> items;

    RadixPlatformClassListModel(Set<RadixPlatformClassListItem> items) {
//        this.items = new TreeSet<RadixPlatformClassListItem>(RadixPlatformClassListItem.PLATFORM_CLASS_COMPARATOR);
        this.items = new ArrayList<RadixPlatformClassListItem>();
        this.items.addAll(items);
        sortItems();
    }

    RadixPlatformClassListModel(List<String> classes) {
//        this.items = new TreeSet<RadixPlatformClassListItem>(RadixPlatformClassListItem.PLATFORM_CLASS_COMPARATOR);
        this.items = new ArrayList<RadixPlatformClassListItem>();
        for (String cls : classes) {
            int dot = cls.lastIndexOf(".");
            if (dot != -1) {
                String sub = cls.substring(dot + 1, cls.length());
                if (!INNER_CLASS_NAME.matcher(sub).matches() && sub.length() != 1) {
                    addItem(new RadixPlatformClassListItem(cls));
//                    this.items.add(new RadixPlatformClassListItem(cls));
                }
            } else {
                addItem(new RadixPlatformClassListItem(cls));
//                this.items.add(new RadixPlatformClassListItem(cls));
            }
        }
        sortItems();
    }

    private void sortItems() {
        Collections.sort(items, RadixPlatformClassListItem.PLATFORM_CLASS_COMPARATOR);
    }

    private void addItem(RadixPlatformClassListItem item) {
        this.items.add(item);
    }

    @Override
    public Object getElementAt(int index) {
//        return items.toArray()[index];
        return items.get(index);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    public Set<RadixPlatformClassListItem> findMatches(String text) {
        String lc = text.toLowerCase();
        List<Set<RadixPlatformClassListItem>> matches = new ArrayList<Set<RadixPlatformClassListItem>>(2);
        for (int i = 0; i < 2; i++) {
            matches.add(new TreeSet<RadixPlatformClassListItem>(RadixPlatformClassListItem.PLATFORM_CLASS_COMPARATOR));
        }
        for (RadixPlatformClassListItem item : this.items) {
            int level = 2;
            if (SearchFieldAdapter.isFitingToken(lc, item.toString().toLowerCase())) {
                level = Math.min(0, level);
            } else if (SearchFieldAdapter.isFitingToken(lc, item.getOwnerName().toLowerCase())) {
                level = Math.min(1, level);
            }
            if (level < 2) {
                matches.get(level).add(item);
            }
        }
        Set<RadixPlatformClassListItem> result = new HashSet<RadixPlatformClassListItem>();
        for (Set<RadixPlatformClassListItem> m : matches) {
            result.addAll(m);
        }
        return result;
    }

    public List<RadixPlatformClassListItem> getItems() {
        return new LinkedList<RadixPlatformClassListItem>(items);
    }
}
