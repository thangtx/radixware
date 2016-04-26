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

package org.radixware.kernel.common.sqml.tags;

import org.radixware.kernel.common.sqml.Sqml;

/**
 * Тэг - ELSE IF для тэга {@link IfParamTag}.
 * Транслируется в пустую строку.
 */
public class ElseIfTag extends Sqml.Tag {

    protected ElseIfTag() {
        super();
    }

    public static final class Factory {

        private Factory() {
        }

        public static ElseIfTag newInstance() {
            return new ElseIfTag();
        }
    }
    public static final String ELSE_IF_TAG_TYPE_TITLE = "Else Tag";
    public static final String ELSE_IF_TAG_TYPES_TITLE = "Else Tags";

    @Override
    public String getTypeTitle() {
        return ELSE_IF_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ELSE_IF_TAG_TYPES_TITLE;
    }
}
