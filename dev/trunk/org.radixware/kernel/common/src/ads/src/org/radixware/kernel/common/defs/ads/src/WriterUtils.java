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
package org.radixware.kernel.common.defs.ads.src;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import static org.radixware.kernel.common.defs.ads.AdsValAsStr.EValueType.VAL_AS_STR;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItemsOrder;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.RADIX_ID_CLASS_NAME;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.RADIX_OBJECT_REF_ANNOTATION;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.RADIX_TRACE_INTERFACE_NAME;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.RADIX_TRACE_LOOKUP_CLASS_NAME;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeEnumFieldInvocation;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeIdArrayUsage;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeIdUsage;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeNull;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writePackage;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeProfilerFinalization;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeProfilerInitialization;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeRadixValAsStr;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeServerArteAccessMethodDeclaration;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeServerArteAccessMethodInvocation;
import static org.radixware.kernel.common.defs.ads.src.WriterUtils.writeStringArrayUsage;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.*;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.lang.MetaInfo;
import org.radixware.kernel.common.lang.RadMetaClass;
import org.radixware.kernel.common.lang.RadixObjectRef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.scml.LiteralWriter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.XmlObjectProcessor;

public class WriterUtils {
    private static final String IS_LIGHT_GENERATION = "rdx.is.light.generation";

    public static final char[] ILLEGAL_USAGE_ERROR_CLASS_NAME = IllegalUsageError.class.getName().toCharArray();
    public static final char[] RADIX_ADS_TYPE_PACKAGE_PREFIX = "org.radixware.ads".toCharArray();
    public static final char[] RADIX_ADS_TYPE_USAGE_PURPOSE_SELECTOR_SERVER = "server".toCharArray();
    public static final char[] RADIX_ADS_TYPE_USAGE_PURPOSE_SELECTOR_EXPLORER = "explorer".toCharArray();
    public static final char[] RADIX_ADS_TYPE_USAGE_PURPOSE_SELECTOR_COMMON = "common".toCharArray();
    public static final char[] RADIX_XML_OBJECT_PROCESSOR_CLASS_NAME = XmlObjectProcessor.class.getName().toCharArray();
    private static final char[] RADIX_DEFAULT_VALUE_CLASS_NAME = RadixDefaultValue.class.getName().toCharArray();
    private static final char[] RADIX_VAL_AS_STR_CLASS_NAME = ValAsStr.class.getName().toCharArray();
    public static final char[] RADIX_ID_CLASS_NAME = Id.class.getName().toCharArray();
    public static final char[] RADIX_LANGUAGE_CLASS_NAME = EIsoLanguage.class.getName().toCharArray();
    public static final char[] RADIX_VAL_TYPE_CLASS_NAME = EValType.class.getName().toCharArray();
    private static final char[] TEXT_NULL = "null".toCharArray();
//    private static final char[] SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME = "$arte_instance_for_internal_use$".toCharArray();
    public static final char[] GET_ARTE_INVOCATON = "org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal()".toCharArray();
    public static final char[] ARTE_CLASS_NAME = "org.radixware.kernel.server.arte.Arte".toCharArray();
    public static final char[] PRESENTATIONS_META_SERVER_PACKAGE_NAME = "org.radixware.kernel.server.meta.presentations".toCharArray();
    public static final char[] PRESENTATIONS_META_EXPLORER_PACKAGE_NAME = "org.radixware.kernel.common.client.meta".toCharArray();
    public static final char[] EXPLORER_ITEMS_META_EXPLORER_PACKAGE_NAME = "org.radixware.kernel.common.client.meta.explorerItems".toCharArray();
    public static final char[] FILTERS_META_EXPLORER_PACKAGE_NAME = "org.radixware.kernel.common.client.meta.filters".toCharArray();
    public static final char[] CLASSES_META_SERVER_PACKAGE_NAME = "org.radixware.kernel.server.meta.clazzes".toCharArray();
    public static final char[] META_COMMON_PACKAGE_NAME = "org.radixware.kernel.common.meta".toCharArray();
    public static final char[] RADIX_APP_EXCEPTION_CLASS_NAME = AppException.class.getName().toCharArray();
    public static final char[] FORM_HANDLER_NEXT_DIALOG_REQUEST_CLASS_NAME = CharOperations.merge(JavaSourceSupport.RADIX_SERVER_TYPES_PACKAGE_NAME, "FormHandler.NextDialogsRequest".toCharArray(), '.');
    private static final char[] UTIL_VAR_NAME_PREFIX = "$$rdx__auto_internal_variable$$".toCharArray();
    public static final char[] RADIX_PID_CLASS_NAME = "org.radixware.kernel.server.types.Pid".toCharArray();
    public static final char[] ENTITY_OBJECT_NOT_EXISTS_ERROR_CLASS_NAME = "org.radixware.kernel.server.exceptions.EntityObjectNotExistsError".toCharArray();
    public static final char[] RADIX_OBJECT_REF_ANNOTATION = RadixObjectRef.class.getName().toCharArray();
    public static final char[] IKERNEL_INT_ENUM_CLASS_NAME = IKernelIntEnum.class.getName().toCharArray();
    public static final char[] IKERNEL_STR_ENUM_CLASS_NAME = IKernelStrEnum.class.getName().toCharArray();
    public static final char[] IKERNEL_CHAR_ENUM_CLASS_NAME = IKernelCharEnum.class.getName().toCharArray();
    public static final char[] XML_OBJECT_PROCESSOR_CLASS_NAME = XmlObjectProcessor.class.getName().toCharArray();
    public static final char[] RADIX_TRACE_INTERFACE_NAME = IRadixTrace.class.getName().toCharArray();
    public static final char[] RADIX_TRACE_LOOKUP_CLASS_NAME = IRadixTrace.Lookup.class.getName().replace('$', '.').toCharArray();
    public static final char[] RADIX_CLASS_LOADER_CLASS_NAME = IRadixClassLoader.class.getName().toCharArray();
    public static final char[] RADIX_ENVIRONMENT_CLASS_NAME = IRadixEnvironment.class.getName().toCharArray();
    public static final char[] EXPLORER_MODEL_CLASS_NAME = "org.radixware.kernel.common.client.models.Model".toCharArray();
    public static final char[] USER_SESSION_CLASS_NAME = "org.radixware.kernel.common.client.IClientEnvironment".toCharArray();
    public static final char[] META_INFO_CLASS_NAME = MetaInfo.class.getName().toCharArray();
    
