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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.client.IClientEnvironment;


final class RegExpValidator implements org.radixware.kernel.common.client.meta.mask.validators.IInputValidator {

    private final Pattern pattern;

    public RegExpValidator(final String regExpPattern, final boolean caseSensitive) {        
        if (!caseSensitive) {
            pattern = Pattern.compile(regExpPattern, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regExpPattern);
        }
    }

    @Override
    public String fixup(final String input) {
        return input;
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final String input, final int position) {
        if (input == null) {
            return ValidationResult.INTERMEDIATE;
        }

        final Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            return ValidationResult.ACCEPTABLE;
        } else {
            if (matcher.hitEnd()) {
                return ValidationResult.INTERMEDIATE;
            } else {
                return ValidationResult.INVALID;
            }

        }
    }

    public static RegExpValidator loadFromXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr){
        return new RegExpValidator(editMaskStr.getMask() , editMaskStr.getCaseSensitive());
    }

    public void appendToXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr){
        editMaskStr.setMask(pattern.pattern());
        if ((pattern.flags() & Pattern.CASE_INSENSITIVE)!=0){
            editMaskStr.setCaseSensitive(false);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.pattern.pattern().hashCode();
        hash = 71 * hash + this.pattern.flags();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this){
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegExpValidator other = (RegExpValidator) obj;
        if (!Objects.equals(this.pattern.pattern(), other.pattern.pattern())) {
            return false;
        }
        if (!Objects.equals(this.pattern.flags(), other.pattern.flags())) {
            return false;
        }        
        return true;
    }

}
