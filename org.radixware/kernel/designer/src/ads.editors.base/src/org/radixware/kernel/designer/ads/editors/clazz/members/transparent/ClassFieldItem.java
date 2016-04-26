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
import java.util.Collection;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Field;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector.IItemInfo;
import org.radixware.kernel.designer.common.dialogs.utils.AdsClassMembersUtils;


public class ClassFieldItem extends ClassMemberItem<Field> {

    private final MultipleListItemSelector.IItemInfo info;
    public ClassFieldItem(Field field, Definition context) {
        super(field, false);

        info = new AdsClassMembersUtils.TransparentFieldInfo(field, context);
    }

    @Override
    public EClassMemberType getClassMemberType() {
        return EClassMemberType.FIELD;
    }

    @Override
    public IItemInfo getInfo() {
        return info;
    }

    public static Collection<ClassMemberItem> createCollection(Collection<Field> fields, Definition context, RadixPlatformClass platformClass) {

        ArrayList<ClassMemberItem> collection = new ArrayList<>();

        for (Field method : fields) {
            collection.add(new ClassFieldItem(method, context));
        }
        return collection;
    }

    @Override
    public void afterSwitchSelection() {
    }
}
