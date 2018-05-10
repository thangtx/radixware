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
package org.radixware.kernel.common.utils;

import java.sql.SQLException;

/**
 * <p>
This interface is a provider to serve blob and clob content for the fake result sets */
public interface IResultSetLobProvider {
    /**
     * <p>Can get blob content for the giver reference (usually URL)</p>
     * @param blobReference reference to check ability for
     * @return true if the reference can be processed
     */
    boolean canGetBlobContent(final String blobReference);
    
    /**
     * <p>Can get clob content for the giver reference (usually URL)</p>
     * @param clobReference reference to check ability for
     * @return true if the reference can be processed
     */
    boolean canGetClobContent(final String clobReference);
    
    /**
     * <p>Get blob content for the given reference</p>
     * @param blobReference reference to get content for
     * @return blob content. Can be empty but not null
     * @throws SQLException 
     */
    byte[] getBlobContent(final String blobReference) throws SQLException;
    
    /**
     * <p>Get clob content for the given reference</p>
     * @param clobReference reference to get content for
     * @return clob content. Can be empty but not null
     * @throws SQLException 
     */
    char[] getClobContent(final String clobReference) throws SQLException;
}
