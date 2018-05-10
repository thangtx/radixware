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
package org.radixware.kernel.server.types;

import org.radixware.kernel.server.reports.ResultSetCacheSizeController;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.fop.apps.MimeConstants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsXlsxReportInfo;
import org.radixware.kernel.common.enums.EReportExportFormat;
import static org.radixware.kernel.common.enums.EReportExportFormat.XML;
import org.radixware.kernel.common.enums.EReportSummaryCellType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.NullOutputStream;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.AbstractReportGenerator.SpecialInfo;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.IReportPreviewEnvironment;
import org.radixware.kernel.server.reports.PdfSecurityOptions;
import org.radixware.kernel.server.reports.PoiReportGenerator;
import org.radixware.kernel.server.reports.RadReportColumnDef;
import org.radixware.kernel.server.reports.ReportExportParameters;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportResultSetInfo;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.server.reports.SerializedReportFileResultSet;
import org.radixware.kernel.server.reports.csv.CsvReportGenerator;
import org.radixware.kernel.server.reports.csv.CsvReportWriter;
import org.radixware.kernel.server.reports.fo.FopReportGenerator;
import org.radixware.kernel.server.reports.txt.TxtReportGenerator;
import org.radixware.kernel.server.reports.xlsx.XlsxReportGenerator;
import org.radixware.kernel.server.reports.xml.CustomReportGenerator;
import org.radixware.kernel.server.reports.xml.IReportWriter;
import org.radixware.kernel.server.reports.xml.MsdlReportGenerator;
import org.radixware.kernel.server.reports.xml.XmlReportGenerator;
import org.radixware.schemas.reports.ColumnSettings;
import org.radixware.schemas.reports.ReportColumnsList;

public abstract class Report extends Cursor implements IRadClassInstance {

    private static final int DEFAULT_MAX_RESULT_SET_CACHE_SIZE_KB = 50 * 1024;
    protected static final ThreadLocal<IReportPreviewEnvironment> PREVIEW_ENVIRONMENT = new ThreadLocal<>();

    protected Report(final Arte arte) { // for unit tests
        super(arte);
    }

    protected Report() {
        this(null);
    }

    public abstract Id getId();

    public static void setPreviewEnvironment(IReportPreviewEnvironment env) {
        PREVIEW_ENVIRONMENT.set(env);
    }

    public static IReportPreviewEnvironment getPreviewEnvironment() {
        return PREVIEW_ENVIRONMENT.get();
    }

    private static void disableWarningsOfClass(String className) {
        org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(className);
        if (log instanceof org.apache.commons.logging.impl.Jdk14Logger) {
            org.apache.commons.logging.impl.Jdk14Logger jdk14Logger = (org.apache.commons.logging.impl.Jdk14Logger) log;
            jdk14Logger.getLogger().setLevel(java.util.logging.Level.OFF);
        } else if (log instanceof org.apache.commons.logging.impl.Log4JLogger) {
            org.apache.commons.logging.impl.Log4JLogger log4JLogger = (org.apache.commons.logging.impl.Log4JLogger) log;
            //log4JLogger.getLogger().setLevel(java.util.logging.Level.OFF);
        }
    }

    static {
        //disableWarningsOfClass("org.apache.fop.layoutmgr.PageBreakingAlgorithm");
        //disableWarningsOfClass("org.apache.fop.layoutmgr.BreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.fonts.autodetect.FontInfoFinder");
        disableWarningsOfClass("org.apache.fop.fonts.truetype.TTFFile");
    }

    protected boolean isDoublePass() {
        return false;
    }

    public boolean isMultyFile() {
        return separateFilesGroupNumber() >= 0;
    }

    protected int separateFilesGroupNumber() {
        return -1;
    }

    private boolean dbImageResizeSupressed = false;
    protected ColumnSettings columnSetting = null;
    protected int maxResultSetCacheSizeKb = DEFAULT_MAX_RESULT_SET_CACHE_SIZE_KB;

    public void setMaxResultSetCacheSizeKb(Integer maxResultSetCacheSizeKb) {
        this.maxResultSetCacheSizeKb = maxResultSetCacheSizeKb != null ? maxResultSetCacheSizeKb : DEFAULT_MAX_RESULT_SET_CACHE_SIZE_KB;
    }

    public Integer getMaxResultSetCacheSizeKb() {
        return this.maxResultSetCacheSizeKb;
    }

