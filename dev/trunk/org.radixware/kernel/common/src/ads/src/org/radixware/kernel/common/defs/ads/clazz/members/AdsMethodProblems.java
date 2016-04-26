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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;


public class AdsMethodProblems extends AdsDefinitionProblems {

    public static final int OVERRIDE_FINAL_METHOD = 3000;
    public static final int UNPUBLISHED_ABSTRACT_METHOD = 3001;
    public static final int MISSING_IMPLEMENTATION_ON_OVERRIDE = 3002;
    public static final int MISSING_COMMAND_HANDLER_IMPLEMENTATION = 3003;

    private static class MakeAbstractMethodPublished implements Fix {

        private AdsMethodDef method;

        public MakeAbstractMethodPublished(AdsMethodDef method) {
            this.method = method;
        }

        @Override
        public void fix() {
            if (method.isInBranch()) {
                method.setPublished(true);
            }
        }

        @Override
        public String getDescription() {
            return "Make method published";
        }
    }

    AdsMethodProblems(AdsDefinition owner, List<Integer> warnings) {
        super(owner);
        if (warnings != null) {
            int arr[] = new int[warnings.size()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = warnings.get(i);
            }
            setSuppressedWarnings(arr);
        }
    }

    @Override
    public boolean canFix(int code, List<Fix> fixes) {
        switch (code) {
            case UNPUBLISHED_ABSTRACT_METHOD:
                if (!owner.isReadOnly() && owner instanceof AdsMethodDef) {
                    fixes.add(new MakeAbstractMethodPublished((AdsMethodDef) owner));
                    return true;
                } else {
                    return false;
                }
            default:
                return super.canFix(code, fixes);
        }

    }

    @Override
    public boolean canSuppressWarning(int code) {
        switch (code) {
            case UNPUBLISHED_ABSTRACT_METHOD:
            case MISSING_IMPLEMENTATION_ON_OVERRIDE:
                return true;
            default:
                return super.canSuppressWarning(code);
        }

    }
}
