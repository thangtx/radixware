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

package org.radixware.kernel.server.reports.xml;

import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.types.Report;


class SingleBandXmlReportGenerator extends XmlReportGenerator {

    private final AdsReportBand band;
    private final Report report;
    private BandXmlRoot root = null;

    public SingleBandXmlReportGenerator(Report report, AdsReportBand band) {
        super(report,null);
        this.report = report;
        this.band = band;
    }

    private void openRoot(OutputStream foStream) throws XMLStreamException {
        final XMLStreamWriter foXmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(foStream, FileUtils.XML_ENCODING);
        root = new BandXmlRoot(foXmlStreamWriter);
    }

    private void closeRoot() throws XMLStreamException {
        //do nothing
    }

    @Override
    public void generateReport(OutputStream stream) throws ReportGenerationException {
        // export only one current band.
        try {
            openRoot(stream);
            super.viewBand(report, band, root);
            closeRoot();
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
    }
}
