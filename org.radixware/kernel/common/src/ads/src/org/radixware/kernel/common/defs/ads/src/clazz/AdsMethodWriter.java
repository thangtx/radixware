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
package org.radixware.kernel.common.defs.ads.src.clazz;

import java.util.Arrays;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.lang.ReflectiveCallable;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;

public class AdsMethodWriter<T extends AdsMethodDef> extends AbstractDefinitionWriter<T> {

    private AdsClassDef ownerClass;
    private char[] methodId;

    public AdsMethodWriter(JavaSourceSupport support, T method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
        this.ownerClass = method.getOwnerClass();
        this.methodId = method.getId().toCharArray();
    }

    @Override
    protected String getLocatorMarker() {
        if (def instanceof AdsUserMethodDef) {
            return ((AdsUserMethodDef) def).getSource(usagePurpose.getEnvironment()).getName();
        } else {
            return super.getLocatorMarker();
        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        if (def.isConstructor()) {
            AdsType type = def.getOwnerClass().getType(EValType.USER_CLASS, null);
            writeUsage(printer, type);
        } else {
            printer.print(comuteEffectiveJavaName(def, printer instanceof IHumanReadablePrinter));
        }
    }
    
    public static char[] comuteEffectiveJavaName(AdsMethodDef method, boolean isHumanReadable) {
        AdsMethodDef root = method;

        while (root != null) {
            AdsMethodDef ovr = root.getHierarchy().findOverridden().get();
            if (ovr != null) {
                root = ovr;
            } else {
                break;
            }
        }
        if (root instanceof AdsTransparentMethodDef) {
            return ((AdsTransparentMethodDef) root).getPublishedMethodName();
        } else {
            if (isHumanReadable) {
                return method.getName().toCharArray();
            } else {
                return method.getId().toCharArray();
            }
        }
    }

    public static char[] comuteEffectiveJavaName(AdsMethodDef method) {
        return comuteEffectiveJavaName(method, false);
    }
    private static final char[] REFLECTIVE_CALLABLE_CLASS_NAME = ReflectiveCallable.class.getName().toCharArray();

    private void writeSuperConstructorCall(CodePrinter printer) {

        printer.enterBlock();
        printer.print("super(");

        final Profile profile = def.getProfile();

        boolean isFirst = true;
        for (final MethodParameter p : profile.getParametersList()) {
            if (isFirst) {
                isFirst = false;
            } else {
                printer.printComma();
                printer.printSpace();
            }
            printer.print(p.getName());
        }

        printer.print(");");
        printer.leaveBlock();
    }

    // RADIX-5796
    private boolean isContructorOfExtendableClass() {
        return AdsTransparence.isTransparent(def)
                && def.isConstructor() && def.getOwnerClass() != null && AdsTransparence.isTransparent(def.getOwnerClass(), true);
    }

    @Override
    public boolean writeExecutable(CodePrinter printer) {

        if (AdsTransparence.isTransparent(def) && !isContructorOfExtendableClass()) {
            return true;
        } else {
            final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
            RadixObjectLocator.RadixObjectData marker = null;
            if (locator != null) {
                marker = locator.start(def);
            }

            if (def.isReflectiveCallable()) {
                printer.print('@');
                printer.println(REFLECTIVE_CALLABLE_CLASS_NAME);
            }
            if (def.getNature() == EMethodNature.PRESENTATION_SLOT || def.getNature() == EMethodNature.SYSTEM) {
                printer.println("@SuppressWarnings(\"unused\")");
            }
            if (def.isOverride() && def.getId() != AdsPropertiesWriter.CREATE_PROP_METHOD_ID) {
                printer.println(JavaSourceSupport.ANNOTATION_OVERRIDE);
            }
            WriterUtils.writeMetaAnnotation(printer, def, true);

            final Profile profile = def.getProfile();
//            if (profile.getAccessFlags().isDeprecated()) {
//                printer.println(JavaSourceSupport.ANNOTATION_DEPRECATED);
//            }

            writeCode(printer, profile.getAccessFlags());
            printer.printSpace();
            if (def.isConstructor()) {
                char[] clsName = ownerClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getLocalTypeName(UsagePurpose.getPurpose(def.getUsageEnvironment(), usagePurpose.getCodeType()), printer instanceof IHumanReadablePrinter);
                if (ownerClass.isNested()) {
                    for (int i = clsName.length - 1; i >= 0; i--) {
                        if (clsName[i] == '.') {
                            clsName = Arrays.copyOfRange(clsName, i + 1, clsName.length);
                            break;
                        }
                    }
                }
                printer.print(clsName);
            } else {
                writeUsage(printer, profile.getReturnValue().getType(), def);
                //profile.getReturnValue().getType().getJavaSourceSupport(getSupport().getContext()).getCodeWriter(usagePurpose).writeCode(printer);
                printer.printSpace();
                if (def.getId() == AdsPropertiesWriter.CREATE_PROP_METHOD_ID) {
                    printer.print(def.getId());

                } else {
                    writeUsage(printer);
                }
            }
            printer.printSpace();
            printer.print('(');
            boolean isFirst = true;
            for (MethodParameter p : profile.getParametersList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    printer.printComma();
                    printer.printSpace();
                }
//                writeCode(printer, p);
                //p.getType().getJavaSourceSupport(getSupport().getContext()).getCodeWriter(usagePurpose).writeCode(printer);
                if (p.isVariable()) {
                    if (p.getType().isArray()) {
                        writeUsage(printer, p.getType().toArrayType(p.getType().getArrayDimensionCount() - 1), def);
                    }
                    printer.print("...");
                } else {
                    writeUsage(printer, p.getType(), def);
                }
                printer.printSpace();
                printer.print(p.getName());
            }
            printer.print(')');

            if (!profile.getThrowsList().isEmpty()) {
                isFirst = true;
                printer.print(" throws ");
                for (AdsMethodThrowsList.ThrowsListItem ti : profile.getThrowsList()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        printer.printComma();
                    }
                    AdsTypeDeclaration decl = ti.getException();
                    if (decl == null) {
                        printer.printError();
                    } else {
                        writeCode(printer, decl, def);
                    }
                }
            }

            if (profile.getAccessFlags().isAbstract() || (ownerClass instanceof AdsInterfaceClassDef)) {
                printer.printlnSemicolon();
                if (marker != null) {
                    marker.commit();
                }
            } else {
                printer.printSpace();
                if (marker != null) {
                    marker.commit();
                }
                printer.println('{');
                try {
                    printer.enterCodeSection(def.getLocationDescriptor(usagePurpose.getEnvironment()));
                    WriterUtils.writeProfilerInitialization(printer, def);
                    writeBody(printer);
                    WriterUtils.writeProfilerFinalization(printer, def);
                } finally {
                    printer.leaveCodeSection();
                }
                printer.println();
                printer.println("}");
            }

        }
        return true;
    }

    protected void writeBody(CodePrinter printer) {

        if (isContructorOfExtendableClass()) {
            writeSuperConstructorCall(printer);
        } else if (def instanceof AdsUserMethodDef) {
            printer.enterBlock(1);
            Jml source = ((AdsUserMethodDef) def).getSource(usagePurpose.getEnvironment());
            if (source != null) {
                writeCode(printer, source);
            }
            printer.leaveBlock(1);
        }
    }

    public static char[] writeBody(AdsMethodDef method, UsagePurpose up) {
        AdsMethodWriter w = (AdsMethodWriter) method.getJavaSourceSupport().getCodeWriter(up);
        CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
        w.writeBody(printer);
        return printer.getContents();
    }

    public static void printProfileErasure(CodeWriter ownerWriter, CodePrinter printer, AdsMethodDef method, Definition resolutionContext, boolean writeStatic, boolean reflectiveCallable) {
        printProfileErasure(ownerWriter, printer, method, resolutionContext, writeStatic, reflectiveCallable, null, null);
    }

    public static void printProfileErasure(CodeWriter ownerWriter, CodePrinter printer, AdsMethodDef method, Definition resolutionContext, boolean writeStatic, boolean reflectiveCallable, String alternateName, String access) {
        AdsMethodDef.Profile profile = method.getProfile();

        if (reflectiveCallable) {
            printer.print('@');
            printer.println(REFLECTIVE_CALLABLE_CLASS_NAME);
        }
        if (access != null) {
            printer.print(access);
            printer.printSpace();
        } else {
            printer.print("public ");
        }
        if (writeStatic) {
            printer.print("static ");
        }

        ownerWriter.writeUsage(printer, profile.getReturnValue().getType(), resolutionContext);
        printer.printSpace();
        if (alternateName != null) {
            printer.print(alternateName);
        } else {
            ownerWriter.writeUsage(printer, method);
        }
        printer.printSpace();
        printer.print('(');

        boolean isFirst = true;
        for (MethodParameter p : profile.getParametersList()) {
            if (isFirst) {
                isFirst = false;
            } else {
                printer.printComma();
                printer.printSpace();
            }
            ownerWriter.writeUsage(printer, p.getType(), resolutionContext);
            printer.printSpace();
            printer.print(p.getName());
        }
        printer.print(')');

        if (!profile.getThrowsList().isEmpty()) {
            isFirst = true;
            printer.print(" throws ");

            for (AdsMethodThrowsList.ThrowsListItem ti : profile.getThrowsList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    printer.printComma();
                }
                AdsTypeDeclaration decl = ti.getException();
                if (decl == null) {
                    printer.printError();
                } else {
                    ownerWriter.writeCode(printer, decl, resolutionContext);
                }
            }
        }
    }
}
