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

import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsSqlStatementSendBatchMethod extends AdsSqlMethodDef {

    private final AdsMethodDef.Profile profile = new ProfileImpl();

    private class ProfileImpl extends AdsMethodDef.Profile {

        public ProfileImpl() {
            getAccessFlags().setStatic(true);
        }

        @Override
        public AdsMethodParameters getParametersList() {
            final AdsMethodParameters parameters = super.getParametersList();
            if (!parameters.isEmpty()) {
                parameters.clear();
            }
            return parameters;
        }

        @Override
        protected boolean isPersistent() {
            return false;
        }
    }

    protected AdsSqlStatementSendBatchMethod() {
        super(AdsSystemMethodDef.ID_STMT_SEND_BATCH, "sendBatch");
    }

    protected AdsSqlStatementSendBatchMethod(AbstractMethodDefinition xMethod) {
        super(xMethod);
    }

    @Override
    public AdsSqlClassDef getOwnerClass() {
        return (AdsSqlClassDef) super.getOwnerClass();
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    protected boolean isPersistent() {
        return false;
    }
}
