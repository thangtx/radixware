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
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

/**
 * Base class for radix types based on fiexed string descriptor: promitive type references or java class references
 */
public abstract class FixedType extends AdsType {

    protected class FixedNameSourceSupport extends TypeJavaSourceSupport {

        private final char[] name;

        public FixedNameSourceSupport(String name) {
            super(FixedType.this);
            this.name = name.toCharArray();
        }

        @Override
        public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {
            return TypeJavaSourceSupport.DEFAULT_PACKAGE;
        }

        @Override
        public char[] getQualifiedTypeName(UsagePurpose env, boolean isHumanReadable) {
            return name;
        }

        @Override
        public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
            return name;
        }
    }
    protected final String typeName;
    private final String localName;

    protected FixedType(String typeName) {
        this.typeName = typeName;

        String localMatch = typeName;

        String[] names = typeName.split("\\.");
        for (int i = 0; i < names.length; i++) {
            if (Character.isUpperCase(names[i].charAt(0))) {
                StringBuilder builder = new StringBuilder();
                for (int j = i; j < names.length; j++) {
                    if (j > i) {
                        builder.append('.');
                    }
                    builder.append(names[j]);
                }
                localMatch = builder.toString();
                break;
            }
        }

        localName = localMatch;
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new FixedNameSourceSupport(typeName);
    }

    @Override
    public String getName() {
        return localName;
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return typeName;
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        if (typeName == null || typeName.isEmpty()) {
            error(referenceContext, problemHandler, "Missing java class or type name");
        }
    }

    @Override
    public String getToolTip() {
        return "<html><b>Native type reference</b><br>" + typeName + "</html>";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            if (obj instanceof FixedType) {
                return this.getClass() == obj.getClass() && ((FixedType) obj).typeName.equals(typeName);
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.typeName != null ? this.typeName.hashCode() : 0);
        hash = 29 * hash + (this.localName != null ? this.localName.hashCode() : 0);
        return hash;
    }
}
