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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EValType;

public final class InvalidPropertyTypeError extends RuntimeException implements IClientError {

    static final long serialVersionUID = -5771659160185724297L;
    final String modelInfo;
    final String definitionInfo;
    final String definitionClassInfo;
    final String propertyInfo;
    final EValType expectedType, actualType;

    public InvalidPropertyTypeError(final Property property, final EValType actualType) {
        super();
        modelInfo = property.getOwner().getTitle();
        definitionInfo = property.getOwner().getDefinition().toString();
        if (property.getOwner() instanceof EntityModel) {
            definitionClassInfo = ((EntityModel) property.getOwner()).getClassPresentationDef().toString();
        } else {
            definitionClassInfo = null;
        }
        propertyInfo = property.getDefinition().toString();
        this.actualType = actualType;
        expectedType = property.getDefinition().getType();
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Property Has Invalid Type");
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String msg;
        msg = mp.translate("ExplorerError", "Property %s in model \'%s\' has invalid type.");
        return String.format(msg, propertyInfo, modelInfo);
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getLocalizedMessage() + "\n"
                + mp.translate("ExplorerError", "Entity:") + " \"" + modelInfo + "\"\n"
                + mp.translate("ExplorerError", "Editor presentation:") + " \"" + definitionInfo + "\"\n"
                + mp.translate("ExplorerError", "Class:") + " \"" + definitionClassInfo + "\"";
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }
}
