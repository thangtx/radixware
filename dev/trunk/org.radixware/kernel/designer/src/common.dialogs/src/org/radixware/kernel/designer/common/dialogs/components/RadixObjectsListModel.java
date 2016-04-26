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

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import javax.swing.AbstractListModel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport;
import org.radixware.kernel.common.types.Id;


public class RadixObjectsListModel extends AbstractListModel {

    public class RadixObjectsListComparator<Object> implements Comparator<Object> {

        private ExtendableDefinitions<Definition> extendableContext;
        private PropertyUsageSupport propertyContext;
        private AdsDefinition definitionContext;

        public RadixObjectsListComparator(PropertyUsageSupport propertyContext) {
            this.propertyContext = propertyContext;
        }

        public RadixObjectsListComparator(ExtendableDefinitions extendableContext) {
            this.extendableContext = extendableContext;
        }

        public RadixObjectsListComparator(AdsDefinition definitionContext) {
            this.definitionContext = definitionContext;
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Id && o2 instanceof Id) {
                RadixObject rdx1 = null;
                RadixObject rdx2 = null;

                if (extendableContext != null) {
                    rdx1 = extendableContext.findById((Id) o1, EScope.LOCAL).get();
                    rdx2 = extendableContext.findById((Id) o2, EScope.LOCAL).get();
                } else if (propertyContext != null) {
                    rdx1 = propertyContext.getReference((Id) o1).findProperty();
                    rdx2 = propertyContext.getReference((Id) o2).findProperty();
                } else if (definitionContext != null && definitionContext.getOwnerDef() instanceof AdsClassDef) {
                    rdx1 = ((AdsClassDef) definitionContext.getOwnerDef()).getProperties().findById((Id) o1, EScope.ALL).get();
                    rdx2 = ((AdsClassDef) definitionContext.getOwnerDef()).getProperties().findById((Id) o2, EScope.ALL).get();
                }

                if (rdx1 != null && rdx2 != null) {
                    return compareRDX(rdx1, rdx2);
                } else {
                    String str1 = "";
                    if (rdx1 == null) {
                        str1 = "<unknown: " + ((Id) o1).toString() + ">";
                    } else {
                        str1 = rdx1.getName();
                    }
                    String str2 = "";
                    if (rdx2 == null) {
                        str2 = "<unknown: " + ((Id) o2).toString() + ">";
                    } else {
                        str2 = rdx2.getName();
                    }
                    return Collator.getInstance().compare(str1, str2);
                }
            }

            return 0;
        }

        private int compareRDX(RadixObject rdx1, RadixObject rdx2) {
            String name1 = rdx1.getName();
            String name2 = rdx2.getName();
            int result = Collator.getInstance().compare(name1, name2);
            if (result == 0) {
                return ((Integer) rdx1.hashCode()).compareTo((Integer) rdx2.hashCode());
            } else {
                return result;
            }
        }
    }
    protected TreeSet<Id> content;
    private ExtendableDefinitions<Definition> extendableContext;
    private PropertyUsageSupport propertyContext;
    private AdsDefinition definitionContext;

    public RadixObjectsListModel(List<Id> ids, ExtendableDefinitions context) {
        this(context);
        for (Id id : ids) {
            content.add(id);
        }
        this.extendableContext = context;
    }

    public RadixObjectsListModel(ExtendableDefinitions context) {
        content = new TreeSet<Id>(new RadixObjectsListComparator(context));
        this.extendableContext = context;
    }

    public RadixObjectsListModel(List<Id> ids, PropertyUsageSupport support) {
        content = new TreeSet<Id>(new RadixObjectsListComparator(support));
        for (Id id : ids) {
            content.add(id);
        }
        this.propertyContext = support;
    }

    public RadixObjectsListModel(List<Id> ids, AdsDefinition definition) {
        content = new TreeSet<Id>(new RadixObjectsListComparator<Id>(definition));
        for (Id id : ids) {
            content.add(id);
        }
        this.definitionContext = definition;
    }

    @Override
    public Object getElementAt(int index) {
        if (index > -1 && index < content.size()) {
            return content.toArray()[index];
        }
        return null;
    }

    @Override
    public int getSize() {
        return content.size();
    }

    public void addElement(Id def) {
        if (def != null
                && !content.contains(def)) {
            RadixObject rdx = null;
            if (extendableContext != null) {
                rdx = extendableContext.findById(def, EScope.ALL).get();
            } else if (propertyContext != null) {
                if (propertyContext.getReference(def) != null) {
                    rdx = propertyContext.getReference(def).findProperty();
                }
            } else if (definitionContext != null) {
                rdx = ((AdsClassDef) definitionContext.getOwnerDef()).getProperties().findById(def, EScope.ALL).get();
            }

            if (rdx != null) {
                int size = content.size();
                content.add(def);
                fireIntervalAdded(this, size, size);
            }
        }
    }

    public void addElement(Definition def) {
        if (!content.contains(def.getId())) {
            int size = content.size();
            content.add(def.getId());
            fireIntervalAdded(this, size, size);
        }
    }

    public void removeElement(Id def) {
        if (content.contains(def)) {
            int size = content.size();
            content.remove(def);
            fireContentsChanged(this, 0, size);
        }
    }
}
