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


public class SQLConstants {
    public enum TokenType {
        // SQL tokens
        TK_NAME,
        TK_STRING,
        TK_SEMICOLON,
        TK_DECLARE,
        TK_BEGIN,
        TK_CREATE,
        TK_OR,
        TK_REPLACE,
        TK_PACKAGE,
        TK_TYPE,
        TK_BODY,
        TK_AS,
        TK_WRAPPED,
        TK_FUNCTION,
        TK_PROCEDURE,
        TK_TRIGGER,
        TK_END_BLOCK,
        // Command tokens
        TK_CMD_ACCEPT,
        TK_CMD_PROMPT,
        TK_CMD_DEFAULT,
        TK_CMD_NUMBER,
        TK_CMD_CHAR,
        TK_CMD_HIDE,
        TK_CMD_DEFINE,
        TK_CMD_PAUSE,
        TK_CMD_CONNECT,
        TK_CMD_DISCONNECT,
        TK_CMD_INCLUDE,
        TK_CMD_AS,
        TK_CMD_SYSOPER,
        TK_CMD_SYSDBA,
        TK_CMD_SHOW,
        TK_CMD_ERRORS,
        TK_CMD_PACKAGE,
        TK_CMD_BODY,
        TK_CMD_FUNCTION,
        TK_CMD_PROCEDURE,
        TK_CMD_TRIGGER,
        TK_CMD_SET,
        TK_CMD_WHENEVER,
        TK_CMD_SQLERROR,
        TK_CMD_CONTINUE,
        TK_CMD_EXIT,
        TK_CMD_STOPONERROR,
        TK_CMD_ON,
        TK_CMD_OFF,
        // SQL script tokens
        TK_SCRIPT_PP_STMT,
        TK_SCRIPT_PP_PRAGMA,
        TK_SCRIPT_STMT,
        TK_SCRIPT_NAME,
        TK_SCRIPT_VALUE,
        TK_SCRIPT_ASSIGN,
        TK_SCRIPT_PLUS,
        TK_SCRIPT_MINUS,
        TK_SCRIPT_EQUALS,
        TK_SCRIPT_NOT_EQUALS,
        TK_SCRIPT_LESS,
        TK_SCRIPT_MORE,
        TK_SCRIPT_LESS_OR_EQUALS,
        TK_SCRIPT_MORE_OR_EQUALS,
        TK_SCRIPT_LEFT_BRACKET,
        TK_SCRIPT_RIGHT_BRACKET,
        TK_SCRIPT_COMMA,
        TK_SCRIPT_EXCLAMATION,
        TK_SCRIPT_WHILE,
        TK_SCRIPT_ENDWHILE,
        TK_SCRIPT_BREAK,
        TK_SCRIPT_IF,
        TK_SCRIPT_ENDIF,
        TK_SCRIPT_THEN,
        TK_SCRIPT_ELSE,
        TK_SCRIPT_AND,
        TK_SCRIPT_OR,
        TK_SCRIPT_END,
        TK_UNKNOWN,
        TK_EOF
    };

    public enum StatementType {
        ST_PROMPT,
        ST_ACCEPT,
        ST_DEFINE,
        ST_PAUSE,
        ST_CONNECT,
        ST_DISCONNECT,
        ST_COMMAND,
        ST_SHOW_ERRORS,
        ST_INCLUDE,
        ST_PRAGMA,
        ST_TEXT,
        ST_SCRIPT
    };
    
    public final static int MAX_GET_STR = 1024;
    public final static String LINE_INFO = " pp_line ";
}
