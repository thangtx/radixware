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

package org.radixware.kernel.common.defs.ads.clazz.enumeration;

import java.util.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.EnumClassFieldDefinition;


public final class AdsFields extends ExtendableMembers<AdsEnumClassFieldDef> implements IJavaSource {

    public static final class Factory {

        public static AdsFields newInstance(AdsEnumClassDef owner) {
            return new AdsFields(owner);
        }

        public static AdsFields loadFrom(AdsEnumClassDef owner, ClassDefinition.EnumClassFields xSet) {
            return new AdsFields(owner, xSet);
        }
    }

    public static class LocalFields extends AdsDefinitions<AdsEnumClassFieldDef> {

        private LocalFields() {
            super();
        }
    }

    private AdsFields(AdsEnumClassDef context) {
        super(context, new LocalFields());
    }

    private AdsFields(AdsEnumClassDef context, ClassDefinition.EnumClassFields xSet) {
        this(context);

        if (xSet != null) {
            final List<EnumClassFieldDefinition> list = xSet.getFieldList();
            if (list != null && !list.isEmpty()) {
                for (final EnumClassFieldDefinition f : list) {
                    AdsEnumClassFieldDef field = AdsEnumClassFieldDef.Factory.loadFrom(f);
                    if (field != null) {
                        getLocal().add(field);
                    }
                }
            }
        }
    }

    /**
     *
     * @return number of local fields that is not overwriting lower fields
     */
    public int getLocalSize() {
        int size = getLocal().size();
        for (final AdsEnumClassFieldDef f : getLocal()) {
            if (f.isOverwrite() && f.getHierarchy().findOverwritten().get() != null) {
                --size;
            }
        }
        return size;
    }

    /**
     *
     * @return number of all local and overwrite fields
     */
    public int getFullSize() {

        final HierarchyIterator<ExtendableDefinitions<AdsEnumClassFieldDef>> iter = newIterator(EScope.LOCAL_AND_OVERWRITE, HierarchyIterator.Mode.FIND_FIRST);
        int size = 0;
        while (iter.hasNext()) {
            size += ((AdsFields) iter.next().first()).getLocalSize();
        }
        return size;
    }

    /**
     *
     * @return field list ordered by hierarchy
     */
    public List<AdsEnumClassFieldDef> getOrdered() {
        final List<AdsEnumClassFieldDef> list = new ArrayList<>();
        final Map<Id, Integer> ids = new HashMap<>();

        final List<ExtendableDefinitions<AdsEnumClassFieldDef>> levels = new ArrayList<>();
        final HierarchyIterator<ExtendableDefinitions<AdsEnumClassFieldDef>> iter = newIterator(EScope.LOCAL_AND_OVERWRITE, HierarchyIterator.Mode.FIND_FIRST);

        while (iter.hasNext()) {
            levels.add(iter.next().first());
        }

        int pos = 0;
        for (int i = levels.size() - 1; i >= 0; --i) {
            for (final AdsEnumClassFieldDef f : levels.get(i).getLocal()) {

                final Id currId = f.getId();

                if (ids.containsKey(currId)) {
                    int index = ids.get(currId);
                    list.set(index, f);
                } else {
                    list.add(f);
                    ids.put(currId, pos++);
                }
            }
        }

        return list;
    }

    public void appendTo(ClassDefinition.EnumClassFields xSet, ESaveMode saveMode) {
        for (final AdsEnumClassFieldDef field : this.getLocal()) {
            if (saveMode == ESaveMode.API && !field.isPublished()) {
                continue;
            }
            field.appendTo(xSet.addNewField(), saveMode);
        }
    }

    @Override
    public ExtendableMembers<AdsEnumClassFieldDef> findInstance(AdsDefinition clazz) {
        return clazz instanceof AdsEnumClassDef ? ((AdsEnumClassDef) clazz).getFields() : null;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEnumFieldsCodeWriter(this, AdsFields.this, purpose);
            }
        };
    }

    @Override
    public String getName() {
        return "Items";
    }
}