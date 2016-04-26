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

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * Тэг - идентификатор текущей таблицы.
 * Актуален только в контекстах, где существует понятие "текущая таблица".
 */
public class ThisTableIdTag extends Sqml.Tag {

    protected ThisTableIdTag() {
        super();
    }

    public static final class Factory {

        private Factory() {
        }

        public static ThisTableIdTag newInstance() {
            return new ThisTableIdTag();
        }
    }

    public DdsTableDef findTable() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findThisTable();
        } else {
            return null;
        }
    }
    public static final String THIS_TABLE_ID_TAG_TYPE_TITLE = "This Table Identifier Tag";
    public static final String THIS_TABLE_ID_TAG_TYPES_TITLE = "This Table Identifier Tags";

    @Override
    public String getTypeTitle() {
        return THIS_TABLE_ID_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return THIS_TABLE_ID_TAG_TYPES_TITLE;
    }
}
