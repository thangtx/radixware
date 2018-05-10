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
package org.radixware.kernel.common.defs.ads.src.ui.rwt;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.enums.EArrayClassName;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.defs.ads.ui.generation.AdsUIWriter;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;

public class RwtWidgetWriter {

    public static RwtWidgetWriter getInstance(AdsRwtWidgetDef widget) {
        String className = widget.getClassName();
        return new RwtWidgetWriter(widget);
    }
    protected final AdsRwtWidgetDef widget;

    public RwtWidgetWriter(AdsRwtWidgetDef widget) {
        this.widget = widget;
    }

    public void writeWidgetFieldDecls(final CodePrinter printer) {
        AdsUIUtil.visitUI(widget, new AdsUIUtil.IVisitorUI() {
            @Override
            public void visit(RadixObject node, boolean active) {
                getInstance((AdsRwtWidgetDef) node).writeDeclaration(printer);
            }
        }, true);
    }

    public void writeWidgetInitializations(final CodePrinter printer) {
        final Map<AdsRwtWidgetDef, AdsUIProperty.AnchorProperty> defferedAnchors = new HashMap<>();
        final List<RadixObject> widgets = new ArrayList<>();
        AdsUIUtil.visitUI(widget, new AdsUIUtil.IVisitorUI() {
            @Override
            public void visit(RadixObject node, boolean active) {
                widgets.add(node);
            }
        }, true);

        Collections.sort(widgets, new Comparator<RadixObject>() {
            @Override
            public int compare(RadixObject o1, RadixObject o2) {
                final String className1 = AdsUIUtil.getUiClassName((AdsRwtWidgetDef) o1);
                final String className2 = AdsUIUtil.getUiClassName((AdsRwtWidgetDef) o2);

                return getRate(className2) - getRate(className1);
            }

            private int getRate(String className) {
                switch (className) {
                    case AdsMetaInfo.RWT_ACTION:
                        return 10;
                    default:
                        return 0;
                }
            }
        });

        for (RadixObject wdg : widgets) {
            getInstance((AdsRwtWidgetDef) wdg).writeInitialization(printer, defferedAnchors);
        }

        List<AdsRwtWidgetDef> keys = new ArrayList<>(defferedAnchors.keySet());
        Collections.sort(keys, new Comparator<AdsRwtWidgetDef>() {
            @Override
            public int compare(AdsRwtWidgetDef o1, AdsRwtWidgetDef o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        for (AdsRwtWidgetDef key : keys) {
            RwtWidgetWriter w = getInstance(key);
            AdsUIProperty.AnchorProperty value = defferedAnchors.get(key);
            boolean cast = isValEditorController(key.getClassName());
            w.writeAnchorProperty(printer, value.getTop(), "Top", cast);
            w.writeAnchorProperty(printer, value.getLeft(), "Left", cast);
            w.writeAnchorProperty(printer, value.getBottom(), "Bottom", cast);
            w.writeAnchorProperty(printer, value.getRight(), "Right", cast);
        }
        AdsUIUtil.visitUI(widget, new AdsUIUtil.IVisitorUI() {
            @Override
            public void visit(RadixObject node, boolean active) {

                getInstance((AdsRwtWidgetDef) node).writePanelGridInitialization(printer);
            }
        }, true);

        AdsUIUtil.visitUI(widget, new AdsUIUtil.IVisitorUI() {
            @Override
            public void visit(RadixObject node, boolean active) {

                if (node != widget && AdsUIUtil.isCustomWidget(node)) {
                    printer.print("this.");
                    printer.print(((AdsRwtWidgetDef) node).getId());
                    printer.println(".open();");
                }
                String className = AdsUIUtil.getUiClassName(node);
                if (AdsMetaInfo.RWT_PROPERTIES_GRID.equals(className) || AdsMetaInfo.RWT_PROP_EDITOR.equals(className) || AdsMetaInfo.RWT_PROP_LABEL.equals(className)) {
                    printer.print("this.");
                    printer.print(((AdsRwtWidgetDef) node).getId());
                    printer.println(".bind();");
                }
            }
        }, true);



    }

    private static void writeCustomType(CodePrinter printer, AdsAbstractUIDef ui) {
        WriterUtils.writePackage(printer, ui, UsagePurpose.WEB_EXECUTABLE);
        printer.print(".");
        printer.print(JavaSourceSupport.getName(ui, printer instanceof IHumanReadablePrinter, true));
    }

    public static boolean writeWidgetType(AdsRwtWidgetDef widget, CodePrinter printer) {
        if (isValEditorController(widget.getClassName())) {
            String widgetClassName = getValEditorClassName(widget.getClassName(), widget, printer);
            if (widgetClassName == null) {
                return false;
            }
            printer.print(widgetClassName);
        } else {
            if (AdsUIUtil.isCustomWidget(widget)) {
                AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(widget);
                if (customUI == null) {
                    return false;
                }
                writeCustomType(printer, customUI);
            } else if (AdsMetaInfo.RWT_PROP_EDITOR.equals(widget.getClassName())) {
                printer.print("org.radixware.wps.views.editor.property.AbstractPropEditor");//RADIX-8121
            } else {
                if (printer instanceof IHumanReadablePrinter) {
                    RadixObject ro = widget.getContainer();
                    String name;
                    if (ro instanceof AdsAbstractUIDef) {
                        name = ro.getName();
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    } else {
                        name = widget.getClassName();
                    }
                    printer.print(name);
                } else {
                    printer.print(widget.getClassName());
                }
            }
        }
        return true;
    }

    private boolean writeDeclaration(final CodePrinter printer) {
        printer.print("public ");

        if (!writeWidgetType(widget, printer)) {
            return false;
        }
        printer.printSpace();
        final char[] varName = JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter);
        printer.print(varName);
        printer.printlnSemicolon();
        if (AdsMetaInfo.RWT_TAB_SET.equals(widget.getClassName())) {
            for (AdsRwtWidgetDef tab : widget.getWidgets()) {
                final char[] tabClassName = JavaSourceSupport.getName(tab, printer instanceof IHumanReadablePrinter, true);
                final char[] tabName = JavaSourceSupport.getName(tab, printer instanceof IHumanReadablePrinter);
                printer.print("public ");
                //printer.print(tab.getClassName());
                if (!writeWidgetType(tab, printer)) {
                    return false;
                }
                printer.printSpace();
                printer.print(tabClassName);
                printer.printlnSemicolon();

                if (!writeWidgetType(tab, printer)) {
                    return false;
                }
                printer.printSpace();
                printer.print("get");
                printer.print(tabClassName);
                printer.print("(){ return  ");
                printer.print(tabName);
                printer.println(";}");
            }
        }

        //weite getter 
        printer.print("public ");

        if (!writeWidgetType(widget, printer)) {
            return false;
        }
        printer.printSpace();
        printer.print("get");
        printer.print(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter, true));
        printer.print("(){ return  ");
        printer.print(varName);
        printer.println(";}");

        return true;
    }

    private void writePanelGridInitialization(final CodePrinter printer) {
        final char[] varName = JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter);
        if (widget.isGridPanel()) {
            printer.println("//============ " + widget.getQualifiedName() + "  child setup ==============");
            AdsRwtWidgetDef.PanelGrid grid = widget.getPanelGrid();

            for (AdsRwtWidgetDef.PanelGrid.Row row : grid.getRows()) {
                printer.println("{");
                printer.enterBlock();
                printer.print(AdsMetaInfo.RWT_UI_PANEL);
                printer.print(".Table.Row ");
                printer.print("_row_");
                printer.print(" = this.");
                printer.print(varName);
                if (widget.getClassName().equals(AdsMetaInfo.RWT_UI_GROUP_BOX)) {
                    printer.print(".getPanel()");
                }
                printer.println(".getTable().addRow();");

                for (AdsRwtWidgetDef.PanelGrid.Row.Cell cell : row.getCells()) {
                    printer.println("{");
                    printer.enterBlock();
                    printer.print(AdsMetaInfo.RWT_UI_PANEL);
                    printer.println(".Table.Row.Cell _cell_ = _row_.addCell();");
                    AdsRwtWidgetDef target = cell.findWidget();
                    if (target != null) {

                        printer.print("_cell_.setComponent(");
                        printer.print("this.");
                        printer.print(JavaSourceSupport.getName(target, printer instanceof IHumanReadablePrinter));
                        printer.println(");");
                    }
                    printer.leaveBlock();
                    printer.println("}");

                }
                printer.leaveBlock();
                printer.println("}");
            }

        } else if (AdsMetaInfo.RWT_SPLITTER.equals(widget.getClassName())) {
            int index = 0;
            float tw = 0;
            for (AdsRwtWidgetDef w : widget.getWidgets()) {
                final char[] childName = JavaSourceSupport.getName(w, printer instanceof IHumanReadablePrinter);
                printer.print(childName);
                printer.println(".setTop(0);");
                printer.print(childName);
                printer.println(".setLeft(0);");
                printer.print(childName);
                printer.println(".setVCoverage(100);");
                printer.print(childName);
                printer.println(".setHCoverage(100);");
                printer.print(varName);
                tw += w.getWeight();
                printer.print(".setPart(");
                printer.print(index);
                printer.printComma();
                printer.print(tw + "f");
                printer.println(");");
                index++;
            }
        }
    }

