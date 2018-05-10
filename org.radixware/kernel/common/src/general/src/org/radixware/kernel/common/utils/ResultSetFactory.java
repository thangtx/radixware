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
package org.radixware.kernel.common.utils;

/**
 * <p>
 * This class produces a full-functional fake ResultSet, based on the different
 * table-styled content. See test for the class to understand use case</p>
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetFactory {

    private ResultSetFactory() {
    }

    interface IContentKeeper {

        int getColumnCount();

        int getRowCount();

        Object getCell(int row, int col);
    }

    /**
     * <p> Build result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @return full-functional result set. Column names for the given result set
     * will be the first row content from the input stream
     * @throws SQLException
     */
    public static ResultSet buildResultSet(final ResultSetContentType type, final InputStream content) throws SQLException {
        return buildResultSet(type, content, true, new String[0]);
    }

    /**
     * <p> Build scrollable result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @return full-functional result set. Column names for the given result set
     * will be the first row content from the input stream
     * @throws SQLException
     */
    public static ResultSet buildScrollableResultSet(final ResultSetContentType type, final InputStream content) throws SQLException {
        return buildScrollableResultSet(type, content, true, new String[0]);
    }
    
    /**
     * <p> Build result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param useFirstRowAsColumnNames use the first row from data as column
     * @return full-functional result set. Column names for the given result set
     * will be the first row content from the input stream
     * @throws SQLException
     */
    public static ResultSet buildResultSet(final ResultSetContentType type, final InputStream content, final boolean useFirstRowAsColumnNames) throws SQLException {
        return buildResultSet(type, content, useFirstRowAsColumnNames, new String[0]);
    }

    /**
     * <p> Build scrollable result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param useFirstRowAsColumnNames use the first row from data as column
     * @return full-functional result set. Column names for the given result set
     * will be the first row content from the input stream
     * @throws SQLException
     */
    public static ResultSet buildScrollableResultSet(final ResultSetContentType type, final InputStream content, final boolean useFirstRowAsColumnNames) throws SQLException {
        return buildScrollableResultSet(type, content, useFirstRowAsColumnNames, new String[0]);
    }
    
    /**
     * <p> Build result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param columnNames column names to use with the content. The first row of
     * the content from the input stream will be used as data, not column names
     * @return full-functional result set.
     * @throws SQLException
     */
    public static ResultSet buildResultSet(final ResultSetContentType type, final InputStream content, final String... columnNames) throws SQLException {
        return buildResultSet(type, content, false, columnNames);
    }

    /**
     * <p> Build scrollable result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param columnNames column names to use with the content. The first row of
     * the content from the input stream will be used as data, not column names
     * @return full-functional result set.
     * @throws SQLException
     */
    public static ResultSet buildScrollableResultSet(final ResultSetContentType type, final InputStream content, final String... columnNames) throws SQLException {
        return buildScrollableResultSet(type, content, false, columnNames);
    }
    
    /**
     * <p> Build result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param useFirstRowAsColumnNames use the first row from data as column names
     * @param columnNames column names to use with the content. Empty list defaults all
     * @return full-functional result set.
     * @throws SQLException
     */
    public static ResultSet buildResultSet(final ResultSetContentType type, final InputStream content, final boolean useFirstRowAsColumnNames, final String... columnNames) throws SQLException {
        return buildResultSet(type, content, new HashMap<ResultSetOptions,Object>(){{put(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES,useFirstRowAsColumnNames)
                                                                                   ; put(ResultSetOptions.RSO_COLUMN_NAMES, columnNames);}});
    }

    /**
     * <p> Build scrollable result set for the given content type</p>
     *
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param useFirstRowAsColumnNames use the first row from data as column names
     * @param columnNames column names to use with the content. Empty list defaults all
     * @return full-functional result set.
     * @throws SQLException
     */
    public static ResultSet buildScrollableResultSet(final ResultSetContentType type, final InputStream content, final boolean useFirstRowAsColumnNames, final String... columnNames) throws SQLException {
        return buildScrollableResultSet(type, content, new HashMap<ResultSetOptions,Object>(){{put(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES,useFirstRowAsColumnNames)
                                                                                             ; put(ResultSetOptions.RSO_COLUMN_NAMES, columnNames);}});
    }


    /**
     * <p> Build result set for the given content type</p>
     * 
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param options options for the given steam (see {@link ResultSetOptions})
     * @return full-functional result set.
     * @throws SQLException 
     */
    public static ResultSet buildResultSet(final ResultSetContentType type, final InputStream content, final Map<ResultSetOptions,Object> options) throws SQLException {
        boolean     useFirstRowAsColumnNames;
        String[]    columnNames;
        
        if (type == null) {
            throw new IllegalArgumentException("Content type can't be null");
        } else if (content == null) {
            throw new IllegalArgumentException("Content stream can't be null");
        } else if (options == null) {
            throw new IllegalArgumentException("Options can't be null");
        } else if (options.containsKey(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES) && !(options.get(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES) instanceof Boolean)) {
            throw new IllegalArgumentException("[" + ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES+"] option in the options map need not be null and need have Boolean type");
        } else if (options.containsKey(ResultSetOptions.RSO_COLUMN_NAMES) && !(options.get(ResultSetOptions.RSO_COLUMN_NAMES) instanceof String[])) {
            throw new IllegalArgumentException("[" + ResultSetOptions.RSO_COLUMN_NAMES+"] option in the options map need not be null and need have String[] type");
        } else if (options.containsKey(ResultSetOptions.RSO_LOB_PROVIDER) && !(options.get(ResultSetOptions.RSO_LOB_PROVIDER) instanceof IResultSetLobProvider)) {
            throw new IllegalArgumentException("[" + ResultSetOptions.RSO_LOB_PROVIDER+"] option in the options mapneed not be null and need have IResultSetLobProvider type");
        } else {
            useFirstRowAsColumnNames = options.containsKey(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES) ? ((Boolean)options.get(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES)).booleanValue() : false;
            columnNames = options.containsKey(ResultSetOptions.RSO_COLUMN_NAMES) ? (String[])options.get(ResultSetOptions.RSO_COLUMN_NAMES) : new String[0];
            
            if (!useFirstRowAsColumnNames && columnNames.length == 0) {
                throw new IllegalArgumentException("Neither useFirstRowAsColumnNames=true nor columnNames present. Can't define column names for the ResultSet!");
            }
            
            switch (type) {
                case XLS:
                    try {
                        final IContentKeeper ck = new XLSContentKeeper(content);
                        return new ExcelResultSet(columnNames, ck, useFirstRowAsColumnNames, (IResultSetLobProvider)options.get(ResultSetOptions.RSO_LOB_PROVIDER));
                    } catch (IOException e) {
                        throw new SQLException(e.getMessage(), e);
                    }
                case XLSX:
                    try {
                        final IContentKeeper ck = new XLSXContentKeeper(content);
                        return new ExcelResultSet(columnNames, ck, useFirstRowAsColumnNames, (IResultSetLobProvider)options.get(ResultSetOptions.RSO_LOB_PROVIDER));
                    } catch (IOException e) {
                        throw new SQLException(e.getMessage(), e);
                    }
                default:
                    throw new UnsupportedOperationException("Coontent type [" + type + "] is not supported yet");
            }
        }
    }
    
    /**
     * <p> Build scrollale result set for the given content type</p>
     * 
     * @param type content type (see {@link ResultSetContentType})
     * @param content input stream containing the given content
     * @param options options for the given steam (see {@link ResultSetOptions})
     * @return full-functional result set.
     * @throws SQLException 
     */
    public static ResultSet buildScrollableResultSet(final ResultSetContentType type, final InputStream content, final Map<ResultSetOptions,Object> options) throws SQLException {
        boolean     useFirstRowAsColumnNames;
        String[]    columnNames;
        
        if (type == null) {
            throw new IllegalArgumentException("Content type can't be null");
        } else if (content == null) {
            throw new IllegalArgumentException("Content stream can't be null");
        } else if (options == null) {
            throw new IllegalArgumentException("Options can't be null");
        } else if (options.containsKey(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES) && !(options.get(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES) instanceof Boolean)) {
            throw new IllegalArgumentException("[" + ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES+"] option in the options map need not be null and need have Boolean type");
        } else if (options.containsKey(ResultSetOptions.RSO_COLUMN_NAMES) && !(options.get(ResultSetOptions.RSO_COLUMN_NAMES) instanceof String[])) {
            throw new IllegalArgumentException("[" + ResultSetOptions.RSO_COLUMN_NAMES+"] option in the options map need not be null and need have String[] type");
        } else if (options.containsKey(ResultSetOptions.RSO_LOB_PROVIDER) && !(options.get(ResultSetOptions.RSO_LOB_PROVIDER) instanceof IResultSetLobProvider)) {
            throw new IllegalArgumentException("[" + ResultSetOptions.RSO_LOB_PROVIDER+"] option in the options mapneed not be null and need have IResultSetLobProvider type");
        } else {
            useFirstRowAsColumnNames = options.containsKey(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES) ? ((Boolean)options.get(ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES)).booleanValue() : false;
            columnNames = options.containsKey(ResultSetOptions.RSO_COLUMN_NAMES) ? (String[])options.get(ResultSetOptions.RSO_COLUMN_NAMES) : new String[0];
            
            if (!useFirstRowAsColumnNames && columnNames.length == 0) {
                throw new IllegalArgumentException("Neither useFirstRowAsColumnNames=true nor columnNames present. Can't define column names for the ResultSet!");
            }
            
            switch (type) {
                case XLS:
                    try {
                        final IContentKeeper ck = new XLSContentKeeper(content);
                        return new ExcelResultSet(columnNames, ResultSet.TYPE_SCROLL_INSENSITIVE, ck, useFirstRowAsColumnNames, (IResultSetLobProvider)options.get(ResultSetOptions.RSO_LOB_PROVIDER));
                    } catch (IOException e) {
                        throw new SQLException(e.getMessage(), e);
                    }
                case XLSX:
                    try {
                        final IContentKeeper ck = new XLSXContentKeeper(content);
                        return new ExcelResultSet(columnNames, ResultSet.TYPE_SCROLL_INSENSITIVE, ck, useFirstRowAsColumnNames, (IResultSetLobProvider)options.get(ResultSetOptions.RSO_LOB_PROVIDER));
                    } catch (IOException e) {
                        throw new SQLException(e.getMessage(), e);
                    }
                default:
                    throw new UnsupportedOperationException("Coontent type [" + type + "] is not supported yet");
            }
        }
    }


}
