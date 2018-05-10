/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.sqlscript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.junit.Assert;
import org.junit.Test;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLScriptFunctionHandler;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;

/**
 *
 * @author achernomyrdin
 */
public class SQLPreprocessorTest {
    private static final String TEST_LINEAR = "line1\n" +
                                        "#IF DB_TYPE == \"ORACLE\" THEN\n" +
                                        "line2\n" +
                                        "#ENDIF\n" +
                                        "line3\n";
    private static final String RESULT_LINEAR_ALL = "line1\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" THEN*/\n" +
                                        "line2\n" +
                                        "/*#ENDIF*/\n" +
                                        "line3\n";
    private static final String RESULT_REMOVE_LINEAR_ALL = "line1\n" +
                                        "\n" +
                                        "line2\n" +
                                        "\n" +
                                        "line3\n";
    private static final String RESULT_LINEAR_NONE = "line1\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" THEN\n" +
                                        "line2\n" +
                                        "#ENDIF*/\n" +
                                        "line3\n";
    private static final String RESULT_REMOVE_LINEAR_NONE = "line1\n" +
                                        "\n" +
                                        "line3\n";
            
    private static final String TEST_NESTED = "line1\n" +
                                        "#IF DB_TYPE == \"ORACLE\" THEN\n" +
                                        "line2\n" +
                                        "#IF DB_TYPE == \"ORACLE\" and DB_VERSION == \"10\" THEN\n" +
                                        "line21\n" +
                                        "#ENDIF\n" +
                                        "line3\n" +
                                        "#ENDIF\n" +
                                        "line4\n";
    private static final String RESULT_NESTED_ALL = "line1\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" THEN*/\n" +
                                        "line2\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" and DB_VERSION == \"10\" THEN*/\n" +
                                        "line21\n" +
                                        "/*#ENDIF*/\n" +
                                        "line3\n" +
                                        "/*#ENDIF*/\n" +
                                        "line4\n";
    private static final String RESULT_REMOVE_NESTED_ALL = "line1\n" +
                                        "\n" +
                                        "line2\n" +
                                        "\n" +
                                        "line21\n" +
                                        "\n" +
                                        "line3\n" +
                                        "\n" +
                                        "line4\n";
    private static final String RESULT_NESTED_PARTIAL = "line1\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" THEN*/\n" +
                                        "line2\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" and DB_VERSION == \"10\" THEN\n" +
                                        "line21\n" +
                                        "#ENDIF*/\n" +
                                        "line3\n" +
                                        "/*#ENDIF*/\n" +
                                        "line4\n";
    private static final String RESULT_REMOVE_NESTED_PARTIAL = "line1\n" +
                                        "\n" +
                                        "line2\n" +
                                        "\n" +
                                        "line3\n" +
                                        "\n" +
                                        "line4\n";
    private static final String RESULT_NESTED_NONE = "line1\n" +
                                        "/*#IF DB_TYPE == \"ORACLE\" THEN\n" +
                                        "line2\n" +
                                        "#IF DB_TYPE == \"ORACLE\" and DB_VERSION == \"10\" THEN\n" +
                                        "line21\n" +
                                        "#ENDIF\n" +
                                        "line3\n" +
                                        "#ENDIF*/\n" +
                                        "line4\n";
    private static final String RESULT_REMOVE_NESTED_NONE = "line1\n" +
                                        "\n" +
                                        "line4\n";
    
