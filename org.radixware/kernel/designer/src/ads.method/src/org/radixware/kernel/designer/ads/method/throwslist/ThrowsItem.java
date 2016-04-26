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

package org.radixware.kernel.designer.ads.method.throwslist;

import java.text.Collator;
import java.util.Comparator;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;


final class ThrowsItem implements Comparable<ThrowsItem> {

    public static final String WAIT_LIST = NbBundle.getMessage(ThrowsItem.class, "WaitList");
    public static final String EMPTY_LIST = NbBundle.getMessage(ThrowsItem.class, "EmptyThrowsList");
    public static Comparator<ThrowsItem> THROWS_NAME_COMPARATOR;

    static {
        THROWS_NAME_COMPARATOR = new Comparator<ThrowsItem>() {

            @Override
            public int compare(ThrowsItem item1, ThrowsItem item2) {
                int result = Collator.getInstance().compare(item1.toString(), item2.toString());
                if (result != 0) {
                    return result;
                }
                Integer i1 = item1.hashCode();
                Integer i2 = item2.hashCode();
                return i1.compareTo(i2);
            }
        };
    }
    private AdsTypeDeclaration type;
    private AdsMethodThrowsList.ThrowsListItem sourceItem;
    private final DescriptionModel descriptionModel;

    public ThrowsItem(AdsMethodThrowsList.ThrowsListItem sourceItem) {
        this.type = sourceItem.getException();
        this.sourceItem = sourceItem;

        descriptionModel = DescriptionModel.Factory.newInstance(sourceItem);
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final ThrowsItem item = (ThrowsItem) obj;
        return type.equals(item.type);
    }

    @Override
    public String toString() {
        return type != null ? type.getQualifiedName(sourceItem) : "<undefined>";
    }

    @Override
    public int compareTo(ThrowsItem other) {
        int result = other.toString().compareTo(this.toString());
        return result;
    }

    AdsMethodThrowsList.ThrowsListItem createItem() {
        return AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(type);
    }

    AdsTypeDeclaration getType() {
        return type;
    }

    void setType(AdsTypeDeclaration type) {
        this.type = type;
    }

    DescriptionModel getDescriptionModel() {
        return descriptionModel;
    }
}
