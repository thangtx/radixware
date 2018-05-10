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
package org.radixware.kernel.radixdoc.ditagenerator;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.radixdoc.enums.ELocalizationMapKeys;

public class LocalizationMap {

    private static final Map<ELocalizationMapKeys, Id> DOC_LOCALIZATION_MAP = new HashMap<>();

    // General
    static {
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.OVERVIEW, Id.Factory.loadFrom("mls3TOR4EDZKNFELB374Y7PTIK4LM"));
    }

    // Rules and Conventions
    static {
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.RNC_TITLE, Id.Factory.loadFrom("mls5ZCPXOME3BGGLJCTDT7J56PTEQ"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.NS_PREFIXES_TITLE, Id.Factory.loadFrom("mlsUBXV57BYI5C47NISBGJA2CY5FA"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.NAMESPACE, Id.Factory.loadFrom("mlsDIC4XJIWDFCB7N5QPBZHRXQROM"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.PREFIX, Id.Factory.loadFrom("mlsGDTEHWKSGFEPVEXTSNWLBYWXHQ"));
    }

    // XSD
    static {
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.XSD_TITLE, Id.Factory.loadFrom("mlsXPFO24MVZVDN7H5A2LMJWZAAWM"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.SCHEMA_TITLE, Id.Factory.loadFrom("mlsYFS4BVWVJBG3HMRR7NX7EBTQAY"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.TARGET_NAMESPACE, Id.Factory.loadFrom("mlsJANU3PDFIFFM7P5NBBDWWL72BE"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.CHANGE_LOG, Id.Factory.loadFrom("mls2TTTIHU4ZNFYZHWDRNQORXP3NM"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.VERSION, Id.Factory.loadFrom("mls7ALRBTAOA5EXXJEJCWMIVSPFAA"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.DATE, Id.Factory.loadFrom("mls3DJJV5B43ZBDJPTSEPXPZPLHN4"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.DESCRIPTION, Id.Factory.loadFrom("mlsPEC5W3VHK5BNHJBQQBCLVJYULM"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.TYPE, Id.Factory.loadFrom("mlsHJZ3XZYBSZF4VNXWFQKE2ARXB4"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.COMPLEX_TYPE, Id.Factory.loadFrom("mlsWHYL5AH5IBADDAOIY5G7G4ID3A"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.SCHEMA, Id.Factory.loadFrom("mlsMW22UBTCIJA6HOXRFJVMYEZ4KE"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.OWNER, Id.Factory.loadFrom("mlsLWEVXZ73OFFDPL3MBYMMCMKQMQ"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.CONTAINER_TYPE, Id.Factory.loadFrom("mlsDPVVNDQLGVH2ZP3XG6QF3BB7AA"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.OCCURRENCES, Id.Factory.loadFrom("mlsBA47YBKS6BH3VARVHY6MVYAFUY"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.MANDATORY, Id.Factory.loadFrom("mlsFQCM3PKDWZDGDPZNKVI33OA2MU"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.SINCE_VERSION, Id.Factory.loadFrom("mlsMA7EXH5INRETVJCC4GN2FBWQHY"));
    }

    // Enum    
    static {
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.ENUM_TITLE, Id.Factory.loadFrom("mls7HT6GZEMM5FUFEQCPIMNCE6NUQ"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.ENUMERATION, Id.Factory.loadFrom("mlsOHQHDKNBBJBJRLFYKQFV657W64"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.VALUE_TYPE, Id.Factory.loadFrom("mlsP675O6Q62ZG5NCRAKYNBHNYVEQ"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.ITEMS, Id.Factory.loadFrom("mlsPQHAXCWRZREJXMA4UC24SFFFGA"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.VALUE, Id.Factory.loadFrom("mlsNQHRF7L7QJHFFN47HWFFQTV6RY"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.NAME, Id.Factory.loadFrom("mlsDHF3VAASXVD6JI2JC7YHMCNZJA"));
    }

    // XPath parts
    static {
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.PATH_PART_ELEM, Id.Factory.loadFrom("mls7JRCXMHX2RAHNBB7RRNTQTN2UI"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.PATH_PART_TYPE, Id.Factory.loadFrom("mlsNPFYLJN3TJBDRBLPN23EJ4DXUQ"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.PATH_PART_ATTR_GROUP, Id.Factory.loadFrom("mls5HFZ2ZNNNVC3JPHG245GGWI3WY"));
        DOC_LOCALIZATION_MAP.put(ELocalizationMapKeys.PATH_PART_ATTR, Id.Factory.loadFrom("mlsBKVB5GWBNVFVXONUD7RGDBHKOY"));
    }

    public static Id get(ELocalizationMapKeys key) {
        return DOC_LOCALIZATION_MAP.get(key);
    }
}
