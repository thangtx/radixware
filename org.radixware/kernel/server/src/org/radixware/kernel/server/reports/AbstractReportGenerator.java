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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.ParsedURLData;
import org.apache.batik.util.ParsedURLDefaultProtocolHandler;
import org.apache.batik.util.ParsedURLProtocolHandler;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportExpressionCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormattedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroupBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginMm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportTextCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.enums.EReportSummaryCellType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.meta.RadEnumDef;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.NullOutputStream;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.clazzes.IRadPropAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.server.reports.fo.ChartBuilder;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Report;
import org.radixware.schemas.reports.ColumnSettings;

public abstract class AbstractReportGenerator {

    protected static class ReportGenData {

        public final Id reportId;
        public final Report report;
        public final AdsReportForm form;
        public final List<GroupInfo> groupInfos;
        public final SummaryCollector totalSummary;
        public final Set<RadPropDef> summaryProps;
        public final Set<ChartDataSet> chartDataSets;

        public ReportGenData(final Report report, AdsReportForm.Mode getMode, ReportGenData parent) {
            this(report, getMode, parent.totalSummary.getStateInfo());
        }

        public ReportGenData(final Report report, AdsReportForm.Mode genMode, ReportStateInfo stateInfo) {
            this.report = report;
            this.form = report.createForm();
            this.form.setMode(genMode);
            totalSummary = new SummaryCollector(stateInfo);

            this.reportId = report.getId();//Id.Factory.loadFrom(report.getClass().getSimpleName());
            this.summaryProps = calcSummaryProps();
            this.chartDataSets = createChartDataSets();

            final int groupCount = form.getGroups().size();
            if (groupCount > 0) {
                // all groups are opened at first record, it is easier to hold all infos with 'opened' flag than enlarge list.
                this.groupInfos = new ArrayList<>(groupCount);
                for (int i = 0; i < groupCount; i++) {
                    final GroupInfo groupInfo = new GroupInfo();
                    this.groupInfos.add(groupInfo);
                }
            } else {
                this.groupInfos = null;
            }
        }