    public static void setIsLightGeneration(final CodePrinter cp, boolean isInstallation) {
        cp.putProperty(IS_LIGHT_GENERATION, isInstallation);
    }    
    
    public static boolean isLightGeneration(final CodePrinter cp) {
        if (cp.getProperty(IS_LIGHT_GENERATION) != null) {
            return (Boolean) cp.getProperty(IS_LIGHT_GENERATION);
        }
        return false;
    }
    
    public static void enterHumanUnreadableBlock(final CodePrinter cp) {
        if (cp instanceof IHumanReadablePrinter) {
            ((IHumanReadablePrinter) cp).enterHumanUnreadableBlock();
        }
    }
    
    public static void leaveHumanUnreadableBlock(final CodePrinter cp) {
        if (cp instanceof IHumanReadablePrinter) {
            ((IHumanReadablePrinter) cp).leaveHumanUnreadableBlock();
        }
    }
    
    public static void writeEnumFieldInvocation(CodePrinter printer, Enum enumValue) {
        if (enumValue == null) {
            writeNull(printer);
        } else {
            printer.print(enumValue.getDeclaringClass().getName().replace('$', '.'));
            printer.print('.');
            printer.print(enumValue.name());
        }
    }

    public static void writeAutoVariable(CodePrinter printer, char[] name) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.print(UTIL_VAR_NAME_PREFIX);
        WriterUtils.leaveHumanUnreadableBlock(printer);
        printer.print(name);
    }

    public static void writeDefinitionRefAnnotation(CodePrinter printer, AdsDefinition def, String extStr) {
        printer.print('@');
        printer.print(RADIX_OBJECT_REF_ANNOTATION);
        printer.print("(path=\"");
        Id[] ids = def.getIdPath();
        boolean isFirst = true;
        for (int i = 0; i < ids.length; i++) {
            if (isFirst) {
                isFirst = false;
            } else {
                printer.print('.');
            }
            printer.print(ids[i].toCharArray());
        }
        if (extStr != null) {
            printer.print('.');
            printer.print(extStr);
        }
        printer.print("\",qualifiedName=\"");
        printer.print(def.getQualifiedName());
        printer.println("\")");
    }

    public static <E extends Enum<E>, C extends Class<E>> void writeEnumSet(CodePrinter printer, EnumSet<E> enumSet, C enumClass) {
        if (enumSet == null || enumSet.isEmpty()) {
            printer.print("java.util.EnumSet.noneOf(");
            printer.print(enumClass.getName());
            printer.print(".class)");
        } else {
            printer.print("java.util.EnumSet.of(");
            boolean first = true;
            for (E e : enumSet) {
                if (first) {
                    first = false;
                } else {
                    printer.printComma();
                }
                printer.print(enumClass.getName());
                printer.print('.');
                printer.print(e.name());
            }
            printer.print(')');
        }
    }

    public static void writePackageDeclaration(CodePrinter printer, Definition def, JavaSourceSupport.UsagePurpose up) {
        printer.print("package ");
        writePackage(printer, def, up);
        /*
         * char[][] packageNames =
         * JavaSourceSupport.getPackageNameComponents(module, environment); for
         * (int i = 0; i < packageNames.length; i++) {
         * printer.print(packageNames[i]); if (i + 1 < packageNames.length) {
         * printer.print('.'); } }
         */
        printer.printlnSemicolon();
    }

    public static void writePackage(CodePrinter printer, Definition def, JavaSourceSupport.UsagePurpose up) {
        writePackage(printer, def, up, '.');
    }

    public static void writePackage(CodePrinter printer, Definition def, JavaSourceSupport.UsagePurpose up, char separator) {
        char[][] packageNames = JavaSourceSupport.getPackageNameComponents(def, printer instanceof IHumanReadablePrinter, up);
        for (int i = 0; i < packageNames.length; i++) {
            printer.print(packageNames[i]);
            if (i + 1 < packageNames.length) {
                printer.print(separator);
            }
        }
    }

    public static void writeIdUsage(CodePrinter printer, Id id, RadixObject referencer) {
        if (id == null) {
            writeNull(printer);
        } else {

            RadixObjectLocator locator = null;
            if (referencer != null) {
                locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);

            }
            if (locator != null) {
                RadixObjectLocator.RadixObjectData marker = locator.start(referencer);
                printer.print(RADIX_ID_CLASS_NAME);
                printer.print(".Factory");
                marker.commit();
                printer.print('.');
                marker = locator.start(referencer);
                printer.print("loadFrom(");
                printer.printStringLiteral(id.toString());
                printer.print(')');
                marker.commit();
            } else {
                printer.print(RADIX_ID_CLASS_NAME);
                printer.print(".Factory.loadFrom(");
                printer.printStringLiteral(id.toString());
                printer.print(')');
            }

        }
    }

    public static void writeIdUsage(CodePrinter printer, Id id) {
        if (id == null) {
            writeNull(printer);
        } else {
            printer.print(RADIX_ID_CLASS_NAME);
            printer.print(".Factory.loadFrom(");
            printer.printStringLiteral(id.toString());
            printer.print(')');
        }
    }

    public static void writeIdArrayUsage(CodePrinter printer, Id[] ids) {
        writeIdArrayUsage(printer, ids == null || ids.length == 0 ? null : Arrays.asList(ids));
    }

    public static void writeIdArrayUsage(CodePrinter printer, List<Id> ids) {
        writeIdArrayUsage(printer, (Collection<Id>) ids);
    }

    public static void writeIdArrayUsage(CodePrinter printer, Collection<Id> ids) {
        writeIdArrayUsage(printer, ids, true);
    }

    public static void writeIdArrayUsage(CodePrinter printer, Collection<Id> ids, boolean emptyAsNull) {
        if (ids == null || (emptyAsNull && ids.isEmpty())) {
            writeNull(printer);
        } else {
            printer.print("new ");
            printer.print(RADIX_ID_CLASS_NAME);
            printer.print("[]{");
            boolean isFirst = true;
            for (Id id : ids) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    printer.printComma();
                }
                writeIdUsage(printer, id);
            }
            printer.print('}');
        }
    }

    public static void writeStringArrayUsage(CodePrinter printer, String[] ids) {
        writeStringArrayUsage(printer, ids == null || ids.length == 0 ? null : Arrays.asList(ids));
    }

    public static void writeStringArrayUsage(CodePrinter printer, Collection<String> ids) {
        writeStringArrayUsage(printer, ids, true);
    }

    public static void writeStringArrayUsage(CodePrinter printer, Collection<String> ids, boolean emptyAsNull) {
        writeStringArrayUsage(printer, ids, emptyAsNull, false);
    }

    public static void writeStringArrayUsage(CodePrinter printer, Collection<String> ids, boolean emptyAsNull, boolean nullItemsAsNulls) {
        if (ids == null || (emptyAsNull && ids.isEmpty())) {
            writeNull(printer);
        } else {
            printer.print("new String[]{");
            boolean isFirst = true;
            for (String id : ids) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    printer.printComma();
                }
                if (id == null && nullItemsAsNulls) {
                    writeNull(printer);
                } else {
                    printer.printStringLiteral(id);
                }
            }
            printer.print('}');
        }
    }

    public static void writeIntArrayUsage(CodePrinter printer, int[] values) {
        if (values == null || values.length == 0) {
            writeNull(printer);
        } else {
            printer.print("new int[]{");
            boolean isFirst = true;
            for (int value : values) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    printer.printComma();
                }
                printer.print(value);
            }
            printer.print('}');
        }
    }

    public static void writeNull(CodePrinter printer) {
        printer.print(TEXT_NULL);
    }

    public static void writeRadixDefaultValue(CodePrinter printer, AdsValAsStr val) {
        if (val == null || val.toString().equals("null")) {
            writeNull(printer);
        } else {
            switch (val.getValueType()) {
                case VAL_AS_STR:
                    printer.print(RADIX_DEFAULT_VALUE_CLASS_NAME);
                    printer.print(".Factory.newValAsStr(");
                    printer.print(RADIX_VAL_AS_STR_CLASS_NAME);
                    printer.print(".Factory.loadFrom(");
                    printer.printStringLiteral(val.toString());
                    printer.print("))");
                    break;
                default:
                    WriterUtils.writeNull(printer);
            }

        }
    }

