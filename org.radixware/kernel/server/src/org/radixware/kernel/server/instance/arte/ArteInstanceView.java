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

package org.radixware.kernel.server.instance.arte;

import java.awt.Window;
import java.sql.Connection;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.ViewModel;


public class ArteInstanceView implements ViewModel {

    private final ArteInstance arteInstance;

    public ArteInstanceView(final ArteInstance arteInstance) {
        this.arteInstance = arteInstance;
    }

    @Override
    public Window getParentView() {
        return arteInstance.getInstance().getView().getFrame();
    }

    @Override
    public String getTitle() {
        return arteInstance.getTitle();
    }

    @Override
    public long getSeqNumber() {
        return arteInstance.getSeqNumber();
    }

    @Override
    public Connection getDbConnection() {
        return arteInstance.getDbConnection();
    }

    @Override
    public void setDbConnection(Connection dbConnection) {
        throw new UnsupportedOperationException("Arte doesn't support setting of new connection");
    }

    @Override
    public ServerTrace getTrace() {
        return arteInstance.getTrace();
    }
}
