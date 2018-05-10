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

package org.radixware.kernel.common.client.meta.mask;

import org.radixware.kernel.common.meta.InputMask;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;

import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Bin;

public final class EditMaskStr extends org.radixware.kernel.common.client.meta.mask.EditMask {

    private final static int DEFAULT_MAX_LENGTH = 1024 * 100;
    private boolean isPassword = false;
    private InputMask inputMask = InputMask.Factory.newInstance(null);
    private boolean noBlankCharacter = false;
    private Integer maxLength = null;
    private boolean allowEmptyString = true;
    private IInputValidator validator;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.STR, EValType.CHAR, EValType.CLOB,
            EValType.ARR_STR, EValType.ARR_CLOB, EValType.ARR_CHAR);

    public EditMaskStr( //qt-маска ввода. Если задано значение inputMask, то  maximumLen игнорируется
            final String inputMask,
            final boolean isPassword,
            final int maximumLen) {
        super();
        this.inputMask = InputMask.Factory.newInstance(inputMask);
        this.saveSeparators = true;
        this.isPassword = isPassword;
        this.maxLength = maximumLen;        
    }

    public EditMaskStr( //qt-маска ввода. Если задано значение inputMask, то  maximumLen игнорируется
            final String inputMask,
            final boolean saveSeparators,
            final boolean isPassword,
            final int maximumLen) {
        super();
        this.inputMask = InputMask.Factory.newInstance(inputMask);
        this.saveSeparators = saveSeparators;
        this.isPassword = isPassword;
        this.maxLength = maximumLen;        
    }

    public EditMaskStr( //qt-маска ввода. Если задано значение inputMask, то  maximumLen игнорируется
            final String inputMask,
            final boolean saveSeparators,
            final boolean isPassword,
            final int maximumLen,
            final boolean allowEmptyString) {
        super();
        this.inputMask = InputMask.Factory.newInstance(inputMask);
        this.saveSeparators = saveSeparators;
        this.isPassword = isPassword;
        this.maxLength = maximumLen;
        this.allowEmptyString = allowEmptyString;        
    }

    public EditMaskStr( //qt-маска ввода.
            final IInputValidator validator,
            final boolean isPassword,
            final int maximumLen,
            final boolean allowEmptyString) {
        super();
        this.validator = validator;
        this.saveSeparators = true;
        this.isPassword = isPassword;
        this.maxLength = maximumLen;
        this.allowEmptyString = allowEmptyString;
    }

    public EditMaskStr() {
        super();
    }

    public EditMaskStr(final EditMaskStr source) {
        super();
        this.inputMask = source.inputMask;
        this.saveSeparators = source.saveSeparators;
        this.isPassword = source.isPassword;
        this.maxLength = source.maxLength;
        this.validator = source.getValidator();
        this.allowEmptyString = source.allowEmptyString;
        this.noBlankCharacter = source.noBlankCharacter;
    }

    public EditMaskStr(final org.radixware.schemas.editmask.EditMaskStr editMask) {
        super();
        isPassword = editMask.getIsPassword();
        allowEmptyString = editMask.getAllowEmptyString();
        maxLength = editMask.getMaxLength();        
        if (!editMask.isSetValidatorType() || editMask.getValidatorType()==0){
            String inputMaskStr = editMask.getMask();
            if (inputMaskStr != null && !inputMaskStr.isEmpty()) {                
                noBlankCharacter = editMask.getNoBlankCharacter();
                if (noBlankCharacter) {                    
                    inputMaskStr += ';' + String.valueOf((char) 0);//NOPMD
                }                
                inputMask = InputMask.Factory.newInstance(inputMaskStr);
                saveSeparators = editMask.getKeepSeparators();
                maxLength = null;
            }
        }
        else{
            inputMask = InputMask.Factory.newInstance("");
            if (editMask.isSetValidatorType()){
                validator = ValidatorsFactory.loadFromXml(editMask);
            }
        }
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = editMask.addNewStr();

        if (validator==null || !ValidatorsFactory.appendToXml(validator, editMaskStr)){
            if (inputMask != null && !inputMask.isEmpty()) {
                editMaskStr.setValidatorType(0);
                String inputMaskStr = inputMask.getPattern();
                final int pos = inputMaskStr.lastIndexOf(';');
                if (pos>=0 && inputMaskStr.length()>pos+1){
                    final char blankChar = inputMaskStr.charAt(pos+1);
                    if (blankChar==0){
                        inputMaskStr = inputMaskStr.substring(0, pos);
                        editMaskStr.setNoBlankCharacter(true);
                    }
                    else{
                        editMaskStr.setNoBlankCharacter(false);
                    }
                }
                else{
                    editMaskStr.setNoBlankCharacter(false);
                }
                editMaskStr.setMask(inputMaskStr);
                editMaskStr.setKeepSeparators(saveSeparators);
            } else if (maxLength != null && maxLength > 0 && maxLength <= DEFAULT_MAX_LENGTH) {
                editMaskStr.setMaxLength(maxLength);
            }
        }
        else if (maxLength != null && maxLength > 0 && maxLength <= DEFAULT_MAX_LENGTH) {
            editMaskStr.setMaxLength(maxLength);
        }

        editMaskStr.setIsPassword(isPassword);
        if (!allowEmptyString)
            editMaskStr.setAllowEmptyString(false);
    }

    public InputMask getInputMask() {
        return inputMask==null ? InputMask.Factory.newInstance("") : inputMask;
    }    

    public void setInputMask(final String pattern) {
        this.inputMask = InputMask.Factory.newInstance(pattern);
        if (!inputMask.isEmpty()) {
            maxLength = null;
        }        
        afterModify();
    }
    
    public void setInputMask(final InputMask inputMask){
        this.inputMask = inputMask==null ? InputMask.Factory.newInstance("") : inputMask;
        if (!this.inputMask.isEmpty()){
            maxLength = null;
        }
        afterModify();
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(final boolean isPassword) {
        this.isPassword = isPassword;
        afterModify();
    }

    public int getMaxLength() {
        return maxLength == null || maxLength <= 0 ? DEFAULT_MAX_LENGTH : maxLength;
    }
    
    public boolean isMaxLengthDefined(){
        return maxLength!=null && maxLength>0;
    }

    public void setMaxLength(final int length) {
        if (inputMask != null && !inputMask.isEmpty()) {
            throw new IllegalStateException("Cannot set maximum length when input mask defined");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("maximum size of string cannot be equal or less zero");
        }
        if (length > DEFAULT_MAX_LENGTH) {
            throw new IllegalArgumentException("maximum size of string cannot be grater 100K");
        }
        maxLength = length;
    }

    public IInputValidator getValidator() {
        return validator;
    }

    public void setValidator(final IInputValidator validator) {
        this.validator = validator;
        afterModify();
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object object) {
        //if (wasInherited()) return INHERITED_VALUE;
        if (object == null) {
            return getNoValueStr(environment.getMessageProvider());
        }        
        if (object instanceof String) {
            final String str = (String) object;
            if (isPassword) {
                final Character pwdChar = environment.getApplication().getTextOptionsManager().getPasswordCharacter();
                final StringBuffer buf = new StringBuffer("");                
                for (int i = 0; i < str.length(); i++) {
                    buf.append(pwdChar);
                }
                return buf.toString();
            }
            return (String) object;
        }
        if (object instanceof Character) {
            if (isPassword) {
                return String.valueOf(environment.getApplication().getTextOptionsManager().getPasswordCharacter());
            }
            return object.toString();
        }
        if (object instanceof Bin) {
            return bin2Str(((Bin) object).get(), " ");
        } else if (object instanceof Arr) {
            return arrToStr(environment, (Arr) object);
        }
        return "type mismatch";
    }

    private static String bin2Str(final byte[] value, final String devider) {
        final StringBuffer result = new StringBuffer("");
        boolean first = true;
        for (byte x : value) {
            int d = x;
            if (d < 0) {
                d += 256;
            }
            if (first) {
                first = false;
            } else {
                result.append(devider);
            }
            result.append(Character.forDigit(d / 16, 16));
            result.append(Character.forDigit(d % 16, 16));
        }
        return result.toString();
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (isSpecialValue(value))
            return ValidationResult.ACCEPTABLE;
        if (value instanceof Arr)
            return validateArray(environment,(Arr)value);
        if ((value instanceof Character) || (value instanceof Bin))
            return ValidationResult.ACCEPTABLE;
        if (value instanceof String){
            if ("".equals(value) && isEmptyStringAllowed()){
                return ValidationResult.ACCEPTABLE;
            }            
            if (validator!=null){
                final ValidationResult validation = validator.validate(environment, (String)value, ((String)value).length());
                if (validation.getState()!=EValidatorState.ACCEPTABLE)
                    return ValidationResult.Factory.newInvalidResult(validation.getInvalidValueReason());
            }
            else if (!inputMask.isEmpty() && !inputMask.isAcceptableInput((String)value)){
                return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForWrongFormatValue(environment));
            }
            return ValidationResult.ACCEPTABLE;
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }

    private boolean saveSeparators = true;

    public boolean isSaveSeparators() {
        return saveSeparators;
    }

    public void setSaveSeparators(final boolean saveSeparators) {
        this.saveSeparators = saveSeparators;
    }

    public boolean isEmptyStringAllowed() {
        return allowEmptyString;
    }

    public void setEmptyStringAllowed(final boolean allowed) {
        allowEmptyString = allowed;
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.STR;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.isPassword ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.inputMask);
        hash = 37 * hash + (this.noBlankCharacter ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.maxLength);
        hash = 37 * hash + (this.allowEmptyString ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.validator);
        hash = 37 * hash + (this.saveSeparators ? 1 : 0);
        hash = 37 * hash + super.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this){
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskStr other = (EditMaskStr) obj;
        if (this.isPassword != other.isPassword) {
            return false;
        }
        if (!Objects.equals(this.inputMask, other.inputMask)) {
            return false;
        }
        if (this.noBlankCharacter != other.noBlankCharacter) {
            return false;
        }
        if (!Objects.equals(this.maxLength, other.maxLength)) {
            return false;
        }
        if (this.allowEmptyString != other.allowEmptyString) {
            return false;
        }
        if (!Objects.equals(this.validator, other.validator)) {
            return false;
        }
        if (this.saveSeparators != other.saveSeparators) {
            return false;
        }
        return super.equals(obj);
    }  
}