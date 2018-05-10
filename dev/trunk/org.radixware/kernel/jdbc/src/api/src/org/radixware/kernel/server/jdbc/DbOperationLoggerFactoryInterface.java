/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.server.jdbc;

import java.sql.Connection;

/**
 *
 * @author achernomyrdin
 */
public interface DbOperationLoggerFactoryInterface {
    DBOperationLoggerInterface getLogger(final Connection conn);
}
