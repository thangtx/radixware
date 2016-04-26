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
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - имя колонки в базе данных.
 */
public class PropSqlNameTag extends PropAbstractTag implements ISqlTag {

    /**warning suppression support*/
    public static class Problems extends RadixProblem.WarningSuppressionSupport {

        public static final int PROPERTY_HAS_USER_DEFINED_GETTER = 100000;
        public static final int PROPERTY_VALUE_IS_INHERITABLE = 100001;

        public Problems(RadixObject owner, List<Integer> warnings) {
            super(owner);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = warnings.get(i);
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            switch (code) {
                case PROPERTY_HAS_USER_DEFINED_GETTER:
                case PROPERTY_VALUE_IS_INHERITABLE:
                    return true;
                default:
                    return false;
            }
        }
    }
    private Problems warningsSupport = null;

    @Override
    public WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    /**
     * Префикс колонки.
     */
    public static enum EOwnerType {

        /**
         * Подставлять альяс дочерней таблицы в качестве префикса колонки.
         * Актуально только в контексте, где существует дочерняя таблица.
         */
        CHILD,
        /**
         * Подставлять альяс родительской таблицы в качестве префикса колонки.
         * Актуально только в контексте, где существует родительская таблица.
         */
        PARENT,
        /**
         * Подставлять альяс текущей таблицы в качестве префикса колонки.
         * Актуально только в контекстах, где существует текущая таблица.
         */
        THIS,
        /**
         * Подставлять альяс таблицы, которой принадлежит колонка.
         */
        TABLE,
        /**
         * Не подставлять ничего в качестве префикса колонки.
         */
        NONE
    }

    protected PropSqlNameTag() {
        super();
    }

    protected PropSqlNameTag(List<Integer> suppressedWarnings) {
        super();
        if (suppressedWarnings != null) {
            this.warningsSupport = new Problems(this, suppressedWarnings);
        }
    }
    private EOwnerType ownerType = EOwnerType.TABLE;

    /**
     * Получить префикс колонки.
     */
    public EOwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(EOwnerType ownerType) {
        if (!Utils.equals(this.ownerType, ownerType)) {
            this.ownerType = ownerType;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String tableAlias = "";

    /**
     * Получить альяс таблицы колонки.
     */
    public String getTableAlias() {
        return tableAlias;
    }

    public boolean isTableAliasDefined() {
        return tableAlias != null && !tableAlias.isEmpty();
    }

    public void setTableAlias(String tableAlias) {
        if (!Utils.equals(this.tableAlias, tableAlias)) {
            this.tableAlias = tableAlias;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static PropSqlNameTag newInstance() {
            return new PropSqlNameTag();
        }

        public static PropSqlNameTag newInstance(List<Integer> warnings) {
            return new PropSqlNameTag(warnings);
        }
    }

    @Override
    public ISqmlProperty findProperty() {
        final Id propertyId = getPropId();
        final ISqmlEnvironment environment = getEnvironment();
        if (environment == null) {
            return null;
        }

        switch (getOwnerType()) {
            case CHILD:
                return environment.findChildPropertyById(propertyId);
            case PARENT:
                return environment.findParentPropertyById(propertyId);
            case THIS:
                return environment.findThisPropertyById(propertyId);
            default:
                return super.findProperty();
        }
    }
    public static final String PROP_SQL_NAME_TAG_TYPE_TITLE = "Property SQL Name Tag";
    public static final String PROP_SQL_NAME_TAG_TYPES_TITLE = "Property SQL Name Tags";

    @Override
    public String getTypeTitle() {
        return PROP_SQL_NAME_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return PROP_SQL_NAME_TAG_TYPES_TITLE;
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
