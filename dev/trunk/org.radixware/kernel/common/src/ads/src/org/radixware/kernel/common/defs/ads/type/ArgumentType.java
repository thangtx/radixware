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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class ArgumentType extends AdsType {

    AdsTypeDeclaration.TypeArgument source;

    public ArgumentType(AdsTypeDeclaration.TypeArgument argument) {
        this.source = argument;
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new TypeJavaSourceSupport(this) {

            @Override
            public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {
                return JavaSourceSupport.DEFAULT_PACKAGE;
            }

            @Override
            public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
                return source.getName().toCharArray();
            }
        };
    }

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return getName();
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType environment, IProblemHandler problemHandler) {
        //do nothing
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            if (other instanceof ArgumentType) {
                return ((ArgumentType) other).source == source;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.source != null ? this.source.hashCode() : 0);
        return hash;
    }
}
