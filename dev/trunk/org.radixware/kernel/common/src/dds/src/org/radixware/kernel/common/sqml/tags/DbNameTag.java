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
import org.radixware.kernel.common.defs.DefinitionLink;
import org.radixware.kernel.common.defs.dds.DdsPath;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Tag - definition database name.
 * Translated into database name of definition.
 */
public class DbNameTag extends Sqml.Tag implements ISqlTag {

    protected DbNameTag(Id[] ids) {
        super();
        this.ids = ids;
    }
    private final Id[] ids;

    /**
     * Получить путь до дефиниции.
     */
    public Id[] getPath() {
        return ids;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DbNameTag newInstance(final Id[] ids) {
            return new DbNameTag(ids);
        }
    }

    private class TargetLink extends DefinitionLink<Definition> {

        @Override
        protected Definition search() {
            final ISqmlEnvironment environment = getEnvironment();
            if (environment != null) {
                return environment.findDefinitionByIds(ids);
            } else {
                return null;
            }
        }
    }
    private final TargetLink targetLink = new TargetLink();

    /**
     * Find target definition.
     * @return definition or null if not found.
     */
    public Definition findTarget() {
        return targetLink.find();
    }

    /**
     * Find target definition.
     * @return definition.
     * @throws DefinitionNotFoundError
     */
    public Definition getTarget() {
        final Definition definition = findTarget();
        if (definition != null) {
            return definition;
        } else {
            throw new DefinitionNotFoundError(ids.length > 0 ? ids[ids.length - 1] : null);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        Definition target = findTarget();
        if (target != null) {
            list.add(target);
        }
    }
    public static final String DB_NAME_TAG_TYPE_TITLE = "Definition Database Name Tag";
    public static final String DB_NAME_TAG_TYPES_TITLE = "Definition Database Name Tags";

    @Override
    public String getTypeTitle() {
        return DB_NAME_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return DB_NAME_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        final DdsPath ddsPath = new DdsPath(ids);
        sb.append("<br>Path: " + String.valueOf(ddsPath));
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
