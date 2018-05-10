/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors.sqmleditor;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.SqmlTag_IfParam;
import org.radixware.schemas.xscml.Sqml;


final class SqmlPreprocessor {
    
    private final static Pattern PREPROCESSOR_STATEMENT_INDEX_PATTERN = Pattern.compile("#(\\d+)");

    private static enum ESkipState {
        SKIP_BY_PARENT_CONDITION,
        SKIP_BY_THIS_CONDITION,
        NO_SKIP,
        TEXT_REPLACEMENT,
    }
    
    private final Stack<ESkipState> skipState = new Stack<>();
    private final List<String> replacements = new LinkedList<>();        
    
    public Sqml preprocess(final Sqml sqml,
                                       final ISqmlParameters params,
                                       final Map<Id,Object> paramValues,
                                       final IClientEnvironment environment,
                                       final EDefinitionDisplayMode displayMode) {
        if (sqml == null || sqml.getItemList()==null) {
            return null;
        }
        if (!hasPreprocessorTags(sqml)) {
            return sqml;
        }        
        final List<Sqml.Item> sqmlItems = sqml.getItemList();
        Sqml.Item item;
        final Sqml result = Sqml.Factory.newInstance();        
        skipState.clear();
        replacements.clear();
        for (int i=0,size=sqmlItems.size(); i<size; i++) {
            item = sqmlItems.get(i);
            
            if (item.isSetIfParam()){
                if (isSkipping()) {
                    skipState.push(ESkipState.SKIP_BY_PARENT_CONDITION);
                } else {
                    final Sqml.Item.IfParam xmlParam = item.getIfParam();
                    final boolean isParamDefined = paramValues!=null && paramValues.containsKey(xmlParam.getParamId());
                    if (isParamDefined){
                        final boolean skip = checkIfParam(xmlParam, paramValues.get(xmlParam.getParamId()));
                        skipState.push(skip ? ESkipState.NO_SKIP : ESkipState.SKIP_BY_THIS_CONDITION);
                    }else{
                        if (params==null){
                            replacements.add("\n"+translateIfParam(xmlParam)+"\n");
                            result.addNewItem().setSql("#"+(replacements.size()-1));
                        }else{
                            final SqmlTag_IfParam tag = new SqmlTag_IfParam(environment, xmlParam, 0, params, displayMode);
                            replacements.add("\n"+tag.getDisplayName()+"\n");
                            result.addNewItem().setSql("#"+(replacements.size()-1));
                        }
                        skipState.add(ESkipState.TEXT_REPLACEMENT);
                    }
                }
            }else if (item.isSetElseIf()) {
                if (skipState.peek() == ESkipState.SKIP_BY_THIS_CONDITION) {
                    skipState.pop();
                    skipState.push(ESkipState.NO_SKIP);
                } else if (skipState.peek() == ESkipState.NO_SKIP) {
                    skipState.pop();
                    skipState.push(ESkipState.SKIP_BY_THIS_CONDITION);
                } else if (skipState.peek() == ESkipState.TEXT_REPLACEMENT){
                    replacements.add("\n#ELSE IF\n");
                    result.addNewItem().setSql("#"+(replacements.size()-1));
                }
            } else if (item.isSetEndIf()) {
                if (skipState.pop()==ESkipState.TEXT_REPLACEMENT){
                    replacements.add("\n#END IF\n");
                    result.addNewItem().setSql("#"+(replacements.size()-1));
                }
            } else if (!isSkipping()) {
                if (item.isSetSql()){
                    final Matcher indexMatcher = PREPROCESSOR_STATEMENT_INDEX_PATTERN.matcher(item.getSql());
                    int startIndex = 0;                    
                    while (indexMatcher.find(startIndex)){
                        final String indexAsStr = indexMatcher.group(1);
                        if (indexAsStr!=null && !indexAsStr.isEmpty()){
                            try{
                                if (Integer.parseInt(indexAsStr)==replacements.size()){
                                    replacements.add("#"+indexAsStr);
                                }
                            }catch(NumberFormatException ex){                                
                            }
                        }
                        startIndex = indexMatcher.end()+1;
                    }
                }
                result.addNewItem().set((Sqml.Item)item.copy());
            }
        }
        skipState.clear();
        return result;
    }
    
