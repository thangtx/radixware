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

package org.radixware.kernel.explorer.editors.filterparameditor;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.Application;


enum EFilterParamAttribute {

    NAME,
    PROPERTY,
    DEFAULT_VALUE,
    PERSISTENT_VALUE_DEFINED,
    PERSISTENT_VALUE,
    VALUE_TYPE,
    ENUM,
    IS_MANDATORY,
    EDIT_MASK,
    NULL_TITLE,
    SELECTOR_PRESENTATION,
    ADDITIONAL_SELECTOR_CONDITION,
    PARENT_REF_EDITING_MODE;

    public String getTitle(final IClientEnvironment environment) {
        switch (this) {
            case NAME:
                return environment.getMessageProvider().translate("SqmlEditor", "&Name:");
            case PROPERTY:
                return environment.getMessageProvider().translate("SqmlEditor", "Base &property:");
            case DEFAULT_VALUE:
                return environment.getMessageProvider().translate("SqmlEditor", "Default &value:");
            case VALUE_TYPE:
                return environment.getMessageProvider().translate("SqmlEditor", "Value &type:");
            case ENUM:
                return environment.getMessageProvider().translate("SqmlEditor", "&Enumeration:");
            case IS_MANDATORY:
                return environment.getMessageProvider().translate("SqmlEditor", "Value m&ust be defined:");
            case EDIT_MASK:
                return environment.getMessageProvider().translate("SqmlEditor", "Edit &mask:");
            case NULL_TITLE:
                return environment.getMessageProvider().translate("SqmlEditor", "&Display instead of '<Not defined>':");
            case SELECTOR_PRESENTATION:
                return environment.getMessageProvider().translate("SqmlEditor", "&Presentation:");
            case PERSISTENT_VALUE_DEFINED:
                return environment.getMessageProvider().translate("SqmlEditor", "Constant value de&fined:");
            case PERSISTENT_VALUE:
                return environment.getMessageProvider().translate("SqmlEditor", "&Constant value:");
            case PARENT_REF_EDITING_MODE:
                return environment.getMessageProvider().translate("SqmlEditor", "Editing &mode:");
            case ADDITIONAL_SELECTOR_CONDITION:
                return environment.getMessageProvider().translate("SqmlEditor", "&Additional condition:");
        }
        return "";
    }
}
