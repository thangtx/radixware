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

package org.radixware.kernel.common.defs.dds.utils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.utils.RegExpUtils;
import org.radixware.kernel.common.utils.RegExpUtils.AnalysisNamesResult;

/**
 * Utilities for db names.
 *
 */
public class DbNameUtils {

    private static final List<String> sortedReservedWords = Collections.unmodifiableList(Arrays.asList(
            "ACCESS",
            "ACSMETA",
            "ADD",
            "ALL",
            "ALTER",
            "AND",
            "ANY",
            "AS",
            "ASC",
            "AUDIT",
            "BETWEEN",
            "BY",
            "CHAR",
            "CHECK",
            "CLUSTER",
            "COLUMN",
            "COMMENT",
            "COMPRESS",
            "CONNECT",
            "CREATE",
            "CURRENT",
            "DATE",
            "DECIMAL",
            "DEFAULT",
            "DELETE",
            "DESC",
            "DISTINCT",
            "DROP",
            "ELSE",
            "EXCLUSIVE",
            "EXISTS",
            "FILE",
            "FLOAT",
            "FOR",
            "FROM",
            "GRANT",
            "GROUP",
            "HAVING",
            "IDENTIFIED",
            "IMMEDIATE",
            "IN",
            "INCREMENT",
            "INDEX",
            "INITIAL",
            "INSERT",
            "INTEGER",
            "INTERSECT",
            "INTO",
            "IS",
            "LEVEL",
            "LIKE",
            "LOCK",
            "LONG",
            "MAXEXTENTS",
            "MINUS",
            "MLSLABEL",
            "MODE",
            "MODIFY",
            "NOAUDIT",
            "NOCOMPRESS",
            "NOT",
            "NOWAIT",
            "NULL",
            "NUMBER",
            "OF",
            "OFFLINE",
            "ON",
            "ONLINE",
            "OPTION",
            "OR",
            "ORDER",
            "PCTFREE",
            "PRIOR",
            "PRIVILEGES",
            "PUBLIC",
            "RAW",
            "RENAME",
            "RESOURCE",
            "REVOKE",
            "ROW",
            "ROWID",
            "ROWNUM",
            "ROWS",
            "SELECT",
            "SESSION",
            "SET",
            "SHARE",
            "SIZE",
            "SMALLINT",
            "START",
            "SUCCESSFUL",
            "SYNONYM",
            "SYSDATE",
            "TABLE",
            "THEN",
            "TO",
            "TRIGGER",
            "UID",
            "UNION",
            "UNIQUE",
            "UPDATE",
            "USER",
            "VALIDATE",
            "VALUES",
            "VARCHAR",
            "VARCHAR2",
            "VIEW",
            "WHENEVER",
            "WHERE",
            "WITH"));

    public static boolean isCorrectDbName(String dbName) {
        if (dbName == null) {
            return false;
        }
        if (!dbName.matches("\\\"?[a-zA-Z][a-zA-Z0-9_\\%\\$]{0,29}\\\"?")) {
            return false;
        }
        if (Collections.binarySearch(sortedReservedWords, dbName.toUpperCase()) >= 0) {
            return false;
        }
        return true;
    }

