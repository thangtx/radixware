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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.ADS;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.CLAZZ;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.COMMON;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.DEFS;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.FACTORY;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.ID_TYPE_NAME;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.JAVALANGOBJECT_TYPENAME;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.JAVAUTILMAP_TYPE_NAME;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.KERNEL;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.ORG;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.RADIXWARE;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.REPORT;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.SQL;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.ReenterableSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.BranchLabel;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.radixware.kernel.common.compiler.core.JmlHelper;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsChartDataInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportExpressionCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormattedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroupBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroups;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportTextCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.common.enums.EReportCellType;
import static org.radixware.kernel.common.enums.EReportCellType.CHART;
import static org.radixware.kernel.common.enums.EReportCellType.DB_IMAGE;
import static org.radixware.kernel.common.enums.EReportCellType.EXPRESSION;
import static org.radixware.kernel.common.enums.EReportCellType.IMAGE;
import static org.radixware.kernel.common.enums.EReportCellType.PROPERTY;
import static org.radixware.kernel.common.enums.EReportCellType.SPECIAL;
import static org.radixware.kernel.common.enums.EReportCellType.SUMMARY;
import static org.radixware.kernel.common.enums.EReportCellType.TEXT;
import org.radixware.kernel.common.enums.EReportChartAxisLabelPositions;
import org.radixware.kernel.common.enums.EReportChartAxisType;
import org.radixware.kernel.common.enums.EReportChartCellType;
import org.radixware.kernel.common.enums.EReportChartLegendPosition;
import org.radixware.kernel.common.enums.EReportChartSeriesType;
import org.radixware.kernel.common.enums.EReportSummaryCellType;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public class ReportClassGenerator extends SqlClassGenerator {

    public static final char[][] ADSREPORTFORM_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportForm".toCharArray()
    };
    public static final char[][] ADSSUBREPORT_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsSubReport".toCharArray()
    };
    public static final char[][] ADSSUBREPORTFACTORY_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, ADSSUBREPORT_TYPE_NAME[9], FACTORY
    };
    public static final char[][] ADSSUBREPORTASSOCIATION_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, ADSSUBREPORT_TYPE_NAME[9], "Association".toCharArray()
    };
    public static final char[][] ADSSUBREPORTASSOCIATIONFACTORY_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, ADSSUBREPORT_TYPE_NAME[9], "Association".toCharArray(), FACTORY
    };
    public static final char[][] ADSREPORTFORMFACTORY_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, ADSREPORTFORM_TYPE_NAME[9], FACTORY
    };
    public static final char[][] ADSREPORTGROUP_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportGroup".toCharArray()
    };
    public static final char[][] ADSREPORTCELLFACTORY_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportCellFactory".toCharArray()
    };
    public static final char[][] ADSREPORTWIDGETCONTAINER_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportWidgetContainer".toCharArray()
    };
    public static final char[][] ADSREPORTCELL_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportCell".toCharArray()
    };
    public static final char[][] ADSREPORTBAND_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportBand".toCharArray()
    };
    public static final char[][] ADSREPORTCHARTAXIS_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportChartAxis".toCharArray()
    };
    public static final char[][] ADSREPORTCHARTSERIES_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT, "AdsReportChartSeries".toCharArray()
    };
    public static final char[][] ADSREPORTS_PACJAGE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, DEFS, ADS, CLAZZ, SQL, REPORT
    };

    public ReportClassGenerator(CompilationResult compilationResult, ReferenceContext referenceContext) {
        super(compilationResult, referenceContext);
    }

    @Override
    public void addSpecificInnerTypesAndMethods(AdsSqlClassDef sqlClass, CompilationUnitScope scope, List<TypeDeclaration> types, List<AbstractMethodDeclaration> methods, List<FieldDeclaration> fields) {
        super.addSpecificInnerTypesAndMethods(sqlClass, scope, types, methods, fields);
        if (!(sqlClass instanceof AdsReportClassDef)) {
            return;
        }
        AdsReportClassDef report = (AdsReportClassDef) sqlClass;
        methods.add(createGetIdMethod(report));
        final AdsReportForm form = report.getForm();
        for (AdsReportBand band : form.getBands()) {
            TypeDeclaration bandClass = createBandClass(report, band, scope);
            if (bandClass != null) {
                types.add(bandClass);
            }
            createCellClasses(report, band, scope, types, methods);
        }
        methods.add(createGroupConditionMethod(report, scope, form.getGroups()));
        MethodDeclaration cf = createFormMethod(form);
        if (cf == null) {
            return;
        }
        methods.add(cf);

        if (report.getCsvInfo() != null && report.getCsvInfo().isExportToCsvEnabled()) {
            AdsCsvReportInfo csvInfo = report.getCsvInfo();
            methods.add(createCsvExportColumnsMethod(csvInfo));
            if (csvInfo.isExportToCsvEnabled() && csvInfo.getRowCondition() != null) {
                methods.add(createCsvRowConditionMethod(report, csvInfo, scope));
            }
        }

        MethodDeclaration apv = createAssignParamValuesMethod(report);
        if (apv == null) {
            return;
        }
        methods.add(apv);
        MethodDeclaration om = createOpenMapMethod(report);
        if (om == null) {
            return;
        }
        methods.add(om);
        methods.add(createContextParameterIdMethod(report));
    }

    public MethodDeclaration createGetIdMethod(AdsReportClassDef report) {
        MethodDeclaration getId = createMethod("getId", true, createQualifiedType(ID_TYPE_NAME), ClassFileConstants.AccPublic | ClassFileConstants.AccFinal);
        getId.statements = new Statement[]{
            new ReturnStatement(createIdInvocation(report.getId()), 0, 0)
        };
        return getId;
    }

    public MethodDeclaration createGroupConditionMethod(AdsReportClassDef report, CompilationUnitScope scope, AdsReportGroups groups) {
        MethodDeclaration method = createMethod("getGroupCondition", true, createQualifiedType(JAVALANGOBJECT_TYPENAME), ClassFileConstants.AccPublic);
        method.arguments = new Argument[]{
            new Argument("groupIndex".toCharArray(), 0, new SingleTypeReference("int".toCharArray(), 0), 0)
        };
        List<Statement> stmts = new LinkedList<>();
        if (!groups.isEmpty()) {
            SwitchStatement sw = new SwitchStatement();
            sw.cases = new CaseStatement[groups.size()];
            sw.expression = new SingleNameReference("groupIndex".toCharArray(), 0);
            List<Statement> switchStatements = new LinkedList<>();
            for (int i = 0; i < groups.size(); i++) {
                AdsReportGroup group = groups.get(i);
                CaseStatement c = new CaseStatement(IntLiteral.buildIntLiteral(String.valueOf(group.getIndex()).toCharArray(), 0, 0), 0, 0);
                c.targetLabel = new BranchLabel();
                c.targetLabel.position = switchStatements.size();
                Statement[] caseStatements = JmlHelper.parseStatements(scope, report, referenceContext, (CompilationUnitDeclaration) scope.referenceContext(), group.getCondition());
                if (caseStatements != null) {
                    for (Statement stmt : caseStatements) {
                        switchStatements.add(stmt);
                    }
                }
                sw.cases[i] = c;
            }
            sw.statements = switchStatements.toArray(new Statement[switchStatements.size()]);
        }
        stmts.add(createThrow("IllegalStateException".toCharArray(), null, false));
        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    private static String getBandJavaName(IReportWidgetContainer band) {
        if (band instanceof AdsReportGroupBand) {
            final AdsReportGroup group = ((AdsReportGroupBand) band).getOwnerGroup();
            final int index = group.getIndex();
            return "Group" + String.valueOf(index) + (group.getFooterBand() == band ? "Footer" : "Header") + "Band";
        } else if (band instanceof AdsReportWidgetContainer) {
            AdsReportWidgetContainer cellContainer = (AdsReportWidgetContainer) band;
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
        IReportWidgetContainer reportContainer = (IReportWidgetContainer) cell.getOwnerWidget();
        final int index = reportContainer.getWidgets().indexOf(cell);
        final String bandJavaName = getBandJavaName(reportContainer);
        return bandJavaName + "Cell" + index;
    }

    public MethodDeclaration createFormMethod(AdsReportForm form) {
        TypeReference reportFormType = createQualifiedType(ADSREPORTFORM_TYPE_NAME);
        MethodDeclaration method = createMethod("createForm", true, reportFormType, ClassFileConstants.AccPublic);
        List<Statement> stmts = new LinkedList<>();
        char[] formVarName = "form".toCharArray();
        LocalDeclaration formVar = new LocalDeclaration(formVarName, 0, 0);
        formVar.type = reportFormType;
        formVar.modifiers = ClassFileConstants.AccFinal;
        MessageSend formInit = new MessageSend();
        formInit.selector = NEWINSTANCE;
        formInit.receiver = createQualifiedName(ADSREPORTFORMFACTORY_TYPE_NAME);
        formVar.initialization = formInit;

        stmts.add(formVar);
        ReenterableSingleNameReference formVarRef = new ReenterableSingleNameReference(formVarName);

        stmts.add(createSetBoolean("setColumnsHeaderForEachGroupDisplayed".toCharArray(), formVarRef, form.isColumnsHeaderForEachGroupDisplayed()));

        stmts.add(createSetColor("setBgColor".toCharArray(), formVarRef, form.getBgColor()));
        stmts.add(createSetColor("setFgColor".toCharArray(), formVarRef, form.getBgColor()));

        MessageSend getMargin = new MessageSend();
        getMargin.selector = "getMargin".toCharArray();
        getMargin.receiver = formVarRef;

        stmts.add(createSetDouble("setTopMm".toCharArray(), getMargin, form.getMargin().getTopMm()));
        stmts.add(createSetDouble("setRightMm".toCharArray(), getMargin, form.getMargin().getRightMm()));
        stmts.add(createSetDouble("setBottomMm".toCharArray(), getMargin, form.getMargin().getBottomMm()));
        stmts.add(createSetDouble("setLeftMm".toCharArray(), getMargin, form.getMargin().getLeftMm()));

        stmts.add(createSetInt("setPageWidthMm".toCharArray(), formVarRef, form.getPageWidthMm()));
        stmts.add(createSetInt("setPageHeightMm".toCharArray(), formVarRef, form.getPageHeightMm()));

        for (AdsReportBand band : form.getBands()) {
            if (!(band instanceof AdsReportGroupBand)) {
                final String bandJavaName = getBandJavaName(band);
                if (!createBand(band, formVarName, bandJavaName, stmts)) {
                    return null;
                }
            }
        }

        for (int i = 0; i < form.getGroups().size(); i++) {
            final AdsReportGroup group = form.getGroups().get(i);
            final char[] groupName = ("group" + i).toCharArray();
            LocalDeclaration groupVar = new LocalDeclaration(groupName, 0, 0);
            groupVar.type = createQualifiedType(ADSREPORTGROUP_TYPE_NAME);
            groupVar.modifiers = ClassFileConstants.AccFinal;
            MessageSend addGroup = new MessageSend();
            MessageSend getGroups = new MessageSend();
            getGroups.selector = "getGroups".toCharArray();
            getGroups.receiver = formVarRef;
            addGroup.selector = "addNew".toCharArray();
            addGroup.receiver = getGroups;

            groupVar.initialization = addGroup;

            stmts.add(groupVar);

            final AdsReportBand headerBand = group.getHeaderBand();
            if (headerBand != null) {
                if (!createBand(headerBand, groupName, "Header", stmts)) {
                    return null;
                }

            } else {
                stmts.add(createSetValue("setHeaderBand".toCharArray(), new SingleNameReference(groupName, 0), new NullLiteral(0, 0)));
            }
            final AdsReportBand footerBand = group.getFooterBand();
            if (footerBand != null) {
                if (!createBand(footerBand, groupName, "Footer", stmts)) {
                    return null;
                }
            } else {
                stmts.add(createSetValue("setFooterBand".toCharArray(), new SingleNameReference(groupName, 0), new NullLiteral(0, 0)));
            }
        }

        stmts.add(new ReturnStatement(formVarRef, 0, 0));
        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    private boolean createBand(AdsReportBand band, char[] ownerName, String setterName, List<Statement> stmts) {
        final String bandJavaName = getBandJavaName(band);
        final char[] bandVarName = toLower(bandJavaName).toCharArray();
        TypeReference bandType = (band.getOnAdding().getItems().isEmpty() ? createQualifiedType(getNameOfTypeFromReportsPackage(band.getClass().getSimpleName())) : new SingleTypeReference(bandJavaName.toCharArray(), 0));
        LocalDeclaration bandVar = new LocalDeclaration(bandVarName, 0, 0);
        bandVar.type = bandType;
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = bandVar.type;
        bandVar.initialization = alloc;
        stmts.add(bandVar);
        ReenterableSingleNameReference ownerRef = new ReenterableSingleNameReference(ownerName);
        ReenterableSingleNameReference bandVarRef = new ReenterableSingleNameReference(bandVarName);
        stmts.add(createSetValue(("set" + setterName + "Band").toCharArray(), ownerRef, bandVarRef));

        createBandAppearance(band, bandVarRef, stmts);
        stmts.add(createSetEnumValue("setLayout".toCharArray(), bandVarRef, band.getLayout()));

        for (AdsReportWidget cell : band.getWidgets()) {
            if (!createCell(cell, bandVarRef, stmts)) {
                return false;
            }
        }
        for (int i = 0; i < band.getPreReports().size(); i++) {
            final AdsSubReport subReport = band.getPreReports().get(i);
            final char[] subReportVarName = (bandVarName + "PreReport" + i).toCharArray();

            if (!createSubReport(subReport, subReportVarName, stmts)) {
                return false;
            }
            stmts.add(createMessageSend(createMessageSend(bandVarRef, "getPreReports".toCharArray()), "add".toCharArray(), new SingleNameReference(subReportVarName, 0)));
        }

        for (int i = 0; i < band.getPostReports().size(); i++) {
            final AdsSubReport subReport = band.getPostReports().get(i);
            final char[] subReportVarName = (bandVarName + "PostReport" + i).toCharArray();
            if (!createSubReport(subReport, subReportVarName, stmts)) {
                return false;
            }
            stmts.add(createMessageSend(createMessageSend(bandVarRef, "getPostReports".toCharArray()), "add".toCharArray(), new SingleNameReference(subReportVarName, 0)));
        }
        return true;
    }

    private void createBandAppearance(AdsReportBand band, SingleNameReference bandVarRef, List<Statement> stmts) {
        stmts.add(createSetDouble("setHeightMm".toCharArray(), bandVarRef, band.getHeightMm()));
        stmts.add(createSetBoolean("setStartOnNewPage".toCharArray(), bandVarRef, band.isStartOnNewPage()));
        stmts.add(createSetBoolean("setLastOnPage".toCharArray(), bandVarRef, band.isLastOnPage()));
        stmts.add(createSetBoolean("setMultiPage".toCharArray(), bandVarRef, band.isMultiPage()));
        stmts.add(createSetBoolean("setAutoHeight".toCharArray(), bandVarRef, band.isAutoHeight()));
        stmts.add(createSetBoolean("setCellWrappingEnabled".toCharArray(), bandVarRef, band.isCellWrappingEnabled()));


        createAppearance(band, bandVarRef, stmts);
    }

    private void createAppearance(AdsReportAbstractAppearance appearance, SingleNameReference recv, List<Statement> stmts) {
        if (!appearance.isBgColorInherited()) {
            stmts.add(createSetBoolean("setBgColorInherited".toCharArray(), recv, false));
            stmts.add(createSetColor("setBgColor".toCharArray(), recv, appearance.getBgColor()));
        }

        if (!appearance.isFgColorInherited()) {
            stmts.add(createSetBoolean("setFgColorInherited".toCharArray(), recv, false));
            stmts.add(createSetColor("setFgColor".toCharArray(), recv, appearance.getBgColor()));
        }

        if (!(appearance instanceof AdsReportCell) || !((AdsReportCell) appearance).isFontInherited()) {
            final AdsReportAbstractAppearance.Font font = appearance.getFont();
            createFontAppearance(recv, "getFont", font, stmts);
        }

        final AdsReportAbstractAppearance.Border border = appearance.getBorder();
        if (border.isDisplayed()) {
            MessageSend getBorder = new MessageSend();
            getBorder.selector = "getBorder".toCharArray();
            getBorder.receiver = recv;
            if (border.getStyle() != EReportBorderStyle.SOLID) {
                stmts.add(createSetEnumValue("setStyle".toCharArray(), getBorder, border.getStyle()));
            }

            if (border.isOnTop()) {
                stmts.add(createSetBoolean("setOnTop".toCharArray(), getBorder, true));
            }
            if (border.isOnRight()) {
                stmts.add(createSetBoolean("setOnRight".toCharArray(), getBorder, true));
            }
            if (border.isOnBottom()) {
                stmts.add(createSetBoolean("setOnBottom".toCharArray(), getBorder, true));
            }
            if (border.isOnLeft()) {
                stmts.add(createSetBoolean("setOnLeft".toCharArray(), getBorder, true));
            }

            final Color borderColor = border.getColor();
            if (!Color.BLACK.equals(borderColor)) {
                stmts.add(createSetColor("setColor".toCharArray(), getBorder, border.getColor()));
            }

            if (border.getThicknessMm() != AdsReportAbstractAppearance.Border.DEFAULT_THICKNESS_MM) {
                stmts.add(createSetDouble("setThicknessMm".toCharArray(), getBorder, border.getThicknessMm()));
            }
        }
    }

    private void createFontAppearance(final SingleNameReference recv, final String fontMthName, final AdsReportAbstractAppearance.Font font, List<Statement> stmts) {
        MessageSend getFont = new MessageSend();
        getFont.selector = fontMthName.toCharArray();
        getFont.receiver = recv;

        stmts.add(createSetValue("setName".toCharArray(), getFont, new StringLiteral(font.getName().toCharArray(), 0, 0, 0)));
        stmts.add(createSetDouble("setSizeMm".toCharArray(), getFont, font.getSizeMm()));
        stmts.add(createSetBoolean("setBold".toCharArray(), getFont, font.isBold()));
        stmts.add(createSetBoolean("setItalic".toCharArray(), getFont, font.isItalic()));
    }

    private boolean createSubReport(AdsSubReport subReport, char[] subReportVarName, List<Statement> stmts) {
        LocalDeclaration subReportVar = new LocalDeclaration(subReportVarName, 0, 0);
        subReportVar.type = createQualifiedType(ADSSUBREPORT_TYPE_NAME);
        MessageSend init = new MessageSend();
        init.selector = NEWINSTANCE;
        init.receiver = createQualifiedName(ADSSUBREPORTFACTORY_TYPE_NAME);
        subReportVar.initialization = init;
        stmts.add(subReportVar);

        AdsReportClassDef srd = subReport.findReport();
        if (srd == null) {
            return false;
        }
        SingleNameReference subReportVarRef = new ReenterableSingleNameReference(subReportVarName);
        stmts.add(createSetValue("setReportId".toCharArray(), subReportVarRef, createIdInvocation(srd.getId())));

        for (int j = 0; j < subReport.getAssociations().size(); j++) {
            final AdsSubReport.Association association = subReport.getAssociations().get(j);
            final char[] associationVarName = (subReportVarName + "Association" + j).toCharArray();
            LocalDeclaration assocVar = new LocalDeclaration(associationVarName, 0, 0);
            assocVar.type = createQualifiedType(ADSSUBREPORTASSOCIATION_TYPE_NAME);
            MessageSend initAssoc = new MessageSend();
            initAssoc.selector = NEWINSTANCE;
            initAssoc.receiver = createQualifiedName(ADSSUBREPORTASSOCIATIONFACTORY_TYPE_NAME);
            assocVar.initialization = initAssoc;
            stmts.add(assocVar);

            AdsPropertyDef prop = association.findProperty();
            if (prop == null) {
                return false;
            }
            SingleNameReference assocVarRef = new ReenterableSingleNameReference(associationVarName);

            stmts.add(createSetValue("setPropertyId".toCharArray(), assocVarRef, createIdInvocation(prop.getId())));


            AdsParameterPropertyDef param = association.findParameter();
            if (param == null) {
                return false;
            }
            stmts.add(createSetValue("setParameterId".toCharArray(), assocVarRef, createIdInvocation(param.getId())));

            stmts.add(createMessageSend(createMessageSend(subReportVarRef, "getAssociations".toCharArray()), "add".toCharArray(), assocVarRef));
        }
        return true;
    }

    public MethodDeclaration createCsvExportColumnsMethod(AdsCsvReportInfo csvReportInfo) {
        MethodDeclaration method = createMethod("getExportCsvInfo", true, createQualifiedType(getNameOfTypeFromReportsPackage("AdsCsvReportInfo")), ClassFileConstants.AccPublic);
        List<Statement> stmts = new LinkedList<>();
        LocalDeclaration csvInfoVar = new LocalDeclaration("getExportCsvInfo".toCharArray(), 0, 0);
        csvInfoVar.type = createQualifiedType(getNameOfTypeFromReportsPackage("AdsCsvReportInfo"));
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = createQualifiedType(getNameOfTypeFromReportsPackage("AdsCsvReportInfo"));
        csvInfoVar.initialization = alloc;
        stmts.add(csvInfoVar);
        SingleNameReference csvInfoVarRef = new ReenterableSingleNameReference(csvInfoVar.name);
        AdsCsvReportInfo.CsvExportColumns exportCsvColumns = csvReportInfo.getExportCsvColumns();
        if (exportCsvColumns != null && !exportCsvColumns.isEmpty()) {
            LocalDeclaration exportCsvColumnVar = new LocalDeclaration("exportCsvColumn".toCharArray(), 0, 0);
            exportCsvColumnVar.type = createQualifiedType(getNameOfTypeFromReportsPackage("AdsExportCsvColumn"));
            stmts.add(exportCsvColumnVar);
            LocalDeclaration formatVar = new LocalDeclaration("format".toCharArray(), 0, 0);
            formatVar.type = createQualifiedType(getNameOfTypeFromReportsPackage("AdsReportFormat"));
            stmts.add(formatVar);
            for (AdsExportCsvColumn exportCsvColumn : exportCsvColumns) {
                SingleNameReference formatVarRef = new ReenterableSingleNameReference(formatVar.name);
                alloc = new AllocationExpression();
                alloc.type = formatVar.type;
                stmts.add(new Assignment(formatVarRef, alloc, 0));
                AdsReportFormat format = exportCsvColumn.getFormat();
                if (format != null) {
                    stmts.add(createSetBoolean("setUseDefaultFormat", formatVarRef, format.getUseDefaultFormat()));

                    if (!format.getUseDefaultFormat()) {
                        if (format.getDateStyle() != null) {
                            stmts.add(createSetEnumValue("setDateStyle", formatVarRef, format.getDateStyle()));
                        }
                        if (format.getTimeStyle() != null) {
                            stmts.add(createSetEnumValue("setTimeStyle", formatVarRef, format.getTimeStyle()));
                        }
                        if (format.getPattern() != null) {
                            stmts.add(createSetString("setPattern", formatVarRef, format.getPattern()));
                        }
                        stmts.add(createSetInt("setPrecission", formatVarRef, format.getPrecission()));
                        stmts.add(createSetString("setDesimalDelimeter", formatVarRef, format.getDesimalDelimeter()));
                        stmts.add(createSetEnumValue("setTriadDelimeterType", formatVarRef, format.getTriadDelimeterType()));
                        if (format.getTriadDelimeterType() == ETriadDelimeterType.SPECIFIED) {
                            stmts.add(createSetString("setTriadDelimeter", formatVarRef, format.getTriadDelimeter()));
                        }
                    }
                }
                SingleNameReference exportCsvColumnVarRef = new ReenterableSingleNameReference(exportCsvColumnVar.name);
                alloc = new AllocationExpression();
                alloc.arguments = new Expression[]{
                    createIdInvocation(exportCsvColumn.getPropId()),
                    createStringLiteral(exportCsvColumn.getExtName()),
                    formatVarRef
                };
                alloc.type = exportCsvColumnVar.type;
                stmts.add(new Assignment(exportCsvColumnVarRef, alloc, 0));
                stmts.add(createMessageSend(createMessageSend(csvInfoVarRef, "getExportCsvColumns"), "add", exportCsvColumnVarRef));
            }
        }
        stmts.add(createSetString("setDelimiter", csvInfoVarRef, csvReportInfo.getDelimiter()));
        stmts.add(createSetBoolean("setIsExportColumnName", csvInfoVarRef, csvReportInfo.isExportColumnName()));
        stmts.add(new ReturnStatement(csvInfoVarRef, 0, 0));
        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    public MethodDeclaration createCsvRowConditionMethod(AdsReportClassDef report, AdsCsvReportInfo csvInfo, CompilationUnitScope scope) {
        MethodDeclaration method = createMethod("isCsvRowVisible", true, new SingleTypeReference(TypeReference.BOOLEAN, 0), ClassFileConstants.AccPublic);
        List<Statement> stmts = new LinkedList<>();

        Statement[] conditionStatements = JmlHelper.parseStatements(scope, report, referenceContext, (CompilationUnitDeclaration) scope.referenceContext(), csvInfo.getRowCondition());
        wrapWithProfilerCode(csvInfo, conditionStatements, stmts);

        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    public MethodDeclaration createAssignParamValuesMethod(AdsReportClassDef report) {
        MethodDeclaration method = createMethod("assignParamValues", true, null, ClassFileConstants.AccPublic);

        method.arguments = new Argument[]{
            createParamId2ValArg()
        };
        List<Statement> stmts = new LinkedList<>();
        createGetParamValuesBody(report, stmts);

        for (AdsParameterPropertyDef param : report.getInputParameters()) {
            stmts.add(createSetValue("set" + param.getId() + "$$$", new SingleNameReference(param.getName().toCharArray(), 0)));
        }
        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    private void createGetParamValuesBody(AdsReportClassDef report, List<Statement> stmts) {
        for (AdsParameterPropertyDef param : report.getInputParameters()) {
            final char[] paramVarName = param.getName().toCharArray();
            final char[] paramValueVarName = (param.getName() + "Value$").toCharArray();

            LocalDeclaration paramValVar = new LocalDeclaration(paramValueVarName, 0, 0);
            paramValVar.modifiers = ClassFileConstants.AccFinal;
            paramValVar.type = createQualifiedType(JAVALANGOBJECT_TYPENAME);

            SingleNameReference paramId2ValueRef = new ReenterableSingleNameReference("paramId2Value".toCharArray());
            paramValVar.initialization = createMessageSend(paramId2ValueRef, "get", createIdInvocation(param.getId()));
            stmts.add(paramValVar);
            final AdsTypeDeclaration type = param.getValue().getType();
            SingleNameReference paramValVarRef = new ReenterableSingleNameReference(paramValueVarName);

            LocalDeclaration paramVar = new LocalDeclaration(paramVarName, 0, 0);
            paramVar.type = new AdsTypeReference(report, type);
            paramVar.modifiers = ClassFileConstants.AccFinal;
            Expression paramVarInit;

            final AdsType adsType = type.resolve(param).get();

            if (adsType instanceof AdsEnumType) {
                //final AdsEnumType enumType = (AdsEnumType) type;
                //final AdsEnumDef enumDef = enumType.getSource();
                final EValType valType = type.getTypeId();
                final boolean multy = valType.isArrayType();

                // support enum value or enum, for example:
                // final EnumType value = (obj instanceof Int ? EnumType.getForValue((Int)obj) : (EnumType)obj);
                // final EnumType.Arr value = (obj instanceof ArrInt ? new EnumTypeArr((ArrInt)obj) : (EnumType.Arr)obj);
                // that allows to simplify load values from xml PropertyList.
                Expression convert;
                if (multy) {
                    AllocationExpression alloc = new AllocationExpression();
                    alloc.type = new AdsTypeReference(report, type);
                    alloc.arguments = new Expression[]{
                        createCastExpression(paramValVarRef, new AdsTypeReference(report, AdsTypeDeclaration.Factory.newInstance(valType)))
                    };
                    convert = alloc;
                } else {
                    TaggedSingleNameReference recv = new TaggedSingleNameReference(report, ((AdsEnumType) adsType).getSource(), false);
                    convert = createMessageSend(recv, "getForValue", createCastExpression(paramValVarRef, new AdsTypeReference(report, AdsTypeDeclaration.Factory.newInstance(valType))));
                }

                paramVarInit = new ConditionalExpression(new InstanceOfExpression(paramValVarRef, new AdsTypeReference(report, AdsTypeDeclaration.Factory.newInstance(valType))),
                        convert,
                        createCastExpression(paramValVarRef, new AdsTypeReference(report, type)));

            } else {
                paramVarInit = createCastExpression(paramValVarRef, new AdsTypeReference(report, type));
            }
            paramVar.initialization = paramVarInit;
            stmts.add(paramVar);
        }
    }

    private Argument createParamId2ValArg() {
        return new Argument("paramId2Value".toCharArray(), 0, createQualifiedType(JAVAUTILMAP_TYPE_NAME, createQualifiedType(ID_TYPE_NAME), createQualifiedType(JAVALANGOBJECT_TYPENAME)), 0);
    }

    public MethodDeclaration createOpenMapMethod(AdsReportClassDef report) {
        MethodDeclaration method = createMethod("open", true, null, ClassFileConstants.AccPublic);
        method.arguments = new Argument[]{
            createParamId2ValArg()
        };
        List<Statement> stmts = new LinkedList<>();

        createGetParamValuesBody(report, stmts);

        MessageSend callOpen = createMessageSend(ThisReference.implicitThis(), AdsSystemMethodDef.ID_REPORT_OPEN.toCharArray());

        List<Expression> params = new LinkedList<>();

        for (AdsParameterPropertyDef param : report.getInputParameters()) {

            params.add(new SingleNameReference(param.getName().toCharArray(), 0));

        }
        callOpen.arguments = params.toArray(new Expression[params.size()]);

        stmts.add(callOpen);
        method.statements = stmts.toArray(new Statement[stmts.size()]);

        return method;
    }

    public MethodDeclaration createContextParameterIdMethod(AdsReportClassDef report) {
        MethodDeclaration getId = createMethod("getContextParameterId", true, createQualifiedType(ID_TYPE_NAME), ClassFileConstants.AccPublic | ClassFileConstants.AccFinal);
        final Id contextParameterId = report.getContextParameterId();
        if (contextParameterId != null) {
            getId.statements = new Statement[]{
                new ReturnStatement(createIdInvocation(contextParameterId), 0, 0)
            };
        } else {
            getId.statements = new Statement[]{
                new ReturnStatement(new NullLiteral(0, 0), 0, 0)
            };
        }

        return getId;
    }
    private static Map<String, char[][]> cellTypeNames = new HashMap<>();

    private char[][] getNameOfTypeFromReportsPackage(String cellClassName) {
        char[][] name = cellTypeNames.get(cellClassName);
        if (name == null) {
            name = new char[ADSREPORTS_PACJAGE_NAME.length + 1][];
            System.arraycopy(ADSREPORTS_PACJAGE_NAME, 0, name, 0, ADSREPORTS_PACJAGE_NAME.length);
            name[ADSREPORTS_PACJAGE_NAME.length] = cellClassName.toCharArray();
            cellTypeNames.put(cellClassName, name);
        }
        return name;
    }

    private boolean createCell(AdsReportWidget widget, final SingleNameReference bandVarRef, List<Statement> stmts) {
        final String cellJavaName = getCellJavaName(widget);
        final char[] cellVarName = toLower(cellJavaName).toCharArray();


        final char[][] cellTypeName = getNameOfTypeFromReportsPackage(widget.getClass().getSimpleName());

        LocalDeclaration cellVar = new LocalDeclaration(cellVarName, 0, 0);
        cellVar.type = createQualifiedType(cellTypeName);
        if (widget.isReportContainer()) {
            AllocationExpression alloc = new AllocationExpression();
            alloc.type = createQualifiedType(ADSREPORTWIDGETCONTAINER_TYPE_NAME);
            cellVar.initialization = alloc;
        } else {
            AdsReportCell cell = (AdsReportCell) widget;
            if (!cell.getOnAdding().getItems().isEmpty() || cell.getCellType() == EReportCellType.EXPRESSION) {
                AllocationExpression alloc = new AllocationExpression();
                alloc.type = new SingleTypeReference(cellJavaName.toCharArray(), 0);
                cellVar.initialization = alloc;
            } else {
                cellVar.initialization = createMessageSend(createQualifiedName(ADSREPORTCELLFACTORY_TYPE_NAME), ("new" + cell.getCellType().getValue() + "Cell").toCharArray());
            }
        }
        stmts.add(cellVar);
        SingleNameReference cellVarRef = new ReenterableSingleNameReference(cellVarName);
        stmts.add(createMessageSend(createMessageSend(bandVarRef, "getWidgets".toCharArray()), "add".toCharArray(), cellVarRef));
        stmts.add(createSetDouble("setLeftMm", cellVarRef, widget.getLeftMm()));
        stmts.add(createSetDouble("setTopMm", cellVarRef, widget.getTopMm()));
        stmts.add(createSetDouble("setWidthMm", cellVarRef, widget.getWidthMm()));
        stmts.add(createSetDouble("setHeightMm", cellVarRef, widget.getHeightMm()));
        stmts.add(createSetInt("setColumnSpan", cellVarRef, widget.getColumnSpan()));


        final String name = widget.getName();
        if (name != null && !name.isEmpty()) {
            stmts.add(createSetString("setName", cellVarRef, widget.getName()));
        }

        if (widget.isReportContainer()) {
            stmts.add(createSetEnumValue("setLayout", cellVarRef, ((IReportWidgetContainer) widget).getLayout()));
            for (AdsReportWidget cell : ((IReportWidgetContainer) widget).getWidgets()) {
                if (!createCell(cell, cellVarRef, stmts)) {
                    return false;
                }
            }
        } else {
            AdsReportCell cell = (AdsReportCell) widget;

            switch (cell.getCellType()) {
                case EXPRESSION:
                    break;
                case PROPERTY:
                    final AdsReportPropertyCell propertyCell = (AdsReportPropertyCell) cell;
                    AdsPropertyDef refProp = propertyCell.findProperty();
                    if (refProp == null) {
                        return false;
                    }
                    stmts.add(createSetId("setPropertyId", cellVarRef, refProp.getId()));

                    if (propertyCell.getNullTitleId() != null) {
                        stmts.add(createSetId("setNullTitleId", cellVarRef, propertyCell.getNullTitleId()));
                    }
                    break;
                case TEXT:
                    final AdsReportTextCell textCell = (AdsReportTextCell) cell;
                    if (textCell.getTextId() != null) {
                        stmts.add(createSetId("setTextId", cellVarRef, textCell.getTextId()));
                    }
                    break;
                case IMAGE:
                    final AdsReportImageCell imageCell = (AdsReportImageCell) cell;
                    if (imageCell.getImageId() != null) {
                        stmts.add(createSetId("setImageId", cellVarRef, imageCell.getImageId()));
                    }
                    break;
                case DB_IMAGE:
                    final AdsReportDbImageCell dbImageCell = (AdsReportDbImageCell) cell;
                    if (dbImageCell.getDataPropertyId() != null) {
                        stmts.add(createSetId("setDataPropertyId", cellVarRef, dbImageCell.getDataPropertyId()));
                    }
                    if (dbImageCell.getMimeTypePropertyId() != null) {
                        stmts.add(createSetId("setMimeTypePropertyId", cellVarRef, dbImageCell.getMimeTypePropertyId()));
                    }
                    break;
                case SPECIAL:
                    final AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
                    stmts.add(createSetEnumValue("setSpecialType", cellVarRef, specialCell.getSpecialType()));
                    break;
                case SUMMARY:
                    final AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
                    final EReportSummaryCellType summaryType = summaryCell.getSummaryType();
                    if (summaryType != EReportSummaryCellType.SUM) {
                        stmts.add(createSetEnumValue("setSummaryType", cellVarRef, summaryType));
                    }
                    if (summaryCell.getPropertyId() != null) {
                        stmts.add(createSetId("setPropertyId", cellVarRef, summaryCell.getPropertyId()));
                    }

                    final int groupCount = summaryCell.getGroupCount();
                    if (groupCount > 0) {
                        stmts.add(createSetInt("setGroupCount", cellVarRef, groupCount));
                    }
                    break;
                case CHART:
                    final AdsReportChartCell chartCell = (AdsReportChartCell) cell;
                    final EReportChartCellType chartType = chartCell.getChartType();
                    if (chartType != EReportChartCellType.CATEGORY) {
                        stmts.add(createSetEnumValue("setChartType", cellVarRef, chartType));
                    }
                    final EReportChartLegendPosition legendPosition = chartCell.getLegendPosition();
                    if (legendPosition != EReportChartLegendPosition.BOTTOM) {
                        stmts.add(createSetEnumValue("setLegendPosition", cellVarRef, legendPosition));
                    }
                    if (chartCell.getTitleId() != null) {
                        stmts.add(createSetId("setTitleId", cellVarRef, chartCell.getTitleId()));
                    }
                    stmts.add(createSetBoolean("setXAxisGridVisible", cellVarRef, chartCell.isXAxisGridVisible()));
                    stmts.add(createSetBoolean("setYAxisGridVisible", cellVarRef, chartCell.isYAxisGridVisible()));
                    stmts.add(createSetBoolean("setIsHorizontalOrientation", cellVarRef, chartCell.isHorizontalOrientation()));

                    stmts.add(createSetBoolean("setAutoAxisSpace", cellVarRef, chartCell.isAutoAxisSpace()));

                    stmts.add(createSetInt("setLeftAxisSpacePx", cellVarRef, chartCell.getLeftAxisSpacePx()));
                    stmts.add(createSetInt("setBottomAxisSpacePx", cellVarRef, chartCell.getBottomAxisSpacePx()));
                    stmts.add(createSetInt("setTopAxisSpacePx", cellVarRef, chartCell.getTopAxisSpacePx()));
                    stmts.add(createSetInt("setRightAxisSpacePx", cellVarRef, chartCell.getRightAxisSpacePx()));

                    if (chartCell.getChartSeries() != null && !createChartSeries(chartCell.getChartSeries(), cellVarRef, stmts)) {
                        return false;
                    }
                    if (chartCell.getDomainAxes() != null && !createChartAxis(chartCell.getDomainAxes(), cellVarRef, stmts)) {
                        return false;
                    }
                    if (chartCell.getRangeAxes() != null && !createChartAxis(chartCell.getRangeAxes(), cellVarRef, stmts)) {
                        return false;
                    }

                    stmts.add(createSetBoolean("setTitleFontInherited", cellVarRef, chartCell.isTitleFontInherited()));
                    stmts.add(createSetBoolean("setAxesFontInherited", cellVarRef, chartCell.isAxesFontInherited()));
                    stmts.add(createSetBoolean("setLegendFontInherited", cellVarRef, chartCell.isLegendFontInherited()));


                    if (!chartCell.isTitleFontInherited()) {
                        createFontAppearance(cellVarRef, "getTitleFont", chartCell.getTitleFont(), stmts);
                    }
                    if (!chartCell.isAxesFontInherited()) {
                        createFontAppearance(cellVarRef, "getAxesFont", chartCell.getAxesFont(), stmts);
                    }
                    if (!chartCell.isLegendFontInherited()) {
                        createFontAppearance(cellVarRef, "getLegendFont", chartCell.getLegendFont(), stmts);
                    }
                    stmts.add(createSetDouble("setForegroundAlpha", cellVarRef, chartCell.getForegroundAlpha()));
                    break;
            }

            if (cell instanceof AdsReportFormattedCell) {
                final AdsReportFormattedCell formatterCell = (AdsReportFormattedCell) cell;
                MessageSend getFormat = createMessageSend(cellVarRef, "getFormat".toCharArray());
                stmts.add(createSetBoolean("setUseDefaultFormat", getFormat, formatterCell.getUseDefaultFormat()));

                if (!formatterCell.getUseDefaultFormat()) {
                    if (formatterCell.getDateStyle() != null) {
                        stmts.add(createSetEnumValue("setDateStyle", getFormat, formatterCell.getDateStyle()));
                    }
                    if (formatterCell.getTimeStyle() != null) {
                        stmts.add(createSetEnumValue("setTimeStyle", getFormat, formatterCell.getTimeStyle()));
                    }
                    if (formatterCell.getPattern() != null) {
                        stmts.add(createSetString("setPattern", getFormat, formatterCell.getPattern()));
                    }
                    stmts.add(createSetInt("setPrecission", getFormat, formatterCell.getPrecission()));
                    stmts.add(createSetString("setDesimalDelimeter", getFormat, formatterCell.getDesimalDelimeter()));
                    stmts.add(createSetEnumValue("setTriadDelimeterType", getFormat, formatterCell.getTriadDelimeterType()));

                    if (formatterCell.getTriadDelimeterType() == ETriadDelimeterType.SPECIFIED) {
                        stmts.add(createSetString("setTriadDelimeter", getFormat, formatterCell.getTriadDelimeter()));
                    }
                }
            }
            stmts.add(createSetBoolean("setAdjustHeight", cellVarRef, cell.isAdjustHeight()));
            stmts.add(createSetBoolean("setAdjustWidth", cellVarRef, cell.isAdjustWidth()));

            createCellAppearance(cell, cellVarRef, stmts);
        }
        return true;
    }

    private void createCellAppearance(AdsReportCell cell, SingleNameReference cellVarRef, List<Statement> stmts) {
        stmts.add(createSetEnumValue("setHAlign", cellVarRef, cell.getHAlign()));
        stmts.add(createSetEnumValue("setVAlign", cellVarRef, cell.getVAlign()));

        if (!cell.isFontInherited()) {
            stmts.add(createSetBoolean("setFontInherited", cellVarRef, false));
        }

        if (cell.getMarginTopMm() != AdsReportCell.DEFAULT_MARGIN) {
            stmts.add(createSetDouble("setMarginTopMm", cellVarRef, cell.getMarginTopMm()));
        }
        if (cell.getMarginBottomMm() != AdsReportCell.DEFAULT_MARGIN) {
            stmts.add(createSetDouble("setMarginBottomMm", cellVarRef, cell.getMarginBottomMm()));
        }
        if (cell.getMarginLeftMm() != AdsReportCell.DEFAULT_MARGIN) {
            stmts.add(createSetDouble("setMarginLeftMm", cellVarRef, cell.getMarginLeftMm()));
        }
        if (cell.getMarginRightMm() != AdsReportCell.DEFAULT_MARGIN) {
            stmts.add(createSetDouble("setMarginRightMm", cellVarRef, cell.getMarginRightMm()));
        }

        if (cell.getLineSpacingMm() != AdsReportCell.DEFAULT_LINE_SPACING) {
            stmts.add(createSetDouble("setLineSpacingMm", cellVarRef, cell.getLineSpacingMm()));
        }

        if (cell.isWrapWord()) {
            stmts.add(createSetBoolean("setWrapWord", cellVarRef, true));
        }
        if (cell.isSnapTopEdge()) {
            stmts.add(createSetBoolean("setSnapTopEdge", cellVarRef, true));

        }
        if (cell.isSnapBottomEdge()) {
            stmts.add(createSetBoolean("setSnapBottomEdge", cellVarRef, true));
        }

        if (cell.isClipContent()) {
            stmts.add(createSetBoolean("setClipContent", cellVarRef, true));
        }

        createAppearance(cell, cellVarRef, stmts);
    }

    private boolean createChartSeries(AdsReportChartCell.ChartSeries chartSeries, SingleNameReference cellVarRef, List<Statement> stmts) {
        for (AdsReportChartSeries series : chartSeries) {
            final String seriesName = series.getName();
            final char[] seriesVarName = (String.valueOf(cellVarRef.token) + "_" + toLower(seriesName)).toCharArray();

            final EReportChartSeriesType seriesType = series.getSeriesType();
            LocalDeclaration seriesVar = new LocalDeclaration(seriesVarName, 0, 0);
            seriesVar.type = createQualifiedType(getNameOfTypeFromReportsPackage(series.getClass().getSimpleName()));
            AllocationExpression alloc = new AllocationExpression();
            alloc.type = createQualifiedType(ADSREPORTCHARTSERIES_TYPE_NAME);
            alloc.arguments = new Expression[]{
                cellVarRef,
                createEnumFieldRef(seriesType),
                new StringLiteral(series.getName().toCharArray(), 0, 0, 0)
            };
            seriesVar.initialization = alloc;
            stmts.add(seriesVar);

            SingleNameReference seriesVarRef = new ReenterableSingleNameReference(seriesVarName);
            createChartDataSetInfoCell(series.getSeriesData(), seriesVarRef, "getSeriesData", stmts);
            createChartDataSetInfoCell(series.getDomainData(), seriesVarRef, "getDomainData", stmts);
            createChartDataSetInfoCell(series.getRangeData(), seriesVarRef, "getRangeData", stmts);

            stmts.add(createSetBoolean("setIsShowItemLabel", seriesVarRef, series.isShowItemLabel()));
            stmts.add(createSetInt("setGroupIndex", seriesVarRef, series.getGroupIndex()));


            if (series.getTitleId() != null) {
                stmts.add(createSetId("setTitleId", seriesVarRef, series.getTitleId()));
            }
            stmts.add(createMessageSend(createMessageSend(cellVarRef, "getChartSeries"), "add", seriesVarRef));
        }
        return true;
    }

    private boolean createChartAxis(AdsReportChartCell.AdsReportChartAxes chartAxes, SingleNameReference cellVarRef, List<Statement> stmts) {
        for (AdsReportChartAxis axis : chartAxes) {
            final String axisName = axis.getName();
            final char[] axisVarName = (String.valueOf(cellVarRef.token) + "_" + toLower(axisName)).toCharArray();
            //final String seriesClassSimpleName = axis.getClass().getSimpleName();
            final EReportChartAxisType axisType = axis.getAxisType();
            final String strSeriesType = axisType.getClass().getSimpleName() + "." + axisType.name();

            LocalDeclaration axisVar = new LocalDeclaration(axisVarName, 0, 0);
            AllocationExpression alloc = new AllocationExpression();
            alloc.type = createQualifiedType(ADSREPORTCHARTAXIS_TYPE_NAME);
            alloc.arguments = new Expression[]{
                cellVarRef,
                new StringLiteral(axis.getName().toCharArray(), 0, 0, 0),
                createEnumFieldRef(axisType)
            };
            axisVar.initialization = alloc;
            axisVar.type = createQualifiedType(getNameOfTypeFromReportsPackage(axis.getClass().getSimpleName()));
            stmts.add(axisVar);

            SingleNameReference axisVarRef = new ReenterableSingleNameReference(axisVarName);
            stmts.add(createSetBoolean("setIsAutoRangeIncludeZero", axisVarRef, axis.isAutoRangeIncludeZero()));
            stmts.add(createSetBoolean("setIsRightOrTop", axisVarRef, axis.isRightOrTop()));
            stmts.add(createSetBoolean("setIsDateAxis", axisVarRef, axis.isDateAxis()));
            stmts.add(createSetBoolean("setIsVisible", axisVarRef, axis.isVisible()));


            final EReportChartAxisLabelPositions labelPosition = axis.getAxisLabelPosition();
            if (labelPosition != EReportChartAxisLabelPositions.STANDARD) {
                stmts.add(createSetEnumValue("setAxisLabelPosition", axisVarRef, labelPosition));
            }

            if (axis.getTitleId() != null) {
                stmts.add(createSetId("setTitleId", axisVarRef, axis.getTitleId()));
                return false;
            }

            stmts.add(createMessageSend(createMessageSend(cellVarRef, axisType == EReportChartAxisType.DOMAIN_AXIS ? "getDomainAxes" : "getRangeAxes"), "add", axisVarRef));
        }
        return true;
    }

    private void createChartDataSetInfoCell(AdsChartDataInfo dataSetInfo, SingleNameReference seriesVarRef, String methodName, List<Statement> stmts) {
        if (dataSetInfo.getPropId() != null) {
            stmts.add(createMessageSend(createMessageSend(seriesVarRef, methodName), "setPropId", createIdInvocation(dataSetInfo.getPropId())));
        }
        stmts.add(createSetInt("setAxisIndex", createMessageSend(seriesVarRef, methodName), dataSetInfo.getAxisIndex()));
    }

    private TypeDeclaration createBandClass(AdsReportClassDef report, AdsReportBand band, CompilationUnitScope scope) {
        final Jml onAdding = band.getOnAdding();
        if (!onAdding.getItems().isEmpty()) {
            TypeDeclaration bandClass = new TypeDeclaration(compilationResult);
            final String classSimpleName = getBandJavaName(band);
            bandClass.name = classSimpleName.toCharArray();
            bandClass.superclass = createQualifiedType(getNameOfTypeFromReportsPackage(band.getClass().getSimpleName()));
            bandClass.modifiers = ClassFileConstants.AccPrivate | ClassFileConstants.AccFinal;
            bandClass.methods = new AbstractMethodDeclaration[]{
                createOnAdding(report, band, scope)};
            bandClass.createDefaultConstructor(true, true);
            return bandClass;
        }
        return null;
    }

    private MethodDeclaration createOnAdding(AdsReportClassDef report, AdsReportBand band, CompilationUnitScope scope) {
        Jml onAdding = band.getOnAdding();
        MethodDeclaration method = createMethod("onAdding", true, null, ClassFileConstants.AccPublic);
        List<Statement> stmts = new LinkedList<>();

        Statement[] userStatements = JmlHelper.parseStatements(scope, report, referenceContext, (CompilationUnitDeclaration) scope.referenceContext(), onAdding);
        wrapWithProfilerCode(band, userStatements, stmts);

        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    private MethodDeclaration createOnAdding(AdsReportClassDef report, AdsReportCell cell, CompilationUnitScope scope) {
        Jml onAdding = cell.getOnAdding();
        MethodDeclaration method = createMethod("onAdding", true, null, ClassFileConstants.AccPublic);
        List<Statement> stmts = new LinkedList<>();

        Statement[] userStatements = JmlHelper.parseStatements(scope, report, referenceContext, (CompilationUnitDeclaration) scope.referenceContext(), onAdding);
        wrapWithProfilerCode(cell, userStatements, stmts);

        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

    private void createCellClasses(AdsReportClassDef report, IReportWidgetContainer cellContainer, CompilationUnitScope scope, List<TypeDeclaration> types, List<AbstractMethodDeclaration> methods) {
        for (AdsReportWidget w : cellContainer.getWidgets()) {
            if (w instanceof AdsReportCell) {
                createCellClass(report, (AdsReportCell) w, scope, types, methods);
            } else if (w instanceof AdsReportWidgetContainer) {
                createCellClasses(report, (AdsReportWidgetContainer) w, scope, types, methods);
            }
        }
    }

    private void createCellClass(AdsReportClassDef report, AdsReportCell cell, CompilationUnitScope scope, List<TypeDeclaration> types, List<AbstractMethodDeclaration> methods) {
        final String classSimpleName = getCellJavaName(cell);
        final boolean isExpression = cell.getCellType() == EReportCellType.EXPRESSION;
        final boolean isOnAddind = !cell.getOnAdding().getItems().isEmpty();
        final String getExpressionMethodName = "get" + classSimpleName + "Expression";

        if (isExpression) {
            final AdsReportExpressionCell expressionCell = (AdsReportExpressionCell) cell;
            methods.add(createGetExpressionMethod(report, expressionCell, getExpressionMethodName, scope));
        }

        if (isOnAddind || isExpression) {
            TypeDeclaration cellClass = new TypeDeclaration(compilationResult);
            cellClass.methods = new AbstractMethodDeclaration[isOnAddind && isExpression ? 2 : 1];
            cellClass.name = classSimpleName.toCharArray();
            cellClass.superclass = createQualifiedType(getNameOfTypeFromReportsPackage(cell.getClass().getSimpleName()));
            cellClass.modifiers = ClassFileConstants.AccPrivate | ClassFileConstants.AccFinal;

            int index = 0;


            if (isOnAddind) {
                cellClass.methods[index++] = createOnAdding(report, cell, scope);
            }
            if (isExpression) {
                cellClass.methods[index] = createCalcExpressionMethod(getExpressionMethodName);
            }
            cellClass.createDefaultConstructor(true, true);
            types.add(cellClass);
        }
    }

    private MethodDeclaration createCalcExpressionMethod(String getExpressionMethodName) {
        MethodDeclaration method = createMethod("calcExpression", true, createQualifiedType(JAVALANGSTRING_TYPENAME), ClassFileConstants.AccPublic);
        method.statements = new Statement[]{
            new ReturnStatement(createMessageSend(ThisReference.implicitThis(), getExpressionMethodName), 0, 0)
        };

        return method;
    }

    private MethodDeclaration createGetExpressionMethod(AdsReportClassDef report, AdsReportExpressionCell expressionCell, String getExpressionMethodName, CompilationUnitScope scope) {
        MethodDeclaration method = createMethod(getExpressionMethodName, false, createQualifiedType(JAVALANGSTRING_TYPENAME), ClassFileConstants.AccPrivate);

        List<Statement> stmts = new LinkedList<>();
        final Jml expression = expressionCell.getExpression();
        Statement[] userStatements = JmlHelper.parseStatements(scope, report, referenceContext, (CompilationUnitDeclaration) scope.referenceContext(), expression);
        wrapWithProfilerCode(expressionCell, userStatements, stmts);

        method.statements = stmts.toArray(new Statement[stmts.size()]);
        return method;
    }

   
}
