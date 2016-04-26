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

package org.radixware.kernel.common.client.meta.mask.validators;


public class ValidationResult {

    public static final ValidationResult ACCEPTABLE = new ValidationResult(EValidatorState.ACCEPTABLE,null);
    public static final ValidationResult INTERMEDIATE = new ValidationResult(EValidatorState.INTERMEDIATE,null);
    public static final ValidationResult INVALID = new ValidationResult(EValidatorState.INVALID,null);

    public static class Factory{

        private Factory(){
        }

        public static ValidationResult newInvalidResult(final String reason){
            return new ValidationResult(EValidatorState.INVALID, InvalidValueReason.Factory.createForInvalidValue(reason));
        }
        public static ValidationResult newIntermediateResult(final String reason){
            return new ValidationResult(EValidatorState.INTERMEDIATE, InvalidValueReason.Factory.createForInvalidValue(reason));
        }
        public static ValidationResult newInvalidResult(final InvalidValueReason reason){
            return new ValidationResult(EValidatorState.INVALID, reason);
        }
        public static ValidationResult newIntermediateResult(final InvalidValueReason reason){
            return new ValidationResult(EValidatorState.INTERMEDIATE, reason);
        }
    }

    private final EValidatorState state;
    private final InvalidValueReason reason;

    private ValidationResult(final EValidatorState state, final InvalidValueReason reason){
        this.state = state;
        this.reason = reason;
    }

    public EValidatorState getState(){
        return state;
    }

    public InvalidValueReason getInvalidValueReason(){
        return reason==null ? InvalidValueReason.NO_REASON : reason;
    }

    @Override
    public String toString() {
        if (state==EValidatorState.ACCEPTABLE){
            return state.getName();
        }else{
            return state.getName()+", reason: \'"+reason.toString()+"\'";
        }
    }
    
    
}
