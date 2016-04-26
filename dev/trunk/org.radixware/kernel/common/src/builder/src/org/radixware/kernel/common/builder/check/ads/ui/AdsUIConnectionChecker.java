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

package org.radixware.kernel.common.builder.check.ads.ui;

import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;


@RadixObjectCheckerRegistration
public class AdsUIConnectionChecker extends RadixObjectChecker<AdsUIConnection> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsUIConnection.class;
    }

    @Override
    public void check(AdsUIConnection conn, IProblemHandler problemHandler) {
        super.check(conn, problemHandler);
        if (conn.getSlot() == null) {
            final AdsUIItemDef w = conn.getSender();
            error(w == null ? AdsUIUtil.getUiDef(conn) : w, problemHandler, "Can not find slot for signal \'" + conn.getSignalName() + "\'");
        }
    }
}
