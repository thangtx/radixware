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

package org.radixware.kernel.common.utils;

import java.util.EnumSet;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;


public class IdPrefixes {

    private IdPrefixes() {
    }

    private static final EnumSet<EDefinitionIdPrefix> ads_prop_prefixes = EnumSet.of(EDefinitionIdPrefix.DDS_COLUMN,
            EDefinitionIdPrefix.ADS_DYNAMIC_PROP,
            EDefinitionIdPrefix.ADS_FORM_PROP,
            EDefinitionIdPrefix.ADS_PRESENTATION_PROP,
            EDefinitionIdPrefix.ADS_USER_PROP);

    private static final EnumSet<EDefinitionIdPrefix> ads_nested_class_prefixes = EnumSet.of(
        EDefinitionIdPrefix.ADS_DYNAMIC_CLASS,
        EDefinitionIdPrefix.ADS_INTERFACE_CLASS,
        EDefinitionIdPrefix.ADS_ENUM_CLASS,
        EDefinitionIdPrefix.ADS_EXCEPTION_CLASS);

    private static boolean isPrefixIn(Id id, EnumSet<EDefinitionIdPrefix> es) {
        try {
            EDefinitionIdPrefix prefix = id.getPrefix();
            return es.contains(prefix);
        } catch (NoConstItemWithSuchValueError e) {
            return false;
        }
    }

    private static boolean isPrefixIs(Id id, EDefinitionIdPrefix prefix) {
        try {
            return id.getPrefix() == prefix;
        } catch (NoConstItemWithSuchValueError e) {
            return false;
        }
    }

    private static boolean isPrefixIn(Id id, EDefinitionIdPrefix... prefixes) {
        for (EDefinitionIdPrefix pref : prefixes) {
            if (id.getPrefix() == pref) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isAdsPropertyId(Id id) {
        return isPrefixIn(id, ads_prop_prefixes);
    }

    public static final boolean isAdsMethodId(Id id) {
        return isPrefixIs(id, EDefinitionIdPrefix.ADS_CLASS_METHOD);
    }
    public static final boolean isAdsCommandId(Id id) {
        return isPrefixIs(id, EDefinitionIdPrefix.COMMAND);
    }
    public static final boolean isAdsEnumClassFieldId(Id id) {
        return isPrefixIs(id, EDefinitionIdPrefix.ADS_ENUM_CLASS_FIELD);
    }
    public static final boolean isAdsEnumClassParameterId(Id id) {
        return isPrefixIs(id, EDefinitionIdPrefix.ADS_ENUM_CLASS_PARAM);
    }

    public static final boolean isAdsNestedClassId(Id id) {
        return isPrefixIn(id, ads_nested_class_prefixes);
    }

    public static boolean isCommandId(Id id) {
        return isPrefixIn(id, EDefinitionIdPrefix.COMMAND, EDefinitionIdPrefix.CONTEXTLESS_COMMAND);
    }
}
