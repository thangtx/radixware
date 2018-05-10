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

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef.RefMapItem;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef.Getter;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef.Setter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EPropNature;
import static org.radixware.kernel.common.enums.EPropNature.DYNAMIC;
import static org.radixware.kernel.common.enums.EPropNature.EVENT_CODE;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public abstract class AdsPropertyWriter<T extends AdsPropertyDef> extends AbstractDefinitionWriter<T> {
    
    public static final String PROPERTY_ACCESSOR_WARNING = "/**Property accessor for metainformation. Do not use this class directly*/";

    protected static final char[] TEXT_GET = "get".toCharArray();
    protected static final char[] TEXT_SET = "set".toCharArray();
    protected static final char[] TEXT_BAKS = "$$$".toCharArray();
    protected static final char[] TEXT_BRACKETS = "()".toCharArray();
    protected static final char[] TEXT_SETTER_PARAM_VARIABLE_NAME = "val".toCharArray();
    protected static final char[] TEXT_SETTER_PREFIX = " void set".toCharArray();
    protected static final char[] TEXT_GETTER_SUFFIX = "() {".toCharArray();
    protected static final char[] TEXT_SETTER_SUFFIX = " val) {".toCharArray();
    protected static final char[] TEXT_HIDDEN_ACC_PREFIX = "protected ".toCharArray();
    protected static final char[] TEXT_HIDDEN_STATIC_ACC_PREFIX = "protected static ".toCharArray();
    static final char[] PROPERTY_SERVER_META_CLASS_NAME = CharOperations.merge(WriterUtils.CLASSES_META_SERVER_PACKAGE_NAME, "RadPropDef".toCharArray(), '.');
    static final char[] PROPERTY_EXPLORER_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadPropertyDef".toCharArray(), '.');

    public static final class Factory {

        public static final PresentationPropertyCache ppCache = new PresentationPropertyCache();

        public static final class PresentationPropertyCache {

            private HashMap<IModelPublishableProperty, AdsPropertyWriter> writers = null;
            private Lock lock = new ReentrantLock();

            public void initialize() {
                lock.lock();
            }

            public void reset() {
                if (writers != null) {
                    writers.clear();
                }
                lock.unlock();
            }

            public AdsPropertyWriter getInstance(final IModelPublishableProperty property, final UsagePurpose usagePurpose) {
                synchronized (this) {
                    AdsPropertyWriter w = null;
                    if (writers != null) {
                        w = writers.get(property);
                    }
                    if (w == null) {
                        AdsPropertyPresentationPropertyDef pp = AdsPropertyPresentationPropertyDef.Factory.newTemporaryInstance(property);
                        w = new PropertyPresentationPropertyWriter(pp.getJavaSourceSupport(), pp, usagePurpose);
                        if (writers == null) {
                            writers = new HashMap<>();
                            writers.put(property, w);
                        }
                    }
                    return w;
                }
            }
        }

        private Factory() {
            super();
        }

        @SuppressWarnings("unchecked")
        public static AdsPropertyWriter newInstance(final JavaSourceSupport support, final AdsPropertyDef property, final UsagePurpose usagePurpose) {
            if (usagePurpose.getEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                if (property.getOwnerClass().getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {//for dynamic property presentation resolution
                    return ppCache.getInstance(property, usagePurpose);
                }
            }

            switch (property.getNature()) {
                case PROPERTY_PRESENTATION:
                    return new PropertyPresentationPropertyWriter(support, (AdsPropertyPresentationPropertyDef) property, usagePurpose);
                case DYNAMIC:
                    if (property.getAccessFlags().isAbstract()) {
                        return new AdsAbstractPropertyWriter(support, property, usagePurpose);
                    }
                case EVENT_CODE:
                case SQL_CLASS_PARAMETER:
                //case FORM_PROPERTY:
                case GROUP_PROPERTY:
                    return new NonPersystentPropertyWriter(support, property, usagePurpose);
                case USER:
                    return new PersistentPropertyWriter.UserPropertyWriter(support, property, usagePurpose);
                case DETAIL_PROP:
                case EXPRESSION:
                case INNATE:
                    return new PersistentPropertyWriter.RegularPropertyWriter(support, property, usagePurpose);
                case FIELD:
                case FIELD_REF:
                    return new FieldPropertyWriter(support, (AdsFieldPropertyDef) property, usagePurpose);
                case PARENT_PROP:
                    return new PersistentPropertyWriter.ParentPropertyWriter(support, (AdsParentPropertyDef) property, usagePurpose);
                default:
                    throw new DefinitionError("Unsupported property type for java source generating", property);
            }
        }
    }
    protected final AdsClassDef ownerClass;
    protected final char[] propId;
    private CodeWriter typeWriter = null;
    private CodeWriter accessWriter;
    private AdsType resolvedType;
    private CodeWriter ownerClassTypeWriter2;
    private boolean isInitializerWritten;
    private boolean isRefine;
    private AdsPropertyDef rootProp;

    public AdsPropertyDef getProperty() {
        return def;
    }

    protected AdsPropertyWriter(final JavaSourceSupport support, final T property, final UsagePurpose usagePurpose) {
        super(support, property, usagePurpose);
        this.ownerClass = property.getOwnerClass();
        this.propId = property.getId().toCharArray();

        rootProp = findRootBaseProperty(def);
        isInitializerWritten = rootProp != def;
        isRefine = calcRefine(def);
    }

    protected final AdsPropertyDef findRootBaseProperty(AdsPropertyDef currProp) {
        if (currProp instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) currProp).isTemporary()) {
            return currProp;
        }
        SearchResult<AdsPropertyDef> overriddenResult = currProp.getHierarchy().findOverridden();
        while (!overriddenResult.isEmpty()) {
            final AdsPropertyDef curr = overriddenResult.get();
            if (!curr.getAccessFlags().isAbstract()) {
                return curr;
            }
            overriddenResult = curr.getHierarchy().findOverridden();
        }
        return currProp;
    }

    protected final AdsPropertyDef getRootProperty() {
        return rootProp;
    }

    private boolean equalsPropertyType(AdsPropertyDef curr, AdsPropertyDef condidate) {
        return AdsTypeDeclaration.equals(curr, curr.getValue().getType(), condidate.getValue().getType());
    }

    private boolean calcRefine(AdsPropertyDef currProp) {
        if (!canRefine()) {
            return false;
        }

        SearchResult<AdsPropertyDef> overriddenResult = currProp.getHierarchy().findOverridden();
        while (!overriddenResult.isEmpty()) {
            final AdsPropertyDef curr = overriddenResult.get();
            if (!equalsPropertyType(curr, def)) {
                return true;
            }
            overriddenResult = curr.getHierarchy().findOverridden();
        }
        return false;
    }

    protected final boolean isRefine() {
        return isRefine;
    }

    protected boolean canRefine() {
        if (this.usagePurpose.getEnvironment() == ERuntimeEnvironmentType.SERVER || this.usagePurpose.getEnvironment() == ERuntimeEnvironmentType.COMMON) {
            return true;
        } else {
            return def.getNature() == EPropNature.DYNAMIC;
        }
    }

    protected void printSetterParamVariable(final CodePrinter printer) {
        printer.print(TEXT_SETTER_PARAM_VARIABLE_NAME);
    }

    protected final CodeWriter getTypeWriter() {
        if (typeWriter == null) {
            this.typeWriter = getCodeWriter(def.getValue().getTypeForCodeWriter(), def);
        }
        return typeWriter;
    }

    protected final CodeWriter getOwnerClassTypeWriter() {
        if (this.ownerClassTypeWriter2 == null) {
            this.ownerClassTypeWriter2 = ownerClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose);
        }
        return this.ownerClassTypeWriter2;
    }

    protected final AdsType getResolvedType() {
        if (this.resolvedType == null) {
            this.resolvedType = def.getValue().getType().resolve(getSupport().getCurrentRoot()).get();
        }
        return this.resolvedType;
    }

    protected final CodeWriter getAccessWriter() {
        if (accessWriter == null) {
//            if (def.getOwnerDefinition() instanceof AdsPropertyDef) {
//                this.accessWriter = ((AdsPropertyDef) def.getOwnerDefinition()).getAccessFlags().getJavaSourceSupport().getCodeWriter(usagePurpose);
//            } else {
            this.accessWriter = getAccessWriter(def.getAccessFlags());
            //}
        }
        return accessWriter;
    }

    protected final CodeWriter getAccessWriter(AdsAccessFlags accessFlags) {
        return accessFlags.getJavaSourceSupport().getCodeWriter(usagePurpose);
    }
    private Getter getterInstance;
    private boolean noGetter = false;

    private Getter findGetter() {
        if (noGetter) {
            return null;
        }
        if (getterInstance == null) {
            EScope scope;
            if (def.getOwnerDef() instanceof AdsPropertyDef) {//def is temporary property presentation
                scope = EScope.LOCAL;
            } else {
                scope = EScope.LOCAL_AND_OVERWRITE;
            }
            getterInstance = def.findGetter(scope).get();
            if (getterInstance == null) {
                noGetter = true;
            }
        }
        return getterInstance;
    }
    private Setter setterInstance;
    private boolean noSetter = false;

    private Setter findSetter() {
        if (noSetter) {
            return null;
        }
        if (setterInstance == null) {
            EScope scope;
            if (def.getOwnerDef() instanceof AdsPropertyDef) {//def is temporary property presentation
                scope = EScope.LOCAL;
            } else {
                scope = EScope.LOCAL_AND_OVERWRITE;
            }
            setterInstance = def.findSetter(scope).get();
            if (setterInstance == null) {
                noSetter = true;
            }
        }
        return setterInstance;
    }

    protected boolean writePropertyIdRef() {
        return !def.getOwnerClass().isInner();
    }

    protected void writePropertyIdRef(CodePrinter printer) {
        writePropertyIdRef(printer, def.getId());
    }

    protected static void writePropertyIdRef(CodePrinter printer, Id propId) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private static final ");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.printSpace();
        WriterUtils.writeAutoVariable(printer, propId.toCharArray());
        printer.print(" = ");
        WriterUtils.writeIdUsage(printer, propId);
        printer.printlnSemicolon();
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }

    @Override
    public final boolean writeExecutable(final CodePrinter printer) {

        if (getProperty() instanceof AdsTransparentPropertyDef) {
            return true;
        }

        if (writePropertyIdRef()) {
            writePropertyIdRef(printer);
        }

        final AdsPropertyDef ovr = findOverriddenProp();
        final AdsPropertyDef.Getter getter = findGetter();
        final AdsPropertyDef.Setter setter = findSetter();

        if (ignoreOvrMode() || ovr == null) {

            if (isWriteInitialization()) {
                if (!writeInitialization(printer)) {
                    return false;
                }
            }

            if (isWriteHidden()) {
                WriterUtils.enterHumanUnreadableBlock(printer);
                writeHiddenStdGetter(printer);
                writeHiddenStdSetter(printer);
                WriterUtils.leaveHumanUnreadableBlock(printer);
            }

            writeGetter(printer);
            writeSetter(printer);

            // генерация всех перегрузок сеттера
            if (!def.isConst() && !def.getAccessFlags().isAbstract()) {
                writeSetterOverloads(printer);
            }

            if (!writeFinalization(printer)) {
                return false;
            }
            return true;//writeAccessorCode(printer);
        } else {
            if (getter != null) {
                writeGetter(printer);
            }
            if (setter != null || (ovr.isConst() && !def.isConst())) {
                writeSetter(printer);
            }

            return true;
        }
    }

    protected boolean isWriteInitialization() {
        return isWriteHidden();
    }

    protected boolean writeSetterOverloads(final CodePrinter printer) {
        if (def instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) def).isTemporary()) {
            return true;
        }
        SearchResult<AdsPropertyDef> overriddenResult = def.getHierarchy().findOverridden();
        while (!overriddenResult.isEmpty()) {
            final AdsPropertyDef curr = overriddenResult.get();
            overriddenResult = curr.getHierarchy().findOverridden();

            if (equalsPropertyType(curr, def)) {
                continue;
            }

            writeSetterOverload(curr, printer);
            if (curr == rootProp) {
                break;
            }
        }

        return true;
    }

    protected boolean writeSetterOverload(AdsPropertyDef prop, final CodePrinter printer) {

        writeCustomMarker(printer, "set");
        printer.println(JavaSourceSupport.ANNOTATION_OVERRIDE);
        writeSetterAnnotations(printer);

        writeSetterAccessFlags(printer);
        printer.print("final ");
        printer.printSpace();
        printer.print(TEXT_SETTER_PREFIX);
        writePropertyName(printer, true);
        printer.print('(');

        getCodeWriter(prop.getValue().getType(), def).writeCode(printer);
        printer.enterBlock(1);
        printer.println(TEXT_SETTER_SUFFIX);

        printer.print("if (val != null && !(val instanceof ");
        getCodeWriter(def.getValue().getType().toRawType(), def).writeCode(printer);
        printer.println(")) {");
        printer.enterBlock();
        printer.println("throw new java.lang.ClassCastException(\"Property's type was overridden\");");
        printer.leaveBlock();
        printer.println("}");

        printer.print("set");
        writePropertyName(printer, true);
        printer.print("((");
        getTypeWriter().writeCode(printer);
        printer.println(")val);");

        printer.leaveBlock(1);
        printer.println('}');

        return true;
    }

    protected boolean isWriteHidden() {
        final AdsPropertyDef over = findOverriddenProp();
        return !isInitializerWritten && (over == null || over.getAccessFlags().isAbstract());
    }

    protected boolean ignoreOvrMode() {
        return isRefine();
    }

    void writeGetterAccessFlags(CodePrinter printer) {
        if (getProperty().isSetReadAccess()) {
            final AdsAccessFlags accessFlags = AdsAccessFlags.Factory.newCopy(
                    getProperty(), getProperty().getAccessFlags(), getProperty().getReadAccess());

            getAccessWriter(accessFlags).writeCode(printer);
        } else {
            getAccessWriter().writeCode(printer);
        }
    }

    void writeGetter(final CodePrinter printer) {
        final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
        RadixObjectLocator.RadixObjectData marker = null;
        if (locator != null) {
            RadixObject markerSrc = findGetter();
            if (markerSrc == null) {
                markerSrc = def;
            }
            marker = locator.start(markerSrc);
        }
        if (!def.isDeprecated() && def instanceof AdsPropertyPresentationPropertyDef) {
            IModelPublishableProperty sp = ((AdsPropertyPresentationPropertyDef) def).findServerSideProperty();
            if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
                printer.println("@Deprecated");
            }
        }
        writeCustomMarker(printer, "get");
        writeGetterAnnotations(printer);
        writeGetterAccessFlags(printer);

        printer.printSpace();
        getTypeWriter().writeCode(printer);
        printer.printSpace();
        printer.print(TEXT_GET);
        writePropertyName(printer, true);
        printer.enterBlock(1);
        printer.println(TEXT_GETTER_SUFFIX);
        if (marker != null) {
            marker.commit();
        }
        writeGetterBody(printer);
        printer.leaveBlock(1);
        printer.println('}');
    }

    void writeSetterAccessFlags(CodePrinter printer) {
        if (getProperty().isSetWriteAccess()) {
            final AdsAccessFlags accessFlags = AdsAccessFlags.Factory.newCopy(
                    getProperty(), getProperty().getAccessFlags(), getProperty().getWriteAccess());

            getAccessWriter(accessFlags).writeCode(printer);
        } else {
            getAccessWriter().writeCode(printer);
        }
    }

    void writeSetter(final CodePrinter printer) {

        if (def.isConst()) {
            return;
        }
        final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
        RadixObjectLocator.RadixObjectData marker = null;
        if (locator != null) {
            RadixObject markerSrc = findSetter();
            if (markerSrc == null) {
                markerSrc = def;
            }
            marker = locator.start(markerSrc);
        }
        if (!def.isDeprecated() && def instanceof AdsPropertyPresentationPropertyDef) {
            IModelPublishableProperty sp = ((AdsPropertyPresentationPropertyDef) def).findServerSideProperty();
            if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
                printer.println("@Deprecated");
            }
        }

        writeCustomMarker(printer, "set");
        writeSetterAnnotations(printer);
        writeSetterAccessFlags(printer);

        printer.printSpace();
        printer.print(TEXT_SETTER_PREFIX);
        writePropertyName(printer, true);
        printer.print('(');
        getTypeWriter().writeCode(printer);
        printer.enterBlock(1);
        printer.println(TEXT_SETTER_SUFFIX);
        if (marker != null) {
            marker.commit();
        }
        writeSetterBody(printer);
        printer.leaveBlock(1);
        printer.println('}');
    }

    void writeSetterAnnotations(CodePrinter printer) {
        final AdsPropertyDef overridden = getProperty().getHierarchy().findOverridden().get();
        if (getProperty().isOverride() && overridden != null && !overridden.isConst() && !isRefine()) {
            printer.println(JavaSourceSupport.ANNOTATION_OVERRIDE);
        }

        printer.println("@SuppressWarnings({\"unused\",\"unchecked\"})");
        writeMetaAnnotation(printer);
    }

    void writeGetterAnnotations(CodePrinter printer) {
        if (getProperty().isOverride()) {
            printer.println(JavaSourceSupport.ANNOTATION_OVERRIDE);
        }

        printer.println("@SuppressWarnings({\"unused\",\"unchecked\"})");
        writeMetaAnnotation(printer);
    }

    void writeMetaAnnotation(CodePrinter printer) {
        WriterUtils.writeMetaAnnotation(printer, def, true);
    }

    public boolean writeAbstractDeclaration(CodePrinter printer) {
        //DO NOTHINT
        return true;
    }

    public boolean writeConcreteDeclaration(CodePrinter printer) {
        //DO NOTHINT
        return true;
    }

    private void writeGetterBody(final CodePrinter printer) {
        final Getter getter = findGetter();
        boolean written = false;
        if (getter != null) {
            Jml src = getter.getSource(usagePurpose.getEnvironment());
            if (src != null) {
                try {

                    writeCustomMarker(printer, src.getName());
                    printer.enterCodeSection(getter.getLocationDescriptor(usagePurpose.getEnvironment()));
                    WriterUtils.writeProfilerInitialization(printer, getter);
                    writeCode(printer, src);
                    WriterUtils.writeProfilerFinalization(printer, getter);
                } finally {
                    printer.leaveCodeSection();
                }
                printer.println();
                written = true;
            }
        }
        if (!written) {
            printer.print("return ");
            if (isRefine()) {
                printer.print("(");
                getTypeWriter().writeCode(printer);
                printer.print(")");
            }
            AdsPropertyDef ovr = findOverriddenProp();
            if (ovr != null && def.getNature() == EPropNature.PROPERTY_PRESENTATION) {
                printer.print("super.");
                writePropertyName(printer);
            } else {
                writeInternalInvocation(printer);
            }
            printer.printlnSemicolon();
        }
    }

    private void writeSetterBody(final CodePrinter printer) {
        final Setter setter = findSetter();
        boolean written = false;
        if (setter != null) {
            Jml src = setter.getSource(usagePurpose.getEnvironment());
            if (src != null) {
                try {
                    writeCustomMarker(printer, src.getName());
                    printer.enterCodeSection(setter.getLocationDescriptor(usagePurpose.getEnvironment()));
                    WriterUtils.writeProfilerInitialization(printer, setter);
                    writeCode(printer, src);
                    WriterUtils.writeProfilerFinalization(printer, setter);
                } finally {
                    printer.leaveCodeSection();
                }
                printer.println();
                written = true;
            }
        }
        if (!written) {
            //writeStdSetterBody(printer);
            AdsPropertyDef ovr = findOverriddenProp();
            if (ovr != null && def.getNature() == EPropNature.PROPERTY_PRESENTATION) {
                printer.print("super.");
                writePropertyName(printer);
            } else {
                writeInternalInvocation(printer);
            }

            printer.println(" = val;");
        }
    }

    private void writeHiddenStdGetter(final CodePrinter printer) {
        writeCustomMarker(printer, "get");
        printer.println("@SuppressWarnings(\"unused\")");
        if (def.isDeprecated()) {
            printer.println("@Deprecated");
        } else if (def instanceof AdsPropertyPresentationPropertyDef) {
            IModelPublishableProperty sp = ((AdsPropertyPresentationPropertyDef) def).findServerSideProperty();
            if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
                printer.println("@Deprecated");
            }
        }

        if (def.getAccessFlags().isStatic()) {
            printer.print(TEXT_HIDDEN_STATIC_ACC_PREFIX);
        } else {
            printer.print(TEXT_HIDDEN_ACC_PREFIX);
        }
        getTypeWriter().writeCode(printer);
        printer.printSpace();
        printer.print(TEXT_GET);
        writePropertyName(printer, true);
        printer.print(TEXT_BAKS);
        printer.println(TEXT_GETTER_SUFFIX);
        printer.enterBlock();
        writeStdGetterBody(printer);
        printer.leaveBlock();
        printer.println('}');
    }

    private boolean writeHiddenStdSetter(final CodePrinter printer) {
        if (def.isConst()) {
            if (def.getNature() == EPropNature.FIELD || def.getNature() == EPropNature.FIELD_REF || def.getNature() == EPropNature.PARENT_PROP) {
                return true;
            }
        }
        writeCustomMarker(printer, "set");
        printer.println("@SuppressWarnings(\"unused\")");
        if (def.isDeprecated()) {
            printer.println("@Deprecated");
        } else if (def instanceof AdsPropertyPresentationPropertyDef) {
            IModelPublishableProperty sp = ((AdsPropertyPresentationPropertyDef) def).findServerSideProperty();
            if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
                printer.println("@Deprecated");
            }
        }
        if (def.getAccessFlags().isStatic()) {
            printer.print(TEXT_HIDDEN_STATIC_ACC_PREFIX);
        } else {
            printer.print(TEXT_HIDDEN_ACC_PREFIX);
        }
        printer.print(TEXT_SETTER_PREFIX);
        writePropertyName(printer, true);
        printer.print(TEXT_BAKS);
        printer.print('(');
        if (!getTypeWriter().writeCode(printer)) {
            return false;
        }
        printer.println(TEXT_SETTER_SUFFIX);
        printer.enterBlock();
        writeStdSetterBody(printer);
        printer.leaveBlock();
        printer.println('}');
        return true;
    }

    protected final boolean writeEnumBasedTypeValueConversion(final CodePrinter printer, final AdsType type, final CodeWriter tw, final String srcName, final String recvName, final boolean fullCheck) {
        if (!tw.writeCode(printer)) {
            return false;
        }
        printer.printSpace();
        printer.print(recvName);
        printer.print(" = ");

        if (fullCheck) {
            printer.print(srcName);
            printer.print(" == null ? null : (");
            printer.print(srcName);
            printer.print(" instanceof ");
            tw.writeCode(printer);
            printer.print(" ? (");
            tw.writeCode(printer);
            printer.print(')');
            printer.print(srcName);
            printer.print(" : ");
        }

        EValType simpleTypeId;
        if (type instanceof AdsEnumType.Array) {
            printer.print("new ");
            tw.writeCode(printer);
            printer.print("((");
            simpleTypeId = ((AdsEnumType) type).getSource().getItemType().getArrayType();
        } else {
            tw.writeCode(printer);
            printer.print(".getForValue((");
            final AdsEnumType enumType = (AdsEnumType) type;
            simpleTypeId = enumType.getSource().getItemType();
        }

        if (simpleTypeId == EValType.INT) {
            printer.print("(");
        }

        RadixType.Factory.newInstance(simpleTypeId).getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
        printer.print(")");
        printer.print("org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(");
        printer.print(srcName);
        printer.printComma();
        WriterUtils.writeEnumFieldInvocation(printer, simpleTypeId);
        printer.print("))");

        if (simpleTypeId == EValType.INT) {
            printer.print(".longValue())");
        }

        if (fullCheck) {
            printer.print(')');
        }

        printer.println(";");
        return true;
    }

    protected final boolean writeXmlBasedTypeValueConversion(final CodePrinter printer, final AdsType type, final CodeWriter tw, final String srcName, final String recvName) {
        if (!tw.writeCode(printer)) {
            return false;
        }
        printer.printSpace();
        printer.print(recvName);

        printer.print(" = ");
        printer.print(srcName);
        printer.print(" == null ? null : (");
        tw.writeCode(printer);
        printer.print(')');
        printer.print(WriterUtils.XML_OBJECT_PROCESSOR_CLASS_NAME);
        printer.print(".castToXmlClass(");
        if (def.getAccessFlags().isStatic()) {
            this.getOwnerClassTypeWriter().writeCode(printer);
            printer.print(".class.getClassLoader()");
        } else {
            printer.print("getClass().getClassLoader()");
        }
        printer.printComma();
        printer.print("(org.apache.xmlbeans.XmlObject)");
        printer.print(srcName);
        printer.printComma();
        tw.writeCode(printer);
        printer.println(".class);");
        return true;
    }

    /**
     * Should call printer.leaveBlock()before final string is printed
     */
    protected boolean writeStdGetterBody(final CodePrinter printer) {
        final AdsType type = getResolvedType();
        final CodeWriter tw = getTypeWriter();
        if (type instanceof AdsEnumType) {
            printer.print("Object dummy = ");
            writeGetterInvocation(printer);
            printer.printlnSemicolon();
            printer.print("if(dummy == null){\n\treturn null;\n}else if(dummy instanceof ");
            tw.writeCode(printer);
            printer.enterBlock();
            printer.print("){\nreturn (");
            tw.writeCode(printer);
            printer.println(")dummy;");
            printer.leaveBlock();
            printer.println("}else{");
            printer.enterBlock();

            if (!writeEnumBasedTypeValueConversion(printer, type, tw, "dummy", "dummy2", false)) {
                return false;
            }

            switch (def.getNature()) {
                case INNATE:
                case USER:
                case DETAIL_PROP:
                case EXPRESSION:
                    printer.print("refinePropVal(");
                    WriterUtils.writeAutoVariable(printer, propId);
                    printer.println(",dummy2);");
                    break;
                case PROPERTY_PRESENTATION:
                    printer.print("refineValue(");
                    printer.println("dummy2);");
                    break;
                default:
                    writeSetterInvocation(printer, "dummy2");
                    printer.printlnSemicolon();
            }
            printer.println("return dummy2;");
            printer.leaveBlock();
            printer.println("}");
        } else {
            printer.print("Object result = ");
            writeGetterInvocation(printer);
            printer.printlnSemicolon();
            if (!writeExtendedValueConversionAfterGetFromSystemHandler("result", printer)) {
                return false;
            }
            printer.print("return (");
            tw.writeCode(printer);
            printer.println(")result;");
        }
        return true;
    }

    protected boolean isIdRequiredSystemValueGet() {
        return true;
    }

    private void writeGetterInvocation(final CodePrinter printer) {
        printer.print("get");
        writeAccessMethodName(printer);
        printer.print("(");
        if (isIdRequiredSystemValueGet()) {
            WriterUtils.writeAutoVariable(printer, propId);
        }
        printer.print(")");
    }

    private void writeSetterInvocation(final CodePrinter printer, String varName) {
        printer.print("set");
        writeAccessMethodName(printer);
        printer.print("(");
        if (isIdRequiredSystemValueSet()) {
            WriterUtils.writeAutoVariable(printer, propId);
            printer.printComma();
        }
        printer.print(varName);
        printer.print(")");
    }

    protected boolean isIdRequiredSystemValueSet() {
        return true;
    }
    private AdsPropertyDef ovr$;
    private boolean noOvr$ = false;

    protected AdsPropertyDef findOverriddenProp() {
        synchronized (this) {
            if (noOvr$) {
                return null;
            }
            if (ovr$ == null) {
                if ((def.getContainer() instanceof IModelPublishableProperty)) {
                    noOvr$ = true;
                } else {
                    ovr$ = def.getHierarchy().findOverridden().get();
                    if (ovr$ == null) {
                        noOvr$ = true;
                    }
                }
            }
            return ovr$;
        }
    }

    protected boolean writeStdSetterBody(final CodePrinter printer) {
        boolean useExt = isUseExtendedValueConversionBeforePassToSystemHandler();
        if (useExt) {
            printer.println("Object result = val;");
        }
        if (!writeExtendedValueConversionBeforePassToSystemHandler("result", printer)) {
            return false;
        }
        printer.print("set");
        writeAccessMethodName(printer);
        printer.print("(");
        if (isIdRequiredSystemValueSet()) {
            WriterUtils.writeAutoVariable(printer, propId);
            printer.printComma();
        }
        if (useExt) {
            printer.print("result)");
        } else {
            printer.print("val)");
        }
        printer.printlnSemicolon();
        return true;
    }

    protected boolean isUseExtendedValueConversionBeforePassToSystemHandler() {
        return false;
    }

    protected boolean writeExtendedValueConversionBeforePassToSystemHandler(String varName, CodePrinter printer) {
        return true;
    }

    protected boolean writeExtendedValueConversionAfterGetFromSystemHandler(String varName, CodePrinter printer) {
        return true;
    }

    protected boolean writeInitialization(CodePrinter printer) {
        return true;
    }

    protected boolean writeFinalization(CodePrinter printer) {
        return true;
    }

    protected abstract void writeAccessMethodName(CodePrinter printer);

    protected void writePropertyName(final CodePrinter printer) {
        writePropertyName(printer, false);
    }
    
    protected void writePropertyName(final CodePrinter printer, boolean capitalize) {
        printer.print(JavaSourceSupport.getName(getProperty(), printer instanceof IHumanReadablePrinter, capitalize));
    }
    
    public static final String READ_ACCESSOR_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor";
    public static final String WRITE_ACCESSOR_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor";
    public static final String READ_WRITE_ACCESSOR_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.IRadPropReadWriteAccessor";
    public static final String PROP_ACCESSOR_PROVIDER_INTERFACE = "org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider";
    public static final String GET_READ_PROP_ACCESSOR_METHOD_NAME = "getReadAccessor";
    public static final String GET_READ_WRITE_PROP_ACCESSOR_METHOD_NAME = "getReadWriteAccessor";
    public static final String R_ACCESSOR_INSTANCE = "org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE";
    public static final String RW_ACCESSOR_INSTANCE = "org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE";

    public boolean writeAccessorGetter(final CodePrinter printer) {

        printer.print("if(propId == ");

        if (writePropertyIdRef()) {
            WriterUtils.writeAutoVariable(printer, propId);
        } else {
            WriterUtils.writeIdUsage(printer, def.getId());
        }

        printer.print(") return ");
        if (def.getAccessFlags().isStatic()) {
            if (!getOwnerClassTypeWriter().writeCode(printer)) {
                return false;
            }
        } else {
            printer.print("((");
            if (!getOwnerClassTypeWriter().writeCode(printer)) {
                return false;
            }
            printer.print(")owner)");
        }
        printer.print(".get");
        printer.print(def.getId());
        // printer.leaveBlock(1);
        printer.println("();");
        return true;
    }

    public boolean writeAccessorSetter(final CodePrinter printer) {
        printer.print("if(propId == ");
        if (writePropertyIdRef()) {
            WriterUtils.writeAutoVariable(printer, propId);
        } else {
            WriterUtils.writeIdUsage(printer, def.getId());
        }

        printer.println("){");
        printer.enterBlock();

        final CodeWriter tw = getTypeWriter();

        // tw.writeCode(printer);
        if (!writeVarConversionBeforeSetToStringBuffer(printer)) {
            return false;
        }

        if (def.getAccessFlags().isStatic()) {
            if (!getOwnerClassTypeWriter().writeCode(printer)) {
                return false;
            }
        } else {
            printer.print("((");
            if (!getOwnerClassTypeWriter().writeCode(printer)) {
                return false;
            }
            printer.print(")owner)");
        }
        printer.print(".set");
        printer.print(def.getId());
        //printer.print('(');

        printer.print("(dummy)");
        //printer.print(')');
        printer.printlnSemicolon();
        printer.println("return;");
        printer.leaveBlock();
        printer.println("}");
        return true;
    }

    private boolean writeAccessorCode(final CodePrinter printer) {
        switch (def.getNature()) {
            case PROPERTY_PRESENTATION:
                return true;
            case DYNAMIC:
            case EVENT_CODE:
                if (def.getUsageEnvironment() != ERuntimeEnvironmentType.SERVER) {
                    return true;
                } else {
                    break;
                }
            default:
                break;
        }
        if (def.getOwnerClass().isInner()) {
            return true;
        }

        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.println(PROPERTY_ACCESSOR_WARNING);
        printer.print("public static final class Access_");
        printer.print(def.getId());
        printer.print(" implements ");
        printer.print(READ_ACCESSOR_CLASS_NAME);
        if (!def.isConst()) {
            printer.printComma();
            printer.print(WRITE_ACCESSOR_CLASS_NAME);
        }
        printer.print('{');

        final CodeWriter ownerClassTypeWriter = getOwnerClassTypeWriter();
        printer.enterBlock(1);
        printer.println();
        printer.println("@SuppressWarnings(\"unchecked\")");
        //printer.enterBlock(1);
        printer.println("public Object get(Object owner){");
        printer.enterBlock();
        printer.print("return ");
        if (def.getAccessFlags().isStatic()) {
            if (!ownerClassTypeWriter.writeCode(printer)) {
                return false;
            }
        } else {
            printer.print("((");
            if (!ownerClassTypeWriter.writeCode(printer)) {
                return false;
            }
            printer.print(")owner)");
        }
        printer.print(".get");
        printer.print(def.getId());
        // printer.leaveBlock(1);
        printer.println("();");
        printer.leaveBlock(1);
        printer.println("}");
        printer.leaveBlock();
        if (!def.isConst()) {
            printer.enterBlock(1);
            printer.println();
            printer.println("@SuppressWarnings(\"unchecked\")");
            //printer.enterBlock(1);
            printer.println("public void set(Object owner,Object x) {");
            printer.enterBlock();

            final CodeWriter tw = getTypeWriter();

            // tw.writeCode(printer);
            if (!writeVarConversionBeforeSetToStringBuffer(printer)) {
                return false;
            }

            if (def.getAccessFlags().isStatic()) {
                if (!ownerClassTypeWriter.writeCode(printer)) {
                    return false;
                }
            } else {
                printer.print("((");
                if (!ownerClassTypeWriter.writeCode(printer)) {
                    return false;
                }
                printer.print(")owner)");
            }
            printer.print(".set");
            printer.print(def.getId());
            //printer.print('(');
            printer.leaveBlock();
            printer.print("(dummy)");
            //printer.print(')');
            printer.printlnSemicolon();
            printer.println("}");
            printer.leaveBlock(1);

        }
        printer.println('}');
        WriterUtils.leaveHumanUnreadableBlock(printer);
        return true;
    }

    protected boolean writeVarConversionBeforeSetToStringBuffer(final CodePrinter printer) {
        final AdsType type = getResolvedType();
        final CodeWriter tw = getTypeWriter();
        if (type instanceof XmlType) {
            if (!writeXmlBasedTypeValueConversion(printer, type, tw, "x", "dummy")) {
                return false;
            }
//            printer.print("(x == null ? null : ");
//            printer.print("(");
//            tw.writeCode(printer);
//            printer.print(")");
//            printer.print(WriterUtils.RADIX_XML_OBJECT_PROCESSOR_CLASS_NAME);
//
//            printer.print(".castToXmlClass(");
//            if (def.getAccessFlags().isStatic()) {
//                getOwnerClassTypeWriter().writeUsage(printer);
//                printer.print(".class.getClassLoader()");
//            } else {
//                printer.print("getClass().getClassLoader()");
//            }
//            //  WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
//            printer.print(",(org.apache.xmlbeans.XmlObject)x,");
//            tw.writeCode(printer);
//            printer.print(".class))");
        } else if (type instanceof AdsEnumType) {
            if (!writeEnumBasedTypeValueConversion(printer, type, tw, "x", "dummy", true)) {
                return false;
            }
//            printer.print("( x instanceof ");
//            tw.writeCode(printer);
//            printer.print(" ? (");
//            tw.writeCode(printer);
//            printer.print(" )x : ");
//
//            EValType simpleTypeId;
//            if (type instanceof AdsEnumType.Array) {
//                printer.print("new ");
//                tw.writeCode(printer);
//                printer.print("((");
//                simpleTypeId = ((AdsEnumType) type).getSource().getItemType().getArrayType();
//            } else {
//                tw.writeCode(printer);
//                printer.print(".getForValue((");
//                simpleTypeId = ((AdsEnumType) type).getSource().getItemType();
//            }
//            RadixType.Factory.newInstance(simpleTypeId).getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
//            printer.println(")x))");
        } else {
            if (!tw.writeCode(printer)) {
                return false;
            }
            printer.print(" dummy = ");

            if (!def.getValue().getType().isArray() && type instanceof JavaType) {
                printer.print('(');
                ((JavaType.PrimitiveTypeJavaSourceSupport.PrimitiveTypeCodeWriter) type.getJavaSourceSupport().getCodeWriter(usagePurpose)).writeConversionFromObjTypeCode(printer, "x");
                printer.print(')');
            } else {
                printer.print("((");
                tw.writeCode(printer);
                printer.print(")x)");
            }
            printer.printlnSemicolon();
        }
        return true;
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        EValType typeId = def.getValue().getType().getTypeId();
        if (def.getValue().getType().getArrayDimensionCount() > 0) {
            typeId = EValType.JAVA_CLASS;
        }

        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                final EPropNature nature = def.getNature();
                printer.print("new ");
                boolean isParentRef = false;
                if (nature == DYNAMIC) {
                    AdsType type = def.getValue().getType().resolve(def).get();
                    if (type instanceof AdsClassType.EntityObjectType) {
                        isParentRef = true;
                    }
                }

                /*
                 * if (typeId == EValType.XML && def.getNature() ==
                 * EPropNature.USER) { typeId = EValType.CLOB; }
                 */
                writeServerMetaClassName(printer, nature, typeId);
                printer.print('(');
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                Id titleId = null;
                if (def instanceof IAdsPresentableProperty) {
                    ServerPresentationSupport support = ((IAdsPresentableProperty) def).getPresentationSupport();
                    if (support != null) {
                        PropertyPresentation pps = support.getPresentation();
                        if (pps != null) {
                            titleId = pps.getTitleId();
                        }
                    }
                }
                WriterUtils.writeIdUsage(printer, titleId);
                printer.printComma();

                if (!((nature == EPropNature.INNATE || nature == EPropNature.DETAIL_PROP) && (typeId == EValType.PARENT_REF || typeId == EValType.ARR_REF)
                        || nature == EPropNature.PARENT_PROP)) {
                    WriterUtils.writeEnumFieldInvocation(printer, typeId);
                    printer.printComma();
                    if (nature == EPropNature.EXPRESSION || nature == EPropNature.FIELD) {
                        //by BAO RADIX-4482
                        final String dbType = ((DbPropertyValue) def.getValue()).getDbType();
//                                nature == EPropNature.EXPRESSION
//                                ? ((Dbp) def).getVgetDbType()
//                                : ((AdsFieldPropertyDef) def).getDbType();
                        printer.printStringLiteral(dbType);
                        printer.printComma();
                    }
                    if (!(typeId == EValType.PARENT_REF || isParentRef) && typeId != EValType.OBJECT && (typeId != EValType.ARR_REF || (typeId == EValType.ARR_REF && nature == EPropNature.FIELD))) {
                        if (getResolvedType() instanceof AdsEnumType) {
                            WriterUtils.writeIdUsage(printer, ((AdsEnumType) getResolvedType()).getSource().getId());
                        } else {
                            WriterUtils.writeNull(printer);
                        }
                        printer.printComma();
                    }

                }
                switch (nature) {
                    case DETAIL_PROP: {
                        switch (typeId) {
                            case PARENT_REF: {
                                final AdsDetailRefPropertyDef ref = (AdsDetailRefPropertyDef) def;
                                WriterUtils.writeIdUsage(printer, ref.getDetailReferenceInfo().getDetailReferenceId());
                                printer.printComma();
                                WriterUtils.writeIdUsage(printer, ref.getParentReferenceInfo().getParentReferenceId());
                                printer.printComma();
                                writeDescClassIdForRefPropMeta(printer);
                                writeValueInheritanceMeta(printer);
                            }

                            break;
                            default: {
                                final AdsDetailColumnPropertyDef prop = (AdsDetailColumnPropertyDef) def;
                                WriterUtils.writeIdUsage(printer, prop.getDetailReferenceInfo().getDetailReferenceId());
                                printer.printComma();
                                WriterUtils.writeIdUsage(printer, prop.getColumnInfo().getColumnId());
                                printer.printComma();
                                writeValueInheritanceMeta(printer);
                                writeInitValueMeta(printer);
                                if (typeId == EValType.ARR_REF) {
                                    writeDescClassIdForRefPropMeta(printer);
//                                    final AdsType type = def.getValue().getType().resolve(getSupport().getCurrentRoot());
//                                    if (type instanceof AdsClassType.EntityObjectType) {
//                                        WriterUtils.writeIdUsage(printer, ((AdsClassType.EntityObjectType) type).getSourceEntityId());
//                                    } else {
//                                        WriterUtils.writeNull(printer);
//                                    }
//                                    printer.printComma();
                                }

                            }
                            break;
                        }

                    }
                    break;
                    case PARENT_PROP:
                        final AdsParentPropertyDef pp = (AdsParentPropertyDef) def;
                        WriterUtils.writeIdArrayUsage(printer, pp.getParentInfo().getParentPath().getRefPropIds());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, pp.getParentInfo().getOriginalPropertyId());
                        printer.printComma();

                        break;
                    case DYNAMIC:
                    case EVENT_CODE:
                    case GROUP_PROPERTY:
                    case SQL_CLASS_PARAMETER:
                        switch (typeId) {
                            case PARENT_REF:
                            case ARR_REF: {
                                writeInitValueMeta(printer);
                                if (def.getValue().getType().getPath() != null) {
                                    WriterUtils.writeIdUsage(printer, def.getValue().getType().getPath().getTargetId());
                                } else {
                                    WriterUtils.writeNull(printer);
                                }
                                printer.printComma();
                            }

                            break;
                            default: {
                                writeInitValueMeta(printer);
                                if (isParentRef) {
                                    if (def.getValue().getType().getPath() != null) {
                                        WriterUtils.writeIdUsage(printer, def.getValue().getType().getPath().getTargetId());
                                    } else {
                                        WriterUtils.writeNull(printer);
                                    }
                                    printer.printComma();
                                }
                            }
                        }
                        break;

                    case EXPRESSION:
                        final AdsExpressionPropertyDef exp = (AdsExpressionPropertyDef) def;
                        writeValueInheritanceMeta(printer);
                        writeInitValueMeta(printer);
                        WriterUtils.writeSqmlAsXmlStr(printer, exp.getExpresssion());
                        printer.printComma();
                        printer.print(!exp.isInvisibleForArte());
                        printer.printComma();
                        break;

                    case FIELD_REF: {
                        final AdsFieldRefPropertyDef ref = (AdsFieldRefPropertyDef) def;
                        writeDescClassIdForRefPropMeta(printer);
                        DdsIndexDef usedIndex = ref.findUsedIndex();
                        if (usedIndex == null) {
                            return false;
                        }
                        if (!writeCursorRefMapMeta(printer, ref, usedIndex, true)) {
                            return false;
                        }
                        if (!writeCursorRefMapMeta(printer, ref, usedIndex, false)) {
                            return false;
                        }
                    }
                    break;
                    case FIELD: {
                        break;
                    }
                    case INNATE:
                        switch (typeId) {
                            case PARENT_REF: {
                                final AdsInnateRefPropertyDef ref = (AdsInnateRefPropertyDef) def;
                                WriterUtils.writeIdUsage(printer, ref.getParentReferenceInfo().getParentReferenceId());
                                printer.printComma();
                                writeDescClassIdForRefPropMeta(printer);
                                writeValueInheritanceMeta(printer);
                            }
                            break;
                            default:
                                writeValueInheritanceMeta(printer);
                                writeInitValueMeta(printer);
                                if (typeId == EValType.ARR_REF) {
                                    writeDescClassIdForRefPropMeta(printer);
                                }

                        }
                        break;
                    case USER:
                        writeValueInheritanceMeta(printer);
                        writeInitValueMeta(printer);
                        if (ownerClass instanceof AdsEntityObjectClassDef) {
                            WriterUtils.writeIdUsage(printer, ((AdsEntityObjectClassDef) ownerClass).getEntityId());
                        } else {
                            printer.printError();
                        }

                        printer.printComma();
                        switch (typeId) {
                            case PARENT_REF:
                            case OBJECT:
                            case ARR_REF:
                                writeDescClassIdForRefPropMeta(printer);
                                break;

                            default:
                        }
                        printer.print(((AdsUserPropertyDef) def).isAuditUpdate());
                        printer.printComma();
                        break;
                }

                if (def.getNature() == EPropNature.EXPRESSION && ((AdsExpressionPropertyDef) def).isInvisibleForArte()) {
                    WriterUtils.writeNull(printer);
                } else {
                    final AdsClassDef propOwnerClass;
                    //final AdsPropertyDef ovr = findOverriddenProp();
                    ///if (ovr != null) {
//                        propOwnerClass = ovr.getOwnerClass();
                    //                  } else {
                    propOwnerClass = def.getOwnerClass();
                    //                }
                    if (!propOwnerClass.isNested() && propOwnerClass.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                        if (def.isConst()) {
                            printer.print(R_ACCESSOR_INSTANCE);
                        } else {
                            printer.print(RW_ACCESSOR_INSTANCE);
                        }
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                }
                printer.print(")");
                return true;
            default:
                return super.writeMeta(printer);

        }
    }

    private void writeServerMetaClassName(final CodePrinter printer, final EPropNature nature, final EValType typeId) {
        printer.print(WriterUtils.CLASSES_META_SERVER_PACKAGE_NAME);
        printer.print('.');
        switch (nature) {
            case DETAIL_PROP: {
                switch (typeId) {
                    case PARENT_REF:
                        printer.print("RadDetailParentRefPropDef");
                        break;

                    case ARR_REF:
                        printer.print("RadDetailArrRefPropDef");
                        break;

                    default:

                        printer.print("RadDetailPropDef");
                }

            }
            break;
            case DYNAMIC:
            case EVENT_CODE:
            case GROUP_PROPERTY:
            case SQL_CLASS_PARAMETER: {
                switch (typeId) {
                    case PARENT_REF:
                    case ARR_REF:
                        printer.print("RadDynamicRefPropDef");
                        break;
                    default:
                        if (nature == DYNAMIC) {
                            AdsType type = def.getValue().getType().resolve(def).get();
                            if (type instanceof AdsClassType.EntityObjectType) {
                                printer.print("RadDynamicRefPropDef");
                            } else {
                                printer.print("RadDynamicPropDef");
                            }
                        } else {
                            printer.print("RadDynamicPropDef");
                        }
                }
            }
            break;
            case EXPRESSION:
                printer.print("RadSqmlPropDef");
                break;

            case FIELD:
                printer.print("RadCursorFieldPropDef");
                break;
            case FIELD_REF:
                printer.print("RadCursorRefFieldPropDef");
                break;
//            case FORM_PROPERTY:
//            case GROUP_PROPERTY:
//                switch (typeId) {
//                    case PARENT_REF:
//                    case ARR_REF:
//                        printer.print("RadFormRefPropDef");
//                        break;
//
//                    default:
//
//                        printer.print("RadFormPropDef");
//                }
//
//                break;
            case INNATE:
                switch (typeId) {
                    case PARENT_REF:
                        printer.print("RadInnateRefPropDef");
                        break;

                    case ARR_REF:
                        printer.print("RadInnateArrRefPropDef");
                        break;

                    default:

                        printer.print("RadInnatePropDef");
                }

                break;
            case PARENT_PROP:
                printer.print("RadParentPropDef");
                break;

            case USER:
                switch (typeId) {
                    case PARENT_REF:
                    case ARR_REF:
                    case OBJECT:
                        //case ARR_OBJECT:
                        printer.print("RadUserRefPropDef");
                        break;

                    default:

                        printer.print("RadUserPropDef");

                }

                break;
        }

    }
    private static final char[] VAL_INH_PATH_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath".toCharArray();

    private void writeValueInheritanceMeta(final CodePrinter printer) {
        final ValueInheritanceRules rules = def.getValueInheritanceRules();
        printer.print(rules.getInheritable());
        printer.printComma();
        WriterUtils.writeRadixValAsStr(printer, rules.getInheritanceMark());
        printer.printComma();

        new WriterUtils.SameObjectArrayWriter<ValueInheritanceRules.InheritancePath>(VAL_INH_PATH_CLASS_NAME) {
            @Override
            public void writeItemConstructorParams(final CodePrinter printer, final ValueInheritanceRules.InheritancePath path) {
                WriterUtils.writeIdArrayUsage(printer, path.getReferenceIds());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, path.getPropertyId());
            }
        }.write(printer, rules.getPathes());
        printer.printComma();
        if (rules.getInitializationPolicy() == null) {
            printer.println("null");
        } else {
            printer.print(EPropInitializationPolicy.class.getName() + "." + rules.getInitializationPolicy().name());
        }
        printer.printComma();
    }

    private void writeInitValueMeta(final CodePrinter printer) {
        WriterUtils.writeRadixDefaultValue(printer, def.getValue().getInitial());
        printer.printComma();
    }
    private static final char[] REF_MAP_ITEM_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.RadCursorRefFieldPropDef.RefMapItem".toCharArray();

    private boolean writeCursorRefMapMeta(final CodePrinter printer, final AdsFieldRefPropertyDef ref, final DdsIndexDef usedIndex, final boolean key) {
        final AdsFieldRefPropertyDef.FieldToColumnMap map = ref.getFieldToColumnMap();
        if (map == null || map.isEmpty()) {
            WriterUtils.writeNull(printer);
        } else {
            final DdsTableDef table = ref.findReferencedTable();
            if (table == null) {
                return false;
            }
            new WriterUtils.SameObjectArrayWriter<AdsFieldRefPropertyDef.RefMapItem>(REF_MAP_ITEM_CLASS_NAME) {
                @Override
                public void writeItemConstructorParams(final CodePrinter printer, final AdsFieldRefPropertyDef.RefMapItem item) {
                    WriterUtils.writeIdUsage(printer, item.getFieldId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, item.getColumnId());
                }
            }.write(printer, map.list(new IFilter<AdsFieldRefPropertyDef.RefMapItem>() {
                @Override
                public boolean isTarget(RefMapItem radixObject) {
                    DdsColumnDef column = table.getColumns().findById(radixObject.getColumnId(), EScope.ALL).get();
                    if (column == null) {
                        return false;
                    } else {
                        boolean isKeyColumn = false;
                        for (DdsIndexDef.ColumnInfo info : usedIndex.getColumnsInfo()) {
                            if (info.getColumnId() == column.getId()) {
                                isKeyColumn = true;
                                break;
                            }
                        }
                        return isKeyColumn == key;
                    }
                }
            }));
        }

        printer.printComma();
        return true;
    }

    private void writeDescClassIdForRefPropMeta(final CodePrinter printer) {
        final AdsType type = def.getValue().getType().resolve(getSupport().getCurrentRoot()).get();
        if (type instanceof AdsClassType.EntityObjectType) {
            final AdsEntityObjectClassDef clazz = ((AdsClassType.EntityObjectType) type).getSource();
            if (clazz != null) {
                WriterUtils.writeIdUsage(printer, clazz.getId());
            } else {
                WriterUtils.writeNull(printer);
            }

        } else {
            WriterUtils.writeNull(printer);
        }

        printer.printComma();
    }

//    private void writeDescEntityIdForRefPropMeta(final CodePrinter printer) {
//        final AdsType type = def.getValue().getType().resolve(getSupport().getCurrentRoot());
//        if (type instanceof AdsClassType.EntityObjectType) {
//            WriterUtils.writeIdUsage(printer, ((AdsClassType.EntityObjectType) type).getSourceEntityId());
//        } else {
//            WriterUtils.writeNull(printer);
//        }
//
//        printer.printComma();
//    }
//
    public void writeInternalInvocation(final CodePrinter printer) {
        writePropertyName(printer);
        if (!(printer instanceof IHumanReadablePrinter)) {
            printer.print(TEXT_BAKS);
        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        if (getProperty() instanceof AdsTransparentPropertyDef) {
            printer.print(((AdsTransparentPropertyDef) getProperty()).getPublishedPropertyName());
        } else {
            super.writeUsage(printer);
        }
    }
}