    private boolean writeInitialization(final CodePrinter printer, Map<AdsRwtWidgetDef, AdsUIProperty.AnchorProperty> defferedAnchors) {
        final char[] varName = JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter);
        printer.println("//============ " + widget.getQualifiedName() + " ==============");
        AdsRwtWidgetDef ownerWidget = widget.getOwnerWidget();
        final char[] ownerName = ownerWidget == null? null : JavaSourceSupport.getName(ownerWidget, printer instanceof IHumanReadablePrinter);
        printer.print("this.");
        printer.print(varName);
        boolean cast = false;
        if (widget == AdsUIUtil.getUiDef(widget).getWidget()) {
            printer.println(" = this;");
        } else {

            if (isValEditorController(widget.getClassName())) {
                String controllerClassName = getEditorControllerClassName(widget.getClassName(), widget, printer);
                if (controllerClassName == null) {
                    printer.println("");
                    return false;
                }
                cast = true;
                printer.print(" = new ");
                printer.print(controllerClassName);
                if (AdsMetaInfo.RWT_VAL_ARR_EDITOR.equals(widget.getClassName())) {

                    AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ARRAY_TYPE);
                    if (prop instanceof AdsUIProperty.EnumValueProperty) {
                        EArrayClassName arrTypeName = (EArrayClassName) ((AdsUIProperty.EnumValueProperty) prop).value;
                        if (arrTypeName == null) {
                            return false;
                        } else {
                            printer.print("(getEnvironment(),");
                            WriterUtils.writeEnumFieldInvocation(printer, arrTypeName.getRadixEnum());
                            printer.println("," + arrTypeName.getValue() + ".class).getValEditor();");
                        }
                    } else {
                        return false;
                    }

                } else {
                    if (widget.getClassName().equals(AdsMetaInfo.RWT_VAL_ENUM_EDITOR)) {
                        AdsUIProperty enumerationProp = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ENUMERATION);
                        if (enumerationProp != null && enumerationProp instanceof AdsUIProperty.EnumRefProperty) {
                            AdsEnumDef enumeration = ((AdsUIProperty.EnumRefProperty) enumerationProp).findEnum();
                            if (enumeration != null) {
                                Id type = enumeration.getId();
                                printer.println("(getEnvironment(), ");
                                WriterUtils.writeIdUsage(printer, type);
                                printer.println(").getValEditor();");
                            } else {
                                printer.println("(getEnvironment()).getValEditor();");
                            }
                        } else {
                            printer.println("(getEnvironment()).getValEditor();");
                        }
                    } else {
                        printer.println("(getEnvironment()).getValEditor();");
                    }
                }
            } else if (AdsMetaInfo.RWT_PROP_EDITOR.equals(widget.getClassName())) {
                AdsUIProperty.PropertyRefProperty prop = (AdsUIProperty.PropertyRefProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.PROPERTY);
                if (prop != null) {
                    if (AdsMetaInfo.RWT_PROPERTIES_GRID.equals(ownerWidget.getClassName())) {

                        //public final void addEditor(UIObject editor, int col, int row) {
                        int col = 0;
                        AdsUIProperty.IntProperty cp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.GRID_COLUMN);
                        if (cp != null) {
                            col = cp.value;
                        }
                        int row = -1;
                        AdsUIProperty.IntProperty rp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.GRID_ROW);
                        if (rp != null) {
                            row = rp.value;
                        }
                        int span = -1;
                        AdsUIProperty.IntProperty sp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.COL_SPAN);
                        if (sp != null) {
                            span = sp.value;
                        }

                        boolean needStick = true;
                        //stick to left is requred when empty space above property editor is required
                        printer.print(" = ");
                        printer.print("this.");
                        printer.print(ownerName);
                        printer.print(".addProperty(");
                        writePropValue(printer, prop);
                        printer.printComma();
                        printer.print(col);
                        printer.printComma();
                        printer.print(row);
                        printer.printComma();
                        printer.print(span);

                        if (needStick) {
                            printer.printComma();
                            printer.print(true);
                            printer.printComma();
                            printer.print(false);
                        }
                        printer.println(");");

                    } else {

                        printer.print(" = (");
                        printer.print("org.radixware.wps.views.editor.property.AbstractPropEditor");
                        //printer.print(AdsMetaInfo.RWT_PROP_EDITOR);//radix-8121
                        printer.print(")");
                        writePropValue(printer, prop);
                        printer.println(".createPropertyEditor();");
                    }
                }
            } else {
                printer.print(" = new ");

                if (AdsUIUtil.isCustomWidget(widget)) {
                    AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(widget);
                    if (customUI == null) {
                        return false;
                    }
                    writeCustomType(printer, customUI);
                } else {
                    printer.print(widget.getClassName());
                }


                printer.print('(');
                writeWidgetConstructorParams(printer);
                printer.print(')');
                printer.printlnSemicolon();

                if (AdsMetaInfo.RWT_EMBEDDED_EDITOR.equals(widget.getClassName())) {
                    final AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);

                    if (prop.getExplorerItemId() != null) {
                        printer.print("this.");
                        printer.print(varName);
                        printer.print(".setExplorerItem(model, ");
                        WriterUtils.writeIdUsage(printer, prop.getExplorerItemId());
                        printer.println(");");
                    }
                }

                if (AdsMetaInfo.RWT_EDITOR_PAGE.equals(widget.getClassName())) {
                    printer.print(varName);
                    printer.print(".setParent(");
                    printer.print(ownerName);
                    printer.println(");");
                }

            }
        }


        writeWidgetProperties(printer, ownerWidget, defferedAnchors);

        if (ownerWidget != null) {
            if (AdsMetaInfo.RWT_PROPERTIES_GRID.equals(ownerWidget.getClassName())) {
                //DO nothing
            } else if (AdsMetaInfo.RWT_LABELED_EDIT_GRID.equals(ownerWidget.getClassName())) {

                //public final void addEditor(UIObject editor, int col, int row) {
                int col = 0;
                AdsUIProperty.IntProperty cp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.GRID_COLUMN);
                if (cp != null) {
                    col = cp.value;
                }
                int row = -1;
                AdsUIProperty.IntProperty rp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.GRID_ROW);
                if (rp != null) {
                    row = rp.value;
                }
                printer.print("this.");
                printer.print(ownerName);
                printer.print(".addEditor(");
                writeWidgetWithCast(printer, "this." + new String(varName), cast);
                printer.printComma();
                printer.print(col);
                printer.printComma();
                printer.print(row);
                printer.println(");");

            } else if (!ownerWidget.isGridPanel() && !widget.isActionWidget()) {

                if (AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER.equals(ownerWidget.getClassName())) {

                    int col = -1;
                    int row = -1;
                    int rs = 1;
                    int cs = 1;

                    AdsUIProperty.IntProperty cp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.COL);
                    if (cp != null) {
                        col = cp.value;
                    }

                    AdsUIProperty.IntProperty rp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ROW);
                    if (rp != null) {
                        row = rp.value;
                    }

                    AdsUIProperty.IntProperty rsp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ROW_SPAN);
                    if (rsp != null) {
                        rs = rsp.value;
                    }

                    AdsUIProperty.IntProperty csp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.COL_SPAN);
                    if (csp != null) {
                        cs = csp.value;
                    }

                    cast = true;

                    printer.print("this.");
                    printer.print(ownerName);
                    printer.print(".add(");
                    writeWidgetWithCast(printer, "this." + new String(varName), cast);
                    printer.printComma();
                    printer.print(row);
                    printer.printComma();
                    printer.print(col);
                    printer.println(");");

                    if (cs > 1) {
                        printer.print("this.");
                        printer.print(ownerName);
                        printer.print(".setColSpan(");
                        printer.print(row);
                        printer.printComma();
                        printer.print(col);
                        printer.printComma();
                        printer.print(cs);
                        printer.println(");");
                    }

                    if (rs > 1) {
                        printer.print("this.");
                        printer.print(ownerName);
                        printer.print(".setRowSpan(");
                        printer.print(row);
                        printer.printComma();
                        printer.print(col);
                        printer.printComma();
                        printer.print(rs);
                        printer.println(");");
                    }

                    return true;
                } else {
                    printer.print("this.");
                    printer.print(ownerName);
                    printer.print(".add(");
                    if (cast) {
                        printer.print("(" + AdsMetaInfo.RWT_UI_OBJECT + ")");
                    }
                    printer.print("this.");
                    printer.print(varName);
                    printer.println(");");

                    if (AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER.equals(ownerWidget.getClassName()) || AdsMetaInfo.RWT_HORIZONTAL_BOX_CONTAINER.equals(ownerWidget.getClassName())) {
                        AdsUIProperty.BooleanProperty expand = (AdsUIProperty.BooleanProperty) widget.getProperties().getByName(AdsWidgetProperties.EXPAND);
                        if (expand != null && expand.value) {
                            printer.print("this.");
                            printer.print(ownerName);
                            printer.print(".setAutoSize(");
                            if (cast) {
                                printer.print("(" + AdsMetaInfo.RWT_UI_OBJECT + ")");
                            }
                            printer.print("this.");
                            printer.print(varName);
                            printer.println(",true);");
                        }
                    }
                }
            }


            if (AdsMetaInfo.RWT_TAB_SET.equals(widget.getClassName())) {
                for (AdsRwtWidgetDef tab : widget.getWidgets()) {
                    printer.print("this.");
                    printer.print(JavaSourceSupport.getName(tab, printer instanceof IHumanReadablePrinter));
                    printer.print(" = ");
                    printer.print(varName);
                    printer.print(".addTab(");
                    AdsUIProperty prop = tab.getProperties().getByName(AdsWidgetProperties.TITLE);
                    if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
                        writePropValue(printer, prop);
                    } else {
                        printer.printStringLiteral("");
                    }
                    printer.println(");");
                }
            }

        }
        
        
        boolean writeBind = false;
        if (AdsMetaInfo.RWT_COMMAND_PUSH_BUTTON.equals(widget.getClassName())) {
            writeBind = true;
        } else if (AdsMetaInfo.RWT_EMBEDDED_EDITOR.equals(widget.getClassName())) {
            AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
            writeBind = prop != null && !prop.isEmpty();
        } else if (AdsMetaInfo.RWT_EMBEDDED_SELECTOR.equals(widget.getClassName())) {
            AdsUIProperty.EmbeddedSelectorOpenParamsProperty prop = (AdsUIProperty.EmbeddedSelectorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
            writeBind = prop != null && !prop.isEmpty();
        } else if (AdsMetaInfo.RWT_EDITOR_PAGE.equals(widget.getClassName())) {
            writeBind = true;
        }

        if (writeBind) {
            printer.print("this.");
            printer.print(varName);
            printer.println(".bind();");
        }
        
        return true;

    }

    private void writeWidgetProperties(final CodePrinter printer, AdsRwtWidgetDef ownerWidget, Map<AdsRwtWidgetDef, AdsUIProperty.AnchorProperty> defferedAnchors) {
        boolean cast = isValEditorController(widget.getClassName());
        for (AdsUIProperty prop : widget.getProperties()) {
            String propName = prop.getName();
            if (AdsWidgetProperties.AUTO_HEIGHT.equals(propName) || AdsWidgetProperties.AUTO_WIDTH.equals(propName) 
                    || AdsWidgetProperties.EDITOR_PAGE.equals(propName) || AdsWidgetProperties.LABEL.equals(propName) 
                    || AdsWidgetProperties.GRID_COLUMN.equals(propName) || AdsWidgetProperties.GRID_ROW.equals(propName) 
                    || AdsWidgetProperties.PROPERTY.equals(propName) || AdsWidgetProperties.EXPAND.equals(propName) 
                    || AdsWidgetProperties.COMMAND.equals(propName) || AdsWidgetProperties.OPEN_PARAMS.equals(propName) 
                    || AdsWidgetProperties.COL_SPAN.equals(propName) || AdsWidgetProperties.ENUMERATION.equals(propName) 
                    || AdsWidgetProperties.ITEM_TYPE.equals(propName) || AdsWidgetProperties.ARRAY_TYPE.equals(propName) 
                    || AdsWidgetProperties.ROW.equals(propName) || AdsWidgetProperties.COL.equals(propName) 
                    || AdsWidgetProperties.ROW_SPAN.equals(propName) || "rowNum".equals(propName) 
                    || "colNum".equals(propName)) {
                continue;
            } else if (AdsWidgetProperties.WINDOW_TITLE.equals(propName)) {
                AdsAbstractUIDef ui = widget.getOwnerUIDef();
                if (ui instanceof AdsRwtCustomDialogDef) {
                    continue;
                }
            } else if (AdsWidgetProperties.GEOMETRY.equals(propName)) {
                if (widget.isActionWidget()) {
                    continue;
                }
                boolean ownerIsGrid = ownerWidget != null && (ownerWidget.getClassName().equals(AdsMetaInfo.RWT_LABELED_EDIT_GRID) || ownerWidget.getClassName().equals(AdsMetaInfo.RWT_PROPERTIES_GRID));

                if (ownerWidget != null && ownerWidget.isGridPanel()) {
                } else {
                    Rectangle rect = ((AdsUIProperty.RectProperty) prop).getRectangle();
                    if (!AdsUIUtil.isUIRoot(widget) && !ownerIsGrid) {

                        writePropertySet(printer, "setTop", String.valueOf(rect.y), cast);
                        writePropertySet(printer, "setLeft", String.valueOf(rect.x), cast);
                    }
//                    if (!useHCoverage()) {
                    AdsUIProperty.BooleanProperty autoWidth = (AdsUIProperty.BooleanProperty) widget.getProperties().getByName(AdsWidgetProperties.AUTO_WIDTH);
                    if (!ownerIsGrid && (autoWidth == null || !autoWidth.value)) {
                        writePropertySet(printer, "setWidth", String.valueOf(rect.width), cast);
                    }
//                    } else {
//                        float hc = ((AdsUIProperty.FloatProperty) AdsUIUtil.getUiProperty(widget, "hCoverage")).value;
//                        writePropertySet(printer, "setHCoverage", "(float)" + String.valueOf(hc));
//                    }
//                    if (!useVCoverage()) {
                    AdsUIProperty.BooleanProperty autoHeight = (AdsUIProperty.BooleanProperty) widget.getProperties().getByName(AdsWidgetProperties.AUTO_HEIGHT);
                    if (autoHeight == null || !autoHeight.value) {
                        writePropertySet(printer, "setHeight", String.valueOf(rect.height), cast);
                    }
//                    } else {
//                        float vc = ((AdsUIProperty.FloatProperty) AdsUIUtil.getUiProperty(widget, "hCoverage")).value;
//                        writePropertySet(printer, "setVCoverage", "(float)" + String.valueOf(vc));
//                    }
                }
                continue;
            } else if (AdsWidgetProperties.STANDARD_BUTTONS.equals(propName)) {
                UIEnum[] buttonSet = ((AdsUIProperty.SetProperty) prop).getValues();
                StringBuilder sb = new StringBuilder();
                sb.append("java.util.EnumSet.of(");
                boolean first = true;
                for (UIEnum e : buttonSet) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }
                    EStandardButton button = (EStandardButton) e;
                    sb.append(button.getQualifiedWebValue());
                }
                sb.append(")");
                writePropertySet(printer, "setStandardButtons", sb.toString(), cast);
                continue;
            } else if (AdsWidgetProperties.ANCHOR.equals(propName)) {
                if (widget.isActionWidget()) {
                    continue;
                }
                AdsUIProperty.AnchorProperty anchorProperty = (AdsUIProperty.AnchorProperty) prop;

                defferedAnchors.put(widget, anchorProperty);

                continue;
            } else if ("hCoverage".equals(propName) || "vCoverage".equals(propName) || "useHCoverage".equals(propName) || "useVCoverage".equals(propName)) {
                continue;
            } else if (AdsWidgetProperties.BUTTON_BOX_VISIBLE.equals(propName)) {
                boolean bb = ((AdsUIProperty.BooleanProperty) prop).value;
                String str = String.valueOf(bb);
                writePropertySet(printer, "setButtonBoxVisible", str, cast);
                continue;
            } else if (widget.getClassName().equals(AdsMetaInfo.RWT_TOOL_BAR) && AdsWidgetProperties.ORIENTATION.equals(propName)) {
                AdsUIProperty.EnumValueProperty p = (AdsUIProperty.EnumValueProperty) prop;
                EOrientation or = (EOrientation) p.value;
                final char[] varName = JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter);
                if (or == EOrientation.Horizontal) {
                    printer.print(varName).print(".setHorizontal()");
                    printer.printlnSemicolon();
                } else {
                    printer.print(varName).print(".setVertical()");
                    printer.printlnSemicolon();
                }
                continue;
            } else if (AdsWidgetProperties.AUTO_RESIZE.equals(propName)) {
                boolean bb = ((AdsUIProperty.BooleanProperty) prop).value;
                if (bb == false) {
                    String str = String.valueOf(bb);
                    writePropertySet(printer, "setAdjustSizeEnabled", str, cast);
                }
                continue;
            }
            propName = Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
            if ("Text".equals(propName) && AdsMetaInfo.RWT_COMMAND_PUSH_BUTTON.equals(widget.getClassName())) {
                propName = "Title";
            }

            String setterName = "set" + propName;
            writePropertySet(printer, setterName, prop, cast);
        }
    }

    private void writeAnchorProperty(CodePrinter printer, AdsUIProperty.AnchorProperty.Anchor anchor, String name, boolean cast) {
        if (anchor != null) {
            if (anchor.refId != null) {
                String ref = anchor.refId.toString();
                if (printer instanceof IHumanReadablePrinter) {
                    AdsRwtWidgetDef adsRwtWidgetDef = widget.getOwnerWidget().findWidgetById(anchor.refId);
                    if (adsRwtWidgetDef != null) {
                        ref = adsRwtWidgetDef.getName();
                    }
                }
                
                writePropertySet(printer, "getAnchors().set" + name, "new org.radixware.wps.rwt.UIObject.Anchors.Anchor(" + anchor.part + "f," + anchor.offset + ",("
                        + AdsMetaInfo.RWT_UI_OBJECT
                        + ")" + ref + ")", cast);
            } else {
                writePropertySet(printer, "getAnchors().set" + name, "new org.radixware.wps.rwt.UIObject.Anchors.Anchor(" + anchor.part + "f," + anchor.offset + ")", cast);
            }
        }
    }

    private void writePropertySet(final CodePrinter printer, String methodName, String propertyValue, boolean cast) {
        writeWidgetWithCast(printer, new String(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter)), cast);

        printer.print('.');
        printer.print(methodName);
        printer.print('(');
        printer.print(propertyValue);
        printer.print(')');
        printer.printlnSemicolon();
    }

    private void writePropertySet(final CodePrinter printer, String methodName, AdsUIProperty prop, boolean cast) {
        writeWidgetWithCast(printer, new String(JavaSourceSupport.getName(widget, printer instanceof IHumanReadablePrinter)), cast);

        printer.print('.');
        printer.print(methodName);
        printer.print('(');
        writePropValue(printer, prop);
        printer.print(')');
        printer.printlnSemicolon();
    }

    private void writeWidgetWithCast(CodePrinter printer, String id, boolean cast) {
        if (cast) {
            printer.print("((");
            printer.print(AdsMetaInfo.RWT_UI_OBJECT);
            printer.print(")");
            printer.print(id);
            printer.print(")");
        } else {
            printer.print(id);
        }
    }
    private static final String PROPERTY_REF = "org.radixware.kernel.common.client.models.items.properties.PropertyRef";
    private static final String PROPERTY_REFERENCE = "org.radixware.kernel.common.client.models.items.properties.PropertyReference";

    private String getViewModel() {
        switch (AdsUIUtil.getUiDef(widget).getId().getPrefix()) {
            case CUSTOM_FORM_DIALOG:
                return "getFormModel()";
            case CUSTOM_EDITOR_PAGE:
                return "getModel()";
            default:
                return "getEntityModel()";
        }
    }

    private void writeEmbeddedEditorConstructorParams(CodePrinter printer) {
        AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
        if (prop.getPropertyId() != null) {
            printer.print("model.getEnvironment(), (");
            printer.print(PROPERTY_REFERENCE);
            printer.print(")getModel().getProperty(");
            WriterUtils.writeIdUsage(printer, prop.getPropertyId());
            printer.print(")");
        } else if (prop.getExplorerItemId() != null) {
            printer.print("model.getEnvironment()");
        } else if (prop.getClassId() != null && prop.getEditorPresentationId() != null) {
            printer.print("model.getEnvironment(), ");
            WriterUtils.writeIdUsage(printer, prop.getClassId());
            printer.print(", ");
            WriterUtils.writeIdUsage(printer, prop.getEditorPresentationId());
            printer.print(", null");
        } else {
            printer.print("model.getEnvironment()");
        }
    }

    private void writeEmbeddedSelectorConstructorParams(CodePrinter printer) {
        AdsUIProperty.EmbeddedSelectorOpenParamsProperty prop = (AdsUIProperty.EmbeddedSelectorOpenParamsProperty) AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.OPEN_PARAMS);
        if (prop.getPropertyId() != null) {
            printer.print("(org.radixware.wps.WpsEnvironment) model.getEnvironment(), (");
            printer.print(PROPERTY_REF);
            printer.print(")getModel().getProperty(");
            WriterUtils.writeIdUsage(printer, prop.getPropertyId());
            printer.print(")");
        } else if (prop.getExplorerItemId() != null) {
            printer.print("(org.radixware.wps.WpsEnvironment) model.getEnvironment(), this, ");
            WriterUtils.writeIdUsage(printer, prop.getExplorerItemId());
        } else {
            printer.print("model.getEnvironment()");
        }
    }

    protected void writeWidgetConstructorParams(CodePrinter printer) {
        if (AdsUIUtil.isCustomWidget(widget) || widget.isActionWidget()) {
            printer.print("Environment");
        } else if (AdsMetaInfo.RWT_EMBEDDED_EDITOR.equals(widget.getClassName())) {
            writeEmbeddedEditorConstructorParams(printer);
        } else if (AdsMetaInfo.RWT_EMBEDDED_SELECTOR.equals(widget.getClassName())) {
            writeEmbeddedSelectorConstructorParams(printer);
        } else if (AdsMetaInfo.RWT_LABELED_EDIT_GRID.equals(widget.getClassName())) {
            printer.println("new " + AdsMetaInfo.RWT_LABELED_EDIT_GRID + ".AbstractEditor2LabelMatcher(){");
            printer.enterBlock();
            printer.println("protected " + AdsMetaInfo.RWT_UI_OBJECT + " createLabelComonent(" + AdsMetaInfo.RWT_UI_OBJECT + " editorComponent) {");
            printer.enterBlock();
            boolean first = true;
            printer.println("String label = \"\";");
            for (AdsRwtWidgetDef w : widget.getWidgets()) {
                if (first) {
                    first = false;
                } else {
                    printer.print("else ");
                }

                AdsUIProperty.LocalizedStringRefProperty prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(w, AdsWidgetProperties.LABEL);
                if (prop != null) {
                    printer.println("if(editorComponent==" + w.getId().toString() + ")");
                    printer.enterBlock();
                    printer.print("label=");
                    writePropValue(printer, prop);
                    printer.leaveBlock();
                    printer.printlnSemicolon();
                }
            }
            printer.print(AdsMetaInfo.RWT_UI_LABEL);
            printer.print(" result = new ");
            printer.print(AdsMetaInfo.RWT_UI_LABEL);
            printer.println("(label);");
            printer.println("result.setTextWrapDisabled(true);");
            printer.println("return result;");
            printer.leaveBlock();
            printer.println("}");
            printer.leaveBlock();
            printer.print("}");
        } else if (AdsMetaInfo.RWT_COMMAND_PUSH_BUTTON.equals(widget.getClassName())) {
            AdsUIProperty.CommandRefProperty command = (AdsUIProperty.CommandRefProperty) widget.getProperties().getByName(AdsWidgetProperties.COMMAND);
            if (command != null) {
                writePropValue(printer, command);
            } else {
                WriterUtils.writeNull(printer);
            }

        } else if (AdsMetaInfo.RWT_PROP_LABEL.equals(widget.getClassName())) {
            AdsUIProperty.PropertyRefProperty prop = (AdsUIProperty.PropertyRefProperty) widget.getProperties().getByName(AdsWidgetProperties.PROPERTY);
            if (prop != null) {
                writePropValue(printer, prop);
            } else {
                WriterUtils.writeNull(printer);
            }
        } else if (AdsMetaInfo.RWT_EDITOR_PAGE.equals(widget.getClassName())) {

            AdsUIProperty.EditorPageRefProperty prop = (AdsUIProperty.EditorPageRefProperty) widget.getProperties().getByName(AdsWidgetProperties.EDITOR_PAGE);
            if (prop != null && prop.getEditorPageId() != null) {
                printer.print(getViewModel() + ".getEditorPage(");
                WriterUtils.writeIdUsage(printer, prop.getEditorPageId());
                printer.println(")");
            }
        }
    }

    private void writePropValue(CodePrinter printer, AdsUIProperty prop) {
        if (prop == null) {
            boolean ok = true;
        }
        assert prop != null : "Property is null";
        //final String name = prop.getName();
        if (prop instanceof AdsUIProperty.StringProperty) {
            AdsUIProperty.StringProperty p = (AdsUIProperty.StringProperty) prop;
            printer.printStringLiteral(p.value);
        } else if (prop instanceof AdsUIProperty.BooleanValueProperty) {
            AdsUIProperty.BooleanValueProperty p = (AdsUIProperty.BooleanValueProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.LongProperty) {
            AdsUIProperty.LongProperty p = (AdsUIProperty.LongProperty) prop;
            printer.print(String.valueOf(p.value));
        } else if (prop instanceof AdsUIProperty.BooleanProperty) {
            AdsUIProperty.BooleanProperty p = (AdsUIProperty.BooleanProperty) prop;
            printer.print(String.valueOf(p.value));
        } //else if (prop instanceof AdsUIProperty.ColorProperty) {
        //            Color c = ((AdsUIProperty.ColorProperty) prop).color;
        //            printer.print("new " + getGuiClassName("QColor") + "(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha() + ")");
        //        } else if (prop instanceof AdsUIProperty.BrushProperty) {
        //            Color c = ((AdsUIProperty.BrushProperty) prop).color;
        //            printer.print("new " + getGuiClassName("QBrush") + "(new " + getGuiClassName("QColor") + "(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha() + "))");
        //        } else if (prop instanceof AdsUIProperty.CursorProperty) {
        //            AdsUIProperty.CursorProperty p = (AdsUIProperty.CursorProperty) prop;
        //            printer.print(String.valueOf(p.cursorType));
        //        } else if (prop instanceof AdsUIProperty.CursorShapeProperty) {
        //            AdsUIProperty.CursorShapeProperty p = (AdsUIProperty.CursorShapeProperty) prop;
        //            printer.print("new " + getGuiClassName("QCursor") + "(" + String.valueOf(p.shape.getQualifiedValue()) + ")");
        //        } else if (prop instanceof AdsUIProperty.DateProperty) {
        //            AdsUIProperty.DateProperty p = (AdsUIProperty.DateProperty) prop;
        //            printer.print("new " + getCoreClassName("QDate") + "(" + p.year + ", " + p.month + ", " + p.day + ")");
        //        } else if (prop instanceof AdsUIProperty.DateTimeProperty) {
        //            AdsUIProperty.DateTimeProperty p = (AdsUIProperty.DateTimeProperty) prop;
        //            printer.print("new " + getCoreClassName("QDateTime") + "(new " + getCoreClassName("QDate") + "(" + p.year + ", " + p.month + ", " + p.day + "), new " + getCoreClassName("QTime") + "(" + p.hour + ", " + p.minute + ", " + p.second + "))");
        //        } else if (prop instanceof AdsUIProperty.TimeProperty) {
        //            AdsUIProperty.TimeProperty p = (AdsUIProperty.TimeProperty) prop;
        //            printer.print("new " + getCoreClassName("QTime") + "(" + p.hour + ", " + p.minute + ", " + p.second + ")");
        //        } else if (prop instanceof AdsUIProperty.DoubleProperty) {
        //            AdsUIProperty.DoubleProperty p = (AdsUIProperty.DoubleProperty) prop;
        //            printer.print(String.valueOf(p.value));
        //        }
        else if (prop instanceof AdsUIProperty.EnumValueProperty) {
            AdsUIProperty.EnumValueProperty p = (AdsUIProperty.EnumValueProperty) prop;
            if (widget.getClassName().equals(AdsMetaInfo.RWT_SPLITTER) && AdsWidgetProperties.ORIENTATION.equals(p.getName())) {
                EOrientation or = (EOrientation) p.value;
                if (or == EOrientation.Horizontal) {
                    printer.print(AdsMetaInfo.RWT_SPLITTER + ".Orientation.HORIZONTAL");
                } else {
                    printer.print(AdsMetaInfo.RWT_SPLITTER + ".Orientation.VERTICAL");
                }

            } else {
                printer.print(p.value.getQualifiedValue());
            }
        } //        } else if (prop instanceof AdsUIProperty.FloatProperty) {
        //            AdsUIProperty.FloatProperty p = (AdsUIProperty.FloatProperty) prop;
        //            printer.print(String.valueOf(p.value));
        //        } else if (prop instanceof AdsUIProperty.FontProperty) {
        //            AdsUIProperty.FontProperty p = (AdsUIProperty.FontProperty) prop;
        else if (prop instanceof AdsUIProperty.ImageProperty) {
            AdsUIProperty.ImageProperty p = (AdsUIProperty.ImageProperty) prop;
            if (p.getImageId() != null) {
                printer.print("(org.radixware.wps.icons.WpsIcon)getEnvironment().getDefManager().getImage(");
                WriterUtils.writeIdUsage(printer, p.getImageId());
                printer.print(")");
            } else {
                WriterUtils.writeNull(printer);
            }
        } //        } else if (prop instanceof AdsUIProperty.RectProperty) {
        //            AdsUIProperty.RectProperty p = (AdsUIProperty.RectProperty) prop;
        //            printer.print("new " + getCoreClassName("QRect") + "(" + p.x + ", " + p.y + ", " + p.width + ", " + p.height + ")");
        //        } else if (prop instanceof AdsUIProperty.SetProperty) {
        //            AdsUIProperty.SetProperty p = (AdsUIProperty.SetProperty) prop;
        //            if (name.equals("alignment") || name.equals("textAlignment")) {
        //                printer.print("new " + getCoreClassName("Qt.Alignment("));
        //                UIEnum[] values = p.getValues();
        //                for (int i = 0; i < values.length; i++) {
        //                    printer.print(values[i].getQualifiedValue());
        //                    if (i < values.length - 1) {
        //                        printer.print(", ");
        //                    }
        //                }
        //                printer.print(")");
        //                if (name.equals("textAlignment") || AdsUIUtil.getUiClassName(AdsUIUtil.getOwner(prop)).equals(AdsMetaInfo.GROUP_BOX_CLASS)) {
        //                    printer.print(".value()");
        //                }
        //            } else if (name.equals("standardButtons")) {
        //                UIEnum[] values = p.getValues();
        //                for (int i = 0; i < values.length; i++) {
        //                    printer.print(values[i].getQualifiedValue());
        //                    if (i < values.length - 1) {
        //                        printer.print(", ");
        //                    }
        //                }
        //            } else {
        //                WriterUtils.writeNull(printer);
        //            }
        else if (prop instanceof AdsUIProperty.SizeProperty) {
            AdsUIProperty.SizeProperty p = (AdsUIProperty.SizeProperty) prop;
            printer.print(p.width + ", " + p.height);
            //  } else if (prop instanceof AdsUIProperty.SizePolicyProperty) {
            //            AdsUIProperty.SizePolicyProperty p = (AdsUIProperty.SizePolicyProperty) prop;
            //            printer.print("new " + getGuiClassName("QSizePolicy") + "(" + p.hSizeType.getQualifiedValue() + ", " + p.vSizeType.getQualifiedValue() + ")");
            //        } else if (prop instanceof AdsUIProperty.StringProperty) {
            //            AdsUIProperty.StringProperty p = (AdsUIProperty.StringProperty) prop;
            //            printer.printStringLiteral(p.value);
            //        }
        } else if (prop instanceof AdsUIProperty.IntProperty) {
            AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) prop;
            printer.print(String.valueOf(p.value));
        } //else if (prop instanceof AdsUIProperty.UrlProperty) {
        //            AdsUIProperty.UrlProperty p = (AdsUIProperty.UrlProperty) prop;
        else if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
            AdsUIProperty.LocalizedStringRefProperty p = (AdsUIProperty.LocalizedStringRefProperty) prop;
            if (p.getStringId() != null) {
                WriterUtils.writeNLSInvocation(printer, AdsUIUtil.getUiDef(p).getLocalizingBundleId(), p.getStringId(), widget.getOwnerUIDef(), UsagePurpose.WEB_EXECUTABLE, false);
//                printer.print(TEXT_DEF_MANAGER + ".getMlStringValue(");
//                WriterUtils.writeIdUsage(printer, AdsUIUtil.getUiDef(p).getId());
//                printer.print(", ");
//                WriterUtils.writeIdUsage(printer, p.getStringId());
//                printer.print(")");
            } else {
                WriterUtils.writeNull(printer);
            }
        } else if (prop instanceof AdsUIProperty.IdListProperty) {
            List<Id> ids = ((AdsUIProperty.IdListProperty) prop).getIds();
            printer.print("new org.radixware.kernel.common.client.widgets.actions.Action[]{");
            boolean first = true;
            for (Id id : ids) {
                if (first) {
                    first = false;
                } else {
                    printer.printComma();
                }
                printer.print("this.");
                printer.print(AdsUIWriter.getListItemName(widget, id, printer instanceof IHumanReadablePrinter));
            }
            printer.print('}');
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
        } //        } else if (prop instanceof AdsUIProperty.EditorPageRefProperty) {
        //            AdsUIProperty.EditorPageRefProperty p = (AdsUIProperty.EditorPageRefProperty) prop;
        //            if (p.getEditorPageId() == null) {
        //                WriterUtils.writeNull(printer);
        //            } else {
        //                printer.print(getViewModel() + ".getEditorPage(");
        //                WriterUtils.writeIdUsage(printer, p.getEditorPageId());
        //                printer.print(")");
        //            }
        else if (prop instanceof AdsUIProperty.CommandRefProperty) {
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

    private static boolean isValEditorController(String className) {
        return AdsMetaInfo.RWT_VAL_BIN_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_ENUM_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_INT_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_LIST_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_NUM_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_REF_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_STR_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_TIME_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_DATE_TIME_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_ARR_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_BOOL_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_FILE_PATH_EDITOR.equals(className)
                || AdsMetaInfo.RWT_VAL_TIME_INTERVAL_EDITOR.equals(className);
    }

    private String getEditorControllerClassName(String className, AdsRwtWidgetDef widget, final CodePrinter cp) {
        if (AdsMetaInfo.RWT_VAL_BIN_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValBinEditorController";
        } else if (AdsMetaInfo.RWT_VAL_ENUM_EDITOR.equals(className)) {
            AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ENUMERATION);
            if (prop instanceof AdsUIProperty.EnumRefProperty) {
                AdsEnumDef enumeration = ((AdsUIProperty.EnumRefProperty) prop).findEnum();
                if (enumeration == null) {
                    return null;
                }
                AdsType type = enumeration.getType(enumeration.getItemType(), null);
                CodePrinter printer = CodePrinter.Factory.newJavaPrinter(cp);
                type.getJavaSourceSupport().getCodeWriter(UsagePurpose.WEB_EXECUTABLE).writeCode(printer);
                return "org.radixware.wps.views.editors.valeditors.ValEnumEditorController<" + printer.toString() + ">";
            } else {
                return null;
            }
        } else if (AdsMetaInfo.RWT_VAL_INT_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValIntEditorController";
        } else if (AdsMetaInfo.RWT_VAL_LIST_EDITOR.equals(className)) {

            AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ITEM_TYPE);
            if (prop instanceof AdsUIProperty.TypeDeclarationProperty) {
                AdsType type = ((AdsUIProperty.TypeDeclarationProperty) prop).getType().resolve(widget).get();
                if (type == null) {
                    return null;
                }

                CodePrinter printer = CodePrinter.Factory.newJavaPrinter(cp);
                type.getJavaSourceSupport().getCodeWriter(UsagePurpose.WEB_EXECUTABLE).writeCode(printer);
                return "org.radixware.wps.views.editors.valeditors.ValListEditorController<" + printer.toString() + ">";
            } else {
                return null;
            }

        } else if (AdsMetaInfo.RWT_VAL_NUM_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValNumEditorController";
        } else if (AdsMetaInfo.RWT_VAL_REF_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValReferenceEditorController";
        } else if (AdsMetaInfo.RWT_VAL_STR_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValStrEditorController";
        } else if (AdsMetaInfo.RWT_VAL_TIME_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValTimeEditorController";
        } else if (AdsMetaInfo.RWT_VAL_TIME_INTERVAL_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValTimeIntervalEditorController";
        } else if (AdsMetaInfo.RWT_VAL_DATE_TIME_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValDateTimeEditorController";
        } else if (AdsMetaInfo.RWT_VAL_FILE_PATH_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValFilePathEditorController";
        } else if (AdsMetaInfo.RWT_VAL_BOOL_EDITOR.equals(className)) {
            return "org.radixware.wps.views.editors.valeditors.ValBoolEditorController";
        } else if (AdsMetaInfo.RWT_VAL_ARR_EDITOR.equals(className)) {

            AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ARRAY_TYPE);
            if (prop instanceof AdsUIProperty.EnumValueProperty) {
                EArrayClassName arrTypeName = (EArrayClassName) ((AdsUIProperty.EnumValueProperty) prop).value;
                if (arrTypeName == null) {
                    return null;
                }


                return "org.radixware.wps.views.editors.valeditors.ValArrEditorController<" + arrTypeName.getValue() + ">";
            } else {
                return null;
            }

        }
        return null;
    }

    private static String getValEditorClassName(String className, AdsRwtWidgetDef widget, final CodePrinter cp) {
        if (AdsMetaInfo.RWT_VAL_ENUM_EDITOR.equals(className)) {
            final AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ENUMERATION);
            if (prop instanceof AdsUIProperty.EnumRefProperty && ((AdsUIProperty.EnumRefProperty) prop).findEnum() != null) {
                final AdsEnumDef enumeration = ((AdsUIProperty.EnumRefProperty) prop).findEnum();
                if (enumeration == null) {
                    return null;
                }
                final AdsType type = enumeration.getType(enumeration.getItemType(), null);
                final CodePrinter printer = CodePrinter.Factory.newJavaPrinter(cp);
                type.getJavaSourceSupport().getCodeWriter(UsagePurpose.WEB_EXECUTABLE).writeCode(printer);
                return className.replace("<T,", "<" + printer.toString() + ",");
            } else {
                return null;
            }
        } else if (AdsMetaInfo.RWT_VAL_LIST_EDITOR.equals(className)) {

            final AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ITEM_TYPE);
            if (prop instanceof AdsUIProperty.TypeDeclarationProperty) {
                final AdsType type = ((AdsUIProperty.TypeDeclarationProperty) prop).getType().resolve(widget).get();
                if (type == null) {
                    return null;
                }

                final CodePrinter printer = CodePrinter.Factory.newJavaPrinter(cp);
                type.getJavaSourceSupport().getCodeWriter(UsagePurpose.WEB_EXECUTABLE).writeCode(printer);
                return className.replace("<T,", "<" + printer.toString() + ",");
            } else {
                return null;
            }

        } else if (AdsMetaInfo.RWT_VAL_ARR_EDITOR.equals(className)) {
            final AdsUIProperty prop = AdsUIUtil.getUiProperty(widget, AdsWidgetProperties.ARRAY_TYPE);
            if (prop instanceof AdsUIProperty.EnumValueProperty) {
                final EArrayClassName arrTypeName = (EArrayClassName) ((AdsUIProperty.EnumValueProperty) prop).value;
                if (arrTypeName == null) {
                    return null;
                }
                return className.replace("<T,", "<" + arrTypeName.getValue() + ",");
            } else {
                return null;
            }

        } else {
            return className;
        }
    }
}
