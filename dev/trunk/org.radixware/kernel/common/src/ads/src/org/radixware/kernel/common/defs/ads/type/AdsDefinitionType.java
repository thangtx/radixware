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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public abstract class AdsDefinitionType extends AdsType {

    protected final Definition source;

    protected AdsDefinitionType(Definition source) {
        this.source = source;
    }

    public Definition getSource() {
        return source;
    }

    @Override
    public String getName() {
        if (source == null) {
            return "<Unresolved Type Ref>";
        }
        return source.getName();
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        if (source == null) {
            return "<Unresolved Type Ref>";
        }
        return source.getQualifiedName(context);
    }

    @Override
    public String getToolTip() {
        if (source != null) {
            return source.getToolTip();
        } else {
            return "<html><font color=\"#FF0000\">Referenced Definition Not Found</font><html>";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            if (obj instanceof AdsDefinitionType) {
                return this.getClass() == obj.getClass() && ((AdsDefinitionType) obj).source == this.source;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.source != null ? this.source.hashCode() : 0);
        return hash;
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType environment, IProblemHandler problemHandler) {
        Definition def = getSource();
        if (def instanceof AdsDefinition) {
            AdsUtils.checkAccessibility(referenceContext, (AdsDefinition) def, false, problemHandler);
        }
    }
}
