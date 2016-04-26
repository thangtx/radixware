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

package org.radixware.kernel.server.units.mq.interfaces;

import java.sql.SQLException;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.units.mq.MqDbQueries;

/**
 * <p>This interface describes high level db queries for the MQUnit.</p>
 */
public interface IMqUnitDbQueries  extends IDbQueries {
    /**
     * <p>Read options for the module</p>
     * @param moduleId unit id
     * @return options
     * @throws SQLException 
     */
    MqDbQueries.MqUnitOptions[] readOptions(long moduleId) throws SQLException;
    
    /**
     * <p>Define master module for the given slave</p>
     * @param moduleId own module id
     * @return master module id or own module id when the one is master.
     * @throws SQLException 
     */
    long defineMastertModule(long moduleId) throws SQLException;
}
