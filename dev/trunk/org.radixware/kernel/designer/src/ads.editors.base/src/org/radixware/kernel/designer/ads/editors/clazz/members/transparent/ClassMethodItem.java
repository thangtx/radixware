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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector.IItemInfo;
import org.radixware.kernel.designer.common.dialogs.utils.AdsClassMembersUtils;


public class ClassMethodItem extends ClassMemberItem<Method> {

    private final MultipleListItemSelector.IItemInfo info;

    public ClassMethodItem(Method method, Definition context, RadixPlatformClass platformClass) {
        super(method, false);

        info = new AdsClassMembersUtils.TransparentMethodInfo(platformClass, method, context);
    }

    public ClassMethodItem(Method method, AdsClassDef context) {
        super(method, false);

        info = new AdsClassMembersUtils.TransparentMethodInfo(method, context);
    }

    @Override
    public EClassMemberType getClassMemberType() {
        return EClassMemberType.METHOD;
    }

    @Override
    public IItemInfo getInfo() {
        return info;
    }

    public boolean isOverriden(){
        return  getSource().isOverriden();
    }
    public static Collection<ClassMemberItem> createCollection(Collection<Method> methods, Definition context, RadixPlatformClass platformClass) {

        final ArrayList<ClassMemberItem> collection = new ArrayList<>();

        for (final Method method : methods) {
            collection.add(new ClassMethodItem(method, context, platformClass));
        }
        return collection;
    }

    @Override
    public void afterSwitchSelection() {
    }

    
}
