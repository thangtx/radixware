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
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.sqlscript.parser.SQLAdditionParserOptions.BehaviorWhenVariablesIsNotDefined;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.StatementType;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.TokenType;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;


public class SQLParser {

    private VariablesProvider variablesProvider;
    private boolean preprocessor;
    private Lang currentLang;
    private StringBuffer statement;
    private StringBuffer substStr;
    private int substIndex;
    private SQLPosition ppStatementPos;
    ISQLReader in;
    boolean ignoreSQLErrors;
    Map<String, Integer> newObjectsLines;

    private enum Lang {

        NO, SQL, PP, SCRIPT
    };

    public SQLParser(VariablesProvider variablesProvider, ISQLReader sqlReader, boolean pPreprocessor) throws IOException {
        this.variablesProvider = variablesProvider;
        preprocessor = pPreprocessor;
        currentLang = Lang.NO;
        substIndex = 0;
        ignoreSQLErrors = false;
        statement = new StringBuffer();
        substStr = new StringBuffer();
        in = sqlReader;
        newObjectsLines = new HashMap();
        ppStatementPos = in.getPosition().fork();
    }

    public SQLParser(Reader reader, String fileName, VariablesProvider variablesProvider, boolean pPreprocessor) throws IOException {
        this(variablesProvider, new SQLReader(reader, fileName), pPreprocessor);
    }

    public void setPosition(SQLPosition pos) throws IOException {
        substStr.setLength(0);
        substIndex = 0;
        in.setPosition(pos);
    }

    public SQLPosition getPosition() {
        return in.getPosition();
    }
     
    private SQLAdditionParserOptions additionOptions=null;
    public  void setAdditionOptions(SQLAdditionParserOptions additionOptions){
        this.additionOptions = additionOptions;
    }
    public SQLAdditionParserOptions getAdditionOptions() {
        return additionOptions;
    }
    
    
    

    SQLParseStatement getPPStatement(boolean expandVariables) throws SQLScriptException {
        try {
            SQLPosition pos = new SQLPosition();
            statement.setLength(0);
            while (currentLang != Lang.PP) {
                SQLToken token = getSQLToken(expandVariables);
                switch (token.getType()) {
                    case TK_SCRIPT_PP_STMT:
                        ppStatementPos = token.getPosition();
                        currentLang = Lang.PP;
                        break;
                    case TK_EOF:
                        if (!pos.isEmpty() && statement.length() != 0) {
                            return new SQLTextStatement(pos, statement.toString());
                        } else {
                            return null;
                        }
                    default:
                        if (pos.isEmpty()) {
                            pos = token.getPosition().fork();
                        }
                }
            }
            if (!pos.isEmpty() && statement.length() != 0) {
                return new SQLTextStatement(pos, statement.toString());
            } else {
                SQLScriptStatement stat = new SQLScriptStatement(ppStatementPos);
                for (;;) {
                    SQLToken token = getScriptToken();
                    
                    TokenType type = token.getType();
                    
                    if (type == TokenType.TK_SCRIPT_END) {
                        break;
                    }
                    stat.appendToken(token);
                    
                    if (type == TokenType.TK_SCRIPT_THEN || 
                        type == TokenType.TK_SCRIPT_ENDIF ||
                        type == TokenType.TK_SCRIPT_ELSE
                            ) {
                        break;
                    }                    
                }
                currentLang = Lang.NO;
                return stat;
            }
        } catch (SQLScriptException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLScriptException(ex, getPosition());
        }
    }

