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
package org.radixware.kernel.common.defs.ads.clazz.sql.report.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class AbstractReportPreviewDataProvider {

    public abstract byte[] preview(final Id reportId, final String reportLayerUri, final String exportFormat, final byte[] reportJar, final byte[] testDataZip, final String exportLocale);

    protected abstract void addReporToJar(final AdsReportClassDef report, final JarOutputStream reportsJar)  throws IOException;

    public byte[] getReportJar(AdsReportClassDef report) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (JarOutputStream jos = new JarOutputStream(baos)) {
            addReporToJar(report, jos);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ByteArrayOutputStream resultBaos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(resultBaos)) {
            zos.setMethod(ZipOutputStream.DEFLATED);
            ZipEntry ze = new ZipEntry(report.getId().toString() + ".jar");
            zos.putNextEntry(ze);
            FileUtils.copyStream(bais, zos);
            zos.closeEntry();
        } finally {
            bais.close();
        }

        return resultBaos.toByteArray();
    }
}
