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

package org.radixware.kernel.common.defs.dds;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.Sqml;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.Sqml.Tag;
import org.radixware.kernel.common.utils.Utils;

/**
 * Метаинформация о колонке {@link DdsTableDef таблицы} в базе данных.
 *
 */
public class DdsColumnDef extends DdsColumnTemplateDef implements IDdsTableItemDef, ISqmlProperty {
    private static final Set<String> RESERVED_ORACLE = new HashSet<>();
    private static final Set<String> RESERVED_ENTERPRISEDB = new HashSet<>();
    private static final Set<String> RESERVED_POSTGRES = new HashSet<>();

    static {
/*        RESERVED_DB_NAMES.addAll(Arrays.asList(
                "ABORT", "ABS", "ABSENT", "ABSOLUTE", "ACCESS", "ACCORDING", "ACTION", "ADA", "ADD", "ADMIN", "AFTER", "AGGREGATE", "ALL", "ALLOCATE", "ALSO", "ALTER",
                "ALWAYS", "ANALYSE", "ANALYZE", "AND", "ANY", "ARE", "ARRAY", "ARRAY_AGG", "ARRAY_MAX_CARDINALITY", "AS", "ASC", "ASENSITIVE", "ASSERTION", "ASSIGNMENT", "ASYMMETRIC", "AT",
                "ATOMIC", "ATTRIBUTE", "ATTRIBUTES", "AUTHORIZATION", "AVG", "BACKWARD", "BASE64", "BEFORE", "BEGIN", "BEGIN_FRAME", "BEGIN_PARTITION", "BERNOULLI", "BETWEEN", "BIGINT", "BINARY", "BIT",
                "BIT_LENGTH", "BLOB", "BLOCKED", "BOM", "BOOLEAN", "BOTH", "BREADTH", "BY", "CACHE", "CALL", "CALLED", "CARDINALITY", "CASCADE", "CASCADED", "CASE", "CAST",
                "CATALOG", "CATALOG_NAME", "CEIL", "CEILING", "CHAIN", "CHAR", "CHARACTER", "CHARACTERISTICS", "CHARACTERS", "CHARACTER_LENGTH", "CHARACTER_SET_CATALOG", "CHARACTER_SET_NAME", "CHARACTER_SET_SCHEMA", "CHAR_LENGTH", "CHECK", "CHECKPOINT",
                "CLASS", "CLASS_ORIGIN", "CLOB", "CLOSE", "CLUSTER", "COALESCE", "COBOL", "COLLATE", "COLLATION", "COLLATION_CATALOG", "COLLATION_NAME", "COLLATION_SCHEMA", "COLLECT", "COLUMN", "COLUMNS", "COLUMN_NAME",
                "COMMAND_FUNCTION", "COMMAND_FUNCTION_CODE", "COMMENT", "COMMENTS", "COMMIT", "COMMITTED", "CONCURRENTLY", "CONDITION", "CONDITION_NUMBER", "CONFIGURATION", "CONFLICT", "CONNECT", "CONNECTION", "CONNECTION_NAME", "CONSTRAINT", "CONSTRAINTS",
                "CONSTRAINT_CATALOG", "CONSTRAINT_NAME", "CONSTRAINT_SCHEMA", "CONSTRUCTOR", "CONTAINS", "CONTENT", "CONTINUE", "CONTROL", "CONVERSION", "CONVERT", "COPY", "CORR", "CORRESPONDING", "COST", "COUNT", "COVAR_POP",
                "COVAR_SAMP", "CREATE", "CROSS", "CSV", "CUBE", "CUME_DIST", "CURRENT", "CURRENT_CATALOG", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_ROW", "CURRENT_SCHEMA", "CURRENT_TIME", "CURRENT_TIMESTAMP",
                "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CURSOR_NAME", "CYCLE", "DATA", "DATABASE", "DATALINK", "DATE", "DATETIME_INTERVAL_CODE", "DATETIME_INTERVAL_PRECISION", "DAY", "DB", "DEALLOCATE", "DEC", "DECIMAL",
                "DECLARE", "DEFAULT", "DEFAULTS", "DEFERRABLE", "DEFERRED", "DEFINED", "DEFINER", "DEGREE", "DELETE", "DELIMITER", "DELIMITERS", "DENSE_RANK", "DEPENDS", "DEPTH", "DEREF", "DERIVED",
                "DESC", "DESCRIBE", "DESCRIPTOR", "DETERMINISTIC", "DIAGNOSTICS", "DICTIONARY", "DISABLE", "DISCARD", "DISCONNECT", "DISPATCH", "DISTINCT", "DLNEWCOPY", "DLPREVIOUSCOPY", "DLURLCOMPLETE", "DLURLCOMPLETEONLY", "DLURLCOMPLETEWRITE",
                "DLURLPATH", "DLURLPATHONLY", "DLURLPATHWRITE", "DLURLSCHEME", "DLURLSERVER", "DLVALUE", "DO", "DOCUMENT", "DOMAIN", "DOUBLE", "DROP", "DYNAMIC", "DYNAMIC_FUNCTION", "DYNAMIC_FUNCTION_CODE", "EACH", "ELEMENT",
                "ELSE", "EMPTY", "ENABLE", "ENCODING", "ENCRYPTED", "END", "END-EXEC", "END_FRAME", "END_PARTITION", "ENFORCED", "ENUM", "EQUALS", "ESCAPE", "EVENT", "EVERY", "EXCEPT",
                "EXCEPTION", "EXCLUDE", "EXCLUDING", "EXCLUSIVE", "EXEC", "EXECUTE", "EXISTS", "EXP", "EXPLAIN", "EXPRESSION", "EXTENSION", "EXTERNAL", "EXTRACT", "FALSE", "FAMILY", "FETCH",
                "FILE", "FILTER", "FINAL", "FIRST", "FIRST_VALUE", "FLAG", "FLOAT", "FLOOR", "FOLLOWING", "FOR", "FORCE", "FOREIGN", "FORTRAN", "FORWARD", "FOUND", "FRAME_ROW",
                "FREE", "FREEZE", "FROM", "FS", "FULL", "FUNCTION", "FUNCTIONS", "FUSION", "GENERAL", "GENERATED", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GRANTED",
                "GREATEST", "GROUP", "GROUPING", "GROUPS", "HANDLER", "HAVING", "HEADER", "HEX", "HIERARCHY", "HOLD", "HOUR", "IDENTITY", "IF", "IGNORE", "ILIKE",
                "IMMEDIATE", "IMMEDIATELY", "IMMUTABLE", "IMPLEMENTATION", "IMPLICIT", "IMPORT", "IN", "INCLUDING", "INCREMENT", "INDENT", "INDEX", "INDEXES", "INDICATOR", "INHERIT", "INHERITS", "INITIALLY",
                "INLINE", "INNER", "INOUT", "INPUT", "INSENSITIVE", "INSERT", "INSTANCE", "INSTANTIABLE", "INSTEAD", "INT", "INTEGER", "INTEGRITY", "INTERSECT", "INTERSECTION", "INTERVAL", "INTO",
                "INVOKER", "IS", "ISNULL", "ISOLATION", "JOIN", "KEY", "KEY_MEMBER", "KEY_TYPE", "LABEL", "LAG", "LARGE", "LAST", "LAST_VALUE", "LATERAL", "LEAD",
                "LEADING", "LEAKPROOF", "LEAST", "LEFT", "LENGTH", "LEVEL", "LIBRARY", "LIKE", "LIKE_REGEX", "LIMIT", "LINK", "LISTEN", "LN", "LOAD", "LOCAL", "LOCALTIME",
                "LOCALTIMESTAMP", "LOCATION", "LOCATOR", "LOCK", "LOCKED", "LOGGED", "LOWER", "MAP", "MAPPING", "MATCH", "MATCHED", "MATERIALIZED", "MAX", "MAXVALUE", "MAX_CARDINALITY", "MEMBER",
                "MERGE", "MESSAGE_LENGTH", "MESSAGE_OCTET_LENGTH", "MESSAGE_TEXT", "METHOD", "MIN", "MINUTE", "MINVALUE", "MOD", "MODE", "MODIFIES", "MODULE", "MONTH", "MORE", "MOVE", "MULTISET",
                "MUMPS", "NAME", "NAMES", "NAMESPACE", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NESTING", "NEW", "NEXT", "NFC", "NFD", "NFKC", "NFKD", "NIL",
                "NO", "NONE", "NORMALIZE", "NORMALIZED", "NOT", "NOTHING", "NOTIFY", "NOTNULL", "NOWAIT", "NTH_VALUE", "NTILE", "NULL", "NULLABLE", "NULLIF", "NULLS", "NUMBER",
                "NUMERIC", "OBJECT", "OCCURRENCES_REGEX", "OCTETS", "OCTET_LENGTH", "OF", "OFF", "OFFSET", "OIDS", "OLD", "ON", "ONLY", "OPEN", "OPERATOR", "OPTION", "OPTIONS",
                "OR", "ORDER", "ORDERING", "ORDINALITY", "OTHERS", "OUT", "OUTER", "OUTPUT", "OVER", "OVERLAPS", "OVERLAY", "OVERRIDING", "OWNED", "OWNER", "PAD", "PARALLEL",
                "PARAMETER", "PARAMETER_MODE", "PARAMETER_NAME", "PARAMETER_ORDINAL_POSITION", "PARAMETER_SPECIFIC_CATALOG", "PARAMETER_SPECIFIC_NAME", "PARAMETER_SPECIFIC_SCHEMA", "PARSER", "PARTIAL", "PARTITION", "PASCAL", "PASSING", "PASSTHROUGH", "PASSWORD", "PATH", "PERCENT",
                "PERCENTILE_CONT", "PERCENTILE_DISC", "PERCENT_RANK", "PERIOD", "PERMISSION", "PLACING", "PLANS", "PLI", "POLICY", "PORTION", "POSITION", "POSITION_REGEX", "POWER", "PRECEDES", "PRECEDING", "PRECISION",
                "PREPARE", "PREPARED", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURAL", "PROCEDURE", "PROGRAM", "PUBLIC", "QUOTE", "RANGE", "RANK", "READ", "READS", "REAL",
                "REASSIGN", "RECHECK", "RECOVERY", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REFRESH", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY",
                "REGR_SYY", "REINDEX", "RELATIVE", "RELEASE", "RENAME", "REPEATABLE", "REPLACE", "REPLICA", "REQUIRING", "RESET", "RESPECT", "RESTART", "RESTORE", "RESTRICT", "RESULT", "RETURN",
                "RETURNED_CARDINALITY", "RETURNED_LENGTH", "RETURNED_OCTET_LENGTH", "RETURNED_SQLSTATE", "RETURNING", "RETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINE", "ROUTINE_CATALOG", "ROUTINE_NAME", "ROUTINE_SCHEMA", "ROW",
                "ROWS", "ROW_COUNT", "ROW_NUMBER", "RULE", "SAVEPOINT", "SCALE", "SCHEMA", "SCHEMA_NAME", "SCOPE", "SCOPE_CATALOG", "SCOPE_NAME", "SCOPE_SCHEMA", "SCROLL", "SEARCH", "SECOND", "SECTION",
                "SECURITY", "SELECT", "SELECTIVE", "SELF", "SENSITIVE", "SEQUENCE", "SEQUENCES", "SERIALIZABLE", "SERVER", "SERVER_NAME", "SESSION", "SESSION_USER", "SET", "SETOF", "SETS", "SHARE",
                "SHOW", "SIMILAR", "SIMPLE", "SIZE", "SKIP", "SMALLINT", "SNAPSHOT", "SOME", "SOURCE", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SPECIFIC_NAME", "SQL", "SQLCODE", "SQLERROR",
                "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQRT", "STABLE", "STANDALONE", "START", "STATEMENT", "STATIC", "STATISTICS", "STDDEV_POP", "STDDEV_SAMP", "STDIN", "STDOUT", "STORAGE",
                "STRICT", "STRIP", "STRUCTURE", "STYLE", "SUBCLASS_ORIGIN", "SUBMULTISET", "SUBSTRING", "SUBSTRING_REGEX", "SUCCEEDS", "SUM", "SYMMETRIC", "SYSID", "SYSTEM", "SYSTEM_TIME", "SYSTEM_USER", "TABLE",
                "TABLES", "TABLESAMPLE", "TABLESPACE", "TABLE_NAME", "TEMP", "TEMPLATE", "TEMPORARY", "TEXT", "THEN", "TIES", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TOKEN",
                "TOP_LEVEL_COUNT", "TRAILING", "TRANSACTION", "TRANSACTIONS_COMMITTED", "TRANSACTIONS_ROLLED_BACK", "TRANSACTION_ACTIVE", "TRANSFORM", "TRANSFORMS", "TRANSLATE", "TRANSLATE_REGEX", "TRANSLATION", "TREAT", "TRIGGER", "TRIGGER_CATALOG", "TRIGGER_NAME", "TRIGGER_SCHEMA",
                "TRIM", "TRIM_ARRAY", "TRUE", "TRUNCATE", "TRUSTED", "TYPE", "TYPES", "UESCAPE", "UNBOUNDED", "UNCOMMITTED", "UNDER", "UNENCRYPTED", "UNION", "UNIQUE", "UNKNOWN", "UNLINK",
                "UNLISTEN", "UNLOGGED", "UNNAMED", "UNNEST", "UNTIL", "UNTYPED", "UPDATE", "UPPER", "URI", "USAGE", "USER", "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_CODE", "USER_DEFINED_TYPE_NAME", "USER_DEFINED_TYPE_SCHEMA", "USING",
                "VACUUM", "VALID", "VALIDATE", "VALIDATOR", "VALUE", "VALUES", "VALUE_OF", "VARBINARY", "VARCHAR", "VARIADIC", "VARYING", "VAR_POP", "VAR_SAMP", "VERBOSE", "VERSION", "VERSIONING",
                "VIEW", "VIEWS", "VOLATILE", "WHEN", "WHENEVER", "WHERE", "WHITESPACE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "WORK", "WRAPPER", "WRITE", "XML",
                "XMLAGG", "XMLATTRIBUTES", "XMLBINARY", "XMLCAST", "XMLCOMMENT", "XMLCONCAT", "XMLDECLARATION", "XMLDOCUMENT", "XMLELEMENT", "XMLEXISTS", "XMLFOREST", "XMLITERATE", "XMLNAMESPACES", "XMLPARSE", "XMLPI", "XMLQUERY",
                "XMLROOT", "XMLSCHEMA", "XMLSERIALIZE", "XMLTABLE", "XMLTEXT", "XMLVALIDATE", "YEAR", "YES", "ZONE"
                )
        );        
*/        
        RESERVED_ORACLE.addAll(Arrays.asList(
                "ACCESS","ADD","ALL","ALTER","AND","ANY","AS","ASC","AUDIT","BETWEEN","BY","CHAR","CHECK","CLUSTER","COLUMN","COMMENT","COMPRESS","CONNECT","CREATE","CURRENT","DATE"
                ,"DECIMAL","DEFAULT","DELETE","DESC","DISTINCT","DROP","ELSE","EXCLUSIVE","EXISTS","FILE","FLOAT","FOR","FROM","GRANT","GROUP","HAVING","IDENTIFIED","IMMEDIATE","IN"
                ,"INCREMENT","INDEX","INITIAL","INSERT","INTEGER","INTERSECT","INTO","IS","LEVEL","LIKE","LOCK","LONG","MAXEXTENTS","MINUS","MLSLABEL","MODE","MODIFY","NOAUDIT"
                ,"NOCOMPRESS","NOT","NOWAIT","NULL","NUMBER","OF","OFFLINE","ON","ONLINE","OPTION","OR","ORDER","PCTFREE","PRIOR","PRIVILEGES","PUBLIC","RAW","RENAME","RESOURCE","REVOKE"
                ,"ROW","ROWID","ROWNUM","ROWS","SELECT","SESSION","SET","SHARE","SIZE","SMALLINT","START","SUCCESSFUL","SYNONYM","SYSDATE","TABLE","THEN","TO","TRIGGER","UID","UNION"
                ,"UNIQUE","UPDATE","USER","VALIDATE","VALUES","VARCHAR","VARCHAR2","VIEW","WHENEVER","WHERE","WITH"        
                )
        );
        RESERVED_ENTERPRISEDB.addAll(Arrays.asList(
                "ALL","ANALYSE","ANALYZE","AND","ANY","ARRAY","AS","ASC","ASYMMETRIC","BOTH","CASE","CAST","CHECK","COLLATE","COLUMN","CONNECT","CONNECT_BY_ROOT","CONSTRAINT","CONSTRUCTOR"
                ,"CREATE","CURRENT_CATALOG","CURRENT_ROLE","CURRENT_TIME","CURRENT_TIMESTAMP","CURRENT_USER","DEFAULT","DEFERRABLE","DESC","DISTINCT","DO","ELSE","END","EXCEPT","FALSE","FETCH"
                ,"FINAL","FOR","FOREIGN","FROM","GRANT","GROUP","HAVING","IN","INITIALLY","INSTANTIABLE","INTERSECT","INTO","LATERAL","LEADING","LIMIT","LOCALTIME","LOCALTIMESTAMP","MINUS"
                ,"NOT","NULL","OFFSET","ON","ONLY","OR","ORDER","OVERRIDING","PLACING","REFERENCES","REPEATABLE","RETURNING","SELECT","SESSION_USER","SOME","SYMMETRIC","SYS_CONNECT_BY_PATH","TABLE","THEN"
                ,"TO","TYPE","TRAILING","TRUE","UNION","UNIQUE","USING","VARIADIC","WHEN","WHERE","WINDOW","WITH"        
                )
        );
        RESERVED_POSTGRES.addAll(Arrays.asList(
                "ALL","ANALYSE","ANALYZE","AND","ANY","ARRAY","AS","ASC","ASYMMETRIC","BOTH","CASE","CAST","CHECK","COLLATE","COLUMN","CONNECT","CONNECT_BY_ROOT","CONSTRAINT","CONSTRUCTOR"
                ,"CREATE","CURRENT_CATALOG","CURRENT_ROLE","CURRENT_TIME","CURRENT_TIMESTAMP","CURRENT_USER","DEFAULT","DEFERRABLE","DESC","DISTINCT","DO","ELSE","END","EXCEPT","FALSE","FETCH"
                ,"FINAL","FOR","FOREIGN","FROM","GRANT","GROUP","HAVING","IN","INITIALLY","INSTANTIABLE","INTERSECT","INTO","LATERAL","LEADING","LIMIT","LOCALTIME","LOCALTIMESTAMP","MINUS"
                ,"NOT","NULL","OFFSET","ON","ONLY","OR","ORDER","OVERRIDING","PLACING","REFERENCES","REPEATABLE","RETURNING","SELECT","SESSION_USER","SOME","SYMMETRIC","SYS_CONNECT_BY_PATH","TABLE","THEN"
                ,"TO","TYPE","TRAILING","TRUE","UNION","UNIQUE","USING","VARIADIC","WHEN","WHERE","WINDOW","WITH"        
                )
        );
    }

