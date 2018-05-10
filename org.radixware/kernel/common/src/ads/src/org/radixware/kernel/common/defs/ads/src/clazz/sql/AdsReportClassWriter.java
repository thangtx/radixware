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
package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginMm;
import java.awt.Color;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.*;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginTxt;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.JavaType;
import org.radixware.kernel.common.defs.ads.type.RadixType;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;

public class AdsReportClassWriter extends AdsSqlClassWriter<AdsReportClassDef> {

    private final AdsReportClassDef report;

    public AdsReportClassWriter(JavaSourceSupport support, AdsReportClassDef report, UsagePurpose usagePurpose) {
        super(support, report, usagePurpose);
        this.report = report;
    }

    @Override
    protected void writeCustomHeader(CodePrinter cp) {
        super.writeCustomHeader(cp);
        cp.println("import org.radixware.kernel.common.defs.ads.clazz.sql.report.*;");
        cp.println("import java.awt.Color;");
    }

    @Override
    protected void writeCustomBody(CodePrinter cp) {
        super.writeCustomBody(cp);

        if (report.walkTwiceOnExport()) {
            cp.println("@Override");
            cp.println("protected boolean isDoublePass() {");
            cp.println("    return true;\n");
            cp.println("}");
        }
        if (report.getForm().getMultifileGroupLevel() >= 0) {
            cp.println("@Override");
            cp.println("protected int separateFilesGroupNumber() {");
            cp.print("    return ");
            cp.print(report.getForm().getMultifileGroupLevel());
            cp.printlnSemicolon();
            cp.println("}");
        }

        //write uds wrappers
        if (report instanceof AdsUserReportClassDef) {
            final List<AdsDefinition> wrappers = ((AdsUserReportClassDef) report).getUsedWrappers();
            for (AdsDefinition wd : wrappers) {
                if (wd instanceof IJavaSource) {
                    ((IJavaSource) wd).getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(cp);
                }
            }
        }

        cp.println("@Override");
        cp.print("public ");
        cp.print(WriterUtils.RADIX_ID_CLASS_NAME);

        cp.print(" getId(){ return ");
        WriterUtils.writeIdUsage(cp, def.getId());
        cp.println(";}");
        final AdsReportForm form = report.getForm();
        for (AdsReportBand band : form.getBands()) {
            writeBandClass(band, cp);
            writeCellClasses(band, cp);
        }

        writeGroupConditionMethod(form.getGroups(), cp);
        writeFormMethod(form, cp);

        writeGetDeclaredColumnsMethod(cp);

        if (report.getCsvInfo()
                != null && report.getCsvInfo().isExportToCsvEnabled()) {
            final AdsCsvReportInfo csvInfo = report.getCsvInfo();
            writeCsvExportColumnsMethod(cp, csvInfo);
            if (csvInfo.isExportToCsvEnabled() && csvInfo.getRowCondition() != null) {
                writeCsvRowCondition(csvInfo, cp);
            }
        }

        if (report.getXlsxReportInfo() != null && report.getXlsxReportInfo().isExportToXlsxEnabled()) {
            final AdsXlsxReportInfo xlsxInfo = report.getXlsxReportInfo();
            writeGetExportXlsxInfoMethod(cp, xlsxInfo);
            if (xlsxInfo.isExportToXlsxEnabled() && xlsxInfo.getRowCondition() != null) {
                writeXlsxRowCondition(xlsxInfo, cp);
            }
        }

        writeAssignParamValuesMethod(cp);

        writeOpenMapMethod(cp);
        //writeExecuteMethod(cp);
        //writeOpenMethod(cp);

        writeGetContextParameterIdMethod(cp);
    }

    private void writeCellClasses(IReportWidgetContainer cellContainer, CodePrinter cp) {
        for (AdsReportWidget w : cellContainer.getWidgets()) {
            if (w instanceof AdsReportCell) {
                writeCellClass((AdsReportCell) w, cp);
            } else if (w instanceof AdsReportWidgetContainer) {
                writeCellClasses((AdsReportWidgetContainer) w, cp);
            }
        }
    }

    private static String getBandJavaName(IReportWidgetContainer band) {
        if (band instanceof AdsReportGroupBand) {
            final AdsReportGroup group = ((AdsReportGroupBand) band).getOwnerGroup();
            final int index = group.getIndex();
            return "Group" + String.valueOf(index) + (group.getFooterBand() == band ? "Footer" : "Header") + "Band";
        } else if (band instanceof AdsReportWidgetContainer) {
            final AdsReportWidgetContainer cellContainer = (AdsReportWidgetContainer) band;
            final String name = getCellJavaName(cellContainer);
            //IReportWidgetContainer container =(IReportWidgetContainer)cellContainer.getOwnerWidget();
            //final int index =container.getWidgets().indexOf(cellContainer);
            return name;
        } else {
            final String name = band.getName();
            return name.replaceAll(" ", "");
        }
    }

    private static String getCellJavaName(AdsReportWidget cell) {
        /*
         * final AdsReportBand band = cell.getOwnerBand(); final int index =
         * band.getWidgets().indexOf(cell); final String bandJavaName =
         * getBandJavaName(band); return bandJavaName + "Cell" + index;
         */
        final IReportWidgetContainer reportContainer = (IReportWidgetContainer) cell.getOwnerWidget();
        final int index = reportContainer.getWidgets().indexOf(cell);
        final String bandJavaName = getBandJavaName(reportContainer);
        return bandJavaName + "Cell" + index;
    }

    private static void writeString(String varName, String methodName, String s, CodePrinter cp) {
        cp.print(varName);
        cp.print(".");
        cp.print(methodName);
        cp.print("(");
        cp.print(s);
        cp.println(");");
    }

    private static void writeStringLiteral(String varName, String methodName, String s, CodePrinter cp) {
        cp.print(varName);
        cp.print(".");
        cp.print(methodName);
        cp.print("(");
        cp.printStringLiteral(s);
        cp.println(");");
    }

    private static boolean writeId(String varName, String methodName, Definition def, CodePrinter cp) {
        if (def == null) {
            return false;
        }
        cp.print(varName);
        cp.print(".");
        cp.print(methodName);
        cp.print("(");
        cp
                .print(Id.class
                        .getName());
        cp.print(
                ".Factory.loadFrom(");
        cp.printStringLiteral(def.getId().toString());
        cp.print(
                ")); // ");
        cp.println(def.getName());

        return true;
    }

    private static void writeColor(String varName, String methodName, Color color, CodePrinter cp) {
        String s;
        if (color != null) {
            s = "new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
        } else {
            s = "null";
        }
        writeString(varName, methodName, s, cp);
    }

    private static void writeDouble(String varName, String methodName, double d, CodePrinter cp) {
        final String s = String.valueOf(d);
        writeString(varName, methodName, s, cp);
    }

    private static void writeLong(String varName, String methodName, long i, CodePrinter cp) {
        final String s = String.valueOf(i);
        writeString(varName, methodName, s, cp);
    }

    private static void writeBoolean(String varName, String methodName, boolean b, CodePrinter cp) {
        final String s = b ? "true" : "false";
        writeString(varName, methodName, s, cp);
    }

    private static void writeEnum(String varName, String methodName, Enum e, CodePrinter cp) {
        final String s = e.getClass().getSimpleName() + "." + e.name();
        writeString(varName, methodName, s, cp);
    }

    private static void writeEnumCanonical(String varName, String methodName, Enum e, CodePrinter cp) {
        final String s = e.getClass().getCanonicalName() + "." + e.name();
        writeString(varName, methodName, s, cp);
    }

