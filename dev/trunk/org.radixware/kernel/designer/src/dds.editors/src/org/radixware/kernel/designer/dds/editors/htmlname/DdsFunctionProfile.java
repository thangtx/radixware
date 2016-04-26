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

package org.radixware.kernel.designer.dds.editors.htmlname;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.enums.ENamingPolicy;

public class DdsFunctionProfile extends RadixObject {

    private final DdsFunctionDef function;

    public DdsFunctionProfile(DdsFunctionDef function) {
        this.function = function;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append(function.getName());
        sb.append('(');
        boolean addedFlag = false;
        for (DdsParameterDef param : function.getParameters()) {
            if (addedFlag) {
                sb.append(", ");
            } else {
                addedFlag = true;
            }
            sb.append(param.getName());
            sb.append(' ');
            switch (param.getDirection()) {
                case IN:
                    sb.append("in");
                    break;
                case BOTH:
                    sb.append("in out");
                    break;
                case OUT:
                    sb.append("out");
                    break;
            }
            sb.append(' ');
            sb.append(param.getDbType());
        }
        sb.append(')');
        if (function.getResultValType() != null) {
            sb.append(" return ");
            sb.append(function.getResultDbType());
        }
        return sb.toString();
    }
}
