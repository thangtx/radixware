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

package org.radixware.kernel.designer.dds.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;

public class DdsScriptInternalUtils {
    public static final int LEV_DELETE = 1;
    public static final int LEV_REPLACE = 2;
    public static final int LEV_INSERT = 3;
    public static final int LEV_NONE = 4;

    /**
     * <p>return the first value if not null otherwise the second one</p>
     * @param <T>
     * @param value value to return when is not null
     * @param defaultValue value to return otherwise
     * @return value returned
     */
    public static <T> T nvl(final T value, final T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * <p>Select and build column list for the given table</p>
     * @param table table to build column list for
     * @param alwaysCreateInDb ignore 'create in database' flag
     * @return column list built
     */
    public static String selectColumnList(final DdsTableDef table, final boolean alwaysCreateInDb) {
        if (table == null) {
            throw new IllegalArgumentException("Table can't be null");
        }
        else {
            final StringBuilder sb = new StringBuilder();

            String prefix = "";
            for (DdsColumnDef column : table.getColumns().getLocal()) {
                if (column.isGeneratedInDb() || alwaysCreateInDb) {
                    sb.append(prefix).append(column.getDbName());
                }
                prefix = ",";
            }
            return sb.toString();
        }
    }

    /**
     * <p>Substitute string content with he values by it's keys</p>
     * @param source string to substitute to
     * @param pairs key/value pairs. Even amount needs!
     * @return substituted string
     */
    public static String substitute(final String source, final String... pairs) {
        if (source == null) {
            throw new IllegalArgumentException("Source string can't be null");
        }
        else if (pairs == null) {
            throw new IllegalArgumentException("Pair list can't be null");
        }
        else if (pairs.length %2 != 0) {
            throw new IllegalArgumentException("Odd amount of the pairs argument. Need be key/value pairs only");
        }
        else {
            final Properties props = new Properties();
            
            for (int index = 0; index < pairs.length; index += 2) {
                if (pairs[index] == null || pairs[index].isEmpty()) {
                    throw new IllegalArgumentException("The "+index+"-th argument in the pairs list is null or empty!");
                }
                else if (pairs[index+1] == null) {
                    throw new IllegalArgumentException("The "+(index+1)+"-th argument in the pairs list is null!");
                }
                else {
                    props.setProperty(pairs[index],pairs[index+1]);
                }
            }
            return substitute(source,props);
        }
    }

    /**
     * <p>Substitute string content with he values by it's keys</p>
     * @param source string to substitute to
     * @param pairs key/value pairs
     * @return substituted string
     */
    public static String substitute(final String source, final Properties pairs) {
        if (source == null) {
            throw new IllegalArgumentException("Source string can't be null");
        }
        else if (pairs == null) {
            throw new IllegalArgumentException("Pairs can't bew null");
        }
        else if (source.indexOf("${") == -1) {
            return source;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            final int len = source.length();
            int from = 0, to, end;
            
            while ((to = source.indexOf("${",from)) >= 0) {
                if ((end = source.indexOf('}',to+2)) == -1) {
                    throw new IllegalArgumentException("Source string ["+source+"], position "+to+": unpaired bracket '${'");
                }
                else {
                    sb.append(source,from,to).append(pairs.getProperty(source.substring(to+2,end)));
                    from = end + 1;
                }
            }
            if (from < len) {
                sb.append(source,from,len);
            }
            return sb.toString();
        }
    }

    /**
     * <p>Escape fields names that intersects with the database keywords
     * @param source source field to escape
     * @return field escaped
     */
    public static String escapeDbName(final String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source string can't be null");
        }
        else {
            switch (source.toUpperCase()) {
                case "TYPE" : case "OFFSET" : case "REPEATABLE" : return '\"'+source+'\"';
                default : return source;
            }
        }
    }
    