    public boolean isDbImageResizeSupressed() {
        return dbImageResizeSupressed;
    }

    protected void setDbImageResizeSupressed(boolean dbImageResizeSupressed) {
        this.dbImageResizeSupressed = dbImageResizeSupressed;
    }

    

    /**
     * Execute report and export it in output stream
     */
    protected void doExport(final OutputStream stream, final ReportGeneratorFactory factory) {
        if (isMultyFile()) {
            throw new RadixError("Unable to generate multy-file report to single stream");
        }
        OutputStream realOut;
        if (isDoublePass()) {
            realOut = new NullOutputStream();
        } else {
            realOut = stream;
        }

        currentExportFormat = factory.getGenerationFormat();
        try {
            AbstractReportGenerator reportGenerator = factory.createGenerator(null);
            reportGenerator.setColumnsSettings(columnSetting);
            try {
                reportGenerator.generateReport(realOut);
            } catch (ReportGenerationException ex) {
                throw new RadixError("Unable to generate report.", ex);
            }
            if (isDoublePass()) {
                beforeFirst();
                reportGenerator = factory.createGenerator(reportGenerator.getStateInfo());
                reportGenerator.setColumnsSettings(columnSetting);
                try {
                    reportGenerator.generateReport(stream);
                } catch (ReportGenerationException ex) {
                    throw new RadixError("Unable to generate report.", ex);
                }
            }
        } finally {
            currentExportFormat = null;
        }
    }

    protected void doExport(final IReportFileController controller, final ReportGeneratorFactory factory) {
        if (factory instanceof MultiformatReportGeneratorFactory) {
            MultiformatReportGeneratorFactory generationFactory = (MultiformatReportGeneratorFactory) factory;

            ReportResultSetInfo info = new ReportResultSetInfo();
            ResultSetCacheSizeController resultSetCacheSizeController = new ResultSetCacheSizeController(maxResultSetCacheSizeKb * 1024);
            try {
                info.setReportResultColumnDescriptors(getId().toString(), getResultSet().getMetaData());
                while (generationFactory.hasNext()) {
                    if (info.isResultSetFileExists(getId().toString())) {
                        ResultSet resultSet = new SerializedReportFileResultSet(
                                info.getResultSetFile(getId().toString()),
                                getColumnNames(),
                                info.getReportResultColumnDescriptors(getId().toString()),
                                info.getReportResultSetSize(getId().toString())
                        );
                        open(resultSet);
                    }

                    currentExportFormat = generationFactory.getNextGenerationFormat();

                    AbstractReportGenerator reportGenerator = generationFactory.nextGenerator(null);
                    reportGenerator.setResultSetInfo(info);
                    reportGenerator.setResultSetCacheSizeController(resultSetCacheSizeController);

                    generateReport(reportGenerator, factory, controller);
                }
            } catch (IOException | SQLException ex) {
                throw new RadixError("Unable to generate report.", ex);
            } finally {
                if (getResultSet() != null && getResultSet() instanceof SerializedReportFileResultSet) {
                    try {
                        getResultSet().close();
                    } catch (SQLException ex) {
                        throw new RadixError("Unable to generate report.", ex);
                    }
                }
                info.deleteResultSetContentDir();
                currentExportFormat = null;
            }
        } else {
            AbstractReportGenerator reportGenerator = factory.createGenerator(null);
            currentExportFormat = factory.getGenerationFormat();

            try {
                generateReport(reportGenerator, factory, controller);
            } finally {
                currentExportFormat = null;
            }
        }
    }

    private void generateReport(AbstractReportGenerator reportGenerator, ReportGeneratorFactory factory, IReportFileController fileController) {
        boolean isDoublePassFormat = factory.getGenerationFormat() != EReportExportFormat.XLSX && factory.getGenerationFormat() != EReportExportFormat.CSV;
        DefaultReportFileController tmpController = isDoublePassFormat ? new DefaultReportFileController(fileController, isDoublePass()) : new DefaultReportFileController(fileController, false);

        reportGenerator.setColumnsSettings(columnSetting);
        reportGenerator.setSeparateFileGroupLevel(separateFilesGroupNumber());
        try {
            reportGenerator.generateReport(tmpController);
        } catch (ReportGenerationException ex) {
            throw new RadixError("Unable to generate report.", ex);
        }

        if (isDoublePass() && isDoublePassFormat) {
            beforeFirst();
            tmpController = new DefaultReportFileController(fileController, false);
            reportGenerator = factory.createGenerator(reportGenerator.getStateInfo());
            reportGenerator.setColumnsSettings(columnSetting);
            reportGenerator.setSeparateFileGroupLevel(separateFilesGroupNumber());
            try {
                reportGenerator.generateReport(tmpController);
            } catch (ReportGenerationException ex) {
                throw new RadixError("Unable to generate report.", ex);
            }
        }
    }

