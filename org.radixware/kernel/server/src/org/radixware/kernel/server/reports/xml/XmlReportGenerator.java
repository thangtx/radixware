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

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.enums.EReportSpecialCellType;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellsUtils;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.HtmlParser;
import org.radixware.kernel.server.types.Report;

public class XmlReportGenerator extends AbstractReportGenerator {

    private ReportXmlRoot root = null;

    public XmlReportGenerator(Report report, ReportStateInfo stateInfo) {
        super(report, EReportExportFormat.XML, stateInfo);
    }

    @Override
    protected boolean isInfiniteHeight() {
        return true;
    }

    @Override
    protected boolean isFormattingSupported() {
        return false;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        // NOTHING - XML doesn't required any formatting
        return null;
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
        // NOTHING - XML doesn't required any formatting
    }

    public static boolean isExportable(AdsReportWidget widget) {
        if (widget.isReportContainer()) {
            return false;
        }
        AdsReportCell cell = (AdsReportCell) widget;
        final EReportCellType cellType = cell.getCellType();

        if (cellType == EReportCellType.TEXT || cellType == EReportCellType.IMAGE || cellType == EReportCellType.DB_IMAGE) {
            return false;
        } else if (cellType == EReportCellType.SPECIAL) {
            final AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
            if (specialCell.getSpecialType() == EReportSpecialCellType.PAGE_NUMBER || specialCell.getSpecialType() == EReportSpecialCellType.TOTAL_PAGE_COUNT || specialCell.getSpecialType() == EReportSpecialCellType.FILE_PAGE_COUNT) {
                return false;
            }
        }
        return true;
    }

    public static boolean isExportable(AdsReportBand band) {
        for (AdsReportWidget cell : band.getWidgets()) {
            if (isExportable(cell)) {
                return true;
            }
        }
        return false;
    }

    private void viewCell(Report report, AdsReportCell cell, ReportXmlBand xBand) throws XMLStreamException {
        if (!isExportable(cell)) {
            return;
        }

        CellsUtils.getCellContant(cell);
        final ReportXmlCell xCell = xBand.addNewCell();
        xCell.begin();

        final String cellName = getCellXmlName(report, cell);
        final String cellValue = cell.getRunTimeContent();

        xCell.setName(cellName);

        if (cellValue != null) {
            xCell.setValue(cellValue);
        }

        xCell.end();
    }

    void viewBand(Report report, AdsReportBand band, ReportXmlBand xBand) throws XMLStreamException {
        xBand.begin();

        final String bandType = band.getType().getValue();
        xBand.setType(bandType);

        final int bandGroupLevel = getBandLevel(band);
        if (bandGroupLevel > 0) {
            xBand.setGroupLevel(bandGroupLevel);
        }

        for (AdsReportWidget widget : band.getWidgets()) {
            if (!widget.isReportContainer()) {
                viewCell(report, (AdsReportCell) widget, xBand);
            }
        }

        xBand.end();
    }

    @Override
    protected void viewBand(final List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {
        if (!isExportable(band)) {
            return;
        }
        try {
            final Report report = genDataList.get(genDataList.size() - 1).report;
            final ReportXmlBand xBand = root.addNewBand();
            viewBand(report, band, xBand);
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
    }

    private void openRoot(OutputStream foStream) throws XMLStreamException {
        final XMLStreamWriter foXmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(foStream, FileUtils.XML_ENCODING);
        root = new ReportXmlRoot(foXmlStreamWriter);
        root.begin();
    }

    private void closeRoot() throws XMLStreamException {
        root.end();
    }

//    @Override
//    public void generateReport(OutputStream stream) throws ReportGenerationException {
//        try {
//            openRoot(stream);
//            super.generateReport(stream);
//            closeRoot();
//        } catch (XMLStreamException ex) {
//            throw new ReportGenerationException(ex);
//        }
//    }

    @Override
    public void generateReport(IReportFileController controller) throws ReportGenerationException {
        final IReportFileController wrapper = new DefaultReportFileController(controller) {

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                try {
                    closeRoot();
                    super.beforeCloseFile(report, file, output);
                } catch (XMLStreamException ex) {
                    throw new ReportGenerationException(ex);
                }
            }

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                try {
                    super.afterCreateFile(report, file, output);
                    openRoot(output);
                } catch (XMLStreamException ex) {
                    throw new ReportGenerationException(ex);
                }
            }

        };
        super.generateReport(wrapper);
    }

}
