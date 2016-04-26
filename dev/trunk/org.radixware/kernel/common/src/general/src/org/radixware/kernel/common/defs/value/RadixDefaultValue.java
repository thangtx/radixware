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

package org.radixware.kernel.common.defs.value;

import org.radixware.kernel.common.enums.ERadixDefaultValueChoice;
import org.radixware.kernel.common.exceptions.RadixError;

/**
 * Default value for Radix columns and properties.
 * Pattern: Immutable.
 */
public class RadixDefaultValue {

    /**
     * Use Factory
     */
    protected RadixDefaultValue(ERadixDefaultValueChoice choice) {
        super();
        this.choice = choice;
        this.valAsStr = null;
        this.expression = null;
    }

    protected RadixDefaultValue(ValAsStr valAsStr) {
        super();
        this.choice = ERadixDefaultValueChoice.VAL_AS_STR;
        this.valAsStr = valAsStr;
        this.expression = null;
    }

    protected RadixDefaultValue(String expression) {
        super();
        this.choice = ERadixDefaultValueChoice.EXPRESSION;
        this.valAsStr = null;
        this.expression = expression;
    }
    private final ValAsStr valAsStr;
    private final ERadixDefaultValueChoice choice;
    private final String expression;

    public ERadixDefaultValueChoice getChoice() {
        return choice;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RadixDefaultValue)) {
            return false;
        }

        final RadixDefaultValue other = (RadixDefaultValue) obj;
        
        if (this.choice != other.choice) {
            return false;
        }
        
        if (this.valAsStr != other.valAsStr && (this.valAsStr == null || !this.valAsStr.equals(other.valAsStr))) {
            return false;
        }
        
        if ((this.expression == null) ? (other.expression != null) : !this.expression.equals(other.expression)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.valAsStr != null ? this.valAsStr.hashCode() : 0);
        hash = 97 * hash + (this.choice != null ? this.choice.hashCode() : 0);
        hash = 97 * hash + (this.expression != null ? this.expression.hashCode() : 0);
        return hash;
    }
    
    

    @Override
    public String toString() {
        switch (choice) {
            case EXPRESSION:
                return expression;
            case VAL_AS_STR:
                return String.valueOf(valAsStr);
            default:
                return choice.getName();
        }
    }

    public static final class Factory {

        private Factory() {
        }

        /**
         * @return default value as current date time.
         */
        public static RadixDefaultValue newDateTime() {
            return new RadixDefaultValue(ERadixDefaultValueChoice.DATE_TIME);
        }

        /**
         * @return default value as current exact date time.
         */
        public static RadixDefaultValue newExactDateTime() {
            return new RadixDefaultValue(ERadixDefaultValueChoice.EXACT_DATE_TIME);
        }

        /**
         * Create new default value as ValAsStr
         * @return new default value instance or null if valAsStr is null.
         */
        public static RadixDefaultValue newValAsStr(ValAsStr valAsStr) {
            if (valAsStr == null) {
                return null;
            }
            return new RadixDefaultValue(valAsStr);
        }

        /**
         * Create new default value as expression
         * @return new default value instance or null if expression is null.
         */
        public static RadixDefaultValue newExpression(String expression) {
            if (expression == null) {
                return null;
            }
            return new RadixDefaultValue(expression);
        }

        public static RadixDefaultValue loadFrom(org.radixware.schemas.commondef.DefaultValue xDefaultValue) {
            final ERadixDefaultValueChoice choice = xDefaultValue.getType();
            final String value = xDefaultValue.getValue();

            switch (choice) {
                case EXPRESSION:
                    return newExpression(value);
                case VAL_AS_STR:
                    final ValAsStr valAsStr = ValAsStr.Factory.loadFrom(value);
                    return newValAsStr(valAsStr);
                default:
                    return new RadixDefaultValue(choice);
            }
        }
    }

    public void appendTo(org.radixware.schemas.commondef.DefaultValue xDefaultValue) {
        xDefaultValue.setType(choice);

        switch (choice) {
            case EXPRESSION:
                xDefaultValue.setValue(expression);
                break;
            case VAL_AS_STR:
                xDefaultValue.setValue(valAsStr.toString());
                break;
        }
    }

    /**
     * Get default value as ValAsStr.
     * @return default value as ValAsStr.
     * @throws RadixError if choice is not VAL_AS_STR.
     */
    public ValAsStr getValAsStr() {
        if (choice == ERadixDefaultValueChoice.VAL_AS_STR) {
            return valAsStr;
        } else {
            throw new RadixError("Attemp to get default value as ValAsStr for " + String.valueOf(choice));
        }
    }

    /**
     * Get default value as expression.
     * @return default value as expression.
     * @throws RadixError if choice is not EXPRESSION.
     */
    public String getExpression() {
        if (choice == ERadixDefaultValueChoice.EXPRESSION) {
            return expression;
        } else {
            throw new RadixError("Attemp to get default value as expression for " + String.valueOf(choice));
        }
    }
}