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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * Тэг - имя текущей таблицы в базе данных.
 * Актуален только в контекстах, где существует понятие "текущая таблица".
 */
public class ThisTableSqlNameTag extends Sqml.Tag {

    protected ThisTableSqlNameTag() {
        super();
    }

    public static final class Factory {

        private Factory() {
        }

        public static ThisTableSqlNameTag newInstance() {
            return new ThisTableSqlNameTag();
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
    public static final String THIS_TABLE_SQL_NAME_TAG_TYPE_TITLE = "This Table SQL Name Tag";
    public static final String THIS_TABLE_SQL_NAME_TAG_TYPES_TITLE = "This Table SQL Name Tags";

    @Override
    public String getTypeTitle() {
        return THIS_TABLE_SQL_NAME_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return THIS_TABLE_SQL_NAME_TAG_TYPES_TITLE;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final DdsTableDef table = findTable();
        if (table != null) {
            list.add(table);
        }
    }
}
