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

package org.radixware.kernel.common.enums;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EPresentationAttrInheritance implements IKernelIntEnum {

    CONDITION(1, "Condition"),
    CUSTOM_DIALOG(2, "Custom View"),
    ADDONS(8, "Addons"),
    ICON(16, "Icon"),
    RESTRICTIONS(32, "Restrictions"),
    COLUMNS(64, "Columns"),
    TITLE(128, "Title"),
    CHILDREN(1024, "Explorer Items"),
    OBJECT_TITLE_FORMAT(2048, "Object Title Format"),
    PAGES(4096, "Editor Pages"),
    REPORTS(8192, "Reports"),
    CLASS_CATALOG(16384, "Class Catalog"),
    PROPERTY_PRESENTATION_ATTRIBUTES(32768, "Property Presentation Attributes"),
    RIGHTS_INHERITANCE_MODE(65536, "Rights Inheritance Mode"),
    CHILDREN_ORDER(131072, "Explorer Items Order");
    private long value;
    private String title;

    private EPresentationAttrInheritance(long val, String title) {
        this.value = val;
        this.title = title;
    }

    @Override
    public Long getValue() {
        return Long.valueOf(value);
    }

    @Override
    public String getName() {
        return name();
    }

    public String getTitle() {
        return title;
    }

    public static final long toBitField(Set<EPresentationAttrInheritance> enumSet) {
        long result = 0;
        for (EPresentationAttrInheritance e : enumSet) {
            result |= e.value;
        }
        return result;
    }

    public static final EnumSet<EPresentationAttrInheritance> fromBitField(long field) {
        EnumSet<EPresentationAttrInheritance> enumSet = EnumSet.noneOf(EPresentationAttrInheritance.class);

        for (EPresentationAttrInheritance e : values()) {
            if ((field & e.value) != 0L) {
                enumSet.add(e);
            }
        }

        return enumSet;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