    public String postprocess(final String sql){
        if (sql==null || sql.isEmpty() || replacements.isEmpty()){
            return sql;
        }
        final StringBuilder sqlBuilder = new StringBuilder();
        final Matcher indexMatcher = PREPROCESSOR_STATEMENT_INDEX_PATTERN.matcher(sql);
        int startIndex = 0;
        int replacementIndex = 0;
        while (indexMatcher.find(startIndex)){
            sqlBuilder.append(sql.substring(startIndex, indexMatcher.start()));
            final String indexAsStr = indexMatcher.group(1);
            if (indexAsStr!=null && !indexAsStr.isEmpty()){
                try{
                    if (Integer.parseInt(indexAsStr)==replacementIndex && replacementIndex<replacements.size()){
                        sqlBuilder.append(replacements.get(replacementIndex));
                        replacementIndex++;
                    }else{
                        sqlBuilder.append(sql.substring(indexMatcher.start(), indexMatcher.end()+1));
                    }
                }catch(NumberFormatException ex){
                    sqlBuilder.append(sql.substring(indexMatcher.start(), indexMatcher.end()+1));
                }
            }else{
                sqlBuilder.append(sql.substring(indexMatcher.start(), indexMatcher.end()+1));
            }
            startIndex = indexMatcher.end()+1;            
        }
        sqlBuilder.append(sql.substring(startIndex, sql.length()));
        return sqlBuilder.toString();
    }
    
