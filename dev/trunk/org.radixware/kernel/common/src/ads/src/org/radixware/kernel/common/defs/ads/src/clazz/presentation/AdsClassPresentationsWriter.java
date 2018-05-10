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
package org.radixware.kernel.common.defs.ads.src.clazz.presentation;

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AlgoClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityGroupPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.FormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ReportPresentations;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.command.AdsCommandWriter;
import org.radixware.kernel.common.enums.EClassType;
import static org.radixware.kernel.common.enums.EClassType.REPORT;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsClassPresentationsWriter<T extends ClassPresentations> extends RadixObjectWriter<T> {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static <T extends ClassPresentations> AdsClassPresentationsWriter newInstance(final T p, final JavaSourceSupport support, final UsagePurpose usagePurpose) {
            final AdsClassDef clazz = p.getOwnerClass();
            switch (clazz.getClassDefType()) {
                case ENTITY:
                    return new AdsEntityPresentationsWriter(support, (EntityPresentations) p, usagePurpose);
                case APPLICATION:
                    return new AdsEntityObjectPresentationsWriter<EntityObjectPresentations>(support, (EntityObjectPresentations) p, usagePurpose);
                case ENTITY_GROUP:
                    return new AdsEntityGroupPresentationsWriter(support, (EntityGroupPresentations) p, usagePurpose);
                case FORM_HANDLER:
                    return new AdsFormPresentationWriter(support, (FormPresentations) p, usagePurpose);
                case REPORT:
                    return new AdsReportPresentationWriter(support, (ReportPresentations) p, usagePurpose);
                case ALGORITHM:
                    return new AdsAlgoPresentationsWriter(support, (AlgoClassPresentations) p, usagePurpose);
                default:
                    throw new DefinitionError("Unexpected owner class type" + clazz.getClassDefType().getName());
            }
        }
    }
    protected final AdsClassDef ownerClass;

    protected AdsClassPresentationsWriter(final JavaSourceSupport support, final T presentations, final UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
        this.ownerClass = presentations.getOwnerClass();
    }
    public static final char[] CLASS_PRESENTATIONS_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadClassPresentationDef".toCharArray(), '.');
    public static final char[] CLASS_PRESENTATIONS_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadClassPresentationDef".toCharArray(), '.');

    @Override
    protected boolean writeMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                /**
                 * public RadClassPresentationDef( final Id classId, final
                 * String name, final Id titleId, final
                 * RadPropertyPresentationDef[] propPresentations, final
                 * RadCommandDef[] commands, final RadSortingDef[] sortings,
                 * final RadFilterDef[] filters, final RadColorSchemeDef[]
                 * colorSchemes, final RadEditorPresentationDef[]
                 * editorPresentations, final RadSelectorPresentationDef[]
                 * selectorPresentations, final Id
                 * defaultSelectorPresentationId, final RadPresentationMapDef
                 * classPresentationMap, final RadEntityTitleFormatDef
                 * defaultObjectTitleFormat, final
                 * RadClassPresentationDef.ClassCatalog[] classCatalogs, final
                 * Restrictions restrictions)
                 */
                printer.println("/*Class presentation attributes*/");
                printer.print(" new ");
                printer.print(CLASS_PRESENTATIONS_SERVER_CLASS_NAME);
                printer.print('(');
                printer.enterBlock(1);
                printer.println("/*Owner Class Id*/");
                WriterUtils.writeIdUsage(printer, ownerClass.getId());
                printer.printComma();
                printer.println();
                printer.println("/*Owner Class Name*/");
                printer.printStringLiteral(ownerClass.getName());
                printer.printComma();
                printer.println();
                printer.println("/*Title Id*/");
                if (!ownerClass.isTitleInherited()) {
                    WriterUtils.writeIdUsage(printer, ownerClass.getTitleId());
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                printer.println();
                printer.println("/*Property presentations*/");
                ownerClass.getProperties().getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_ADDON).writeCode(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Commands*/");
                writeCommandsMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Sortings*/");
                writeSortingsMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Filters*/");
                writeFiltersMeta(printer);
                printer.printComma();
                printer.println();
//                printer.println("/*Color Schemes*/");
//                writeColorSchemesMeta(printer);
//                printer.printComma();
//                printer.println();
                printer.println("/*Editor presentations*/");
                writeEditorPresentationsMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Selector presentations*/");
                writeSelectorPresentationsMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Default selector presentation Id*/");
                writeDefaultSelectorPresentationsId(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Presentation Map*/");
                writePresentationMapDefMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Object title format*/");
                writeObjectTitleFormatMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Class catalogs*/");
                writeClassCatalogsMeta(printer);
                printer.printComma();
                printer.println();
                printer.println("/*Restrictions*/");
                writeRestrictionsMeta(printer);
                printer.leaveBlock(1);
                printer.printComma();
                if (def.getOwnerClass().getClassDefType() == EClassType.REPORT) {//write supported formats

                    final EnumSet<EReportExportFormat> set = EnumSet.of(EReportExportFormat.XML);
                    final AdsReportClassDef report = (AdsReportClassDef) def.getOwnerClass();
                    if (report.getCsvInfo() != null && report.getCsvInfo().isExportToCsvEnabled()) {
                        set.add(EReportExportFormat.CSV);
                    }
                    if (report.getXlsxReportInfo() != null && report.getXlsxReportInfo().isExportToXlsxEnabled()) {
                        set.add(EReportExportFormat.XLSX);
                    }
                    if (report.getForm() != null && !report.getForm().getBands().isEmpty()) {
                        set.add(EReportExportFormat.PDF);
                        set.add(EReportExportFormat.OOXML);
                        if (report.getForm().isSupportsTxt()) {
                            set.add(EReportExportFormat.TXT);
                        }
                    }

                    WriterUtils.writeEnumSet(printer, set.isEmpty() ? null : set, EReportExportFormat.class);
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                printer.print(getBits());
                printer.print(')');
                return true;
            default:
                return false;
        }
    }

    protected int getBits() {
        return 0;
    }

    protected void writeCommandsMeta(final CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
                WriterUtils.SameObjectArrayWriter<AdsScopeCommandDef> writer = new WriterUtils.SameObjectArrayWriter<AdsScopeCommandDef>(AdsCommandWriter.SERVER_COMMAND_META_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(final CodePrinter printer, final AdsScopeCommandDef command) {
                        command.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                    }
                };
                writer.write(printer, def.getCommands().get(EScope.LOCAL_AND_OVERWRITE));
                break;
            case WEB:
            case EXPLORER:
                new WriterUtils.ObjectArrayWriter<AdsScopeCommandDef>(AdsCommandWriter.EXPLORER_COMMAND_META_CLASS_NAME) {
                    @Override
                    public void writeItemConstructor(final CodePrinter printer, final AdsScopeCommandDef item) {
                        printer.print("new ");
                        printer.print(AdsCommandWriter.EXPLORER_ENTITY_COMMAND_META_CLASS_NAME);
                        printer.print('(');
                        item.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                        printer.print(')');
                    }
                }.write(printer, def.getCommands().get(EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsScopeCommandDef>() {
                    @Override
                    public boolean isTarget(AdsScopeCommandDef radixObject) {
                        ERuntimeEnvironmentType commandEnv = radixObject.getPresentation().getClientEnvironment();
                        if (commandEnv != ERuntimeEnvironmentType.COMMON_CLIENT && commandEnv != env) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }));
                break;
        }
    }

//    protected void writeCommandClasses(CodePrinter printer) {
//        printer.println("/*------------------- Command Classes ---------------------*/");
//        for (AdsCommandDef cmd : def.getCommands().get(EScope.LOCAL)) {
//            cmd.getJavaSourceSupport().getCodeWriter(UsagePurpose.EXPLORER_EXECUTABLE).writeCode(printer);
//            printer.println();
//        }
//    }
    protected void writeSortingsMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writeFiltersMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

//    protected void writeColorSchemesMeta(final CodePrinter printer) {
//        switch (usagePurpose.getEnvironment()) {
//            case SERVER:
//                WriterUtils.writeNull(printer);
//                break;
//        }
//    }
    protected void writeEditorPresentationsMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writeSelectorPresentationsMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writeDefaultSelectorPresentationsId(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writeObjectTitleFormatMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writeClassCatalogsMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writeRestrictionsMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    protected void writePresentationMapDefMeta(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeNull(printer);
                break;
        }
    }

    @Override
    public void writeUsage(final CodePrinter printer) {
        //don use directly
    }

    public char[] getExplorerMetaClassName() {
        return new char[0];
    }
}
