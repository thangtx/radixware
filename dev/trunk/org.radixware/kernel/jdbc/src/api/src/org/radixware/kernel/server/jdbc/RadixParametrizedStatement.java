/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.server.jdbc;

import java.sql.PreparedStatement;

/**
 * <p>This interface describes a special ability of the {@link PreparedStatement} interface to use in the debugging purposes. It allow to analyze real parameters used in the
 * last 'setParam' calls</p>
 */
public interface RadixParametrizedStatement {
    /**
     * <p>Get values of parameters was bounded at the last call</p>
     * @return parameter values list. Can be empty but not null
     */
     Object[] getParamValues();
}
