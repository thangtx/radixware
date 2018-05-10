/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.arte;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 *
 * @author dsafonov
 */
public class AbstractArteEventListener implements IArteEventListener {

    @Override
    public void beforeReleaseUnload() {
    }

    @Override
    public void beforeDbCommit() {
    }

    @Override
    public void afterDbCommit() {
    }

    @Override
    public void onDbCommitError(SQLException ex) {
    }

    @Override
    public void beforeDbRollback(Savepoint sp, String savepointId, long nesting) {
    }

    @Override
    public void afterDbRollback(Savepoint sp, String savepointId, long nesting) {
    }

    @Override
    public void onDbRollbackError(Savepoint sp, String savepointId, long nesting, SQLException ex) {
    }

    @Override
    public void beforeRequestProcessing() {
    }

    @Override
    public void afterRequestProcessing() {
    }

}