        private Set<RadPropDef> calcSummaryProps() {
            final Set<RadPropDef> result = new HashSet<>();

            form.visitAll(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof AdsReportCell) {
                        final AdsReportCell cell = (AdsReportCell) radixObject;
                        if (cell.getCellType() == EReportCellType.SUMMARY) {
                            final AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
                            final RadPropDef propDef = report.getRadMeta().getPropById(summaryCell.getPropertyId());
                            result.add(propDef);
                        }
                    }
                }
            });

            return result;
        }

        private Set<ChartDataSet> createChartDataSets() {
            final Set<ChartDataSet> resultChart = new LinkedHashSet<>();

            form.visitAll(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof AdsReportCell) {
                        final AdsReportCell cell = (AdsReportCell) radixObject;
                        if (cell.getCellType() == EReportCellType.CHART) {
                            final AdsReportChartCell chartCell = (AdsReportChartCell) cell;
                            if (chartCell.getChartSeries() != null && !chartCell.getChartSeries().isEmpty()) {
                                for (AdsReportChartSeries series : chartCell.getChartSeries()) {
                                    final int groupIndex = series.getGroupIndex();

                                    final RadPropDef seriesPropDef = series.getSeriesData() != null && series.getSeriesData().getPropId() != null
                                            ? report.getRadMeta().getPropById(series.getSeriesData().getPropId()) : null;

                                    final RadPropDef domainPropDef = series.getDomainData() != null && series.getDomainData().getPropId() != null
                                            ? report.getRadMeta().getPropById(series.getDomainData().getPropId()) : null;

                                    final RadPropDef rangePropDef = series.getRangeData() != null && series.getRangeData().getPropId() != null
                                            ? report.getRadMeta().getPropById(series.getRangeData().getPropId()) : null;

                                    final int domainAxisIndex = series.getDomainData() != null
                                            ? series.getDomainData().getAxisIndex() : 0;

                                    final int rangeAxisIndex = series.getRangeData() != null
                                            ? series.getRangeData().getAxisIndex() : 0;

                                    resultChart.add(new ChartDataSet(groupIndex, seriesPropDef, domainPropDef, rangePropDef,
                                            series, domainAxisIndex, rangeAxisIndex));
                                }
                            }
                        }
                    }
                }
            });
            return resultChart;
        }

        public GroupInfo getCurrentGroupInfo() {
            if (groupInfos != null) {
                for (int i = groupInfos.size() - 1; i >= 0; i--) {
                    final GroupInfo group = groupInfos.get(i);
                    if (group.isOpened()) {
                        return group;
                    }
                }
            }
            return null;
        }
    }

    protected static class SubReportGenData extends ReportGenData {

        private final AdsSubReport subReportInfo;

        public SubReportGenData(Report report, AdsReportForm.Mode getMode, ReportGenData parent) {
            this(report, getMode, parent, null);
        }

        public SubReportGenData(Report report, AdsReportForm.Mode genMode, ReportStateInfo stateInfo) {
            this(report, genMode, stateInfo, null);
        }

        public SubReportGenData(Report report, AdsReportForm.Mode getMode, ReportGenData parent, AdsSubReport subReportInfo) {
            super(report, getMode, parent);
            this.subReportInfo = subReportInfo;
        }

        public SubReportGenData(Report report, AdsReportForm.Mode genMode, ReportStateInfo stateInfo, AdsSubReport subReportInfo) {
            super(report, genMode, stateInfo);
            this.subReportInfo = subReportInfo;
        }

        public AdsReportMarginMm getMargin() {
            return subReportInfo != null ? subReportInfo.getMarginMm() : null;
        }

    }

    private static class GenTimeInfo implements Report.IGenTimeInfo {

        private final Timestamp generationTime;
        private boolean firstDataBandOnPage = true;
        private boolean isInPageFooter = false;
        private double curHeight = 0.0;
        private int curHeightRows = 0;
        private int pageNumber = 0;
        private int totalPageNumber = 0;
        private int fileNumber = 0;
        private boolean isPageOpened = false;
        private boolean isFileOpened = false;
        private OutputStream currentStream;
        private File currentFile;
        private double currentPageFooterMM;
        private int currentPageFooterRows;

        public GenTimeInfo() {
            this.generationTime = new Timestamp(System.currentTimeMillis());
        }

        @Override
        public int getCurrentPageNumber() {
            return pageNumber;
        }

        @Override
        public Timestamp getGenerationTime() {
            return generationTime;
        }
    }

    public class SpecialInfo /*implements Report.ISpecialInfo */ {

        private final ReportGenData reportGenData;

        SpecialInfo(final ReportGenData reportGenData) {
            this.reportGenData = reportGenData;
        }

        public double getSummaryCellValue(EReportSummaryCellType summaryType, Id propertyId, int groupLevel) {
            double summary = 0.0;
            final SummaryCollector summaryCollector = getSummaryCollector(groupLevel);
            if (summaryCollector != null) {
                switch (summaryType) {
                    case MIN:
                        summary = summaryCollector.getFieldMinById(propertyId);
                        break;
                    case MAX:
                        summary = summaryCollector.getFieldMaxById(propertyId);
                        break;
                    case SUM:
                        summary = summaryCollector.getFieldSumById(propertyId);
                        break;
                }
            }
            return summary;
        }

        public int getSubItemCount(int groupLevel) {
            final SummaryCollector summaryCollector = getSummaryCollector(groupLevel);
            if (summaryCollector != null) {
                return summaryCollector.getSubItemCount();
            }
            return 0;
        }

        public int getCurGroupRecordCount() {
            if (reportGenData != null) {
                final GroupInfo currentGroup = reportGenData.getCurrentGroupInfo();
                return (currentGroup != null ? currentGroup.getSummary().getRecordCount() : 0);
            }
            return 0;
        }

        public int getTotalRecordCount() {
            if (reportGenData != null) {
                return reportGenData.totalSummary.getRecordCount();
            }
            return 0;
        }

        private SummaryCollector getSummaryCollector(int groupLevel) {
            final SummaryCollector summaryCollector;
            if (groupLevel <= 0) {
                summaryCollector = reportGenData.totalSummary;
            } else if (groupLevel <= reportGenData.form.getGroups().size()) {
                summaryCollector = reportGenData.groupInfos.get(groupLevel - 1).getSummary();
            } else {
                return null;
            }
            return summaryCollector;
        }
    }

    private final ReportGenData rootGenData;
    private final GenTimeInfo genTimeInfo = new GenTimeInfo();

    private ReportResultSetInfo resultSetInfo = null;
    private ResultSetCacheSizeController resultSetCacheSizeController = null;
    private final Map<Id, Integer> reportCallCount = new HashMap<>();

    private final AdsReportForm.Mode genMode;
    protected IReportFileController fileController;
    protected final EReportExportFormat format;
    protected int separateFileGroupNumber;
    private final List<File> tmpFiles = new LinkedList<>();
    
    protected ColumnSettings columnsSettings = null;

    protected AbstractReportGenerator(Report report, EReportExportFormat format, AdsReportForm.Mode genMode, ReportStateInfo stateInfo) {
        this.rootGenData = new ReportGenData(report, genMode, stateInfo);
        this.genMode = genMode;
        this.separateFileGroupNumber = -1;
        this.fileController = null; 
        this.format = format;
    }

    protected AbstractReportGenerator(Report report, EReportExportFormat format, ReportStateInfo stateInfo) {
        this(report, format, AdsReportForm.Mode.GRAPHICS, stateInfo);
    }

    protected boolean isInfiniteHeight() {
        return false;
    }

    protected abstract boolean isFormattingSupported();

    public void setSeparateFileGroupLevel(int separateFileGroupNumber) {
        this.separateFileGroupNumber = separateFileGroupNumber;
    }

    protected String formatObject(Object object, AdsReportFormattedCell formattedCell) {
        return formatObject(object, formattedCell.getFormat());
    }

    public ReportStateInfo getStateInfo() {
        return rootGenData.totalSummary.getStateInfo();
    }

    protected String formatObject(Object object, AdsReportFormat format) {
        if (object != null) {
            if (object instanceof Clob) {
                final Clob clob = (Clob) object;
                try {
                    object = clob.getSubString(1, (int) clob.length());
                } catch (SQLException ex) {
                    throw new DatabaseError(ex);
                }
            }
            if (isFormattingSupported()) {
                Locale locale = rootGenData.report.getArte().getClientLocale();
                return CellValueFormatter.formatValue(object, format, locale);//TitleItemFormatter.format(pattern, object,  groupSeparator,groupSize);
            } else {
                return object.toString();
            }
        } else {
            return null;
        }
    }

    protected Object getPropertyValueById(final Report report, final Id propertyId) {
        final RadPropDef prop = report.getRadMeta().getPropById(propertyId);
        final IRadPropReadAccessor readAccessor = (IRadPropReadAccessor) prop.getAccessor();
        try {
            return getPropValFromReadAccessor(readAccessor, report, propertyId);
        } catch (NoConstItemWithSuchValueError ex) {
            return ex.value; // RADIX-4630
        }
    }

    protected String formatProperty(final Report report, final Id propertyId, AdsReportFormat format) {
        Object value = getPropertyValueById(report, propertyId);

        if (value instanceof IKernelEnum) {
            final IKernelEnum e = (IKernelEnum) value;
            if (isFormattingSupported()) {
                final Id enumId = report.getRadMeta().getPropById(propertyId).getEnumId();
                final Arte arte = report.getArte();
                final RadEnumDef enumDef = arte.getDefManager().getEnumDef(enumId);
                final RadEnumDef.IItem enumItem = enumDef.getItemByVal(e.getValue());
                value = enumItem.getTitle(arte);
                if (value == null || ((value instanceof String) && ((String) value).isEmpty())) {
                    value = enumItem.getName();
                }
            } else {
                value = e.getValue();
            }
        } else if (value instanceof Entity) {
            final Entity entity = (Entity) value;
            value = entity.calcTitle();
        }

        return formatObject(value, format);
    }
    private static final ParsedURLProtocolHandler starterURLProtocolHandler = new ParsedURLDefaultProtocolHandler() {
        @Override
        public String getProtocolHandled() {
            return "starter";
        }

        @Override
        public ParsedURLData parseURL(ParsedURL base, String string) {
            final ParsedURLData result;
            if (string != null && string.startsWith("starter:")) {
                result = parseURL(string);
            } else {
                result = super.parseURL(base, string);
            }
            return result;
        }
    };

    static {
        ParsedURL.registerHandler(starterURLProtocolHandler);
    }

    private String calcCellContent(final ReportGenData reportGenData, final AdsReportCell cell) {
        if (cell instanceof AdsReportTextCell) {
            final AdsReportTextCell textCell = (AdsReportTextCell) cell;
            return org.radixware.kernel.common.types.MultilingualString.get(reportGenData.report.getArte(), reportGenData.reportId, textCell.getTextId());
        } else if (cell instanceof AdsReportExpressionCell) {
            final AdsReportExpressionCell expressionCell = (AdsReportExpressionCell) cell;
            return expressionCell.calcExpression();
        } else if (cell instanceof AdsReportPropertyCell) {
            final AdsReportPropertyCell propertyCell = (AdsReportPropertyCell) cell;
            final String value = formatProperty(reportGenData.report, propertyCell.getPropertyId(), propertyCell.getFormat());
            if (value == null) {
                final Id nullTitleId = propertyCell.getNullTitleId();
                if (nullTitleId != null) {
                    return org.radixware.kernel.common.types.MultilingualString.get(reportGenData.report.getArte(), reportGenData.reportId, nullTitleId);
                }
            }
            return value;

        } else if (cell instanceof AdsReportSpecialCell) {
            final AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
            switch (specialCell.getSpecialType()) {
                case PAGE_NUMBER:
                    return formatObject(genTimeInfo.pageNumber, specialCell);
                case TOTAL_PAGE_NUMBER:
                    return formatObject(genTimeInfo.totalPageNumber, specialCell);
                case TOTAL_PAGE_COUNT:
                    return formatObject(rootGenData.totalSummary.getTotalPageCount(), specialCell);
                case FILE_COUNT:
                    return formatObject(rootGenData.totalSummary.getFileCount(), specialCell);
                case FILE_NUMBER:
                    return formatObject(genTimeInfo.fileNumber, specialCell);
                case FILE_PAGE_COUNT:
                    return formatObject(reportGenData.totalSummary.getPageCount(genTimeInfo.fileNumber), specialCell);
                case GENERATION_TIME:
                    return formatObject(genTimeInfo.generationTime, specialCell);
                case CURRENT_GROUP_RECORD_COUNT:
                    final GroupInfo currentGroup = reportGenData.getCurrentGroupInfo();
                    final int recordCountInCurrentGroup = (currentGroup != null ? currentGroup.getSummary().getRecordCount() : 0);
                    return formatObject(Long.valueOf(recordCountInCurrentGroup), specialCell);
                case SUB_ITEM_COUNT:
                    final SummaryCollector summary;
                    if (specialCell.getOwnerBand().getType() == EReportBandType.GROUP_HEADER) {
                        final int groupIdx = ((AdsReportGroupBand) specialCell.getOwnerBand()).getOwnerGroup().getIndex() - 1;
                        summary = (groupIdx >= 0 ? reportGenData.groupInfos.get(groupIdx).getSummary() : reportGenData.totalSummary);
                    } else {
                        final GroupInfo currentGroupInfo = reportGenData.getCurrentGroupInfo();
                        summary = (currentGroupInfo != null ? currentGroupInfo.getSummary() : reportGenData.totalSummary);
                    }
                    final int subItemCount = summary.getSubItemCount();
                    return formatObject(Long.valueOf(subItemCount), specialCell);
                case TOTAL_RECORD_COUNT:
                    return formatObject(Long.valueOf(reportGenData.totalSummary.getRecordCount()), specialCell);
                default:
                    return String.valueOf(specialCell);
            }
        } else if (cell instanceof AdsReportSummaryCell) {
            final AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
            final SummaryCollector summaryCollector;
            if (summaryCell.getGroupCount() <= 0) {
                summaryCollector = reportGenData.totalSummary;
            } else if (summaryCell.getGroupCount() <= reportGenData.form.getGroups().size()) {
                summaryCollector = reportGenData.groupInfos.get(summaryCell.getGroupCount() - 1).getSummary();
            } else {
                return "";
            }

            final Id propertyId = summaryCell.getPropertyId();
            final double summary;
            switch (summaryCell.getSummaryType()) {
                case MIN:
                    summary = summaryCollector.getFieldMinById(propertyId);
                    break;
                case MAX:
                    summary = summaryCollector.getFieldMaxById(propertyId);
                    break;
                case SUM:
                    summary = summaryCollector.getFieldSumById(propertyId);
                    break;
                default:
                    summary = 0.0;
            }
            Object value;
            final RadPropDef prop = reportGenData.report.getRadMeta().getPropById(propertyId);
            if (prop.getValType() == EValType.INT) {
                value = (long) summary;
            } else {
                value = BigDecimal.valueOf(summary);
            }
            return formatObject(value, summaryCell);
        } else if (cell instanceof AdsReportImageCell) {
            final AdsReportImageCell imageCell = (AdsReportImageCell) cell;
            final Id imageId = imageCell.getImageId();
            final DefManager defManager = reportGenData.report.getArte().getDefManager();

            if (reportGenData.reportId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
                try {
                    DefManager.ImageData data = defManager.getDbImageData(reportGenData.reportId, imageId);
                    if (data == null) {
                        return "";
                    } else {
                        if (data.getMimeType() != null && data.getMimeType().contains("/svg")) {
                            File file = createSvgImage(data.getData(), cell);
                            return "url('" + file.toURI().toString() + "')";
                        }
                        final String dataInBase64 = Base64.encode(data.getData());
                        return "url('data:" + data.getMimeType() + ";base64," + dataInBase64 + "')";
                    }
                } catch (Exception e) {
                    return "";
                }
            } else {
                final String imageFileName = defManager.getImgFileNameByImgId(imageId);
                final URL url = defManager.getClassLoader().getResource(imageFileName);
                return "url('" + url.toExternalForm() + "')";
            }
        } else if (cell instanceof AdsReportDbImageCell) {
            final AdsReportDbImageCell dbImageCell = (AdsReportDbImageCell) cell;

            final Id mimeTypePropId = dbImageCell.getMimeTypePropertyId();
            String mimeType = (String) getPropertyValueById(reportGenData.report, mimeTypePropId);
            if (mimeType == null || mimeType.isEmpty()) {
                return "";
            }

            final Id imageDataPropId = dbImageCell.getDataPropertyId();
            final Blob data = (Blob) getPropertyValueById(reportGenData.report, imageDataPropId);
            if (data == null) {
                return "";
            }

            byte[] bytes;
            try {
                bytes = data.getBytes(1, (int) data.length());
            } catch (SQLException ex) {
                throw new DefinitionError("Unable to get image data", ex);
            }
            byte[] convertedImage = setupDbImageCellSize(dbImageCell, mimeType, bytes, reportGenData.report.isDbImageResizeSupressed());
            if (convertedImage != bytes) {
                mimeType = "image/png";
                bytes = convertedImage;
            }
            dbImageCell.setMimeType(mimeType);

            if (mimeType.contains("/svg")) {
                File file = createSvgImage(bytes, cell);
                return "url('" + file.toURI().toString() + "')";
            }
            final String dataInBase64 = Base64.encode(bytes);
            //this is the place to calc cell size according to cell resize policy
            return "url('data:" + mimeType + ";base64," + dataInBase64 + "')";
        } else if (cell instanceof AdsReportChartCell) {
            cell.onAdding();
            final AdsReportChartCell chartCell = (AdsReportChartCell) cell;
            final Set<ChartDataSet> chartDataSets = new LinkedHashSet<>();
            for (ChartDataSet chartDataSet : reportGenData.chartDataSets) {
                if (chartDataSet.getOwnerCell() != null && chartDataSet.getOwnerCell().equals(chartCell)) {
                    chartDataSets.add(chartDataSet);
                }
            }
            ChartBuilder cb = ChartBuilder.Factory.newInstance(chartCell, reportGenData.report, chartDataSets);//new ChartBuilder(chartCell,reportGenData.chartDataSets);
            cb.buildChart();
            if (cb.file != null) {
                tmpFiles.add(cb.file);
            }
            for (ChartDataSet chartDataSet : chartDataSets) {
                chartDataSet.getDataSetMap().clear();
            }

            return cb.file.toURI().toString();

            //return cb != null ? "\"" + cb.file.getPath() + "\"" : "";
        } else {
            return "";
        }
    }

    private File createSvgImage(byte[] bytes, AdsReportCell cell) {
        File file = null;
        try {
            file = File.createTempFile("image" + cell.getName(), ".svg");
            file.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }
            tmpFiles.add(file);

        } catch (IOException ex) {
            if (file != null) {
                FileUtils.deleteFile(file);
            }
            throw new RadixError("Unable to create svg file for report.", ex);
        }
        return file;
    }

    protected byte[] setupDbImageCellSize(AdsReportDbImageCell cell, String mimeType, byte[] imageData, boolean dbImageResizeSupressed) {
        return imageData;
    }

    protected boolean shouldSkipSubReport(Report subReport) {
        return false;
    }

    protected final void buildSubReport(final List<ReportGenData> genDataList, final AdsSubReport subReportInfo) throws ReportGenerationException {
        ReportGenData parentGenData = genDataList.get(genDataList.size() - 1);
        final Report parentReport = parentGenData.report;
        final Id subReportId = subReportInfo.getReportId();

        Class subReportClass = null;
        if (Report.getPreviewEnvironment() != null) {
            try {
                subReportClass = Report.getPreviewEnvironment().findReportClassById(subReportId);
            } catch (DefinitionNotFoundError e) {
                subReportClass = parentReport.getArte().getDefManager().getClass(subReportId);
            }
        } else {
            subReportClass = parentReport.getArte().getDefManager().getClass(subReportId);
        }
        final Report subReport;

        try {
            subReport = (Report) subReportClass.newInstance();
            if (shouldSkipSubReport(subReport)) {
                return;
            }
        } catch (InstantiationException | IllegalAccessException cause) {
            throw new DefinitionError("Unable to instantiate subreport #" + subReportId, cause);
        }

        setIsFirstLine(true);
        final Map<Id, Object> parameterId2Value = new HashMap<>();

        for (int i = genDataList.size() - 1; i >= 0; i--) {
            parameterId2Value.clear();
            boolean ok = true;
            final Report paramSourceReport = genDataList.get(i).report;
            for (AdsSubReport.Association association : subReportInfo.getAssociations()) {
                final Id parameterId = association.getParameterId();
                final Id propertyId = association.getPropertyId();
                try {
                    final Object value = getPropertyValueById(paramSourceReport, propertyId);
                    parameterId2Value.put(parameterId, value);
                } catch (DefinitionNotFoundError e) {
                    if (i == 0) {
                        final StringBuilder sb = new StringBuilder();
                        for (ReportGenData gd : genDataList) {
                            if (sb.length() > 0) {
                                sb.append("->");
                            }
                            sb.append(gd.reportId);
                        }
                        throw new IllegalStateException("Unable to find parameter '" + parameterId + "' of subreport " + subReportInfo.getReportId() + ", stack: " + sb, e);
                    } else {
                        ok = false;
                        break;
                    }
                }
            }
            if (ok) {
                break;
            }
        }

        try {
            subReport.open(parameterId2Value);

            incReportCallCount(subReportId);
            if (resultSetInfo != null) {
                try {
                    int callNumber = reportCallCount.get(subReportId) - 1;
                    String resultSetFileName = subReportId.toString() + "_" + callNumber;

                    if (resultSetInfo.isResultSetFileExists(resultSetFileName)) {
                        ResultSet resultSet = new SerializedReportFileResultSet(
                                resultSetInfo.getResultSetFile(resultSetFileName),
                                subReport.getColumnNames(),
                                resultSetInfo.getReportResultColumnDescriptors(resultSetFileName),
                                resultSetInfo.getReportResultSetSize(resultSetFileName)
                        );
                        subReport.open(resultSet);
                    } else {
                        resultSetInfo.setReportResultColumnDescriptors(resultSetFileName, subReport.getResultSet().getMetaData());
                    }
                } catch (IOException | SQLException ex) {
                    throw new ReportGenerationException(ex);
                }
            }

            final SubReportGenData subReportGenData = new SubReportGenData(subReport, genMode, parentGenData, subReportInfo);
            if (rootGenData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
                genTimeInfo.curHeight += subReportInfo.getMarginMm().getTopMm();
            }
            genDataList.add(subReportGenData);
            buildReport(genDataList);
            genDataList.remove(subReportGenData);
            if (rootGenData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
                genTimeInfo.curHeight += subReportInfo.getMarginMm().getBottomMm();
            }
            afterSubReportClose(subReportGenData);
        } finally {
            subReport.close();
        }
    }

    protected void finishPage(final List<ReportGenData> genDataList) throws ReportGenerationException {
        if (genTimeInfo.pageNumber > 0) {
            if (isFlowDependent()) {//if not flow dependent, footer built in newPage();
                buildPageFooter(genDataList);
            }
        }
        genTimeInfo.isPageOpened = false;
    }

    protected void newFile(List<ReportGenData> genDataList) throws ReportGenerationException {
        if (fileController != null) {
            String fileName = rootGenData.report.calcOutputFileName(genTimeInfo.fileNumber + 1, format, rootGenData.totalSummary.getFileCountIfComputed());
            fileName = fileController.adjustFileName(rootGenData.report, fileName);
            try {
                if (fileController instanceof DefaultReportFileController && ((DefaultReportFileController) fileController).createNullStream()) {
                    genTimeInfo.currentStream = new NullOutputStream();
                } else {
                    final File directory = fileController.getDirectory();
                    directory.mkdirs();
                    final File reportFile = new File(directory, fileName);
                    genTimeInfo.currentStream = getOutputStream(reportFile);
                    genTimeInfo.currentFile = reportFile;
                }
                fileController.afterCreateFile(rootGenData.report, genTimeInfo.currentFile, genTimeInfo.currentStream);
                newFile(genDataList, genTimeInfo.currentStream);
            } catch (IOException ex) {
                throw new RadixError("Unable to create file for report", ex);
            }
        }

    }

    protected OutputStream getOutputStream(File reportFile) throws IOException {
        return new FileOutputStream(reportFile);
    }

    protected void newFile(List<ReportGenData> genDataList, OutputStream stream) throws ReportGenerationException {
        rootGenData.totalSummary.incFileCount();
        genTimeInfo.fileNumber++;
        genTimeInfo.pageNumber = 0;
        genTimeInfo.isFileOpened = true;
    }

    protected void finishFile(List<ReportGenData> genDataList) throws ReportGenerationException {
        genTimeInfo.isFileOpened = false;
        if (fileController != null && genTimeInfo.currentFile != null && genTimeInfo.currentStream != null) {
            try {
                fileController.beforeCloseFile(rootGenData.report, genTimeInfo.currentFile, genTimeInfo.currentStream);
                try {
                    genTimeInfo.currentStream.flush();
                } catch (IOException ex) {
                    throw new RadixError("Unable to save report data", ex);
                }
                try {
                    genTimeInfo.currentStream.close();
                } catch (IOException ex) {
                    //Ignore
                }
                fileController.afterCloseFile(rootGenData.report, genTimeInfo.currentFile);
            } finally {
                genTimeInfo.currentFile = null;
                genTimeInfo.currentStream = null;
            }

        }
    }

    protected void newPage(List<ReportGenData> genDataList) throws ReportGenerationException {
        if (!genTimeInfo.isFileOpened) {
            newFile(genDataList);
        }
        genTimeInfo.pageNumber++;
        genTimeInfo.totalPageNumber++;
        genTimeInfo.isPageOpened = true;
        if (rootGenData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
            genTimeInfo.curHeight = rootGenData.form.getMargin().getTopMm();
        } else {
            genTimeInfo.curHeightRows = 0;
        }

        rootGenData.totalSummary.incPageCount(genTimeInfo.fileNumber);
        buildPageHeader(genDataList);

        if (!isFlowDependent()) {
            buildPageFooter(genDataList);
        }
        genTimeInfo.firstDataBandOnPage = true;
    }

    protected void buildPageFooter(List<ReportGenData> genDataList) throws ReportGenerationException {
        try {
            genTimeInfo.isInPageFooter = true;
            for (int i = 0; i < genDataList.size(); i++) {
                ReportGenData genData = genDataList.get(i);
                for (AdsReportBand footer : genData.form.getPageFooterBands()) {
                    buildBand(genDataList, footer, EReportBandType.PAGE_FOOTER, i);
                }
            }
        } finally {
            genTimeInfo.isInPageFooter = false;
        }
    }

    protected void setFooterHeight(int rows, double mms) {
        genTimeInfo.currentPageFooterMM = mms;
        genTimeInfo.currentPageFooterRows = rows;
    }

    protected int getCurPageFooterHeightRows() {
        return genTimeInfo.currentPageFooterRows;
    }

    protected double getCurPageFooterHeightMm() {
        return genTimeInfo.currentPageFooterMM;
    }

    protected void buildPageHeader(List<ReportGenData> genDataList) throws ReportGenerationException {
        //put all headers from all reports/subreports until root
        for (int i = 0; i < genDataList.size(); i++) {
            for (AdsReportBand header : genDataList.get(i).form.getPageHeaderBands()) {
                buildBand(genDataList, header, EReportBandType.PAGE_HEADER, i);
            }
        }
    }

    protected abstract Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException;

    private void adjustCellsToColumns(final ReportGenData reportGenData) {
        if (columnsSettings == null || !columnsSettings.getIsUsed()) {
            return;
        }
        
        for (AdsReportBand band : reportGenData.form.getColumnHeaderBands()) {
            adjustCellsToColumns(band);
        }
        
        for (AdsReportBand band : reportGenData.form.getDetailBands()) {
            adjustCellsToColumns(band);
        }
        
        for (AdsReportBand band : reportGenData.form.getSummaryBands()) {
            adjustCellsToColumns(band);
        }
                
        for (AdsReportGroup group : reportGenData.form.getGroups()) {
            if (group.getFooterBand() != null) {
                adjustCellsToColumns(group.getFooterBand());
            }
        }
    }
    
    protected void adjustCellsToColumns(AdsReportBand container) {
    }
    
    private void adjustCellsPosition(AdsReportForm.Bands container) {
        for (AdsReportBand band : container) {
            adjustCellsPosition(band);
        }
    }        

    protected abstract void adjustCellsPosition(AdsReportBand container);

    protected void setIsFirstLine(boolean isFirstLine) {
    }

    private boolean isNewPageRequiredForBand(final ReportGenData genData, final AdsReportBand band) {
        if (!genTimeInfo.isPageOpened) {
            return true;
        }

        if (isInfiniteHeight()) {
            return false;
        }

        if (genTimeInfo.firstDataBandOnPage) {
            return false;
        }

        if (band.isStartOnNewPage()) {
            return true;
        }

        if (band.isMultiPage()) {
            return false;
        }
        if (isFlowDependent()) {
            if (genData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
                double footerHeight = 0;

                for (final AdsReportBand footer : genData.form.getPageFooterBands()) {
                    footerHeight += (footer != null && footer.isVisible() ? footer.getHeightMm() : 0);
                }
                return (genTimeInfo.curHeight + band.getHeightMm() > genData.form.getPageHeightMm() - genData.form.getMargin().getBottomMm() - footerHeight);
            } else {
                int footerHeight = 0;

                for (final AdsReportBand footer : genData.form.getPageFooterBands()) {
                    footerHeight += (footer != null && footer.isVisible() ? footer.getHeightRows() : 0);
                }
                return (genTimeInfo.curHeightRows + band.getHeightRows() > genData.form.getPageHeightRows() - footerHeight);
            }
        } else {
            if (genData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
                double checkValue = genData.form.getPageHeightMm() - genData.form.getMargin().getBottomMm();
                if (!genTimeInfo.isInPageFooter) {
                    checkValue -= genTimeInfo.currentPageFooterMM;
                } else if (!isFlowDependent()) {
                    //footer size already adjusted to go into page
                    return false;
                }
                return (genTimeInfo.curHeight + band.getHeightMm() > checkValue);
            } else {
                int checkValue = genData.form.getPageHeightRows();
                if (!genTimeInfo.isInPageFooter) {
                    checkValue -= genTimeInfo.currentPageFooterRows;
                } else if (!isFlowDependent()) {
                    //footer size already adjusted to go into page
                    return false;
                }
                return (genTimeInfo.curHeightRows + band.getHeightRows() > checkValue);
            }
        }
    }

    protected abstract void viewBand(final List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException;

    protected final AdsReportForm getRootForm() {
        return rootGenData.form;
    }

    protected void calcTableStructure(AdsReportForm form) {
    }

    protected final double getCurHeightMm() {
        return genTimeInfo.curHeight;
    }

    protected final void setCurHeightMm(double height) {
        genTimeInfo.curHeight = height;
    }

    protected final int getCurHeightRows() {
        return genTimeInfo.curHeightRows;
    }
    
    protected final void setCurHeightRows(int height) {
        genTimeInfo.curHeightRows = height;
    }

    protected final boolean isFirstDataBandOnPage() {
        return genTimeInfo.firstDataBandOnPage;
    }

    private void calcCellContent(final ReportGenData genData, RadixObjects<AdsReportWidget> widgets) {
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                calcCellContent(genData, ((IReportWidgetContainer) widget).getWidgets());
            } else {
                AdsReportCell cell = (AdsReportCell) widget;
                final String content = calcCellContent(genData, cell);
                cell.setRunTimeContent(content);
            }
        }
    }

    private void onAddingCells(RadixObjects<AdsReportWidget> widgets) {
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                onAddingCells(((IReportWidgetContainer) widget).getWidgets());
            } else {
                AdsReportCell cell = (AdsReportCell) widget;
                if (!(cell instanceof AdsReportChartCell)) {
                    cell.onAdding();
                }
            }
        }
    }

    private double getFooterStartOffsetFromBottomMm(AdsReportBand footer) {
        final AdsReportForm.Bands bands = rootGenData.form.getPageFooterBands();
        int index = bands.indexOf(footer);
        double offset = 0;
        for (int i = index; i < bands.size(); i++) {
            AdsReportBand band = bands.get(i);
            if (band.isVisible()) {
                offset += band.getHeightMm();
            }
        }
        return offset;
    }

    private int getFooterStartOffsetFromBottomRows(AdsReportBand footer) {
        final AdsReportForm.Bands bands = rootGenData.form.getPageFooterBands();
        int index = bands.indexOf(footer);
        int offset = 0;
        for (int i = index; i < bands.size(); i++) {
            AdsReportBand band = bands.get(i);
            if (band.isVisible()) {
                offset += band.getHeightRows();
            }
        }
        return offset;
    }

    private void repeatGroupsOnNewPage(List<ReportGenData> reportGenDataList) throws ReportGenerationException {
        if (rootGenData.form.isRepeatGroupHeadersOnNewPage()) {
            ReportGenData reportGenData = reportGenDataList.get(reportGenDataList.size() - 1);
            int groupSize = reportGenData.form.getGroups().size();
            for (int groupNum = 0; groupNum < groupSize; groupNum++) {
                final AdsReportGroup group = reportGenData.form.getGroups().get(groupNum);
                buildBand(reportGenDataList, group.getHeaderBand(), EReportBandType.GROUP_HEADER);
            }
        }
    }

    //private List<AdsReportBand> buildedBand=new LinkedList<>();
    protected void buildBand(final List<ReportGenData> genDataList, final AdsReportBand band, EReportBandType bandType) throws ReportGenerationException {
        buildBand(genDataList, band, bandType, -1);
    }

    protected void buildBand(final List<ReportGenData> genDataList, final AdsReportBand band, EReportBandType bandType, int indexExt) throws ReportGenerationException {
        if (band == null) {
            return;
        }

        final int index = indexExt >= 0 ? indexExt : genDataList.size() - 1;
        final BandSizes bandSizes = new BandSizes(band); // remember band and cells sizes

        band.onAdding();
        if (!band.isVisible()) {
            //RADIX-11272
//            if ((band.getType() == EReportBandType.PAGE_HEADER || band.getType() == EReportBandType.PAGE_FOOTER) && genDataList.size() > 1) {
//                List<ReportGenData> genDataBaseList = new ArrayList<>(genDataList);
//                genDataBaseList.remove(index);
//
//                EReportBandType bandBaseType;
//                final AdsReportForm.Bands bandsBase;
//                if (band.getType() == EReportBandType.PAGE_HEADER) {
//                    bandsBase = genDataBaseList.get(index - 1).form.getPageHeaderBands();
//                    bandBaseType = EReportBandType.PAGE_HEADER;
//                } else {
//                    bandsBase = genDataBaseList.get(index - 1).form.getPageFooterBands();
//                    bandBaseType = EReportBandType.PAGE_FOOTER;
//                }
//                for (AdsReportBand bandBase : bandsBase) {
//                    buildBand(genDataBaseList, bandBase, bandBaseType);
//                }
//            }
//            return;
            return;
        }

        final ReportGenData genData = genDataList.get(index);

        for (AdsSubReport subReport : band.getPreReports()) {
            buildSubReport(genDataList, subReport);
        }
        calcCellContent(genData, band.getWidgets());

        onAddingCells(band.getWidgets());

        Map<AdsReportCell, AdjustedCell> adjustedCellContents = adjustBand(band);
        boolean isRootReportFooter = band.isVisible() && rootGenData.form.getPageFooterBands().contains(band);

        if (band.getType() != EReportBandType.PAGE_HEADER && band.getType() != EReportBandType.PAGE_FOOTER && isNewPageRequiredForBand(genData, band)) {
            if (genTimeInfo.isPageOpened) {
                finishPage(genDataList);
            }
            newPage(genDataList);
            if (bandType == EReportBandType.DETAIL) {
                repeatGroupsOnNewPage(genDataList);
            }
        }

        if (!isFlowDependent()) {
            viewBand(genDataList, band, adjustedCellContents, genData);
            if (genData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
                genTimeInfo.curHeight += band.getHeightMm();
            } else {
                genTimeInfo.curHeightRows += band.getHeightRows();
            }
        } else {
            double curHeightMM = genTimeInfo.curHeight;
            int curHeightRows = genTimeInfo.curHeightRows;
            if (genData.form.getMode() == AdsReportForm.Mode.GRAPHICS) {
                if (isRootReportFooter && !isInfiniteHeight()) {
                    genTimeInfo.curHeight = genData.form.getPageHeightMm() - genData.form.getMargin().getBottomMm() - getFooterStartOffsetFromBottomMm(band);
                }
                viewBand(genDataList, band, adjustedCellContents, genData);
                if (isRootReportFooter || isInfiniteHeight()) {
                    genTimeInfo.curHeight += band.getHeightMm();
                } else {
                    genTimeInfo.curHeight = curHeightMM;
                }
            } else {
                if (isRootReportFooter && !isInfiniteHeight()) {
                    genTimeInfo.curHeightRows = genData.form.getPageHeightRows() - getCurPageFooterHeightRows();
                }
                viewBand(genDataList, band, adjustedCellContents, genData);
                if (isRootReportFooter || isInfiniteHeight()) {
                    genTimeInfo.curHeightRows += band.getHeightRows();
                } else {
                    genTimeInfo.curHeightRows = curHeightRows;
                }
            }
        }

        if (band.getType() != EReportBandType.PAGE_HEADER && band.getType() != EReportBandType.PAGE_FOOTER) {
            genTimeInfo.firstDataBandOnPage = false;
            if (band.isLastOnPage() && !isInfiniteHeight()) {
                if (genTimeInfo.isPageOpened) {
                    finishPage(genDataList);
                }
                newPage(genDataList);
            }
        }

        for (AdsSubReport subReport : band.getPostReports()) {
            buildSubReport(genDataList, subReport);
        }

        bandSizes.assignTo(band);
    }

    protected double lastBandPartHeight = 0;

    private void closeGroupsDownTo(final List<ReportGenData> reportGenDataList, final int curGroupNum) throws ReportGenerationException {
        ReportGenData reportGenData = reportGenDataList.get(reportGenDataList.size() - 1);
        for (int groupNum = reportGenData.form.getGroups().size() - 1; groupNum >= curGroupNum; groupNum--) {
            final GroupInfo groupInfo = reportGenData.groupInfos.get(groupNum);
            if (groupInfo.isOpened()) {
                reportGenData.report.previous();
                final AdsReportGroup group = reportGenData.form.getGroups().get(groupNum);
                buildBand(reportGenDataList, group.getFooterBand(), EReportBandType.GROUP_FOOTER);
                beforeCloseGroup(reportGenData, groupNum);
                collectDataSets(reportGenData, groupNum);
                groupInfo.close();
                groupInfo.getSummary().clear();
                reportGenData.report.next();
                if (fileController != null && separateFileGroupNumber == groupNum) {
                    //close current file and start new one
                    finishPage(reportGenDataList);
                    finishFile(reportGenDataList);
                }
            }
        }
    }

    private void incSummary(final ReportGenData reportGenData, final SummaryCollector summary) {
        for (RadPropDef prop : reportGenData.summaryProps) {
            final IRadPropAccessor accessor = prop.getAccessor();
            if (accessor instanceof IRadPropReadAccessor) {
                final IRadPropReadAccessor readAccessor = (IRadPropReadAccessor) accessor;
                Double value = null;
                final Object valueAsObject = getPropValFromReadAccessor(readAccessor, reportGenData.report, prop.getId());
                if (valueAsObject instanceof Long) {
                    final Long longValue = (Long) valueAsObject;
                    value = new Double(longValue.doubleValue());
                } else if (valueAsObject instanceof BigDecimal) {
                    final BigDecimal bigDecimalValue = (BigDecimal) valueAsObject;
                    value = new Double(bigDecimalValue.doubleValue());
                } else if (valueAsObject instanceof Double) {
                    value = (Double) valueAsObject;
                }
                if (value != null) {
                    summary.addFieldValue(prop.getId(), value.doubleValue());
                }
            }
        }

        summary.incRecordCount();
    }

    private void collectDataSets(final ReportGenData reportGenData, final int groupIndex) {
        for (ChartDataSet chartDataSet : reportGenData.chartDataSets) {
            if (chartDataSet.getGroupIndex() == groupIndex) {
                RadPropDef prop = chartDataSet.getSeriesPropDef();
                final Object seriesValue = prop != null ? getPropValue(reportGenData, prop) : null;
                prop = chartDataSet.getDomainPropDef();
                final Object categoryValue = prop != null ? getPropValue(reportGenData, prop) : null;
                prop = chartDataSet.getRangePropDef();
                final Object valueValue = prop != null ? getPropValue(reportGenData, prop) : null;
                chartDataSet.addDataSetItem(seriesValue, categoryValue, valueValue);
            }
        }
    }

    private Object getPropValue(final ReportGenData reportGenData, RadPropDef prop) {
        final IRadPropAccessor accessor = prop.getAccessor();
        if (accessor instanceof IRadPropReadAccessor) {
            final IRadPropReadAccessor readAccessor = (IRadPropReadAccessor) accessor;
            try {
                return getPropValFromReadAccessor(readAccessor, reportGenData.report, prop.getId());
            } catch (NoConstItemWithSuchValueError ex) {
                return ex.value; // RADIX-4630
            }
        }
        return null;
    }

    protected void validateCellsPosition(final ReportGenData reportGenData) {
        adjustCellsPosition(reportGenData.form.getPageHeaderBands());
        adjustCellsPosition(reportGenData.form.getTitleBands());
        adjustCellsPosition(reportGenData.form.getColumnHeaderBands());
        adjustCellsPosition(reportGenData.form.getDetailBands());
        adjustCellsPosition(reportGenData.form.getSummaryBands());
        adjustCellsPosition(reportGenData.form.getPageFooterBands());
        for (AdsReportGroup group : reportGenData.form.getGroups()) {
            adjustCellsPosition(group.getHeaderBand());
            adjustCellsPosition(group.getFooterBand());
        }
    }

    /**
     * Indicates that band sequence is important to correct report generation
     * For example: this is useful for textual reports In down components safely
     * can be inserted into report body before top components Returns true by
     * default If returns false, footer will be placed in document before
     * details and summary.
     */
    protected boolean isFlowDependent() {
        return true;
    }

    public void removeTmpFiles() {
        for (File file : tmpFiles) {
            FileUtils.deleteFile(file);
        }
        tmpFiles.clear();
    }

    public List<File> getTemporaryFiles() {
        return Collections.unmodifiableList(tmpFiles);
    }

    public void addTmpFile(File file) {
        tmpFiles.add(file);
    }

    private void buildReport(final List<ReportGenData> reportGenDataList) throws ReportGenerationException {
        ReportGenData reportGenData = reportGenDataList.get(reportGenDataList.size() - 1);
        
        adjustCellsToColumns(reportGenData);
        validateCellsPosition(reportGenData);

        reportGenData.report.setSpecialInfo(new SpecialInfo(reportGenData));
        reportGenData.report.setGenTimeInfo(genTimeInfo);

        String resultSetFileName;
        if (reportCallCount.get(reportGenData.report.getId()) != null) {
            resultSetFileName = reportGenData.report.getId().toString() + "_" + (reportCallCount.get(reportGenData.report.getId()) - 1);
        } else {
            resultSetFileName = reportGenData.report.getId().toString();
        }

        ObjectOutputStream resultSetStream = null;
        int resultSetSize = 0;
        if (resultSetInfo != null && !resultSetInfo.isResultSetFileExists(resultSetFileName)) {
            try {
                ResultSetCacheOutputStream resultSetCacheStream = new ResultSetCacheOutputStream(resultSetInfo.createResultSetFile(resultSetFileName), resultSetCacheSizeController);
                GZIPOutputStream gzipResultSetStream = new GZIPOutputStream(resultSetCacheStream);                        
                resultSetStream = new ObjectOutputStream(gzipResultSetStream);
                resultSetInfo.setReportResultSetSize(resultSetFileName, resultSetSize);
            } catch (IOException ex) {
                throw new ReportGenerationException("Unable to create result set temporary file for multiformat report", ex);
            }
        }

        final boolean wasData = reportGenData.report.next();
        for (AdsReportBand title : reportGenData.form.getTitleBands()) {
            buildBand(reportGenDataList, title, EReportBandType.TITLE);
        }
        boolean columnHeadersDisplayed = false;
        boolean isInData = wasData;

        while (isInData) {
            incSummary(reportGenData, reportGenData.totalSummary);
            SummaryCollector prevSummaryCollector = reportGenData.totalSummary;
            boolean groupChanged = false;

            int groupSize = reportGenData.form.getGroups().size();
            for (int groupNum = 0; groupNum < groupSize; groupNum++) {

                final GroupInfo groupInfo = reportGenData.groupInfos.get(groupNum);
                final Object currentGroupConditionValue = reportGenData.report.getGroupCondition(groupNum);
                if (groupInfo.isChanged(currentGroupConditionValue)) {
                    closeGroupsDownTo(reportGenDataList, groupNum);
                    if (!genTimeInfo.isFileOpened) {
                        for (AdsReportBand title : reportGenData.form.getTitleBands()) {
                            buildBand(reportGenDataList, title, EReportBandType.TITLE);
                        }
                        columnHeadersDisplayed = false;
                    }
                    groupInfo.open(currentGroupConditionValue);
                    incSummary(reportGenData, groupInfo.getSummary());
                    prevSummaryCollector.incSubItemCount();

                    if (!genTimeInfo.isPageOpened) {
                        newPage(reportGenDataList);
                    }

                    final AdsReportGroup group = reportGenData.form.getGroups().get(groupNum);
                    buildBand(reportGenDataList, group.getHeaderBand(), EReportBandType.GROUP_HEADER);
                    groupChanged = true;

                } else if (groupInfo.isOpened()) {
                    incSummary(reportGenData, groupInfo.getSummary());
                }
                prevSummaryCollector = groupInfo.getSummary();

            }

            if (!columnHeadersDisplayed || reportGenData.form.isColumnsHeaderForEachGroupDisplayed() && groupChanged) {
                for (AdsReportBand columnHeader : reportGenData.form.getColumnHeaderBands()) {
                    buildBand(reportGenDataList, columnHeader, EReportBandType.COLUMN_HEADER);
                }
                columnHeadersDisplayed = true;
            }

            // do not break if detail band absent, is is possible that required to display calculated summary
            prevSummaryCollector.incSubItemCount();
            //RADIX-10228. page was not opened for CSV reports
            if (!genTimeInfo.isPageOpened) {
                newPage(reportGenDataList);
            }
            AdsReportForm.Bands detailBands = reportGenData.form.getDetailBands();
            if (detailBands.size() > 0) {
                for (AdsReportBand detail : detailBands) {
                    buildBand(reportGenDataList, detail, EReportBandType.DETAIL);
                }
            } else {
                buildWithoutBand(reportGenDataList, EReportBandType.DETAIL);
            }
            collectDataSets(reportGenData, -1);

            isInData = reportGenData.report.next();

            if (resultSetStream != null) {
                ArrayList<Serializable> columns = new ArrayList<>();
                for (Object obj : reportGenData.report.getCache()) {
                    if (obj instanceof Blob) {
                        try {
                            SerialBlob blob = new SerialBlob((Blob) obj);
                            columns.add(blob.getBytes(1, (int) blob.length()));
                        } catch (SQLException ex) {
                            throw new ResultSetCacheWritingException("Unable to write record into report result set temporary file", ex);
                        }
                    } else if (obj instanceof Clob) {
                        try {
                            SerialClob clob = new SerialClob((Clob) obj);
                            columns.add(clob.getSubString(1, (int) clob.length()));
                        } catch (SQLException ex) {
                            throw new ResultSetCacheWritingException("Unable to write record into report result set temporary file", ex);
                        }
                    } else {
                        columns.add((Serializable) obj);
                    }
                }

                SerializedReportFileResultSet.Record record = new SerializedReportFileResultSet.Record(columns);
                try {
                    resultSetStream.writeObject(record);
                    resultSetInfo.setReportResultSetSize(resultSetFileName, ++resultSetSize);
                } catch (IOException ex) {
                    throw new ResultSetCacheWritingException("Unable to write record into report result set temporary file", ex);
                }
            }
        }
        if (resultSetStream != null) {
            try {
                resultSetStream.flush();
                resultSetStream.close();
            } catch (IOException ex) {
                throw new ReportGenerationException("Unable to flush report result set records into temporary file", ex);
            }
        }

        closeGroupsDownTo(reportGenDataList, 0);

        if (wasData) {
            reportGenData.report.previous();
        }
        for (AdsReportBand summary : reportGenData.form.getSummaryBands()) {
            buildBand(reportGenDataList, summary, EReportBandType.SUMMARY);
        }
        if (wasData) {
            reportGenData.report.next();
        }
    }

    public void generateReport(final OutputStream stream) throws ReportGenerationException {
        final File tmpDir = DefaultReportFileController.createTmpDir();
        try {
            generateReport(new IReportFileController() {

                @Override
                public File getDirectory() {
                    return tmpDir;
                }

                @Override
                public String adjustFileName(Report report, String fileName) {
                    return fileName;
                }

                @Override
                public void afterCreateFile(Report report, File file, OutputStream output) {

                }

                @Override
                public void beforeCloseFile(Report report, File file, OutputStream output) {

                }

                @Override
                public void afterCloseFile(Report report, File file) {
                    try {
                        FileInputStream input = new FileInputStream(file);
                        try {
                            FileUtils.copyStream(input, stream);
                        } finally {
                            FileUtils.deleteDirectory(tmpDir);
                        }
                    } catch (IOException ex) {

                    }
                }

            });
        } catch (ReportGenerationException ex) {
            FileUtils.deleteDirectory(tmpDir);
            throw ex;
        }
    }

    public void generateReport(IReportFileController controller) throws ReportGenerationException {
        try {
            this.fileController = controller;
            if (rootGenData.report.isClosed()) {
                throw new IllegalStateException("Attempt to generate report for closed cursor");
            }

            final List<ReportGenData> reportGenDataList = new LinkedList<>();
            reportGenDataList.add(rootGenData);

            calcTableStructure(rootGenData.form);
            buildReport(reportGenDataList);

            if (genTimeInfo.pageNumber == 0) {
                newPage(reportGenDataList);
            }

            if (genTimeInfo.isPageOpened) {
                finishPage(reportGenDataList);
            }
            if (genTimeInfo.isFileOpened) {
                finishFile(reportGenDataList);
            }
        } finally {
            closeAllResources();
        }
    }
    
    protected void closeAllResources() {
    }

    public static int getBandLevel(AdsReportBand band) {
        if (band instanceof AdsReportGroupBand) {
            final AdsReportGroupBand groupBand = (AdsReportGroupBand) band;
            return groupBand.getOwnerGroup().getIndex() + 1;
        } else {
            return 0;
        }
    }

    public static String getCellXmlName(Report report, AdsReportCell cell) {
        switch (cell.getCellType()) {
            case EXPRESSION:
                return cell.getName();
            case PROPERTY:
                final AdsReportPropertyCell propertyCell = (AdsReportPropertyCell) cell;
                return getPropNameById(report, propertyCell.getPropertyId());
            case SPECIAL:
                final AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
                return specialCell.getSpecialType().getValue();
            case SUMMARY:
                final AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
                final String propName = getPropNameById(report, summaryCell.getPropertyId());
                return propName + summaryCell.getSummaryType().getValue();
            default:
                throw new IllegalStateException();
        }
    }

    private static String getPropNameById(Report report, Id propId) {
        final String name = report.getRadMeta().getPropById(propId).getName();
        if (name.length() > 1) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return name.toUpperCase();
        }
    }

    private Object getPropValFromReadAccessor(IRadPropReadAccessor readAccessor, Object owner, Id id) {
        //possibly no such method,so it is probably old style report, invoke previous implementation
        try {
            Method method = readAccessor.getClass().getDeclaredMethod("get", Object.class);
            return method.invoke(readAccessor, owner);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
        }

        return readAccessor.get(owner, id);
    }

    protected void buildWithoutBand(final List<ReportGenData> genDataList, EReportBandType bandType) {
    }

    protected void beforeCloseGroup(final ReportGenData reportGenData, final int groupIndex) {
    }

    protected void afterSubReportClose(final SubReportGenData subReportGenData) {
    }

    private void incReportCallCount(Id reportId) {
        int callCount = reportCallCount.get(reportId) == null ? 0 : reportCallCount.get(reportId);
        reportCallCount.put(reportId, callCount + 1);
    }

    public ReportResultSetInfo getResultSetInfo() {
        return resultSetInfo;
    }

    public void setResultSetInfo(ReportResultSetInfo resultSetInfo) {
        this.resultSetInfo = resultSetInfo;
    }

    public ResultSetCacheSizeController getResultSetCacheSizeController() {
        return resultSetCacheSizeController;
    }

    public void setResultSetCacheSizeController(ResultSetCacheSizeController resultSetCacheSizeController) {
        this.resultSetCacheSizeController = resultSetCacheSizeController;
    }

    public ColumnSettings getColumnsSettings() {
        return columnsSettings;
    }

    public void setColumnsSettings(ColumnSettings columnsSettings) {
        this.columnsSettings = columnsSettings;
    }
}
