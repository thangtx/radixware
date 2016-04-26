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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class SQLKeywords {

    private static final Set<String> KEYWORDS;

    static {
        KEYWORDS = new HashSet<String>();
        KEYWORDS.addAll(Arrays.asList(
                "ABSOLUTE",
                "ACTION",
                "ADD",
                "ADMIN",
                "AFTER",
                "AGGREGATE",
                "ALIAS",
                "ALL",
                "ALLOCATE",
                "ALTER",
                "AND",
                "ANY",
                "ARE",
                "ARRAY",
                "AS",
                "ASC",
                "ASSERTION",
                "AT",
                "AUTHORIZATION",
                "BEFORE",
                "BEGIN",
                "BINARY",
                "BIT",
                "BLOB",
                "BOOLEAN",
                "BOTH",
                "BREADTH",
                "BY",
                "CALL",
                "CASCADE",
                "CASCADED",
                "CASE",
                "CAST",
                "CATALOG",
                "CHAR",
                "CHARACTER",
                "CHECK",
                "CLASS",
                "CLOB",
                "CLOSE",
                "COLLATE",
                "COLLATION",
                "COLUMN",
                "COMMIT",
                "COMPLETION",
                "CONDITION",
                "CONNECT",
                "CONNECTION",
                "CONSTRAINT",
                "CONSTRAINTS",
                "CONSTRUCTOR",
                "CONTAINS",
                "CONTINUE",
                "CORRESPONDING",
                "CREATE",
                "CROSS",
                "CUBE",
                "CURRENT",
                "CURRENT_DATE",
                "CURRENT_PATH",
                "CURRENT_ROLE",
                "CURRENT_TIME",
                "CURRENT_TIMESTAMP",
                "CURRENT_USER",
                "CURSOR",
                "CYCLE",
                "DATA",
                "DATALINK",
                "DATE",
                "DAY",
                "DEALLOCATE",
                "DEC",
                "DECIMAL",
                "DECLARE",
                "DEFAULT",
                "DEFERRABLE",
                "DEFERRED",
                "DELETE",
                "DEPTH",
                "DEREF",
                "DESC",
                "DESCRIBE",
                "DESCRIPTOR",
                "DESTROY",
                "DESTRUCTOR",
                "DETERMINISTIC",
                "DIAGNOSTICS",
                "DICTIONARY",
                "DISCONNECT",
                "DISTINCT",
                "DO",
                "DOMAIN",
                "DOUBLE",
                "DROP",
                "DYNAMIC",
                "EACH",
                "ELSE",
                "ELSEIF",
                "END",
                "END-EXEC",
                "EQUALS",
                "ESCAPE",
                "EVERY",
                "EXCEPT",
                "EXCEPTION",
                "EXEC",
                "EXECUTE",
                "EXIT",
                "EXPAND",
                "EXPANDING",
                "EXTERNAL",
                "FALSE",
                "FETCH",
                "FIRST",
                "FLOAT",
                "FOR",
                "FOREIGN",
                "FOUND",
                "FREE",
                "FROM",
                "FULL",
                "FUNCTION",
                "GENERAL",
                "GET",
                "GLOBAL",
                "GO",
                "GOTO",
                "GRANT",
                "GROUP",
                "GROUPING",
                "HANDLER",
                "HASH",
                "HAVING",
                "HOST",
                "HOUR",
                "IDENTITY",
                "IF",
                "IGNORE",
                "IMMEDIATE",
                "IN",
                "INDICATOR",
                "INITIALIZE",
                "INITIALLY",
                "INNER",
                "INOUT",
                "INPUT",
                "INSERT",
                "INT",
                "INTEGER",
                "INTERSECT",
                "INTERVAL",
                "INTO",
                "IS",
                "ISOLATION",
                "ITERATE",
                "JOIN",
                "KEY",
                "LANGUAGE",
                "LARGE",
                "LAST",
                "LATERAL",
                "LEADING",
                "LEAVE",
                "LEFT",
                "LESS",
                "LEVEL",
                "LIKE",
                "LIMIT",
                "LOCAL",
                "LOCALTIME",
                "LOCALTIMESTAMP",
                "LOCATOR",
                "LOOP",
                "MATCH",
                "MEETS",
                "MINUTE",
                "MODIFIES",
                "MODIFY",
                "MODULE",
                "MONTH",
                "NAMES",
                "NATIONAL",
                "NATURAL",
                "NCHAR",
                "NCLOB",
                "NEW",
                "NEXT",
                "NO",
                "NONE",
                "NORMALIZE",
                "NOT",
                "NULL",
                "NUMERIC",
                "OBJECT",
                "OF",
                "OFF",
                "OLD",
                "ON",
                "ONLY",
                "OPEN",
                "OPERATION",
                "OPTION",
                "OR",
                "ORDER",
                "ORDINALITY",
                "OUT",
                "OUTER",
                "OUTPUT",
                "PAD",
                "PARAMETER",
                "PARAMETERS",
                "PARTIAL",
                "PATH",
                "PERIOD",
                "POSTFIX",
                "PRECEDES",
                "PRECISION",
                "PREFIX",
                "PREORDER",
                "PREPARE",
                "PRESERVE",
                "PRIMARY",
                "PRIOR",
                "PRIVILEGES",
                "PROCEDURE",
                "PUBLIC",
                "READ",
                "READS",
                "REAL",
                "RECURSIVE",
                "REDO",
                "REF",
                "REFERENCES",
                "REFERENCING",
                "RELATIVE",
                "REPEAT",
                "RESIGNAL",
                "RESTRICT",
                "RESULT",
                "RETURN",
                "RETURNS",
                "REVOKE",
                "RIGHT",
                "ROLE",
                "ROLLBACK",
                "ROLLUP",
                "ROUTINE",
                "ROW",
                "ROWS",
                "SAVEPOINT",
                "SCHEMA",
                "SCROLL",
                "SEARCH",
                "SECOND",
                "SECTION",
                "SELECT",
                "SEQUENCE",
                "SESSION",
                "SESSION_USER",
                "SET",
                "SETS",
                "SIGNAL",
                "SIZE",
                "SMALLINT",
                "SOME",
                "SPACE",
                "SPECIFIC",
                "SPECIFICTYPE",
                "SQL",
                "SQLEXCEPTION",
                "SQLSTATE",
                "SQLWARNING",
                "START",
                "STATE",
                "STATEMENT",
                "STATIC",
                "STRUCTURE",
                "SYSTEM_USER",
                "TABLE",
                "TEMPORARY",
                "TERMINATE",
                "THAN",
                "THEN",
                "TIME",
                "TIMESTAMP",
                "TIMEZONE_HOUR",
                "TIMEZONE_MINUTE",
                "TO",
                "TRAILING",
                "TRANSACTION",
                "TRANSLATION",
                "TREAT",
                "TRIGGER",
                "TRUE",
                "UNDER",
                "UNION",
                "UNIQUE",
                "UNKNOWN",
                "UNTIL",
                "UPDATE",
                "USAGE",
                "USER",
                "USING",
                "VALUE",
                "VALUES",
                "VARCHAR",
                "VARIABLE",
                "VARYING",
                "VIEW",
                "WHEN",
                "WHENEVER",
                "WHERE",
                "WHILE",
                "WITH",
                "WITHOUT",
                "WORK",
                "WRITE",
                "YEAR",
                "ZONE"));
    }

    public static boolean isKeyWord(final String token) {
        return token == null ? false : KEYWORDS.contains(token.toUpperCase());
    }
}