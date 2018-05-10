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
package org.radixware.kernel.common.defs.ads.src.exploreritems;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

/**
 * public RadParagraphExplorerItemDef( final Id id, final Id ownerDefId, final
 * Id titleId, final RadExplorerItemDef[] children )
 *
 */
public class AdsParagraphWriter extends AdsExplorerItemWriter<AdsParagraphExplorerItemDef> {

    public AdsParagraphWriter(JavaSourceSupport support, AdsParagraphExplorerItemDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    static final char[] EXPLORER_ITEM_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadParagraphExplorerItemDef".toCharArray(), '.');
    public static final char[] EXPLORER_ITEM_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.EXPLORER_ITEMS_META_EXPLORER_PACKAGE_NAME, "RadParagraphDef".toCharArray(), '.');
    private static final char[] ROOT_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadExplorerRootDef".toCharArray(), '.');

    @Override
    protected void writeInnerSpecific(ERuntimeEnvironmentType env, CodePrinter printer) {
        WriterUtils.writeIdUsage(printer, def.getTitleId());
        printer.printComma();
    }

    @Override
    protected void writeFooterSpecific(ERuntimeEnvironmentType env, CodePrinter printer) {
        printer.printStringLiteral(def.getLayer().getURI());
        printer.printComma();
        writeCode(printer, def.getExplorerItems());

        if (env.isClientEnv()) {
            printer.printComma();
            WriterUtils.writeExplorerItemsOrderAndVisibility(printer, def.getExplorerItems());
            printer.printComma();
            WriterUtils.writeIdArrayUsage(printer, def.getUsedContextlessCommands().getCommandIds());
            printer.printComma();
            WriterUtils.writeIdUsage(printer, def.getIconId());
            printer.printComma();
            WriterUtils.writeIdUsage(printer, def.getLogoId());
            printer.printComma();
            printer.print(def.isRoot());
        }
        printer.printComma();
        printer.print(def.isHidden());

        if (def.getHierarchy().findOverwritten().get() != null) {
            if (def.isTopLevelDefinition()) {
                printer.print(",____$$$____bcl()");
            } else {
                if (env.isClientEnv()) {
                    printer.print(",____$$$____bcl(");
                    Id[] path = def.getIdPath();
                    WriterUtils.writeIdArrayUsage(printer, path);
                    printer.print(')');
                } else {
                    printer.print(",null");
                }
            }
        } else {
            printer.print(",null");
        }
        // if (env.isClientEnv()) {
        printer.printComma();
        if (def.isExplorerItemsInherited()) {
            WriterUtils.writeNull(printer);
        } else {
            List<Id> ids = def.getExplorerItems().getInheritedExplorerItemIds();
            Collections.sort(ids);
            WriterUtils.writeIdArrayUsage(printer, ids);
        }
        //}

    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                if (def.isTopLevelDefinition()) {
                    WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
                    printer.println();
                    WriterUtils.writeMetaShareabilityAnnotation(printer, def);
                    printer.println();
                    printer.print("public final class ");
                    //writeUsage(printer);
                    printer.print(JavaSourceSupport.getMetaName(def, printer instanceof IHumanReadablePrinter));
                    printer.enterBlock(1);
                    printer.println("{");
                    if (env == ERuntimeEnvironmentType.SERVER) {
                        WriterUtils.writeServerArteAccessMethodDeclaration(def, JavaSourceSupport.CodeType.META, printer);
                    }
                    printer.print("public static final ");
                    writeMetaClassName(env, printer);
                    printer.print(" rdxMeta = ");
                    //new AdsParagraphWriter(getSupport(), def, usagePurpose.getEnvironment() == ESystemComponent.SERVER ? UsagePurpose.SERVER_META : UsagePurpose.EXPLORER_META).writeCode(printer);
                    if (!writeMetaImpl(printer)) {
                        return false;
                    }
                    printer.printlnSemicolon();
                    if (def.getHierarchy().findOverwritten().get() != null) {
                        WriterUtils.writeLoadMetaClassFromBaseLayer(printer, def.getLayer().getURI(), def.getModule().getId(), env, def.getId(), getMetaClassName(env));
                        if (env.isClientEnv()) {
                            WriterUtils.writeLookForDefinitionInBaseLayer(printer, getMetaClassName(env));
                        }
                    }
                    printer.leaveBlock(1);
                    printer.print("}");
                    return true;
                } else {
                    return writeMetaImpl(printer);
                }
            default:
                return false;
        }
    }

    private boolean writeMetaImpl(CodePrinter printer) {
        if (super.writeMeta(printer)) {
            if (usagePurpose.getEnvironment().isClientEnv()) {
                printer.println("{");
                printer.enterBlock();
                printer.println("@Override");
                printer.print("protected ");
                printer.print(WriterUtils.EXPLORER_MODEL_CLASS_NAME);
                printer.print(" createModelImpl(");
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession) {");
                printer.enterBlock();
                printer.print("return new ");
                writeCode(printer, def.getModel().getType(EValType.USER_CLASS, null));
                printer.println("(userSession,this);");
                printer.leaveBlock();
                printer.println("}");
                printer.leaveBlock();
                printer.println("}");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void writeMetaClassName(ERuntimeEnvironmentType env, CodePrinter printer) {
        switch (env) {
            case SERVER:
                if (def.isRoot()) {
                    printer.print(ROOT_META_SERVER_CLASS_NAME);
                } else {
                    printer.print(EXPLORER_ITEM_META_SERVER_CLASS_NAME);
                }
                break;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                printer.print(EXPLORER_ITEM_META_EXPLORER_CLASS_NAME);
                break;
        }
    }

    private String getMetaClassName(ERuntimeEnvironmentType env) {
        switch (env) {
            case SERVER:
                if (def.isRoot()) {
                    return new String(ROOT_META_SERVER_CLASS_NAME);
                } else {
                    return new String(EXPLORER_ITEM_META_SERVER_CLASS_NAME);
                }
            default:
                return new String(EXPLORER_ITEM_META_EXPLORER_CLASS_NAME);

        }
    }
}
