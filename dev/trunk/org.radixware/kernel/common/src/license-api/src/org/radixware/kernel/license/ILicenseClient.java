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

package org.radixware.kernel.license;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface ILicenseClient {
    
    public void scheduleStart(ILicenseEnvironment licenseEnvironment);

    public CountDownLatch scheduleStop();

    public CountDownLatch scheduleDestroy();

    public byte[] processRequest(final byte[] encryptedRequest);

    public List<ILicenseInfo> getDefinedLicenses(final String licenseServerAddress) throws IOException;
    
    public void sendReport(Date reportDate, Reader data, Connection dbConnection) throws IOException;

}
