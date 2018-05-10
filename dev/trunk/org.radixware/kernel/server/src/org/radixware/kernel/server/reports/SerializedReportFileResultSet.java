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
package org.radixware.kernel.server.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.radixware.kernel.common.utils.FakeResultSet;

public class SerializedReportFileResultSet extends FakeResultSet {

    public static class Record implements Serializable, Iterable<Serializable> {

        private final ArrayList<Serializable> columns;

        public Record(List<Serializable> columns) {
            this.columns = new ArrayList<>(columns);
        }

        public Serializable get(int index) {
            return columns.get(index);
        }

        public int size() {
            return columns.size();
        }

        @Override
        public Iterator<Serializable> iterator() {
            return columns.iterator();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            boolean isFirst = true;
            for (Serializable column : columns) {
                if (!isFirst) {
                    sb.append(" ");
                }
                isFirst = false;
                sb.append(String.valueOf(column));
            }

            return sb.toString();
        }
    }

    public static class MetaDataBasedColumnDescriptor extends ColumnDescriptor {

        public MetaDataBasedColumnDescriptor(ResultSetMetaData metaData, int columnIndex) throws SQLException {
            super(
                    columnIndex,
                    metaData.getColumnName(columnIndex + 1),
                    metaData.getColumnType(columnIndex + 1),
                    metaData.getColumnTypeName(columnIndex + 1),
                    metaData.getColumnClassName(columnIndex + 1),
                    metaData.isCurrency(columnIndex + 1),
                    metaData.isSigned(columnIndex + 1),
                    metaData.getPrecision(columnIndex + 1),
                    metaData.getScale(columnIndex + 1)
            );
        }
    }

    private static final String TABLE_NAME = "ReportDataTable";

    private final int resultSetSize;
    private final File contentFile;
    private final ColumnDescriptor[] columnDescriptors;

    private ObjectInputStream contentStream;

    private Record currentRecord = null;

    public SerializedReportFileResultSet(File resultSetContentFile, String[] columns, ColumnDescriptor[] columnDescriptors, int resultSetSize) throws FileNotFoundException, IOException {
        super(TABLE_NAME, columns, ResultSet.TYPE_SCROLL_INSENSITIVE, null);

        this.resultSetSize = resultSetSize;
        this.columnDescriptors = columnDescriptors;
        this.contentFile = resultSetContentFile;

        FileInputStream fis = new FileInputStream(resultSetContentFile);
        GZIPInputStream gzis = new GZIPInputStream(fis);
        contentStream = new ObjectInputStream(gzis);
    }

    @Override
    protected int getResultSetSize() {
        return resultSetSize;
    }

    @Override
    protected Object getCell(int row, int col) {
        return currentRecord.get(col);
    }

    @Override
    protected ColumnDescriptor[] getColumnDescriptors() {
        return columnDescriptors;
    }

    @Override
    public boolean next() throws SQLException {
        boolean result = super.next();

        if (result) {
            try {
                currentRecord = (Record) contentStream.readObject();
            } catch (ClassNotFoundException | IOException ex) {
                throw new SQLException("Unable to obtain row [" + getRow() + "]. Exception occured while reading file", ex);
            }
        }

        return result;
    }

    @Override
    public void close() throws SQLException {
        if (!isClosed()) {
            super.close();
            try {
                contentStream.close();
            } catch (IOException ex) {
                throw new SQLException("Unable to close result set", ex);
            }
        }
    }

    @Override
    public void beforeFirst() throws SQLException {
        try {
            super.beforeFirst();

            contentStream.close();
            FileInputStream fis = new FileInputStream(contentFile);
            contentStream = new ObjectInputStream(fis);
        } catch (IOException ex) {
            throw new SQLException("Unable to move set result set before first", ex);
        }

    }
}
