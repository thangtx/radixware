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

import java.util.ArrayList;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;


class AdsEntityObjectPresentationsWriter<T extends EntityObjectPresentations> extends AdsEntityBasedPresentationsWriter<T> {

    private static final char[] EXPLORER_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadClassPresentationDef".toCharArray(), '.');

    AdsEntityObjectPresentationsWriter(JavaSourceSupport support, T presentations, UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }

    @Override
    protected boolean writeExecutable(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            default:
                return false;
        }
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                /*RadClassPresentationDef(
                 final Id id,
                 final String name,
                 final Id baseClassId,
                 final Id iconId,
                 final Id defaultSelectorPresentationId,
                 final Id classTitleId,
                 final Id grpTitleId,
                 final Id objTitleId,
                 final long restrictionsMask,
                 final RadPropertyDef[] properties,
                 final RadCommandDef[] commands,
                 final RadFilterDef[] filters,
                 final RadSortingDef[] sortings,
                 final RadColorSchemeDef[] colorSchemes,
                 final Id[] presentationIds)*/
                printer.print("new ");
                printer.print(EXPLORER_META_CLASS_NAME);
                printer.print('(');
                printer.enterBlock(2);
                AdsEntityObjectClassDef clazz = ((EntityObjectPresentations) def).getOwnerClass();
                WriterUtils.writeIdUsage(printer, clazz.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(clazz.getQualifiedName());
                printer.printComma();
                printer.println();
                if (clazz.getClassDefType() == EClassType.APPLICATION) {
                    AdsTypeDeclaration decl = clazz.getInheritance().getSuperClassRef();
                    if (decl != null && decl.getTypeId() == EValType.USER_CLASS) {
                        WriterUtils.writeIdUsage(printer, decl.getPath().getTargetId());
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                printer.println();
                EntityPresentations epr = def instanceof EntityPresentations ? (EntityPresentations) def : null;
                if (epr != null) {
                    WriterUtils.writeIdUsage(printer, epr.getIconId());
                    printer.printComma();
                    printer.println();

                    WriterUtils.writeIdUsage(printer, epr.getDefaultSelectorPresentationId());
                } else {
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeNull(printer);
                }

                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, clazz.getTitleId());
                if (epr != null) {
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, epr.getEntityTitleId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, epr.getObjectTitleId());
                } else {
                    printer.printComma();
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                if (epr != null) {
                    writeCode(printer, epr.getRestrictions());
                } else {
                    printer.print(0);
                }
                printer.printComma();
                printer.println();
                writeCode(printer, clazz.getProperties());
                printer.printComma();
                printer.println();
                writeCommandsMeta(printer);
                printer.printComma();
                printer.println();
                writeFiltersMeta(printer);
                printer.printComma();
                printer.println();
                writeSortingsMeta(printer);
                printer.printComma();
                printer.println();
//                writeColorSchemesMeta(printer);
//                printer.printComma();
//                printer.println();
                writeExplorerRefList(printer);
                printer.printComma();
                printer.println();
                ArrayList<Id> presentationIds = new ArrayList<Id>();
                for (AdsEditorPresentationDef e : def.getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE)) {
                    if (e.getClientEnvironment() == usagePurpose.getEnvironment() || e.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        presentationIds.add(e.getId());
                    }
                }
                for (AdsSelectorPresentationDef s : def.getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE)) {
                    if (s.getClientEnvironment() == usagePurpose.getEnvironment() || s.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        presentationIds.add(s.getId());
                    }
                }
                WriterUtils.writeIdArrayUsage(printer, presentationIds);
                printer.printComma();//added by yremizov
                printer.println();//added by yremizov
                boolean polymorph = false;
                boolean audit = false;
                DdsTableDef t = clazz.findTable(def);
                if (t != null) {
                    polymorph = t.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES);
                    audit = t.getAuditInfo().isEnabled();
                }
                printer.print(polymorph);//added by yremizov
                printer.printComma();
                printer.print(audit);

                boolean isUserFunc = false;
                Id tableId = def.getOwnerClass().getEntityId();
                if (tableId == AdsEntityObjectClassDef.USER_FUNC_TABLE_ID) {
                    isUserFunc = true;
                }

                printer.printComma();
                printer.print(isUserFunc);

                printer.leaveBlock(2);

                printer.print(')');
                printer.printlnSemicolon();
                return true;
            default:
                return super.writeMeta(printer);
        }
    }

    @Override
    protected void writeSortingsMeta(CodePrinter printer) {
        ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
            case WEB:
            case EXPLORER:
                WriterUtils.SameObjectArrayWriter<AdsSortingDef> writer = new WriterUtils.SameObjectArrayWriter<AdsSortingDef>(env == ERuntimeEnvironmentType.SERVER ? AdsSortingWriter.SORTING_META_SERVER_CLASS_NAME : AdsSortingWriter.SORTING_META_EXPLORER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsSortingDef sorting) {
                        writeCode(printer, sorting);
                    }
                };
                writer.write(printer, def.getSortings().get(EScope.LOCAL_AND_OVERWRITE));
                break;
        }
    }

    @Override
    protected void writeFiltersMeta(CodePrinter printer) {
        ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
            case WEB:
            case EXPLORER:
                WriterUtils.SameObjectArrayWriter<AdsFilterDef> writer = new WriterUtils.SameObjectArrayWriter<AdsFilterDef>(env == ERuntimeEnvironmentType.SERVER ? AdsFilterWriter.FILTER_META_SERVER_CLASS_NAME : AdsFilterWriter.FILTER_META_EXPLORER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsFilterDef filter) {
                        writeCode(printer, filter);
                    }
                };
                writer.write(printer, def.getFilters().get(EScope.LOCAL_AND_OVERWRITE));
                break;
        }
    }

