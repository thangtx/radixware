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
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchNameError;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - значение константы. Транслируется в значение константы в формате SQL.
 *
 */
public class ConstValueTag extends Sqml.Tag implements ISqlTag {

    protected ConstValueTag() {
        super();
    }
    private Id enumId = null;

    /**
     * Получить идентификатор набора констант, в котором хранится константа.
     */
    public Id getEnumId() {
        return enumId;
    }

    public void setEnumId(Id enumId) {
        if (!Utils.equals(this.enumId, enumId)) {
            this.enumId = enumId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id itemId = null;

    /**
     * Получить идентификатор элемента набора констант, в котором хранится
     * константа.
     */
    public Id getItemId() {
        return itemId;
    }

    public void setItemId(Id itemId) {
        if (!Utils.equals(this.itemId, itemId)) {
            this.itemId = itemId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static ConstValueTag newInstance() {
            return new ConstValueTag();
        }
    }

    /**
     * Find enum.
     *
     * @return enum or null if not found.
     */
    public IEnumDef findEnum() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findEnumById(enumId);
        } else {
            return null;
        }
    }

    /**
     * Find enum.
     *
     * @throws NoConstItemWithSuchNameError if not found.
     */
    public IEnumDef getEnum() {
        final IEnumDef enm = findEnum();
        if (enm == null) {
            throw new DefinitionNotFoundError(enumId);
        }
        return enm;
    }

    /**
     * Find enum item.
     *
     * @return enum item or null if not found.
     */
    public IEnumDef.IItem findEnumItem() {
        final IEnumDef enumDef = findEnum();
        if (enumDef != null) {
            return enumDef.getItems().findItemById(itemId, EScope.ALL);
        } else {
            return null;
        }
    }

    /**
     * Find enum item.
     *
     * @throws NoConstItemWithSuchNameError if not found.
     */
    public IEnumDef.IItem getEnumItem() {
        final IEnumDef enumDef = getEnum();
        final IEnumDef.IItem item = enumDef.getItems().findItemById(itemId, EScope.ALL);
        if (item != null) {
            return item;
        } else {
            throw new DefinitionNotFoundError(getItemId());
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        IEnumDef.IItem item = findEnumItem();
        if (item instanceof Definition) {
            list.add((Definition) item);
        }
    }
    public static final String CONST_VALUE_TAG_TYPE_TITLE = "Enumeration Item Value Tag";
    public static final String CONST_VALUE_TAG_TYPES_TITLE = "Enumeration Item Value Tags";

    @Override
    public String getTypeTitle() {
        return CONST_VALUE_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return CONST_VALUE_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Enumeration Id: " + String.valueOf(getEnumId()));
        sb.append("<br>Item Id: " + String.valueOf(getItemId()));
    }
    private String sql;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public void setSql(final String sql) {
        if (!Utils.equals(this.sql, sql)) {
            this.sql = sql;
            setEditState(EEditState.MODIFIED);
        }
    }
}
