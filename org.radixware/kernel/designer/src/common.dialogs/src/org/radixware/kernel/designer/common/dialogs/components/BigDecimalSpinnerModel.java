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

package org.radixware.kernel.designer.common.dialogs.components;


import java.math.BigDecimal;
import javax.swing.AbstractSpinnerModel;

import java.io.Serializable;


public class BigDecimalSpinnerModel extends AbstractSpinnerModel implements Serializable
{
    private BigDecimal stepSize, value;
    private BigDecimal minimum, maximum;


    public BigDecimalSpinnerModel(BigDecimal value, BigDecimal minimum, BigDecimal maximum, BigDecimal stepSize) {
	if ((value == null) || (stepSize == null)) {
	    throw new IllegalArgumentException("value and stepSize must be non-null");
	}
	if (!(((minimum == null) || (minimum.compareTo(value) <= 0)) &&
	      ((maximum == null) || (maximum.compareTo(value) >= 0)))) {
	    throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
	}
	this.value = value;
	this.minimum = minimum;
	this.maximum = maximum;
	this.stepSize = stepSize;
    }


  
    public BigDecimalSpinnerModel(int value, int minimum, int maximum, int stepSize) {
	this(new BigDecimal(value), new BigDecimal(minimum), new BigDecimal(maximum), new BigDecimal(stepSize));
    }


    public BigDecimalSpinnerModel(double value, double minimum, double maximum, double stepSize) {
	this(new BigDecimal(value), new BigDecimal(minimum), new BigDecimal(maximum), new BigDecimal(stepSize));
    }

    public BigDecimalSpinnerModel() {
	this(BigDecimal.ZERO, null, null, BigDecimal.ONE);
    }


    public void setMinimum(BigDecimal minimum) {
	if ((minimum == null) ? (this.minimum != null) : !minimum.equals(this.minimum)) {
	    this.minimum = minimum;
	    fireStateChanged();
	}
    }

    public Comparable getMinimum() {
	return minimum;
    }

    public void setMaximum(BigDecimal maximum) {
	if ((maximum == null) ? (this.maximum != null) : !maximum.equals(this.maximum)) {
	    this.maximum = maximum;
	    fireStateChanged();
	}
    }

    public Comparable getMaximum() {
	return maximum;
    }

    public void setStepSize(BigDecimal stepSize) {
	if (stepSize == null) {
	    throw new IllegalArgumentException("null stepSize");
	}
	if (!stepSize.equals(this.stepSize)) {
	    this.stepSize = stepSize;
	    fireStateChanged();
	}
    }

    public Number getStepSize() {
	return stepSize;
    }


    private BigDecimal incrValue(int dir)
    {
        BigDecimal newValue;
        if (dir == 1){
            newValue = value.add(stepSize);
        }else{
            assert(dir == -1);
            newValue = value.subtract(stepSize);
        }

        if ((maximum != null) && (maximum.compareTo(newValue) < 0)) {
	    return null;
	}
	if ((minimum != null) && (minimum.compareTo(newValue) > 0)) {
	    return null;
	}
	else {
	    return newValue;
	}
    }

    public Object getNextValue() {
	return incrValue(+1);
    }


    public Object getPreviousValue() {
	return incrValue(-1);
    }


    public BigDecimal getNumber() {
	return value;
    }


    public Object getValue() {
	return value;
    }


   
    public void setValue(Object value) {
	if ((value == null) || !(value instanceof BigDecimal)) {
	    throw new IllegalArgumentException("illegal value");
	}
	if (this.value.compareTo((BigDecimal)value) != 0) {
	    this.value = (BigDecimal)value;
	    fireStateChanged();
	}
    }
}

