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
package org.radixware.kernel.common.defs.ads.ui.generation;

import java.awt.Color;
import java.util.*;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Column;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Columns;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Row;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Rows;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.WidgetItem;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef.Types;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef.Widgets;
import org.radixware.kernel.common.defs.ads.ui.enums.ECursorShape;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.ESizeConstraint;
import org.radixware.kernel.common.defs.ads.ui.enums.ESizePolicy;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class AdsUIWriter extends AbstractDefinitionWriter<AdsUIDef> {
    //=======================================================================

    private static final String TEXT_CLASS = "class";
    private static final String TEXT_EXTENDS = "extends";
    private static final String TEXT_PUBLIC = "public";
    private static final String TEXT_PRIVATE = "private";
    private static final String TEXT_FINAL = "final";
    private static final String TEXT_VOID = "void";
    private static final String TEXT_MODEL = String.valueOf(WriterUtils.EXPLORER_MODEL_CLASS_NAME);
    private static final String TEXT_VIEW = "org.radixware.kernel.common.client.views.IView";
    private static final String TEXT_EDITOR_PAGE_DEF = "org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef";
    private static final String TEXT_PROPERTY_REF = "org.radixware.kernel.common.client.models.items.properties.PropertyRef";
    private static final String TEXT_PROPERTY_REFERENCE = "org.radixware.kernel.common.client.models.items.properties.PropertyReference";
    private static final String TEXT_THIS = "this";
    private static final EnumSet<EDefinitionIdPrefix> DIALOGS_WITH_CONTENT_WIDGET = EnumSet.of(
            EDefinitionIdPrefix.CUSTOM_SELECTOR,
            EDefinitionIdPrefix.CUSTOM_EDITOR,
            EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR,
            EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG);
    private static final String GUI_PATH = "com.trolltech.qt.gui.";
    private static final String CORE_PATH = "com.trolltech.qt.core.";
    //private static String SVG_PATH = "com.trolltech.qt.svg.";

    //=======================================================================
    private static void writeDefaultFontDeclaration(CodePrinter printer) {
        printer.println("final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();");
    }

    private static String getCoreClassName(String clazz) {
        return CORE_PATH + clazz;
    }

    private static AdsAbstractUIDef getCustomUI(AdsWidgetDef widget) {
        return AdsMetaInfo.getCustomUI(widget);
    }
    //=======================================================================
    private final WidgetsCodeGenerator gen = new WidgetsCodeGenerator();

    public AdsUIWriter(JavaSourceSupport support, AdsUIDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    //=======================================================================
    @Override
    protected boolean writeExecutable(CodePrinter printer) {
        //   ERuntimeEnvironmentType env = usagePurpose.getEnvironment();

        Definition userDef = null;
        if (def instanceof AdsCustomReportDialogDef) {
            AdsClassDef owner = ((AdsCustomReportDialogDef) def).getOwnerClass();
            if (owner instanceof AdsUserReportClassDef) {
                userDef = def;
            }
        }
        if (userDef != null) {
            WriterUtils.writeUserDefinitionHeader(printer, userDef);
        }

        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
//        writeExecutableHeader(printer);
        writeTypeDeclaration(printer);

        printer.println('{');
        printer.enterBlock();

        if(!writeExecutableBody(printer)){
            return false;
        }

        printer.leaveBlock();
        printer.println();
        printer.println('}');

        return true;
    }
    private static final String ABSTRACT_PROP_EDITOR_CLASS = "org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor";
    private static final String EXPLORER_DIALOG_BUTTON_BOX = "org.radixware.kernel.explorer.widgets.ExplorerDialogButtonBox";
    private static final String EXPLORER_TAB_WIDGET = "org.radixware.kernel.explorer.widgets.QExtTabWidget";
    // use only for code generation

    private static String getActualClass(String cls) {
        if (cls == null) {
            return null;
        }
        switch (cls) {
            case AdsMetaInfo.PROP_EDITOR_CLASS:
                return ABSTRACT_PROP_EDITOR_CLASS;
            case AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS:
                return EXPLORER_DIALOG_BUTTON_BOX;
            case AdsMetaInfo.TAB_WIDGET_CLASS:
                return EXPLORER_TAB_WIDGET;
        }
        return cls;
    }

    public static final boolean writeWidgetType(AdsUIDef def, AdsWidgetDef widget, UsagePurpose up, CodePrinter printer) {
        if (AdsUIUtil.isCustomWidget(widget)) {
            AdsAbstractUIDef customUI = getCustomUI(widget);
            return writeCustomType(printer, customUI, up);
        } else {
            String clazz = widget.getClassName();
            clazz = getActualClass(clazz);
            printer.print(getGuiClassNameStatic(def, clazz, printer instanceof  IHumanReadablePrinter));
        }
        return true;
    }

    private boolean writeExecutableBody(final CodePrinter printer) {
        // register default font
        writeDefaultFontDeclaration(printer);
        // register signals
        writeCustomSignals(printer);
        // register properties
        writeCustomProperties(printer);
        
        final List<AdsWidgetDef> failWidgets = new ArrayList<>();
        // register data
        def.getWidget().visit(new IVisitor() {
            @Override
            public void accept(RadixObject object) {
                if (object instanceof AdsWidgetDef) {
                    AdsWidgetDef widget = (AdsWidgetDef) object;
                    printer.print(TEXT_PUBLIC);
                    printer.printSpace();
                    if (!writeWidgetType(def, widget, usagePurpose, printer)){
                        failWidgets.add(widget);
                        return;
                    }
                    printer.printSpace();
                    printer.print(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter));
                    printer.printlnSemicolon();

                    printer.print(TEXT_PUBLIC);
                    printer.printSpace();
                    if (!writeWidgetType(def, widget, usagePurpose, printer)){
                        failWidgets.add(widget);
                        return;
                    }
                    printer.printSpace();
                    printer.print("get");
                    printer.print(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter, true));
                    printer.print("(){ return ");
                    printer.print(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter));
                    printer.println(";}");

                } else if (object instanceof AdsLayout) {
                    final AdsLayout layout = (AdsLayout) object;
                    if (layout.isSetName()) {
                        final String className = layout.getClassName();
                        final String name = layout.getName();
                        printer.print(TEXT_PUBLIC);
                        printer.printSpace();
                        printer.print(getGuiClassName(className));
                        printer.printSpace();
                        printer.print(name);
                        printer.printlnSemicolon();
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        if (!failWidgets.isEmpty()){
            return false;
        }
        // constructor
        writeConstructor(printer);

        // open
        printer.println(TEXT_PUBLIC + " " + TEXT_VOID + " open(" + TEXT_MODEL + " model) {");
        printer.enterBlock();

        // code
        printer.println("super.open(model);");
        writeWidget(printer, def.getWidget(), TEXT_THIS);

        Id id = def.getId();
        if (DIALOGS_WITH_CONTENT_WIDGET.contains(id.getPrefix())) {
            printer.println("opened.emit(" + TEXT_THIS + ".content);");
        } else {
            if (def instanceof AdsCustomSelectorDef) {
                printer.println("notifyOpened();");
            } else {
                printer.println("opened.emit(" + TEXT_THIS + ");");
            }
        }

        printer.leaveBlock();
        printer.println("}");

        AdsModelClassDef model = AdsUIUtil.getModelByUI(def, false);
        if (model != null) {
            AdsTypeDeclaration decl = AdsTypeDeclaration.Factory.newInstance(model);
            printer.print(TEXT_PUBLIC + " " + TEXT_FINAL + " ");
            writeUsage(printer, decl, def);
            printer.println(" getModel() {");
            printer.enterBlock();
            printer.print("return (");
            writeUsage(printer, decl, def);
            printer.println(") super.getModel();");
            printer.leaveBlock();
            printer.println("}");
        }
        
        return true;
    }

    private void writeTypeDeclaration(CodePrinter printer) {
        printer.println();
        if (def.isDeprecated()) {
            printer.println("@Deprecated");
        }
        WriterUtils.writeMetaAnnotation(printer, def, false);
        printer.print("public ");
        printer.print(TEXT_CLASS);
        printer.printSpace();
        writeUsage(printer);
        printer.printSpace();
        printer.print(TEXT_EXTENDS);
        printer.printSpace();
        printer.print(AdsUIUtil.getQtClassName(def.getWidget()));
        printer.printSpace();
    }

    private void writeConstructor(CodePrinter printer) {
        String clazz = AdsUIUtil.getQtClassName(def.getWidget());

        if (clazz.equals(AdsMetaInfo.CUSTOM_PROP_EDITOR)
                || clazz.equals(AdsMetaInfo.CUSTOM_DIALOG)) {

            printer.print(TEXT_PUBLIC + " ");
            writeUsage(printer);
            printer.print("(");
            printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
            printer.println(" userSession) {");

            printer.enterBlock();
            printer.print("super(userSession,");
            WriterUtils.writeIdUsage(printer, def.getId());
            printer.println(", null, null);");
            printer.leaveBlock();

            printer.println("}");
        }
        if (clazz.equals(AdsMetaInfo.CUSTOM_WIDGET)) {
            printer.print(TEXT_PUBLIC + " ");
            writeUsage(printer);
            printer.print('(');
            printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
            printer.print(" userSession,");
            printer.println(getGuiClassName("QWidget") + " owner) {");
            printer.enterBlock();
            printer.println("super(userSession,owner);");
            printer.leaveBlock();
            printer.println("}");

            printer.println(TEXT_PUBLIC + " org.radixware.kernel.common.types.Id getId() {");
            printer.enterBlock();
            printer.print("return ");
            WriterUtils.writeIdUsage(printer, def.getId());
            printer.println(";");
            printer.leaveBlock();
            printer.println("}");
        }
        if (clazz.equals(AdsMetaInfo.CUSTOM_FORM_DIALOG)
                || clazz.equals(AdsMetaInfo.CUSTOM_EDITOR)
                || clazz.equals(AdsMetaInfo.CUSTOM_REPORT_DIALOG)
                || clazz.equals(AdsMetaInfo.CUSTOM_FILTER_DIALOG)
                || clazz.equals(AdsMetaInfo.CUSTOM_SELECTOR)
                || clazz.equals(AdsMetaInfo.CUSTOM_PARAG_EDITOR)) {

            printer.print(TEXT_PUBLIC + " ");
            writeUsage(printer);
            printer.print('(');
            printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
            printer.println(" userSession) {");

            printer.enterBlock();
            printer.println("super(userSession);");
            printer.leaveBlock();

            printer.println("}");
        }
        if (clazz.equals(AdsMetaInfo.CUSTOM_EDITOR_PAGE)) {

            printer.print(TEXT_PUBLIC + " ");
            writeUsage(printer);
            printer.print('(');
            printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
            printer.println(" userSession,");
            printer.println(TEXT_VIEW + " parent, " + TEXT_EDITOR_PAGE_DEF + " page) {");

            printer.enterBlock();
            printer.println("super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);");
            printer.leaveBlock();

            printer.println("}");
        }
    }

    private void writeCustomSignals(CodePrinter printer) {
        if (!(def instanceof AdsCustomWidgetDef)) {
            return;
        }
        AdsCustomWidgetDef ui = (AdsCustomWidgetDef) def;
        for (AdsUISignalDef signal : ui.getSignals()) {
            printer.print(TEXT_PUBLIC + " " + TEXT_FINAL + " ");
            writeSignalType(printer, signal);
            printer.print(" ");
            printer.print(JavaSourceSupport.getName(signal, printer instanceof IHumanReadablePrinter));
            printer.print(" = new ");
            writeSignalType(printer, signal);
            printer.println("();");
        }
    }

    private void writeCustomProperties(CodePrinter printer) {
        if (!(def instanceof AdsCustomWidgetDef)) {
            return;
        }
        printer.println();
        AdsCustomWidgetDef ui = (AdsCustomWidgetDef) def;
        for (AdsUIProperty prop : ui.getProperties()) {
            if (prop instanceof AdsUIProperty.AccessProperty) {
                continue;
            }
            final String name = prop.getName();
            final String setName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            final String getName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            String type = null;

            if (prop instanceof AdsUIProperty.BooleanProperty) {
                type = TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_BOOLEAN;
            }
            if (prop instanceof AdsUIProperty.ColorProperty) {
                type = getGuiClassName("QColor");
            }
            if (prop instanceof AdsUIProperty.DateProperty) {
                type = getCoreClassName("QDate");
            }
            if (prop instanceof AdsUIProperty.DateTimeProperty) {
                type = getCoreClassName("QDateTime");
            }
            if (prop instanceof AdsUIProperty.TimeProperty) {
                type = getCoreClassName("QTime");
            }
            if (prop instanceof AdsUIProperty.DoubleProperty) {
                type = TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_DOUBLE;
            }
            if (prop instanceof AdsUIProperty.FloatProperty) {
                type = TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_FLOAT;
            }
            if (prop instanceof AdsUIProperty.FontProperty) {
                type = getGuiClassName("QFont");
            }
            if (prop instanceof AdsUIProperty.ImageProperty) {
                type = getGuiClassName("QIcon");
            }
            if (prop instanceof AdsUIProperty.RectProperty) {
                type = getCoreClassName("QRect");
            }
            if (prop instanceof AdsUIProperty.SizeProperty) {
                type = getCoreClassName("QSize");
            }
            if (prop instanceof AdsUIProperty.StringProperty) {
                type = "String";
            }
            if (prop instanceof AdsUIProperty.IntProperty) {
                type = TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_INT;
            }
            if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
                type = "String";
            }
            if (prop instanceof AdsUIProperty.PropertyRefProperty) {
                type = "org.radixware.kernel.common.client.models.items.properties.Property";
            }

            if (type != null) {
                printer.println(TEXT_PRIVATE + " " + type + " " + name + ";");
                printer.println(TEXT_PUBLIC + " " + type + " " + getName + "() { return " + name + "; };");
                printer.println(TEXT_PUBLIC + " void " + setName + "(" + type + " " + name + ") { this." + name + " = " + name + "; };");
                printer.println();
            }
        }
    }

    private void writeSignalType(CodePrinter printer, AdsUISignalDef signal) {
        Types types = signal.getTypes();
        printer.print("Signal");
        printer.print(String.valueOf(types.size()));
        if (types.size() > 0) {
            printer.print("<");
            for (int i = 0; i < types.size(); i++) {
                AdsTypeDeclaration decl = types.get(i);
                writeUsage(printer, decl, def);
                if (i < types.size() - 1) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private String writeWidget(CodePrinter printer, AdsWidgetDef widget, String ownerName) {
        return gen.writeWidget(printer, widget, ownerName);
    }

    //=======================================================================
    private String getViewModel() {
        switch (def.getId().getPrefix()) {
            case CUSTOM_FORM_DIALOG:
                return "getFormModel()";
            case CUSTOM_EDITOR_PAGE:
                return "getModel()";
            default:
                return "getEntityModel()";
        }
    }

    private static String getGuiClassNameStatic(AdsUIDef def, String clazz, boolean isHumanReadable) {
        if (isHumanReadable) {
            if (clazz.equals(String.valueOf(def.getId()))){
                return def.getName();
            }
        }
        return clazz.equals(String.valueOf(def.getId())) || clazz.contains(".") ? clazz : GUI_PATH + clazz;
    }

    private String getGuiClassName(String clazz) {
        return clazz.equals(String.valueOf(def.getId())) || clazz.contains(".") ? clazz : GUI_PATH + clazz;
    }

    private void writePropValue(CodePrinter printer, AdsUIProperty prop) {
        assert prop != null : "Property is null";
        final String name = prop.getName();
        if (prop instanceof AdsUIProperty.BooleanValueProperty) {
            AdsUIProperty.BooleanValueProperty p = (AdsUIProperty.BooleanValueProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.LongProperty) {
            AdsUIProperty.LongProperty p = (AdsUIProperty.LongProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.BooleanProperty) {
            AdsUIProperty.BooleanProperty p = (AdsUIProperty.BooleanProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.ColorProperty) {
            Color c = ((AdsUIProperty.ColorProperty) prop).color;
            printer.print("new " + getGuiClassName("QColor") + "(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha() + ")");
        } else if (prop instanceof AdsUIProperty.BrushProperty) {
            Color c = ((AdsUIProperty.BrushProperty) prop).color;
            printer.print("new " + getGuiClassName("QBrush") + "(new " + getGuiClassName("QColor") + "(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha() + "))");
        } else if (prop instanceof AdsUIProperty.CursorProperty) {
            AdsUIProperty.CursorProperty p = (AdsUIProperty.CursorProperty) prop;
            printer.print(String.valueOf(p.cursorType));
        } else if (prop instanceof AdsUIProperty.CursorShapeProperty) {
            AdsUIProperty.CursorShapeProperty p = (AdsUIProperty.CursorShapeProperty) prop;
            printer.print("new " + getGuiClassName("QCursor") + "(" + String.valueOf(p.shape.getQualifiedValue()) + ")");
        } else if (prop instanceof AdsUIProperty.DateProperty) {
            AdsUIProperty.DateProperty p = (AdsUIProperty.DateProperty) prop;
            printer.print("new " + getCoreClassName("QDate") + "(" + p.year + ", " + p.month + ", " + p.day + ")");
        } else if (prop instanceof AdsUIProperty.DateTimeProperty) {
            AdsUIProperty.DateTimeProperty p = (AdsUIProperty.DateTimeProperty) prop;
            printer.print("new " + getCoreClassName("QDateTime") + "(new " + getCoreClassName("QDate") + "(" + p.year + ", " + p.month + ", " + p.day + "), new " + getCoreClassName("QTime") + "(" + p.hour + ", " + p.minute + ", " + p.second + "))");
        } else if (prop instanceof AdsUIProperty.TimeProperty) {
            AdsUIProperty.TimeProperty p = (AdsUIProperty.TimeProperty) prop;
            printer.print("new " + getCoreClassName("QTime") + "(" + p.hour + ", " + p.minute + ", " + p.second + ")");
        } else if (prop instanceof AdsUIProperty.DoubleProperty) {
            AdsUIProperty.DoubleProperty p = (AdsUIProperty.DoubleProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.EnumValueProperty) {
            AdsUIProperty.EnumValueProperty p = (AdsUIProperty.EnumValueProperty) prop;
            if (p.value instanceof ECursorShape) {
                printer.print("new " + getGuiClassName("QCursor") + "(" + p.value.getQualifiedValue() + ")");
            } else {
                printer.print(p.value.getQualifiedValue());
            }
        } else if (prop instanceof AdsUIProperty.FloatProperty) {
            AdsUIProperty.FloatProperty p = (AdsUIProperty.FloatProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.FontProperty) {
            AdsUIProperty.FontProperty p = (AdsUIProperty.FontProperty) prop;
        } else if (prop instanceof AdsUIProperty.ImageProperty) {
            AdsUIProperty.ImageProperty p = (AdsUIProperty.ImageProperty) prop;
            if (p.getImageId() != null) {
                printer.print("org.radixware.kernel.explorer.env.ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(");
                WriterUtils.writeIdUsage(printer, p.getImageId());
                printer.print("))");
            } else {
                WriterUtils.writeNull(printer);
            }
        } else if (prop instanceof AdsUIProperty.RectProperty) {
            AdsUIProperty.RectProperty p = (AdsUIProperty.RectProperty) prop;
            printer.print("new " + getCoreClassName("QRect") + "(" + p.x + ", " + p.y + ", " + p.width + ", " + p.height + ")");
        } else if (prop instanceof AdsUIProperty.SetProperty) {
            AdsUIProperty.SetProperty p = (AdsUIProperty.SetProperty) prop;
            if (name.equals("alignment") || name.equals("textAlignment")) {
                printer.print("new " + getCoreClassName("Qt.Alignment("));
                UIEnum[] values = p.getValues();
                for (int i = 0; i < values.length; i++) {
                    printer.print(values[i].getQualifiedValue());
                    if (i < values.length - 1) {
                        printer.print(", ");
                    }
                }
                printer.print(")");
                if (name.equals("textAlignment") || AdsUIUtil.getUiClassName(AdsUIUtil.getOwner(prop)).equals(AdsMetaInfo.GROUP_BOX_CLASS)) {
                    printer.print(".value()");
                }
            } else if (name.equals(AdsWidgetProperties.STANDARD_BUTTONS)) {
                UIEnum[] values = p.getValues();
                for (int i = 0; i < values.length; i++) {
                    printer.print(values[i].getQualifiedValue());
                    if (i < values.length - 1) {
                        printer.print(", ");
                    }
                }
            } else {
                WriterUtils.writeNull(printer);
            }
        } else if (prop instanceof AdsUIProperty.SizeProperty) {
            AdsUIProperty.SizeProperty p = (AdsUIProperty.SizeProperty) prop;
            printer.print("new " + getCoreClassName("QSize") + "(" + p.width + ", " + p.height + ")");
        } else if (prop instanceof AdsUIProperty.SizePolicyProperty) {
            AdsUIProperty.SizePolicyProperty p = (AdsUIProperty.SizePolicyProperty) prop;
            printer.print("new " + getGuiClassName("QSizePolicy") + "(" + p.hSizeType.getQualifiedValue() + ", " + p.vSizeType.getQualifiedValue() + ")");
        } else if (prop instanceof AdsUIProperty.StringProperty) {
            AdsUIProperty.StringProperty p = (AdsUIProperty.StringProperty) prop;
            printer.printStringLiteral(p.value);
        } else if (prop instanceof AdsUIProperty.IntProperty) {
            AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.UrlProperty) {
            AdsUIProperty.UrlProperty p = (AdsUIProperty.UrlProperty) prop;
        } else if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
            AdsUIProperty.LocalizedStringRefProperty p = (AdsUIProperty.LocalizedStringRefProperty) prop;
            if (p.getStringId() != null) {
                WriterUtils.writeNLSInvocation(printer, AdsUIUtil.getUiDef(p).getLocalizingBundleId(), p.getStringId(), def, usagePurpose, false);
//                printer.print(TEXT_DEF_MANAGER + ".getMlStringValue(");
//                WriterUtils.writeIdUsage(printer, AdsUIUtil.getUiDef(p).getId());
//                printer.print(", ");
//                WriterUtils.writeIdUsage(printer, p.getStringId());
//                printer.print(")");
            } else {
                WriterUtils.writeNull(printer);
            }
        } else if (prop instanceof AdsUIProperty.PropertyRefProperty) {
            AdsUIProperty.PropertyRefProperty p = (AdsUIProperty.PropertyRefProperty) prop;
            if (p.getPropertyId() == null) {
                WriterUtils.writeNull(printer);
            } else {
                printer.print("model.getProperty(");
                WriterUtils.writeIdUsage(printer, p.getPropertyId());
                printer.print(")");
            }
        } else if (prop instanceof AdsUIProperty.EmbeddedEditorOpenParamsProperty) {
            AdsUIProperty.EmbeddedEditorOpenParamsProperty p = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) prop;
        } else if (prop instanceof AdsUIProperty.EmbeddedSelectorOpenParamsProperty) {
            AdsUIProperty.EmbeddedSelectorOpenParamsProperty p = (AdsUIProperty.EmbeddedSelectorOpenParamsProperty) prop;
        } else if (prop instanceof AdsUIProperty.EditorPageRefProperty) {
            AdsUIProperty.EditorPageRefProperty p = (AdsUIProperty.EditorPageRefProperty) prop;
            if (p.getEditorPageId() == null) {
                WriterUtils.writeNull(printer);
            } else {
                printer.print(getViewModel() + ".getEditorPage(");
                WriterUtils.writeIdUsage(printer, p.getEditorPageId());
                printer.print(")");
            }
        } else if (prop instanceof AdsUIProperty.CommandRefProperty) {
            AdsUIProperty.CommandRefProperty p = (AdsUIProperty.CommandRefProperty) prop;
            if (p.getCommandId() == null) {
                WriterUtils.writeNull(printer);
            } else {
                printer.print("model.getCommand(");
                WriterUtils.writeIdUsage(printer, p.getCommandId());
                printer.print(")");
            }
        }
    }

    private static boolean writeCustomType(CodePrinter printer, AdsAbstractUIDef ui, UsagePurpose up) {
        if (ui != null){
            WriterUtils.writePackage(printer, ui, up);
            printer.print(".");
            printer.print(JavaSourceSupport.getName(ui, printer instanceof IHumanReadablePrinter));
            return true;
        }
        return false;
    }

    //=======================================================================
    private interface ICodeGenerator extends Cloneable {

        void generate(CodePrinter printer, AdsWidgetDef widget, String ownerName);

        ICodeGenerator clone();
    }
    static final String RESIZE_MODE = "resizeMode";

    private final class WidgetsCodeGenerator {

        private final Set<String> STRING_EMPTY_SET = Collections.<String>emptySet();
        private final Map<String, String> STRING_EMPTY_MAP = Collections.<String, String>emptyMap();
        private final Map<String, ICodeGenerator> generators = new HashMap<>();
        private int layoutCounter = 0;
        private int spacerCounter = 0;
        private int itemCounter = 0;

        private class DefaultCodeGen implements ICodeGenerator {

            AdsWidgetDef widget;
            String ownerName;
//            String widgetName;
            String widgetClass;
            RadixObject owner;
            final Set<String> postProp;
            final Set<String> excludeProp;
            final Map<String, String> propNames;
            private Map<String, String> data;

            DefaultCodeGen() {
                this(Collections.<String>emptySet(), STRING_EMPTY_SET);
            }

            DefaultCodeGen(Map<String, String> propNameTranslation) {
                this(STRING_EMPTY_SET, STRING_EMPTY_SET, propNameTranslation);
            }

            DefaultCodeGen(Set<String> postProp, Set<String> excludeProp) {
                this(postProp, excludeProp, STRING_EMPTY_MAP);
            }

            DefaultCodeGen(Set<String> postProp, Set<String> excludeProp, Map<String, String> propNameTranslation) {

                this.propNames = propNameTranslation;
                this.postProp = postProp;
                this.excludeProp = excludeProp;
            }
            
            char[] getName(CodePrinter printer) {
                return JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter);
            }

            final String getVal(String key) {
                return data.get(key);
            }

            final void setVal(String key, String val) {
                data.put(key, val);
            }

            void open(AdsWidgetDef widget, String ownerName) {

                this.ownerName = ownerName;
                this.widget = widget;

                
                widgetClass = widget.getClassName();
                owner = AdsUIUtil.getOwner(widget);

                data = new HashMap<>();
            }

            void genCreate(CodePrinter printer) {
                printer.print(getName(printer));
                printer.print(" = new ");
                printer.print(getGuiClassName(getActualClass(widgetClass)));
                printer.print('(');
                printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                printer.println(");");
            }

            void genPreProp(CodePrinter printer) {

                if (widget.getProperties().getByName(AdsWidgetProperties.OBJECT_NAME) == null) {
                    final char[] name = getName(printer);
                    final String n = !(AdsMetaInfo.WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(widget)) && widget.getLayout() != null) 
                            || AdsMetaInfo.TAB_WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner)) ? 
                            widget.getName() : String.valueOf(name);
                    printer.print(name);
                    printer.print(".setObjectName(");
                    printer.printStringLiteral(n);
                    printer.println(");");
                }

                for (AdsUIProperty prop : widget.getProperties()) {
                    if (!postProp.contains(prop.getName()) && !excludeProp.contains(prop.getName())) {
                        genProperty(printer, prop);
                    }
                }
                for (AdsUIProperty prop : widget.getAttributes()) {
                    if (!postProp.contains(prop.getName()) && !excludeProp.contains(prop.getName())) {
                        genProperty(printer, prop);
                    }
                }

                if (!AdsMetaInfo.PROP_EDITOR_CLASS.equals(this.widgetClass) && !AdsMetaInfo.PROP_LABEL_CLASS.equals(this.widgetClass)) {
                    printer.print(getName(printer));
                    printer.println(".setFont(DEFAULT_FONT);");
                }
            }

            void genChilds(CodePrinter printer) {

                if (widget.getLayout() != null) {
                    writeLayout(printer, widget.getLayout(), String.valueOf(getName(printer)));
                } else {
                    for (AdsWidgetDef w : widget.getWidgets()) {
                        genChild(printer, w);
                    }
                }
            }

            void genPostProp(CodePrinter printer) {
                for (AdsUIProperty prop : widget.getProperties()) {
                    if (postProp.contains(prop.getName()) && !excludeProp.contains(prop.getName())) {
                        genProperty(printer, prop);
                    }
                }
                for (AdsUIProperty prop : widget.getAttributes()) {
                    if (postProp.contains(prop.getName()) && !excludeProp.contains(prop.getName())) {
                        genProperty(printer, prop);
                    }
                }
            }

            void genFeature(CodePrinter printer) {
            }

            void genOwner(CodePrinter printer) {
            }

            void genConnections(CodePrinter printer) {

                List<AdsUIDef> chain = new LinkedList<>();
                chain.add(def);
                Set<AdsUIDef> processed = new HashSet<>();
                while (!chain.isEmpty()) {
                    List<AdsDefinition> next = new LinkedList<>();
                    for (AdsUIDef current : chain) {
                        if (processed.contains(current)) {
                            continue;
                        }
                        processed.add(current);
                        for (AdsUIConnection c : current.getConnections()) {
                            if (widget.equals(c.getSender())) {
                                printer.print(getName(printer));
                                printer.print("." + c.getSignalName() + ".connect(model, ");
                                printer.print("\"" + c.getSlotId() + "(");
                                final List<AdsUIConnection.Parameter> params = c.getSignalParams();
                                for (AdsUIConnection.Parameter param : params) {
                                    final AdsTypeDeclaration type = param.getType();
                                    final EValType typeId = type.getTypeId();
                                    if (typeId == EValType.INT && type.isPure()) {
                                        printer.print("Long");
//                        printer.print("java.lang.Integer");
                                    } else if (typeId == EValType.NUM && type.isPure()) {
                                        printer.print("java.math.BigDecimal");
                                    } else if (typeId == EValType.STR && type.isPure()) {
                                        printer.print("String");
                                    } else if (typeId == EValType.BOOL && type.isPure()) {
                                        printer.print("Boolean");
                                    } else if (typeId == EValType.CHAR && type.isPure()) {
                                        printer.print("Character");
                                    } else if (typeId == EValType.DATE_TIME && type.isPure()) {
                                        printer.print("Timestamp"/*
                                         * "QDateTime"
                                         */);
                                    } else {
                                        if (type.isGeneric()) {
                                            writeUsage(printer, type.toRawType(), def);
                                        } else {
                                            writeUsage(printer, type, def);
                                        }
                                    }
                                    if (params.indexOf(param) < params.size() - 1) {
                                        printer.print(",");
                                    }
                                }
                                printer.println(")\");");
                            }
                        }
                        current.getHierarchy().findOverwritten().save(next);
                    }
                    chain.clear();
                    for (AdsDefinition def : next) {
                        chain.add((AdsUIDef) def);
                    }
                }
            }

            void genChild(CodePrinter printer, AdsWidgetDef child) {
                writeWidget(printer, child, String.valueOf(getName(printer)));
            }

            void genProperty(CodePrinter printer, AdsUIProperty prop) {
                String propName = translatePropName(prop.getName());
                if (AdsWidgetProperties.TEXT.equals(propName) && AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS.equals(widget.getClassName())) {
                    propName = AdsWidgetProperties.TITLE;
                }

                writeProperty(printer, prop, propName, getName(printer));
            }

            /**
             * Translate propery name
             *
             * @param propName propery name in RADIX designer properies panel
             * @return propery name in generated code
             */
            final String translatePropName(String propName) {
                if (propNames.containsKey(propName)) {
                    propName = propNames.get(propName);
                }
                return propName;
            }

            @Override
            public void generate(CodePrinter printer, AdsWidgetDef widget, String ownerName) {
                open(widget, ownerName);

                genCreate(printer);
                genPreProp(printer);
                genChilds(printer);
                genPostProp(printer);
                genFeature(printer);
                genOwner(printer);
                genConnections(printer);
            }

            @Override
            public DefaultCodeGen clone() {
                try {
                    DefaultCodeGen clone = (DefaultCodeGen) super.clone();
                    return clone;
                } catch (CloneNotSupportedException ex) {
                    return null;
                }
            }

            final void printLine(CodePrinter printer, String... content) {
                int size = content.length;
                if (size > 0) {
                    for (int i = 0; i < size - 1; i++) {
                        printer.print(content[i]);
                    }

                    printer.println(content[size - 1]);
                }
            }

            final void print(CodePrinter printer, String... content) {
                for (String val : content) {
                    printer.print(val);
                }
            }
        }

        public WidgetsCodeGenerator() {
            init();
        }

        private void init() {

            Set<String> postProp = null, excludeProp = null;
            Map<String, String> propNames = null;

            generators.put(AdsMetaInfo.ADVANCED_SPLITTER_CLASS, new DefaultCodeGen() {
                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = new ");
                    printer.print(getGuiClassName(widgetClass));
                    printer.print('(');
                    printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                    printer.printComma();
                    printer.print(" model.getEnvironment().getConfigStore()");
                    printer.println(");");
                }

                @Override
                void genFeature(CodePrinter printer) {
                    final Widgets widgets = widget.getWidgets();
                    final int size = widgets.size();
                    if (size > 0) {
                        printer.print(getName(printer));
                        printer.print(".setSizes(java.util.Arrays.asList(");
                        for (int i = 0; i < size; i++) {
                            printer.print((int) (widgets.get(i).getWeight() * 1000));
                            if (i < size - 1) {
                                printer.print(", ");
                            }
                        }
                        printer.println("));");
                    }
                }
            });

            generators.put(AdsMetaInfo.SPLITTER_CLASS, new DefaultCodeGen() {
                @Override
                void genFeature(CodePrinter printer) {
                    final Widgets widgets = widget.getWidgets();
                    final int size = widgets.size();
                    if (size > 0) {
                        printer.print(getName(printer));
                        printer.print(".setSizes(java.util.Arrays.asList(");
                        for (int i = 0; i < size; i++) {
                            printer.print((int) (widgets.get(i).getWeight() * 1000));
                            if (i < size - 1) {
                                printer.print(", ");
                            }
                        }
                        printer.println("));");
                    }
                }
            });

            postProp = Collect.set(AdsWidgetProperties.CURRENT_INDEX);
            excludeProp = Collect.set("currentPageName");
            generators.put(AdsMetaInfo.STACKED_WIDGET_CLASS, new DefaultCodeGen(postProp, excludeProp) {
                @Override
                void genChild(CodePrinter printer, AdsWidgetDef child) {
                    super.genChild(printer, child);
                    printer.print(getName(printer));
                    printer.print(".addWidget(");
                    printer.print(JavaSourceSupport.getName(child, printer instanceof IHumanReadablePrinter));
                    printer.println(");");
                }
            });

            excludeProp = Collect.set("contentGeometry");
            generators.put(AdsMetaInfo.SCROLL_AREA_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genChild(CodePrinter printer, AdsWidgetDef child) {
                    super.genChild(printer, child);
                    printer.print(getName(printer)).print(".setWidget(")
                            .print(JavaSourceSupport.getName(child, printer instanceof IHumanReadablePrinter))
                            .print(")").printlnSemicolon();
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.COMMAND);
            generators.put(AdsMetaInfo.COMMAND_TOOL_BUTTON_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genCreate(CodePrinter printer) {
                    AdsUIProperty.CommandRefProperty prop = (AdsUIProperty.CommandRefProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.COMMAND);
                    if (prop.getCommandId() == null) {
                        super.genCreate(printer);
                    } else {
                        final char[] widgetName = getName(printer);
                        printer.print(widgetName);
                        printer.print(" = (");
                        printer.print(AdsMetaInfo.COMMAND_TOOL_BUTTON_CLASS);
                        printer.print(")model.getCommand(");
                        WriterUtils.writeIdUsage(printer, prop.getCommandId());
                        printer.println(").createToolButton();");

                        if (ownerName != null) {
                            printer.print(widgetName);
                            printer.print(".setParent(");
                            printer.print(ownerName);
                            printer.println(");");
                        }
                        printer.print(widgetName);
                        printer.println(".bind();");
                    }
                }
            });

            postProp = Collect.set(AdsWidgetProperties.CURRENT_INDEX);
            generators.put(AdsMetaInfo.TAB_WIDGET_CLASS, new DefaultCodeGen(postProp, STRING_EMPTY_SET) {
                @Override
                void genChild(CodePrinter printer, AdsWidgetDef child) {
                    super.genChild(printer, child);

                    String childName = String.valueOf(child.getId());

                    AdsUIProperty title = AdsUIUtil.getUiProperty(child, AdsWidgetProperties.TITLE);
                    AdsUIProperty icon = AdsUIUtil.getUiProperty(child, AdsWidgetProperties.ICON);

                    printer.print(getName(printer));
                    printer.print(".addTab(");
                    printer.print(childName);
                    printer.print(", ");

                    if (icon == null) {
                        WriterUtils.writeNull(printer);
                    } else {
                        writePropValue(printer, icon);
                    }
                    printer.print(", ");
                    if (title == null) {
                        printer.printStringLiteral("");
                    } else {
                        writePropValue(printer, title);
                    }
                    printer.println(");");
                }
            });

            generators.put(AdsMetaInfo.COMBO_BOX_CLASS, new DefaultCodeGen() {
                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);

                    AdsItemWidgetDef w = (AdsItemWidgetDef) widget;
                    for (WidgetItem item : w.getItems()) {
                        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(item, AdsWidgetProperties.TEXT);
                        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(item, AdsWidgetProperties.ICON);
                        printer.print(getName(printer));
                        printer.print(".addItem((com.trolltech.qt.gui.QIcon)");
                        writePropValue(printer, icon);
                        printer.print(", (String)");
                        writePropValue(printer, text);
                        printer.println(");");
                    }
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.ACTIONS);
            generators.put(AdsMetaInfo.TOOL_BAR_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);
                    final AdsUIProperty.IdListProperty actions = (AdsUIProperty.IdListProperty) AdsUIUtil.getUiProperty(this.widget, AdsWidgetProperties.ACTIONS);
                    if (actions != null) {
                        for (final Id id : actions.getIds()) {
                            printer.print(getName(printer)).print(".addAction(")
                                    .print(getListItemName(widget, id, printer instanceof IHumanReadablePrinter))
                                    .print(")").printlnSemicolon();
                        }
                    }
                }

                @Override
                void genChild(CodePrinter printer, AdsWidgetDef child) {
                    super.genChild(printer, child);
                    printer.print(getName(printer)).print(".addWidget(")
                                    .print(JavaSourceSupport.getName(child, printer instanceof IHumanReadablePrinter))
                                    .print(")").printlnSemicolon();
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.GEOMETRY);
            generators.put(AdsMetaInfo.ACTION_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp));

            generators.put(AdsMetaInfo.LIST_WIDGET_CLASS, new DefaultCodeGen() {
                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);

                    AdsItemWidgetDef w = (AdsItemWidgetDef) widget;
                    for (WidgetItem item : w.getItems()) {
                        String n = "$item" + (++itemCounter);
                        printer.print(getGuiClassName(AdsMetaInfo.LIST_WIDGET_ITEM_CLASS));
                        printer.printSpace();
                        printer.print(n);
                        printer.print(" = new ");
                        printer.print(getGuiClassName(AdsMetaInfo.LIST_WIDGET_ITEM_CLASS));
                        printer.print("(");
                        printer.print(getName(printer));
                        printer.println(");");
                        writeProps(printer, n, item.getProperties(), Collect.set(RESIZE_MODE), -1);
                    }
                }
            });

            generators.put(AdsMetaInfo.TREE_WIDGET_CLASS, new DefaultCodeGen() {
                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);

                    if (AdsMetaInfo.TREE_WIDGET_CLASS.equals(widgetClass)) {
                        AdsItemWidgetDef w = (AdsItemWidgetDef) widget;
                        Columns cols = w.getColumns();
                        final char [] widgetName = getName(printer);
                        String n = "$header" + (++itemCounter);
                        printer.print(widgetName);
                        printer.print(".setColumnCount(");
                        printer.print(cols.size());
                        printer.println(");");

                        printer.print(getGuiClassName(AdsMetaInfo.TREE_WIDGET_ITEM_CLASS));
                        printer.printSpace();
                        printer.print(n);
                        printer.print(" = new ");
                        printer.print(getGuiClassName(AdsMetaInfo.TREE_WIDGET_ITEM_CLASS));
                        printer.println("();");

                        for (int i = 0; i < cols.size(); i++) {
                            Column col = cols.get(i);
                            writeProps(printer, n, col.getProperties(), Collect.set(RESIZE_MODE), i);
                        }
                        printer.print(widgetName);
                        printer.print(".setHeaderItem(");
                        printer.print(n);
                        printer.println(");");

                        writeTreeItems(printer, widgetName, w.getItems());
                    }
                }
            });

            generators.put(AdsMetaInfo.TABLE_WIDGET_CLASS, new DefaultCodeGen() {
                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);
                    final char [] widgetName = getName(printer);
                    if (AdsMetaInfo.TABLE_WIDGET_CLASS.equals(widgetClass)) {
                        AdsItemWidgetDef w = (AdsItemWidgetDef) widget;

                        Rows rows = w.getRows();
                        Columns cols = w.getColumns();

                        printer.print(widgetName);
                        printer.print(".setColumnCount(");
                        printer.print(cols.size());
                        printer.println(");");

                        printer.print(widgetName);
                        printer.print(".setRowCount(");
                        printer.print(rows.size());
                        printer.println(");");

                        for (int i = 0; i < rows.size(); i++) {
                            Row row = rows.get(i);
                            String n = "$row" + (++itemCounter);
                            printer.print(getGuiClassName(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS));
                            printer.printSpace();
                            printer.print(n);
                            printer.print(" = new ");
                            printer.print(getGuiClassName(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS));
                            printer.println("();");

                            writeProps(printer, n, row.getProperties(), Collect.set(RESIZE_MODE), -1);
                            printer.print(widgetName);
                            printer.print(".setVerticalHeaderItem(");
                            printer.print(i);
                            printer.print(", ");
                            printer.print(n);
                            printer.println(");");
                        }
                        for (int i = 0; i < cols.size(); i++) {
                            Column col = cols.get(i);
                            String n = "$col" + (++itemCounter);
                            printer.print(getGuiClassName(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS));
                            printer.printSpace();
                            printer.print(n);
                            printer.print(" = new ");
                            printer.print(getGuiClassName(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS));
                            printer.println("();");

                            writeProps(printer, n, col.getProperties(), Collect.set(RESIZE_MODE), -1);

                            printer.print(widgetName);
                            printer.print(".setHorizontalHeaderItem(");
                            printer.print(i);
                            printer.print(", ");
                            printer.print(n);
                            printer.println(");");

                            final AdsUIProperty resizeModeProp = col.getProperties().getByName(RESIZE_MODE);
                            if (resizeModeProp instanceof AdsUIProperty.EnumValueProperty) {
                                final AdsUIProperty.EnumValueProperty property = (AdsUIProperty.EnumValueProperty) resizeModeProp;

                                printer.print(widgetName);
                                printer.print(".horizontalHeader().setResizeMode(");
                                printer.print(i);
                                printer.print(", ");
                                printer.print(property.value.getQualifiedValue());
                                printer.println(");");
                            }
                        }
                        WidgetItem[][] items = w.getItemsAsArray();
                        for (int i = 0; i < items.length; i++) {
                            for (int j = 0; j < items[i].length; j++) {
                                WidgetItem item = items[i][j];
                                if (item == null || item.getProperties().size() == 0) {
                                    continue;
                                }
                                String n = "$item" + (++itemCounter);
                                printer.print(getGuiClassName(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS));
                                printer.printSpace();
                                printer.print(n);
                                printer.print(" = new ");
                                printer.print(getGuiClassName(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS));
                                printer.println("();");

                                writeProps(printer, n, item.getProperties(), Collect.set(RESIZE_MODE), -1);
                                printer.print(widgetName);
                                printer.print(".setItem(");
                                printer.print(i);
                                printer.print(", ");
                                printer.print(j);
                                printer.print(", ");
                                printer.print(n);
                                printer.println(");");
                            }
                        }
                    }
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.COMMAND);
            generators.put(AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genCreate(CodePrinter printer) {
                    AdsUIProperty.CommandRefProperty prop = (AdsUIProperty.CommandRefProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.COMMAND);
                    if (prop.getCommandId() == null) {
                        super.genCreate(printer);
                    } else {
                        final char [] widgetName = getName(printer);
                        printer.print(widgetName);
                        printer.print(" = (");
                        printer.print(AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS);
                        printer.print(")model.getCommand(");
                        WriterUtils.writeIdUsage(printer, prop.getCommandId());
                        printer.println(").createPushButton();");

                        printer.print(widgetName);
                        printer.println(".bind();");
                        if (ownerName != null) {
                            printer.print(widgetName);
                            printer.print(".setParent(");
                            printer.print(ownerName);
                            printer.println(");");
                        }
                    }
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.PROPERTY);
            generators.put(AdsMetaInfo.PROP_EDITOR_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genCreate(CodePrinter printer) {
                    AdsUIProperty.PropertyRefProperty prop = (AdsUIProperty.PropertyRefProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.PROPERTY);
                    final char [] widgetName = getName(printer);
                    if (prop.getPropertyId() == null) {
                        printer.print(widgetName);
                        printer.println(" = null;");
                    } else {
                        printer.print(widgetName);
                        printer.print(" = (");
                        printer.print(ABSTRACT_PROP_EDITOR_CLASS);
                        printer.print(")model.getProperty(");
                        WriterUtils.writeIdUsage(printer, prop.getPropertyId());
                        printer.println(").createPropertyEditor();");

                        if (ownerName != null) {
                            printer.print(widgetName);
                            printer.print(".setParent(");
                            printer.print(ownerName);
                            printer.println(");");
                        }

                        printer.print(widgetName);
                        printer.println(".bind();");
                    }
                }
            });

            excludeProp = Collect.set("editorPageRef", AdsWidgetProperties.EDITOR_PAGE);//!
            generators.put(AdsMetaInfo.EDITOR_PAGE_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genCreate(CodePrinter printer) {
                    AdsUIProperty.EditorPageRefProperty prop = (AdsUIProperty.EditorPageRefProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.EDITOR_PAGE);
                    final char [] widgetName = getName(printer);
                    if (prop.getEditorPageId() == null) {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.println("(model.getEnvironment());");
                    } else {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.print("(");
                        printer.print(getViewModel());
                        printer.print(".getEditorPage(");
                        WriterUtils.writeIdUsage(printer, prop.getEditorPageId());
                        printer.println("));");

                        if (ownerName != null) {
                            printer.print(widgetName);
                            printer.print(".setParent(");
                            printer.print(ownerName);
                            printer.println(");");
                        }

                        printer.print(widgetName);
                        printer.println(".bind();");

                    }
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.OPEN_PARAMS);
            generators.put(AdsMetaInfo.EMBEDDED_SELECTOR_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genCreate(CodePrinter printer) {
                    AdsUIProperty.EmbeddedSelectorOpenParamsProperty prop = (AdsUIProperty.EmbeddedSelectorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
                    final char [] widgetName = getName(printer);
                    if (prop.getPropertyId() != null) {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.print("(model.getEnvironment(),(");
                        printer.print(TEXT_PROPERTY_REF);
                        printer.print(")getModel().getProperty(");
                        WriterUtils.writeIdUsage(printer, prop.getPropertyId());
                        printer.println("));");

                    } else if (prop.getExplorerItemId() != null) {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.print("(model.getEnvironment(),");
                        printer.print(TEXT_THIS);
                        printer.print(", ");
                        WriterUtils.writeIdUsage(printer, prop.getExplorerItemId());
                        printer.println(");");

                    } else {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.println("(model.getEnvironment());");
                    }
                    if (ownerName != null) {
                        printer.print(widgetName);
                        printer.print(".setParent(");
                        printer.print(ownerName);
                        printer.println(");");
                    }
                }

                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);
                    AdsUIProperty.EmbeddedSelectorOpenParamsProperty prop = (AdsUIProperty.EmbeddedSelectorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
                    if (prop.getPropertyId() != null || prop.getExplorerItemId() != null) {
                        printer.print(getName(printer));
                        printer.println(".bind();");
                    }
                }
                
                
            });

            excludeProp = Collect.set(AdsWidgetProperties.OPEN_PARAMS);
            generators.put(AdsMetaInfo.EMBEDDED_EDITOR_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp) {
                @Override
                void genCreate(CodePrinter printer) {
                    AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
                    final char [] widgetName = getName(printer);
                    if (prop.getPropertyId() != null) {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.print("(model.getEnvironment(),(");
                        printer.print(TEXT_PROPERTY_REFERENCE);
                        printer.print(")getModel().getProperty(");
                        WriterUtils.writeIdUsage(printer, prop.getPropertyId());
                        printer.println("));");

                    } else if (prop.getExplorerItemId() != null) {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.print("(model.getEnvironment(),");
                        printer.print(TEXT_THIS);
                        printer.print(", ");
                        WriterUtils.writeIdUsage(printer, prop.getExplorerItemId());
                        printer.println(");");
                    } else if (prop.getClassId() != null && prop.getEditorPresentationId() != null) {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.print("(model.getEnvironment(),");
                        WriterUtils.writeIdUsage(printer, prop.getClassId());
                        printer.print(", ");
                        WriterUtils.writeIdUsage(printer, prop.getEditorPresentationId());
                        printer.println(", null);");
                    } else {
                        printer.print(widgetName);
                        printer.print(" = new ");
                        printer.print(widgetClass);
                        printer.println("(model.getEnvironment());");
                    }
                    if (ownerName != null) {
                        printer.print(widgetName);
                        printer.print(".setParent(");
                        printer.print(ownerName);
                        printer.println(");");
                    }
                }

                @Override
                void genPreProp(CodePrinter printer) {
                    super.genPreProp(printer);
                    AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
                    if (prop.getPropertyId() != null || prop.getExplorerItemId() != null || (prop.getClassId() != null && prop.getEditorPresentationId() != null)) {
                        printer.print(getName(printer));
                        printer.println(".bind();");
                    }
                }
                
                
            });

            class ValEditorGen extends DefaultCodeGen {

                public ValEditorGen() {
                    this(STRING_EMPTY_SET, STRING_EMPTY_SET, STRING_EMPTY_MAP);
                }

                public ValEditorGen(Map<String, String> propNameTranslation) {
                    this(STRING_EMPTY_SET, STRING_EMPTY_SET, propNameTranslation);
                }

                @SuppressWarnings("unchecked")
                public ValEditorGen(Set<String> postProp, Set<String> excludeProp, Map<String, String> propNameTranslation) {
                    super(postProp,
                            Collect.set("frameShape", "frameShadow"),
                            Collect.<String, String>map(
                                    Collect.pair("notNull", "mandatory"),
                                    Collect.pair("autoValidation", "autoValidationEnabled")));

                    this.excludeProp.addAll(excludeProp);
                    this.propNames.putAll(propNameTranslation);
                }

                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = new ");
                    printer.print(getGuiClassName(widgetClass));
                    printer.print('(');
                    printer.print("model.getEnvironment(), ");
                    printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                    printer.println(");");
                }

                @Override
                void genChild(CodePrinter printer, AdsWidgetDef child) {
                    writeWidget(printer, child, null);

                    final char[] childName = JavaSourceSupport.getName(child, printer instanceof IHumanReadablePrinter);

                    printer.print(getName(printer));
                    printer.print(".addButton(");
                    printer.print(childName);
                    printer.println(");");
                }
            }

            generators.put(AdsMetaInfo.VAL_BIN_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_BOOL_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_CHAR_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_STR_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_INT_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_NUM_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_FILE_PATH_EDITOR_CLASS, new ValEditorGen());//!

            excludeProp = Collect.set("enumRef");
            generators.put(AdsMetaInfo.VAL_CONST_SET_EDITOR_CLASS, new ValEditorGen(STRING_EMPTY_SET, excludeProp, STRING_EMPTY_MAP) {
                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = new ");
                    printer.print(getGuiClassName(widgetClass));
                    printer.print('(');
                    printer.print("model.getEnvironment(), ");
                    printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                    printer.printComma();

                    AdsUIProperty.EnumRefProperty enumRef = (AdsUIProperty.EnumRefProperty) widget.getProperties().getByName("enumRef");
                    if (enumRef == null || enumRef.getEnumId() == null) {
                        printer.print(" null");
                    } else {
                        printer.print(" org.radixware.kernel.common.types.Id.Factory.loadFrom(");
                        printer.printStringLiteral(enumRef.getEnumId().toString());
                        printer.print(")");
                    }
                    printer.println(");");
                }
            });

            generators.put(AdsMetaInfo.VAL_DATE_TIME_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.VAL_TIMEINTERVAL_EDITOR_CLASS, new ValEditorGen() {
                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = new ");
                    printer.print(getGuiClassName(widgetClass));
                    printer.print('(');
                    printer.print("model.getEnvironment(), ");
                    printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                    printer.printComma();
                    printer.print("org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale.NONE");
                    printer.println(");");
                }
            });

            generators.put(AdsMetaInfo.VAL_LIST_EDITOR_CLASS, new ValEditorGen() {
                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = new ");
                    printer.print(getGuiClassName(widgetClass));
                    printer.print('(');
                    printer.print("model.getEnvironment(), ");
                    printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                    printer.printComma();
                    printer.print("java.util.Collections.<org.radixware.kernel.common.client.meta.mask.EditMaskList.Item>emptyList()");
                    printer.println(");");
                }
            });

            generators.put(AdsMetaInfo.VAL_REF_EDITOR_CLASS, new ValEditorGen());

            generators.put(AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS, new DefaultCodeGen() {
                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = new ");
                    printer.print(getActualClass(AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS));
                    printer.print("(model.getEnvironment(), ");
                    printer.print((String) Utils.nvl(ownerName, "(" + getGuiClassName(AdsMetaInfo.WIDGET_CLASS) + ")null"));
                    printer.println(");");
                }

                @Override
                void genProperty(CodePrinter printer, AdsUIProperty prop) {
                    if (prop != null) {
                        switch (prop.getName()) {
                            case "autoConnectAcceptSignal":
                                if (((AdsUIProperty.BooleanProperty) prop).value) {
                                    printer.print(getName(printer));
                                    printer.println(".accepted.connect(this, \"accept()\");");
                                }
                                return;
                            case "autoConnectRejectSignal":
                                if (((AdsUIProperty.BooleanProperty) prop).value) {
                                    printer.print(getName(printer));
                                    printer.println(".rejected.connect(this, \"reject()\");");
                                }
                                return;
                            case AdsWidgetProperties.STANDARD_BUTTONS:
                                final AdsUIProperty.SetProperty buttons = (AdsUIProperty.SetProperty) prop;
                                final UIEnum[] values = buttons.getValues();
                                if (values.length > 0) {
                                    printer.print(getName(printer));
                                    printer.print(".addButtons(java.util.EnumSet.of(");
                                    boolean first = true;
                                    for (UIEnum value : values) {
                                        if (first) {
                                            first = false;
                                        } else {
                                            printer.print(", ");
                                        }
                                        printer.print(value.getQualifiedValue());
                                    }
                                    printer.println("));");
                                }
                                return;
                        }
                    }
                    super.genProperty(printer, prop);
                }
            });

            generators.put("%THIS_GENERATOR", new DefaultCodeGen() {
                @Override
                void genCreate(CodePrinter printer) {
                    printer.print(getName(printer));
                    printer.print(" = ");
                    printer.print(TEXT_THIS);
                    printer.println(";");
                }

                @Override
                void genChilds(CodePrinter printer) {
                    String thisName = String.valueOf(getName(printer));

                    if (DIALOGS_WITH_CONTENT_WIDGET.contains(def.getId().getPrefix())) {
                        thisName = TEXT_THIS + ".content";
                    }

                    setVal("THIS_NAME", thisName);

                    genActions(printer);

                    if (widget.getLayout() != null) {
                        writeLayout(printer, widget.getLayout(), thisName);
                    } else {
                        for (final AdsWidgetDef wdg : widget.getWidgets()) {
                            if (!wdg.isActionWidget()) {
                                genChild(printer, wdg);
                            }
                        }
                    }
                }

                @Override
                void genChild(CodePrinter printer, AdsWidgetDef child) {
                    writeWidget(printer, child, getVal("THIS_NAME"));
                }

                void genActions(CodePrinter printer) {
                    for (final AdsWidgetDef wdg : widget.getWidgets()) {
                        if (wdg.isActionWidget()) {
                            genChild(printer, wdg);
                        }
                    }
                }
            });

            generators.put("%CUSTOM_WIDGET_GENERATOR", new DefaultCodeGen() {
                @Override
                void genCreate(CodePrinter printer) {
                    final char [] widgetName = getName(printer);
                    printer.print(widgetName);
                    printer.print(" = new ");
                    AdsAbstractUIDef customUI = getCustomUI(widget);
                    writeCustomType(printer, customUI, usagePurpose);
                    printer.print("(model.getEnvironment(),");
                    printer.print(String.valueOf(ownerName));
                    printer.println(");");

                    printer.print(widgetName);
                    printer.print(".open(");
                    printer.print(widgetName);
                    printer.println(".getModel());");
                }
            });

            excludeProp = Collect.set(AdsWidgetProperties.TITLE, AdsWidgetProperties.ICON);
            generators.put(AdsMetaInfo.WIDGET_CLASS, new DefaultCodeGen(STRING_EMPTY_SET, excludeProp));

            generators.put("%DEFAULT_GENERATOR", new DefaultCodeGen());

        }

        boolean containsGenerator(String key) {
            return generators.containsKey(key);
        }

        ICodeGenerator getGenerator(String key) {
            return generators.get(key).clone();
        }

        private AdsAbstractUIDef getCustomUI(AdsWidgetDef widget) {
            return AdsMetaInfo.getCustomUI(widget);
        }

        private String writeWidget(CodePrinter printer, AdsWidgetDef widget, String ownerName) {
            assert printer != null && widget != null;

            ICodeGenerator generator;

            if (TEXT_THIS.equals(ownerName)) {
                generator = getGenerator("%THIS_GENERATOR");
            } else if (AdsUIUtil.isCustomWidget(widget)) {
                generator = getGenerator("%CUSTOM_WIDGET_GENERATOR");
            } else if (containsGenerator(widget.getClassName())) {
                generator = getGenerator(widget.getClassName());
            } else {
                generator = getGenerator("%DEFAULT_GENERATOR");
            }
            generator.generate(printer, widget, ownerName);

            return String.valueOf(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter));
        }

        private void writeProperty(CodePrinter printer, AdsUIProperty prop, String propName, final char[] ownerName) {
            if (prop instanceof AdsUIProperty.AccessProperty) {
                return;
            }

            String methodName = "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);

            if (prop instanceof AdsUIProperty.StringProperty && "buddy".equals(propName)) {
                AdsUIProperty.StringProperty p = (AdsUIProperty.StringProperty) prop;
                if (p.value != null && !p.value.isEmpty()) {
                    writePropertySet(printer, p.value, methodName, ownerName);
                }
                return;
            }
            if (AdsWidgetProperties.ENABLED.equals(propName) && prop instanceof AdsUIProperty.BooleanProperty && ((AdsUIProperty.BooleanProperty) prop).value) {
                return;
            }

            writePropertySet(printer, prop, methodName, ownerName);
        }

        private void writePropertySet(CodePrinter printer, AdsUIProperty prop, String methodName, final char[] ownerName) {

            printer.print(ownerName);
            printer.print(".");
            printer.print(methodName);
            printer.print("(");
            writePropValue(printer, prop);
            printer.println(");");
        }

        private void writePropertySet(CodePrinter printer, String propValue, String methodName, final char[] ownerName) {

            if (!propValue.isEmpty()) {
                printer.print(ownerName);
                printer.print(".");
                printer.print(methodName);
                printer.print("(");
                printer.print(propValue);
                printer.println(");");
            }
        }

        private void writeProps(CodePrinter printer, String name, UiProperties props) {
            writeProps(printer, name, props, -1);
        }

        private void writeProps(CodePrinter printer, String name, UiProperties props, int idx) {
            writeProps(printer, name, props, Collections.<String>emptySet(), idx);
        }

        private void writeProps(CodePrinter printer, String name, UiProperties props, Set<String> exclude, int idx) {
            for (AdsUIProperty prop : props) {
                if (prop instanceof AdsUIProperty.AccessProperty) {
                    continue;
                }
                String n = prop.getName();
                if (n.equals("editorPageRef")) {
                    n = "editorPage";
                }

                if (exclude.contains(n)) {
                    continue;
                }

                if (prop instanceof AdsUIProperty.StringProperty && "buddy".equals(n)) {
                    AdsUIProperty.StringProperty p = (AdsUIProperty.StringProperty) prop;
                    if (p.value != null && !p.value.isEmpty()) {
                        printer.print(name + ".set" + n.substring(0, 1).toUpperCase() + n.substring(1) + "(");
                        printer.print(p.value);
                        printer.println(");");
                    }
                    continue;
                }

                if (AdsWidgetProperties.ENABLED.equals(n) && prop instanceof AdsUIProperty.BooleanProperty && ((AdsUIProperty.BooleanProperty) prop).value) {
                    continue;
                }
                printer.print(name + ".set" + n.substring(0, 1).toUpperCase() + n.substring(1) + "(");
                if (idx >= 0) {
                    printer.print(String.valueOf(idx) + ", ");
                }

                writePropValue(printer, prop);
                printer.println(");");
            }
        }

        private void writeTreeItems(CodePrinter printer, char[] name, AdsItemWidgetDef.Items items) {
            for (WidgetItem item : items) {
                String n = "$item" + (++itemCounter);
                printer.print(getGuiClassName(AdsMetaInfo.TREE_WIDGET_ITEM_CLASS)).print(" ").print(n)
                        .print(" = new ").print(getGuiClassName(AdsMetaInfo.TREE_WIDGET_ITEM_CLASS))
                        .print("("). print(name).print(")").printlnSemicolon();
                writeProps(printer, n, item.getProperties(), Collect.set(RESIZE_MODE), 0);
                writeTreeItems(printer, n.toCharArray(), item.getItems());
            }
        }

        private String writeLayout(CodePrinter printer, AdsLayout layout, String ownerName) {
            return writeLayout(printer, layout, ownerName, false);
        }

        private String writeLayout(CodePrinter printer, AdsLayout layout, String ownerName, boolean inLayout) {
            final String clazz = AdsUIUtil.getUiClassName(layout);
            final String name;

            if (!layout.isSetName()) {
                printer.print(TEXT_FINAL);
                printer.printSpace();
                printer.print(getGuiClassName(clazz));
                printer.printSpace();

                name = "$layout" + (++layoutCounter);
            } else {
                name = layout.getName();
            }

            if (inLayout) {
                printer.println(name + " = new " + getGuiClassName(clazz) + "();");
            } else {
                printer.println(name + " = new " + getGuiClassName(clazz) + "(" + Utils.nvl(ownerName, "") + ");");
            }

            printer.print(name + ".setObjectName(");
            printer.printStringLiteral(layout.getName());
            printer.println(");");

            int left = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutLeftMargin")).value;
            int top = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutTopMargin")).value;
            int right = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutRightMargin")).value;
            int bottom = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutBottomMargin")).value;
            printer.println(name + ".setContentsMargins(" + left + ", " + top + ", " + right + ", " + bottom + ");");

            final AdsUIProperty.EnumValueProperty sizeConstraintProperty = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(layout, "layoutSizeConstraint");
            if (sizeConstraintProperty != null) {
                final ESizeConstraint sizeConstraint = (ESizeConstraint) sizeConstraintProperty.value;
                printer.println(name + ".setSizeConstraint(" + sizeConstraint.getQualifiedValue() + ");");
            }

            if (clazz.equals(AdsMetaInfo.GRID_LAYOUT_CLASS)) {
                int horizontalSpacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutHorizontalSpacing")).value;
                printer.println(name + ".setHorizontalSpacing(" + horizontalSpacing + ");");
                int verticalSpacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutVerticalSpacing")).value;
                printer.println(name + ".setVerticalSpacing(" + verticalSpacing + ");");
            } else {
                int spacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutSpacing")).value;
                printer.println(name + ".setSpacing(" + spacing + ");");
            }

            for (AdsLayout.Item item : layout.getItems()) {
                String n = null, it = null;
                RadixObject node = AdsUIUtil.getItemNode(item);
                if (node instanceof AdsLayout.SpacerItem) {
                    n = writeSpacer(printer, (AdsLayout.SpacerItem) node);
                    it = AdsMetaInfo.GRID_LAYOUT_CLASS.equals(layout.getClassName()) ? "Item" : "SpacerItem";
                }
                if (node instanceof AdsWidgetDef) {
                    n = writeWidget(printer, (AdsWidgetDef) node, ownerName);
                    it = "Widget";
                }
                if (node instanceof AdsLayout) {
                    n = writeLayout(printer, (AdsLayout) node, ownerName, true);
                    it = "Layout";
                }
                printer.print(name + ".add" + it + "(" + n);
                if (clazz.equals(AdsMetaInfo.GRID_LAYOUT_CLASS)) {
                    printer.print(", " + item.row + ", " + item.column + ", " + item.rowSpan + ", " + item.columnSpan);
                }
                printer.println(");");
            }
            return name;
        }

        private String writeSpacer(CodePrinter printer, AdsLayout.SpacerItem spacer) {
            final String name = "$spacer" + (++spacerCounter);

            AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(spacer, AdsWidgetProperties.ORIENTATION);
            EOrientation o = orientation.getValue();

            AdsUIProperty.EnumValueProperty sizeType = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(spacer, "sizeType");
            AdsUIProperty.SizeProperty sizeHint = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(spacer, "sizeHint");
            ESizePolicy hData = o.equals(EOrientation.Horizontal) ? (ESizePolicy) sizeType.value : ESizePolicy.Minimum;
            ESizePolicy vData = o.equals(EOrientation.Vertical) ? (ESizePolicy) sizeType.value : ESizePolicy.Minimum;

            printer.print(getGuiClassName(AdsMetaInfo.SPACER_CLASS) + " " + name + " = new " + getGuiClassName(AdsMetaInfo.SPACER_CLASS) + "(");
            printer.print(String.valueOf(sizeHint.width) + ", " + String.valueOf(sizeHint.height) + ", " + hData.getQualifiedValue() + ", " + vData.getQualifiedValue());
            printer.println(");");

            //printer.print(name + ".setObjectName(");
            //printer.printStringLiteral(spacer.getName());
            //printer.println(");");
            return name;
        }
    }
    
        
    public static String getListItemName(AdsUIItemDef widget, Id id, boolean isHumanReadable) {
        if (isHumanReadable) {
            AdsUIItemDef item = widget.getOwnerUIDef().getWidget().findWidgetById(id);
            if (item != null) {
                return item.getName();
            } else {
                return id.toString();
            }
        } else {
            return id.toString();
        }
    }

    private static class Collect {

        public static class Pair<TFirst, TSecond> {

            public final TFirst first;
            public final TSecond second;

            public Pair(TFirst first, TSecond second) {
                this.first = first;
                this.second = second;
            }
        }

        public static <TFirst, TSecond> Pair<TFirst, TSecond> pair(TFirst first, TSecond second) {
            return new Pair<>(first, second);
        }

        @SafeVarargs
        public static <T> Set<T> set(T... items) {
            if (items.length == 0) {
                return Collections.<T>emptySet();
            }
            return new HashSet<>(Arrays.asList(items));
        }

        @SafeVarargs
        public static <TFirst, TSecond> Map<TFirst, TSecond> map(Pair<TFirst, TSecond>... items) {

            if (items.length == 0) {
                return Collections.<TFirst, TSecond>emptyMap();
            }

            final Map<TFirst, TSecond> map = new HashMap<>();
            for (final Pair<TFirst, TSecond> item : items) {
                assert item.first != null && item.second != null;
                map.put(item.first, item.second);
            }

            return map;
        }
    }
}
