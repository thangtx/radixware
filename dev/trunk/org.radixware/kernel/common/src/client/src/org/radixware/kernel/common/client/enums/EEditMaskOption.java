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

package org.radixware.kernel.common.client.enums;

import java.util.List;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

/**
 * Настройки маски редактирования
 */
public enum EEditMaskOption implements IKernelIntEnum{ 
    INT_NUMBER_BASE(1,"EditMaskIntNumberBaseOption"),
    INT_MIN_LENGTH(2,"EditMaskIntMinLengthOption"),
    INT_MIN_VALUE(3,"EditMaskIntMinValueOption"),
    INT_MAX_VALUE(4,"EditMaskIntMaxValueOption"),
    INT_TRIAD_DELIMETER(5,"EditMaskIntTriadDelimeterOption"),
    INT_PAD_CHARACTER(6, "EditMaskIntPadCharacter"),
    INT_STEP_SIZE(7, "EditMaskIntStepSize"),
    
    STR_IS_PASSWORD(1, "EditMaskStrIsPasswordOption"),
    STR_MAX_LENGTH(2, "EditMaskStrMaxLengthOption"),
    STR_ALLOW_EMPTY(3, "EditMaskStrAllowEmptyStringOption"),
    STR_VALUE_CONTROL(4, "EditMaskStrValueControlOption"),
    STR_VC_DEFAULT_QTMASK(5, "EditMaskStrQtMaskOption"),
    STR_VC_DEFAULT_DONOTUSEBLANKCHAR(6, "EditMaskStrDoNotUseBlankCharOption"),
    STR_VC_DEFAULT_KEEPSEPARATORS(7, "EditMaskStrKeepSeparatorsOption"),
    STR_VC_INTEGER_MINIMUM(8, "EditMaskStrIntegerMinimum"),
    STR_VC_INTEGER_MAXIMUM(9, "EditMaskStrIntegerMaximum"),
    STR_VC_NUMBER_MINIMUM(10, "EditMaskStrNumberMinimum"),
    STR_VC_NUMBER_MAXIMUM(11, "EditMaskStrNumberMaximum"),
    STR_VC_NUMBER_PRECISION(12, "EditMaskStrNumberPrecision"),
    STR_VC_REGEXP_PATTERN(13, "EditMaskStrRegexpPattern"),
    STR_VC_REGEXP_MATCHCASE(14, "EditMaskStrRegexpMatchCase"),
    
    DATETIME_QTMASK(1, "EditMaskDateTimeQtMask"),
    DATETIME_MIN(2, "EditMaskDateTimeMinimumValue"),
    DATETIME_MAX(3, "EditMaskDateTimeMaximumValue"),
    DATETIME_DATESTYLE(4, "EditMaskDateTimeDateStyle"),
    DATETIME_TIMESTYLE(5, "EditMaskDateTimeTimeStyle"),
    
    CONST_SORT(1, "EditMaskConstSetSortOption"),
    CONST_CORRECTION(2, "EditMaskConstSetListCorrection"),
    
    NUM_PRECISION(1, "EditMaskNumPrecision"),
    NUM_MINIMUM(2, "EditMaskNumMinimumValue"),
    NUM_MAXIMUM(3, "EditMaskNumMaximumValue"),
    NUM_SCALE(4, "EditMaskNumScale"),
    NUM_TRIAD_DELIMITER(5, "EditMaskNumTriadDelimiter"), 
    NUM_DECIMAL_DELIMITER(6, "EditMaskNumDecimalDelimiter"),
    
    TIMEINTERVAL_SCALE(1, "EditMaskTimeIntervalScale"),
    TIMEINTERVAL_QTMASK(2, "EditMaskTimeIntervalQtMask"),
    TIMEINTERVAL_MINIMUM(3, "EditMaskTimeIntervalMinimum"),
    TIMEINTERVAL_MAXIMUM(4, "EditMaskTimeIntervalMaximum"), 
    
    LIST_VALUES(1, "EditMaskListValues"),
    

    BOOLEAN_TRUE_VALUE(1, "EditMaskBoolTrueVal"),
    BOOLEAN_FALSE_VALUE(2, "EditMaskBoolFalseVal"),
    BOOLEAN_TITLE_VALUES_VISIBLE(3, "EditMaskBoolTitleValuesVisible"),
    BOOLEAN_FALSE_TITLE_ID(4, "EditMaskBoolFalseTitleId"),
    BOOLEAN_TRUE_TITLE_ID(5, "EditMaskBoolTrueTitleId"),
    BOOLEAN_TITLE_OWNER_ID(6, "EditMaskBoolTitleOwnerId"),
    BOOLEAN_TRUE_TITLE(7, "EditMaskBoolTrueTitle"),
    BOOLEAN_FALSE_TITLE(8, "EditMaskBoolFalseTitle"),    
    BOOLEAN_COMPATIBLE_TYPE(9, "ECompatibleTypesForBool"),
    
    FILEPATH_SELECTION_MODE(1,"EFileSelectionMode"),
    FILEPATH_FILE_TYPE_FILTER(2,"EditMaskFilePathFileTypeFilter"),
    FILEPATH_INIT_PATH(3,"EditMaskFilePathInitPath"),
    FILEPATH_FREE_INPUTAVAILABLE(4,"EditMaskFilePathFreeInputAvailable"), 
    FILEPATH_MULTIPLE_SELECTION(5,"EditMaskFilePathMultipleSelection"),
    FILEPATH_FILE_DIALOG_TITLE(6,"EditMaskFilePathFileDialogTitle"),
    FILEPATH_PATH_EXIST(7, "EditMaskFilePathExists"),
    FILEPATH_FILE_STORE_PATH(8, "EditMaskFilePathStorePath"),
    FILEPATH_FILE_DIALOG_OPEN_MODE(9, "EditMaskFilePathDialogOpenMode"),
    
    REF_SELECTOR_PRESENTATION_ID(1,"EditMaskRefSelectorPresentationId"),
    REF_EDITOR_PRESENTATION_ID(2,"EditMaskRefEditorPresentationId"),
    REF_FILTER_ID(3,"EditMaskRefFilterId"),
    REF_SORTING_ID(4,"EditMaskRefSortingId"),
    REF_DROP_DOWN(5, "EditMaskRefUseDropDownList");
    
    private final long value;
    private final String name;//Имя графического объекта редактора настройки

    private EEditMaskOption(final long value, final String name){
        this.value = value;
        this.name = name;
    }

    //Возвращает заголовок подписи к редактору настройки
    public String getTitle(){
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

}
