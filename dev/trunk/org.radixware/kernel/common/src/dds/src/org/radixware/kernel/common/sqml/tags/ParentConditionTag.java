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

import org.radixware.kernel.common.utils.Utils;

/**
 * Транслируется:
 * * ref=obj -> Колонки внешнего ключа равны
 * * ref!=obj ->Хотя бы одна колонка внешнего ключа не равна
 * * ref is null -> Хотя бы одна колонка внешнего ключа null
 * * ref is not null -> Все колонки внешнего ключа не null
 *
 * Тэг жестко задается на этапе создания фильтра (валюта=рубль).
 * Тэг доступен только в проводнике.
 */
public class ParentConditionTag extends PropAbstractTag {

    public static enum Operator {

        EQUAL,
        NOT_EQUAL,
        IS_NULL,
        IS_NOT_NULL
    }

    protected ParentConditionTag() {
        super();
    }
    private Operator operator = Operator.EQUAL;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        if (!Utils.equals(this.operator, operator)) {
            this.operator = operator;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String parentPid = null;

    public String getParentPid() {
        return parentPid;
    }

    public void setParentPid(String parentPid) {
        if (!Utils.equals(this.parentPid, parentPid)) {
            this.parentPid = parentPid;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String parentTitle = "";

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        if (!Utils.equals(this.parentTitle, parentTitle)) {
            this.parentTitle = parentTitle;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static ParentConditionTag newInstance() {
            return new ParentConditionTag();
        }
    }

    public static final String PARENT_CONDITION_TAG_TYPE_TITLE = "Parent Condition Tag";
    public static final String PARENT_CONDITION_TAG_TYPES_TITLE = "Parent Condition Tags";

    @Override
    public String getTypeTitle() {
        return PARENT_CONDITION_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return PARENT_CONDITION_TAG_TYPES_TITLE;
    }
}
