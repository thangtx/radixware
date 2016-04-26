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
 * Тэг - завершение тэга {@link IfParamTag}.
 * Транслируется в пустую строку.
 */
public class EndIfTag extends Sqml.Tag {

    protected EndIfTag() {
        super();
    }

    public static final class Factory {

        private Factory() {
        }

        public static EndIfTag newInstance() {
            return new EndIfTag();
        }
    }
    public static final String END_IF_TAG_TYPE_TITLE = "End If Tag";
    public static final String END_IF_TAG_TYPES_TITLE = "End If Tags";

    @Override
    public String getTypeTitle() {
        return END_IF_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return END_IF_TAG_TYPES_TITLE;
    }
}
