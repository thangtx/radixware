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
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.reports.SerializedReportFileResultSet.MetaDataBasedColumnDescriptor;

public class ReportResultSetInfo {

    private File resultSetContentDir;
    
    private final Map<String, Integer> reportResultSetSize = new HashMap<>();
    private final Map<String, MetaDataBasedColumnDescriptor[]> reportResultSetColumnDescriptors = new HashMap<>();

    public ReportResultSetInfo() {
        try {            
            createResultSetContentDir();
        } catch (IOException ex) {
            Arte.get().getTrace().put(EEventSeverity.ERROR, "Unable to create result set temporary directory for multiformat report " + Arte.get().getTrace().exceptionStackToString(ex), EEventSource.ARTE_REPORTS);
        }
    }

    public final void createResultSetContentDir() throws IOException {
        resultSetContentDir = Files.createTempDirectory("ReportExportResultSets").toFile();
    }

    public final void deleteResultSetContentDir() {
        FileUtils.deleteDirectory(resultSetContentDir);
    }

    public File getResultSetContentDir() {
        return resultSetContentDir;
    }

    public int getReportResultSetSize(String resultSetFileName) {
        return reportResultSetSize.get(resultSetFileName);
    }

    public void setReportResultSetSize(String resultSetFileName, int size) {
        reportResultSetSize.put(resultSetFileName, size);
    }

    public MetaDataBasedColumnDescriptor[] getReportResultColumnDescriptors(String resultSetFileName) {
        return reportResultSetColumnDescriptors.get(resultSetFileName);
    }

    public void setReportResultColumnDescriptors(String resultSetFileName, ResultSetMetaData metaData) throws SQLException {
        MetaDataBasedColumnDescriptor[] descriptors = new MetaDataBasedColumnDescriptor[metaData.getColumnCount()];
        for (int i = 0; i < descriptors.length; i++) {
            descriptors[i] = new MetaDataBasedColumnDescriptor(metaData, i);
        }

        reportResultSetColumnDescriptors.put(resultSetFileName, descriptors);
    }

    public boolean isResultSetFileExists(String resultSetFileName) {
        File result = new File(resultSetContentDir, resultSetFileName);
        
        return result.exists();
    }

    public File getResultSetFile(String resultSetFileName) {
        File result = new File(resultSetContentDir, resultSetFileName);        

        return result.exists() ? result : null;
    }

    public File createResultSetFile(String resultSetFileName) throws IOException {        
        File result = new File(resultSetContentDir, resultSetFileName);
        if (!result.exists()) {
            result.createNewFile();
        }

        return result;
    }      
}
