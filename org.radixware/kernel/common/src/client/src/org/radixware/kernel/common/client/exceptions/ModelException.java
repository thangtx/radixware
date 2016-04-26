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

import java.util.StringTokenizer;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ExceptionEnum;

public abstract class ModelException extends Exception implements IClientError {

    private static final long serialVersionUID = -9203950243395341081L;
    protected String definitionInfo;
    protected final String modelInfo;
    protected final IClientEnvironment environment;

    protected ModelException(final Model model) {
        super();
        environment = model.getEnvironment();
        modelInfo = model.getTitle();
        definitionInfo = model.getDefinition().toString();
    }

    public static ModelException create(final Model sourceModel, final ServiceCallFault fault) {

        if (fault.getFaultString().equals(ExceptionEnum.PROPERTY_IS_MANDATORY.toString())) {
            final String message = fault.getMessage();
            final StringTokenizer lines = new StringTokenizer(message, "\n");
            final Id classId = Id.Factory.loadFrom(lines.nextToken()),
                    propertyId = Id.Factory.loadFrom(lines.nextToken());
            if ((sourceModel instanceof GroupModel) && classId.getPrefix() == EDefinitionIdPrefix.FILTER) {
                final GroupModel group = (GroupModel) sourceModel;
                final FilterModel filter = group.getFilters().findById(classId);
                if (filter != null) {
                    return new PropertyIsMandatoryException(sourceModel, filter.getDefinition(), propertyId);
                }
            }
            return new PropertyIsMandatoryException(sourceModel, classId, propertyId);
        } else if (fault.getFaultString().equals(ExceptionEnum.INVALID_PROPERTY_VALUE.toString())) {
            final String message = fault.getMessage();
            final StringTokenizer lines = new StringTokenizer(message, "\n");
            final Id classId = Id.Factory.loadFrom(lines.nextToken()),
                    propertyId = Id.Factory.loadFrom(lines.nextToken());
            final StringBuilder reason = new StringBuilder();
            for (int i = 0; lines.hasMoreTokens(); i++) {
                if (i > 0) {
                    reason.append("\n");
                }
                reason.append(lines.nextToken());
            }

            return new InvalidPropertyValueException(sourceModel, classId, propertyId, null, reason.length() > 0 ? reason.toString() : null);
        }
        return null;
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getMessage(mp);
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return getDetailMessage(messageProvider);
    }

    @Override
    public String getLocalizedMessage() {
        return getDetailMessage(DefaultMessageProvider.getInstance());
    }

    public abstract String getMessage(MessageProvider mp);

    @Override
    public String getMessage() {
        return getMessage(DefaultMessageProvider.getInstance());
    }
}
