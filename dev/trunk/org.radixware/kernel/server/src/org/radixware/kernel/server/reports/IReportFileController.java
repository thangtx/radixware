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
import java.io.OutputStream;
import org.radixware.kernel.server.types.Report;

/**
 *
 * @author akrylov
 */
public interface IReportFileController {

    File getDirectory() throws ReportGenerationException;

    String adjustFileName(Report report, String fileName) throws ReportGenerationException;

    void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException;

    void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException;

    void afterCloseFile(Report report, File file) throws ReportGenerationException;
}
