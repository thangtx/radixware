/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.reports;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;
import org.radixware.kernel.server.types.Report;

/**
 *
 * @author akrylov
 */
public class DefaultReportFileController implements IReportFileController {

    protected final IReportFileController delegate;
    private File targetDir;
    private final boolean createNullStream;

    public DefaultReportFileController(IReportFileController delegate, boolean createNullStream) {
        this.delegate = delegate;
        this.createNullStream = createNullStream;
    }

    public DefaultReportFileController(IReportFileController delegate) {
        this.delegate = delegate;
        this.createNullStream = (delegate instanceof DefaultReportFileController) ? ((DefaultReportFileController) delegate).createNullStream() : false;
    }

    public boolean createNullStream() {
        return createNullStream;
    }

    @Override
    public String adjustFileName(Report report, String fileName) throws ReportGenerationException {
        if (delegate != null) {
            return delegate.adjustFileName(report, fileName);
        } else {
            return fileName;
        }
    }

    @Override
    public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
        if (delegate != null && file != null) {
            delegate.afterCreateFile(report, file, output);
        }
    }

    @Override
    public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
        if (delegate != null && file != null) {
            delegate.beforeCloseFile(report, file, output);
        }
    }

    @Override
    public void afterCloseFile(Report report, File file) throws ReportGenerationException {
        if (delegate != null && file != null) {
            delegate.afterCloseFile(report, file);
        }
    }

    @Override
    public File getDirectory() throws ReportGenerationException {
        if (delegate != null) {
            return delegate.getDirectory();
        } else {
            if (targetDir == null) {
                targetDir = createTmpDir();
            }
            return targetDir;
        }
    }

    public static File createTmpDir() {
        try {
            File result = Files.createTempDirectory(UUID.randomUUID().toString()).toFile();
            result.deleteOnExit();
            
            return result;
        } catch (IOException e) {
            return null;
        }
    }

}