    @Test
    public void basicTest() throws SQLScriptException, IOException {
        Assert.assertEquals(preprocessAsComment(TEST_LINEAR,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("ORACLE"))),RESULT_LINEAR_ALL);
        Assert.assertEquals(preprocessAsRemove(TEST_LINEAR,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("ORACLE"))),RESULT_REMOVE_LINEAR_ALL);
        
        Assert.assertEquals(preprocessAsComment(TEST_LINEAR,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("POSTGRESENTERPRISE"))),RESULT_LINEAR_NONE);
        Assert.assertEquals(preprocessAsRemove(TEST_LINEAR,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("POSTGRESENTERPRISE"))),RESULT_REMOVE_LINEAR_NONE);

        Assert.assertEquals(preprocessAsComment(TEST_NESTED,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("ORACLE"),"DB_VERSION",new SQLScriptValue("10"))),RESULT_NESTED_ALL);
        Assert.assertEquals(preprocessAsRemove(TEST_NESTED,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("ORACLE"),"DB_VERSION",new SQLScriptValue("10"))),RESULT_REMOVE_NESTED_ALL);

        Assert.assertEquals(preprocessAsComment(TEST_NESTED,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("ORACLE"),"DB_VERSION",new SQLScriptValue("9"))),RESULT_NESTED_PARTIAL);
        Assert.assertEquals(preprocessAsRemove(TEST_NESTED,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("ORACLE"),"DB_VERSION",new SQLScriptValue("9"))),RESULT_REMOVE_NESTED_PARTIAL);
        
        Assert.assertEquals(preprocessAsComment(TEST_NESTED,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("POSTGRESENTERPRISE"))),RESULT_NESTED_NONE);
        Assert.assertEquals(preprocessAsRemove(TEST_NESTED,new VariablesProvider4Test("DB_TYPE",new SQLScriptValue("POSTGRESENTERPRISE"))),RESULT_REMOVE_NESTED_NONE);
        
        try{new SQLPreprocessor(null,"MyScript",new VariablesProvider4Test());
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLPreprocessor("","MyScript",null);
            Assert.fail("Mandatory exception was not detected (null 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }            
    }

    @Test
    public void listReducerTest() throws SQLScriptException, IOException {
        final List<SQLPreprocessor.CommentItemWithBoolean> list = new ArrayList<SQLPreprocessor.CommentItemWithBoolean>();
        
        list.clear();   // Non-nested ranges
        list.add(new SQLPreprocessor.CommentItemWithBoolean(new SQLPosition(1,1,1,1,null),new SQLPosition(2,1,2,1,null),true));
        list.add(new SQLPreprocessor.CommentItemWithBoolean(new SQLPosition(3,1,3,1,null),new SQLPosition(4,1,4,1,null),true));
        list.add(new SQLPreprocessor.CommentItemWithBoolean(new SQLPosition(5,1,5,1,null),new SQLPosition(6,1,6,1,null),true));
        SQLPreprocessor.reduceCommentList(list);
        Assert.assertEquals(list.size(),3);

        list.clear();   // Nested ranges - need remove!
        list.add(new SQLPreprocessor.CommentItemWithBoolean(new SQLPosition(1,1,1,1,null),new SQLPosition(4,1,4,1,null),true));
        list.add(new SQLPreprocessor.CommentItemWithBoolean(new SQLPosition(2,1,2,1,null),new SQLPosition(3,1,3,1,null),true));
        list.add(new SQLPreprocessor.CommentItemWithBoolean(new SQLPosition(5,1,5,1,null),new SQLPosition(6,1,6,1,null),true));
        SQLPreprocessor.reduceCommentList(list);
        Assert.assertEquals(list.size(),2);
    }

    @Test
    public void builtinsTest() throws SQLScriptException {
        final VariablesProvider vars = new VariablesProvider4Test("EnAbLeD",new SQLScriptValue("ENABLED"),"ENABLED",new SQLScriptValue("ENABLED"),"DeFiNeD",new SQLScriptValue("Value"),"DEFINED",new SQLScriptValue("Value"),"NULLABLE",null);
        final SQLScriptFunctionHandler handler1 = new SQLPreprocessor("","MyScript",vars){
                                            public SQLScriptFunctionHandler getFunctionHandler(final String name){return functionsHandlers.get(name.toUpperCase());}
                                        }.getFunctionHandler(SQLPreprocessor.FUNC_IS_DEFINED);
        final Vector<SQLScriptValue> parmList = new Vector<>();
        
        parmList.clear();   parmList.add(new SQLScriptValue("DeFiNeD"));
        Assert.assertTrue(handler1.scriptFunction(parmList).getBoolean());
        parmList.clear();   parmList.add(new SQLScriptValue("DEFINED"));
        Assert.assertTrue(handler1.scriptFunction(parmList).getBoolean());
        parmList.clear();   parmList.add(new SQLScriptValue("NULLABLE"));
        Assert.assertFalse(handler1.scriptFunction(parmList).getBoolean());
        parmList.clear();   parmList.add(new SQLScriptValue("UNKNOWN"));
        Assert.assertFalse(handler1.scriptFunction(parmList).getBoolean());
        
        try{handler1.scriptFunction(null);
            Assert.fail("Mandatory exception was not detected (null argument)");
        } catch (SQLScriptException exc) {            
        }
        parmList.clear();
        try{handler1.scriptFunction(parmList);
            Assert.fail("Mandatory exception was not detected (wrong number of argument)");
        } catch (SQLScriptException exc) {            
        }
        
        final SQLScriptFunctionHandler handler2 = new SQLPreprocessor("","MyScript",vars){
                                            public SQLScriptFunctionHandler getFunctionHandler(final String name){return functionsHandlers.get(name.toUpperCase());}
                                        }.getFunctionHandler(SQLPreprocessor.FUNC_IS_ENABLED);
        
        parmList.clear();   parmList.add(new SQLScriptValue("EnAbLeD"));
        Assert.assertTrue(handler2.scriptFunction(parmList).getBoolean());
        parmList.clear();   parmList.add(new SQLScriptValue("ENABLED"));
        Assert.assertTrue(handler2.scriptFunction(parmList).getBoolean());
        parmList.clear();   parmList.add(new SQLScriptValue("NULLABLE"));
        Assert.assertFalse(handler2.scriptFunction(parmList).getBoolean());
        parmList.clear();   parmList.add(new SQLScriptValue("UNKNOWN"));
        Assert.assertFalse(handler2.scriptFunction(parmList).getBoolean());
        
        try{handler2.scriptFunction(null);
            Assert.fail("Mandatory exception was not detected (null argument)");
        } catch (SQLScriptException exc) {            
        }
        parmList.clear();
        try{handler2.scriptFunction(parmList);
            Assert.fail("Mandatory exception was not detected (wrong number of argument)");
        } catch (SQLScriptException exc) {            
        }
        
    }
    
    
    private String preprocessAsComment(final String source, final VariablesProvider vars) throws SQLScriptException, IOException {
        final SQLPreprocessor sqlPreprocessor = new SQLPreprocessor(source,"MyScript",vars);
        return sqlPreprocessor.preprocess(SQLPreprocessor.PreprocessBehavior.PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT, null);
    }

    private String preprocessAsRemove(final String source, final VariablesProvider vars) throws SQLScriptException, IOException {
        final SQLPreprocessor sqlPreprocessor = new SQLPreprocessor(source,"MyScript",vars);
        return sqlPreprocessor.preprocess(SQLPreprocessor.PreprocessBehavior.PT_REMOVE_UNUSED_BLOCKS, null);
    }
}

