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

package org.radixware.kernel.common.sqlscript.parser;

import java.io.IOException;
import org.radixware.kernel.common.sqlscript.parser.SQLAdditionParserOptions.BehaviorWhenVariablesIsNotDefined;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.TokenType;

import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;


public class SQLCommandParser {

    private final StringTokenizer st;
    private final SQLPosition position;
    private final boolean expandVariables;
    private final VariablesProvider variablesProvider;
    private final boolean preprocessor;
    private final SQLAdditionParserOptions options;
    
    public SQLCommandParser(SQLParser parser, boolean expandVariables, VariablesProvider variables, boolean preprocessor) throws IOException {
        options = parser.getAdditionOptions();
        position = parser.getPosition().fork();
        st = new StringTokenizer(parser.getCommand(), " ", "\"'");
        this.expandVariables = expandVariables;
        this.variablesProvider = variables;
        this.preprocessor = preprocessor;
    }


    public SQLToken getToken() throws IOException, SQLScriptException {
        position.setColumn(position.getColumn() + st.getPosistion());
        SQLPosition pos = position.fork();
        //TODO StringTokenier does not return position
        //pos.column += st.getIndex();
        if (!st.hasMoreTokens()) {
            return new SQLToken(pos, TokenType.TK_EOF);
        }
        String str = st.nextToken();
        String ustr = str.toUpperCase();
        if (ustr.equals("PROMPT")) {
            return new SQLToken(pos, TokenType.TK_CMD_PROMPT);
        }
        if (ustr.equals("HIDE")) {
            return new SQLToken(pos, TokenType.TK_CMD_HIDE);
        }
        if (ustr.equals("AS")) {
            return new SQLToken(pos, TokenType.TK_CMD_AS);
        }
        if (ustr.equals("SYSOPER")) {
            return new SQLToken(pos, TokenType.TK_CMD_SYSOPER);
        }
        if (ustr.equals("SYSDBA")) {
            return new SQLToken(pos, TokenType.TK_CMD_SYSDBA);
        }
        if (ustr.equals("ERRORS")) {
            return new SQLToken(pos, TokenType.TK_CMD_ERRORS);
        }
        if (ustr.equals("PACKAGE")) {
            return new SQLToken(pos, TokenType.TK_CMD_PACKAGE);
        }
        if (ustr.equals("BODY")) {
            return new SQLToken(pos, TokenType.TK_CMD_BODY);
        }
        if (ustr.equals("FUNCTION")) {
            return new SQLToken(pos, TokenType.TK_CMD_FUNCTION);
        }
        if (ustr.equals("PROCEDURE")) {
            return new SQLToken(pos, TokenType.TK_CMD_PROCEDURE);
        }
        if (ustr.equals("TRIGGER")) {
            return new SQLToken(pos, TokenType.TK_CMD_TRIGGER);
        }
        if (ustr.equals("SQLERROR")) {
            return new SQLToken(pos, TokenType.TK_CMD_SQLERROR);
        }
        if (ustr.equals("CONTINUE")) {
            return new SQLToken(pos, TokenType.TK_CMD_CONTINUE);
        }
        if (ustr.equals("EXIT")) {
            return new SQLToken(pos, TokenType.TK_CMD_EXIT);
        }
        if (ustr.equals("STOPONERROR")) {
            return new SQLToken(pos, TokenType.TK_CMD_STOPONERROR);
        }
        if (ustr.equals("ON")) {
            return new SQLToken(pos, TokenType.TK_CMD_ON);
        }
        if (ustr.equals("OFF")) {
            return new SQLToken(pos, TokenType.TK_CMD_OFF);
        } else {
            return new SQLToken(pos, TokenType.TK_NAME, expandVariables(str, preprocessor));
        }
    }

    public String getArgument() throws IOException, SQLScriptException {
        return expandVariables(st.nextToken(), preprocessor);
    }

    public boolean hasMoreTokens() {
        return st.hasMoreTokens();
    }

    public String expandVariables(String str, boolean preprocessor) throws SQLScriptException {
        final StringBuilder sb = new StringBuilder();
        final char wrap_char = preprocessor ? '$' : '&';
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == wrap_char) {
                SQLPosition pos = position.fork();
                pos.setColumn(pos.getColumn() + st.getPosistion());
                final StringBuffer var = new StringBuffer();
                for (i++; i < str.length(); i++) {
                    if (str.charAt(i) == wrap_char) {
                        SQLScriptValue value = variablesProvider.getVariable(var.toString().toUpperCase());
                        if (value == null) {
                            final BehaviorWhenVariablesIsNotDefined policyTypeWhenVariablesIsNotDefined = 
                                    options==null ? 
                                    BehaviorWhenVariablesIsNotDefined.ThrowExeption : 
                                    options.getBehaviorWhenVariablesIsNotDefined();
                            
                            if (policyTypeWhenVariablesIsNotDefined==BehaviorWhenVariablesIsNotDefined.ThrowExeption){
                                throw new SQLScriptException("The SQL script variable \"" + var.toString() + "\" has not been defined", pos);
                            }
                            else if (policyTypeWhenVariablesIsNotDefined==BehaviorWhenVariablesIsNotDefined.CollectUndefinedVariables){
                                options.getUndefinedVariablesCollection().add(var.toString().toUpperCase());
                            }
                            else if (policyTypeWhenVariablesIsNotDefined==BehaviorWhenVariablesIsNotDefined.Nothing){
                            }
                            
                        }
                        if (value!=null){
                            sb.append(value.getString());
                        }
                        break;
                    } else {
                        var.append(str.charAt(i));
                    }
                }
                if (i == str.length()) {
                    throw new SQLScriptException("Invalid token", pos);
                }
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }
}