    /**
     * <p>Exclude 'char' / 'byte' descriptions from field lengths</p>
     * @param source field type
     * @return field type converted
     */
    public static String convertFieldDbType(final String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source string can't be null");
        }
        else if (source.startsWith("integer(")) {
            return "integer";
        }
        else if (source.endsWith(" char)")) {
            return source.replace(" char)",")");
        }
        else if (source.endsWith(" byte)")) {
            return source.replace(" byte)",")");
        }
        else {
            return source;
        }
    }

    /**
     * <p>Exclude 'char' / 'byte' descriptions from field lengths</p>
     * @param source field type
     * @return field type converted
     */
    public static String convertFieldDbType4ProcHeaders(final String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source string can't be null");
        }
        else if (source.startsWith("integer")) {
            return "bigint";
        }
        else if (source.endsWith(" char)")) {
            return source.replace(" char)",")");
        }
        else if (source.endsWith(" byte)")) {
            return source.replace(" byte)",")");
        }
        else {
            return source;
        }
    }
    
    /**
     * <p>Sequential number generator to identify sequential number of the item in the module</p>
     * @param def definition to define it's sequential number
     * @return sequential number
     */
    public static int getSequentialNumber4TheDefinitionInTheModule(final DdsTypeDef def) {
        final DdsModule module = def.getModule();
        final int[] value = new int[]{-1,-1};
        
        module.visitAll(new IVisitor() {
                @Override
                public void accept(final RadixObject radixObject) {
                    value[0]++;
                    if (radixObject == def) {
                        value[1] = value[0];
                    }
                }
            }
        );
        return value[1];
    }
    
    public static class Prescription {
	public int[][] route;
	public int distance;
        
	Prescription(int distance, int[][] route) {
            this.distance = distance;
            this.route = route;
	}

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Prescription(dist="+distance+") :");
     
            for (int index = 0; index < route.length; index++) {
                sb.append('\n').append(index).append(" : ");
                switch (route[index][0]) {
                    case LEV_DELETE : sb.append("delete "); break;
                    case LEV_REPLACE : sb.append("replace "); break;
                    case LEV_INSERT : sb.append("insert "); break;
                    case LEV_NONE : sb.append("not changed "); break;
                }
                sb.append(' ').append(route[index][1]).append(" and ").append(route[index][2]);
            }
            return sb.toString();
        }
    }

    /**
     * <p>Calculate edition prescriptions (see https://ru.wikibooks.org/wiki/%D0%A0%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8_%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%BE%D0%B2/%D0%A0%D0%B5%D0%B4%D0%B0%D0%BA%D1%86%D0%B8%D0%BE%D0%BD%D0%BD%D0%BE%D0%B5_%D0%BF%D1%80%D0%B5%D0%B4%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5 )
     * @param str1 source string array
     * @param str2 target string array
     * @return 
     */
    public static Prescription calcLevenstain(final String[] str1, final String[] str2) {
	final int m = str1.length, n = str2.length;
	final int[][] D = new int[m + 1][n + 1];
	final char[][] P = new char[m + 1][n + 1];

	for (int i = 0; i <= m; i++) {
            D[i][0] = i;
            P[i][0] = 'D';
	}
	for (int i = 0; i <= n; i++) {
		D[0][i] = i;
		P[0][i] = 'I';
	}

	for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                final int cost = !str1[i - 1].equals(str2[j - 1]) ? 1 : 0;

                if(D[i][j - 1] < D[i - 1][j] && D[i][j - 1] < D[i - 1][j - 1] + cost) {
                    D[i][j] = D[i][j - 1] + 1;
                    P[i][j] = 'I';
                }
                else if(D[i - 1][j] < D[i - 1][j - 1] + cost) {
                    D[i][j] = D[i - 1][j] + 1;
                    P[i][j] = 'D';
                }
                else {
                    D[i][j] = D[i - 1][j - 1] + cost;
                    P[i][j] = (cost == 1) ? 'R' : 'M';
                }
            }
        }

        final List<int[]> opers = new ArrayList<>();
	int i = m, j = n;
        
	do {char c = P[i][j];
        
            if(c == 'R' || c == 'M') {
                opers.add(0,new int[]{c == 'M' ? LEV_NONE : LEV_REPLACE,i,j});
                i --;
                j --;
            }
            else if(c == 'D') {
                opers.add(0,new int[]{LEV_DELETE,i,j});
                i --;
            }
            else {
                opers.add(0,new int[]{LEV_INSERT,i,j});
                j --;
            }
	} while((i != 0) || (j != 0));
        
	return new Prescription(D[m][n], opers.toArray(new int[opers.size()][]));
    }
}
