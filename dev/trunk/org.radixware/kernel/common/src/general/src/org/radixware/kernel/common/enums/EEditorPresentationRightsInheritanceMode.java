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

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;

public enum EEditorPresentationRightsInheritanceMode implements IKernelIntEnum {

    FROM_REPLACED(Long.valueOf(2), RadixResourceBundle.getMessage(EEditorPresentationRightsInheritanceMode.class, "EditorPresentation-RightInheritanceMode-FromReplaced")),
    FROM_DEFINED(Long.valueOf(1), RadixResourceBundle.getMessage(EEditorPresentationRightsInheritanceMode.class, "EditorPresentation-RightInheritanceMode-FromDefined")),
    NONE(Long.valueOf(0), RadixResourceBundle.getMessage(EEditorPresentationRightsInheritanceMode.class, "EditorPresentation-RightInheritanceMode-None"));

    private final Long value;
    private final String name;
    //constructors   

    private EEditorPresentationRightsInheritanceMode(final Long x, final String name) {
        value = x;
        this.name = name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EEditorPresentationRightsInheritanceMode getForName(final String name){
        for (EEditorPresentationRightsInheritanceMode e : EEditorPresentationRightsInheritanceMode.values()){
            if (e.name.equals(name)){
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EEdPresRightsInheritMode has no item with name: " + name,name);
    }

    public static EEditorPresentationRightsInheritanceMode getForValue(final long val) {
        for (EEditorPresentationRightsInheritanceMode e : EEditorPresentationRightsInheritanceMode.values()) {
            if (e.value.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EEdPresRightsInheritMode has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }
}
