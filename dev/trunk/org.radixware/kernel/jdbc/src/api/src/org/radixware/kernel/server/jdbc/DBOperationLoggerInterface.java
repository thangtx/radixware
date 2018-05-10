/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc;

import java.sql.SQLException;
import org.radixware.kernel.common.trace.IRadixTrace;

/**
 *
 * @author achernomyrdin
 */
public interface DBOperationLoggerInterface extends IDbQueries {

    public void beforeDbOperation(final String sql);
    
    public void beforeDbOperation(final String sql, final EDbOperationType operationType);

    public String getExecutingSql();

    public void afterDbOperation();

    IRadixTrace getRadixTrace();

    ARTEReducedInterface getArteInterface();

    void throwDatabaseError(SQLException cause);

    void throwDatabaseError(String message, Throwable cause);
    
    void setOperationDescription(String description);
}
