/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.sqlscript.parser;

import org.junit.Test;
import org.junit.Assert;
import org.radixware.kernel.common.sqlscript.parser.SQLAdditionParserOptions.BehaviorWhenVariablesIsNotDefined;
import org.radixware.kernel.common.sqlscript.parser.interfaces.IAfterParseStatement;

public class SQLEntitiesTest {
    @Test
    public void SQLPositionTest() {
        final SQLPosition pos = new SQLPosition();
        
        Assert.assertEquals(pos.getIndex(),0);
        Assert.assertEquals(pos.getLine(),-1);
        Assert.assertEquals(pos.getColumn(),-1);
        Assert.assertEquals(pos.getSourceLine(),-1);
        Assert.assertNull(pos.getSrcName());
        
        pos.setIndex(10); Assert.assertEquals(pos.getIndex(),10);
        pos.setLine(20); Assert.assertEquals(pos.getLine(),20);
        pos.setColumn(30); Assert.assertEquals(pos.getColumn(),30);
        pos.setSourceLine(40); Assert.assertEquals(pos.getSourceLine(),40);
        pos.setSrcName("test"); Assert.assertEquals(pos.getSrcName(),"test");
        
        final SQLPosition fork = pos.fork();
        
        Assert.assertEquals(fork.getIndex(),10);
        Assert.assertEquals(fork.getLine(),20);
        Assert.assertEquals(fork.getColumn(),30);
        Assert.assertEquals(fork.getSourceLine(),40);
        Assert.assertEquals(fork.getSrcName(),"test");
        
        final SQLPosition def = new SQLPosition(1,2,3,4,"check");
        
        Assert.assertEquals(def.getIndex(),1);
        Assert.assertEquals(def.getLine(),2);
        Assert.assertEquals(def.getColumn(),3);
        Assert.assertEquals(def.getSourceLine(),4);
        Assert.assertEquals(def.getSrcName(),"check");
        
        try{new SQLPosition(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
    
    @Test
    public void SQLParseStatementTest() {
        final SQLParseStatement stmt = new SQLParseStatement(new SQLPosition(),SQLConstants.StatementType.ST_TEXT);
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_TEXT);
        
        try{new SQLParseStatement(null,SQLConstants.StatementType.ST_TEXT);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseStatement(new SQLPosition(),null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
    
    @Test
    public void SQLTextStatementTest() {
        final SQLTextStatement stmt = new SQLTextStatement(new SQLPosition(),"myText");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_TEXT);
        Assert.assertEquals(stmt.getText(),"myText");
        
        try{new SQLParseStatement(null,SQLConstants.StatementType.ST_TEXT);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseStatement(new SQLPosition(),null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
    

    @Test
    public void SQLScriptValueTest() throws SQLScriptException {
        final SQLScriptValue undef = new SQLScriptValue();
        
        Assert.assertEquals(undef.getType(),SQLScriptValue.Type.UNDEFINED);
        try{undef.operatorAdd(undef);
            Assert.fail("Mandatory exception was not detected(Illegal value types combination)");
        } catch (SQLScriptException exc) {
        }
        try{undef.operatorSub(undef);
            Assert.fail("Mandatory exception was not detected(Illegal value types combination)");
        } catch (SQLScriptException exc) {
        }
        try{undef.compare(undef);
            Assert.fail("Mandatory exception was not detected(Illegal value types combination)");
        } catch (SQLScriptException exc) {
        }
        
        
        final SQLScriptValue string1 = new SQLScriptValue("test "), string2 = new SQLScriptValue(SQLScriptValue.Type.STRING,"string");
        
        Assert.assertEquals(string1.getType(),SQLScriptValue.Type.STRING);
        Assert.assertEquals(string1.getString(),"test ");
        try{string1.getInt();
            Assert.fail("Mandatory exception was not detected(can't convert content to int)");
        } catch (SQLScriptException exc) {
        }
        Assert.assertEquals(string2.getType(),SQLScriptValue.Type.STRING);
        Assert.assertEquals(string2.getString(),"string");
        try{string2.getInt();
            Assert.fail("Mandatory exception was not detected(can't convert content to int)");
        } catch (SQLScriptException exc) {
        }
        
        SQLScriptValue value = string1.operatorAdd(string2);
        
        Assert.assertEquals(value.getType(),SQLScriptValue.Type.STRING);
        Assert.assertEquals(value.getString(),"test string");
        
        try{string1.operatorSub(string2);
            Assert.fail("Mandatory exception was not detected(Illegal operation for value types combination)");
        } catch (SQLScriptException exc) {
        }
        
        Assert.assertTrue(string1.compare(string2) > 0);

        
        final SQLScriptValue int1 = new SQLScriptValue(10), int2 = new SQLScriptValue(SQLScriptValue.Type.INT,"20"), int3 = new SQLScriptValue(30);
        
        Assert.assertEquals(int1.getType(),SQLScriptValue.Type.INT);
        Assert.assertEquals(int1.getInt(),Integer.valueOf(10));
        Assert.assertEquals(int1.getString(),"10");
        Assert.assertEquals(int2.getType(),SQLScriptValue.Type.INT);
        Assert.assertEquals(int2.getInt(),Integer.valueOf(20));
        Assert.assertEquals(int2.getString(),"20");
        
        Assert.assertEquals(int1.operatorAdd(int2),int3);
        Assert.assertEquals(int1.operatorSub(int2),new SQLScriptValue(-10));
        Assert.assertTrue(int1.compare(int2) < 0);
        
        final SQLScriptValue bool1 = new SQLScriptValue("true"), bool2 = new SQLScriptValue(false);
        
        Assert.assertEquals(bool1.getType(),SQLScriptValue.Type.STRING);
        Assert.assertEquals(bool1.getInt(),Integer.valueOf(1));
        Assert.assertEquals(bool2.getType(),SQLScriptValue.Type.INT);
        Assert.assertEquals(bool2.getInt(),Integer.valueOf(0));
        
           
        try{new SQLScriptValue((String)null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLScriptValue((Integer)null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLScriptValue((Boolean)null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLScriptValue(null,"test");
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLScriptValue(SQLScriptValue.Type.STRING,null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }

        try{undef.operatorAdd(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{undef.operatorSub(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{undef.compare(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
    
    @Test
    public void SQLTokenTest() throws SQLScriptException {
        final SQLToken token = new SQLToken();
        
        Assert.assertEquals(token.getType(),SQLConstants.TokenType.TK_UNKNOWN);
        Assert.assertNull(token.getPosition());
        
        final SQLToken type = new SQLToken(new SQLPosition(),SQLConstants.TokenType.TK_CMD_AS);
        
        Assert.assertEquals(type.getType(),SQLConstants.TokenType.TK_CMD_AS);
        Assert.assertEquals(type.getPosition().getIndex(),0);
        try{type.getName();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }
        try{type.getScriptName();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }
        try{type.getScriptValue();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }

        final SQLToken name  = new SQLToken(new SQLPosition(),SQLConstants.TokenType.TK_NAME,"myName");
        
        Assert.assertEquals(name.getType(),SQLConstants.TokenType.TK_NAME);
        Assert.assertEquals(name.getPosition().getIndex(),0);
        Assert.assertEquals(name.getName(),"myName");
        try{name.getScriptName();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }
        try{name.getScriptValue();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }

        final SQLToken value  = new SQLToken(new SQLPosition(),new SQLScriptValue(100));
        
        Assert.assertEquals(value.getType(),SQLConstants.TokenType.TK_SCRIPT_VALUE);
        Assert.assertEquals(value.getPosition().getIndex(),0);
        Assert.assertEquals(value.getScriptValue(),new SQLScriptValue(100));
        try{value.getName();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }
        try{value.getScriptName();
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (SQLScriptException exc) {
        }
        
        try{new SQLToken(new SQLPosition(),(SQLConstants.TokenType)null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLToken(new SQLPosition(),(SQLScriptValue)null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLToken(new SQLPosition(),SQLConstants.TokenType.TK_AS,null);
            Assert.fail("Mandatory exception was not detected (null 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
    
    @Test
    public void SQLParseAcceptStatementTest() {
        final SQLParseAcceptStatement stmt = new SQLParseAcceptStatement(new SQLPosition(),SQLScriptValue.Type.STRING,"myVar","myDefaultValue","myPrompt",false);
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_ACCEPT);
        Assert.assertEquals(stmt.getVarType(),SQLScriptValue.Type.STRING);
        Assert.assertEquals(stmt.getVar(),"myVar");
        Assert.assertEquals(stmt.getDefaultValue(),"myDefaultValue");
        Assert.assertEquals(stmt.getPrompt(),"myPrompt");
        Assert.assertFalse(stmt.isHide());
        
        try{new SQLParseAcceptStatement(new SQLPosition(),null,"myVar","myDefaultValue","myPrompt",false);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseAcceptStatement(new SQLPosition(),SQLScriptValue.Type.STRING,null,"myDefaultValue","myPrompt",false);
            Assert.fail("Mandatory exception was not detected (null 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseAcceptStatement(new SQLPosition(),SQLScriptValue.Type.STRING,"","myDefaultValue","myPrompt",false);
            Assert.fail("Mandatory exception was not detected (empty 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLParseCommandStatementTest() {
        final SQLParseCommandStatement stmt = new SQLParseCommandStatement(new SQLPosition(),"my command","myType","myName");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_COMMAND);
        Assert.assertEquals(stmt.getCommand(),"my command");
        Assert.assertEquals(stmt.getNewObjectType(),"myType");
        Assert.assertEquals(stmt.getNewObjectName(),"myName");
        Assert.assertFalse(stmt.isIgnoreSQLErrors());
        
        try{new SQLParseCommandStatement(new SQLPosition(),null,"myType","myName");
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLParseConnectStatementTest() {
        final SQLParseConnectStatement stmt = new SQLParseConnectStatement(new SQLPosition(),"myUser","myPasswd","myAlias");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_CONNECT);
        Assert.assertEquals(stmt.getUser(),"myUser");
        Assert.assertEquals(stmt.getPassword(),"myPasswd");
        Assert.assertEquals(stmt.getBaseAlias(),"myAlias");
        
        try{new SQLParseConnectStatement(new SQLPosition(),null,"myPasswd","myAlias");
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseConnectStatement(new SQLPosition(),"","myPasswd","myAlias");
            Assert.fail("Mandatory exception was not detected (empty 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseConnectStatement(new SQLPosition(),"myUser",null,"myAlias");
            Assert.fail("Mandatory exception was not detected (null 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
    
    @Test
    public void SQLParseDefinetStatementTest() {
        final SQLParseDefinetStatement stmt = new SQLParseDefinetStatement(new SQLPosition(),"myVar","myValue");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_DEFINE);
        Assert.assertEquals(stmt.getVar(),"myVar");
        Assert.assertEquals(stmt.getValue(),"myValue");
        
        try{new SQLParseDefinetStatement(new SQLPosition(),null,"myValue");
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseDefinetStatement(new SQLPosition(),"","myValue");
            Assert.fail("Mandatory exception was not detected (empty 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLParseIncludeStatementTest() {
        final SQLParseIncludeStatement stmt = new SQLParseIncludeStatement(new SQLPosition(),"myFile");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_INCLUDE);
        Assert.assertEquals(stmt.getIncludeFile(),"myFile");
        
        try{new SQLParseIncludeStatement(new SQLPosition(),null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseIncludeStatement(new SQLPosition(),"");
            Assert.fail("Mandatory exception was not detected (empty 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLParsePauseStatementTest() {
        final SQLParsePauseStatement stmt = new SQLParsePauseStatement(new SQLPosition(),"myPrompt");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_PAUSE);
        Assert.assertEquals(stmt.getPrompt(),"myPrompt");
        
        try{new SQLParsePauseStatement(new SQLPosition(),null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLParsePromptStatementTest() {
        final SQLParsePromptStatement stmt = new SQLParsePromptStatement(new SQLPosition(),"myPrompt");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_PROMPT);
        Assert.assertEquals(stmt.getPrompt(),"myPrompt");
        
        try{new SQLParsePromptStatement(new SQLPosition(),null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLParseShowErrorsStatementTest() {
        final SQLParseShowErrorsStatement stmt = new SQLParseShowErrorsStatement(new SQLPosition(),"myType","myObject");
        
        Assert.assertEquals(stmt.getPosition().getIndex(),0);
        Assert.assertEquals(stmt.getType(),SQLConstants.StatementType.ST_SHOW_ERRORS);
        Assert.assertEquals(stmt.getObjectType(),"myType");
        Assert.assertEquals(stmt.getObjectName(),"myObject");
        
        try{new SQLParseShowErrorsStatement(new SQLPosition(),null,"myObject");
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseShowErrorsStatement(new SQLPosition(),"","myObject");
            Assert.fail("Mandatory exception was not detected (empty 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseShowErrorsStatement(new SQLPosition(),"myType",null);
            Assert.fail("Mandatory exception was not detected (null 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new SQLParseShowErrorsStatement(new SQLPosition(),"myType","");
            Assert.fail("Mandatory exception was not detected (empty 3-rd argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void SQLAdditionParserOptionsTest() {
        final SQLAdditionParserOptions apo = SQLAdditionParserOptions.Factory.create("superuser");
        
        Assert.assertEquals(apo.getSupersedingSysUser(),"superuser");
        Assert.assertEquals(apo.getBehaviorWhenVariablesIsNotDefined(),BehaviorWhenVariablesIsNotDefined.ThrowExeption);
        Assert.assertEquals(apo.getUndefinedVariablesCollection().size(),0);
        Assert.assertNull(apo.getVariablesPositionCollector());
        Assert.assertNull(apo.getAfterProcessBlock());
        
        apo.setSupersedingSysUser(null);
        apo.setBehaviorWhenVariablesIsNotDefined(BehaviorWhenVariablesIsNotDefined.Nothing);
        apo.setVariablesPositionCollector(new SQLAdditionParserOptions.IVariablesPositionCollector() {
                @Override
                public void collect(char boundarySymbol, int indexInScriptBody, String variableName) {
                }
            }
        );
        apo.setAfterProcessBlock(new IAfterParseStatement() {
                @Override
                public void afterProcess(SQLParseStatement statement, int deepLevel, SQLParser parser) {
                }
            }
        );
        
        Assert.assertNull(apo.getSupersedingSysUser());
        Assert.assertEquals(apo.getBehaviorWhenVariablesIsNotDefined(),BehaviorWhenVariablesIsNotDefined.Nothing);
        Assert.assertNotNull(apo.getVariablesPositionCollector());
        Assert.assertNotNull(apo.getAfterProcessBlock());
        
        try{apo.setUndefinedVariablesCollection(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{apo.setBehaviorWhenVariablesIsNotDefined(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
    }
}
