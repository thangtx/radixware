/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author achernomyrdin
 */
public class DbConfigurationTest {
    private final List<String[]> pseudoDatabase = new ArrayList<String[]>(){{
                                            add(new String[]{Layer.ORG_RADIXWARE_LAYER_URI,Layer.DatabaseOption.TARGET_DB_TYPE,"ORACLE"});
                                            add(new String[]{Layer.ORG_RADIXWARE_LAYER_URI,Layer.DatabaseOption.TARGET_DB_VERSION,"10"});
                                            add(new String[]{"something.else","NAME","VALUE"});
                                        }};
    private final int[] pseudoDatabaseCursor = new int[1];
    
    private final InvocationHandler ihrs = new InvocationHandler(){
                                        @Override
                                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                            switch (method.getName()) {
                                                case "close" : return null;
                                                case "next" : return ++pseudoDatabaseCursor[0] < pseudoDatabase.size();
                                                case "getString" :
                                                    switch (args[0].toString()) {
                                                        case DbConfiguration.DB_LAYER_URI : return pseudoDatabase.get(pseudoDatabaseCursor[0])[0];
                                                        case DbConfiguration.DB_NAME : return pseudoDatabase.get(pseudoDatabaseCursor[0])[1];
                                                        case DbConfiguration.DB_VALUE : return pseudoDatabase.get(pseudoDatabaseCursor[0])[2];
                                                        default : throw new SQLException("Unknown field name ["+args[0].toString()+"]");
                                                    }
                                                default : throw new UnsupportedOperationException("Not supported yet: "+method);
                                            }
                                        }
                                    };
    private final InvocationHandler ihps = new InvocationHandler(){
                                        @Override
                                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                            switch (method.getName()) {
                                                case "close" : return null;
                                                case "executeQuery" :
                                                    pseudoDatabaseCursor[0] = -1;
                                                    return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class<?>[]{ResultSet.class}, ihrs);
                                                default : throw new UnsupportedOperationException("Not supported yet: "+method);
                                            }
                                        }
                                    };
    private final InvocationHandler ihConn = new InvocationHandler(){
                                        @Override
                                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                            switch (method.getName()) {
                                                case "close" : return null;
                                                case "prepareStatement" :
                                                    return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class<?>[]{PreparedStatement.class}, ihps);
                                                default : throw new UnsupportedOperationException("Not supported yet: "+method);
                                            }
                                        }
                                    };

    @Test
    public void basicTest() throws SQLException {
        final DbConfiguration dbc = new DbConfiguration("ORACLE",new BigDecimal("10"), null);
        
        Assert.assertEquals(dbc.getTargetDbType(),"ORACLE");
        Assert.assertEquals(dbc.getTargetDbVersion(),new BigDecimal("10"));
        Assert.assertEquals(dbc.getOptions().size(),0);
    }
    
    @Test
    public void staticTest() throws SQLException {
        try(final Connection conn = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class<?>[]{Connection.class},ihConn)) {
            final DbConfiguration dbc = DbConfiguration.read(conn);
            
            Assert.assertEquals(dbc.getTargetDbType(),"ORACLE");
            Assert.assertEquals(dbc.getTargetDbVersion(),new BigDecimal("10"));
            Assert.assertEquals(dbc.getOptions().size(),1);
        }
    }
}
