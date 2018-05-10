/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.sqlscript.parser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Layer.DatabaseOption;
import org.radixware.kernel.common.sqlscript.connection.SQLConnection;
import org.radixware.kernel.common.sqlscript.connection.SQLConnectionTest;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLDialogHandler;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLMonitor;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;

/**
 *
 * @author achernomyrdin
 */
public class SQLScriptTest {
    final SQLConnectionTest ct = new SQLConnectionTest();
    
    @Before
    public void setUp() throws SQLException, IOException {
        ct.setUp();
    }
    
    @After
    public void tearDown() throws SQLException {
        ct.tearDown();
    }
    
    @Test
    public void test() {
        final Map<String,SQLScriptValue> vars = new HashMap<>();
    
        final VariablesProvider vp = new VariablesProvider() {
            @Override public SQLScriptValue getVariable(String name) {return vars.get(name);}
            @Override public void putVariable(String name, SQLScriptValue value) {}
            @Override public void removeVariable(String name) {}
        };
    
        final SQLScript qs = new SQLScript(ct.getSqlConnection(), ct.getDbURL(), "", "", vp, null, null);
        
        vars.put(DatabaseOption.TARGET_DB_TYPE, new SQLScriptValue("ORACLE"));        
        Assert.assertEquals(qs.ExcludeCommentsIfNeeded("123 /* */ 456"),"123 /* */ 456");
        
        vars.put(DatabaseOption.TARGET_DB_TYPE, new SQLScriptValue("ENTERPRISEDB"));        
        Assert.assertEquals(qs.ExcludeCommentsIfNeeded("123 /* */ 456"),"123 456");

        vars.put(DatabaseOption.TARGET_DB_TYPE, new SQLScriptValue("UNKNOWN"));        
        Assert.assertEquals(qs.ExcludeCommentsIfNeeded("123 /* */ 456"),"123 /* */ 456");
        
        vars.remove(DatabaseOption.TARGET_DB_TYPE);        
        Assert.assertEquals(qs.ExcludeCommentsIfNeeded("123 /* */ 456"),"123 /* */ 456");
    }
}
