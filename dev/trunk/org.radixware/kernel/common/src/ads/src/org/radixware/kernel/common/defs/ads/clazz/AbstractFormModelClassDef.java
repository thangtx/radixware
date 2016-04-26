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

package org.radixware.kernel.common.defs.ads.clazz;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.schemas.adsdef.ClassDefinition;


public abstract class AbstractFormModelClassDef<T extends AdsClassDef & IAdsFormPresentableClass> extends AdsModelClassDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AbstractFormModelClassDef loadFrom(final AdsClassDef owner, final ClassDefinition xDef) {
            if (owner instanceof AdsFormHandlerClassDef) {
                return AdsFormModelClassDef.Factory.loadFrom((AdsFormHandlerClassDef) owner, xDef);
            } else if (owner instanceof AdsReportClassDef) {
                return AdsReportModelClassDef.Factory.loadFrom((AdsReportClassDef) owner, xDef);
            } else {
                throw new RadixError("Invalid owner class for form model");
            }

        }

        public static AbstractFormModelClassDef newInstance(final AdsClassDef owner) {
            if (owner instanceof AdsFormHandlerClassDef) {
                return AdsFormModelClassDef.Factory.newInstance((AdsFormHandlerClassDef) owner);
            } else if (owner instanceof AdsReportClassDef) {
                return AdsReportModelClassDef.Factory.newInstance((AdsReportClassDef) owner);
            } else {
                throw new RadixError("Invalid owner class for form model");
            }
        }
    }

    protected AbstractFormModelClassDef(final T owner, final EDefinitionIdPrefix idprefix) {
        super(owner, idprefix);
    }

    protected AbstractFormModelClassDef(final T owner, final ClassDefinition xDef, final EDefinitionIdPrefix idprefix) {
        super(owner, xDef, idprefix);
    }

    @Override
    public AdsClassDef findServerSideClasDef() {
        return getOwnerClass();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsClassDef) {
                return (T) owner;
            }
        }
        return null;
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsClassDef ownerClass = getOwnerClass();
        if (ownerClass == null) {
            return super.getClientEnvironment();
        } else {
            return ownerClass.getClientEnvironment();
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return getClientEnvironment();
    }
}