    public static String calcAutoDbName(String... parts) {
        final StringBuilder sb = new StringBuilder();
        boolean wasAdded = false;
        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                if (!wasAdded) {
                    wasAdded = true;
                } else {
                    sb.append('_');
                }
                sb.append(part);
            }
        }

        String result = sb.toString();
        result = result.replace(' ', '_');
        if (result.length() > 30) {
            result = result.substring(0, 30);
        }
        result = result.toUpperCase();
        return result;
    }

    private static class Updater implements IVisitor {

        @Override
        public void accept(RadixObject object) {
            if (object instanceof IDdsAutoDbNamedDefinition) {
                final IDdsAutoDbNamedDefinition autoDbNamedDefinition = (IDdsAutoDbNamedDefinition) object;
                if (autoDbNamedDefinition.isAutoDbName()) {
                    final String dbName = autoDbNamedDefinition.calcAutoDbName();
                    autoDbNamedDefinition.setDbName(dbName);
                }
            }
        }
    }

    /**
     * Update auto db names in all objects of the specified context
     *
     * @param scope
     */
    public static void updateAutoDbNames(RadixObject context) {
        context.visit(new Updater(), DdsVisitorProviderFactory.newDdsDefinitionVisitorProvider());
    }

    /**
     * Generation DdsModel prefix-based restriction.
     */
    private static String generateDefaultRestrictionTest(Layer layer, boolean caseSensitive) {
        if (layer == null) {
            return null;
        }

        final Set<String> prefs = new HashSet<>();

        layer.visit(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                final String dbName = ((DdsModelDef) radixObject).getDbAttributes().getDbNamePrefix();
                if (dbName != null && !dbName.isEmpty()) {
                    prefs.add(dbName);
                }
            }
        }, new DdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof DdsModelDef;
            }
        });

        final List<String> sortedNames = new ArrayList<>(prefs);
        Collections.sort(sortedNames);

        if (prefs.isEmpty()) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(caseSensitive ? "(?-i)" : "(?i)");
        builder.append("(?!(.*_(");

        boolean first = true;
        for (String pref : sortedNames) {
            if (first) {
                first = false;
            } else {
                builder.append("|");
            }
            builder.append(pref);
        }

        builder.append(")_.*)).*");
        return builder.toString();
    }

    /**
     * Generation definition names prefix-based restriction.
     */
    public static String generateDefaultRestriction(Layer layer, boolean caseSensitive) {

        if (layer == null) {
            return null;
        }

        final Set<String> names = new HashSet<>();

        layer.visit(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                final String dbName = ((IDdsDbDefinition) radixObject).getDbName();
                if (dbName != null && !dbName.isEmpty()) {
                    names.add(dbName);
                }
            }
        }, new DdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return isCheckable(radixObject);
            }
        });

        final List<String> sortedNames = new ArrayList<>(names);
        Collections.sort(sortedNames);

        if (names.isEmpty()) {
            return "";
        }

        final AnalysisNamesResult result = RegExpUtils.analyseNames(
                sortedNames, Arrays.asList("CKC_", "FK_", "IDX_", "PK_", "SQN_", "TADR_", "TAI", "UNQ_", "TAUR_", "TBDR_", "DB_"), 3, 2);

        final StringBuilder builder = new StringBuilder();
        builder.append(caseSensitive ? "(?-i)" : "(?i)");
        builder.append("(?!");
        builder.append(result.getExpress());
        builder.append(").*");
        return builder.toString();
    }

    /**
     * Indicates whether the object has checkable database name.
     */
    public static boolean isCheckable(RadixObject radixObject) {
        if (radixObject instanceof IDdsDbDefinition) {

            if (!((IDdsDbDefinition) radixObject).isGeneratedInDb()) {
                return false;
            }

            if (radixObject instanceof DdsColumnDef
                    || radixObject instanceof DdsParameterDef
                    || radixObject instanceof DdsTypeFieldDef
                    || radixObject instanceof DdsExtTableDef
                    || radixObject instanceof ISqmlProperty
                    || radixObject instanceof DdsFunctionDef) {
                return false;
            }

            if (radixObject instanceof IOverwritable && ((IOverwritable) radixObject).isOverwrite()) {
                return false;
            }

            if (radixObject instanceof DdsTriggerDef) {
                return ((DdsTriggerDef) radixObject).getType() == DdsTriggerDef.EType.NONE;
            }
            return true;
        }
        return false;
    }

    public static CheckResult checkRestriction(final Layer layer, final String name) {

        if (layer == null || name == null || name.isEmpty()) {
            return new CheckResult(false, null, layer);
        }

        final Layer result = Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Layer>() {

            @Override
            public void accept(HierarchyWalker.Controller<Layer> controller, Layer radixObject) {

                final String restriction = radixObject.getDbObjectNamesRestriction();
                if (restriction != null && !restriction.isEmpty()) {

                    Pattern pattern;
                    try {
                        pattern = Pattern.compile(restriction);
                    } catch (PatternSyntaxException e) {
                        Logger.getLogger(DbNameUtils.class.getName()).log(Level.WARNING, "Incorrect regexp restriction.");
                        return;
                    }

                    if (!pattern.matcher(name).matches()) {
                        if (layer != radixObject) {
                            controller.setResultAndStop(radixObject);
                        }
                    } else if (layer == radixObject) {
                        controller.setResultAndStop(radixObject);
                    }
                }
            }
        });

        return new CheckResult(result == null, result, layer);
    }

    public static class CheckResult {

        public final boolean valid;
        public final Layer layer;
        public final String message;

        public CheckResult(boolean valid, Layer layer, Layer root) {
            this.valid = valid;
            this.layer = layer;

            if (!valid) {

                if (layer == root) {
                    message = "The name of database object can not match the restriction of its layer";
                } else if (layer != null) {
                    message = "The name of database object does't match the restriction of layer " + layer.getName();
                } else {
                    message = "Invalid database name";
                }
            } else {
                message = "";
            }
        }
    }

}