    public String[] getColumnNames() throws SQLException {
        int columnCount = 0;
        if (getResultSet() != null && getResultSet().getMetaData() != null) {
            columnCount = getResultSet().getMetaData().getColumnCount();
        }

        String[] result = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            result[i] = getResultSet().getMetaData().getColumnName(i + 1);
        }

        return result;
    }

    //-
    protected void export(final OutputStream stream, final EReportExportFormat exportFormat) {
        export(stream, exportFormat, null);
    }

    protected void export(final IReportFileController controller, final EReportExportFormat exportFormat) {
        export(controller, exportFormat, null);
    }

    //-
    protected void export(final OutputStream stream, final EReportExportFormat exportFormat, String encoding) {
        doExport(stream, new DefaultReportGeneratorFactory(this, exportFormat, encoding));
    }

    protected void export(final IReportFileController controller, final EReportExportFormat exportFormat, String encoding) {
        doExport(controller, new DefaultReportGeneratorFactory(this, exportFormat, encoding));
    }

    //-
    public void export(final OutputStream stream, Map<Id, Object> paramId2Value, EReportExportFormat exportFormat) {
        export(stream, paramId2Value, exportFormat, null);
    }

    public void export(final IReportFileController controller, Map<Id, Object> paramId2Value, EReportExportFormat exportFormat) {
        export(controller, paramId2Value, exportFormat, null);
    }

    public void export(final IReportFileController controller, Map<Id, Object> paramId2Value, List<EReportExportFormat> exportFormats, Integer maxResultSetCacheSizeKb) {
        export(controller, paramId2Value, exportFormats, maxResultSetCacheSizeKb, null);
    }

    //-
    public void export(final OutputStream stream, Map<Id, Object> paramId2Value, EReportExportFormat exportFormat, String encoding) {
        export(stream, paramId2Value, new DefaultReportGeneratorFactory(this, exportFormat, encoding));
    }

    public void export(final IReportFileController controller, Map<Id, Object> paramId2Value, EReportExportFormat exportFormat, String encoding) {
        export(controller, paramId2Value, new DefaultReportGeneratorFactory(this, exportFormat, encoding));
    }

    public void export(final IReportFileController controller, Map<Id, Object> paramId2Value, List<EReportExportFormat> exportFormats, Integer maxResultSetCacheSizeKb, String encoding) {
        setMaxResultSetCacheSizeKb(maxResultSetCacheSizeKb);
        export(controller, paramId2Value, new MultiformatReportGeneratorFactory(this, exportFormats, encoding));
    }
    //-

    public void export(final OutputStream stream, Map<Id, Object> paramId2Value, final IReportWriter reportWriter) {
        export(stream, paramId2Value, new ReportGeneratorFactory() {
            @Override
            public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                return new CustomReportGenerator(Report.this, reportWriter, stateInfo);
            }

            @Override
            public EReportExportFormat getGenerationFormat() {
                return EReportExportFormat.CUSTOM;
            }
        });
    }

    public void export(final IReportFileController controller, Map<Id, Object> paramId2Value, final IReportWriter reportWriter) {
        export(controller, paramId2Value, new ReportGeneratorFactory() {
            @Override
            public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                return new CustomReportGenerator(Report.this, reportWriter, stateInfo);
            }

            @Override
            public EReportExportFormat getGenerationFormat() {
                return EReportExportFormat.CUSTOM;
            }
        });
    }

    public void export(final OutputStream stream, final Map<Id, Object> paramId2Value, final ReportGeneratorFactory generatorFactory) {
        export(stream, paramId2Value, generatorFactory, null);
    }

    public void export(final IReportFileController controller, final Map<Id, Object> paramId2Value, final ReportGeneratorFactory generatorFactory) {
        export(controller, paramId2Value, generatorFactory, null);
    }

    public void export(final ReportExportParameters parameters) {
        ReportGeneratorFactory generatorFactory = null;
        if (parameters.getExportFormat() != null) {
            generatorFactory = new DefaultReportGeneratorFactory(this, parameters.getExportFormat(), parameters.getEncoding());
        }
        if (parameters.getExportFormats() != null) {
            generatorFactory = new MultiformatReportGeneratorFactory(this, parameters.getExportFormats(), parameters.getEncoding());
        }
        if (parameters.getReportWriter() != null) {
            generatorFactory = new ReportGeneratorFactory() {
                @Override
                public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                    return new CustomReportGenerator(Report.this, parameters.getReportWriter(), stateInfo);
                }

                @Override
                public EReportExportFormat getGenerationFormat() {
                    return EReportExportFormat.CUSTOM;
                }
            };
        }
        if (parameters.getReportMsdl() != null) {
            generatorFactory = new ReportGeneratorFactory() {
                @Override
                public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                    return new MsdlReportGenerator(Report.this, parameters.getReportMsdl(), stateInfo);
                }

                @Override
                public EReportExportFormat getGenerationFormat() {
                    return EReportExportFormat.MSDL;
                }
            };
        }
        if (generatorFactory == null) {
            throw new IllegalArgumentException("Unable to determine report export format. Parameter exportFormat or exportFormats or reportWriter or reportMsdl should be defined");
        }

        if (parameters.getMaxResultSetCacheSizeKb() != null) {
            setMaxResultSetCacheSizeKb(parameters.getMaxResultSetCacheSizeKb());
        }

        if (parameters.getColumnSettings() != null) {
            columnSetting = parameters.getColumnSettings();
        }

        try {
            open(parameters.getParamId2Value());
            if (parameters.getController() != null) {
                doExport(parameters.getController(), generatorFactory);
            } else {
                doExport(parameters.getStream(), generatorFactory);
            }
        } finally {
            close();
            afterExecute();
            columnSetting = null;
        }
    }

    public void export(final OutputStream stream, final Map<Id, Object> paramId2Value, final ReportGeneratorFactory generatorFactory, final String encoding) {
        try {
            open(paramId2Value);
            doExport(stream, generatorFactory);
        } finally {
            close();
            afterExecute();
        }
    }

    public void export(final IReportFileController controller, final Map<Id, Object> paramId2Value, final ReportGeneratorFactory generatorFactory, final String encoding) {
        try {
            open(paramId2Value);
            doExport(controller, generatorFactory);
        } finally {
            close();
            afterExecute();
        }
    }

    public void execute(final OutputStream stream, final EReportExportFormat mimeType, Map<Id, Object> paramId2Value) {
        execute(stream, mimeType, null, paramId2Value);
    }

    public void execute(final OutputStream stream, final EReportExportFormat mimeType, String encoding, Map<Id, Object> paramId2Value) {
        execute(stream, paramId2Value, new DefaultReportGeneratorFactory(this, mimeType, encoding));
    }

    public void execute(final OutputStream stream, Map<Id, Object> paramId2Value, final org.radixware.schemas.reports.ReportMsdlType xReportMsdl) {
        execute(stream, paramId2Value, new ReportGeneratorFactory() {
            @Override
            public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                return new MsdlReportGenerator(Report.this, xReportMsdl, stateInfo);
            }

            @Override
            public EReportExportFormat getGenerationFormat() {
                return EReportExportFormat.MSDL;
            }
        });
    }

    public void execute(final OutputStream stream, Map<Id, Object> paramId2Value, final IReportWriter reportWriter) {
        execute(stream, paramId2Value, new ReportGeneratorFactory() {
            @Override
            public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                return new CustomReportGenerator(Report.this, reportWriter, stateInfo);
            }

            @Override
            public EReportExportFormat getGenerationFormat() {
                return EReportExportFormat.CUSTOM;
            }
        });
    }

    //-
    public void execute(final IReportFileController controller, final EReportExportFormat mimeType, Map<Id, Object> paramId2Value) {
        execute(controller, mimeType, null, paramId2Value);
    }

    public void execute(final IReportFileController controller, final List<EReportExportFormat> mimeTypes, Map<Id, Object> paramId2Value, Integer maxResultSetCacheSizeKb) {
        execute(controller, mimeTypes, null, paramId2Value, maxResultSetCacheSizeKb);
    }

    public void execute(final IReportFileController controller, final EReportExportFormat mimeType, String encoding, Map<Id, Object> paramId2Value) {
        execute(controller, paramId2Value, new DefaultReportGeneratorFactory(this, mimeType, encoding));
    }

    public void execute(final IReportFileController controller, final List<EReportExportFormat> mimeTypes, String encoding, Map<Id, Object> paramId2Value, Integer maxResultSetCacheSizeKb) {
        setMaxResultSetCacheSizeKb(maxResultSetCacheSizeKb);
        execute(controller, paramId2Value, new MultiformatReportGeneratorFactory(this, mimeTypes, encoding));
    }

    public void execute(final IReportFileController controller, Map<Id, Object> paramId2Value, final org.radixware.schemas.reports.ReportMsdlType xReportMsdl) {
        execute(controller, paramId2Value, new ReportGeneratorFactory() {
            @Override
            public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                return new MsdlReportGenerator(Report.this, xReportMsdl, stateInfo);
            }

            @Override
            public EReportExportFormat getGenerationFormat() {
                return EReportExportFormat.MSDL;
            }
        });
    }

    public void execute(final IReportFileController controller, Map<Id, Object> paramId2Value, final IReportWriter reportWriter) {
        execute(controller, paramId2Value, new ReportGeneratorFactory() {
            @Override
            public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
                return new CustomReportGenerator(Report.this, reportWriter, stateInfo);
            }

            @Override
            public EReportExportFormat getGenerationFormat() {
                return EReportExportFormat.CUSTOM;
            }
        });
    }

    public abstract AdsReportForm createForm();

    //public abstract Map<Id,String> getExportCsvColumns();
    public AdsCsvReportInfo getExportCsvInfo() {
        return null;
    }

    public boolean isCsvRowVisible() {
        return true;
    }

    public AdsXlsxReportInfo getExportXlsxInfo() {
        return null;
    }

    public boolean isXlsxRowVisible() {
        return true;
    }

    public List<RadReportColumnDef> getDeclaredColumns() {
        return null;
    }

    public List<RadReportColumnDef> getVisibleColumns() {
        if (columnSetting == null || !columnSetting.getIsUsed() || currentExportFormat == EReportExportFormat.OOXML) {
            return getDeclaredColumns();
        }

        List<RadReportColumnDef> result = new ArrayList<>();
        if (columnSetting.getVisibleColumns() == null) {
            return result;
        }

        for (RadReportColumnDef column : getDeclaredColumns()) {
            for (ReportColumnsList.Column xColumn : columnSetting.getVisibleColumns().getColumnList()) {
                if (xColumn.getColumnId() == column.getId()) {
                    result.add(column);
                }
            }
        }

        return result;
    }

    public int afterXlsxRowWrite(Row row) {
        return 0;
    }

    public int beforeXlsxRowWrite(Sheet sheet) {
        return 0;
    }

    public int afterLastXlsxRowWrite(Sheet sheet) {
        return 0;
    }

    public int beforeFirstXlsxRowWrite(Sheet sheet) {
        return 0;
    }

    public void beforeInputParams() {
    }

    protected void beforeExecute() {
    }

    protected void afterExecute() {
    }

    public abstract Object getGroupCondition(int groupIndex);

    public void execute(final OutputStream stream, final Map<Id, Object> paramId2Value, final ReportGeneratorFactory generatorFactory) {
        export(stream, paramId2Value, generatorFactory);
    }

    public void execute(final IReportFileController controller, final Map<Id, Object> paramId2Value, final ReportGeneratorFactory generatorFactory) {
        export(controller, paramId2Value, generatorFactory);
    }

    // Used by open() and before checkSuitability().
    public abstract void assignParamValues(Map<Id, Object> paramId2Value);

    public abstract void open(Map<Id, Object> paramId2Value);

    public boolean checkSuitability() {
        return true;
    }

    public abstract Id getContextParameterId();

    public Id getContextClassId() {
        final Id contextParameterId = getContextParameterId();
        if (contextParameterId == null) {
            return null;
        }
        final RadPropDef prop = getRadMeta().getPropById(contextParameterId);
        if (prop instanceof IRadRefPropertyDef) {
            final IRadRefPropertyDef ref = (IRadRefPropertyDef) prop;
            return ref.getDestinationClassId();
        } else {
            throw new IllegalUsageError("Context parameter #" + contextParameterId + " must me reference.");
        }
    }

    // ========= RADIX-11868: open cursor with test ResultSet =============
    @Override
    protected void open(PreparedStatement statement, ResultSet resultSet) {
        super.open(statement, Report.getPreviewEnvironment() == null ? resultSet : Report.getPreviewEnvironment().getTestResultSet(this));
    }
    //
    // ========= RADIX-3572: RETURN NULL IF NO DATA =============
    private boolean wasData = false;
    // ========= RADIX-3841: cache one record ===========
    private boolean prev = false;
    private Object[] cache = null;
    private PdfSecurityOptions pdfSecurityOptions;

    @Override
    public boolean next() {
        if (prev) {
            prev = false;
            return true;
        }
        if (wasData) {
            try {
                int fieldCount = getResultSet().getMetaData().getColumnCount();
                cache = new Object[fieldCount];
                for (int i = 0; i < fieldCount; i++) {
                    try {
                        cache[i] = getResultSet().getTimestamp(i + 1);
                    } catch (Exception ex) {
                        cache[i] = getResultSet().getObject(i + 1);
                    }
                }
            } catch (SQLException ex) {
                throw new DatabaseError(ex);
            }
        }
        wasData = super.next();
        return wasData;
    }

    @Override
    public boolean previous() {
        if (prev) {
            throw new IllegalStateException("Attempt to move to report previous record twice");
        }
        if (cache == null) {
            throw new IllegalStateException("Attempt to move to undeterminied previous record of report");
        }
        prev = true;
        return true;
    }

    public Object[] getCache() {
        return cache;
    }

    public boolean isWasData() {
        return wasData;
    }

    @Override
    protected BigDecimal getBigDecimal(int idx) {
        if (prev) {
            return (BigDecimal) cache[idx - 1];
        } else if (wasData) {
            return super.getBigDecimal(idx);
        } else {
            return null;
        }
    }

    @Override
    protected BigDecimal getBigDecimal(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Bin getBin(int idx) {
        if (prev) {
            return (Bin) cache[idx - 1];
        } else if (wasData) {
            return super.getBin(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Bin getBin(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Blob getBlob(int idx) {
        if (prev) {
            return (Blob) cache[idx - 1];
        } else if (wasData) {
            return super.getBlob(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Blob getBlob(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Boolean getBoolean(int idx) {
        if (prev) {
            final Object obj = cache[idx - 1];
            if (obj instanceof BigDecimal) {
                return !BigDecimal.ZERO.equals((BigDecimal) obj);
            } else {
                return (Boolean) obj;
            }
        } else if (wasData) {
            return super.getBoolean(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Boolean getBoolean(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Character getChar(int idx) {
        if (prev) {
            final Object obj = cache[idx - 1];
            if (obj instanceof String) {
                return ((String) obj).charAt(0);
            } else {
                return (Character) obj;
            }
        } else if (wasData) {
            return super.getChar(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Character getChar(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Clob getClob(int idx) {
        if (prev) {
            return (Clob) cache[idx - 1];
        } else if (wasData) {
            return super.getClob(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Clob getClob(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Long getLong(int idx) {
        if (prev) {
            final Object obj = cache[idx - 1];
            if (obj instanceof BigDecimal) {
                return ((BigDecimal) obj).longValue();
            } else {
                return (Long) cache[idx - 1];
            }
        } else if (wasData) {
            return super.getLong(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Long getLong(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getString(int idx) {
        if (prev) {
            return (String) cache[idx - 1];
        } else if (wasData) {
            return super.getString(idx);
        } else {
            return null;
        }
    }

    @Override
    protected String getString(String fieldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Timestamp getTimestamp(int idx) {
        if (prev) {
            return (Timestamp) cache[idx - 1];
        } else if (wasData) {
            return super.getTimestamp(idx);
        } else {
            return null;
        }
    }

    @Override
    protected Timestamp getTimestamp(String fieldName) {
        throw new UnsupportedOperationException();
    }

    public interface IGenTimeInfo {

        public int getCurrentPageNumber();

        public Timestamp getGenerationTime();
    }
    private IGenTimeInfo genTimeInfo = null;

    public void setGenTimeInfo(IGenTimeInfo genTimeInfo) {
        this.genTimeInfo = genTimeInfo;
    }

    /**
     * Get current page number for using during report document generation. 1 -
     * first page, 0 - no generated pages.
     */
    public Integer getCurrentPageNumber() {
        if (genTimeInfo != null) {
            return genTimeInfo.getCurrentPageNumber();
        } else {
            return 0;
        }
    }

    public Timestamp getGenerationTime() {
        if (genTimeInfo != null) {
            return genTimeInfo.getGenerationTime();
        }
        return null;
    }

    private EReportExportFormat currentExportFormat = null;

    public EReportExportFormat getCurrentExportFormat() {
        return currentExportFormat;
    }

    public BigDecimal getCellsSumValue(EReportSummaryCellType summaryType, Id propertyId, int groupLevel) {
        if (specialInfo != null) {
            return new BigDecimal(specialInfo.getSummaryCellValue(summaryType, propertyId, groupLevel));
        }
        return new BigDecimal(0);
    }

    public Integer getSubItemCount(int groupLevel) {
        if (specialInfo != null) {
            return specialInfo.getSubItemCount(groupLevel);
        }
        return 0;

    }

    public Integer getCurGroupRecordCount() {
        if (specialInfo != null) {
            return specialInfo.getCurGroupRecordCount();
        }
        return 0;

    }

    public Integer getTotalRecordCount() {
        if (specialInfo != null) {
            return specialInfo.getTotalRecordCount();
        }
        return 0;

    }

    //
    //Implementation of IRadClassInstance
    //
    @Override
    public Object getProp(Id id) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropReadAccessor) {
                return ((IRadPropReadAccessor) prop.getAccessor()).get(this, id);
            } else {
                throw new IllegalUsageError("Property #" + id + " has no read accessor");
            }
        }
        throw new DefinitionNotFoundError(id);
    }

    @Override
    public String onCalcParentTitle(Id parentTitlePropId, Entity parent, String title) {
        return title;
    }

    @Override
    public void setProp(Id id, Object x) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                ((IRadPropWriteAccessor) prop.getAccessor()).set(this, id, x);
            } else {
                throw new IllegalUsageError("Property #" + id + " has no write accessor");
            }
        } else {
            throw new DefinitionNotFoundError(id);
        }
    }
    private SpecialInfo specialInfo = null;

    public void setSpecialInfo(SpecialInfo specialInfo) {
        this.specialInfo = specialInfo;
    }

    //public SpecialInfo getSpecialInfo() {
    //    return specialInfo;
    //}   
    public static interface ReportGeneratorFactory {

        public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo);

        public EReportExportFormat getGenerationFormat();

    }

    private static class DefaultReportGeneratorFactory implements ReportGeneratorFactory {

        private final Report report;
        private final EReportExportFormat format;
        private final String encoding;

        public DefaultReportGeneratorFactory(Report report, EReportExportFormat format, String encoding) {
            this.report = report;
            this.format = format;
            this.encoding = encoding;
        }

        @Override
        public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
            switch (format) {
                case CSV:
                    return new CsvReportGenerator(report, encoding, stateInfo);
                case PDF:
                    return new FopReportGenerator(report, MimeConstants.MIME_PDF, format, stateInfo);
                case RTF:
                    return new FopReportGenerator(report, MimeConstants.MIME_RTF, format, stateInfo);
                case XML:
                    return new XmlReportGenerator(report, stateInfo);
                case OOXML:
                    return new PoiReportGenerator(report, stateInfo);
                case TXT:
                    return new TxtReportGenerator(report, encoding, stateInfo);
                case XLSX:
                    return new XlsxReportGenerator(report, stateInfo);
                default:
                    throw new IllegalArgumentException("Unsupported report export format: " + String.valueOf(format));
            }
        }

        @Override
        public EReportExportFormat getGenerationFormat() {
            return format;
        }
    }

    private static class MultiformatReportGeneratorFactory implements ReportGeneratorFactory {

        private final Report report;
        private final List<EReportExportFormat> formats;
        private final String encoding;

        private int currentFormatIndex = -1;

        public MultiformatReportGeneratorFactory(Report report, List<EReportExportFormat> formats, String encoding) {
            this.report = report;
            this.formats = formats;
            this.encoding = encoding;
        }

        @Override
        public EReportExportFormat getGenerationFormat() {
            return currentFormatIndex == -1 ? null : formats.get(currentFormatIndex);
        }

        @Override
        public AbstractReportGenerator createGenerator(ReportStateInfo stateInfo) {
            return currentFormatIndex == -1 ? null : createGenerator(formats.get(currentFormatIndex), stateInfo);
        }

        public AbstractReportGenerator createGenerator(EReportExportFormat format, ReportStateInfo stateInfo) {
            switch (format) {
                case CSV:
                    return new CsvReportGenerator(report, encoding, stateInfo);
                case PDF:
                    return new FopReportGenerator(report, MimeConstants.MIME_PDF, format, stateInfo);
                case RTF:
                    return new FopReportGenerator(report, MimeConstants.MIME_RTF, format, stateInfo);
                case XML:
                    return new XmlReportGenerator(report, stateInfo);
                case OOXML:
                    return new PoiReportGenerator(report, stateInfo);
                case TXT:
                    return new TxtReportGenerator(report, encoding, stateInfo);
                case XLSX:
                    return new XlsxReportGenerator(report, stateInfo);
                default:
                    throw new IllegalArgumentException("Unsupported report export format: " + String.valueOf(format));
            }
        }

        public AbstractReportGenerator nextGenerator(ReportStateInfo stateInfo) {
            currentFormatIndex = currentFormatIndex + 1 >= formats.size() ? currentFormatIndex : currentFormatIndex + 1;
            return createGenerator(stateInfo);
        }

        public boolean hasNext() {
            return currentFormatIndex + 1 < formats.size() && !formats.isEmpty();
        }

        public EReportExportFormat getNextGenerationFormat() {
            int nextformatIndex = currentFormatIndex + 1 >= formats.size() ? currentFormatIndex : currentFormatIndex + 1;
            return formats.get(nextformatIndex);
        }
    }

    public String adjustCsvColumnValue(String rawValue, String escapeValue, char delimiterChar) {
        return escapeValue;
    }

    public void configureCsvWriter(final CsvReportWriter.Writer writer) {

    }

    private static final Id MLS_ID_OF_OWNER = Id.Factory.loadFrom("adcXCB5KK6HMJH7NP6E642OHPOMXY");
    private static final Id MLS_ID_OF = Id.Factory.loadFrom("mlsZKPNTZU64FDZTDB3FFEAXCTYYU");

    public String calcOutputFileName(int fileNumber, EReportExportFormat exportFormat, Long totalFileCount) {
        final String ext;
        switch (exportFormat) {
            case CSV:
                ext = ".csv";
                break;
            case OOXML:
                ext = ".xls";
                break;
            case XLSX:
                ext = ".xlsx";
                break;
            case PDF:
                ext = ".pdf";
                break;
            case RTF:
                ext = ".rtf";
                break;
            case TXT:
                ext = ".txt";
                break;
            case XML:
                ext = ".xml";
                break;
            default:
                ext = ".rep";
                break;
        }

        StringBuilder fileName = new StringBuilder();
        fileName.append(getRadMeta().getTitle());
        if (isMultyFile()) {
            fileName.append(" ").append(fileNumber);
            if (totalFileCount != null) {
                String of = MultilingualString.get(Arte.get(), MLS_ID_OF_OWNER, MLS_ID_OF);
                fileName.append(" ").append(of).append(" ").append(totalFileCount);
            }
        }
        return FileUtils.string2UniversalFileName(fileName.toString(), '_') + ext;
    }

    public PdfSecurityOptions getPdfSecurityOptions() {
        return pdfSecurityOptions;
    }

    public void setPdfSecurityOptions(PdfSecurityOptions pdfSecurityOptions) {
        this.pdfSecurityOptions = pdfSecurityOptions;
    }

    protected void buildCsvExportColumns(final AdsCsvReportInfo.CsvExportColumns csvColumns) {
        for (RadReportColumnDef column : getVisibleColumns()) {
            if (column.getPropertyId() != null) {
                String columnName = column.getTitle() == null ? column.getLegacyCsvName() : column.getTitle();
                if (columnName == null) {
                    RadPropDef prop = getRadMeta().getPropById(column.getPropertyId());
                    if (prop != null) {
                        columnName = prop.getName();
                    }
                }
                AdsExportCsvColumn csvColumn = new AdsExportCsvColumn(column.getPropertyId(), columnName, column.getCsvExportFormat());
                csvColumns.add(csvColumn);
            }
        }
    }
}