    public SQLParseStatement getStatement(boolean expandVariables) throws SQLScriptException {
        try {
            for (;;) {
                statement.setLength(0);
                currentLang = Lang.NO;
                SQLToken token = getSQLToken(expandVariables);
                switch (token.getType()) {
                    case TK_SEMICOLON:
                    case TK_END_BLOCK:
                        continue;
                    case TK_DECLARE:
                    case TK_BEGIN:
                        currentLang = Lang.SQL;
                        return new SQLParseCommandStatement(token.getPosition(), getBlockString(expandVariables, false), "", "", ignoreSQLErrors);
                    case TK_CREATE: {
                        currentLang = Lang.SQL;
                        SQLToken tk = getSQLToken(expandVariables);
                        if (tk.getType() == TokenType.TK_OR) {
                            getSQLToken(expandVariables).checkType(TokenType.TK_REPLACE);
                            tk = getSQLToken(expandVariables);
                        }
                        SQLToken tk1 = getSQLToken(expandVariables);
                        String obj_type;
                        switch (tk.getType()) {
                            case TK_PACKAGE:
                                if (tk1.getType() == TokenType.TK_BODY) {
                                    obj_type = "PACKAGE BODY";
                                    tk1 = getSQLToken(expandVariables);
                                } else {
                                    obj_type = "PACKAGE";
                                }
                                break;
                            case TK_TYPE:
                                if (tk1.getType() == TokenType.TK_BODY) {
                                    obj_type = "TYPE BODY";
                                    tk1 = getSQLToken(expandVariables);
                                } else {
                                    obj_type = "TYPE";
                                }
                                break;
                            case TK_FUNCTION:
                                obj_type = "FUNCTION";
                                break;
                            case TK_PROCEDURE:
                                obj_type = "PROCEDURE";
                                break;
                            case TK_TRIGGER:
                                obj_type = "TRIGGER";
                                break;
                            default:
                                return new SQLParseCommandStatement(token.getPosition(), getStatementString(expandVariables), "", "", ignoreSQLErrors);
                        }
                        String obj_name = tk1.getName();
                        boolean wrapped = false;
                        SQLPosition pos = getPosition();
                        SQLToken tk2 = getSQLToken(expandVariables, false);
                        if (tk2.getType() == TokenType.TK_WRAPPED) {
                            wrapped = true;
                        }
                        newObjectsLines.put(obj_name.toUpperCase(), tk1.getPosition().getSourceLine());
                        return new SQLParseCommandStatement(token.getPosition(), getBlockString(expandVariables, wrapped),
                                obj_type, obj_name, ignoreSQLErrors);
                    }
                    case TK_CMD_SHOW: {
                        currentLang = Lang.SQL;
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        cp.getToken().checkType(TokenType.TK_CMD_ERRORS);
                        SQLToken tk = cp.getToken();
                        SQLToken tk1 = cp.getToken();
                        String obj_type;
                        switch (tk.getType()) {
                            case TK_CMD_PACKAGE:
                                if (tk1.getType() == TokenType.TK_CMD_BODY) {
                                    obj_type = "PACKAGE BODY";
                                    tk1 = cp.getToken();
                                } else {
                                    obj_type = "PACKAGE";
                                }
                                break;
                            case TK_CMD_FUNCTION:
                                obj_type = "FUNCTION";
                                break;
                            case TK_CMD_PROCEDURE:
                                obj_type = "PROCEDURE";
                                break;
                            case TK_CMD_TRIGGER:
                                obj_type = "TRIGGER";
                                break;
                            case TK_SEMICOLON:
                                throw new SQLScriptException("Commmand \"show errors\" was not implemented", tk.getPosition());
                            default:
                                throw new SQLScriptException("Unexpected token \"" + tk.getName() + "\" [" + tk.getType() + "]", tk.getPosition());
                        }
                        String obj_name = tk1.getName();
                        return new SQLParseShowErrorsStatement(token.getPosition(), obj_type, obj_name);
                    }
                    case TK_CMD_WHENEVER: {
                        currentLang = Lang.SQL;
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        SQLToken tk = cp.getToken();
                        if (tk.getType() == TokenType.TK_CMD_SQLERROR) {
                            ignoreSQLErrors = (cp.getToken().getType() == TokenType.TK_CMD_CONTINUE);
                        }
                        continue;
                    }
                    case TK_CMD_SET: {
                        currentLang = Lang.SQL;
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        SQLToken tk = cp.getToken();
                        if (tk.getType() == TokenType.TK_CMD_STOPONERROR) {
                            ignoreSQLErrors = (cp.getToken().getType() != TokenType.TK_CMD_ON);
                        }
                        continue;
                    }
                    case TK_CMD_PROMPT: {
                        currentLang = Lang.SQL;
                        return new SQLParsePromptStatement(ppStatementPos, forEOL());
                    }
                    case TK_CMD_ACCEPT: {
                        currentLang = Lang.SQL;
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        String var = cp.getArgument();
                        SQLScriptValue.Type type = SQLScriptValue.Type.STRING;
                        String prompt = null;
                        String default_value = null;
                        boolean hide = false;
                        while (cp.hasMoreTokens()) {
                            SQLToken tk = cp.getToken();
                            if (tk.getType() == TokenType.TK_CMD_PROMPT) {
                                prompt = cp.getArgument();
                            } else if (tk.getType() == TokenType.TK_CMD_DEFAULT) {
                                default_value = cp.getArgument();
                            } else if (tk.getType() == TokenType.TK_CMD_NUMBER) {
                                type = SQLScriptValue.Type.INT;
                            } else if (tk.getType() == TokenType.TK_CMD_CHAR) {
                                type = SQLScriptValue.Type.STRING;
                            } else if (tk.getType() == TokenType.TK_CMD_HIDE) {
                                hide = true;
                            } else {
                                throw new SQLScriptException("Invalid token " + tk.getType().toString(), tk.getPosition());
                            }
                        }
                        return new SQLParseAcceptStatement(ppStatementPos, type, var, default_value, prompt, hide);
                    }
                    case TK_CMD_DEFINE: {
                        currentLang = Lang.SQL;
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        String var;
                        String value;
                        String arg = cp.getArgument();
                        int index = arg.indexOf('=');
                        if (index != -1) {
                            var = arg.substring(0, index);
                            if (index != arg.length() - 1) {
                                value = arg.substring(index + 1);
                            } else {
                                value = cp.getArgument();
                            }
                        } else {
                            var = arg;
                            SQLToken tk = cp.getToken();
                            arg = tk.getName();
                            if (arg.equals("=")) {
                                value = cp.getArgument();
                            } else if (arg.charAt(0) == '=') {
                                value = arg.substring(1);
                            } else {
                                throw new SQLScriptException("Invalid token " + tk.getType().toString(), tk.getPosition());
                            }
                        }
                        return new SQLParseDefinetStatement(ppStatementPos, var, value);
                    }
                    case TK_CMD_PAUSE: {
                        return new SQLParsePauseStatement(ppStatementPos, getCommand());
                    }
                    case TK_CMD_CONNECT: {
                        currentLang = Lang.SQL;
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        Reference<String> user = new Reference();
                        Reference<String> password = new Reference();
                        Reference<String> base_alias = new Reference();
                        oraParseConnectString(cp, user, password, base_alias);
                        return new SQLParseConnectStatement(token.getPosition(), user.get(), password.get(), base_alias.get());
                    }
                    case TK_CMD_DISCONNECT: {
                        return new SQLParseStatement(token.getPosition(), StatementType.ST_DISCONNECT);
                    }
                    case TK_CMD_INCLUDE: {
                        SQLCommandParser cp = new SQLCommandParser(this, expandVariables, variablesProvider, preprocessor);
                        String file = cp.getToken().getName();
                        if (file.length() > 0) {
                            return new SQLParseIncludeStatement(token.getPosition(), file);
                        }
                        continue;
                    }
                    case TK_SCRIPT_STMT: {
                        currentLang = Lang.SCRIPT;
                        SQLScriptStatement stat = new SQLScriptStatement(token.getPosition());
                        for (;;) {
                            SQLToken tk = getScriptToken();
                            if (tk.getType() == TokenType.TK_SCRIPT_END) {
                                break;
                            }
                            stat.appendToken(tk);
                        }
                        currentLang = Lang.NO;
                        return stat;
                    }
                    case TK_CMD_EXIT:
                    case TK_EOF:
                        return null;
                    default:
                        return new SQLParseCommandStatement(token.getPosition(), getStatementString(expandVariables), "", "", ignoreSQLErrors);
                }
            }
        } catch (SQLScriptException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLScriptException(ex, getPosition());
        }
    }

