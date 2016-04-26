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
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - JOIN между двумя таблицами.
 * Транслируется в JOIN выражение.
 */
public class JoinTag extends Sqml.Tag {

    /**
     * Способ объединения таблиц в тэге {@link JoinTag}.
     */
    public static enum Type {

        /**
         * Для каждой записи из левой таблицы отобразить запись из правой таблицы или null,
         * если запись в правой таблице не найдена.
         */
        LEFT,
        /**
         * Для каждой записи из правой таблицы отобразить запись из левой таблицы или null,
         * если запись в левой таблице не найдена.
         */
        RIGHT,
        /**
         * Для каждой записи из левой и правой таблицы отобразить запись из соответствущей таблицы,
         * но только в том случае, если запись в соответствующей таблице найдена.
         */
        INNER,
        /**
         * Для каждой записи из левой и правой таблицы отобразить запись из соответствущей таблицы или null,
         * если запись из соответствущей таблицы не найдена.
         */
        FULL
    }

    protected JoinTag() {
        super();
    }
    private Type type = Type.INNER;

    /** 
     * Получить способ объединения таблиц.
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (!Utils.equals(this.type, type)) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id referenceId = null;

    /**
     * Получить идентификатор связи, по которой строится JOIN выражение.
     * В качестве "левой" таблицы используется дочерняя таблица связи,
     * в качестве "правой" таблицы используется родительская таблица связи.
     */
    public Id getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Id referenceId) {
        if (!Utils.equals(this.referenceId, referenceId)) {
            this.referenceId = referenceId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String childTableAlias = "";

    /**
     * Получить альяс дочерней таблицы, который подставится вместо имени таблицы при трансляции тэга.
     */
    public String getChildTableAlias() {
        return childTableAlias;
    }

    public boolean isChildTableAliasDefined() {
        return childTableAlias != null && !childTableAlias.isEmpty();
    }

    public void setChildTableAlias(String childTableAlias) {
        if (!Utils.equals(this.childTableAlias, childTableAlias)) {
            this.childTableAlias = childTableAlias;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String parentTableAlias = "";

    /**
     * Получить алиас родительской таблицы, который подставится вместо имени таблицы при трансляции тэга.
     */
    public String getParentTableAlias() {
        return parentTableAlias;
    }

    public boolean isParentTableAliasDefined() {
        return parentTableAlias != null && !parentTableAlias.isEmpty();
    }

    public void setParentTableAlias(String parentTableAlias) {
        if (!Utils.equals(this.parentTableAlias, parentTableAlias)) {
            this.parentTableAlias = parentTableAlias;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static JoinTag newInstance() {
            return new JoinTag();
        }
    }

    /**
     * Find reference.
     * @return reference or null if not found.
     */
    public DdsReferenceDef findReference() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findReferenceById(referenceId);
        } else {
            return null;
        }
    }

    /**
     * Find reference.
     * @throw DefinitionNotFoundError if not found.
     */
    public DdsReferenceDef getReference() {
        DdsReferenceDef reference = findReference();
        if (reference == null) {
            throw new DefinitionNotFoundError(referenceId);
        }
        return reference;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsReferenceDef reference = findReference();
        if (reference != null) {
            list.add(reference);
        }
    }
    public static final String JOIN_TAG_TYPE_TITLE = "Join Tag";
    public static final String JOIN_TAG_TYPES_TITLE = "Join Tags";

    @Override
    public String getTypeTitle() {
        return JOIN_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return JOIN_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Reference Id: " + String.valueOf(getReferenceId()));
    }
}
