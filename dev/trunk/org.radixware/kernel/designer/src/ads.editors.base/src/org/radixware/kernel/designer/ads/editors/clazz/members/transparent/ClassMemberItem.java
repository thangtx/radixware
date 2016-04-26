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

import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector;


public abstract class ClassMemberItem<MemberType extends RadixPlatformClass.ClassMember> implements MultipleListItemSelector.ICheckableItem {

    private boolean selected;
    private final MemberType source;

    public ClassMemberItem(MemberType source, boolean selected) {
        this.selected = selected;
        this.source = source;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void switchSelection() {
        selected = !selected;
    }
    
    @Override
    public String toString() {
        if (getInfo() != null) {
            return getInfo().getShortName();
        }
        return "";
    }

    public final MemberType getSource() {
        return source;
    }

    public EAccess getAccess() {
        return source.getAccess();
    }

    public boolean isStatic() {
        return source.isStatic();
    }

    public boolean isFinal() {
        return source.isFinal();
    }

    public boolean isAbstract() {
        return source.isAbstract();
    }

    public abstract EClassMemberType getClassMemberType();
}
