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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EComparisonOperator;
import static org.radixware.kernel.common.enums.EComparisonOperator.EQUALS;
import static org.radixware.kernel.common.enums.EComparisonOperator.GREATER_OR_EQUALS;
import static org.radixware.kernel.common.enums.EComparisonOperator.GREATER_THEN;
import static org.radixware.kernel.common.enums.EComparisonOperator.LESS_OR_EQUALS;
import static org.radixware.kernel.common.enums.EComparisonOperator.LESS_THAN;
import static org.radixware.kernel.common.enums.EComparisonOperator.NOT_EQUALS;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.utils.Utils;


public class SqmlPreprocessor {

    private static enum ESkipState {

        SKIP_BY_PARENT_CONDITION,
        SKIP_BY_THIS_CONDITION,
        NO_SKIP,
        PLACEHOLDER,
    }

    public Sqml preprocess(Sqml sqml, final ISqmlPreprocessorConfig config) {
        if (sqml == null) {
            return null;
        }

        if (config.alwaysCreateCopy() == false) {
            boolean hasPreprocessorTags = false;
            for (Scml.Item item : sqml.getItems()) {
                if (item instanceof TargetDbPreprocessorTag) {
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
                if (tag instanceof TargetDbPreprocessorTag) {
                    if (isSkipping()) {
                        skipState.push(ESkipState.SKIP_BY_PARENT_CONDITION);
                    } else {
                        final TargetDbPreprocessorTag ptag = (TargetDbPreprocessorTag) tag;
                        skipState.push(
                                checkCondition(
                                ptag.getDbTypeName(),
                                ptag.isCheckVersion(),
                                ptag.getVersionOperator(),
                                ptag.getDbVersion(),
                                ptag.isCheckOptions(),
                                ptag.getDbOptions(), config) ? ESkipState.NO_SKIP : ESkipState.SKIP_BY_THIS_CONDITION);
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