    private boolean writeCsvExportColumnsMethod(CodePrinter cp, final AdsCsvReportInfo csvReportInfo) {
        cp.println("private AdsCsvReportInfo csvExportInfo = null;");
        cp.println("@Override");
        cp.println("public AdsCsvReportInfo getExportCsvInfo() {");
        cp.enterBlock();
        cp.println("if (csvExportInfo == null) {");
        cp.enterBlock();
        cp.println("csvExportInfo = new AdsCsvReportInfo();");
        cp.println("buildCsvExportColumns(csvExportInfo.getExportCsvColumns());");
        cp.print("csvExportInfo.setDelimiter(");
        cp.printStringLiteral(csvReportInfo.getDelimiter());
        cp.println(");");
        writeBoolean("csvExportInfo", "setIsExportColumnName", csvReportInfo.isExportColumnName(), cp);
        writeBoolean("csvExportInfo", "setIsExportToCsvEnabled", csvReportInfo.isExportToCsvEnabled(), cp);
        cp.leaveBlock();
        cp.println("}");

        cp.println("return csvExportInfo;");
        cp.leaveBlock();
        cp.println("}");
        cp.println();
        return true;
    }

    private boolean writeFormMethod(AdsReportForm form, CodePrinter cp) {
        cp.println("@Override");
        cp.println("public AdsReportForm createForm() {");
        cp.enterBlock();

        cp.println("final AdsReportForm form = AdsReportForm.Factory.newInstance();");
        final String varName = "form";

        writeBoolean(varName, "setColumnsHeaderForEachGroupDisplayed", form.isColumnsHeaderForEachGroupDisplayed(), cp);
        writeBoolean(varName, "setRepeatGroupHeadersOnNewPage", form.isRepeatGroupHeadersOnNewPage(), cp);

        writeColor(varName, "setBgColor", form.getBgColor(), cp);
        writeColor(varName, "setFgColor", form.getFgColor(), cp);
        writeDouble(varName, "getMargin().setTopMm", form.getMargin().getTopMm(), cp);
        writeDouble(varName, "getMargin().setRightMm", form.getMargin().getRightMm(), cp);
        writeDouble(varName, "getMargin().setBottomMm", form.getMargin().getBottomMm(), cp);
        writeDouble(varName, "getMargin().setLeftMm", form.getMargin().getLeftMm(), cp);
//        
//        writeLong(varName, "getMarginTxt().setTopRows", form.getMarginTxt().getTopRows(), cp);
//        writeLong(varName, "getMarginTxt().setRightCols", form.getMarginTxt().getRightCols(), cp);
//        writeLong(varName, "getMarginTxt().setBottomRows", form.getMarginTxt().getBottomRows(), cp);
//        writeLong(varName, "getMarginTxt().setLeftCols", form.getMarginTxt().getLeftCols(), cp);

        writeLong(varName, "setPageWidthMm", form.getPageWidthMm(), cp);
        writeLong(varName, "setPageHeightMm", form.getPageHeightMm(), cp);
        writeLong(varName, "setPageWidthCols", form.getPageWidthCols(), cp);
        writeLong(varName, "setPageHeightRows", form.getPageHeightRows(), cp);

        cp.println();
        final CodePrinter after = CodePrinter.Factory.newJavaPrinter(cp);

        for (AdsReportBand band : form.getBands()) {
            if (!(band instanceof AdsReportGroupBand)) {
                final String bandJavaName;
                final AdsReportForm.Bands ownerBands = band.getOwnerBands();

                if (ownerBands == null) {
                    return false;
                } else {

                    switch (ownerBands.getType()) {
                        case PAGE_HEADER:
                            bandJavaName = "PageHeader";
                            break;
                        case TITLE:
                            bandJavaName = "Title";
                            break;
                        case COLUMN_HEADER:
                            bandJavaName = "ColumnHeader";
                            break;
                        case DETAIL:
                            bandJavaName = "Detail";
                            break;
                        case SUMMARY:
                            bandJavaName = "Summary";
                            break;
                        case PAGE_FOOTER:
                            bandJavaName = "PageFooter";
                            break;
                        default:
                            return false;
                    }
                    if (!writeBand(band, "form", bandJavaName, cp, after)) {
                        return false;
                    }
                }
            }
        }

        for (int i = 0; i < form.getGroups().size(); i++) {
            final AdsReportGroup group = form.getGroups().get(i);
            final String groupName = "group" + i;
            cp.println("final AdsReportGroup " + groupName + " = form.getGroups().addNew();");
            cp.println(groupName + ".setName(\"" + groupName + "\");");
            final AdsReportBand headerBand = group.getHeaderBand();
            if (headerBand != null) {
                if (!writeBand(headerBand, groupName, "Header", cp, after)) {
                    return false;
                }

            } else {
                writeString(groupName, "setHeaderBand", "null", cp);
            }
            final AdsReportBand footerBand = group.getFooterBand();
            if (footerBand != null) {
                if (!writeBand(footerBand, groupName, "Footer", cp, after)) {
                    return false;
                }
            } else {
                writeString(groupName, "setFooterBand", "null", cp);
            }
        }

        cp.println("return form;");
        cp.leaveBlock();
        cp.println("}");
        cp.println();
        cp.println(after.getContents());
        cp.println();
        return true;
    }

    private void writeMarker(CodePrinter cp, Jml jml) {
        final String marker = AdsReportWriterUtils.getJmlMarker(jml);
        writeCustomMarker(cp, marker);
    }

    private void writeJml(CodePrinter cp, Jml jml) {
        writeMarker(cp, jml);
        writeCode(cp, jml);
    }

    private void writeGroupConditionMethod(AdsReportGroups groups, CodePrinter cp) {
        cp.println("@Override");
        cp.println("public Object getGroupCondition(int groupIndex) {");
        cp.enterBlock();
        if (!groups.isEmpty()) {
            cp.println("switch (groupIndex) {");
            for (AdsReportGroup group : groups) {
                cp.enterBlock();
                cp.println("case " + group.getIndex() + ":");
                cp.enterBlock();
                writeJml(cp, group.getCondition());
                cp.println();
                cp.leaveBlock();
                cp.leaveBlock();
            }
            cp.println("}");
        }
        cp.println("throw new IllegalStateException();");
        cp.leaveBlock();
        cp.println("}");
        cp.println();
    }

    private void writeOnAdding(AdsReportBand band, CodePrinter cp) {
        final Jml onAdding = band.getOnAdding();
        cp.enterBlock();
        writeMarker(cp, onAdding);
        cp.println("@Override");
        cp.println("public void onAdding() {");
        cp.enterBlock();

        try {
            cp.enterCodeSection(onAdding.getLocationDescriptor());//нужно для отладчика
            WriterUtils.writeProfilerInitialization(cp, band);//нужно для работы профилера
            writeJml(cp, onAdding);
            WriterUtils.writeProfilerFinalization(cp, band);//нужно для работы профилера
        } finally {
            cp.leaveCodeSection();//нужно для отладчика
        }

        cp.println();
        cp.leaveBlock();
        cp.println("}");
        cp.leaveBlock();
    }

    private void writeOnAdding(AdsReportCell cell, CodePrinter cp) {
        final Jml onAdding = cell.getOnAdding();
        cp.enterBlock();
        writeMarker(cp, onAdding);
        cp.println("@Override");
        cp.println("public void onAdding() {");
        cp.enterBlock();

        try {
            cp.enterCodeSection(onAdding.getLocationDescriptor());//нужно для отладчика
            WriterUtils.writeProfilerInitialization(cp, cell);//нужно для работы профилера
            writeJml(cp, onAdding);
            WriterUtils.writeProfilerFinalization(cp, cell);//нужно для работы профилера
        } finally {
            cp.leaveCodeSection();//нужно для отладчика
        }

        cp.println();
        cp.leaveBlock();
        cp.println("}");
        cp.leaveBlock();
    }

