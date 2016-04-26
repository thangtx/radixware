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

package org.radixware.kernel.common.defs.ads.rights;

import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;


public class AdsRoleProblems extends AdsDefinitionProblems {         

        protected AdsRoleProblems(final AdsRoleDef owner, final List<Integer> warnings) {
            super(owner, warnings);
        }

        @Override
        public boolean canSuppressWarning(final int code) {
            switch (code) {
                case DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN:
                    return true;
                default:
                    return super.canSuppressWarning(code);
            }
        }
}
