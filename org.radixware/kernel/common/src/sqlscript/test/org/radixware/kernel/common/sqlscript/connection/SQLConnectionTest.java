/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.sqlscript.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author achernomyrdin
 */

public class SQLConnectionTest {
    private static final String DB_URL = "DB_URL";
    private static final String DB_USER = "DB_USER";
    private static final String DB_PWD = "DB_PWD";
    
    private final Properties props = new Properties();
    private Connection  conn;
    
    @Before
    public void setUp() throws SQLException, IOException {
        props.clear();
        try(final InputStream is = this.getClass().getResourceAsStream("conn.properties")) {
            props.load(is);
        }
        conn =  DriverManager.getConnection(props.getProperty(DB_URL),props.getProperty(DB_USER),props.getProperty(DB_PWD));
    }
    
    @After
    public void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    public void test() throws SQLException {
         final SQLConnection sqlc = getSqlConnection();
         final List<String> content = new ArrayList<>(), empty = new ArrayList<>();
         
         sqlc.enableDbmsOutput();
         sqlc.dbmsOut("line 1");
         sqlc.dbmsOut("line 2");
         
         sqlc.getDbmsOutput(content);
         Assert.assertEquals(content.size(),2);
         Assert.assertEquals(content.get(0),"line 1");
         sqlc.getDbmsOutput(empty);
         Assert.assertEquals(empty.size(),0);
    }

    public SQLConnection getSqlConnection() {
        return new SQLConnection(conn,getDbURL());
    }
    
    public String getDbURL() {
        return props.getProperty(DB_URL);
    }
}
