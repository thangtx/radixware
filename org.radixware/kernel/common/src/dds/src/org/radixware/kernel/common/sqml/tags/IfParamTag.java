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

import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - препроцессор SQL выражения.
 * Проверяет соответствует-ли значение параметра указанному значению и,
 * в зависимости от результата операции, транслируется либо в пустую строку,
 * либо во всё, что идет после этого тэга до тэга {@link EndIfTag}.
 */
public class IfParamTag extends ParameterAbstractTag {

    protected IfParamTag() {
        super();
    }
    private EIfParamTagOperator operator = null;

    /**
     * Получить оператор, накладываемый на значение параметра.
     */
    public EIfParamTagOperator getOperator() {
        return operator;
    }

    public void setOperator(EIfParamTagOperator operator) {
        if (!Utils.equals(this.operator, operator)) {
            this.operator = operator;
            setEditState(EEditState.MODIFIED);
        }
    }
    private ValAsStr value = null;

    /**
     * Get value to witch parameter equals.
     * @return value or null.
     */
    public ValAsStr getValue() {
        return value;
    }

    public void setValue(ValAsStr value) {
        if (!Utils.equals(this.value, value)) {
            this.value = value;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static IfParamTag newInstance() {
            return new IfParamTag();
        }
    }
    public static final String IF_PARAMETER_TAG_TYPE_TITLE = "Condition By Parameter Value Tag";
    public static final String IF_PARAMETER_TAG_TYPES_TITLE = "Condition By Parameter Value Tags";

    @Override
    public String getTypeTitle() {
        return IF_PARAMETER_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return IF_PARAMETER_TAG_TYPES_TITLE;
    }
}