    private void writeBandClass(AdsReportBand band, CodePrinter cp) {
        final Jml onAdding = band.getOnAdding();
        if (!onAdding.getItems().isEmpty()) {
            final String classSimpleName = getBandJavaName(band);
            final String extendsClassSimpleName = band.getClass().getSimpleName();
            cp.println("private final class " + classSimpleName + " extends " + extendsClassSimpleName + " {");
            writeOnAdding(band, cp);
            cp.println("}");
            cp.println();
        }
    }

    private void writeGetExpressionMethod(AdsReportExpressionCell expressionCell, String getExpressionMethodName, CodePrinter cp) {
        final Jml expression = expressionCell.getExpression();
        writeMarker(cp, expression);
        cp.println("private String " + getExpressionMethodName + "() {");
        cp.enterBlock();
        try {
            cp.enterCodeSection(expression.getLocationDescriptor());//нужно для отладчика
            WriterUtils.writeProfilerInitialization(cp, expressionCell);//нужно для работы профилера
            writeJml(cp, expression);
            WriterUtils.writeProfilerFinalization(cp, expressionCell);//нужно для работы профилера
        } finally {
            cp.leaveCodeSection();//нужно для отладчика
        }
        cp.println();
        cp.leaveBlock();
        cp.println("}");
    }

    private void writeCsvRowCondition(AdsCsvReportInfo csvInfo, CodePrinter cp) {
        cp.enterBlock();
        final Jml csvRowCondition = csvInfo.getRowCondition();
        writeMarker(cp, csvRowCondition);
        cp.println("@Override");
        cp.println("public boolean isCsvRowVisible() {");
        cp.enterBlock();
        try {
            cp.enterCodeSection(csvRowCondition.getLocationDescriptor());//нужно для отладчика
            WriterUtils.writeProfilerInitialization(cp, csvInfo);//нужно для работы профилера
            writeJml(cp, csvRowCondition);
            WriterUtils.writeProfilerFinalization(cp, csvInfo);//нужно для работы профилера
        } finally {
            cp.leaveCodeSection();//нужно для отладчика
        }
        cp.println();
        cp.leaveBlock();
        cp.println("}");
        cp.leaveBlock();
    }

    private void writeXlsxRowCondition(AdsXlsxReportInfo xlsxInfo, CodePrinter cp) {
        cp.enterBlock();
        final Jml xlsxRowCondition = xlsxInfo.getRowCondition();
        writeMarker(cp, xlsxRowCondition);
        cp.println("@Override");
        cp.println("public boolean isXlsxRowVisible() {");
        cp.enterBlock();
        try {
            cp.enterCodeSection(xlsxRowCondition.getLocationDescriptor());//нужно для отладчика
            WriterUtils.writeProfilerInitialization(cp, xlsxInfo);//нужно для работы профилера
            writeJml(cp, xlsxRowCondition);
            WriterUtils.writeProfilerFinalization(cp, xlsxInfo);//нужно для работы профилера
        } finally {
            cp.leaveCodeSection();//нужно для отладчика
        }
        cp.println();
        cp.leaveBlock();
        cp.println("}");
        cp.leaveBlock();
    }

    private void writeCalcExpressionMethod(String getExpressionMethodName, CodePrinter cp) {
        cp.enterBlock();
        cp.println("@Override");
        cp.println("public String calcExpression() {");
        cp.enterBlock();
        cp.println("return " + getExpressionMethodName + "();");
        cp.leaveBlock();
        cp.println("}");
        cp.leaveBlock();
    }

    private void writeCellClass(AdsReportCell cell, CodePrinter cp) {
        final String classSimpleName = getCellJavaName(cell);
        final boolean isExpression = cell.getCellType() == EReportCellType.EXPRESSION;
        final boolean isOnAddind = !cell.getOnAdding().getItems().isEmpty();
        final String getExpressionMethodName = "get" + classSimpleName + "Expression";

        if (isExpression) {
            final AdsReportExpressionCell expressionCell = (AdsReportExpressionCell) cell;
            writeGetExpressionMethod(expressionCell, getExpressionMethodName, cp);
        }

        final Jml onAdding = cell.getOnAdding();

        if (isOnAddind || isExpression) {
            final String extendsClassSimpleName = cell.getClass().getSimpleName();
            cp.println("private final class " + classSimpleName + " extends " + extendsClassSimpleName + " {");
            if (isOnAddind) {
                writeOnAdding(cell, cp);
            }
            if (isExpression) {
                writeCalcExpressionMethod(getExpressionMethodName, cp);
            }
            cp.println("}");
            cp.println();
        }
    }

    private boolean writeSubReport(AdsSubReport subReport, String subReportVarName, CodePrinter cp) {
        cp.println("AdsSubReport " + subReportVarName + " = AdsSubReport.Factory.newInstance();");
        final AdsReportClassDef srd = subReport.findReport();
        if (srd == null) {
            return false;
        }
        if (!writeId(subReportVarName, "setReportId", srd, cp)) {
            return false;
        }
        for (int j = 0; j < subReport.getAssociations().size(); j++) {
            final AdsSubReport.Association association = subReport.getAssociations().get(j);
            final String associationVarName = subReportVarName + "Association" + j;
            cp.println("AdsSubReport.Association " + associationVarName + " = AdsSubReport.Association.Factory.newInstance();");
            final AdsPropertyDef prop = association.findProperty();
            if (prop == null) {
                return false;
            }
            if (!writeId(associationVarName, "setPropertyId", prop, cp)) {
                return false;
            }
            final AdsParameterPropertyDef param = association.findParameter();
            if (param == null) {
                return false;
            }
            if (!writeId(associationVarName, "setParameterId", param, cp)) {
                return false;
            }
            cp.println(subReportVarName + ".getAssociations().add(" + associationVarName + ");");
        }

        AdsReportMarginMm marginMm = subReport.getMarginMm();
        cp.print(subReportVarName + ".setMarginMm(");
        cp.print(String.valueOf(marginMm.getMarginString()));
        cp.println(");");

        AdsReportMarginTxt marginTxt = subReport.getMarginTxt();
        cp.print(subReportVarName + ".setMarginTxt(");
        cp.print(String.valueOf(marginTxt.getMarginString()));
        cp.println(");");

        return true;
    }

    private boolean writeBand(AdsReportBand band, String ownerName, String setterName, CodePrinter cp, CodePrinter after) {
        final String bandJavaName = getBandJavaName(band);
        final String bandVarName = toLower(bandJavaName);
        final String bandClass = (band.getOnAdding().getItems().isEmpty() ? band.getClass().getSimpleName() : bandJavaName);
        cp.println("// " + bandVarName);
        cp.println(bandClass + " " + bandVarName + " = new " + bandClass + "();");
        if (band instanceof AdsReportGroupBand) {
            cp.println(ownerName + ".set" + setterName + "Band(" + bandVarName + ");");
        } else {
            cp.println(ownerName + ".get" + setterName + "Bands().add(" + bandVarName + ");");
        }
        writeBandAppearance(band, bandVarName, cp);
        writeEnum(bandVarName, "setLayout", band.getLayout(), cp);

        for (AdsReportWidget cell : band.getWidgets()) {
            if (!writeCell(cell, bandVarName, cp, after)) {
                return false;
            }
        }

        for (int i = 0; i < band.getPreReports().size(); i++) {
            final AdsSubReport subReport = band.getPreReports().get(i);
            final String subReportVarName = bandVarName + "PreReport" + i;
            if (!writeSubReport(subReport, subReportVarName, cp)) {
                return false;
            }
            cp.println(bandVarName + ".getPreReports().add(" + subReportVarName + ");");
        }

        for (int i = 0; i < band.getPostReports().size(); i++) {
            final AdsSubReport subReport = band.getPostReports().get(i);
            final String subReportVarName = bandVarName + "PostReport" + i;
            if (!writeSubReport(subReport, subReportVarName, cp)) {
                return false;
            }
            cp.println(bandVarName + ".getPostReports().add(" + subReportVarName + ");");
        }
        return true;
    }