    private String dbName = "";

    /**
     * Получить имя колонки в базе данных. Используется для генерации SQL
     * выражений.
     */
    @Override
    public String getDbName() {
        return dbName;
    }
    
    @Override
    public void setDbName(final String dbName) {
        if (dbName == null) {
            throw new IllegalArgumentException("Column name can't be null!");
        }
        else if (!Utils.equals(this.dbName, dbName)) {
            this.dbName = dbName;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    
    public boolean canUseAsDbName(final String dbName, final EDatabaseType dbType) {
        return testCanUseAsDbName(dbName, dbType);
    }
    
    public static boolean testCanUseAsDbName(final String dbName, final EDatabaseType dbType) {
        if (dbName == null) {
            throw new IllegalArgumentException("Name can't be null!");
        }
        else if (dbType == null) {
            throw new IllegalArgumentException("Database type to check can't be null!");
        }
        else if (!dbName.isEmpty()) {
            switch (dbType) {
                case ORACLE         : return  !RESERVED_ORACLE.contains(dbName.toUpperCase());
                case ENTERPRISEDB   : return  !RESERVED_ENTERPRISEDB.contains(dbName.toUpperCase());
                case POSTGRESQL     : return  !RESERVED_POSTGRES.contains(dbName.toUpperCase());
                default : throw new UnsupportedOperationException("Checking for database type ["+dbType+"] is not supported yet");
            }
        }
        else {
            return false;
        }
    }
    
    
    private boolean autoDbName = true;

    @Override
    public boolean isAutoDbName() {
        return autoDbName;
    }

    public void setAutoDbName(boolean autoDbName) {
        if (this.autoDbName != autoDbName) {
            this.autoDbName = autoDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean notNull = false;

    /**
     * Является-ли значение колонки обязательным (т.е. не может содержать null).
     */
    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        if (!Utils.equals(this.notNull, notNull)) {
            this.notNull = notNull;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Входит-ли колонка в первичный ключ. Подсчитывается через наличие ссылки
     * на колонку в первичном ключе таблицы.
     */
    public boolean isPrimaryKey() {
        final DdsPrimaryKeyDef pk = getOwnerTable().getPrimaryKey();
        for (DdsIndexDef.ColumnInfo columnInfo : pk.getColumnsInfo()) {
            if (columnInfo.getColumnId().equals(this.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void check(Tag source, IProblemHandler problemHandler) {
        //do nothing
    }

    /**
     * Информация об аудите {@link DdsTableDef таблицы}. См. rx_audit.doc.
     */
    public class AuditInfo {

        private final DdsAuditMask auditMask;
        private boolean onUpdate = false;

        public AuditInfo() {
            this.auditMask = new DdsAuditMask(DdsColumnDef.this);
        }

        protected AuditInfo(org.radixware.schemas.ddsdef.Column xColumn) {
            super();

            this.auditMask = new DdsAuditMask(DdsColumnDef.this);

            if (xColumn.isSetAuditMask()) {
                this.auditMask.loadFrom(xColumn.getAuditMask());
            }

            if (xColumn.isSetAuditOnUpdate()) {
                this.onUpdate = xColumn.getAuditOnUpdate();
            } else {
                this.onUpdate = auditMask.isUpdate();
            }
        }

        public void appendTo(org.radixware.schemas.ddsdef.Column xColumn) {
            if (auditMask.toLong() != 0) {
                xColumn.setAuditMask(auditMask.toLong());
            }
            if (onUpdate != auditMask.isUpdate()) {
                xColumn.setAuditOnUpdate(onUpdate);
            }
        }

        public boolean isSaveValueOnInsert() {
            return auditMask.isInsert();
        }

        public void setSaveValueOnInsert(boolean saveValue) {
            auditMask.setInsert(saveValue);
        }

        public boolean isSaveValuesOnUpdate() {
            return auditMask.isUpdate();
        }

        public void setSaveValuesOnUpdate(boolean saveValues) {
            auditMask.setUpdate(saveValues);
        }

        public boolean isSaveValueOnDelete() {
            return auditMask.isDelete();
        }

        public void setSaveValueOnDelete(boolean saveValue) {
            auditMask.setDelete(saveValue);
        }

        public boolean isOnUpdate() {
            return onUpdate;
        }

        public void setOnUpdate(boolean onUpdate) {
            if (this.onUpdate != onUpdate) {
                this.onUpdate = onUpdate;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isSaveValues() {
            return auditMask.isEnabled();
        }
    }
    private final AuditInfo auditInfo;

    /**
     * Получить информацию об аудите таблицы.
     */
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }
    private final Object expressionLock = new Object();
    private DdsSqml expression = null;

    /**
     * Получить SQML выражение, по которому строится SQL скрипт для получения
     * значения виртуальной колонки.
     *
     * @return SQML выражение, или null, если колонка не виртуальная.
     */
    @Override
    public Sqml getExpression() {
        return expression;
    }

    public boolean isExpression() {
        return expression != null;
    }

    /**
     * Create new expression for the column
     *
     * @return new SQML instance, attached to the column.
     */
    public Sqml createNewExpression() {
        synchronized (expressionLock) {
            if (this.expression != null) {
                this.expression.getItems().clear();
            } else {
                this.expression = new DdsSqml(this);
            }
            return this.expression;
        }
    }

    public void removeExpression() {
        synchronized (expressionLock) {
            if (this.expression != null) {
                this.expression.unsetOwner();
                this.expression = null;
            }
        }
    }
    private Id sequenceId = null;

    /**
     * Получить указатель на {@link DdsSequenceDef последовательность} к
     * которому привязана колонка. Используется на более высоких уровнях для
     * автоматического получения значений колонки в добавляемой записи.
     */
    public Id getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Id sequenceId) {
        if (!Utils.equals(this.sequenceId, sequenceId)) {
            this.sequenceId = sequenceId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find {@link DdsSequenceDef} of the column.
     *
     * @return sequence or null it not found.
     */
    public DdsSequenceDef findSequence() {
        final DdsModule module = getModule();
        if (module != null) {
            return module.getDdsSequenceSearcher().findById(getSequenceId()).get();
        } else {
            return null;
        }
    }

    /**
     * Find {@link DdsSequenceDef}.
     *
     * @throws DefinitionNotFoundError
     */
    public DdsSequenceDef getSequence() {
        DdsSequenceDef sequence = findSequence();
        if (sequence != null) {
            return sequence;
        }
        throw new DefinitionNotFoundError(getSequenceId());
    }
    boolean generatedInDb = true;

    @Override
    public boolean isGeneratedInDb() {
        if (!generatedInDb) {
            return false;
        }
        final DdsTableDef ownerTable = getOwnerTable();
        return ownerTable.isGeneratedInDb();
    }

    public void setGeneratedInDb(boolean generatedInDb) {
        if (this.generatedInDb != generatedInDb) {
            this.generatedInDb = generatedInDb;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean hidden = false;

    /**
     * Должна-ли колонка показываться пользователю. Используется на более
     * высоких уровнях, когда требуется скрыть от пользователя некоторые колонки
     * в списках в целях дополнительной безопасности.
     */
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        if (this.hidden != hidden) {
            this.hidden = hidden;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final List<ValAsStr> initialValues = new ArrayList<ValAsStr>();

    /**
     * Получить список начальных значений колонки в таблице. Используется при
     * генерации скрипта создания и изменения базы данных. Используйте
     * RadixWareValueConverter для преобразования в конечное значение. См. также
     * {@link #getValType()}. Размеры списков начальных значений всех колонок
     * {@link DdsTableDef таблицы} должны совпадать, за исключением, если список
     * пуст, это означает, что для данной колонки не заданы начальные значения,
     * т.е. всегда используется значение по умолчанию.
     *
     * @return список начальных значений.
     */
    public List<ValAsStr> getInitialValues() {
        return initialValues;
    }
    private DdsCheckConstraintDef checkConstraint = null;

    /**
     * Получить метаинформацию об ограничении, накладываемом на значение колонки
     * в базу данных.
     *
     * @return check constraint or null if not defined.
     */
    public DdsCheckConstraintDef getCheckConstraint() {
        return checkConstraint;
    }

    public void setCheckConstraint(DdsCheckConstraintDef checkConstraint) {
        if (this.checkConstraint == checkConstraint) {
            return;
        }
        if (this.checkConstraint != null) {
            this.checkConstraint.setOwnerColumn(null);
        }
        this.checkConstraint = checkConstraint;
        if (checkConstraint != null) {
            checkConstraint.setOwnerColumn(this);
        }
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public RadixIcon getIcon() {
        final EValType curValType = getValType();
        if (curValType != null) {
            return RadixObjectIcon.getForValType(curValType);
        } else {
            return RadixObjectIcon.UNKNOWN;
        }
    }

    protected DdsColumnDef(final String name) {
        super(EDefinitionIdPrefix.DDS_COLUMN, name);
        this.auditInfo = new AuditInfo();
    }

    protected DdsColumnDef(org.radixware.schemas.ddsdef.Column xColumn) {
        super(xColumn);

        if (xColumn.isSetGenerateInDb()) {
            this.generatedInDb = xColumn.getGenerateInDb();
        }

        this.dbName = xColumn.getDbName();

        this.notNull = xColumn.getNotNull();

        if (xColumn.isSetAutoDbName()) {
            this.autoDbName = xColumn.getAutoDbName();
        }

        if (xColumn.isSetSequenceId()) {
            this.sequenceId = Id.Factory.loadFrom(xColumn.getSequenceId());
        }

        if (xColumn.isSetTemplateId()) {
            this.templateId = Id.Factory.loadFrom(xColumn.getTemplateId());
        }

        if (xColumn.isSetCheckConstraint()) {
            this.checkConstraint = DdsCheckConstraintDef.Factory.loadFrom(xColumn.getCheckConstraint());
            this.checkConstraint.setOwnerColumn(this);
        }

        this.auditInfo = new AuditInfo(xColumn);

        if (xColumn.isSetHidden()) {
            this.hidden = xColumn.getHidden();
        }

        if (xColumn.isSetInitialValues()) {
            final List<String> xInitialValues = xColumn.getInitialValues().getValueList();
            for (String initialValue : xInitialValues) {
                // null всегда выгружается в виде пустой строки, 
                // чтобы обеспечить одинаковое количество записей начальных значений всех колонок таблицы.
                if (initialValue != null && !initialValue.isEmpty()) {
                    this.initialValues.add(ValAsStr.Factory.loadFrom(initialValue));
                } else {
                    this.initialValues.add(null);
                }
            }
        }

        if (xColumn.isSetDefaultVal()) {
            this.defaultValue = RadixDefaultValue.Factory.loadFrom(xColumn.getDefaultVal());
        }

        if (xColumn.isSetExpression()) {
            createNewExpression().loadFrom(xColumn.getExpression());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("; DbName: ");
        result.append(dbName);
        result.append("; GeneratedInDb: ");
        result.append(generatedInDb);
        result.append("; NotNull: ");
        result.append(notNull);
        result.append("; DefaultValue: ");
        result.append(defaultValue);

        return result.toString();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (checkConstraint != null) {
            checkConstraint.visit(visitor, provider);
        }

        Sqml thisExpression = expression;
        if (thisExpression != null) {
            thisExpression.visit(visitor, provider);
        }
    }

    @Override
    public DdsTableDef getOwnerTable() {
        return (DdsTableDef) getOwnerDefinition();
    }

    @Override
    public DdsTableDef findOwnerTable() {
        return (DdsTableDef) getOwnerDefinition();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsColumnDef newInstance(final String name) {
            return new DdsColumnDef(name);
        }

        public static DdsColumnDef loadFrom(org.radixware.schemas.ddsdef.Column xColumn) {
            return new DdsColumnDef(xColumn);
        }
    }
    private Id templateId = null;

    /**
     * Get {@link DdsColumnTemplateDef} identifier.
     */
    public Id getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Id templateId) {
        if (!Utils.equals(this.templateId, templateId)) {
            this.templateId = templateId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find {@link DdsColumnTemplateDef}.
     *
     * @return column template or null it not found.
     */
    public DdsColumnTemplateDef findTemplate() {
        final DdsModule module = getModule();
        if (module != null) {
            return module.getDdsColumnTemplateSearcher().findById(getTemplateId()).get();
        } else {
            return null;
        }
    }

    /**
     * Find {@link DdsColumnTemplateDef}.
     *
     * @throws DefinitionNotFoundError
     */
    public DdsColumnTemplateDef getTemplate() {
        DdsColumnTemplateDef template = findTemplate();
        if (template != null) {
            return template;
        }
        throw new DefinitionNotFoundError(getTemplateId());
    }
    private RadixDefaultValue defaultValue = null;

    /**
     * Получить начальное значение колонки в базе данных. Подставляется при
     * добавлении записи в таблицу, если значение колонки не указано явным
     * образом.
     *
     * @return начальное значение колонки в базе данных, в т.ч. null.
     */
    public RadixDefaultValue getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(RadixDefaultValue defaultValue) {
        if (!Utils.equals(this.defaultValue, defaultValue)) {
            this.defaultValue = defaultValue;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String calcAutoDbName() {
        String name = getName();
        return DbNameUtils.calcAutoDbName(name);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsSequenceDef sequence = findSequence();
        if (sequence != null) {
            list.add(sequence);
        }
        DdsColumnTemplateDef template = findTemplate();
        if (template != null) {
            list.add(template);
        }
    }

    private class DdsColumnClipboardSupport extends DdsClipboardSupport<DdsColumnDef> {

        public DdsColumnClipboardSupport() {
            super(DdsColumnDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Column xColumn = org.radixware.schemas.ddsdef.Column.Factory.newInstance();
            DdsModelWriter.writeColumn(DdsColumnDef.this, xColumn);
            return xColumn;
        }

        @Override
        protected DdsColumnDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Column xColumn = (org.radixware.schemas.ddsdef.Column) xmlObject;
            return DdsColumnDef.Factory.loadFrom(xColumn);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Column.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsColumnDef> getClipboardSupport() {
        return new DdsColumnClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        final List<String> attributes = new ArrayList<String>();
        if (isPrimaryKey()) {
            attributes.add("Primary key");
        }
        if (isNotNull()) {
            attributes.add("Not null");
        }
        if (!isGeneratedInDb()) {
            attributes.add("Virtual");
        }
        if (getExpression() != null) {
            attributes.add("Expression");
        }
        if (isHidden()) {
            attributes.add("Hidden");
        }

        if (!attributes.isEmpty()) {
            sb.append("<br>Attributes: ");
            boolean added = false;
            for (String attribute : attributes) {
                if (added) {
                    sb.append(", ");
                } else {
                    added = true;
                }
                sb.append(attribute);
            }
        }

        super.appendAdditionalToolTip(sb);
    }

    /**
     * @return true if this column used in one of outgoing references of this
     * table. It is not recommended to use this function frequently, because
     * gathering of outgoing references required some time.
     */
    public boolean isForeignKey() {
        final DdsTableDef table = getOwnerTable();
        for (DdsReferenceDef reference : table.collectOutgoingReferences()) {
            for (DdsReferenceDef.ColumnsInfoItem item : reference.getColumnsInfo()) {
                if (Utils.equals(item.getChildColumnId(), getId())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static final String COLUMN_TYPE_TITLE = "Column";
    public static final String COLUMN_TYPES_TITLE = "Columns";

    @Override
    public String getTypeTitle() {
        return COLUMN_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return COLUMN_TYPES_TITLE;
    }
}
