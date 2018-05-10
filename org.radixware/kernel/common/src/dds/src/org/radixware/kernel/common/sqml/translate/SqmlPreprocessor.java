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
package org.radixware.kernel.common.sqml.translate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EComparisonOperator;
import static org.radixware.kernel.common.enums.EComparisonOperator.EQUALS;
import static org.radixware.kernel.common.enums.EComparisonOperator.GREATER_OR_EQUALS;
import static org.radixware.kernel.common.enums.EComparisonOperator.GREATER_THEN;
import static org.radixware.kernel.common.enums.EComparisonOperator.LESS_OR_EQUALS;
import static org.radixware.kernel.common.enums.EComparisonOperator.LESS_THAN;
import static org.radixware.kernel.common.enums.EComparisonOperator.NOT_EQUALS;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.Utils;

public class SqmlPreprocessor {

    private static enum ESkipState {

        SKIP_BY_PARENT_CONDITION,
        SKIP_BY_THIS_CONDITION,
        NO_SKIP,
        PLACEHOLDER,
    }

    public Sqml preprocess(Sqml sqml, final ISqmlPreprocessorConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Preprocessor confuguration can't be null!");
        }
        if (sqml == null) {
            return null;
        }

        if (config.alwaysCreateCopy() == false) {
            boolean hasPreprocessorTags = false;
            for (Scml.Item item : sqml.getItems()) {
                if (item instanceof TargetDbPreprocessorTag || item instanceof IfParamTag) {
                    hasPreprocessorTags = true;
                    break;
                }
            }
            if (!hasPreprocessorTags) {
                return sqml;
            }
        }
        sqml = sqml.getClipboardSupport().copy();
        final List<Scml.Item> result = new ArrayList<>();
        SqmlProcessor processor = new SqmlProcessor() {
            private Stack<ESkipState> skipState = new Stack<>();

            @Override
            protected void processText(Scml.Text text) {
                if (!isSkipping()) {
                    result.add(text);
                }
            }

            boolean isSkipping() {
                int i = skipState.size() - 1;
                while (i >= 0) {
                    if (skipState.elementAt(i) != ESkipState.PLACEHOLDER) {
                        return skipState.elementAt(i) != ESkipState.NO_SKIP;
                    }
                    i--;
                }
                return false;
            }

            @Override
            protected void processTagInComment(Scml.Tag tag) {
                //skip
            }

            @Override
            protected void processTag(Scml.Tag tag) {
                if (tag instanceof TargetDbPreprocessorTag || tag instanceof IfParamTag) {
                    if (isSkipping()) {
                        skipState.push(ESkipState.SKIP_BY_PARENT_CONDITION);
                    } else {
                        boolean skip;
                        if (tag instanceof TargetDbPreprocessorTag) {
                            final TargetDbPreprocessorTag ptag = (TargetDbPreprocessorTag) tag;
                            skip = checkCondition(
                                    ptag.getDbTypeName(),
                                    ptag.isCheckVersion(),
                                    ptag.getVersionOperator(),
                                    ptag.getDbVersion(),
                                    ptag.isCheckOptions(),
                                    ptag.getDbOptions(), config);
                        } else if (tag instanceof IfParamTag) {
                            IfParamTag itag = (IfParamTag) tag;
                            final ISqmlPreprocessorConfig.PreprocessorParameter param = config.getParameter(itag.getParameterId());
                            if (param == null) {
                                throw new IllegalStateException("Parameter #" + itag.getParameterId() + " not defined");
                            }
                            skip = checkIfParam(itag, param);
                        } else {
                            throw new IllegalArgumentException("Unsupported preprocessor tag type:" + tag.getClass().getName());
                        }
                        skipState.push(skip ? ESkipState.NO_SKIP : ESkipState.SKIP_BY_THIS_CONDITION);
                    }
                } else if (tag instanceof ElseIfTag) {
                    if (skipState.peek() == ESkipState.SKIP_BY_THIS_CONDITION) {
                        skipState.pop();
                        skipState.push(ESkipState.NO_SKIP);
                    } else if (skipState.peek() == ESkipState.NO_SKIP) {
                        skipState.pop();
                        skipState.push(ESkipState.SKIP_BY_THIS_CONDITION);
                    }
                } else if (tag instanceof EndIfTag) {
                    skipState.pop();
                } else if (tag instanceof IfParamTag) {
                    skipState.push(ESkipState.PLACEHOLDER);
                } else if (!isSkipping()) {
                    result.add(tag);
                }
            }
        };
        processor.process(sqml);
        sqml.getItems().clear();
        for (Scml.Item item : result) {
            sqml.getItems().add(item);
        }
        return sqml;
    }

    private static boolean checkIfParam(final IfParamTag tag, final ISqmlPreprocessorConfig.PreprocessorParameter param) {
        final EnumSet<EIfParamTagOperator> supportedOperators = getSupportedOperators(param.getValue());
        if (supportedOperators == null || !supportedOperators.contains(tag.getOperator())) {
            throw new IllegalStateException("Preprocessor operator '" + tag.getOperator() + "' is not supported for parameter " + (param.getName() == null ? "" : "'" + param.getName() + "' ") + "#" + tag.getParameterId() + " = '" + param.getValue() + "'");
        }
        switch (tag.getOperator()) {
            case EMPTY:
                if (param.getValue() instanceof Arr) {
                    return ((Arr) param.getValue()).isEmpty();
                }
                return false;
            case NOT_EMPTY:
                if (param.getValue() instanceof Arr) {
                    return !((Arr) param.getValue()).isEmpty();
                }
                return false;
            case EQUAL:
                if (param.getValue() instanceof Arr){
                    return arrayContains(tag, (Arr)param.getValue());
                }else{
                    return param.getValue() == null ? false : scalarValueEquals(tag, param);
                }
            case NOT_EQUAL:
                if (param.getValue() instanceof Arr){
                    return !arrayContains(tag, (Arr)param.getValue());
                }else{
                    return param.getValue() == null ? false : !scalarValueEquals(tag, param);
                }
            case NULL:
                return param.getValue() == null;
            case NOT_NULL:
                return param.getValue() != null;
            default:
                throw new IllegalArgumentException("Unexpected preprocessor operator: " + tag.getOperator());
        }
    }

    private static boolean scalarValueEquals(final IfParamTag tag, final ISqmlPreprocessorConfig.PreprocessorParameter param) {
        final EValType valType = inferScalarValType(param.getValue());
        if (valType == null) {
            throw new IllegalStateException("Cant infer type of parameter #" + tag.getParameterId());
        }
        final String valStr = ValAsStr.toStr(param.getValue(), valType);
        final String goldenValStr = tag.getValue() == null ? null : tag.getValue().toString();

        return Objects.equals(valStr, goldenValStr);
    }
    
    private static boolean arrayContains(final IfParamTag tag, final Arr array){
        final EValType valType = inferArrayType(array);
        if (valType==null){
            throw new IllegalStateException("Cant infer type of array parameter #" + tag.getParameterId());
        }  
        final String searchingValue = tag.getValue()==null ? null : tag.getValue().toString();
        for (Object item: array){
            final String valStr = ValAsStr.toStr(item, valType);
            if (Objects.equals(valStr, searchingValue)){
                return true;
            }
        }
        return false;
    }

    public static Collection<IfParamTag> getIfParamTags(final Sqml sqml) {
        final Collection<IfParamTag> result = new LinkedList<>();
        if (sqml != null) {
            for (Sqml.Item item : sqml.getItems()) {
                if (item instanceof IfParamTag) {
                    result.add((IfParamTag) item);
                }
            }
        }
        return result;
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

    public static EValType inferScalarValType(final Object object) {
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
    
    public static EValType inferArrayType(final Arr array){
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

    public static String scalarParamValueAsStr(final Object object) {
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

    public static boolean checkCondition(
            final String dbType,
            final boolean checkVersion,
            final EComparisonOperator op,
            final String version,
            final boolean checkOptions,
            final List<DbOptionValue> options,
            ISqmlPreprocessorConfig config) {
        if (!Utils.equals(toUpper(dbType), toUpper(config.getDbTypeName()))) {
            return false;
        }
        if (checkVersion) {
            if (config.getDbVersion() != null && (version == null || !compare(config.getDbVersion(), version, op))) {
                return false;
            }
        }
        if (checkOptions && options != null) {
            Iterator<DbOptionValue> iter = options.iterator();
            if (iter.hasNext()) {
                DbOptionValue option = iter.next();
                final Object value = config.getProperty(option.getOptionName());
                return ((value == null) == (option.getMode() == EOptionMode.DISABLED));
            }
        }
        return true;
    }

    private static boolean compare(Object left, Object right, EComparisonOperator op) {
        if (left == null || right == null) {
            return false;
        }
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        };
        try {
            BigDecimal leftDec = new BigDecimal(left.toString());
            BigDecimal rightDec = new BigDecimal(right.toString());
            left = leftDec;
            right = rightDec;
            comparator = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((BigDecimal) o1).compareTo((BigDecimal) o2);
                }
            };
        } catch (Exception ex) {
            Logger.getLogger(SqmlPreprocessor.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        int compareResult = comparator.compare(left, right);
        switch (op) {
            case EQUALS:
                return compareResult == 0;
            case GREATER_OR_EQUALS:
                return compareResult >= 0;
            case GREATER_THEN:
                return compareResult > 0;
            case LESS_OR_EQUALS:
                return compareResult <= 0;
            case LESS_THAN:
                return compareResult < 0;
            case NOT_EQUALS:
                return compareResult != 0;
            default:
                throw new IllegalArgumentException("Unsupported comparison operator " + op);
        }
    }
}