    public static boolean hasPreprocessorTags(final Sqml sqml){        
        final List<Sqml.Item> sqmlItems = sqml==null ? Collections.<Sqml.Item>emptyList() :  sqml.getItemList();
        Sqml.Item item;
        for (int i=0,size=sqmlItems.size(); i<size; i++) {
            item = sqmlItems.get(i);
            if (item.isSetIfParam()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSkipping() {
        int i = skipState.size() - 1;
        while (i >= 0) {
            if (skipState.elementAt(i) != ESkipState.TEXT_REPLACEMENT) {
                return skipState.elementAt(i) != ESkipState.NO_SKIP;
            }
            i--;
        }
        return false;
    }    

    private static boolean checkIfParam(final Sqml.Item.IfParam xmlParam, final Object paramValue) {
        final EnumSet<EIfParamTagOperator> supportedOperators = getSupportedOperators(paramValue);        
        if (supportedOperators == null || !supportedOperators.contains(xmlParam.getOperation())) {
            throw new IllegalStateException("Preprocessor operator '" + xmlParam.getOperation() + "' is not supported for parameter #" +xmlParam.getParamId().toString() + " = '" + String.valueOf(paramValue) + "'");
        }
        switch (xmlParam.getOperation()) {
            case EMPTY:
                if (paramValue instanceof Arr) {
                    return ((Arr) paramValue).isEmpty();
                }
                return false;
            case NOT_EMPTY:
                if (paramValue instanceof Arr) {
                    return !((Arr) paramValue).isEmpty();
                }
                return false;
            case EQUAL:
                if (paramValue instanceof Arr){
                    return arrayContains(xmlParam, (Arr)paramValue);
                }else{
                    return paramValue == null ? false : scalarValueEquals(xmlParam, paramValue);
                }
            case NOT_EQUAL:
                if (paramValue instanceof Arr){
                    return !arrayContains(xmlParam, (Arr)paramValue);
                }else{
                    return paramValue == null ? false : !scalarValueEquals(xmlParam, paramValue);
                }
            case NULL:
                return paramValue == null;
            case NOT_NULL:
                return paramValue != null;
            default:
                throw new IllegalArgumentException("Unexpected preprocessor operator: " + xmlParam.getOperation());
        }
    }

    private static boolean scalarValueEquals(final Sqml.Item.IfParam tag, final Object paramValue) {
        final EValType valType = inferScalarValType(paramValue);
        if (valType == null) {
            throw new IllegalStateException("Cant infer type of parameter #" + tag.getParamId().toString());
        }
        final String valStr = ValAsStr.toStr(paramValue, valType);
        final String goldenValStr = tag.getValue()==null ? null : tag.getValue();

        return Objects.equals(valStr, goldenValStr);
    }
    
    private static boolean arrayContains(final Sqml.Item.IfParam tag, final Arr array){
        final EValType valType = inferArrayType(array);
        if (valType==null){
            throw new IllegalStateException("Cant infer type of array parameter #" + tag.getParamId());
        }  
        final String searchingValue = tag.getValue()==null ? null : tag.getValue();
        for (Object item: array){
            final String valStr = ValAsStr.toStr(item, valType);
            if (Objects.equals(valStr, searchingValue)){
                return true;
            }
        }
        return false;
    }

    private static EnumSet<EIfParamTagOperator> getSupportedOperators(final Object value) {
        if (value == null || value instanceof Arr) {
            //null can be part of any operator, and IS_NULL returns true, all other - false
            //allOf not used here to protect from future enum extention
            return EnumSet.of(EIfParamTagOperator.EQUAL, EIfParamTagOperator.NOT_EQUAL, EIfParamTagOperator.NULL, EIfParamTagOperator.NOT_NULL, EIfParamTagOperator.EMPTY, EIfParamTagOperator.NOT_EMPTY);
        } else  if (inferScalarValType(value) == null) {//unknown type
            return EnumSet.of(EIfParamTagOperator.NULL, EIfParamTagOperator.NOT_NULL);
        }else {//known scalar type
            return EnumSet.of(EIfParamTagOperator.EQUAL, EIfParamTagOperator.NOT_EQUAL, EIfParamTagOperator.NULL, EIfParamTagOperator.NOT_NULL);
        }
    }

    private static EValType inferScalarValType(final Object object) {
        if (object instanceof Number) {
            return EValType.NUM;
        } else if (object instanceof String) {
            return EValType.STR;
        } else if (object instanceof Timestamp) {
            return EValType.DATE_TIME;
        } else if (object instanceof Boolean) {
            return EValType.BOOL;
        }
        return null;
    }
    
    private static EValType inferArrayType(final Arr array){
        if (array instanceof ArrBin){
            return EValType.ARR_BIN;
        }else if (array instanceof ArrBool){
            return EValType.ARR_BOOL;
        } else if (array instanceof ArrChar){
            return EValType.ARR_CHAR;
        } else if (array instanceof ArrDateTime){
            return EValType.ARR_DATE_TIME;
        } else if (array instanceof ArrInt){
            return EValType.ARR_INT;
        } else if (array instanceof ArrNum){
            return EValType.ARR_NUM;
        } else if (array instanceof ArrStr){
            return EValType.ARR_STR;
        }else{
            return null;
        }
    }

    private static String scalarParamValueAsStr(final Object object) {
        if (object == null) {
            return null;
        }

        final EValType valType = inferScalarValType(object);
        if (valType == null) {
            throw new IllegalStateException("Unable to infer valtype of parameter  '" + object + "' of class " + object.getClass().getCanonicalName());
        }
        return ValAsStr.toStr(object, valType);
    }

    private static String toUpper(final String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }
    
    private static String translateIfParam(final Sqml.Item.IfParam xmlParam){
        final StringBuilder displayTextBuilder = new StringBuilder("#IF ( ");
        final String paramStr;
        final Id paramId = xmlParam.getParamId();
        final String paramName = paramId==null ? "???" : paramId.toString();
        final String value = xmlParam.getValue();
        final EIfParamTagOperator operator = xmlParam.getOperation();
        if (operator==null){
            displayTextBuilder.append(paramName);
            displayTextBuilder.append(" ??? ");
            displayTextBuilder.append(String.valueOf(value));
        }else{
            switch (operator){
                case EMPTY:{
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(".IsEmpty()");
                    break;
                }
                case EQUAL:{
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(" == ");
                    displayTextBuilder.append(String.valueOf(value));
                    break;
                }
                case NOT_EMPTY:{
                    displayTextBuilder.append('!');
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(".IsEmpty()");                    
                    break;
                }
                case NOT_EQUAL:{
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(" != ");
                    displayTextBuilder.append(String.valueOf(value));
                    break;                    
                }
                case NOT_NULL:{
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(" != null");
                    break;                    
                }
                case NULL:{
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(" == null");
                    break;                    
                }
                default:{
                    displayTextBuilder.append(paramName);
                    displayTextBuilder.append(" ??? ");
                    displayTextBuilder.append(String.valueOf(value));                    
                }
            }
        }
        displayTextBuilder.append(" ) THEN");
        return displayTextBuilder.toString();
    }
}
