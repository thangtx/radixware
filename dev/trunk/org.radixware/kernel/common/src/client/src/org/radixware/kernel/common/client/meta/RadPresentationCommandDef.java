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

package org.radixware.kernel.common.client.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.types.Id;

public class RadPresentationCommandDef extends RadCommandDef {

//Public fields 
    public final ECommandScope scope;
    private final List<Id> propIds;  // if not null props restricted
    private final boolean rereadAfterExecute;
    private final ECommandAccessibility accessibility;

//Constructor
    public RadPresentationCommandDef(
            final Id id,
            final String name,
            final Id ownerId,
            final Id titleId,
            final Id iconId,
            final Id startFormId,
            final ECommandNature nature,
            final boolean visible,
            final boolean confirmation,
            final ECommandScope scope,
            final Id[] props,
            final ECommandAccessibility accessibility) {
        this(id,
             name,
             ownerId,
             titleId,
             iconId,
             startFormId,
             nature,
             visible,
             confirmation,
             false,
             scope,
             props,
             accessibility,
             accessibility==ECommandAccessibility.ONLY_FOR_FIXED);
    }
    
    public RadPresentationCommandDef(
            final Id id,
            final String name,
            final Id ownerId,
            final Id titleId,
            final Id iconId,
            final Id startFormId,
            final ECommandNature nature,
            final boolean visible,
            final boolean confirmation,
            final boolean isReadOnly,
            final ECommandScope scope,
            final Id[] props,
            final ECommandAccessibility accessibility) {
        this(id, 
             name, 
             ownerId, 
             titleId, 
             iconId, 
             startFormId, 
             nature, 
             visible, 
             confirmation, 
             isReadOnly, 
             scope,
             props,
             accessibility,
             accessibility==ECommandAccessibility.ONLY_FOR_FIXED);
    }
    
    public RadPresentationCommandDef(
            final Id id,
            final String name,
            final Id ownerId,
            final Id titleId,
            final Id iconId,
            final Id startFormId,
            final ECommandNature nature,
            final boolean visible,
            final boolean confirmation,
            final boolean isReadOnly,
            final ECommandScope scope,
            final Id[] props,
            final ECommandAccessibility accessibility,
            final boolean rereadAfterExecute) {
        super(id, name, ownerId, titleId, iconId, startFormId, nature, visible, confirmation, isReadOnly);
        this.scope = scope;
        this.accessibility = accessibility;
        if (props != null && props.length > 0) {
            propIds = new ArrayList<Id>(props.length);
            Collections.addAll(propIds, props);
        } else {
            propIds = null;
        }
        this.rereadAfterExecute = rereadAfterExecute;
    }    

//Public methods 
    public final boolean isApplicableForProperty(final Id propId) {
        return propIds != null && propIds.contains(propId);
    }

    @Override
    public ECommandAccessibility getAccessibility() {
        return accessibility;
    }
    
    public boolean needToRereadOwnerAfterExecuteCommand(){
        return rereadAfterExecute;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "entity command %s");
        return String.format(desc, super.getDescription());
    }
}
