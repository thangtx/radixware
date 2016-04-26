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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.rwt.AbstractRwtCustomFormDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomReportDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;

public class AdsRwtUICodeWriter extends AbstractDefinitionWriter<AdsRwtUIDef> {

    public AdsRwtUICodeWriter(JavaSourceSupport support, AdsRwtUIDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected boolean writeExecutable(CodePrinter printer) {
        Definition userDef = null;
        if (def instanceof AdsRwtCustomReportDialogDef) {
            final AdsClassDef owner = ((AdsRwtCustomReportDialogDef) def).getOwnerClass();
            if (owner instanceof AdsUserReportClassDef) {
                userDef = def;
            }
        }
        if (userDef != null) {
            WriterUtils.writeUserDefinitionHeader(printer, userDef);
        }
        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        printer.println("@SuppressWarnings({\"unchecked\",\"rawtypes\"})");
        WriterUtils.writeMetaAnnotation(printer, def, false);
        printer.print("public class ");
        printer.print(def.getId());
        printer.print(" extends ");
        printer.print(def.getSuperClassName());
        printer.enterBlock();
        printer.println('{');
        RwtWidgetWriter.getInstance(def.getWidget()).writeWidgetFieldDecls(printer);
        writeConstructor(printer);

        if (def.getDefinitionType() == EDefType.CUSTOM_WIDGET_DEF) {
            printer.print("private static final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.print(" _definition_id_storage_field_=");
            WriterUtils.writeIdUsage(printer, def.getId());
            printer.println(";");
            printer.print("public final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" getId(){return _definition_id_storage_field_;}");
        }

        final String jsCode = def.getJsCode();
        if (jsCode != null && !jsCode.isEmpty()) {
            printer.println("protected String[] clientScriptsRequired() {");
            printer.enterBlock();
            printer.println("final String[] sr=super.clientScriptsRequired();");
            printer.println("if(sr != null && sr.length > 0){");
            printer.enterBlock();
            printer.println("String[] tmp = new String[sr.length+1];");
            printer.println("System.arraycopy(sr,0,tmp,0,sr.length);");
            printer.print("tmp[sr.length]=\"");
            WriterUtils.writePackage(printer, def, usagePurpose, '/');
            printer.print('/');
            printer.print(def.getId());
            printer.println(".js\";");
            printer.println("return tmp;");
            printer.leaveBlock();
            printer.print("}else");
            printer.enterBlock();
            printer.println('{');
            printer.print("return new String[]{\"");
            WriterUtils.writePackage(printer, def, usagePurpose, '/');
            printer.print('/');
            printer.print(def.getId());
            printer.println(".js\"};");
            printer.leaveBlock();
            printer.println('}');
            printer.leaveBlock();
            printer.println('}');
        }
        printer.leaveBlock();
        printer.println('}');
        return true;
    }

    @Override
    public boolean writeAddon(CodePrinter printer) {
        final String js = def.getJsCode();
        if (js != null && !js.isEmpty()) {
            printer.print(js);
        }
        return true;
    }

    private boolean writeConnections(CodePrinter printer) {

        List<AdsRwtUIDef> chain = new LinkedList<>();
        chain.add(def);
        Set<AdsRwtUIDef> processed = new HashSet<>();
        while (!chain.isEmpty()) {
            List<AdsDefinition> next = new LinkedList<>();
            for (AdsRwtUIDef current : chain) {
                if (processed.contains(current)) {
                    continue;
                }
                processed.add(current);
                for (AdsUIConnection c : current.getConnections()) {
                    final AdsUIItemDef sender = c.getSender();
                    if (sender == null) {
                        return false;
                    }
                    final AdsMethodDef slot = c.getSlot();
                    if (slot == null) {
                        return false;
                    }
                    final String iface = c.getInterfaceName();
                    if (iface == null || iface.isEmpty()) {
                        return false;
                    }
                    final String registrator = c.getRegistrator();
                    if (registrator == null || registrator.isEmpty()) {
                        return false;
                    }
                    printer.print("this.");
                    printer.print(sender.getId());
                    printer.print('.');
                    printer.print(registrator);
                    printer.print("(new ");
                    printer.print(iface);
                    printer.println("(){");
                    printer.enterBlock();
                    printer.print("public ");
                    final AdsTypeDeclaration decl = c.getSlotReturnType();
                    decl.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, decl, def);
                    printer.print(' ');
                    final String signalStr = c.getSignal();
                    if (signalStr == null || signalStr.isEmpty()) {
                        return false;
                    }

                    int counter = 0;
                    final int openParen = signalStr.indexOf('(');
                    if (openParen < 0) {
                        return false;
                    }
                    final int closeParen = signalStr.indexOf(')');
                    if (closeParen < 0) {
                        return false;
                    }
                    final String typeStr = signalStr.substring(openParen + 1, closeParen);
                    final String[] params = typeStr.split(",");
                    printer.print(signalStr.substring(0, openParen + 1));
                    for (final String param : params) {
                        final String p = param.trim();
                        if (p.isEmpty()) {
                            continue;
                        }
                        if (counter > 0) {
                            printer.printComma();
                            printer.printSpace();
                        }
                        final int spaceIndex = p.indexOf(' ');
                        final String typeName;
                        if (spaceIndex > 0) {
                            typeName = p.substring(0, spaceIndex);
                        } else {
                            typeName = p;
                        }
                        printer.print(typeName);
                        printer.print(" p");
                        printer.print(counter);
                        counter++;
                    }
                    printer.print(")");
                    printer.println("{");
                    printer.enterBlock();
                    final AdsClassDef slotOwnerClass = slot.getOwnerClass();
                    if (slotOwnerClass == null) {
                        return false;
                    }
                    if (c.getSlotReturnType() != AdsTypeDeclaration.VOID) {
                        printer.print(" return ");
                    }
                    printer.print("((");
                    slotOwnerClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
                    printer.print(")getModel()).");
                    printer.print(slot.getId());
                    printer.print('(');
                    for (int i = 0; i < counter; i++) {
                        if (i > 0) {
                            printer.printComma();
                        }
                        printer.print("p" + i);
                    }
                    printer.print(')');
                    printer.printlnSemicolon();
                    printer.leaveBlock();
                    printer.println('}');
                    printer.leaveBlock();
                    printer.print("})");
                    printer.printlnSemicolon();
                }
                current.getHierarchy().findOverwritten().save(next);
            }
            chain.clear();
            for (AdsDefinition def : next) {
                chain.add((AdsRwtUIDef) def);
            }
        }
        return true;
    }

    private boolean writeConstructor(CodePrinter printer) {
        printer.print("public ");
        printer.print(def.getId());
        printer.print("(org.radixware.kernel.common.client.IClientEnvironment env");
        switch (def.getDefinitionType()) {
            case CUSTOM_PROP_EDITOR:
            case CUSTOM_DIALOG:
            case CUSTOM_EDITOR:
            case CUSTOM_SELECTOR:
            case CUSTOM_PARAG_EDITOR:
            case CUSTOM_FORM_EDITOR:
            case CUSTOM_REPORT_EDITOR:
            case CUSTOM_WIDGET_DEF:
            case CUSTOM_FILTER_DIALOG:
                printer.println("){");
                break;
            case CUSTOM_PAGE_EDITOR:
                printer.printComma();
                printer.println("org.radixware.kernel.common.client.views.IView view");
                printer.printComma();
                printer.println("org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){");
                break;
            default:
                break;
        }
        printer.enterBlock();
        printer.print("super(env");
        switch (def.getDefinitionType()) {
            case CUSTOM_PROP_EDITOR:
            case CUSTOM_DIALOG:
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();

                final AdsUIProperty prop = def.getWidget().getProperties().getByName("windowTitle");
                Id titleId = null;
                if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
                    titleId = ((AdsUIProperty.LocalizedStringRefProperty) prop).getStringId();
                }

                WriterUtils.writeIdUsage(printer, titleId);
                printer.printComma();
                WriterUtils.writeIdUsage(printer, null);
                break;
            case CUSTOM_PAGE_EDITOR:
                printer.printComma();
                printer.print("view");
                printer.printComma();
                printer.print("page");
                break;
            default:
                break;
        }

        printer.leaveBlock();
        printer.print(')');
        printer.printlnSemicolon();
        printer.enterBlock();
        final boolean initialize = initializeInConstructor();
        if (initialize) {
            RwtWidgetWriter.getInstance(def.getWidget()).writeWidgetInitializations(printer);
            writeConnections(printer);
        }

        printer.leaveBlock();
        printer.println('}');
        if (!initialize) {
            printer.print("public void open(");
            printer.print(WriterUtils.EXPLORER_MODEL_CLASS_NAME);
            printer.enterBlock();
            printer.println(" model){");
            printer.println("super.open(model);");
            RwtWidgetWriter.getInstance(def.getWidget()).writeWidgetInitializations(printer);
            writeConnections(printer);
            printer.println("fireOpened();");
            printer.leaveBlock();
            printer.println("}");

        }

        if (def instanceof AdsRwtCustomDialogDef) {
            final AdsRwtCustomDialogDef dlg = (AdsRwtCustomDialogDef) def;
            final AdsModelClassDef clazz = dlg.getModelClass();
            final AdsType type = clazz.getType(EValType.USER_CLASS, null);
            printer.print("public ");
            type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
            printer.print(" getModel(){ return (");
            type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeUsage(printer);
            printer.println(" )super.getModel();}");
        }

        return true;
    }

    private boolean initializeInConstructor() {
        return def.getDefinitionType() != EDefType.CUSTOM_DIALOG
                && def.getDefinitionType() != EDefType.CUSTOM_WIDGET_DEF
                && def.getDefinitionType() != EDefType.CUSTOM_PARAG_EDITOR
                && def.getDefinitionType() != EDefType.CUSTOM_EDITOR
                && def.getDefinitionType() != EDefType.CUSTOM_SELECTOR
                && def.getDefinitionType() != EDefType.CUSTOM_PROP_EDITOR
                && def.getDefinitionType() != EDefType.CUSTOM_PAGE_EDITOR
                && def.getDefinitionType() != EDefType.CUSTOM_FILTER_DIALOG
                && !(def instanceof AbstractRwtCustomFormDialogDef);
    }
}
