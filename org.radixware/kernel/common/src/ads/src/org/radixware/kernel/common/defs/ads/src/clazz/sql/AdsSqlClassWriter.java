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

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EComparisonOperator;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;

public class AdsSqlClassWriter<T extends AdsSqlClassDef> extends AdsClassWriter<T> {

    private final T sqlClass;
    public static final String TEST_METHOD_NAME = "mth_stmt_prepare_test_method_synthetic";

    public AdsSqlClassWriter(JavaSourceSupport support, T target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
        this.sqlClass = target;
    }

    public T getSqlClass() {
        return sqlClass;
    }

    @Override
    protected void writeCustomHeader(CodePrinter cp) {
        super.writeCustomHeader(cp);
        cp.println("import org.radixware.kernel.common.enums.*;");
    }

    private void printPreparedStatementCacheInstantiation(CodePrinter cp) {
        cp.print("private static final PreparedStatementsCache $preparedStatementsCache = new PreparedStatementsCache(");
        cp.print("EClassType." + sqlClass.getClassDefType().name() + ", ");
        cp.print(sqlClass.getCacheSize());

        final EClassType classType = sqlClass.getClassDefType();

        boolean uniDirectCursor = (sqlClass instanceof AdsCursorClassDef) ? ((AdsCursorClassDef) sqlClass).isUniDirect()
                : (sqlClass instanceof AdsReportClassDef ? ((AdsReportClassDef) sqlClass).walkTwiceOnExport() ? false : true : true);
        cp.print(", ");
        cp.print(uniDirectCursor);

        boolean readOnlyCursor = classType == EClassType.REPORT || sqlClass.isDbReadOnly();
        cp.print(", ");
        cp.print(readOnlyCursor);

        cp.println(");");

        if (classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) {
            cp.println("@Override");
            cp.println("protected PreparedStatementsCache getPreparedStatementsCache() {");
            cp.println("\treturn $preparedStatementsCache;");
            cp.println("}");
        }
    }

//    private void introduceProcedureOutParams(CodePrinter cp) {
//        final Set<AdsParameterPropertyDef> outputParams = new HashSet<AdsParameterPropertyDef>();
//
//        for (Sqml.Item item : sqlClass.getSource().getItems()) {
//            if (item instanceof ParameterTag) {
//                final ParameterTag paramTag = (ParameterTag) item;
//                if (paramTag.getDirection() != EParamDirection.IN) {
//                    final IParameterDef iParam = paramTag.findParameter();
//                    final AdsParameterPropertyDef parameterProperty = (iParam != null ? (AdsParameterPropertyDef) iParam.getDefinition() : null);
//                    if (parameterProperty != null) {
//                        if (outputParams.add(parameterProperty)) {
//                            cp.print("private ");
//                            final AdsTypeDeclaration type = parameterProperty.getValue().getType();
//                            writeUsage(cp, type, parameterProperty);
//                            cp.println(" " + parameterProperty.getId() + "_Val=null;");
//                        }
//                    } else {
//                        cp.printError();
//                    }
//                }
//            }
//        }
//    }
    @Override
    protected void writeCustomBody(CodePrinter cp) {
        super.writeCustomBody(cp);

        //final EClassType classType = sqlClass.getClassDefType();
        printPreparedStatementCacheInstantiation(cp);

        if (sqlClass.getClassDefType() == EClassType.SQL_STATEMENT) {
            printStaticCacheRegistration(cp);
        }
        printTestPreparationMethod(cp);

//        if (classType == EClassType.SQL_PROCEDURE) {
//            introduceProcedureOutParams(cp);
//        }
    }

    private void printTestPreparationMethod(CodePrinter printer) {

        if (sqlClass.getClassDefType() == EClassType.REPORT) {
            printer.println("@SuppressWarnings(\"unused\")");
            printer.print("private void ");
            printer.print(TEST_METHOD_NAME);
            printer.print("(");
            boolean paramPrinted = false;
            for (AdsParameterPropertyDef param : sqlClass.getInputParameters()) {
                if (paramPrinted) {
                    printer.print(", ");
                }
                final AdsTypeDeclaration type = param.getValue().getType();
                writeUsage(printer, type, param);
                printer.print(" ");
                printer.print(param.getName());
                paramPrinted = true;
            }
            printer.print(")");
        } else {
            AdsMethodDef method = sqlClass.getMethods().findById(AdsSystemMethodDef.ID_STMT_EXECUTE, ExtendableDefinitions.EScope.LOCAL).get();
            if (method != null) {
                printer.println("@SuppressWarnings(\"unused\")");
                AdsMethodWriter.printProfileErasure(this, printer, method, sqlClass, true, true, TEST_METHOD_NAME, "private");
            } else {
                return;
            }
        }
        printer.println('{');
        AdsSqlClassExecuteWriter.writeBody(sqlClass, printer, true);
        printer.println();
        printer.println('}');
    }

    private void printStaticCacheRegistration(final CodePrinter cp) {
        cp.println();
        cp.println("static {");
        cp.enterBlock();
        cp.print(WriterUtils.GET_ARTE_INVOCATON);
        cp.println(".registerStatementsCache($preparedStatementsCache);");
        cp.leaveBlock();
        cp.println("}");
    }

    public static void printTargetDbPreprocessorTag(final CodePrinter cp, final TargetDbPreprocessorTag tag) {
        cp.print("if (");
        cp.print("checkPreprocessorCondition(");
        cp.print("\"");
        cp.print(tag.getDbTypeName());
        cp.print("\", ");
        cp.print(Boolean.toString(tag.isCheckVersion()));
        cp.print(", ");
        cp.print(EComparisonOperator.class.getName() + "." + tag.getVersionOperator().name());
        cp.print(", \"");
        cp.print(tag.getDbVersion());
        cp.print("\", ");
        cp.print(Boolean.toString(tag.isCheckOptions()));
        cp.print(", java.util.Arrays.asList(");
        if (tag.isCheckOptions()) {
            if (tag.getDbOptions() != null) {
                boolean first = true;
                for (DbOptionValue dbOptVal : tag.getDbOptions()) {
                    if (first) {
                        first = false;
                    } else {
                        cp.print(", ");
                    }
                    cp.print("new org.radixware.kernel.common.repository.DbOptionValue(\"");
                    cp.print(dbOptVal.getOptionName().replaceAll("\\\\", "\\\\\\\\"));
                    cp.print("\", ");
                    cp.print("org.radixware.kernel.common.enums.EOptionMode.");
                    cp.print(dbOptVal.getMode().name());
                    cp.print(")");
                }
            }
        }
        cp.print("), \"");
        cp.print(tag.getOwnerScml().getLayer().getURI());
        cp.print("\", ");
        cp.print(WriterUtils.GET_ARTE_INVOCATON);
        cp.print(")");
        cp.print(") {");
        cp.enterBlock();
    }

    public static AdsSqlClassSqlProcessor.QueryInfo[] getQueryVariants(AdsSqlClassDef clazz, int maxCount) {
        return AdsSqlClassSqlProcessor.getVariants(clazz.getSource(), maxCount).toArray(new AdsSqlClassSqlProcessor.QueryInfo[0]);
    }
}
