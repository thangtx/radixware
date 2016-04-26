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

package org.radixware.kernel.common.defs;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.environment.IRadixEnvironment;


public interface IEnumDef {

    Id getId();

    String getName();

    String getDescription();

    EValType getItemType();

    interface IItem {

        Id getId();

        String getName();

        String getTitle(final IRadixEnvironment env);

        String getTitle(final IRadixEnvironment env, final EIsoLanguage lang);

        ValAsStr getValue();

        Collection<Id> getDomainIds();
    }

    interface IItems<T extends IItem> {

        List<T> list(EScope scope);

        IItem findItemById(Id itemId, EScope scope);
    }

    IItems<? extends IItem> getItems();
}
