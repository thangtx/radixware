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

package org.radixware.kernel.common.builder.check.ads;

import java.text.MessageFormat;
import org.radixware.kernel.common.enums.EValType;


public class MessageFormatValidator {

    public static final boolean formatIsValid(EValType type, String pattern) {
        if (pattern == null) {
            return true;
        }
        try {
            switch (type) {
                case INT:
                    MessageFormat.format(pattern, 123423);
                    break;
                case NUM:
                    MessageFormat.format(pattern, 123423.33354);
                    break;
                case STR:
                case CLOB:
                case OBJECT:
                case PARENT_REF:
                    MessageFormat.format(pattern, "asdsad");
                    break;
                case CHAR:
                    MessageFormat.format(pattern, 'A');
                    break;
                case DATE_TIME:
                    MessageFormat.format(pattern, System.currentTimeMillis());
                    break;
                default:
                //
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }
}
