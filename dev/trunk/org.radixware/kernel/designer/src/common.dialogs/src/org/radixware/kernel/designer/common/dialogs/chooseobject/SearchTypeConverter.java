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
package org.radixware.kernel.designer.common.dialogs.chooseobject;

import org.netbeans.spi.jumpto.type.SearchType;
import org.radixware.kernel.common.utils.namefilter.ESearchType;

/**
 *
 * @author dlastochkin
 */
public class SearchTypeConverter {

    public static ESearchType convertNb2RdxSearchType(SearchType nbSearchType) {
        switch (nbSearchType) {
            case CAMEL_CASE:
                return ESearchType.CAMEL_CASE;
            case CASE_INSENSITIVE_EXACT_NAME:
                return ESearchType.CASE_INSENSITIVE_EXACT_NAME;
            case CASE_INSENSITIVE_PREFIX:
                return ESearchType.CASE_INSENSITIVE_PREFIX;
            case CASE_INSENSITIVE_REGEXP:
                return ESearchType.CASE_INSENSITIVE_REGEXP;
            case EXACT_NAME:
                return ESearchType.EXACT_NAME;
            case PREFIX:
                return ESearchType.PREFIX;
            case REGEXP:
                return ESearchType.REGEXP;
            default:
                return null;
        }
    }
    
    public static SearchType convertRdx2NbSearchType(ESearchType rdxSearchType) {
        switch (rdxSearchType) {
            case CAMEL_CASE:
                return SearchType.CAMEL_CASE;
            case CASE_INSENSITIVE_EXACT_NAME:
                return SearchType.CASE_INSENSITIVE_EXACT_NAME;
            case CASE_INSENSITIVE_PREFIX:
                return SearchType.CASE_INSENSITIVE_PREFIX;
            case CASE_INSENSITIVE_REGEXP:
                return SearchType.CASE_INSENSITIVE_REGEXP;
            case EXACT_NAME:
                return SearchType.EXACT_NAME;
            case PREFIX:
                return SearchType.PREFIX;
            case REGEXP:
                return SearchType.REGEXP;
            default:
                return null;
        }
    }
}
