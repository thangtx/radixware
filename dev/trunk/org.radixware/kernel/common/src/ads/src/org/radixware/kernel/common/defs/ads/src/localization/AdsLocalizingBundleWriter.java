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
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
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
        final Set<EIsoLanguage> langs = layer.getLanguages() == null ? Collections.<EIsoLanguage>emptySet() : EnumSet.copyOf(layer.getLanguages());

        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        WriterUtils.writeMetaShareabilityAnnotation(printer, def);
        printer.println();

        printer.print("public final class ");
        //writeUsage(printer);
        printer.print(def.getRuntimeId());
        printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
        printer.println('{');
        printer.enterBlock();
        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private static final ");
        writeItemMapDeclaration(printer);
        printer.print(" $$$items$$$ = new ");
        writeItemMapDeclaration(printer);
        printer.println("();");
        printer.println("static{");
        printer.enterBlock();
        printer.println("@SuppressWarnings(\"unused\")");
        writeStringsMapDeclaration(printer);
        printer.print("$$$strings$$$ = new ");
        writeStringsMapDeclaration(printer);
        printer.println("();");

        final EnumSet<EIsoLanguage> unchecked = EnumSet.noneOf(EIsoLanguage.class);
        final List<IMultilingualStringDef> strings = def.getUsedStrings(EScope.LOCAL_AND_OVERWRITE);
        Collections.sort(strings, new Comparator<IMultilingualStringDef>() {
            @Override
            public int compare(IMultilingualStringDef o1, IMultilingualStringDef o2) {
                return o1.getId().toString().compareTo(o2.getId().toString());
            }
        });
        for (IMultilingualStringDef string : strings) {

            printer.println("$$$strings$$$.clear();");

            final List<IMultilingualStringDef.StringStorage> vals = string.getValues(EScope.LOCAL_AND_OVERWRITE);

            unchecked.clear();
            for (IMultilingualStringDef.StringStorage e : vals) {
                if (!langs.contains(e.getLanguage())) {
                    continue;
                }
                printer.print("$$$strings$$$.put(");
                WriterUtils.writeEnumFieldInvocation(printer, e.getLanguage());
                printer.printComma();
                printer.printStringLiteral(e.getValue());
                printer.println(");");
                if (!e.isChecked()) {
                    unchecked.add(e.getLanguage());
                }
            }

            printer.print("$$$items$$$.put(");
            WriterUtils.writeIdUsage(printer, string.getId());
            printer.printComma();
            printer.print("new ");
            printer.print(MLS_CLASS_NAME);
            printer.print("($$$strings$$$");
            printer.printComma();

            if (string instanceof AdsEventCodeDef) {
                WriterUtils.writeEnumFieldInvocation(printer, ELocalizedStringKind.EVENT_CODE);
                printer.printComma();
                final AdsEventCodeDef ec = (AdsEventCodeDef) string;
                WriterUtils.writeEnumFieldInvocation(printer, ec.getEventSeverity());
                printer.printComma();
                printer.printStringLiteral(ec.getEventSource());
            } else {
                WriterUtils.writeEnumFieldInvocation(printer, ELocalizedStringKind.SIMPLE);
                printer.printComma();
                printer.print("null,null");
            }
            printer.printComma();
            if (unchecked.isEmpty()) {
                WriterUtils.writeNull(printer);
            } else {
                WriterUtils.writeEnumSet(printer, unchecked, EIsoLanguage.class);
            }
            printer.printComma();
            printer.printStringLiteral(string.getModule().getDirectory().getAbsolutePath());
            printer.print("));");

        }
        printer.leaveBlock();
        printer.println();
        printer.println("}");
        printer.print("public static final ");
        printer.print(MLB_CLASS_NAME);
        printer.print(" rdxMeta = new ");
        printer.print(MLB_CLASS_NAME);
        printer.print("(");
        //writeUsage(printer);
        printer.print(def.getRuntimeId());
        printer.print(JavaSourceSupport.META_CLASS_SUFFIX);

        printer.print(".class,");
        WriterUtils.writeIdUsage(printer, def.getId());
        printer.printComma();
        printer.printStringLiteral(def.getName());
        printer.println(",$$$items$$$);");
        printer.leaveBlock();
        printer.println('}');
        return true;
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
