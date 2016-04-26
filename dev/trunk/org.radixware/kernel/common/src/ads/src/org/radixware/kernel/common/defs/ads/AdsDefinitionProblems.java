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

package org.radixware.kernel.common.defs.ads;

import java.util.List;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IOverwritable;


public class AdsDefinitionProblems extends RadixProblem.WarningSuppressionSupport implements RadixProblem.ProblemFixSupport {

    // AdsModule                100
    // AdsDefinitionProblems    1000
    // AdsPropertyDef           2000
    // AdsMethodProblems        3000
    // AdsEditorPresentationDef 4000
    // AdsFilterDef             5000
    // AdsEditorPageDef         20000
    // AdsClassDef              100000
    // AdsModelClassDef         200000
    // AdsEnumDef               1000000
    public static final int OVERWRITE_FINAL_DEFINITION = 1000;
    public static final int MISSING_OVERWRITE_FLAG = 1001;
    public static final int INVALID_OVERWRITE_FLAG = 1002;
    public static final int OVERWRITE_NOT_ALLOWED = 1003;
    public static final int MISSING_WEB_CUSTOM_VIEW = 1004;
    public static final int MISSING_EXPLORER_CUSTOM_VIEW = 1005;
    public static final int PRESENTATION_WITH_LAZY_PROPS_IN_CREATE_CONTEXT = 1006;    
    public static final int DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN = 1007;
    public static final int LAZY_PROPS_IN_SELECTOR_COLUMNS = 1008;
    

    protected AdsDefinitionProblems(AdsDefinition owner) {
        super(owner);
    }

    protected AdsDefinitionProblems(AdsDefinition owner, List warnings) {
        this(owner);
        if (warnings != null) {
            int arr[] = new int[warnings.size()];
            for (int i = 0; i < arr.length; i++) {
                if (warnings.get(i) instanceof Integer) {
                    arr[i] = (Integer) warnings.get(i);
                }
            }
            setSuppressedWarnings(arr);
        }
    }

    @Override
    public boolean canSuppressWarning(int code) {
        return code == MISSING_WEB_CUSTOM_VIEW || code == MISSING_EXPLORER_CUSTOM_VIEW || PRESENTATION_WITH_LAZY_PROPS_IN_CREATE_CONTEXT == code || DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN == code;
    }

    @Override
    public boolean canFix(int code, List<Fix> fixes) {
        switch (code) {
            case MISSING_OVERWRITE_FLAG:
                if (!owner.isReadOnly() && owner instanceof IOverwritable) {
                    fixes.add(new OverwriteFlagSwitcher((IOverwritable) owner, true));
                }
                break;
            case INVALID_OVERWRITE_FLAG:
                if (!owner.isReadOnly() && owner instanceof IOverwritable) {
                    fixes.add(new OverwriteFlagSwitcher((IOverwritable) owner, false));
                }
                break;
            default:
                break;
        }
        return !fixes.isEmpty();
    }

    //fixes
    private static class OverwriteFlagSwitcher implements RadixProblem.ProblemFixSupport.Fix {

        private final IOverwritable def;
        private final boolean flag;

        public OverwriteFlagSwitcher(IOverwritable def, boolean flag) {
            this.def = def;
            this.flag = flag;
        }

        @Override
        public void fix() {
            this.def.setOverwrite(flag);
        }

        @Override
        public String getDescription() {
            return "Turn " + (flag ? "on" : "off") + " \"Overwrite\" option";
        }
    }
}