    private void writeBandAppearance(AdsReportBand band, String bandVarName, CodePrinter cp) {
        writeDouble(bandVarName, "setHeightMm", band.getHeightMm(), cp);
        writeLong(bandVarName, "setHeightRows", band.getHeightRows(), cp);
        writeBoolean(bandVarName, "setStartOnNewPage", band.isStartOnNewPage(), cp);
        writeBoolean(bandVarName, "setLastOnPage", band.isLastOnPage(), cp);
        writeBoolean(bandVarName, "setMultiPage", band.isMultiPage(), cp);
        writeBoolean(bandVarName, "setAutoHeight", band.isAutoHeight(), cp);
        writeBoolean(bandVarName, "setCellWrappingEnabled", band.isCellWrappingEnabled(), cp);
        if (band.getAltBgColor() != null) {
            writeColor(bandVarName, "setAltBgColor", band.getAltBgColor(), cp);
        }
        if (!band.isInsideAltColor()) {
            writeBoolean(bandVarName, "setInsideAltColor", band.isInsideAltColor(), cp);
        }
        writeAppearance(band, bandVarName, cp);
    }

    private void writeAppearance(AdsReportAbstractAppearance appearance, String varName, CodePrinter cp) {
        if (!appearance.isBgColorInherited()) {
            writeBoolean(varName, "setBgColorInherited", false, cp);
            writeColor(varName, "setBgColor", appearance.getBgColor(), cp);
        }
        if (appearance instanceof AdsReportCell) {
            AdsReportCell cell = (AdsReportCell) appearance;
            if (cell.isIgnoreAltBgColor()) {
                writeBoolean(varName, "setIgnoreAltBgColor", true, cp);
            }
        }

        if (!appearance.isFgColorInherited()) {
            writeBoolean(varName, "setFgColorInherited", false, cp);
            writeColor(varName, "setFgColor", appearance.getFgColor(), cp);
        }

        if (!(appearance instanceof AdsReportCell) || !((AdsReportCell) appearance).isFontInherited()) {
            final Font font = appearance.getFont();
            writeFontAppearance(varName, "getFont()", font, cp);
            /*
             * writeStringLiteral(varName, "getFont().setName", font.getName(),
             * cp); writeDouble(varName, "getFont().setSizeMm",
             * font.getSizeMm(), cp);
             *
             * writeBoolean(varName, "getFont().setBold", font.isBold(), cp);
             * writeBoolean(varName, "getFont().setItalic", font.isItalic(),
             * cp);
             */
        }
        final AdsReportAbstractAppearance.Border border = appearance.getBorder();
        if (border.isDisplayed()) {
            if (border.isOnTop()) {
                writeBoolean(varName, "getBorder().setOnTop", true, cp);
                writeBorderAppearance(varName, "getBorder().getTopBorder()", border.getTopStyle(), border.getTopColor(), border.getTopThicknessMm(), cp);
            }

            if (border.isOnBottom()) {
                writeBoolean(varName, "getBorder().setOnBottom", true, cp);
                writeBorderAppearance(varName, "getBorder().getBottomBorder()", border.getBottomStyle(), border.getBottomColor(), border.getBottomThicknessMm(), cp);
            }

            if (border.isOnLeft()) {
                writeBoolean(varName, "getBorder().setOnLeft", true, cp);
                writeBorderAppearance(varName, "getBorder().getLeftBorder()", border.getLeftStyle(), border.getLeftColor(), border.getLeftThicknessMm(), cp);
            }

            if (border.isOnRight()) {
                writeBoolean(varName, "getBorder().setOnRight", true, cp);
                writeBorderAppearance(varName, "getBorder().getRightBorder()", border.getRightStyle(), border.getRightColor(), border.getRightThicknessMm(), cp);
            }
        }

        cp.println();
    }

    private void writeBorderAppearance(final String varName, final String borderMthName, final EReportBorderStyle style, final Color color, double thicknessMm, final CodePrinter cp) {
        if (style != EReportBorderStyle.SOLID) {
            writeEnum(varName, borderMthName + ".setStyle", style, cp);
        }

        final Color borderColor = color;
        if (!Color.BLACK.equals(borderColor)) {
            writeColor(varName, borderMthName + ".setColor", color, cp);
        }

        if (thicknessMm != AdsReportAbstractAppearance.Border.DEFAULT_THICKNESS_MM) {
            writeDouble(varName, borderMthName + ".setThicknessMm", thicknessMm, cp);
        }
    }

    private void writeFontAppearance(final String varName, final String fontMthName, final Font font, final CodePrinter cp) {
        writeStringLiteral(varName, fontMthName + ".setName", font.getName(), cp);
        writeDouble(varName, fontMthName + ".setSizeMm", font.getSizeMm(), cp);
        writeBoolean(varName, fontMthName + ".setBold", font.isBold(), cp);
        writeBoolean(varName, fontMthName + ".setItalic", font.isItalic(), cp);
    }

    private static String toLower(final String s) {
        return String.valueOf(Character.toLowerCase(s.charAt(0))) + s.substring(1);
    }

    private boolean writeCell(AdsReportWidget widget, final String bandVarName, CodePrinter cp, CodePrinter after) {
        final String cellJavaName = getCellJavaName(widget);
        final String cellVarName = toLower(cellJavaName);
        cp.println("// " + cellVarName);

        cp.println(bandVarName + ".getWidgets().add(get" + cellJavaName + "());");
        return writeCellMethod(widget, after);
    }

