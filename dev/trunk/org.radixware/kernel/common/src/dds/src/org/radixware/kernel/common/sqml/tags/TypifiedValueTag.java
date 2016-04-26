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

import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.utils.Utils;

/**
 * Типизирование с точностью до типа конкретной колонки значение.
 * Позволяет упростить ввод значения, зная тип, значение по умолчанию,
 * маску редактирование и т.п. атрибуты колонки.
 */
public class TypifiedValueTag extends PropAbstractTag {

    private ValAsStr value = null;
    private String displayValue = "";
    private boolean literal = false;

    protected TypifiedValueTag() {
        super();
    }

    /**
     * Получить подставляемое в SQL значение
     * кроме ValType = ParentRef и OBJECT
     */
    public ValAsStr getValue() {
        return value;
    }

    /**
     * Установить подставляемое в SQL значение
     * кроме ValType = ParentRef и OBJECT
     */
    public void setValue(ValAsStr value) {
        if (!Utils.equals(this.value, value)) {
            this.value = value;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Получить отображаемое в редакторе значение
     */
    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        if (!Utils.equals(this.displayValue, displayValue)) {
            this.displayValue = displayValue;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isLiteral() {
        return literal;
    }

    public void setLiteral(boolean literal) {
        if (this.literal != literal) {
            this.literal = literal;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static TypifiedValueTag newInstance() {
            return new TypifiedValueTag();
        }
    }
    public static final String TYPEFIED_VALUE_TAG_TYPE_TITLE = "Typefied Value Tag";
    public static final String TYPEFIED_VALUE_TAG_TYPES_TITLE = "Typefied Value Tags";

    @Override
    public String getTypeTitle() {
        return TYPEFIED_VALUE_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return TYPEFIED_VALUE_TAG_TYPES_TITLE;
    }
}