    public int getNewObjectLine(String objectName) {
        Integer line = newObjectsLines.get(objectName.toUpperCase());
        if (line != null) {
            return line.intValue();
        }
        return 0;
    }

    public SQLToken getSQLToken(boolean expandVariables) throws IOException, SQLScriptException {
        return getSQLToken(expandVariables, false, false);
    }

    public SQLToken getSQLToken(boolean expandVariables, boolean endBlock) throws IOException, SQLScriptException {
        return getSQLToken(expandVariables, endBlock, false);
    }

    public SQLToken getSQLToken(boolean expandVariables, boolean endBlock, boolean wrapped) throws IOException, SQLScriptException {
        for (;;) {            
            SQLPosition begin_pos = in.getPosition().fork();
            int stat_length = statement.length();
            int ch = read();
            switch (ch) {
                case '\n':
                case '\r':
                case ' ':
                case '\t':
                    continue;
                case '\'': {
                    if (wrapped) {
                        continue;
                    }
                    for (;;) {
                        int c;
                        while ((c = read()) != '\'' && c != -1) {
                        }
                        if ((c = read()) != '\'') {
                            unread(c);
                            break;
                        }
                    }
                    if (endBlock) {
                        continue;
                    }
                    return new SQLToken(begin_pos, TokenType.TK_STRING);
                }
                case '"': {
                    if (wrapped) {
                        continue;
                    }
                    for (;;) {
                        int c;
                        while ((c = read()) != '\"' && c != -1) {
                        }
                        if ((c = read()) != '\"') {
                            unread(c);
                            break;
                        }
                    }
                    if (endBlock) {
                        continue;
                    }
                    return new SQLToken(begin_pos, TokenType.TK_STRING);
                }
                case ';':
                    if (endBlock) {
                        continue;
                    }
                    if (!preprocessor) {
                        statement.setLength(stat_length);
                    }
                    return new SQLToken(begin_pos, TokenType.TK_SEMICOLON);
                case '/': {
                    int c = read();
                    if (!wrapped && c == '*') {
                        for (;;) {
                            if (readByte() == '*') {
                                if ((ch = readByte()) == '/') {
                                    break;
                                }
                                unread(ch);
                            }
                        }
                        continue;
                    }
                    if (begin_pos.getColumn() == 1) {
                        while (c == ' ' || c == '\t') {
                            c = read();
                        }
                        if (c == '\n' || c == '\r' || c == -1) {
                            if (!preprocessor) {
                                statement.setLength(stat_length);
                            }
                            return new SQLToken(begin_pos, TokenType.TK_END_BLOCK);
                        }
                    }
                    if (endBlock) {
                        continue;
                    }
                    return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                }
                case '-': {
                    if (wrapped) {
                        continue;
                    }
                    int c = read();
                    if (c == '-') {
                        int stat_size = statement.length() - 2;
                        String line = forEOL();
                        if (line.indexOf(SQLConstants.LINE_INFO) == 0) {
                            in.getPosition().setSourceLine(Integer.parseInt(line.substring(SQLConstants.LINE_INFO.length())));
                            //in.getPosition().setLine(in.getPosition().getLine() - 1);
                            statement.setLength(stat_size);
                        }
                    } else {
                        unread(c);
                        if (endBlock) {
                            continue;
                        }
                        return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                    }
                    continue;
                }
                case '@':
                    if (endBlock) {
                        continue;
                    }
                    if (begin_pos.getColumn() == 1) {
                        return new SQLToken(begin_pos, TokenType.TK_CMD_INCLUDE);
                    } else {
                        return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                    }
                case '#': {
//                    if (endBlock) {
//                        continue;
//                    }
                    final int MAX = 6;
                    SQLPosition cur_pos = in.getPosition().fork();
                    
                    StringBuilder sb = new StringBuilder(MAX);
                    for (int i=0; i<MAX; i++){
                        char ch1 = (char)in.read();
                        if (ch1<1)
                            break;
                        sb.append(ch1);
                    }
                    
                    in.setPosition(cur_pos);
                    
                    String s = sb.toString();
                    
                    if (
                        !s.startsWith("IF") && 
                        !s.startsWith("ELSE") && 
                        !s.startsWith("ENDIF")
                       )
                        continue;

                    statement.setLength(statement.length() - 1);
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_PP_STMT);
                    
                    //return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                }
                case '!': {
                    if (endBlock) {
                        continue;
                    }
                    if (begin_pos.getColumn() == 1) {
                        if (currentLang == Lang.SQL) {
                            throw new SQLScriptException("The SQL script statement can't be inside PL/SQL command", begin_pos);
                        }
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_STMT);
                    }
                    return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                }
                case '$':
                case '&': {
                    if (wrapped) {
                        continue;
                    }
                    if (expandVariables && ((preprocessor && ch == '$') || (!preprocessor && ch == '&'))) {
                        int stat_len = statement.length() - 1;
                        ch = read();
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') {
                            StringBuffer name = new StringBuffer();
                            name.append((char) ch);
                            for (;;) {
                                ch = read();
                                if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || (ch >= '0' && ch <= '9')) {
                                    name.append((char) ch);
                                } else if ((preprocessor && ch == '$') || (!preprocessor && ch == '&')) {
                                    SQLScriptValue value = variablesProvider.getVariable(name.toString().toUpperCase());
                                    if (value == null) {
                                        final BehaviorWhenVariablesIsNotDefined policyTypeWhenVariablesIsNotDefined = 
                                                additionOptions==null ? 
                                                BehaviorWhenVariablesIsNotDefined.ThrowExeption : 
                                                additionOptions.getBehaviorWhenVariablesIsNotDefined();
                                        
                                           if (policyTypeWhenVariablesIsNotDefined==BehaviorWhenVariablesIsNotDefined.ThrowExeption){
                                                throw new SQLScriptException("The SQL script variable \"" + name + "\" has not been defined", begin_pos);
                                            }
                                            else if (policyTypeWhenVariablesIsNotDefined==BehaviorWhenVariablesIsNotDefined.CollectUndefinedVariables){
                                                additionOptions.getUndefinedVariablesCollection().add(name.toString());
                                            }
                                            else if (policyTypeWhenVariablesIsNotDefined==BehaviorWhenVariablesIsNotDefined.Nothing){
                                            }
                                    }
                                    else{                                        
                                        if (additionOptions!=null && additionOptions.hidePassword && name.toString().toUpperCase().equals("PASSWORD")){
                                                int len = value.getString().length();
                                                char[] data = new char[len];
                                                for (int i=0; i<len; i++)
                                                    data[i] = '*';
                                                String asterisk = String.valueOf(data);
                                             setSubstString(asterisk);   
                                            }
                                        else
                                            setSubstString(value.getString());
                                        }
                                    
                                    
                                    
                                    statement.setLength(stat_len);
                                    break;
                                } else {
                                    return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                                }
                            }
                        } else {
                            if (endBlock) {
                                unread(ch);
                            } else {
                                return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                            }
                        }
                        continue;
                    } else {
                        if (endBlock) {
                            continue;
                        }
                        return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);
                    }
                }
                case -1:
                    return new SQLToken(begin_pos, TokenType.TK_EOF);
                default:
                    if (endBlock) {
                        continue;
                    }
            }
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') {
                StringBuffer str = new StringBuffer(Character.toString((char) ch));
                for (;;) {
                    ch = read();
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_'
                            || (ch >= '0' && ch <= '9')) {
                        str.append((char) ch);
                    } else {
                        unread(ch);
                        String ustr = str.toString().toUpperCase();
                        if (ustr.equals("DECLARE")) {
                            return new SQLToken(begin_pos, TokenType.TK_DECLARE);
                        }
                        if (ustr.equals("BEGIN")) {
                            return new SQLToken(begin_pos, TokenType.TK_BEGIN);
                        }
                        if (ustr.equals("CREATE")) {
                            return new SQLToken(begin_pos, TokenType.TK_CREATE);
                        }
                        if (ustr.equals("OR")) {
                            return new SQLToken(begin_pos, TokenType.TK_OR);
                        }
                        if (ustr.equals("REPLACE")) {
                            return new SQLToken(begin_pos, TokenType.TK_REPLACE);
                        }
                        if (ustr.equals("PACKAGE")) {
                            return new SQLToken(begin_pos, TokenType.TK_PACKAGE);
                        }
                        if (ustr.equals("BODY")) {
                            return new SQLToken(begin_pos, TokenType.TK_BODY);
                        }
                        if (ustr.equals("AS")) {
                            return new SQLToken(begin_pos, TokenType.TK_AS);
                        }
                        if (ustr.equals("WRAPPED")) {
                            return new SQLToken(begin_pos, TokenType.TK_WRAPPED);
                        }
                        if (ustr.equals("FUNCTION")) {
                            return new SQLToken(begin_pos, TokenType.TK_FUNCTION);
                        }
                        if (ustr.equals("PROCEDURE")) {
                            return new SQLToken(begin_pos, TokenType.TK_PROCEDURE);
                        }
                        if (ustr.equals("TRIGGER")) {
                            return new SQLToken(begin_pos, TokenType.TK_TRIGGER);
                        }
                        if (ustr.equals("TYPE")) {
                            return new SQLToken(begin_pos, TokenType.TK_TYPE);
                        }
                        if (ustr.equals("ACCEPT")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_ACCEPT);
                        }
                        if (ustr.equals("PROMPT")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_PROMPT);
                        }
                        if (ustr.equals("HIDE")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_HIDE);
                        }
                        if (ustr.equals("DEFINE")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_DEFINE);
                        }
                        if (ustr.equals("PAUSE")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_PAUSE);
                        }
                        if (ustr.equals("CONNECT")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_CONNECT);
                        }
                        if (ustr.equals("DISCONNECT")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_DISCONNECT);
                        }
                        if (ustr.equals("SET")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_SET);
                        }
                        if (ustr.equals("SHOW")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_SHOW);
                        }
                        if (ustr.equals("WHENEVER")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_WHENEVER);
                        }
                        if (ustr.equals("EXIT")) {
                            return new SQLToken(begin_pos, TokenType.TK_CMD_EXIT);
                        }
                        if (ustr.equals("REM")) {
                            int stat_size = statement.length() - 3;
                            forEOL();
                            statement.setLength(stat_size);
                            break;
                        }
                        return new SQLToken(begin_pos, TokenType.TK_NAME, str.toString());
                    }
                }
                continue;
            }
            return new SQLToken(begin_pos, TokenType.TK_UNKNOWN);//NOPMD
        }
    }

    public SQLToken getScriptToken() throws SQLScriptException, IOException {
        for (;;) {
            SQLPosition begin_pos = in.getPosition().fork();
            int ch = read();
            switch (ch) {
                case '\r':
                case '\n':
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_END);
                case ' ':
                case '\t':
                    continue;
                case '"': {
                    StringBuffer str = new StringBuffer();
                    for (;;) {
                        ch = readByte();
                        if (ch == '"') {
                            return new SQLToken(begin_pos, new SQLScriptValue(str.toString()));
                        } else if (ch == '\\') {
                            ch = read();
                            switch (ch) {
                                case 'n': {
                                    str.append('\n');
                                    break;
                                }
                                case 'x':
                                case 'X': {
                                    char ch1 = (char) read();
                                    if (isHexChar(ch1)) {
                                        char ch2 = (char) read();
                                        if (isHexChar(ch2)) {
                                            str.append((char) ((parseHexChar(ch1) << 4) | parseHexChar(ch2)));
                                        } else {
                                            str.append((char) parseHexChar(ch1));
                                            unread(ch2);
                                        }
                                    } else {
                                        str.append(parseHexChar((char) ch));
                                        unread(ch1);
                                    }
                                    break;
                                }
                                default: {
                                    str.append((char) ch);
                                    break;
                                }
                            }
                            continue;
                        } else if (ch == '\n' || ch == '\r') {
                            throw new SQLScriptException("Invalid SQL script token", begin_pos);
                        }
                        str.append((char) ch);
                    }
                }
                case '\\': {
                    ch = readByte();
                    if (ch != '\n' && ch != '\r') {
                        throw new SQLScriptException("Invalid SQL script token", begin_pos);
                    }
                    for (;;) {
                        ch = readByte();
                        if (ch != '\n' && ch != '\r') {
                            unread(ch);
                            break;
                        }
                    }
                    continue;
                }
                case '/': {
                    int c = read();
                    if (c == '*') {
                        for (;;) {
                            if (readByte() == '*') {
                                if ((ch = readByte()) == '/') {
                                    break;
                                }
                                unread(ch);
                            }
                        }
                        continue;
                    }
                    throw new SQLScriptException("Invalid SQL script token", begin_pos);
                }
                case '=':
                    if ((ch = readByte()) == '=') {
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_EQUALS);
                    }
                    unread(ch);
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_ASSIGN);
                case '+':
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_PLUS);
                case '-': {
                    if ((ch = readByte()) == '-') {
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_END);
                    }
                    unread(ch);
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_MINUS);
                }
                case '!':
                    if ((ch = readByte()) == '=') {
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_NOT_EQUALS);
                    }
                    unread(ch);
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_EXCLAMATION);
                case '<':
                    if ((ch = readByte()) == '=') {
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_LESS_OR_EQUALS);
                    }
                    unread(ch);
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_LESS);
                case '>':
                    if ((ch = readByte()) == '=') {
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_MORE_OR_EQUALS);
                    }
                    unread(ch);
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_MORE);
                case '(':
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_LEFT_BRACKET);
                case ')':
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_RIGHT_BRACKET);
                case ',':
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_COMMA);
                case -1:
                    return new SQLToken(begin_pos, TokenType.TK_SCRIPT_END);
            }
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') {
                String str = Character.toString((char) ch);
                for (;;) {
                    ch = read();
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_'
                            || (ch >= '0' && ch <= '9')) {
                        str += Character.toString((char) ch);
                    } else {
                        unread(ch);
                        String ustr = str.toUpperCase();
                        if (ustr.equals("WHILE")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_WHILE);
                        }
                        if (ustr.equals("ENDWHILE")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_ENDWHILE);
                        }
                        if (ustr.equals("BREAK")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_BREAK);
                        }
                        if (ustr.equals("IF")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_IF);
                        }
                        if (ustr.equals("ENDIF")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_ENDIF);
                        }
                        if (ustr.equals("THEN")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_THEN);
                        }
                        if (ustr.equals("ELSE")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_ELSE);
                        }
                        if (ustr.equals("AND")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_AND);
                        }
                        if (ustr.equals("OR")) {
                            return new SQLToken(begin_pos, TokenType.TK_SCRIPT_OR);
                        }
                        if (ustr.equals("TRUE")) {
                            return new SQLToken(begin_pos, new SQLScriptValue(1));
                        }
                        if (ustr.equals("FALSE")) {
                            return new SQLToken(begin_pos, new SQLScriptValue(0));
                        }
                        return new SQLToken(begin_pos, TokenType.TK_SCRIPT_NAME, ustr);
                    }
                }
            } else if (ch >= '0' && ch <= '9') {
                int num = ch - '0';
                for (;;) {
                    ch = read();
                    if (ch >= '0' && ch <= '9') {
                        num = num * 10 + (ch - '0');
                    } else {
                        unread(ch);
                        return new SQLToken(begin_pos, new SQLScriptValue(num));
                    }
                }
            }
            //char cH = Character.valueOf(11);
            throw new SQLScriptException("Invalid SQL script token \'" + Character.valueOf((char)ch) + "\'", begin_pos);
        }
    }

    public void setSubstString(String pSubstStr) {
        substStr = new StringBuffer(pSubstStr);
        substIndex = 0;
    }

    public int read() throws IOException {
        int ch;
        if (substStr.length() != 0) {
            if (substIndex == substStr.length()) {
                substStr.setLength(0);
                substIndex = 0;
                ch = in.read();
            } else {
                ch = substStr.charAt(substIndex++);
            }
        } else {
            ch = in.read();
        }
        if (ch != -1) {
            if (statement.length() != 0 || (ch != ' ' && ch != '\n' && ch != '\r' && ch != '\t')) {
                statement.append((char) ch);
            }
        }
        return ch;
    }

    public int readByte() throws IOException, SQLScriptException {
        int ch = read();
        if (ch == -1) {
            throw new SQLScriptException("Unexpected EOF", getPosition());
        }
        return ch;
    }

    public void unread(int ch) {
        if (substIndex != 0) {
            --substIndex;
            substStr.setCharAt(substIndex, (char) ch);
        } else {
            in.unread(ch);
        }
        statement.setLength(statement.length() - 1);
    }

    public String getBlockString(boolean expandVariables, boolean wrapped) throws IOException, SQLScriptException {
        SQLToken tk = getSQLToken(expandVariables, true, wrapped);
        if (tk.getType() == TokenType.TK_EOF) {
            throw new SQLScriptException("Unexpected EOF", getPosition());
        }
        if (tk.getType() != TokenType.TK_END_BLOCK) {
            throw new SQLScriptException("Runtime : error of getting end of PL/SQL block", getPosition());
        }
        return statement.toString();
    }

    public String getStatementString(boolean expandVariables) throws IOException, SQLScriptException {
        SQLToken tk;
        for (;;) {
            tk = getSQLToken(expandVariables);
            if (tk.getType() == TokenType.TK_END_BLOCK || tk.getType() == TokenType.TK_SEMICOLON) {
                int stat_length = statement.length();
                while (stat_length > 0 && (statement.charAt(stat_length - 1) == '\n' || statement.charAt(stat_length - 1) == '\r')) {
                    stat_length--;
                }
                statement.setLength(stat_length);
                return statement.toString();
            }
            if (tk.getType() == TokenType.TK_EOF) {
                throw new SQLScriptException("Unexpected EOF", getPosition());
            }
        }
    }

    public String getCommand() throws IOException {
        int ch;
        StringBuffer str = new StringBuffer();
        while ((ch = read()) != '\n' && ch != '\r' && ch != ';' && ch != -1) {
            str.append((char) ch);
        }
        return str.toString();
    }

    public String forEOL() throws IOException {
        StringBuffer str = new StringBuffer();
        int ch;
        while ((ch = read()) != '\n' && ch != '\r' && ch != -1) {
            str.append((char) ch);
        }
        return str.toString();
    }

    private static int findFirstNotSpaceSymbol(final String str, final int index){
        for (int i=index; i<str.length(); i++){
            final char chr = str.charAt(i);
            if (chr != ' ' && chr != '\t'  && chr != '\n'   && chr != '\r' ){
                return i;            
            }
        }
        return -1;
    }
    
    
    public void oraParseConnectString(SQLCommandParser parser, Reference<String> userName, Reference<String> password, Reference<String> baseAlias) throws SQLScriptException {
        try {            
            String arg = parser.getArgument();
            int index = arg.indexOf('/');
            if (index == -1) {
                userName.set(arg);
            } else {
                userName.set(arg.substring(0, index));
                int index2 = findFirstNotSpaceSymbol(arg, index + 1);                
                if (index2!=-1 && arg.charAt(index2) == '\"'){  //RADIXMANAGER-194
                    final int index3 = arg.indexOf('\"', index2+1);
                    if (index3 == -1){                        
                        final SQLToken tk = parser.getToken();
                        throw new SQLScriptException("Invalid token " + tk.getType().toString() + " '\"' not found", tk.getPosition());
                    }
                    
                    
                    final String pwd = arg.substring(index2 + 1, index3);
                    password.set(pwd);
                    
                    if (index3 < arg.length()-1){
                        final String tail = arg.substring(index3 + 1);//tail must be empty
                        if (!tail.trim().isEmpty()){
                            final SQLToken tk = parser.getToken();
                            throw new SQLScriptException("Invalid token(s) " + tail, tk.getPosition());
                        }
                    }                    
                }
                else{
                    int index1 = arg.indexOf('@', index);
                    if (index1 == -1) {
                        final String pwd = arg.substring(index + 1);
                        if (pwd.length() != 0) {
                            password.set(pwd);
                        }
                    } else {
                        final String pwd = arg.substring(index + 1, index1);
                        if (pwd.length() != 0) {
                            password.set(pwd);
                        }
                        final String base_alias = arg.substring(index1 + 1);
                        if (base_alias.length() != 0) {
                            baseAlias.set(base_alias);
                        }
                    }
                }
            }
            if (parser.hasMoreTokens()) {
                parser.getToken().checkType(TokenType.TK_CMD_AS);
                SQLToken tk = parser.getToken();
                if (tk.getType() == TokenType.TK_CMD_SYSDBA) {
                    userName.set(userName.get() + " as sysdba");
                } else if (tk.getType() == TokenType.TK_CMD_SYSOPER) {
                    userName.set(userName.get() + " as sysoper");
                } else {
                    throw new SQLScriptException("Invalid token " + tk.getType().toString(), tk.getPosition());
                }
            }
        } catch (IOException ex) {
            throw new SQLScriptException(ex, getPosition());
        }
    }

    public boolean isHexChar(char ch) {
        return (ch >= '0' && ch <= '9')
                || (ch >= 'a' && ch <= 'f')
                || (ch >= 'A' && ch <= 'F');
    }

    public int parseHexChar(char ch) throws SQLScriptException {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        } else if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        } else if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        } else {
            throw new SQLScriptException("Number format");
        }
    }    
}