//    public static final void writeRadixDefaultValue(CodePrinter printer, JavaSourceSupport.CodeWriter contextWriter, AdsValAsStr.IValueController controller, AdsValAsStr val) {
//        if (val == null || val.toString().equals("null")) {
//            writeNull(printer);
//        } else {
//            AdsValAsStr.print(val, controller, contextWriter, printer);
//        }
//    }
    public static void writeRadixValAsStr(CodePrinter printer, ValAsStr val) {
        if (val == null || val.toString().equals("null")) {
            writeNull(printer);
        } else {
            printer.print(RADIX_VAL_AS_STR_CLASS_NAME);
            printer.print(".Factory.loadFrom(");
            printer.printStringLiteral(val.toString());
            printer.print(")");
        }
    }

    public static void writeRadixValAsStr(CodePrinter printer, AdsValAsStr val) {
        if (val == null || val.toString().equals("null")) {
            writeNull(printer);
        } else {
            if (val.getValueType() == AdsValAsStr.EValueType.VAL_AS_STR) {
                writeRadixValAsStr(printer, val.getValAsStr());
            } else {
                writeNull(printer);
            }
        }
    }

    public static void writeLoadFromRadixValAsStr(CodePrinter printer, String val, EValType ownerType) {
        if (val == null || val.toString().equals("null")) {
            writeNull(printer);
        } else {
            printer.print(RADIX_VAL_AS_STR_CLASS_NAME);
            printer.print(".fromStr(");
            printer.printStringLiteral(val.toString());
            printer.printComma();
            writeEnumFieldInvocation(printer, ownerType);
            printer.print(")");
        }
    }

    public static void writeSqmlAsXmlStr(CodePrinter printer, Sqml sqml) {
        if (sqml == null) {
            writeNull(printer);
        } else {
            org.radixware.schemas.xscml.SqmlDocument xDoc = org.radixware.schemas.xscml.SqmlDocument.Factory.newInstance();
            sqml.appendTo(xDoc.addNewSqml());
            printer.printStringLiteral(xDoc.xmlText());
        }
    }

    public static void writeServerArteAccessMethodInvocation(AdsDefinition usageContext, CodePrinter printer) {
//        printer.print("_arte_access_storage_.");
//        printer.print(SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME);
//        printer.print("()");
        printer.print(GET_ARTE_INVOCATON);
    }

    public static void writeReleaseAccessorInMetaClass(CodePrinter printer) {
        printer.print("org.radixware.kernel.server.arte.MetaContextProvider.getRelease()");
    }

    public static void writeMetaShareabilityAnnotation(CodePrinter cp, Definition def) {
        cp.print("@");
        cp.print(RadMetaClass.class.getCanonicalName());
        cp.print("(shareabilityArea=");
        cp.print(EShareabilityArea.class.getCanonicalName());
        cp.print(".");
        if (isShareableMeta(def.getId())) {
            cp.print(EShareabilityArea.RELEASE.name());
        } else {
            cp.print(EShareabilityArea.ARTE.name());
        }
        cp.print(")");
    }

    public static boolean isShareableMeta(final Id id) {
        return !(id.getPrefix() == EDefinitionIdPrefix.ADS_ENUMERATION || id.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND);
    }

    public static void writeServerArteAccessMethodDeclaration(AdsDefinition declarator, CodePrinter printer) {
        writeServerArteAccessMethodDeclaration(declarator, CodeType.EXCUTABLE, printer);
    }

    public static void writeServerArteAccessMethodDeclaration(Definition declarator, CodeType codeType, CodePrinter printer) {
//        printer.println("/*Internal arte storage*/");
//        printer.println("static final class _arte_access_storage_{");
//        printer.enterBlock(1);
//        printer.print("private static ");
//        printer.print(WriterUtils.ARTE_CLASS_NAME);
//        printer.printSpace();
//        printer.print(SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME);
//        printer.printlnSemicolon();
//        printer.println("/*Internal arte accessor Used in generated code. Warning: never call this method directly*/");
//        printer.println("@SuppressWarnings(\"unused\")");
//        printer.print("private static ");
//        printer.print(WriterUtils.ARTE_CLASS_NAME);
//        printer.printSpace();
//        printer.print(WriterUtils.SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME);
//        printer.enterBlock(1);
//        printer.println("(){");
//        printer.print("if(");
//        printer.print(SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME);
//        printer.enterBlock(1);
//        printer.println("==null){");
//        printer.print(SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME);
//        printer.print("=(");
//
//        String declaratorClassName = declarator instanceof AdsClassDef ? ((AdsClassDef) declarator).getRuntimeLocalClassName() : (declarator instanceof AdsRoleDef) ? ((AdsRoleDef) declarator).getRuntimeId().toString() : declarator.getId().toString();
//
//        printer.print(declaratorClassName);
//        if (codeType == CodeType.META) {
//            printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
//        }
//        printer.print(".class.getClassLoader() instanceof ");
//        printer.print(WriterUtils.RADIX_CLASS_LOADER_CLASS_NAME);
//        printer.print(") ? ");
//        printer.print("(");
//        printer.print(WriterUtils.ARTE_CLASS_NAME);
//        printer.print(")((");
//        printer.print(WriterUtils.RADIX_CLASS_LOADER_CLASS_NAME);
//        printer.print(")");
//        printer.print(declaratorClassName);
//        if (codeType == CodeType.META) {
//            printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
//        }
//        printer.leaveBlock(1);
//        printer.println(".class.getClassLoader()).getEnvironment() : null;");
//        printer.println("}");
//        printer.print("return ");
//        printer.print(SERVER_CLASS_INTERNAL_ARTE_ACCESSOR_METHOD_NAME);
//        printer.leaveBlock(1);
//        printer.printlnSemicolon();
//        printer.println("}");
//        printer.leaveBlock(1);
//        printer.println("}");
    }

    public static abstract class ObjectArrayWriter<T> {

        protected final char[] itemClassName;

        public ObjectArrayWriter(char[] itemClassName) {
            this.itemClassName = Arrays.copyOf(itemClassName, itemClassName.length);
        }

        protected boolean skip(T item) {
            return false;
        }

        public final void write(CodePrinter printer, Collection<T> list) {
            boolean first = true;
            ArrayList<T> realList = list == null ? null : new ArrayList<T>(list.size());
            if (realList != null) {
                for (T item : list) {
                    if (!skip(item)) {
                        realList.add(item);
                    }
                }
            }
            if (realList == null || realList.isEmpty()) {
                WriterUtils.writeNull(printer);
            } else {
                printer.print("new ");
                printer.print(itemClassName);
                printer.enterBlock(2);
                printer.println("[]{");
                for (T item : realList) {
                    if (first) {
                        first = false;
                    } else {
                        printer.printComma();
                        printer.println();
                    }
                    writeItemConstructor(printer, item);
                }
                printer.leaveBlock(2);
                printer.println();
                printer.print('}');
            }
        }

        public abstract void writeItemConstructor(CodePrinter printer, T item);
    }

    public static abstract class SameObjectArrayWriter<T> extends WriterUtils.ObjectArrayWriter<T> {

        protected SameObjectArrayWriter(char[] itemClassName) {
            super(itemClassName);
        }

        @Override
        public void writeItemConstructor(CodePrinter printer, T item) {
            printer.println();
            printer.print("new ");
            printer.print(itemClassName);
            printer.print('(');
            writeItemConstructorParams(printer, item);
            printer.print(')');
        }

        public abstract void writeItemConstructorParams(CodePrinter printer, T item);
    }
    private static final char[] MAP_UTIL_CLASS_NAME = Maps.class.getName().toCharArray();

    public static void writeIdMapFromArraysCreation(CodePrinter printer, List<Id> keys, List<Id> values) {
        if (keys.isEmpty()) {
            writeNull(printer);
        } else {
            printer.print(MAP_UTIL_CLASS_NAME);
            printer.print(".fromArrays(");
            writeIdArrayUsage(printer, keys);
            printer.printComma();
            writeIdArrayUsage(printer, values);
            printer.print(')');
        }
    }
    private static final char[] NLS_LOOKUP_CLASS_NAME = IMlStringBundle.Lookup.class.getName().replace('$', '.').toCharArray();

    public static void writeNLSInvocation(CodePrinter printer, Id bundleId, Id stringId, AdsDefinition contextDef, JavaSourceSupport.UsagePurpose usagePurpose, boolean objectRequired) {
        printer.print(NLS_LOOKUP_CLASS_NAME);
        //printer.print(".getValue(");
        //printer.print(contextVarName);
        if (objectRequired) {
            printer.print(".getStringSet(");
        } else {
            printer.print(".getValue(");
        }
//      Works, but too slow. Do not delete this lines,they will be required on naming rules changes
//        if (contextDef instanceof IAdsTypeSource) {
//            ((IAdsTypeSource) contextDef).getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
//        } else {0
        /**
         * While only nls owner can invoke generating code and enclosing class
         * name is the same with it's id string representation this code is
         * correct. If not - uncomment upper 3 commented lines
         */
        char[] className;
        if (contextDef instanceof AdsClassDef) {
            className = ((AdsClassDef) contextDef).getRuntimeLocalClassName(printer instanceof IHumanReadablePrinter).toCharArray();
        } else {
            className = JavaSourceSupport.getName(contextDef, printer instanceof IHumanReadablePrinter, true);
        }
        printer.print(className);//by BAO
        //  }
        printer.print(".class");//by BAO
        printer.printComma();
        writeIdUsage(printer, bundleId);
        printer.printComma();
        writeIdUsage(printer, stringId);
        printer.print(')');
    }

    public static void writeNLSInvocation(CodePrinter printer, Id bundleId, String titleIdVarName, AdsDefinition contextDef, JavaSourceSupport.UsagePurpose usagePurpose) {
        printer.print(NLS_LOOKUP_CLASS_NAME);
        //printer.print(".getValue(");
        //printer.print(contextVarName);
        printer.print(".getValue(");
//      Works, but too slow. Do not delete this lines,they will be rwquired on naming rules changes
//        if (contextDef instanceof IAdsTypeSource) {
//            ((IAdsTypeSource) contextDef).getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
//        } else {
        /**
         * While only nls owner can invoke generating code and enclosing class
         * name is the same with it's id string representation this code is
         * correct. If not - uncomment upper 3 commented lines
         */
        printer.print(contextDef.getId().toCharArray());//by BAO
//        }
        printer.print(".class");//by BAO
        printer.printComma();
        writeIdUsage(printer, bundleId);
        printer.printComma();
        printer.print(titleIdVarName);
        printer.print(')');

    }

    public static void writeNLSInvocation(CodePrinter printer, Id bundleId, String titleIdVarName, String langVarName, AdsDefinition contextDef, JavaSourceSupport.UsagePurpose usagePurpose) {
        printer.print(NLS_LOOKUP_CLASS_NAME);
        //printer.print(".getValue(");
        //printer.print(contextVarName);
        printer.print(".getValue(");
//      Works, but too slow. Do not delete this lines,they will be rwquired on naming rules changes
//        if (contextDef instanceof IAdsTypeSource) {
//            ((IAdsTypeSource) contextDef).getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
//        } else {
        /**
         * While only nls owner can invoke generating code and enclosing class
         * name is the same with it's id string representation this code is
         * correct. If not - uncomment upper 3 commented lines
         */
        printer.print(contextDef.getId().toCharArray());//by BAO
//        }
        printer.print(".class");//by BAO
        printer.printComma();
        writeIdUsage(printer, bundleId);
        printer.printComma();
        printer.print(titleIdVarName);
        printer.printComma();
        printer.print(langVarName);
        printer.print(')');

    }

    public static void writeTraceAccessCode(CodePrinter printer, String traceVarName, AdsDefinition context, JavaSourceSupport.UsagePurpose env) {
        printer.print(RADIX_TRACE_INTERFACE_NAME);
        printer.printSpace();
        printer.print(traceVarName);
        printer.print(" = ");
        printer.print(RADIX_TRACE_LOOKUP_CLASS_NAME);
        printer.print(".findInstance(");
        AdsType contextType = null;
        if (context instanceof AdsMethodDef) {
            AdsMethodDef method = (AdsMethodDef) context;
            if (method.getProfile().getAccessFlags().isStatic()) {
                contextType = method.getOwnerClass().getType(EValType.USER_CLASS, null);
            }
        } else if (context instanceof AdsContextlessCommandDef) {
            contextType = ((AdsContextlessCommandDef) context).getType(EValType.USER_CLASS, null);
        }
        if (contextType != null) {
            contextType.getJavaSourceSupport().getCodeWriter(env).writeUsage(printer);
            printer.print(".class");
        } else {
            printer.print("getClass()");
        }
        printer.println(");");
    }

    public static void writeTracePutCode(CodePrinter printer, String traceVarName, EEventSource source, EEventSeverity severity, String message) {
        printer.print("if(");
        printer.print(traceVarName);
        printer.enterBlock();
        printer.println(" != null){");
        printer.print(traceVarName);
        printer.print(".put(");
        //put(final EEventSeverity severity, final String localizedMess, final EEventSource source);
        writeEnumFieldInvocation(printer, severity);
        printer.printComma();
        printer.printStringLiteral(message);
        printer.printComma();
        writeEnumFieldInvocation(printer, source);
        printer.println(");");
        printer.leaveBlock();
        printer.println("}");
    }

    public static void writeTimestamp(CodePrinter printer, Timestamp ts) {
        if (ts == null) {
            writeNull(printer);
        } else {
            printer.print("new java.sql.Timestamp(");
            printer.print(ts.getTime());
            printer.print(')');
        }
    }

    public static void writeTimestamp(CodePrinter printer, Calendar ts) {
        if (ts == null) {
            writeNull(printer);
        } else {
            printer.print("new java.sql.Timestamp(");
            printer.print(ts.getTimeInMillis());
            printer.print(')');
        }
    }

    public static void writeBigDecimal(CodePrinter printer, BigDecimal d) {
        if (d == null) {
            writeNull(printer);
        } else {
            printer.print("new java.math.BigDecimal(");
            printer.printStringLiteral(d.toString());
            printer.print(",java.math.MathContext.UNLIMITED)");
        }
    }

    public static void writeCharacter(CodePrinter printer, String s) {
        if (s == null || s.isEmpty()) {
            writeNull(printer);
        } else {
            printer.print("Character.valueOf('");
            printer.print(LiteralWriter.str2Literal(s));
            printer.print("')");
        }
    }

    public static void writeBoolean(CodePrinter printer, Boolean val) {
        if (val == null) {
            writeNull(printer);
        } else {
            printer.print(val ? "Boolean.TRUE" : "Boolean.FALSE");
        }
    }

    public static void writeProfilerInitialization(CodePrinter printer, IProfileable prof) {
        writeProfilerInitialization(printer, prof, null);
    }

    public static void writeProfilerInitialization(CodePrinter printer, IProfileable prof, String arteAccess) {
        AdsProfileSupport support = prof.getProfileSupport();
        if (support != null && support.isProfiled()) {
            printer.println("\n//>>>>>>>>>>>>>AUTOGENERATED CODE>>>>>>>>>>>>>");
            printer.println("try{");
            printer.enterBlock();
            if (arteAccess == null) {
                writeServerArteAccessMethodInvocation((prof.getAdsDefinition()), printer);
            } else {
                printer.print(arteAccess);
            }
            printer.print(".getProfiler().enterTimingSection(");
            printer.printStringLiteral(support.getTimingSectionId());
            printer.println(");");
            printer.leaveBlock();
            printer.println("\n//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            printer.enterBlock();
        }
    }

    public static void writeProfilerFinalization(CodePrinter printer, IProfileable prof) {
        writeProfilerFinalization(printer, prof, null);
    }

    public static void writeProfilerFinalization(CodePrinter printer, IProfileable prof, String arteAccess) {
        AdsProfileSupport support = prof.getProfileSupport();
        if (support != null && support.isProfiled()) {
            printer.leaveBlock();
            printer.println("\n//>>>>>>>>>>>>>AUTOGENERATED CODE>>>>>>>>>>>>>");
            printer.println("}finally{");
            printer.enterBlock();
            if (arteAccess == null) {
                writeServerArteAccessMethodInvocation((prof.getAdsDefinition()), printer);
            } else {
                printer.print(arteAccess);
            }
            printer.print(".getProfiler().leaveTimingSection(");
            printer.printStringLiteral(support.getTimingSectionId());
            printer.println(");");
            printer.leaveBlock();
            printer.println("}");
            printer.println("\n//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public static boolean writeSimpleRadixTypeValueConversion(JavaSourceSupport.CodeWriter contextCodeWriter, AdsTypeDeclaration typeDecl, AdsType type, AdsDefinition context, String testVarName, String varName, JavaSourceSupport.UsagePurpose usagePurpose, CodePrinter printer, boolean isResultOfRPC, Id resultTableId) {
        if (testVarName != null) {
            printer.print(testVarName);
            printer.print(" == null ? null : ");
        }
        if (type instanceof AdsEnumType) {
            JavaSourceSupport.CodeWriter w = type.getJavaSourceSupport().getCodeWriter(usagePurpose);
            printer.print(varName);
            printer.print(" instanceof ");
            w.writeCode(printer);
            printer.print(" ? (");
            w.writeCode(printer);
            printer.print(") ");
            printer.print(varName);
            printer.print(" : ");

            if (typeDecl.getTypeId().isArrayType()) {
                printer.print("new ");
                w.writeCode(printer);
                printer.print('(');
                printer.print("(");
                AdsType simpleType = RadixType.Factory.newInstance(typeDecl.getTypeId());
                contextCodeWriter.writeCode(printer, simpleType, context);
                printer.print(")");
                printer.print(varName);
                printer.println(");");
            } else {
                w.writeCode(printer);
                printer.print(".getForValue(");
                printer.print("(");
                AdsType simpleType = RadixType.Factory.newInstance(typeDecl.getTypeId());
                contextCodeWriter.writeCode(printer, simpleType, context);
                printer.print(")");
                printer.print(varName);
                printer.println(");");
            }
            return true;
        } else if (type instanceof RadixType) {
            printer.print("(");
            contextCodeWriter.writeCode(printer, typeDecl, context);
            printer.print(")");
            printer.print(varName);
            printer.println(";");
            return true;
        } else if (type instanceof XmlType) {
            JavaSourceSupport.CodeWriter w = type.getJavaSourceSupport().getCodeWriter(usagePurpose);

            printer.print(varName);
            printer.print(" instanceof ");
            w.writeCode(printer);
            printer.print(" ? (");
            w.writeCode(printer);
            printer.print(") ");
            printer.print(varName);
            printer.print(" : (");
            w.writeCode(printer);
            printer.print(") ");
            printer.print(WriterUtils.XML_OBJECT_PROCESSOR_CLASS_NAME);
            printer.print(".castToXmlClass(");
            printer.print("getClass().getClassLoader()");
            printer.printComma();
            printer.print("(org.apache.xmlbeans.XmlObject)");
            printer.print(varName);
            printer.printComma();
            w.writeCode(printer);
            printer.println(".class);");
            return true;
        } else if (type instanceof ParentRefType) {
            if (usagePurpose.getEnvironment().isClientEnv()) {
                if (isResultOfRPC) {//String to Reference
                    if (resultTableId == null) {
                        return false;
                    }
                    printer.print("new org.radixware.kernel.common.client.types.Reference(new org.radixware.kernel.common.client.types.Pid(");
                    WriterUtils.writeIdUsage(printer, resultTableId);
                    printer.printComma();
                    printer.print("(Str)");
                    printer.print(varName);
                    printer.printComma();
                    printer.print("getEnvironment().getDefManager()");
                    printer.println("));");
                } else {
                    printer.print("org.radixware.kernel.common.client.types.Reference)");
                    printer.print(varName);
                    printer.println(';');
                }
                return true;
            } else {
                return false;
            }
        } else if (type instanceof AdsClassType) {
            if (usagePurpose.getEnvironment() == ERuntimeEnvironmentType.SERVER) {
                AdsClassDef clazz = ((AdsClassType) type).getSource();
                if (clazz instanceof AdsEntityObjectClassDef) {
                    printer.print('(');
                    type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                    printer.print(')');
                    printer.print(varName);
                    printer.println(';');
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static boolean printValAsStrAsJavaInitializerForVariable(ValAsStr initVal, AdsTypeDeclaration decl, Definition contextDef, JavaSourceSupport.CodeWriter contextWriter, CodePrinter printer) {

        if (decl.getTypeId() == EValType.USER_CLASS || decl.getTypeId() == EValType.JAVA_TYPE || decl.getTypeId() == EValType.JAVA_CLASS) {
            if (initVal != null) {
                printer.print(initVal.toString());
            } else {
                if (decl.getTypeId() == EValType.JAVA_TYPE) {
                    AdsType type = decl.resolve(contextDef).get();
                    if (type instanceof JavaType) {
                        //printer.print('=');
                        printer.print(((JavaType) type).getDefaultInitValAsCharArray());
                    } else if (type instanceof ArrayType) {
                        printer.print("null");
                    } else {
                        return false;
                    }
                } else {
                    printer.print("null");
                }
            }
        } else {
            if (initVal != null) {
                AdsType type = decl.resolve(contextDef).get();
                if (type == null) {
                    return false;
                }
                AdsType castType = type;
                if (type instanceof AdsEnumType) {
                    EValType typeId = ((AdsEnumType) type).getSource().getItemType();
                    if (type instanceof AdsEnumType.Array) {
                        typeId = typeId.getArrayType();
                    }
                    castType = RadixType.Factory.newInstance(typeId);
                    contextWriter.getCodeWriter(decl, contextDef).writeCode(printer);
                    printer.print(".getForValue(");
                }
                printer.print('(');
                castType.getJavaSourceSupport().getCodeWriter(contextWriter.usagePurpose).writeCode(printer);
                printer.print(')');
                printer.print("org.radixware.kernel.common.defs.value.ValAsStr.fromStr(");
                printer.printStringLiteral(initVal.toString());
                printer.printComma();
                WriterUtils.writeEnumFieldInvocation(printer, decl.getTypeId());
                printer.print(")");
                if (type instanceof AdsEnumType) {
                    printer.print(')');
                }
            } else {
                printer.print("null");
            }
        }
        return true;
    }

    public static boolean writeAdsValAsStr(AdsValAsStr value, AdsValAsStr.IValueController controller, JavaSourceSupport.CodeWriter contextWriter, CodePrinter printer) {
        if (value == null || value.getValueType() != AdsValAsStr.EValueType.JML) {
            return WriterUtils.printValAsStrAsJavaInitializerForVariable(value == null ? null : value.getValAsStr(), controller.getContextType(), controller.getContextDefinition(), contextWriter, printer);
        } else {
            return contextWriter.getCodeWriter(value.getJml()).writeCode(printer);
        }
    }
    private static final String USER_DEF_HEADER = "//USER_DEFINED_DEFINITION:";

    public static void writeUserDefinitionHeader(CodePrinter printer, Definition userDefinition) {
        printer.print(USER_DEF_HEADER);
        printer.print(userDefinition.getId());
        printer.println('.');
    }

    public static Id getUserDefinitionId(File srcFile) {
        InputStreamReader in = null;
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(srcFile);
            in = new InputStreamReader(fileStream, FileUtils.XML_ENCODING);
            final int matchLen = USER_DEF_HEADER.length();
            char[] buffer = new char[matchLen + 50];
            int count;
            StringBuilder sb = new StringBuilder();
            boolean initialMatchPassed = false;
            int dotIndex = -1;
            while ((count = in.read(buffer)) >= 0) {
                sb.append(buffer, 0, count);
                if (sb.length() >= matchLen) {
                    if (initialMatchPassed) {
                        dotIndex = sb.indexOf(".");
                    } else {
                        if (!CharOperations.startsWith(sb, USER_DEF_HEADER, 0)) {
                            return null;
                        } else {
                            initialMatchPassed = true;
                            dotIndex = sb.indexOf(".");
                        }
                    }
                    if (dotIndex > 0) {
                        break;
                    } else {
                        int endlnIndex = sb.indexOf("\n");
                        if (endlnIndex > 0) {
                            return null;
                        }
                    }
                }
            }
            if (dotIndex <= matchLen) {
                return null;
            } else {
                String idCandidate = sb.substring(matchLen, dotIndex);
                if (idCandidate.length() >= 29) {
                    try {
                        return Id.Factory.loadFrom(idCandidate);
                    } catch (NoConstItemWithSuchValueError e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }

        } catch (IOException ex) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(WriterUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(WriterUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    public static void writeLookForDefinitionInBaseLayer(CodePrinter printer, String resultClassName) {
        printer.println("private static final " + resultClassName + " ____$$$____bcl(org.radixware.kernel.common.types.Id[] path) {");
        printer.println("   " + resultClassName + " base =  ____$$$____bcl();");
        printer.println("   if(base == null) return null;");
        printer.println("   return " + resultClassName + ".LookupHelper.findChild(base,path);");
        printer.println("}");
    }

    public static void writeLoadMetaClassFromBaseLayer(CodePrinter printer, String currentLayer, Id moduleId, ERuntimeEnvironmentType env, Id id, String resultClassName) {
        printer.println("private static final " + resultClassName + " ____$$$____bcl(){");
        printer.enterBlock();
        printer.println("boolean search=false;");
        printer.println("for (org.radixware.kernel.starter.meta.LayerMeta meta : org.radixware.kernel.starter.radixloader.RadixLoader.getInstance().getCurrentRevisionMeta().getAllLayersSortedFromTop()) {");
        printer.enterBlock();
        printer.println("if(\"" + currentLayer + "\".equals(meta.getUri())){search=true;continue;}else if(!search)continue;");
        printer.print("String className = meta.getUri().replace(\"-\",\"_\") + \".ads." + moduleId.toString() + ".");
        switch (env) {
            case SERVER:
                printer.print(AdsModule.GEN_SERVER_DIR_NAME_C);
                break;
            case EXPLORER:
                printer.print(AdsModule.GEN_EXPLORER_DIR_NAME_C);
                break;
            case COMMON:
                printer.print(AdsModule.GEN_COMMON_DIR_NAME_C);
                break;
            case COMMON_CLIENT:
                printer.print(AdsModule.GEN_COMMON_CLIENT_DIR_NAME_C);
                break;
            case WEB:
                printer.print(AdsModule.GEN_WEB_DIR_NAME_C);
                break;
        }

        printer.print(".");
        printer.print(id.toString());
        printer.println("_mi\";");
        printer.println("try{");
        printer.println("    Class c = " + id + "_mi.class.getClassLoader().loadClass(className);");
        printer.println("    java.lang.reflect.Field f = c.getField(\"rdxMeta\");");
        printer.println("    return (" + resultClassName + ") f.get(null);");
        printer.println("}catch(ClassNotFoundException e){");
        printer.println("} catch (NoSuchFieldException ex) {");
        printer.println("} catch (IllegalAccessException ex) {");
        printer.println("} catch (SecurityException ex) {");
        printer.println("}");
        printer.leaveBlock();
        printer.println("}");
        printer.println("return null;");
        printer.leaveBlock();
        printer.println("}");

    }

    public static void writeExplorerItemsOrderAndVisibility(CodePrinter printer, ExplorerItems eis) {
        List<ExplorerItemsOrder.OrderAndVisibilityRules> rules = eis.getItemsOrder().getOrderAndVisibilityRules();
        final char[] explorerItemsSettingsClassName = CharOperations.merge(WriterUtils.EXPLORER_ITEMS_META_EXPLORER_PACKAGE_NAME, "RadExplorerItemsSettings".toCharArray(), '.');
        if (rules.isEmpty() || (eis.getOwnerDefinition() instanceof AdsParagraphExplorerItemDef && !((AdsDefinition) eis.getOwnerDefinition()).isTopLevelDefinition())) {
            printer.print("(");
            printer.print(explorerItemsSettingsClassName);
            printer.print("[])");
            WriterUtils.writeNull(printer);
        } else {
            printer.println(" new ");
            printer.print(explorerItemsSettingsClassName);
            printer.print("[]{");
            printer.enterBlock();
            boolean first = true;
            for (ExplorerItemsOrder.OrderAndVisibilityRules rule : rules) {
                if (first) {
                    first = false;
                } else {
                    printer.printComma();
                    printer.println();
                }
                printer.print("new ");
                printer.print(explorerItemsSettingsClassName);
                printer.print("(");
                WriterUtils.writeIdUsage(printer, rule.rootId);
                printer.printComma();
                if (rule.order == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    printer.enterBlock();
                    printer.println();
                    WriterUtils.writeIdArrayUsage(printer, rule.order);
                    printer.leaveBlock();
                }
                printer.printComma();
                if (rule.visible == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    printer.print("new ");
                    printer.print(explorerItemsSettingsClassName);
                    printer.print(".ItemVisibility[]{");
                    printer.enterBlock();
                    printer.println();
                    List<Id> ids = new ArrayList<>(rule.visible.keySet());
                    Collections.sort(ids);
                    boolean first2 = true;
                    for (Id id : ids) {
                        if (first2) {
                            first2 = false;
                        } else {
                            printer.printComma();
                            printer.println();
                        }
                        printer.print("new ");
                        printer.print(explorerItemsSettingsClassName);
                        printer.print(".ItemVisibility(");
                        WriterUtils.writeIdUsage(printer, id);
                        printer.printComma();
                        printer.print(rule.visible.get(id));
                        printer.print(")");
                    }
                    printer.leaveBlock();
                    printer.print("}");
                    printer.println();
                }
                printer.print(')');
            }
            printer.leaveBlock();
            printer.println("}");
        }
    }

    public static void writeMetaAnnotation(CodePrinter printer, RadixObject def, boolean writeLine) {
        printer.print('@');
        printer.print(META_INFO_CLASS_NAME);
        printer.print("(name=");
        printer.printStringLiteral(def.getQualifiedName());
        if (writeLine && !(printer instanceof IHumanReadablePrinter)) {
            printer.print(",line=" + (printer.getLineNumber(printer.length()) + 2));
        }
        printer.println(")");
    }
}
