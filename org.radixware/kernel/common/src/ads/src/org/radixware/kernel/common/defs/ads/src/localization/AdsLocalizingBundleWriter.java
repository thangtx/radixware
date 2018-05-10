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
package org.radixware.kernel.common.defs.ads.src.localization;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;

/**
 * public RadMlStringBundleDef(Id id, String name, Map<Id, MultilingualString>
 * stringsById) public MultilingualString(final Map<EIsoLanguage, String>
 * content, final ELocalizedStringKind kind, final EEventSeverity eventSeverity,
 * final String eventSource) {
 *
 */
public class AdsLocalizingBundleWriter extends AbstractDefinitionWriter<AdsLocalizingBundleDef> {

    private static final char[] MLB_CLASS_NAME = RadMlStringBundleDef.class.getName().toCharArray();
    private static final char[] MLS_CLASS_NAME = RadMlStringBundleDef.MultilingualString.class.getName().replace("$", ".").toCharArray();
    private static final Comparator LANGUAGE_COMPORATOR = new Comparator<IMultilingualStringDef.StringStorage>() {

        @Override
        public int compare(IMultilingualStringDef.StringStorage o1, IMultilingualStringDef.StringStorage o2) {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 != null && o2 != null) {
                EIsoLanguage lang1 = o1.getLanguage();
                if (lang1 == null) {
                    return o2.getLanguage() == null ? 0 : -1;
                }
                return lang1.compareTo(o2.getLanguage());
            } else {
                return o1 == null ? -1 : 1;
            }
        }

    };

    public AdsLocalizingBundleWriter(final JavaSourceSupport support, final AdsLocalizingBundleDef target, final UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    public boolean writeCode(final CodePrinter printer) {

        final Definition owner = ((AdsLocalizingBundleDef) def).findBundleOwner();
        if (owner instanceof AdsUserReportClassDef) {
            WriterUtils.writeUserDefinitionHeader(printer, def);
        }
        if (owner == null) {
            return false;
        }
        final Layer layer = owner.getLayer();
        if (layer == null) {
            return false;
        }
        final Set<EIsoLanguage> langs = layer.getLanguages() == null || layer.getLanguages().isEmpty() ? Collections.<EIsoLanguage>emptySet() : EnumSet.copyOf(layer.getLanguages());

        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        WriterUtils.writeMetaShareabilityAnnotation(printer, def);
        printer.println();

        printer.print("public final class ");
        //writeUsage(printer);
        printer.print(JavaSourceSupport.getMetaName(def, def.getRuntimeId(), printer instanceof IHumanReadablePrinter));
        printer.println('{');
        printer.enterBlock();
        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private static final ");
        writeItemMapDeclaration(printer);
        printer.print(" $$$items$$$ = new ");
        writeItemMapDeclaration(printer);
        printer.println("();");

        final CodePrinter postPrinter = CodePrinter.Factory.newJavaPrinter(printer);

        printer.println("static{");
        printer.enterBlock();

        final List<IMultilingualStringDef> strings = def.getUsedStrings(EScope.LOCAL_AND_OVERWRITE);
        Collections.sort(strings, new Comparator<IMultilingualStringDef>() {
            @Override
            public int compare(IMultilingualStringDef o1, IMultilingualStringDef o2) {
                return o1.getId().toString().compareTo(o2.getId().toString());
            }
        });
        int[] count = new int[1];
        writeLoadStringMethods(langs, strings, postPrinter, count);
        for (int i = 1; i <= count[0]; i++) {
            writeLoadStringMethod(printer, i);
            printer.println(';');
        }
        printer.leaveBlock();
        printer.println("}");
        printer.println();

        printer.print(postPrinter.getContents());
        printer.print("public static final ");
        printer.print(MLB_CLASS_NAME);
        printer.print(" rdxMeta = new ");
        printer.print(MLB_CLASS_NAME);
        printer.print("(");
        //writeUsage(printer);
        printer.print(JavaSourceSupport.getMetaName(def, def.getRuntimeId(), printer instanceof IHumanReadablePrinter));

        printer.print(".class,");
        WriterUtils.writeIdUsage(printer, def.getId());
        printer.printComma();
        printer.printStringLiteral(def.getName());
        printer.println(",$$$items$$$);");
        printer.leaveBlock();
        printer.println('}');
        return true;
    }

    private boolean writeLoadStringMethods(final Set<EIsoLanguage> langs, List<IMultilingualStringDef> strings, final CodePrinter printer, int[] methodCount) {
        final CodePrinter methodPrinter = CodePrinter.Factory.newJavaPrinter(printer);
        final EnumSet<EIsoLanguage> unchecked = EnumSet.noneOf(EIsoLanguage.class);
        int count = 1;
        writeLoadStringMethodStart(methodPrinter, count);
        int methodStrings = 0;
        for (IMultilingualStringDef string : strings) {
            final CodePrinter stingPrinter = CodePrinter.Factory.newJavaPrinter(printer);
            stingPrinter.println("$$$strings$$$.clear();");

            final List<IMultilingualStringDef.StringStorage> vals = string.getValues(EScope.LOCAL_AND_OVERWRITE);
            Collections.sort(vals, LANGUAGE_COMPORATOR);
            unchecked.clear();
            for (IMultilingualStringDef.StringStorage e : vals) {
                if (!langs.contains(e.getLanguage())) {
                    continue;
                }
                stingPrinter.print("$$$strings$$$.put(");
                WriterUtils.writeEnumFieldInvocation(stingPrinter, e.getLanguage());
                stingPrinter.printComma();
                stingPrinter.printStringLiteral(e.getValue());
                stingPrinter.println(");");
                if (!e.isChecked()) {
                    unchecked.add(e.getLanguage());
                }
            }

            stingPrinter.print("$$$items$$$.put(");
            WriterUtils.writeIdUsage(stingPrinter, string.getId());
            stingPrinter.printComma();
            stingPrinter.print("new ");
            stingPrinter.print(MLS_CLASS_NAME);
            stingPrinter.print("($$$strings$$$");
            stingPrinter.printComma();

            if (string instanceof AdsEventCodeDef) {
                WriterUtils.writeEnumFieldInvocation(stingPrinter, ELocalizedStringKind.EVENT_CODE);
                stingPrinter.printComma();
                final AdsEventCodeDef ec = (AdsEventCodeDef) string;
                WriterUtils.writeEnumFieldInvocation(stingPrinter, ec.getEventSeverity());
                stingPrinter.printComma();
                stingPrinter.printStringLiteral(ec.getEventSource());
            } else {
                WriterUtils.writeEnumFieldInvocation(stingPrinter, ELocalizedStringKind.SIMPLE);
                stingPrinter.printComma();
                stingPrinter.print("null,null");
            }
            stingPrinter.printComma();
            if (unchecked.isEmpty()) {
                WriterUtils.writeNull(stingPrinter);
            } else {
                WriterUtils.writeEnumSet(stingPrinter, unchecked, EIsoLanguage.class);
            }
            stingPrinter.printComma();
            WriterUtils.writeNull(stingPrinter);
            stingPrinter.print("));");
            stingPrinter.println();

            if (methodStrings > 100) {
                methodPrinter.leaveBlock();
                methodPrinter.println("}");
                methodPrinter.println();
                printer.print(methodPrinter.getContents());
                methodPrinter.clear();
                count++;
                writeLoadStringMethodStart(methodPrinter, count);
                methodStrings = 0;
            }

            methodPrinter.print(stingPrinter.getContents());
            methodStrings++;
        }
        methodPrinter.leaveBlock();
        methodPrinter.println("}");
        methodPrinter.println();
        methodCount[0] = count;
        printer.print(methodPrinter.getContents());
        return true;
    }

    private void writeLoadStringMethodStart(final CodePrinter methodPrinter, int count) {
        methodPrinter.println("@SuppressWarnings(\"unused\")");
        methodPrinter.print("private static void ");
        writeLoadStringMethod(methodPrinter, count);
        methodPrinter.println("{");
        methodPrinter.enterBlock();

        writeStringsMapDeclaration(methodPrinter);
        methodPrinter.print("$$$strings$$$ = new ");
        writeStringsMapDeclaration(methodPrinter);
        methodPrinter.println("();");
    }

    private void writeLoadStringMethod(final CodePrinter printer, int count) {
        printer.print("loadStrings");
        printer.print(count);
        printer.print("()");
    }

    private void writeItemMapDeclaration(final CodePrinter printer) {
        printer.print("java.util.HashMap<");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.printComma();
        printer.print(MLS_CLASS_NAME);
        printer.print('>');

    }

    private void writeStringsMapDeclaration(final CodePrinter printer) {
        printer.print("java.util.HashMap<");
        printer.print(WriterUtils.RADIX_LANGUAGE_CLASS_NAME);
        printer.printComma();
        printer.print("String>");
    }
}
