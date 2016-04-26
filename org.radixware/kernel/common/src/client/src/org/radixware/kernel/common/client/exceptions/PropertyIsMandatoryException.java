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

package org.radixware.kernel.common.client.exceptions;

import java.util.Collections;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadPropertyDef;

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.types.Id;

public final class PropertyIsMandatoryException extends ModelPropertyException {

    private static final long serialVersionUID = -979904038361114548L;
    private final String propertyTitle;

    public PropertyIsMandatoryException(final Model model, final RadPropertyDef prop) {
        super(model, model.getDefinition().getId(), prop.getId());
        propertyTitle = getPropertyTitle(model, prop);
    }

    public PropertyIsMandatoryException(final Model model, final IModelDefinition owner, final Id propertyId) {
        super(model, owner.getId(), propertyId);
        propertyTitle = getPropertyTitle(model, propertyId);
    }

    public PropertyIsMandatoryException(final Model model, final Id ownerId, final Id propertyId) {
        super(model, ownerId, propertyId);
        propertyTitle = getPropertyTitle(model, propertyId);
    }

    @Override
    public String getTitle(final MessageProvider mp) {
        return mp.translate("ExplorerException", "Value is not Defined");
    }

    @Override
    public String getMessage(final MessageProvider mp) {
        if (propertyTitle==null || propertyTitle.isEmpty()){
            return getMessageForMandatoryProperties(mp, Collections.<String>singletonList(getPropertyTitle()));
        }else{
            return getMessageForMandatoryProperties(mp, Collections.<String>singletonList(propertyTitle));
        }
    }        
}