//    @Override
//    protected void writeColorSchemesMeta(CodePrinter printer) {
//        ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
//        switch (env) {
//            case SERVER:
//            case WEB:
//            case EXPLORER:
//                WriterUtils.SameObjectArrayWriter<AdsColorSchemeDef> writer = new WriterUtils.SameObjectArrayWriter<AdsColorSchemeDef>(env == ERuntimeEnvironmentType.SERVER ? AdsColorSchemeWriter.COLOR_SCHEME_SERVER_META_CLASS_NAME : AdsColorSchemeWriter.COLOR_SCHEME_EXPLORER_META_CLASS_NAME) {
//                    @Override
//                    public void writeItemConstructorParams(CodePrinter printer, AdsColorSchemeDef colorScheme) {
//                        writeCode(printer, colorScheme);
//                    }
//                };
//                writer.write(printer, def.getColorSchemes().get(EScope.LOCAL_AND_OVERWRITE));
//                break;
//        }
//    }
    protected void writeExplorerRefList(CodePrinter printer) {
        WriterUtils.writeNull(printer);
    }

    @Override
    protected void writeEditorPresentationsMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.SameObjectArrayWriter<AdsEditorPresentationDef> writer = new WriterUtils.SameObjectArrayWriter<AdsEditorPresentationDef>(AdsEditorPresentationWriter.EDITOR_PRESENTATION_META_SERVER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsEditorPresentationDef editorPresentation) {
                        writeCode(printer, editorPresentation);
                    }
                };
                writer.write(printer, def.getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE));
                break;
        }
    }

    @Override
    protected void writeSelectorPresentationsMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.SameObjectArrayWriter<AdsSelectorPresentationDef> writer = new WriterUtils.SameObjectArrayWriter<AdsSelectorPresentationDef>(AdsSelectorPresentationWriter.SELECTOR_PRESENTATION_META_SERVER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsSelectorPresentationDef selectorPresentation) {
                        writeCode(printer, selectorPresentation);
                    }
                };
                writer.write(printer, def.getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE));
                break;
        }
    }

    @Override
    protected void writeClassCatalogsMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.SameObjectArrayWriter<AdsClassCatalogDef> writer = new WriterUtils.SameObjectArrayWriter<AdsClassCatalogDef>(AdsClassCatalogWriter.CLASS_CATALOG_META_SERVER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsClassCatalogDef classCatalog) {
                        writeCode(printer, classCatalog);
                    }
                };
                writer.write(printer, def.getClassCatalogs().get(EScope.LOCAL_AND_OVERWRITE));
                break;
        }
    }

    @Override
    protected void writePresentationMapDefMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                //public RadPresentationMapDef(final Map<MapKey, Id> map)
                //public MapKey(final Id classId, final Id origPresId)
                ArrayList<Id> keys = new ArrayList<Id>();
                ArrayList<Id> values = new ArrayList<Id>();
                for (AdsEditorPresentationDef p : def.getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE)) {
                    if (p.getReplacedEditorPresentationId() != null) {
                        keys.add(p.getReplacedEditorPresentationId());
                        values.add(p.getId());
                    }
                }
                WriterUtils.writeIdMapFromArraysCreation(printer, keys, values);
                break;
        }
    }

    @Override
    public char[] getExplorerMetaClassName() {
        return EXPLORER_META_CLASS_NAME;
    }
}
