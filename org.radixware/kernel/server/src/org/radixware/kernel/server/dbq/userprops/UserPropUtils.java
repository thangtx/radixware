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

package org.radixware.kernel.server.dbq.userprops;

import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;

public class UserPropUtils {

    public static String getGetterNameByType(final EValType type) throws DefinitionError {
        return "RDX_Entity.getUserProp" + getGetterSetterPostfixByType(type);
    }

    public static String getSetterNameByType(final EValType type) throws DefinitionError {
        return "RDX_Entity.setUserProp" + getGetterSetterPostfixByType(type);
    }

    private static String getGetterSetterPostfixByType(final EValType type) throws DefinitionError {
        if (type == null) {
            throw new IllegalUsageError("Type is null");
        }
        if (type.isArrayType()) {
            return "ArrAsStr";
        }
        switch (type) {
            case BOOL:
                return "Bool";
            case INT:
                return "Int";
            case NUM:
                return "Num";
            case STR:
                return "Str";
            case CHAR:
                return "Char";
            case DATE_TIME:
                return "DateTime";
            case PARENT_REF:
            case OBJECT:
                return "Ref";
            case BIN:
                return "Bin";
            case CLOB:
                return "Clob";
            case XML:
                return "Clob";
            case BLOB:
                return "Blob";
            default:
                throw new DefinitionError("Unsupported property type: " + type.getName());
        }
    }
}