    private boolean writeCellInit(AdsReportWidget widget, String cellVarName, String cellJavaName, CodePrinter cp) {
        final String cellClassSimpleName = widget.getClass().getSimpleName();
        if (widget.isReportContainer()) {
            cp.println(cellClassSimpleName + " " + cellVarName + " = new AdsReportWidgetContainer();");
        } else {
            final AdsReportCell cell = (AdsReportCell) widget;
            if (!cell.getOnAdding().getItems().isEmpty() || cell.getCellType() == EReportCellType.EXPRESSION) {
                cp.println(cellClassSimpleName + " " + cellVarName + " = new " + cellJavaName + "();");
            } else {
                cp.println(cellClassSimpleName + " " + cellVarName + " = AdsReportCellFactory.new" + cell.getCellType().getValue() + "Cell();");
            }
        }
        writeDouble(cellVarName, "setLeftMm", widget.getLeftMm(), cp);
        writeDouble(cellVarName, "setTopMm", widget.getTopMm(), cp);
        writeDouble(cellVarName, "setWidthMm", widget.getWidthMm(), cp);
        writeDouble(cellVarName, "setHeightMm", widget.getHeightMm(), cp);

        writeLong(cellVarName, "setLeftColumn", widget.getLeftColumn(), cp);
        writeLong(cellVarName, "setTopRow", widget.getTopRow(), cp);
        writeLong(cellVarName, "setWidthCols", widget.getWidthCols(), cp);
        writeLong(cellVarName, "setHeightRows", widget.getHeightRows(), cp);

        writeLong(cellVarName, "setColumnSpan", widget.getColumnSpan(), cp);

        if (widget instanceof AdsReportDbImageCell) {
            writeEnum(cellVarName, "setScaleType", ((AdsReportDbImageCell) widget).getScaleType(), cp);
        }

        final String name = widget.getName();
        if (name != null && !name.isEmpty()) {
            writeStringLiteral(cellVarName, "setName", widget.getName(), cp);
        }

        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            writeEnum(cellVarName, "setLayout", container.getLayout(), cp);
            for (AdsReportWidget cell : container.getWidgets()) {
                final String javaName = getCellJavaName(cell);
                cp.println(cellVarName + ".getWidgets().add(get" + javaName + "());");
            }
        } else {
            final AdsReportCell cell = (AdsReportCell) widget;
            
            if (cell.getId() != null) {
                cp.print(cellVarName + ".setId(");
                WriterUtils.writeIdUsage(cp, cell.getId());
                cp.println(");");
            }

            switch (cell.getCellType()) {
                case EXPRESSION:
                    break;
                case PROPERTY:
                    final AdsReportPropertyCell propertyCell = (AdsReportPropertyCell) cell;
                    if (!writeId(cellVarName, "setPropertyId", propertyCell.findProperty(), cp)) {
                        return false;
                    }
                    if (propertyCell.getNullTitleId() != null) {
                        if (!writeId(cellVarName, "setNullTitleId", propertyCell.findNullTitle(), cp)) {
                            return false;
                        }
                    }
                    break;
                case TEXT:
                    final AdsReportTextCell textCell = (AdsReportTextCell) cell;
                    if (!writeId(cellVarName, "setTextId", textCell.findText(), cp)) {
                        return false;
                    }
                    break;
                case IMAGE:
                    final AdsReportImageCell imageCell = (AdsReportImageCell) cell;
                    if (!writeId(cellVarName, "setImageId", imageCell.findImage(), cp)) {
                        return false;
                    }
                    break;
                case DB_IMAGE:
                    final AdsReportDbImageCell dbImageCell = (AdsReportDbImageCell) cell;
                    if (!writeId(cellVarName, "setDataPropertyId", dbImageCell.findDataProperty(), cp)) {
                        return false;
                    }                    
                    if (!writeId(cellVarName, "setMimeTypePropertyId", dbImageCell.findMimeTypeProperty(), cp)) {
                        return false;
                    }
                    writeBoolean(cellVarName, "setIsResizeImage", dbImageCell.isResizeImage(), cp);
                    break;
                case SPECIAL:
                    final AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
                    writeEnum(cellVarName, "setSpecialType", specialCell.getSpecialType(), cp);
                    break;
                case SUMMARY:
                    final AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
                    final EReportSummaryCellType summaryType = summaryCell.getSummaryType();
                    if (summaryType != EReportSummaryCellType.SUM) {
                        writeEnum(cellVarName, "setSummaryType", summaryType, cp);
                    }

                    if (!writeId(cellVarName, "setPropertyId", summaryCell.findProperty(), cp)) {
                        return false;
                    }

                    final int groupCount = summaryCell.getGroupCount();
                    if (groupCount > 0) {
                        writeLong(cellVarName, "setGroupCount", groupCount, cp);
                    }
                    break;
                case CHART:
                    final AdsReportChartCell chartCell = (AdsReportChartCell) cell;
                    final EReportChartCellType chartType = chartCell.getChartType();
                    if (chartType != EReportChartCellType.CATEGORY) {
                        writeEnum(cellVarName, "setChartType", chartType, cp);
                    }
                    final EReportChartLegendPosition legendPosition = chartCell.getLegendPosition();
                    if (legendPosition != EReportChartLegendPosition.BOTTOM) {
                        writeEnum(cellVarName, "setLegendPosition", legendPosition, cp);
                    }
                    if (chartCell.getTitleId() != null && !writeId(cellVarName, "setTitleId", chartCell.findTitle(), cp)) {
                        return false;
                    }
                    writeBoolean(cellVarName, "setXAxisGridVisible", chartCell.isXAxisGridVisible(), cp);
                    writeBoolean(cellVarName, "setYAxisGridVisible", chartCell.isYAxisGridVisible(), cp);
                    writeBoolean(cellVarName, "setIsHorizontalOrientation", chartCell.isHorizontalOrientation(), cp);

                    writeBoolean(cellVarName, "setAutoAxisSpace", chartCell.isAutoAxisSpace(), cp);
                    writeLong(cellVarName, "setLeftAxisSpacePx", chartCell.getLeftAxisSpacePx(), cp);
                    writeLong(cellVarName, "setBottomAxisSpacePx", chartCell.getBottomAxisSpacePx(), cp);
                    writeLong(cellVarName, "setTopAxisSpacePx", chartCell.getTopAxisSpacePx(), cp);
                    writeLong(cellVarName, "setRightAxisSpacePx", chartCell.getRightAxisSpacePx(), cp);

                    if (chartCell.getChartSeries() != null && !writeChartSeries(cp, chartCell.getChartSeries(), cellVarName)) {
                        return false;
                    }
                    if (chartCell.getDomainAxes() != null && !writeChartAxis(cp, chartCell.getDomainAxes(), cellVarName, chartCell.getChartType())) {
                        return false;
                    }
                    if (chartCell.getRangeAxes() != null && !writeChartAxis(cp, chartCell.getRangeAxes(), cellVarName, chartCell.getChartType())) {
                        return false;
                    }
                    writeBoolean(cellVarName, "setTitleFontInherited", chartCell.isTitleFontInherited(), cp);
                    writeBoolean(cellVarName, "setAxesFontInherited", chartCell.isAxesFontInherited(), cp);
                    writeBoolean(cellVarName, "setLegendFontInherited", chartCell.isLegendFontInherited(), cp);
                    if (!chartCell.isTitleFontInherited()) {
                        writeFontAppearance(cellVarName, "getTitleFont()", chartCell.getTitleFont(), cp);
                    }
                    if (!chartCell.isAxesFontInherited()) {
                        writeFontAppearance(cellVarName, "getAxesFont()", chartCell.getAxesFont(), cp);
                    }
                    if (!chartCell.isLegendFontInherited()) {
                        writeFontAppearance(cellVarName, "getLegendFont()", chartCell.getLegendFont(), cp);
                    }
                    writeBoolean(cellVarName, "setPlotMinSizeEnable", chartCell.isPlotMinSizeEnable(), cp);
                    if (chartCell.isPlotMinSizeEnable()) {
                        writeDouble(cellVarName, "setMinPlotHeightMm", chartCell.getMinPlotHeightMm(), cp);
                        writeDouble(cellVarName, "setMinPlotWidthMm", chartCell.getMinPlotWidthMm(), cp);
                    }

                    writeDouble(cellVarName, "setForegroundAlpha", chartCell.getForegroundAlpha(), cp);
                    break;
            }
            if (cell.getPreferredMode() != null) {
                writeEnumCanonical(cellVarName, "setPreferredMode", cell.getPreferredMode(), cp);
            }

            writeEnum(cellVarName, "setTextFormat", cell.getTextFormat(), cp);

            if (cell instanceof AdsReportFormattedCell) {
                final AdsReportFormattedCell formatterCell = (AdsReportFormattedCell) cell;
                writeFormat(cellVarName + ".getFormat()", cp, formatterCell.getFormat());
                /*writeBoolean(cellVarName, "getFormat().setUseDefaultFormat", formatterCell.getUseDefaultFormat(), cp);
                 if (!formatterCell.getUseDefaultFormat()) {
                 if (formatterCell.getDateStyle() != null) {
                 writeEnum(cellVarName, "getFormat().setDateStyle", formatterCell.getDateStyle(), cp);
                 }
                 if (formatterCell.getTimeStyle() != null) {
                 writeEnum(cellVarName, "getFormat().setTimeStyle", formatterCell.getTimeStyle(), cp);
                 }
                 if (formatterCell.getPattern() != null) {
                 writeStringLiteral(cellVarName, "getFormat().setPattern", formatterCell.getPattern(), cp);
                 }

                 writeLong(cellVarName, "getFormat().setPrecission", formatterCell.getPrecission(), cp);
                 writeStringLiteral(cellVarName, "getFormat().setDesimalDelimeter", formatterCell.getDesimalDelimeter(), cp);
                 writeEnum(cellVarName, "getFormat().setTriadDelimeterType", formatterCell.getTriadDelimeterType(), cp);
                 if (formatterCell.getTriadDelimeterType() == ETriadDelimeterType.SPECIFIED) {
                 writeStringLiteral(cellVarName, "getFormat().setTriadDelimeter", formatterCell.getTriadDelimeter(), cp);
                 }
                 }*/
            }
            writeBoolean(cellVarName, "setAdjustHeight", cell.isAdjustHeight(), cp);
            writeBoolean(cellVarName, "setAdjustWidth", cell.isAdjustWidth(), cp);
            
            if (cell.getAssociatedColumnId() != null) {
                cp.print(cellVarName + ".setAssociatedColumnId(");
                WriterUtils.writeIdUsage(cp, cell.getAssociatedColumnId());
                cp.println(");");
                
                writeBoolean(cellVarName, "setVisible", false, cp);
                
                cp.println("for (org.radixware.kernel.server.reports.RadReportColumnDef column : getVisibleColumns()) {");
                cp.enterBlock();
                
                cp.print("if (column.getId() == ");
                WriterUtils.writeIdUsage(cp, cell.getAssociatedColumnId());
                cp.println(") {");
                cp.enterBlock();
                
                writeBoolean(cellVarName, "setVisible", true, cp);
                cp.println("break;");
                
                cp.leaveBlock();
                cp.println("}");
                
                cp.leaveBlock();
                cp.println("}");
            }
            
            if (cell.getLeftCellId() != null) {
                cp.print(cellVarName + ".setLeftCellId(");
                WriterUtils.writeIdUsage(cp, cell.getLeftCellId());
                cp.println(");");
            }
            
            if (cell.getRightCellIdList() != null && !cell.getRightCellIdList().isEmpty()) {
                for (Id rigthCellId : cell.getRightCellIdList()) {
                    cp.print(cellVarName + ".addRightCellId(");
                    WriterUtils.writeIdUsage(cp, rigthCellId);
                    cp.println(");");
                }
            }            
            writeBoolean(cellVarName, "setChangeTopOnMoving", cell.isChangeTopOnMoving(), cp);
            
            
            writeCellAppearance(cell, cellVarName, cp);
        }
        return true;
    }

    private boolean writeCellMethod(AdsReportWidget widget, CodePrinter cp) {
        final String cellJavaName = getCellJavaName(widget);
        final String cellVarName = toLower(cellJavaName);
        cp.println("// " + cellVarName);
        cp.println("private  AdsReportWidget get" + cellJavaName + "() {");
        cp.enterBlock();
        if (!writeCellInit(widget, cellVarName, cellJavaName, cp)) {
            return false;
        }
        cp.println("return " + cellVarName + ";");
        cp.println();
        cp.leaveBlock();
        cp.println("}");
        cp.println();
        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            for (AdsReportWidget cell : container.getWidgets()) {
                if (!writeCellMethod(cell, cp)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean writeFormat(String varName, CodePrinter cp, AdsReportFormat format) {
        writeBoolean(varName, "setUseDefaultFormat", format.getUseDefaultFormat(), cp);
        if (!format.getUseDefaultFormat()) {
            if (format.getDateStyle() != null) {
                writeEnum(varName, "setDateStyle", format.getDateStyle(), cp);
            }
            if (format.getTimeStyle() != null) {
                writeEnum(varName, "setTimeStyle", format.getTimeStyle(), cp);
            }
            if (format.getPattern() != null) {
                writeStringLiteral(varName, "setPattern", format.getPattern(), cp);
            }

            writeLong(varName, "setPrecission", format.getPrecission(), cp);
            writeStringLiteral(varName, "setDesimalDelimeter", format.getDesimalDelimeter(), cp);
            writeEnum(varName, "setTriadDelimeterType", format.getTriadDelimeterType(), cp);
            if (format.getTriadDelimeterType() == ETriadDelimeterType.SPECIFIED) {
                writeStringLiteral(varName, "setTriadDelimeter", format.getTriadDelimeter(), cp);
            }
        }
        return true;
    }

    private boolean writeChartSeries(CodePrinter cp, AdsReportChartCell.ChartSeries chartSeries, String cellVarName) {
        for (AdsReportChartSeries series : chartSeries) {
            final String seriesName = series.getName();
            final String seriesVarName = cellVarName + "_" + toLower(seriesName);
            final String seriesClassSimpleName = series.getClass().getSimpleName();
            final EReportChartSeriesType seriesType = series.getSeriesType();
            final String strSeriesType = seriesType.getClass().getSimpleName() + "." + seriesType.name();
            cp.println("// " + seriesVarName);
            cp.print(seriesClassSimpleName + " " + seriesVarName + " = new AdsReportChartSeries(" + cellVarName + ", " + strSeriesType + ", ");
            cp.printStringLiteral(series.getName());
            cp.println(");");

            writeChartDataSetInfoCell(cp, series.getSeriesData(), seriesVarName, "getSeriesData");
            writeChartDataSetInfoCell(cp, series.getDomainData(), seriesVarName, "getDomainData");
            writeChartDataSetInfoCell(cp, series.getRangeData(), seriesVarName, "getRangeData");

            writeBoolean(seriesVarName, "setIsShowItemLabel", series.isShowItemLabel(), cp);
            writeLong(seriesVarName, "setGroupIndex", series.getGroupIndex(), cp);
            if (series.getTitleId() != null && !writeId(seriesVarName, "setTitleId", series.findTitle(), cp)) {
                return false;
            }
            cp.println(cellVarName + ".getChartSeries().add(" + seriesVarName + ");");
        }
        return true;
    }

    private boolean writeChartAxis(CodePrinter cp, AdsReportChartCell.AdsReportChartAxes chartAxes, String cellVarName, EReportChartCellType chartType) {
        for (AdsReportChartAxis axis : chartAxes) {
            final String axisName = axis.getName();
            final String axisVarName = cellVarName + "_" + toLower(axisName);
            final String seriesClassSimpleName = axis.getClass().getSimpleName();
            final EReportChartAxisType axisType = axis.getAxisType();
            final String strSeriesType = axisType.getClass().getSimpleName() + "." + axisType.name();
            cp.println("// " + axisVarName);
            cp.print(seriesClassSimpleName + " " + axisVarName + " = new AdsReportChartAxis(" + cellVarName + ", ");
            cp.printStringLiteral(axis.getName());
            cp.print(", " + strSeriesType);
            cp.println(");");
            writeBoolean(axisVarName, "setIsAutoRangeIncludeZero", axis.isAutoRangeIncludeZero(), cp);
            writeBoolean(axisVarName, "setIsRightOrTop", axis.isRightOrTop(), cp);
            writeBoolean(axisVarName, "setIsDateAxis", axis.isDateAxis(), cp);
            writeBoolean(axisVarName, "setIsVisible", axis.isVisible(), cp);
            final EReportChartAxisLabelPositions labelPosition = axis.getAxisLabelPosition();
            if (labelPosition != EReportChartAxisLabelPositions.STANDARD) {
                writeEnum(axisVarName, "setAxisLabelPosition", labelPosition, cp);
            }

            if (axis.getTitleId() != null && !writeId(axisVarName, "setTitleId", axis.findTitle(), cp)) {
                return false;
            }

            if (axisType == EReportChartAxisType.DOMAIN_AXIS) {
                cp.println(cellVarName + ".getDomainAxes().add(" + axisVarName + ");");
                if (chartType != EReportChartCellType.CATEGORY) {
                    writeFormat(axisVarName + ".getFormat()", cp, axis.getFormat());
                }
            } else {
                cp.println(cellVarName + ".getRangeAxes().add(" + axisVarName + ");");
                writeFormat(axisVarName + ".getFormat()", cp, axis.getFormat());
            }
        }
        return true;
    }

    private void writeChartDataSetInfoCell(CodePrinter cp, AdsChartDataInfo dataSetInfo, String seriesVarName, String methodName) {
        if (dataSetInfo.getPropId() != null) {
            cp.print(seriesVarName + "." + methodName + "().setPropId(");
            cp
                    .print(Id.class
                            .getName());
            cp.print(
                    ".Factory.loadFrom(");
            cp.printStringLiteral(dataSetInfo.getPropId().toString());
            cp.println(
                    "));");
        }
        cp.print(seriesVarName + "." + methodName + "().setAxisIndex(");
        cp.print(dataSetInfo.getAxisIndex());
        cp.println("); ");
    }

    private void writeCellAppearance(AdsReportCell cell, String cellVarName, CodePrinter cp) {
        writeEnum(cellVarName, "setHAlign", cell.getHAlign(), cp);
        writeEnum(cellVarName, "setVAlign", cell.getVAlign(), cp);

        if (!cell.isFontInherited()) {
            writeBoolean(cellVarName, "setFontInherited", false, cp);
        }

        if (cell.getMarginTopMm() != AdsReportCell.DEFAULT_MARGIN) {
            writeDouble(cellVarName, "setMarginTopMm", cell.getMarginTopMm(), cp);
        }
        if (cell.getMarginBottomMm() != AdsReportCell.DEFAULT_MARGIN) {
            writeDouble(cellVarName, "setMarginBottomMm", cell.getMarginBottomMm(), cp);
        }
        if (cell.getMarginLeftMm() != AdsReportCell.DEFAULT_MARGIN) {
            writeDouble(cellVarName, "setMarginLeftMm", cell.getMarginLeftMm(), cp);
        }
        if (cell.getMarginRightMm() != AdsReportCell.DEFAULT_MARGIN) {
            writeDouble(cellVarName, "setMarginRightMm", cell.getMarginRightMm(), cp);
        }

        if (cell.getMarginTopRows() != 0) {
            writeLong(cellVarName, "setMarginTopRows", cell.getMarginTopRows(), cp);
        }
        if (cell.getMarginBottomRows() != 0) {
            writeLong(cellVarName, "setMarginBottomRows", cell.getMarginBottomRows(), cp);
        }
        if (cell.getMarginLeftCols() != 0) {
            writeLong(cellVarName, "setMarginLeftCols", cell.getMarginLeftCols(), cp);
        }
        if (cell.getMarginRightCols() != 0) {
            writeLong(cellVarName, "setMarginRightCols", cell.getMarginRightCols(), cp);
        }

        if (cell.getLineSpacingMm() != AdsReportCell.DEFAULT_LINE_SPACING) {
            writeDouble(cellVarName, "setLineSpacingMm", cell.getLineSpacingMm(), cp);
        }

        if (cell.isWrapWord()) {
            writeBoolean(cellVarName, "setWrapWord", true, cp);
        }
        if (cell.isSnapTopEdge()) {
            writeBoolean(cellVarName, "setSnapTopEdge", true, cp);
        }
        if (cell.isSnapBottomEdge()) {
            writeBoolean(cellVarName, "setSnapBottomEdge", true, cp);
        }

        if (cell.isClipContent()) {
            writeBoolean(cellVarName, "setClipContent", true, cp);
        }
        
        if (!cell.isUseTxtPadding()) {
            writeBoolean(cellVarName, "setUseTxtPadding", false, cp);
        }

        writeAppearance(cell, cellVarName, cp);
    }

    private void writeAssignParamValuesMethod(CodePrinter cp) {
        cp.println("@Override");
        cp.println("@SuppressWarnings({\"deprecation\",\"unchecked\"})");
        cp.println("public void assignParamValues(java.util.Map<org.radixware.kernel.common.types.Id, Object> paramId2Value) {");
        cp.enterBlock();

        writeGetParamValuesBody(cp);

        for (AdsParameterPropertyDef param : report.getInputParameters()) {
            cp.println("set" + param.getId() + "$$$(" + param.getName() + ");");
        }

        cp.leaveBlock();
        cp.println("}");
        cp.println();
    }

    private void writeGetParamValuesBody(CodePrinter cp) {
        for (AdsParameterPropertyDef param : report.getInputParameters()) {
            final AdsTypeDeclaration type = param.getValue().getType();
            final AdsType adsType = type.resolve(param).get();
            final String paramValueVarName = param.getName() + "Value$";
            final String paramVarName = param.getName();
            cp.print("final Object ");
            cp.print(paramValueVarName);
            cp.print(" = ");
            cp.print("paramId2Value.get(");
            cp.print("org.radixware.kernel.common.types.Id.Factory.loadFrom(");
            cp.printStringLiteral(param.getId().toString());
            cp.println("));");
            if (!type.isArray() && adsType instanceof JavaType) {
                writeUsage(cp, type, param);
                cp.print(" ");
                cp.print(paramVarName);
                cp.println(" = " + param.getId() + ";");
                cp.println("if (" + paramValueVarName + "!= null){");
                cp.enterBlock();
                cp.print(paramVarName);
                cp.print(" = ");
                writeSetParamsValue(cp, param, type, adsType);
                cp.leaveBlock();
                cp.println();
                cp.print("}");
            } else {
                cp.print("final ");
                writeUsage(cp, type, param);
                cp.print(" ");
                cp.print(paramVarName);
                cp.print(" = ");
                writeSetParamsValue(cp, param, type, adsType);
            }
            cp.println();
            cp.println();
        }
    }

    private void writeSetParamsValue(CodePrinter cp, AdsParameterPropertyDef param, final AdsTypeDeclaration type, final AdsType adsType) {
        final String paramValueVarName = param.getName() + "Value$";
        if (adsType instanceof AdsEnumType) {
            //final AdsEnumType enumType = (AdsEnumType) type;
            //final AdsEnumDef enumDef = enumType.getSource();
            final EValType valType = type.getTypeId();
            final boolean multy = valType.isArrayType();
            final AdsType simpleType = RadixType.Factory.newInstance(valType);

            // support enum value or enum, for example:
            // final EnumType value = (obj instanceof Int ? EnumType.getForValue((Int)obj) : (EnumType)obj);
            // final EnumType.Arr value = (obj instanceof ArrInt ? new EnumTypeArr((ArrInt)obj) : (EnumType.Arr)obj);
            // that allows to simplify load values from xml PropertyList.
            cp.print("(");
            cp.print(paramValueVarName);
            cp.print(" instanceof ");
            writeUsage(cp, simpleType, param);
            cp.print(" ? ");
            if (multy) {
                cp.print("new ");
            }
            writeUsage(cp, type, param);
            if (multy) {
                cp.print("(");
            } else {
                cp.print(".getForValue(");
            }
            cp.print("(");
            writeUsage(cp, simpleType, param);
            cp.print(")");
            cp.print(paramValueVarName);
            cp.print(") : (");
            writeUsage(cp, type, param);
            cp.print(")");
            cp.print(paramValueVarName);
            cp.print(");");
        } else if (!type.isArray() && adsType instanceof JavaType) {
            cp.print('(');
            ((JavaType.PrimitiveTypeJavaSourceSupport.PrimitiveTypeCodeWriter) adsType.getJavaSourceSupport().getCodeWriter(usagePurpose)).writeConversionFromObjTypeCode(cp, paramValueVarName);
            cp.print(')');
            cp.print(";");
        } else {
            cp.print("(");
            writeUsage(cp, type, param);
            cp.print(")");
            cp.print(paramValueVarName);
            cp.print(";");
        }
    }

    private void writeOpenMapMethod(CodePrinter cp) {
        cp.println("@Override");
        cp.println("@SuppressWarnings({\"deprecation\",\"unchecked\"})");
        cp.println("public void open(java.util.Map<org.radixware.kernel.common.types.Id, Object> paramId2Value) {");
        cp.enterBlock();

        writeGetParamValuesBody(cp);

        cp.print(AdsSystemMethodDef.ID_REPORT_OPEN + "(");

        boolean paramPrinted = false;
        for (AdsParameterPropertyDef param : report.getInputParameters()) {
            if (paramPrinted) {
                cp.print(", ");
            }
            cp.print(param.getName());
            paramPrinted = true;
        }

        cp.println(");");

        cp.leaveBlock();
        cp.println("}");
        cp.println();
    }

    /* private void writeExecuteMethod(CodePrinter cp) {
     cp.print("public void execute(java.io.OutputStream stream, org.radixware.kernel.common.enums.EReportExportFormat format");

     for (AdsParameterPropertyDef param : report.getInputParameters()) {
     cp.print(", ");
     final AdsTypeDeclaration type = param.getValue().getType();
     writeUsage(cp, type, param);
     cp.print(" ");
     cp.print(param.getName());
     }

     cp.println(") {");

     cp.enterBlock();

     cp.println("try {");
     cp.enterBlock();
     cp.print("open(");

     boolean paramPrinted = false;
     for (AdsParameterPropertyDef param : report.getInputParameters()) {
     if (paramPrinted) {
     cp.print(", ");
     }
     cp.print(param.getName());
     paramPrinted = true;
     }

     cp.println(");");
     cp.println("export(stream, format);");
     cp.leaveBlock();
     cp.println("} finally {");
     cp.println("\tclose();");
     cp.println("}");

     cp.leaveBlock();
     cp.println("}");
     cp.println();
     }

     private void writeOpenMethod(CodePrinter cp) {
     cp.print("public void open(");

     boolean paramPrinted = false;
     for (AdsParameterPropertyDef param : report.getInputParameters()) {
     if (paramPrinted) {
     cp.print(", ");
     }
     final AdsTypeDeclaration type = param.getValue().getType();
     writeUsage(cp, type, param);
     cp.print(" ");
     cp.print(param.getName());
     paramPrinted = true;
     }

     cp.println(") {");

     AdsSqlClassExecuteWriter.writeBody(report, cp, true);

     cp.println("}");
     cp.println();
     }*/
    private void writeGetContextParameterIdMethod(CodePrinter cp) {
        final Id contextParameterId = report.getContextParameterId();
        if (contextParameterId != null) {
            cp.print("private static org.radixware.kernel.common.types.Id contextParameterId$ = org.radixware.kernel.common.types.Id.Factory.loadFrom(\"" + contextParameterId.toString() + "\");");
        }
        cp.println();
        cp.println("@Override");
        cp.println("public org.radixware.kernel.common.types.Id getContextParameterId() {");
        cp.enterBlock();
        if (contextParameterId != null) {
            cp.println("return contextParameterId$;");
        } else {
            cp.println("return null;");
        }
        cp.leaveBlock();
        cp.println("}");
        cp.println();
    }

    private void writeGetDeclaredColumnsMethod(CodePrinter cp) {
        AdsReportColumns columns = report.getColumns();
        cp.println("private final java.util.List<org.radixware.kernel.server.reports.RadReportColumnDef> declaredColumns = new java.util.ArrayList();");

        if (!columns.isEmpty()) {
            cp.println("{");
            cp.enterBlock();
            cp.println("org.radixware.kernel.server.reports.RadReportColumnDef column;");
            cp.println("AdsReportFormat csvExportFormat;");

            for (AdsReportColumnDef column : columns) {
                cp.println("csvExportFormat = new AdsReportFormat();");

                final AdsReportFormat format = column.getCsvExportFormat();
                if (format != null) {
                    writeFormat("csvExportFormat", cp, format);
                }

                cp.print("column = new org.radixware.kernel.server.reports.RadReportColumnDef(");
                // column id
                WriterUtils.writeIdUsage(cp, column.getId());
                cp.print(", ");

                // column name
                cp.print("\"" + column.getName() + "\", ");

                // popertyId
                if (column.getPropertyId() != null) {
                    WriterUtils.writeIdUsage(cp, column.getPropertyId());
                    cp.print(", ");
                } else {
                    cp.print("null, ");
                }

                // legacyCsvName
                if (column.getLegacyCsvName() != null) {
                    cp.print("\"" + column.getLegacyCsvName() + "\", ");
                } else {
                    cp.print("null, ");
                }

                // csvExportFormat
                cp.print("csvExportFormat, ");

                // titleId
                if (column.getTitleId() != null) {
                    WriterUtils.writeIdUsage(cp, column.getTitleId());
                    cp.print(", ");
                } else {
                    cp.print("null, ");
                }               

                // reportId
                cp.println("this.getId());");
                
                // xlsxExportParameters
                if (column.getXlsxExportParameters() != null) {
                    cp.println("column.setXlsxExportParameters(new org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef.XlsxExportParameters(");
                    cp.enterBlock();
                    if (column.getXlsxExportParameters().getXlsxExportFormat() != null) {
                        cp.println("\"" + column.getXlsxExportParameters().getXlsxExportFormat().trim() + "\", ");
                    } else {
                        cp.println("null, ");
                    }
                    WriterUtils.writeEnumFieldInvocation(cp, column.getXlsxExportParameters().getResizeMode());
                    cp.println(", ");
                    cp.println(column.getXlsxExportParameters().getWidth() + "));");
                    cp.leaveBlock();
                }

                cp.println("declaredColumns.add(column);");
            }
            cp.leaveBlock();
            cp.println("}");
        }

        cp.println("@Override");
        cp.println("public java.util.List<org.radixware.kernel.server.reports.RadReportColumnDef> getDeclaredColumns() {");
        cp.enterBlock();
        cp.println("return declaredColumns;");
        cp.leaveBlock();
        cp.println("}");
        cp.println();
    }

    private void writeGetExportXlsxInfoMethod(CodePrinter cp, final AdsXlsxReportInfo xlsxReportInfo) {
        cp.println("private AdsXlsxReportInfo xlsxExportInfo = new AdsXlsxReportInfo();");
        cp.println("{");
        cp.enterBlock();

        writeBoolean("xlsxExportInfo", "setIsExportColumnName", xlsxReportInfo.isExportColumnName(), cp);
        writeBoolean("xlsxExportInfo", "setIsExportToXlsxEnabled", xlsxReportInfo.isExportToXlsxEnabled(), cp);

        if (xlsxReportInfo.getSheetNameId() != null) {
            cp.print("xlsxExportInfo.setSheetNameId(");
            WriterUtils.writeIdUsage(cp, xlsxReportInfo.getSheetNameId());
            cp.println(");");
        }

        cp.leaveBlock();
        cp.println("}");
        cp.println();

        cp.println("@Override");
        cp.println("public AdsXlsxReportInfo getExportXlsxInfo() {");
        cp.enterBlock();
        cp.println("return xlsxExportInfo;");
        cp.leaveBlock();
        cp.println("}");
        cp.println();
    }
}
