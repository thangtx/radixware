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

package org.radixware.kernel.designer.ads.editors.sortings;

import java.text.Collator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultListModel;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.types.Id;


public class AvailableRadixObjectListModel extends DefaultListModel {

    private class ComparableRadixObject implements Comparable<ComparableRadixObject> {

        Object property;
        RadixObject idAsRdx;

        public ComparableRadixObject(Object property) {
            assert (property != null
                    && (property instanceof RadixObject
                    || property.getClass().equals(Id.class)));
            this.property = property;
            if (property.getClass().equals(Id.class)
                    && members != null) {
                idAsRdx = members.findById((Id) property, EScope.ALL).get();
            }
        }

        @Override
        public int compareTo(ComparableRadixObject o) {
            String ownLC = "<unknown>";
            if (property instanceof RadixObject) {
                ownLC = ((RadixObject) property).getName();
            } else {
                ownLC = idAsRdx != null ? idAsRdx.getName() : ((Id) property).toString();
            }
            String otherLC = "<unknown>";
            if (o.property instanceof RadixObject) {
                otherLC = ((RadixObject) o.property).getName();
            } else {
                otherLC = o.idAsRdx != null ? o.idAsRdx.getName() : ((Id) o.property).toString();
            }
            int result = ownLC.compareTo(otherLC);
            if (result != 0) {
                return result;
            } else {
                if (o.idAsRdx != null && this.idAsRdx != null) {
                    return Collator.getInstance().compare(idAsRdx.toString(), o.idAsRdx.toString());
                } else {
                    return ((Integer) hashCode()).compareTo((Integer) o.hashCode());
                }
            }
        }
    }
    private Set<ComparableRadixObject> content;
    private ExtendableMembers<AdsClassMember> members;

    public AvailableRadixObjectListModel(ExtendableMembers members) {
        this();
        this.members = members;
    }

    public AvailableRadixObjectListModel() {
        content = new TreeSet<ComparableRadixObject>();
    }

    public AvailableRadixObjectListModel(List<? extends Object> radixObjectsList) {
        content = new TreeSet<ComparableRadixObject>();
        for (Object xPropertyDef : radixObjectsList) {
            content.add(new ComparableRadixObject(xPropertyDef));
        }
    }

    public AvailableRadixObjectListModel(List<? extends Object> radixObjectsList, ExtendableMembers members) {
        this.members = members;
        content = new TreeSet<ComparableRadixObject>();
        for (Object xPropertyDef : radixObjectsList) {
            content.add(new ComparableRadixObject(xPropertyDef));
        }
    }

    @Override
    public int getSize() {
        return content.size();
    }

    @Override
    public Object getElementAt(int index) {
        return ((ComparableRadixObject) content.toArray()[index]).property;
    }

    @Override
    public void addElement(Object obj) {
        assert (obj instanceof RadixObject
                || obj.getClass().equals(Id.class));
        ComparableRadixObject cp = new ComparableRadixObject(obj);
        if (!content.contains(cp)) {
            int size = content.size();
            content.add(cp);
            fireContentsChanged(this, 0, size);
        }
    }

    @Override
    public boolean removeElement(Object obj) {
        if (obj instanceof RadixObject
                || obj.getClass().equals(Id.class)) {
            ComparableRadixObject cp = new ComparableRadixObject(obj);
            int size = content.size();
            content.remove(cp);
            fireContentsChanged(this, 0, size);
            return true;

        }
        return false;
    }
}
